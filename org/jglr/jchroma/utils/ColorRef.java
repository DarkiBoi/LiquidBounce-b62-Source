// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma.utils;

public class ColorRef
{
    public static final ColorRef NULL;
    private int red;
    private int green;
    private int blue;
    
    public ColorRef(final int red, final int green, final int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public int getValue() {
        return (this.getBlue() & 0xFF) << 16 | (this.getGreen() & 0xFF) << 8 | (this.getRed() & 0xFF);
    }
    
    public int getRed() {
        return this.red;
    }
    
    public void setRed(final int red) {
        this.red = red;
    }
    
    public int getGreen() {
        return this.green;
    }
    
    public void setGreen(final int green) {
        this.green = green;
    }
    
    public int getBlue() {
        return this.blue;
    }
    
    public void setBlue(final int blue) {
        this.blue = blue;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ColorRef) {
            final ColorRef other = (ColorRef)obj;
            return other.red == this.red && other.blue == this.blue && other.green == this.green;
        }
        return super.equals(obj);
    }
    
    public static ColorRef fromRGB(final int bgr) {
        final int blue = bgr & 0xFF;
        final int green = bgr >> 8 & 0xFF;
        final int red = bgr >> 16 & 0xFF;
        return new ColorRef(red, green, blue);
    }
    
    public static ColorRef fromBGR(final int bgr) {
        final int red = bgr & 0xFF;
        final int green = bgr >> 8 & 0xFF;
        final int blue = bgr >> 16 & 0xFF;
        return new ColorRef(red, green, blue);
    }
    
    static {
        NULL = new ColorRef(0, 0, 0);
    }
}
