// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC6BHop extends SpeedMode
{
    private boolean legitJump;
    
    public AAC6BHop() {
        super("AAC6BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        AAC6BHop.mc.field_71428_T.field_74278_d = 1.0f;
        if (AAC6BHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC6BHop.mc.field_71439_g.field_70122_E) {
                if (this.legitJump) {
                    AAC6BHop.mc.field_71439_g.field_70181_x = 0.4;
                    MovementUtils.strafe(0.15f);
                    AAC6BHop.mc.field_71439_g.field_70122_E = false;
                    this.legitJump = false;
                    return;
                }
                AAC6BHop.mc.field_71439_g.field_70181_x = 0.41;
                MovementUtils.strafe(0.47458485f);
            }
            if (AAC6BHop.mc.field_71439_g.field_70181_x < 0.0 && AAC6BHop.mc.field_71439_g.field_70181_x > -0.2) {
                AAC6BHop.mc.field_71428_T.field_74278_d = (float)(1.2 + AAC6BHop.mc.field_71439_g.field_70181_x);
            }
            AAC6BHop.mc.field_71439_g.field_71102_ce = 0.022151f;
        }
        else {
            this.legitJump = true;
            AAC6BHop.mc.field_71439_g.field_70159_w = 0.0;
            AAC6BHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
    }
    
    @Override
    public void onDisable() {
        AAC6BHop.mc.field_71428_T.field_74278_d = 1.0f;
        AAC6BHop.mc.field_71439_g.field_71102_ce = 0.02f;
    }
}
