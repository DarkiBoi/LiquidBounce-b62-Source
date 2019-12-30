// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import javax.management.MBeanServer;
import javax.management.JMException;
import javax.management.ObjectName;
import javax.management.MBeanServerFactory;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import org.apache.log4j.Logger;

public class Agent
{
    static Logger log;
    
    private static Object createServer() {
        Object newInstance = null;
        try {
            newInstance = Class.forName("com.sun.jdmk.comm.HtmlAdapterServer").newInstance();
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.toString());
        }
        catch (InstantiationException ex2) {
            throw new RuntimeException(ex2.toString());
        }
        catch (IllegalAccessException ex3) {
            throw new RuntimeException(ex3.toString());
        }
        return newInstance;
    }
    
    private static void startServer(final Object server) {
        try {
            server.getClass().getMethod("start", (Class<?>[])new Class[0]).invoke(server, new Object[0]);
        }
        catch (InvocationTargetException ex) {
            final Throwable cause = ex.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            if (cause != null) {
                if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                throw new RuntimeException(cause.toString());
            }
            throw new RuntimeException();
        }
        catch (NoSuchMethodException ex2) {
            throw new RuntimeException(ex2.toString());
        }
        catch (IllegalAccessException ex3) {
            throw new RuntimeException(ex3.toString());
        }
    }
    
    public void start() {
        final MBeanServer server = MBeanServerFactory.createMBeanServer();
        final Object html = createServer();
        try {
            Agent.log.info("Registering HtmlAdaptorServer instance.");
            server.registerMBean(html, new ObjectName("Adaptor:name=html,port=8082"));
            Agent.log.info("Registering HierarchyDynamicMBean instance.");
            final HierarchyDynamicMBean hdm = new HierarchyDynamicMBean();
            server.registerMBean(hdm, new ObjectName("log4j:hiearchy=default"));
        }
        catch (JMException e) {
            Agent.log.error("Problem while registering MBeans instances.", e);
            return;
        }
        catch (RuntimeException e2) {
            Agent.log.error("Problem while registering MBeans instances.", e2);
            return;
        }
        startServer(html);
    }
    
    static {
        Agent.log = Logger.getLogger(Agent.class);
    }
}
