// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.awt.Color;
import co.uk.hexeption.utils.OutlineUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ RendererLivingEntity.class })
public abstract class MixinRendererLivingEntity extends MixinRender
{
    @Shadow
    protected ModelBase field_77045_g;
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    private <T extends EntityLivingBase> void injectChamsPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.targetsValue.asBoolean() && EntityUtils.isSelected((Entity)entity, false)) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    private <T extends EntityLivingBase> void injectChamsPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.targetsValue.asBoolean() && EntityUtils.isSelected((Entity)entity, false)) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
    
    @Inject(method = { "canRenderName" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityLivingBase> void canRenderName(final T entity, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || (ModuleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected((Entity)entity, false))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Overwrite
    protected <T extends EntityLivingBase> void func_77036_a(final T entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor) {
        final boolean visible = !entitylivingbaseIn.func_82150_aj();
        final TrueSight trueSight = (TrueSight)ModuleManager.getModule(TrueSight.class);
        final boolean semiVisible = !visible && (!entitylivingbaseIn.func_98034_c((EntityPlayer)Minecraft.func_71410_x().field_71439_g) || (trueSight.getState() && trueSight.entitiesValue.asBoolean()));
        if (visible || semiVisible) {
            if (!this.func_180548_c(entitylivingbaseIn)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.func_179132_a(false);
                GlStateManager.func_179147_l();
                GlStateManager.func_179112_b(770, 771);
                GlStateManager.func_179092_a(516, 0.003921569f);
            }
            final ESP esp = (ESP)ModuleManager.getModule(ESP.class);
            if (esp.getState() && EntityUtils.isSelected((Entity)entitylivingbaseIn, false)) {
                final Minecraft mc = Minecraft.func_71410_x();
                final boolean fancyGraphics = mc.field_71474_y.field_74347_j;
                mc.field_71474_y.field_74347_j = false;
                final float gamma = mc.field_71474_y.field_74333_Y;
                mc.field_71474_y.field_74333_Y = 100000.0f;
                final String lowerCase = esp.modeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        RenderUtils.glColor(esp.getColor((Entity)entitylivingbaseIn));
                        GL11.glLineWidth(esp.wireframeWidth.asFloat());
                        this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                    case "outline": {
                        ClientUtils.disableFastRender();
                        GlStateManager.func_179117_G();
                        final Color color = esp.getColor((Entity)entitylivingbaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(esp.outlineWidth.asFloat());
                        this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                        break;
                    }
                }
                mc.field_71474_y.field_74347_j = fancyGraphics;
                mc.field_71474_y.field_74333_Y = gamma;
            }
            this.field_77045_g.func_78088_a((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            if (semiVisible) {
                GlStateManager.func_179084_k();
                GlStateManager.func_179092_a(516, 0.1f);
                GlStateManager.func_179121_F();
                GlStateManager.func_179132_a(true);
            }
        }
    }
}
