// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import org.apache.log4j.helpers.ThreadLocalMap;
import org.apache.log4j.helpers.Loader;
import java.lang.reflect.Method;

public class MDC
{
    static final MDC mdc;
    static final int HT_SIZE = 7;
    boolean java1;
    Object tlm;
    private Method removeMethod;
    
    private MDC() {
        if (!(this.java1 = Loader.isJava1())) {
            this.tlm = new ThreadLocalMap();
        }
        try {
            this.removeMethod = ThreadLocal.class.getMethod("remove", (Class[])null);
        }
        catch (NoSuchMethodException ex) {}
    }
    
    public static void put(final String key, final Object o) {
        if (MDC.mdc != null) {
            MDC.mdc.put0(key, o);
        }
    }
    
    public static Object get(final String key) {
        if (MDC.mdc != null) {
            return MDC.mdc.get0(key);
        }
        return null;
    }
    
    public static void remove(final String key) {
        if (MDC.mdc != null) {
            MDC.mdc.remove0(key);
        }
    }
    
    public static Hashtable getContext() {
        if (MDC.mdc != null) {
            return MDC.mdc.getContext0();
        }
        return null;
    }
    
    public static void clear() {
        if (MDC.mdc != null) {
            MDC.mdc.clear0();
        }
    }
    
    private void put0(final String key, final Object o) {
        if (this.java1 || this.tlm == null) {
            return;
        }
        Hashtable ht = ((ThreadLocalMap)this.tlm).get();
        if (ht == null) {
            ht = new Hashtable(7);
            ((ThreadLocalMap)this.tlm).set(ht);
        }
        ht.put(key, o);
    }
    
    private Object get0(final String key) {
        if (this.java1 || this.tlm == null) {
            return null;
        }
        final Hashtable ht = ((ThreadLocalMap)this.tlm).get();
        if (ht != null && key != null) {
            return ht.get(key);
        }
        return null;
    }
    
    private void remove0(final String key) {
        if (!this.java1 && this.tlm != null) {
            final Hashtable ht = ((ThreadLocalMap)this.tlm).get();
            if (ht != null) {
                ht.remove(key);
                if (ht.isEmpty()) {
                    this.clear0();
                }
            }
        }
    }
    
    private Hashtable getContext0() {
        if (this.java1 || this.tlm == null) {
            return null;
        }
        return ((ThreadLocalMap)this.tlm).get();
    }
    
    private void clear0() {
        if (!this.java1 && this.tlm != null) {
            final Hashtable ht = ((ThreadLocalMap)this.tlm).get();
            if (ht != null) {
                ht.clear();
            }
            if (this.removeMethod != null) {
                try {
                    this.removeMethod.invoke(this.tlm, (Object[])null);
                }
                catch (IllegalAccessException e) {}
                catch (InvocationTargetException ex) {}
            }
        }
    }
    
    static {
        mdc = new MDC();
    }
}
