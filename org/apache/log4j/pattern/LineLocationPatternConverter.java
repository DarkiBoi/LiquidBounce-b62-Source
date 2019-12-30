// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class LineLocationPatternConverter extends LoggingEventPatternConverter
{
    private static final LineLocationPatternConverter INSTANCE;
    
    private LineLocationPatternConverter() {
        super("Line", "line");
    }
    
    public static LineLocationPatternConverter newInstance(final String[] options) {
        return LineLocationPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer output) {
        final LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            output.append(locationInfo.getLineNumber());
        }
    }
    
    static {
        INSTANCE = new LineLocationPatternConverter();
    }
}
