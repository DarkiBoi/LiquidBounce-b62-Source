// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.RenderEntityEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Render.class })
public abstract class MixinRender
{
    @Shadow
    protected abstract <T extends Entity> boolean func_180548_c(final T p0);
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    private void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new RenderEntityEvent(entity, x, y, z, entityYaw, partialTicks));
    }
}
