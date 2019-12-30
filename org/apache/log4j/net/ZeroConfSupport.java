// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.apache.log4j.helpers.LogLog;
import java.util.Map;

public class ZeroConfSupport
{
    private static Object jmDNS;
    Object serviceInfo;
    private static Class jmDNSClass;
    private static Class serviceInfoClass;
    
    public ZeroConfSupport(final String zone, final int port, final String name, final Map properties) {
        boolean isVersion3 = false;
        try {
            ZeroConfSupport.jmDNSClass.getMethod("create", (Class[])null);
            isVersion3 = true;
        }
        catch (NoSuchMethodException ex) {}
        if (isVersion3) {
            LogLog.debug("using JmDNS version 3 to construct serviceInfo instance");
            this.serviceInfo = this.buildServiceInfoVersion3(zone, port, name, properties);
        }
        else {
            LogLog.debug("using JmDNS version 1.0 to construct serviceInfo instance");
            this.serviceInfo = this.buildServiceInfoVersion1(zone, port, name, properties);
        }
    }
    
    public ZeroConfSupport(final String zone, final int port, final String name) {
        this(zone, port, name, new HashMap());
    }
    
    private static Object createJmDNSVersion1() {
        try {
            return ZeroConfSupport.jmDNSClass.newInstance();
        }
        catch (InstantiationException e) {
            LogLog.warn("Unable to instantiate JMDNS", e);
        }
        catch (IllegalAccessException e2) {
            LogLog.warn("Unable to instantiate JMDNS", e2);
        }
        return null;
    }
    
    private static Object createJmDNSVersion3() {
        try {
            final Method jmDNSCreateMethod = ZeroConfSupport.jmDNSClass.getMethod("create", (Class[])null);
            return jmDNSCreateMethod.invoke(null, (Object[])null);
        }
        catch (IllegalAccessException e) {
            LogLog.warn("Unable to instantiate jmdns class", e);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to access constructor", e2);
        }
        catch (InvocationTargetException e3) {
            LogLog.warn("Unable to call constructor", e3);
        }
        return null;
    }
    
    private Object buildServiceInfoVersion1(final String zone, final int port, final String name, final Map properties) {
        final Hashtable hashtableProperties = new Hashtable(properties);
        try {
            final Class[] args = { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class };
            final Constructor constructor = ZeroConfSupport.serviceInfoClass.getConstructor((Class[])args);
            final Object[] values = { zone, name, new Integer(port), new Integer(0), new Integer(0), hashtableProperties };
            final Object result = constructor.newInstance(values);
            LogLog.debug("created serviceinfo: " + result);
            return result;
        }
        catch (IllegalAccessException e) {
            LogLog.warn("Unable to construct ServiceInfo instance", e);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to get ServiceInfo constructor", e2);
        }
        catch (InstantiationException e3) {
            LogLog.warn("Unable to construct ServiceInfo instance", e3);
        }
        catch (InvocationTargetException e4) {
            LogLog.warn("Unable to construct ServiceInfo instance", e4);
        }
        return null;
    }
    
    private Object buildServiceInfoVersion3(final String zone, final int port, final String name, final Map properties) {
        try {
            final Class[] args = { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class };
            final Method serviceInfoCreateMethod = ZeroConfSupport.serviceInfoClass.getMethod("create", (Class[])args);
            final Object[] values = { zone, name, new Integer(port), new Integer(0), new Integer(0), properties };
            final Object result = serviceInfoCreateMethod.invoke(null, values);
            LogLog.debug("created serviceinfo: " + result);
            return result;
        }
        catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke create method", e);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to find create method", e2);
        }
        catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke create method", e3);
        }
        return null;
    }
    
    public void advertise() {
        try {
            final Method method = ZeroConfSupport.jmDNSClass.getMethod("registerService", ZeroConfSupport.serviceInfoClass);
            method.invoke(ZeroConfSupport.jmDNS, this.serviceInfo);
            LogLog.debug("registered serviceInfo: " + this.serviceInfo);
        }
        catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke registerService method", e);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("No registerService method", e2);
        }
        catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke registerService method", e3);
        }
    }
    
    public void unadvertise() {
        try {
            final Method method = ZeroConfSupport.jmDNSClass.getMethod("unregisterService", ZeroConfSupport.serviceInfoClass);
            method.invoke(ZeroConfSupport.jmDNS, this.serviceInfo);
            LogLog.debug("unregistered serviceInfo: " + this.serviceInfo);
        }
        catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke unregisterService method", e);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("No unregisterService method", e2);
        }
        catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke unregisterService method", e3);
        }
    }
    
    private static Object initializeJMDNS() {
        try {
            ZeroConfSupport.jmDNSClass = Class.forName("javax.jmdns.JmDNS");
            ZeroConfSupport.serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
        }
        catch (ClassNotFoundException e) {
            LogLog.warn("JmDNS or serviceInfo class not found", e);
        }
        boolean isVersion3 = false;
        try {
            ZeroConfSupport.jmDNSClass.getMethod("create", (Class[])null);
            isVersion3 = true;
        }
        catch (NoSuchMethodException ex) {}
        if (isVersion3) {
            return createJmDNSVersion3();
        }
        return createJmDNSVersion1();
    }
    
    public static Object getJMDNSInstance() {
        return ZeroConfSupport.jmDNS;
    }
    
    static {
        ZeroConfSupport.jmDNS = initializeJMDNS();
    }
}
