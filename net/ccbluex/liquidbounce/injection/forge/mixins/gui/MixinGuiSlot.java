// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiSlot.class })
public abstract class MixinGuiSlot
{
    @Shadow
    protected boolean field_178041_q;
    @Shadow
    protected int field_148150_g;
    @Shadow
    protected int field_148162_h;
    @Shadow
    public int field_148152_e;
    @Shadow
    public int field_148153_b;
    @Shadow
    public int field_148155_a;
    @Shadow
    protected float field_148169_q;
    @Shadow
    protected boolean field_148165_u;
    @Shadow
    public int field_148151_d;
    @Shadow
    public int field_148154_c;
    @Shadow
    @Final
    protected Minecraft field_148161_k;
    @Shadow
    public int field_148158_l;
    
    @Shadow
    protected abstract void func_148123_a();
    
    @Shadow
    protected abstract void func_148121_k();
    
    @Shadow
    public abstract int func_148139_c();
    
    @Shadow
    protected abstract void func_148129_a(final int p0, final int p1, final Tessellator p2);
    
    @Shadow
    protected abstract void func_148120_b(final int p0, final int p1, final int p2, final int p3);
    
    @Shadow
    protected abstract int func_148138_e();
    
    @Shadow
    public abstract int func_148135_f();
    
    @Shadow
    protected abstract void func_148142_b(final int p0, final int p1);
    
    @Overwrite
    public void func_148128_a(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        if (this.field_178041_q) {
            this.field_148150_g = mouseXIn;
            this.field_148162_h = mouseYIn;
            this.func_148123_a();
            final int i = this.func_148137_d();
            final int j = i + 6;
            this.func_148121_k();
            GlStateManager.func_179140_f();
            GlStateManager.func_179106_n();
            final Tessellator tessellator = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer = tessellator.func_178180_c();
            final int k = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2 + 2;
            final int l = this.field_148153_b + 4 - (int)this.field_148169_q;
            if (this.field_148165_u) {
                this.func_148129_a(k, l, tessellator);
            }
            this.func_148120_b(k, l + 2, mouseXIn, mouseYIn + 2);
            GlStateManager.func_179097_i();
            final int i2 = 4;
            final ScaledResolution scaledResolution = new ScaledResolution(this.field_148161_k);
            Gui.func_73734_a(0, 0, scaledResolution.func_78326_a(), this.field_148153_b, Integer.MIN_VALUE);
            Gui.func_73734_a(0, this.field_148154_c, scaledResolution.func_78326_a(), this.field_148158_l, Integer.MIN_VALUE);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 0, 1);
            GlStateManager.func_179118_c();
            GlStateManager.func_179103_j(7425);
            GlStateManager.func_179090_x();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)(this.field_148153_b + i2), 0.0).func_181673_a(0.0, 1.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)(this.field_148153_b + i2), 0.0).func_181673_a(1.0, 1.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148153_b, 0.0).func_181673_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148153_b, 0.0).func_181673_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            tessellator.func_78381_a();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148154_c, 0.0).func_181673_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148154_c, 0.0).func_181673_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)(this.field_148154_c - i2), 0.0).func_181673_a(1.0, 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)(this.field_148154_c - i2), 0.0).func_181673_a(0.0, 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
            tessellator.func_78381_a();
            final int j2 = this.func_148135_f();
            if (j2 > 0) {
                int k2 = (this.field_148154_c - this.field_148153_b) * (this.field_148154_c - this.field_148153_b) / this.func_148138_e();
                k2 = MathHelper.func_76125_a(k2, 32, this.field_148154_c - this.field_148153_b - 8);
                int l2 = (int)this.field_148169_q * (this.field_148154_c - this.field_148153_b - k2) / j2 + this.field_148153_b;
                if (l2 < this.field_148153_b) {
                    l2 = this.field_148153_b;
                }
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                worldrenderer.func_181662_b((double)i, (double)this.field_148154_c, 0.0).func_181673_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                worldrenderer.func_181662_b((double)j, (double)this.field_148154_c, 0.0).func_181673_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                worldrenderer.func_181662_b((double)j, (double)this.field_148153_b, 0.0).func_181673_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                worldrenderer.func_181662_b((double)i, (double)this.field_148153_b, 0.0).func_181673_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
                tessellator.func_78381_a();
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                worldrenderer.func_181662_b((double)i, (double)(l2 + k2), 0.0).func_181673_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                worldrenderer.func_181662_b((double)j, (double)(l2 + k2), 0.0).func_181673_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                worldrenderer.func_181662_b((double)j, (double)l2, 0.0).func_181673_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                worldrenderer.func_181662_b((double)i, (double)l2, 0.0).func_181673_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
                tessellator.func_78381_a();
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                worldrenderer.func_181662_b((double)i, (double)(l2 + k2 - 1), 0.0).func_181673_a(0.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                worldrenderer.func_181662_b((double)(j - 1), (double)(l2 + k2 - 1), 0.0).func_181673_a(1.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                worldrenderer.func_181662_b((double)(j - 1), (double)l2, 0.0).func_181673_a(1.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                worldrenderer.func_181662_b((double)i, (double)l2, 0.0).func_181673_a(0.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
                tessellator.func_78381_a();
            }
            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.func_179098_w();
            GlStateManager.func_179103_j(7424);
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
        }
    }
    
    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }
}
