// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.Event;

public class BlockBBEvent extends Event
{
    private final int x;
    private final int y;
    private final int z;
    private final Block block;
    private AxisAlignedBB axisAlignedBB;
    
    public BlockBBEvent(final BlockPos blockPos, final Block block, final AxisAlignedBB axisAlignedBB) {
        this.x = blockPos.func_177958_n();
        this.y = blockPos.func_177956_o();
        this.z = blockPos.func_177952_p();
        this.block = block;
        this.setBoundingBox(axisAlignedBB);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.axisAlignedBB;
    }
    
    public void setBoundingBox(final AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
}
