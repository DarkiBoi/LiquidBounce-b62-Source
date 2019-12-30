// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

public class CompositeGuardingDynamicLinker implements GuardingDynamicLinker, Serializable
{
    private static final long serialVersionUID = 1L;
    private final GuardingDynamicLinker[] linkers;
    
    public CompositeGuardingDynamicLinker(final Iterable<? extends GuardingDynamicLinker> linkers) {
        final List<GuardingDynamicLinker> l = new LinkedList<GuardingDynamicLinker>();
        for (final GuardingDynamicLinker linker : linkers) {
            l.add(linker);
        }
        this.linkers = l.toArray(new GuardingDynamicLinker[l.size()]);
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        for (final GuardingDynamicLinker linker : this.linkers) {
            final GuardedInvocation invocation = linker.getGuardedInvocation(linkRequest, linkerServices);
            if (invocation != null) {
                return invocation;
            }
        }
        return null;
    }
}
