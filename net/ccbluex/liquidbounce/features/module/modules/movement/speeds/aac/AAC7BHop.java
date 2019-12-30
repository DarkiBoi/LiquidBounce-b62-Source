// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC7BHop extends SpeedMode
{
    public AAC7BHop() {
        super("AAC7BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving() || AAC7BHop.mc.field_71439_g.field_70154_o != null || AAC7BHop.mc.field_71439_g.field_70737_aN > 0) {
            return;
        }
        if (AAC7BHop.mc.field_71439_g.field_70122_E) {
            AAC7BHop.mc.field_71439_g.func_70664_aZ();
            AAC7BHop.mc.field_71439_g.field_70181_x = 0.405;
            final EntityPlayerSP field_71439_g = AAC7BHop.mc.field_71439_g;
            field_71439_g.field_70159_w *= 1.004;
            final EntityPlayerSP field_71439_g2 = AAC7BHop.mc.field_71439_g;
            field_71439_g2.field_70179_y *= 1.004;
            return;
        }
        final double speed = MovementUtils.getSpeed() * 1.0072;
        final double yaw = Math.toRadians(AAC7BHop.mc.field_71439_g.field_70177_z);
        AAC7BHop.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * speed;
        AAC7BHop.mc.field_71439_g.field_70179_y = Math.cos(yaw) * speed;
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
