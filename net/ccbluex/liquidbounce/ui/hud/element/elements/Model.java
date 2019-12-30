// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Model extends Element
{
    private float rotate;
    private boolean rotateDirection;
    
    @Override
    public void drawElement() {
        final int delta = RenderUtils.deltaTime;
        if (this.rotateDirection) {
            if (this.rotate <= 70.0f) {
                this.rotate += 0.12f * delta;
            }
            else {
                this.rotateDirection = false;
                this.rotate = 70.0f;
            }
        }
        else if (this.rotate >= -70.0f) {
            this.rotate -= 0.12f * delta;
        }
        else {
            this.rotateDirection = true;
            this.rotate = -70.0f;
        }
        final int[] location = this.getLocationFromFacing();
        final EntityPlayerSP entityPlayerSP = Minecraft.func_71410_x().field_71439_g;
        float pitch = entityPlayerSP.field_70125_A;
        if (pitch > 0.0f) {
            pitch = -entityPlayerSP.field_70125_A;
        }
        else {
            pitch = Math.abs(entityPlayerSP.field_70125_A);
        }
        this.drawEntityOnScreen(location[0], location[1], this.rotate, pitch, (EntityLivingBase)entityPlayerSP);
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] + 30), (float)(location[1] + 10), (float)(location[0] - 30), (float)(location[1] - 100), 3.0f, Integer.MIN_VALUE, 0);
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
        return mouseX >= location[0] - 30 && mouseY >= location[1] - 100 && mouseX <= location[0] + 30 && mouseY <= location[1] + 10;
    }
    
    private void drawEntityOnScreen(final int x, final int y, final float yaw, final float pitch, final EntityLivingBase entityLivingBase) {
        GlStateManager.func_179117_G();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)x, (float)y, 50.0f);
        GlStateManager.func_179152_a(-50.0f, 50.0f, 50.0f);
        GlStateManager.func_179114_b(180.0f, 0.0f, 0.0f, 1.0f);
        final float var6 = entityLivingBase.field_70761_aq;
        final float var7 = entityLivingBase.field_70177_z;
        final float var8 = entityLivingBase.field_70125_A;
        final float var9 = entityLivingBase.field_70758_at;
        final float var10 = entityLivingBase.field_70759_as;
        GlStateManager.func_179114_b(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(-(float)Math.atan(pitch / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        entityLivingBase.field_70761_aq = (float)Math.atan(yaw / 40.0f) * 20.0f;
        entityLivingBase.field_70177_z = (float)Math.atan(yaw / 40.0f) * 40.0f;
        entityLivingBase.field_70125_A = -(float)Math.atan(pitch / 40.0f) * 20.0f;
        entityLivingBase.field_70759_as = entityLivingBase.field_70177_z;
        entityLivingBase.field_70758_at = entityLivingBase.field_70177_z;
        GlStateManager.func_179109_b(0.0f, 0.0f, 0.0f);
        final RenderManager var11 = Minecraft.func_71410_x().func_175598_ae();
        var11.func_178631_a(180.0f);
        var11.func_178633_a(false);
        var11.func_147940_a((Entity)entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.func_178633_a(true);
        entityLivingBase.field_70761_aq = var6;
        entityLivingBase.field_70177_z = var7;
        entityLivingBase.field_70125_A = var8;
        entityLivingBase.field_70758_at = var9;
        entityLivingBase.field_70759_as = var10;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
        GlStateManager.func_179117_G();
    }
}
