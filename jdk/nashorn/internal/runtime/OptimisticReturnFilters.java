// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.support.TypeUtilities;
import java.lang.invoke.MethodHandle;

public final class OptimisticReturnFilters
{
    private static final MethodHandle[] ENSURE_INT;
    private static final MethodHandle[] ENSURE_NUMBER;
    private static final int VOID_TYPE_INDEX;
    private static final int BOOLEAN_TYPE_INDEX;
    private static final int CHAR_TYPE_INDEX;
    private static final int LONG_TYPE_INDEX;
    private static final int FLOAT_TYPE_INDEX;
    
    public static MethodHandle filterOptimisticReturnValue(final MethodHandle mh, final Class<?> expectedReturnType, final int programPoint) {
        if (!UnwarrantedOptimismException.isValid(programPoint)) {
            return mh;
        }
        final MethodType type = mh.type();
        final Class<?> actualReturnType = type.returnType();
        if (TypeUtilities.isConvertibleWithoutLoss(actualReturnType, expectedReturnType)) {
            return mh;
        }
        final MethodHandle guard = getOptimisticTypeGuard(expectedReturnType, actualReturnType);
        return (guard == null) ? mh : Lookup.MH.filterReturnValue(mh, Lookup.MH.insertArguments(guard, guard.type().parameterCount() - 1, programPoint));
    }
    
    public static GuardedInvocation filterOptimisticReturnValue(final GuardedInvocation inv, final CallSiteDescriptor desc) {
        if (!NashornCallSiteDescriptor.isOptimistic(desc)) {
            return inv;
        }
        return inv.replaceMethods(filterOptimisticReturnValue(inv.getInvocation(), desc.getMethodType().returnType(), NashornCallSiteDescriptor.getProgramPoint(desc)), inv.getGuard());
    }
    
    private static MethodHandle getOptimisticTypeGuard(final Class<?> actual, final Class<?> provable) {
        final int provableTypeIndex = getProvableTypeIndex(provable);
        MethodHandle guard;
        if (actual == Integer.TYPE) {
            guard = OptimisticReturnFilters.ENSURE_INT[provableTypeIndex];
        }
        else if (actual == Double.TYPE) {
            guard = OptimisticReturnFilters.ENSURE_NUMBER[provableTypeIndex];
        }
        else {
            guard = null;
            assert !actual.isPrimitive() : actual + ", " + provable;
        }
        if (guard != null && !provable.isPrimitive()) {
            return guard.asType(guard.type().changeParameterType(0, provable));
        }
        return guard;
    }
    
    private static int getProvableTypeIndex(final Class<?> provable) {
        final int accTypeIndex = JSType.getAccessorTypeIndex(provable);
        if (accTypeIndex != -1) {
            return accTypeIndex;
        }
        if (provable == Boolean.TYPE) {
            return OptimisticReturnFilters.BOOLEAN_TYPE_INDEX;
        }
        if (provable == Void.TYPE) {
            return OptimisticReturnFilters.VOID_TYPE_INDEX;
        }
        if (provable == Byte.TYPE || provable == Short.TYPE) {
            return 0;
        }
        if (provable == Character.TYPE) {
            return OptimisticReturnFilters.CHAR_TYPE_INDEX;
        }
        if (provable == Long.TYPE) {
            return OptimisticReturnFilters.LONG_TYPE_INDEX;
        }
        if (provable == Float.TYPE) {
            return OptimisticReturnFilters.FLOAT_TYPE_INDEX;
        }
        throw new AssertionError((Object)provable.getName());
    }
    
    private static int ensureInt(final long arg, final int programPoint) {
        if (JSType.isRepresentableAsInt(arg)) {
            return (int)arg;
        }
        throw UnwarrantedOptimismException.createNarrowest(arg, programPoint);
    }
    
    private static int ensureInt(final double arg, final int programPoint) {
        if (JSType.isStrictlyRepresentableAsInt(arg)) {
            return (int)arg;
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.NUMBER);
    }
    
    public static int ensureInt(final Object arg, final int programPoint) {
        if (isPrimitiveNumberWrapper(arg)) {
            final double d = ((Number)arg).doubleValue();
            if (JSType.isStrictlyRepresentableAsInt(d)) {
                return (int)d;
            }
        }
        throw UnwarrantedOptimismException.createNarrowest(arg, programPoint);
    }
    
    private static boolean isPrimitiveNumberWrapper(final Object obj) {
        if (obj == null) {
            return false;
        }
        final Class<?> c = obj.getClass();
        return c == Integer.class || c == Double.class || c == Long.class || c == Float.class || c == Short.class || c == Byte.class;
    }
    
    private static int ensureInt(final boolean arg, final int programPoint) {
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }
    
    private static int ensureInt(final char arg, final int programPoint) {
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }
    
    private static int ensureInt(final int programPoint) {
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint, Type.OBJECT);
    }
    
    private static double ensureNumber(final long arg, final int programPoint) {
        if (JSType.isRepresentableAsDouble(arg)) {
            return (double)arg;
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }
    
    public static double ensureNumber(final Object arg, final int programPoint) {
        if (isPrimitiveNumberWrapper(arg) && (arg.getClass() != Long.class || JSType.isRepresentableAsDouble((long)arg))) {
            return ((Number)arg).doubleValue();
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), OptimisticReturnFilters.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        final MethodHandle INT_DOUBLE = findOwnMH("ensureInt", Integer.TYPE, Double.TYPE, Integer.TYPE);
        ENSURE_INT = new MethodHandle[] { null, INT_DOUBLE, findOwnMH("ensureInt", Integer.TYPE, Object.class, Integer.TYPE), findOwnMH("ensureInt", Integer.TYPE, Integer.TYPE), findOwnMH("ensureInt", Integer.TYPE, Boolean.TYPE, Integer.TYPE), findOwnMH("ensureInt", Integer.TYPE, Character.TYPE, Integer.TYPE), findOwnMH("ensureInt", Integer.TYPE, Long.TYPE, Integer.TYPE), INT_DOUBLE.asType(INT_DOUBLE.type().changeParameterType(0, (Class<?>)Float.TYPE)) };
        VOID_TYPE_INDEX = OptimisticReturnFilters.ENSURE_INT.length - 5;
        BOOLEAN_TYPE_INDEX = OptimisticReturnFilters.ENSURE_INT.length - 4;
        CHAR_TYPE_INDEX = OptimisticReturnFilters.ENSURE_INT.length - 3;
        LONG_TYPE_INDEX = OptimisticReturnFilters.ENSURE_INT.length - 2;
        FLOAT_TYPE_INDEX = OptimisticReturnFilters.ENSURE_INT.length - 1;
        ENSURE_NUMBER = new MethodHandle[] { null, null, findOwnMH("ensureNumber", Double.TYPE, Object.class, Integer.TYPE), OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.VOID_TYPE_INDEX].asType(OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.VOID_TYPE_INDEX].type().changeReturnType((Class<?>)Double.TYPE)), OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.BOOLEAN_TYPE_INDEX].asType(OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.BOOLEAN_TYPE_INDEX].type().changeReturnType((Class<?>)Double.TYPE)), OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.CHAR_TYPE_INDEX].asType(OptimisticReturnFilters.ENSURE_INT[OptimisticReturnFilters.CHAR_TYPE_INDEX].type().changeReturnType((Class<?>)Double.TYPE)), findOwnMH("ensureNumber", Double.TYPE, Long.TYPE, Integer.TYPE), null };
    }
}
