// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.Event;

public class RenderEntityEvent extends Event
{
    private final Entity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float entityYaw;
    private final float partialTicks;
    
    public RenderEntityEvent(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }
    
    public Entity getEntity() {
        return this.entity;
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
    
    public float getEntityYaw() {
        return this.entityYaw;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
