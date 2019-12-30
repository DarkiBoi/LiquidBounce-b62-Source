// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.arrays.IteratorAction;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.arrays.NumericElements;
import jdk.nashorn.internal.runtime.arrays.IntElements;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.OptimisticBuiltins;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeArray extends ScriptObject implements OptimisticBuiltins
{
    private static final Object JOIN;
    private static final Object EVERY_CALLBACK_INVOKER;
    private static final Object SOME_CALLBACK_INVOKER;
    private static final Object FOREACH_CALLBACK_INVOKER;
    private static final Object MAP_CALLBACK_INVOKER;
    private static final Object FILTER_CALLBACK_INVOKER;
    private static final Object REDUCE_CALLBACK_INVOKER;
    private static final Object CALL_CMP;
    private static final Object TO_LOCALE_STRING;
    private static PropertyMap $nasgenmap$;
    
    NativeArray() {
        this(ArrayData.initialArray());
    }
    
    NativeArray(final long length) {
        this(ArrayData.allocate(length));
    }
    
    NativeArray(final int[] array) {
        this(ArrayData.allocate(array));
    }
    
    NativeArray(final double[] array) {
        this(ArrayData.allocate(array));
    }
    
    NativeArray(final long[] array) {
        this(ArrayData.allocate(array.length));
        ArrayData arrayData = this.getArray();
        Class<?> widest = Integer.TYPE;
        for (int index = 0; index < array.length; ++index) {
            final long value = array[index];
            if (widest == Integer.TYPE && JSType.isRepresentableAsInt(value)) {
                arrayData = arrayData.set(index, (int)value, false);
            }
            else if (widest != Object.class && JSType.isRepresentableAsDouble(value)) {
                arrayData = arrayData.set(index, (double)value, false);
                widest = Double.TYPE;
            }
            else {
                arrayData = arrayData.set(index, (Object)value, false);
                widest = Object.class;
            }
        }
        this.setArray(arrayData);
    }
    
    NativeArray(final Object[] array) {
        this(ArrayData.allocate(array.length));
        ArrayData arrayData = this.getArray();
        for (int index = 0; index < array.length; ++index) {
            final Object value = array[index];
            if (value == ScriptRuntime.EMPTY) {
                arrayData = arrayData.delete(index);
            }
            else {
                arrayData = arrayData.set(index, value, false);
            }
        }
        this.setArray(arrayData);
    }
    
    NativeArray(final ArrayData arrayData) {
        this(arrayData, Global.instance());
    }
    
    NativeArray(final ArrayData arrayData, final Global global) {
        super(global.getArrayPrototype(), NativeArray.$nasgenmap$);
        this.setArray(arrayData);
        this.setIsArray();
    }
    
    @Override
    protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
        final GuardedInvocation inv = this.getArray().findFastGetMethod(this.getArray().getClass(), desc, request, operator);
        if (inv != null) {
            return inv;
        }
        return super.findGetMethod(desc, request, operator);
    }
    
    @Override
    protected GuardedInvocation findGetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = this.getArray().findFastGetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findGetIndexMethod(desc, request);
    }
    
    @Override
    protected GuardedInvocation findSetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = this.getArray().findFastSetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findSetIndexMethod(desc, request);
    }
    
    private static InvokeByName getJOIN() {
        return Global.instance().getInvokeByName(NativeArray.JOIN, new Callable<InvokeByName>() {
            @Override
            public InvokeByName call() {
                return new InvokeByName("join", ScriptObject.class);
            }
        });
    }
    
    private static MethodHandle createIteratorCallbackInvoker(final Object key, final Class<?> rtype) {
        return Global.instance().getDynamicInvoker(key, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", rtype, Object.class, Object.class, Object.class, Double.TYPE, Object.class);
            }
        });
    }
    
    private static MethodHandle getEVERY_CALLBACK_INVOKER() {
        return createIteratorCallbackInvoker(NativeArray.EVERY_CALLBACK_INVOKER, Boolean.TYPE);
    }
    
    private static MethodHandle getSOME_CALLBACK_INVOKER() {
        return createIteratorCallbackInvoker(NativeArray.SOME_CALLBACK_INVOKER, Boolean.TYPE);
    }
    
    private static MethodHandle getFOREACH_CALLBACK_INVOKER() {
        return createIteratorCallbackInvoker(NativeArray.FOREACH_CALLBACK_INVOKER, Void.TYPE);
    }
    
    private static MethodHandle getMAP_CALLBACK_INVOKER() {
        return createIteratorCallbackInvoker(NativeArray.MAP_CALLBACK_INVOKER, Object.class);
    }
    
    private static MethodHandle getFILTER_CALLBACK_INVOKER() {
        return createIteratorCallbackInvoker(NativeArray.FILTER_CALLBACK_INVOKER, Boolean.TYPE);
    }
    
    private static MethodHandle getREDUCE_CALLBACK_INVOKER() {
        return Global.instance().getDynamicInvoker(NativeArray.REDUCE_CALLBACK_INVOKER, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Undefined.class, Object.class, Object.class, Double.TYPE, Object.class);
            }
        });
    }
    
    private static MethodHandle getCALL_CMP() {
        return Global.instance().getDynamicInvoker(NativeArray.CALL_CMP, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Double.TYPE, Object.class, Object.class, Object.class, Object.class);
            }
        });
    }
    
    private static InvokeByName getTO_LOCALE_STRING() {
        return Global.instance().getInvokeByName(NativeArray.TO_LOCALE_STRING, new Callable<InvokeByName>() {
            @Override
            public InvokeByName call() {
                return new InvokeByName("toLocaleString", ScriptObject.class, String.class, (Class<?>[])new Class[0]);
            }
        });
    }
    
    @Override
    public String getClassName() {
        return "Array";
    }
    
    @Override
    public Object getLength() {
        final long length = this.getArray().length();
        assert length >= 0L;
        if (length <= 2147483647L) {
            return (int)length;
        }
        return length;
    }
    
    private boolean defineLength(final long oldLen, final PropertyDescriptor oldLenDesc, final PropertyDescriptor desc, final boolean reject) {
        if (!desc.has("value")) {
            return super.defineOwnProperty("length", desc, reject);
        }
        final PropertyDescriptor newLenDesc = desc;
        final long newLen = validLength(newLenDesc.getValue());
        newLenDesc.setValue(JSType.toNarrowestNumber(newLen));
        if (newLen >= oldLen) {
            return super.defineOwnProperty("length", newLenDesc, reject);
        }
        if (!oldLenDesc.isWritable()) {
            if (reject) {
                throw ECMAErrors.typeError("property.not.writable", "length", ScriptRuntime.safeToString(this));
            }
            return false;
        }
        else {
            final boolean newWritable = !newLenDesc.has("writable") || newLenDesc.isWritable();
            if (!newWritable) {
                newLenDesc.setWritable(true);
            }
            final boolean succeeded = super.defineOwnProperty("length", newLenDesc, reject);
            if (!succeeded) {
                return false;
            }
            long o = oldLen;
            while (newLen < o) {
                --o;
                final boolean deleteSucceeded = this.delete((double)o, false);
                if (!deleteSucceeded) {
                    newLenDesc.setValue(o + 1L);
                    if (!newWritable) {
                        newLenDesc.setWritable(false);
                    }
                    super.defineOwnProperty("length", newLenDesc, false);
                    if (reject) {
                        throw ECMAErrors.typeError("property.not.writable", "length", ScriptRuntime.safeToString(this));
                    }
                    return false;
                }
            }
            if (!newWritable) {
                final ScriptObject newDesc = Global.newEmptyInstance();
                newDesc.set("writable", false, 0);
                return super.defineOwnProperty("length", newDesc, false);
            }
            return true;
        }
    }
    
    @Override
    public boolean defineOwnProperty(final String key, final Object propertyDesc, final boolean reject) {
        final PropertyDescriptor desc = ScriptObject.toPropertyDescriptor(Global.instance(), propertyDesc);
        final PropertyDescriptor oldLenDesc = (PropertyDescriptor)super.getOwnPropertyDescriptor("length");
        final long oldLen = JSType.toUint32(oldLenDesc.getValue());
        if ("length".equals(key)) {
            final boolean result = this.defineLength(oldLen, oldLenDesc, desc, reject);
            if (desc.has("writable") && !desc.isWritable()) {
                this.setIsLengthNotWritable();
            }
            return result;
        }
        final int index = ArrayIndex.getArrayIndex(key);
        if (!ArrayIndex.isValidArrayIndex(index)) {
            return super.defineOwnProperty(key, desc, reject);
        }
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex >= oldLen && !oldLenDesc.isWritable()) {
            if (reject) {
                throw ECMAErrors.typeError("property.not.writable", Long.toString(longIndex), ScriptRuntime.safeToString(this));
            }
            return false;
        }
        else {
            final boolean succeeded = super.defineOwnProperty(key, desc, false);
            if (succeeded) {
                if (longIndex >= oldLen) {
                    oldLenDesc.setValue(longIndex + 1L);
                    super.defineOwnProperty("length", oldLenDesc, false);
                }
                return true;
            }
            if (reject) {
                throw ECMAErrors.typeError("cant.redefine.property", key, ScriptRuntime.safeToString(this));
            }
            return false;
        }
    }
    
    @Override
    public final void defineOwnProperty(final int index, final Object value) {
        assert ArrayIndex.isValidArrayIndex(index) : "invalid array index";
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex >= this.getArray().length()) {
            this.setArray(this.getArray().ensure(longIndex));
        }
        this.setArray(this.getArray().set(index, value, false));
    }
    
    public Object[] asObjectArray() {
        return this.getArray().asObjectArray();
    }
    
    @Override
    public void setIsLengthNotWritable() {
        super.setIsLengthNotWritable();
        this.setArray(ArrayData.setIsLengthNotWritable(this.getArray()));
    }
    
    public static boolean isArray(final Object self, final Object arg) {
        return ScriptObject.isArray(arg) || (arg instanceof JSObject && ((JSObject)arg).isArray());
    }
    
    public static Object length(final Object self) {
        if (!ScriptObject.isArray(self)) {
            return 0;
        }
        final long length = ((ScriptObject)self).getArray().length();
        assert length >= 0L;
        if (length <= 2147483647L) {
            return (int)length;
        }
        return length;
    }
    
    public static void length(final Object self, final Object length) {
        if (ScriptObject.isArray(self)) {
            ((ScriptObject)self).setLength(validLength(length));
        }
    }
    
    public static Object getProtoLength(final Object self) {
        return length(self);
    }
    
    public static void setProtoLength(final Object self, final Object length) {
        length(self, length);
    }
    
    static long validLength(final Object length) {
        final double doubleLength = JSType.toNumber(length);
        if (doubleLength != JSType.toUint32(length)) {
            throw ECMAErrors.rangeError("inappropriate.array.length", ScriptRuntime.safeToString(length));
        }
        return (long)doubleLength;
    }
    
    public static Object toString(final Object self) {
        final Object obj = Global.toObject(self);
        if (obj instanceof ScriptObject) {
            final InvokeByName joinInvoker = getJOIN();
            final ScriptObject sobj = (ScriptObject)obj;
            try {
                final Object join = joinInvoker.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(join)) {
                    return joinInvoker.getInvoker().invokeExact(join, sobj);
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
        }
        return ScriptRuntime.builtinObjectToString(self);
    }
    
    public static Object assertNumeric(final Object self) {
        if (!(self instanceof NativeArray) || !((NativeArray)self).getArray().getOptimisticType().isNumeric()) {
            throw ECMAErrors.typeError("not.a.numeric.array", ScriptRuntime.safeToString(self));
        }
        return Boolean.TRUE;
    }
    
    public static String toLocaleString(final Object self) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(self, true);
        while (iter.hasNext()) {
            final Object obj = iter.next();
            if (obj != null && obj != ScriptRuntime.UNDEFINED) {
                final Object val = JSType.toScriptObject(obj);
                try {
                    if (val instanceof ScriptObject) {
                        final InvokeByName localeInvoker = getTO_LOCALE_STRING();
                        final ScriptObject sobj = (ScriptObject)val;
                        final Object toLocaleString = localeInvoker.getGetter().invokeExact(sobj);
                        if (!Bootstrap.isCallable(toLocaleString)) {
                            throw ECMAErrors.typeError("not.a.function", "toLocaleString");
                        }
                        sb.append(localeInvoker.getInvoker().invokeExact(toLocaleString, sobj));
                    }
                }
                catch (Error | RuntimeException error) {
                    final Throwable t2;
                    final Throwable t = t2;
                    throw t;
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    public static NativeArray construct(final boolean newObj, final Object self, final Object... args) {
        switch (args.length) {
            case 0: {
                return new NativeArray(0L);
            }
            case 1: {
                final Object len = args[0];
                if (!(len instanceof Number)) {
                    return new NativeArray(new Object[] { args[0] });
                }
                if (len instanceof Integer || len instanceof Long) {
                    final long length = ((Number)len).longValue();
                    if (length >= 0L && length < 4294967295L) {
                        return new NativeArray(length);
                    }
                }
                final long length = JSType.toUint32(len);
                final double numberLength = ((Number)len).doubleValue();
                if (length != numberLength) {
                    throw ECMAErrors.rangeError("inappropriate.array.length", JSType.toString(numberLength));
                }
                return new NativeArray(length);
            }
            default: {
                return new NativeArray(args);
            }
        }
    }
    
    public static NativeArray construct(final boolean newObj, final Object self) {
        return new NativeArray(0L);
    }
    
    public static Object construct(final boolean newObj, final Object self, final boolean element) {
        return new NativeArray(new Object[] { element });
    }
    
    public static NativeArray construct(final boolean newObj, final Object self, final int length) {
        if (length >= 0) {
            return new NativeArray(length);
        }
        return construct(newObj, self, new Object[] { length });
    }
    
    public static NativeArray construct(final boolean newObj, final Object self, final long length) {
        if (length >= 0L && length <= 4294967295L) {
            return new NativeArray(length);
        }
        return construct(newObj, self, new Object[] { length });
    }
    
    public static NativeArray construct(final boolean newObj, final Object self, final double length) {
        final long uint32length = JSType.toUint32(length);
        if (uint32length == length) {
            return new NativeArray(uint32length);
        }
        return construct(newObj, self, new Object[] { length });
    }
    
    public static NativeArray concat(final Object self, final int arg) {
        final ContinuousArrayData newData = getContinuousArrayDataCCE(self, Integer.class).copy();
        newData.fastPush(arg);
        return new NativeArray(newData);
    }
    
    public static NativeArray concat(final Object self, final long arg) {
        final ContinuousArrayData newData = getContinuousArrayDataCCE(self, Long.class).copy();
        newData.fastPush(arg);
        return new NativeArray(newData);
    }
    
    public static NativeArray concat(final Object self, final double arg) {
        final ContinuousArrayData newData = getContinuousArrayDataCCE(self, Double.class).copy();
        newData.fastPush(arg);
        return new NativeArray(newData);
    }
    
    public static NativeArray concat(final Object self, final Object arg) {
        final ContinuousArrayData selfData = getContinuousArrayDataCCE(self);
        ContinuousArrayData newData;
        if (arg instanceof NativeArray) {
            final ContinuousArrayData argData = (ContinuousArrayData)((NativeArray)arg).getArray();
            if (argData.isEmpty()) {
                newData = selfData.copy();
            }
            else if (selfData.isEmpty()) {
                newData = argData.copy();
            }
            else {
                final Class<?> widestElementType = selfData.widest(argData).getBoxedElementType();
                newData = ((ContinuousArrayData)selfData.convert(widestElementType)).fastConcat((ContinuousArrayData)argData.convert(widestElementType));
            }
        }
        else {
            newData = getContinuousArrayDataCCE(self, Object.class).copy();
            newData.fastPush(arg);
        }
        return new NativeArray(newData);
    }
    
    public static NativeArray concat(final Object self, final Object... args) {
        final ArrayList<Object> list = new ArrayList<Object>();
        concatToList(list, Global.toObject(self));
        for (final Object obj : args) {
            concatToList(list, obj);
        }
        return new NativeArray(list.toArray());
    }
    
    private static void concatToList(final ArrayList<Object> list, final Object obj) {
        final boolean isScriptArray = ScriptObject.isArray(obj);
        final boolean isScriptObject = isScriptArray || obj instanceof ScriptObject;
        if (isScriptArray || obj instanceof Iterable || (obj != null && obj.getClass().isArray())) {
            final Iterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(obj, true);
            if (iter.hasNext()) {
                int i = 0;
                while (iter.hasNext()) {
                    final Object value = iter.next();
                    final boolean lacksIndex = obj != null && !((ScriptObject)obj).has(i);
                    if (value == ScriptRuntime.UNDEFINED && isScriptObject && lacksIndex) {
                        list.add(ScriptRuntime.EMPTY);
                    }
                    else {
                        list.add(value);
                    }
                    ++i;
                }
            }
            else if (!isScriptArray) {
                list.add(obj);
            }
        }
        else {
            list.add(obj);
        }
    }
    
    public static String join(final Object self, final Object separator) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(self, true);
        final String sep = (separator == ScriptRuntime.UNDEFINED) ? "," : JSType.toString(separator);
        while (iter.hasNext()) {
            final Object obj = iter.next();
            if (obj != null && obj != ScriptRuntime.UNDEFINED) {
                sb.append(JSType.toString(obj));
            }
            if (iter.hasNext()) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }
    
    public static int popInt(final Object self) {
        return getContinuousNonEmptyArrayDataCCE(self, IntElements.class).fastPopInt();
    }
    
    public static double popDouble(final Object self) {
        return getContinuousNonEmptyArrayDataCCE(self, NumericElements.class).fastPopDouble();
    }
    
    public static Object popObject(final Object self) {
        return getContinuousArrayDataCCE(self, null).fastPopObject();
    }
    
    public static Object pop(final Object self) {
        try {
            final ScriptObject sobj = (ScriptObject)self;
            if (bulkable(sobj)) {
                return sobj.getArray().pop();
            }
            final long len = JSType.toUint32(sobj.getLength());
            if (len == 0L) {
                sobj.set("length", 0, 2);
                return ScriptRuntime.UNDEFINED;
            }
            final long index = len - 1L;
            final Object element = sobj.get((double)index);
            sobj.delete((double)index, true);
            sobj.set("length", (double)index, 2);
            return element;
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(self));
        }
    }
    
    public static double push(final Object self, final int arg) {
        return getContinuousArrayDataCCE(self, Integer.class).fastPush(arg);
    }
    
    public static double push(final Object self, final long arg) {
        return getContinuousArrayDataCCE(self, Long.class).fastPush(arg);
    }
    
    public static double push(final Object self, final double arg) {
        return getContinuousArrayDataCCE(self, Double.class).fastPush(arg);
    }
    
    public static double pushObject(final Object self, final Object arg) {
        return getContinuousArrayDataCCE(self, Object.class).fastPush(arg);
    }
    
    public static Object push(final Object self, final Object... args) {
        try {
            final ScriptObject sobj = (ScriptObject)self;
            if (bulkable(sobj) && sobj.getArray().length() + args.length <= 4294967295L) {
                final ArrayData newData = sobj.getArray().push(true, args);
                sobj.setArray(newData);
                return JSType.toNarrowestNumber(newData.length());
            }
            long len = JSType.toUint32(sobj.getLength());
            for (final Object element : args) {
                sobj.set((double)(len++), element, 2);
            }
            sobj.set("length", (double)len, 2);
            return JSType.toNarrowestNumber(len);
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError(Context.getGlobal(), e, "not.an.object", ScriptRuntime.safeToString(self));
        }
    }
    
    public static double push(final Object self, final Object arg) {
        try {
            final ScriptObject sobj = (ScriptObject)self;
            final ArrayData arrayData = sobj.getArray();
            final long length = arrayData.length();
            if (bulkable(sobj) && length < 4294967295L) {
                sobj.setArray(arrayData.push(true, arg));
                return (double)(length + 1L);
            }
            long len = JSType.toUint32(sobj.getLength());
            sobj.set((double)(len++), arg, 2);
            sobj.set("length", (double)len, 2);
            return (double)len;
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(self));
        }
    }
    
    public static Object reverse(final Object self) {
        try {
            final ScriptObject sobj = (ScriptObject)self;
            final long len = JSType.toUint32(sobj.getLength());
            for (long middle = len / 2L, lower = 0L; lower != middle; ++lower) {
                final long upper = len - lower - 1L;
                final Object lowerValue = sobj.get((double)lower);
                final Object upperValue = sobj.get((double)upper);
                final boolean lowerExists = sobj.has((double)lower);
                final boolean upperExists = sobj.has((double)upper);
                if (lowerExists && upperExists) {
                    sobj.set((double)lower, upperValue, 2);
                    sobj.set((double)upper, lowerValue, 2);
                }
                else if (!lowerExists && upperExists) {
                    sobj.set((double)lower, upperValue, 2);
                    sobj.delete((double)upper, true);
                }
                else if (lowerExists && !upperExists) {
                    sobj.delete((double)lower, true);
                    sobj.set((double)upper, lowerValue, 2);
                }
            }
            return sobj;
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(self));
        }
    }
    
    public static Object shift(final Object self) {
        final Object obj = Global.toObject(self);
        Object first = ScriptRuntime.UNDEFINED;
        if (!(obj instanceof ScriptObject)) {
            return first;
        }
        final ScriptObject sobj = (ScriptObject)obj;
        long len = JSType.toUint32(sobj.getLength());
        if (len > 0L) {
            first = sobj.get(0);
            if (bulkable(sobj)) {
                sobj.getArray().shiftLeft(1);
            }
            else {
                boolean hasPrevious = true;
                for (long k = 1L; k < len; ++k) {
                    final boolean hasCurrent = sobj.has((double)k);
                    if (hasCurrent) {
                        sobj.set((double)(k - 1L), sobj.get((double)k), 2);
                    }
                    else if (hasPrevious) {
                        sobj.delete((double)(k - 1L), true);
                    }
                    hasPrevious = hasCurrent;
                }
            }
            sobj.delete((double)(--len), true);
        }
        else {
            len = 0L;
        }
        sobj.set("length", (double)len, 2);
        return first;
    }
    
    public static Object slice(final Object self, final Object start, final Object end) {
        final Object obj = Global.toObject(self);
        if (!(obj instanceof ScriptObject)) {
            return ScriptRuntime.UNDEFINED;
        }
        final ScriptObject sobj = (ScriptObject)obj;
        final long len = JSType.toUint32(sobj.getLength());
        final long relativeStart = JSType.toLong(start);
        final long relativeEnd = (end == ScriptRuntime.UNDEFINED) ? len : JSType.toLong(end);
        long k = (relativeStart < 0L) ? Math.max(len + relativeStart, 0L) : Math.min(relativeStart, len);
        final long finale = (relativeEnd < 0L) ? Math.max(len + relativeEnd, 0L) : Math.min(relativeEnd, len);
        if (k >= finale) {
            return new NativeArray(0L);
        }
        if (bulkable(sobj)) {
            return new NativeArray(sobj.getArray().slice(k, finale));
        }
        final NativeArray copy = new NativeArray(finale - k);
        long n = 0L;
        while (k < finale) {
            if (sobj.has((double)k)) {
                copy.defineOwnProperty(ArrayIndex.getArrayIndex(n), sobj.get((double)k));
            }
            ++n;
            ++k;
        }
        return copy;
    }
    
    private static Object compareFunction(final Object comparefn) {
        if (comparefn == ScriptRuntime.UNDEFINED) {
            return null;
        }
        if (!Bootstrap.isCallable(comparefn)) {
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(comparefn));
        }
        return comparefn;
    }
    
    private static Object[] sort(final Object[] array, final Object comparefn) {
        final Object cmp = compareFunction(comparefn);
        final List<Object> list = Arrays.asList(array);
        final Object cmpThis = (cmp == null || Bootstrap.isStrictCallable(cmp)) ? ScriptRuntime.UNDEFINED : Global.instance();
        try {
            Collections.sort(list, new Comparator<Object>() {
                private final MethodHandle call_cmp = getCALL_CMP();
                
                @Override
                public int compare(final Object x, final Object y) {
                    if (x == ScriptRuntime.UNDEFINED && y == ScriptRuntime.UNDEFINED) {
                        return 0;
                    }
                    if (x == ScriptRuntime.UNDEFINED) {
                        return 1;
                    }
                    if (y == ScriptRuntime.UNDEFINED) {
                        return -1;
                    }
                    if (cmp != null) {
                        try {
                            return (int)Math.signum(this.call_cmp.invokeExact(cmp, cmpThis, x, y));
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
                    return JSType.toString(x).compareTo(JSType.toString(y));
                }
            });
        }
        catch (IllegalArgumentException ex) {}
        return list.toArray(new Object[array.length]);
    }
    
    public static ScriptObject sort(final Object self, final Object comparefn) {
        try {
            final ScriptObject sobj = (ScriptObject)self;
            final long len = JSType.toUint32(sobj.getLength());
            ArrayData array = sobj.getArray();
            if (len > 1L) {
                final ArrayList<Object> src = new ArrayList<Object>();
                final Iterator<Long> iter = array.indexIterator();
                while (iter.hasNext()) {
                    final long index = iter.next();
                    if (index >= len) {
                        break;
                    }
                    src.add(array.getObject((int)index));
                }
                final Object[] sorted = sort(src.toArray(), comparefn);
                for (int i = 0; i < sorted.length; ++i) {
                    array = array.set(i, sorted[i], true);
                }
                if (sorted.length != len) {
                    array = array.delete(sorted.length, len - 1L);
                }
                sobj.setArray(array);
            }
            return sobj;
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(self));
        }
    }
    
    public static Object splice(final Object self, final Object... args) {
        final Object obj = Global.toObject(self);
        if (!(obj instanceof ScriptObject)) {
            return ScriptRuntime.UNDEFINED;
        }
        final Object start = (args.length > 0) ? args[0] : ScriptRuntime.UNDEFINED;
        final Object deleteCount = (args.length > 1) ? args[1] : ScriptRuntime.UNDEFINED;
        Object[] items;
        if (args.length > 2) {
            items = new Object[args.length - 2];
            System.arraycopy(args, 2, items, 0, items.length);
        }
        else {
            items = ScriptRuntime.EMPTY_ARRAY;
        }
        final ScriptObject sobj = (ScriptObject)obj;
        final long len = JSType.toUint32(sobj.getLength());
        final long relativeStart = JSType.toLong(start);
        final long actualStart = (relativeStart < 0L) ? Math.max(len + relativeStart, 0L) : Math.min(relativeStart, len);
        final long actualDeleteCount = Math.min(Math.max(JSType.toLong(deleteCount), 0L), len - actualStart);
        NativeArray returnValue;
        if (actualStart <= 2147483647L && actualDeleteCount <= 2147483647L && bulkable(sobj)) {
            try {
                returnValue = new NativeArray(sobj.getArray().fastSplice((int)actualStart, (int)actualDeleteCount, items.length));
                for (int k = (int)actualStart, i = 0; i < items.length; ++i, ++k) {
                    sobj.defineOwnProperty(k, items[i]);
                }
            }
            catch (UnsupportedOperationException uoe) {
                returnValue = slowSplice(sobj, actualStart, actualDeleteCount, items, len);
            }
        }
        else {
            returnValue = slowSplice(sobj, actualStart, actualDeleteCount, items, len);
        }
        return returnValue;
    }
    
    private static NativeArray slowSplice(final ScriptObject sobj, final long start, final long deleteCount, final Object[] items, final long len) {
        final NativeArray array = new NativeArray(deleteCount);
        for (long k = 0L; k < deleteCount; ++k) {
            final long from = start + k;
            if (sobj.has((double)from)) {
                array.defineOwnProperty(ArrayIndex.getArrayIndex(k), sobj.get((double)from));
            }
        }
        if (items.length < deleteCount) {
            for (long k = start; k < len - deleteCount; ++k) {
                final long from = k + deleteCount;
                final long to = k + items.length;
                if (sobj.has((double)from)) {
                    sobj.set((double)to, sobj.get((double)from), 2);
                }
                else {
                    sobj.delete((double)to, true);
                }
            }
            for (long k = len; k > len - deleteCount + items.length; --k) {
                sobj.delete((double)(k - 1L), true);
            }
        }
        else if (items.length > deleteCount) {
            for (long k = len - deleteCount; k > start; --k) {
                final long from = k + deleteCount - 1L;
                final long to = k + items.length - 1L;
                if (sobj.has((double)from)) {
                    final Object fromValue = sobj.get((double)from);
                    sobj.set((double)to, fromValue, 2);
                }
                else {
                    sobj.delete((double)to, true);
                }
            }
        }
        long k = start;
        for (int i = 0; i < items.length; ++i, ++k) {
            sobj.set((double)k, items[i], 2);
        }
        final long newLength = len - deleteCount + items.length;
        sobj.set("length", (double)newLength, 2);
        return array;
    }
    
    public static Object unshift(final Object self, final Object... items) {
        final Object obj = Global.toObject(self);
        if (!(obj instanceof ScriptObject)) {
            return ScriptRuntime.UNDEFINED;
        }
        final ScriptObject sobj = (ScriptObject)obj;
        final long len = JSType.toUint32(sobj.getLength());
        if (items == null) {
            return ScriptRuntime.UNDEFINED;
        }
        if (bulkable(sobj)) {
            sobj.getArray().shiftRight(items.length);
            for (int j = 0; j < items.length; ++j) {
                sobj.setArray(sobj.getArray().set(j, items[j], true));
            }
        }
        else {
            for (long k = len; k > 0L; --k) {
                final long from = k - 1L;
                final long to = k + items.length - 1L;
                if (sobj.has((double)from)) {
                    final Object fromValue = sobj.get((double)from);
                    sobj.set((double)to, fromValue, 2);
                }
                else {
                    sobj.delete((double)to, true);
                }
            }
            for (int j = 0; j < items.length; ++j) {
                sobj.set(j, items[j], 2);
            }
        }
        final long newLength = len + items.length;
        sobj.set("length", (double)newLength, 2);
        return JSType.toNarrowestNumber(newLength);
    }
    
    public static double indexOf(final Object self, final Object searchElement, final Object fromIndex) {
        try {
            final ScriptObject sobj = (ScriptObject)Global.toObject(self);
            final long len = JSType.toUint32(sobj.getLength());
            if (len == 0L) {
                return -1.0;
            }
            final long n = JSType.toLong(fromIndex);
            if (n >= len) {
                return -1.0;
            }
            for (long k = Math.max(0L, (n < 0L) ? (len - Math.abs(n)) : n); k < len; ++k) {
                if (sobj.has((double)k) && ScriptRuntime.EQ_STRICT(sobj.get((double)k), searchElement)) {
                    return (double)k;
                }
            }
        }
        catch (ClassCastException ex) {}
        catch (NullPointerException ex2) {}
        return -1.0;
    }
    
    public static double lastIndexOf(final Object self, final Object... args) {
        try {
            final ScriptObject sobj = (ScriptObject)Global.toObject(self);
            final long len = JSType.toUint32(sobj.getLength());
            if (len == 0L) {
                return -1.0;
            }
            final Object searchElement = (args.length > 0) ? args[0] : ScriptRuntime.UNDEFINED;
            final long n = (args.length > 1) ? JSType.toLong(args[1]) : (len - 1L);
            for (long k = (n < 0L) ? (len - Math.abs(n)) : Math.min(n, len - 1L); k >= 0L; --k) {
                if (sobj.has((double)k) && ScriptRuntime.EQ_STRICT(sobj.get((double)k), searchElement)) {
                    return (double)k;
                }
            }
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(self));
        }
        return -1.0;
    }
    
    public static boolean every(final Object self, final Object callbackfn, final Object thisArg) {
        return applyEvery(Global.toObject(self), callbackfn, thisArg);
    }
    
    private static boolean applyEvery(final Object self, final Object callbackfn, final Object thisArg) {
        return new IteratorAction<Boolean>(Global.toObject(self), callbackfn, thisArg, Boolean.valueOf(true)) {
            private final MethodHandle everyInvoker = getEVERY_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                final Boolean value = this.everyInvoker.invokeExact(this.callbackfn, this.thisArg, val, i, this.self);
                this.result = (T)value;
                return value;
            }
        }.apply();
    }
    
    public static boolean some(final Object self, final Object callbackfn, final Object thisArg) {
        return new IteratorAction<Boolean>(Global.toObject(self), callbackfn, thisArg, Boolean.valueOf(false)) {
            private final MethodHandle someInvoker = getSOME_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                final Boolean value = this.someInvoker.invokeExact(this.callbackfn, this.thisArg, val, i, this.self);
                this.result = (T)value;
                return !value;
            }
        }.apply();
    }
    
    public static Object forEach(final Object self, final Object callbackfn, final Object thisArg) {
        return new IteratorAction<Object>(Global.toObject(self), callbackfn, thisArg, ScriptRuntime.UNDEFINED) {
            private final MethodHandle forEachInvoker = getFOREACH_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                this.forEachInvoker.invokeExact(this.callbackfn, this.thisArg, val, i, this.self);
                return true;
            }
        }.apply();
    }
    
    public static NativeArray map(final Object self, final Object callbackfn, final Object thisArg) {
        return new IteratorAction<NativeArray>(Global.toObject(self), callbackfn, thisArg, null) {
            private final MethodHandle mapInvoker = getMAP_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                final Object r = this.mapInvoker.invokeExact(this.callbackfn, this.thisArg, val, i, this.self);
                ((NativeArray)this.result).defineOwnProperty(ArrayIndex.getArrayIndex(this.index), r);
                return true;
            }
            
            public void applyLoopBegin(final ArrayLikeIterator<Object> iter0) {
                this.result = (T)new NativeArray(iter0.getLength());
            }
        }.apply();
    }
    
    public static NativeArray filter(final Object self, final Object callbackfn, final Object thisArg) {
        return new IteratorAction<NativeArray>(Global.toObject(self), callbackfn, thisArg, new NativeArray()) {
            private long to = 0L;
            private final MethodHandle filterInvoker = getFILTER_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                if (this.filterInvoker.invokeExact(this.callbackfn, this.thisArg, val, i, this.self)) {
                    ((NativeArray)this.result).defineOwnProperty(ArrayIndex.getArrayIndex(this.to++), val);
                }
                return true;
            }
        }.apply();
    }
    
    private static Object reduceInner(final ArrayLikeIterator<Object> iter, final Object self, final Object... args) {
        final Object callbackfn = (args.length > 0) ? args[0] : ScriptRuntime.UNDEFINED;
        final boolean initialValuePresent = args.length > 1;
        Object initialValue = initialValuePresent ? args[1] : ScriptRuntime.UNDEFINED;
        if (callbackfn == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError("not.a.function", "undefined");
        }
        if (!initialValuePresent) {
            if (!iter.hasNext()) {
                throw ECMAErrors.typeError("array.reduce.invalid.init", new String[0]);
            }
            initialValue = iter.next();
        }
        return new IteratorAction<Object>(Global.toObject(self), callbackfn, ScriptRuntime.UNDEFINED, initialValue, iter) {
            private final MethodHandle reduceInvoker = getREDUCE_CALLBACK_INVOKER();
            
            @Override
            protected boolean forEach(final Object val, final double i) throws Throwable {
                this.result = (T)this.reduceInvoker.invokeExact(this.callbackfn, ScriptRuntime.UNDEFINED, (Object)this.result, val, i, this.self);
                return true;
            }
        }.apply();
    }
    
    public static Object reduce(final Object self, final Object... args) {
        return reduceInner(ArrayLikeIterator.arrayLikeIterator(self), self, args);
    }
    
    public static Object reduceRight(final Object self, final Object... args) {
        return reduceInner(ArrayLikeIterator.reverseArrayLikeIterator(self), self, args);
    }
    
    private static boolean bulkable(final ScriptObject self) {
        return self.isArray() && !hasInheritedArrayEntries(self) && !self.isLengthNotWritable();
    }
    
    private static boolean hasInheritedArrayEntries(final ScriptObject self) {
        for (ScriptObject proto = self.getProto(); proto != null; proto = proto.getProto()) {
            if (proto.hasArrayEntries()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "NativeArray@" + Debug.id(this) + " [" + this.getArray().getClass().getSimpleName() + ']';
    }
    
    @Override
    public SpecializedFunction.LinkLogic getLinkLogic(final Class<? extends SpecializedFunction.LinkLogic> clazz) {
        if (clazz == PushLinkLogic.class) {
            return PushLinkLogic.INSTANCE;
        }
        if (clazz == PopLinkLogic.class) {
            return PopLinkLogic.INSTANCE;
        }
        if (clazz == ConcatLinkLogic.class) {
            return ConcatLinkLogic.INSTANCE;
        }
        return null;
    }
    
    @Override
    public boolean hasPerInstanceAssumptions() {
        return true;
    }
    
    private static final <T> ContinuousArrayData getContinuousNonEmptyArrayDataCCE(final Object self, final Class<T> clazz) {
        try {
            final ContinuousArrayData data = (ContinuousArrayData)((NativeArray)self).getArray();
            if (data.length() != 0L) {
                return data;
            }
        }
        catch (NullPointerException ex) {}
        throw new ClassCastException();
    }
    
    private static final ContinuousArrayData getContinuousArrayDataCCE(final Object self) {
        try {
            return (ContinuousArrayData)((NativeArray)self).getArray();
        }
        catch (NullPointerException e) {
            throw new ClassCastException();
        }
    }
    
    private static final ContinuousArrayData getContinuousArrayDataCCE(final Object self, final Class<?> elementType) {
        try {
            return (ContinuousArrayData)((NativeArray)self).getArray(elementType);
        }
        catch (NullPointerException e) {
            throw new ClassCastException();
        }
    }
    
    static {
        JOIN = new Object();
        EVERY_CALLBACK_INVOKER = new Object();
        SOME_CALLBACK_INVOKER = new Object();
        FOREACH_CALLBACK_INVOKER = new Object();
        MAP_CALLBACK_INVOKER = new Object();
        FILTER_CALLBACK_INVOKER = new Object();
        REDUCE_CALLBACK_INVOKER = new Object();
        CALL_CMP = new Object();
        TO_LOCALE_STRING = new Object();
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeArray.$clinit$:()V'.
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
    
    private abstract static class ArrayLinkLogic extends SpecializedFunction.LinkLogic
    {
        protected ArrayLinkLogic() {
        }
        
        protected static ContinuousArrayData getContinuousArrayData(final Object self) {
            try {
                return (ContinuousArrayData)((NativeArray)self).getArray();
            }
            catch (Exception e) {
                return null;
            }
        }
        
        @Override
        public Class<? extends Throwable> getRelinkException() {
            return ClassCastException.class;
        }
    }
    
    private static final class ConcatLinkLogic extends ArrayLinkLogic
    {
        private static final SpecializedFunction.LinkLogic INSTANCE;
        
        @Override
        public boolean canLink(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
            final Object[] args = request.getArguments();
            if (args.length != 3) {
                return false;
            }
            final ContinuousArrayData selfData = ArrayLinkLogic.getContinuousArrayData(self);
            if (selfData == null) {
                return false;
            }
            final Object arg = args[2];
            if (arg instanceof NativeArray) {
                final ContinuousArrayData argData = ArrayLinkLogic.getContinuousArrayData(arg);
                if (argData == null) {
                    return false;
                }
            }
            return true;
        }
        
        static {
            INSTANCE = new ConcatLinkLogic();
        }
    }
    
    private static final class PushLinkLogic extends ArrayLinkLogic
    {
        private static final SpecializedFunction.LinkLogic INSTANCE;
        
        @Override
        public boolean canLink(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
            return ArrayLinkLogic.getContinuousArrayData(self) != null;
        }
        
        static {
            INSTANCE = new PushLinkLogic();
        }
    }
    
    private static final class PopLinkLogic extends ArrayLinkLogic
    {
        private static final SpecializedFunction.LinkLogic INSTANCE;
        
        @Override
        public boolean canLink(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
            final ContinuousArrayData data = getContinuousNonEmptyArrayData(self);
            if (data != null) {
                final Class<?> elementType = data.getElementType();
                final Class<?> returnType = desc.getMethodType().returnType();
                final boolean typeFits = JSType.getAccessorTypeIndex(returnType) >= JSType.getAccessorTypeIndex(elementType);
                return typeFits;
            }
            return false;
        }
        
        private static ContinuousArrayData getContinuousNonEmptyArrayData(final Object self) {
            final ContinuousArrayData data = ArrayLinkLogic.getContinuousArrayData(self);
            if (data != null) {
                return (data.length() == 0L) ? null : data;
            }
            return null;
        }
        
        static {
            INSTANCE = new PopLinkLogic();
        }
    }
}
