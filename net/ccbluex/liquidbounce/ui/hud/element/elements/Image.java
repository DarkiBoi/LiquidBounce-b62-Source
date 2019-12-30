// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import java.nio.file.Files;
import java.util.Base64;
import java.io.File;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.valuesystem.types.TextValue;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Image extends Element
{
    private final TextValue image;
    private final ResourceLocation resourceLocation;
    private int width;
    private int height;
    
    public Image() {
        this.image = new TextValue("Image", "") {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                if (Image.this.image.asString().isEmpty()) {
                    return;
                }
                Image.this.setImage(Image.this.image.asString());
            }
        };
        this.resourceLocation = new ResourceLocation(RandomUtils.randomNumber(128));
        this.width = 64;
        this.height = 64;
    }
    
    @Override
    public void drawElement() {
        final int[] location = this.getLocationFromFacing();
        RenderUtils.drawImage(this.resourceLocation, location[0], location[1], this.width, this.height);
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)location[0], (float)location[1], (float)(location[0] + this.width), (float)(location[1] + this.height), 3.0f, Integer.MIN_VALUE, 0);
        }
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
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + this.width && mouseY <= location[1] + this.height;
    }
    
    private Image setImage(final String image) {
        try {
            this.image.setValueSilent(image);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(image));
            final BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            Minecraft.func_71410_x().func_110434_K().func_110579_a(this.resourceLocation, (ITextureObject)new DynamicTexture(bufferedImage));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public Image setImage(final File image) {
        try {
            this.setImage(Base64.getEncoder().encodeToString(Files.readAllBytes(image.toPath())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
