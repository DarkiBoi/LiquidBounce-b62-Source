// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.ccbluex.liquidbounce.utils.VectorUtils;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fly", description = "Allows you to fly in survival mode.", category = ModuleCategory.MOVEMENT, keyBind = 33)
public class Fly extends Module
{
    public final ListValue modeValue;
    private final FloatValue vanillaSpeedValue;
    private final BoolValue vanillaKickBypassValue;
    private final FloatValue mineplexSpeedValue;
    private final FloatValue ncpMotionValue;
    private final FloatValue aacSpeedValue;
    private final FloatValue aacMotion;
    private final FloatValue aacMotion2;
    private final IntegerValue neruxVaceTicks;
    private final BoolValue hypixelLatestBoostValue;
    private final IntegerValue hypixelLatestBoostDelayValue;
    private final FloatValue hypixelLatestBoostTimerValue;
    private final BoolValue markValue;
    private double startY;
    private final MSTimer flyTimer;
    private final MSTimer groundTimer;
    private boolean noPacketModify;
    private double aacJump;
    private int aac3delay;
    private int aac3glideDelay;
    private boolean noFlag;
    private final MSTimer mineSecureVClipTimer;
    private final TickTimer spartanTimer;
    private final MSTimer hypixelTimer;
    private long minesuchtTP;
    private boolean hypixelSwitch;
    private final MSTimer mineplexTimer;
    private boolean wasDead;
    private final TickTimer latestHypixelTimer;
    private int boostHypixelState;
    private double moveSpeed;
    private double lastDistance;
    private final TickTimer cubecraft2TickTimer;
    private final TickTimer cubecraftTeleportTickTimer;
    private final TickTimer freeHypixelTimer;
    private float freeHypixelYaw;
    private float freeHypixelPitch;
    
    public Fly() {
        this.modeValue = new ListValue("Mode", new String[] { "Vanilla", "SmoothVanilla", "NCP", "OldNCP", "AAC", "AACv3", "FastAACv3", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "Gomme", "Flag", "CubeCraft", "InfinityCubeCraft", "InfinityVCubeCraft", "Hypixel", "OtherHypixel", "LatestHypixel", "BoostHypixel", "FreeHypixel", "Rewinside", "TeleportRewinside", "Mineplex", "KeepAlive", "MineSecure", "Spartan", "Spartan2", "BugSpartan", "KillSwitch", "HawkEye", "Minesucht", "Jetpack", "HAC", "WatchCat", "NeruxVace" }, "Vanilla");
        this.vanillaSpeedValue = new FloatValue("VanillaSpeed", 2.0f, 0.0f, 5.0f);
        this.vanillaKickBypassValue = new BoolValue("VanillaKickBypass", false);
        this.mineplexSpeedValue = new FloatValue("MineplexSpeed", 1.0f, 0.5f, 10.0f);
        this.ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f);
        this.aacSpeedValue = new FloatValue("AACSpeed", 0.3f, 0.0f, 1.0f);
        this.aacMotion = new FloatValue("AAC3.3.12-Motion", 10.0f, 0.1f, 10.0f);
        this.aacMotion2 = new FloatValue("AAC3.3.13-Motion", 10.0f, 0.1f, 10.0f);
        this.neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);
        this.hypixelLatestBoostValue = new BoolValue("HypixelLatest-Boost", true);
        this.hypixelLatestBoostDelayValue = new IntegerValue("HypixelLatest-BoostDelay", 1200, 0, 2000);
        this.hypixelLatestBoostTimerValue = new FloatValue("HypixelLatest-BoostTimer", 1.0f, 0.0f, 5.0f);
        this.markValue = new BoolValue("Mark", true);
        this.flyTimer = new MSTimer();
        this.groundTimer = new MSTimer();
        this.mineSecureVClipTimer = new MSTimer();
        this.spartanTimer = new TickTimer();
        this.hypixelTimer = new MSTimer();
        this.mineplexTimer = new MSTimer();
        this.latestHypixelTimer = new TickTimer();
        this.boostHypixelState = 1;
        this.cubecraft2TickTimer = new TickTimer();
        this.cubecraftTeleportTickTimer = new TickTimer();
        this.freeHypixelTimer = new TickTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fly", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Fly.this.modeValue.contains(args[2])) {
                            Fly.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Fly mode was set to §8" + Fly.this.modeValue.asString().toUpperCase() + "§7.");
                            Fly$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".fly mode §c<§8" + Strings.join(Fly.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("vanillaspeed")) {
                        if (args.length > 2) {
                            try {
                                final float speed = Float.parseFloat(args[2]);
                                Fly.this.vanillaSpeedValue.setValue(speed);
                                this.chat("§7Fly vanillaspeed was set to §8" + speed + "§7.");
                                Fly$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly vanillaspeed <speed>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("ncpmotion")) {
                        if (args.length > 2) {
                            try {
                                final float motion = Float.parseFloat(args[2]);
                                Fly.this.ncpMotionValue.setValue(motion);
                                this.chat("§7Fly ncpmotion was set to §8" + motion + "§7.");
                                Fly$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly ncpmotion <motion>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("aacspeed")) {
                        if (args.length > 2) {
                            try {
                                final float speed = Float.parseFloat(args[2]);
                                Fly.this.aacSpeedValue.setValue(speed);
                                this.chat("§7Fly aacspeed was set to §8" + speed + "§7.");
                                Fly$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly aacspeed <speed>");
                        return;
                    }
                }
                this.chatSyntax(".fly <mode, vanillaspeed, ncpmotion, aacspeed>");
            }
        });
    }
    
    @Override
    public void onEnable() {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        this.flyTimer.reset();
        this.noPacketModify = true;
        final double x = Fly.mc.field_71439_g.field_70165_t;
        final double y = Fly.mc.field_71439_g.field_70163_u;
        final double z = Fly.mc.field_71439_g.field_70161_v;
        final String mode = this.modeValue.asString();
        final String lowerCase = mode.toLowerCase();
        switch (lowerCase) {
            case "ncp": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    break;
                }
                for (int i = 0; i < 65; ++i) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
                final EntityPlayerSP field_71439_g = Fly.mc.field_71439_g;
                field_71439_g.field_70159_w *= 0.1;
                final EntityPlayerSP field_71439_g2 = Fly.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 0.1;
                Fly.mc.field_71439_g.func_71038_i();
                break;
            }
            case "oldncp": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    break;
                }
                for (int i = 0; i < 4; ++i) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.01, z, false));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.field_71439_g.func_70664_aZ();
                Fly.mc.field_71439_g.func_71038_i();
                break;
            }
            case "bugspartan": {
                for (int i = 0; i < 65; ++i) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
                final EntityPlayerSP field_71439_g3 = Fly.mc.field_71439_g;
                field_71439_g3.field_70159_w *= 0.1;
                final EntityPlayerSP field_71439_g4 = Fly.mc.field_71439_g;
                field_71439_g4.field_70179_y *= 0.1;
                Fly.mc.field_71439_g.func_71038_i();
                break;
            }
            case "infinitycubecraft": {
                ChatUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                break;
            }
            case "infinityvcubecraft": {
                ChatUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 2.0, Fly.mc.field_71439_g.field_70161_v);
                break;
            }
            case "boosthypixel": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    break;
                }
                for (int i = 0; i <= 64; ++i) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.0625, Fly.mc.field_71439_g.field_70161_v, false));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, i >= 64));
                }
                Fly.mc.field_71439_g.func_70664_aZ();
                this.boostHypixelState = 1;
                this.moveSpeed = 0.1;
                this.lastDistance = 0.0;
                break;
            }
        }
        this.startY = Fly.mc.field_71439_g.field_70163_u;
        this.aacJump = -3.8;
        this.noPacketModify = false;
        if (mode.equalsIgnoreCase("freehypixel")) {
            this.freeHypixelTimer.reset();
            Fly.mc.field_71439_g.func_70634_a(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.21, Fly.mc.field_71439_g.field_70161_v);
            this.freeHypixelYaw = Fly.mc.field_71439_g.field_70177_z;
            this.freeHypixelPitch = Fly.mc.field_71439_g.field_70125_A;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.wasDead = false;
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        this.noFlag = false;
        final String flyMode = this.modeValue.asString();
        if (!flyMode.toUpperCase().startsWith("AAC") && !flyMode.equalsIgnoreCase("LatestHypixel") && !flyMode.equalsIgnoreCase("CubeCraft")) {
            Fly.mc.field_71439_g.field_70159_w = 0.0;
            Fly.mc.field_71439_g.field_70181_x = 0.0;
            Fly.mc.field_71439_g.field_70179_y = 0.0;
        }
        Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
        Fly.mc.field_71428_T.field_74278_d = 1.0f;
        Fly.mc.field_71439_g.field_71102_ce = 0.02f;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String flyMode = this.modeValue.asString();
        final float vanillaSpeed = this.vanillaSpeedValue.asFloat();
        final String lowerCase = flyMode.toLowerCase();
        switch (lowerCase) {
            case "vanilla": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    final EntityPlayerSP field_71439_g = Fly.mc.field_71439_g;
                    field_71439_g.field_70181_x += vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    final EntityPlayerSP field_71439_g2 = Fly.mc.field_71439_g;
                    field_71439_g2.field_70181_x -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                this.handleVanillaKickBypass();
                break;
            }
            case "smoothvanilla": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                this.handleVanillaKickBypass();
                break;
            }
            case "cubecraft": {
                Fly.mc.field_71428_T.field_74278_d = 0.6f;
                this.cubecraftTeleportTickTimer.update();
                break;
            }
            case "infinitycubecraft": {
                Fly.mc.field_71428_T.field_74278_d = 0.6f;
                if (Fly.mc.field_71439_g.field_70122_E || this.startY <= Fly.mc.field_71439_g.field_70163_u) {
                    this.cubecraft2TickTimer.reset();
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                this.cubecraft2TickTimer.update();
                this.cubecraftTeleportTickTimer.update();
                if (this.cubecraft2TickTimer.hasTimePassed(3)) {
                    Fly.mc.field_71439_g.field_70181_x = 0.07;
                    this.cubecraft2TickTimer.reset();
                    break;
                }
                break;
            }
            case "infinityvcubecraft": {
                Fly.mc.field_71428_T.field_74278_d = 0.5f;
                if (Fly.mc.field_71439_g.field_70122_E || this.startY <= Fly.mc.field_71439_g.field_70163_u) {
                    this.cubecraft2TickTimer.reset();
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                this.cubecraft2TickTimer.update();
                this.cubecraftTeleportTickTimer.update();
                if (this.cubecraft2TickTimer.hasTimePassed(3)) {
                    Fly.mc.field_71439_g.field_70181_x = 0.07;
                    this.cubecraft2TickTimer.reset();
                    break;
                }
                break;
            }
            case "ncp": {
                Fly.mc.field_71439_g.field_70181_x = -this.ncpMotionValue.asFloat();
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.5;
                }
                MovementUtils.strafe();
                break;
            }
            case "oldncp": {
                if (this.startY > Fly.mc.field_71439_g.field_70163_u) {
                    Fly.mc.field_71439_g.field_70181_x = -1.0E-33;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.2;
                }
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d() && Fly.mc.field_71439_g.field_70163_u < this.startY - 0.1) {
                    Fly.mc.field_71439_g.field_70181_x = 0.2;
                }
                MovementUtils.strafe();
                break;
            }
            case "aac": {
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    this.aacJump += 0.2;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    this.aacJump -= 0.2;
                }
                if (this.startY + this.aacJump > Fly.mc.field_71439_g.field_70163_u) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    Fly.mc.field_71439_g.field_70181_x = 0.8;
                    MovementUtils.strafe(this.aacSpeedValue.asFloat());
                }
                MovementUtils.strafe();
                break;
            }
            case "aacv3": {
                if (this.aac3delay == 2) {
                    Fly.mc.field_71439_g.field_70181_x = 0.1;
                }
                else if (this.aac3delay > 2) {
                    this.aac3delay = 0;
                }
                ++this.aac3delay;
                break;
            }
            case "fastaacv3": {
                if (this.aac3delay == 2) {
                    Fly.mc.field_71439_g.field_70181_x = 0.1;
                }
                else if (this.aac3delay > 2) {
                    this.aac3delay = 0;
                }
                if (Fly.mc.field_71439_g.field_71158_b.field_78902_a == 0.0) {
                    Fly.mc.field_71439_g.field_70747_aH = 0.08f;
                }
                else {
                    Fly.mc.field_71439_g.field_70747_aH = 0.0f;
                }
                ++this.aac3delay;
                break;
            }
            case "gomme": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                if (this.aac3delay == 2) {
                    final EntityPlayerSP field_71439_g3 = Fly.mc.field_71439_g;
                    field_71439_g3.field_70181_x += 0.05;
                }
                else if (this.aac3delay > 2) {
                    final EntityPlayerSP field_71439_g4 = Fly.mc.field_71439_g;
                    field_71439_g4.field_70181_x -= 0.05;
                    this.aac3delay = 0;
                }
                ++this.aac3delay;
                if (!this.noFlag) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, Fly.mc.field_71439_g.field_70122_E));
                }
                if (Fly.mc.field_71439_g.field_70163_u <= 0.0) {
                    this.noFlag = true;
                    break;
                }
                break;
            }
            case "flag": {
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 999.0, Fly.mc.field_71439_g.field_70163_u + (Fly.mc.field_71474_y.field_74314_A.func_151470_d() ? 1.5624 : 1.0E-8) - (Fly.mc.field_71474_y.field_74311_E.func_151470_d() ? 0.0624 : 2.0E-8), Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 999.0, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 999.0, Fly.mc.field_71439_g.field_70163_u - 6969.0, Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 999.0, Fly.mc.field_71439_g.field_70177_z, Fly.mc.field_71439_g.field_70125_A, true));
                Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t + Fly.mc.field_71439_g.field_70159_w * 11.0, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v + Fly.mc.field_71439_g.field_70179_y * 11.0);
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "keepalive": {
                Fly.mc.func_147114_u().func_147297_a((Packet)new C00PacketKeepAlive());
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    final EntityPlayerSP field_71439_g5 = Fly.mc.field_71439_g;
                    field_71439_g5.field_70181_x += vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    final EntityPlayerSP field_71439_g6 = Fly.mc.field_71439_g;
                    field_71439_g6.field_70181_x -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "minesecure": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                if (!Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Fly.mc.field_71439_g.field_70181_x = -0.009999999776482582;
                }
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                MovementUtils.strafe(vanillaSpeed);
                if (this.mineSecureVClipTimer.hasTimePassed(150L) && Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 5.0, Fly.mc.field_71439_g.field_70161_v, false));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, -1000.0, 0.5, false));
                    final double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                    final double x = -Math.sin(yaw) * 0.4;
                    final double z = Math.cos(yaw) * 0.4;
                    Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t + x, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v + z);
                    this.mineSecureVClipTimer.reset();
                    break;
                }
                break;
            }
            case "killswitch": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                break;
            }
            case "hawkeye": {
                Fly.mc.field_71439_g.field_70181_x = ((Fly.mc.field_71439_g.field_70181_x <= -0.42) ? 0.42 : -0.42);
                break;
            }
            case "hac": {
                final EntityPlayerSP field_71439_g7 = Fly.mc.field_71439_g;
                field_71439_g7.field_70159_w *= 0.8;
                final EntityPlayerSP field_71439_g8 = Fly.mc.field_71439_g;
                field_71439_g8.field_70179_y *= 0.8;
                Fly.mc.field_71439_g.field_70181_x = ((Fly.mc.field_71439_g.field_70181_x <= -0.42) ? 0.42 : -0.42);
                break;
            }
            case "teleportrewinside": {
                final Vec3 vectorStart = new Vec3(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v);
                final Vec3 vectorEnd = new VectorUtils(vectorStart, -Fly.mc.field_71439_g.field_70177_z, -Fly.mc.field_71439_g.field_70125_A, 9.9).getEndVector();
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 2.0, vectorEnd.field_72449_c, true));
                Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 2.0, vectorStart.field_72449_c, true));
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "minesucht": {
                final double posX = Fly.mc.field_71439_g.field_70165_t;
                final double posY = Fly.mc.field_71439_g.field_70163_u;
                final double posZ = Fly.mc.field_71439_g.field_70161_v;
                if (!Fly.mc.field_71474_y.field_74351_w.func_151470_d()) {
                    break;
                }
                if (System.currentTimeMillis() - this.minesuchtTP > 99L) {
                    final Vec3 vec3 = Fly.mc.field_71439_g.func_174824_e(0.0f);
                    final Vec3 vec4 = Fly.mc.field_71439_g.func_70676_i(0.0f);
                    final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * 7.0, vec4.field_72448_b * 7.0, vec4.field_72449_c * 7.0);
                    if (Fly.mc.field_71439_g.field_70143_R > 0.8) {
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 50.0, posZ, false));
                        Fly.mc.field_71439_g.func_180430_e(100.0f, 100.0f);
                        Fly.mc.field_71439_g.field_70143_R = 0.0f;
                        Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 20.0, posZ, true));
                    }
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec5.field_72450_a, Fly.mc.field_71439_g.field_70163_u + 50.0, vec5.field_72449_c, true));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec5.field_72450_a, posY, vec5.field_72449_c, true));
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    this.minesuchtTP = System.currentTimeMillis();
                    break;
                }
                Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, false));
                Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
                break;
            }
            case "jetpack": {
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Fly.mc.field_71452_i.func_178927_a(EnumParticleTypes.FLAME.func_179348_c(), Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.2, Fly.mc.field_71439_g.field_70161_v, -Fly.mc.field_71439_g.field_70159_w, -0.5, -Fly.mc.field_71439_g.field_70179_y, new int[0]);
                    final EntityPlayerSP field_71439_g9 = Fly.mc.field_71439_g;
                    field_71439_g9.field_70181_x += 0.15;
                    final EntityPlayerSP field_71439_g10 = Fly.mc.field_71439_g;
                    field_71439_g10.field_70159_w *= 1.1;
                    final EntityPlayerSP field_71439_g11 = Fly.mc.field_71439_g;
                    field_71439_g11.field_70179_y *= 1.1;
                    break;
                }
                break;
            }
            case "mineplex": {
                if (Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null) {
                    if (Fly.mc.field_71474_y.field_74314_A.func_151470_d() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 0.6, Fly.mc.field_71439_g.field_70161_v);
                        this.mineplexTimer.reset();
                    }
                    if (Fly.mc.field_71439_g.func_70093_af() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 0.6, Fly.mc.field_71439_g.field_70161_v);
                        this.mineplexTimer.reset();
                    }
                    final BlockPos blockPos = new BlockPos(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.func_174813_aQ().field_72338_b - 1.0, Fly.mc.field_71439_g.field_70161_v);
                    final Vec3 vec6 = new Vec3((Vec3i)blockPos).func_72441_c(0.4000000059604645, 0.4000000059604645, 0.4000000059604645).func_178787_e(new Vec3(EnumFacing.UP.func_176730_m()));
                    Fly.mc.field_71442_b.func_178890_a(Fly.mc.field_71439_g, Fly.mc.field_71441_e, Fly.mc.field_71439_g.field_71071_by.func_70448_g(), blockPos, EnumFacing.UP, new Vec3(vec6.field_72450_a * 0.4000000059604645, vec6.field_72448_b * 0.4000000059604645, vec6.field_72449_c * 0.4000000059604645));
                    MovementUtils.strafe(0.27f);
                    Fly.mc.field_71428_T.field_74278_d = 1.0f + this.mineplexSpeedValue.asFloat();
                    break;
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                this.setState(false);
                ChatUtils.displayChatMessage("§8[§c§lMineplex-§a§lFly§8] §aSelect an empty slot to fly.");
                break;
            }
            case "aac3.3.12": {
                if (Fly.mc.field_71439_g.field_70163_u < -70.0) {
                    Fly.mc.field_71439_g.field_70181_x = this.aacMotion.asFloat();
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                if (Keyboard.isKeyDown(29)) {
                    Fly.mc.field_71428_T.field_74278_d = 0.2f;
                    Fly.mc.field_71467_ac = 0;
                    break;
                }
                break;
            }
            case "aac3.3.12-glide": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay == 2) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f;
                }
                if (this.aac3glideDelay == 12) {
                    Fly.mc.field_71428_T.field_74278_d = 0.1f;
                }
                if (this.aac3glideDelay >= 12 && !Fly.mc.field_71439_g.field_70122_E) {
                    this.aac3glideDelay = 0;
                    Fly.mc.field_71439_g.field_70181_x = 0.015;
                    break;
                }
                break;
            }
            case "aac3.3.13": {
                if (Fly.mc.field_71439_g.field_70128_L) {
                    this.wasDead = true;
                }
                if (this.wasDead || Fly.mc.field_71439_g.field_70122_E) {
                    this.wasDead = false;
                    Fly.mc.field_71439_g.field_70181_x = this.aacMotion2.asFloat();
                    Fly.mc.field_71439_g.field_70122_E = false;
                }
                Fly.mc.field_71428_T.field_74278_d = 1.0f;
                if (Keyboard.isKeyDown(29)) {
                    Fly.mc.field_71428_T.field_74278_d = 0.2f;
                    Fly.mc.field_71467_ac = 0;
                    break;
                }
                break;
            }
            case "watchcat": {
                MovementUtils.strafe(0.15f);
                Fly.mc.field_71439_g.func_70031_b(true);
                if (Fly.mc.field_71439_g.field_70163_u < this.startY + 2.0) {
                    Fly.mc.field_71439_g.field_70181_x = Math.random() * 0.5;
                    break;
                }
                if (this.startY > Fly.mc.field_71439_g.field_70163_u) {
                    MovementUtils.strafe(0.0f);
                    break;
                }
                break;
            }
            case "spartan": {
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                this.spartanTimer.update();
                if (this.spartanTimer.hasTimePassed(12)) {
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 8.0, Fly.mc.field_71439_g.field_70161_v, true));
                    Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u - 8.0, Fly.mc.field_71439_g.field_70161_v, true));
                    this.spartanTimer.reset();
                    break;
                }
                break;
            }
            case "spartan2": {
                MovementUtils.strafe(0.264f);
                if (Fly.mc.field_71439_g.field_70173_aa % 8 == 0) {
                    Fly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 10.0, Fly.mc.field_71439_g.field_70161_v, true));
                    break;
                }
                break;
            }
            case "neruxvace": {
                if (!Fly.mc.field_71439_g.field_70122_E) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay >= this.neruxVaceTicks.asInteger() && !Fly.mc.field_71439_g.field_70122_E) {
                    this.aac3glideDelay = 0;
                    Fly.mc.field_71439_g.field_70181_x = 0.015;
                    break;
                }
                break;
            }
            case "latesthypixel": {
                final int boostDelay = this.hypixelLatestBoostDelayValue.asInteger();
                if (this.hypixelLatestBoostValue.asBoolean() && !this.flyTimer.hasTimePassed(boostDelay)) {
                    Fly.mc.field_71428_T.field_74278_d = 1.0f + this.hypixelLatestBoostTimerValue.asFloat() * (this.flyTimer.hasTimeLeft(boostDelay) / (float)boostDelay);
                }
                this.latestHypixelTimer.update();
                if (this.latestHypixelTimer.hasTimePassed(2)) {
                    Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 1.0E-5, Fly.mc.field_71439_g.field_70161_v);
                    this.latestHypixelTimer.reset();
                    break;
                }
                break;
            }
            case "freehypixel": {
                if (this.freeHypixelTimer.hasTimePassed(10)) {
                    Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                    break;
                }
                Fly.mc.field_71439_g.field_70177_z = this.freeHypixelYaw;
                Fly.mc.field_71439_g.field_70125_A = this.freeHypixelPitch;
                final EntityPlayerSP field_71439_g12 = Fly.mc.field_71439_g;
                final EntityPlayerSP field_71439_g13 = Fly.mc.field_71439_g;
                final EntityPlayerSP field_71439_g14 = Fly.mc.field_71439_g;
                final double field_70159_w = 0.0;
                field_71439_g14.field_70181_x = field_70159_w;
                field_71439_g13.field_70179_y = field_70159_w;
                field_71439_g12.field_70159_w = field_70159_w;
                if (this.startY == new BigDecimal(Fly.mc.field_71439_g.field_70163_u).setScale(3, RoundingMode.HALF_DOWN).doubleValue()) {
                    this.freeHypixelTimer.update();
                    break;
                }
                break;
            }
            case "bugspartan": {
                Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
                Fly.mc.field_71439_g.field_70181_x = 0.0;
                Fly.mc.field_71439_g.field_70159_w = 0.0;
                Fly.mc.field_71439_g.field_70179_y = 0.0;
                if (Fly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    final EntityPlayerSP field_71439_g15 = Fly.mc.field_71439_g;
                    field_71439_g15.field_70181_x += vanillaSpeed;
                }
                if (Fly.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    final EntityPlayerSP field_71439_g16 = Fly.mc.field_71439_g;
                    field_71439_g16.field_70181_x -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (this.modeValue.asString().equalsIgnoreCase("boosthypixel")) {
            switch (event.getEventState()) {
                case PRE: {
                    this.latestHypixelTimer.update();
                    if (this.latestHypixelTimer.hasTimePassed(2)) {
                        Fly.mc.field_71439_g.func_70107_b(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u + 1.0E-5, Fly.mc.field_71439_g.field_70161_v);
                        this.latestHypixelTimer.reset();
                    }
                    Fly.mc.field_71439_g.field_70181_x = 0.0;
                    break;
                }
                case POST: {
                    final double xDist = Fly.mc.field_71439_g.field_70165_t - Fly.mc.field_71439_g.field_70169_q;
                    final double zDist = Fly.mc.field_71439_g.field_70161_v - Fly.mc.field_71439_g.field_70166_s;
                    this.lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final String flyMode = this.modeValue.asString();
        if (!this.markValue.asBoolean() || flyMode.equalsIgnoreCase("Vanilla") || flyMode.equalsIgnoreCase("SmoothVanilla") || flyMode.equalsIgnoreCase("LAAC") || flyMode.equalsIgnoreCase("KillSwitch")) {
            return;
        }
        final double y = this.startY + 2.0;
        RenderUtils.drawPlatform(Fly.mc.field_71439_g.field_70165_t, y, Fly.mc.field_71439_g.field_70161_v, (Fly.mc.field_71439_g.func_174813_aQ().field_72337_e < y) ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
        final String lowerCase = flyMode.toLowerCase();
        switch (lowerCase) {
            case "aac": {
                RenderUtils.drawPlatform(Fly.mc.field_71439_g.field_70165_t, this.startY + this.aacJump, Fly.mc.field_71439_g.field_70161_v, new Color(0, 0, 255, 90), 1.0);
                break;
            }
            case "aac3.3.12": {
                RenderUtils.drawPlatform(Fly.mc.field_71439_g.field_70165_t, -70.0, Fly.mc.field_71439_g.field_70161_v, new Color(0, 0, 255, 90), 1.0);
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (this.noPacketModify) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            final String mode = this.modeValue.asString();
            if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("OtherHypixel") || mode.equalsIgnoreCase("Rewinside") || (mode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null)) {
                packetPlayer.field_149474_g = true;
                if (mode.equalsIgnoreCase("Hypixel")) {
                    if (this.hypixelSwitch) {
                        final C03PacketPlayer c03PacketPlayer = packetPlayer;
                        c03PacketPlayer.field_149477_b += 1.1E-5;
                    }
                    this.hypixelSwitch = !this.hypixelSwitch;
                }
                if (mode.equalsIgnoreCase("OtherHypixel") && this.hypixelTimer.hasTimePassed(100L)) {
                    final C03PacketPlayer c03PacketPlayer2 = packetPlayer;
                    c03PacketPlayer2.field_149477_b += 0.001;
                    this.hypixelTimer.reset();
                }
            }
            if (mode.equalsIgnoreCase("LatestHypixel") || mode.equalsIgnoreCase("BoostHypixel")) {
                packetPlayer.field_149474_g = false;
            }
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "cubecraft": {
                if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                    final double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                    event.x = -Math.sin(yaw) * 2.4;
                    event.z = Math.cos(yaw) * 2.4;
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                final double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                event.x = -Math.sin(yaw) * 0.2;
                event.z = Math.cos(yaw) * 0.2;
                break;
            }
            case "infinitycubecraft":
            case "infinityvcubecraft": {
                if (Fly.mc.field_71439_g.field_70122_E) {
                    break;
                }
                if (this.startY <= Fly.mc.field_71439_g.field_70163_u) {
                    break;
                }
                if (this.cubecraftTeleportTickTimer.hasTimePassed(4) && !Fly.mc.field_71439_g.func_70093_af()) {
                    final double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                    event.x = -Math.sin(yaw) * 2.0;
                    event.z = Math.cos(yaw) * 2.0;
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                final double yaw = Math.toRadians(Fly.mc.field_71439_g.field_70177_z);
                event.x = -Math.sin(yaw) * 0.2;
                event.z = Math.cos(yaw) * 0.2;
                break;
            }
            case "boosthypixel": {
                if (!MovementUtils.isMoving()) {
                    final double n2 = 0.0;
                    event.z = n2;
                    event.x = n2;
                    break;
                }
                final double amplifier = 1.0 + (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? (0.2 * (Fly.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1)) : 0.0);
                final double baseSpeed = 0.29 * amplifier;
                switch (this.boostHypixelState) {
                    case 1: {
                        this.moveSpeed = (Fly.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? 1.56 : 2.034) * baseSpeed;
                        this.boostHypixelState = 2;
                        break;
                    }
                    case 2: {
                        this.moveSpeed *= 2.16;
                        this.boostHypixelState = 3;
                        break;
                    }
                    case 3: {
                        this.moveSpeed = this.lastDistance - ((Fly.mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                        this.boostHypixelState = 4;
                        break;
                    }
                    default: {
                        this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                        break;
                    }
                }
                this.moveSpeed = Math.max(this.moveSpeed, 0.3);
                final double yaw2 = MovementUtils.getDirection();
                final EntityPlayerSP field_71439_g = Fly.mc.field_71439_g;
                final double n3 = -Math.sin(yaw2) * this.moveSpeed;
                event.x = n3;
                field_71439_g.field_70159_w = n3;
                final EntityPlayerSP field_71439_g2 = Fly.mc.field_71439_g;
                final double n4 = Math.cos(yaw2) * this.moveSpeed;
                event.z = n4;
                field_71439_g2.field_70179_y = n4;
                break;
            }
            case "freehypixel": {
                if (!this.freeHypixelTimer.hasTimePassed(10)) {
                    final double x = 0.0;
                    event.z = x;
                    event.y = x;
                    event.x = x;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onBB(final BlockBBEvent event) {
        if (Fly.mc.field_71439_g == null) {
            return;
        }
        final String flyMode = this.modeValue.asString();
        if (event.getBlock() instanceof BlockAir && (flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null)) && event.getY() < Fly.mc.field_71439_g.field_70163_u) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), Fly.mc.field_71439_g.field_70163_u, (double)(event.getZ() + 1)));
        }
    }
    
    @EventTarget
    public void onJump(final JumpEvent e) {
        final String flyMode = this.modeValue.asString();
        if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null)) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onStep(final StepEvent e) {
        final String flyMode = this.modeValue.asString();
        if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.field_71439_g.field_71071_by.func_70448_g() == null)) {
            e.setStepHeight(0.0f);
        }
    }
    
    private void handleVanillaKickBypass() {
        if (!this.vanillaKickBypassValue.asBoolean() || !this.groundTimer.hasTimePassed(1000L)) {
            return;
        }
        final double ground = this.calculateGround();
        for (double posY = Fly.mc.field_71439_g.field_70163_u; posY > ground; posY -= 8.0) {
            Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY - 8.0 < ground) {
                break;
            }
        }
        Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, ground, Fly.mc.field_71439_g.field_70161_v, true));
        for (double posY = ground; posY < Fly.mc.field_71439_g.field_70163_u; posY += 8.0) {
            Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, posY, Fly.mc.field_71439_g.field_70161_v, true));
            if (posY + 8.0 > Fly.mc.field_71439_g.field_70163_u) {
                break;
            }
        }
        Fly.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.field_71439_g.field_70165_t, Fly.mc.field_71439_g.field_70163_u, Fly.mc.field_71439_g.field_70161_v, true));
        this.groundTimer.reset();
    }
    
    private double calculateGround() {
        final AxisAlignedBB playerBoundingBox = Fly.mc.field_71439_g.func_174813_aQ();
        for (double blockHeight = 1.0, ground = Fly.mc.field_71439_g.field_70163_u; ground > 0.0; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.field_72336_d, ground + blockHeight, playerBoundingBox.field_72334_f, playerBoundingBox.field_72340_a, ground, playerBoundingBox.field_72339_c);
            if (Fly.mc.field_71441_e.func_72829_c(customBox)) {
                if (blockHeight <= 0.05) {
                    return ground + blockHeight;
                }
                ground += blockHeight;
                blockHeight = 0.05;
            }
        }
        return 0.0;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
