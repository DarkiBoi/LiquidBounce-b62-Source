// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.spi.LoggingEvent;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import org.apache.log4j.helpers.LogLog;
import java.net.ServerSocket;
import org.apache.log4j.helpers.CyclicBuffer;
import java.util.Vector;
import org.apache.log4j.AppenderSkeleton;

public class SocketHubAppender extends AppenderSkeleton
{
    static final int DEFAULT_PORT = 4560;
    private int port;
    private Vector oosList;
    private ServerMonitor serverMonitor;
    private boolean locationInfo;
    private CyclicBuffer buffer;
    private String application;
    private boolean advertiseViaMulticastDNS;
    private ZeroConfSupport zeroConf;
    public static final String ZONE = "_log4j_obj_tcpaccept_appender.local.";
    private ServerSocket serverSocket;
    
    public SocketHubAppender() {
        this.port = 4560;
        this.oosList = new Vector();
        this.serverMonitor = null;
        this.locationInfo = false;
        this.buffer = null;
    }
    
    public SocketHubAppender(final int _port) {
        this.port = 4560;
        this.oosList = new Vector();
        this.serverMonitor = null;
        this.locationInfo = false;
        this.buffer = null;
        this.port = _port;
        this.startServer();
    }
    
    public void activateOptions() {
        if (this.advertiseViaMulticastDNS) {
            (this.zeroConf = new ZeroConfSupport("_log4j_obj_tcpaccept_appender.local.", this.port, this.getName())).advertise();
        }
        this.startServer();
    }
    
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        LogLog.debug("closing SocketHubAppender " + this.getName());
        this.closed = true;
        if (this.advertiseViaMulticastDNS) {
            this.zeroConf.unadvertise();
        }
        this.cleanUp();
        LogLog.debug("SocketHubAppender " + this.getName() + " closed");
    }
    
    public void cleanUp() {
        LogLog.debug("stopping ServerSocket");
        this.serverMonitor.stopMonitor();
        this.serverMonitor = null;
        LogLog.debug("closing client connections");
        while (this.oosList.size() != 0) {
            final ObjectOutputStream oos = this.oosList.elementAt(0);
            if (oos != null) {
                try {
                    oos.close();
                }
                catch (InterruptedIOException e) {
                    Thread.currentThread().interrupt();
                    LogLog.error("could not close oos.", e);
                }
                catch (IOException e2) {
                    LogLog.error("could not close oos.", e2);
                }
                this.oosList.removeElementAt(0);
            }
        }
    }
    
    public void append(final LoggingEvent event) {
        if (event != null) {
            if (this.locationInfo) {
                event.getLocationInformation();
            }
            if (this.application != null) {
                event.setProperty("application", this.application);
            }
            event.getNDC();
            event.getThreadName();
            event.getMDCCopy();
            event.getRenderedMessage();
            event.getThrowableStrRep();
            if (this.buffer != null) {
                this.buffer.add(event);
            }
        }
        if (event == null || this.oosList.size() == 0) {
            return;
        }
        for (int streamCount = 0; streamCount < this.oosList.size(); ++streamCount) {
            ObjectOutputStream oos = null;
            try {
                oos = this.oosList.elementAt(streamCount);
            }
            catch (ArrayIndexOutOfBoundsException ex) {}
            if (oos == null) {
                break;
            }
            try {
                oos.writeObject(event);
                oos.flush();
                oos.reset();
            }
            catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                this.oosList.removeElementAt(streamCount);
                LogLog.debug("dropped connection");
                --streamCount;
            }
        }
    }
    
    public boolean requiresLayout() {
        return false;
    }
    
    public void setPort(final int _port) {
        this.port = _port;
    }
    
    public void setApplication(final String lapp) {
        this.application = lapp;
    }
    
    public String getApplication() {
        return this.application;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setBufferSize(final int _bufferSize) {
        this.buffer = new CyclicBuffer(_bufferSize);
    }
    
    public int getBufferSize() {
        if (this.buffer == null) {
            return 0;
        }
        return this.buffer.getMaxSize();
    }
    
    public void setLocationInfo(final boolean _locationInfo) {
        this.locationInfo = _locationInfo;
    }
    
    public boolean getLocationInfo() {
        return this.locationInfo;
    }
    
    public void setAdvertiseViaMulticastDNS(final boolean advertiseViaMulticastDNS) {
        this.advertiseViaMulticastDNS = advertiseViaMulticastDNS;
    }
    
    public boolean isAdvertiseViaMulticastDNS() {
        return this.advertiseViaMulticastDNS;
    }
    
    private void startServer() {
        this.serverMonitor = new ServerMonitor(this.port, this.oosList);
    }
    
    protected ServerSocket createServerSocket(final int socketPort) throws IOException {
        return new ServerSocket(socketPort);
    }
    
    private class ServerMonitor implements Runnable
    {
        private int port;
        private Vector oosList;
        private boolean keepRunning;
        private Thread monitorThread;
        
        public ServerMonitor(final int _port, final Vector _oosList) {
            this.port = _port;
            this.oosList = _oosList;
            this.keepRunning = true;
            (this.monitorThread = new Thread(this)).setDaemon(true);
            this.monitorThread.setName("SocketHubAppender-Monitor-" + this.port);
            this.monitorThread.start();
        }
        
        public synchronized void stopMonitor() {
            if (this.keepRunning) {
                LogLog.debug("server monitor thread shutting down");
                this.keepRunning = false;
                try {
                    if (SocketHubAppender.this.serverSocket != null) {
                        SocketHubAppender.this.serverSocket.close();
                        SocketHubAppender.this.serverSocket = null;
                    }
                }
                catch (IOException ex) {}
                try {
                    this.monitorThread.join();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                this.monitorThread = null;
                LogLog.debug("server monitor thread shut down");
            }
        }
        
        private void sendCachedEvents(final ObjectOutputStream stream) throws IOException {
            if (SocketHubAppender.this.buffer != null) {
                for (int i = 0; i < SocketHubAppender.this.buffer.length(); ++i) {
                    stream.writeObject(SocketHubAppender.this.buffer.get(i));
                }
                stream.flush();
                stream.reset();
            }
        }
        
        public void run() {
            SocketHubAppender.this.serverSocket = null;
            try {
                SocketHubAppender.this.serverSocket = SocketHubAppender.this.createServerSocket(this.port);
                SocketHubAppender.this.serverSocket.setSoTimeout(1000);
            }
            catch (Exception e) {
                if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("exception setting timeout, shutting down server socket.", e);
                this.keepRunning = false;
                return;
            }
            try {
                try {
                    SocketHubAppender.this.serverSocket.setSoTimeout(1000);
                }
                catch (SocketException e2) {
                    LogLog.error("exception setting timeout, shutting down server socket.", e2);
                    return;
                }
                while (this.keepRunning) {
                    Socket socket = null;
                    try {
                        socket = SocketHubAppender.this.serverSocket.accept();
                    }
                    catch (InterruptedIOException e5) {}
                    catch (SocketException e3) {
                        LogLog.error("exception accepting socket, shutting down server socket.", e3);
                        this.keepRunning = false;
                    }
                    catch (IOException e4) {
                        LogLog.error("exception accepting socket.", e4);
                    }
                    if (socket != null) {
                        try {
                            final InetAddress remoteAddress = socket.getInetAddress();
                            LogLog.debug("accepting connection from " + remoteAddress.getHostName() + " (" + remoteAddress.getHostAddress() + ")");
                            final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            if (SocketHubAppender.this.buffer != null && SocketHubAppender.this.buffer.length() > 0) {
                                this.sendCachedEvents(oos);
                            }
                            this.oosList.addElement(oos);
                        }
                        catch (IOException e4) {
                            if (e4 instanceof InterruptedIOException) {
                                Thread.currentThread().interrupt();
                            }
                            LogLog.error("exception creating output stream on socket.", e4);
                        }
                    }
                }
            }
            finally {
                try {
                    SocketHubAppender.this.serverSocket.close();
                }
                catch (InterruptedIOException e6) {
                    Thread.currentThread().interrupt();
                }
                catch (IOException ex) {}
            }
        }
    }
}
