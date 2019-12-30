// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.packet.packets;

import net.ccbluex.liquidbounce.chat.io.NetOutput;
import java.io.IOException;
import net.ccbluex.liquidbounce.chat.io.NetInput;
import net.ccbluex.liquidbounce.chat.packet.Packet;

public class MessagePacket implements Packet
{
    private String message;
    
    public MessagePacket() {
    }
    
    public MessagePacket(final String message) {
        this.message = message;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.message = in.readString();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.message);
    }
    
    public String getMessage() {
        return this.message;
    }
}
