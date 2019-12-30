// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.CallSiteDescriptor;

public interface LinkRequest
{
    CallSiteDescriptor getCallSiteDescriptor();
    
    Object getCallSiteToken();
    
    Object[] getArguments();
    
    Object getReceiver();
    
    int getLinkCount();
    
    boolean isCallSiteUnstable();
    
    LinkRequest withoutRuntimeContext();
    
    LinkRequest replaceArguments(final CallSiteDescriptor p0, final Object[] p1);
}
