// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;

public class SimpleLayout extends Layout
{
    StringBuffer sbuf;
    
    public SimpleLayout() {
        this.sbuf = new StringBuffer(128);
    }
    
    public void activateOptions() {
    }
    
    public String format(final LoggingEvent event) {
        this.sbuf.setLength(0);
        this.sbuf.append(event.getLevel().toString());
        this.sbuf.append(" - ");
        this.sbuf.append(event.getRenderedMessage());
        this.sbuf.append(SimpleLayout.LINE_SEP);
        return this.sbuf.toString();
    }
    
    public boolean ignoresThrowable() {
        return true;
    }
}
