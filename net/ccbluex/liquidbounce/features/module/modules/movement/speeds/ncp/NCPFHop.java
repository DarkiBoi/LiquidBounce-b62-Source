// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPFHop extends SpeedMode
{
    public NCPFHop() {
        super("NCPFHop");
    }
    
    @Override
    public void onEnable() {
        NCPFHop.mc.field_71428_T.field_74278_d = 1.0866f;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NCPFHop.mc.field_71439_g.field_71102_ce = 0.02f;
        NCPFHop.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (NCPFHop.mc.field_71439_g.field_70122_E) {
                NCPFHop.mc.field_71439_g.func_70664_aZ();
                final EntityPlayerSP field_71439_g = NCPFHop.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.01;
                final EntityPlayerSP field_71439_g2 = NCPFHop.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.01;
                NCPFHop.mc.field_71439_g.field_71102_ce = 0.0223f;
            }
            final EntityPlayerSP field_71439_g3 = NCPFHop.mc.field_71439_g;
            field_71439_g3.field_70181_x -= 9.9999E-4;
            MovementUtils.strafe();
        }
        else {
            NCPFHop.mc.field_71439_g.field_70159_w = 0.0;
            NCPFHop.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
