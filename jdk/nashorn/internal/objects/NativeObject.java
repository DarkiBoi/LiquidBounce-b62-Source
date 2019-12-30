// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.Collections;
import jdk.internal.dynalink.support.LinkRequestImpl;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;
import java.lang.invoke.MethodHandles;
import java.util.List;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.runtime.AccessorProperty;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

public final class NativeObject
{
    public static final MethodHandle GET__PROTO__;
    public static final MethodHandle SET__PROTO__;
    private static final Object TO_STRING;
    private static final MethodType MIRROR_GETTER_TYPE;
    private static final MethodType MIRROR_SETTER_TYPE;
    private static PropertyMap $nasgenmap$;
    
    private static InvokeByName getTO_STRING() {
        return Global.instance().getInvokeByName(NativeObject.TO_STRING, new Callable<InvokeByName>() {
            @Override
            public InvokeByName call() {
                return new InvokeByName("toString", ScriptObject.class);
            }
        });
    }
    
    private static ScriptObject get__proto__(final Object self) {
        final ScriptObject sobj = Global.checkObject(Global.toObject(self));
        return sobj.getProto();
    }
    
    private static Object set__proto__(final Object self, final Object proto) {
        Global.checkObjectCoercible(self);
        if (!(self instanceof ScriptObject)) {
            return ScriptRuntime.UNDEFINED;
        }
        final ScriptObject sobj = (ScriptObject)self;
        if (proto == null || proto instanceof ScriptObject) {
            sobj.setPrototypeOf(proto);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    private NativeObject() {
        throw new UnsupportedOperationException();
    }
    
    private static ECMAException notAnObject(final Object obj) {
        return ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
    }
    
    public static ScriptObject setIndexedPropertiesToExternalArrayData(final Object self, final Object obj, final Object buf) {
        Global.checkObject(obj);
        final ScriptObject sobj = (ScriptObject)obj;
        if (buf instanceof ByteBuffer) {
            sobj.setArray(ArrayData.allocate((ByteBuffer)buf));
            return sobj;
        }
        throw ECMAErrors.typeError("not.a.bytebuffer", "setIndexedPropertiesToExternalArrayData's buf argument");
    }
    
    public static Object getPrototypeOf(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getProto();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).getProto();
        }
        final JSType type = JSType.of(obj);
        if (type == JSType.OBJECT) {
            return null;
        }
        throw notAnObject(obj);
    }
    
    public static Object setPrototypeOf(final Object self, final Object obj, final Object proto) {
        if (obj instanceof ScriptObject) {
            ((ScriptObject)obj).setPrototypeOf(proto);
            return obj;
        }
        if (obj instanceof ScriptObjectMirror) {
            ((ScriptObjectMirror)obj).setProto(proto);
            return obj;
        }
        throw notAnObject(obj);
    }
    
    public static Object getOwnPropertyDescriptor(final Object self, final Object obj, final Object prop) {
        if (obj instanceof ScriptObject) {
            final String key = JSType.toString(prop);
            final ScriptObject sobj = (ScriptObject)obj;
            return sobj.getOwnPropertyDescriptor(key);
        }
        if (obj instanceof ScriptObjectMirror) {
            final String key = JSType.toString(prop);
            final ScriptObjectMirror sobjMirror = (ScriptObjectMirror)obj;
            return sobjMirror.getOwnPropertyDescriptor(key);
        }
        throw notAnObject(obj);
    }
    
    public static ScriptObject getOwnPropertyNames(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return new NativeArray(((ScriptObject)obj).getOwnKeys(true));
        }
        if (obj instanceof ScriptObjectMirror) {
            return new NativeArray(((ScriptObjectMirror)obj).getOwnKeys(true));
        }
        throw notAnObject(obj);
    }
    
    public static ScriptObject create(final Object self, final Object proto, final Object props) {
        if (proto != null) {
            Global.checkObject(proto);
        }
        final ScriptObject newObj = Global.newEmptyInstance();
        newObj.setProto((ScriptObject)proto);
        if (props != ScriptRuntime.UNDEFINED) {
            defineProperties(self, newObj, props);
        }
        return newObj;
    }
    
    public static ScriptObject defineProperty(final Object self, final Object obj, final Object prop, final Object attr) {
        final ScriptObject sobj = Global.checkObject(obj);
        sobj.defineOwnProperty(JSType.toString(prop), attr, true);
        return sobj;
    }
    
    public static ScriptObject defineProperties(final Object self, final Object obj, final Object props) {
        final ScriptObject sobj = Global.checkObject(obj);
        final Object propsObj = Global.toObject(props);
        if (propsObj instanceof ScriptObject) {
            final String[] ownKeys;
            final Object[] keys = ownKeys = ((ScriptObject)propsObj).getOwnKeys(false);
            for (final Object key : ownKeys) {
                final String prop = JSType.toString(key);
                sobj.defineOwnProperty(prop, ((ScriptObject)propsObj).get(prop), true);
            }
        }
        return sobj;
    }
    
    public static Object seal(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).seal();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).seal();
        }
        throw notAnObject(obj);
    }
    
    public static Object freeze(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).freeze();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).freeze();
        }
        throw notAnObject(obj);
    }
    
    public static Object preventExtensions(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).preventExtensions();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).preventExtensions();
        }
        throw notAnObject(obj);
    }
    
    public static boolean isSealed(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isSealed();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isSealed();
        }
        throw notAnObject(obj);
    }
    
    public static boolean isFrozen(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isFrozen();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isFrozen();
        }
        throw notAnObject(obj);
    }
    
    public static boolean isExtensible(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isExtensible();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isExtensible();
        }
        throw notAnObject(obj);
    }
    
    public static ScriptObject keys(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)obj;
            return new NativeArray(sobj.getOwnKeys(false));
        }
        if (obj instanceof ScriptObjectMirror) {
            final ScriptObjectMirror sobjMirror = (ScriptObjectMirror)obj;
            return new NativeArray(sobjMirror.getOwnKeys(false));
        }
        throw notAnObject(obj);
    }
    
    public static Object construct(final boolean newObj, final Object self, final Object value) {
        final JSType type = JSType.ofNoFunction(value);
        if (!newObj && type != JSType.NULL && type != JSType.UNDEFINED) {
            return Global.toObject(value);
        }
        switch (type) {
            case BOOLEAN:
            case NUMBER:
            case STRING: {
                return Global.toObject(value);
            }
            case OBJECT: {
                return value;
            }
            default: {
                return Global.newEmptyInstance();
            }
        }
    }
    
    public static String toString(final Object self) {
        return ScriptRuntime.builtinObjectToString(self);
    }
    
    public static Object toLocaleString(final Object self) {
        final Object obj = JSType.toScriptObject(self);
        if (obj instanceof ScriptObject) {
            final InvokeByName toStringInvoker = getTO_STRING();
            final ScriptObject sobj = (ScriptObject)obj;
            try {
                final Object toString = toStringInvoker.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString)) {
                    return toStringInvoker.getInvoker().invokeExact(toString, sobj);
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
            throw ECMAErrors.typeError("not.a.function", "toString");
        }
        return ScriptRuntime.builtinObjectToString(self);
    }
    
    public static Object valueOf(final Object self) {
        return Global.toObject(self);
    }
    
    public static boolean hasOwnProperty(final Object self, final Object v) {
        final Object key = JSType.toPrimitive(v, String.class);
        final Object obj = Global.toObject(self);
        return obj instanceof ScriptObject && ((ScriptObject)obj).hasOwnProperty(key);
    }
    
    public static boolean isPrototypeOf(final Object self, final Object v) {
        if (!(v instanceof ScriptObject)) {
            return false;
        }
        final Object obj = Global.toObject(self);
        ScriptObject proto = (ScriptObject)v;
        do {
            proto = proto.getProto();
            if (proto == obj) {
                return true;
            }
        } while (proto != null);
        return false;
    }
    
    public static boolean propertyIsEnumerable(final Object self, final Object v) {
        final String str = JSType.toString(v);
        final Object obj = Global.toObject(self);
        if (obj instanceof ScriptObject) {
            final Property property = ((ScriptObject)obj).getMap().findProperty(str);
            return property != null && property.isEnumerable();
        }
        return false;
    }
    
    public static Object bindProperties(final Object self, final Object target, final Object source) {
        final ScriptObject targetObj = Global.checkObject(target);
        Global.checkObjectCoercible(source);
        if (source instanceof ScriptObject) {
            final ScriptObject sourceObj = (ScriptObject)source;
            final PropertyMap sourceMap = sourceObj.getMap();
            final Property[] properties = sourceMap.getProperties();
            final ArrayList<Property> propList = new ArrayList<Property>();
            for (final Property prop : properties) {
                if (prop.isEnumerable()) {
                    final Object value = sourceObj.get(prop.getKey());
                    prop.setType(Object.class);
                    prop.setValue(sourceObj, sourceObj, value, false);
                    propList.add(prop);
                }
            }
            if (!propList.isEmpty()) {
                targetObj.addBoundProperties(sourceObj, propList.toArray(new Property[propList.size()]));
            }
        }
        else if (source instanceof ScriptObjectMirror) {
            final ScriptObjectMirror mirror = (ScriptObjectMirror)source;
            final String[] keys = mirror.getOwnKeys(false);
            if (keys.length == 0) {
                return target;
            }
            final AccessorProperty[] props = new AccessorProperty[keys.length];
            for (int idx = 0; idx < keys.length; ++idx) {
                final String name = keys[idx];
                final MethodHandle getter = Bootstrap.createDynamicInvoker("dyn:getMethod|getProp|getElem:" + name, NativeObject.MIRROR_GETTER_TYPE);
                final MethodHandle setter = Bootstrap.createDynamicInvoker("dyn:setProp|setElem:" + name, NativeObject.MIRROR_SETTER_TYPE);
                props[idx] = AccessorProperty.create(name, 0, getter, setter);
            }
            targetObj.addBoundProperties(source, props);
        }
        else if (source instanceof StaticClass) {
            final Class<?> clazz = ((StaticClass)source).getRepresentedClass();
            Bootstrap.checkReflectionAccess(clazz, true);
            bindBeanProperties(targetObj, source, BeansLinker.getReadableStaticPropertyNames(clazz), BeansLinker.getWritableStaticPropertyNames(clazz), BeansLinker.getStaticMethodNames(clazz));
        }
        else {
            final Class<?> clazz = source.getClass();
            Bootstrap.checkReflectionAccess(clazz, false);
            bindBeanProperties(targetObj, source, BeansLinker.getReadableInstancePropertyNames(clazz), BeansLinker.getWritableInstancePropertyNames(clazz), BeansLinker.getInstanceMethodNames(clazz));
        }
        return target;
    }
    
    public static Object bindAllProperties(final ScriptObject target, final ScriptObjectMirror source) {
        final Set<String> keys = source.keySet();
        final AccessorProperty[] props = new AccessorProperty[keys.size()];
        int idx = 0;
        for (final String name : keys) {
            final MethodHandle getter = Bootstrap.createDynamicInvoker("dyn:getMethod|getProp|getElem:" + name, NativeObject.MIRROR_GETTER_TYPE);
            final MethodHandle setter = Bootstrap.createDynamicInvoker("dyn:setProp|setElem:" + name, NativeObject.MIRROR_SETTER_TYPE);
            props[idx] = AccessorProperty.create(name, 0, getter, setter);
            ++idx;
        }
        target.addBoundProperties(source, props);
        return target;
    }
    
    private static void bindBeanProperties(final ScriptObject targetObj, final Object source, final Collection<String> readablePropertyNames, final Collection<String> writablePropertyNames, final Collection<String> methodNames) {
        final Set<String> propertyNames = new HashSet<String>(readablePropertyNames);
        propertyNames.addAll(writablePropertyNames);
        final Class<?> clazz = source.getClass();
        final MethodType getterType = MethodType.methodType(Object.class, clazz);
        final MethodType setterType = MethodType.methodType(Object.class, clazz, Object.class);
        final GuardingDynamicLinker linker = BeansLinker.getLinkerForClass(clazz);
        final List<AccessorProperty> properties = new ArrayList<AccessorProperty>(propertyNames.size() + methodNames.size());
        for (final String methodName : methodNames) {
            MethodHandle method;
            try {
                method = getBeanOperation(linker, "dyn:getMethod:" + methodName, getterType, source);
            }
            catch (IllegalAccessError e) {
                continue;
            }
            properties.add(AccessorProperty.create(methodName, 1, getBoundBeanMethodGetter(source, method), Lookup.EMPTY_SETTER));
        }
        for (final String propertyName : propertyNames) {
            MethodHandle getter;
            if (readablePropertyNames.contains(propertyName)) {
                try {
                    getter = getBeanOperation(linker, "dyn:getProp:" + propertyName, getterType, source);
                }
                catch (IllegalAccessError e) {
                    getter = Lookup.EMPTY_GETTER;
                }
            }
            else {
                getter = Lookup.EMPTY_GETTER;
            }
            final boolean isWritable = writablePropertyNames.contains(propertyName);
            MethodHandle setter;
            if (isWritable) {
                try {
                    setter = getBeanOperation(linker, "dyn:setProp:" + propertyName, setterType, source);
                }
                catch (IllegalAccessError e2) {
                    setter = Lookup.EMPTY_SETTER;
                }
            }
            else {
                setter = Lookup.EMPTY_SETTER;
            }
            if (getter != Lookup.EMPTY_GETTER || setter != Lookup.EMPTY_SETTER) {
                properties.add(AccessorProperty.create(propertyName, (int)(isWritable ? 0 : 1), getter, setter));
            }
        }
        targetObj.addBoundProperties(source, properties.toArray(new AccessorProperty[properties.size()]));
    }
    
    private static MethodHandle getBoundBeanMethodGetter(final Object source, final MethodHandle methodGetter) {
        try {
            return MethodHandles.dropArguments(MethodHandles.constant(Object.class, Bootstrap.bindCallable(methodGetter.invoke(source), source, null)), 0, Object.class);
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
    
    private static MethodHandle getBeanOperation(final GuardingDynamicLinker linker, final String operation, final MethodType methodType, final Object source) {
        GuardedInvocation inv;
        try {
            inv = NashornBeansLinker.getGuardedInvocation(linker, createLinkRequest(operation, methodType, source), Bootstrap.getLinkerServices());
            assert passesGuard(source, inv.getGuard());
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        assert inv.getSwitchPoints() == null;
        return inv.getInvocation();
    }
    
    private static boolean passesGuard(final Object obj, final MethodHandle guard) throws Throwable {
        return guard == null || guard.invoke(obj);
    }
    
    private static LinkRequest createLinkRequest(final String operation, final MethodType methodType, final Object source) {
        return new LinkRequestImpl(CallSiteDescriptorFactory.create(MethodHandles.publicLookup(), operation, methodType), null, 0, false, new Object[] { source });
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeObject.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        GET__PROTO__ = findOwnMH("get__proto__", ScriptObject.class, Object.class);
        SET__PROTO__ = findOwnMH("set__proto__", Object.class, Object.class, Object.class);
        TO_STRING = new Object();
        MIRROR_GETTER_TYPE = MethodType.methodType(Object.class, ScriptObjectMirror.class);
        MIRROR_SETTER_TYPE = MethodType.methodType(Object.class, ScriptObjectMirror.class, Object.class);
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeObject.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
