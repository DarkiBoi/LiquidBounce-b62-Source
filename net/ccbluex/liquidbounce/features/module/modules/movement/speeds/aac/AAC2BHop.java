// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC2BHop extends SpeedMode
{
    public AAC2BHop() {
        super("AAC2BHop");
    }
    
    @Override
    public void onMotion() {
        if (AAC2BHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC2BHop.mc.field_71439_g.field_70122_E) {
                AAC2BHop.mc.field_71439_g.func_70664_aZ();
                final EntityPlayerSP field_71439_g = AAC2BHop.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.02;
                final EntityPlayerSP field_71439_g2 = AAC2BHop.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.02;
            }
            else if (AAC2BHop.mc.field_71439_g.field_70181_x > -0.2) {
                AAC2BHop.mc.field_71439_g.field_70747_aH = 0.08f;
                final EntityPlayerSP field_71439_g3 = AAC2BHop.mc.field_71439_g;
                field_71439_g3.field_70181_x += 0.01431;
                AAC2BHop.mc.field_71439_g.field_70747_aH = 0.07f;
            }
        }
        else {
            AAC2BHop.mc.field_71439_g.field_70159_w = 0.0;
            AAC2BHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
