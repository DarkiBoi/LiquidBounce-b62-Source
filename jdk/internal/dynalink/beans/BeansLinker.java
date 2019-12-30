// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.util.Collections;
import java.util.Collection;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

public class BeansLinker implements GuardingDynamicLinker
{
    private static final ClassValue<TypeBasedGuardingDynamicLinker> linkers;
    
    public static TypeBasedGuardingDynamicLinker getLinkerForClass(final Class<?> clazz) {
        return BeansLinker.linkers.get(clazz);
    }
    
    public static boolean isDynamicMethod(final Object obj) {
        return obj instanceof DynamicMethod;
    }
    
    public static boolean isDynamicConstructor(final Object obj) {
        return obj instanceof DynamicMethod && ((DynamicMethod)obj).isConstructor();
    }
    
    public static Object getConstructorMethod(final Class<?> clazz, final String signature) {
        return StaticClassLinker.getConstructorMethod(clazz, signature);
    }
    
    public static Collection<String> getReadableInstancePropertyNames(final Class<?> clazz) {
        final TypeBasedGuardingDynamicLinker linker = getLinkerForClass(clazz);
        if (linker instanceof BeanLinker) {
            return ((BeanLinker)linker).getReadablePropertyNames();
        }
        return (Collection<String>)Collections.emptySet();
    }
    
    public static Collection<String> getWritableInstancePropertyNames(final Class<?> clazz) {
        final TypeBasedGuardingDynamicLinker linker = getLinkerForClass(clazz);
        if (linker instanceof BeanLinker) {
            return ((BeanLinker)linker).getWritablePropertyNames();
        }
        return (Collection<String>)Collections.emptySet();
    }
    
    public static Collection<String> getInstanceMethodNames(final Class<?> clazz) {
        final TypeBasedGuardingDynamicLinker linker = getLinkerForClass(clazz);
        if (linker instanceof BeanLinker) {
            return ((BeanLinker)linker).getMethodNames();
        }
        return (Collection<String>)Collections.emptySet();
    }
    
    public static Collection<String> getReadableStaticPropertyNames(final Class<?> clazz) {
        return StaticClassLinker.getReadableStaticPropertyNames(clazz);
    }
    
    public static Collection<String> getWritableStaticPropertyNames(final Class<?> clazz) {
        return StaticClassLinker.getWritableStaticPropertyNames(clazz);
    }
    
    public static Collection<String> getStaticMethodNames(final Class<?> clazz) {
        return StaticClassLinker.getStaticMethodNames(clazz);
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final CallSiteDescriptor callSiteDescriptor = request.getCallSiteDescriptor();
        final int l = callSiteDescriptor.getNameTokenCount();
        if (l < 2 || "dyn" != callSiteDescriptor.getNameToken(0)) {
            return null;
        }
        final Object receiver = request.getReceiver();
        if (receiver == null) {
            return null;
        }
        return getLinkerForClass(receiver.getClass()).getGuardedInvocation(request, linkerServices);
    }
    
    static {
        linkers = new ClassValue<TypeBasedGuardingDynamicLinker>() {
            @Override
            protected TypeBasedGuardingDynamicLinker computeValue(final Class<?> clazz) {
                return (clazz == Class.class) ? new ClassLinker() : ((clazz == StaticClass.class) ? new StaticClassLinker() : (DynamicMethod.class.isAssignableFrom(clazz) ? new DynamicMethodLinker() : new BeanLinker(clazz)));
            }
        };
    }
}
