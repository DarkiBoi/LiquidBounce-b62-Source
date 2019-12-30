// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACYPort extends SpeedMode
{
    public AACYPort() {
        super("AACYPort");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving() && !AACYPort.mc.field_71439_g.func_70093_af()) {
            AACYPort.mc.field_71439_g.field_70726_aT = 0.0f;
            if (AACYPort.mc.field_71439_g.field_70122_E) {
                AACYPort.mc.field_71439_g.field_70181_x = 0.3425000011920929;
                final EntityPlayerSP field_71439_g = AACYPort.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.5893000364303589;
                final EntityPlayerSP field_71439_g2 = AACYPort.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.5893000364303589;
            }
            else {
                AACYPort.mc.field_71439_g.field_70181_x = -0.19;
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
