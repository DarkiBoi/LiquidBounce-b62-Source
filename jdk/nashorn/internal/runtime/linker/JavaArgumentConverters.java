// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.util.HashMap;
import jdk.internal.dynalink.support.TypeUtilities;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.util.Map;
import java.lang.invoke.MethodHandle;

final class JavaArgumentConverters
{
    private static final MethodHandle TO_BOOLEAN;
    private static final MethodHandle TO_STRING;
    private static final MethodHandle TO_DOUBLE;
    private static final MethodHandle TO_NUMBER;
    private static final MethodHandle TO_LONG;
    private static final MethodHandle TO_LONG_PRIMITIVE;
    private static final MethodHandle TO_CHAR;
    private static final MethodHandle TO_CHAR_PRIMITIVE;
    private static final Map<Class<?>, MethodHandle> CONVERTERS;
    
    private JavaArgumentConverters() {
    }
    
    static MethodHandle getConverter(final Class<?> targetType) {
        return JavaArgumentConverters.CONVERTERS.get(targetType);
    }
    
    private static Boolean toBoolean(final Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean)obj;
        }
        if (obj == null) {
            return null;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return null;
        }
        if (obj instanceof Number) {
            final double num = ((Number)obj).doubleValue();
            return num != 0.0 && !Double.isNaN(num);
        }
        if (JSType.isString(obj)) {
            return ((CharSequence)obj).length() > 0;
        }
        if (obj instanceof ScriptObject) {
            return true;
        }
        throw assertUnexpectedType(obj);
    }
    
    private static Character toChar(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            final int ival = ((Number)o).intValue();
            if (ival >= 0 && ival <= 65535) {
                return (char)ival;
            }
            throw ECMAErrors.typeError("cant.convert.number.to.char", new String[0]);
        }
        else {
            final String s = toString(o);
            if (s == null) {
                return null;
            }
            if (s.length() != 1) {
                throw ECMAErrors.typeError("cant.convert.string.to.char", new String[0]);
            }
            return s.charAt(0);
        }
    }
    
    static char toCharPrimitive(final Object obj0) {
        final Character c = toChar(obj0);
        return (c == null) ? '\0' : c;
    }
    
    static String toString(final Object obj) {
        return (obj == null) ? null : JSType.toString(obj);
    }
    
    private static Double toDouble(final Object obj0) {
        Object obj = obj0;
        while (obj != null) {
            if (obj instanceof Double) {
                return (Double)obj;
            }
            if (obj instanceof Number) {
                return ((Number)obj).doubleValue();
            }
            if (obj instanceof String) {
                return JSType.toNumber((String)obj);
            }
            if (obj instanceof ConsString) {
                return JSType.toNumber(obj.toString());
            }
            if (obj instanceof Boolean) {
                return obj ? 1.0 : 0.0;
            }
            if (obj instanceof ScriptObject) {
                obj = JSType.toPrimitive(obj, Number.class);
            }
            else {
                if (obj == ScriptRuntime.UNDEFINED) {
                    return Double.NaN;
                }
                throw assertUnexpectedType(obj);
            }
        }
        return null;
    }
    
    private static Number toNumber(final Object obj0) {
        Object obj = obj0;
        while (obj != null) {
            if (obj instanceof Number) {
                return (Number)obj;
            }
            if (obj instanceof String) {
                return JSType.toNumber((String)obj);
            }
            if (obj instanceof ConsString) {
                return JSType.toNumber(obj.toString());
            }
            if (obj instanceof Boolean) {
                return obj ? 1.0 : 0.0;
            }
            if (obj instanceof ScriptObject) {
                obj = JSType.toPrimitive(obj, Number.class);
            }
            else {
                if (obj == ScriptRuntime.UNDEFINED) {
                    return Double.NaN;
                }
                throw assertUnexpectedType(obj);
            }
        }
        return null;
    }
    
    private static Long toLong(final Object obj0) {
        Object obj = obj0;
        while (obj != null) {
            if (obj instanceof Long) {
                return (Long)obj;
            }
            if (obj instanceof Integer) {
                return (long)obj;
            }
            if (obj instanceof Double) {
                final Double d = (Double)obj;
                if (Double.isInfinite(d)) {
                    return 0L;
                }
                return d.longValue();
            }
            else if (obj instanceof Float) {
                final Float f = (Float)obj;
                if (Float.isInfinite(f)) {
                    return 0L;
                }
                return f.longValue();
            }
            else {
                if (obj instanceof Number) {
                    return ((Number)obj).longValue();
                }
                if (JSType.isString(obj)) {
                    return JSType.toLong(obj);
                }
                if (obj instanceof Boolean) {
                    return (long)(((boolean)obj) ? 1 : 0);
                }
                if (obj instanceof ScriptObject) {
                    obj = JSType.toPrimitive(obj, Number.class);
                }
                else {
                    if (obj == ScriptRuntime.UNDEFINED) {
                        return null;
                    }
                    throw assertUnexpectedType(obj);
                }
            }
        }
        return null;
    }
    
    private static AssertionError assertUnexpectedType(final Object obj) {
        return new AssertionError((Object)("Unexpected type" + obj.getClass().getName() + ". Guards should have prevented this"));
    }
    
    private static long toLongPrimitive(final Object obj0) {
        final Long l = toLong(obj0);
        return (l == null) ? 0L : l;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), JavaArgumentConverters.class, name, Lookup.MH.type(rtype, types));
    }
    
    private static void putDoubleConverter(final Class<?> targetType) {
        final Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        JavaArgumentConverters.CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(JSType.TO_NUMBER.methodHandle(), JSType.TO_NUMBER.methodHandle().type().changeReturnType(primitive)));
        JavaArgumentConverters.CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(JavaArgumentConverters.TO_DOUBLE, findOwnMH(primitive.getName() + "Value", targetType, Double.class)));
    }
    
    private static void putLongConverter(final Class<?> targetType) {
        final Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        JavaArgumentConverters.CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(JavaArgumentConverters.TO_LONG_PRIMITIVE, JavaArgumentConverters.TO_LONG_PRIMITIVE.type().changeReturnType(primitive)));
        JavaArgumentConverters.CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(JavaArgumentConverters.TO_LONG, findOwnMH(primitive.getName() + "Value", targetType, Long.class)));
    }
    
    private static Byte byteValue(final Long l) {
        return (l == null) ? null : Byte.valueOf(l.byteValue());
    }
    
    private static Short shortValue(final Long l) {
        return (l == null) ? null : Short.valueOf(l.shortValue());
    }
    
    private static Integer intValue(final Long l) {
        return (l == null) ? null : Integer.valueOf(l.intValue());
    }
    
    private static Float floatValue(final Double d) {
        return (d == null) ? null : Float.valueOf(d.floatValue());
    }
    
    static {
        TO_BOOLEAN = findOwnMH("toBoolean", Boolean.class, Object.class);
        TO_STRING = findOwnMH("toString", String.class, Object.class);
        TO_DOUBLE = findOwnMH("toDouble", Double.class, Object.class);
        TO_NUMBER = findOwnMH("toNumber", Number.class, Object.class);
        TO_LONG = findOwnMH("toLong", Long.class, Object.class);
        TO_LONG_PRIMITIVE = findOwnMH("toLongPrimitive", Long.TYPE, Object.class);
        TO_CHAR = findOwnMH("toChar", Character.class, Object.class);
        TO_CHAR_PRIMITIVE = findOwnMH("toCharPrimitive", Character.TYPE, Object.class);
        (CONVERTERS = new HashMap<Class<?>, MethodHandle>()).put(Number.class, JavaArgumentConverters.TO_NUMBER);
        JavaArgumentConverters.CONVERTERS.put(String.class, JavaArgumentConverters.TO_STRING);
        JavaArgumentConverters.CONVERTERS.put(Boolean.TYPE, JSType.TO_BOOLEAN.methodHandle());
        JavaArgumentConverters.CONVERTERS.put(Boolean.class, JavaArgumentConverters.TO_BOOLEAN);
        JavaArgumentConverters.CONVERTERS.put(Character.TYPE, JavaArgumentConverters.TO_CHAR_PRIMITIVE);
        JavaArgumentConverters.CONVERTERS.put(Character.class, JavaArgumentConverters.TO_CHAR);
        JavaArgumentConverters.CONVERTERS.put(Double.TYPE, JSType.TO_NUMBER.methodHandle());
        JavaArgumentConverters.CONVERTERS.put(Double.class, JavaArgumentConverters.TO_DOUBLE);
        JavaArgumentConverters.CONVERTERS.put(Long.TYPE, JavaArgumentConverters.TO_LONG_PRIMITIVE);
        JavaArgumentConverters.CONVERTERS.put(Long.class, JavaArgumentConverters.TO_LONG);
        putLongConverter(Byte.class);
        putLongConverter(Short.class);
        putLongConverter(Integer.class);
        putDoubleConverter(Float.class);
    }
}
