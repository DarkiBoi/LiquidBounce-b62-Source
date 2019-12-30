// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event;

public class CancellableEvent extends Event
{
    private boolean cancel;
    
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
}
