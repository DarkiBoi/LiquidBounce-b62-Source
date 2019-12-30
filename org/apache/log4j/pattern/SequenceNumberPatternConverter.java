// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public class SequenceNumberPatternConverter extends LoggingEventPatternConverter
{
    private static final SequenceNumberPatternConverter INSTANCE;
    
    private SequenceNumberPatternConverter() {
        super("Sequence Number", "sn");
    }
    
    public static SequenceNumberPatternConverter newInstance(final String[] options) {
        return SequenceNumberPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append("0");
    }
    
    static {
        INSTANCE = new SequenceNumberPatternConverter();
    }
}
