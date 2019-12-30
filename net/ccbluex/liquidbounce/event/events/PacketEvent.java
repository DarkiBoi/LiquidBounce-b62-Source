// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event.events;

import net.minecraft.network.Packet;
import net.ccbluex.liquidbounce.event.CancellableEvent;

public class PacketEvent extends CancellableEvent
{
    private final Packet packet;
    
    public PacketEvent(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
}
