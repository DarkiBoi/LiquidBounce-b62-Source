// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ TileEntityChestRenderer.class })
public class MixinTileEntityChestRenderer
{
    @Inject(method = { "renderTileEntityAt" }, at = { @At("HEAD") })
    private void injectChamsPre(final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.chestsValue.asBoolean()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }
    
    @Inject(method = { "renderTileEntityAt" }, at = { @At("RETURN") })
    private void injectChamsPost(final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.chestsValue.asBoolean()) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
