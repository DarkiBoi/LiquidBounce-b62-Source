// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.Event;

public class AttackEvent extends Event
{
    private final Entity targetEntity;
    
    public AttackEvent(final Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
