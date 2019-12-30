// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.packet.packets;

import net.ccbluex.liquidbounce.chat.io.NetOutput;
import java.io.IOException;
import net.ccbluex.liquidbounce.chat.io.NetInput;
import net.ccbluex.liquidbounce.chat.packet.Packet;

public class PrivateMessagePacket implements Packet
{
    private String target;
    private String message;
    
    public PrivateMessagePacket() {
    }
    
    public PrivateMessagePacket(final String target, final String message) {
        this.target = target;
        this.message = message;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.target = in.readString();
        this.message = in.readString();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.target);
        out.writeString(this.message);
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public String getMessage() {
        return this.message;
    }
}
