// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoBow", description = "Automatically shoots an arrow whenever your bow is fully loaded.", category = ModuleCategory.COMBAT)
public class AutoBow extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoBow.mc.field_71439_g.func_70694_bm() != null && AutoBow.mc.field_71439_g.func_70694_bm().func_77973_b() == Items.field_151031_f && AutoBow.mc.field_71439_g.func_71039_bw() && AutoBow.mc.field_71439_g.func_71057_bx() > 20) {
            AutoBow.mc.field_71439_g.func_71034_by();
            AutoBow.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
        }
    }
}
