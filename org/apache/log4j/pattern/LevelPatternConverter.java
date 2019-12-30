// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public final class LevelPatternConverter extends LoggingEventPatternConverter
{
    private static final int TRACE_INT = 5000;
    private static final LevelPatternConverter INSTANCE;
    
    private LevelPatternConverter() {
        super("Level", "level");
    }
    
    public static LevelPatternConverter newInstance(final String[] options) {
        return LevelPatternConverter.INSTANCE;
    }
    
    public void format(final LoggingEvent event, final StringBuffer output) {
        output.append(event.getLevel().toString());
    }
    
    public String getStyleClass(final Object e) {
        if (!(e instanceof LoggingEvent)) {
            return "level";
        }
        final int lint = ((LoggingEvent)e).getLevel().toInt();
        switch (lint) {
            case 5000: {
                return "level trace";
            }
            case 10000: {
                return "level debug";
            }
            case 20000: {
                return "level info";
            }
            case 30000: {
                return "level warn";
            }
            case 40000: {
                return "level error";
            }
            case 50000: {
                return "level fatal";
            }
            default: {
                return "level " + ((LoggingEvent)e).getLevel().toString();
            }
        }
    }
    
    static {
        INSTANCE = new LevelPatternConverter();
    }
}
