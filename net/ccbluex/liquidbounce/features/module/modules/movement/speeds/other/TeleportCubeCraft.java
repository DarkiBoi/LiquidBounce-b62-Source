// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class TeleportCubeCraft extends SpeedMode
{
    private final MSTimer timer;
    
    public TeleportCubeCraft() {
        super("TeleportCubeCraft");
        this.timer = new MSTimer();
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        if (MovementUtils.isMoving() && TeleportCubeCraft.mc.field_71439_g.field_70122_E && this.timer.hasTimePassed(300L)) {
            final double yaw = MovementUtils.getDirection();
            final float length = ((Speed)ModuleManager.getModule(Speed.class)).cubecraftPortLengthValue.asFloat();
            event.setX(-Math.sin(yaw) * length);
            event.setZ(Math.cos(yaw) * length);
            this.timer.reset();
        }
    }
}
