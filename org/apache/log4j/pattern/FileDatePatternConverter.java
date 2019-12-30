// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

public final class FileDatePatternConverter
{
    private FileDatePatternConverter() {
    }
    
    public static PatternConverter newInstance(final String[] options) {
        if (options == null || options.length == 0) {
            return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" });
        }
        return DatePatternConverter.newInstance(options);
    }
}
