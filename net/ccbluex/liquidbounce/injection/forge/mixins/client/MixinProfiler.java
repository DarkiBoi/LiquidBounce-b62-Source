// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Profiler.class })
public class MixinProfiler
{
    @Inject(method = { "startSection" }, at = { @At("HEAD") })
    private void startSection(final String name, final CallbackInfo callbackInfo) {
        if (name.equals("bossHealth") && ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(0.0f));
        }
    }
}
