// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public final class MessagePatternConverter extends LoggingEventPatternConverter
{
    private static final MessagePatternConverter INSTANCE;
    
    private MessagePatternConverter() {
        super("Message", "message");
    }
    
    public static MessagePatternConverter newInstance(final String[] options) {
        return MessagePatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append(event.getRenderedMessage());
    }
    
    static {
        INSTANCE = new MessagePatternConverter();
    }
}
