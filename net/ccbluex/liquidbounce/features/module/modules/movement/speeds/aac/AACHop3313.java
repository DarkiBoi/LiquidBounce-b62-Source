// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACHop3313 extends SpeedMode
{
    public AACHop3313() {
        super("AACHop3.3.13");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || AACHop3313.mc.field_71439_g.func_70090_H() || AACHop3313.mc.field_71439_g.func_180799_ab() || AACHop3313.mc.field_71439_g.func_70617_f_() || AACHop3313.mc.field_71439_g.func_70115_ae()) {
            return;
        }
        if (AACHop3313.mc.field_71439_g.field_70122_E) {
            AACHop3313.mc.field_71439_g.func_70664_aZ();
            AACHop3313.mc.field_71439_g.field_70181_x = 0.41;
        }
        else if (!AACHop3313.mc.field_71439_g.field_70123_F && AACHop3313.mc.field_71439_g.field_70737_aN <= 6) {
            AACHop3313.mc.field_71439_g.field_70747_aH = 0.027f;
            final float boostUp = (AACHop3313.mc.field_71439_g.field_70181_x <= 0.0) ? RandomUtils.nextFloat(1.002f, 1.0023f) : RandomUtils.nextFloat(1.0059f, 1.0061f);
            final EntityPlayerSP field_71439_g = AACHop3313.mc.field_71439_g;
            field_71439_g.field_70159_w *= boostUp;
            final EntityPlayerSP field_71439_g2 = AACHop3313.mc.field_71439_g;
            field_71439_g2.field_70179_y *= boostUp;
            MovementUtils.forward(0.0019);
            final EntityPlayerSP field_71439_g3 = AACHop3313.mc.field_71439_g;
            field_71439_g3.field_70181_x -= 0.01489999983459711;
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
        AACHop3313.mc.field_71439_g.field_70747_aH = 0.02f;
    }
}
