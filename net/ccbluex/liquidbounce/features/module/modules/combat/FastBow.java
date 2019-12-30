// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemBow;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
public class FastBow extends Module
{
    private final IntegerValue packetsValue;
    
    public FastBow() {
        this.packetsValue = new IntegerValue("Packets", 20, 3, 20);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!FastBow.mc.field_71439_g.func_71039_bw()) {
            return;
        }
        if (FastBow.mc.field_71439_g.field_71071_by.func_70448_g() != null && FastBow.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) {
            FastBow.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(BlockPos.field_177992_a, 255, FastBow.mc.field_71439_g.func_71045_bC(), 0.0f, 0.0f, 0.0f));
            final float yaw = RotationUtils.lookChanged ? RotationUtils.targetYaw : FastBow.mc.field_71439_g.field_70177_z;
            final float pitch = RotationUtils.lookChanged ? RotationUtils.targetPitch : FastBow.mc.field_71439_g.field_70125_A;
            for (int i = 0; i < this.packetsValue.asInteger(); ++i) {
                FastBow.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
            }
            FastBow.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            FastBow.mc.field_71439_g.field_71072_f = FastBow.mc.field_71439_g.field_71071_by.func_70448_g().func_77988_m() - 1;
        }
    }
}
