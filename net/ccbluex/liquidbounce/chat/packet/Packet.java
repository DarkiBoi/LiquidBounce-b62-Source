// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.packet;

import net.ccbluex.liquidbounce.chat.io.NetOutput;
import java.io.IOException;
import net.ccbluex.liquidbounce.chat.io.NetInput;

public interface Packet
{
    void read(final NetInput p0) throws IOException;
    
    void write(final NetOutput p0) throws IOException;
}
