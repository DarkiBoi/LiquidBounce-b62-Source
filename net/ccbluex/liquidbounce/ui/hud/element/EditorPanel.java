// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element;

import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Field;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.hud.NameIndex;
import net.ccbluex.liquidbounce.ui.font.LiquidFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.Value;
import java.math.BigDecimal;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EditorPanel
{
    private final GuiHudDesigner hudDesigner;
    private int x;
    private int y;
    private int width;
    private int height;
    private int realHeight;
    private boolean mouseDown;
    private boolean rightMouseDown;
    private boolean drag;
    private int dragX;
    private int dragY;
    private int scroll;
    private Element element;
    
    public EditorPanel(final GuiHudDesigner hudDesigner, final int x, final int y) {
        this.width = 80;
        this.height = 20;
        this.realHeight = 20;
        this.hudDesigner = hudDesigner;
        this.x = x;
        this.y = y;
    }
    
    public void drawPanel(final int mouseX, int mouseY, final int wheel) {
        this.drag(mouseX, mouseY);
        if (this.element != this.hudDesigner.selectedElement) {
            this.scroll = 0;
        }
        this.element = this.hudDesigner.selectedElement;
        final boolean shouldScroll = this.realHeight > 200;
        if (shouldScroll) {
            GL11.glPushMatrix();
            RenderUtils.makeScissorBox((float)this.x, (float)(this.y + 1), (float)(this.x + this.width), (float)(this.y + 200));
            GL11.glEnable(3089);
            if (this.y + 200 < mouseY) {
                mouseY = -1;
            }
            if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 200 && Mouse.hasWheel()) {
                if (wheel < 0 && -this.scroll + 205 <= this.realHeight) {
                    this.scroll -= 5;
                }
                else if (wheel > 0) {
                    this.scroll += 5;
                    if (this.scroll > 0) {
                        this.scroll = 0;
                    }
                }
            }
        }
        Gui.func_73734_a(this.x, this.y + 12, this.x + this.width, this.y + this.realHeight, new Color(27, 34, 40).getRGB());
        if (this.hudDesigner.selectedElement != null) {
            this.height = this.scroll + 15;
            this.realHeight = 15;
            this.width = 80;
            Fonts.font35.func_78276_b("X: " + this.hudDesigner.selectedElement.getLocationFromFacing()[0] + " (" + this.hudDesigner.selectedElement.getX() + ")", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            this.height += 10;
            this.realHeight += 10;
            Fonts.font35.func_78276_b("Y: " + this.hudDesigner.selectedElement.getLocationFromFacing()[1] + " (" + this.hudDesigner.selectedElement.getY() + ")", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            this.height += 10;
            this.realHeight += 10;
            BigDecimal bd = new BigDecimal(Float.toString(this.hudDesigner.selectedElement.getScale()));
            bd = bd.setScale(2, 4);
            Fonts.font35.func_78276_b("Scale: " + bd.toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            this.height += 10;
            this.realHeight += 10;
            Fonts.font35.func_78276_b("H:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            Fonts.font35.func_78276_b(this.hudDesigner.selectedElement.getFacing().getHorizontal().getName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
            if (Mouse.isButtonDown(0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                for (int i = 0; i < Facing.Horizontal.values().length; ++i) {
                    if (Facing.Horizontal.values()[i] == this.hudDesigner.selectedElement.getFacing().getHorizontal()) {
                        if (++i >= Facing.Horizontal.values().length) {
                            i = 0;
                        }
                        final int[] location = this.hudDesigner.selectedElement.getLocationFromFacing();
                        this.hudDesigner.selectedElement.getFacing().setHorizontal(Facing.Horizontal.values()[i]);
                        this.hudDesigner.selectedElement.setScreenX(location[0]).setScreenY(location[1]);
                        break;
                    }
                }
            }
            this.height += 10;
            this.realHeight += 10;
            if (Mouse.isButtonDown(0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                for (int i = 0; i < Facing.Vertical.values().length; ++i) {
                    if (Facing.Vertical.values()[i] == this.hudDesigner.selectedElement.getFacing().getVertical()) {
                        if (++i >= Facing.Vertical.values().length) {
                            i = 0;
                        }
                        final int[] location = this.hudDesigner.selectedElement.getLocationFromFacing();
                        this.hudDesigner.selectedElement.getFacing().setVertical(Facing.Vertical.values()[i]);
                        this.hudDesigner.selectedElement.setScreenX(location[0]).setScreenY(location[1]);
                        break;
                    }
                }
            }
            Fonts.font35.func_78276_b("V:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            Fonts.font35.func_78276_b(this.hudDesigner.selectedElement.getFacing().getVertical().getName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
            this.height += 10;
            this.realHeight += 10;
            this.width = 100;
            for (final Field field : this.hudDesigner.selectedElement.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object o = field.get(this.hudDesigner.selectedElement);
                    if (o instanceof Value) {
                        final Value value = (Value)o;
                        if (value instanceof BoolValue) {
                            final BoolValue boolValue = (BoolValue)value;
                            Fonts.font35.func_78276_b(value.getValueName(), this.x + 2, this.y + this.height, boolValue.asBoolean() ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                            final int stringWidth = Fonts.font35.func_78256_a(value.getValueName());
                            if (this.width < stringWidth + 8) {
                                this.width = stringWidth + 8;
                            }
                            if (Mouse.isButtonDown(0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                                value.setValue(!boolValue.asBoolean());
                            }
                            this.height += 10;
                            this.realHeight += 10;
                        }
                        if (o instanceof FloatValue) {
                            final float valueNumber = ((FloatValue)o).asFloat();
                            final float minNumber = ((FloatValue)o).getMinimum();
                            final float maxNumber = ((FloatValue)o).getMaximum();
                            BigDecimal floatBigDecimal = new BigDecimal(Float.toString(valueNumber));
                            floatBigDecimal = floatBigDecimal.setScale(2, 4);
                            Fonts.font35.func_78276_b(value.getValueName() + ": §c" + floatBigDecimal.toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                            final int stringWidth2 = Fonts.font35.func_78256_a(value.getValueName() + ": §c" + valueNumber);
                            if (this.width < stringWidth2 + 8) {
                                this.width = stringWidth2 + 8;
                            }
                            RenderUtils.drawRect((float)(this.x + 8), (float)(this.y + this.height + 12), (float)(this.x + this.width), (float)(this.y + this.height + 13), Color.WHITE);
                            final float sliderValue = this.x + (this.width - 12) * (valueNumber - minNumber) / (maxNumber - minNumber);
                            RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height + 9), sliderValue + 11.0f, (float)(this.y + this.height + 15), new Color(37, 126, 255).getRGB());
                            if (mouseX >= this.x + 8 && mouseX <= this.x + this.width && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown(0)) {
                                final float j = MathHelper.func_76131_a((mouseX - this.x - 8) / (float)(this.width - 12), 0.0f, 1.0f);
                                value.setValue(minNumber + (maxNumber - minNumber) * j);
                            }
                            this.height += 20;
                            this.realHeight += 20;
                        }
                        if (o instanceof IntegerValue) {
                            final int valueNumber2 = ((IntegerValue)o).asInteger();
                            final int minNumber2 = ((IntegerValue)o).getMinimum();
                            final int maxNumber2 = ((IntegerValue)o).getMaximum();
                            Fonts.font35.func_78276_b(value.getValueName() + ": §c" + valueNumber2, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                            BigDecimal integerBigDecimal = new BigDecimal(Float.toString((float)valueNumber2));
                            integerBigDecimal = integerBigDecimal.setScale(2, 4);
                            final int stringWidth2 = Fonts.font35.func_78256_a(value.getValueName() + ": §c" + integerBigDecimal.toString());
                            if (this.width < stringWidth2 + 8) {
                                this.width = stringWidth2 + 8;
                            }
                            RenderUtils.drawRect((float)(this.x + 8), (float)(this.y + this.height + 12), (float)(this.x + this.width - 8), (float)(this.y + this.height + 13), Color.WHITE);
                            final float sliderValue = (float)(this.x + (this.width - 18) * (valueNumber2 - minNumber2) / (maxNumber2 - minNumber2));
                            RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height + 9), sliderValue + 11.0f, (float)(this.y + this.height + 15), new Color(37, 126, 255).getRGB());
                            if (mouseX >= this.x + 8 && mouseX <= this.x + this.width && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown(0)) {
                                final float j = MathHelper.func_76131_a((mouseX - this.x - 8) / (float)(this.width - 18), 0.0f, 1.0f);
                                value.setValue((int)(minNumber2 + (maxNumber2 - minNumber2) * j));
                            }
                            this.height += 20;
                            this.realHeight += 20;
                        }
                        if (o instanceof ListValue) {
                            final ListValue listValue = (ListValue)o;
                            Fonts.font35.func_78276_b(value.getValueName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                            this.height += 10;
                            this.realHeight += 10;
                            for (final String s : listValue.getValues()) {
                                Fonts.font35.func_78276_b("§c> §r" + s, this.x + 2, this.y + this.height, s.equals(listValue.asString()) ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                                final int stringWidth3 = Fonts.font35.func_78256_a("§c> §r" + s);
                                if (this.width < stringWidth3 + 8) {
                                    this.width = stringWidth3 + 8;
                                }
                                if (Mouse.isButtonDown(0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                                    value.setValue(s);
                                }
                                this.height += 10;
                                this.realHeight += 10;
                            }
                        }
                    }
                    if (o instanceof FontRenderer) {
                        final FontRenderer fontRenderer = (FontRenderer)o;
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof LiquidFontRenderer) {
                            final LiquidFontRenderer liquidFontRenderer = (LiquidFontRenderer)fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getFont().getFont().getName() + " - " + liquidFontRenderer.getFont().getFont().getSize();
                        }
                        else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: Minecraft";
                        }
                        Fonts.font35.func_78276_b(displayString, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                        final int stringWidth = Fonts.font35.func_78256_a(displayString);
                        if (this.width < stringWidth + 8) {
                            this.width = stringWidth + 8;
                        }
                        if (((Mouse.isButtonDown(0) && !this.mouseDown) || (Mouse.isButtonDown(1) && !this.rightMouseDown)) && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                            final List<FontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown(0)) {
                                for (int k = 0; k < fonts.size(); ++k) {
                                    final FontRenderer font = fonts.get(k);
                                    if (font == fontRenderer) {
                                        if (++k >= fonts.size()) {
                                            k = 0;
                                        }
                                        field.set(this.hudDesigner.selectedElement, fonts.get(k));
                                        break;
                                    }
                                }
                            }
                            else {
                                for (int k = fonts.size() - 1; k >= 0; --k) {
                                    final FontRenderer font = fonts.get(k);
                                    if (font == fontRenderer) {
                                        if (--k >= fonts.size()) {
                                            k = 0;
                                        }
                                        if (k < 0) {
                                            k = fonts.size() - 1;
                                        }
                                        field.set(this.hudDesigner.selectedElement, fonts.get(k));
                                        break;
                                    }
                                }
                            }
                        }
                        this.height += 10;
                        this.realHeight += 10;
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + 12, new Color(37, 126, 255).getRGB());
            Fonts.font35.drawString("§l" + NameIndex.getName(this.hudDesigner.selectedElement.getClass()), (float)(this.x + 2), this.y + 3.5f, Color.WHITE.getRGB());
        }
        else {
            this.height = 15 + this.scroll;
            this.realHeight = 15;
            this.width = 90;
            for (final Element element : LiquidBounce.CLIENT.hud.getElements()) {
                Fonts.font35.func_78276_b(NameIndex.getName(element.getClass()), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                final int stringWidth4 = Fonts.font35.func_78256_a(NameIndex.getName(element.getClass()));
                if (this.width < stringWidth4 + 8) {
                    this.width = stringWidth4 + 8;
                }
                if (Mouse.isButtonDown(0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                    this.hudDesigner.selectedElement = element;
                }
                this.height += 10;
                this.realHeight += 10;
            }
            Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + 12, new Color(37, 126, 255).getRGB());
            Fonts.font35.drawString("§lAvailable Elements", (float)(this.x + 2), this.y + 3.5f, Color.WHITE.getRGB());
        }
        if (shouldScroll) {
            Gui.func_73734_a(this.x + this.width - 5, this.y + 15, this.x + this.width - 2, this.y + 197, new Color(41, 41, 41).getRGB());
            final float v = 197.0f * (-this.scroll / (this.realHeight - 170.0f));
            RenderUtils.drawRect((float)(this.x + this.width - 5), this.y + 15 + v, (float)(this.x + this.width - 2), this.y + 15 + v + 5.0f, new Color(37, 126, 255).getRGB());
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
        this.mouseDown = Mouse.isButtonDown(0);
        this.rightMouseDown = Mouse.isButtonDown(1);
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 12 && Mouse.isButtonDown(0) && !this.mouseDown) {
            this.drag = true;
            this.dragX = mouseX - this.x;
            this.dragY = mouseY - this.y;
        }
        if (Mouse.isButtonDown(0) && this.drag) {
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
        }
        else {
            this.drag = false;
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getRealHeight() {
        return this.realHeight;
    }
}
