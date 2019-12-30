// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.StepConfirmEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockAir;
import net.minecraft.world.World;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Step", description = "Allows you to step up blocks.", category = ModuleCategory.MOVEMENT)
public class Step extends Module {
    private final ListValue modeValue;
    private final FloatValue heightValue;
    private final IntegerValue delayValue;
    private boolean isStep;
    private double stepX;
    private double stepY;
    private double stepZ;
    private boolean spartanSwitch;
    private boolean isAACStep;
    private final MSTimer msTimer;

    public Step() {
        this.modeValue = new ListValue("Mode", new String[]{"Vanilla", "Jump", "NCP", "AAC", "LAAC", "AAC3.3.4", "OldNCP", "Spartan", "Rewinside"}, "NCP");
        this.heightValue = new FloatValue("Height", 1.0f, 0.6f, 10.0f);
        this.delayValue = new IntegerValue("Delay", 0, 0, 500);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("step", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("height")) {
                        if (args.length > 2) {
                            try {
                                final float height = Float.parseFloat(args[2]);
                                Step.this.heightValue.setValue(height);
                                this.chat("§7Step height was set to §8" + height + "§7.");
                                Step$1.mc.func_147118_V().func_147682_a((ISound) PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            } catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".step height <value>");
                        return;
                    } else if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Step.this.modeValue.contains(args[2])) {
                            Step.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Step mode was set to §8" + Step.this.modeValue.asString().toUpperCase() + "§7.");
                            Step$1.mc.func_147118_V().func_147682_a((ISound) PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".step mode §c<§8" + Strings.join(Step.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    } else if (args[1].equalsIgnoreCase("delay")) {
                        if (args.length > 2) {
                            try {
                                final int delay = Integer.parseInt(args[2]);
                                Step.this.delayValue.setValue(delay);
                                this.chat("§7Step delay was set to §8" + delay + "§7.");
                                Step$1.mc.func_147118_V().func_147682_a((ISound) PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            } catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".step delay <value>");
                        return;
                    }
                }
                this.chatSyntax(".step <height, mode, delay>");
            }
        });
    }

    @Override
    public void onDisable() {
        if (Step.mc.field_71439_g == null) {
            return;
        }
        Step.mc.field_71439_g.field_70138_W = 0.5f;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "jump": {
                if (Step.mc.field_71439_g.field_70123_F && Step.mc.field_71439_g.field_70122_E && !Step.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Step.mc.field_71439_g.func_70664_aZ();
                    break;
                }
                break;
            }
            case "laac": {
                if (Step.mc.field_71439_g.field_70123_F && !Step.mc.field_71439_g.func_70617_f_() && !Step.mc.field_71439_g.func_70090_H() && !Step.mc.field_71439_g.func_180799_ab() && !Step.mc.field_71439_g.field_70134_J) {
                    if (Step.mc.field_71439_g.field_70122_E && this.msTimer.hasTimePassed(this.delayValue.asInteger())) {
                        this.isStep = true;
                        final EntityPlayerSP field_71439_g = Step.mc.field_71439_g;
                        field_71439_g.field_70181_x += 0.620000001490116;
                        final float f = Step.mc.field_71439_g.field_70177_z * 0.017453292f;
                        final EntityPlayerSP field_71439_g2 = Step.mc.field_71439_g;
                        field_71439_g2.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
                        final EntityPlayerSP field_71439_g3 = Step.mc.field_71439_g;
                        field_71439_g3.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
                        this.msTimer.reset();
                    }
                    Step.mc.field_71439_g.field_70122_E = true;
                    break;
                }
                this.isStep = false;
                break;
            }
            case "aac3.3.4": {
                if (!Step.mc.field_71439_g.field_70123_F || !MovementUtils.isMoving()) {
                    this.isAACStep = false;
                    break;
                }
                if (Step.mc.field_71439_g.field_70122_E) {
                    final double yaw = Math.toRadians(Step.mc.field_71439_g.field_70177_z);
                    final double x = -Math.sin(yaw) * 1.0;
                    final double z = Math.cos(yaw) * 1.0;
                    final BlockPos blockPos = new BlockPos(Step.mc.field_71439_g.field_70165_t + x, Step.mc.field_71439_g.field_70163_u + 1.0, Step.mc.field_71439_g.field_70161_v + z);
                    final Block block = BlockUtils.getBlock(blockPos);
                    final AxisAlignedBB axisAlignedBB = block.func_180640_a((World) Step.mc.field_71441_e, blockPos, BlockUtils.getState(blockPos));
                    if (!(BlockUtils.getBlock(new BlockPos(Step.mc.field_71439_g.field_70165_t + x, Step.mc.field_71439_g.field_70163_u, Step.mc.field_71439_g.field_70161_v + z)) instanceof BlockAir) && (axisAlignedBB == null || block instanceof BlockSnow)) {
                        final EntityPlayerSP field_71439_g4 = Step.mc.field_71439_g;
                        field_71439_g4.field_70159_w *= 1.26;
                        final EntityPlayerSP field_71439_g5 = Step.mc.field_71439_g;
                        field_71439_g5.field_70179_y *= 1.26;
                        Step.mc.field_71439_g.func_70664_aZ();
                        this.isAACStep = true;
                    }
                }
                if (this.isAACStep && Step.mc.field_71439_g.field_70702_br == 0.0f) {
                    final EntityPlayerSP field_71439_g6 = Step.mc.field_71439_g;
                    field_71439_g6.field_70181_x -= 0.015;
                    Step.mc.field_71439_g.field_70747_aH = 0.3f;
                    break;
                }
                break;
            }
        }
    }

    @EventTarget
    public void onStep(final StepEvent event) {
        if (Step.mc.field_71439_g == null) {
            return;
        }
        if (ModuleManager.getModule(Phase.class).getState()) {
            event.setStepHeight(0.0f);
            return;
        }
        final Fly fly = (Fly) ModuleManager.getModule(Fly.class);
        if (fly.getState()) {
            final String flyMode = fly.modeValue.asString();
            if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Step.mc.field_71439_g.field_71071_by.func_70448_g() == null)) {
                event.setStepHeight(0.0f);
                return;
            }
        }
        final String mode = this.modeValue.asString();
        if (!Step.mc.field_71439_g.field_70122_E || !this.msTimer.hasTimePassed(this.delayValue.asInteger()) || mode.equalsIgnoreCase("Jump") || mode.equalsIgnoreCase("LAAC") || mode.equalsIgnoreCase("AAC3.3.4")) {
            event.setStepHeight(Step.mc.field_71439_g.field_70138_W = 0.5f);
            return;
        }
        final float height = this.heightValue.asFloat();
        event.setStepHeight(Step.mc.field_71439_g.field_70138_W = height);
        if (event.getStepHeight() > 0.5f) {
            this.isStep = true;
            this.stepX = Step.mc.field_71439_g.field_70165_t;
            this.stepY = Step.mc.field_71439_g.field_70163_u;
            this.stepZ = Step.mc.field_71439_g.field_70161_v;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onStepConfirm(final StepConfirmEvent event) {
        if (this.isStep) {
            final String mode = this.modeValue.asString();
            if (Step.mc.field_71439_g.func_174813_aQ().field_72338_b - this.stepY > 0.5) {
                if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("AAC")) {
                    Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    this.msTimer.reset();
                } else if (mode.equalsIgnoreCase("Spartan")) {
                    if (this.spartanSwitch) {
                        Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                        Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                        Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                    } else {
                        Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                    }
                    this.spartanSwitch = !this.spartanSwitch;
                    this.msTimer.reset();
                } else if (mode.equalsIgnoreCase("Rewinside")) {
                    Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    Step.mc.func_147114_u().func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                    this.msTimer.reset();
                }
            }
            this.isStep = false;
            this.stepX = 0.0;
            this.stepY = 0.0;
            this.stepZ = 0.0;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
            if (this.isStep && this.modeValue.asString().equalsIgnoreCase("OldNCP")) {
                final C03PacketPlayer c03PacketPlayer = packetPlayer;
                c03PacketPlayer.field_149477_b += 0.07;
                this.isStep = false;
            }
        }
    }

    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
