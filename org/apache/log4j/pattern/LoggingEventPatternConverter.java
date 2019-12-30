// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public abstract class LoggingEventPatternConverter extends PatternConverter
{
    protected LoggingEventPatternConverter(final String name, final String style) {
        super(name, style);
    }
    
    public abstract void format(final LoggingEvent p0, final StringBuffer p1);
    
    public void format(final Object obj, final StringBuffer output) {
        if (obj instanceof LoggingEvent) {
            this.format((LoggingEvent)obj, output);
        }
    }
    
    public boolean handlesThrowable() {
        return false;
    }
}
