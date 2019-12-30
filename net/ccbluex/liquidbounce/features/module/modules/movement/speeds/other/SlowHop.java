// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SlowHop extends SpeedMode
{
    public SlowHop() {
        super("SlowHop");
    }
    
    @Override
    public void onMotion() {
        if (SlowHop.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (SlowHop.mc.field_71439_g.field_70122_E) {
                SlowHop.mc.field_71439_g.func_70664_aZ();
            }
            else {
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.011f);
            }
        }
        else {
            SlowHop.mc.field_71439_g.field_70159_w = 0.0;
            SlowHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
