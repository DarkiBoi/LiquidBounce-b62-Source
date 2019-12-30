// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class MiJump extends SpeedMode
{
    public MiJump() {
        super("MiJump");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (MiJump.mc.field_71439_g.field_70122_E && !MiJump.mc.field_71439_g.field_71158_b.field_78901_c) {
            final EntityPlayerSP field_71439_g = MiJump.mc.field_71439_g;
            field_71439_g.field_70181_x += 0.1;
            final double multiplier = 1.8;
            final EntityPlayerSP field_71439_g2 = MiJump.mc.field_71439_g;
            field_71439_g2.field_70159_w *= 1.8;
            final EntityPlayerSP field_71439_g3 = MiJump.mc.field_71439_g;
            field_71439_g3.field_70179_y *= 1.8;
            final double currentSpeed = Math.sqrt(Math.pow(MiJump.mc.field_71439_g.field_70159_w, 2.0) + Math.pow(MiJump.mc.field_71439_g.field_70179_y, 2.0));
            final double maxSpeed = 0.66;
            if (currentSpeed > 0.66) {
                MiJump.mc.field_71439_g.field_70159_w = MiJump.mc.field_71439_g.field_70159_w / currentSpeed * 0.66;
                MiJump.mc.field_71439_g.field_70179_y = MiJump.mc.field_71439_g.field_70179_y / currentSpeed * 0.66;
            }
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
