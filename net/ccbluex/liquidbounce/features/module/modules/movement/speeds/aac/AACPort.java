// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACPort extends SpeedMode
{
    public AACPort() {
        super("AACPort");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        final float f = AACPort.mc.field_71439_g.field_70177_z * 0.017453292f;
        for (double d = 0.2; d <= ((Speed)ModuleManager.getModule(Speed.class)).portMax.asFloat(); d += 0.2) {
            final double x = AACPort.mc.field_71439_g.field_70165_t - MathHelper.func_76126_a(f) * d;
            final double z = AACPort.mc.field_71439_g.field_70161_v + MathHelper.func_76134_b(f) * d;
            if (AACPort.mc.field_71439_g.field_70163_u < (int)AACPort.mc.field_71439_g.field_70163_u + 0.5 && !(BlockUtils.getBlock(new BlockPos(x, AACPort.mc.field_71439_g.field_70163_u, z)) instanceof BlockAir)) {
                break;
            }
            AACPort.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, AACPort.mc.field_71439_g.field_70163_u, z, true));
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
