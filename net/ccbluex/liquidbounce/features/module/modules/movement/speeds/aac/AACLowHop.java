// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop extends SpeedMode
{
    private boolean legitJump;
    
    public AACLowHop() {
        super("AACLowHop");
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
        super.onEnable();
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (AACLowHop.mc.field_71439_g.field_70122_E) {
                if (this.legitJump) {
                    AACLowHop.mc.field_71439_g.func_70664_aZ();
                    this.legitJump = false;
                    return;
                }
                AACLowHop.mc.field_71439_g.field_70181_x = 0.34299999475479126;
                MovementUtils.strafe(0.534f);
            }
        }
        else {
            this.legitJump = true;
            AACLowHop.mc.field_71439_g.field_70159_w = 0.0;
            AACLowHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
