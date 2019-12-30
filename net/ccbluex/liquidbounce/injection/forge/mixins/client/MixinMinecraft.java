// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MultiActions;
import net.ccbluex.liquidbounce.event.events.WorldEvent;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.utils.render.IconUtils;
import net.minecraft.util.Util;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import org.lwjgl.Sys;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.ScreenEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.ui.mainmenu.GuiOldMainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;
import net.ccbluex.liquidbounce.ui.GuiWelcome;
import net.ccbluex.liquidbounce.ui.GuiUpdate;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ Minecraft.class })
public abstract class MixinMinecraft
{
    @Shadow
    public GuiScreen field_71462_r;
    @Shadow
    public boolean field_71454_w;
    @Shadow
    private int field_71429_W;
    @Shadow
    public MovingObjectPosition field_71476_x;
    @Shadow
    public WorldClient field_71441_e;
    @Shadow
    public EntityPlayerSP field_71439_g;
    @Shadow
    public EffectRenderer field_71452_i;
    @Shadow
    public PlayerControllerMP field_71442_b;
    @Shadow
    public int field_71443_c;
    @Shadow
    public int field_71440_d;
    @Shadow
    private int field_71467_ac;
    private long lastFrame;
    
    public MixinMinecraft() {
        this.lastFrame = this.getTime();
    }
    
    @Inject(method = { "run" }, at = { @At("HEAD") })
    private void init(final CallbackInfo callbackInfo) {
        if (this.field_71443_c < 1067) {
            this.field_71443_c = 1067;
        }
        if (this.field_71440_d < 622) {
            this.field_71440_d = 622;
        }
    }
    
    @Inject(method = { "startGame" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER) })
    private void startGame(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.startClient();
    }
    
    @Inject(method = { "startGame" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", shift = At.Shift.AFTER) })
    private void afterMainScreen(final CallbackInfo callbackInfo) {
        if (LiquidBounce.CLIENT.latestVersion > 62) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiUpdate());
        }
        else if (LiquidBounce.CLIENT.firstStart) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiWelcome());
        }
    }
    
    @Inject(method = { "createDisplay" }, at = { @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER) })
    private void createDisplay(final CallbackInfo callbackInfo) {
        Display.setTitle("LiquidBounce b62 | 1.8.9");
    }
    
    @Inject(method = { "displayGuiScreen" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", shift = At.Shift.AFTER) })
    private void displayGuiScreen(final CallbackInfo callbackInfo) {
        if (this.field_71462_r instanceof GuiMainMenu || (this.field_71462_r != null && this.field_71462_r.getClass().getName().startsWith("net.labymod") && this.field_71462_r.getClass().getSimpleName().equals("ModGuiMainMenu"))) {
            final String lowerCase = LiquidBounce.CLIENT.mainMenuStyle.toLowerCase();
            switch (lowerCase) {
                case "new": {
                    this.field_71462_r = new net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu();
                    break;
                }
                case "old": {
                    this.field_71462_r = new GuiOldMainMenu();
                    break;
                }
            }
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
            this.field_71462_r.func_146280_a(Minecraft.func_71410_x(), scaledResolution.func_78326_a(), scaledResolution.func_78328_b());
            this.field_71454_w = false;
        }
        LiquidBounce.CLIENT.eventManager.callEvent(new ScreenEvent(this.field_71462_r));
    }
    
    @Inject(method = { "runGameLoop" }, at = { @At("HEAD") })
    private void runGameLoop(final CallbackInfo callbackInfo) {
        final long currentTime = this.getTime();
        final int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtils.deltaTime = deltaTime;
    }
    
    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    @Inject(method = { "runTick" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift = At.Shift.BEFORE) })
    private void onTick(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new TickEvent());
    }
    
    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER) })
    private void onKey(final CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && this.field_71462_r == null) {
            LiquidBounce.CLIENT.eventManager.callEvent(new KeyEvent((Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey()));
        }
    }
    
    @Inject(method = { "sendClickBlockToController" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;") })
    private void onClickBlock(final CallbackInfo callbackInfo) {
        if (this.field_71429_W == 0 && this.field_71441_e.func_180495_p(this.field_71476_x.func_178782_a()).func_177230_c().func_149688_o() != Material.field_151579_a) {
            LiquidBounce.CLIENT.eventManager.callEvent(new ClickBlockEvent(this.field_71476_x.func_178782_a(), this.field_71476_x.field_178784_b));
        }
    }
    
    @Inject(method = { "setWindowIcon" }, at = { @At("HEAD") }, cancellable = true)
    private void setWindowIcon(final CallbackInfo callbackInfo) {
        if (Util.func_110647_a() != Util.EnumOS.OSX) {
            final ByteBuffer[] liquidBounceFavicon = IconUtils.getFavicon();
            if (liquidBounceFavicon != null) {
                Display.setIcon(liquidBounceFavicon);
                callbackInfo.cancel();
            }
        }
    }
    
    @Inject(method = { "shutdown" }, at = { @At("HEAD") })
    private void shutdown(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.stopClient();
    }
    
    @Inject(method = { "clickMouse" }, at = { @At("HEAD") })
    private void clickMouse(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(AutoClicker.class).getState()) {
            this.field_71429_W = 0;
        }
    }
    
    @Inject(method = { "rightClickMouse" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelayTimer:I", shift = At.Shift.AFTER) })
    private void rightClickMouse(final CallbackInfo callbackInfo) {
        final FastPlace fastPlace = (FastPlace)ModuleManager.getModule(FastPlace.class);
        if (fastPlace.getState()) {
            this.field_71467_ac = fastPlace.speedValue.asInteger();
        }
    }
    
    @Inject(method = { "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V" }, at = { @At("HEAD") })
    private void loadWorld(final WorldClient p_loadWorld_1_, final String p_loadWorld_2_, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new WorldEvent(p_loadWorld_1_));
    }
    
    @Overwrite
    private void func_147115_a(final boolean leftClick) {
        if (!leftClick) {
            this.field_71429_W = 0;
        }
        if (this.field_71429_W <= 0 && (!this.field_71439_g.func_71039_bw() || ModuleManager.getModule(MultiActions.class).getState())) {
            if (leftClick && this.field_71476_x != null && this.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockPos = this.field_71476_x.func_178782_a();
                if (this.field_71429_W == 0) {
                    LiquidBounce.CLIENT.eventManager.callEvent(new ClickBlockEvent(blockPos, this.field_71476_x.field_178784_b));
                }
                if (this.field_71441_e.func_180495_p(blockPos).func_177230_c().func_149688_o() != Material.field_151579_a && this.field_71442_b.func_180512_c(blockPos, this.field_71476_x.field_178784_b)) {
                    this.field_71452_i.func_180532_a(blockPos, this.field_71476_x.field_178784_b);
                    this.field_71439_g.func_71038_i();
                }
            }
            else if (!ModuleManager.getModule(AbortBreaking.class).getState()) {
                this.field_71442_b.func_78767_c();
            }
        }
    }
}
