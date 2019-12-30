// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.utils.ServerUtils;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiIngameMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiIngameMenu.class })
public abstract class MixinGuiIngameMenu extends MixinGuiScreen
{
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void initGui(final CallbackInfo callbackInfo) {
        if (!this.field_146297_k.func_71387_A()) {
            this.field_146292_n.add(new GuiButton(1337, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 128, "Reconnect"));
        }
    }
    
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") })
    private void actionPerformed(final GuiButton button, final CallbackInfo callbackInfo) {
        if (button.field_146127_k == 1337) {
            this.field_146297_k.field_71441_e.func_72882_A();
            ServerUtils.connectToLastServer();
        }
    }
}
