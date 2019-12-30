// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.FadeState;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Notifications extends Element
{
    private final Notification exampleNotification;
    
    public Notifications() {
        this.exampleNotification = new Notification("Example Notification");
    }
    
    @Override
    public void drawElement() {
        if (LiquidBounce.CLIENT.hud.getNotifications().size() > 0) {
            LiquidBounce.CLIENT.hud.getNotifications().get(0).draw(this);
        }
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiHudDesigner) {
            if (!LiquidBounce.CLIENT.hud.getNotifications().contains(this.exampleNotification)) {
                LiquidBounce.CLIENT.hud.addNotification(this.exampleNotification);
            }
            this.exampleNotification.fadeState = FadeState.STAY;
            this.exampleNotification.x = (float)(this.exampleNotification.textLength + 8);
            final int[] location = this.getLocationFromFacing();
            RenderUtils.drawBorderedRect((float)(location[0] - 95), (float)(location[1] - 20), (float)location[0], (float)location[1], 3.0f, Integer.MIN_VALUE, 0);
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
        return mouseX <= location[0] && mouseY <= location[1] && mouseX >= location[0] - 95 && mouseY >= location[1] - 20;
    }
}
