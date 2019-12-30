// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collections;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.Context;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.internal.runtime.ListAdapter;
import java.util.Collection;
import java.util.Queue;
import java.util.Deque;
import java.util.List;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeJava
{
    private static PropertyMap $nasgenmap$;
    
    private NativeJava() {
        throw new UnsupportedOperationException();
    }
    
    public static boolean isType(final Object self, final Object type) {
        return type instanceof StaticClass;
    }
    
    public static Object synchronizedFunc(final Object self, final Object func, final Object obj) {
        if (func instanceof ScriptFunction) {
            return ((ScriptFunction)func).createSynchronized(obj);
        }
        throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(func));
    }
    
    public static boolean isJavaMethod(final Object self, final Object obj) {
        return Bootstrap.isDynamicMethod(obj);
    }
    
    public static boolean isJavaFunction(final Object self, final Object obj) {
        return Bootstrap.isCallable(obj) && !(obj instanceof ScriptFunction);
    }
    
    public static boolean isJavaObject(final Object self, final Object obj) {
        return obj != null && !(obj instanceof ScriptObject);
    }
    
    public static boolean isScriptObject(final Object self, final Object obj) {
        return obj instanceof ScriptObject;
    }
    
    public static boolean isScriptFunction(final Object self, final Object obj) {
        return obj instanceof ScriptFunction;
    }
    
    public static Object type(final Object self, final Object objTypeName) throws ClassNotFoundException {
        return type(objTypeName);
    }
    
    private static StaticClass type(final Object objTypeName) throws ClassNotFoundException {
        return StaticClass.forClass(type(JSType.toString(objTypeName)));
    }
    
    private static Class<?> type(final String typeName) throws ClassNotFoundException {
        if (typeName.endsWith("[]")) {
            return arrayType(typeName);
        }
        return simpleType(typeName);
    }
    
    public static Object typeName(final Object self, final Object type) {
        if (type instanceof StaticClass) {
            return ((StaticClass)type).getRepresentedClass().getName();
        }
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object to(final Object self, final Object obj, final Object objType) throws ClassNotFoundException {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof ScriptObject) && !(obj instanceof JSObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        Class<?> targetClass;
        if (objType == ScriptRuntime.UNDEFINED) {
            targetClass = Object[].class;
        }
        else {
            StaticClass targetType;
            if (objType instanceof StaticClass) {
                targetType = (StaticClass)objType;
            }
            else {
                targetType = type(objType);
            }
            targetClass = targetType.getRepresentedClass();
        }
        if (targetClass.isArray()) {
            try {
                return JSType.toJavaArray(obj, targetClass.getComponentType());
            }
            catch (Exception exp) {
                throw ECMAErrors.typeError(exp, "java.array.conversion.failed", targetClass.getName());
            }
        }
        if (targetClass == List.class || targetClass == Deque.class || targetClass == Queue.class || targetClass == Collection.class) {
            return ListAdapter.create(obj);
        }
        throw ECMAErrors.typeError("unsupported.java.to.type", targetClass.getName());
    }
    
    public static NativeArray from(final Object self, final Object objArray) {
        if (objArray == null) {
            return null;
        }
        if (objArray instanceof Collection) {
            return new NativeArray(((Collection)objArray).toArray());
        }
        if (objArray instanceof Object[]) {
            return new NativeArray(((Object[])objArray).clone());
        }
        if (objArray instanceof int[]) {
            return new NativeArray(((int[])objArray).clone());
        }
        if (objArray instanceof double[]) {
            return new NativeArray(((double[])objArray).clone());
        }
        if (objArray instanceof long[]) {
            return new NativeArray(((long[])objArray).clone());
        }
        if (objArray instanceof byte[]) {
            return new NativeArray(copyArray((byte[])objArray));
        }
        if (objArray instanceof short[]) {
            return new NativeArray(copyArray((short[])objArray));
        }
        if (objArray instanceof char[]) {
            return new NativeArray(copyArray((char[])objArray));
        }
        if (objArray instanceof float[]) {
            return new NativeArray(copyArray((float[])objArray));
        }
        if (objArray instanceof boolean[]) {
            return new NativeArray(copyArray((boolean[])objArray));
        }
        throw ECMAErrors.typeError("cant.convert.to.javascript.array", objArray.getClass().getName());
    }
    
    private static int[] copyArray(final byte[] in) {
        final int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }
    
    private static int[] copyArray(final short[] in) {
        final int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }
    
    private static int[] copyArray(final char[] in) {
        final int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }
    
    private static double[] copyArray(final float[] in) {
        final double[] out = new double[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }
    
    private static Object[] copyArray(final boolean[] in) {
        final Object[] out = new Object[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }
    
    private static Class<?> simpleType(final String typeName) throws ClassNotFoundException {
        final Class<?> primClass = TypeUtilities.getPrimitiveTypeByName(typeName);
        if (primClass != null) {
            return primClass;
        }
        final Context ctx = Global.getThisContext();
        try {
            return ctx.findClass(typeName);
        }
        catch (ClassNotFoundException e) {
            final StringBuilder nextName = new StringBuilder(typeName);
            int lastDot = nextName.length();
            while (true) {
                lastDot = nextName.lastIndexOf(".", lastDot - 1);
                if (lastDot == -1) {
                    break;
                }
                nextName.setCharAt(lastDot, '$');
                try {
                    return ctx.findClass(nextName.toString());
                }
                catch (ClassNotFoundException ex) {}
            }
            throw e;
        }
    }
    
    private static Class<?> arrayType(final String typeName) throws ClassNotFoundException {
        return Array.newInstance(type(typeName.substring(0, typeName.length() - 2)), 0).getClass();
    }
    
    public static Object extend(final Object self, final Object... types) {
        if (types == null || types.length == 0) {
            throw ECMAErrors.typeError("extend.expects.at.least.one.argument", new String[0]);
        }
        final int l = types.length;
        ScriptObject classOverrides;
        int typesLen;
        if (types[l - 1] instanceof ScriptObject) {
            classOverrides = (ScriptObject)types[l - 1];
            typesLen = l - 1;
            if (typesLen == 0) {
                throw ECMAErrors.typeError("extend.expects.at.least.one.type.argument", new String[0]);
            }
        }
        else {
            classOverrides = null;
            typesLen = l;
        }
        final Class<?>[] stypes = (Class<?>[])new Class[typesLen];
        try {
            for (int i = 0; i < typesLen; ++i) {
                stypes[i] = ((StaticClass)types[i]).getRepresentedClass();
            }
        }
        catch (ClassCastException e) {
            throw ECMAErrors.typeError("extend.expects.java.types", new String[0]);
        }
        MethodHandles.Lookup lookup;
        if (self instanceof MethodHandles.Lookup) {
            lookup = (MethodHandles.Lookup)self;
        }
        else {
            lookup = MethodHandles.publicLookup();
        }
        return JavaAdapterFactory.getAdapterClassFor(stypes, classOverrides, lookup);
    }
    
    public static Object _super(final Object self, final Object adapter) {
        return Bootstrap.createSuperAdapter(adapter);
    }
    
    public static Object asJSONCompatible(final Object self, final Object obj) {
        return ScriptObjectMirror.wrapAsJSONCompatible(obj, Context.getGlobal());
    }
    
    static {
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeJava.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
