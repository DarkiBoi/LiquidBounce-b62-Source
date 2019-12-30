// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.font;

import net.minecraft.client.resources.IResourceManager;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.TextEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.awt.Font;
import net.minecraft.client.gui.FontRenderer;

public class LiquidFontRenderer extends FontRenderer
{
    private LiquidFont font;
    private LiquidFont boldFont;
    private LiquidFont italicFont;
    private LiquidFont boldItalicFont;
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    
    LiquidFontRenderer(final Font font) {
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), Minecraft.func_71410_x().func_110434_K(), false);
        this.setFont(font);
        this.field_78288_b = this.getHeight();
    }
    
    public void drawString(final String s, final float x, final float y, final int color) {
        this.func_175065_a(s, x, y, color, false);
    }
    
    public int func_175063_a(final String text, final float x, final float y, final int color) {
        return this.func_175065_a(text, x, y, color, true);
    }
    
    public void drawCenteredString(final String s, final int x, final int y, final int color, final boolean shadow) {
        this.func_175065_a(s, (float)(x - this.func_78256_a(s) / 2), (float)y, color, shadow);
    }
    
    public void drawCenteredString(final String s, final int x, final int y, final int color) {
        this.func_175063_a(s, (float)(x - this.func_78256_a(s) / 2), (float)y, color);
    }
    
    public int func_175065_a(String text, final float x, float y, final int color, final boolean shadow) {
        final TextEvent event = new TextEvent(text);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        text = StringUtils.patchString(event.getText());
        y -= 3.0f;
        if (shadow) {
            this.drawText(text, x + 1.0f, y + 1.0f, new Color(0, 0, 0, 150).getRGB(), true);
        }
        return this.drawText(text, x, y, color, false);
    }
    
    private int drawText(final String text, final float x, final float y, int color, final boolean ignoreColor) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x - 1.5, y + 0.5, 0.0);
        final boolean wasBlend = GL11.glGetBoolean(3042);
        GlStateManager.func_179141_d();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3553);
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final Color startColor = new Color(red, green, blue, alpha);
        if (text.contains("§")) {
            final String[] parts = text.split("§");
            Color currentColor = startColor;
            LiquidFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikeThrough = false;
            boolean underline = false;
            for (int index = 0; index < parts.length; ++index) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        currentFont.drawString(parts[index], width, 0.0, currentColor);
                        width += currentFont.getStringWidth(parts[index]);
                    }
                    else {
                        final String words = parts[index].substring(1);
                        final char type = parts[index].charAt(0);
                        final int colorIndex = "0123456789abcdefklmnor".indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                if (!ignoreColor) {
                                    final int colorCode = ChatColor.colors[colorIndex];
                                    currentColor = new Color((colorCode >> 16) / 255.0f, (colorCode >> 8 & 0xFF) / 255.0f, (colorCode & 0xFF) / 255.0f, alpha);
                                }
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikeThrough = false;
                            }
                            else if (colorIndex == 16) {
                                randomCase = true;
                            }
                            else if (colorIndex == 17) {
                                bold = true;
                            }
                            else if (colorIndex == 18) {
                                strikeThrough = true;
                            }
                            else if (colorIndex == 19) {
                                underline = true;
                            }
                            else if (colorIndex == 20) {
                                italic = true;
                            }
                            else if (colorIndex == 21) {
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikeThrough = false;
                                currentColor = startColor;
                            }
                        }
                        if (bold && italic) {
                            this.boldItalicFont.drawString(randomCase ? ChatColor.toRandom(words) : words, width, 0.0, currentColor);
                            currentFont = this.boldItalicFont;
                        }
                        else if (bold) {
                            this.boldFont.drawString(randomCase ? ChatColor.toRandom(words) : words, width, 0.0, currentColor);
                            currentFont = this.boldFont;
                        }
                        else if (italic) {
                            this.italicFont.drawString(randomCase ? ChatColor.toRandom(words) : words, width, 0.0, currentColor);
                            currentFont = this.italicFont;
                        }
                        else {
                            this.font.drawString(randomCase ? ChatColor.toRandom(words) : words, width, 0.0, currentColor);
                            currentFont = this.font;
                        }
                        final float u = this.field_78288_b / 16.0f;
                        final int h = currentFont.getStringHeight(words);
                        if (strikeThrough) {
                            RenderUtils.drawLine(width / 2.0 + 1.0, h / 3, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 3, u);
                        }
                        if (underline) {
                            RenderUtils.drawLine(width / 2.0 + 1.0, h / 2, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 2, u);
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
            }
        }
        else {
            this.font.drawString(text, 0.0, 0.0, startColor);
        }
        if (!wasBlend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (int)(x + this.func_78256_a(text));
    }
    
    public int getHeight() {
        return this.font.getHeight() / 2;
    }
    
    public int func_175064_b(final char charCode) {
        return ChatColor.colors["0123456789abcdef".indexOf(charCode)];
    }
    
    public int func_78256_a(String text) {
        if (text == null) {
            return 0;
        }
        final TextEvent event = new TextEvent(text);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        text = event.getText();
        text = StringUtils.patchString(text);
        if (text.contains("§")) {
            final String[] parts = text.split("§");
            LiquidFont currentFont = this.font;
            int width = 0;
            boolean bold = false;
            boolean italic = false;
            for (int index = 0; index < parts.length; ++index) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        width += currentFont.getStringWidth(parts[index]);
                    }
                    else {
                        final String words = parts[index].substring(1);
                        final char type = parts[index].charAt(0);
                        final int colorIndex = "0123456789abcdefklmnor".indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                bold = false;
                                italic = false;
                            }
                            else if (colorIndex == 17) {
                                bold = true;
                            }
                            else if (colorIndex == 20) {
                                italic = true;
                            }
                            else if (colorIndex == 21) {
                                bold = false;
                                italic = false;
                            }
                        }
                        if (bold && italic) {
                            currentFont = this.boldItalicFont;
                        }
                        else if (bold) {
                            currentFont = this.boldFont;
                        }
                        else if (italic) {
                            currentFont = this.italicFont;
                        }
                        else {
                            currentFont = this.font;
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
            }
            return width / 2;
        }
        return this.font.getStringWidth(text) / 2;
    }
    
    public int func_78263_a(final char character) {
        return this.func_78256_a(Character.toString(character));
    }
    
    private void setFont(final Font font) {
        synchronized (this) {
            this.font = new LiquidFont(font);
            this.boldFont = new LiquidFont(font.deriveFont(1));
            this.italicFont = new LiquidFont(font.deriveFont(2));
            this.boldItalicFont = new LiquidFont(font.deriveFont(3));
            this.field_78288_b = this.getHeight();
        }
    }
    
    public LiquidFont getFont() {
        return this.font;
    }
    
    public int getSize() {
        return this.font.getFont().getSize();
    }
    
    public void func_110549_a(final IResourceManager resourceManager) {
    }
}
