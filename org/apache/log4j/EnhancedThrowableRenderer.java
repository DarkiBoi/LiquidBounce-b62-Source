// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.net.URL;
import java.security.CodeSource;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import org.apache.log4j.spi.ThrowableRenderer;

public final class EnhancedThrowableRenderer implements ThrowableRenderer
{
    private Method getStackTraceMethod;
    private Method getClassNameMethod;
    
    public EnhancedThrowableRenderer() {
        try {
            final Class[] noArgs = null;
            this.getStackTraceMethod = Throwable.class.getMethod("getStackTrace", (Class[])noArgs);
            final Class ste = Class.forName("java.lang.StackTraceElement");
            this.getClassNameMethod = ste.getMethod("getClassName", (Class[])noArgs);
        }
        catch (Exception ex) {}
    }
    
    public String[] doRender(final Throwable throwable) {
        if (this.getStackTraceMethod != null) {
            try {
                final Object[] noArgs = null;
                final Object[] elements = (Object[])this.getStackTraceMethod.invoke(throwable, noArgs);
                final String[] lines = new String[elements.length + 1];
                lines[0] = throwable.toString();
                final Map classMap = new HashMap();
                for (int i = 0; i < elements.length; ++i) {
                    lines[i + 1] = this.formatElement(elements[i], classMap);
                }
                return lines;
            }
            catch (Exception ex) {}
        }
        return DefaultThrowableRenderer.render(throwable);
    }
    
    private String formatElement(final Object element, final Map classMap) {
        final StringBuffer buf = new StringBuffer("\tat ");
        buf.append(element);
        try {
            final String className = this.getClassNameMethod.invoke(element, (Object[])null).toString();
            final Object classDetails = classMap.get(className);
            if (classDetails != null) {
                buf.append(classDetails);
            }
            else {
                final Class cls = this.findClass(className);
                final int detailStart = buf.length();
                buf.append('[');
                try {
                    final CodeSource source = cls.getProtectionDomain().getCodeSource();
                    if (source != null) {
                        final URL locationURL = source.getLocation();
                        if (locationURL != null) {
                            if ("file".equals(locationURL.getProtocol())) {
                                final String path = locationURL.getPath();
                                if (path != null) {
                                    int lastSlash = path.lastIndexOf(47);
                                    final int lastBack = path.lastIndexOf(File.separatorChar);
                                    if (lastBack > lastSlash) {
                                        lastSlash = lastBack;
                                    }
                                    if (lastSlash <= 0 || lastSlash == path.length() - 1) {
                                        buf.append(locationURL);
                                    }
                                    else {
                                        buf.append(path.substring(lastSlash + 1));
                                    }
                                }
                            }
                            else {
                                buf.append(locationURL);
                            }
                        }
                    }
                }
                catch (SecurityException ex) {}
                buf.append(':');
                final Package pkg = cls.getPackage();
                if (pkg != null) {
                    final String implVersion = pkg.getImplementationVersion();
                    if (implVersion != null) {
                        buf.append(implVersion);
                    }
                }
                buf.append(']');
                classMap.put(className, buf.substring(detailStart));
            }
        }
        catch (Exception ex2) {}
        return buf.toString();
    }
    
    private Class findClass(final String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            }
            catch (ClassNotFoundException e2) {
                return this.getClass().getClassLoader().loadClass(className);
            }
        }
    }
}
