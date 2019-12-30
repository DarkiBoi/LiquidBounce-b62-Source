// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemSword;
import net.minecraft.world.WorldSettings;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import java.util.Objects;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "KillAura", description = "Automatically attacks targets around you.", category = ModuleCategory.COMBAT, keyBind = 19)
public class KillAura extends Module
{
    private final IntegerValue maxCPS;
    private final IntegerValue minCPS;
    private final IntegerValue hurtTimeValue;
    private final FloatValue rangeValue;
    private final FloatValue throughWallsRangeValue;
    private final FloatValue rangeSprintReducementValue;
    private final ListValue priorityValue;
    private final ListValue targetModeValue;
    private final ListValue multiModeValue;
    private final BoolValue swingValue;
    private final BoolValue keepSprintValue;
    private final BoolValue autoBlockValue;
    private final BoolValue interactAutoBlockValue;
    private final IntegerValue blockRate;
    private final BoolValue aacValue;
    private final BoolValue raycastValue;
    private final BoolValue raycastIgnoredValue;
    private final BoolValue livingRaycastValue;
    private final BoolValue predictValue;
    private final FloatValue maxPredictSize;
    private final FloatValue minPredictSize;
    private final BoolValue noInventoryAttackValue;
    private final IntegerValue noInventoryDelayValue;
    private final FloatValue failRateValue;
    private final IntegerValue limitedMultiTargetsValue;
    private final FloatValue maxTurnSpeed;
    private final FloatValue minTurnSpeed;
    private final BoolValue fakeSwingValue;
    private final BoolValue silentRotationValue;
    private final BoolValue randomCenterValue;
    private final BoolValue outborderValue;
    private final FloatValue fovValue;
    private final BoolValue markValue;
    private final BoolValue fakeSharpValue;
    public EntityLivingBase target;
    private EntityLivingBase currentTarget;
    private boolean hitable;
    private final List<Integer> prevTargetEntities;
    private final MSTimer attackTimer;
    private long attackDelay;
    private long containerOpen;
    public boolean blocking;
    
    public KillAura() {
        this.maxCPS = new IntegerValue("MaxCPS", 8, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = KillAura.this.minCPS.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                KillAura.this.attackDelay = TimeUtils.randomClickDelay(KillAura.this.minCPS.asInteger(), KillAura.this.maxCPS.asInteger());
            }
        };
        this.minCPS = new IntegerValue("MinCPS", 5, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = KillAura.this.maxCPS.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
                KillAura.this.attackDelay = TimeUtils.randomClickDelay(KillAura.this.minCPS.asInteger(), KillAura.this.maxCPS.asInteger());
            }
        };
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
        this.throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
        this.rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
        this.priorityValue = new ListValue("Priority", new String[] { "Health", "Distance", "Direction", "LivingTime" }, "Distance");
        this.targetModeValue = new ListValue("TargetMode", new String[] { "Single", "Switch", "Multi" }, "Switch");
        this.multiModeValue = new ListValue("MultiMode", new String[] { "Vanilla", "NCP" }, "NCP");
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        this.autoBlockValue = new BoolValue("AutoBlock", false);
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
        this.blockRate = new IntegerValue("BlockRate", 100, 1, 100);
        this.aacValue = new BoolValue("AAC", false);
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.predictValue = new BoolValue("Predict", true);
        this.maxPredictSize = new FloatValue("MaxPredictSize", 1.0f, 0.1f, 5.0f) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final float v = KillAura.this.minPredictSize.asFloat();
                if (v > Float.parseFloat(String.valueOf(newValue))) {
                    this.setValue(v);
                }
            }
        };
        this.minPredictSize = new FloatValue("MinPredictSize", 1.0f, 0.1f, 5.0f) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final float v = KillAura.this.maxPredictSize.asFloat();
                if (v < Float.parseFloat(String.valueOf(newValue))) {
                    this.setValue(v);
                }
            }
        };
        this.noInventoryAttackValue = new BoolValue("NoInvAttack", false);
        this.noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
        this.failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
        this.limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 100);
        this.maxTurnSpeed = new FloatValue("MaxTurnSpeed", 180.0f, 1.0f, 180.0f) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final float turn = KillAura.this.minTurnSpeed.asFloat();
                if (turn > Float.parseFloat(String.valueOf(newValue))) {
                    this.setValue(turn);
                }
            }
        };
        this.minTurnSpeed = new FloatValue("MinTurnSpeed", 180.0f, 1.0f, 180.0f) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final float turn = KillAura.this.maxTurnSpeed.asFloat();
                if (turn < Float.parseFloat(String.valueOf(newValue))) {
                    this.setValue(turn);
                }
            }
        };
        this.fakeSwingValue = new BoolValue("FakeSwing", true);
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.randomCenterValue = new BoolValue("RandomCenter", true);
        this.outborderValue = new BoolValue("Outborder", false);
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.markValue = new BoolValue("Mark", true);
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.prevTargetEntities = new ArrayList<Integer>();
        this.attackTimer = new MSTimer();
        this.containerOpen = -1L;
        this.attackDelay = TimeUtils.randomClickDelay(this.minCPS.asInteger(), this.maxCPS.asInteger());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("killaura", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxcps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value < 1) {
                                    this.chat("CPS can't lower than 1.");
                                    return;
                                }
                                if (KillAura.this.minCPS.asInteger() > value) {
                                    this.chat("MinCPS can't higher as MaxCPS!");
                                    return;
                                }
                                KillAura.this.maxCPS.setValue(value);
                                this.chat("§7KillAura maxCPS was set to §8" + value + "§7.");
                                KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".killaura maxcps <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mincps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value < 1) {
                                    this.chat("CPS can't lower than 1.");
                                    return;
                                }
                                if (KillAura.this.maxCPS.asInteger() < value) {
                                    this.chat("MinCPS can't higher as MaxCPS!");
                                    return;
                                }
                                KillAura.this.minCPS.setValue(value);
                                this.chat("§7KillAura MinCPS was set to §8" + value + "§7.");
                                KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".killaura mincps <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("hurttime")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value <= 10) {
                                    KillAura.this.hurtTimeValue.setValue(value);
                                    this.chat("§7KillAura HurtTime was set to §8" + value + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("Value can't higher as 10.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".killaura hurttime <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("range")) {
                        if (args.length > 2) {
                            try {
                                final float range = Float.parseFloat(args[2]);
                                if (range <= 8.0f) {
                                    KillAura.this.rangeValue.setValue(range);
                                    this.chat("§7KillAura Range was set to §8" + range + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("Range can't higher as 8.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".killaura range <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("RangeSprintReducement")) {
                        if (args.length > 2) {
                            try {
                                final float range = Float.parseFloat(args[2]);
                                if (range < 0.0f) {
                                    this.chat("§7Negative value is not allowed.");
                                    return;
                                }
                                KillAura.this.rangeSprintReducementValue.setValue(range);
                                this.chat("§7KillAura RangeSprintReducement was set to §8" + range + "§7.");
                                KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".killaura RangeSprintReducement <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("priority")) {
                        if (args.length > 2 && KillAura.this.priorityValue.contains(args[2])) {
                            KillAura.this.priorityValue.setValue(args[2].toLowerCase());
                            this.chat("§7KillAura priority was set to §8" + KillAura.this.priorityValue.asString().toUpperCase() + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".killaura priority §c<§8" + Strings.join(KillAura.this.priorityValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("targetmode")) {
                        if (args.length > 2 && KillAura.this.targetModeValue.contains(args[2])) {
                            KillAura.this.targetModeValue.setValue(args[2].toLowerCase());
                            this.chat("§7KillAura targetmode was set to §8" + KillAura.this.targetModeValue.asString().toUpperCase() + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".killaura targetmode §c<§8" + Strings.join(KillAura.this.targetModeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("multimode")) {
                        if (args.length > 2 && KillAura.this.multiModeValue.contains(args[2])) {
                            KillAura.this.multiModeValue.setValue(args[2].toLowerCase());
                            this.chat("§7KillAura multimode was set to §8" + KillAura.this.multiModeValue.asString().toUpperCase() + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".killaura multimode §c<§8" + Strings.join(KillAura.this.multiModeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("swing")) {
                            KillAura.this.swingValue.setValue(!KillAura.this.swingValue.asBoolean());
                            this.chat("§7KillAura swing was toggled §8" + (KillAura.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("aac")) {
                            KillAura.this.aacValue.setValue(!KillAura.this.aacValue.asBoolean());
                            this.chat("§7KillAura AAC was toggled §8" + (KillAura.this.aacValue.asBoolean() ? "on" : "off") + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("keepsprint")) {
                            KillAura.this.keepSprintValue.setValue(!KillAura.this.keepSprintValue.asBoolean());
                            this.chat("§7KillAura keepsprint was toggled §8" + (KillAura.this.keepSprintValue.asBoolean() ? "on" : "off") + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("autoblock")) {
                            KillAura.this.autoBlockValue.setValue(!KillAura.this.autoBlockValue.asBoolean());
                            this.chat("§7KillAura autoblock was toggled §8" + (KillAura.this.autoBlockValue.asBoolean() ? "on" : "off") + "§7.");
                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("maxturnspeed")) {
                            if (args.length > 2) {
                                try {
                                    final float turnSpeed = (float)Integer.parseInt(args[2]);
                                    if (turnSpeed <= 180.0f) {
                                        if (turnSpeed <= 0.0f) {
                                            this.chat("TurnSpeed cant lower as 0");
                                            return;
                                        }
                                        KillAura.this.maxTurnSpeed.setValue(turnSpeed);
                                        this.chat("§7KillAura MaxTurnSpeed was set to §8" + turnSpeed + "§7.");
                                        KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    else {
                                        this.chat("TurnSpeed cant higher as 180.");
                                    }
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".killaura maxturnspeed <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("minturnspeed")) {
                            if (args.length > 2) {
                                try {
                                    final float turnSpeed = (float)Integer.parseInt(args[2]);
                                    if (turnSpeed <= 180.0f) {
                                        if (turnSpeed <= 0.0f) {
                                            this.chat("TurnSpeed cant lower as 0");
                                            return;
                                        }
                                        KillAura.this.minTurnSpeed.setValue(turnSpeed);
                                        this.chat("§7KillAura MinTurnSpeed was set to §8" + turnSpeed + "§7.");
                                        KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    else {
                                        this.chat("TurnSpeed cant higher as 180.");
                                    }
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".killaura minturnspeed <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("failrate")) {
                            if (args.length > 2) {
                                try {
                                    final float failRate = Float.parseFloat(args[2]);
                                    if (failRate <= 100.0f) {
                                        if (failRate < 0.0f) {
                                            this.chat("FailRate cant lower as 0.");
                                            return;
                                        }
                                        KillAura.this.failRateValue.setValue(failRate);
                                        this.chat("§7KillAura failrate was set to §8" + failRate + "§7.");
                                        KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    else {
                                        this.chat("FailRate cant higher as 100.");
                                    }
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".killaura failrate <value>");
                            return;
                        }
                        else {
                            if (args[1].equalsIgnoreCase("randomcenter")) {
                                KillAura.this.randomCenterValue.setValue(!KillAura.this.randomCenterValue.asBoolean());
                                this.chat("§7KillAura RandomCenter was toggled §8" + (KillAura.this.randomCenterValue.asBoolean() ? "on" : "off") + "§7.");
                                KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            if (args[1].equalsIgnoreCase("fov")) {
                                if (args.length > 2) {
                                    try {
                                        final float fov = (float)Integer.parseInt(args[2]);
                                        if (fov <= 180.0f) {
                                            if (fov < 0.0f) {
                                                this.chat("FOV cant lower as 0");
                                                return;
                                            }
                                            KillAura.this.fovValue.setValue(fov);
                                            this.chat("§7KillAura fov was set to §8" + fov + "§7.");
                                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                        }
                                        else {
                                            this.chat("FOV cant higher as 180.");
                                        }
                                    }
                                    catch (NumberFormatException exception) {
                                        this.chatSyntaxError();
                                    }
                                    return;
                                }
                                this.chatSyntax(".killaura fov <value>");
                                return;
                            }
                            else {
                                if (args[1].equalsIgnoreCase("raycast")) {
                                    KillAura.this.raycastValue.setValue(!KillAura.this.raycastValue.asBoolean());
                                    this.chat("§7KillAura raycast was toggled §8" + (KillAura.this.raycastValue.asBoolean() ? "on" : "off") + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    return;
                                }
                                if (args[1].equalsIgnoreCase("raycastignored")) {
                                    KillAura.this.raycastIgnoredValue.setValue(!KillAura.this.raycastIgnoredValue.asBoolean());
                                    this.chat("§7KillAura raycast ignored was toggled §8" + (KillAura.this.raycastIgnoredValue.asBoolean() ? "on" : "off") + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    return;
                                }
                                if (args[1].equalsIgnoreCase("livingraycast")) {
                                    KillAura.this.livingRaycastValue.setValue(!KillAura.this.livingRaycastValue.asBoolean());
                                    this.chat("§7KillAura livingraycast was toggled §8" + (KillAura.this.livingRaycastValue.asBoolean() ? "on" : "off") + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    return;
                                }
                                if (args[1].equalsIgnoreCase("NoInvAttack")) {
                                    KillAura.this.noInventoryAttackValue.setValue(!KillAura.this.noInventoryAttackValue.asBoolean());
                                    this.chat("§7KillAura noinvattack was toggled §8" + (KillAura.this.noInventoryAttackValue.asBoolean() ? "on" : "off") + "§7.");
                                    KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    return;
                                }
                                if (args[1].equalsIgnoreCase("NoInvAttackDelay")) {
                                    if (args.length > 2) {
                                        try {
                                            final int value = Integer.parseInt(args[2]);
                                            KillAura.this.noInventoryDelayValue.setValue(value);
                                            this.chat("§7KillAura NoInvAttackDelay was set to §8" + value + "§7.");
                                            KillAura$7.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                        }
                                        catch (NumberFormatException exception) {
                                            this.chatSyntaxError();
                                        }
                                        return;
                                    }
                                    this.chatSyntax(".killaura NoInvAttackDelay <value>");
                                    return;
                                }
                            }
                        }
                    }
                }
                this.chatSyntax(new String[] { "MaxCPS", "MinCPS", "HurtTime", "Range", "Priority", "TargetMode", "MultiMode", "ThroughWalls", "Swing", "KeepSprint", "AutoBlock", "aac", "NoInvAttack", "NoInvAttackDelay", "FailRate", "MaxTurnSpeed", "MinTurnSpeed", "RandomCenter", "FOV" });
            }
        });
    }
    
    @Override
    public void onEnable() {
        if (KillAura.mc.field_71439_g == null || KillAura.mc.field_71441_e == null) {
            return;
        }
        this.updateTarget();
    }
    
    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.stopBlocking();
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (KillAura.mc.field_71439_g == null || this.target == null || this.currentTarget == null || event.getEventState() == EventState.POST || KillAura.mc.field_71439_g.func_175149_v() || !this.isLiving((EntityLivingBase)KillAura.mc.field_71439_g) || ModuleManager.getModule(Blink.class).getState() || ModuleManager.getModule(FreeCam.class).getState()) {
            return;
        }
        AxisAlignedBB axisAlignedBB = this.target.func_174813_aQ();
        if (this.predictValue.asBoolean()) {
            axisAlignedBB = axisAlignedBB.func_72317_d((this.target.field_70165_t - this.target.field_70169_q) * RandomUtils.nextFloat(this.minPredictSize.asFloat(), this.maxPredictSize.asFloat()), (this.target.field_70163_u - this.target.field_70167_r) * RandomUtils.nextFloat(this.minPredictSize.asFloat(), this.maxPredictSize.asFloat()), (this.target.field_70161_v - this.target.field_70166_s) * RandomUtils.nextFloat(this.minPredictSize.asFloat(), this.maxPredictSize.asFloat()));
        }
        final float[] rotations = RotationUtils.getNeededRotations(this.randomCenterValue.asBoolean() ? RotationUtils.getRandomCenter(axisAlignedBB, this.outborderValue.asBoolean() && !this.attackTimer.hasTimePassed(this.attackDelay / 2L)) : RotationUtils.getCenter(axisAlignedBB), this.predictValue.asBoolean());
        final float[] limitRotations = RotationUtils.limitAngleChange(new float[] { RotationUtils.lastLook[0], RotationUtils.lastLook[1] }, rotations, (float)(Math.random() * (this.maxTurnSpeed.asFloat() - this.minTurnSpeed.asFloat()) + this.minTurnSpeed.asFloat()));
        if (this.silentRotationValue.asBoolean()) {
            RotationUtils.setTargetRotation(limitRotations[0], limitRotations[1]);
        }
        else {
            KillAura.mc.field_71439_g.field_70177_z = limitRotations[0];
            KillAura.mc.field_71439_g.field_70125_A = limitRotations[1];
        }
        if (this.attackTimer.hasTimePassed(this.attackDelay) && this.currentTarget.field_70737_aN <= this.hurtTimeValue.asInteger()) {
            final float failRate = this.failRateValue.asFloat();
            final boolean swing = this.swingValue.asBoolean();
            final boolean multi = this.targetModeValue.asString().equalsIgnoreCase("Multi");
            final boolean openInventory = this.aacValue.asBoolean() && KillAura.mc.field_71462_r instanceof GuiInventory;
            final boolean failHit = failRate > 0.0f && RandomUtils.getRandom().nextInt(100) <= failRate;
            if (openInventory) {
                KillAura.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
            }
            if (!this.hitable || failHit) {
                if (swing && (this.fakeSwingValue.asBoolean() || failHit)) {
                    KillAura.mc.field_71439_g.func_71038_i();
                }
            }
            else {
                if (!multi) {
                    this.attackEntity(this.currentTarget);
                    if (openInventory) {
                        KillAura.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    }
                    this.showCriticals(this.currentTarget);
                }
                else {
                    int targets = 0;
                    for (final Entity entity : KillAura.mc.field_71441_e.field_72996_f) {
                        if (this.isSelected(entity) && (!this.multiModeValue.asString().equalsIgnoreCase("NCP") || RotationUtils.getRotationDifference(entity) <= 40.0) && KillAura.mc.field_71439_g.func_70032_d(entity) <= this.getRange(entity)) {
                            final EntityLivingBase multiTarget = (EntityLivingBase)entity;
                            this.attackEntity(multiTarget);
                            this.showCriticals(multiTarget);
                            ++targets;
                            if (this.limitedMultiTargetsValue.asInteger() > 0 && this.limitedMultiTargetsValue.asInteger() <= targets) {
                                break;
                            }
                            continue;
                        }
                    }
                }
                this.prevTargetEntities.add(this.aacValue.asBoolean() ? this.target.func_145782_y() : this.currentTarget.func_145782_y());
                if (this.target.equals((Object)this.currentTarget)) {
                    this.target = null;
                }
            }
            if (openInventory) {
                KillAura.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            this.attackTimer.reset();
            this.attackDelay = TimeUtils.randomClickDelay(this.minCPS.asInteger(), this.maxCPS.asInteger());
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (KillAura.mc.field_71439_g == null || KillAura.mc.field_71439_g.func_175149_v() || !this.isLiving((EntityLivingBase)KillAura.mc.field_71439_g) || ModuleManager.getModule(Blink.class).getState() || ModuleManager.getModule(FreeCam.class).getState()) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (this.noInventoryAttackValue.asBoolean() && (KillAura.mc.field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < this.noInventoryDelayValue.asInteger())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (KillAura.mc.field_71462_r instanceof GuiContainer) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        this.updateTarget();
        if (this.target == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target;
        if (this.raycastValue.asBoolean()) {
            final Entity raycastedEntity = RaycastUtils.raycastEntity(this.rangeValue.asFloat() - (KillAura.mc.field_71439_g.func_70051_ag() ? this.rangeSprintReducementValue.asFloat() : 0.0f), entity -> (!this.livingRaycastValue.asBoolean() || (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))) && (this.isSelected(entity) || this.raycastIgnoredValue.asBoolean() || (this.aacValue.asBoolean() && !KillAura.mc.field_71441_e.func_72839_b(entity, entity.func_174813_aQ()).isEmpty())));
            if (this.raycastValue.asBoolean() && raycastedEntity instanceof EntityLivingBase && (Objects.requireNonNull(ModuleManager.getModule(NoFriends.class)).getState() || !EntityUtils.isFriend(raycastedEntity))) {
                this.currentTarget = (EntityLivingBase)raycastedEntity;
            }
            this.hitable = this.currentTarget.equals((Object)raycastedEntity);
        }
        else {
            this.hitable = RotationUtils.isFaced((Entity)this.currentTarget, this.getRange((Entity)this.currentTarget));
        }
        if (!this.targetModeValue.asString().equalsIgnoreCase("Switch") && this.isSelected((Entity)this.currentTarget)) {
            this.target = this.currentTarget;
        }
        if (!this.targetModeValue.asString().equalsIgnoreCase("Multi") && this.markValue.asBoolean()) {
            RenderUtils.drawPlatform((Entity)this.target, this.hitable ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
        }
    }
    
    private void updateTarget() {
        this.target = null;
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity2 : KillAura.mc.field_71441_e.field_72996_f) {
            if (this.isSelected(entity2) && KillAura.mc.field_71439_g.func_70032_d(entity2) <= this.getRange(entity2) && RotationUtils.getRotationDifference(entity2) <= (float)this.fovValue.asObject() && ((EntityLivingBase)entity2).field_70737_aN <= (int)this.hurtTimeValue.asObject()) {
                targets.add((EntityLivingBase)entity2);
            }
        }
        if (targets.isEmpty()) {
            return;
        }
        if (this.targetModeValue.asString().equalsIgnoreCase("Switch")) {
            targets.removeIf(entityLivingBase -> this.prevTargetEntities.contains(entityLivingBase.func_145782_y()));
            if (targets.isEmpty()) {
                this.prevTargetEntities.clear();
                this.updateTarget();
                return;
            }
        }
        final String lowerCase = this.priorityValue.asString().toLowerCase();
        switch (lowerCase) {
            case "distance": {
                targets.sort(Comparator.comparingDouble(value -> KillAura.mc.field_71439_g.func_70032_d(value)));
                break;
            }
            case "health": {
                targets.sort(Comparator.comparingDouble(EntityLivingBase::func_110143_aJ));
                break;
            }
            case "direction": {
                targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)RotationUtils::getRotationDifference));
                break;
            }
            case "livingtime": {
                targets.sort(Comparator.comparingInt(entity -> -entity.field_70173_aa));
                break;
            }
        }
        if (!targets.isEmpty()) {
            this.target = targets.get(0);
        }
    }
    
    private void showCriticals(final EntityLivingBase target) {
        final Criticals criticals = (Criticals)ModuleManager.getModule(Criticals.class);
        for (int i = 0; i < 3; ++i) {
            if ((KillAura.mc.field_71439_g.field_70143_R > 0.0f && !KillAura.mc.field_71439_g.field_70122_E && !KillAura.mc.field_71439_g.func_70617_f_() && !KillAura.mc.field_71439_g.func_70090_H() && !KillAura.mc.field_71439_g.func_70644_a(Potion.field_76440_q) && KillAura.mc.field_71439_g.field_70154_o == null) || (criticals.getState() && criticals.msTimer.hasTimePassed(criticals.delayValue.asInteger()) && !KillAura.mc.field_71439_g.func_70090_H() && !KillAura.mc.field_71439_g.func_180799_ab() && !KillAura.mc.field_71439_g.field_70134_J)) {
                KillAura.mc.field_71439_g.func_71009_b((Entity)target);
            }
            if (EnchantmentHelper.func_152377_a(KillAura.mc.field_71439_g.func_70694_bm(), target.func_70668_bt()) > 0.0f || this.fakeSharpValue.asBoolean()) {
                KillAura.mc.field_71439_g.func_71047_c((Entity)target);
            }
        }
    }
    
    private boolean isSelected(final Entity entity) {
        if (!(entity instanceof EntityLivingBase) || (!EntityUtils.targetDead && !this.isLiving((EntityLivingBase)entity)) || entity.equals((Object)KillAura.mc.field_71439_g) || (!EntityUtils.targetInvisible && entity.func_82150_aj())) {
            return false;
        }
        if (!EntityUtils.targetPlayer || !(entity instanceof EntityPlayer)) {
            return (EntityUtils.targetMobs && EntityUtils.isMob(entity)) || (EntityUtils.targetAnimals && EntityUtils.isAnimal(entity));
        }
        final EntityPlayer entityPlayer = (EntityPlayer)entity;
        if (AntiBot.isBot((EntityLivingBase)entityPlayer)) {
            return false;
        }
        if (EntityUtils.isFriend((Entity)entityPlayer) && !ModuleManager.getModule(NoFriends.class).getState()) {
            return false;
        }
        if (entityPlayer.func_175149_v()) {
            return false;
        }
        final Teams teams = (Teams)ModuleManager.getModule(Teams.class);
        return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entityPlayer);
    }
    
    public void attackEntity(final EntityLivingBase targetEntity) {
        if (KillAura.mc.field_71439_g.func_70632_aY() || this.blocking) {
            KillAura.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            this.blocking = false;
        }
        LiquidBounce.CLIENT.eventManager.callEvent(new AttackEvent((Entity)targetEntity));
        if (this.swingValue.asBoolean()) {
            KillAura.mc.field_71439_g.func_71038_i();
        }
        KillAura.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)targetEntity, C02PacketUseEntity.Action.ATTACK));
        if (this.keepSprintValue.asBoolean()) {
            if (KillAura.mc.field_71439_g.field_70143_R > 0.0f && !KillAura.mc.field_71439_g.field_70122_E && !KillAura.mc.field_71439_g.func_70617_f_() && !KillAura.mc.field_71439_g.func_70090_H() && !KillAura.mc.field_71439_g.func_70644_a(Potion.field_76440_q) && KillAura.mc.field_71439_g.field_70154_o == null) {
                KillAura.mc.field_71439_g.func_71009_b((Entity)targetEntity);
            }
            if (EnchantmentHelper.func_152377_a(KillAura.mc.field_71439_g.func_70694_bm(), targetEntity.func_70668_bt()) > 0.0f) {
                KillAura.mc.field_71439_g.func_71047_c((Entity)targetEntity);
            }
        }
        else if (KillAura.mc.field_71442_b.func_178889_l() != WorldSettings.GameType.SPECTATOR) {
            KillAura.mc.field_71439_g.func_71059_n((Entity)targetEntity);
        }
        if (KillAura.mc.field_71439_g.func_70632_aY() || (this.autoBlockValue.asBoolean() && KillAura.mc.field_71439_g.func_70694_bm() != null && KillAura.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword)) {
            if (this.blockRate.asInteger() <= 0 || RandomUtils.getRandom().nextInt(100) > this.blockRate.asInteger()) {
                return;
            }
            if (this.interactAutoBlockValue.asBoolean()) {
                KillAura.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)targetEntity, targetEntity.func_174791_d()));
                KillAura.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)targetEntity, C02PacketUseEntity.Action.INTERACT));
            }
            KillAura.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(KillAura.mc.field_71439_g.field_71071_by.func_70448_g()));
            this.blocking = true;
        }
    }
    
    private void stopBlocking() {
        if (this.blocking) {
            KillAura.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            this.blocking = false;
        }
    }
    
    private boolean isLiving(final EntityLivingBase entity) {
        return (entity.func_70089_S() && entity.func_110143_aJ() > 0.0f) || (this.aacValue.asBoolean() && entity.field_70737_aN > 5);
    }
    
    private float getRange(final Entity entity) {
        return (KillAura.mc.field_71439_g.func_70685_l(entity) ? this.rangeValue.asFloat() : this.throughWallsRangeValue.asFloat()) - (KillAura.mc.field_71439_g.func_70051_ag() ? this.rangeSprintReducementValue.asFloat() : 0.0f);
    }
    
    @Override
    public String getTag() {
        return this.targetModeValue.asString();
    }
}
