// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.ui.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.clickgui.style.styles.LiquidBounceStyle;
import net.minecraft.client.gui.GuiScreen;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import java.awt.Color;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = 54, canEnable = false)
public class ClickGUI extends Module
{
    private final ListValue styleValue;
    public final FloatValue scaleValue;
    public final IntegerValue maxElementsValue;
    private static final IntegerValue colorRedValue;
    private static final IntegerValue colorGreenValue;
    private static final IntegerValue colorBlueValue;
    private static final BoolValue colorRainbow;
    
    public ClickGUI() {
        this.styleValue = new ListValue("Style", new String[] { "LiquidBounce", "Null", "Slowly" }, "Slowly") {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                ClickGUI.this.updateStyle();
            }
        };
        this.scaleValue = new FloatValue("Scale", 1.0f, 0.7f, 2.0f);
        this.maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("clickgui", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("style")) {
                        if (args.length > 2 && ClickGUI.this.styleValue.contains(args[2])) {
                            ClickGUI.this.styleValue.setValue(args[2].toLowerCase());
                            this.chat("§7ClickGUI Style was set to §8" + ClickGUI.this.styleValue.asString().toUpperCase() + "§7.");
                            ClickGUI$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".clickgui style §c<§8" + Strings.join(ClickGUI.this.styleValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("scale")) {
                        if (args.length > 2) {
                            try {
                                final float scale = Float.parseFloat(args[2]);
                                ClickGUI.this.scaleValue.setValue(scale);
                                this.chat("§7ClickGUI Scale was set to §8" + scale + "§7.");
                                ClickGUI$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".clickgui scale <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("maxelements")) {
                        if (args.length > 2) {
                            try {
                                final int i = Integer.parseInt(args[2]);
                                ClickGUI.this.maxElementsValue.setValue(i);
                                this.chat("§7ClickGUI MaxElements was set to §8" + i + "§7.");
                                ClickGUI$2.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".clickgui maxelements <value>");
                        return;
                    }
                }
                this.chatSyntax(".clickgui <style, scale, maxelements>");
            }
        });
    }
    
    public static Color generateColor() {
        return ClickGUI.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(ClickGUI.colorRedValue.asInteger(), ClickGUI.colorGreenValue.asInteger(), ClickGUI.colorBlueValue.asInteger());
    }
    
    @Override
    public void onEnable() {
        this.updateStyle();
        ClickGUI.mc.func_147108_a((GuiScreen)LiquidBounce.CLIENT.clickGui);
    }
    
    private void updateStyle() {
        final String lowerCase = this.styleValue.asString().toLowerCase();
        switch (lowerCase) {
            case "liquidbounce": {
                LiquidBounce.CLIENT.clickGui.style = new LiquidBounceStyle();
                break;
            }
            case "null": {
                LiquidBounce.CLIENT.clickGui.style = new NullStyle();
                break;
            }
            case "slowly": {
                LiquidBounce.CLIENT.clickGui.style = new SlowlyStyle();
                break;
            }
        }
    }
    
    static {
        colorRedValue = new IntegerValue("R", 0, 0, 255);
        colorGreenValue = new IntegerValue("G", 160, 0, 255);
        colorBlueValue = new IntegerValue("B", 255, 0, 255);
        colorRainbow = new BoolValue("Rainbow", false);
    }
}
