// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.Event;

public class Render2DEvent extends Event
{
    private final float partialTicks;
    
    public Render2DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
