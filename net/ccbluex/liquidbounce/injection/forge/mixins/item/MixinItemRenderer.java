// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.RenderHelper;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemMap;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Shadow
    private float field_78451_d;
    @Shadow
    private float field_78454_c;
    @Shadow
    @Final
    private Minecraft field_78455_a;
    @Shadow
    private ItemStack field_78453_b;
    
    @Shadow
    protected abstract void func_178101_a(final float p0, final float p1);
    
    @Shadow
    protected abstract void func_178109_a(final AbstractClientPlayer p0);
    
    @Shadow
    protected abstract void func_178110_a(final EntityPlayerSP p0, final float p1);
    
    @Shadow
    protected abstract void func_178097_a(final AbstractClientPlayer p0, final float p1, final float p2, final float p3);
    
    @Shadow
    protected abstract void func_178096_b(final float p0, final float p1);
    
    @Shadow
    protected abstract void func_178104_a(final AbstractClientPlayer p0, final float p1);
    
    @Shadow
    protected abstract void func_178103_d();
    
    @Shadow
    protected abstract void func_178098_a(final float p0, final AbstractClientPlayer p1);
    
    @Shadow
    protected abstract void func_178105_d(final float p0);
    
    @Shadow
    public abstract void func_178099_a(final EntityLivingBase p0, final ItemStack p1, final ItemCameraTransforms.TransformType p2);
    
    @Shadow
    protected abstract void func_178095_a(final AbstractClientPlayer p0, final float p1, final float p2);
    
    @Overwrite
    public void func_78440_a(final float partialTicks) {
        final float f = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
        final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.field_78455_a.field_71439_g;
        final float f2 = abstractclientplayer.func_70678_g(partialTicks);
        final float f3 = abstractclientplayer.field_70127_C + (abstractclientplayer.field_70125_A - abstractclientplayer.field_70127_C) * partialTicks;
        final float f4 = abstractclientplayer.field_70126_B + (abstractclientplayer.field_70177_z - abstractclientplayer.field_70126_B) * partialTicks;
        this.func_178101_a(f3, f4);
        this.func_178109_a(abstractclientplayer);
        this.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.func_179091_B();
        GlStateManager.func_179094_E();
        if (this.field_78453_b != null) {
            final KillAura killAura = (KillAura)ModuleManager.getModule(KillAura.class);
            if (this.field_78453_b.func_77973_b() instanceof ItemMap) {
                this.func_178097_a(abstractclientplayer, f3, f, f2);
            }
            else if (abstractclientplayer.func_71052_bv() > 0 || (this.field_78453_b.func_77973_b() instanceof ItemSword && killAura.blocking)) {
                final EnumAction enumaction = killAura.blocking ? EnumAction.BLOCK : this.field_78453_b.func_77975_n();
                switch (enumaction) {
                    case NONE: {
                        this.func_178096_b(f, 0.0f);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.func_178104_a(abstractclientplayer, partialTicks);
                        this.func_178096_b(f, f2);
                        break;
                    }
                    case BLOCK: {
                        this.func_178096_b(f + 0.1f, f2);
                        this.func_178103_d();
                        GlStateManager.func_179109_b(-0.5f, 0.2f, 0.0f);
                        break;
                    }
                    case BOW: {
                        this.func_178096_b(f, f2);
                        this.func_178098_a(partialTicks, abstractclientplayer);
                        break;
                    }
                }
            }
            else {
                if (!ModuleManager.getModule(SwingAnimation.class).getState()) {
                    this.func_178105_d(f2);
                }
                this.func_178096_b(f, f2);
            }
            this.func_178099_a((EntityLivingBase)abstractclientplayer, this.field_78453_b, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.func_82150_aj()) {
            this.func_178095_a(abstractclientplayer, f, f2);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179101_C();
        RenderHelper.func_74518_a();
    }
    
    @Inject(method = { "renderFireInFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && antiBlind.getFireEffect().asBoolean()) {
            callbackInfo.cancel();
        }
    }
}
