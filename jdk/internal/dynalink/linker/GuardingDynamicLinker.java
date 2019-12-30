// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

public interface GuardingDynamicLinker
{
    GuardedInvocation getGuardedInvocation(final LinkRequest p0, final LinkerServices p1) throws Exception;
}
