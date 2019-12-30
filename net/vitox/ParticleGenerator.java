// 
// Decompiled by Procyon v0.5.36
// 

package net.vitox;

import java.util.Random;
import java.util.Iterator;
import net.vitox.particle.util.RenderUtils;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleGenerator
{
    private final List<Particle> particles;
    private final int amount;
    private int prevWidth;
    private int prevHeight;
    
    public ParticleGenerator(final int amount) {
        this.particles = new ArrayList<Particle>();
        this.amount = amount;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.func_71410_x().field_71443_c || this.prevHeight != Minecraft.func_71410_x().field_71440_d) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.func_71410_x().field_71443_c;
        this.prevHeight = Minecraft.func_71410_x().field_71440_d;
        for (final Particle particle : this.particles) {
            particle.fall();
            particle.interpolation();
            final int range = 50;
            final boolean mouseOver = mouseX >= particle.x - range && mouseY >= particle.y - range && mouseX <= particle.x + range && mouseY <= particle.y + range;
            if (mouseOver) {
                final Particle particle2;
                final int n;
                this.particles.stream().filter(part -> part.getX() > particle2.getX() && part.getX() - particle2.getX() < n && particle2.getX() - part.getX() < n && ((part.getY() > particle2.getY() && part.getY() - particle2.getY() < n) || (particle2.getY() > part.getY() && particle2.getY() - part.getY() < n))).forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size, -1);
        }
    }
    
    private void create() {
        final Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.func_71410_x().field_71443_c), random.nextInt(Minecraft.func_71410_x().field_71440_d)));
        }
    }
}
