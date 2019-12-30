// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Objects;
import java.security.CodeSource;

final class ScriptLoader extends NashornLoader
{
    private static final String NASHORN_PKG_PREFIX = "jdk.nashorn.internal.";
    private final Context context;
    
    Context getContext() {
        return this.context;
    }
    
    ScriptLoader(final Context context) {
        super(context.getStructLoader());
        this.context = context;
    }
    
    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        NashornLoader.checkPackageAccess(name);
        return super.loadClass(name, resolve);
    }
    
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final ClassLoader appLoader = this.context.getAppLoader();
        if (appLoader == null || name.startsWith("jdk.nashorn.internal.")) {
            throw new ClassNotFoundException(name);
        }
        return appLoader.loadClass(name);
    }
    
    synchronized Class<?> installClass(final String name, final byte[] data, final CodeSource cs) {
        return this.defineClass(name, data, 0, data.length, Objects.requireNonNull(cs));
    }
}
