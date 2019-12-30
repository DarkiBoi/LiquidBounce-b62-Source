// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager.sub;

import java.io.IOException;
import net.minecraft.util.Session;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiChangeName extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField name;
    private String status;
    
    public GuiChangeName(final GuiScreen gui) {
        this.prevGui = gui;
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Change"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        (this.name = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20)).func_146195_b(true);
        this.name.func_146180_a(this.field_146297_k.func_110432_I().func_111285_a());
        this.name.func_146203_f(Integer.MAX_VALUE);
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a(30, 30, this.field_146294_l - 30, this.field_146295_m - 30, Integer.MIN_VALUE);
        this.func_73732_a(this.field_146297_k.field_71466_p, "Change Name", this.field_146294_l / 2, 34, 16777215);
        this.func_73732_a(this.field_146297_k.field_71466_p, (this.status == null) ? "" : this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 86, 16777215);
        this.name.func_146194_f();
        if (this.name.func_146179_b().isEmpty()) {
            this.func_73732_a(this.field_146289_q, "§7Username", this.field_146294_l / 2 - 50, 65, 16777215);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                if (this.name.func_146179_b().isEmpty()) {
                    this.status = "§c§lYou need to write a name.";
                    return;
                }
                if (!this.name.func_146179_b().equalsIgnoreCase(this.field_146297_k.func_110432_I().func_111285_a())) {
                    this.status = "§c§lThe new name must look like the old one";
                    return;
                }
                this.field_146297_k.field_71449_j = new Session(this.name.func_146179_b(), this.field_146297_k.func_110432_I().func_148255_b(), this.field_146297_k.func_110432_I().func_148254_d(), this.field_146297_k.func_110432_I().func_152428_f().name());
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
        super.func_146284_a(button);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        if (this.name.func_146206_l()) {
            this.name.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.name.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        super.func_146281_b();
    }
}
