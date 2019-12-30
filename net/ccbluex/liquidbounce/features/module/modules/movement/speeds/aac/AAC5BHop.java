// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC5BHop extends SpeedMode
{
    private boolean legitJump;
    
    public AAC5BHop() {
        super("AAC5BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onTick() {
        AAC5BHop.mc.field_71428_T.field_74278_d = 1.0f;
        if (AAC5BHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC5BHop.mc.field_71439_g.field_70122_E) {
                if (this.legitJump) {
                    AAC5BHop.mc.field_71439_g.func_70664_aZ();
                    this.legitJump = false;
                    return;
                }
                AAC5BHop.mc.field_71439_g.field_70181_x = 0.41;
                AAC5BHop.mc.field_71439_g.field_70122_E = false;
                MovementUtils.strafe(0.374f);
            }
            else if (AAC5BHop.mc.field_71439_g.field_70181_x < 0.0) {
                AAC5BHop.mc.field_71439_g.field_71102_ce = 0.0201f;
                AAC5BHop.mc.field_71428_T.field_74278_d = 1.02f;
            }
            else {
                AAC5BHop.mc.field_71428_T.field_74278_d = 1.01f;
            }
        }
        else {
            this.legitJump = true;
            AAC5BHop.mc.field_71439_g.field_70159_w = 0.0;
            AAC5BHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
}
