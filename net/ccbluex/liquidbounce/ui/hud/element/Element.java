// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Element
{
    private int x;
    private int y;
    private float scale;
    private Facing facing;
    public boolean drag;
    public int prevMouseX;
    public int prevMouseY;
    
    public Element() {
        this.scale = 1.0f;
    }
    
    public abstract void drawElement();
    
    public abstract void destroyElement();
    
    public abstract void handleMouseClick(final int p0, final int p1, final int p2);
    
    public abstract void handleKey(final char p0, final int p1);
    
    public abstract boolean isMouseOverElement(final int p0, final int p1);
    
    public int getX() {
        return this.x;
    }
    
    public Element setX(final int x) {
        this.x = x;
        return this;
    }
    
    public int getY() {
        return this.y;
    }
    
    public Element setY(final int y) {
        this.y = y;
        return this;
    }
    
    public Element setScale(float scale) {
        if (scale < 0.0f) {
            scale = 0.0f;
        }
        this.scale = scale;
        return this;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public Facing getFacing() {
        return this.facing;
    }
    
    public Element setFacing(final Facing facing) {
        this.facing = facing;
        return this;
    }
    
    public int[] getLocationFromFacing() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        return new int[] { (this.facing.getHorizontal() == Facing.Horizontal.RIGHT) ? (scaledResolution.func_78326_a() - this.x) : ((this.facing.getHorizontal() == Facing.Horizontal.MIDDLE) ? (scaledResolution.func_78326_a() / 2 + this.x) : this.x), (this.facing.getVertical() == Facing.Vertical.DOWN) ? (scaledResolution.func_78328_b() - this.y) : ((this.facing.getVertical() == Facing.Vertical.MIDDLE) ? (scaledResolution.func_78328_b() / 2 + this.y) : this.y) };
    }
    
    public Element setScreenX(final int x) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        switch (this.facing.getHorizontal()) {
            case LEFT: {
                this.x = x;
                break;
            }
            case MIDDLE: {
                this.x = x - scaledResolution.func_78326_a() / 2;
                break;
            }
            case RIGHT: {
                this.x = scaledResolution.func_78326_a() - x;
                break;
            }
        }
        return this;
    }
    
    public Element setScreenY(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        switch (this.facing.getVertical()) {
            case UP: {
                this.y = y;
                break;
            }
            case MIDDLE: {
                this.y = y - scaledResolution.func_78328_b() / 2;
                break;
            }
            case DOWN: {
                this.y = scaledResolution.func_78328_b() - y;
                break;
            }
        }
        return this;
    }
}
