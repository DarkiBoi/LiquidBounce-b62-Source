// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "XRay", description = "Allows you to see ores through walls.", category = ModuleCategory.RENDER)
public class XRay extends Module
{
    public final List<Block> xrayBlocks;
    
    public XRay() {
        this.xrayBlocks = new ArrayList<Block>(Arrays.asList(Blocks.field_150365_q, Blocks.field_150366_p, Blocks.field_150352_o, Blocks.field_150450_ax, Blocks.field_150369_x, Blocks.field_150482_ag, Blocks.field_150412_bA, Blocks.field_150449_bY, Blocks.field_150435_aG, Blocks.field_150426_aN, Blocks.field_150462_ai, Blocks.field_150478_aa, Blocks.field_150468_ap, Blocks.field_150335_W, Blocks.field_150402_ci, Blocks.field_150339_S, Blocks.field_150340_R, Blocks.field_150484_ah, Blocks.field_150475_bE, Blocks.field_150451_bX, Blocks.field_150368_y, (Block)Blocks.field_150480_ab, Blocks.field_150341_Y, Blocks.field_150474_ac, Blocks.field_150378_br, Blocks.field_150381_bn, Blocks.field_150342_X, Blocks.field_150483_bI, (Block)Blocks.field_150353_l, (Block)Blocks.field_150356_k, (Block)Blocks.field_150355_j, (Block)Blocks.field_150358_i, Blocks.field_150460_al, Blocks.field_150470_am));
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("xray", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length > 2) {
                            try {
                                final Block block2 = Block.func_149729_e(Integer.parseInt(args[2]));
                                if (XRay.this.xrayBlocks.contains(block2)) {
                                    this.chat("The block is already in the list.");
                                    return;
                                }
                                XRay.this.xrayBlocks.add(block2);
                                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.xrayConfig);
                                this.chat("§7Added block §8" + block2.func_149732_F() + "§7.");
                                XRay$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".xray add <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length > 2) {
                            try {
                                final Block block2 = Block.func_149729_e(Integer.parseInt(args[2]));
                                if (!XRay.this.xrayBlocks.contains(block2)) {
                                    this.chat("The block is not in the list.");
                                    return;
                                }
                                XRay.this.xrayBlocks.remove(block2);
                                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.xrayConfig);
                                this.chat("§7Removed block §8" + block2.func_149732_F() + "§7.");
                                XRay$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".xray remove <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("list")) {
                        this.chat("§8XRAY-Blocks:");
                        XRay.this.xrayBlocks.forEach(block -> this.chat("§8" + block.func_149732_F() + " §7-§c " + Block.func_149682_b(block)));
                        return;
                    }
                }
                this.chatSyntax(".xray <add, remove, list>");
            }
        });
    }
    
    @Override
    public void onToggle(final boolean state) {
        XRay.mc.field_71438_f.func_72712_a();
    }
}
