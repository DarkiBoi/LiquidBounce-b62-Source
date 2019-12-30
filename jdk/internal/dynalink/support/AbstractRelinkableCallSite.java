// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.RelinkableCallSite;
import java.lang.invoke.MutableCallSite;

public abstract class AbstractRelinkableCallSite extends MutableCallSite implements RelinkableCallSite
{
    private final CallSiteDescriptor descriptor;
    
    protected AbstractRelinkableCallSite(final CallSiteDescriptor descriptor) {
        super(descriptor.getMethodType());
        this.descriptor = descriptor;
    }
    
    @Override
    public CallSiteDescriptor getDescriptor() {
        return this.descriptor;
    }
    
    @Override
    public void initialize(final MethodHandle relinkAndInvoke) {
        this.setTarget(relinkAndInvoke);
    }
}
