// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodType;

class DefaultCallSiteDescriptor extends AbstractCallSiteDescriptor
{
    private final String[] tokenizedName;
    private final MethodType methodType;
    
    DefaultCallSiteDescriptor(final String[] tokenizedName, final MethodType methodType) {
        this.tokenizedName = tokenizedName;
        this.methodType = methodType;
    }
    
    @Override
    public int getNameTokenCount() {
        return this.tokenizedName.length;
    }
    
    @Override
    public String getNameToken(final int i) {
        try {
            return this.tokenizedName[i];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    String[] getTokenizedName() {
        return this.tokenizedName;
    }
    
    @Override
    public MethodType getMethodType() {
        return this.methodType;
    }
    
    @Override
    public CallSiteDescriptor changeMethodType(final MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new DefaultCallSiteDescriptor(this.tokenizedName, newMethodType));
    }
}
