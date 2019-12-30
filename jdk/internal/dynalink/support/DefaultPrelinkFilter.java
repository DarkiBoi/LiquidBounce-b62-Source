// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.GuardedInvocationFilter;

public class DefaultPrelinkFilter implements GuardedInvocationFilter
{
    @Override
    public GuardedInvocation filter(final GuardedInvocation inv, final LinkRequest request, final LinkerServices linkerServices) {
        return inv.asType(linkerServices, request.getCallSiteDescriptor().getMethodType());
    }
}
