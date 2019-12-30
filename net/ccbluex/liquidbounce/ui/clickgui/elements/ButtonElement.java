// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.clickgui.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ButtonElement extends Element
{
    protected String displayName;
    protected int color;
    public int hoverTime;
    
    public ButtonElement(final String displayName) {
        this.color = 16777215;
        this.createButton(displayName);
    }
    
    public void createButton(final String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float button) {
        LiquidBounce.CLIENT.clickGui.style.drawButtonElement(mouseX, mouseY, this);
        super.drawScreen(mouseX, mouseY, button);
    }
    
    @Override
    public int getHeight() {
        return 16;
    }
    
    public boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + 16;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public int getColor() {
        return this.color;
    }
}
