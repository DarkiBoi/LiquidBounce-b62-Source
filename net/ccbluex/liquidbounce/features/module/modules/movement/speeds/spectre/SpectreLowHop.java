// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpectreLowHop extends SpeedMode
{
    public SpectreLowHop() {
        super("SpectreLowHop");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || SpectreLowHop.mc.field_71439_g.field_71158_b.field_78901_c) {
            return;
        }
        if (SpectreLowHop.mc.field_71439_g.field_70122_E) {
            MovementUtils.strafe(1.1f);
            SpectreLowHop.mc.field_71439_g.field_70181_x = 0.15;
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
