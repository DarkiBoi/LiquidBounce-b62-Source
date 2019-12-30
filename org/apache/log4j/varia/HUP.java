// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.varia;

import java.net.Socket;
import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.log4j.helpers.LogLog;
import java.net.ServerSocket;

class HUP extends Thread
{
    int port;
    ExternallyRolledFileAppender er;
    
    HUP(final ExternallyRolledFileAppender er, final int port) {
        this.er = er;
        this.port = port;
    }
    
    public void run() {
        while (!this.isInterrupted()) {
            try {
                final ServerSocket serverSocket = new ServerSocket(this.port);
                while (true) {
                    final Socket socket = serverSocket.accept();
                    LogLog.debug("Connected to client at " + socket.getInetAddress());
                    new Thread(new HUPNode(socket, this.er), "ExternallyRolledFileAppender-HUP").start();
                }
            }
            catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                continue;
            }
            catch (IOException e2) {
                e2.printStackTrace();
                continue;
            }
            catch (RuntimeException e3) {
                e3.printStackTrace();
                continue;
            }
            break;
        }
    }
}
