// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.codegen.ApplySpecialization;
import jdk.internal.dynalink.beans.StaticClass;
import java.util.Locale;
import java.util.Objects;
import jdk.nashorn.internal.objects.NativeObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import java.util.NoSuchElementException;
import java.util.Collections;
import jdk.nashorn.internal.objects.Global;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Array;
import java.util.Iterator;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.codegen.CompilerConstants;

public final class ScriptRuntime
{
    public static final Object[] EMPTY_ARRAY;
    public static final Undefined UNDEFINED;
    public static final Undefined EMPTY;
    public static final CompilerConstants.Call ADD;
    public static final CompilerConstants.Call EQ_STRICT;
    public static final CompilerConstants.Call OPEN_WITH;
    public static final CompilerConstants.Call MERGE_SCOPE;
    public static final CompilerConstants.Call TO_PROPERTY_ITERATOR;
    public static final CompilerConstants.Call TO_VALUE_ITERATOR;
    public static final CompilerConstants.Call APPLY;
    public static final CompilerConstants.Call THROW_REFERENCE_ERROR;
    public static final CompilerConstants.Call THROW_CONST_TYPE_ERROR;
    public static final CompilerConstants.Call INVALIDATE_RESERVED_BUILTIN_NAME;
    
    private ScriptRuntime() {
    }
    
    public static int switchTagAsInt(final Object tag, final int deflt) {
        if (tag instanceof Number) {
            final double d = ((Number)tag).doubleValue();
            if (JSType.isRepresentableAsInt(d)) {
                return (int)d;
            }
        }
        return deflt;
    }
    
    public static int switchTagAsInt(final boolean tag, final int deflt) {
        return deflt;
    }
    
    public static int switchTagAsInt(final long tag, final int deflt) {
        return JSType.isRepresentableAsInt(tag) ? ((int)tag) : deflt;
    }
    
    public static int switchTagAsInt(final double tag, final int deflt) {
        return JSType.isRepresentableAsInt(tag) ? ((int)tag) : deflt;
    }
    
    public static String builtinObjectToString(final Object self) {
        final JSType type = JSType.ofNoFunction(self);
        String className = null;
        switch (type) {
            case BOOLEAN: {
                className = "Boolean";
                break;
            }
            case NUMBER: {
                className = "Number";
                break;
            }
            case STRING: {
                className = "String";
                break;
            }
            case NULL: {
                className = "Null";
                break;
            }
            case UNDEFINED: {
                className = "Undefined";
                break;
            }
            case OBJECT: {
                if (self instanceof ScriptObject) {
                    className = ((ScriptObject)self).getClassName();
                    break;
                }
                if (self instanceof JSObject) {
                    className = ((JSObject)self).getClassName();
                    break;
                }
                className = self.getClass().getName();
                break;
            }
            default: {
                className = self.getClass().getName();
                break;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("[object ");
        sb.append(className);
        sb.append(']');
        return sb.toString();
    }
    
    public static String safeToString(final Object obj) {
        return JSType.toStringImpl(obj, true);
    }
    
    public static Iterator<?> toPropertyIterator(final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).propertyIterator();
        }
        if (obj != null && obj.getClass().isArray()) {
            return new RangeIterator(Array.getLength(obj));
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).keySet().iterator();
        }
        if (obj instanceof List) {
            return new RangeIterator(((List)obj).size());
        }
        if (obj instanceof Map) {
            return ((Map)obj).keySet().iterator();
        }
        final Object wrapped = Global.instance().wrapAsObject(obj);
        if (wrapped instanceof ScriptObject) {
            return ((ScriptObject)wrapped).propertyIterator();
        }
        return Collections.emptyIterator();
    }
    
    public static Iterator<?> toValueIterator(final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).valueIterator();
        }
        if (obj != null && obj.getClass().isArray()) {
            final Object array = obj;
            final int length = Array.getLength(obj);
            return new Iterator<Object>() {
                private int index = 0;
                
                @Override
                public boolean hasNext() {
                    return this.index < length;
                }
                
                @Override
                public Object next() {
                    if (this.index >= length) {
                        throw new NoSuchElementException();
                    }
                    return Array.get(array, this.index++);
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove");
                }
            };
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).values().iterator();
        }
        if (obj instanceof Map) {
            return ((Map)obj).values().iterator();
        }
        if (obj instanceof Iterable) {
            return ((Iterable)obj).iterator();
        }
        final Object wrapped = Global.instance().wrapAsObject(obj);
        if (wrapped instanceof ScriptObject) {
            return ((ScriptObject)wrapped).valueIterator();
        }
        return Collections.emptyIterator();
    }
    
    public static ScriptObject mergeScope(final ScriptObject scope) {
        final ScriptObject parentScope = scope.getProto();
        parentScope.addBoundProperties(scope);
        return parentScope;
    }
    
    public static Object apply(final ScriptFunction target, final Object self, final Object... args) {
        try {
            return target.invoke(self, args);
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
    
    public static void throwReferenceError(final String name) {
        throw ECMAErrors.referenceError("not.defined", name);
    }
    
    public static void throwConstTypeError(final String name) {
        throw ECMAErrors.typeError("assign.constant", name);
    }
    
    public static Object construct(final ScriptFunction target, final Object... args) {
        try {
            return target.construct(args);
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
    
    public static boolean sameValue(final Object x, final Object y) {
        final JSType xType = JSType.ofNoFunction(x);
        final JSType yType = JSType.ofNoFunction(y);
        if (xType != yType) {
            return false;
        }
        if (xType == JSType.UNDEFINED || xType == JSType.NULL) {
            return true;
        }
        if (xType == JSType.NUMBER) {
            final double xVal = ((Number)x).doubleValue();
            final double yVal = ((Number)y).doubleValue();
            return (Double.isNaN(xVal) && Double.isNaN(yVal)) || ((xVal != 0.0 || Double.doubleToLongBits(xVal) == Double.doubleToLongBits(yVal)) && xVal == yVal);
        }
        if (xType == JSType.STRING || yType == JSType.BOOLEAN) {
            return x.equals(y);
        }
        return x == y;
    }
    
    public static String parse(final String code, final String name, final boolean includeLoc) {
        return JSONWriter.parse(Context.getContextTrusted(), code, name, includeLoc);
    }
    
    public static boolean isJSWhitespace(final char ch) {
        return Lexer.isJSWhitespace(ch);
    }
    
    public static ScriptObject openWith(final ScriptObject scope, final Object expression) {
        final Global global = Context.getGlobal();
        if (expression == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError(global, "cant.apply.with.to.undefined", new String[0]);
        }
        if (expression == null) {
            throw ECMAErrors.typeError(global, "cant.apply.with.to.null", new String[0]);
        }
        if (expression instanceof ScriptObjectMirror) {
            final Object unwrapped = ScriptObjectMirror.unwrap(expression, global);
            if (unwrapped instanceof ScriptObject) {
                return new WithObject(scope, (ScriptObject)unwrapped);
            }
            final ScriptObject exprObj = global.newObject();
            NativeObject.bindAllProperties(exprObj, (ScriptObjectMirror)expression);
            return new WithObject(scope, exprObj);
        }
        else {
            final Object wrappedExpr = JSType.toScriptObject(global, expression);
            if (wrappedExpr instanceof ScriptObject) {
                return new WithObject(scope, (ScriptObject)wrappedExpr);
            }
            throw ECMAErrors.typeError(global, "cant.apply.with.to.non.scriptobject", new String[0]);
        }
    }
    
    public static Object ADD(final Object x, final Object y) {
        final boolean xIsNumber = x instanceof Number;
        final boolean yIsNumber = y instanceof Number;
        if (xIsNumber && yIsNumber) {
            return ((Number)x).doubleValue() + ((Number)y).doubleValue();
        }
        final boolean xIsUndefined = x == ScriptRuntime.UNDEFINED;
        final boolean yIsUndefined = y == ScriptRuntime.UNDEFINED;
        if ((xIsNumber && yIsUndefined) || (xIsUndefined && yIsNumber) || (xIsUndefined && yIsUndefined)) {
            return Double.NaN;
        }
        final Object xPrim = JSType.toPrimitive(x);
        final Object yPrim = JSType.toPrimitive(y);
        if (!JSType.isString(xPrim)) {
            if (!JSType.isString(yPrim)) {
                return JSType.toNumber(xPrim) + JSType.toNumber(yPrim);
            }
        }
        try {
            return new ConsString(JSType.toCharSequence(xPrim), JSType.toCharSequence(yPrim));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "concat.string.too.big", new String[0]);
        }
        return JSType.toNumber(xPrim) + JSType.toNumber(yPrim);
    }
    
    public static Object DEBUGGER() {
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object NEW(final Object clazz, final Object... args) {
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object TYPEOF(final Object object, final Object property) {
        Object obj = object;
        if (property != null) {
            if (obj instanceof ScriptObject) {
                obj = ((ScriptObject)obj).get(property);
                if (Global.isLocationPropertyPlaceholder(obj)) {
                    if (CompilerConstants.__LINE__.name().equals(property)) {
                        obj = 0;
                    }
                    else {
                        obj = "";
                    }
                }
            }
            else if (object instanceof Undefined) {
                obj = ((Undefined)obj).get(property);
            }
            else {
                if (object == null) {
                    throw ECMAErrors.typeError("cant.get.property", safeToString(property), "null");
                }
                if (JSType.isPrimitive(obj)) {
                    obj = ((ScriptObject)JSType.toScriptObject(obj)).get(property);
                }
                else if (obj instanceof JSObject) {
                    obj = ((JSObject)obj).getMember(property.toString());
                }
                else {
                    obj = ScriptRuntime.UNDEFINED;
                }
            }
        }
        return JSType.of(obj).typeName();
    }
    
    public static Object REFERENCE_ERROR(final Object lhs, final Object rhs, final Object msg) {
        throw ECMAErrors.referenceError("cant.be.used.as.lhs", Objects.toString(msg));
    }
    
    public static boolean DELETE(final Object obj, final Object property, final Object strict) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).delete(property, Boolean.TRUE.equals(strict));
        }
        if (obj instanceof Undefined) {
            return ((Undefined)obj).delete(property, false);
        }
        if (obj == null) {
            throw ECMAErrors.typeError("cant.delete.property", safeToString(property), "null");
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).delete(property);
        }
        if (JSType.isPrimitive(obj)) {
            return ((ScriptObject)JSType.toScriptObject(obj)).delete(property, Boolean.TRUE.equals(strict));
        }
        if (obj instanceof JSObject) {
            ((JSObject)obj).removeMember(Objects.toString(property));
            return true;
        }
        return true;
    }
    
    public static boolean SLOW_DELETE(final Object obj, final Object property, final Object strict) {
        if (obj instanceof ScriptObject) {
            ScriptObject sobj = (ScriptObject)obj;
            final String key = property.toString();
            while (sobj != null && sobj.isScope()) {
                final FindProperty find = sobj.findProperty(key, false);
                if (find != null) {
                    return sobj.delete(key, Boolean.TRUE.equals(strict));
                }
                sobj = sobj.getProto();
            }
        }
        return DELETE(obj, property, strict);
    }
    
    public static boolean FAIL_DELETE(final Object property, final Object strict) {
        if (Boolean.TRUE.equals(strict)) {
            throw ECMAErrors.syntaxError("strict.cant.delete", safeToString(property));
        }
        return false;
    }
    
    public static boolean EQ(final Object x, final Object y) {
        return equals(x, y);
    }
    
    public static boolean NE(final Object x, final Object y) {
        return !EQ(x, y);
    }
    
    private static boolean equals(final Object x, final Object y) {
        if (x == y) {
            return true;
        }
        if (x instanceof ScriptObject && y instanceof ScriptObject) {
            return false;
        }
        if (x instanceof ScriptObjectMirror || y instanceof ScriptObjectMirror) {
            return ScriptObjectMirror.identical(x, y);
        }
        return equalValues(x, y);
    }
    
    private static boolean equalValues(final Object x, final Object y) {
        final JSType xType = JSType.ofNoFunction(x);
        final JSType yType = JSType.ofNoFunction(y);
        if (xType == yType) {
            return equalSameTypeValues(x, y, xType);
        }
        return equalDifferentTypeValues(x, y, xType, yType);
    }
    
    private static boolean equalSameTypeValues(final Object x, final Object y, final JSType type) {
        if (type == JSType.UNDEFINED || type == JSType.NULL) {
            return true;
        }
        if (type == JSType.NUMBER) {
            return ((Number)x).doubleValue() == ((Number)y).doubleValue();
        }
        if (type == JSType.STRING) {
            return x.toString().equals(y.toString());
        }
        if (type == JSType.BOOLEAN) {
            return (boolean)x == (boolean)y;
        }
        return x == y;
    }
    
    private static boolean equalDifferentTypeValues(final Object x, final Object y, final JSType xType, final JSType yType) {
        if (isUndefinedAndNull(xType, yType) || isUndefinedAndNull(yType, xType)) {
            return true;
        }
        if (isNumberAndString(xType, yType)) {
            return equalNumberToString(x, y);
        }
        if (isNumberAndString(yType, xType)) {
            return equalNumberToString(y, x);
        }
        if (xType == JSType.BOOLEAN) {
            return equalBooleanToAny(x, y);
        }
        if (yType == JSType.BOOLEAN) {
            return equalBooleanToAny(y, x);
        }
        if (isNumberOrStringAndObject(xType, yType)) {
            return equalNumberOrStringToObject(x, y);
        }
        return isNumberOrStringAndObject(yType, xType) && equalNumberOrStringToObject(y, x);
    }
    
    private static boolean isUndefinedAndNull(final JSType xType, final JSType yType) {
        return xType == JSType.UNDEFINED && yType == JSType.NULL;
    }
    
    private static boolean isNumberAndString(final JSType xType, final JSType yType) {
        return xType == JSType.NUMBER && yType == JSType.STRING;
    }
    
    private static boolean isNumberOrStringAndObject(final JSType xType, final JSType yType) {
        return (xType == JSType.NUMBER || xType == JSType.STRING) && yType == JSType.OBJECT;
    }
    
    private static boolean equalNumberToString(final Object num, final Object str) {
        return ((Number)num).doubleValue() == JSType.toNumber(str.toString());
    }
    
    private static boolean equalBooleanToAny(final Object bool, final Object any) {
        return equals(JSType.toNumber((Boolean)bool), any);
    }
    
    private static boolean equalNumberOrStringToObject(final Object numOrStr, final Object any) {
        return equals(numOrStr, JSType.toPrimitive(any));
    }
    
    public static boolean EQ_STRICT(final Object x, final Object y) {
        return strictEquals(x, y);
    }
    
    public static boolean NE_STRICT(final Object x, final Object y) {
        return !EQ_STRICT(x, y);
    }
    
    private static boolean strictEquals(final Object x, final Object y) {
        final JSType xType = JSType.ofNoFunction(x);
        final JSType yType = JSType.ofNoFunction(y);
        return xType == yType && equalSameTypeValues(x, y, xType);
    }
    
    public static boolean IN(final Object property, final Object obj) {
        final JSType rvalType = JSType.ofNoFunction(obj);
        if (rvalType != JSType.OBJECT) {
            throw ECMAErrors.typeError("in.with.non.object", rvalType.toString().toLowerCase(Locale.ENGLISH));
        }
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).has(property);
        }
        return obj instanceof JSObject && ((JSObject)obj).hasMember(Objects.toString(property));
    }
    
    public static boolean INSTANCEOF(final Object obj, final Object clazz) {
        if (clazz instanceof ScriptFunction) {
            return obj instanceof ScriptObject && ((ScriptObject)clazz).isInstance((ScriptObject)obj);
        }
        if (clazz instanceof StaticClass) {
            return ((StaticClass)clazz).getRepresentedClass().isInstance(obj);
        }
        if (clazz instanceof JSObject) {
            return ((JSObject)clazz).isInstance(obj);
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).isInstanceOf(clazz);
        }
        throw ECMAErrors.typeError("instanceof.on.non.object", new String[0]);
    }
    
    public static boolean LT(final Object x, final Object y) {
        final Object px = JSType.toPrimitive(x, Number.class);
        final Object py = JSType.toPrimitive(y, Number.class);
        return areBothString(px, py) ? (px.toString().compareTo(py.toString()) < 0) : (JSType.toNumber(px) < JSType.toNumber(py));
    }
    
    private static boolean areBothString(final Object x, final Object y) {
        return JSType.isString(x) && JSType.isString(y);
    }
    
    public static boolean GT(final Object x, final Object y) {
        final Object px = JSType.toPrimitive(x, Number.class);
        final Object py = JSType.toPrimitive(y, Number.class);
        return areBothString(px, py) ? (px.toString().compareTo(py.toString()) > 0) : (JSType.toNumber(px) > JSType.toNumber(py));
    }
    
    public static boolean LE(final Object x, final Object y) {
        final Object px = JSType.toPrimitive(x, Number.class);
        final Object py = JSType.toPrimitive(y, Number.class);
        return areBothString(px, py) ? (px.toString().compareTo(py.toString()) <= 0) : (JSType.toNumber(px) <= JSType.toNumber(py));
    }
    
    public static boolean GE(final Object x, final Object y) {
        final Object px = JSType.toPrimitive(x, Number.class);
        final Object py = JSType.toPrimitive(y, Number.class);
        return areBothString(px, py) ? (px.toString().compareTo(py.toString()) >= 0) : (JSType.toNumber(px) >= JSType.toNumber(py));
    }
    
    public static void invalidateReservedBuiltinName(final String name) {
        final Context context = Context.getContextTrusted();
        final SwitchPoint sp = context.getBuiltinSwitchPoint(name);
        assert sp != null;
        context.getLogger(ApplySpecialization.class).info("Overwrote special name '" + name + "' - invalidating switchpoint");
        SwitchPoint.invalidateAll(new SwitchPoint[] { sp });
    }
    
    static {
        EMPTY_ARRAY = new Object[0];
        UNDEFINED = Undefined.getUndefined();
        EMPTY = Undefined.getEmpty();
        ADD = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "ADD", Object.class, Object.class, Object.class);
        EQ_STRICT = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "EQ_STRICT", Boolean.TYPE, Object.class, Object.class);
        OPEN_WITH = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "openWith", ScriptObject.class, ScriptObject.class, Object.class);
        MERGE_SCOPE = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "mergeScope", ScriptObject.class, ScriptObject.class);
        TO_PROPERTY_ITERATOR = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "toPropertyIterator", Iterator.class, Object.class);
        TO_VALUE_ITERATOR = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "toValueIterator", Iterator.class, Object.class);
        APPLY = CompilerConstants.staticCall(MethodHandles.lookup(), ScriptRuntime.class, "apply", Object.class, ScriptFunction.class, Object.class, Object[].class);
        THROW_REFERENCE_ERROR = CompilerConstants.staticCall(MethodHandles.lookup(), ScriptRuntime.class, "throwReferenceError", Void.TYPE, String.class);
        THROW_CONST_TYPE_ERROR = CompilerConstants.staticCall(MethodHandles.lookup(), ScriptRuntime.class, "throwConstTypeError", Void.TYPE, String.class);
        INVALIDATE_RESERVED_BUILTIN_NAME = CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "invalidateReservedBuiltinName", Void.TYPE, String.class);
    }
    
    private static final class RangeIterator implements Iterator<Integer>
    {
        private final int length;
        private int index;
        
        RangeIterator(final int length) {
            this.length = length;
        }
        
        @Override
        public boolean hasNext() {
            return this.index < this.length;
        }
        
        @Override
        public Integer next() {
            return this.index++;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
