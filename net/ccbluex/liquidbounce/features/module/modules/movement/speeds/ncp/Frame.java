// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class Frame extends SpeedMode
{
    private int motionTicks;
    private boolean move;
    private final TickTimer tickTimer;
    
    public Frame() {
        super("Frame");
        this.tickTimer = new TickTimer();
    }
    
    @Override
    public void onMotion() {
        if (Frame.mc.field_71439_g.field_71158_b.field_78900_b > 0.0f || Frame.mc.field_71439_g.field_71158_b.field_78902_a > 0.0f) {
            final double speed = 4.25;
            if (Frame.mc.field_71439_g.field_70122_E) {
                Frame.mc.field_71439_g.func_70664_aZ();
                if (this.motionTicks == 1) {
                    this.tickTimer.reset();
                    if (this.move) {
                        Frame.mc.field_71439_g.field_70159_w = 0.0;
                        Frame.mc.field_71439_g.field_70179_y = 0.0;
                        this.move = false;
                    }
                    this.motionTicks = 0;
                }
                else {
                    this.motionTicks = 1;
                }
            }
            else if (!this.move && this.motionTicks == 1 && this.tickTimer.hasTimePassed(5)) {
                final EntityPlayerSP field_71439_g = Frame.mc.field_71439_g;
                field_71439_g.field_70159_w *= 4.25;
                final EntityPlayerSP field_71439_g2 = Frame.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 4.25;
                this.move = true;
            }
            if (!Frame.mc.field_71439_g.field_70122_E) {
                MovementUtils.strafe();
            }
            this.tickTimer.update();
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
