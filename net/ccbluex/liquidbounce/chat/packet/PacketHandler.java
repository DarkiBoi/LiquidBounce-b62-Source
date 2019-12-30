// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.packet;

import net.ccbluex.liquidbounce.chat.io.NetOutput;
import net.ccbluex.liquidbounce.chat.io.NetInput;
import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.chat.packet.packets.PrivateMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.MessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.AuthenticatePacket;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler
{
    private final Map<Integer, Class> incomingPackets;
    private final Map<Class, Integer> outgoingPackets;
    
    public PacketHandler() {
        this.incomingPackets = new HashMap<Integer, Class>();
        this.outgoingPackets = new HashMap<Class, Integer>();
    }
    
    public void registerPackets() {
        this.registerPacket(AuthenticatePacket.class, 0);
        this.registerPacket(MessagePacket.class, 1);
        this.registerPacket(PrivateMessagePacket.class, 2);
    }
    
    public void registerPacket(final Class<?> packetClass, final int packetID) {
        this.incomingPackets.put(packetID, packetClass);
        this.outgoingPackets.put(packetClass, packetID);
    }
    
    public Packet read(final byte[] bytes) {
        try {
            final NetInput in = new NetInput(ByteBuffer.wrap(bytes));
            final int packetID = in.readInt();
            final Packet packet = this.incomingPackets.get(packetID).newInstance();
            packet.read(in);
            return packet;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] write(final Packet packet) throws Exception {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        final NetOutput out = new NetOutput(byteBuffer);
        out.writeInt(this.outgoingPackets.get(packet.getClass()));
        packet.write(out);
        return byteBuffer.array();
    }
}
