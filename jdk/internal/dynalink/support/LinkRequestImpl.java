// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.LinkRequest;

public class LinkRequestImpl implements LinkRequest
{
    private final CallSiteDescriptor callSiteDescriptor;
    private final Object callSiteToken;
    private final Object[] arguments;
    private final boolean callSiteUnstable;
    private final int linkCount;
    
    public LinkRequestImpl(final CallSiteDescriptor callSiteDescriptor, final Object callSiteToken, final int linkCount, final boolean callSiteUnstable, final Object... arguments) {
        this.callSiteDescriptor = callSiteDescriptor;
        this.callSiteToken = callSiteToken;
        this.linkCount = linkCount;
        this.callSiteUnstable = callSiteUnstable;
        this.arguments = arguments;
    }
    
    @Override
    public Object[] getArguments() {
        return (Object[])((this.arguments != null) ? ((Object[])this.arguments.clone()) : null);
    }
    
    @Override
    public Object getReceiver() {
        return (this.arguments != null && this.arguments.length > 0) ? this.arguments[0] : null;
    }
    
    @Override
    public CallSiteDescriptor getCallSiteDescriptor() {
        return this.callSiteDescriptor;
    }
    
    @Override
    public Object getCallSiteToken() {
        return this.callSiteToken;
    }
    
    @Override
    public boolean isCallSiteUnstable() {
        return this.callSiteUnstable;
    }
    
    @Override
    public int getLinkCount() {
        return this.linkCount;
    }
    
    @Override
    public LinkRequest withoutRuntimeContext() {
        return this;
    }
    
    @Override
    public LinkRequest replaceArguments(final CallSiteDescriptor newCallSiteDescriptor, final Object[] newArguments) {
        return new LinkRequestImpl(newCallSiteDescriptor, this.callSiteToken, this.linkCount, this.callSiteUnstable, newArguments);
    }
}
