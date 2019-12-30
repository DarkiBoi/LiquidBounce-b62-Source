// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Layout;

public final class LineSeparatorPatternConverter extends LoggingEventPatternConverter
{
    private static final LineSeparatorPatternConverter INSTANCE;
    private final String lineSep;
    
    private LineSeparatorPatternConverter() {
        super("Line Sep", "lineSep");
        this.lineSep = Layout.LINE_SEP;
    }
    
    public static LineSeparatorPatternConverter newInstance(final String[] options) {
        return LineSeparatorPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append(this.lineSep);
    }
    
    public void format(final Object obj, final StringBuffer toAppendTo) {
        toAppendTo.append(this.lineSep);
    }
    
    static {
        INSTANCE = new LineSeparatorPatternConverter();
    }
}
