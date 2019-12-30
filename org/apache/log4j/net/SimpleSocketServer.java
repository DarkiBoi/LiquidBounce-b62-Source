// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import java.net.Socket;
import org.apache.log4j.LogManager;
import java.net.ServerSocket;
import org.apache.log4j.Logger;

public class SimpleSocketServer
{
    static Logger cat;
    static int port;
    
    public static void main(final String[] argv) {
        if (argv.length == 2) {
            init(argv[0], argv[1]);
        }
        else {
            usage("Wrong number of arguments.");
        }
        try {
            SimpleSocketServer.cat.info("Listening on port " + SimpleSocketServer.port);
            final ServerSocket serverSocket = new ServerSocket(SimpleSocketServer.port);
            while (true) {
                SimpleSocketServer.cat.info("Waiting to accept a new client.");
                final Socket socket = serverSocket.accept();
                SimpleSocketServer.cat.info("Connected to client at " + socket.getInetAddress());
                SimpleSocketServer.cat.info("Starting new socket node.");
                new Thread(new SocketNode(socket, LogManager.getLoggerRepository()), "SimpleSocketServer-" + SimpleSocketServer.port).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static void usage(final String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + SimpleSocketServer.class.getName() + " port configFile");
        System.exit(1);
    }
    
    static void init(final String portStr, final String configFile) {
        try {
            SimpleSocketServer.port = Integer.parseInt(portStr);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            usage("Could not interpret port number [" + portStr + "].");
        }
        if (configFile.endsWith(".xml")) {
            DOMConfigurator.configure(configFile);
        }
        else {
            PropertyConfigurator.configure(configFile);
        }
    }
    
    static {
        SimpleSocketServer.cat = Logger.getLogger(SimpleSocketServer.class);
    }
}
