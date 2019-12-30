// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.support.AbstractRelinkableCallSite;

public class MonomorphicCallSite extends AbstractRelinkableCallSite
{
    public MonomorphicCallSite(final CallSiteDescriptor descriptor) {
        super(descriptor);
    }
    
    @Override
    public void relink(final GuardedInvocation guardedInvocation, final MethodHandle relink) {
        this.setTarget(guardedInvocation.compose(relink));
    }
    
    @Override
    public void resetAndRelink(final GuardedInvocation guardedInvocation, final MethodHandle relink) {
        this.relink(guardedInvocation, relink);
    }
}
