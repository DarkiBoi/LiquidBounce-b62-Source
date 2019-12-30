// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BlockWalk", description = "Allows you to walk on non-fullblock blocks.", category = ModuleCategory.MOVEMENT)
public class BlockWalk extends Module
{
    private final BoolValue cobwebValue;
    private final BoolValue snowValue;
    
    public BlockWalk() {
        this.cobwebValue = new BoolValue("Cobweb", true);
        this.snowValue = new BoolValue("Snow", true);
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if ((this.cobwebValue.asBoolean() && event.getBlock() == Blocks.field_150321_G) || (this.snowValue.asBoolean() && event.getBlock() == Blocks.field_150431_aC)) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), event.getY() + 1.0, (double)(event.getZ() + 1)));
        }
    }
}
