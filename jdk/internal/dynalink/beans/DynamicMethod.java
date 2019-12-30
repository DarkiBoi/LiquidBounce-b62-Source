// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.CallSiteDescriptor;

abstract class DynamicMethod
{
    private final String name;
    
    DynamicMethod(final String name) {
        this.name = name;
    }
    
    String getName() {
        return this.name;
    }
    
    abstract MethodHandle getInvocation(final CallSiteDescriptor p0, final LinkerServices p1);
    
    abstract SingleDynamicMethod getMethodForExactParamTypes(final String p0);
    
    abstract boolean contains(final SingleDynamicMethod p0);
    
    static String getClassAndMethodName(final Class<?> clazz, final String name) {
        final String clazzName = clazz.getCanonicalName();
        return ((clazzName == null) ? clazz.getName() : clazzName) + "." + name;
    }
    
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + " " + this.getName() + "]";
    }
    
    boolean isConstructor() {
        return false;
    }
}
