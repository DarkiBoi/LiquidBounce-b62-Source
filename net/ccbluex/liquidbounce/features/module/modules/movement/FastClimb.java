// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockLadder;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastClimb", description = "Allows you to climb up ladders and vines faster.", category = ModuleCategory.MOVEMENT)
public class FastClimb extends Module
{
    public final ListValue modeValue;
    private final FloatValue normalSpeedValue;
    
    public FastClimb() {
        this.modeValue = new ListValue("Mode", new String[] { "Normal", "InstantTP", "AAC", "AACv3", "OAAC", "LAAC" }, "Normal");
        this.normalSpeedValue = new FloatValue("NormalSpeed", 0.2872f, 0.01f, 5.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fastclimb", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".fastclimb <mode>");
                    return;
                }
                if (args.length > 2 && FastClimb.this.modeValue.contains(args[2])) {
                    FastClimb.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7FastClimb mode was set to §8" + FastClimb.this.modeValue.asString().toUpperCase() + "§7.");
                    FastClimb$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".fastclimb mode §c<§8" + Strings.join(FastClimb.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        final String mode = this.modeValue.asString();
        if (mode.equalsIgnoreCase("Normal") && FastClimb.mc.field_71439_g.field_70123_F && FastClimb.mc.field_71439_g.func_70617_f_()) {
            event.setY(this.normalSpeedValue.asFloat());
            FastClimb.mc.field_71439_g.field_70181_x = 0.0;
        }
        else if (mode.equalsIgnoreCase("AAC") && FastClimb.mc.field_71439_g.field_70123_F) {
            final EnumFacing facing = FastClimb.mc.field_71439_g.func_174811_aO();
            double x = 0.0;
            double z = 0.0;
            if (facing == EnumFacing.NORTH) {
                z = -0.99;
            }
            if (facing == EnumFacing.EAST) {
                x = 0.99;
            }
            if (facing == EnumFacing.SOUTH) {
                z = 0.99;
            }
            if (facing == EnumFacing.WEST) {
                x = -0.99;
            }
            final BlockPos blockPos = new BlockPos(FastClimb.mc.field_71439_g.field_70165_t + x, FastClimb.mc.field_71439_g.field_70163_u, FastClimb.mc.field_71439_g.field_70161_v + z);
            final Block block2 = BlockUtils.getBlock(blockPos);
            if (block2 instanceof BlockLadder || block2 instanceof BlockVine) {
                event.setY(0.5);
                FastClimb.mc.field_71439_g.field_70181_x = 0.0;
            }
        }
        else if (mode.equalsIgnoreCase("AACv3") && BlockUtils.collideBlockIntersects(FastClimb.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLadder || block instanceof BlockVine) && FastClimb.mc.field_71474_y.field_74351_w.func_151470_d()) {
            event.setY(0.5);
            event.setX(0.0);
            event.setZ(0.0);
            FastClimb.mc.field_71439_g.field_70181_x = 0.0;
            FastClimb.mc.field_71439_g.field_70159_w = 0.0;
            FastClimb.mc.field_71439_g.field_70179_y = 0.0;
        }
        else if (mode.equalsIgnoreCase("OAAC") && FastClimb.mc.field_71439_g.field_70123_F && FastClimb.mc.field_71439_g.func_70617_f_()) {
            event.setY(0.1649);
            FastClimb.mc.field_71439_g.field_70181_x = 0.0;
        }
        else if (mode.equalsIgnoreCase("LAAC") && FastClimb.mc.field_71439_g.field_70123_F && FastClimb.mc.field_71439_g.func_70617_f_()) {
            event.setY(0.1699);
            FastClimb.mc.field_71439_g.field_70181_x = 0.0;
        }
        else if (mode.equalsIgnoreCase("InstantTP") && FastClimb.mc.field_71439_g.func_70617_f_() && FastClimb.mc.field_71474_y.field_74351_w.func_151470_d()) {
            for (int i = (int)FastClimb.mc.field_71439_g.field_70163_u; i < 256; ++i) {
                final Block block3 = BlockUtils.getBlock(new BlockPos(FastClimb.mc.field_71439_g.field_70165_t, (double)i, FastClimb.mc.field_71439_g.field_70161_v));
                if (!(block3 instanceof BlockLadder)) {
                    final EnumFacing horizontalFacing = FastClimb.mc.field_71439_g.func_174811_aO();
                    double x2 = 0.0;
                    double z2 = 0.0;
                    switch (horizontalFacing) {
                        case DOWN: {}
                        case NORTH: {
                            z2 = -1.0;
                            break;
                        }
                        case EAST: {
                            x2 = 1.0;
                            break;
                        }
                        case SOUTH: {
                            z2 = 1.0;
                            break;
                        }
                        case WEST: {
                            x2 = -1.0;
                            break;
                        }
                    }
                    FastClimb.mc.field_71439_g.func_70107_b(FastClimb.mc.field_71439_g.field_70165_t + x2, (double)i, FastClimb.mc.field_71439_g.field_70161_v + z2);
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (FastClimb.mc.field_71439_g != null && (event.getBlock() instanceof BlockLadder || event.getBlock() instanceof BlockVine) && this.modeValue.asString().equalsIgnoreCase("AACv3") && FastClimb.mc.field_71439_g.func_70617_f_()) {
            event.setBoundingBox(null);
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
