// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Collections;
import java.util.Arrays;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.List;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;

public enum JSType
{
    UNDEFINED("undefined"), 
    NULL("object"), 
    BOOLEAN("boolean"), 
    NUMBER("number"), 
    STRING("string"), 
    OBJECT("object"), 
    FUNCTION("function");
    
    private final String typeName;
    public static final long MAX_UINT = 4294967295L;
    private static final MethodHandles.Lookup JSTYPE_LOOKUP;
    public static final CompilerConstants.Call TO_BOOLEAN;
    public static final CompilerConstants.Call TO_BOOLEAN_D;
    public static final CompilerConstants.Call TO_INTEGER;
    public static final CompilerConstants.Call TO_LONG;
    public static final CompilerConstants.Call TO_LONG_D;
    public static final CompilerConstants.Call TO_NUMBER;
    public static final CompilerConstants.Call TO_NUMBER_OPTIMISTIC;
    public static final CompilerConstants.Call TO_STRING;
    public static final CompilerConstants.Call TO_INT32;
    public static final CompilerConstants.Call TO_INT32_L;
    public static final CompilerConstants.Call TO_INT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_INT32_D;
    public static final CompilerConstants.Call TO_UINT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_UINT32_DOUBLE;
    public static final CompilerConstants.Call TO_UINT32;
    public static final CompilerConstants.Call TO_UINT32_D;
    public static final CompilerConstants.Call TO_STRING_D;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_STRING;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_CHARSEQUENCE;
    public static final CompilerConstants.Call THROW_UNWARRANTED;
    public static final CompilerConstants.Call ADD_EXACT;
    public static final CompilerConstants.Call SUB_EXACT;
    public static final CompilerConstants.Call MUL_EXACT;
    public static final CompilerConstants.Call DIV_EXACT;
    public static final CompilerConstants.Call DIV_ZERO;
    public static final CompilerConstants.Call REM_ZERO;
    public static final CompilerConstants.Call REM_EXACT;
    public static final CompilerConstants.Call DECREMENT_EXACT;
    public static final CompilerConstants.Call INCREMENT_EXACT;
    public static final CompilerConstants.Call NEGATE_EXACT;
    public static final CompilerConstants.Call TO_JAVA_ARRAY;
    public static final CompilerConstants.Call VOID_RETURN;
    public static final CompilerConstants.Call IS_STRING;
    public static final CompilerConstants.Call IS_NUMBER;
    private static final List<Type> ACCESSOR_TYPES;
    public static final int TYPE_UNDEFINED_INDEX = -1;
    public static final int TYPE_INT_INDEX = 0;
    public static final int TYPE_DOUBLE_INDEX = 1;
    public static final int TYPE_OBJECT_INDEX = 2;
    public static final List<MethodHandle> CONVERT_OBJECT;
    public static final List<MethodHandle> CONVERT_OBJECT_OPTIMISTIC;
    public static final int UNDEFINED_INT = 0;
    public static final long UNDEFINED_LONG = 0L;
    public static final double UNDEFINED_DOUBLE = Double.NaN;
    private static final long MAX_PRECISE_DOUBLE = 9007199254740992L;
    private static final long MIN_PRECISE_DOUBLE = -9007199254740992L;
    public static final List<MethodHandle> GET_UNDEFINED;
    private static final double INT32_LIMIT = 4.294967296E9;
    
    private JSType(final String typeName) {
        this.typeName = typeName;
    }
    
    public final String typeName() {
        return this.typeName;
    }
    
    public static JSType of(final Object obj) {
        if (obj == null) {
            return JSType.NULL;
        }
        if (obj instanceof ScriptObject) {
            return (obj instanceof ScriptFunction) ? JSType.FUNCTION : JSType.OBJECT;
        }
        if (obj instanceof Boolean) {
            return JSType.BOOLEAN;
        }
        if (isString(obj)) {
            return JSType.STRING;
        }
        if (isNumber(obj)) {
            return JSType.NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return JSType.UNDEFINED;
        }
        return Bootstrap.isCallable(obj) ? JSType.FUNCTION : JSType.OBJECT;
    }
    
    public static JSType ofNoFunction(final Object obj) {
        if (obj == null) {
            return JSType.NULL;
        }
        if (obj instanceof ScriptObject) {
            return JSType.OBJECT;
        }
        if (obj instanceof Boolean) {
            return JSType.BOOLEAN;
        }
        if (isString(obj)) {
            return JSType.STRING;
        }
        if (isNumber(obj)) {
            return JSType.NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return JSType.UNDEFINED;
        }
        return JSType.OBJECT;
    }
    
    public static void voidReturn() {
    }
    
    public static boolean isRepresentableAsInt(final long number) {
        return (int)number == number;
    }
    
    public static boolean isRepresentableAsInt(final double number) {
        return (int)number == number;
    }
    
    public static boolean isStrictlyRepresentableAsInt(final double number) {
        return isRepresentableAsInt(number) && isNotNegativeZero(number);
    }
    
    public static boolean isRepresentableAsInt(final Object obj) {
        return obj instanceof Number && isRepresentableAsInt(((Number)obj).doubleValue());
    }
    
    public static boolean isRepresentableAsLong(final double number) {
        return (long)number == number;
    }
    
    public static boolean isRepresentableAsDouble(final long number) {
        return 9007199254740992L >= number && number >= -9007199254740992L;
    }
    
    private static boolean isNotNegativeZero(final double number) {
        return Double.doubleToRawLongBits(number) != Long.MIN_VALUE;
    }
    
    public static boolean isPrimitive(final Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED || isString(obj) || isNumber(obj) || obj instanceof Boolean;
    }
    
    public static Object toPrimitive(final Object obj) {
        return toPrimitive(obj, null);
    }
    
    public static Object toPrimitive(final Object obj, final Class<?> hint) {
        if (obj instanceof ScriptObject) {
            return toPrimitive((ScriptObject)obj, hint);
        }
        if (isPrimitive(obj)) {
            return obj;
        }
        if (hint == Number.class && obj instanceof Number) {
            return ((Number)obj).doubleValue();
        }
        if (obj instanceof JSObject) {
            return toPrimitive((JSObject)obj, hint);
        }
        if (obj instanceof StaticClass) {
            final String name = ((StaticClass)obj).getRepresentedClass().getName();
            return new StringBuilder(12 + name.length()).append("[JavaClass ").append(name).append(']').toString();
        }
        return obj.toString();
    }
    
    private static Object toPrimitive(final ScriptObject sobj, final Class<?> hint) {
        return requirePrimitive(sobj.getDefaultValue(hint));
    }
    
    private static Object requirePrimitive(final Object result) {
        if (!isPrimitive(result)) {
            throw ECMAErrors.typeError("bad.default.value", result.toString());
        }
        return result;
    }
    
    public static Object toPrimitive(final JSObject jsobj, final Class<?> hint) {
        try {
            return requirePrimitive(AbstractJSObject.getDefaultValue(jsobj, hint));
        }
        catch (UnsupportedOperationException e) {
            throw new ECMAException(Context.getGlobal().newTypeError(e.getMessage()), e);
        }
    }
    
    public static String toPrimitiveToString(final Object obj) {
        return toString(toPrimitive(obj));
    }
    
    public static CharSequence toPrimitiveToCharSequence(final Object obj) {
        return toCharSequence(toPrimitive(obj));
    }
    
    public static boolean toBoolean(final double num) {
        return num != 0.0 && !Double.isNaN(num);
    }
    
    public static boolean toBoolean(final Object obj) {
        if (obj instanceof Boolean) {
            return (boolean)obj;
        }
        if (nullOrUndefined(obj)) {
            return false;
        }
        if (obj instanceof Number) {
            final double num = ((Number)obj).doubleValue();
            return num != 0.0 && !Double.isNaN(num);
        }
        return !isString(obj) || ((CharSequence)obj).length() > 0;
    }
    
    public static String toString(final Object obj) {
        return toStringImpl(obj, false);
    }
    
    public static CharSequence toCharSequence(final Object obj) {
        if (obj instanceof ConsString) {
            return (CharSequence)obj;
        }
        return toString(obj);
    }
    
    public static boolean isString(final Object obj) {
        return obj instanceof String || obj instanceof ConsString;
    }
    
    public static boolean isNumber(final Object obj) {
        if (obj != null) {
            final Class<?> c = obj.getClass();
            return c == Integer.class || c == Double.class || c == Float.class || c == Short.class || c == Byte.class;
        }
        return false;
    }
    
    public static String toString(final int num) {
        return Integer.toString(num);
    }
    
    public static String toString(final double num) {
        if (isRepresentableAsInt(num)) {
            return Integer.toString((int)num);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        return NumberToString.stringFor(num);
    }
    
    public static String toString(final double num, final int radix) {
        assert radix >= 2 && radix <= 36 : "invalid radix";
        if (isRepresentableAsInt(num)) {
            return Integer.toString((int)num, radix);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        if (num == 0.0) {
            return "0";
        }
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
        final StringBuilder sb = new StringBuilder();
        final boolean negative = num < 0.0;
        final double signedNum = negative ? (-num) : num;
        double intPart = Math.floor(signedNum);
        double decPart = signedNum - intPart;
        do {
            final double remainder = intPart % radix;
            sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int)remainder));
            intPart -= remainder;
            intPart /= radix;
        } while (intPart >= 1.0);
        if (negative) {
            sb.append('-');
        }
        sb.reverse();
        if (decPart > 0.0) {
            final int dot = sb.length();
            sb.append('.');
            do {
                decPart *= radix;
                final double d = Math.floor(decPart);
                sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int)d));
                decPart -= d;
            } while (decPart > 0.0 && sb.length() - dot < 1100);
        }
        return sb.toString();
    }
    
    public static double toNumber(final Object obj) {
        if (obj instanceof Double) {
            return (double)obj;
        }
        if (obj instanceof Number) {
            return ((Number)obj).doubleValue();
        }
        return toNumberGeneric(obj);
    }
    
    public static double toNumberForEq(final Object obj) {
        return (obj == null) ? Double.NaN : toNumber(obj);
    }
    
    public static double toNumberForStrictEq(final Object obj) {
        if (obj instanceof Double) {
            return (double)obj;
        }
        if (isNumber(obj)) {
            return ((Number)obj).doubleValue();
        }
        return Double.NaN;
    }
    
    public static Number toNarrowestNumber(final long l) {
        return isRepresentableAsInt(l) ? ((double)(int)l) : ((double)l);
    }
    
    public static double toNumber(final Boolean b) {
        return b ? 1.0 : 0.0;
    }
    
    public static double toNumber(final ScriptObject obj) {
        return toNumber(toPrimitive(obj, Number.class));
    }
    
    public static double toNumberOptimistic(final Object obj, final int programPoint) {
        if (obj != null) {
            final Class<?> clz = obj.getClass();
            if (clz == Double.class || clz == Integer.class || clz == Long.class) {
                return ((Number)obj).doubleValue();
            }
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }
    
    public static double toNumberMaybeOptimistic(final Object obj, final int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? toNumberOptimistic(obj, programPoint) : toNumber(obj);
    }
    
    public static int digit(final char ch, final int radix) {
        return digit(ch, radix, false);
    }
    
    public static int digit(final char ch, final int radix, final boolean onlyIsoLatin1) {
        final char maxInRadix = (char)(97 + (radix - 1) - 10);
        final char c = Character.toLowerCase(ch);
        if (c >= 'a' && c <= maxInRadix) {
            return Character.digit(ch, radix);
        }
        if (Character.isDigit(ch) && (!onlyIsoLatin1 || (ch >= '0' && ch <= '9'))) {
            return Character.digit(ch, radix);
        }
        return -1;
    }
    
    public static double toNumber(final String str) {
        int end = str.length();
        if (end == 0) {
            return 0.0;
        }
        int start;
        char f;
        for (start = 0, f = str.charAt(0); Lexer.isJSWhitespace(f); f = str.charAt(start)) {
            if (++start == end) {
                return 0.0;
            }
        }
        while (Lexer.isJSWhitespace(str.charAt(end - 1))) {
            --end;
        }
        boolean negative;
        if (f == '-') {
            if (++start == end) {
                return Double.NaN;
            }
            f = str.charAt(start);
            negative = true;
        }
        else {
            if (f == '+') {
                if (++start == end) {
                    return Double.NaN;
                }
                f = str.charAt(start);
            }
            negative = false;
        }
        double value;
        if (start + 1 < end && f == '0' && Character.toLowerCase(str.charAt(start + 1)) == 'x') {
            value = parseRadix(str.toCharArray(), start + 2, end, 16);
        }
        else {
            if (f == 'I' && end - start == 8 && str.regionMatches(start, "Infinity", 0, 8)) {
                return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
            for (int i = start; i < end; ++i) {
                f = str.charAt(i);
                if ((f < '0' || f > '9') && f != '.' && f != 'e' && f != 'E' && f != '+' && f != '-') {
                    return Double.NaN;
                }
            }
            try {
                value = Double.parseDouble(str.substring(start, end));
            }
            catch (NumberFormatException e) {
                return Double.NaN;
            }
        }
        return negative ? (-value) : value;
    }
    
    public static int toInteger(final Object obj) {
        return (int)toNumber(obj);
    }
    
    public static long toLong(final Object obj) {
        return (long)((obj instanceof Long) ? obj : toLong(toNumber(obj)));
    }
    
    public static long toLong(final double num) {
        return (long)num;
    }
    
    public static int toInt32(final Object obj) {
        return toInt32(toNumber(obj));
    }
    
    public static int toInt32Optimistic(final Object obj, final int programPoint) {
        if (obj != null && obj.getClass() == Integer.class) {
            return (int)obj;
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }
    
    public static int toInt32MaybeOptimistic(final Object obj, final int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? toInt32Optimistic(obj, programPoint) : toInt32(obj);
    }
    
    public static int toInt32(final long num) {
        return (int)((num >= -9007199254740992L && num <= 9007199254740992L) ? num : ((long)(num % 4.294967296E9)));
    }
    
    public static int toInt32(final double num) {
        return (int)doubleToInt32(num);
    }
    
    public static long toUint32(final Object obj) {
        return toUint32(toNumber(obj));
    }
    
    public static long toUint32(final double num) {
        return doubleToInt32(num) & 0xFFFFFFFFL;
    }
    
    public static long toUint32(final int num) {
        return (long)num & 0xFFFFFFFFL;
    }
    
    public static int toUint32Optimistic(final int num, final int pp) {
        if (num >= 0) {
            return num;
        }
        throw new UnwarrantedOptimismException(toUint32Double(num), pp, Type.NUMBER);
    }
    
    public static double toUint32Double(final int num) {
        return (double)toUint32(num);
    }
    
    public static int toUint16(final Object obj) {
        return toUint16(toNumber(obj));
    }
    
    public static int toUint16(final int num) {
        return num & 0xFFFF;
    }
    
    public static int toUint16(final long num) {
        return (int)num & 0xFFFF;
    }
    
    public static int toUint16(final double num) {
        return (int)doubleToInt32(num) & 0xFFFF;
    }
    
    private static long doubleToInt32(final double num) {
        final int exponent = Math.getExponent(num);
        if (exponent < 31) {
            return (long)num;
        }
        if (exponent >= 84) {
            return 0L;
        }
        final double d = (num >= 0.0) ? Math.floor(num) : Math.ceil(num);
        return (long)(d % 4.294967296E9);
    }
    
    public static boolean isFinite(final double num) {
        return !Double.isInfinite(num) && !Double.isNaN(num);
    }
    
    public static Double toDouble(final double num) {
        return num;
    }
    
    public static Double toDouble(final long num) {
        return Double.valueOf(num);
    }
    
    public static Double toDouble(final int num) {
        return (double)num;
    }
    
    public static Object toObject(final boolean bool) {
        return bool;
    }
    
    public static Object toObject(final int num) {
        return num;
    }
    
    public static Object toObject(final long num) {
        return num;
    }
    
    public static Object toObject(final double num) {
        return num;
    }
    
    public static Object toObject(final Object obj) {
        return obj;
    }
    
    public static Object toScriptObject(final Object obj) {
        return toScriptObject(Context.getGlobal(), obj);
    }
    
    public static Object toScriptObject(final Global global, final Object obj) {
        if (nullOrUndefined(obj)) {
            throw ECMAErrors.typeError(global, "not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (obj instanceof ScriptObject) {
            return obj;
        }
        return global.wrapAsObject(obj);
    }
    
    public static Object toJavaArray(final Object obj, final Class<?> componentType) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getArray().asArrayOfType(componentType);
        }
        if (obj instanceof JSObject) {
            final ArrayLikeIterator<?> itr = ArrayLikeIterator.arrayLikeIterator(obj);
            final int len = (int)itr.getLength();
            final Object[] res = new Object[len];
            int idx = 0;
            while (itr.hasNext()) {
                res[idx++] = itr.next();
            }
            return convertArray(res, componentType);
        }
        if (obj == null) {
            return null;
        }
        throw new IllegalArgumentException("not a script object");
    }
    
    public static Object convertArray(final Object[] src, final Class<?> componentType) {
        if (componentType == Object.class) {
            for (int i = 0; i < src.length; ++i) {
                final Object e = src[i];
                if (e instanceof ConsString) {
                    src[i] = e.toString();
                }
            }
        }
        final int l = src.length;
        final Object dst = Array.newInstance(componentType, l);
        final MethodHandle converter = Bootstrap.getLinkerServices().getTypeConverter(Object.class, componentType);
        try {
            for (int j = 0; j < src.length; ++j) {
                Array.set(dst, j, invoke(converter, src[j]));
            }
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e2 = t2;
            throw e2;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return dst;
    }
    
    public static boolean nullOrUndefined(final Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED;
    }
    
    static String toStringImpl(final Object obj, final boolean safe) {
        if (obj instanceof String) {
            return (String)obj;
        }
        if (obj instanceof ConsString) {
            return obj.toString();
        }
        if (isNumber(obj)) {
            return toString(((Number)obj).doubleValue());
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return "undefined";
        }
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        if (safe && obj instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)obj;
            final Global gobj = Context.getGlobal();
            return gobj.isError(sobj) ? ECMAException.safeToString(sobj) : sobj.safeToString();
        }
        return toString(toPrimitive(obj, String.class));
    }
    
    static String trimLeft(final String str) {
        int start;
        for (start = 0; start < str.length() && Lexer.isJSWhitespace(str.charAt(start)); ++start) {}
        return str.substring(start);
    }
    
    private static Object throwUnwarrantedOptimismException(final Object value, final int programPoint) {
        throw new UnwarrantedOptimismException(value, programPoint);
    }
    
    public static int addExact(final int x, final int y, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.addExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(x + (double)y, programPoint);
        }
    }
    
    public static int subExact(final int x, final int y, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.subtractExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(x - (double)y, programPoint);
        }
    }
    
    public static int mulExact(final int x, final int y, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.multiplyExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(x * (double)y, programPoint);
        }
    }
    
    public static int divExact(final int x, final int y, final int programPoint) throws UnwarrantedOptimismException {
        int res;
        try {
            res = x / y;
        }
        catch (ArithmeticException e) {
            assert y == 0;
            throw new UnwarrantedOptimismException((x > 0) ? Double.POSITIVE_INFINITY : ((x < 0) ? Double.NEGATIVE_INFINITY : Double.NaN), programPoint);
        }
        final int rem = x % y;
        if (rem == 0) {
            return res;
        }
        throw new UnwarrantedOptimismException(x / (double)y, programPoint);
    }
    
    public static int divZero(final int x, final int y) {
        return (y == 0) ? 0 : (x / y);
    }
    
    public static int remZero(final int x, final int y) {
        return (y == 0) ? 0 : (x % y);
    }
    
    public static int remExact(final int x, final int y, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return x % y;
        }
        catch (ArithmeticException e) {
            assert y == 0;
            throw new UnwarrantedOptimismException(Double.NaN, programPoint);
        }
    }
    
    public static int decrementExact(final int x, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.decrementExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(x - 1.0, programPoint);
        }
    }
    
    public static int incrementExact(final int x, final int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.incrementExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(x + 1.0, programPoint);
        }
    }
    
    public static int negateExact(final int x, final int programPoint) throws UnwarrantedOptimismException {
        try {
            if (x == 0) {
                throw new UnwarrantedOptimismException(-0.0, programPoint);
            }
            return Math.negateExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(-x, programPoint);
        }
    }
    
    public static int getAccessorTypeIndex(final Type type) {
        return getAccessorTypeIndex(type.getTypeClass());
    }
    
    public static int getAccessorTypeIndex(final Class<?> type) {
        if (type == null) {
            return -1;
        }
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Double.TYPE) {
            return 1;
        }
        if (!type.isPrimitive()) {
            return 2;
        }
        return -1;
    }
    
    public static Type getAccessorType(final int index) {
        return JSType.ACCESSOR_TYPES.get(index);
    }
    
    public static int getNumberOfAccessorTypes() {
        return JSType.ACCESSOR_TYPES.size();
    }
    
    private static double parseRadix(final char[] chars, final int start, final int length, final int radix) {
        int pos = 0;
        for (int i = start; i < length; ++i) {
            if (digit(chars[i], radix) == -1) {
                return Double.NaN;
            }
            ++pos;
        }
        if (pos == 0) {
            return Double.NaN;
        }
        double value = 0.0;
        for (int j = start; j < start + pos; ++j) {
            value *= radix;
            value += digit(chars[j], radix);
        }
        return value;
    }
    
    private static double toNumberGeneric(final Object obj) {
        if (obj == null) {
            return 0.0;
        }
        if (obj instanceof String) {
            return toNumber((String)obj);
        }
        if (obj instanceof ConsString) {
            return toNumber(obj.toString());
        }
        if (obj instanceof Boolean) {
            return toNumber((Boolean)obj);
        }
        if (obj instanceof ScriptObject) {
            return toNumber((ScriptObject)obj);
        }
        if (obj instanceof Undefined) {
            return Double.NaN;
        }
        return toNumber(toPrimitive(obj, Number.class));
    }
    
    private static Object invoke(final MethodHandle mh, final Object arg) {
        try {
            return mh.invoke(arg);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    public static MethodHandle unboxConstant(final Object o) {
        if (o != null) {
            if (o.getClass() == Integer.class) {
                return Lookup.MH.constant(Integer.TYPE, (int)o);
            }
            if (o.getClass() == Double.class) {
                return Lookup.MH.constant(Double.TYPE, (double)o);
            }
        }
        return Lookup.MH.constant(Object.class, o);
    }
    
    public static Class<?> unboxedFieldType(final Object o) {
        if (o == null) {
            return Object.class;
        }
        if (o.getClass() == Integer.class) {
            return Integer.TYPE;
        }
        if (o.getClass() == Double.class) {
            return Double.TYPE;
        }
        return Object.class;
    }
    
    private static final List<MethodHandle> toUnmodifiableList(final MethodHandle... methodHandles) {
        return Collections.unmodifiableList((List<? extends MethodHandle>)Arrays.asList((T[])methodHandles));
    }
    
    static {
        JSTYPE_LOOKUP = MethodHandles.lookup();
        TO_BOOLEAN = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Object.class);
        TO_BOOLEAN_D = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Double.TYPE);
        TO_INTEGER = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toInteger", Integer.TYPE, Object.class);
        TO_LONG = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Object.class);
        TO_LONG_D = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Double.TYPE);
        TO_NUMBER = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toNumber", Double.TYPE, Object.class);
        TO_NUMBER_OPTIMISTIC = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toNumberOptimistic", Double.TYPE, Object.class, Integer.TYPE);
        TO_STRING = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toString", String.class, Object.class);
        TO_INT32 = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Object.class);
        TO_INT32_L = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Long.TYPE);
        TO_INT32_OPTIMISTIC = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toInt32Optimistic", Integer.TYPE, Object.class, Integer.TYPE);
        TO_INT32_D = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Double.TYPE);
        TO_UINT32_OPTIMISTIC = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toUint32Optimistic", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_UINT32_DOUBLE = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toUint32Double", Double.TYPE, Integer.TYPE);
        TO_UINT32 = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Object.class);
        TO_UINT32_D = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Double.TYPE);
        TO_STRING_D = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toString", String.class, Double.TYPE);
        TO_PRIMITIVE_TO_STRING = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toPrimitiveToString", String.class, Object.class);
        TO_PRIMITIVE_TO_CHARSEQUENCE = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toPrimitiveToCharSequence", CharSequence.class, Object.class);
        THROW_UNWARRANTED = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "throwUnwarrantedOptimismException", Object.class, Object.class, Integer.TYPE);
        ADD_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "addExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        SUB_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "subExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        MUL_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "mulExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "divExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_ZERO = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "divZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_ZERO = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "remZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "remExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DECREMENT_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "decrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        INCREMENT_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "incrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        NEGATE_EXACT = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "negateExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_JAVA_ARRAY = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "toJavaArray", Object.class, Object.class, Class.class);
        VOID_RETURN = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "voidReturn", Void.TYPE, (Class<?>[])new Class[0]);
        IS_STRING = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "isString", Boolean.TYPE, Object.class);
        IS_NUMBER = CompilerConstants.staticCall(JSType.JSTYPE_LOOKUP, JSType.class, "isNumber", Boolean.TYPE, Object.class);
        ACCESSOR_TYPES = Collections.unmodifiableList((List<? extends Type>)Arrays.asList(Type.INT, Type.NUMBER, Type.OBJECT));
        CONVERT_OBJECT = toUnmodifiableList(JSType.TO_INT32.methodHandle(), JSType.TO_NUMBER.methodHandle(), null);
        CONVERT_OBJECT_OPTIMISTIC = toUnmodifiableList(JSType.TO_INT32_OPTIMISTIC.methodHandle(), JSType.TO_NUMBER_OPTIMISTIC.methodHandle(), null);
        GET_UNDEFINED = toUnmodifiableList(Lookup.MH.constant(Integer.TYPE, 0), Lookup.MH.constant(Double.TYPE, Double.NaN), Lookup.MH.constant(Object.class, Undefined.getUndefined()));
    }
}
