// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Effects extends Element
{
    private FontRenderer fontRenderer;
    private final BoolValue shadow;
    private int x2;
    private int y2;
    
    public Effects() {
        this.fontRenderer = Fonts.font35;
        this.shadow = new BoolValue("Shadow", true);
    }
    
    @Override
    public void drawElement() {
        final int[] location = this.getLocationFromFacing();
        int y = location[1];
        int width = 0;
        for (final PotionEffect effect : Minecraft.func_71410_x().field_71439_g.func_70651_bq()) {
            final Potion potion = Potion.field_76425_a[effect.func_76456_a()];
            String name = I18n.func_135052_a(potion.func_76393_a(), new Object[0]);
            if (effect.func_76458_c() == 1) {
                name += " II";
            }
            else if (effect.func_76458_c() == 2) {
                name += " III";
            }
            else if (effect.func_76458_c() == 3) {
                name += " IV";
            }
            else if (effect.func_76458_c() == 4) {
                name += " V";
            }
            else if (effect.func_76458_c() == 5) {
                name += " VI";
            }
            else if (effect.func_76458_c() == 6) {
                name += " VII";
            }
            else if (effect.func_76458_c() == 7) {
                name += " VIII";
            }
            else if (effect.func_76458_c() == 8) {
                name += " IX";
            }
            else if (effect.func_76458_c() == 9) {
                name += " X";
            }
            else if (effect.func_76458_c() > 10) {
                name += " X+";
            }
            else {
                name += " I";
            }
            name = name + "§f: §7" + Potion.func_76389_a(effect);
            if (width < this.fontRenderer.func_78256_a(name)) {
                width = this.fontRenderer.func_78256_a(name);
            }
            this.fontRenderer.func_175065_a(name, (float)(location[0] - this.fontRenderer.func_78256_a(name)), (float)y, potion.func_76401_j(), this.shadow.asBoolean());
            y -= this.fontRenderer.field_78288_b;
        }
        if (width == 0) {
            width = 40;
        }
        if (location[1] == y) {
            y = location[1] - 10;
        }
        this.y2 = y + this.fontRenderer.field_78288_b - 2;
        this.x2 = location[0] - width;
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] + 2), (float)(location[1] + this.fontRenderer.field_78288_b), (float)(this.x2 - 2), (float)this.y2, 3.0f, Integer.MIN_VALUE, 0);
        }
    }
    
    @Override
    public void destroyElement() {
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void handleKey(final char c, final int keyCode) {
    }
    
    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        return mouseX <= location[0] + 2 && mouseY <= location[1] + this.fontRenderer.field_78288_b && mouseX >= this.x2 - 2 && mouseY >= this.y2;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public Effects setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
    
    public boolean isShadow() {
        return this.shadow.asBoolean();
    }
    
    public Effects setShadow(final boolean b) {
        this.shadow.setValue(b);
        return this;
    }
}
