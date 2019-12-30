// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.packet.packets;

import net.ccbluex.liquidbounce.chat.io.NetOutput;
import java.io.IOException;
import net.ccbluex.liquidbounce.chat.io.NetInput;
import java.util.UUID;
import net.ccbluex.liquidbounce.chat.packet.Packet;

public class AuthenticatePacket implements Packet
{
    private String username;
    private UUID uuid;
    private String identificationID;
    private boolean cracked;
    
    public AuthenticatePacket() {
    }
    
    public AuthenticatePacket(final String username, final UUID uuid, final String identificationID, final boolean cracked) {
        this.username = username;
        this.uuid = uuid;
        this.identificationID = identificationID;
        this.cracked = cracked;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.username = in.readString();
        this.uuid = in.readUUID();
        this.identificationID = in.readString();
        this.cracked = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.username);
        out.writeUUID(this.uuid);
        out.writeString(this.identificationID);
        out.writeBoolean(this.cracked);
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public String getIdentificationID() {
        return this.identificationID;
    }
    
    public boolean isCracked() {
        return this.cracked;
    }
}
