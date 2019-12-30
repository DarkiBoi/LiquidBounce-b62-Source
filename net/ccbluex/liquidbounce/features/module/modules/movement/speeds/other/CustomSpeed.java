// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class CustomSpeed extends SpeedMode
{
    public CustomSpeed() {
        super("Custom");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            final Speed speed = (Speed)ModuleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            CustomSpeed.mc.field_71428_T.field_74278_d = speed.customTimerValue.asFloat();
            if (CustomSpeed.mc.field_71439_g.field_70122_E) {
                MovementUtils.strafe(speed.customSpeedValue.asFloat());
                CustomSpeed.mc.field_71439_g.field_70181_x = speed.customYValue.asFloat();
            }
            else if (speed.customStrafeValue.asBoolean()) {
                MovementUtils.strafe(speed.customSpeedValue.asFloat());
            }
            else {
                MovementUtils.strafe();
            }
        }
        else {
            final EntityPlayerSP field_71439_g = CustomSpeed.mc.field_71439_g;
            final EntityPlayerSP field_71439_g2 = CustomSpeed.mc.field_71439_g;
            final double n = 0.0;
            field_71439_g2.field_70179_y = n;
            field_71439_g.field_70159_w = n;
        }
    }
    
    @Override
    public void onEnable() {
        final Speed speed = (Speed)ModuleManager.getModule(Speed.class);
        if (speed == null) {
            return;
        }
        if (speed.resetXZValue.asBoolean()) {
            final EntityPlayerSP field_71439_g = CustomSpeed.mc.field_71439_g;
            final EntityPlayerSP field_71439_g2 = CustomSpeed.mc.field_71439_g;
            final double n = 0.0;
            field_71439_g2.field_70179_y = n;
            field_71439_g.field_70159_w = n;
        }
        if (speed.resetYValue.asBoolean()) {
            CustomSpeed.mc.field_71439_g.field_70181_x = 0.0;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        CustomSpeed.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
