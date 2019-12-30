// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class IconUtils
{
    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[] { readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/" + "LiquidBounce".toLowerCase() + "/icon_16x16.png")), readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/" + "LiquidBounce".toLowerCase() + "/icon_32x32.png")) };
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        if (imageStream == null) {
            return null;
        }
        final BufferedImage bufferedImage = ImageIO.read(imageStream);
        final int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for (final int i : rgb) {
            byteBuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}
