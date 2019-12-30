// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Iterator;
import jdk.nashorn.internal.codegen.CompileUnit;
import java.util.List;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "time")
public final class Timing implements Loggable
{
    private DebugLogger log;
    private TimeSupplier timeSupplier;
    private final boolean isEnabled;
    private final long startTime;
    private static final String LOGGER_NAME;
    
    public Timing(final boolean isEnabled) {
        this.isEnabled = isEnabled;
        this.startTime = System.nanoTime();
    }
    
    public String getLogInfo() {
        assert this.isEnabled();
        return this.timeSupplier.get();
    }
    
    public String[] getLogInfoLines() {
        assert this.isEnabled();
        return this.timeSupplier.getStrings();
    }
    
    boolean isEnabled() {
        return this.isEnabled;
    }
    
    public void accumulateTime(final String module, final long durationNano) {
        if (this.isEnabled()) {
            this.ensureInitialized(Context.getContextTrusted());
            this.timeSupplier.accumulateTime(module, durationNano);
        }
    }
    
    private DebugLogger ensureInitialized(final Context context) {
        if (this.isEnabled() && this.log == null) {
            this.log = this.initLogger(context);
            if (this.log.isEnabled()) {
                this.timeSupplier = new TimeSupplier();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        final StringBuilder sb = new StringBuilder();
                        for (final String str : Timing.this.timeSupplier.getStrings()) {
                            sb.append('[').append(Timing.getLoggerName()).append("] ").append(str).append('\n');
                        }
                        System.err.print(sb);
                    }
                });
            }
        }
        return this.log;
    }
    
    static String getLoggerName() {
        return Timing.LOGGER_NAME;
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    public static String toMillisPrint(final long durationNano) {
        return Long.toString(TimeUnit.NANOSECONDS.toMillis(durationNano));
    }
    
    static {
        LOGGER_NAME = Timing.class.getAnnotation(Logger.class).name();
    }
    
    final class TimeSupplier implements Supplier<String>
    {
        private final Map<String, LongAdder> timings;
        private final LinkedBlockingQueue<String> orderedTimingNames;
        private final Function<String, LongAdder> newTimingCreator;
        
        TimeSupplier() {
            this.timings = new ConcurrentHashMap<String, LongAdder>();
            this.orderedTimingNames = new LinkedBlockingQueue<String>();
            this.newTimingCreator = new Function<String, LongAdder>() {
                @Override
                public LongAdder apply(final String s) {
                    TimeSupplier.this.orderedTimingNames.add(s);
                    return new LongAdder();
                }
            };
        }
        
        String[] getStrings() {
            final List<String> strs = new ArrayList<String>();
            final BufferedReader br = new BufferedReader(new StringReader(this.get()));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    strs.add(line);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return strs.toArray(new String[strs.size()]);
        }
        
        @Override
        public String get() {
            final long t = System.nanoTime();
            long knownTime = 0L;
            int maxKeyLength = 0;
            int maxValueLength = 0;
            for (final Map.Entry<String, LongAdder> entry : this.timings.entrySet()) {
                maxKeyLength = Math.max(maxKeyLength, entry.getKey().length());
                maxValueLength = Math.max(maxValueLength, Timing.toMillisPrint(entry.getValue().longValue()).length());
            }
            ++maxKeyLength;
            final StringBuilder sb = new StringBuilder();
            sb.append("Accumulated compilation phase timings:\n\n");
            for (final String timingName : this.orderedTimingNames) {
                int len = sb.length();
                sb.append(timingName);
                len = sb.length() - len;
                while (len++ < maxKeyLength) {
                    sb.append(' ');
                }
                final long duration = this.timings.get(timingName).longValue();
                final String strDuration = Timing.toMillisPrint(duration);
                len = strDuration.length();
                for (int i = 0; i < maxValueLength - len; ++i) {
                    sb.append(' ');
                }
                sb.append(strDuration).append(" ms\n");
                knownTime += duration;
            }
            final long total = t - Timing.this.startTime;
            sb.append('\n');
            sb.append("Total runtime: ").append(Timing.toMillisPrint(total)).append(" ms (Non-runtime: ").append(Timing.toMillisPrint(knownTime)).append(" ms [").append((int)(knownTime * 100.0 / total)).append("%])");
            sb.append("\n\nEmitted compile units: ").append(CompileUnit.getEmittedUnitCount());
            return sb.toString();
        }
        
        private void accumulateTime(final String module, final long duration) {
            this.timings.computeIfAbsent(module, this.newTimingCreator).add(duration);
        }
    }
}
