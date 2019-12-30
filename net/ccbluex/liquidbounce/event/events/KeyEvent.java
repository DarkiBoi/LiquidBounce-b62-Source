// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.Event;

public class KeyEvent extends Event
{
    private final int key;
    
    public KeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
