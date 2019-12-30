// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class FileLocationPatternConverter extends LoggingEventPatternConverter
{
    private static final FileLocationPatternConverter INSTANCE;
    
    private FileLocationPatternConverter() {
        super("File Location", "file");
    }
    
    public static FileLocationPatternConverter newInstance(final String[] options) {
        return FileLocationPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer output) {
        final LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            output.append(locationInfo.getFileName());
        }
    }
    
    static {
        INSTANCE = new FileLocationPatternConverter();
    }
}
