// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.ThrowableInformation;
import org.apache.log4j.spi.LoggingEvent;

public class ThrowableInformationPatternConverter extends LoggingEventPatternConverter
{
    private int maxLines;
    
    private ThrowableInformationPatternConverter(final String[] options) {
        super("Throwable", "throwable");
        this.maxLines = Integer.MAX_VALUE;
        if (options != null && options.length > 0) {
            if ("none".equals(options[0])) {
                this.maxLines = 0;
            }
            else if ("short".equals(options[0])) {
                this.maxLines = 1;
            }
            else {
                try {
                    this.maxLines = Integer.parseInt(options[0]);
                }
                catch (NumberFormatException ex) {}
            }
        }
    }
    
    public static ThrowableInformationPatternConverter newInstance(final String[] options) {
        return new ThrowableInformationPatternConverter(options);
    }
    
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        if (this.maxLines != 0) {
            final ThrowableInformation information = event.getThrowableInformation();
            if (information != null) {
                final String[] stringRep = information.getThrowableStrRep();
                int length = stringRep.length;
                if (this.maxLines < 0) {
                    length += this.maxLines;
                }
                else if (length > this.maxLines) {
                    length = this.maxLines;
                }
                for (final String string : stringRep) {
                    toAppendTo.append(string).append("\n");
                }
            }
        }
    }
    
    public boolean handlesThrowable() {
        return true;
    }
}
