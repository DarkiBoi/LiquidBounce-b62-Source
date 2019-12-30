// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

class SimpleDynamicMethod extends SingleDynamicMethod
{
    private final MethodHandle target;
    private final boolean constructor;
    
    SimpleDynamicMethod(final MethodHandle target, final Class<?> clazz, final String name) {
        this(target, clazz, name, false);
    }
    
    SimpleDynamicMethod(final MethodHandle target, final Class<?> clazz, final String name, final boolean constructor) {
        super(getName(target, clazz, name, constructor));
        this.target = target;
        this.constructor = constructor;
    }
    
    private static String getName(final MethodHandle target, final Class<?> clazz, final String name, final boolean constructor) {
        return SingleDynamicMethod.getMethodNameWithSignature(target.type(), constructor ? name : DynamicMethod.getClassAndMethodName(clazz, name), !constructor);
    }
    
    @Override
    boolean isVarArgs() {
        return this.target.isVarargsCollector();
    }
    
    @Override
    MethodType getMethodType() {
        return this.target.type();
    }
    
    @Override
    MethodHandle getTarget(final MethodHandles.Lookup lookup) {
        return this.target;
    }
    
    @Override
    boolean isConstructor() {
        return this.constructor;
    }
}
