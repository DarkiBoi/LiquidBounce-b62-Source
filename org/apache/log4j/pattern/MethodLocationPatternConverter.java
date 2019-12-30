// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class MethodLocationPatternConverter extends LoggingEventPatternConverter
{
    private static final MethodLocationPatternConverter INSTANCE;
    
    private MethodLocationPatternConverter() {
        super("Method", "method");
    }
    
    public static MethodLocationPatternConverter newInstance(final String[] options) {
        return MethodLocationPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        final LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            toAppendTo.append(locationInfo.getMethodName());
        }
    }
    
    static {
        INSTANCE = new MethodLocationPatternConverter();
    }
}
