// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.client.gui.GuiScreen;
import net.ccbluex.liquidbounce.event.Event;

public class ScreenEvent extends Event
{
    private final GuiScreen guiScreen;
    
    public ScreenEvent(final GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }
    
    public GuiScreen getGuiScreen() {
        return this.guiScreen;
    }
}
