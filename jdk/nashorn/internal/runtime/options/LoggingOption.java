// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.options;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.Locale;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LoggingOption extends KeyValueOption
{
    private final Map<String, LoggerInfo> loggers;
    
    LoggingOption(final String value) {
        super(value);
        this.loggers = new HashMap<String, LoggerInfo>();
        this.initialize(this.getValues());
    }
    
    public Map<String, LoggerInfo> getLoggers() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends LoggerInfo>)this.loggers);
    }
    
    private void initialize(final Map<String, String> logMap) throws IllegalArgumentException {
        try {
            for (final Map.Entry<String, String> entry : logMap.entrySet()) {
                final String name = lastPart(entry.getKey());
                final String levelString = entry.getValue().toUpperCase(Locale.ENGLISH);
                Level level;
                boolean isQuiet;
                if ("".equals(levelString)) {
                    level = Level.INFO;
                    isQuiet = false;
                }
                else if ("QUIET".equals(levelString)) {
                    level = Level.INFO;
                    isQuiet = true;
                }
                else {
                    level = Level.parse(levelString);
                    isQuiet = false;
                }
                this.loggers.put(name, new LoggerInfo(level, isQuiet));
            }
        }
        catch (IllegalArgumentException | SecurityException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw e;
        }
    }
    
    private static String lastPart(final String packageName) {
        final String[] parts = packageName.split("\\.");
        if (parts.length == 0) {
            return packageName;
        }
        return parts[parts.length - 1];
    }
    
    public static class LoggerInfo
    {
        private final Level level;
        private final boolean isQuiet;
        
        LoggerInfo(final Level level, final boolean isQuiet) {
            this.level = level;
            this.isQuiet = isQuiet;
        }
        
        public Level getLevel() {
            return this.level;
        }
        
        public boolean isQuiet() {
            return this.isQuiet;
        }
    }
}
