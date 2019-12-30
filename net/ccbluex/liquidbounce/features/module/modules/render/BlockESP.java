// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.Collection;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BlockValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BlockESP", description = "Allows you to see a selected block through walls.", category = ModuleCategory.RENDER)
public class BlockESP extends Module
{
    private final ListValue modeValue;
    private final BlockValue blockValue;
    private final IntegerValue radiusValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    private final MSTimer searchTimer;
    private final List<BlockPos> posList;
    private Thread thread;
    
    public BlockESP() {
        this.modeValue = new ListValue("Mode", new String[] { "Box", "2D" }, "Box");
        this.blockValue = new BlockValue("id", 168);
        this.radiusValue = new IntegerValue("Radius", 40, 5, 120);
        this.colorRedValue = new IntegerValue("R", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 179, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 72, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.searchTimer = new MSTimer();
        this.posList = new ArrayList<BlockPos>();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("blockesp", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("select")) {
                        if (args.length > 2) {
                            int id;
                            try {
                                id = Integer.parseInt(args[2]);
                            }
                            catch (NumberFormatException exception) {
                                id = Block.func_149682_b(Block.func_149684_b(args[2]));
                            }
                            if (id != 0 && Block.func_149729_e(id) != null && Block.func_149729_e(id) != Blocks.field_150350_a) {
                                BlockESP.this.blockValue.setValue(id);
                                this.chat("§7The block was set to " + BlockUtils.getBlockName(id) + ".");
                                BlockESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            else {
                                this.chat("Block was not found or the block is air.");
                            }
                            return;
                        }
                        this.chat("§7Current: §8" + Block.func_149729_e(BlockESP.this.blockValue.asInteger()).func_149732_F());
                        this.chatSyntax(".blockesp select <name/id>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("radius")) {
                        if (args.length > 2) {
                            try {
                                final int newValue = Integer.parseInt(args[2]);
                                BlockESP.this.radiusValue.setValue(newValue);
                                this.chat("§7BlockESP radius was set to §8" + newValue + "§7.");
                                BlockESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception2) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".blockesp radius <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("color")) {
                        if (args.length > 2) {
                            if (args[2].equalsIgnoreCase("rainbow")) {
                                BlockESP.this.colorRainbow.setValue(!BlockESP.this.colorRainbow.asBoolean());
                                this.chat("§a§lRainbow §7was toggled §c§l" + (BlockESP.this.colorRainbow.asBoolean() ? "on" : "off") + "§7.");
                                BlockESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            if (args.length > 4) {
                                try {
                                    final int r = Integer.parseInt(args[2]);
                                    final int g = Integer.parseInt(args[3]);
                                    final int b = Integer.parseInt(args[4]);
                                    if (r > 255 || g > 255 || b > 255) {
                                        this.chatSyntaxError();
                                        return;
                                    }
                                    BlockESP.this.colorRedValue.setValue(r);
                                    BlockESP.this.colorGreenValue.setValue(g);
                                    BlockESP.this.colorBlueValue.setValue(b);
                                    this.chat("§a§lRGB §7was set to §c§l" + r + ", " + g + ", " + b + "§7.");
                                    BlockESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException e) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                        }
                        this.chatSyntax(".blockesp color <r> <g> <b> / .blockesp color <rainbow>");
                        return;
                    }
                }
                this.chatSyntax(".blockesp <select/radius/color>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.searchTimer:Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;
        //     4: ldc2_w          1000
        //     7: invokevirtual   net/ccbluex/liquidbounce/utils/timer/MSTimer.hasTimePassed:(J)Z
        //    10: ifeq            89
        //    13: aload_0         /* this */
        //    14: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.thread:Ljava/lang/Thread;
        //    17: ifnull          30
        //    20: aload_0         /* this */
        //    21: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.thread:Ljava/lang/Thread;
        //    24: invokevirtual   java/lang/Thread.isAlive:()Z
        //    27: ifne            89
        //    30: aload_0         /* this */
        //    31: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.radiusValue:Lnet/ccbluex/liquidbounce/valuesystem/types/IntegerValue;
        //    34: invokevirtual   net/ccbluex/liquidbounce/valuesystem/types/IntegerValue.asInteger:()I
        //    37: istore_2        /* radius */
        //    38: aload_0         /* this */
        //    39: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.blockValue:Lnet/ccbluex/liquidbounce/valuesystem/types/BlockValue;
        //    42: invokevirtual   net/ccbluex/liquidbounce/valuesystem/types/BlockValue.asInteger:()I
        //    45: invokestatic    net/minecraft/block/Block.func_149729_e:(I)Lnet/minecraft/block/Block;
        //    48: astore_3        /* selectedBlock */
        //    49: aload_3         /* selectedBlock */
        //    50: ifnull          60
        //    53: aload_3         /* selectedBlock */
        //    54: getstatic       net/minecraft/init/Blocks.field_150350_a:Lnet/minecraft/block/Block;
        //    57: if_acmpne       61
        //    60: return         
        //    61: aload_0         /* this */
        //    62: new             Ljava/lang/Thread;
        //    65: dup            
        //    66: aload_0         /* this */
        //    67: iload_2         /* radius */
        //    68: aload_3         /* selectedBlock */
        //    69: invokedynamic   BootstrapMethod #0, run:(Lnet/ccbluex/liquidbounce/features/module/modules/render/BlockESP;ILnet/minecraft/block/Block;)Ljava/lang/Runnable;
        //    74: ldc             "BlockESP-BlockFinder"
        //    76: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;Ljava/lang/String;)V
        //    79: putfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.thread:Ljava/lang/Thread;
        //    82: aload_0         /* this */
        //    83: getfield        net/ccbluex/liquidbounce/features/module/modules/render/BlockESP.thread:Ljava/lang/Thread;
        //    86: invokevirtual   java/lang/Thread.start:()V
        //    89: return         
        //    StackMapTable: 00 04 1E FD 00 1D 01 07 00 99 00 F9 00 1B
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        synchronized (this.posList) {
            final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger());
            for (final BlockPos blockPos : this.posList) {
                final String lowerCase = this.modeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "box": {
                        RenderUtils.drawBlockBox(blockPos, color, true);
                        continue;
                    }
                    case "2d": {
                        RenderUtils.draw2D(blockPos, color.getRGB(), Color.BLACK.getRGB());
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public String getTag() {
        return BlockUtils.getBlockName(this.blockValue.asInteger());
    }
}
