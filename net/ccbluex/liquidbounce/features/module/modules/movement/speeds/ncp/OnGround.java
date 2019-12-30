// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class OnGround extends SpeedMode
{
    public OnGround() {
        super("OnGround");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (OnGround.mc.field_71439_g.field_70143_R > 3.994) {
            return;
        }
        if (OnGround.mc.field_71439_g.func_70090_H() || OnGround.mc.field_71439_g.func_70617_f_() || OnGround.mc.field_71439_g.field_70123_F) {
            return;
        }
        final EntityPlayerSP field_71439_g = OnGround.mc.field_71439_g;
        field_71439_g.field_70163_u -= 0.3993000090122223;
        OnGround.mc.field_71439_g.field_70181_x = -1000.0;
        OnGround.mc.field_71439_g.field_70726_aT = 0.3f;
        OnGround.mc.field_71439_g.field_70140_Q = 44.0f;
        OnGround.mc.field_71428_T.field_74278_d = 1.0f;
        if (OnGround.mc.field_71439_g.field_70122_E) {
            final EntityPlayerSP field_71439_g2 = OnGround.mc.field_71439_g;
            field_71439_g2.field_70163_u += 0.3993000090122223;
            OnGround.mc.field_71439_g.field_70181_x = 0.3993000090122223;
            OnGround.mc.field_71439_g.field_82151_R = 44.0f;
            final EntityPlayerSP field_71439_g3 = OnGround.mc.field_71439_g;
            field_71439_g3.field_70159_w *= 1.590000033378601;
            final EntityPlayerSP field_71439_g4 = OnGround.mc.field_71439_g;
            field_71439_g4.field_70179_y *= 1.590000033378601;
            OnGround.mc.field_71439_g.field_70726_aT = 0.0f;
            OnGround.mc.field_71428_T.field_74278_d = 1.199f;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
