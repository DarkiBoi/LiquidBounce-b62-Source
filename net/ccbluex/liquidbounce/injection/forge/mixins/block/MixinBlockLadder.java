// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.BlockLadder;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ BlockLadder.class })
public abstract class MixinBlockLadder extends MixinBlock
{
    @Shadow
    @Final
    public static PropertyDirection field_176382_a;
    
    @Overwrite
    public void func_180654_a(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.func_180495_p(pos);
        if (iblockstate.func_177230_c() instanceof BlockLadder) {
            final FastClimb fastClimb = (FastClimb)ModuleManager.getModule(FastClimb.class);
            final float f = (fastClimb.getState() && fastClimb.modeValue.asString().equalsIgnoreCase("AAC")) ? 0.99f : 0.125f;
            switch ((EnumFacing)iblockstate.func_177229_b((IProperty)MixinBlockLadder.field_176382_a)) {
                case NORTH: {
                    this.func_149676_a(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.func_149676_a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
                    break;
                }
                case WEST: {
                    this.func_149676_a(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                default: {
                    this.func_149676_a(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
}
