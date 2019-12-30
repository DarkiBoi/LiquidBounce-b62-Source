// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.GuardedInvocation;

public interface GuardedInvocationFilter
{
    GuardedInvocation filter(final GuardedInvocation p0, final LinkRequest p1, final LinkerServices p2);
}
