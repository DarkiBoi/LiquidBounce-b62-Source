// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.particle.EntityParticleEmitter;
import java.util.List;
import net.minecraft.client.particle.EffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ EffectRenderer.class })
public abstract class MixinEffectRenderer
{
    @Shadow
    private List<EntityParticleEmitter> field_178933_d;
    
    @Shadow
    protected abstract void func_178922_a(final int p0);
    
    @Overwrite
    public void func_78868_a() {
        try {
            for (int i = 0; i < 4; ++i) {
                this.func_178922_a(i);
            }
            final Iterator<EntityParticleEmitter> it = this.field_178933_d.iterator();
            while (it.hasNext()) {
                final EntityParticleEmitter entityParticleEmitter = it.next();
                entityParticleEmitter.func_70071_h_();
                if (entityParticleEmitter.field_70128_L) {
                    it.remove();
                }
            }
        }
        catch (ConcurrentModificationException ex) {}
    }
}
