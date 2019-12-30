// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui.elements;

public class Element
{
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float button) {
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
