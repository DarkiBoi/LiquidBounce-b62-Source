// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class YPort2 extends SpeedMode
{
    public YPort2() {
        super("YPort2");
    }
    
    @Override
    public void onMotion() {
        if (YPort2.mc.field_71439_g.func_70617_f_() || YPort2.mc.field_71439_g.func_70090_H() || YPort2.mc.field_71439_g.func_180799_ab() || YPort2.mc.field_71439_g.field_70134_J || !MovementUtils.isMoving()) {
            return;
        }
        if (YPort2.mc.field_71439_g.field_70122_E) {
            YPort2.mc.field_71439_g.func_70664_aZ();
        }
        else {
            YPort2.mc.field_71439_g.field_70181_x = -1.0;
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
