// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.Comparator;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import java.util.Iterator;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.render.TNTESP;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.movement.PerfectHorseJump;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.movement.BlockWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.render.HeadRotations;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.movement.Freeze;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Damage;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoFish;
import net.ccbluex.liquidbounce.features.module.modules.render.RemoteView;
import net.ccbluex.liquidbounce.features.module.modules.misc.RazerKeyboard;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallGlide;
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ItemTeleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ForceUnicodeChat;
import net.ccbluex.liquidbounce.features.module.modules.movement.WaterFly;
import net.ccbluex.liquidbounce.features.module.modules.combat.TeleportHit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GodMode;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.misc.AtAllProvider;
import net.ccbluex.liquidbounce.features.module.modules.world.Liquids;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Kick;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MoreCarry;
import net.ccbluex.liquidbounce.features.module.modules.movement.SlimeJump;
import net.ccbluex.liquidbounce.features.module.modules.combat.Ignite;
import net.ccbluex.liquidbounce.features.module.modules.movement.WaterSpeed;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.player.PotionSaver;
import net.ccbluex.liquidbounce.features.module.modules.player.FarmKingBot;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.movement.BugUp;
import net.ccbluex.liquidbounce.features.module.modules.exploit.BedGodMode;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.combat.TNTBlock;
import net.ccbluex.liquidbounce.features.module.modules.movement.ReverseStep;
import net.ccbluex.liquidbounce.features.module.modules.fun.Derp;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastStairs;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MultiActions;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.movement.LadderJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ConsoleSpammer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Plugins;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.player.Eagle;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.world.AutoBreak;
import net.ccbluex.liquidbounce.features.module.modules.movement.AutoWalk;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Paralyze;
import net.ccbluex.liquidbounce.features.module.modules.fun.SkinDerp;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.misc.MidClick;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.player.Regen;
import net.ccbluex.liquidbounce.features.module.modules.player.Zoot;
import net.ccbluex.liquidbounce.features.module.modules.movement.IceSpeed;
import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoRespawn;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PingSpoof;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Projectiles;
import net.ccbluex.liquidbounce.features.module.modules.render.StorageESP;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.player.FastUse;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.modules.world.FastBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.HighJump;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.combat.Trigger;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoWeapon;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoSoup;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoLeave;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public final class ModuleManager implements EventListener
{
    private static final List<Module> modules;
    
    public ModuleManager() {
        LiquidBounce.CLIENT.eventManager.registerListener(this);
    }
    
    public void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModule(AutoArmor.class);
        this.registerModule(AutoBow.class);
        this.registerModule(AutoLeave.class);
        this.registerModule(AutoPot.class);
        this.registerModule(AutoSoup.class);
        this.registerModule(AutoWeapon.class);
        this.registerModule(BowAimbot.class);
        this.registerModule(Criticals.class);
        this.registerModule(KillAura.class);
        this.registerModule(Trigger.class);
        this.registerModule(Velocity.class);
        this.registerModule(Fly.class);
        this.registerModule(ClickGUI.class);
        this.registerModule(HighJump.class);
        this.registerModule(InventoryMove.class);
        this.registerModule(NoSlow.class);
        this.registerModule(LiquidWalk.class);
        this.registerModule(SafeWalk.class);
        this.registerModule(WallClimb.class);
        this.registerModule(Strafe.class);
        this.registerModule(Sprint.class);
        this.registerModule(Teams.class);
        this.registerModule(NoRotateSet.class);
        this.registerModule(AntiBot.class);
        this.registerModule(ChestStealer.class);
        this.registerModule(Scaffold.class);
        this.registerModule(CivBreak.class);
        this.registerModule(Tower.class);
        this.registerModule(FastBreak.class);
        this.registerModule(FastPlace.class);
        this.registerModule(ESP.class);
        this.registerModule(Speed.class);
        this.registerModule(Tracers.class);
        this.registerModule(NameTags.class);
        this.registerModule(FastUse.class);
        this.registerModule(Teleport.class);
        this.registerModule(Fullbright.class);
        this.registerModule(ItemESP.class);
        this.registerModule(StorageESP.class);
        this.registerModule(Projectiles.class);
        this.registerModule(NoClip.class);
        this.registerModule(Nuker.class);
        this.registerModule(PingSpoof.class);
        this.registerModule(FastClimb.class);
        this.registerModule(Step.class);
        this.registerModule(AutoRespawn.class);
        this.registerModule(AutoTool.class);
        this.registerModule(NoWeb.class);
        this.registerModule(Spammer.class);
        this.registerModule(IceSpeed.class);
        this.registerModule(Zoot.class);
        this.registerModule(Regen.class);
        this.registerModule(NoFall.class);
        this.registerModule(Blink.class);
        this.registerModule(NameProtect.class);
        this.registerModule(NoHurtCam.class);
        this.registerModule(Ghost.class);
        this.registerModule(MidClick.class);
        this.registerModule(XRay.class);
        this.registerModule(Fucker.class);
        this.registerModule(Timer.class);
        this.registerModule(ChestAura.class);
        this.registerModule(Sneak.class);
        this.registerModule(SkinDerp.class);
        this.registerModule(Paralyze.class);
        this.registerModule(GhostHand.class);
        this.registerModule(AutoWalk.class);
        this.registerModule(AutoBreak.class);
        this.registerModule(FreeCam.class);
        this.registerModule(Aimbot.class);
        this.registerModule(Eagle.class);
        this.registerModule(HitBox.class);
        this.registerModule(AntiCactus.class);
        this.registerModule(Plugins.class);
        this.registerModule(AntiHunger.class);
        this.registerModule(ConsoleSpammer.class);
        this.registerModule(NoScoreboard.class);
        this.registerModule(LongJump.class);
        this.registerModule(Parkour.class);
        this.registerModule(LadderJump.class);
        this.registerModule(FastBow.class);
        this.registerModule(MultiActions.class);
        this.registerModule(AirJump.class);
        this.registerModule(AutoClicker.class);
        this.registerModule(NoBob.class);
        this.registerModule(BlockOverlay.class);
        this.registerModule(NoFriends.class);
        this.registerModule(BlockESP.class);
        this.registerModule(Chams.class);
        this.registerModule(VClip.class);
        this.registerModule(Phase.class);
        this.registerModule(ServerCrasher.class);
        this.registerModule(NoFOV.class);
        this.registerModule(FastStairs.class);
        this.registerModule(SwingAnimation.class);
        this.registerModule(Derp.class);
        this.registerModule(ReverseStep.class);
        this.registerModule(TNTBlock.class);
        this.registerModule(InventoryCleaner.class);
        this.registerModule(TrueSight.class);
        this.registerModule(LiquidChat.class);
        this.registerModule(AntiBlind.class);
        this.registerModule(NoSwing.class);
        this.registerModule(BedGodMode.class);
        this.registerModule(BugUp.class);
        this.registerModule(Breadcrumbs.class);
        this.registerModule(AbortBreaking.class);
        this.registerModule(FarmKingBot.class);
        this.registerModule(PotionSaver.class);
        this.registerModule(CameraClip.class);
        this.registerModule(WaterSpeed.class);
        this.registerModule(Ignite.class);
        this.registerModule(SlimeJump.class);
        this.registerModule(MoreCarry.class);
        this.registerModule(NoPitchLimit.class);
        this.registerModule(Kick.class);
        this.registerModule(Liquids.class);
        this.registerModule(AtAllProvider.class);
        this.registerModule(AirLadder.class);
        this.registerModule(GodMode.class);
        this.registerModule(TeleportHit.class);
        this.registerModule(WaterFly.class);
        this.registerModule(ForceUnicodeChat.class);
        this.registerModule(ItemTeleport.class);
        this.registerModule(BufferSpeed.class);
        this.registerModule(SuperKnockback.class);
        this.registerModule(WallGlide.class);
        this.registerModule(ProphuntESP.class);
        this.registerModule(RazerKeyboard.class);
        this.registerModule(RemoteView.class);
        this.registerModule(AutoFish.class);
        this.registerModule(Damage.class);
        this.registerModule(Freeze.class);
        this.registerModule(KeepContainer.class);
        this.registerModule(VehicleOneHit.class);
        this.registerModule(Reach.class);
        this.registerModule(HeadRotations.class);
        this.registerModule(NoJumpDelay.class);
        this.registerModule(BlockWalk.class);
        this.registerModule(AntiAFK.class);
        this.registerModule(PerfectHorseJump.class);
        this.registerModule(HUD.class);
        this.registerModule(TNTESP.class);
        this.registerModule(ComponentOnHover.class);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + ModuleManager.modules.size() + " modules.");
    }
    
    public void registerModule(final Module module) {
        ModuleManager.modules.add(module);
        LiquidBounce.CLIENT.eventManager.registerListener(module);
    }
    
    public void registerModule(final Class moduleClass) {
        try {
            this.registerModule(moduleClass.newInstance());
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to load module: " + moduleClass.getName() + " (" + e.getClass().getName() + ": " + e.getMessage() + ")");
        }
    }
    
    public void unregisterModule(final Module module) {
        ModuleManager.modules.remove(module);
        LiquidBounce.CLIENT.eventManager.unregisterListener(module);
    }
    
    public static Module getModule(final Class<?> targetClass) {
        for (final Module module : ModuleManager.modules) {
            if (targetClass.equals(module.getClass())) {
                return module;
            }
        }
        return null;
    }
    
    public static Module getModule(final String moduleName) {
        for (final Module module : ModuleManager.modules) {
            if (module.getName().equalsIgnoreCase(moduleName)) {
                return module;
            }
        }
        return null;
    }
    
    @EventTarget
    private void onKey(final KeyEvent event) {
        ModuleManager.modules.stream().filter(module -> module.getKeyBind() == event.getKey()).forEach(Module::toggle);
    }
    
    public static List<Module> getModules() {
        synchronized (ModuleManager.modules) {
            return ModuleManager.modules;
        }
    }
    
    public void sortModules() {
        ModuleManager.modules.sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getName));
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
    
    static {
        modules = new ArrayList<Module>();
    }
}
