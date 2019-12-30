// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodType;

class UnnamedDynCallSiteDescriptor extends AbstractCallSiteDescriptor
{
    private final MethodType methodType;
    private final String op;
    
    UnnamedDynCallSiteDescriptor(final String op, final MethodType methodType) {
        this.op = op;
        this.methodType = methodType;
    }
    
    @Override
    public int getNameTokenCount() {
        return 2;
    }
    
    String getOp() {
        return this.op;
    }
    
    @Override
    public String getNameToken(final int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.op;
            }
            default: {
                throw new IndexOutOfBoundsException(String.valueOf(i));
            }
        }
    }
    
    @Override
    public MethodType getMethodType() {
        return this.methodType;
    }
    
    @Override
    public CallSiteDescriptor changeMethodType(final MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new UnnamedDynCallSiteDescriptor(this.op, newMethodType));
    }
}
