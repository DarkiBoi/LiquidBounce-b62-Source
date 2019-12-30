// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.DynamicLinker;
import java.util.logging.Level;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import java.util.logging.Logger;

public class Guards
{
    private static final Logger LOG;
    private static final MethodHandle IS_INSTANCE;
    private static final MethodHandle IS_OF_CLASS;
    private static final MethodHandle IS_ARRAY;
    private static final MethodHandle IS_IDENTICAL;
    private static final MethodHandle IS_NULL;
    private static final MethodHandle IS_NOT_NULL;
    
    private Guards() {
    }
    
    public static MethodHandle isOfClass(final Class<?> clazz, final MethodType type) {
        final Class<?> declaredType = type.parameterType(0);
        if (clazz == declaredType) {
            Guards.LOG.log(Level.WARNING, "isOfClassGuardAlwaysTrue", new Object[] { clazz.getName(), 0, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantTrue(type);
        }
        if (!declaredType.isAssignableFrom(clazz)) {
            Guards.LOG.log(Level.WARNING, "isOfClassGuardAlwaysFalse", new Object[] { clazz.getName(), 0, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantFalse(type);
        }
        return getClassBoundArgumentTest(Guards.IS_OF_CLASS, clazz, 0, type);
    }
    
    public static MethodHandle isInstance(final Class<?> clazz, final MethodType type) {
        return isInstance(clazz, 0, type);
    }
    
    public static MethodHandle isInstance(final Class<?> clazz, final int pos, final MethodType type) {
        final Class<?> declaredType = type.parameterType(pos);
        if (clazz.isAssignableFrom(declaredType)) {
            Guards.LOG.log(Level.WARNING, "isInstanceGuardAlwaysTrue", new Object[] { clazz.getName(), pos, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantTrue(type);
        }
        if (!declaredType.isAssignableFrom(clazz)) {
            Guards.LOG.log(Level.WARNING, "isInstanceGuardAlwaysFalse", new Object[] { clazz.getName(), pos, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantFalse(type);
        }
        return getClassBoundArgumentTest(Guards.IS_INSTANCE, clazz, pos, type);
    }
    
    public static MethodHandle isArray(final int pos, final MethodType type) {
        final Class<?> declaredType = type.parameterType(pos);
        if (declaredType.isArray()) {
            Guards.LOG.log(Level.WARNING, "isArrayGuardAlwaysTrue", new Object[] { pos, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantTrue(type);
        }
        if (!declaredType.isAssignableFrom(Object[].class)) {
            Guards.LOG.log(Level.WARNING, "isArrayGuardAlwaysFalse", new Object[] { pos, type, DynamicLinker.getLinkedCallSiteLocation() });
            return constantFalse(type);
        }
        return asType(Guards.IS_ARRAY, pos, type);
    }
    
    public static boolean canReferenceDirectly(final ClassLoader referrerLoader, final ClassLoader referredLoader) {
        if (referredLoader == null) {
            return true;
        }
        if (referrerLoader == null) {
            return false;
        }
        ClassLoader referrer = referrerLoader;
        while (referrer != referredLoader) {
            referrer = referrer.getParent();
            if (referrer == null) {
                return false;
            }
        }
        return true;
    }
    
    private static MethodHandle getClassBoundArgumentTest(final MethodHandle test, final Class<?> clazz, final int pos, final MethodType type) {
        return asType(test.bindTo(clazz), pos, type);
    }
    
    public static MethodHandle asType(final MethodHandle test, final MethodType type) {
        return test.asType(getTestType(test, type));
    }
    
    public static MethodHandle asType(final LinkerServices linkerServices, final MethodHandle test, final MethodType type) {
        return linkerServices.asType(test, getTestType(test, type));
    }
    
    private static MethodType getTestType(final MethodHandle test, final MethodType type) {
        return type.dropParameterTypes(test.type().parameterCount(), type.parameterCount()).changeReturnType((Class<?>)Boolean.TYPE);
    }
    
    private static MethodHandle asType(final MethodHandle test, final int pos, final MethodType type) {
        assert test != null;
        assert type != null;
        assert type.parameterCount() > 0;
        assert pos >= 0 && pos < type.parameterCount();
        assert test.type().parameterCount() == 1;
        assert test.type().returnType() == Boolean.TYPE;
        return MethodHandles.permuteArguments(test.asType(test.type().changeParameterType(0, type.parameterType(pos))), type.changeReturnType((Class<?>)Boolean.TYPE), pos);
    }
    
    public static MethodHandle getClassGuard(final Class<?> clazz) {
        return Guards.IS_OF_CLASS.bindTo(clazz);
    }
    
    public static MethodHandle getInstanceOfGuard(final Class<?> clazz) {
        return Guards.IS_INSTANCE.bindTo(clazz);
    }
    
    public static MethodHandle getIdentityGuard(final Object obj) {
        return Guards.IS_IDENTICAL.bindTo(obj);
    }
    
    public static MethodHandle isNull() {
        return Guards.IS_NULL;
    }
    
    public static MethodHandle isNotNull() {
        return Guards.IS_NOT_NULL;
    }
    
    private static boolean isNull(final Object obj) {
        return obj == null;
    }
    
    private static boolean isNotNull(final Object obj) {
        return obj != null;
    }
    
    private static boolean isArray(final Object o) {
        return o != null && o.getClass().isArray();
    }
    
    private static boolean isOfClass(final Class<?> c, final Object o) {
        return o != null && o.getClass() == c;
    }
    
    private static boolean isIdentical(final Object o1, final Object o2) {
        return o1 == o2;
    }
    
    private static MethodHandle constantTrue(final MethodType type) {
        return constantBoolean(Boolean.TRUE, type);
    }
    
    private static MethodHandle constantFalse(final MethodType type) {
        return constantBoolean(Boolean.FALSE, type);
    }
    
    private static MethodHandle constantBoolean(final Boolean value, final MethodType type) {
        return MethodHandles.permuteArguments(MethodHandles.constant(Boolean.TYPE, value), type.changeReturnType((Class<?>)Boolean.TYPE), new int[0]);
    }
    
    static {
        LOG = Logger.getLogger(Guards.class.getName(), "jdk.internal.dynalink.support.messages");
        IS_INSTANCE = Lookup.PUBLIC.findVirtual(Class.class, "isInstance", MethodType.methodType(Boolean.TYPE, Object.class));
        final Lookup lookup = new Lookup(MethodHandles.lookup());
        IS_OF_CLASS = lookup.findOwnStatic("isOfClass", Boolean.TYPE, Class.class, Object.class);
        IS_ARRAY = lookup.findOwnStatic("isArray", Boolean.TYPE, Object.class);
        IS_IDENTICAL = lookup.findOwnStatic("isIdentical", Boolean.TYPE, Object.class, Object.class);
        IS_NULL = lookup.findOwnStatic("isNull", Boolean.TYPE, Object.class);
        IS_NOT_NULL = lookup.findOwnStatic("isNotNull", Boolean.TYPE, Object.class);
    }
}
