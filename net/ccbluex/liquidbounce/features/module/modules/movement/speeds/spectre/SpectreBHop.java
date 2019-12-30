// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpectreBHop extends SpeedMode
{
    public SpectreBHop() {
        super("SpectreBHop");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || SpectreBHop.mc.field_71439_g.field_71158_b.field_78901_c) {
            return;
        }
        if (SpectreBHop.mc.field_71439_g.field_70122_E) {
            MovementUtils.strafe(1.1f);
            SpectreBHop.mc.field_71439_g.field_70181_x = 0.44;
            return;
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
