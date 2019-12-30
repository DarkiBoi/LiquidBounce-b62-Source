// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LiquidWalk", description = "Allows you to walk on water.", category = ModuleCategory.MOVEMENT, keyBind = 36)
public class LiquidWalk extends Module
{
    private final ListValue modeValue;
    private boolean nextTick;
    
    public LiquidWalk() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "AAC3.3.11", "Spartan", "Dolphin" }, "NCP");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("liquidwalk", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".liquidwalk <mode>");
                    return;
                }
                if (args.length > 2 && LiquidWalk.this.modeValue.contains(args[2])) {
                    LiquidWalk.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7LiquidWalk mode was set to §8" + LiquidWalk.this.modeValue.asString().toUpperCase() + "§7.");
                    LiquidWalk$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".liquidwalk mode §c<§8" + Strings.join(LiquidWalk.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || LiquidWalk.mc.field_71439_g.func_70093_af()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "ncp": {
                if (BlockUtils.collideBlock(LiquidWalk.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) && LiquidWalk.mc.field_71439_g.func_70055_a(Material.field_151579_a) && !LiquidWalk.mc.field_71439_g.func_70093_af()) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.08;
                    break;
                }
                break;
            }
            case "aac": {
                final BlockPos blockPos = LiquidWalk.mc.field_71439_g.func_180425_c().func_177977_b();
                if ((!LiquidWalk.mc.field_71439_g.field_70122_E && BlockUtils.getBlock(blockPos) == Blocks.field_150355_j) || LiquidWalk.mc.field_71439_g.func_70090_H()) {
                    if (!LiquidWalk.mc.field_71439_g.func_70051_ag()) {
                        final EntityPlayerSP field_71439_g = LiquidWalk.mc.field_71439_g;
                        field_71439_g.field_70159_w *= 0.99999;
                        final EntityPlayerSP field_71439_g2 = LiquidWalk.mc.field_71439_g;
                        field_71439_g2.field_70181_x *= 0.0;
                        final EntityPlayerSP field_71439_g3 = LiquidWalk.mc.field_71439_g;
                        field_71439_g3.field_70179_y *= 0.99999;
                        if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                            LiquidWalk.mc.field_71439_g.field_70181_x = (int)(LiquidWalk.mc.field_71439_g.field_70163_u - (int)(LiquidWalk.mc.field_71439_g.field_70163_u - 1.0)) / 8.0f;
                        }
                    }
                    else {
                        final EntityPlayerSP field_71439_g4 = LiquidWalk.mc.field_71439_g;
                        field_71439_g4.field_70159_w *= 0.99999;
                        final EntityPlayerSP field_71439_g5 = LiquidWalk.mc.field_71439_g;
                        field_71439_g5.field_70181_x *= 0.0;
                        final EntityPlayerSP field_71439_g6 = LiquidWalk.mc.field_71439_g;
                        field_71439_g6.field_70179_y *= 0.99999;
                        if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                            LiquidWalk.mc.field_71439_g.field_70181_x = (int)(LiquidWalk.mc.field_71439_g.field_70163_u - (int)(LiquidWalk.mc.field_71439_g.field_70163_u - 1.0)) / 8.0f;
                        }
                    }
                    if (LiquidWalk.mc.field_71439_g.field_70143_R >= 4.0f) {
                        LiquidWalk.mc.field_71439_g.field_70181_x = -0.004;
                    }
                    else if (LiquidWalk.mc.field_71439_g.func_70090_H()) {
                        LiquidWalk.mc.field_71439_g.field_70181_x = 0.09;
                    }
                }
                if (LiquidWalk.mc.field_71439_g.field_70737_aN != 0) {
                    LiquidWalk.mc.field_71439_g.field_70122_E = false;
                    break;
                }
                break;
            }
            case "spartan": {
                if (!LiquidWalk.mc.field_71439_g.func_70090_H()) {
                    break;
                }
                if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                    final EntityPlayerSP field_71439_g7 = LiquidWalk.mc.field_71439_g;
                    field_71439_g7.field_70181_x += 0.15;
                    return;
                }
                final Block block2 = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.0, LiquidWalk.mc.field_71439_g.field_70161_v));
                final Block blockUp = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.1, LiquidWalk.mc.field_71439_g.field_70161_v));
                if (blockUp instanceof BlockLiquid) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.1;
                }
                else if (block2 instanceof BlockLiquid) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.0;
                }
                LiquidWalk.mc.field_71439_g.field_70122_E = true;
                final EntityPlayerSP field_71439_g8 = LiquidWalk.mc.field_71439_g;
                field_71439_g8.field_70159_w *= 1.085;
                final EntityPlayerSP field_71439_g9 = LiquidWalk.mc.field_71439_g;
                field_71439_g9.field_70179_y *= 1.085;
                break;
            }
            case "dolphin": {
                if (LiquidWalk.mc.field_71439_g.func_70090_H()) {
                    final EntityPlayerSP field_71439_g10 = LiquidWalk.mc.field_71439_g;
                    field_71439_g10.field_70181_x += 0.03999999910593033;
                    break;
                }
                break;
            }
            case "aac3.3.11": {
                if (!LiquidWalk.mc.field_71439_g.func_70090_H()) {
                    break;
                }
                final EntityPlayerSP field_71439_g11 = LiquidWalk.mc.field_71439_g;
                field_71439_g11.field_70159_w *= 1.17;
                final EntityPlayerSP field_71439_g12 = LiquidWalk.mc.field_71439_g;
                field_71439_g12.field_70179_y *= 1.17;
                if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.24;
                    break;
                }
                if (LiquidWalk.mc.field_71441_e.func_180495_p(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.0, LiquidWalk.mc.field_71439_g.field_70161_v)).func_177230_c() != Blocks.field_150350_a) {
                    final EntityPlayerSP field_71439_g13 = LiquidWalk.mc.field_71439_g;
                    field_71439_g13.field_70181_x += 0.04;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || LiquidWalk.mc.field_71439_g.func_174813_aQ() == null) {
            return;
        }
        if (event.getBlock() instanceof BlockLiquid && !BlockUtils.collideBlock(LiquidWalk.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) && !LiquidWalk.mc.field_71439_g.func_70093_af()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "ncp": {
                    event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)(event.getY() + 1), (double)(event.getZ() + 1)));
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || !this.modeValue.asString().equalsIgnoreCase("NCP")) {
            return;
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
            if (BlockUtils.collideBlock(new AxisAlignedBB(LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72336_d, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72337_e, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72334_f, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72340_a, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
                this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    final C03PacketPlayer c03PacketPlayer = packetPlayer;
                    c03PacketPlayer.field_149477_b -= 0.001;
                }
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
