// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.block;

import net.minecraft.util.MathHelper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class BlockUtils
{
    private static final Minecraft mc;
    
    public static Block getBlock(final BlockPos blockPos) {
        return BlockUtils.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
    }
    
    public static Material getMaterial(final BlockPos blockPos) {
        return getBlock(blockPos).func_149688_o();
    }
    
    public static boolean isReplaceable(final BlockPos blockPos) {
        return getMaterial(blockPos).func_76222_j();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtils.mc.field_71441_e.func_180495_p(pos);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).func_176209_a(getState(pos), false);
    }
    
    public static String getBlockName(final int id) {
        return Block.func_149729_e(id).func_149732_F();
    }
    
    public static boolean isFullBlock(final BlockPos blockPos) {
        final AxisAlignedBB axisAlignedBB = getBlock(blockPos).func_180640_a((World)BlockUtils.mc.field_71441_e, blockPos, getState(blockPos));
        return axisAlignedBB != null && axisAlignedBB.field_72336_d - axisAlignedBB.field_72340_a == 1.0 && axisAlignedBB.field_72337_e - axisAlignedBB.field_72338_b == 1.0 && axisAlignedBB.field_72334_f - axisAlignedBB.field_72339_c == 1.0;
    }
    
    public static double getCenterDistance(final BlockPos blockPos) {
        return BlockUtils.mc.field_71439_g.func_70011_f(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5);
    }
    
    public static Map<BlockPos, Block> searchBlocks(final int radius) {
        final Map<BlockPos, Block> blocks = new HashMap<BlockPos, Block>();
        for (int x = radius; x > -radius; --x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = radius; z > -radius; --z) {
                    final BlockPos blockPos = new BlockPos((int)BlockUtils.mc.field_71439_g.field_70165_t + x, (int)BlockUtils.mc.field_71439_g.field_70163_u + y, (int)BlockUtils.mc.field_71439_g.field_70161_v + z);
                    final Block block = getBlock(blockPos);
                    blocks.put(blockPos, block);
                }
            }
        }
        return blocks;
    }
    
    public static boolean collideBlock(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72340_a); x < MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72339_c); z < MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
                final Block block = getBlock(new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z));
                if (block != null && !collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean collideBlockIntersects(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72340_a); x < MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72339_c); z < MathHelper.func_76128_c(BlockUtils.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
                final BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z);
                final Block block = getBlock(blockPos);
                if (block != null && collide.collideBlock(block)) {
                    final AxisAlignedBB boundingBox = block.func_180640_a((World)BlockUtils.mc.field_71441_e, blockPos, getState(blockPos));
                    if (boundingBox != null && BlockUtils.mc.field_71439_g.func_174813_aQ().func_72326_a(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
    
    public interface ICollide
    {
        boolean collideBlock(final Block p0);
    }
}
