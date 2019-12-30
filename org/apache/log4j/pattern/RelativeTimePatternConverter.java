// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public class RelativeTimePatternConverter extends LoggingEventPatternConverter
{
    private CachedTimestamp lastTimestamp;
    
    public RelativeTimePatternConverter() {
        super("Time", "time");
        this.lastTimestamp = new CachedTimestamp(0L, "");
    }
    
    public static RelativeTimePatternConverter newInstance(final String[] options) {
        return new RelativeTimePatternConverter();
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        final long timestamp = event.timeStamp;
        if (!this.lastTimestamp.format(timestamp, toAppendTo)) {
            final String formatted = Long.toString(timestamp - LoggingEvent.getStartTime());
            toAppendTo.append(formatted);
            this.lastTimestamp = new CachedTimestamp(timestamp, formatted);
        }
    }
    
    private static final class CachedTimestamp
    {
        private final long timestamp;
        private final String formatted;
        
        public CachedTimestamp(final long timestamp, final String formatted) {
            this.timestamp = timestamp;
            this.formatted = formatted;
        }
        
        public boolean format(final long newTimestamp, final StringBuffer toAppendTo) {
            if (newTimestamp == this.timestamp) {
                toAppendTo.append(this.formatted);
                return true;
            }
            return false;
        }
    }
}
