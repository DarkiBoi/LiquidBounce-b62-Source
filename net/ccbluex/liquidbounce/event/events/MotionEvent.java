// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.Event;

public class MotionEvent extends Event
{
    private final EventState eventState;
    
    public MotionEvent(final EventState eventState) {
        this.eventState = eventState;
    }
    
    public EventState getEventState() {
        return this.eventState;
    }
}
