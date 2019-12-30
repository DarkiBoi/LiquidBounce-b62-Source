// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public class JumpEvent extends CancellableEvent
{
    private float motion;
    
    public JumpEvent(final float motion) {
        this.motion = motion;
    }
    
    public void setMotion(final float motion) {
        this.motion = motion;
    }
    
    public float getMotion() {
        return this.motion;
    }
}
