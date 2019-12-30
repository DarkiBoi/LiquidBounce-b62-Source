// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;

class LookupCallSiteDescriptor extends DefaultCallSiteDescriptor
{
    private final MethodHandles.Lookup lookup;
    
    LookupCallSiteDescriptor(final String[] tokenizedName, final MethodType methodType, final MethodHandles.Lookup lookup) {
        super(tokenizedName, methodType);
        this.lookup = lookup;
    }
    
    @Override
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }
    
    @Override
    public CallSiteDescriptor changeMethodType(final MethodType newMethodType) {
        return new LookupCallSiteDescriptor(this.getTokenizedName(), newMethodType, this.lookup);
    }
}
