// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class HiveHop extends SpeedMode
{
    public HiveHop() {
        super("HiveHop");
    }
    
    @Override
    public void onEnable() {
        HiveHop.mc.field_71439_g.field_71102_ce = 0.0425f;
        HiveHop.mc.field_71428_T.field_74278_d = 1.04f;
    }
    
    @Override
    public void onDisable() {
        HiveHop.mc.field_71439_g.field_71102_ce = 0.02f;
        HiveHop.mc.field_71428_T.field_74278_d = 1.0f;
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (HiveHop.mc.field_71439_g.field_70122_E) {
                HiveHop.mc.field_71439_g.field_70181_x = 0.3;
            }
            HiveHop.mc.field_71439_g.field_71102_ce = 0.0425f;
            HiveHop.mc.field_71428_T.field_74278_d = 1.04f;
            MovementUtils.strafe();
        }
        else {
            final EntityPlayerSP field_71439_g = HiveHop.mc.field_71439_g;
            final EntityPlayerSP field_71439_g2 = HiveHop.mc.field_71439_g;
            final double n = 0.0;
            field_71439_g2.field_70179_y = n;
            field_71439_g.field_70159_w = n;
            HiveHop.mc.field_71439_g.field_71102_ce = 0.02f;
            HiveHop.mc.field_71428_T.field_74278_d = 1.0f;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
