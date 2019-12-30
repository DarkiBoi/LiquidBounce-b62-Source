// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityEnderChest;
import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import co.uk.hexeption.utils.OutlineUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "StorageESP", description = "Allows you to see chests, dispensers, etc. through walls.", category = ModuleCategory.RENDER)
public class StorageESP extends Module
{
    private final ListValue modeValue;
    private final BoolValue chestValue;
    private final BoolValue enderChestValue;
    private final BoolValue furnaceValue;
    private final BoolValue dispenserValue;
    private final BoolValue hopperValue;
    
    public StorageESP() {
        this.modeValue = new ListValue("Mode", new String[] { "Box", "OtherBox", "Outline", "ShaderOutline", "ShaderGlow", "2D", "WireFrame" }, "Outline");
        this.chestValue = new BoolValue("Chest", true);
        this.enderChestValue = new BoolValue("EnderChest", true);
        this.furnaceValue = new BoolValue("Furnace", true);
        this.dispenserValue = new BoolValue("Dispenser", true);
        this.hopperValue = new BoolValue("Hopper", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("storageesp", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".storageesp <mode>");
                    return;
                }
                if (args.length > 2 && StorageESP.this.modeValue.contains(args[2])) {
                    StorageESP.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7StorageESP mode was set to §8" + StorageESP.this.modeValue.asString().toUpperCase() + "§7.");
                    StorageESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".storageesp mode §c<§8" + Strings.join(StorageESP.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        try {
            final String mode = this.modeValue.asString();
            if (mode.equalsIgnoreCase("outline")) {
                ClientUtils.disableFastRender();
                OutlineUtils.checkSetupFBO();
            }
            final float gamma = StorageESP.mc.field_71474_y.field_74333_Y;
            StorageESP.mc.field_71474_y.field_74333_Y = 100000.0f;
            for (final TileEntity tileEntity : StorageESP.mc.field_71441_e.field_147482_g) {
                Color color = null;
                if (this.chestValue.asBoolean() && tileEntity instanceof TileEntityChest && !ChestAura.ALREADY_OPENED_BLOCKS.contains(tileEntity.func_174877_v())) {
                    color = new Color(0, 66, 255);
                }
                if (this.enderChestValue.asBoolean() && tileEntity instanceof TileEntityEnderChest && !ChestAura.ALREADY_OPENED_BLOCKS.contains(tileEntity.func_174877_v())) {
                    color = Color.MAGENTA;
                }
                if (this.furnaceValue.asBoolean() && tileEntity instanceof TileEntityFurnace) {
                    color = Color.BLACK;
                }
                if (this.dispenserValue.asBoolean() && tileEntity instanceof TileEntityDispenser) {
                    color = Color.BLACK;
                }
                if (this.hopperValue.asBoolean() && tileEntity instanceof TileEntityHopper) {
                    color = Color.GRAY;
                }
                if (color == null) {
                    continue;
                }
                if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest)) {
                    RenderUtils.drawBlockBox(tileEntity.func_174877_v(), color, !mode.equalsIgnoreCase("otherbox"));
                }
                else {
                    final String lowerCase = mode.toLowerCase();
                    switch (lowerCase) {
                        case "otherbox":
                        case "box": {
                            RenderUtils.drawBlockBox(tileEntity.func_174877_v(), color, !mode.equalsIgnoreCase("otherbox"));
                            continue;
                        }
                        case "2d": {
                            RenderUtils.draw2D(tileEntity.func_174877_v(), color.getRGB(), Color.BLACK.getRGB());
                            continue;
                        }
                        case "outline": {
                            RenderUtils.glColor(color);
                            OutlineUtils.renderOne(3.0f);
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            OutlineUtils.renderTwo();
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            OutlineUtils.renderThree();
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            OutlineUtils.renderFour(color);
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            OutlineUtils.renderFive();
                            continue;
                        }
                        case "wireframe": {
                            GL11.glPushMatrix();
                            GL11.glPushAttrib(1048575);
                            GL11.glPolygonMode(1032, 6913);
                            GL11.glDisable(3553);
                            GL11.glDisable(2896);
                            GL11.glDisable(2929);
                            GL11.glEnable(2848);
                            GL11.glEnable(3042);
                            GL11.glBlendFunc(770, 771);
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            RenderUtils.glColor(color);
                            GL11.glLineWidth(1.5f);
                            TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                            GL11.glPopAttrib();
                            GL11.glPopMatrix();
                            continue;
                        }
                    }
                }
            }
            for (final Entity entity : StorageESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityMinecartChest) {
                    final String lowerCase2 = mode.toLowerCase();
                    switch (lowerCase2) {
                        case "otherbox":
                        case "box": {
                            RenderUtils.drawEntityBox(entity, new Color(0, 66, 255), !mode.equalsIgnoreCase("otherbox"));
                            continue;
                        }
                        case "2d": {
                            RenderUtils.draw2D(entity.func_180425_c(), new Color(0, 66, 255).getRGB(), Color.BLACK.getRGB());
                            continue;
                        }
                        case "outline": {
                            final boolean entityShadow = StorageESP.mc.field_71474_y.field_181151_V;
                            StorageESP.mc.field_71474_y.field_181151_V = false;
                            RenderUtils.glColor(new Color(0, 66, 255));
                            OutlineUtils.renderOne(3.0f);
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            OutlineUtils.renderTwo();
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            OutlineUtils.renderThree();
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            OutlineUtils.renderFour(new Color(0, 66, 255));
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            OutlineUtils.renderFive();
                            StorageESP.mc.field_71474_y.field_181151_V = entityShadow;
                            continue;
                        }
                        case "wireframe": {
                            final boolean entityShadow = StorageESP.mc.field_71474_y.field_181151_V;
                            StorageESP.mc.field_71474_y.field_181151_V = false;
                            GL11.glPushMatrix();
                            GL11.glPushAttrib(1048575);
                            GL11.glPolygonMode(1032, 6913);
                            GL11.glDisable(3553);
                            GL11.glDisable(2896);
                            GL11.glDisable(2929);
                            GL11.glEnable(2848);
                            GL11.glEnable(3042);
                            GL11.glBlendFunc(770, 771);
                            RenderUtils.glColor(new Color(0, 66, 255));
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            RenderUtils.glColor(new Color(0, 66, 255));
                            GL11.glLineWidth(1.5f);
                            StorageESP.mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                            GL11.glPopAttrib();
                            GL11.glPopMatrix();
                            StorageESP.mc.field_71474_y.field_181151_V = entityShadow;
                            continue;
                        }
                    }
                }
            }
            RenderUtils.glColor(new Color(255, 255, 255, 255));
            StorageESP.mc.field_71474_y.field_74333_Y = gamma;
        }
        catch (Exception ex) {}
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "shaderoutline": {
                OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());
                StorageESP.mc.field_71441_e.field_147482_g.stream().filter(entity -> entity instanceof TileEntityChest).forEach(entity -> TileEntityRendererDispatcher.field_147556_a.func_147549_a(entity, entity.func_174877_v().func_177958_n() - StorageESP.mc.func_175598_ae().field_78725_b, entity.func_174877_v().func_177956_o() - StorageESP.mc.func_175598_ae().field_78726_c, entity.func_174877_v().func_177952_p() - StorageESP.mc.func_175598_ae().field_78723_d, event.getPartialTicks()));
                StorageESP.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityMinecartChest).forEach(entity -> StorageESP.mc.func_175598_ae().func_147936_a(entity, event.getPartialTicks(), true));
                OutlineShader.OUTLINE_SHADER.stopDraw(new Color(0, 66, 255), 1.5f, 1.0f);
                break;
            }
            case "shaderglow": {
                GlowShader.GLOW_SHADER.startDraw(event.getPartialTicks());
                StorageESP.mc.field_71441_e.field_147482_g.stream().filter(entity -> entity instanceof TileEntityChest).forEach(entity -> TileEntityRendererDispatcher.field_147556_a.func_147549_a(entity, entity.func_174877_v().func_177958_n() - StorageESP.mc.func_175598_ae().field_78725_b, entity.func_174877_v().func_177956_o() - StorageESP.mc.func_175598_ae().field_78726_c, entity.func_174877_v().func_177952_p() - StorageESP.mc.func_175598_ae().field_78723_d, event.getPartialTicks()));
                StorageESP.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityMinecartChest).forEach(entity -> StorageESP.mc.func_175598_ae().func_147936_a(entity, event.getPartialTicks(), true));
                GlowShader.GLOW_SHADER.stopDraw(new Color(0, 66, 255), 2.5f, 1.0f);
                break;
            }
        }
    }
}
