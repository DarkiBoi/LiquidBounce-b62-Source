// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.event.HoverEvent;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import java.util.Collections;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.BackgroundShader;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiScreen.class })
public abstract class MixinGuiScreen
{
    @Shadow
    public Minecraft field_146297_k;
    @Shadow
    protected List<GuiButton> field_146292_n;
    @Shadow
    public int field_146294_l;
    @Shadow
    public int field_146295_m;
    @Shadow
    protected FontRenderer field_146289_q;
    
    @Shadow
    public void func_73876_c() {
    }
    
    @Shadow
    protected abstract void func_175272_a(final IChatComponent p0, final int p1, final int p2);
    
    @Shadow
    protected abstract void func_146283_a(final List<String> p0, final int p1, final int p2);
    
    @Inject(method = { "drawWorldBackground" }, at = { @At("HEAD") })
    private void drawWorldBackground(final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.inventoryParticle.asBoolean() && this.field_146297_k.field_71439_g != null) {
            final ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            final int width = scaledResolution.func_78326_a();
            final int height = scaledResolution.func_78328_b();
            ParticleUtils.drawParticles(Mouse.getX() * width / this.field_146297_k.field_71443_c, height - Mouse.getY() * height / this.field_146297_k.field_71440_d - 1);
        }
    }
    
    @Overwrite
    public void func_146278_c(final int i) {
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        if (LiquidBounce.CLIENT.background == null) {
            BackgroundShader.BACKGROUND_SHADER.startShader();
            final Tessellator instance = Tessellator.func_178181_a();
            final WorldRenderer worldRenderer = instance.func_178180_c();
            worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
            worldRenderer.func_181662_b(0.0, (double)this.field_146295_m, 0.0).func_181675_d();
            worldRenderer.func_181662_b((double)this.field_146294_l, (double)this.field_146295_m, 0.0).func_181675_d();
            worldRenderer.func_181662_b((double)this.field_146294_l, 0.0, 0.0).func_181675_d();
            worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
            instance.func_78381_a();
            BackgroundShader.BACKGROUND_SHADER.stopShader();
        }
        else {
            final ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            final int width = scaledResolution.func_78326_a();
            final int height = scaledResolution.func_78328_b();
            this.field_146297_k.func_110434_K().func_110577_a(LiquidBounce.CLIENT.background);
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.func_152125_a(0, 0, 0.0f, 0.0f, width, height, width, height, (float)width, (float)height);
            ParticleUtils.drawParticles(Mouse.getX() * width / this.field_146297_k.field_71443_c, height - Mouse.getY() * height / this.field_146297_k.field_71440_d - 1);
        }
    }
    
    @Inject(method = { "sendChatMessage(Ljava/lang/String;Z)V" }, at = { @At("HEAD") }, cancellable = true)
    private void messageSend(final String msg, final boolean addToChat, final CallbackInfo callbackInfo) {
        if (msg.startsWith(".") && addToChat) {
            this.field_146297_k.field_71456_v.func_146158_b().func_146239_a(msg);
            LiquidBounce.CLIENT.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "handleComponentHover" }, at = { @At("HEAD") })
    private void handleHoverOverComponent(final IChatComponent component, final int x, final int y, final CallbackInfo callbackInfo) {
        if (component == null || component.func_150256_b().func_150235_h() == null || !ModuleManager.getModule(ComponentOnHover.class).getState()) {
            return;
        }
        final ChatStyle chatStyle = component.func_150256_b();
        final ClickEvent clickEvent = chatStyle.func_150235_h();
        final HoverEvent hoverEvent = chatStyle.func_150210_i();
        this.func_146283_a(Collections.singletonList("§c§l" + clickEvent.func_150669_a().func_150673_b().toUpperCase() + ": §a" + clickEvent.func_150668_b()), x, y - ((hoverEvent != null) ? 17 : 0));
    }
}
