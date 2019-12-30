// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public class MoveEvent extends CancellableEvent
{
    public double x;
    public double y;
    public double z;
    public boolean safeWalk;
    
    public MoveEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setSafeWalk(final boolean safeWalk) {
        this.safeWalk = safeWalk;
    }
    
    public boolean isSafeWalk() {
        return this.safeWalk;
    }
}
