// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.Event;

public class TextEvent extends Event
{
    private String text;
    
    public TextEvent(final String text) {
        this.text = text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
}
