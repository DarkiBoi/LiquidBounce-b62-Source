// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACYPort2 extends SpeedMode
{
    public AACYPort2() {
        super("AACYPort2");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            AACYPort2.mc.field_71439_g.field_70726_aT = 0.0f;
            if (AACYPort2.mc.field_71439_g.field_70122_E) {
                AACYPort2.mc.field_71439_g.func_70664_aZ();
                AACYPort2.mc.field_71439_g.field_70181_x = 0.38510000705718994;
                final EntityPlayerSP field_71439_g = AACYPort2.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.01;
                final EntityPlayerSP field_71439_g2 = AACYPort2.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.01;
            }
            else {
                AACYPort2.mc.field_71439_g.field_70181_x = -0.21;
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
