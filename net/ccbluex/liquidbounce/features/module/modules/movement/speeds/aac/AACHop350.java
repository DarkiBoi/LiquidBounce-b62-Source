// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventListener;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACHop350 extends SpeedMode implements EventListener
{
    public AACHop350() {
        super("AACHop3.5.0");
        LiquidBounce.CLIENT.eventManager.registerListener(this);
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() == EventState.POST && MovementUtils.isMoving() && !AACHop350.mc.field_71439_g.func_70090_H() && !AACHop350.mc.field_71439_g.func_180799_ab()) {
            final EntityPlayerSP field_71439_g = AACHop350.mc.field_71439_g;
            field_71439_g.field_70747_aH += 0.00208f;
            if (AACHop350.mc.field_71439_g.field_70143_R <= 1.0f) {
                if (AACHop350.mc.field_71439_g.field_70122_E) {
                    AACHop350.mc.field_71439_g.func_70664_aZ();
                    final EntityPlayerSP field_71439_g2 = AACHop350.mc.field_71439_g;
                    field_71439_g2.field_70159_w *= 1.0118000507354736;
                    final EntityPlayerSP field_71439_g3 = AACHop350.mc.field_71439_g;
                    field_71439_g3.field_70179_y *= 1.0118000507354736;
                }
                else {
                    final EntityPlayerSP field_71439_g4 = AACHop350.mc.field_71439_g;
                    field_71439_g4.field_70181_x -= 0.014700000174343586;
                    final EntityPlayerSP field_71439_g5 = AACHop350.mc.field_71439_g;
                    field_71439_g5.field_70159_w *= 1.0013799667358398;
                    final EntityPlayerSP field_71439_g6 = AACHop350.mc.field_71439_g;
                    field_71439_g6.field_70179_y *= 1.0013799667358398;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (AACHop350.mc.field_71439_g.field_70122_E) {
            final EntityPlayerSP field_71439_g = AACHop350.mc.field_71439_g;
            final EntityPlayerSP field_71439_g2 = AACHop350.mc.field_71439_g;
            final double n = 0.0;
            field_71439_g2.field_70179_y = n;
            field_71439_g.field_70159_w = n;
        }
    }
    
    @Override
    public void onDisable() {
        AACHop350.mc.field_71439_g.field_70747_aH = 0.02f;
    }
    
    @Override
    public boolean handleEvents() {
        return this.isActive();
    }
}
