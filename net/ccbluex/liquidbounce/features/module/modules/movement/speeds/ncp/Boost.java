// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class Boost extends SpeedMode
{
    private int motionDelay;
    private float ground;
    
    public Boost() {
        super("Boost");
    }
    
    @Override
    public void onMotion() {
        double speed = 3.1981;
        double offset = 4.69;
        boolean shouldOffset = true;
        for (final Object o : Boost.mc.field_71441_e.func_72945_a((Entity)Boost.mc.field_71439_g, Boost.mc.field_71439_g.func_174813_aQ().func_72317_d(Boost.mc.field_71439_g.field_70159_w / offset, 0.0, Boost.mc.field_71439_g.field_70179_y / offset))) {
            if (o instanceof AxisAlignedBB) {
                shouldOffset = false;
                break;
            }
        }
        if (Boost.mc.field_71439_g.field_70122_E && this.ground < 1.0f) {
            this.ground += 0.2f;
        }
        if (!Boost.mc.field_71439_g.field_70122_E) {
            this.ground = 0.0f;
        }
        if (this.ground == 1.0f && this.shouldSpeedUp()) {
            if (!Boost.mc.field_71439_g.func_70051_ag()) {
                offset += 0.8;
            }
            if (Boost.mc.field_71439_g.field_70702_br != 0.0f) {
                speed -= 0.1;
                offset += 0.5;
            }
            if (Boost.mc.field_71439_g.func_70090_H()) {
                speed -= 0.1;
            }
            switch (++this.motionDelay) {
                case 1: {
                    final EntityPlayerSP field_71439_g = Boost.mc.field_71439_g;
                    field_71439_g.field_70159_w *= speed;
                    final EntityPlayerSP field_71439_g2 = Boost.mc.field_71439_g;
                    field_71439_g2.field_70179_y *= speed;
                    break;
                }
                case 2: {
                    final EntityPlayerSP field_71439_g3 = Boost.mc.field_71439_g;
                    field_71439_g3.field_70159_w /= 1.458;
                    final EntityPlayerSP field_71439_g4 = Boost.mc.field_71439_g;
                    field_71439_g4.field_70179_y /= 1.458;
                    break;
                }
                case 4: {
                    if (shouldOffset) {
                        Boost.mc.field_71439_g.func_70107_b(Boost.mc.field_71439_g.field_70165_t + Boost.mc.field_71439_g.field_70159_w / offset, Boost.mc.field_71439_g.field_70163_u, Boost.mc.field_71439_g.field_70161_v + Boost.mc.field_71439_g.field_70179_y / offset);
                    }
                    this.motionDelay = 0;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    private boolean shouldSpeedUp() {
        return !Boost.mc.field_71439_g.func_70090_H() && !Boost.mc.field_71439_g.func_70617_f_() && !Boost.mc.field_71439_g.func_70093_af() && MovementUtils.isMoving();
    }
}
