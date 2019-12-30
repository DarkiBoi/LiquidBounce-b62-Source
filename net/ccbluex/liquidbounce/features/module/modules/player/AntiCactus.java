// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockCactus;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AntiCactus", description = "Prevents cactuses from damaging you.", category = ModuleCategory.PLAYER)
public class AntiCactus extends Module
{
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (event.getBlock() instanceof BlockCactus) {
            event.setBoundingBox(new AxisAlignedBB((double)event.getX(), (double)event.getY(), (double)event.getZ(), event.getX() + 1.0, event.getY() + 1.0, event.getZ() + 1.0));
        }
    }
}
