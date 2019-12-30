// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodType;

class NamedDynCallSiteDescriptor extends UnnamedDynCallSiteDescriptor
{
    private final String name;
    
    NamedDynCallSiteDescriptor(final String op, final String name, final MethodType methodType) {
        super(op, methodType);
        this.name = name;
    }
    
    @Override
    public int getNameTokenCount() {
        return 3;
    }
    
    @Override
    public String getNameToken(final int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.getOp();
            }
            case 2: {
                return this.name;
            }
            default: {
                throw new IndexOutOfBoundsException(String.valueOf(i));
            }
        }
    }
    
    @Override
    public CallSiteDescriptor changeMethodType(final MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new NamedDynCallSiteDescriptor(this.getOp(), this.name, newMethodType));
    }
}
