// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ TileEntityRendererDispatcher.class })
public class MixinTileEntityRendererDispatcher
{
    @Inject(method = { "renderTileEntity" }, at = { @At("HEAD") }, cancellable = true)
    private void renderTileEntity(final TileEntity tileentityIn, final float partialTicks, final int destroyStage, final CallbackInfo callbackInfo) {
        final XRay xray = (XRay)ModuleManager.getModule(XRay.class);
        if (xray.getState() && !xray.xrayBlocks.contains(tileentityIn.func_145838_q())) {
            callbackInfo.cancel();
        }
    }
}
