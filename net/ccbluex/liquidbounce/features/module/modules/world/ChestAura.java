// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.Map;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;
import java.util.Comparator;
import net.minecraft.util.Vec3;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.ccbluex.liquidbounce.valuesystem.types.BlockValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ChestAura", description = "Automatically opens chests around you.", category = ModuleCategory.WORLD)
public class ChestAura extends Module
{
    private final FloatValue rangeValue;
    private final IntegerValue delayValue;
    private final BoolValue throughWallsValue;
    private final BoolValue visualSwing;
    private final BlockValue chestValue;
    public static final List<BlockPos> ALREADY_OPENED_BLOCKS;
    private BlockPos blockPos;
    private final MSTimer msTimer;
    
    public ChestAura() {
        this.rangeValue = new FloatValue("Range", 5.0f, 1.0f, 6.0f);
        this.delayValue = new IntegerValue("Delay", 100, 50, 200);
        this.throughWallsValue = new BoolValue("ThroughWalls", true);
        this.visualSwing = new BoolValue("VisualSwing", true);
        this.chestValue = new BlockValue("Chest", Block.func_149682_b((Block)Blocks.field_150486_ae));
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("chestaura", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("delay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                ChestAura.this.delayValue.setValue(value);
                                this.chat("§7ChestAura delay was set to §8" + value + "§7.");
                                ChestAura$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".chestaura delay <value>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("throughwalls")) {
                            ChestAura.this.throughWallsValue.setValue(!ChestAura.this.throughWallsValue.asBoolean());
                            this.chat("§7ChestAura throughwalls was toggled §8" + (ChestAura.this.throughWallsValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestAura$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("VisualSwing")) {
                            ChestAura.this.visualSwing.setValue(!ChestAura.this.visualSwing.asBoolean());
                            this.chat("§7ChestAura VisualSwing was toggled §8" + (ChestAura.this.throughWallsValue.asBoolean() ? "on" : "off") + "§7.");
                            ChestAura$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("chest")) {
                            if (args.length > 2) {
                                int id;
                                try {
                                    id = Integer.parseInt(args[2]);
                                }
                                catch (NumberFormatException exception2) {
                                    id = Block.func_149682_b(Block.func_149684_b(args[2]));
                                }
                                if (id != 0) {
                                    ChestAura.this.chestValue.setValue(id);
                                    this.chat("§aThe block was set to " + BlockUtils.getBlockName(id) + ".");
                                    ChestAura$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("Block was not found or the block is air.");
                                }
                                return;
                            }
                            this.chat("§7Current: §8" + BlockUtils.getBlockName(ChestAura.this.chestValue.asInteger()));
                            this.chatSyntax(".chestaura chest <name/id>");
                            return;
                        }
                    }
                }
                this.chatSyntax(".chestaura <delay, throughwalls, visualswing, chest>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        if (ChestAura.mc.field_71439_g == null) {
            return;
        }
        ChestAura.ALREADY_OPENED_BLOCKS.clear();
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (ModuleManager.getModule(Blink.class).getState()) {
            return;
        }
        switch (event.getEventState()) {
            case PRE: {
                if (ChestAura.mc.field_71462_r instanceof GuiContainer) {
                    this.msTimer.reset();
                }
                final Vec3 eyesPos = RotationUtils.getEyesPos();
                final int radius = (int)(this.rangeValue.asFloat() + 1.0f);
                BlockPos blockPos;
                final Vec3 vec3;
                MovingObjectPosition movingObjectPosition;
                BlockUtils.searchBlocks(radius).entrySet().stream().filter(entry -> Block.func_149682_b((Block)entry.getValue()) == this.chestValue.asInteger() && !ChestAura.ALREADY_OPENED_BLOCKS.contains(entry.getKey()) && BlockUtils.getCenterDistance((BlockPos)entry.getKey()) < this.rangeValue.asFloat()).filter(entry -> {
                    if (this.throughWallsValue.asBoolean()) {
                        return true;
                    }
                    else {
                        blockPos = entry.getKey();
                        movingObjectPosition = ChestAura.mc.field_71441_e.func_147447_a(vec3, new Vec3(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5), false, true, false);
                        return movingObjectPosition != null && movingObjectPosition.func_178782_a().equals((Object)blockPos);
                    }
                }).min(Comparator.comparingDouble(value -> BlockUtils.getCenterDistance(value.getKey()))).ifPresent(entry -> this.blockPos = entry.getKey());
                break;
            }
            case POST: {
                if (this.blockPos != null && this.msTimer.hasTimePassed(this.delayValue.asInteger()) && ChestAura.mc.field_71442_b.func_178890_a(ChestAura.mc.field_71439_g, ChestAura.mc.field_71441_e, ChestAura.mc.field_71439_g.func_70694_bm(), this.blockPos, EnumFacing.DOWN, new Vec3((double)this.blockPos.func_177958_n(), (double)this.blockPos.func_177956_o(), (double)this.blockPos.func_177952_p()))) {
                    if (this.visualSwing.asBoolean()) {
                        ChestAura.mc.field_71439_g.func_71038_i();
                    }
                    else {
                        ChestAura.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                    }
                    ChestAura.ALREADY_OPENED_BLOCKS.add(this.blockPos);
                    this.blockPos = null;
                    this.msTimer.reset();
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && this.blockPos != null) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            final double diffX = this.blockPos.func_177958_n() + 0.5 - ChestAura.mc.field_71439_g.field_70165_t;
            final double diffY = this.blockPos.func_177956_o() + 0.5 - (ChestAura.mc.field_71439_g.field_70163_u + ChestAura.mc.field_71439_g.func_70047_e());
            final double diffZ = this.blockPos.func_177952_p() + 0.5 - ChestAura.mc.field_71439_g.field_70161_v;
            final double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
            packetPlayer.field_149476_e = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            packetPlayer.field_149473_f = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            packetPlayer.field_149481_i = true;
        }
    }
    
    static {
        ALREADY_OPENED_BLOCKS = new ArrayList<BlockPos>();
    }
}
