// 
// Decompiled by Procyon v0.5.36
// 

package net.kronos.rkon.core;

import net.kronos.rkon.core.exceptions.AuthenticationException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.Socket;
import java.util.Random;

public class Rcon
{
    private final Object sync;
    private final Random rand;
    private int requestId;
    private Socket socket;
    private Charset charset;
    
    public Rcon(final String host, final int port, final byte[] password) throws IOException, AuthenticationException {
        this.sync = new Object();
        this.rand = new Random();
        this.charset = Charset.forName("UTF-8");
        this.connect(host, port, password);
    }
    
    private void connect(final String host, final int port, final byte[] password) throws IOException, AuthenticationException {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Host can't be null or empty");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port is out of range");
        }
        synchronized (this.sync) {
            this.requestId = this.rand.nextInt();
            this.socket = new Socket(host, port);
        }
        final RconPacket res = this.send(3, password);
        if (res.getRequestId() == -1) {
            this.disconnect();
            throw new AuthenticationException("Password rejected by server");
        }
    }
    
    public void disconnect() throws IOException {
        synchronized (this.sync) {
            this.socket.close();
        }
    }
    
    public String command(final String payload) throws IOException {
        if (payload == null || payload.trim().isEmpty()) {
            throw new IllegalArgumentException("Payload can't be null or empty");
        }
        final RconPacket response = this.send(2, payload.getBytes());
        return new String(response.getPayload(), this.getCharset());
    }
    
    private RconPacket send(final int type, final byte[] payload) throws IOException {
        synchronized (this.sync) {
            return RconPacket.send(this, type, payload);
        }
    }
    
    public int getRequestId() {
        return this.requestId;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    private Charset getCharset() {
        return this.charset;
    }
    
    public void setCharset(final Charset charset) {
        this.charset = charset;
    }
}
