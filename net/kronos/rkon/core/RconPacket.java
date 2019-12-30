// 
// Decompiled by Procyon v0.5.36
// 

package net.kronos.rkon.core;

import java.io.EOFException;
import java.nio.BufferUnderflowException;
import java.io.DataInputStream;
import net.kronos.rkon.core.exceptions.MalformedPacketException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.io.IOException;
import java.net.SocketException;

public class RconPacket
{
    static final int SERVERDATA_EXECCOMMAND = 2;
    static final int SERVERDATA_AUTH = 3;
    private final int requestId;
    private final int type;
    private final byte[] payload;
    
    private RconPacket(final int requestId, final int type, final byte[] payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }
    
    public int getRequestId() {
        return this.requestId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public byte[] getPayload() {
        return this.payload;
    }
    
    public static RconPacket send(final Rcon rcon, final int type, final byte[] payload) throws IOException {
        try {
            write(rcon.getSocket().getOutputStream(), rcon.getRequestId(), type, payload);
        }
        catch (SocketException se) {
            rcon.getSocket().close();
            throw se;
        }
        return read(rcon.getSocket().getInputStream());
    }
    
    private static void write(final OutputStream out, final int requestId, final int type, final byte[] payload) throws IOException {
        final int bodyLength = getBodyLength(payload.length);
        final int packetLength = getPacketLength(bodyLength);
        final ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(bodyLength);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload);
        buffer.put((byte)0);
        buffer.put((byte)0);
        out.write(buffer.array());
        out.flush();
    }
    
    private static RconPacket read(final InputStream in) throws IOException {
        final byte[] header = new byte[12];
        in.read(header);
        try {
            final ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            final int length = buffer.getInt();
            final int requestId = buffer.getInt();
            final int type = buffer.getInt();
            if (length - 4 - 4 - 2 < 0) {
                throw new MalformedPacketException("Cannot read the whole packet");
            }
            final byte[] payload = new byte[length - 4 - 4 - 2];
            final DataInputStream dis = new DataInputStream(in);
            dis.readFully(payload);
            dis.read(new byte[2]);
            return new RconPacket(requestId, type, payload);
        }
        catch (BufferUnderflowException | EOFException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new MalformedPacketException("Cannot read the whole packet");
        }
    }
    
    private static int getPacketLength(final int bodyLength) {
        return 4 + bodyLength;
    }
    
    private static int getBodyLength(final int payloadLength) {
        return 8 + payloadLength + 2;
    }
}
