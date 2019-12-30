// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop3 extends SpeedMode
{
    private boolean firstJump;
    private boolean waitForGround;
    
    public AACLowHop3() {
        super("AACLowHop3");
    }
    
    @Override
    public void onEnable() {
        this.firstJump = true;
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (AACLowHop3.mc.field_71439_g.field_70737_aN <= 0) {
                if (AACLowHop3.mc.field_71439_g.field_70122_E) {
                    this.waitForGround = false;
                    if (!this.firstJump) {
                        this.firstJump = true;
                    }
                    AACLowHop3.mc.field_71439_g.func_70664_aZ();
                    AACLowHop3.mc.field_71439_g.field_70181_x = 0.41;
                }
                else {
                    if (this.waitForGround) {
                        return;
                    }
                    if (AACLowHop3.mc.field_71439_g.field_70123_F) {
                        return;
                    }
                    this.firstJump = false;
                    final EntityPlayerSP field_71439_g = AACLowHop3.mc.field_71439_g;
                    field_71439_g.field_70181_x -= 0.0149;
                }
                if (!AACLowHop3.mc.field_71439_g.field_70123_F) {
                    MovementUtils.forward(this.firstJump ? 0.0016 : 0.001799);
                }
            }
            else {
                this.firstJump = true;
                this.waitForGround = true;
            }
        }
        else {
            AACLowHop3.mc.field_71439_g.field_70179_y = 0.0;
            AACLowHop3.mc.field_71439_g.field_70159_w = 0.0;
        }
        final double speed = MovementUtils.getSpeed();
        AACLowHop3.mc.field_71439_g.field_70159_w = -(Math.sin(MovementUtils.getDirection()) * speed);
        AACLowHop3.mc.field_71439_g.field_70179_y = Math.cos(MovementUtils.getDirection()) * speed;
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
