// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.runtime.Debug;
import java.util.Set;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import jdk.nashorn.internal.runtime.options.Options;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.io.PrintWriter;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.Context;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.concurrent.atomic.LongAdder;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.ChainedCallSite;

public class LinkerCallSite extends ChainedCallSite
{
    public static final int ARGLIMIT = 250;
    private static final String PROFILEFILE;
    private static final MethodHandle INCREASE_MISS_COUNTER;
    private static final MethodHandle ON_CATCH_INVALIDATION;
    private int catchInvalidations;
    private static LongAdder count;
    private static final HashMap<String, AtomicInteger> missCounts;
    private static LongAdder missCount;
    private static final Random r;
    private static final int missSamplingPercentage;
    
    LinkerCallSite(final NashornCallSiteDescriptor descriptor) {
        super(descriptor);
        if (Context.DEBUG) {
            LinkerCallSite.count.increment();
        }
    }
    
    @Override
    protected MethodHandle getPruneCatches() {
        return Lookup.MH.filterArguments(super.getPruneCatches(), 0, LinkerCallSite.ON_CATCH_INVALIDATION);
    }
    
    private static ChainedCallSite onCatchInvalidation(final LinkerCallSite callSite) {
        ++callSite.catchInvalidations;
        return callSite;
    }
    
    public static int getCatchInvalidationCount(final Object callSiteToken) {
        if (callSiteToken instanceof LinkerCallSite) {
            return ((LinkerCallSite)callSiteToken).catchInvalidations;
        }
        return 0;
    }
    
    static LinkerCallSite newLinkerCallSite(final MethodHandles.Lookup lookup, final String name, final MethodType type, final int flags) {
        final NashornCallSiteDescriptor desc = NashornCallSiteDescriptor.get(lookup, name, type, flags);
        if (desc.isProfile()) {
            return ProfilingLinkerCallSite.newProfilingLinkerCallSite(desc);
        }
        if (desc.isTrace()) {
            return new TracingLinkerCallSite(desc);
        }
        return new LinkerCallSite(desc);
    }
    
    @Override
    public String toString() {
        return this.getDescriptor().toString();
    }
    
    public NashornCallSiteDescriptor getNashornDescriptor() {
        return (NashornCallSiteDescriptor)this.getDescriptor();
    }
    
    @Override
    public void relink(final GuardedInvocation invocation, final MethodHandle relink) {
        super.relink(invocation, this.getDebuggingRelink(relink));
    }
    
    @Override
    public void resetAndRelink(final GuardedInvocation invocation, final MethodHandle relink) {
        super.resetAndRelink(invocation, this.getDebuggingRelink(relink));
    }
    
    private MethodHandle getDebuggingRelink(final MethodHandle relink) {
        if (Context.DEBUG) {
            return Lookup.MH.filterArguments(relink, 0, this.getIncreaseMissCounter(relink.type().parameterType(0)));
        }
        return relink;
    }
    
    private MethodHandle getIncreaseMissCounter(final Class<?> type) {
        final MethodHandle missCounterWithDesc = Lookup.MH.bindTo(LinkerCallSite.INCREASE_MISS_COUNTER, this.getDescriptor().getName() + " @ " + getScriptLocation());
        if (type == Object.class) {
            return missCounterWithDesc;
        }
        return Lookup.MH.asType(missCounterWithDesc, missCounterWithDesc.type().changeParameterType(0, type).changeReturnType(type));
    }
    
    private static String getScriptLocation() {
        final StackTraceElement caller = DynamicLinker.getLinkedCallSiteLocation();
        return (caller == null) ? "unknown location" : (caller.getFileName() + ":" + caller.getLineNumber());
    }
    
    public static Object increaseMissCount(final String desc, final Object self) {
        LinkerCallSite.missCount.increment();
        if (LinkerCallSite.r.nextInt(100) < LinkerCallSite.missSamplingPercentage) {
            final AtomicInteger i = LinkerCallSite.missCounts.get(desc);
            if (i == null) {
                LinkerCallSite.missCounts.put(desc, new AtomicInteger(1));
            }
            else {
                i.incrementAndGet();
            }
        }
        return self;
    }
    
    @Override
    protected int getMaxChainLength() {
        return 8;
    }
    
    public static long getCount() {
        return LinkerCallSite.count.longValue();
    }
    
    public static long getMissCount() {
        return LinkerCallSite.missCount.longValue();
    }
    
    public static int getMissSamplingPercentage() {
        return LinkerCallSite.missSamplingPercentage;
    }
    
    public static void getMissCounts(final PrintWriter out) {
        final ArrayList<Map.Entry<String, AtomicInteger>> entries = new ArrayList<Map.Entry<String, AtomicInteger>>(LinkerCallSite.missCounts.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, AtomicInteger>>() {
            @Override
            public int compare(final Map.Entry<String, AtomicInteger> o1, final Map.Entry<String, AtomicInteger> o2) {
                return o2.getValue().get() - o1.getValue().get();
            }
        });
        for (final Map.Entry<String, AtomicInteger> entry : entries) {
            out.println("  " + entry.getKey() + "\t" + entry.getValue().get());
        }
    }
    
    static {
        PROFILEFILE = Options.getStringProperty("nashorn.profilefile", "NashornProfile.txt");
        INCREASE_MISS_COUNTER = Lookup.MH.findStatic(MethodHandles.lookup(), LinkerCallSite.class, "increaseMissCount", Lookup.MH.type(Object.class, String.class, Object.class));
        ON_CATCH_INVALIDATION = Lookup.MH.findStatic(MethodHandles.lookup(), LinkerCallSite.class, "onCatchInvalidation", Lookup.MH.type(ChainedCallSite.class, LinkerCallSite.class));
        missCounts = new HashMap<String, AtomicInteger>();
        r = new Random();
        missSamplingPercentage = Options.getIntProperty("nashorn.tcs.miss.samplePercent", 1);
        if (Context.DEBUG) {
            LinkerCallSite.count = new LongAdder();
            LinkerCallSite.missCount = new LongAdder();
        }
    }
    
    private static class ProfilingLinkerCallSite extends LinkerCallSite
    {
        private static LinkedList<ProfilingLinkerCallSite> profileCallSites;
        private long startTime;
        private int depth;
        private long totalTime;
        private long hitCount;
        private static final MethodHandles.Lookup LOOKUP;
        private static final MethodHandle PROFILEENTRY;
        private static final MethodHandle PROFILEEXIT;
        private static final MethodHandle PROFILEVOIDEXIT;
        
        ProfilingLinkerCallSite(final NashornCallSiteDescriptor desc) {
            super(desc);
        }
        
        public static ProfilingLinkerCallSite newProfilingLinkerCallSite(final NashornCallSiteDescriptor desc) {
            if (ProfilingLinkerCallSite.profileCallSites == null) {
                ProfilingLinkerCallSite.profileCallSites = new LinkedList<ProfilingLinkerCallSite>();
                final Thread profileDumperThread = new Thread(new ProfileDumper());
                Runtime.getRuntime().addShutdownHook(profileDumperThread);
            }
            final ProfilingLinkerCallSite callSite = new ProfilingLinkerCallSite(desc);
            ProfilingLinkerCallSite.profileCallSites.add(callSite);
            return callSite;
        }
        
        @Override
        public void setTarget(final MethodHandle newTarget) {
            final MethodType type = this.type();
            final boolean isVoid = type.returnType() == Void.TYPE;
            final Class<?> newSelfType = newTarget.type().parameterType(0);
            MethodHandle selfFilter = Lookup.MH.bindTo(ProfilingLinkerCallSite.PROFILEENTRY, this);
            if (newSelfType != Object.class) {
                final MethodType selfFilterType = MethodType.methodType(newSelfType, newSelfType);
                selfFilter = selfFilter.asType(selfFilterType);
            }
            MethodHandle methodHandle = Lookup.MH.filterArguments(newTarget, 0, selfFilter);
            if (isVoid) {
                methodHandle = Lookup.MH.filterReturnValue(methodHandle, Lookup.MH.bindTo(ProfilingLinkerCallSite.PROFILEVOIDEXIT, this));
            }
            else {
                final MethodType filter = Lookup.MH.type(type.returnType(), type.returnType());
                methodHandle = Lookup.MH.filterReturnValue(methodHandle, Lookup.MH.asType(Lookup.MH.bindTo(ProfilingLinkerCallSite.PROFILEEXIT, this), filter));
            }
            super.setTarget(methodHandle);
        }
        
        public Object profileEntry(final Object self) {
            if (this.depth == 0) {
                this.startTime = System.nanoTime();
            }
            ++this.depth;
            ++this.hitCount;
            return self;
        }
        
        public Object profileExit(final Object result) {
            --this.depth;
            if (this.depth == 0) {
                this.totalTime += System.nanoTime() - this.startTime;
            }
            return result;
        }
        
        public void profileVoidExit() {
            --this.depth;
            if (this.depth == 0) {
                this.totalTime += System.nanoTime() - this.startTime;
            }
        }
        
        static {
            ProfilingLinkerCallSite.profileCallSites = null;
            LOOKUP = MethodHandles.lookup();
            PROFILEENTRY = Lookup.MH.findVirtual(ProfilingLinkerCallSite.LOOKUP, ProfilingLinkerCallSite.class, "profileEntry", Lookup.MH.type(Object.class, Object.class));
            PROFILEEXIT = Lookup.MH.findVirtual(ProfilingLinkerCallSite.LOOKUP, ProfilingLinkerCallSite.class, "profileExit", Lookup.MH.type(Object.class, Object.class));
            PROFILEVOIDEXIT = Lookup.MH.findVirtual(ProfilingLinkerCallSite.LOOKUP, ProfilingLinkerCallSite.class, "profileVoidExit", Lookup.MH.type(Void.TYPE, (Class<?>[])new Class[0]));
        }
        
        static class ProfileDumper implements Runnable
        {
            @Override
            public void run() {
                PrintWriter out = null;
                boolean fileOutput = false;
                try {
                    try {
                        out = new PrintWriter(new FileOutputStream(LinkerCallSite.PROFILEFILE));
                        fileOutput = true;
                    }
                    catch (FileNotFoundException e) {
                        out = Context.getCurrentErr();
                    }
                    dump(out);
                }
                finally {
                    if (out != null && fileOutput) {
                        out.close();
                    }
                }
            }
            
            private static void dump(final PrintWriter out) {
                int index = 0;
                for (final ProfilingLinkerCallSite callSite : ProfilingLinkerCallSite.profileCallSites) {
                    out.println("" + index++ + '\t' + callSite.getDescriptor().getName() + '\t' + callSite.totalTime + '\t' + callSite.hitCount);
                }
            }
        }
    }
    
    private static class TracingLinkerCallSite extends LinkerCallSite
    {
        private static final MethodHandles.Lookup LOOKUP;
        private static final MethodHandle TRACEOBJECT;
        private static final MethodHandle TRACEVOID;
        private static final MethodHandle TRACEMISS;
        
        TracingLinkerCallSite(final NashornCallSiteDescriptor desc) {
            super(desc);
        }
        
        @Override
        public void setTarget(final MethodHandle newTarget) {
            if (!this.getNashornDescriptor().isTraceEnterExit()) {
                super.setTarget(newTarget);
                return;
            }
            final MethodType type = this.type();
            final boolean isVoid = type.returnType() == Void.TYPE;
            MethodHandle traceMethodHandle = isVoid ? TracingLinkerCallSite.TRACEVOID : TracingLinkerCallSite.TRACEOBJECT;
            traceMethodHandle = Lookup.MH.bindTo(traceMethodHandle, this);
            traceMethodHandle = Lookup.MH.bindTo(traceMethodHandle, newTarget);
            traceMethodHandle = Lookup.MH.asCollector(traceMethodHandle, Object[].class, type.parameterCount());
            traceMethodHandle = Lookup.MH.asType(traceMethodHandle, type);
            super.setTarget(traceMethodHandle);
        }
        
        @Override
        public void initialize(final MethodHandle relinkAndInvoke) {
            super.initialize(this.getFallbackLoggingRelink(relinkAndInvoke));
        }
        
        @Override
        public void relink(final GuardedInvocation invocation, final MethodHandle relink) {
            super.relink(invocation, this.getFallbackLoggingRelink(relink));
        }
        
        @Override
        public void resetAndRelink(final GuardedInvocation invocation, final MethodHandle relink) {
            super.resetAndRelink(invocation, this.getFallbackLoggingRelink(relink));
        }
        
        private MethodHandle getFallbackLoggingRelink(final MethodHandle relink) {
            if (!this.getNashornDescriptor().isTraceMisses()) {
                return relink;
            }
            final MethodType type = relink.type();
            return Lookup.MH.foldArguments(relink, Lookup.MH.asType(Lookup.MH.asCollector(Lookup.MH.insertArguments(TracingLinkerCallSite.TRACEMISS, 0, this, "MISS " + getScriptLocation() + " "), Object[].class, type.parameterCount()), type.changeReturnType((Class<?>)Void.TYPE)));
        }
        
        private void printObject(final PrintWriter out, final Object arg) {
            if (!this.getNashornDescriptor().isTraceObjects()) {
                out.print((Object)((arg instanceof ScriptObject) ? "ScriptObject" : arg));
                return;
            }
            if (arg instanceof ScriptObject) {
                final ScriptObject object = (ScriptObject)arg;
                boolean isFirst = true;
                final Set<Object> keySet = object.keySet();
                if (keySet.isEmpty()) {
                    out.print(ScriptRuntime.safeToString(arg));
                }
                else {
                    out.print("{ ");
                    for (final Object key : keySet) {
                        if (!isFirst) {
                            out.print(", ");
                        }
                        out.print(key);
                        out.print(":");
                        final Object value = object.get(key);
                        if (value instanceof ScriptObject) {
                            out.print("...");
                        }
                        else {
                            this.printObject(out, value);
                        }
                        isFirst = false;
                    }
                    out.print(" }");
                }
            }
            else {
                out.print(ScriptRuntime.safeToString(arg));
            }
        }
        
        private void tracePrint(final PrintWriter out, final String tag, final Object[] args, final Object result) {
            out.print(Debug.id(this) + " TAG " + tag);
            out.print(this.getDescriptor().getName() + "(");
            if (args.length > 0) {
                this.printObject(out, args[0]);
                for (int i = 1; i < args.length; ++i) {
                    final Object arg = args[i];
                    out.print(", ");
                    if (!(arg instanceof ScriptObject) || !((ScriptObject)arg).isScope()) {
                        this.printObject(out, arg);
                    }
                    else {
                        out.print("SCOPE");
                    }
                }
            }
            out.print(")");
            if (tag.equals("EXIT  ")) {
                out.print(" --> ");
                this.printObject(out, result);
            }
            out.println();
        }
        
        public Object traceObject(final MethodHandle mh, final Object... args) throws Throwable {
            final PrintWriter out = Context.getCurrentErr();
            this.tracePrint(out, "ENTER ", args, null);
            final Object result = mh.invokeWithArguments(args);
            this.tracePrint(out, "EXIT  ", args, result);
            return result;
        }
        
        public void traceVoid(final MethodHandle mh, final Object... args) throws Throwable {
            final PrintWriter out = Context.getCurrentErr();
            this.tracePrint(out, "ENTER ", args, null);
            mh.invokeWithArguments(args);
            this.tracePrint(out, "EXIT  ", args, null);
        }
        
        public void traceMiss(final String desc, final Object... args) throws Throwable {
            this.tracePrint(Context.getCurrentErr(), desc, args, null);
        }
        
        static {
            LOOKUP = MethodHandles.lookup();
            TRACEOBJECT = Lookup.MH.findVirtual(TracingLinkerCallSite.LOOKUP, TracingLinkerCallSite.class, "traceObject", Lookup.MH.type(Object.class, MethodHandle.class, Object[].class));
            TRACEVOID = Lookup.MH.findVirtual(TracingLinkerCallSite.LOOKUP, TracingLinkerCallSite.class, "traceVoid", Lookup.MH.type(Void.TYPE, MethodHandle.class, Object[].class));
            TRACEMISS = Lookup.MH.findVirtual(TracingLinkerCallSite.LOOKUP, TracingLinkerCallSite.class, "traceMiss", Lookup.MH.type(Void.TYPE, String.class, Object[].class));
        }
    }
}
