// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render;

import net.vitox.ParticleGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ParticleUtils
{
    private static final ParticleGenerator particleGenerator;
    
    public static void drawParticles(final int mouseX, final int mouseY) {
        ParticleUtils.particleGenerator.draw(mouseX, mouseY);
    }
    
    static {
        particleGenerator = new ParticleGenerator(100);
    }
}
