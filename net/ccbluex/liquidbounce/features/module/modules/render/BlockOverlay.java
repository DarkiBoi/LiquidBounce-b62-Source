// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BlockOverlay", description = "Allows you to change the design of the block overlay.", category = ModuleCategory.RENDER)
public class BlockOverlay extends Module
{
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    public final BoolValue infoValue;
    
    public BlockOverlay() {
        this.colorRedValue = new IntegerValue("R", 68, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 117, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.infoValue = new BoolValue("Info", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("blockoverlay", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("color")) {
                    if (args.length > 2) {
                        if (args[2].equalsIgnoreCase("rainbow")) {
                            BlockOverlay.this.colorRainbow.setValue(!BlockOverlay.this.colorRainbow.asBoolean());
                            this.chat("§a§lRainbow §7was toggled §c§l" + (BlockOverlay.this.colorRainbow.asBoolean() ? "on" : "off") + "§7.");
                            BlockOverlay$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
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
                                BlockOverlay.this.colorRedValue.setValue(r);
                                BlockOverlay.this.colorGreenValue.setValue(g);
                                BlockOverlay.this.colorBlueValue.setValue(b);
                                this.chat("§a§lRGB §7was set to §c§l" + r + ", " + g + ", " + b + "§7.");
                                BlockOverlay$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException e) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                    }
                    this.chatSyntax(".blockoverlay color <r> <g> <b> / .blockoverlay color <rainbow>");
                    return;
                }
                this.chatSyntax(".blockoverlay <color>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (BlockOverlay.mc.field_71476_x != null && BlockOverlay.mc.field_71476_x.func_178782_a() != null && BlockOverlay.mc.field_71441_e.func_180495_p(BlockOverlay.mc.field_71476_x.func_178782_a()).func_177230_c() != null && BlockUtils.canBeClicked(BlockOverlay.mc.field_71476_x.func_178782_a())) {
            final float partialTicks = event.getPartialTicks();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow(0.4f) : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger(), 102);
            RenderUtils.glColor(color);
            GL11.glLineWidth(2.0f);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a(false);
            final BlockPos blockPos = BlockOverlay.mc.field_71476_x.func_178782_a();
            final Block block = BlockOverlay.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
            if (BlockOverlay.mc.field_71441_e.func_175723_af().func_177746_a(blockPos)) {
                block.func_180654_a((IBlockAccess)BlockOverlay.mc.field_71441_e, blockPos);
                final double d0 = BlockOverlay.mc.field_71439_g.field_70142_S + (BlockOverlay.mc.field_71439_g.field_70165_t - BlockOverlay.mc.field_71439_g.field_70142_S) * partialTicks;
                final double d2 = BlockOverlay.mc.field_71439_g.field_70137_T + (BlockOverlay.mc.field_71439_g.field_70163_u - BlockOverlay.mc.field_71439_g.field_70137_T) * partialTicks;
                final double d3 = BlockOverlay.mc.field_71439_g.field_70136_U + (BlockOverlay.mc.field_71439_g.field_70161_v - BlockOverlay.mc.field_71439_g.field_70136_U) * partialTicks;
                final AxisAlignedBB axisAlignedBB = block.func_180646_a((World)BlockOverlay.mc.field_71441_e, blockPos).func_72314_b(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).func_72317_d(-d0, -d2, -d3);
                RenderGlobal.func_181561_a(axisAlignedBB);
                RenderUtils.drawFilledBox(axisAlignedBB);
            }
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179117_G();
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (BlockOverlay.mc.field_71476_x != null && BlockOverlay.mc.field_71476_x.func_178782_a() != null && BlockOverlay.mc.field_71441_e.func_180495_p(BlockOverlay.mc.field_71476_x.func_178782_a()).func_177230_c() != null && BlockUtils.canBeClicked(BlockOverlay.mc.field_71476_x.func_178782_a()) && this.infoValue.asBoolean()) {
            final BlockPos blockPos = BlockOverlay.mc.field_71476_x.func_178782_a();
            final Block block = BlockOverlay.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
            if (BlockOverlay.mc.field_71441_e.func_175723_af().func_177746_a(blockPos)) {
                final String info = block.func_149732_F() + " §7ID: " + Block.func_149682_b(block);
                final ScaledResolution scaledResolution = new ScaledResolution(BlockOverlay.mc);
                RenderUtils.drawBorderedRect((float)(scaledResolution.func_78326_a() / 2 - 2), (float)(scaledResolution.func_78328_b() / 2 + 5), (float)(scaledResolution.func_78326_a() / 2 + Fonts.font40.func_78256_a(info) + 2), (float)(scaledResolution.func_78328_b() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
                GlStateManager.func_179117_G();
                Fonts.font40.func_78276_b(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 + 7, Color.WHITE.getRGB());
            }
        }
    }
}
