// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render.shader;

import org.lwjgl.opengl.GL20;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.Minecraft;

public abstract class FramebufferShader extends Shader
{
    protected Minecraft mc;
    private Framebuffer framebuffer;
    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;
    protected float radius;
    protected float quality;
    private boolean entityShadows;
    
    public FramebufferShader(final String fragmentShader) {
        super(fragmentShader);
        this.mc = Minecraft.func_71410_x();
        this.alpha = 1.0f;
        this.radius = 2.0f;
        this.quality = 1.0f;
    }
    
    public void startDraw(final float partialTicks) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179094_E();
        GlStateManager.func_179123_a();
        (this.framebuffer = this.setupFrameBuffer(this.framebuffer)).func_147614_f();
        this.framebuffer.func_147610_a(true);
        this.entityShadows = this.mc.field_71474_y.field_181151_V;
        this.mc.field_71474_y.field_181151_V = false;
        this.mc.field_71460_t.func_78479_a(partialTicks, 0);
    }
    
    public void stopDraw(final Color color, final float radius, final float quality) {
        this.mc.field_71474_y.field_181151_V = this.entityShadows;
        this.framebuffer.func_147609_e();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.func_147110_a().func_147610_a(true);
        this.red = color.getRed() / 255.0f;
        this.green = color.getGreen() / 255.0f;
        this.blue = color.getBlue() / 255.0f;
        this.alpha = color.getAlpha() / 255.0f;
        this.radius = radius;
        this.quality = quality;
        this.mc.field_71460_t.func_175072_h();
        RenderHelper.func_74518_a();
        this.startShader();
        this.mc.field_71460_t.func_78478_c();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.field_71460_t.func_175072_h();
        GlStateManager.func_179121_F();
        GlStateManager.func_179099_b();
    }
    
    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (frameBuffer == null) {
            return new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
        }
        if (frameBuffer.field_147621_c != this.mc.field_71443_c || frameBuffer.field_147618_d != this.mc.field_71440_d) {
            frameBuffer.func_147608_a();
            frameBuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
        }
        return frameBuffer;
    }
    
    public void drawFramebuffer(final Framebuffer framebuffer) {
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GL11.glBindTexture(3553, framebuffer.field_147617_g);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, (double)scaledResolution.func_78328_b());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d((double)scaledResolution.func_78326_a(), (double)scaledResolution.func_78328_b());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d((double)scaledResolution.func_78326_a(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
}
