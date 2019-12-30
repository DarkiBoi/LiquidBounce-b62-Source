// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

public abstract class NamePatternConverter extends LoggingEventPatternConverter
{
    private final NameAbbreviator abbreviator;
    
    protected NamePatternConverter(final String name, final String style, final String[] options) {
        super(name, style);
        if (options != null && options.length > 0) {
            this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
        }
        else {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        }
    }
    
    protected final void abbreviate(final int nameStart, final StringBuffer buf) {
        this.abbreviator.abbreviate(nameStart, buf);
    }
}
