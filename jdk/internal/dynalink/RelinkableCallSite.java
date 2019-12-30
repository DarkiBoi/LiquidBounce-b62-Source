// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.MethodHandle;

public interface RelinkableCallSite
{
    void initialize(final MethodHandle p0);
    
    CallSiteDescriptor getDescriptor();
    
    void relink(final GuardedInvocation p0, final MethodHandle p1);
    
    void resetAndRelink(final GuardedInvocation p0, final MethodHandle p1);
}
