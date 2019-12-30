// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud;

import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.ccbluex.liquidbounce.ui.hud.element.Element;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUD
{
    private final List<Element> elements;
    private final List<Notification> notifications;
    
    public HUD() {
        this.elements = new ArrayList<Element>();
        this.notifications = new ArrayList<Notification>();
    }
    
    public void render() {
        for (final Element element : this.elements) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(element.getScale(), element.getScale(), element.getScale());
            try {
                element.drawElement();
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
            GlStateManager.func_179121_F();
        }
    }
    
    public void handleMouseClick(final int mouseX, final int mouseY, final int button) {
        for (final Element element : this.elements) {
            element.handleMouseClick((int)(mouseX / element.getScale()), (int)(mouseY / element.getScale()), button);
        }
        for (int i = this.elements.size() - 1; i >= 0; --i) {
            final Element element = this.elements.get(i);
            if (element.isMouseOverElement((int)(mouseX / element.getScale()), (int)(mouseY / element.getScale())) && button == 0) {
                element.drag = true;
                this.elements.remove(element);
                this.elements.add(element);
                break;
            }
        }
    }
    
    public void handleMouseReleased() {
        for (final Element element : this.elements) {
            element.drag = false;
        }
    }
    
    public void handleMouseMove(final int mouseX, final int mouseY) {
        for (final Element element : this.elements) {
            final int scaledX = (int)(mouseX / element.getScale());
            final int scaledY = (int)(mouseY / element.getScale());
            if (element.drag && Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
                switch (element.getFacing().getHorizontal()) {
                    case LEFT:
                    case MIDDLE: {
                        element.setX(element.getX() + (scaledX - element.prevMouseX));
                        break;
                    }
                    case RIGHT: {
                        element.setX(element.getX() - (scaledX - element.prevMouseX));
                        break;
                    }
                }
                switch (element.getFacing().getVertical()) {
                    case UP:
                    case MIDDLE: {
                        element.setY(element.getY() + (scaledY - element.prevMouseY));
                        break;
                    }
                    case DOWN: {
                        element.setY(element.getY() - (scaledY - element.prevMouseY));
                        break;
                    }
                }
            }
            element.prevMouseX = scaledX;
            element.prevMouseY = scaledY;
        }
    }
    
    public void handleKey(final char c, final int keyCode) {
        this.elements.forEach(element -> element.handleKey(c, keyCode));
    }
    
    public void addElement(final Element element) {
        this.elements.add(element);
    }
    
    public void removeElement(final Element element) {
        element.destroyElement();
        this.elements.remove(element);
    }
    
    public List<Element> getElements() {
        return this.elements;
    }
    
    public void addNotification(final Notification notification) {
        this.notifications.add(notification);
    }
    
    public void removeNotification(final Notification notification) {
        this.notifications.remove(notification);
    }
    
    public List<Notification> getNotifications() {
        return this.notifications;
    }
    
    public void clearElements() {
        this.elements.forEach(Element::destroyElement);
        this.elements.clear();
    }
}
