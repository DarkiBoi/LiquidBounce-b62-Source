// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import org.apache.log4j.helpers.LogLog;
import java.util.Iterator;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import org.apache.log4j.spi.LoggingEvent;
import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.log4j.AppenderSkeleton;

public class TelnetAppender extends AppenderSkeleton
{
    private SocketHandler sh;
    private int port;
    
    public TelnetAppender() {
        this.port = 23;
    }
    
    public boolean requiresLayout() {
        return true;
    }
    
    public void activateOptions() {
        try {
            (this.sh = new SocketHandler(this.port)).start();
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
        super.activateOptions();
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public void close() {
        if (this.sh != null) {
            this.sh.close();
            try {
                this.sh.join();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    protected void append(final LoggingEvent event) {
        if (this.sh != null) {
            this.sh.send(this.layout.format(event));
            if (this.layout.ignoresThrowable()) {
                final String[] s = event.getThrowableStrRep();
                if (s != null) {
                    final StringBuffer buf = new StringBuffer();
                    for (int i = 0; i < s.length; ++i) {
                        buf.append(s[i]);
                        buf.append("\r\n");
                    }
                    this.sh.send(buf.toString());
                }
            }
        }
    }
    
    protected class SocketHandler extends Thread
    {
        private Vector writers;
        private Vector connections;
        private ServerSocket serverSocket;
        private int MAX_CONNECTIONS;
        
        public void finalize() {
            this.close();
        }
        
        public void close() {
            synchronized (this) {
                final Enumeration e = this.connections.elements();
                while (e.hasMoreElements()) {
                    try {
                        e.nextElement().close();
                    }
                    catch (InterruptedIOException ex) {
                        Thread.currentThread().interrupt();
                    }
                    catch (IOException ex2) {}
                    catch (RuntimeException ex3) {}
                }
            }
            try {
                this.serverSocket.close();
            }
            catch (InterruptedIOException ex4) {
                Thread.currentThread().interrupt();
            }
            catch (IOException ex5) {}
            catch (RuntimeException ex6) {}
        }
        
        public synchronized void send(final String message) {
            final Iterator ce = this.connections.iterator();
            final Iterator e = this.writers.iterator();
            while (e.hasNext()) {
                ce.next();
                final PrintWriter writer = e.next();
                writer.print(message);
                if (writer.checkError()) {
                    ce.remove();
                    e.remove();
                }
            }
        }
        
        public void run() {
            while (!this.serverSocket.isClosed()) {
                try {
                    final Socket newClient = this.serverSocket.accept();
                    final PrintWriter pw = new PrintWriter(newClient.getOutputStream());
                    if (this.connections.size() < this.MAX_CONNECTIONS) {
                        synchronized (this) {
                            this.connections.addElement(newClient);
                            this.writers.addElement(pw);
                            pw.print("TelnetAppender v1.0 (" + this.connections.size() + " active connections)\r\n\r\n");
                            pw.flush();
                        }
                    }
                    else {
                        pw.print("Too many connections.\r\n");
                        pw.flush();
                        newClient.close();
                    }
                    continue;
                }
                catch (Exception e) {
                    if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    if (!this.serverSocket.isClosed()) {
                        LogLog.error("Encountered error while in SocketHandler loop.", e);
                    }
                }
                break;
            }
            try {
                this.serverSocket.close();
            }
            catch (InterruptedIOException ex) {
                Thread.currentThread().interrupt();
            }
            catch (IOException ex2) {}
        }
        
        public SocketHandler(final int port) throws IOException {
            this.writers = new Vector();
            this.connections = new Vector();
            this.MAX_CONNECTIONS = 20;
            this.serverSocket = new ServerSocket(port);
            this.setName("TelnetAppender-" + this.getName() + "-" + port);
        }
    }
}
