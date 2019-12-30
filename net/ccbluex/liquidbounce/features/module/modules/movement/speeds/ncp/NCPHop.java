// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPHop extends SpeedMode
{
    public NCPHop() {
        super("NCPHop");
    }
    
    @Override
    public void onEnable() {
        NCPHop.mc.field_71428_T.field_74278_d = 1.0865f;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NCPHop.mc.field_71439_g.field_71102_ce = 0.02f;
        NCPHop.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (NCPHop.mc.field_71439_g.field_70122_E) {
                NCPHop.mc.field_71439_g.func_70664_aZ();
                NCPHop.mc.field_71439_g.field_71102_ce = 0.0223f;
            }
            MovementUtils.strafe();
        }
        else {
            NCPHop.mc.field_71439_g.field_70159_w = 0.0;
            NCPHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
