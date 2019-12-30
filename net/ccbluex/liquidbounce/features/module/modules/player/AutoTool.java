// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoTool", description = "Automatically selects the best tool in your inventory to mine a block.", category = ModuleCategory.PLAYER)
public class AutoTool extends Module
{
    @EventTarget
    public void onClick(final ClickBlockEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        this.switchSlot(event.getClickedBlock());
    }
    
    public void switchSlot(final BlockPos blockPos) {
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        final Block block = AutoTool.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = AutoTool.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (item != null) {
                final float speed = item.func_150997_a(block);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        if (bestSlot != -1) {
            AutoTool.mc.field_71439_g.field_71071_by.field_70461_c = bestSlot;
        }
    }
}
