// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.client.multiplayer.WorldClient;
import net.ccbluex.liquidbounce.event.Event;

public class WorldEvent extends Event
{
    private final WorldClient worldClient;
    
    public WorldEvent(final WorldClient worldClient) {
        this.worldClient = worldClient;
    }
    
    public WorldClient getWorldClient() {
        return this.worldClient;
    }
}
