// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpartanYPort extends SpeedMode
{
    private int airMoves;
    
    public SpartanYPort() {
        super("SpartanYPort");
    }
    
    @Override
    public void onMotion() {
        if (SpartanYPort.mc.field_71474_y.field_74351_w.func_151470_d() && !SpartanYPort.mc.field_71474_y.field_74314_A.func_151470_d()) {
            if (SpartanYPort.mc.field_71439_g.field_70122_E) {
                SpartanYPort.mc.field_71439_g.func_70664_aZ();
                this.airMoves = 0;
            }
            else {
                SpartanYPort.mc.field_71428_T.field_74278_d = 1.08f;
                if (this.airMoves >= 3) {
                    SpartanYPort.mc.field_71439_g.field_70747_aH = 0.0275f;
                }
                if (this.airMoves >= 4 && this.airMoves % 2 == 0.0) {
                    SpartanYPort.mc.field_71439_g.field_70181_x = -0.3199999928474426 - 0.009 * Math.random();
                    SpartanYPort.mc.field_71439_g.field_70747_aH = 0.0238f;
                }
                ++this.airMoves;
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
