// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public class ThreadPatternConverter extends LoggingEventPatternConverter
{
    private static final ThreadPatternConverter INSTANCE;
    
    private ThreadPatternConverter() {
        super("Thread", "thread");
    }
    
    public static ThreadPatternConverter newInstance(final String[] options) {
        return ThreadPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append(event.getThreadName());
    }
    
    static {
        INSTANCE = new ThreadPatternConverter();
    }
}
