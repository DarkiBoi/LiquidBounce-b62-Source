// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui.style.styles;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import net.ccbluex.liquidbounce.ui.font.LiquidFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.types.FontValue;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.valuesystem.types.BlockValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.Value;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ModuleElement;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.ui.clickgui.elements.ButtonElement;
import net.minecraft.util.StringUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.clickgui.Panel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.ui.clickgui.style.Style;

@SideOnly(Side.CLIENT)
public class SlowlyStyle implements Style
{
    private boolean mouseDown;
    private boolean rightMouseDown;
    
    @Override
    public void drawPanel(final int mouseX, final int mouseY, final Panel panel) {
        RenderUtils.drawBorderedRect((float)panel.getX(), panel.getY() - 3.0f, panel.getX() + (float)panel.getWidth(), panel.getY() + 17.0f, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect((float)panel.getX(), panel.getY() + 17.0f, panel.getX() + (float)panel.getWidth(), (float)(panel.getY() + 19 + panel.getFade()), 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
            RenderUtils.drawBorderedRect((float)panel.getX(), (float)(panel.getY() + 17 + panel.getFade()), panel.getX() + (float)panel.getWidth(), (float)(panel.getY() + 19 + panel.getFade() + 5), 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        }
        GlStateManager.func_179117_G();
        final float textWidth = (float)Fonts.font35.func_78256_a("§f" + StringUtils.func_76338_a(panel.getName()));
        Fonts.font35.func_78276_b(panel.getName(), (int)(panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7 - 3, Color.WHITE.getRGB());
    }
    
    @Override
    public void drawDescription(final int mouseX, final int mouseY, final String text) {
        final int textWidth = Fonts.font35.func_78256_a(text);
        RenderUtils.drawBorderedRect((float)(mouseX + 9), (float)mouseY, (float)(mouseX + textWidth + 14), (float)(mouseY + Fonts.font35.field_78288_b + 3), 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(text, mouseX + 12, mouseY + Fonts.font35.field_78288_b / 2, Color.WHITE.getRGB());
    }
    
    @Override
    public void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement) {
        Gui.func_73734_a(buttonElement.getX() - 1, buttonElement.getY() - 1, buttonElement.getX() + buttonElement.getWidth() + 1, buttonElement.getY() + buttonElement.getHeight() + 1, this.hoverColor((buttonElement.getColor() != Integer.MAX_VALUE) ? new Color(7, 152, 252) : new Color(54, 71, 96), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 5, Color.WHITE.getRGB());
    }
    
    @Override
    public void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement) {
        final Minecraft mc = Minecraft.func_71410_x();
        Gui.func_73734_a(moduleElement.getX() - 1, moduleElement.getY() - 1, moduleElement.getX() + moduleElement.getWidth() + 1, moduleElement.getY() + moduleElement.getHeight() + 1, this.hoverColor(new Color(54, 71, 96), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a(moduleElement.getX() - 1, moduleElement.getY() - 1, moduleElement.getX() + moduleElement.getWidth() + 1, moduleElement.getY() + moduleElement.getHeight() + 1, this.hoverColor(new Color(7, 152, 252, moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(moduleElement.getDisplayName(), moduleElement.getX() + 5, moduleElement.getY() + 5, Color.WHITE.getRGB());
        final List<Value> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.font35.func_78276_b(">", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + 5, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                if (moduleElement.getSettingsWidth() > 0.0f && moduleElement.slowlySettingsYPos > moduleElement.getY() + 6) {
                    RenderUtils.drawBorderedRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(moduleElement.getY() + 6), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(moduleElement.slowlySettingsYPos + 2), 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
                }
                moduleElement.slowlySettingsYPos = moduleElement.getY() + 6;
                for (final Value value : moduleValues) {
                    if (value instanceof BoolValue) {
                        final String text = value.getValueName();
                        final float textWidth = (float)Fonts.font35.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            value.setValue(!value.asBoolean());
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        Fonts.font35.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, value.asBoolean() ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                        moduleElement.slowlySettingsYPos += 11;
                    }
                    else if (value instanceof ListValue) {
                        final ListValue listValue = (ListValue)value;
                        final String text2 = value.getValueName();
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        Fonts.font35.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, 16777215);
                        Fonts.font35.func_78276_b(listValue.open ? "-" : "+", (int)(moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - (listValue.open ? 5 : 6)), moduleElement.slowlySettingsYPos + 2, 16777215);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + Fonts.font35.field_78288_b && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            listValue.open = !listValue.open;
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        moduleElement.slowlySettingsYPos += Fonts.font35.field_78288_b + 1;
                        for (final String valueOfList : listValue.getValues()) {
                            final float textWidth3 = (float)Fonts.font35.func_78256_a("> " + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth3 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth3 + 12.0f);
                            }
                            if (listValue.open) {
                                if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos + 2 && mouseY <= moduleElement.slowlySettingsYPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                    listValue.setValue(valueOfList);
                                    mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                                }
                                GlStateManager.func_179117_G();
                                Fonts.font35.func_78276_b("> " + valueOfList, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, (listValue.asString() != null && listValue.asString().equalsIgnoreCase(valueOfList)) ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                                moduleElement.slowlySettingsYPos += Fonts.font35.field_78288_b + 1;
                            }
                        }
                        if (listValue.open) {
                            continue;
                        }
                        ++moduleElement.slowlySettingsYPos;
                    }
                    else if (value instanceof FloatValue) {
                        final FloatValue floatValue = (FloatValue)value;
                        final String text2 = value.getValueName() + "§f: " + this.round(floatValue.asFloat());
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        final float valueOfSlide = RenderUtils.drawSlider(floatValue.asFloat(), floatValue.getMinimum(), floatValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(7, 152, 252));
                        if (valueOfSlide != floatValue.asFloat()) {
                            floatValue.setValue(valueOfSlide);
                        }
                        Fonts.font35.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 16777215);
                        moduleElement.slowlySettingsYPos += 19;
                    }
                    else if (value instanceof IntegerValue) {
                        final IntegerValue integerValue = (IntegerValue)value;
                        final String text2 = value.getValueName() + "§f: " + (Object)((value instanceof BlockValue) ? (BlockUtils.getBlockName(integerValue.asInteger()) + " (" + integerValue.asInteger() + ")") : Integer.valueOf(integerValue.asInteger()));
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        final float valueOfSlide = RenderUtils.drawSlider((float)integerValue.asInteger(), (float)integerValue.getMinimum(), (float)integerValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(7, 152, 252));
                        if (valueOfSlide != integerValue.asInteger()) {
                            integerValue.setValue((int)valueOfSlide);
                        }
                        Fonts.font35.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 16777215);
                        moduleElement.slowlySettingsYPos += 19;
                    }
                    else if (value instanceof FontValue) {
                        final FontValue fontValue = (FontValue)value;
                        final FontRenderer fontRenderer = (FontRenderer)fontValue.asObject();
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof LiquidFontRenderer) {
                            final LiquidFontRenderer liquidFontRenderer = (LiquidFontRenderer)fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getFont().getFont().getName() + " - " + liquidFontRenderer.getFont().getFont().getSize();
                        }
                        else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: Minecraft";
                        }
                        else {
                            final Object[] objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects[0] + (((int)objects[1] != -1) ? (" - " + objects[1]) : "");
                            }
                        }
                        Fonts.font35.func_78276_b(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, Color.WHITE.getRGB());
                        final int stringWidth = Fonts.font35.func_78256_a(displayString);
                        if (moduleElement.getSettingsWidth() < stringWidth + 8) {
                            moduleElement.setSettingsWidth((float)(stringWidth + 8));
                        }
                        if (((Mouse.isButtonDown(0) && !this.mouseDown) || (Mouse.isButtonDown(1) && !this.rightMouseDown)) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12) {
                            final List<FontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown(0)) {
                                for (int i = 0; i < fonts.size(); ++i) {
                                    final FontRenderer font = fonts.get(i);
                                    if (font == fontRenderer) {
                                        if (++i >= fonts.size()) {
                                            i = 0;
                                        }
                                        fontValue.setValue(fonts.get(i));
                                        break;
                                    }
                                }
                            }
                            else {
                                for (int i = fonts.size() - 1; i >= 0; --i) {
                                    final FontRenderer font = fonts.get(i);
                                    if (font == fontRenderer) {
                                        if (--i >= fonts.size()) {
                                            i = 0;
                                        }
                                        if (i < 0) {
                                            i = fonts.size() - 1;
                                        }
                                        fontValue.setValue(fonts.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                        moduleElement.slowlySettingsYPos += 11;
                    }
                    else {
                        final String text = value.getValueName() + "§f: " + value.asObject();
                        final float textWidth = (float)Fonts.font35.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 4, 16777215);
                        moduleElement.slowlySettingsYPos += 12;
                    }
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown(0);
                this.rightMouseDown = Mouse.isButtonDown(1);
            }
        }
    }
    
    private BigDecimal round(final float v) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(v));
        bigDecimal = bigDecimal.setScale(2, 4);
        return bigDecimal;
    }
    
    private Color hoverColor(final Color color, final int hover) {
        final int r = color.getRed() - hover * 2;
        final int g = color.getGreen() - hover * 2;
        final int b = color.getBlue() - hover * 2;
        return new Color((r < 0) ? 0 : r, (g < 0) ? 0 : g, (b < 0) ? 0 : b, color.getAlpha());
    }
}
