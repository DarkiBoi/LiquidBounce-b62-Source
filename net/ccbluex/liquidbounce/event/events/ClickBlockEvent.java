// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.event.Event;

public class ClickBlockEvent extends Event
{
    private final BlockPos clickedBlock;
    private final EnumFacing enumFacing;
    
    public ClickBlockEvent(final BlockPos clickedBlock, final EnumFacing enumFacing) {
        this.clickedBlock = clickedBlock;
        this.enumFacing = enumFacing;
    }
    
    public BlockPos getClickedBlock() {
        return this.clickedBlock;
    }
    
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
}
