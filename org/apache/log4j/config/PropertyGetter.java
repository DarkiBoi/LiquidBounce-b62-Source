// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.config;

import org.apache.log4j.Priority;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import org.apache.log4j.helpers.LogLog;
import java.beans.IntrospectionException;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class PropertyGetter
{
    protected static final Object[] NULL_ARG;
    protected Object obj;
    protected PropertyDescriptor[] props;
    
    public PropertyGetter(final Object obj) throws IntrospectionException {
        final BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
        this.props = bi.getPropertyDescriptors();
        this.obj = obj;
    }
    
    public static void getProperties(final Object obj, final PropertyCallback callback, final String prefix) {
        try {
            new PropertyGetter(obj).getProperties(callback, prefix);
        }
        catch (IntrospectionException ex) {
            LogLog.error("Failed to introspect object " + obj, ex);
        }
    }
    
    public void getProperties(final PropertyCallback callback, final String prefix) {
        for (int i = 0; i < this.props.length; ++i) {
            final Method getter = this.props[i].getReadMethod();
            if (getter != null) {
                if (this.isHandledType(getter.getReturnType())) {
                    final String name = this.props[i].getName();
                    try {
                        final Object result = getter.invoke(this.obj, PropertyGetter.NULL_ARG);
                        if (result != null) {
                            callback.foundProperty(this.obj, prefix, name, result);
                        }
                    }
                    catch (IllegalAccessException ex2) {
                        LogLog.warn("Failed to get value of property " + name);
                    }
                    catch (InvocationTargetException ex) {
                        if (ex.getTargetException() instanceof InterruptedException || ex.getTargetException() instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        LogLog.warn("Failed to get value of property " + name);
                    }
                    catch (RuntimeException ex3) {
                        LogLog.warn("Failed to get value of property " + name);
                    }
                }
            }
        }
    }
    
    protected boolean isHandledType(final Class type) {
        return String.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type) || Boolean.TYPE.isAssignableFrom(type) || Priority.class.isAssignableFrom(type);
    }
    
    static {
        NULL_ARG = new Object[0];
    }
    
    public interface PropertyCallback
    {
        void foundProperty(final Object p0, final String p1, final String p2, final Object p3);
    }
}
