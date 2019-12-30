// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class OldAACBHop extends SpeedMode
{
    public OldAACBHop() {
        super("OldAACBHop");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (OldAACBHop.mc.field_71439_g.field_70122_E) {
                MovementUtils.strafe(0.56f);
                OldAACBHop.mc.field_71439_g.field_70181_x = 0.41999998688697815;
            }
            else {
                MovementUtils.strafe(MovementUtils.getSpeed() * ((OldAACBHop.mc.field_71439_g.field_70143_R > 0.4f) ? 1.0f : 1.01f));
            }
        }
        else {
            OldAACBHop.mc.field_71439_g.field_70159_w = 0.0;
            OldAACBHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
