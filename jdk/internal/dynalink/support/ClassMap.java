// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.Reference;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public abstract class ClassMap<T>
{
    private final ConcurrentMap<Class<?>, T> map;
    private final Map<Class<?>, Reference<T>> weakMap;
    private final ClassLoader classLoader;
    
    protected ClassMap(final ClassLoader classLoader) {
        this.map = new ConcurrentHashMap<Class<?>, T>();
        this.weakMap = new WeakHashMap<Class<?>, Reference<T>>();
        this.classLoader = classLoader;
    }
    
    protected abstract T computeValue(final Class<?> p0);
    
    public T get(final Class<?> clazz) {
        final T v = this.map.get(clazz);
        if (v != null) {
            return v;
        }
        Reference<T> ref;
        synchronized (this.weakMap) {
            ref = this.weakMap.get(clazz);
        }
        if (ref != null) {
            final T refv = ref.get();
            if (refv != null) {
                return refv;
            }
        }
        final T newV = this.computeValue(clazz);
        assert newV != null;
        final ClassLoader clazzLoader = AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
        if (Guards.canReferenceDirectly(this.classLoader, clazzLoader)) {
            final T oldV = this.map.putIfAbsent(clazz, newV);
            return (oldV != null) ? oldV : newV;
        }
        synchronized (this.weakMap) {
            ref = this.weakMap.get(clazz);
            if (ref != null) {
                final T oldV2 = ref.get();
                if (oldV2 != null) {
                    return oldV2;
                }
            }
            this.weakMap.put(clazz, new SoftReference<T>(newV));
            return newV;
        }
    }
}
