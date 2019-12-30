// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.lookup;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

public final class Lookup
{
    public static final MethodHandleFunctionality MH;
    public static final MethodHandle EMPTY_GETTER;
    public static final MethodHandle EMPTY_SETTER;
    public static final MethodHandle TYPE_ERROR_THROWER_GETTER;
    public static final MethodHandle TYPE_ERROR_THROWER_SETTER;
    public static final MethodType GET_OBJECT_TYPE;
    public static final MethodType SET_OBJECT_TYPE;
    public static final MethodType GET_PRIMITIVE_TYPE;
    public static final MethodType SET_PRIMITIVE_TYPE;
    
    private Lookup() {
    }
    
    public static Object emptyGetter(final Object self) {
        return ScriptRuntime.UNDEFINED;
    }
    
    public static void emptySetter(final Object self, final Object value) {
    }
    
    public static MethodHandle emptyGetter(final Class<?> type) {
        return filterReturnType(Lookup.EMPTY_GETTER, type);
    }
    
    public static Object typeErrorThrowerGetter(final Object self) {
        throw ECMAErrors.typeError("strict.getter.setter.poison", ScriptRuntime.safeToString(self));
    }
    
    public static void typeErrorThrowerSetter(final Object self, final Object value) {
        throw ECMAErrors.typeError("strict.getter.setter.poison", ScriptRuntime.safeToString(self));
    }
    
    public static MethodHandle filterArgumentType(final MethodHandle mh, final int n, final Class<?> from) {
        final Class<?> to = mh.type().parameterType(n);
        if (from != Integer.TYPE) {
            if (from == Long.TYPE) {
                if (to == Integer.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_INT32_L.methodHandle());
                }
            }
            else if (from == Double.TYPE) {
                if (to == Integer.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_INT32_D.methodHandle());
                }
                if (to == Long.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_UINT32_D.methodHandle());
                }
            }
            else if (!from.isPrimitive()) {
                if (to == Integer.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_INT32.methodHandle());
                }
                if (to == Long.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_UINT32.methodHandle());
                }
                if (to == Double.TYPE) {
                    return Lookup.MH.filterArguments(mh, n, JSType.TO_NUMBER.methodHandle());
                }
                if (!to.isPrimitive()) {
                    return mh;
                }
                assert false : "unsupported Lookup.filterReturnType type " + from + " -> " + to;
            }
        }
        return Lookup.MH.explicitCastArguments(mh, mh.type().changeParameterType(n, from));
    }
    
    public static MethodHandle filterReturnType(final MethodHandle mh, final Class<?> type) {
        final Class<?> retType = mh.type().returnType();
        if (retType != Integer.TYPE) {
            if (retType == Long.TYPE) {
                if (type == Integer.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_INT32_L.methodHandle());
                }
            }
            else if (retType == Double.TYPE) {
                if (type == Integer.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_INT32_D.methodHandle());
                }
                if (type == Long.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_UINT32_D.methodHandle());
                }
            }
            else if (!retType.isPrimitive()) {
                if (type == Integer.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_INT32.methodHandle());
                }
                if (type == Long.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_UINT32.methodHandle());
                }
                if (type == Double.TYPE) {
                    return Lookup.MH.filterReturnValue(mh, JSType.TO_NUMBER.methodHandle());
                }
                if (!type.isPrimitive()) {
                    return mh;
                }
                assert false : "unsupported Lookup.filterReturnType type " + retType + " -> " + type;
            }
        }
        return Lookup.MH.explicitCastArguments(mh, mh.type().changeReturnType(type));
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), Lookup.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        MH = MethodHandleFactory.getFunctionality();
        EMPTY_GETTER = findOwnMH("emptyGetter", Object.class, Object.class);
        EMPTY_SETTER = findOwnMH("emptySetter", Void.TYPE, Object.class, Object.class);
        TYPE_ERROR_THROWER_GETTER = findOwnMH("typeErrorThrowerGetter", Object.class, Object.class);
        TYPE_ERROR_THROWER_SETTER = findOwnMH("typeErrorThrowerSetter", Void.TYPE, Object.class, Object.class);
        GET_OBJECT_TYPE = Lookup.MH.type(Object.class, Object.class);
        SET_OBJECT_TYPE = Lookup.MH.type(Void.TYPE, Object.class, Object.class);
        GET_PRIMITIVE_TYPE = Lookup.MH.type(Long.TYPE, Object.class);
        SET_PRIMITIVE_TYPE = Lookup.MH.type(Void.TYPE, Object.class, Long.TYPE);
    }
}
