// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL20;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;

public final class BackgroundShader extends Shader
{
    public static final BackgroundShader BACKGROUND_SHADER;
    private float time;
    
    public BackgroundShader() {
        super("background.frag");
    }
    
    @Override
    public void setupUniforms() {
        this.setupUniform("iResolution");
        this.setupUniform("iTime");
    }
    
    @Override
    public void updateUniforms() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        final int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform3f(resolutionID, (float)scaledResolution.func_78326_a(), (float)scaledResolution.func_78328_b(), scaledResolution.func_78326_a() + (float)scaledResolution.func_78326_a());
        }
        final int timeID = this.getUniform("iTime");
        if (timeID > -1) {
            GL20.glUniform1f(timeID, this.time);
        }
        this.time += 0.005f * RenderUtils.deltaTime;
    }
    
    static {
        BACKGROUND_SHADER = new BackgroundShader();
    }
}
