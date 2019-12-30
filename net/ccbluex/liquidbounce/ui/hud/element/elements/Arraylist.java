// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.ui.hud.element.Facing;
import java.util.Comparator;
import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Arraylist extends Element
{
    private final ListValue colorModeValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final FloatValue saturationValue;
    private final FloatValue brightnessValue;
    private final BoolValue tags;
    private final BoolValue shadow;
    private final ListValue backgroundColorModeValue;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue backgroundColorGreenValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final ListValue rectValue;
    private final BoolValue upperCaseValue;
    private final FloatValue spaceValue;
    private final FloatValue textHeightValue;
    private final FloatValue textYValue;
    private final BoolValue tagsArrayColor;
    private FontRenderer fontRenderer;
    private int x2;
    private float y2;
    
    public Arraylist() {
        this.colorModeValue = new ListValue("Text-Color", new String[] { "Custom", "Random", "Rainbow" }, "Custom");
        this.colorRedValue = new IntegerValue("Text-R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Text-G", 111, 0, 255);
        this.colorBlueValue = new IntegerValue("Text-B", 255, 0, 255);
        this.rectColorModeValue = new ListValue("Rect-Color", new String[] { "Custom", "Random", "Rainbow" }, "Rainbow");
        this.rectColorRedValue = new IntegerValue("Rect-R", 255, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Rect-G", 255, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Rect-B", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Rect-Alpha", 255, 0, 255);
        this.saturationValue = new FloatValue("Random-Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Random-Brightness", 1.0f, 0.0f, 1.0f);
        this.tags = new BoolValue("Tags", true);
        this.shadow = new BoolValue("ShadowText", true);
        this.backgroundColorModeValue = new ListValue("Background-Color", new String[] { "Custom", "Random", "Rainbow" }, "Custom");
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 0, 0, 255);
        this.rectValue = new ListValue("Rect", new String[] { "None", "Left", "Right" }, "None");
        this.upperCaseValue = new BoolValue("UpperCase", false);
        this.spaceValue = new FloatValue("Space", 0.0f, 0.0f, 5.0f);
        this.textHeightValue = new FloatValue("TextHeight", 11.0f, 1.0f, 20.0f);
        this.textYValue = new FloatValue("TextY", 1.0f, 0.0f, 20.0f);
        this.tagsArrayColor = new BoolValue("TagsArrayColor", false);
        this.fontRenderer = Fonts.font40;
    }
    
    @Override
    public void drawElement() {
        final int delta = RenderUtils.deltaTime;
        for (final Module module2 : ModuleManager.getModules()) {
            if (module2.getState() && module2.showArray()) {
                String displayString = this.tags.asBoolean() ? (this.tagsArrayColor.asBoolean() ? module2.getColorlessTagName() : module2.getTagName()) : module2.getName();
                if (this.upperCaseValue.asBoolean()) {
                    displayString = displayString.toUpperCase();
                }
                final int width = this.fontRenderer.func_78256_a(displayString);
                if (module2.slide < width) {
                    final Module module5 = module2;
                    module5.slide += 0.15f * delta;
                }
                else if (module2.slide > width) {
                    final Module module6 = module2;
                    module6.slide -= 0.15f * delta;
                }
                if (module2.slide > width) {
                    module2.slide = (float)width;
                }
            }
            else if (module2.slide > 0.0f) {
                final Module module7 = module2;
                module7.slide -= 0.15f * delta;
            }
            if (module2.slide < 0.0f) {
                module2.slide = 0.0f;
            }
        }
        final int[] location = this.getLocationFromFacing();
        final String colorMode = this.colorModeValue.asString();
        final String rectColorMode = this.rectColorModeValue.asString();
        final String backgroundColorMode = this.backgroundColorModeValue.asString();
        final int customColor = new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger()).getRGB();
        final int rectCustomColor = new Color(this.rectColorRedValue.asInteger(), this.rectColorGreenValue.asInteger(), this.rectColorBlueValue.asInteger(), this.rectColorBlueAlpha.asInteger()).getRGB();
        final float space = this.spaceValue.asFloat();
        final float textHeight = this.textHeightValue.asFloat();
        final float textY = this.textYValue.asFloat();
        final String rectMode = this.rectValue.asString().toLowerCase();
        final int backgroundCustomColor = new Color(this.backgroundColorRedValue.asInteger(), this.backgroundColorGreenValue.asInteger(), this.backgroundColorBlueValue.asInteger(), this.backgroundColorAlphaValue.asInteger()).getRGB();
        final boolean textShadow = this.shadow.asBoolean();
        final float textSpacer = textHeight + space;
        final float saturation = this.saturationValue.asFloat();
        final float brightness = this.brightnessValue.asFloat();
        final FontRenderer fontRenderer;
        String upperCase;
        final Module[] modules = ModuleManager.getModules().stream().filter(module -> module.showArray() && module.slide > 0.0f).sorted(Comparator.comparingInt(module -> {
            fontRenderer = this.fontRenderer;
            if (this.upperCaseValue.asBoolean()) {
                upperCase = (this.tags.asBoolean() ? (this.tagsArrayColor.asBoolean() ? module.getColorlessTagName() : module.getTagName()) : module.getName()).toUpperCase();
            }
            else {
                upperCase = (this.tags.asBoolean() ? (this.tagsArrayColor.asBoolean() ? module.getColorlessTagName() : module.getTagName()) : module.getName());
            }
            return -fontRenderer.func_78256_a(upperCase);
        })).toArray(Module[]::new);
        switch (this.getFacing().getHorizontal()) {
            case RIGHT:
            case MIDDLE: {
                for (int i = 0; i < modules.length; ++i) {
                    final Module module3 = modules[i];
                    String displayString2 = this.tags.asBoolean() ? (this.tagsArrayColor.asBoolean() ? module3.getColorlessTagName() : module3.getTagName()) : module3.getName();
                    if (this.upperCaseValue.asBoolean()) {
                        displayString2 = displayString2.toUpperCase();
                    }
                    final float xPos = location[0] - module3.slide - 2.0f;
                    final float yPos = location[1] + ((this.getFacing().getVertical() == Facing.Vertical.DOWN) ? (-textSpacer) : textSpacer) * ((this.getFacing().getVertical() == Facing.Vertical.DOWN) ? (i + 1) : i);
                    final int moduleColor = Color.getHSBColor(module3.hue, saturation, brightness).getRGB();
                    RenderUtils.drawRect(xPos - (rectMode.equals("right") ? 5 : 2), yPos - (float)((i == 0) ? 1 : 0), (float)(location[0] - (rectMode.equals("right") ? 3 : 0)), yPos + textHeight, backgroundColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (backgroundColorMode.equalsIgnoreCase("Random") ? moduleColor : backgroundCustomColor));
                    GlStateManager.func_179117_G();
                    this.fontRenderer.func_175065_a(displayString2, xPos - (rectMode.equals("right") ? 3 : 0), yPos + textY, colorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (colorMode.equalsIgnoreCase("Random") ? moduleColor : customColor), textShadow);
                    if (!rectMode.equals("none")) {
                        final int rectColor = this.rectColorModeValue.asString().equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (rectColorMode.equalsIgnoreCase("Random") ? moduleColor : rectCustomColor);
                        final String s = rectMode;
                        switch (s) {
                            case "left": {
                                RenderUtils.drawRect(xPos - 5.0f, yPos - 1.0f, xPos - 2.0f, yPos + textHeight, rectColor);
                                break;
                            }
                            case "right": {
                                RenderUtils.drawRect((float)(location[0] - 3), yPos - 1.0f, (float)location[0], yPos + textHeight, rectColor);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case LEFT: {
                for (int i = 0; i < modules.length; ++i) {
                    final Module module3 = modules[i];
                    String displayString2 = this.tags.asBoolean() ? (this.tagsArrayColor.asBoolean() ? module3.getColorlessTagName() : module3.getTagName()) : module3.getName();
                    if (this.upperCaseValue.asBoolean()) {
                        displayString2 = displayString2.toUpperCase();
                    }
                    final int width2 = this.fontRenderer.func_78256_a(displayString2);
                    final float xPos2 = location[0] - (width2 - module3.slide) + (rectMode.equals("left") ? 5 : 2);
                    final float yPos2 = location[1] + ((this.getFacing().getVertical() == Facing.Vertical.DOWN) ? (-textSpacer) : textSpacer) * ((this.getFacing().getVertical() == Facing.Vertical.DOWN) ? (i + 1) : i);
                    final int moduleColor2 = Color.getHSBColor(module3.hue, saturation, brightness).getRGB();
                    RenderUtils.drawRect((float)location[0], yPos2 - (float)((i == 0) ? 1 : 0), xPos2 + width2 + (rectMode.equals("right") ? 5 : 2), yPos2 + textHeight, backgroundColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (backgroundColorMode.equalsIgnoreCase("Random") ? moduleColor2 : backgroundCustomColor));
                    GlStateManager.func_179117_G();
                    this.fontRenderer.func_175065_a(displayString2, xPos2, yPos2 + textY, colorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (colorMode.equalsIgnoreCase("Random") ? moduleColor2 : customColor), textShadow);
                    if (!rectMode.equals("none")) {
                        final int rectColor2 = this.rectColorModeValue.asString().equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(400000000L * i).getRGB() : (rectColorMode.equalsIgnoreCase("Random") ? moduleColor2 : rectCustomColor);
                        final String s2 = rectMode;
                        switch (s2) {
                            case "left": {
                                RenderUtils.drawRect((float)location[0], yPos2 - 1.0f, (float)(location[0] + 3), yPos2 + textHeight, rectColor2);
                                break;
                            }
                            case "right": {
                                RenderUtils.drawRect(xPos2 + width2 + 2.0f, yPos2 - 1.0f, xPos2 + width2 + 2.0f + 3.0f, yPos2 + textHeight, rectColor2);
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            this.x2 = Integer.MIN_VALUE;
            for (final Module module4 : modules) {
                switch (this.getFacing().getHorizontal()) {
                    case RIGHT:
                    case MIDDLE: {
                        final int xPos3 = location[0] - (int)module4.slide - 2;
                        if (this.x2 == Integer.MIN_VALUE || xPos3 < this.x2) {
                            this.x2 = xPos3;
                            break;
                        }
                        break;
                    }
                    case LEFT: {
                        final int xPos3 = location[0] + (int)module4.slide + 14;
                        if (this.x2 == Integer.MIN_VALUE || xPos3 > this.x2) {
                            this.x2 = xPos3;
                            break;
                        }
                        break;
                    }
                }
            }
            this.y2 = location[1] + ((this.getFacing().getVertical() == Facing.Vertical.DOWN) ? (-textSpacer) : textSpacer) * modules.length;
            RenderUtils.drawBorderedRect((float)location[0], (float)(location[1] - 1), (float)(this.x2 - 7), this.y2, 3.0f, Integer.MIN_VALUE, 0);
        }
        GlStateManager.func_179117_G();
    }
    
    @Override
    public void destroyElement() {
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void handleKey(final char c, final int keyCode) {
    }
    
    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        boolean widthCollide = false;
        boolean heightCollide = false;
        switch (this.getFacing().getHorizontal()) {
            case RIGHT:
            case MIDDLE: {
                widthCollide = (mouseX >= this.x2 - 7 && mouseX <= location[0]);
                break;
            }
            case LEFT: {
                widthCollide = (mouseX <= this.x2 - 7 && mouseX >= location[0]);
                break;
            }
        }
        switch (this.getFacing().getVertical()) {
            case UP:
            case MIDDLE: {
                heightCollide = (mouseY >= location[1] - 2 && mouseY <= this.y2);
                break;
            }
            case DOWN: {
                heightCollide = (mouseY <= location[1] - 2 && mouseY >= this.y2);
                break;
            }
        }
        return widthCollide && heightCollide;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public Arraylist setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
}
