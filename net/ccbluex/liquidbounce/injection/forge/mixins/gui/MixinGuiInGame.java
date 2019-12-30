// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiIngame.class })
public abstract class MixinGuiInGame
{
    @Shadow
    protected abstract void func_175184_a(final int p0, final int p1, final int p2, final float p3, final EntityPlayer p4);
    
    @Inject(method = { "renderScoreboard" }, at = { @At("HEAD") }, cancellable = true)
    private void renderScoreboard(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoScoreboard.class).getState()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "renderTooltip" }, at = { @At("HEAD") }, cancellable = true)
    private void renderTooltip(final ScaledResolution sr, final float partialTicks, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer && hud.getState() && hud.blackHotbarValue.asBoolean()) {
            final EntityPlayer entityPlayer = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
            final int middleScreen = sr.func_78326_a() / 2;
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GuiIngame.func_73734_a(middleScreen - 91, sr.func_78328_b() - 24, middleScreen + 90, sr.func_78328_b(), Integer.MIN_VALUE);
            GuiIngame.func_73734_a(middleScreen - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 1, sr.func_78328_b() - 24, middleScreen - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 22, sr.func_78328_b() - 22 - 1 + 24, Integer.MAX_VALUE);
            GlStateManager.func_179091_B();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            RenderHelper.func_74520_c();
            for (int j = 0; j < 9; ++j) {
                final int k = sr.func_78326_a() / 2 - 90 + j * 20 + 2;
                final int l = sr.func_78328_b() - 16 - 3;
                this.func_175184_a(j, k, l, partialTicks, entityPlayer);
            }
            RenderHelper.func_74518_a();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "renderTooltip" }, at = { @At("RETURN") })
    private void renderTooltipPost(final ScaledResolution sr, final float partialTicks, final CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(partialTicks));
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && antiBlind.getPumpkinEffect().asBoolean()) {
            callbackInfo.cancel();
        }
    }
}
