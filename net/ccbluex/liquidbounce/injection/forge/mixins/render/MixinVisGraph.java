// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ VisGraph.class })
public class MixinVisGraph
{
    @Inject(method = { "func_178606_a" }, at = { @At("HEAD") }, cancellable = true)
    private void func_178606_a(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(XRay.class).getState()) {
            callbackInfo.cancel();
        }
    }
}
