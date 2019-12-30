// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACGround extends SpeedMode
{
    public AACGround() {
        super("AACGround");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        AACGround.mc.field_71428_T.field_74278_d = ((Speed)ModuleManager.getModule(Speed.class)).aacGroundTimerValue.asFloat();
        AACGround.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AACGround.mc.field_71439_g.field_70165_t, AACGround.mc.field_71439_g.field_70163_u, AACGround.mc.field_71439_g.field_70161_v, true));
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACGround.mc.field_71428_T.field_74278_d = 1.0f;
    }
}
