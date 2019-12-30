// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public final class LiteralPatternConverter extends LoggingEventPatternConverter
{
    private final String literal;
    
    public LiteralPatternConverter(final String literal) {
        super("Literal", "literal");
        this.literal = literal;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append(this.literal);
    }
    
    public void format(final Object obj, final StringBuffer toAppendTo) {
        toAppendTo.append(this.literal);
    }
}
