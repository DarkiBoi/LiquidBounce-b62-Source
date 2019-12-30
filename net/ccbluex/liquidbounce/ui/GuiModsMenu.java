// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.script.GuiScripts;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiModsMenu extends GuiScreen
{
    private final GuiScreen prevGui;
    
    public GuiModsMenu(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48, "Forge Mods"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 25, "Scripts"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 50, "Back"));
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiModList((GuiScreen)this));
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiScripts(this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Fonts.fontBold180.drawCenteredString("Mods", (int)(this.field_146294_l / 2.0f), (int)(this.field_146295_m / 8.0f + 5.0f), 4673984, true);
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
