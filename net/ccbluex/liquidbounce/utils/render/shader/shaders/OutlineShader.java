// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import org.lwjgl.opengl.GL20;
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;

public final class OutlineShader extends FramebufferShader
{
    public static final OutlineShader OUTLINE_SHADER;
    
    public OutlineShader() {
        super("outline.frag");
    }
    
    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
    }
    
    @Override
    public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / this.mc.field_71443_c * (this.radius * this.quality), 1.0f / this.mc.field_71440_d * (this.radius * this.quality));
        GL20.glUniform4f(this.getUniform("color"), this.red, this.green, this.blue, this.alpha);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
    }
    
    static {
        OUTLINE_SHADER = new OutlineShader();
    }
}
