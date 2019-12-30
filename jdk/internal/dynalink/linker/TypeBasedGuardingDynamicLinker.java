// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

public interface TypeBasedGuardingDynamicLinker extends GuardingDynamicLinker
{
    boolean canLinkType(final Class<?> p0);
}
