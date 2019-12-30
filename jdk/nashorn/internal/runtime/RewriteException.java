// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.lookup.MethodHandleFactory;
import java.io.ObjectInputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.types.Type;
import java.lang.reflect.Array;
import jdk.nashorn.internal.objects.Global;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;

public final class RewriteException extends Exception
{
    private static final MethodHandleFunctionality MH;
    private ScriptObject runtimeScope;
    private Object[] byteCodeSlots;
    private final int[] previousContinuationEntryPoints;
    public static final CompilerConstants.Call GET_BYTECODE_SLOTS;
    public static final CompilerConstants.Call GET_PROGRAM_POINT;
    public static final CompilerConstants.Call GET_RETURN_VALUE;
    public static final CompilerConstants.Call BOOTSTRAP;
    private static final CompilerConstants.Call POPULATE_ARRAY;
    public static final CompilerConstants.Call TO_LONG_ARRAY;
    public static final CompilerConstants.Call TO_DOUBLE_ARRAY;
    public static final CompilerConstants.Call TO_OBJECT_ARRAY;
    public static final CompilerConstants.Call INSTANCE_OR_NULL;
    public static final CompilerConstants.Call ASSERT_ARRAY_LENGTH;
    
    private RewriteException(final UnwarrantedOptimismException e, final Object[] byteCodeSlots, final String[] byteCodeSymbolNames, final int[] previousContinuationEntryPoints) {
        super("", e, false, Context.DEBUG);
        this.byteCodeSlots = byteCodeSlots;
        this.runtimeScope = mergeSlotsWithScope(byteCodeSlots, byteCodeSymbolNames);
        this.previousContinuationEntryPoints = previousContinuationEntryPoints;
    }
    
    public static RewriteException create(final UnwarrantedOptimismException e, final Object[] byteCodeSlots, final String[] byteCodeSymbolNames) {
        return create(e, byteCodeSlots, byteCodeSymbolNames, null);
    }
    
    public static RewriteException create(final UnwarrantedOptimismException e, final Object[] byteCodeSlots, final String[] byteCodeSymbolNames, final int[] previousContinuationEntryPoints) {
        return new RewriteException(e, byteCodeSlots, byteCodeSymbolNames, previousContinuationEntryPoints);
    }
    
    public static CallSite populateArrayBootstrap(final MethodHandles.Lookup lookup, final String name, final MethodType type, final int startIndex) {
        MethodHandle mh = RewriteException.POPULATE_ARRAY.methodHandle();
        mh = RewriteException.MH.insertArguments(mh, 1, startIndex);
        mh = RewriteException.MH.asCollector(mh, Object[].class, type.parameterCount() - 1);
        mh = RewriteException.MH.asType(mh, type);
        return new ConstantCallSite(mh);
    }
    
    private static ScriptObject mergeSlotsWithScope(final Object[] byteCodeSlots, final String[] byteCodeSymbolNames) {
        final ScriptObject locals = Global.newEmptyInstance();
        final int l = Math.min(byteCodeSlots.length, byteCodeSymbolNames.length);
        ScriptObject runtimeScope = null;
        final String scopeName = CompilerConstants.SCOPE.symbolName();
        for (int i = 0; i < l; ++i) {
            final String name = byteCodeSymbolNames[i];
            final Object value = byteCodeSlots[i];
            if (scopeName.equals(name)) {
                assert runtimeScope == null;
                runtimeScope = (ScriptObject)value;
            }
            else if (name != null) {
                locals.set(name, value, 2);
            }
        }
        locals.setProto(runtimeScope);
        return locals;
    }
    
    public static Object[] populateArray(final Object[] arrayToBePopluated, final int startIndex, final Object[] items) {
        System.arraycopy(items, 0, arrayToBePopluated, startIndex, items.length);
        return arrayToBePopluated;
    }
    
    public static long[] toLongArray(final Object obj, final RewriteException e) {
        if (obj instanceof long[]) {
            return (long[])obj;
        }
        assert obj instanceof int[];
        final int[] in = (int[])obj;
        final long[] out = new long[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return e.replaceByteCodeValue(in, out);
    }
    
    public static double[] toDoubleArray(final Object obj, final RewriteException e) {
        if (obj instanceof double[]) {
            return (double[])obj;
        }
        assert obj instanceof int[] || obj instanceof long[];
        final int l = Array.getLength(obj);
        final double[] out = new double[l];
        for (int i = 0; i < l; ++i) {
            out[i] = Array.getDouble(obj, i);
        }
        return e.replaceByteCodeValue(obj, out);
    }
    
    public static Object[] toObjectArray(final Object obj, final RewriteException e) {
        if (obj instanceof Object[]) {
            return (Object[])obj;
        }
        assert obj instanceof int[] || obj instanceof double[] : obj + " is " + obj.getClass().getName();
        final int l = Array.getLength(obj);
        final Object[] out = new Object[l];
        for (int i = 0; i < l; ++i) {
            out[i] = Array.get(obj, i);
        }
        return e.replaceByteCodeValue(obj, out);
    }
    
    public static Object instanceOrNull(final Object obj, final Class<?> clazz) {
        return clazz.isInstance(obj) ? obj : null;
    }
    
    public static void assertArrayLength(final Object[] arr, final int length) {
        int i = arr.length;
        while (i-- > length) {
            if (arr[i] != ScriptRuntime.UNDEFINED) {
                throw new AssertionError((Object)String.format("Expected array length %d, but it is %d", length, i + 1));
            }
        }
    }
    
    private <T> T replaceByteCodeValue(final Object in, final T out) {
        for (int i = 0; i < this.byteCodeSlots.length; ++i) {
            if (this.byteCodeSlots[i] == in) {
                this.byteCodeSlots[i] = out;
            }
        }
        return out;
    }
    
    private UnwarrantedOptimismException getUOE() {
        return (UnwarrantedOptimismException)this.getCause();
    }
    
    public Object getReturnValueDestructive() {
        assert this.byteCodeSlots != null;
        this.byteCodeSlots = null;
        this.runtimeScope = null;
        return this.getUOE().getReturnValueDestructive();
    }
    
    Object getReturnValueNonDestructive() {
        return this.getUOE().getReturnValueNonDestructive();
    }
    
    public Type getReturnType() {
        return this.getUOE().getReturnType();
    }
    
    public int getProgramPoint() {
        return this.getUOE().getProgramPoint();
    }
    
    public Object[] getByteCodeSlots() {
        return (Object[])((this.byteCodeSlots == null) ? null : ((Object[])this.byteCodeSlots.clone()));
    }
    
    public int[] getPreviousContinuationEntryPoints() {
        return (int[])((this.previousContinuationEntryPoints == null) ? null : ((int[])this.previousContinuationEntryPoints.clone()));
    }
    
    public ScriptObject getRuntimeScope() {
        return this.runtimeScope;
    }
    
    private static String stringify(final Object returnValue) {
        if (returnValue == null) {
            return "null";
        }
        String str = returnValue.toString();
        if (returnValue instanceof String) {
            str = '\'' + str + '\'';
        }
        else if (returnValue instanceof Double) {
            str += 'd';
        }
        else if (returnValue instanceof Long) {
            str += 'l';
        }
        return str;
    }
    
    @Override
    public String getMessage() {
        return this.getMessage(false);
    }
    
    public String getMessageShort() {
        return this.getMessage(true);
    }
    
    private String getMessage(final boolean isShort) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[pp=").append(this.getProgramPoint()).append(", ");
        if (!isShort) {
            final Object[] slots = this.byteCodeSlots;
            if (slots != null) {
                sb.append("slots=").append(Arrays.asList(slots)).append(", ");
            }
        }
        sb.append("type=").append(this.getReturnType()).append(", ");
        sb.append("value=").append(stringify(this.getReturnValueNonDestructive())).append(")]");
        return sb.toString();
    }
    
    private void writeObject(final ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
    
    private void readObject(final ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
    
    static {
        MH = MethodHandleFactory.getFunctionality();
        GET_BYTECODE_SLOTS = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getByteCodeSlots", Object[].class, (Class<?>[])new Class[0]);
        GET_PROGRAM_POINT = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getProgramPoint", Integer.TYPE, (Class<?>[])new Class[0]);
        GET_RETURN_VALUE = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getReturnValueDestructive", Object.class, (Class<?>[])new Class[0]);
        BOOTSTRAP = CompilerConstants.staticCallNoLookup(RewriteException.class, "populateArrayBootstrap", CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Integer.TYPE);
        POPULATE_ARRAY = CompilerConstants.staticCall(MethodHandles.lookup(), RewriteException.class, "populateArray", Object[].class, Object[].class, Integer.TYPE, Object[].class);
        TO_LONG_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toLongArray", long[].class, Object.class, RewriteException.class);
        TO_DOUBLE_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toDoubleArray", double[].class, Object.class, RewriteException.class);
        TO_OBJECT_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toObjectArray", Object[].class, Object.class, RewriteException.class);
        INSTANCE_OR_NULL = CompilerConstants.staticCallNoLookup(RewriteException.class, "instanceOrNull", Object.class, Object.class, Class.class);
        ASSERT_ARRAY_LENGTH = CompilerConstants.staticCallNoLookup(RewriteException.class, "assertArrayLength", Void.TYPE, Object[].class, Integer.TYPE);
    }
}
