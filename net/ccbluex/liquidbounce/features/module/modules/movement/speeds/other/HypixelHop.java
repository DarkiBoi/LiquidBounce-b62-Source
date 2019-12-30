// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class HypixelHop extends SpeedMode
{
    public HypixelHop() {
        super("HypixelHop");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (HypixelHop.mc.field_71439_g.field_70122_E) {
                HypixelHop.mc.field_71439_g.func_70664_aZ();
                float speed = (MovementUtils.getSpeed() < 0.56f) ? (MovementUtils.getSpeed() * 1.045f) : 0.56f;
                if (HypixelHop.mc.field_71439_g.field_70122_E && HypixelHop.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                    speed *= 1.0f + 0.13f * (1 + HypixelHop.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c());
                }
                MovementUtils.strafe(speed);
                return;
            }
            if (HypixelHop.mc.field_71439_g.field_70181_x < 0.2) {
                final EntityPlayerSP field_71439_g = HypixelHop.mc.field_71439_g;
                field_71439_g.field_70181_x -= 0.02;
            }
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.01889f);
        }
        else {
            final EntityPlayerSP field_71439_g2 = HypixelHop.mc.field_71439_g;
            final EntityPlayerSP field_71439_g3 = HypixelHop.mc.field_71439_g;
            final double n = 0.0;
            field_71439_g3.field_70179_y = n;
            field_71439_g2.field_70159_w = n;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
