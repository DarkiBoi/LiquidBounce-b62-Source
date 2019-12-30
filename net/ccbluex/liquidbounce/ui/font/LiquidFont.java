// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.font;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LiquidFont
{
    private final Font font;
    private int fontHeight;
    private int IMAGE_WIDTH;
    private int IMAGE_HEIGHT;
    private int texID;
    private final IntObject[] chars;
    
    LiquidFont(final Font font) {
        this.fontHeight = -1;
        this.IMAGE_HEIGHT = 1024;
        this.chars = new IntObject[2048];
        this.font = font;
        this.setupTexture();
    }
    
    private void setupTexture() {
        if (this.font.getSize() <= 15) {
            this.IMAGE_WIDTH = 256;
            this.IMAGE_HEIGHT = 256;
        }
        if (this.font.getSize() <= 43) {
            this.IMAGE_WIDTH = 512;
            this.IMAGE_HEIGHT = 512;
        }
        else if (this.font.getSize() <= 91) {
            this.IMAGE_WIDTH = 1024;
            this.IMAGE_HEIGHT = 1024;
        }
        else {
            this.IMAGE_WIDTH = 2048;
            this.IMAGE_HEIGHT = 2048;
        }
        final BufferedImage bufferedImage = new BufferedImage(this.IMAGE_WIDTH, this.IMAGE_HEIGHT, 2);
        final Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(this.font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.IMAGE_WIDTH, this.IMAGE_HEIGHT);
        graphics2D.setColor(Color.white);
        int rowHeight = 0;
        int positionX = 0;
        int positionY = 0;
        for (int i = 0; i < 2048; ++i) {
            final char ch = (char)i;
            final BufferedImage fontImage = this.getFontImage(ch);
            final IntObject newIntObject = new IntObject();
            newIntObject.width = fontImage.getWidth();
            newIntObject.height = fontImage.getHeight();
            if (positionX + newIntObject.width >= this.IMAGE_WIDTH) {
                positionX = 0;
                positionY += rowHeight;
                rowHeight = 0;
            }
            newIntObject.storedX = positionX;
            newIntObject.storedY = positionY;
            if (newIntObject.height > this.fontHeight) {
                this.fontHeight = newIntObject.height;
            }
            if (newIntObject.height > rowHeight) {
                rowHeight = newIntObject.height;
            }
            this.chars[i] = newIntObject;
            graphics2D.drawImage(fontImage, positionX, positionY, null);
            positionX += newIntObject.width;
        }
        try {
            this.texID = TextureUtil.func_110989_a(TextureUtil.func_110996_a(), bufferedImage, true, true);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    private BufferedImage getFontImage(final char ch) {
        final BufferedImage tempfontImage = new BufferedImage(1, 1, 2);
        final Graphics2D graphics2D = (Graphics2D)tempfontImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(this.font);
        final FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int charWidth = fontMetrics.charWidth(ch) + 8;
        if (charWidth <= 0) {
            charWidth = 7;
        }
        int charHeight = fontMetrics.getHeight() + 3;
        if (charHeight <= 0) {
            charHeight = this.font.getSize();
        }
        final BufferedImage fontImage = new BufferedImage(charWidth, charHeight, 2);
        final Graphics2D graphics = (Graphics2D)fontImage.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(this.font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(String.valueOf(ch), 3, 1 + fontMetrics.getAscent());
        return fontImage;
    }
    
    private void drawChar(final char c, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(x, y, (float)this.chars[c].width, (float)this.chars[c].height, (float)this.chars[c].storedX, (float)this.chars[c].storedY, (float)this.chars[c].width, (float)this.chars[c].height);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawQuad(final float x, final float y, final float width, final float height, final float srcX, final float srcY, final float srcWidth, final float srcHeight) {
        final float renderSRCX = srcX / this.IMAGE_WIDTH;
        final float renderSRCY = srcY / this.IMAGE_HEIGHT;
        final float renderSRCWidth = srcWidth / this.IMAGE_WIDTH;
        final float renderSRCHeight = srcHeight / this.IMAGE_HEIGHT;
        GL11.glBegin(4);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glEnd();
    }
    
    public void drawString(final String text, double x, double y, final Color color) {
        x *= 2.0;
        y *= 2.0;
        y -= 2.0;
        GL11.glPushMatrix();
        GL11.glScaled(0.25, 0.25, 0.25);
        GlStateManager.func_179144_i(this.texID);
        RenderUtils.glColor(color);
        for (int size = text.length(), indexInString = 0; indexInString < size; ++indexInString) {
            final char character = text.charAt(indexInString);
            if (character < this.chars.length) {
                this.drawChar(character, (float)x, (float)y);
                x += this.chars[character].width - 8;
            }
        }
        GL11.glPopMatrix();
    }
    
    int getStringHeight(final String text) {
        int lines = 1;
        for (final char c : text.toCharArray()) {
            if (c == '\n') {
                ++lines;
            }
        }
        return (this.fontHeight - 8) / 2 * lines;
    }
    
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }
    
    public int getStringWidth(final String text) {
        int width = 0;
        for (final char c : text.toCharArray()) {
            if (c < this.chars.length) {
                width += this.chars[c].width - 8;
            }
        }
        return width / 2;
    }
    
    public Font getFont() {
        return this.font;
    }
    
    private class IntObject
    {
        int width;
        int height;
        int storedX;
        int storedY;
    }
}
