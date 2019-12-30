// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItem;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ItemESP", description = "Allows you to see items through walls.", category = ModuleCategory.RENDER)
public class ItemESP extends Module
{
    private final ListValue modeValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    
    public ItemESP() {
        this.modeValue = new ListValue("Mode", new String[] { "Box", "ShaderOutline" }, "Box");
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 255, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 0, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("itemesp", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("color")) {
                    if (args.length > 2) {
                        if (args[2].equalsIgnoreCase("rainbow")) {
                            ItemESP.this.colorRainbow.setValue(!ItemESP.this.colorRainbow.asBoolean());
                            this.chat("§a§lRainbow §7was toggled §c§l" + (ItemESP.this.colorRainbow.asBoolean() ? "on" : "off") + "§7.");
                            ItemESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
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
                                ItemESP.this.colorRedValue.setValue(r);
                                ItemESP.this.colorGreenValue.setValue(g);
                                ItemESP.this.colorBlueValue.setValue(b);
                                this.chat("§a§lRGB §7was set to §c§l" + r + ", " + g + ", " + b + "§7.");
                                ItemESP$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException e) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                    }
                    this.chatSyntax(".itemesp color <r> <g> <b> / .itemesp color <rainbow>");
                    return;
                }
                this.chatSyntax(".itemesp <color>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.modeValue.asString().equalsIgnoreCase("Box")) {
            final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger());
            ItemESP.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityItem || entity instanceof EntityArrow).forEach(entity -> RenderUtils.drawEntityBox(entity, color, true));
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (this.modeValue.asString().equalsIgnoreCase("ShaderOutline")) {
            OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());
            ItemESP.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityItem || entity instanceof EntityArrow).forEach(entity -> ItemESP.mc.func_175598_ae().func_147936_a(entity, event.getPartialTicks(), true));
            OutlineShader.OUTLINE_SHADER.stopDraw(this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger()), 1.0f, 1.0f);
        }
    }
}
