// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.helpers;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import java.net.URL;

public class Loader
{
    static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    private static boolean java1;
    private static boolean ignoreTCL;
    
    public static URL getResource(final String resource, final Class clazz) {
        return getResource(resource);
    }
    
    public static URL getResource(final String resource) {
        ClassLoader classLoader = null;
        URL url = null;
        try {
            if (!Loader.java1 && !Loader.ignoreTCL) {
                classLoader = getTCL();
                if (classLoader != null) {
                    LogLog.debug("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
                    url = classLoader.getResource(resource);
                    if (url != null) {
                        return url;
                    }
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                LogLog.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (IllegalAccessException t) {
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        catch (InvocationTargetException t2) {
            if (t2.getTargetException() instanceof InterruptedException || t2.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t2);
        }
        catch (Throwable t3) {
            LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t3);
        }
        LogLog.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }
    
    public static boolean isJava1() {
        return Loader.java1;
    }
    
    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        return (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
    }
    
    public static Class loadClass(final String clazz) throws ClassNotFoundException {
        if (Loader.java1 || Loader.ignoreTCL) {
            return Class.forName(clazz);
        }
        try {
            return getTCL().loadClass(clazz);
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
        }
        catch (Throwable t) {}
        return Class.forName(clazz);
    }
    
    static {
        Loader.java1 = true;
        Loader.ignoreTCL = false;
        final String prop = OptionConverter.getSystemProperty("java.version", null);
        if (prop != null) {
            final int i = prop.indexOf(46);
            if (i != -1 && prop.charAt(i + 1) != '1') {
                Loader.java1 = false;
            }
        }
        final String ignoreTCLProp = OptionConverter.getSystemProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            Loader.ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }
}
