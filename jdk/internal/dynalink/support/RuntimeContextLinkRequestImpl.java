// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;

public class RuntimeContextLinkRequestImpl extends LinkRequestImpl
{
    private final int runtimeContextArgCount;
    private LinkRequestImpl contextStrippedRequest;
    
    public RuntimeContextLinkRequestImpl(final CallSiteDescriptor callSiteDescriptor, final Object callSiteToken, final int linkCount, final boolean callSiteUnstable, final Object[] arguments, final int runtimeContextArgCount) {
        super(callSiteDescriptor, callSiteToken, linkCount, callSiteUnstable, arguments);
        if (runtimeContextArgCount < 1) {
            throw new IllegalArgumentException("runtimeContextArgCount < 1");
        }
        this.runtimeContextArgCount = runtimeContextArgCount;
    }
    
    @Override
    public LinkRequest withoutRuntimeContext() {
        if (this.contextStrippedRequest == null) {
            this.contextStrippedRequest = new LinkRequestImpl(CallSiteDescriptorFactory.dropParameterTypes(this.getCallSiteDescriptor(), 1, this.runtimeContextArgCount + 1), this.getCallSiteToken(), this.getLinkCount(), this.isCallSiteUnstable(), this.getTruncatedArguments());
        }
        return this.contextStrippedRequest;
    }
    
    @Override
    public LinkRequest replaceArguments(final CallSiteDescriptor callSiteDescriptor, final Object[] arguments) {
        return new RuntimeContextLinkRequestImpl(callSiteDescriptor, this.getCallSiteToken(), this.getLinkCount(), this.isCallSiteUnstable(), arguments, this.runtimeContextArgCount);
    }
    
    private Object[] getTruncatedArguments() {
        final Object[] args = this.getArguments();
        final Object[] newargs = new Object[args.length - this.runtimeContextArgCount];
        newargs[0] = args[0];
        System.arraycopy(args, this.runtimeContextArgCount + 1, newargs, 1, newargs.length - 1);
        return newargs;
    }
}
