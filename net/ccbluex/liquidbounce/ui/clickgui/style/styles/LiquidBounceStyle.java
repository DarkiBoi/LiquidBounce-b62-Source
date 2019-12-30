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
import net.minecraft.util.MathHelper;
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
import net.ccbluex.liquidbounce.ui.clickgui.elements.ButtonElement;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Objects;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.minecraft.util.StringUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.clickgui.Panel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.ui.clickgui.style.Style;

@SideOnly(Side.CLIENT)
public class LiquidBounceStyle implements Style
{
    private boolean mouseDown;
    private boolean rightMouseDown;
    
    @Override
    public void drawPanel(final int mouseX, final int mouseY, final Panel panel) {
        RenderUtils.drawBorderedRect(panel.getX() - (float)(panel.getScrollbar() ? 4 : 0), (float)panel.getY(), panel.getX() + (float)panel.getWidth(), panel.getY() + 19.0f + panel.getFade(), 1.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        final float textWidth = (float)Fonts.font35.func_78256_a("§f" + StringUtils.func_76338_a(panel.getName()));
        Fonts.font35.func_78276_b("§f" + panel.getName(), (int)(panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7, -16777216);
        if (panel.getScrollbar() && panel.getFade() > 0) {
            RenderUtils.drawRect((float)(panel.getX() - 2), (float)(panel.getY() + 21), (float)panel.getX(), (float)(panel.getY() + 16 + panel.getFade()), Integer.MAX_VALUE);
            RenderUtils.drawRect((float)(panel.getX() - 2), panel.getY() + 30 + (panel.getFade() - 24.0f) / (panel.getElements().size() - Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).maxElementsValue.asInteger()) * panel.getDragged() - 10.0f, (float)panel.getX(), panel.getY() + 40 + (panel.getFade() - 24.0f) / (panel.getElements().size() - Objects.requireNonNull(ModuleManager.getModule(ClickGUI.class)).maxElementsValue.asInteger()) * panel.getDragged(), Integer.MIN_VALUE);
        }
    }
    
    @Override
    public void drawDescription(final int mouseX, final int mouseY, final String text) {
        final int textWidth = Fonts.font35.func_78256_a(text);
        RenderUtils.drawBorderedRect((float)(mouseX + 9), (float)mouseY, (float)(mouseX + textWidth + 14), (float)(mouseY + Fonts.font35.field_78288_b + 3), 1.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(text, mouseX + 12, mouseY + Fonts.font35.field_78288_b / 2, Integer.MAX_VALUE);
    }
    
    @Override
    public void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement) {
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(buttonElement.getDisplayName(), (int)(buttonElement.getX() - (Fonts.font35.func_78256_a(buttonElement.getDisplayName()) - 100.0f) / 2.0f), buttonElement.getY() + 6, buttonElement.getColor());
    }
    
    @Override
    public void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement) {
        final Minecraft mc = Minecraft.func_71410_x();
        final int guiColor = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.font35.func_78276_b(moduleElement.getDisplayName(), (int)(moduleElement.getX() - (Fonts.font35.func_78256_a(moduleElement.getDisplayName()) - 100.0f) / 2.0f), moduleElement.getY() + 6, moduleElement.getModule().getState() ? guiColor : Integer.MAX_VALUE);
        final List<Value> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.font35.func_78276_b("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
                for (final Value value : moduleValues) {
                    if (value instanceof BoolValue) {
                        final String text = value.getValueName();
                        final float textWidth = (float)Fonts.font35.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            value.setValue(!value.asBoolean());
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, value.asBoolean() ? guiColor : Integer.MAX_VALUE);
                        yPos += 12;
                    }
                    else if (value instanceof ListValue) {
                        final ListValue listValue = (ListValue)value;
                        final String text2 = value.getValueName();
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b("§c" + text2, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        Fonts.font35.func_78276_b(listValue.open ? "-" : "+", (int)(moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - (listValue.open ? 5 : 6)), yPos + 4, 16777215);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                            listValue.open = !listValue.open;
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        }
                        yPos += 12;
                        for (final String valueOfList : listValue.getValues()) {
                            final float textWidth3 = (float)Fonts.font35.func_78256_a(">" + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                                moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                            }
                            if (listValue.open) {
                                RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                                if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                    listValue.setValue(valueOfList);
                                    mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                                }
                                GlStateManager.func_179117_G();
                                Fonts.font35.func_78276_b(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                                Fonts.font35.func_78276_b(valueOfList, moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, (listValue.asString() != null && listValue.asString().equalsIgnoreCase(valueOfList)) ? guiColor : Integer.MAX_VALUE);
                                yPos += 12;
                            }
                        }
                    }
                    else if (value instanceof FloatValue) {
                        final FloatValue floatValue = (FloatValue)value;
                        final String text2 = value.getValueName() + "§f: §c" + this.round(floatValue.asFloat());
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        final float sliderValue = moduleElement.getX() + moduleElement.getWidth() + (moduleElement.getSettingsWidth() - 12.0f) * (floatValue.asFloat() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown(0)) {
                            final double i = MathHelper.func_151237_a((double)((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), 0.0, 1.0);
                            floatValue.setValue(this.round((float)(floatValue.getMinimum() + (floatValue.getMaximum() - floatValue.getMinimum()) * i)));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 22;
                    }
                    else if (value instanceof IntegerValue) {
                        final IntegerValue integerValue = (IntegerValue)value;
                        final String text2 = value.getValueName() + "§f: §c" + (Object)((value instanceof BlockValue) ? (BlockUtils.getBlockName(integerValue.asInteger()) + " (" + integerValue.asInteger() + ")") : Integer.valueOf(integerValue.asInteger()));
                        final float textWidth2 = (float)Fonts.font35.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        final float sliderValue = moduleElement.getX() + moduleElement.getWidth() + (moduleElement.getSettingsWidth() - 12.0f) * (integerValue.asInteger() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown(0)) {
                            final double i = MathHelper.func_151237_a((double)((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), 0.0, 1.0);
                            integerValue.setValue((int)(integerValue.getMinimum() + (integerValue.getMaximum() - integerValue.getMinimum()) * i));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 22;
                    }
                    else if (value instanceof FontValue) {
                        final FontValue fontValue = (FontValue)value;
                        final FontRenderer fontRenderer = (FontRenderer)fontValue.asObject();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
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
                        Fonts.font35.func_78276_b(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        final int stringWidth = Fonts.font35.func_78256_a(displayString);
                        if (moduleElement.getSettingsWidth() < stringWidth + 8) {
                            moduleElement.setSettingsWidth((float)(stringWidth + 8));
                        }
                        if (((Mouse.isButtonDown(0) && !this.mouseDown) || (Mouse.isButtonDown(1) && !this.rightMouseDown)) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
                            final List<FontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown(0)) {
                                for (int j = 0; j < fonts.size(); ++j) {
                                    final FontRenderer font = fonts.get(j);
                                    if (font == fontRenderer) {
                                        if (++j >= fonts.size()) {
                                            j = 0;
                                        }
                                        fontValue.setValue(fonts.get(j));
                                        break;
                                    }
                                }
                            }
                            else {
                                for (int j = fonts.size() - 1; j >= 0; --j) {
                                    final FontRenderer font = fonts.get(j);
                                    if (font == fontRenderer) {
                                        if (--j >= fonts.size()) {
                                            j = 0;
                                        }
                                        if (j < 0) {
                                            j = fonts.size() - 1;
                                        }
                                        fontValue.setValue(fonts.get(j));
                                        break;
                                    }
                                }
                            }
                        }
                        yPos += 11;
                    }
                    else {
                        final String text = value.getValueName() + "§f: §c" + value.asObject();
                        final float textWidth = (float)Fonts.font35.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.font35.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 16777215);
                        yPos += 12;
                    }
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown(0);
                this.rightMouseDown = Mouse.isButtonDown(1);
                if (moduleElement.getSettingsWidth() > 0.0f && yPos > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(moduleElement.getY() + 6), moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), (float)(yPos + 2), 1.0f, Integer.MIN_VALUE, 0);
                }
            }
        }
    }
    
    private BigDecimal round(final float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, 4);
        return bd;
    }
}
