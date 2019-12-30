// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import java.net.SocketException;
import java.io.EOFException;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import org.apache.log4j.Logger;
import java.io.ObjectInputStream;
import org.apache.log4j.spi.LoggerRepository;
import java.net.Socket;

public class SocketNode implements Runnable
{
    Socket socket;
    LoggerRepository hierarchy;
    ObjectInputStream ois;
    static Logger logger;
    
    public SocketNode(final Socket socket, final LoggerRepository hierarchy) {
        this.socket = socket;
        this.hierarchy = hierarchy;
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        }
        catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            SocketNode.logger.error("Could not open ObjectInputStream to " + socket, e);
        }
        catch (IOException e2) {
            SocketNode.logger.error("Could not open ObjectInputStream to " + socket, e2);
        }
        catch (RuntimeException e3) {
            SocketNode.logger.error("Could not open ObjectInputStream to " + socket, e3);
        }
    }
    
    public void run() {
        try {
            if (this.ois != null) {
                while (true) {
                    final LoggingEvent event = (LoggingEvent)this.ois.readObject();
                    final Logger remoteLogger = this.hierarchy.getLogger(event.getLoggerName());
                    if (event.getLevel().isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
                        remoteLogger.callAppenders(event);
                    }
                }
            }
        }
        catch (EOFException e5) {
            SocketNode.logger.info("Caught java.io.EOFException closing conneciton.");
        }
        catch (SocketException e6) {
            SocketNode.logger.info("Caught java.net.SocketException closing conneciton.");
        }
        catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            SocketNode.logger.info("Caught java.io.InterruptedIOException: " + e);
            SocketNode.logger.info("Closing connection.");
        }
        catch (IOException e2) {
            SocketNode.logger.info("Caught java.io.IOException: " + e2);
            SocketNode.logger.info("Closing connection.");
        }
        catch (Exception e3) {
            SocketNode.logger.error("Unexpected exception. Closing conneciton.", e3);
        }
        finally {
            if (this.ois != null) {
                try {
                    this.ois.close();
                }
                catch (Exception e4) {
                    SocketNode.logger.info("Could not close connection.", e4);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                }
                catch (InterruptedIOException e7) {
                    Thread.currentThread().interrupt();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    static {
        SocketNode.logger = Logger.getLogger(SocketNode.class);
    }
}
