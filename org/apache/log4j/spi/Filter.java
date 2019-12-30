// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

public abstract class Filter implements OptionHandler
{
    public Filter next;
    public static final int DENY = -1;
    public static final int NEUTRAL = 0;
    public static final int ACCEPT = 1;
    
    public void activateOptions() {
    }
    
    public abstract int decide(final LoggingEvent p0);
    
    public void setNext(final Filter next) {
        this.next = next;
    }
    
    public Filter getNext() {
        return this.next;
    }
}
