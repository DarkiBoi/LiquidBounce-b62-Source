// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.BlockModelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockModelRenderer.class })
public class MixinBlockModelRenderer
{
    @Inject(method = { "renderModelAmbientOcclusion" }, at = { @At("HEAD") }, cancellable = true)
    private void renderModelAmbientOcclusion(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSide, final CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        final XRay xray = (XRay)ModuleManager.getModule(XRay.class);
        if (xray.getState() && !xray.xrayBlocks.contains(blockIn)) {
            booleanCallbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Inject(method = { "renderModelStandard" }, at = { @At("HEAD") }, cancellable = true)
    private void renderModelStandard(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides, final CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        final XRay xray = (XRay)ModuleManager.getModule(XRay.class);
        if (xray.getState() && !xray.xrayBlocks.contains(blockIn)) {
            booleanCallbackInfoReturnable.setReturnValue(false);
        }
    }
}
