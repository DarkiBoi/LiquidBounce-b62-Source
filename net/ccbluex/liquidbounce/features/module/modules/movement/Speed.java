// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.List;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.CustomSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.SlowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.MineplexGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.HypixelHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.HiveHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.TeleportCubeCraft;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreOnGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreLowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan.SpartanYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.OldAACBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACYPort2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACGround2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop3;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACHop350;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACHop3313;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC7BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC6BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC5BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC3BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC2BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.OnGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.MiJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.Frame;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.Boost;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.YPort2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.YPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.SNCPBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPFHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPBHop;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Speed", description = "Allows you to move faster.", category = ModuleCategory.MOVEMENT)
public class Speed extends Module
{
    private final SpeedMode[] speedModes;
    public final ListValue modeValue;
    public final FloatValue customSpeedValue;
    public final FloatValue customYValue;
    public final FloatValue customTimerValue;
    public final BoolValue customStrafeValue;
    public final BoolValue resetXZValue;
    public final BoolValue resetYValue;
    public final FloatValue portMax;
    public final FloatValue aacGroundTimerValue;
    public final FloatValue cubecraftPortLengthValue;
    public final FloatValue mineplexGroundSpeedValue;
    
    public Speed() {
        this.speedModes = new SpeedMode[] { new NCPBHop(), new NCPFHop(), new SNCPBHop(), new NCPHop(), new YPort(), new YPort2(), new NCPYPort(), new Boost(), new Frame(), new MiJump(), new OnGround(), new AACBHop(), new AAC2BHop(), new AAC3BHop(), new AAC4BHop(), new AAC5BHop(), new AAC6BHop(), new AAC7BHop(), new AACHop3313(), new AACHop350(), new AACLowHop(), new AACLowHop2(), new AACLowHop3(), new AACGround(), new AACGround2(), new AACYPort(), new AACYPort2(), new AACPort(), new OldAACBHop(), new SpartanYPort(), new SpectreLowHop(), new SpectreBHop(), new SpectreOnGround(), new TeleportCubeCraft(), new HiveHop(), new HypixelHop(), new MineplexGround(), new SlowHop(), new CustomSpeed() };
        this.modeValue = new ListValue("Mode", this.getModes(), "NCPBHop") {
            @Override
            protected void onChange(final Object oldValue, final Object newValue) {
                if (Speed.this.getState()) {
                    Speed.this.onDisable();
                }
            }
            
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                if (Speed.this.getState()) {
                    Speed.this.onEnable();
                }
            }
        };
        this.customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
        this.customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
        this.customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
        this.customStrafeValue = new BoolValue("CustomStrafe", true);
        this.resetXZValue = new BoolValue("CustomResetXZ", false);
        this.resetYValue = new BoolValue("CustomResetY", false);
        this.portMax = new FloatValue("AAC-PortLength", 1.0f, 1.0f, 20.0f);
        this.aacGroundTimerValue = new FloatValue("AACGround-Timer", 3.0f, 1.1f, 10.0f);
        this.cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1.0f, 0.1f, 2.0f);
        this.mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5f, 0.1f, 1.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("speed", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Speed.this.modeValue.contains(args[2])) {
                            Speed.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Speed mode was set to §8" + Speed.this.modeValue.asString().toUpperCase() + "§7.");
                            Speed$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".speed mode §c<§8" + Strings.join(Speed.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("customspeed")) {
                        if (args.length > 2) {
                            if (args[2].equalsIgnoreCase("speed")) {
                                if (args.length > 3) {
                                    try {
                                        final float speed = Float.parseFloat(args[3]);
                                        Speed.this.customSpeedValue.setValue(speed);
                                        this.chat("§7CustomSpeed speed was set to §8" + speed + "§7.");
                                        Speed$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    catch (NumberFormatException exception) {
                                        this.chatSyntaxError();
                                    }
                                    return;
                                }
                                this.chatSyntax(".speed customspeed speed <value>");
                                return;
                            }
                            else if (args[2].equalsIgnoreCase("y")) {
                                if (args.length > 3) {
                                    try {
                                        final float y = Float.parseFloat(args[3]);
                                        Speed.this.customYValue.setValue(y);
                                        this.chat("§7CustomSpeed y was set to §8" + y + "§7.");
                                        Speed$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    catch (NumberFormatException exception) {
                                        this.chatSyntaxError();
                                    }
                                    return;
                                }
                                this.chatSyntax(".speed customspeed y <value>");
                                return;
                            }
                            else if (args[2].equalsIgnoreCase("timer")) {
                                if (args.length > 3) {
                                    try {
                                        final float y = Float.parseFloat(args[3]);
                                        Speed.this.customTimerValue.setValue(y);
                                        this.chat("§7CustomSpeed timer was set to §8" + y + "§7.");
                                        Speed$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                    }
                                    catch (NumberFormatException exception) {
                                        this.chatSyntaxError();
                                    }
                                    return;
                                }
                                this.chatSyntax(".speed customspeed timer <value>");
                                return;
                            }
                            else if (args[2].equalsIgnoreCase("strafe")) {
                                Speed.this.customStrafeValue.setValue(!Speed.this.customStrafeValue.asBoolean());
                                this.chat("§7Speed customstrafe was toggled §8" + (Speed.this.customStrafeValue.asBoolean() ? "on" : "off") + "§7.");
                                Speed$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                        }
                        this.chatSyntax(".speed customspeed <speed, y, strafe, timer>");
                        this.chat("§7- §cValues §7-");
                        this.chat("§8Speed§7> §c" + Speed.this.customSpeedValue.asFloat());
                        this.chat("§8Y§7> §c" + Speed.this.customYValue.asFloat());
                        this.chat("§8Timer§7> §c" + Speed.this.customTimerValue.asFloat());
                        this.chat("§8Strafe§7> §c" + Speed.this.customStrafeValue.asBoolean());
                        return;
                    }
                }
                this.chatSyntax(".speed <mode, customspeed>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc.field_71439_g.func_70031_b(true);
        }
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onUpdate();
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af() || event.getEventState() != EventState.PRE) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc.field_71439_g.func_70031_b(true);
        }
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onMotion();
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onMove(event);
        }
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onTick();
        }
    }
    
    @Override
    public void onEnable() {
        if (Speed.mc.field_71439_g == null) {
            return;
        }
        Speed.mc.field_71428_T.field_74278_d = 1.0f;
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onEnable();
        }
    }
    
    @Override
    public void onDisable() {
        if (Speed.mc.field_71439_g == null) {
            return;
        }
        Speed.mc.field_71428_T.field_74278_d = 1.0f;
        final SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onDisable();
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
    
    private SpeedMode getMode() {
        final String mode = this.modeValue.asString();
        for (final SpeedMode speedMode : this.speedModes) {
            if (speedMode.modeName.equalsIgnoreCase(mode)) {
                return speedMode;
            }
        }
        return null;
    }
    
    private String[] getModes() {
        final List<String> list = new ArrayList<String>();
        for (final SpeedMode speedMode : this.speedModes) {
            list.add(speedMode.modeName);
        }
        return list.toArray(new String[0]);
    }
}
