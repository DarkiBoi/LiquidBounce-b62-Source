// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.io;

import java.util.UUID;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NetOutput
{
    private final ByteBuffer buffer;
    
    public NetOutput(final ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }
    
    public void writeBoolean(final boolean b) {
        this.buffer.put((byte)(b ? 1 : 0));
    }
    
    public void writeByte(final int b) {
        this.buffer.put((byte)b);
    }
    
    public void writeShort(final int s) {
        this.buffer.putShort((short)s);
    }
    
    public void writeChar(final int c) {
        this.buffer.putChar((char)c);
    }
    
    public void writeInt(final int i) {
        this.buffer.putInt(i);
    }
    
    public void writeVarInt(int i) {
        while ((i & 0xFFFFFF80) != 0x0) {
            this.writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }
        this.writeByte(i);
    }
    
    public void writeLong(final long l) {
        this.buffer.putLong(l);
    }
    
    public void writeVarLong(long l) {
        while ((l & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.writeByte((int)(l & 0x7FL) | 0x80);
            l >>>= 7;
        }
        this.writeByte((int)l);
    }
    
    public void writeFloat(final float f) {
        this.buffer.putFloat(f);
    }
    
    public void writeDouble(final double d) {
        this.buffer.putDouble(d);
    }
    
    public void writeBytes(final byte[] b) {
        this.buffer.put(b);
    }
    
    public void writeBytes(final byte[] b, final int length) {
        this.buffer.put(b, 0, length);
    }
    
    public void writeShorts(final short[] s) throws IOException {
        this.writeShorts(s, s.length);
    }
    
    public void writeShorts(final short[] s, final int length) {
        for (int index = 0; index < length; ++index) {
            this.writeShort(s[index]);
        }
    }
    
    public void writeInts(final int[] i) throws IOException {
        this.writeInts(i, i.length);
    }
    
    public void writeInts(final int[] i, final int length) {
        for (int index = 0; index < length; ++index) {
            this.writeInt(i[index]);
        }
    }
    
    public void writeLongs(final long[] l) throws IOException {
        this.writeLongs(l, l.length);
    }
    
    public void writeLongs(final long[] l, final int length) {
        for (int index = 0; index < length; ++index) {
            this.writeLong(l[index]);
        }
    }
    
    public void writeString(final String s) throws IOException {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null!");
        }
        final byte[] bytes = s.getBytes("UTF-8");
        if (bytes.length > 32767) {
            throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
        }
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
    }
    
    public void writeUUID(final UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
}
