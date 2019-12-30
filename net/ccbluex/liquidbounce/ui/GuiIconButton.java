// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiIconButton extends GuiButton
{
    private float hover;
    private final ResourceLocation resourceLocation;
    
    public GuiIconButton(final int buttonId, final int x, final int y, final int width, final int height, final String buttonText, final ResourceLocation resourceLocation) {
        super(buttonId, x, y, width, height, buttonText);
        this.resourceLocation = resourceLocation;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.field_146125_m) {
            this.field_146123_n = (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i - this.hover && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
            final int delta = RenderUtils.deltaTime;
            if (this.field_146123_n) {
                this.hover += 0.03f * delta;
                if (this.hover >= 5.0f) {
                    this.hover = 5.0f;
                }
            }
            else {
                this.hover -= 0.03f * delta;
                if (this.hover < 0.0f) {
                    this.hover = 0.0f;
                }
            }
            RenderUtils.drawImage(this.resourceLocation, this.field_146128_h, this.field_146129_i - (int)this.hover, this.field_146120_f, this.field_146121_g);
            this.func_146119_b(mc, mouseX, mouseY);
            this.func_73732_a((FontRenderer)Fonts.font35, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + this.field_146121_g + 7 - (int)this.hover, Color.WHITE.getRGB());
        }
    }
}
