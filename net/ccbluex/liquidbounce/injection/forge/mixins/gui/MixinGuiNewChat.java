// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.Iterator;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;
    @Shadow
    @Final
    private List<ChatLine> field_146252_h;
    
    @Shadow
    public abstract int func_146232_i();
    
    @Shadow
    public abstract boolean func_146241_e();
    
    @Shadow
    public abstract float func_146244_h();
    
    @Shadow
    public abstract int func_146228_f();
    
    @Shadow
    public abstract void func_146242_c(final int p0);
    
    @Shadow
    public abstract void func_146229_b(final int p0);
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void drawChat(final int p_drawChat_1_, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.getState() && hud.fontChatValue.asBoolean()) {
            callbackInfo.cancel();
            if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
                final int lvt_2_1_ = this.func_146232_i();
                boolean lvt_3_1_ = false;
                int lvt_4_1_ = 0;
                final int lvt_5_1_ = this.field_146253_i.size();
                final float lvt_6_1_ = this.field_146247_f.field_71474_y.field_74357_r * 0.9f + 0.1f;
                if (lvt_5_1_ > 0) {
                    if (this.func_146241_e()) {
                        lvt_3_1_ = true;
                    }
                    final float lvt_7_1_ = this.func_146244_h();
                    final int lvt_8_1_ = MathHelper.func_76123_f(this.func_146228_f() / lvt_7_1_);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b(2.0f, 20.0f, 0.0f);
                    GlStateManager.func_179152_a(lvt_7_1_, lvt_7_1_, 1.0f);
                    for (int lvt_9_1_ = 0; lvt_9_1_ + this.field_146250_j < this.field_146253_i.size() && lvt_9_1_ < lvt_2_1_; ++lvt_9_1_) {
                        final ChatLine lvt_10_1_ = this.field_146253_i.get(lvt_9_1_ + this.field_146250_j);
                        if (lvt_10_1_ != null) {
                            final int lvt_11_1_ = p_drawChat_1_ - lvt_10_1_.func_74540_b();
                            if (lvt_11_1_ < 200 || lvt_3_1_) {
                                double lvt_12_1_ = lvt_11_1_ / 200.0;
                                lvt_12_1_ = 1.0 - lvt_12_1_;
                                lvt_12_1_ *= 10.0;
                                lvt_12_1_ = MathHelper.func_151237_a(lvt_12_1_, 0.0, 1.0);
                                lvt_12_1_ *= lvt_12_1_;
                                int lvt_14_1_ = (int)(255.0 * lvt_12_1_);
                                if (lvt_3_1_) {
                                    lvt_14_1_ = 255;
                                }
                                lvt_14_1_ *= (int)lvt_6_1_;
                                ++lvt_4_1_;
                                if (lvt_14_1_ > 3) {
                                    final int lvt_15_1_ = 0;
                                    final int lvt_16_1_ = -lvt_9_1_ * 9;
                                    Gui.func_73734_a(lvt_15_1_, lvt_16_1_ - 9, lvt_15_1_ + lvt_8_1_ + 4, lvt_16_1_, lvt_14_1_ / 2 << 24);
                                    final String lvt_17_1_ = lvt_10_1_.func_151461_a().func_150254_d();
                                    Fonts.font40.func_175063_a(lvt_17_1_, lvt_15_1_ + 2.0f, (float)(lvt_16_1_ - 8), 16777215 + (lvt_14_1_ << 24));
                                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                    GlStateManager.func_179117_G();
                                }
                            }
                        }
                    }
                    if (lvt_3_1_) {
                        final int lvt_9_1_ = Fonts.font40.field_78288_b;
                        GlStateManager.func_179109_b(-3.0f, 0.0f, 0.0f);
                        final int lvt_10_2_ = lvt_5_1_ * lvt_9_1_ + lvt_5_1_;
                        final int lvt_11_1_ = lvt_4_1_ * lvt_9_1_ + lvt_4_1_;
                        final int lvt_12_2_ = this.field_146250_j * lvt_11_1_ / lvt_5_1_;
                        final int lvt_13_1_ = lvt_11_1_ * lvt_11_1_ / lvt_10_2_;
                        if (lvt_10_2_ != lvt_11_1_) {
                            final int lvt_14_1_ = (lvt_12_2_ > 0) ? 170 : 96;
                            final int lvt_15_2_ = this.field_146251_k ? 13382451 : 3355562;
                            Gui.func_73734_a(0, -lvt_12_2_, 2, -lvt_12_2_ - lvt_13_1_, lvt_15_2_ + (lvt_14_1_ << 24));
                            Gui.func_73734_a(2, -lvt_12_2_, 1, -lvt_12_2_ - lvt_13_1_, 13421772 + (lvt_14_1_ << 24));
                        }
                    }
                    GlStateManager.func_179121_F();
                }
            }
        }
    }
    
    @Inject(method = { "getChatComponent" }, at = { @At("HEAD") }, cancellable = true)
    private void getChatComponent(final int p_getChatComponent_1_, final int p_getChatComponent_2_, final CallbackInfoReturnable<IChatComponent> callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.getState() && hud.fontChatValue.asBoolean()) {
            if (!this.func_146241_e()) {
                callbackInfo.setReturnValue(null);
            }
            else {
                final ScaledResolution lvt_3_1_ = new ScaledResolution(this.field_146247_f);
                final int lvt_4_1_ = lvt_3_1_.func_78325_e();
                final float lvt_5_1_ = this.func_146244_h();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.func_76141_d(lvt_6_1_ / lvt_5_1_);
                lvt_7_1_ = MathHelper.func_76141_d(lvt_7_1_ / lvt_5_1_);
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    final int lvt_8_1_ = Math.min(this.func_146232_i(), this.field_146253_i.size());
                    if (lvt_6_1_ <= MathHelper.func_76141_d(this.func_146228_f() / this.func_146244_h()) && lvt_7_1_ < Fonts.font40.field_78288_b * lvt_8_1_ + lvt_8_1_) {
                        final int lvt_9_1_ = lvt_7_1_ / Fonts.font40.field_78288_b + this.field_146250_j;
                        if (lvt_9_1_ >= 0 && lvt_9_1_ < this.field_146253_i.size()) {
                            final ChatLine lvt_10_1_ = this.field_146253_i.get(lvt_9_1_);
                            int lvt_11_1_ = 0;
                            for (final IChatComponent lvt_13_1_ : lvt_10_1_.func_151461_a()) {
                                if (lvt_13_1_ instanceof ChatComponentText) {
                                    lvt_11_1_ += Fonts.font40.func_78256_a(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)lvt_13_1_).func_150265_g(), false));
                                    if (lvt_11_1_ > lvt_6_1_) {
                                        callbackInfo.setReturnValue(lvt_13_1_);
                                        return;
                                    }
                                    continue;
                                }
                            }
                        }
                        callbackInfo.setReturnValue(null);
                    }
                    else {
                        callbackInfo.setReturnValue(null);
                    }
                }
                else {
                    callbackInfo.setReturnValue(null);
                }
            }
        }
    }
}
