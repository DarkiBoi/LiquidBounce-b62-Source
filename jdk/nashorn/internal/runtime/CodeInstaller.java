// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Map;
import java.util.Collection;

public interface CodeInstaller
{
    Context getContext();
    
    Class<?> install(final String p0, final byte[] p1);
    
    void initialize(final Collection<Class<?>> p0, final Source p1, final Object[] p2);
    
    void verify(final byte[] p0);
    
    long getUniqueScriptId();
    
    void storeScript(final String p0, final Source p1, final String p2, final Map<String, byte[]> p3, final Map<Integer, FunctionInitializer> p4, final Object[] p5, final int p6);
    
    StoredScript loadScript(final Source p0, final String p1);
    
    CodeInstaller withNewLoader();
    
    boolean isCompatibleWith(final CodeInstaller p0);
}
