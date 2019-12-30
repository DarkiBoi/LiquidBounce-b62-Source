// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACBHop extends SpeedMode
{
    public AACBHop() {
        super("AACBHop");
    }
    
    @Override
    public void onMotion() {
        if (AACBHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            AACBHop.mc.field_71428_T.field_74278_d = 1.08f;
            if (AACBHop.mc.field_71439_g.field_70122_E) {
                AACBHop.mc.field_71439_g.field_70181_x = 0.399;
                final float f = AACBHop.mc.field_71439_g.field_70177_z * 0.017453292f;
                final EntityPlayerSP field_71439_g = AACBHop.mc.field_71439_g;
                field_71439_g.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
                final EntityPlayerSP field_71439_g2 = AACBHop.mc.field_71439_g;
                field_71439_g2.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
                AACBHop.mc.field_71428_T.field_74278_d = 2.0f;
            }
            else {
                final EntityPlayerSP field_71439_g3 = AACBHop.mc.field_71439_g;
                field_71439_g3.field_70181_x *= 0.97;
                final EntityPlayerSP field_71439_g4 = AACBHop.mc.field_71439_g;
                field_71439_g4.field_70159_w *= 1.008;
                final EntityPlayerSP field_71439_g5 = AACBHop.mc.field_71439_g;
                field_71439_g5.field_70179_y *= 1.008;
            }
        }
        else {
            AACBHop.mc.field_71439_g.field_70159_w = 0.0;
            AACBHop.mc.field_71439_g.field_70179_y = 0.0;
            AACBHop.mc.field_71428_T.field_74278_d = 1.0f;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACBHop.mc.field_71428_T.field_74278_d = 1.0f;
    }
}
