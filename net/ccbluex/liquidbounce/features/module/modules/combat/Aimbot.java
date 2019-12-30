// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Aimbot", description = "Automatically faces selected entities around you.", category = ModuleCategory.COMBAT)
public class Aimbot extends Module
{
    private final FloatValue rangeValue;
    private final FloatValue turnSpeedValue;
    private final FloatValue fovValue;
    private final BoolValue onClickValue;
    private final BoolValue jitterValue;
    private final MSTimer clickTimer;
    
    public Aimbot() {
        this.rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f);
        this.turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f);
        this.fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f);
        this.onClickValue = new BoolValue("OnClick", false);
        this.jitterValue = new BoolValue("Jitter", false);
        this.clickTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("aimbot", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("range")) {
                        if (args.length > 2) {
                            try {
                                final float range = Float.parseFloat(args[2]);
                                Aimbot.this.rangeValue.setValue(range);
                                this.chat("§7Aimbot range was set to §8" + range + "§7.");
                                Aimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".aimbot range <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("turnspeed")) {
                        if (args.length > 2) {
                            try {
                                final float turnSpeed = (float)Integer.parseInt(args[2]);
                                if (turnSpeed <= 180.0f) {
                                    if (turnSpeed <= 0.0f) {
                                        this.chat("TurnSpeed cant lower as 0");
                                        return;
                                    }
                                    Aimbot.this.turnSpeedValue.setValue(turnSpeed);
                                    this.chat("§7Aimbot turnSpeed was set to §8" + turnSpeed + "§7.");
                                    Aimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
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
                        this.chatSyntax(".aimbot turnSpeed <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("fov")) {
                        if (args.length > 2) {
                            try {
                                final float fov = (float)Integer.parseInt(args[2]);
                                if (fov <= 180.0f) {
                                    if (fov <= 0.0f) {
                                        this.chat("TurnSpeed cant lower as 0");
                                        return;
                                    }
                                    Aimbot.this.fovValue.setValue(fov);
                                    this.chat("§7Aimbot fov was set to §8" + fov + "§7.");
                                    Aimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
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
                        this.chatSyntax(".aimbot fov <value>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("onclick")) {
                            Aimbot.this.onClickValue.setValue(!Aimbot.this.onClickValue.asBoolean());
                            this.chat("§7Aimbot onclick was toggled §8" + (Aimbot.this.onClickValue.asBoolean() ? "on" : "off") + "§7.");
                            Aimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("jitter")) {
                            Aimbot.this.jitterValue.setValue(!Aimbot.this.jitterValue.asBoolean());
                            this.chat("§7Aimbot jitter was toggled §8" + (Aimbot.this.jitterValue.asBoolean() ? "on" : "off") + "§7.");
                            Aimbot$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                    }
                }
                this.chatSyntax(".aimbot <range, turnspeed, fov, onclick, jitter>");
            }
        });
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() != EventState.PRE) {
            return;
        }
        if (Aimbot.mc.field_71474_y.field_74312_F.func_151470_d()) {
            this.clickTimer.reset();
        }
        if (this.onClickValue.asBoolean() && this.clickTimer.hasTimePassed(500L)) {
            return;
        }
        final float range = this.rangeValue.asFloat();
        final float n;
        final Entity[] entities = (Entity[])Aimbot.mc.field_71441_e.field_72996_f.stream().filter(entity -> EntityUtils.isSelected(entity, true) && Aimbot.mc.field_71439_g.func_70685_l(entity) && Aimbot.mc.field_71439_g.func_70032_d(entity) <= n && RotationUtils.getRotationDifference(entity) <= this.fovValue.asFloat()).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)RotationUtils::getRotationDifference)).toArray(Entity[]::new);
        if (entities.length <= 0) {
            return;
        }
        final float[] rotations = RotationUtils.limitAngleChange(new float[] { Aimbot.mc.field_71439_g.field_70177_z, Aimbot.mc.field_71439_g.field_70125_A }, RotationUtils.getNeededRotations(RotationUtils.getCenter(entities[0].func_174813_aQ()), true), (float)(this.turnSpeedValue.asFloat() + Math.random()));
        if (rotations == null) {
            return;
        }
        Aimbot.mc.field_71439_g.field_70177_z = rotations[0];
        Aimbot.mc.field_71439_g.field_70125_A = rotations[1];
        if (this.jitterValue.asBoolean()) {
            final boolean yaw = RandomUtils.getRandom().nextBoolean();
            final boolean pitch = RandomUtils.getRandom().nextBoolean();
            final boolean yawNegative = RandomUtils.getRandom().nextBoolean();
            final boolean pitchNegative = RandomUtils.getRandom().nextBoolean();
            if (yaw) {
                final EntityPlayerSP field_71439_g = Aimbot.mc.field_71439_g;
                field_71439_g.field_70177_z += (yawNegative ? (-RandomUtils.nextFloat(0.0f, 1.0f)) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (pitch) {
                final EntityPlayerSP field_71439_g2 = Aimbot.mc.field_71439_g;
                field_71439_g2.field_70125_A += (pitchNegative ? (-RandomUtils.nextFloat(0.0f, 1.0f)) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (Aimbot.mc.field_71439_g.field_70125_A > 90.0f) {
                    Aimbot.mc.field_71439_g.field_70125_A = 90.0f;
                }
                else if (Aimbot.mc.field_71439_g.field_70125_A < -90.0f) {
                    Aimbot.mc.field_71439_g.field_70125_A = -90.0f;
                }
            }
        }
    }
}
