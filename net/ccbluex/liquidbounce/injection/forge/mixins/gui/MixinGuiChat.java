// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiChat.class })
public abstract class MixinGuiChat extends MixinGuiScreen
{
    @Shadow
    protected GuiTextField field_146415_a;
    private float yPosOfInputField;
    private float fade;
    
    public MixinGuiChat() {
        this.fade = 0.0f;
    }
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void init(final CallbackInfo callbackInfo) {
        this.field_146415_a.field_146210_g = this.field_146295_m + 1;
        this.yPosOfInputField = (float)this.field_146415_a.field_146210_g;
    }
    
    @Inject(method = { "keyTyped" }, at = { @At("RETURN") })
    private void updateLenght(final CallbackInfo callbackInfo) {
        final String func_146179_b = this.field_146415_a.func_146179_b();
        LiquidBounce.CLIENT.commandManager.getClass();
        if (func_146179_b.startsWith(String.valueOf('.'))) {
            final String func_146179_b2 = this.field_146415_a.func_146179_b();
            final StringBuilder sb = new StringBuilder();
            LiquidBounce.CLIENT.commandManager.getClass();
            if (!func_146179_b2.startsWith(sb.append(String.valueOf('.')).append("lc").toString())) {
                this.field_146415_a.func_146203_f(10000);
                return;
            }
        }
        this.field_146415_a.func_146203_f(100);
    }
    
    @Inject(method = { "updateScreen" }, at = { @At("HEAD") })
    private void updateScreen(final CallbackInfo callbackInfo) {
        final int delta = RenderUtils.deltaTime;
        if (this.fade < 14.0f) {
            this.fade += 0.08f * delta;
        }
        if (this.fade > 14.0f) {
            this.fade = 14.0f;
        }
        if (this.yPosOfInputField > this.field_146295_m - 12) {
            this.yPosOfInputField -= 0.08f * delta;
        }
        if (this.yPosOfInputField < this.field_146295_m - 12) {
            this.yPosOfInputField = (float)(this.field_146295_m - 12);
        }
        this.field_146415_a.field_146210_g = (int)this.yPosOfInputField;
    }
    
    @Overwrite
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.func_73734_a(2, this.field_146295_m - (int)this.fade, this.field_146294_l - 2, this.field_146295_m, Integer.MIN_VALUE);
        this.field_146415_a.func_146194_f();
        final IChatComponent ichatcomponent = this.field_146297_k.field_71456_v.func_146158_b().func_146236_a(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null) {
            this.func_175272_a(ichatcomponent, mouseX, mouseY);
        }
    }
}
