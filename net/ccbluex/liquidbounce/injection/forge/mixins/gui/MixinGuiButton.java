// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiButton.class })
public abstract class MixinGuiButton
{
    @Shadow
    public boolean field_146125_m;
    @Shadow
    public int field_146128_h;
    @Shadow
    public int field_146129_i;
    @Shadow
    public int field_146120_f;
    @Shadow
    public int field_146121_g;
    @Shadow
    protected boolean field_146123_n;
    @Shadow
    public boolean field_146124_l;
    @Shadow
    public String field_146126_j;
    private float cut;
    private float alpha;
    
    @Shadow
    protected abstract void func_146119_b(final Minecraft p0, final int p1, final int p2);
    
    @Overwrite
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.field_146125_m) {
            FontRenderer fontRenderer = mc.field_71466_p;
            final String languageCode = mc.func_135016_M().func_135041_c().func_135034_a();
            if (languageCode.equalsIgnoreCase("de_DE") || languageCode.equalsIgnoreCase("en_US") || languageCode.equalsIgnoreCase("en_GB")) {
                fontRenderer = Fonts.font35;
            }
            this.field_146123_n = (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
            final int delta = RenderUtils.deltaTime;
            if (this.field_146124_l) {
                if (this.field_146123_n) {
                    this.alpha += 0.3f * delta;
                    if (this.alpha >= 210.0f) {
                        this.alpha = 210.0f;
                    }
                }
                else {
                    this.alpha -= 0.3f * delta;
                    if (this.alpha <= 120.0f) {
                        this.alpha = 120.0f;
                    }
                }
            }
            if (this.field_146123_n) {
                this.cut += 0.05f * delta;
                if (this.cut >= 4.0f) {
                    this.cut = 4.0f;
                }
            }
            else {
                this.cut -= 0.05f * delta;
                if (this.cut <= 0.0f) {
                    this.cut = 0.0f;
                }
            }
            if (this.field_146124_l) {
                Gui.func_73734_a(this.field_146128_h + (int)this.cut, this.field_146129_i, this.field_146128_h + this.field_146120_f - (int)this.cut, this.field_146129_i + this.field_146121_g, new Color(0.0f, 0.0f, 0.0f, this.alpha / 255.0f).getRGB());
            }
            else {
                Gui.func_73734_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, new Color(0.5f, 0.5f, 0.5f, 0.5f).getRGB());
            }
            this.func_146119_b(mc, mouseX, mouseY);
            fontRenderer.func_175063_a(this.field_146126_j, (float)(this.field_146128_h + this.field_146120_f / 2 - fontRenderer.func_78256_a(this.field_146126_j) / 2), (float)(this.field_146129_i + (this.field_146121_g - 5) / 2), 14737632);
            GlStateManager.func_179117_G();
        }
    }
}
