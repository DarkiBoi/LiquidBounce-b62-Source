// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.Lookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;

class CallerSensitiveDynamicMethod extends SingleDynamicMethod
{
    private final AccessibleObject target;
    private final MethodType type;
    
    CallerSensitiveDynamicMethod(final AccessibleObject target) {
        super(getName(target));
        this.target = target;
        this.type = getMethodType(target);
    }
    
    private static String getName(final AccessibleObject target) {
        final Member m = (Member)target;
        final boolean constructor = m instanceof Constructor;
        return SingleDynamicMethod.getMethodNameWithSignature(getMethodType(target), constructor ? m.getName() : DynamicMethod.getClassAndMethodName(m.getDeclaringClass(), m.getName()), !constructor);
    }
    
    @Override
    MethodType getMethodType() {
        return this.type;
    }
    
    private static MethodType getMethodType(final AccessibleObject ao) {
        final boolean isMethod = ao instanceof Method;
        final Class<?> rtype = isMethod ? ((Method)ao).getReturnType() : ((Constructor)ao).getDeclaringClass();
        final Class<?>[] ptypes = isMethod ? ((Method)ao).getParameterTypes() : ((Constructor)ao).getParameterTypes();
        final MethodType type = MethodType.methodType(rtype, ptypes);
        final Member m = (Member)ao;
        return type.insertParameterTypes(0, (Class<?>[])new Class[] { isMethod ? (Modifier.isStatic(m.getModifiers()) ? Object.class : m.getDeclaringClass()) : StaticClass.class });
    }
    
    @Override
    boolean isVarArgs() {
        return (this.target instanceof Method) ? ((Method)this.target).isVarArgs() : ((Constructor)this.target).isVarArgs();
    }
    
    @Override
    MethodHandle getTarget(final MethodHandles.Lookup lookup) {
        if (!(this.target instanceof Method)) {
            return StaticClassIntrospector.editConstructorMethodHandle(Lookup.unreflectConstructor(lookup, (Constructor<?>)this.target));
        }
        final MethodHandle mh = Lookup.unreflect(lookup, (Method)this.target);
        if (Modifier.isStatic(((Member)this.target).getModifiers())) {
            return StaticClassIntrospector.editStaticMethodHandle(mh);
        }
        return mh;
    }
    
    @Override
    boolean isConstructor() {
        return this.target instanceof Constructor;
    }
}
