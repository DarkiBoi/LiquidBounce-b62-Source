// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.support.Lookup;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;

class ClassLinker extends BeanLinker
{
    private static final MethodHandle FOR_CLASS;
    
    ClassLinker() {
        super(Class.class);
        this.setPropertyGetter("static", ClassLinker.FOR_CLASS, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
    }
    
    static {
        FOR_CLASS = new Lookup(MethodHandles.lookup()).findStatic(StaticClass.class, "forClass", MethodType.methodType(StaticClass.class, Class.class));
    }
}
