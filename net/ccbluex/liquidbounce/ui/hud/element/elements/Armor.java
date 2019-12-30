// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.block.material.Material;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Armor extends Element
{
    private final Minecraft mc;
    
    public Armor() {
        this.mc = Minecraft.func_71410_x();
    }
    
    @Override
    public void drawElement() {
        final int[] location = this.getLocationFromFacing();
        if (this.mc.field_71442_b.func_78762_g()) {
            int x = location[0];
            GL11.glPushMatrix();
            for (int index = 3; index >= 0; --index) {
                final ItemStack stack = this.mc.field_71439_g.field_71071_by.field_70460_b[index];
                if (stack != null) {
                    this.mc.func_175599_af().func_175042_a(stack, x, location[1] - (this.mc.field_71439_g.func_70055_a(Material.field_151586_h) ? 10 : 0));
                    this.mc.func_175599_af().func_175030_a(this.mc.field_71466_p, stack, x, location[1] - (this.mc.field_71439_g.func_70055_a(Material.field_151586_h) ? 10 : 0));
                    x += 18;
                }
            }
            GlStateManager.func_179129_p();
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179140_f();
            GlStateManager.func_179129_p();
            GL11.glPopMatrix();
        }
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)location[0], (float)location[1], (float)(location[0] + 72), (float)(location[1] + 17), 3.0f, Integer.MIN_VALUE, 0);
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
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + 72 && mouseY <= location[1] + 10;
    }
}
