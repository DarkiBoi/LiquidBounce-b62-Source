// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.util.Collection;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

class StaticClassLinker implements TypeBasedGuardingDynamicLinker
{
    private static final ClassValue<SingleClassStaticsLinker> linkers;
    static final MethodHandle GET_CLASS;
    static final MethodHandle IS_CLASS;
    static final MethodHandle ARRAY_CTOR;
    
    static Object getConstructorMethod(final Class<?> clazz, final String signature) {
        return StaticClassLinker.linkers.get(clazz).getConstructorMethod(signature);
    }
    
    static Collection<String> getReadableStaticPropertyNames(final Class<?> clazz) {
        return StaticClassLinker.linkers.get(clazz).getReadablePropertyNames();
    }
    
    static Collection<String> getWritableStaticPropertyNames(final Class<?> clazz) {
        return StaticClassLinker.linkers.get(clazz).getWritablePropertyNames();
    }
    
    static Collection<String> getStaticMethodNames(final Class<?> clazz) {
        return StaticClassLinker.linkers.get(clazz).getMethodNames();
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final Object receiver = request.getReceiver();
        if (receiver instanceof StaticClass) {
            return StaticClassLinker.linkers.get(((StaticClass)receiver).getRepresentedClass()).getGuardedInvocation(request, linkerServices);
        }
        return null;
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return type == StaticClass.class;
    }
    
    private static boolean isClass(final Class<?> clazz, final Object obj) {
        return obj instanceof StaticClass && ((StaticClass)obj).getRepresentedClass() == clazz;
    }
    
    static {
        linkers = new ClassValue<SingleClassStaticsLinker>() {
            @Override
            protected SingleClassStaticsLinker computeValue(final Class<?> clazz) {
                return new SingleClassStaticsLinker(clazz);
            }
        };
        ARRAY_CTOR = Lookup.PUBLIC.findStatic(Array.class, "newInstance", MethodType.methodType(Object.class, Class.class, Integer.TYPE));
        final Lookup lookup = new Lookup(MethodHandles.lookup());
        GET_CLASS = lookup.findVirtual(StaticClass.class, "getRepresentedClass", MethodType.methodType(Class.class));
        IS_CLASS = lookup.findOwnStatic("isClass", Boolean.TYPE, Class.class, Object.class);
    }
    
    private static class SingleClassStaticsLinker extends AbstractJavaLinker
    {
        private final DynamicMethod constructor;
        
        SingleClassStaticsLinker(final Class<?> clazz) {
            super(clazz, StaticClassLinker.IS_CLASS.bindTo(clazz));
            this.setPropertyGetter("class", StaticClassLinker.GET_CLASS, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
            this.constructor = createConstructorMethod(clazz);
        }
        
        private static DynamicMethod createConstructorMethod(final Class<?> clazz) {
            if (clazz.isArray()) {
                final MethodHandle boundArrayCtor = StaticClassLinker.ARRAY_CTOR.bindTo(clazz.getComponentType());
                return new SimpleDynamicMethod(StaticClassIntrospector.editConstructorMethodHandle(boundArrayCtor.asType(boundArrayCtor.type().changeReturnType(clazz))), clazz, "<init>");
            }
            if (CheckRestrictedPackage.isRestrictedClass(clazz)) {
                return null;
            }
            return AbstractJavaLinker.createDynamicMethod(Arrays.asList(clazz.getConstructors()), clazz, "<init>");
        }
        
        @Override
        FacetIntrospector createFacetIntrospector() {
            return new StaticClassIntrospector(this.clazz);
        }
        
        @Override
        public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
            final GuardedInvocation gi = super.getGuardedInvocation(request, linkerServices);
            if (gi != null) {
                return gi;
            }
            final CallSiteDescriptor desc = request.getCallSiteDescriptor();
            final String op = desc.getNameToken(1);
            if ("new" == op && this.constructor != null) {
                final MethodHandle ctorInvocation = this.constructor.getInvocation(desc, linkerServices);
                if (ctorInvocation != null) {
                    return new GuardedInvocation(ctorInvocation, this.getClassGuard(desc.getMethodType()));
                }
            }
            return null;
        }
        
        @Override
        SingleDynamicMethod getConstructorMethod(final String signature) {
            return (this.constructor != null) ? this.constructor.getMethodForExactParamTypes(signature) : null;
        }
    }
}
