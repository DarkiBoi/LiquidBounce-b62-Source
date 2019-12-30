// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.TextEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ FontRenderer.class })
public class MixinFontRenderer
{
    @ModifyVariable(method = { "renderString" }, at = @At("HEAD"), ordinal = 0)
    private String renderString(final String string) {
        if (string == null || LiquidBounce.CLIENT.eventManager == null) {
            return string;
        }
        final TextEvent textEvent = new TextEvent(string);
        LiquidBounce.CLIENT.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }
    
    @ModifyVariable(method = { "getStringWidth" }, at = @At("HEAD"), ordinal = 0)
    private String getStringWidth(final String string) {
        if (string == null || LiquidBounce.CLIENT.eventManager == null) {
            return string;
        }
        final TextEvent textEvent = new TextEvent(string);
        LiquidBounce.CLIENT.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }
}
