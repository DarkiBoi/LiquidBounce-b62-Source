// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import jdk.nashorn.internal.runtime.NativeJavaPackage;
import jdk.nashorn.internal.runtime.Specialization;
import jdk.nashorn.internal.runtime.GlobalFunctions;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.GlobalConstants;
import jdk.nashorn.tools.ShellFunctions;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.Property;
import java.util.Arrays;
import java.io.IOException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.scripts.JO;
import jdk.nashorn.internal.scripts.JD;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.security.Permission;
import java.util.Map;
import java.lang.invoke.SwitchPoint;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.Scope;

public final class Global extends Scope
{
    private static final Object LAZY_SENTINEL;
    private static final Object LOCATION_PLACEHOLDER;
    private final InvokeByName TO_STRING;
    private final InvokeByName VALUE_OF;
    public Object arguments;
    public Object parseInt;
    public Object parseFloat;
    public Object isNaN;
    public Object isFinite;
    public Object encodeURI;
    public Object encodeURIComponent;
    public Object decodeURI;
    public Object decodeURIComponent;
    public Object escape;
    public Object unescape;
    public Object print;
    public Object load;
    public Object loadWithNewGlobal;
    public Object exit;
    public Object quit;
    public static final double NaN = Double.NaN;
    public static final double Infinity = Double.POSITIVE_INFINITY;
    public static final Object undefined;
    public Object eval;
    public volatile Object object;
    public volatile Object function;
    public volatile Object array;
    public volatile Object string;
    public volatile Object _boolean;
    public volatile Object number;
    private volatile Object date;
    private volatile Object regexp;
    private volatile Object json;
    private volatile Object jsadapter;
    public volatile Object math;
    public volatile Object error;
    private volatile Object evalError;
    private volatile Object rangeError;
    public volatile Object referenceError;
    public volatile Object syntaxError;
    public volatile Object typeError;
    private volatile Object uriError;
    private volatile Object arrayBuffer;
    private volatile Object dataView;
    private volatile Object int8Array;
    private volatile Object uint8Array;
    private volatile Object uint8ClampedArray;
    private volatile Object int16Array;
    private volatile Object uint16Array;
    private volatile Object int32Array;
    private volatile Object uint32Array;
    private volatile Object float32Array;
    private volatile Object float64Array;
    public volatile Object packages;
    public volatile Object com;
    public volatile Object edu;
    public volatile Object java;
    public volatile Object javafx;
    public volatile Object javax;
    public volatile Object org;
    private volatile Object javaImporter;
    private volatile Object javaApi;
    public static final Object __FILE__;
    public static final Object __DIR__;
    public static final Object __LINE__;
    private volatile NativeDate DEFAULT_DATE;
    private volatile NativeRegExp DEFAULT_REGEXP;
    private ScriptFunction builtinFunction;
    private ScriptFunction builtinObject;
    private ScriptFunction builtinArray;
    private ScriptFunction builtinBoolean;
    private ScriptFunction builtinDate;
    private ScriptObject builtinJSON;
    private ScriptFunction builtinJSAdapter;
    private ScriptObject builtinMath;
    private ScriptFunction builtinNumber;
    private ScriptFunction builtinRegExp;
    private ScriptFunction builtinString;
    private ScriptFunction builtinError;
    private ScriptFunction builtinEval;
    private ScriptFunction builtinEvalError;
    private ScriptFunction builtinRangeError;
    private ScriptFunction builtinReferenceError;
    private ScriptFunction builtinSyntaxError;
    private ScriptFunction builtinTypeError;
    private ScriptFunction builtinURIError;
    private ScriptObject builtinPackages;
    private ScriptObject builtinCom;
    private ScriptObject builtinEdu;
    private ScriptObject builtinJava;
    private ScriptObject builtinJavafx;
    private ScriptObject builtinJavax;
    private ScriptObject builtinOrg;
    private ScriptFunction builtinJavaImporter;
    private ScriptObject builtinJavaApi;
    private ScriptFunction builtinArrayBuffer;
    private ScriptFunction builtinDataView;
    private ScriptFunction builtinInt8Array;
    private ScriptFunction builtinUint8Array;
    private ScriptFunction builtinUint8ClampedArray;
    private ScriptFunction builtinInt16Array;
    private ScriptFunction builtinUint16Array;
    private ScriptFunction builtinInt32Array;
    private ScriptFunction builtinUint32Array;
    private ScriptFunction builtinFloat32Array;
    private ScriptFunction builtinFloat64Array;
    private ScriptFunction typeErrorThrower;
    private RegExpResult lastRegExpResult;
    private static final MethodHandle EVAL;
    private static final MethodHandle NO_SUCH_PROPERTY;
    private static final MethodHandle PRINT;
    private static final MethodHandle PRINTLN;
    private static final MethodHandle LOAD;
    private static final MethodHandle LOAD_WITH_NEW_GLOBAL;
    private static final MethodHandle EXIT;
    private static final MethodHandle LEXICAL_SCOPE_FILTER;
    private static PropertyMap $nasgenmap$;
    private final Context context;
    private ThreadLocal<ScriptContext> scontext;
    private ScriptEngine engine;
    private volatile ScriptContext initscontext;
    private final LexicalScope lexicalScope;
    private SwitchPoint lexicalScopeSwitchPoint;
    private final Map<Object, InvokeByName> namedInvokers;
    private final Map<Object, MethodHandle> dynamicInvokers;
    
    public static Object getDate(final Object self) {
        final Global global = instanceFrom(self);
        if (global.date == Global.LAZY_SENTINEL) {
            global.date = global.getBuiltinDate();
        }
        return global.date;
    }
    
    public static void setDate(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.date = value;
    }
    
    public static Object getRegExp(final Object self) {
        final Global global = instanceFrom(self);
        if (global.regexp == Global.LAZY_SENTINEL) {
            global.regexp = global.getBuiltinRegExp();
        }
        return global.regexp;
    }
    
    public static void setRegExp(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.regexp = value;
    }
    
    public static Object getJSON(final Object self) {
        final Global global = instanceFrom(self);
        if (global.json == Global.LAZY_SENTINEL) {
            global.json = global.getBuiltinJSON();
        }
        return global.json;
    }
    
    public static void setJSON(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.json = value;
    }
    
    public static Object getJSAdapter(final Object self) {
        final Global global = instanceFrom(self);
        if (global.jsadapter == Global.LAZY_SENTINEL) {
            global.jsadapter = global.getBuiltinJSAdapter();
        }
        return global.jsadapter;
    }
    
    public static void setJSAdapter(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.jsadapter = value;
    }
    
    public static Object getEvalError(final Object self) {
        final Global global = instanceFrom(self);
        if (global.evalError == Global.LAZY_SENTINEL) {
            global.evalError = global.getBuiltinEvalError();
        }
        return global.evalError;
    }
    
    public static void setEvalError(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.evalError = value;
    }
    
    public static Object getRangeError(final Object self) {
        final Global global = instanceFrom(self);
        if (global.rangeError == Global.LAZY_SENTINEL) {
            global.rangeError = global.getBuiltinRangeError();
        }
        return global.rangeError;
    }
    
    public static void setRangeError(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.rangeError = value;
    }
    
    public static Object getURIError(final Object self) {
        final Global global = instanceFrom(self);
        if (global.uriError == Global.LAZY_SENTINEL) {
            global.uriError = global.getBuiltinURIError();
        }
        return global.uriError;
    }
    
    public static void setURIError(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.uriError = value;
    }
    
    public static Object getArrayBuffer(final Object self) {
        final Global global = instanceFrom(self);
        if (global.arrayBuffer == Global.LAZY_SENTINEL) {
            global.arrayBuffer = global.getBuiltinArrayBuffer();
        }
        return global.arrayBuffer;
    }
    
    public static void setArrayBuffer(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.arrayBuffer = value;
    }
    
    public static Object getDataView(final Object self) {
        final Global global = instanceFrom(self);
        if (global.dataView == Global.LAZY_SENTINEL) {
            global.dataView = global.getBuiltinDataView();
        }
        return global.dataView;
    }
    
    public static void setDataView(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.dataView = value;
    }
    
    public static Object getInt8Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.int8Array == Global.LAZY_SENTINEL) {
            global.int8Array = global.getBuiltinInt8Array();
        }
        return global.int8Array;
    }
    
    public static void setInt8Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.int8Array = value;
    }
    
    public static Object getUint8Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.uint8Array == Global.LAZY_SENTINEL) {
            global.uint8Array = global.getBuiltinUint8Array();
        }
        return global.uint8Array;
    }
    
    public static void setUint8Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.uint8Array = value;
    }
    
    public static Object getUint8ClampedArray(final Object self) {
        final Global global = instanceFrom(self);
        if (global.uint8ClampedArray == Global.LAZY_SENTINEL) {
            global.uint8ClampedArray = global.getBuiltinUint8ClampedArray();
        }
        return global.uint8ClampedArray;
    }
    
    public static void setUint8ClampedArray(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.uint8ClampedArray = value;
    }
    
    public static Object getInt16Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.int16Array == Global.LAZY_SENTINEL) {
            global.int16Array = global.getBuiltinInt16Array();
        }
        return global.int16Array;
    }
    
    public static void setInt16Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.int16Array = value;
    }
    
    public static Object getUint16Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.uint16Array == Global.LAZY_SENTINEL) {
            global.uint16Array = global.getBuiltinUint16Array();
        }
        return global.uint16Array;
    }
    
    public static void setUint16Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.uint16Array = value;
    }
    
    public static Object getInt32Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.int32Array == Global.LAZY_SENTINEL) {
            global.int32Array = global.getBuiltinInt32Array();
        }
        return global.int32Array;
    }
    
    public static void setInt32Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.int32Array = value;
    }
    
    public static Object getUint32Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.uint32Array == Global.LAZY_SENTINEL) {
            global.uint32Array = global.getBuiltinUint32Array();
        }
        return global.uint32Array;
    }
    
    public static void setUint32Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.uint32Array = value;
    }
    
    public static Object getFloat32Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.float32Array == Global.LAZY_SENTINEL) {
            global.float32Array = global.getBuiltinFloat32Array();
        }
        return global.float32Array;
    }
    
    public static void setFloat32Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.float32Array = value;
    }
    
    public static Object getFloat64Array(final Object self) {
        final Global global = instanceFrom(self);
        if (global.float64Array == Global.LAZY_SENTINEL) {
            global.float64Array = global.getBuiltinFloat64Array();
        }
        return global.float64Array;
    }
    
    public static void setFloat64Array(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.float64Array = value;
    }
    
    public static Object getJavaImporter(final Object self) {
        final Global global = instanceFrom(self);
        if (global.javaImporter == Global.LAZY_SENTINEL) {
            global.javaImporter = global.getBuiltinJavaImporter();
        }
        return global.javaImporter;
    }
    
    public static void setJavaImporter(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.javaImporter = value;
    }
    
    public static Object getJavaApi(final Object self) {
        final Global global = instanceFrom(self);
        if (global.javaApi == Global.LAZY_SENTINEL) {
            global.javaApi = global.getBuiltinJavaApi();
        }
        return global.javaApi;
    }
    
    public static void setJavaApi(final Object self, final Object value) {
        final Global global = instanceFrom(self);
        global.javaApi = value;
    }
    
    NativeDate getDefaultDate() {
        return this.DEFAULT_DATE;
    }
    
    NativeRegExp getDefaultRegExp() {
        return this.DEFAULT_REGEXP;
    }
    
    public void setScriptContext(final ScriptContext ctxt) {
        assert this.scontext != null;
        this.scontext.set(ctxt);
    }
    
    public ScriptContext getScriptContext() {
        assert this.scontext != null;
        return this.scontext.get();
    }
    
    public void setInitScriptContext(final ScriptContext ctxt) {
        this.initscontext = ctxt;
    }
    
    private ScriptContext currentContext() {
        final ScriptContext sc = (this.scontext != null) ? this.scontext.get() : null;
        if (sc != null) {
            return sc;
        }
        if (this.initscontext != null) {
            return this.initscontext;
        }
        return (this.engine != null) ? this.engine.getContext() : null;
    }
    
    @Override
    protected Context getContext() {
        return this.context;
    }
    
    @Override
    protected boolean useDualFields() {
        return this.context.useDualFields();
    }
    
    private static PropertyMap checkAndGetMap(final Context context) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.createGlobal"));
        }
        Objects.requireNonNull(context);
        return Global.$nasgenmap$;
    }
    
    public Global(final Context context) {
        super(checkAndGetMap(context));
        this.TO_STRING = new InvokeByName("toString", ScriptObject.class);
        this.VALUE_OF = new InvokeByName("valueOf", ScriptObject.class);
        this.date = Global.LAZY_SENTINEL;
        this.regexp = Global.LAZY_SENTINEL;
        this.json = Global.LAZY_SENTINEL;
        this.jsadapter = Global.LAZY_SENTINEL;
        this.evalError = Global.LAZY_SENTINEL;
        this.rangeError = Global.LAZY_SENTINEL;
        this.uriError = Global.LAZY_SENTINEL;
        this.namedInvokers = new ConcurrentHashMap<Object, InvokeByName>();
        this.dynamicInvokers = new ConcurrentHashMap<Object, MethodHandle>();
        this.context = context;
        this.lexicalScope = (context.getEnv()._es6 ? new LexicalScope(this) : null);
    }
    
    public static Global instance() {
        return Objects.requireNonNull(Context.getGlobal());
    }
    
    private static Global instanceFrom(final Object self) {
        return (Global)((self instanceof Global) ? self : instance());
    }
    
    public static boolean hasInstance() {
        return Context.getGlobal() != null;
    }
    
    static ScriptEnvironment getEnv() {
        return instance().getContext().getEnv();
    }
    
    static Context getThisContext() {
        return instance().getContext();
    }
    
    public ClassFilter getClassFilter() {
        return this.context.getClassFilter();
    }
    
    public boolean isOfContext(final Context ctxt) {
        return this.context == ctxt;
    }
    
    public boolean isStrictContext() {
        return this.context.getEnv()._strict;
    }
    
    public void initBuiltinObjects(final ScriptEngine eng) {
        if (this.builtinObject != null) {
            return;
        }
        this.engine = eng;
        if (this.engine != null) {
            this.scontext = new ThreadLocal<ScriptContext>();
        }
        this.init(eng);
    }
    
    public Object wrapAsObject(final Object obj) {
        if (obj instanceof Boolean) {
            return new NativeBoolean((boolean)obj, this);
        }
        if (obj instanceof Number) {
            return new NativeNumber(((Number)obj).doubleValue(), this);
        }
        if (JSType.isString(obj)) {
            return new NativeString((CharSequence)obj, this);
        }
        if (obj instanceof Object[]) {
            return new NativeArray(ArrayData.allocate((Object[])obj), this);
        }
        if (obj instanceof double[]) {
            return new NativeArray(ArrayData.allocate((double[])obj), this);
        }
        if (obj instanceof int[]) {
            return new NativeArray(ArrayData.allocate((int[])obj), this);
        }
        if (obj instanceof ArrayData) {
            return new NativeArray((ArrayData)obj, this);
        }
        return obj;
    }
    
    public static GuardedInvocation primitiveLookup(final LinkRequest request, final Object self) {
        if (JSType.isString(self)) {
            return NativeString.lookupPrimitive(request, self);
        }
        if (self instanceof Number) {
            return NativeNumber.lookupPrimitive(request, self);
        }
        if (self instanceof Boolean) {
            return NativeBoolean.lookupPrimitive(request, self);
        }
        throw new IllegalArgumentException("Unsupported primitive: " + self);
    }
    
    public static MethodHandle getPrimitiveWrapFilter(final Object self) {
        if (JSType.isString(self)) {
            return NativeString.WRAPFILTER;
        }
        if (self instanceof Number) {
            return NativeNumber.WRAPFILTER;
        }
        if (self instanceof Boolean) {
            return NativeBoolean.WRAPFILTER;
        }
        throw new IllegalArgumentException("Unsupported primitive: " + self);
    }
    
    public ScriptObject newObject() {
        return this.useDualFields() ? new JD(this.getObjectPrototype()) : new JO(this.getObjectPrototype());
    }
    
    public Object getDefaultValue(final ScriptObject sobj, final Class<?> typeHint) {
        Class<?> hint = typeHint;
        if (hint == null) {
            hint = Number.class;
        }
        try {
            if (hint == String.class) {
                final Object toString = this.TO_STRING.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString)) {
                    final Object value = this.TO_STRING.getInvoker().invokeExact(toString, sobj);
                    if (JSType.isPrimitive(value)) {
                        return value;
                    }
                }
                final Object valueOf = this.VALUE_OF.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(valueOf)) {
                    final Object value2 = this.VALUE_OF.getInvoker().invokeExact(valueOf, sobj);
                    if (JSType.isPrimitive(value2)) {
                        return value2;
                    }
                }
                throw ECMAErrors.typeError(this, "cannot.get.default.string", new String[0]);
            }
            if (hint == Number.class) {
                final Object valueOf2 = this.VALUE_OF.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(valueOf2)) {
                    final Object value = this.VALUE_OF.getInvoker().invokeExact(valueOf2, sobj);
                    if (JSType.isPrimitive(value)) {
                        return value;
                    }
                }
                final Object toString2 = this.TO_STRING.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString2)) {
                    final Object value2 = this.TO_STRING.getInvoker().invokeExact(toString2, sobj);
                    if (JSType.isPrimitive(value2)) {
                        return value2;
                    }
                }
                throw ECMAErrors.typeError(this, "cannot.get.default.number", new String[0]);
            }
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public boolean isError(final ScriptObject sobj) {
        final ScriptObject errorProto = this.getErrorPrototype();
        for (ScriptObject proto = sobj.getProto(); proto != null; proto = proto.getProto()) {
            if (proto == errorProto) {
                return true;
            }
        }
        return false;
    }
    
    public ScriptObject newError(final String msg) {
        return new NativeError(msg, this);
    }
    
    public ScriptObject newEvalError(final String msg) {
        return new NativeEvalError(msg, this);
    }
    
    public ScriptObject newRangeError(final String msg) {
        return new NativeRangeError(msg, this);
    }
    
    public ScriptObject newReferenceError(final String msg) {
        return new NativeReferenceError(msg, this);
    }
    
    public ScriptObject newSyntaxError(final String msg) {
        return new NativeSyntaxError(msg, this);
    }
    
    public ScriptObject newTypeError(final String msg) {
        return new NativeTypeError(msg, this);
    }
    
    public ScriptObject newURIError(final String msg) {
        return new NativeURIError(msg, this);
    }
    
    public PropertyDescriptor newGenericDescriptor(final boolean configurable, final boolean enumerable) {
        return new GenericPropertyDescriptor(configurable, enumerable, this);
    }
    
    public PropertyDescriptor newDataDescriptor(final Object value, final boolean configurable, final boolean enumerable, final boolean writable) {
        return new DataPropertyDescriptor(configurable, enumerable, writable, value, this);
    }
    
    public PropertyDescriptor newAccessorDescriptor(final Object get, final Object set, final boolean configurable, final boolean enumerable) {
        final AccessorPropertyDescriptor desc = new AccessorPropertyDescriptor(configurable, enumerable, (get == null) ? ScriptRuntime.UNDEFINED : get, (set == null) ? ScriptRuntime.UNDEFINED : set, this);
        if (get == null) {
            desc.delete("get", false);
        }
        if (set == null) {
            desc.delete("set", false);
        }
        return desc;
    }
    
    private static <T> T getLazilyCreatedValue(final Object key, final Callable<T> creator, final Map<Object, T> map) {
        final T obj = map.get(key);
        if (obj != null) {
            return obj;
        }
        try {
            final T newObj = creator.call();
            final T existingObj = map.putIfAbsent(key, newObj);
            return (existingObj != null) ? existingObj : newObj;
        }
        catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
    
    public InvokeByName getInvokeByName(final Object key, final Callable<InvokeByName> creator) {
        return getLazilyCreatedValue(key, creator, this.namedInvokers);
    }
    
    public MethodHandle getDynamicInvoker(final Object key, final Callable<MethodHandle> creator) {
        return getLazilyCreatedValue(key, creator, this.dynamicInvokers);
    }
    
    public static Object __noSuchProperty__(final Object self, final Object name) {
        final Global global = instance();
        final ScriptContext sctxt = global.currentContext();
        final String nameStr = name.toString();
        if (sctxt != null) {
            final int scope = sctxt.getAttributesScope(nameStr);
            if (scope != -1) {
                return ScriptObjectMirror.unwrap(sctxt.getAttribute(nameStr, scope), global);
            }
        }
        if ("context".equals(nameStr)) {
            return sctxt;
        }
        if ("engine".equals(nameStr) && (System.getSecurityManager() == null || global.getClassFilter() == null)) {
            return global.engine;
        }
        if (self == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.referenceError(global, "not.defined", nameStr);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object eval(final Object self, final Object str) {
        return directEval(self, str, instanceFrom(self), ScriptRuntime.UNDEFINED, false);
    }
    
    public static Object directEval(final Object self, final Object str, final Object callThis, final Object location, final boolean strict) {
        if (!JSType.isString(str)) {
            return str;
        }
        final Global global = instanceFrom(self);
        final ScriptObject scope = (self instanceof ScriptObject && ((ScriptObject)self).isScope()) ? ((ScriptObject)self) : global;
        return global.getContext().eval(scope, str.toString(), callThis, location, strict, true);
    }
    
    public static Object print(final Object self, final Object... objects) {
        return instanceFrom(self).printImpl(false, objects);
    }
    
    public static Object println(final Object self, final Object... objects) {
        return instanceFrom(self).printImpl(true, objects);
    }
    
    public static Object load(final Object self, final Object source) throws IOException {
        final Global global = instanceFrom(self);
        return global.getContext().load(self, source);
    }
    
    public static Object loadWithNewGlobal(final Object self, final Object... args) throws IOException {
        final Global global = instanceFrom(self);
        final int length = args.length;
        final boolean hasArgs = 0 < length;
        final Object from = hasArgs ? args[0] : ScriptRuntime.UNDEFINED;
        final Object[] arguments = hasArgs ? Arrays.copyOfRange(args, 1, length) : args;
        return global.getContext().loadWithNewGlobal(from, arguments);
    }
    
    public static Object exit(final Object self, final Object code) {
        System.exit(JSType.toInt32(code));
        return ScriptRuntime.UNDEFINED;
    }
    
    public ScriptObject getObjectPrototype() {
        return ScriptFunction.getPrototype(this.builtinObject);
    }
    
    public ScriptObject getFunctionPrototype() {
        return ScriptFunction.getPrototype(this.builtinFunction);
    }
    
    ScriptObject getArrayPrototype() {
        return ScriptFunction.getPrototype(this.builtinArray);
    }
    
    ScriptObject getBooleanPrototype() {
        return ScriptFunction.getPrototype(this.builtinBoolean);
    }
    
    ScriptObject getNumberPrototype() {
        return ScriptFunction.getPrototype(this.builtinNumber);
    }
    
    ScriptObject getDatePrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinDate());
    }
    
    ScriptObject getRegExpPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinRegExp());
    }
    
    ScriptObject getStringPrototype() {
        return ScriptFunction.getPrototype(this.builtinString);
    }
    
    ScriptObject getErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinError);
    }
    
    ScriptObject getEvalErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinEvalError());
    }
    
    ScriptObject getRangeErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinRangeError());
    }
    
    ScriptObject getReferenceErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinReferenceError);
    }
    
    ScriptObject getSyntaxErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinSyntaxError);
    }
    
    ScriptObject getTypeErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinTypeError);
    }
    
    ScriptObject getURIErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinURIError());
    }
    
    ScriptObject getJavaImporterPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinJavaImporter());
    }
    
    ScriptObject getJSAdapterPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinJSAdapter());
    }
    
    private synchronized ScriptFunction getBuiltinArrayBuffer() {
        if (this.builtinArrayBuffer == null) {
            this.builtinArrayBuffer = this.initConstructorAndSwitchPoint("ArrayBuffer", ScriptFunction.class);
        }
        return this.builtinArrayBuffer;
    }
    
    ScriptObject getArrayBufferPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinArrayBuffer());
    }
    
    private synchronized ScriptFunction getBuiltinDataView() {
        if (this.builtinDataView == null) {
            this.builtinDataView = this.initConstructorAndSwitchPoint("DataView", ScriptFunction.class);
        }
        return this.builtinDataView;
    }
    
    ScriptObject getDataViewPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinDataView());
    }
    
    private synchronized ScriptFunction getBuiltinInt8Array() {
        if (this.builtinInt8Array == null) {
            this.builtinInt8Array = this.initConstructorAndSwitchPoint("Int8Array", ScriptFunction.class);
        }
        return this.builtinInt8Array;
    }
    
    ScriptObject getInt8ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt8Array());
    }
    
    private synchronized ScriptFunction getBuiltinUint8Array() {
        if (this.builtinUint8Array == null) {
            this.builtinUint8Array = this.initConstructorAndSwitchPoint("Uint8Array", ScriptFunction.class);
        }
        return this.builtinUint8Array;
    }
    
    ScriptObject getUint8ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint8Array());
    }
    
    private synchronized ScriptFunction getBuiltinUint8ClampedArray() {
        if (this.builtinUint8ClampedArray == null) {
            this.builtinUint8ClampedArray = this.initConstructorAndSwitchPoint("Uint8ClampedArray", ScriptFunction.class);
        }
        return this.builtinUint8ClampedArray;
    }
    
    ScriptObject getUint8ClampedArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint8ClampedArray());
    }
    
    private synchronized ScriptFunction getBuiltinInt16Array() {
        if (this.builtinInt16Array == null) {
            this.builtinInt16Array = this.initConstructorAndSwitchPoint("Int16Array", ScriptFunction.class);
        }
        return this.builtinInt16Array;
    }
    
    ScriptObject getInt16ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt16Array());
    }
    
    private synchronized ScriptFunction getBuiltinUint16Array() {
        if (this.builtinUint16Array == null) {
            this.builtinUint16Array = this.initConstructorAndSwitchPoint("Uint16Array", ScriptFunction.class);
        }
        return this.builtinUint16Array;
    }
    
    ScriptObject getUint16ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint16Array());
    }
    
    private synchronized ScriptFunction getBuiltinInt32Array() {
        if (this.builtinInt32Array == null) {
            this.builtinInt32Array = this.initConstructorAndSwitchPoint("Int32Array", ScriptFunction.class);
        }
        return this.builtinInt32Array;
    }
    
    ScriptObject getInt32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt32Array());
    }
    
    private synchronized ScriptFunction getBuiltinUint32Array() {
        if (this.builtinUint32Array == null) {
            this.builtinUint32Array = this.initConstructorAndSwitchPoint("Uint32Array", ScriptFunction.class);
        }
        return this.builtinUint32Array;
    }
    
    ScriptObject getUint32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint32Array());
    }
    
    private synchronized ScriptFunction getBuiltinFloat32Array() {
        if (this.builtinFloat32Array == null) {
            this.builtinFloat32Array = this.initConstructorAndSwitchPoint("Float32Array", ScriptFunction.class);
        }
        return this.builtinFloat32Array;
    }
    
    ScriptObject getFloat32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinFloat32Array());
    }
    
    private synchronized ScriptFunction getBuiltinFloat64Array() {
        if (this.builtinFloat64Array == null) {
            this.builtinFloat64Array = this.initConstructorAndSwitchPoint("Float64Array", ScriptFunction.class);
        }
        return this.builtinFloat64Array;
    }
    
    ScriptObject getFloat64ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinFloat64Array());
    }
    
    public ScriptFunction getTypeErrorThrower() {
        return this.typeErrorThrower;
    }
    
    private synchronized ScriptFunction getBuiltinDate() {
        if (this.builtinDate == null) {
            this.builtinDate = this.initConstructorAndSwitchPoint("Date", ScriptFunction.class);
            final ScriptObject dateProto = ScriptFunction.getPrototype(this.builtinDate);
            this.DEFAULT_DATE = new NativeDate(Double.NaN, dateProto);
        }
        return this.builtinDate;
    }
    
    private synchronized ScriptFunction getBuiltinEvalError() {
        if (this.builtinEvalError == null) {
            this.builtinEvalError = this.initErrorSubtype("EvalError", this.getErrorPrototype());
        }
        return this.builtinEvalError;
    }
    
    private ScriptFunction getBuiltinFunction() {
        return this.builtinFunction;
    }
    
    public static SwitchPoint getBuiltinFunctionApplySwitchPoint() {
        return ScriptFunction.getPrototype(instance().getBuiltinFunction()).getProperty("apply").getBuiltinSwitchPoint();
    }
    
    private static boolean isBuiltinFunctionProperty(final String name) {
        final Global instance = instance();
        final ScriptFunction builtinFunction = instance.getBuiltinFunction();
        if (builtinFunction == null) {
            return false;
        }
        final boolean isBuiltinFunction = instance.function == builtinFunction;
        return isBuiltinFunction && ScriptFunction.getPrototype(builtinFunction).getProperty(name).isBuiltin();
    }
    
    public static boolean isBuiltinFunctionPrototypeApply() {
        return isBuiltinFunctionProperty("apply");
    }
    
    public static boolean isBuiltinFunctionPrototypeCall() {
        return isBuiltinFunctionProperty("call");
    }
    
    private synchronized ScriptFunction getBuiltinJSAdapter() {
        if (this.builtinJSAdapter == null) {
            this.builtinJSAdapter = this.initConstructorAndSwitchPoint("JSAdapter", ScriptFunction.class);
        }
        return this.builtinJSAdapter;
    }
    
    private synchronized ScriptObject getBuiltinJSON() {
        if (this.builtinJSON == null) {
            this.builtinJSON = this.initConstructorAndSwitchPoint("JSON", ScriptObject.class);
        }
        return this.builtinJSON;
    }
    
    private synchronized ScriptFunction getBuiltinJavaImporter() {
        if (this.getContext().getEnv()._no_java) {
            throw new IllegalStateException();
        }
        if (this.builtinJavaImporter == null) {
            this.builtinJavaImporter = this.initConstructor("JavaImporter", ScriptFunction.class);
        }
        return this.builtinJavaImporter;
    }
    
    private synchronized ScriptObject getBuiltinJavaApi() {
        if (this.getContext().getEnv()._no_java) {
            throw new IllegalStateException();
        }
        if (this.builtinJavaApi == null) {
            this.builtinJavaApi = this.initConstructor("Java", ScriptObject.class);
        }
        return this.builtinJavaApi;
    }
    
    private synchronized ScriptFunction getBuiltinRangeError() {
        if (this.builtinRangeError == null) {
            this.builtinRangeError = this.initErrorSubtype("RangeError", this.getErrorPrototype());
        }
        return this.builtinRangeError;
    }
    
    private synchronized ScriptFunction getBuiltinRegExp() {
        if (this.builtinRegExp == null) {
            this.builtinRegExp = this.initConstructorAndSwitchPoint("RegExp", ScriptFunction.class);
            final ScriptObject regExpProto = ScriptFunction.getPrototype(this.builtinRegExp);
            regExpProto.addBoundProperties(this.DEFAULT_REGEXP = new NativeRegExp("(?:)", "", this, regExpProto));
        }
        return this.builtinRegExp;
    }
    
    private synchronized ScriptFunction getBuiltinURIError() {
        if (this.builtinURIError == null) {
            this.builtinURIError = this.initErrorSubtype("URIError", this.getErrorPrototype());
        }
        return this.builtinURIError;
    }
    
    @Override
    public String getClassName() {
        return "global";
    }
    
    public static Object regExpCopy(final Object regexp) {
        return new NativeRegExp((NativeRegExp)regexp);
    }
    
    public static NativeRegExp toRegExp(final Object obj) {
        if (obj instanceof NativeRegExp) {
            return (NativeRegExp)obj;
        }
        return new NativeRegExp(JSType.toString(obj));
    }
    
    public static Object toObject(final Object obj) {
        if (obj == null || obj == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (obj instanceof ScriptObject) {
            return obj;
        }
        return instance().wrapAsObject(obj);
    }
    
    public static NativeArray allocate(final Object[] initial) {
        ArrayData arrayData = ArrayData.allocate(initial);
        for (int index = 0; index < initial.length; ++index) {
            final Object value = initial[index];
            if (value == ScriptRuntime.EMPTY) {
                arrayData = arrayData.delete(index);
            }
        }
        return new NativeArray(arrayData);
    }
    
    public static NativeArray allocate(final double[] initial) {
        return new NativeArray(ArrayData.allocate(initial));
    }
    
    public static NativeArray allocate(final int[] initial) {
        return new NativeArray(ArrayData.allocate(initial));
    }
    
    public static ScriptObject allocateArguments(final Object[] arguments, final Object callee, final int numParams) {
        return NativeArguments.allocate(arguments, (ScriptFunction)callee, numParams);
    }
    
    public static boolean isEval(final Object fn) {
        return fn == instance().builtinEval;
    }
    
    public static Object replaceLocationPropertyPlaceholder(final Object placeholder, final Object locationProperty) {
        return isLocationPropertyPlaceholder(placeholder) ? locationProperty : placeholder;
    }
    
    public static boolean isLocationPropertyPlaceholder(final Object placeholder) {
        return placeholder == Global.LOCATION_PLACEHOLDER;
    }
    
    public static Object newRegExp(final String expression, final String options) {
        if (options == null) {
            return new NativeRegExp(expression);
        }
        return new NativeRegExp(expression, options);
    }
    
    public static ScriptObject objectPrototype() {
        return instance().getObjectPrototype();
    }
    
    public static ScriptObject newEmptyInstance() {
        return instance().newObject();
    }
    
    public static ScriptObject checkObject(final Object obj) {
        if (!(obj instanceof ScriptObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        return (ScriptObject)obj;
    }
    
    public static void checkObjectCoercible(final Object obj) {
        if (obj == null || obj == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
    }
    
    public final ScriptObject getLexicalScope() {
        assert this.context.getEnv()._es6;
        return this.lexicalScope;
    }
    
    @Override
    public void addBoundProperties(final ScriptObject source, final Property[] properties) {
        PropertyMap ownMap = this.getMap();
        LexicalScope lexScope = null;
        PropertyMap lexicalMap = null;
        boolean hasLexicalDefinitions = false;
        if (this.context.getEnv()._es6) {
            lexScope = (LexicalScope)this.getLexicalScope();
            lexicalMap = lexScope.getMap();
            for (final Property property : properties) {
                if (property.isLexicalBinding()) {
                    hasLexicalDefinitions = true;
                }
                final Property globalProperty = ownMap.findProperty(property.getKey());
                if (globalProperty != null && !globalProperty.isConfigurable() && property.isLexicalBinding()) {
                    throw ECMAErrors.syntaxError("redeclare.variable", property.getKey());
                }
                final Property lexicalProperty = lexicalMap.findProperty(property.getKey());
                if (lexicalProperty != null && !property.isConfigurable()) {
                    throw ECMAErrors.syntaxError("redeclare.variable", property.getKey());
                }
            }
        }
        final boolean extensible = this.isExtensible();
        for (final Property property2 : properties) {
            if (property2.isLexicalBinding()) {
                assert lexScope != null;
                lexicalMap = lexScope.addBoundProperty(lexicalMap, source, property2, true);
                if (ownMap.findProperty(property2.getKey()) != null) {
                    this.invalidateGlobalConstant(property2.getKey());
                }
            }
            else {
                ownMap = this.addBoundProperty(ownMap, source, property2, extensible);
            }
        }
        this.setMap(ownMap);
        if (hasLexicalDefinitions) {
            assert lexScope != null;
            lexScope.setMap(lexicalMap);
            this.invalidateLexicalSwitchPoint();
        }
    }
    
    public GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
        final String name = desc.getNameToken(2);
        final boolean isScope = NashornCallSiteDescriptor.isScope(desc);
        if (this.lexicalScope != null && isScope && !NashornCallSiteDescriptor.isApplyToCall(desc) && this.lexicalScope.hasOwnProperty(name)) {
            return this.lexicalScope.findGetMethod(desc, request, operator);
        }
        final GuardedInvocation invocation = super.findGetMethod(desc, request, operator);
        if (isScope && this.context.getEnv()._es6 && (invocation.getSwitchPoints() == null || !this.hasOwnProperty(name))) {
            return invocation.addSwitchPoint(this.getLexicalScopeSwitchPoint());
        }
        return invocation;
    }
    
    @Override
    protected FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        if (this.lexicalScope != null && start != this && start.isScope()) {
            final FindProperty find = this.lexicalScope.findProperty(key, false);
            if (find != null) {
                return find;
            }
        }
        return super.findProperty(key, deep, start);
    }
    
    public GuardedInvocation findSetMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final boolean isScope = NashornCallSiteDescriptor.isScope(desc);
        if (this.lexicalScope != null && isScope) {
            final String name = desc.getNameToken(2);
            if (this.lexicalScope.hasOwnProperty(name)) {
                return this.lexicalScope.findSetMethod(desc, request);
            }
        }
        final GuardedInvocation invocation = super.findSetMethod(desc, request);
        if (isScope && this.context.getEnv()._es6) {
            return invocation.addSwitchPoint(this.getLexicalScopeSwitchPoint());
        }
        return invocation;
    }
    
    public void addShellBuiltins() {
        Object value = ScriptFunction.createBuiltin("input", ShellFunctions.INPUT);
        this.addOwnProperty("input", 2, value);
        value = ScriptFunction.createBuiltin("evalinput", ShellFunctions.EVALINPUT);
        this.addOwnProperty("evalinput", 2, value);
    }
    
    private synchronized SwitchPoint getLexicalScopeSwitchPoint() {
        SwitchPoint switchPoint = this.lexicalScopeSwitchPoint;
        if (switchPoint == null || switchPoint.hasBeenInvalidated()) {
            final SwitchPoint lexicalScopeSwitchPoint = new SwitchPoint();
            this.lexicalScopeSwitchPoint = lexicalScopeSwitchPoint;
            switchPoint = lexicalScopeSwitchPoint;
        }
        return switchPoint;
    }
    
    private synchronized void invalidateLexicalSwitchPoint() {
        if (this.lexicalScopeSwitchPoint != null) {
            this.context.getLogger(GlobalConstants.class).info("Invalidating non-constant globals on lexical scope update");
            SwitchPoint.invalidateAll(new SwitchPoint[] { this.lexicalScopeSwitchPoint });
        }
    }
    
    private static Object lexicalScopeFilter(final Object self) {
        if (self instanceof Global) {
            return ((Global)self).getLexicalScope();
        }
        return self;
    }
    
    private <T extends ScriptObject> T initConstructorAndSwitchPoint(final String name, final Class<T> clazz) {
        final T func = (T)this.initConstructor(name, (Class<ScriptObject>)clazz);
        this.tagBuiltinProperties(name, func);
        return func;
    }
    
    private void init(final ScriptEngine eng) {
        assert Context.getGlobal() == this : "this global is not set as current";
        final ScriptEnvironment env = this.getContext().getEnv();
        this.initFunctionAndObject();
        this.setInitialProto(this.getObjectPrototype());
        final ScriptFunction builtin = ScriptFunction.createBuiltin("eval", Global.EVAL);
        this.builtinEval = builtin;
        this.eval = builtin;
        this.parseInt = ScriptFunction.createBuiltin("parseInt", GlobalFunctions.PARSEINT, new Specialization[] { new Specialization(GlobalFunctions.PARSEINT_Z), new Specialization(GlobalFunctions.PARSEINT_I), new Specialization(GlobalFunctions.PARSEINT_OI), new Specialization(GlobalFunctions.PARSEINT_O) });
        this.parseFloat = ScriptFunction.createBuiltin("parseFloat", GlobalFunctions.PARSEFLOAT);
        this.isNaN = ScriptFunction.createBuiltin("isNaN", GlobalFunctions.IS_NAN, new Specialization[] { new Specialization(GlobalFunctions.IS_NAN_I), new Specialization(GlobalFunctions.IS_NAN_J), new Specialization(GlobalFunctions.IS_NAN_D) });
        this.parseFloat = ScriptFunction.createBuiltin("parseFloat", GlobalFunctions.PARSEFLOAT);
        this.isNaN = ScriptFunction.createBuiltin("isNaN", GlobalFunctions.IS_NAN);
        this.isFinite = ScriptFunction.createBuiltin("isFinite", GlobalFunctions.IS_FINITE);
        this.encodeURI = ScriptFunction.createBuiltin("encodeURI", GlobalFunctions.ENCODE_URI);
        this.encodeURIComponent = ScriptFunction.createBuiltin("encodeURIComponent", GlobalFunctions.ENCODE_URICOMPONENT);
        this.decodeURI = ScriptFunction.createBuiltin("decodeURI", GlobalFunctions.DECODE_URI);
        this.decodeURIComponent = ScriptFunction.createBuiltin("decodeURIComponent", GlobalFunctions.DECODE_URICOMPONENT);
        this.escape = ScriptFunction.createBuiltin("escape", GlobalFunctions.ESCAPE);
        this.unescape = ScriptFunction.createBuiltin("unescape", GlobalFunctions.UNESCAPE);
        this.print = ScriptFunction.createBuiltin("print", env._print_no_newline ? Global.PRINT : Global.PRINTLN);
        this.load = ScriptFunction.createBuiltin("load", Global.LOAD);
        this.loadWithNewGlobal = ScriptFunction.createBuiltin("loadWithNewGlobal", Global.LOAD_WITH_NEW_GLOBAL);
        this.exit = ScriptFunction.createBuiltin("exit", Global.EXIT);
        this.quit = ScriptFunction.createBuiltin("quit", Global.EXIT);
        this.builtinArray = this.initConstructorAndSwitchPoint("Array", ScriptFunction.class);
        this.builtinBoolean = this.initConstructorAndSwitchPoint("Boolean", ScriptFunction.class);
        this.builtinNumber = this.initConstructorAndSwitchPoint("Number", ScriptFunction.class);
        this.builtinString = this.initConstructorAndSwitchPoint("String", ScriptFunction.class);
        this.builtinMath = this.initConstructorAndSwitchPoint("Math", ScriptObject.class);
        final ScriptObject stringPrototype = this.getStringPrototype();
        stringPrototype.addOwnProperty("length", 7, 0.0);
        final ScriptObject arrayPrototype = this.getArrayPrototype();
        arrayPrototype.setIsArray();
        this.initErrorObjects();
        if (!env._no_java) {
            this.javaApi = Global.LAZY_SENTINEL;
            this.javaImporter = Global.LAZY_SENTINEL;
            this.initJavaAccess();
        }
        else {
            this.delete("Java", false);
            this.delete("JavaImporter", false);
            this.delete("Packages", false);
            this.delete("com", false);
            this.delete("edu", false);
            this.delete("java", false);
            this.delete("javafx", false);
            this.delete("javax", false);
            this.delete("org", false);
        }
        if (!env._no_typed_arrays) {
            this.arrayBuffer = Global.LAZY_SENTINEL;
            this.dataView = Global.LAZY_SENTINEL;
            this.int8Array = Global.LAZY_SENTINEL;
            this.uint8Array = Global.LAZY_SENTINEL;
            this.uint8ClampedArray = Global.LAZY_SENTINEL;
            this.int16Array = Global.LAZY_SENTINEL;
            this.uint16Array = Global.LAZY_SENTINEL;
            this.int32Array = Global.LAZY_SENTINEL;
            this.uint32Array = Global.LAZY_SENTINEL;
            this.float32Array = Global.LAZY_SENTINEL;
            this.float64Array = Global.LAZY_SENTINEL;
        }
        if (env._scripting) {
            this.initScripting(env);
        }
        if (Context.DEBUG) {
            final SecurityManager sm = System.getSecurityManager();
            boolean debugOkay;
            if (sm != null) {
                try {
                    sm.checkPermission(new RuntimePermission("nashorn.debugMode"));
                    debugOkay = true;
                }
                catch (SecurityException ignored) {
                    debugOkay = false;
                }
            }
            else {
                debugOkay = true;
            }
            if (debugOkay) {
                this.initDebug();
            }
        }
        this.copyBuiltins();
        this.arguments = this.wrapAsObject(env.getArguments().toArray());
        if (env._scripting) {
            this.addOwnProperty("$ARG", 2, this.arguments);
        }
        if (eng != null) {
            this.addOwnProperty("javax.script.filename", 2, null);
            final ScriptFunction noSuchProp = ScriptFunction.createStrictBuiltin("__noSuchProperty__", Global.NO_SUCH_PROPERTY);
            this.addOwnProperty("__noSuchProperty__", 2, noSuchProp);
        }
    }
    
    private void initErrorObjects() {
        this.builtinError = this.initConstructor("Error", ScriptFunction.class);
        final ScriptObject errorProto = this.getErrorPrototype();
        final ScriptFunction getStack = ScriptFunction.createBuiltin("getStack", NativeError.GET_STACK);
        final ScriptFunction setStack = ScriptFunction.createBuiltin("setStack", NativeError.SET_STACK);
        errorProto.addOwnProperty("stack", 2, getStack, setStack);
        final ScriptFunction getLineNumber = ScriptFunction.createBuiltin("getLineNumber", NativeError.GET_LINENUMBER);
        final ScriptFunction setLineNumber = ScriptFunction.createBuiltin("setLineNumber", NativeError.SET_LINENUMBER);
        errorProto.addOwnProperty("lineNumber", 2, getLineNumber, setLineNumber);
        final ScriptFunction getColumnNumber = ScriptFunction.createBuiltin("getColumnNumber", NativeError.GET_COLUMNNUMBER);
        final ScriptFunction setColumnNumber = ScriptFunction.createBuiltin("setColumnNumber", NativeError.SET_COLUMNNUMBER);
        errorProto.addOwnProperty("columnNumber", 2, getColumnNumber, setColumnNumber);
        final ScriptFunction getFileName = ScriptFunction.createBuiltin("getFileName", NativeError.GET_FILENAME);
        final ScriptFunction setFileName = ScriptFunction.createBuiltin("setFileName", NativeError.SET_FILENAME);
        errorProto.addOwnProperty("fileName", 2, getFileName, setFileName);
        errorProto.set("name", "Error", 0);
        errorProto.set("message", "", 0);
        this.tagBuiltinProperties("Error", this.builtinError);
        this.builtinReferenceError = this.initErrorSubtype("ReferenceError", errorProto);
        this.builtinSyntaxError = this.initErrorSubtype("SyntaxError", errorProto);
        this.builtinTypeError = this.initErrorSubtype("TypeError", errorProto);
    }
    
    private ScriptFunction initErrorSubtype(final String name, final ScriptObject errorProto) {
        final ScriptFunction cons = this.initConstructor(name, ScriptFunction.class);
        final ScriptObject prototype = ScriptFunction.getPrototype(cons);
        prototype.set("name", name, 0);
        prototype.set("message", "", 0);
        prototype.setInitialProto(errorProto);
        this.tagBuiltinProperties(name, cons);
        return cons;
    }
    
    private void initJavaAccess() {
        final ScriptObject objectProto = this.getObjectPrototype();
        this.builtinPackages = new NativeJavaPackage("", objectProto);
        this.builtinCom = new NativeJavaPackage("com", objectProto);
        this.builtinEdu = new NativeJavaPackage("edu", objectProto);
        this.builtinJava = new NativeJavaPackage("java", objectProto);
        this.builtinJavafx = new NativeJavaPackage("javafx", objectProto);
        this.builtinJavax = new NativeJavaPackage("javax", objectProto);
        this.builtinOrg = new NativeJavaPackage("org", objectProto);
    }
    
    private void initScripting(final ScriptEnvironment scriptEnv) {
        ScriptObject value = ScriptFunction.createBuiltin("readLine", ScriptingFunctions.READLINE);
        this.addOwnProperty("readLine", 2, value);
        value = ScriptFunction.createBuiltin("readFully", ScriptingFunctions.READFULLY);
        this.addOwnProperty("readFully", 2, value);
        final String execName = "$EXEC";
        value = ScriptFunction.createBuiltin("$EXEC", ScriptingFunctions.EXEC);
        value.addOwnProperty("throwOnError", 2, false);
        this.addOwnProperty("$EXEC", 2, value);
        value = (ScriptObject)this.get("print");
        this.addOwnProperty("echo", 2, value);
        final ScriptObject options = this.newObject();
        copyOptions(options, scriptEnv);
        this.addOwnProperty("$OPTIONS", 2, options);
        if (System.getSecurityManager() == null) {
            final ScriptObject env = this.newObject();
            env.putAll(System.getenv(), scriptEnv._strict);
            if (!env.containsKey("PWD")) {
                env.put("PWD", System.getProperty("user.dir"), scriptEnv._strict);
            }
            this.addOwnProperty("$ENV", 2, env);
        }
        else {
            this.addOwnProperty("$ENV", 2, ScriptRuntime.UNDEFINED);
        }
        this.addOwnProperty("$OUT", 2, ScriptRuntime.UNDEFINED);
        this.addOwnProperty("$ERR", 2, ScriptRuntime.UNDEFINED);
        this.addOwnProperty("$EXIT", 2, ScriptRuntime.UNDEFINED);
    }
    
    private static void copyOptions(final ScriptObject options, final ScriptEnvironment scriptEnv) {
        for (final Field f : scriptEnv.getClass().getFields()) {
            try {
                options.set(f.getName(), f.get(scriptEnv), 0);
            }
            catch (IllegalArgumentException | IllegalAccessException ex2) {
                final Exception ex;
                final Exception exp = ex;
                throw new RuntimeException(exp);
            }
        }
    }
    
    private void copyBuiltins() {
        this.array = this.builtinArray;
        this._boolean = this.builtinBoolean;
        this.error = this.builtinError;
        this.function = this.builtinFunction;
        this.com = this.builtinCom;
        this.edu = this.builtinEdu;
        this.java = this.builtinJava;
        this.javafx = this.builtinJavafx;
        this.javax = this.builtinJavax;
        this.org = this.builtinOrg;
        this.math = this.builtinMath;
        this.number = this.builtinNumber;
        this.object = this.builtinObject;
        this.packages = this.builtinPackages;
        this.referenceError = this.builtinReferenceError;
        this.string = this.builtinString;
        this.syntaxError = this.builtinSyntaxError;
        this.typeError = this.builtinTypeError;
    }
    
    private void initDebug() {
        this.addOwnProperty("Debug", 2, this.initConstructor("Debug", ScriptObject.class));
    }
    
    private Object printImpl(final boolean newLine, final Object... objects) {
        final ScriptContext sc = this.currentContext();
        final PrintWriter out = (sc != null) ? new PrintWriter(sc.getWriter()) : this.getContext().getEnv().getOut();
        final StringBuilder sb = new StringBuilder();
        for (final Object obj : objects) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(JSType.toString(obj));
        }
        if (newLine) {
            out.println(sb.toString());
        }
        else {
            out.print(sb.toString());
        }
        out.flush();
        return ScriptRuntime.UNDEFINED;
    }
    
    private <T extends ScriptObject> T initConstructor(final String name, final Class<T> clazz) {
        try {
            final StringBuilder sb = new StringBuilder("jdk.nashorn.internal.objects.");
            sb.append("Native");
            sb.append(name);
            sb.append("$Constructor");
            final Class<?> funcClass = Class.forName(sb.toString());
            final T res = clazz.cast(funcClass.newInstance());
            if (res instanceof ScriptFunction) {
                final ScriptFunction func = (ScriptFunction)res;
                func.modifyOwnProperty(func.getProperty("prototype"), 7);
            }
            if (res.getProto() == null) {
                res.setInitialProto(this.getObjectPrototype());
            }
            res.setIsBuiltin();
            return res;
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new RuntimeException(e);
        }
    }
    
    private List<Property> extractBuiltinProperties(final String name, final ScriptObject func) {
        final List<Property> list = new ArrayList<Property>();
        list.addAll(Arrays.asList(func.getMap().getProperties()));
        if (func instanceof ScriptFunction) {
            final ScriptObject proto = ScriptFunction.getPrototype((ScriptFunction)func);
            if (proto != null) {
                list.addAll(Arrays.asList(proto.getMap().getProperties()));
            }
        }
        final Property prop = this.getProperty(name);
        if (prop != null) {
            list.add(prop);
        }
        return list;
    }
    
    private void tagBuiltinProperties(final String name, final ScriptObject func) {
        SwitchPoint sp = this.context.getBuiltinSwitchPoint(name);
        if (sp == null) {
            sp = this.context.newBuiltinSwitchPoint(name);
        }
        for (final Property prop : this.extractBuiltinProperties(name, func)) {
            prop.setBuiltinSwitchPoint(sp);
        }
    }
    
    private void initFunctionAndObject() {
        this.builtinFunction = this.initConstructor("Function", ScriptFunction.class);
        final ScriptFunction anon = ScriptFunction.createAnonymous();
        anon.addBoundProperties(this.getFunctionPrototype());
        this.builtinFunction.setInitialProto(anon);
        this.builtinFunction.setPrototype(anon);
        anon.set("constructor", this.builtinFunction, 0);
        anon.deleteOwnProperty(anon.getMap().findProperty("prototype"));
        (this.typeErrorThrower = ScriptFunction.createBuiltin("TypeErrorThrower", Lookup.TYPE_ERROR_THROWER_GETTER)).preventExtensions();
        this.builtinObject = this.initConstructor("Object", ScriptFunction.class);
        final ScriptObject ObjectPrototype = this.getObjectPrototype();
        anon.setInitialProto(ObjectPrototype);
        final ScriptFunction getProto = ScriptFunction.createBuiltin("getProto", NativeObject.GET__PROTO__);
        final ScriptFunction setProto = ScriptFunction.createBuiltin("setProto", NativeObject.SET__PROTO__);
        ObjectPrototype.addOwnProperty("__proto__", 2, getProto, setProto);
        final Property[] properties2;
        Property[] properties = properties2 = this.getFunctionPrototype().getMap().getProperties();
        for (final Property property : properties2) {
            final Object key = property.getKey();
            final Object value = this.builtinFunction.get(key);
            if (value instanceof ScriptFunction && value != anon) {
                final ScriptFunction func = (ScriptFunction)value;
                func.setInitialProto(this.getFunctionPrototype());
                final ScriptObject prototype = ScriptFunction.getPrototype(func);
                if (prototype != null) {
                    prototype.setInitialProto(ObjectPrototype);
                }
            }
        }
        for (final Property property : this.builtinObject.getMap().getProperties()) {
            final Object key = property.getKey();
            final Object value = this.builtinObject.get(key);
            if (value instanceof ScriptFunction) {
                final ScriptFunction func = (ScriptFunction)value;
                final ScriptObject prototype = ScriptFunction.getPrototype(func);
                if (prototype != null) {
                    prototype.setInitialProto(ObjectPrototype);
                }
            }
        }
        final Property[] properties4;
        properties = (properties4 = this.getObjectPrototype().getMap().getProperties());
        for (final Property property : properties4) {
            final Object key = property.getKey();
            if (!key.equals("constructor")) {
                final Object value = ObjectPrototype.get(key);
                if (value instanceof ScriptFunction) {
                    final ScriptFunction func = (ScriptFunction)value;
                    final ScriptObject prototype = ScriptFunction.getPrototype(func);
                    if (prototype != null) {
                        prototype.setInitialProto(ObjectPrototype);
                    }
                }
            }
        }
        this.tagBuiltinProperties("Object", this.builtinObject);
        this.tagBuiltinProperties("Function", this.builtinFunction);
        this.tagBuiltinProperties("Function", anon);
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), Global.class, name, Lookup.MH.type(rtype, types));
    }
    
    RegExpResult getLastRegExpResult() {
        return this.lastRegExpResult;
    }
    
    void setLastRegExpResult(final RegExpResult regExpResult) {
        this.lastRegExpResult = regExpResult;
    }
    
    @Override
    protected boolean isGlobal() {
        return true;
    }
    
    static {
        LAZY_SENTINEL = new Object();
        LOCATION_PLACEHOLDER = new Object();
        undefined = ScriptRuntime.UNDEFINED;
        __FILE__ = Global.LOCATION_PLACEHOLDER;
        __DIR__ = Global.LOCATION_PLACEHOLDER;
        __LINE__ = Global.LOCATION_PLACEHOLDER;
        EVAL = findOwnMH_S("eval", Object.class, Object.class, Object.class);
        NO_SUCH_PROPERTY = findOwnMH_S("__noSuchProperty__", Object.class, Object.class, Object.class);
        PRINT = findOwnMH_S("print", Object.class, Object.class, Object[].class);
        PRINTLN = findOwnMH_S("println", Object.class, Object.class, Object[].class);
        LOAD = findOwnMH_S("load", Object.class, Object.class, Object.class);
        LOAD_WITH_NEW_GLOBAL = findOwnMH_S("loadWithNewGlobal", Object.class, Object.class, Object[].class);
        EXIT = findOwnMH_S("exit", Object.class, Object.class, Object.class);
        LEXICAL_SCOPE_FILTER = findOwnMH_S("lexicalScopeFilter", Object.class, Object.class);
        $clinit$();
    }
    
    public static void $clinit$() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/Global.$clinit$:()V'.
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:66)
        //     at com.strobel.assembler.metadata.MethodDefinition.tryLoadBody(MethodDefinition.java:729)
        //     at com.strobel.assembler.metadata.MethodDefinition.getBody(MethodDefinition.java:83)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:202)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // Caused by: java.lang.ClassCastException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Object G$arguments() {
        return this.arguments;
    }
    
    public void S$arguments(final Object arguments) {
        this.arguments = arguments;
    }
    
    public Object G$parseInt() {
        return this.parseInt;
    }
    
    public void S$parseInt(final Object parseInt) {
        this.parseInt = parseInt;
    }
    
    public Object G$parseFloat() {
        return this.parseFloat;
    }
    
    public void S$parseFloat(final Object parseFloat) {
        this.parseFloat = parseFloat;
    }
    
    public Object G$isNaN() {
        return this.isNaN;
    }
    
    public void S$isNaN(final Object isNaN) {
        this.isNaN = isNaN;
    }
    
    public Object G$isFinite() {
        return this.isFinite;
    }
    
    public void S$isFinite(final Object isFinite) {
        this.isFinite = isFinite;
    }
    
    public Object G$encodeURI() {
        return this.encodeURI;
    }
    
    public void S$encodeURI(final Object encodeURI) {
        this.encodeURI = encodeURI;
    }
    
    public Object G$encodeURIComponent() {
        return this.encodeURIComponent;
    }
    
    public void S$encodeURIComponent(final Object encodeURIComponent) {
        this.encodeURIComponent = encodeURIComponent;
    }
    
    public Object G$decodeURI() {
        return this.decodeURI;
    }
    
    public void S$decodeURI(final Object decodeURI) {
        this.decodeURI = decodeURI;
    }
    
    public Object G$decodeURIComponent() {
        return this.decodeURIComponent;
    }
    
    public void S$decodeURIComponent(final Object decodeURIComponent) {
        this.decodeURIComponent = decodeURIComponent;
    }
    
    public Object G$escape() {
        return this.escape;
    }
    
    public void S$escape(final Object escape) {
        this.escape = escape;
    }
    
    public Object G$unescape() {
        return this.unescape;
    }
    
    public void S$unescape(final Object unescape) {
        this.unescape = unescape;
    }
    
    public Object G$print() {
        return this.print;
    }
    
    public void S$print(final Object print) {
        this.print = print;
    }
    
    public Object G$load() {
        return this.load;
    }
    
    public void S$load(final Object load) {
        this.load = load;
    }
    
    public Object G$loadWithNewGlobal() {
        return this.loadWithNewGlobal;
    }
    
    public void S$loadWithNewGlobal(final Object loadWithNewGlobal) {
        this.loadWithNewGlobal = loadWithNewGlobal;
    }
    
    public Object G$exit() {
        return this.exit;
    }
    
    public void S$exit(final Object exit) {
        this.exit = exit;
    }
    
    public Object G$quit() {
        return this.quit;
    }
    
    public void S$quit(final Object quit) {
        this.quit = quit;
    }
    
    public double G$NaN() {
        return Global.NaN;
    }
    
    public double G$Infinity() {
        return Global.Infinity;
    }
    
    public Object G$undefined() {
        return Global.undefined;
    }
    
    public Object G$eval() {
        return this.eval;
    }
    
    public void S$eval(final Object eval) {
        this.eval = eval;
    }
    
    public Object G$object() {
        return this.object;
    }
    
    public void S$object(final Object object) {
        this.object = object;
    }
    
    public Object G$function() {
        return this.function;
    }
    
    public void S$function(final Object function) {
        this.function = function;
    }
    
    public Object G$array() {
        return this.array;
    }
    
    public void S$array(final Object array) {
        this.array = array;
    }
    
    public Object G$string() {
        return this.string;
    }
    
    public void S$string(final Object string) {
        this.string = string;
    }
    
    public Object G$_boolean() {
        return this._boolean;
    }
    
    public void S$_boolean(final Object boolean1) {
        this._boolean = boolean1;
    }
    
    public Object G$number() {
        return this.number;
    }
    
    public void S$number(final Object number) {
        this.number = number;
    }
    
    public Object G$math() {
        return this.math;
    }
    
    public void S$math(final Object math) {
        this.math = math;
    }
    
    public Object G$error() {
        return this.error;
    }
    
    public void S$error(final Object error) {
        this.error = error;
    }
    
    public Object G$referenceError() {
        return this.referenceError;
    }
    
    public void S$referenceError(final Object referenceError) {
        this.referenceError = referenceError;
    }
    
    public Object G$syntaxError() {
        return this.syntaxError;
    }
    
    public void S$syntaxError(final Object syntaxError) {
        this.syntaxError = syntaxError;
    }
    
    public Object G$typeError() {
        return this.typeError;
    }
    
    public void S$typeError(final Object typeError) {
        this.typeError = typeError;
    }
    
    public Object G$packages() {
        return this.packages;
    }
    
    public void S$packages(final Object packages) {
        this.packages = packages;
    }
    
    public Object G$com() {
        return this.com;
    }
    
    public void S$com(final Object com) {
        this.com = com;
    }
    
    public Object G$edu() {
        return this.edu;
    }
    
    public void S$edu(final Object edu) {
        this.edu = edu;
    }
    
    public Object G$java() {
        return this.java;
    }
    
    public void S$java(final Object java) {
        this.java = java;
    }
    
    public Object G$javafx() {
        return this.javafx;
    }
    
    public void S$javafx(final Object javafx) {
        this.javafx = javafx;
    }
    
    public Object G$javax() {
        return this.javax;
    }
    
    public void S$javax(final Object javax) {
        this.javax = javax;
    }
    
    public Object G$org() {
        return this.org;
    }
    
    public void S$org(final Object org) {
        this.org = org;
    }
    
    public Object G$__FILE__() {
        return Global.__FILE__;
    }
    
    public Object G$__DIR__() {
        return Global.__DIR__;
    }
    
    public Object G$__LINE__() {
        return Global.__LINE__;
    }
    
    private static class LexicalScope extends ScriptObject
    {
        LexicalScope(final Global global) {
            super(global, PropertyMap.newMap());
        }
        
        @Override
        protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
            return filterInvocation(super.findGetMethod(desc, request, operator));
        }
        
        @Override
        protected GuardedInvocation findSetMethod(final CallSiteDescriptor desc, final LinkRequest request) {
            return filterInvocation(super.findSetMethod(desc, request));
        }
        
        @Override
        protected PropertyMap addBoundProperty(final PropertyMap propMap, final ScriptObject source, final Property property, final boolean extensible) {
            return super.addBoundProperty(propMap, source, property, extensible);
        }
        
        private static GuardedInvocation filterInvocation(final GuardedInvocation invocation) {
            final MethodType type = invocation.getInvocation().type();
            return invocation.asType(type.changeParameterType(0, (Class<?>)Object.class)).filterArguments(0, Global.LEXICAL_SCOPE_FILTER);
        }
    }
}
