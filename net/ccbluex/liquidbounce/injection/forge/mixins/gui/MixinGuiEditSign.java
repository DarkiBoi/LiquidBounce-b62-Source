// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatAllowedCharacters;
import java.io.IOException;
import java.awt.Color;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.inventory.GuiEditSign;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiScreen;

@Mixin({ GuiEditSign.class })
public class MixinGuiEditSign extends GuiScreen
{
    @Shadow
    private int field_146851_h;
    @Shadow
    private TileEntitySign field_146848_f;
    @Shadow
    private GuiButton field_146852_i;
    private boolean enabled;
    private GuiButton toggleButton;
    private GuiTextField signCommand1;
    private GuiTextField signCommand2;
    private GuiTextField signCommand3;
    private GuiTextField signCommand4;
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void initGui(final CallbackInfo callbackInfo) {
        this.field_146292_n.add(this.toggleButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 145, this.enabled ? "Disable Formatting codes" : "Enable Formatting codes"));
        this.signCommand1 = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 15, 200, 10);
        this.signCommand2 = new GuiTextField(1, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 30, 200, 10);
        this.signCommand3 = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 45, 200, 10);
        this.signCommand4 = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 60, 200, 10);
        this.signCommand1.func_146180_a("");
        this.signCommand2.func_146180_a("");
        this.signCommand3.func_146180_a("");
        this.signCommand4.func_146180_a("");
    }
    
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") })
    private void actionPerformed(final GuiButton button, final CallbackInfo callbackInfo) {
        switch (button.field_146127_k) {
            case 0: {
                if (!this.signCommand1.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[0].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand1.func_146179_b())));
                }
                if (!this.signCommand2.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[1].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand2.func_146179_b())));
                }
                if (!this.signCommand3.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[2].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand3.func_146179_b())));
                }
                if (!this.signCommand4.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[3].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand4.func_146179_b())));
                    break;
                }
                break;
            }
            case 1: {
                this.enabled = !this.enabled;
                this.toggleButton.field_146126_j = (this.enabled ? "Disable Formatting codes" : "Enable Formatting codes");
                break;
            }
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    private void drawFields(final CallbackInfo callbackInfo) {
        this.field_146289_q.func_78276_b("§c§lCommands §7(§f§l1.8§7)", this.field_146294_l / 2 - 100, this.field_146295_m - 75, Color.WHITE.getRGB());
        this.signCommand1.func_146194_f();
        this.signCommand2.func_146194_f();
        this.signCommand3.func_146194_f();
        this.signCommand4.func_146194_f();
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.signCommand1.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand2.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand3.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand4.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    @Overwrite
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        this.signCommand1.func_146201_a(typedChar, keyCode);
        this.signCommand2.func_146201_a(typedChar, keyCode);
        this.signCommand3.func_146201_a(typedChar, keyCode);
        this.signCommand4.func_146201_a(typedChar, keyCode);
        if (this.signCommand1.func_146206_l() || this.signCommand2.func_146206_l() || this.signCommand3.func_146206_l() || this.signCommand4.func_146206_l()) {
            return;
        }
        if (keyCode == 200) {
            this.field_146851_h = (this.field_146851_h - 1 & 0x3);
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.field_146851_h = (this.field_146851_h + 1 & 0x3);
        }
        String s = this.field_146848_f.field_145915_a[this.field_146851_h].func_150260_c();
        if (keyCode == 14 && s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        if ((ChatAllowedCharacters.func_71566_a(typedChar) || (this.enabled && typedChar == '§')) && this.field_146289_q.func_78256_a(s + typedChar) <= 90) {
            s += typedChar;
        }
        this.field_146848_f.field_145915_a[this.field_146851_h] = (IChatComponent)new ChatComponentText(s);
        if (keyCode == 1) {
            this.func_146284_a(this.field_146852_i);
        }
    }
}
