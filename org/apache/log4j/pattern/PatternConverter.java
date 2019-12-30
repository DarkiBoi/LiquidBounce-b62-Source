// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

public abstract class PatternConverter
{
    private final String name;
    private final String style;
    
    protected PatternConverter(final String name, final String style) {
        this.name = name;
        this.style = style;
    }
    
    public abstract void format(final Object p0, final StringBuffer p1);
    
    public final String getName() {
        return this.name;
    }
    
    public String getStyleClass(final Object e) {
        return this.style;
    }
}
