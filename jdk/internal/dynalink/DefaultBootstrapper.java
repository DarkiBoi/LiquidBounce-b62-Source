// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;

public class DefaultBootstrapper
{
    private static final DynamicLinker dynamicLinker;
    
    private DefaultBootstrapper() {
    }
    
    public static CallSite bootstrap(final MethodHandles.Lookup caller, final String name, final MethodType type) {
        return bootstrapInternal(caller, name, type);
    }
    
    public static CallSite publicBootstrap(final MethodHandles.Lookup caller, final String name, final MethodType type) {
        return bootstrapInternal(MethodHandles.publicLookup(), name, type);
    }
    
    private static CallSite bootstrapInternal(final MethodHandles.Lookup caller, final String name, final MethodType type) {
        return DefaultBootstrapper.dynamicLinker.link(new MonomorphicCallSite(CallSiteDescriptorFactory.create(caller, name, type)));
    }
    
    static {
        dynamicLinker = new DynamicLinkerFactory().createLinker();
    }
}
