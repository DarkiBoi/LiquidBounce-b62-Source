// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.chat.io;

import java.util.UUID;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NetInput
{
    private final ByteBuffer buffer;
    
    public NetInput(final ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }
    
    public boolean readBoolean() {
        return this.buffer.get() == 1;
    }
    
    public byte readByte() {
        return this.buffer.get();
    }
    
    public int readUnsignedByte() {
        return this.buffer.get() & 0xFF;
    }
    
    public short readShort() {
        return this.buffer.getShort();
    }
    
    public int readUnsignedShort() {
        return this.buffer.getShort() & 0xFFFF;
    }
    
    public char readChar() {
        return this.buffer.getChar();
    }
    
    public int readInt() {
        return this.buffer.getInt();
    }
    
    public int readVarInt() throws IOException {
        int value = 0;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << size++ * 7;
            if (size > 5) {
                throw new IOException("VarInt too long (length must be <= 5)");
            }
        }
        return value | (b & 0x7F) << size * 7;
    }
    
    public long readLong() {
        return this.buffer.getLong();
    }
    
    public long readVarLong() throws IOException {
        long value = 0L;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (long)(b & 0x7F) << size++ * 7;
            if (size > 10) {
                throw new IOException("VarLong too long (length must be <= 10)");
            }
        }
        return value | (long)(b & 0x7F) << size * 7;
    }
    
    public float readFloat() {
        return this.buffer.getFloat();
    }
    
    public double readDouble() {
        return this.buffer.getDouble();
    }
    
    public byte[] readBytes(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final byte[] b = new byte[length];
        this.buffer.get(b);
        return b;
    }
    
    public int readBytes(final byte[] b) {
        return this.readBytes(b, 0, b.length);
    }
    
    public int readBytes(final byte[] b, final int offset, int length) {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length) {
            length = readable;
        }
        this.buffer.get(b, offset, length);
        return length;
    }
    
    public short[] readShorts(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final short[] s = new short[length];
        for (int index = 0; index < length; ++index) {
            s[index] = this.readShort();
        }
        return s;
    }
    
    public int readShorts(final short[] s) throws IOException {
        return this.readShorts(s, 0, s.length);
    }
    
    public int readShorts(final short[] s, final int offset, int length) {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 2) {
            length = readable / 2;
        }
        for (int index = offset; index < offset + length; ++index) {
            s[index] = this.readShort();
        }
        return length;
    }
    
    public int[] readInts(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final int[] i = new int[length];
        for (int index = 0; index < length; ++index) {
            i[index] = this.readInt();
        }
        return i;
    }
    
    public int readInts(final int[] i) throws IOException {
        return this.readInts(i, 0, i.length);
    }
    
    public int readInts(final int[] i, final int offset, int length) {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 4) {
            length = readable / 4;
        }
        for (int index = offset; index < offset + length; ++index) {
            i[index] = this.readInt();
        }
        return length;
    }
    
    public long[] readLongs(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final long[] l = new long[length];
        for (int index = 0; index < length; ++index) {
            l[index] = this.readLong();
        }
        return l;
    }
    
    public int readLongs(final long[] l) throws IOException {
        return this.readLongs(l, 0, l.length);
    }
    
    public int readLongs(final long[] l, final int offset, int length) {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 2) {
            length = readable / 2;
        }
        for (int index = offset; index < offset + length; ++index) {
            l[index] = this.readLong();
        }
        return length;
    }
    
    public String readString() throws IOException {
        final int length = this.readVarInt();
        final byte[] bytes = this.readBytes(length);
        return new String(bytes, "UTF-8");
    }
    
    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }
    
    public int available() {
        return this.buffer.remaining();
    }
}
