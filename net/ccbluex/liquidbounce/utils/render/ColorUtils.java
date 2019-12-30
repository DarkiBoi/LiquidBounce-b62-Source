// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;

public final class ColorUtils
{
    public static Color rainbow() {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() + 400000L) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color(currentColor.getRed() / 255.0f * 1.0f, currentColor.getGreen() / 255.0f * 1.0f, currentColor.getBlue() / 255.0f * 1.0f, currentColor.getAlpha() / 255.0f);
    }
    
    public static Color rainbow(final long offset) {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color(currentColor.getRed() / 255.0f * 1.0f, currentColor.getGreen() / 255.0f * 1.0f, currentColor.getBlue() / 255.0f * 1.0f, currentColor.getAlpha() / 255.0f);
    }
    
    public static Color rainbow(final float alpha) {
        return rainbow(400000L, alpha);
    }
    
    public static Color rainbow(final int alpha) {
        return rainbow(400000L, alpha / 255);
    }
    
    public static Color rainbow(final long offset, final int alpha) {
        return rainbow(offset, alpha / 255.0f);
    }
    
    public static Color rainbow(final long offset, final float alpha) {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color(currentColor.getRed() / 255.0f * 1.0f, currentColor.getGreen() / 255.0f * 1.0f, currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }
}
