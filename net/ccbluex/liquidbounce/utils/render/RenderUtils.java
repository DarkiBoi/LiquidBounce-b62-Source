// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render;

import java.util.HashMap;
import java.math.BigDecimal;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.World;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.util.BlockPos;
import java.util.Map;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderUtils
{
    private static final Map<Integer, Boolean> glCapMap;
    public static int deltaTime;
    
    public static void drawBlockBox(final BlockPos blockPos, final Color color, final boolean outline) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        final Timer timer = Minecraft.func_71410_x().field_71428_T;
        final double x = blockPos.func_177958_n() - renderManager.field_78725_b;
        final double y = blockPos.func_177956_o() - renderManager.field_78726_c;
        final double z = blockPos.func_177952_p() - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            final double d0 = Minecraft.func_71410_x().field_71439_g.field_70142_S + (Minecraft.func_71410_x().field_71439_g.field_70165_t - Minecraft.func_71410_x().field_71439_g.field_70142_S) * timer.field_74281_c;
            final double d2 = Minecraft.func_71410_x().field_71439_g.field_70137_T + (Minecraft.func_71410_x().field_71439_g.field_70163_u - Minecraft.func_71410_x().field_71439_g.field_70137_T) * timer.field_74281_c;
            final double d3 = Minecraft.func_71410_x().field_71439_g.field_70136_U + (Minecraft.func_71410_x().field_71439_g.field_70161_v - Minecraft.func_71410_x().field_71439_g.field_70136_U) * timer.field_74281_c;
            axisAlignedBB = block.func_180646_a((World)Minecraft.func_71410_x().field_71441_e, blockPos).func_72314_b(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).func_72317_d(-d0, -d2, -d3);
        }
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        GL11.glLineWidth(1.0f);
        GL11.glColor4d(0.0, 1.0, 0.0, 0.15000000596046448);
        disableGlCap(3553, 2929);
        GL11.glDepthMask(false);
        glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (color.getAlpha() == 255) ? (outline ? 26 : 35) : color.getAlpha()));
        drawFilledBox(axisAlignedBB);
        if (outline) {
            glColor(color);
            RenderGlobal.func_181561_a(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask(true);
        resetCaps();
    }
    
    public static void drawEntityBox(final Entity entity, final Color color, final boolean outline) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(3553, 2929);
        GL11.glLineWidth(1.0f);
        GL11.glDepthMask(false);
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + (entity.field_70165_t - renderManager.field_78725_b), entity.func_174813_aQ().field_72338_b - entity.field_70163_u + (entity.field_70163_u - renderManager.field_78726_c), entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + (entity.field_70161_v - renderManager.field_78723_d), entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + (entity.field_70165_t - renderManager.field_78725_b), entity.func_174813_aQ().field_72337_e + 0.15 - entity.field_70163_u + (entity.field_70163_u - renderManager.field_78726_c), entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + (entity.field_70161_v - renderManager.field_78723_d));
        if (outline) {
            glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 95));
            RenderGlobal.func_181561_a(axisAlignedBB);
        }
        glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35));
        drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask(true);
        resetCaps();
    }
    
    public static void drawTracer(final Entity entity, final Color color) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        final double x = entity.field_70165_t - renderManager.field_78725_b;
        final double y = entity.field_70163_u - renderManager.field_78726_c;
        final double z = entity.field_70161_v - renderManager.field_78723_d;
        final Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0).func_178789_a(-(float)Math.toRadians(Minecraft.func_71410_x().field_71439_g.field_70125_A)).func_178785_b(-(float)Math.toRadians(Minecraft.func_71410_x().field_71439_g.field_70177_z));
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        glColor(color);
        GL11.glBegin(1);
        GL11.glVertex3d(eyeVector.field_72450_a, Minecraft.func_71410_x().field_71439_g.func_70047_e() + eyeVector.field_72448_b, eyeVector.field_72449_c);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + entity.field_70131_O, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.func_179117_G();
    }
    
    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (color != null) {
            glColor(color);
        }
        drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawPlatform(final double x, final double y, final double z, final Color color, final double size) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        final double renderX = x - renderManager.field_78725_b;
        final double renderY = y - renderManager.field_78726_c;
        final double renderZ = z - renderManager.field_78723_d;
        drawAxisAlignedBB(new AxisAlignedBB(renderX + size, renderY + 0.02, renderZ + size, renderX - size, renderY, renderZ - size), color);
    }
    
    public static void drawPlatform(final Entity entity, final Color color) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        drawAxisAlignedBB(new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + (entity.field_70165_t - renderManager.field_78725_b), entity.func_174813_aQ().field_72337_e - renderManager.field_78726_c + 0.2, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + (entity.field_70161_v - renderManager.field_78723_d), entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + (entity.field_70165_t - renderManager.field_78725_b), entity.func_174813_aQ().field_72337_e - renderManager.field_78726_c + 0.26, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + (entity.field_70161_v - renderManager.field_78723_d)), color);
    }
    
    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public static void drawRect(final float x, final float y, final float x2, final float y2, final int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawRect(final float x, final float y, final float x2, final float y2, final Color color) {
        drawRect(x, y, x2, y2, color.getRGB());
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float width, final int color1, final int color2) {
        drawRect(x, y, x2, y2, color2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(color1);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawLoadingCircle(final float x, final float y) {
        for (int i = 0; i < 4; ++i) {
            final int rot = (int)(System.nanoTime() / 5000000L * i % 360L);
            drawCircle(x, y, (float)(i * 10), rot - 180, rot);
        }
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final int start, final int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        glColor(Color.WHITE);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = (float)end; i >= start; i -= 4.0f) {
            GL11.glVertex2f((float)(x + Math.cos(i * 3.141592653589793 / 180.0) * (radius * 1.001f)), (float)(y + Math.sin(i * 3.141592653589793 / 180.0) * (radius * 1.001f)));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawFilledCircle(final int xx, final int yy, final float radius, final Color col) {
        final int sections = 50;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x = (float)(radius * Math.sin(i * dAngle));
            final float y = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(col.getRed() / 255.0f, col.getGreen() / 255.0f, col.getBlue() / 255.0f, col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.func_179124_c(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image);
        Gui.func_146110_a(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    private static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void resetCaps() {
        RenderUtils.glCapMap.forEach(RenderUtils::setGlState);
    }
    
    public static void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }
    
    public static void enableGlCap(final int... caps) {
        for (final int cap : caps) {
            setGlCap(cap, true);
        }
    }
    
    public static void disableGlCap(final int cap) {
        setGlCap(cap, true);
    }
    
    public static void disableGlCap(final int... caps) {
        for (final int cap : caps) {
            setGlCap(cap, false);
        }
    }
    
    public static void setGlCap(final int cap, final boolean state) {
        RenderUtils.glCapMap.put(cap, GL11.glGetBoolean(cap));
        setGlState(cap, state);
    }
    
    public static void setGlState(final int cap, final boolean state) {
        if (state) {
            GL11.glEnable(cap);
        }
        else {
            GL11.glDisable(cap);
        }
    }
    
    public static void draw2D(final EntityLivingBase entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.func_179114_b(-Minecraft.func_71410_x().func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179139_a(-0.1, -0.1, 0.1);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a(true);
        drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.func_179137_b(0.0, 21.0 + -(entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 12.0, 0.0);
        drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        GL11.glEnable(2929);
        GlStateManager.func_179121_F();
    }
    
    public static void draw2D(final BlockPos blockPos, final int color, final int backgroundColor) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        final double posX = blockPos.func_177958_n() + 0.5 - renderManager.field_78725_b;
        final double posY = blockPos.func_177956_o() - renderManager.field_78726_c;
        final double posZ = blockPos.func_177952_p() + 0.5 - renderManager.field_78723_d;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.func_179114_b(-Minecraft.func_71410_x().func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179139_a(-0.1, -0.1, 0.1);
        setGlCap(2929, false);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a(true);
        drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.func_179109_b(0.0f, 9.0f, 0.0f);
        drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        resetCaps();
        GlStateManager.func_179121_F();
    }
    
    public static void renderNameTag(final String string, final double x, final double y, final double z) {
        final RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        GL11.glPushMatrix();
        GL11.glTranslated(x - renderManager.field_78725_b, y - renderManager.field_78726_c, z - renderManager.field_78723_d);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-Minecraft.func_71410_x().func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(Minecraft.func_71410_x().func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-0.05f, -0.05f, 0.05f);
        setGlCap(2896, false);
        setGlCap(2929, false);
        setGlCap(3042, true);
        GL11.glBlendFunc(770, 771);
        final int width = Fonts.font35.func_78256_a(string) / 2;
        Gui.func_73734_a(-width - 1, -1, width + 1, Fonts.font35.field_78288_b, Integer.MIN_VALUE);
        Fonts.font35.func_175065_a(string, (float)(-width), 1.5f, Color.WHITE.getRGB(), true);
        resetCaps();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public static void makeScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        final int factor = scaledResolution.func_78325_e();
        GL11.glScissor((int)(x * factor), (int)((scaledResolution.func_78328_b() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public static float drawSlider(final float value, final float min, final float max, final int x, final int y, final int width, final int mouseX, final int mouseY, final Color color) {
        final float displayValue = Math.max(min, Math.min(value, max));
        final float sliderValue = x + width * (displayValue - min) / (max - min);
        drawRect((float)x, (float)y, (float)(x + width), (float)(y + 2), Integer.MAX_VALUE);
        drawRect((float)x, (float)y, sliderValue, (float)(y + 2), color);
        drawFilledCircle((int)sliderValue, y + 1, 3.0f, color);
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 3 && Mouse.isButtonDown(0)) {
            final double i = MathHelper.func_151237_a((mouseX - (double)x) / (width - 3.0), 0.0, 1.0);
            BigDecimal bigDecimal = new BigDecimal(Double.toString(min + (max - min) * i));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }
        return value;
    }
    
    static {
        glCapMap = new HashMap<Integer, Boolean>();
    }
}
