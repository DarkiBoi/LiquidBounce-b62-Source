// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiSpectator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiSpectator.class })
public class MixinGuiSpectator
{
    @Inject(method = { "renderTooltip" }, at = { @At("RETURN") })
    private void renderTooltipPost(final ScaledResolution p_175264_1_, final float p_175264_2_, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(p_175264_2_));
    }
}
