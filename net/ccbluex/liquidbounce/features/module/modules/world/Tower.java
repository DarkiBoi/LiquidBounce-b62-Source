// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Tower", description = "Automatically builds a tower beneath you.", category = ModuleCategory.WORLD, keyBind = 24)
public class Tower extends Module
{
    private final ListValue modeValue;
    private final BoolValue autoBlockValue;
    private final BoolValue stayAutoBlock;
    private final BoolValue swingValue;
    private final BoolValue stopWhenBlockAbove;
    private final FloatValue timerValue;
    private final FloatValue jumpMotionValue;
    private final IntegerValue jumpDelayValue;
    private final FloatValue constantMotionValue;
    private final FloatValue constantMotionJumpGroundValue;
    private final FloatValue teleportHeightValue;
    private final IntegerValue teleportDelayValue;
    private final BoolValue teleportGroundValue;
    private final BoolValue teleportNoMotionValue;
    private final BoolValue counterDisplayValue;
    private PlaceInfo placeInfo;
    private final TickTimer packetTimer;
    private final TickTimer teleportTimer;
    private final TickTimer jumpTimer;
    private double jumpGround;
    private int slot;
    
    public Tower() {
        this.modeValue = new ListValue("Mode", new String[] { "Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9" }, "Motion");
        this.autoBlockValue = new BoolValue("AutoBlock", true);
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.swingValue = new BoolValue("Swing", true);
        this.stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.0f, 10.0f);
        this.jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
        this.jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
        this.constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
        this.constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
        this.teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
        this.teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
        this.teleportGroundValue = new BoolValue("TeleportGround", true);
        this.teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.packetTimer = new TickTimer();
        this.teleportTimer = new TickTimer();
        this.jumpTimer = new TickTimer();
        this.jumpGround = 0.0;
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("tower", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Tower.this.modeValue.contains(args[2])) {
                            Tower.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Tower mode was set to §8" + Tower.this.modeValue.asString().toUpperCase() + "§7.");
                            Tower$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".tower mode §c<§8" + Strings.join(Tower.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("autoblock")) {
                            Tower.this.autoBlockValue.setValue(!Tower.this.autoBlockValue.asBoolean());
                            this.chat("§7Tower autoblock was toggled §8" + (Tower.this.autoBlockValue.asBoolean() ? "on" : "off") + "§7.");
                            Tower$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("swing")) {
                            Tower.this.swingValue.setValue(!Tower.this.swingValue.asBoolean());
                            this.chat("§7Tower swing was toggled §8" + (Tower.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                            Tower$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                    }
                }
                this.chatSyntax(".tower <mode, autoblock, swing>");
            }
        });
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        Tower.mc.field_71428_T.field_74278_d = this.timerValue.asFloat();
        switch (event.getEventState()) {
            case PRE: {
                this.packetTimer.update();
                this.teleportTimer.update();
                this.jumpTimer.update();
                if (this.autoBlockValue.asBoolean()) {
                    if (this.findBlock() == -1) {
                        break;
                    }
                }
                else if (Tower.mc.field_71439_g.func_70694_bm() == null || !(Tower.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
                    break;
                }
                if (!this.stopWhenBlockAbove.asBoolean() || BlockUtils.getBlock(new BlockPos(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 2.0, Tower.mc.field_71439_g.field_70161_v)) instanceof BlockAir) {
                    final String lowerCase = this.modeValue.asString().toLowerCase();
                    switch (lowerCase) {
                        case "jump": {
                            if (Tower.mc.field_71439_g.field_70122_E && this.jumpTimer.hasTimePassed(this.jumpDelayValue.asInteger())) {
                                Tower.mc.field_71439_g.field_70181_x = this.jumpMotionValue.asFloat();
                                this.jumpTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "motion": {
                            if (Tower.mc.field_71439_g.field_70122_E) {
                                Tower.mc.field_71439_g.field_70181_x = 0.42;
                                break;
                            }
                            if (Tower.mc.field_71439_g.field_70181_x < 0.1) {
                                Tower.mc.field_71439_g.field_70181_x = -0.3;
                                break;
                            }
                            break;
                        }
                        case "motiontp": {
                            if (Tower.mc.field_71439_g.field_70122_E) {
                                Tower.mc.field_71439_g.field_70181_x = 0.42;
                                break;
                            }
                            if (Tower.mc.field_71439_g.field_70181_x < 0.23) {
                                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, (double)(int)Tower.mc.field_71439_g.field_70163_u, Tower.mc.field_71439_g.field_70161_v);
                                break;
                            }
                            break;
                        }
                        case "packet": {
                            if (Tower.mc.field_71439_g.field_70122_E && this.packetTimer.hasTimePassed(2)) {
                                Tower.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 0.42, Tower.mc.field_71439_g.field_70161_v, false));
                                Tower.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 0.753, Tower.mc.field_71439_g.field_70161_v, false));
                                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 1.0, Tower.mc.field_71439_g.field_70161_v);
                                this.packetTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "teleport": {
                            if (this.teleportNoMotionValue.asBoolean()) {
                                Tower.mc.field_71439_g.field_70181_x = 0.0;
                            }
                            if ((Tower.mc.field_71439_g.field_70122_E || !this.teleportGroundValue.asBoolean()) && this.teleportTimer.hasTimePassed(this.teleportDelayValue.asInteger())) {
                                Tower.mc.field_71439_g.func_70634_a(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + this.teleportHeightValue.asFloat(), Tower.mc.field_71439_g.field_70161_v);
                                this.teleportTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "constantmotion": {
                            if (Tower.mc.field_71439_g.field_70122_E) {
                                this.jumpGround = Tower.mc.field_71439_g.field_70163_u;
                                Tower.mc.field_71439_g.field_70181_x = this.constantMotionValue.asFloat();
                            }
                            if (Tower.mc.field_71439_g.field_70163_u > this.jumpGround + this.constantMotionJumpGroundValue.asFloat()) {
                                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, (double)(int)Tower.mc.field_71439_g.field_70163_u, Tower.mc.field_71439_g.field_70161_v);
                                Tower.mc.field_71439_g.field_70181_x = this.constantMotionValue.asFloat();
                                this.jumpGround = Tower.mc.field_71439_g.field_70163_u;
                                break;
                            }
                            break;
                        }
                        case "aac3.3.9": {
                            if (Tower.mc.field_71439_g.field_70122_E) {
                                Tower.mc.field_71439_g.field_70181_x = 0.4001;
                            }
                            Tower.mc.field_71428_T.field_74278_d = 1.0f;
                            if (Tower.mc.field_71439_g.field_70181_x < 0.0) {
                                final EntityPlayerSP field_71439_g = Tower.mc.field_71439_g;
                                field_71439_g.field_70181_x -= 9.45E-6;
                                Tower.mc.field_71428_T.field_74278_d = 1.6f;
                                break;
                            }
                            break;
                        }
                    }
                }
                final BlockPos blockPos = new BlockPos(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u - 0.5, Tower.mc.field_71439_g.field_70161_v);
                if (Tower.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150350_a && (this.placeInfo = PlaceInfo.get(blockPos)) != null) {
                    RotationUtils.faceBlockPacket(blockPos);
                }
                break;
            }
            case POST: {
                if (this.placeInfo == null) {
                    return;
                }
                int blockSlot = Integer.MAX_VALUE;
                ItemStack itemStack = Tower.mc.field_71439_g.func_70694_bm();
                if (Tower.mc.field_71439_g.func_70694_bm() == null || !(Tower.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
                    if (!this.autoBlockValue.asBoolean()) {
                        return;
                    }
                    blockSlot = this.findBlock();
                    if (blockSlot == -1) {
                        return;
                    }
                    Tower.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                    itemStack = Tower.mc.field_71439_g.field_71069_bz.func_75139_a(blockSlot).func_75211_c();
                }
                if (Tower.mc.field_71442_b.func_178890_a(Tower.mc.field_71439_g, Tower.mc.field_71441_e, itemStack, this.placeInfo.getBlockPos(), this.placeInfo.getEnumFacing(), this.placeInfo.getVec3())) {
                    if (this.swingValue.asBoolean()) {
                        Tower.mc.field_71439_g.func_71038_i();
                    }
                    else {
                        Tower.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                    }
                }
                this.placeInfo = null;
                if (!this.stayAutoBlock.asBoolean() && blockSlot != Integer.MAX_VALUE) {
                    Tower.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Tower.mc.field_71439_g.field_71071_by.field_70461_c));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (Tower.mc.field_71439_g == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.func_149614_c();
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (this.counterDisplayValue.asBoolean()) {
            GlStateManager.func_179094_E();
            final BlockOverlay blockOverlay = (BlockOverlay)ModuleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.infoValue.asBoolean() && Tower.mc.field_71476_x != null && Tower.mc.field_71476_x.func_178782_a() != null && Tower.mc.field_71441_e.func_180495_p(Tower.mc.field_71476_x.func_178782_a()).func_177230_c() != null && BlockUtils.canBeClicked(Tower.mc.field_71476_x.func_178782_a()) && Tower.mc.field_71441_e.func_175723_af().func_177746_a(Tower.mc.field_71476_x.func_178782_a())) {
                GlStateManager.func_179109_b(0.0f, 15.0f, 0.0f);
            }
            final String info = "Blocks: §7" + this.getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(Tower.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.func_78326_a() / 2 - 2), (float)(scaledResolution.func_78328_b() / 2 + 5), (float)(scaledResolution.func_78326_a() / 2 + Fonts.font40.func_78256_a(info) + 2), (float)(scaledResolution.func_78328_b() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.func_179117_G();
            Fonts.font40.func_78276_b(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 + 7, Color.WHITE.getRGB());
            GlStateManager.func_179121_F();
        }
    }
    
    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Tower.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                amount += itemStack.field_77994_a;
            }
        }
        return amount;
    }
    
    @Override
    public void onDisable() {
        if (Tower.mc.field_71439_g == null) {
            return;
        }
        Tower.mc.field_71428_T.field_74278_d = 1.0f;
        if (this.slot != Tower.mc.field_71439_g.field_71071_by.field_70461_c) {
            Tower.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Tower.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }
    
    private int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Tower.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
