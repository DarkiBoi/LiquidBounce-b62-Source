// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiUpdate extends GuiScreen
{
    public void func_73866_w_() {
        final int j = this.field_146295_m / 4 + 48;
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 2, j + 48, 98, 20, "OK"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, j + 48, 98, 20, "Download"));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146278_c(0);
        this.func_73732_a((FontRenderer)Fonts.font35, "b" + LiquidBounce.CLIENT.latestVersion + " got released!", this.field_146294_l / 2, this.field_146295_m / 8 + 80, 16777215);
        this.func_73732_a((FontRenderer)Fonts.font35, "Press \"Download\" to visit our website or dismiss this message by pressing \"OK\".", this.field_146294_l / 2, this.field_146295_m / 8 + 80 + Fonts.font35.field_78288_b, 16777215);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        this.func_73732_a((FontRenderer)Fonts.font35, "New update available!", this.field_146294_l / 2 / 2, this.field_146295_m / 8 / 2 + 20, new Color(255, 0, 0).getRGB());
    }
    
    protected void func_146284_a(final GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMainMenu());
                break;
            }
            case 2: {
                MiscUtils.showURL("https://liquidbounce.net/#download");
                break;
            }
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
