// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.varia;

import org.apache.log4j.helpers.LogLog;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

class HUPNode implements Runnable
{
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    ExternallyRolledFileAppender er;
    
    public HUPNode(final Socket socket, final ExternallyRolledFileAppender er) {
        this.socket = socket;
        this.er = er;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        }
        catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (RuntimeException e3) {
            e3.printStackTrace();
        }
    }
    
    public void run() {
        try {
            final String line = this.dis.readUTF();
            LogLog.debug("Got external roll over signal.");
            if ("RollOver".equals(line)) {
                synchronized (this.er) {
                    this.er.rollOver();
                }
                this.dos.writeUTF("OK");
            }
            else {
                this.dos.writeUTF("Expecting [RollOver] string.");
            }
            this.dos.close();
        }
        catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            LogLog.error("Unexpected exception. Exiting HUPNode.", e);
        }
        catch (IOException e2) {
            LogLog.error("Unexpected exception. Exiting HUPNode.", e2);
        }
        catch (RuntimeException e3) {
            LogLog.error("Unexpected exception. Exiting HUPNode.", e3);
        }
    }
}
