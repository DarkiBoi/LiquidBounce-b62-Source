// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop2 extends SpeedMode
{
    private boolean legitJump;
    
    public AACLowHop2() {
        super("AACLowHop2");
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
        AACLowHop2.mc.field_71428_T.field_74278_d = 1.0f;
    }
    
    @Override
    public void onDisable() {
        AACLowHop2.mc.field_71428_T.field_74278_d = 1.0f;
    }
    
    @Override
    public void onMotion() {
        AACLowHop2.mc.field_71428_T.field_74278_d = 1.0f;
        if (AACLowHop2.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            AACLowHop2.mc.field_71428_T.field_74278_d = 1.09f;
            if (AACLowHop2.mc.field_71439_g.field_70122_E) {
                if (this.legitJump) {
                    AACLowHop2.mc.field_71439_g.func_70664_aZ();
                    this.legitJump = false;
                    return;
                }
                AACLowHop2.mc.field_71439_g.field_70181_x = 0.34299999475479126;
                MovementUtils.strafe(0.534f);
            }
        }
        else {
            this.legitJump = true;
            AACLowHop2.mc.field_71439_g.field_70159_w = 0.0;
            AACLowHop2.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
