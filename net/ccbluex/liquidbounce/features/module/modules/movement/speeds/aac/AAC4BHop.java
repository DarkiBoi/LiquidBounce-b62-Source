// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC4BHop extends SpeedMode
{
    private boolean legitHop;
    
    public AAC4BHop() {
        super("AAC4BHop");
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
    public void onEnable() {
        this.legitHop = true;
    }
    
    @Override
    public void onDisable() {
        AAC4BHop.mc.field_71439_g.field_71102_ce = 0.02f;
    }
    
    @Override
    public void onTick() {
        if (MovementUtils.isMoving()) {
            if (this.legitHop) {
                if (AAC4BHop.mc.field_71439_g.field_70122_E) {
                    AAC4BHop.mc.field_71439_g.func_70664_aZ();
                    AAC4BHop.mc.field_71439_g.field_70122_E = false;
                    this.legitHop = false;
                }
                return;
            }
            if (AAC4BHop.mc.field_71439_g.field_70122_E) {
                AAC4BHop.mc.field_71439_g.field_70122_E = false;
                MovementUtils.strafe(0.375f);
                AAC4BHop.mc.field_71439_g.func_70664_aZ();
                AAC4BHop.mc.field_71439_g.field_70181_x = 0.41;
            }
            else {
                AAC4BHop.mc.field_71439_g.field_71102_ce = 0.0211f;
            }
        }
        else {
            AAC4BHop.mc.field_71439_g.field_70159_w = 0.0;
            AAC4BHop.mc.field_71439_g.field_70179_y = 0.0;
            this.legitHop = true;
        }
    }
}
