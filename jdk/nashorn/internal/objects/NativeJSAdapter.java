// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.util.List;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.scripts.JO;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeJSAdapter extends ScriptObject
{
    public static final String __get__ = "__get__";
    public static final String __put__ = "__put__";
    public static final String __call__ = "__call__";
    public static final String __new__ = "__new__";
    public static final String __getIds__ = "__getIds__";
    public static final String __getKeys__ = "__getKeys__";
    public static final String __getValues__ = "__getValues__";
    public static final String __has__ = "__has__";
    public static final String __delete__ = "__delete__";
    public static final String __preventExtensions__ = "__preventExtensions__";
    public static final String __isExtensible__ = "__isExtensible__";
    public static final String __seal__ = "__seal__";
    public static final String __isSealed__ = "__isSealed__";
    public static final String __freeze__ = "__freeze__";
    public static final String __isFrozen__ = "__isFrozen__";
    private final ScriptObject adaptee;
    private final boolean overrides;
    private static final MethodHandle IS_JSADAPTOR;
    private static PropertyMap $nasgenmap$;
    
    NativeJSAdapter(final Object overrides, final ScriptObject adaptee, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.adaptee = wrapAdaptee(adaptee);
        if (overrides instanceof ScriptObject) {
            this.overrides = true;
            final ScriptObject sobj = (ScriptObject)overrides;
            this.addBoundProperties(sobj);
        }
        else {
            this.overrides = false;
        }
    }
    
    private static ScriptObject wrapAdaptee(final ScriptObject adaptee) {
        return new JO(adaptee);
    }
    
    @Override
    public String getClassName() {
        return "JSAdapter";
    }
    
    @Override
    public int getInt(final Object key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, "__get__", key);
    }
    
    @Override
    public int getInt(final double key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, "__get__", key);
    }
    
    @Override
    public int getInt(final int key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, "__get__", key);
    }
    
    @Override
    public double getDouble(final Object key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, "__get__", key);
    }
    
    @Override
    public double getDouble(final double key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, "__get__", key);
    }
    
    @Override
    public double getDouble(final int key, final int programPoint) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, "__get__", key);
    }
    
    @Override
    public Object get(final Object key) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.get(key) : this.callAdaptee("__get__", key);
    }
    
    @Override
    public Object get(final double key) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.get(key) : this.callAdaptee("__get__", key);
    }
    
    @Override
    public Object get(final int key) {
        return (this.overrides && super.hasOwnProperty(key)) ? super.get(key) : this.callAdaptee("__get__", key);
    }
    
    @Override
    public void set(final Object key, final int value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final Object key, final double value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final Object key, final Object value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final double key, final int value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final double key, final double value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final double key, final Object value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final int key, final int value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final int key, final double value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public void set(final int key, final Object value, final int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        }
        else {
            this.callAdaptee("__put__", key, value, flags);
        }
    }
    
    @Override
    public boolean has(final Object key) {
        return (this.overrides && super.hasOwnProperty(key)) || JSType.toBoolean(this.callAdaptee(Boolean.FALSE, "__has__", key));
    }
    
    @Override
    public boolean has(final int key) {
        return (this.overrides && super.hasOwnProperty(key)) || JSType.toBoolean(this.callAdaptee(Boolean.FALSE, "__has__", key));
    }
    
    @Override
    public boolean has(final double key) {
        return (this.overrides && super.hasOwnProperty(key)) || JSType.toBoolean(this.callAdaptee(Boolean.FALSE, "__has__", key));
    }
    
    @Override
    public boolean delete(final int key, final boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, "__delete__", key, strict));
    }
    
    @Override
    public boolean delete(final double key, final boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, "__delete__", key, strict));
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, "__delete__", key, strict));
    }
    
    @Override
    public Iterator<String> propertyIterator() {
        Object func = this.adaptee.get("__getIds__");
        if (!(func instanceof ScriptFunction)) {
            func = this.adaptee.get("__getKeys__");
        }
        Object obj;
        if (func instanceof ScriptFunction) {
            obj = ScriptRuntime.apply((ScriptFunction)func, this.adaptee, new Object[0]);
        }
        else {
            obj = new NativeArray(0L);
        }
        final List<String> array = new ArrayList<String>();
        final Iterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(obj);
        while (iter.hasNext()) {
            array.add(iter.next());
        }
        return array.iterator();
    }
    
    @Override
    public Iterator<Object> valueIterator() {
        final Object obj = this.callAdaptee(new NativeArray(0L), "__getValues__", new Object[0]);
        return ArrayLikeIterator.arrayLikeIterator(obj);
    }
    
    @Override
    public ScriptObject preventExtensions() {
        this.callAdaptee("__preventExtensions__", new Object[0]);
        return this;
    }
    
    @Override
    public boolean isExtensible() {
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, "__isExtensible__", new Object[0]));
    }
    
    @Override
    public ScriptObject seal() {
        this.callAdaptee("__seal__", new Object[0]);
        return this;
    }
    
    @Override
    public boolean isSealed() {
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, "__isSealed__", new Object[0]));
    }
    
    @Override
    public ScriptObject freeze() {
        this.callAdaptee("__freeze__", new Object[0]);
        return this;
    }
    
    @Override
    public boolean isFrozen() {
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, "__isFrozen__", new Object[0]));
    }
    
    public static NativeJSAdapter construct(final boolean isNew, final Object self, final Object... args) {
        Object proto = ScriptRuntime.UNDEFINED;
        Object overrides = ScriptRuntime.UNDEFINED;
        if (args == null || args.length == 0) {
            throw ECMAErrors.typeError("not.an.object", "null");
        }
        Object adaptee = null;
        switch (args.length) {
            case 1: {
                adaptee = args[0];
                break;
            }
            case 2: {
                overrides = args[0];
                adaptee = args[1];
                break;
            }
            default: {
                proto = args[0];
                overrides = args[1];
                adaptee = args[2];
                break;
            }
        }
        if (!(adaptee instanceof ScriptObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(adaptee));
        }
        final Global global = Global.instance();
        if (proto != null && !(proto instanceof ScriptObject)) {
            proto = global.getJSAdapterPrototype();
        }
        return new NativeJSAdapter(overrides, (ScriptObject)adaptee, (ScriptObject)proto, NativeJSAdapter.$nasgenmap$);
    }
    
    @Override
    protected GuardedInvocation findNewMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.findHook(desc, "__new__", false);
    }
    
    @Override
    protected GuardedInvocation findCallMethodMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        if (this.overrides && super.hasOwnProperty(desc.getNameToken(2))) {
            try {
                final GuardedInvocation inv = super.findCallMethodMethod(desc, request);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception ex) {}
        }
        return this.findHook(desc, "__call__");
    }
    
    @Override
    protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operation) {
        final String name = desc.getNameToken(2);
        if (this.overrides && super.hasOwnProperty(name)) {
            try {
                final GuardedInvocation inv = super.findGetMethod(desc, request, operation);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception ex) {}
        }
        switch (operation) {
            case "getProp":
            case "getElem": {
                return this.findHook(desc, "__get__");
            }
            case "getMethod": {
                final FindProperty find = this.adaptee.findProperty("__call__", true);
                if (find != null) {
                    final Object value = find.getObjectValue();
                    if (value instanceof ScriptFunction) {
                        final ScriptFunction func = (ScriptFunction)value;
                        return new GuardedInvocation(Lookup.MH.dropArguments(Lookup.MH.constant(Object.class, func.createBound(this, new Object[] { name })), 0, Object.class), testJSAdaptor(this.adaptee, null, null, null), this.adaptee.getProtoSwitchPoints("__call__", find.getOwner()), null);
                    }
                }
                throw ECMAErrors.typeError("no.such.function", desc.getNameToken(2), ScriptRuntime.safeToString(this));
            }
            default: {
                throw new AssertionError((Object)"should not reach here");
            }
        }
    }
    
    @Override
    protected GuardedInvocation findSetMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        if (this.overrides && super.hasOwnProperty(desc.getNameToken(2))) {
            try {
                final GuardedInvocation inv = super.findSetMethod(desc, request);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception ex) {}
        }
        return this.findHook(desc, "__put__");
    }
    
    private Object callAdaptee(final String name, final Object... args) {
        return this.callAdaptee(ScriptRuntime.UNDEFINED, name, args);
    }
    
    private double callAdapteeDouble(final int programPoint, final String name, final Object... args) {
        return JSType.toNumberMaybeOptimistic(this.callAdaptee(name, args), programPoint);
    }
    
    private int callAdapteeInt(final int programPoint, final String name, final Object... args) {
        return JSType.toInt32MaybeOptimistic(this.callAdaptee(name, args), programPoint);
    }
    
    private Object callAdaptee(final Object retValue, final String name, final Object... args) {
        final Object func = this.adaptee.get(name);
        if (func instanceof ScriptFunction) {
            return ScriptRuntime.apply((ScriptFunction)func, this.adaptee, args);
        }
        return retValue;
    }
    
    private GuardedInvocation findHook(final CallSiteDescriptor desc, final String hook) {
        return this.findHook(desc, hook, true);
    }
    
    private GuardedInvocation findHook(final CallSiteDescriptor desc, final String hook, final boolean useName) {
        final FindProperty findData = this.adaptee.findProperty(hook, true);
        final MethodType type = desc.getMethodType();
        if (findData != null) {
            final String name = (desc.getNameTokenCount() > 2) ? desc.getNameToken(2) : null;
            final Object value = findData.getObjectValue();
            if (value instanceof ScriptFunction) {
                final ScriptFunction func = (ScriptFunction)value;
                final MethodHandle methodHandle = this.getCallMethodHandle(findData, type, useName ? name : null);
                if (methodHandle != null) {
                    return new GuardedInvocation(methodHandle, testJSAdaptor(this.adaptee, findData.getGetter(Object.class, -1, null), findData.getOwner(), func), this.adaptee.getProtoSwitchPoints(hook, findData.getOwner()), null);
                }
            }
        }
        switch (hook) {
            case "__call__": {
                throw ECMAErrors.typeError("no.such.function", desc.getNameToken(2), ScriptRuntime.safeToString(this));
            }
            default: {
                final MethodHandle methodHandle2 = hook.equals("__put__") ? Lookup.MH.asType(Lookup.EMPTY_SETTER, type) : Lookup.emptyGetter(type.returnType());
                return new GuardedInvocation(methodHandle2, testJSAdaptor(this.adaptee, null, null, null), this.adaptee.getProtoSwitchPoints(hook, null), null);
            }
        }
    }
    
    private static MethodHandle testJSAdaptor(final Object adaptee, final MethodHandle getter, final Object where, final ScriptFunction func) {
        return Lookup.MH.insertArguments(NativeJSAdapter.IS_JSADAPTOR, 1, adaptee, getter, where, func);
    }
    
    private static boolean isJSAdaptor(final Object self, final Object adaptee, final MethodHandle getter, final Object where, final ScriptFunction func) {
        final boolean res = self instanceof NativeJSAdapter && ((NativeJSAdapter)self).getAdaptee() == adaptee;
        if (res && getter != null) {
            try {
                return getter.invokeExact(where) == func;
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
        return res;
    }
    
    public ScriptObject getAdaptee() {
        return this.adaptee;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeJSAdapter.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        IS_JSADAPTOR = findOwnMH("isJSAdaptor", Boolean.TYPE, Object.class, Object.class, MethodHandle.class, Object.class, ScriptFunction.class);
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeJSAdapter.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
