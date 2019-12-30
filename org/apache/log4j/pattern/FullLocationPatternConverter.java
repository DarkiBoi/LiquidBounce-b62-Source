// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class FullLocationPatternConverter extends LoggingEventPatternConverter
{
    private static final FullLocationPatternConverter INSTANCE;
    
    private FullLocationPatternConverter() {
        super("Full Location", "fullLocation");
    }
    
    public static FullLocationPatternConverter newInstance(final String[] options) {
        return FullLocationPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer output) {
        final LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            output.append(locationInfo.fullInfo);
        }
    }
    
    static {
        INSTANCE = new FullLocationPatternConverter();
    }
}
