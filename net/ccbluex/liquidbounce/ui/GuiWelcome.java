// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu;
import java.io.IOException;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWelcome extends GuiScreen
{
    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m - 40, "Ok"));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        Fonts.font35.drawCenteredString("Thank you for downloading and installing our client!", this.field_146294_l / 2, this.field_146295_m / 8 + 70, 16777215, true);
        Fonts.font35.drawCenteredString("Here is some information you might need if you are using liquidbounce for the first time.", this.field_146294_l / 2, this.field_146295_m / 8 + 70 + Fonts.font35.field_78288_b, 16777215, true);
        Fonts.font35.drawCenteredString("§lClickGUI:", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 3, 16777215, true);
        Fonts.font35.drawCenteredString("Press " + Keyboard.getKeyName(ModuleManager.getModule(ClickGUI.class).getKeyBind()) + " to open up the ClickGUI", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 4, 16777215, true);
        Fonts.font35.drawCenteredString("Rightclick modules with a little + next to it to edit it's settings.", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 5, 16777215, true);
        Fonts.font35.drawCenteredString("Hover a module to see what it does.", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 6, 16777215, true);
        Fonts.font35.drawCenteredString("§lImportant Commands:", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 8, 16777215, true);
        Fonts.font35.drawCenteredString(".bind <module> <key> / .bind <module> none", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 9, 16777215, true);
        Fonts.font35.drawCenteredString(".autosettings <servername>", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 10, 16777215, true);
        Fonts.font35.drawCenteredString("§lNeed help? Feel free to contact us!", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 12, 16777215, true);
        Fonts.font35.drawCenteredString("YouTube: https://youtube.com/ccbluex", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 13, 16777215, true);
        Fonts.font35.drawCenteredString("Twitter: https://twitter.com/ccbluex", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 14, 16777215, true);
        Fonts.font35.drawCenteredString("Forum: https://forum.ccbluex.net/", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b * 15, 16777215, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        Fonts.font40.drawCenteredString("Welcome!", this.field_146294_l / 2 / 2, this.field_146295_m / 8 / 2 + 20, new Color(0, 140, 255).getRGB(), true);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMainMenu());
                break;
            }
        }
    }
}
