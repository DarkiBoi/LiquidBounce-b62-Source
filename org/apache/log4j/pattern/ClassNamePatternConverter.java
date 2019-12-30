// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class ClassNamePatternConverter extends NamePatternConverter
{
    private ClassNamePatternConverter(final String[] options) {
        super("Class Name", "class name", options);
    }
    
    public static ClassNamePatternConverter newInstance(final String[] options) {
        return new ClassNamePatternConverter(options);
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        final int initialLength = toAppendTo.length();
        final LocationInfo li = event.getLocationInformation();
        if (li == null) {
            toAppendTo.append("?");
        }
        else {
            toAppendTo.append(li.getClassName());
        }
        this.abbreviate(initialLength, toAppendTo);
    }
}
