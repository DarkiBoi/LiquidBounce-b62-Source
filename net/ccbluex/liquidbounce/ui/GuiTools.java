// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.tools.GuiPortScanner;
import net.ccbluex.liquidbounce.ui.tools.GuiGUIControlBruteforce;
import net.ccbluex.liquidbounce.ui.tools.GuiRconBruteforce;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiTools extends GuiScreen
{
    private final GuiScreen prevGui;
    
    public GuiTools(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48, "Bruteforce - RCON"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 25, "Bruteforce - GUIControl"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 50, "Port Scanner"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 75 + 5, "Back"));
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiRconBruteforce(this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiGUIControlBruteforce(this));
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiPortScanner(this));
                break;
            }
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Fonts.fontBold180.drawCenteredString("Tools", (int)(this.field_146294_l / 2.0f), (int)(this.field_146295_m / 8.0f + 5.0f), 4673984, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
