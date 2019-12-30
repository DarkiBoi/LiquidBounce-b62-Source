// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public final class LoggerPatternConverter extends NamePatternConverter
{
    private static final LoggerPatternConverter INSTANCE;
    
    private LoggerPatternConverter(final String[] options) {
        super("Logger", "logger", options);
    }
    
    public static LoggerPatternConverter newInstance(final String[] options) {
        if (options == null || options.length == 0) {
            return LoggerPatternConverter.INSTANCE;
        }
        return new LoggerPatternConverter(options);
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        final int initialLength = toAppendTo.length();
        toAppendTo.append(event.getLoggerName());
        this.abbreviate(initialLength, toAppendTo);
    }
    
    static {
        INSTANCE = new LoggerPatternConverter(null);
    }
}
