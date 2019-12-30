// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.LinkedHashSet;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.runtime.linker.NashornGuards;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.objects.Global;
import java.util.Iterator;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.concurrent.atomic.LongAdder;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.util.ArrayList;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.arrays.ArrayData;

public abstract class ScriptObject implements PropertyAccess, Cloneable
{
    public static final String PROTO_PROPERTY_NAME = "__proto__";
    public static final String NO_SUCH_METHOD_NAME = "__noSuchMethod__";
    public static final String NO_SUCH_PROPERTY_NAME = "__noSuchProperty__";
    public static final int IS_ARRAY = 1;
    public static final int IS_ARGUMENTS = 2;
    public static final int IS_LENGTH_NOT_WRITABLE = 4;
    public static final int IS_BUILTIN = 8;
    public static final int SPILL_RATE = 8;
    private PropertyMap map;
    private ScriptObject proto;
    private int flags;
    protected long[] primitiveSpill;
    protected Object[] objectSpill;
    private ArrayData arrayData;
    public static final MethodHandle GETPROTO;
    static final MethodHandle MEGAMORPHIC_GET;
    static final MethodHandle GLOBALFILTER;
    static final MethodHandle DECLARE_AND_SET;
    private static final MethodHandle TRUNCATINGFILTER;
    private static final MethodHandle KNOWNFUNCPROPGUARDSELF;
    private static final MethodHandle KNOWNFUNCPROPGUARDPROTO;
    private static final ArrayList<MethodHandle> PROTO_FILTERS;
    public static final CompilerConstants.Call GET_ARRAY;
    public static final CompilerConstants.Call GET_ARGUMENT;
    public static final CompilerConstants.Call SET_ARGUMENT;
    public static final CompilerConstants.Call GET_PROTO;
    public static final CompilerConstants.Call GET_PROTO_DEPTH;
    public static final CompilerConstants.Call SET_GLOBAL_OBJECT_PROTO;
    public static final CompilerConstants.Call SET_PROTO_FROM_LITERAL;
    public static final CompilerConstants.Call SET_USER_ACCESSORS;
    static final MethodHandle[] SET_SLOW;
    public static final CompilerConstants.Call SET_MAP;
    static final MethodHandle CAS_MAP;
    static final MethodHandle EXTENSION_CHECK;
    static final MethodHandle ENSURE_SPILL_SIZE;
    private static LongAdder count;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    public ScriptObject() {
        this(null);
    }
    
    public ScriptObject(final PropertyMap map) {
        if (Context.DEBUG) {
            ScriptObject.count.increment();
        }
        this.arrayData = ArrayData.EMPTY_ARRAY;
        this.setMap((map == null) ? PropertyMap.newMap() : map);
    }
    
    protected ScriptObject(final ScriptObject proto, final PropertyMap map) {
        this(map);
        this.proto = proto;
    }
    
    public ScriptObject(final PropertyMap map, final long[] primitiveSpill, final Object[] objectSpill) {
        this(map);
        this.primitiveSpill = primitiveSpill;
        this.objectSpill = objectSpill;
        assert primitiveSpill.length == objectSpill.length : " primitive spill pool size is not the same length as object spill pool size";
    }
    
    protected boolean isGlobal() {
        return false;
    }
    
    private static int alignUp(final int size, final int alignment) {
        return size + alignment - 1 & ~(alignment - 1);
    }
    
    public static int spillAllocationLength(final int nProperties) {
        return alignUp(nProperties, 8);
    }
    
    public void addBoundProperties(final ScriptObject source) {
        this.addBoundProperties(source, source.getMap().getProperties());
    }
    
    public void addBoundProperties(final ScriptObject source, final Property[] properties) {
        PropertyMap newMap = this.getMap();
        final boolean extensible = newMap.isExtensible();
        for (final Property property : properties) {
            newMap = this.addBoundProperty(newMap, source, property, extensible);
        }
        this.setMap(newMap);
    }
    
    protected PropertyMap addBoundProperty(final PropertyMap propMap, final ScriptObject source, final Property property, final boolean extensible) {
        PropertyMap newMap = propMap;
        final String key = property.getKey();
        final Property oldProp = newMap.findProperty(key);
        if (oldProp == null) {
            if (!extensible) {
                throw ECMAErrors.typeError("object.non.extensible", key, ScriptRuntime.safeToString(this));
            }
            if (property instanceof UserAccessorProperty) {
                final UserAccessorProperty prop = this.newUserAccessors(key, property.getFlags(), property.getGetterFunction(source), property.getSetterFunction(source));
                newMap = newMap.addPropertyNoHistory(prop);
            }
            else {
                newMap = newMap.addPropertyBind((AccessorProperty)property, source);
            }
        }
        else if (property.isFunctionDeclaration() && !oldProp.isConfigurable() && (oldProp instanceof UserAccessorProperty || !oldProp.isWritable() || !oldProp.isEnumerable())) {
            throw ECMAErrors.typeError("cant.redefine.property", key, ScriptRuntime.safeToString(this));
        }
        return newMap;
    }
    
    public void addBoundProperties(final Object source, final AccessorProperty[] properties) {
        PropertyMap newMap = this.getMap();
        final boolean extensible = newMap.isExtensible();
        for (final AccessorProperty property : properties) {
            final String key = property.getKey();
            if (newMap.findProperty(key) == null) {
                if (!extensible) {
                    throw ECMAErrors.typeError("object.non.extensible", key, ScriptRuntime.safeToString(this));
                }
                newMap = newMap.addPropertyBind(property, source);
            }
        }
        this.setMap(newMap);
    }
    
    static MethodHandle bindTo(final MethodHandle methodHandle, final Object receiver) {
        return Lookup.MH.dropArguments(Lookup.MH.bindTo(methodHandle, receiver), 0, methodHandle.type().parameterType(0));
    }
    
    public Iterator<String> propertyIterator() {
        return new KeyIterator(this);
    }
    
    public Iterator<Object> valueIterator() {
        return new ValueIterator(this);
    }
    
    public final boolean isAccessorDescriptor() {
        return this.has("get") || this.has("set");
    }
    
    public final boolean isDataDescriptor() {
        return this.has("value") || this.has("writable");
    }
    
    public final PropertyDescriptor toPropertyDescriptor() {
        final Global global = Context.getGlobal();
        PropertyDescriptor desc;
        if (this.isDataDescriptor()) {
            if (this.has("set") || this.has("get")) {
                throw ECMAErrors.typeError(global, "inconsistent.property.descriptor", new String[0]);
            }
            desc = global.newDataDescriptor(ScriptRuntime.UNDEFINED, false, false, false);
        }
        else if (this.isAccessorDescriptor()) {
            if (this.has("value") || this.has("writable")) {
                throw ECMAErrors.typeError(global, "inconsistent.property.descriptor", new String[0]);
            }
            desc = global.newAccessorDescriptor(ScriptRuntime.UNDEFINED, ScriptRuntime.UNDEFINED, false, false);
        }
        else {
            desc = global.newGenericDescriptor(false, false);
        }
        return desc.fillFrom(this);
    }
    
    public static PropertyDescriptor toPropertyDescriptor(final Global global, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).toPropertyDescriptor();
        }
        throw ECMAErrors.typeError(global, "not.an.object", ScriptRuntime.safeToString(obj));
    }
    
    public Object getOwnPropertyDescriptor(final String key) {
        final Property property = this.getMap().findProperty(key);
        final Global global = Context.getGlobal();
        if (property != null) {
            final ScriptFunction get = property.getGetterFunction(this);
            final ScriptFunction set = property.getSetterFunction(this);
            final boolean configurable = property.isConfigurable();
            final boolean enumerable = property.isEnumerable();
            final boolean writable = property.isWritable();
            if (property instanceof UserAccessorProperty) {
                return global.newAccessorDescriptor((get != null) ? get : ScriptRuntime.UNDEFINED, (set != null) ? set : ScriptRuntime.UNDEFINED, configurable, enumerable);
            }
            return global.newDataDescriptor(this.getWithProperty(property), configurable, enumerable, writable);
        }
        else {
            final int index = ArrayIndex.getArrayIndex(key);
            final ArrayData array = this.getArray();
            if (array.has(index)) {
                return array.getDescriptor(global, index);
            }
            return ScriptRuntime.UNDEFINED;
        }
    }
    
    public Object getPropertyDescriptor(final String key) {
        final Object res = this.getOwnPropertyDescriptor(key);
        if (res != ScriptRuntime.UNDEFINED) {
            return res;
        }
        if (this.getProto() != null) {
            return this.getProto().getOwnPropertyDescriptor(key);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    protected void invalidateGlobalConstant(final String key) {
        final GlobalConstants globalConstants = this.getGlobalConstants();
        if (globalConstants != null) {
            globalConstants.delete(key);
        }
    }
    
    public boolean defineOwnProperty(final String key, final Object propertyDesc, final boolean reject) {
        final Global global = Context.getGlobal();
        final PropertyDescriptor desc = toPropertyDescriptor(global, propertyDesc);
        final Object current = this.getOwnPropertyDescriptor(key);
        final String name = JSType.toString(key);
        this.invalidateGlobalConstant(key);
        if (current == ScriptRuntime.UNDEFINED) {
            if (this.isExtensible()) {
                this.addOwnProperty(key, desc);
                return true;
            }
            if (reject) {
                throw ECMAErrors.typeError(global, "object.non.extensible", name, ScriptRuntime.safeToString(this));
            }
            return false;
        }
        else {
            final PropertyDescriptor currentDesc = (PropertyDescriptor)current;
            final PropertyDescriptor newDesc = desc;
            if (newDesc.type() == 0 && !newDesc.has("configurable") && !newDesc.has("enumerable")) {
                return true;
            }
            if (newDesc.hasAndEquals(currentDesc)) {
                return true;
            }
            if (!currentDesc.isConfigurable()) {
                if (newDesc.has("configurable") && newDesc.isConfigurable()) {
                    if (reject) {
                        throw ECMAErrors.typeError(global, "cant.redefine.property", name, ScriptRuntime.safeToString(this));
                    }
                    return false;
                }
                else if (newDesc.has("enumerable") && currentDesc.isEnumerable() != newDesc.isEnumerable()) {
                    if (reject) {
                        throw ECMAErrors.typeError(global, "cant.redefine.property", name, ScriptRuntime.safeToString(this));
                    }
                    return false;
                }
            }
            int propFlags = Property.mergeFlags(currentDesc, newDesc);
            Property property = this.getMap().findProperty(key);
            if (currentDesc.type() == 1 && (newDesc.type() == 1 || newDesc.type() == 0)) {
                if (!currentDesc.isConfigurable() && !currentDesc.isWritable() && ((newDesc.has("writable") && newDesc.isWritable()) || (newDesc.has("value") && !ScriptRuntime.sameValue(currentDesc.getValue(), newDesc.getValue())))) {
                    if (reject) {
                        throw ECMAErrors.typeError(global, "cant.redefine.property", name, ScriptRuntime.safeToString(this));
                    }
                    return false;
                }
                else {
                    final boolean newValue = newDesc.has("value");
                    final Object value = newValue ? newDesc.getValue() : currentDesc.getValue();
                    if (newValue && property != null) {
                        property = this.modifyOwnProperty(property, 0);
                        this.set(key, value, 0);
                        property = this.getMap().findProperty(key);
                    }
                    if (property == null) {
                        this.addOwnProperty(key, propFlags, value);
                        this.checkIntegerKey(key);
                    }
                    else {
                        this.modifyOwnProperty(property, propFlags);
                    }
                }
            }
            else if (currentDesc.type() == 2 && (newDesc.type() == 2 || newDesc.type() == 0)) {
                if (!currentDesc.isConfigurable() && ((newDesc.has("get") && !ScriptRuntime.sameValue(currentDesc.getGetter(), newDesc.getGetter())) || (newDesc.has("set") && !ScriptRuntime.sameValue(currentDesc.getSetter(), newDesc.getSetter())))) {
                    if (reject) {
                        throw ECMAErrors.typeError(global, "cant.redefine.property", name, ScriptRuntime.safeToString(this));
                    }
                    return false;
                }
                else {
                    this.modifyOwnProperty(property, propFlags, newDesc.has("get") ? newDesc.getGetter() : currentDesc.getGetter(), newDesc.has("set") ? newDesc.getSetter() : currentDesc.getSetter());
                }
            }
            else if (!currentDesc.isConfigurable()) {
                if (reject) {
                    throw ECMAErrors.typeError(global, "cant.redefine.property", name, ScriptRuntime.safeToString(this));
                }
                return false;
            }
            else {
                propFlags = 0;
                boolean value2 = newDesc.has("configurable") ? newDesc.isConfigurable() : currentDesc.isConfigurable();
                if (!value2) {
                    propFlags |= 0x4;
                }
                value2 = (newDesc.has("enumerable") ? newDesc.isEnumerable() : currentDesc.isEnumerable());
                if (!value2) {
                    propFlags |= 0x2;
                }
                final int type = newDesc.type();
                if (type == 1) {
                    value2 = (newDesc.has("writable") && newDesc.isWritable());
                    if (!value2) {
                        propFlags |= 0x1;
                    }
                    this.deleteOwnProperty(property);
                    this.addOwnProperty(key, propFlags, newDesc.getValue());
                }
                else if (type == 2) {
                    if (property == null) {
                        this.addOwnProperty(key, propFlags, newDesc.has("get") ? newDesc.getGetter() : null, newDesc.has("set") ? newDesc.getSetter() : null);
                    }
                    else {
                        this.modifyOwnProperty(property, propFlags, newDesc.has("get") ? newDesc.getGetter() : null, newDesc.has("set") ? newDesc.getSetter() : null);
                    }
                }
            }
            this.checkIntegerKey(key);
            return true;
        }
    }
    
    public void defineOwnProperty(final int index, final Object value) {
        assert ArrayIndex.isValidArrayIndex(index) : "invalid array index";
        final long longIndex = ArrayIndex.toLongIndex(index);
        final long oldLength = this.getArray().length();
        if (longIndex >= oldLength) {
            this.setArray(this.getArray().ensure(longIndex).safeDelete(oldLength, longIndex - 1L, false));
        }
        this.setArray(this.getArray().set(index, value, false));
    }
    
    private void checkIntegerKey(final String key) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.delete(index));
            }
        }
    }
    
    public final void addOwnProperty(final String key, final PropertyDescriptor propertyDesc) {
        PropertyDescriptor pdesc = propertyDesc;
        final int propFlags = Property.toFlags(pdesc);
        if (pdesc.type() == 0) {
            final Global global = Context.getGlobal();
            final PropertyDescriptor dDesc = global.newDataDescriptor(ScriptRuntime.UNDEFINED, false, false, false);
            dDesc.fillFrom((ScriptObject)pdesc);
            pdesc = dDesc;
        }
        final int type = pdesc.type();
        if (type == 1) {
            this.addOwnProperty(key, propFlags, pdesc.getValue());
        }
        else if (type == 2) {
            this.addOwnProperty(key, propFlags, pdesc.has("get") ? pdesc.getGetter() : null, pdesc.has("set") ? pdesc.getSetter() : null);
        }
        this.checkIntegerKey(key);
    }
    
    public final FindProperty findProperty(final String key, final boolean deep) {
        return this.findProperty(key, deep, this);
    }
    
    protected FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        final PropertyMap selfMap = this.getMap();
        final Property property = selfMap.findProperty(key);
        if (property != null) {
            return new FindProperty(start, this, property);
        }
        if (deep) {
            final ScriptObject myProto = this.getProto();
            final FindProperty find = (myProto == null) ? null : myProto.findProperty(key, true, start);
            this.checkSharedProtoMap();
            return find;
        }
        return null;
    }
    
    boolean hasProperty(final String key, final boolean deep) {
        if (this.getMap().findProperty(key) != null) {
            return true;
        }
        if (deep) {
            final ScriptObject myProto = this.getProto();
            if (myProto != null) {
                return myProto.hasProperty(key, true);
            }
        }
        return false;
    }
    
    private SwitchPoint findBuiltinSwitchPoint(final String key) {
        for (ScriptObject myProto = this.getProto(); myProto != null; myProto = myProto.getProto()) {
            final Property prop = myProto.getMap().findProperty(key);
            if (prop != null) {
                final SwitchPoint sp = prop.getBuiltinSwitchPoint();
                if (sp != null && !sp.hasBeenInvalidated()) {
                    return sp;
                }
            }
        }
        return null;
    }
    
    public final Property addOwnProperty(final String key, final int propertyFlags, final ScriptFunction getter, final ScriptFunction setter) {
        return this.addOwnProperty(this.newUserAccessors(key, propertyFlags, getter, setter));
    }
    
    public final Property addOwnProperty(final String key, final int propertyFlags, final Object value) {
        return this.addSpillProperty(key, propertyFlags, value, true);
    }
    
    public final Property addOwnProperty(final Property newProperty) {
        PropertyMap oldMap = this.getMap();
        while (true) {
            final PropertyMap newMap = oldMap.addProperty(newProperty);
            if (this.compareAndSetMap(oldMap, newMap)) {
                return newProperty;
            }
            oldMap = this.getMap();
            final Property oldProperty = oldMap.findProperty(newProperty.getKey());
            if (oldProperty != null) {
                return oldProperty;
            }
        }
    }
    
    private void erasePropertyValue(final Property property) {
        if (!(property instanceof UserAccessorProperty)) {
            assert property != null;
            property.setValue(this, this, ScriptRuntime.UNDEFINED, false);
        }
    }
    
    public final boolean deleteOwnProperty(final Property property) {
        this.erasePropertyValue(property);
        PropertyMap oldMap = this.getMap();
        while (true) {
            final PropertyMap newMap = oldMap.deleteProperty(property);
            if (newMap == null) {
                return false;
            }
            if (this.compareAndSetMap(oldMap, newMap)) {
                if (property instanceof UserAccessorProperty) {
                    ((UserAccessorProperty)property).setAccessors(this, this.getMap(), null);
                }
                this.invalidateGlobalConstant(property.getKey());
                return true;
            }
            oldMap = this.getMap();
        }
    }
    
    protected final void initUserAccessors(final String key, final int propertyFlags, final ScriptFunction getter, final ScriptFunction setter) {
        final PropertyMap oldMap = this.getMap();
        final int slot = oldMap.getFreeSpillSlot();
        this.ensureSpillSize(slot);
        this.objectSpill[slot] = new UserAccessorProperty.Accessors(getter, setter);
        PropertyMap newMap;
        do {
            final Property newProperty = new UserAccessorProperty(key, propertyFlags, slot);
            newMap = oldMap.addProperty(newProperty);
        } while (!this.compareAndSetMap(oldMap, newMap));
    }
    
    public final Property modifyOwnProperty(final Property oldProperty, final int propertyFlags, final ScriptFunction getter, final ScriptFunction setter) {
        Property newProperty;
        if (oldProperty instanceof UserAccessorProperty) {
            final UserAccessorProperty uc = (UserAccessorProperty)oldProperty;
            final int slot = uc.getSlot();
            assert uc.getLocalType() == Object.class;
            final UserAccessorProperty.Accessors gs = uc.getAccessors(this);
            assert gs != null;
            gs.set(getter, setter);
            if (uc.getFlags() == propertyFlags) {
                return oldProperty;
            }
            newProperty = new UserAccessorProperty(uc.getKey(), propertyFlags, slot);
        }
        else {
            this.erasePropertyValue(oldProperty);
            newProperty = this.newUserAccessors(oldProperty.getKey(), propertyFlags, getter, setter);
        }
        return this.modifyOwnProperty(oldProperty, newProperty);
    }
    
    public final Property modifyOwnProperty(final Property oldProperty, final int propertyFlags) {
        return this.modifyOwnProperty(oldProperty, oldProperty.setFlags(propertyFlags));
    }
    
    private Property modifyOwnProperty(final Property oldProperty, final Property newProperty) {
        if (oldProperty == newProperty) {
            return newProperty;
        }
        assert newProperty.getKey().equals(oldProperty.getKey()) : "replacing property with different key";
        PropertyMap oldMap = this.getMap();
        while (true) {
            final PropertyMap newMap = oldMap.replaceProperty(oldProperty, newProperty);
            if (this.compareAndSetMap(oldMap, newMap)) {
                return newProperty;
            }
            oldMap = this.getMap();
            final Property oldPropertyLookup = oldMap.findProperty(oldProperty.getKey());
            if (oldPropertyLookup != null && oldPropertyLookup.equals(newProperty)) {
                return oldPropertyLookup;
            }
        }
    }
    
    public final void setUserAccessors(final String key, final ScriptFunction getter, final ScriptFunction setter) {
        final Property oldProperty = this.getMap().findProperty(key);
        if (oldProperty instanceof UserAccessorProperty) {
            this.modifyOwnProperty(oldProperty, oldProperty.getFlags(), getter, setter);
        }
        else {
            this.addOwnProperty(this.newUserAccessors(key, (oldProperty != null) ? oldProperty.getFlags() : 0, getter, setter));
        }
    }
    
    private static int getIntValue(final FindProperty find, final int programPoint) {
        final MethodHandle getter = find.getGetter(Integer.TYPE, programPoint, null);
        if (getter != null) {
            try {
                return getter.invokeExact((Object)find.getGetterReceiver());
            }
            catch (Error | RuntimeException error) {
                final Throwable t;
                final Throwable e = t;
                throw e;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }
    
    private static double getDoubleValue(final FindProperty find, final int programPoint) {
        final MethodHandle getter = find.getGetter(Double.TYPE, programPoint, null);
        if (getter != null) {
            try {
                return getter.invokeExact((Object)find.getGetterReceiver());
            }
            catch (Error | RuntimeException error) {
                final Throwable t;
                final Throwable e = t;
                throw e;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return Double.NaN;
    }
    
    protected MethodHandle getCallMethodHandle(final FindProperty find, final MethodType type, final String bindName) {
        return getCallMethodHandle(find.getObjectValue(), type, bindName);
    }
    
    protected static MethodHandle getCallMethodHandle(final Object value, final MethodType type, final String bindName) {
        return (value instanceof ScriptFunction) ? ((ScriptFunction)value).getCallMethodHandle(type, bindName) : null;
    }
    
    public final Object getWithProperty(final Property property) {
        return new FindProperty(this, this, property).getObjectValue();
    }
    
    public final Property getProperty(final String key) {
        return this.getMap().findProperty(key);
    }
    
    public Object getArgument(final int key) {
        return this.get(key);
    }
    
    public void setArgument(final int key, final Object value) {
        this.set(key, value, 0);
    }
    
    protected Context getContext() {
        return Context.fromClass(this.getClass());
    }
    
    public final PropertyMap getMap() {
        return this.map;
    }
    
    public final void setMap(final PropertyMap map) {
        this.map = map;
    }
    
    protected final boolean compareAndSetMap(final PropertyMap oldMap, final PropertyMap newMap) {
        if (oldMap == this.map) {
            this.map = newMap;
            return true;
        }
        return false;
    }
    
    public final ScriptObject getProto() {
        return this.proto;
    }
    
    public final ScriptObject getProto(final int n) {
        assert n > 0;
        ScriptObject p = this.getProto();
        int i = n;
        while (i-- > 0) {
            p = p.getProto();
        }
        return p;
    }
    
    public final void setProto(final ScriptObject newProto) {
        final ScriptObject oldProto = this.proto;
        if (oldProto != newProto) {
            this.proto = newProto;
            this.getMap().protoChanged(true);
            this.setMap(this.getMap().changeProto(newProto));
        }
    }
    
    public void setInitialProto(final ScriptObject initialProto) {
        this.proto = initialProto;
    }
    
    public static void setGlobalObjectProto(final ScriptObject obj) {
        obj.setInitialProto(Global.objectPrototype());
    }
    
    public final void setPrototypeOf(final Object newProto) {
        if (newProto != null && !(newProto instanceof ScriptObject)) {
            throw ECMAErrors.typeError("cant.set.proto.to.non.object", ScriptRuntime.safeToString(this), ScriptRuntime.safeToString(newProto));
        }
        if (this.isExtensible()) {
            for (ScriptObject p = (ScriptObject)newProto; p != null; p = p.getProto()) {
                if (p == this) {
                    throw ECMAErrors.typeError("circular.__proto__.set", ScriptRuntime.safeToString(this));
                }
            }
            this.setProto((ScriptObject)newProto);
            return;
        }
        if (newProto == this.getProto()) {
            return;
        }
        throw ECMAErrors.typeError("__proto__.set.non.extensible", ScriptRuntime.safeToString(this));
    }
    
    public final void setProtoFromLiteral(final Object newProto) {
        if (newProto == null || newProto instanceof ScriptObject) {
            this.setPrototypeOf(newProto);
        }
        else {
            this.setPrototypeOf(Global.objectPrototype());
        }
    }
    
    public final String[] getOwnKeys(final boolean all) {
        return this.getOwnKeys(all, null);
    }
    
    protected String[] getOwnKeys(final boolean all, final Set<String> nonEnumerable) {
        final List<Object> keys = new ArrayList<Object>();
        final PropertyMap selfMap = this.getMap();
        final ArrayData array = this.getArray();
        final Iterator<Long> iter = array.indexIterator();
        while (iter.hasNext()) {
            keys.add(JSType.toString((double)iter.next()));
        }
        for (final Property property : selfMap.getProperties()) {
            final boolean enumerable = property.isEnumerable();
            final String key = property.getKey();
            if (all) {
                keys.add(key);
            }
            else if (enumerable) {
                if (nonEnumerable == null || !nonEnumerable.contains(key)) {
                    keys.add(key);
                }
            }
            else if (nonEnumerable != null) {
                nonEnumerable.add(key);
            }
        }
        return keys.toArray(new String[keys.size()]);
    }
    
    public boolean hasArrayEntries() {
        return this.getArray().length() > 0L || this.getMap().containsArrayKeys();
    }
    
    public String getClassName() {
        return "Object";
    }
    
    public Object getLength() {
        return this.get("length");
    }
    
    public String safeToString() {
        return "[object " + this.getClassName() + "]";
    }
    
    public Object getDefaultValue(final Class<?> typeHint) {
        return Context.getGlobal().getDefaultValue(this, typeHint);
    }
    
    public boolean isInstance(final ScriptObject instance) {
        return false;
    }
    
    public ScriptObject preventExtensions() {
        for (PropertyMap oldMap = this.getMap(); !this.compareAndSetMap(oldMap, this.getMap().preventExtensions()); oldMap = this.getMap()) {}
        final ArrayData array = this.getArray();
        assert array != null;
        this.setArray(ArrayData.preventExtension(array));
        return this;
    }
    
    public static boolean isArray(final Object obj) {
        return obj instanceof ScriptObject && ((ScriptObject)obj).isArray();
    }
    
    public final boolean isArray() {
        return (this.flags & 0x1) != 0x0;
    }
    
    public final void setIsArray() {
        this.flags |= 0x1;
    }
    
    public final boolean isArguments() {
        return (this.flags & 0x2) != 0x0;
    }
    
    public final void setIsArguments() {
        this.flags |= 0x2;
    }
    
    public boolean isLengthNotWritable() {
        return (this.flags & 0x4) != 0x0;
    }
    
    public void setIsLengthNotWritable() {
        this.flags |= 0x4;
    }
    
    public final ArrayData getArray(final Class<?> elementType) {
        if (elementType == null) {
            return this.arrayData;
        }
        final ArrayData newArrayData = this.arrayData.convert(elementType);
        if (newArrayData != this.arrayData) {
            this.arrayData = newArrayData;
        }
        return newArrayData;
    }
    
    public final ArrayData getArray() {
        return this.arrayData;
    }
    
    public final void setArray(final ArrayData arrayData) {
        this.arrayData = arrayData;
    }
    
    public boolean isExtensible() {
        return this.getMap().isExtensible();
    }
    
    public ScriptObject seal() {
        PropertyMap oldMap = this.getMap();
        while (true) {
            final PropertyMap newMap = this.getMap().seal();
            if (this.compareAndSetMap(oldMap, newMap)) {
                break;
            }
            oldMap = this.getMap();
        }
        this.setArray(ArrayData.seal(this.getArray()));
        return this;
    }
    
    public boolean isSealed() {
        return this.getMap().isSealed();
    }
    
    public ScriptObject freeze() {
        PropertyMap oldMap = this.getMap();
        while (true) {
            final PropertyMap newMap = this.getMap().freeze();
            if (this.compareAndSetMap(oldMap, newMap)) {
                break;
            }
            oldMap = this.getMap();
        }
        this.setArray(ArrayData.freeze(this.getArray()));
        return this;
    }
    
    public boolean isFrozen() {
        return this.getMap().isFrozen();
    }
    
    public boolean isScope() {
        return false;
    }
    
    public final void setIsBuiltin() {
        this.flags |= 0x8;
    }
    
    public final boolean isBuiltin() {
        return (this.flags & 0x8) != 0x0;
    }
    
    public void clear(final boolean strict) {
        final Iterator<String> iter = this.propertyIterator();
        while (iter.hasNext()) {
            this.delete(iter.next(), strict);
        }
    }
    
    public boolean containsKey(final Object key) {
        return this.has(key);
    }
    
    public boolean containsValue(final Object value) {
        final Iterator<Object> iter = this.valueIterator();
        while (iter.hasNext()) {
            if (iter.next().equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    public Set<Map.Entry<Object, Object>> entrySet() {
        final Iterator<String> iter = this.propertyIterator();
        final Set<Map.Entry<Object, Object>> entries = new HashSet<Map.Entry<Object, Object>>();
        while (iter.hasNext()) {
            final Object key = iter.next();
            entries.add(new AbstractMap.SimpleImmutableEntry<Object, Object>(key, this.get(key)));
        }
        return Collections.unmodifiableSet((Set<? extends Map.Entry<Object, Object>>)entries);
    }
    
    public boolean isEmpty() {
        return !this.propertyIterator().hasNext();
    }
    
    public Set<Object> keySet() {
        final Iterator<String> iter = this.propertyIterator();
        final Set<Object> keySet = new HashSet<Object>();
        while (iter.hasNext()) {
            keySet.add(iter.next());
        }
        return Collections.unmodifiableSet((Set<?>)keySet);
    }
    
    public Object put(final Object key, final Object value, final boolean strict) {
        final Object oldValue = this.get(key);
        final int scriptObjectFlags = strict ? 2 : 0;
        this.set(key, value, scriptObjectFlags);
        return oldValue;
    }
    
    public void putAll(final Map<?, ?> otherMap, final boolean strict) {
        final int scriptObjectFlags = strict ? 2 : 0;
        for (final Map.Entry<?, ?> entry : otherMap.entrySet()) {
            this.set(entry.getKey(), entry.getValue(), scriptObjectFlags);
        }
    }
    
    public Object remove(final Object key, final boolean strict) {
        final Object oldValue = this.get(key);
        this.delete(key, strict);
        return oldValue;
    }
    
    public int size() {
        int n = 0;
        final Iterator<String> iter = this.propertyIterator();
        while (iter.hasNext()) {
            ++n;
            iter.next();
        }
        return n;
    }
    
    public Collection<Object> values() {
        final List<Object> values = new ArrayList<Object>(this.size());
        final Iterator<Object> iter = this.valueIterator();
        while (iter.hasNext()) {
            values.add(iter.next());
        }
        return Collections.unmodifiableList((List<?>)values);
    }
    
    public GuardedInvocation lookup(final CallSiteDescriptor desc, final LinkRequest request) {
        final int c = desc.getNameTokenCount();
        final String s;
        final String operator = s = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        switch (s) {
            case "getProp":
            case "getElem":
            case "getMethod": {
                return (c > 2) ? this.findGetMethod(desc, request, operator) : this.findGetIndexMethod(desc, request);
            }
            case "setProp":
            case "setElem": {
                return (c > 2) ? this.findSetMethod(desc, request) : this.findSetIndexMethod(desc, request);
            }
            case "call": {
                return this.findCallMethod(desc, request);
            }
            case "new": {
                return this.findNewMethod(desc, request);
            }
            case "callMethod": {
                return this.findCallMethodMethod(desc, request);
            }
            default: {
                return null;
            }
        }
    }
    
    protected GuardedInvocation findNewMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.notAFunction(desc);
    }
    
    protected GuardedInvocation findCallMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.notAFunction(desc);
    }
    
    private GuardedInvocation notAFunction(final CallSiteDescriptor desc) {
        throw ECMAErrors.typeError("not.a.function", NashornCallSiteDescriptor.getFunctionErrorMessage(desc, this));
    }
    
    protected GuardedInvocation findCallMethodMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType callType = desc.getMethodType();
        final CallSiteDescriptor getterType = desc.changeMethodType(MethodType.methodType(Object.class, callType.parameterType(0)));
        final GuardedInvocation getter = this.findGetMethod(getterType, request, "getMethod");
        final MethodHandle argDroppingGetter = Lookup.MH.dropArguments(getter.getInvocation(), 1, callType.parameterList().subList(1, callType.parameterCount()));
        final MethodHandle invoker = Bootstrap.createDynamicInvoker("dyn:call", callType.insertParameterTypes(0, (Class<?>[])new Class[] { argDroppingGetter.type().returnType() }));
        return getter.replaceMethods(Lookup.MH.foldArguments(invoker, argDroppingGetter), getter.getGuard());
    }
    
    boolean hasWithScope() {
        return false;
    }
    
    static MethodHandle addProtoFilter(final MethodHandle methodHandle, final int depth) {
        if (depth == 0) {
            return methodHandle;
        }
        final int listIndex = depth - 1;
        MethodHandle filter = (listIndex < ScriptObject.PROTO_FILTERS.size()) ? ScriptObject.PROTO_FILTERS.get(listIndex) : null;
        if (filter == null) {
            filter = addProtoFilter(ScriptObject.GETPROTO, depth - 1);
            ScriptObject.PROTO_FILTERS.add(null);
            ScriptObject.PROTO_FILTERS.set(listIndex, filter);
        }
        return Lookup.MH.filterArguments(methodHandle, 0, filter.asType(filter.type().changeReturnType(methodHandle.type().parameterType(0))));
    }
    
    protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        String name = desc.getNameToken(2);
        if (NashornCallSiteDescriptor.isApplyToCall(desc) && Global.isBuiltinFunctionPrototypeApply()) {
            name = "call";
        }
        if (request.isCallSiteUnstable() || this.hasWithScope()) {
            return findMegaMorphicGetMethod(desc, name, "getMethod".equals(operator));
        }
        final FindProperty find = this.findProperty(name, true);
        if (find != null) {
            final GlobalConstants globalConstants = this.getGlobalConstants();
            if (globalConstants != null) {
                final GuardedInvocation cinv = globalConstants.findGetMethod(find, this, desc);
                if (cinv != null) {
                    return cinv;
                }
            }
            final Class<?> returnType = desc.getMethodType().returnType();
            final Property property = find.getProperty();
            final int programPoint = NashornCallSiteDescriptor.isOptimistic(desc) ? NashornCallSiteDescriptor.getProgramPoint(desc) : -1;
            MethodHandle mh = find.getGetter(returnType, programPoint, request);
            final MethodHandle guard = NashornGuards.getGuard(this, property, desc, explicitInstanceOfCheck);
            final ScriptObject owner = find.getOwner();
            final Class<ClassCastException> exception = explicitInstanceOfCheck ? null : ClassCastException.class;
            SwitchPoint[] protoSwitchPoints;
            if (mh == null) {
                mh = Lookup.emptyGetter(returnType);
                protoSwitchPoints = this.getProtoSwitchPoints(name, owner);
            }
            else if (!find.isSelf()) {
                assert mh.type().returnType().equals(returnType) : "return type mismatch for getter " + mh.type().returnType() + " != " + returnType;
                if (!(property instanceof UserAccessorProperty)) {
                    mh = addProtoFilter(mh, find.getProtoChainLength());
                }
                protoSwitchPoints = this.getProtoSwitchPoints(name, owner);
            }
            else {
                protoSwitchPoints = null;
            }
            final GuardedInvocation inv = new GuardedInvocation(mh, guard, protoSwitchPoints, exception);
            return inv.addSwitchPoint(this.findBuiltinSwitchPoint(name));
        }
        switch (operator) {
            case "getElem":
            case "getProp": {
                return this.noSuchProperty(desc, request);
            }
            case "getMethod": {
                return this.noSuchMethod(desc, request);
            }
            default: {
                throw new AssertionError((Object)operator);
            }
        }
    }
    
    private static GuardedInvocation findMegaMorphicGetMethod(final CallSiteDescriptor desc, final String name, final boolean isMethod) {
        Context.getContextTrusted().getLogger(ObjectClassGenerator.class).warning("Megamorphic getter: " + desc + " " + name + " " + isMethod);
        final MethodHandle invoker = Lookup.MH.insertArguments(ScriptObject.MEGAMORPHIC_GET, 1, name, isMethod, NashornCallSiteDescriptor.isScope(desc));
        final MethodHandle guard = getScriptObjectGuard(desc.getMethodType(), true);
        return new GuardedInvocation(invoker, guard);
    }
    
    private Object megamorphicGet(final String key, final boolean isMethod, final boolean isScope) {
        final FindProperty find = this.findProperty(key, true);
        if (find != null) {
            return find.getObjectValue();
        }
        return isMethod ? this.getNoSuchMethod(key, isScope, -1) : this.invokeNoSuchProperty(key, isScope, -1);
    }
    
    private void declareAndSet(final String key, final Object value) {
        final PropertyMap oldMap = this.getMap();
        final FindProperty find = this.findProperty(key, false);
        assert find != null;
        final Property property = find.getProperty();
        assert property != null;
        assert property.needsDeclaration();
        final PropertyMap newMap = oldMap.replaceProperty(property, property.removeFlags(512));
        this.setMap(newMap);
        this.set(key, value, 0);
    }
    
    protected GuardedInvocation findGetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType callType = desc.getMethodType();
        final Class<?> returnType = callType.returnType();
        final Class<?> returnClass = returnType.isPrimitive() ? returnType : Object.class;
        final Class<?> keyClass = callType.parameterType(1);
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        String name;
        if (returnClass.isPrimitive()) {
            final String returnTypeName = returnClass.getName();
            name = "get" + Character.toUpperCase(returnTypeName.charAt(0)) + returnTypeName.substring(1, returnTypeName.length());
        }
        else {
            name = "get";
        }
        final MethodHandle mh = this.findGetIndexMethodHandle(returnClass, name, keyClass, desc);
        return new GuardedInvocation(mh, getScriptObjectGuard(callType, explicitInstanceOfCheck), (SwitchPoint)null, explicitInstanceOfCheck ? null : ClassCastException.class);
    }
    
    private static MethodHandle getScriptObjectGuard(final MethodType type, final boolean explicitInstanceOfCheck) {
        return ScriptObject.class.isAssignableFrom(type.parameterType(0)) ? null : NashornGuards.getScriptObjectGuard(explicitInstanceOfCheck);
    }
    
    protected MethodHandle findGetIndexMethodHandle(final Class<?> returnType, final String name, final Class<?> elementType, final CallSiteDescriptor desc) {
        if (!returnType.isPrimitive()) {
            return findOwnMH_V(this.getClass(), name, returnType, elementType);
        }
        return Lookup.MH.insertArguments(findOwnMH_V(this.getClass(), name, returnType, elementType, Integer.TYPE), 2, NashornCallSiteDescriptor.isOptimistic(desc) ? NashornCallSiteDescriptor.getProgramPoint(desc) : -1);
    }
    
    public final SwitchPoint[] getProtoSwitchPoints(final String name, final ScriptObject owner) {
        if (owner == this || this.getProto() == null) {
            return null;
        }
        final List<SwitchPoint> switchPoints = new ArrayList<SwitchPoint>();
        for (ScriptObject obj = this; obj != owner && obj.getProto() != null; obj = obj.getProto()) {
            final ScriptObject parent = obj.getProto();
            parent.getMap().addListener(name, obj.getMap());
            final SwitchPoint sp = parent.getMap().getSharedProtoSwitchPoint();
            if (sp != null && !sp.hasBeenInvalidated()) {
                switchPoints.add(sp);
            }
        }
        switchPoints.add(this.getMap().getSwitchPoint(name));
        return switchPoints.toArray(new SwitchPoint[switchPoints.size()]);
    }
    
    private void checkSharedProtoMap() {
        if (this.getMap().isInvalidSharedMapFor(this.getProto())) {
            this.setMap(this.getMap().makeUnsharedCopy());
        }
    }
    
    protected GuardedInvocation findSetMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(2);
        if (request.isCallSiteUnstable() || this.hasWithScope()) {
            return this.findMegaMorphicSetMethod(desc, name);
        }
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        FindProperty find = this.findProperty(name, true, this);
        if (find != null && find.isInherited() && !(find.getProperty() instanceof UserAccessorProperty)) {
            if (this.isExtensible() && !find.getProperty().isWritable()) {
                return this.createEmptySetMethod(desc, explicitInstanceOfCheck, "property.not.writable", true);
            }
            if (!NashornCallSiteDescriptor.isScope(desc) || !find.getOwner().isScope()) {
                find = null;
            }
        }
        if (find != null) {
            if (!find.getProperty().isWritable() && !NashornCallSiteDescriptor.isDeclaration(desc)) {
                if (NashornCallSiteDescriptor.isScope(desc) && find.getProperty().isLexicalBinding()) {
                    throw ECMAErrors.typeError("assign.constant", name);
                }
                return this.createEmptySetMethod(desc, explicitInstanceOfCheck, "property.not.writable", true);
            }
        }
        else if (!this.isExtensible()) {
            return this.createEmptySetMethod(desc, explicitInstanceOfCheck, "object.non.extensible", false);
        }
        final GuardedInvocation inv = new SetMethodCreator(this, find, desc, request).createGuardedInvocation(this.findBuiltinSwitchPoint(name));
        final GlobalConstants globalConstants = this.getGlobalConstants();
        if (globalConstants != null) {
            final GuardedInvocation cinv = globalConstants.findSetMethod(find, this, inv, desc, request);
            if (cinv != null) {
                return cinv;
            }
        }
        return inv;
    }
    
    private GlobalConstants getGlobalConstants() {
        return this.isGlobal() ? this.getContext().getGlobalConstants() : null;
    }
    
    private GuardedInvocation createEmptySetMethod(final CallSiteDescriptor desc, final boolean explicitInstanceOfCheck, final String strictErrorMessage, final boolean canBeFastScope) {
        final String name = desc.getNameToken(2);
        if (NashornCallSiteDescriptor.isStrict(desc)) {
            throw ECMAErrors.typeError(strictErrorMessage, name, ScriptRuntime.safeToString(this));
        }
        if (!ScriptObject.$assertionsDisabled && !canBeFastScope && NashornCallSiteDescriptor.isFastScope(desc)) {
            throw new AssertionError();
        }
        return new GuardedInvocation(Lookup.EMPTY_SETTER, NashornGuards.getMapGuard(this.getMap(), explicitInstanceOfCheck), this.getProtoSwitchPoints(name, null), explicitInstanceOfCheck ? null : ClassCastException.class);
    }
    
    private boolean extensionCheck(final boolean isStrict, final String name) {
        if (this.isExtensible()) {
            return true;
        }
        if (isStrict) {
            throw ECMAErrors.typeError("object.non.extensible", name, ScriptRuntime.safeToString(this));
        }
        return false;
    }
    
    private GuardedInvocation findMegaMorphicSetMethod(final CallSiteDescriptor desc, final String name) {
        final MethodType type = desc.getMethodType().insertParameterTypes(1, (Class<?>[])new Class[] { Object.class });
        final GuardedInvocation inv = findSetIndexMethod(this.getClass(), desc, false, type);
        return inv.replaceMethods(Lookup.MH.insertArguments(inv.getInvocation(), 1, name), inv.getGuard());
    }
    
    private static Object globalFilter(final Object object) {
        ScriptObject sobj;
        for (sobj = (ScriptObject)object; sobj != null && !(sobj instanceof Global); sobj = sobj.getProto()) {}
        return sobj;
    }
    
    protected GuardedInvocation findSetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return findSetIndexMethod(this.getClass(), desc, NashornGuards.explicitInstanceOfCheck(desc, request), desc.getMethodType());
    }
    
    private static GuardedInvocation findSetIndexMethod(final Class<? extends ScriptObject> clazz, final CallSiteDescriptor desc, final boolean explicitInstanceOfCheck, final MethodType callType) {
        assert callType.parameterCount() == 3;
        final Class<?> keyClass = callType.parameterType(1);
        final Class<?> valueClass = callType.parameterType(2);
        MethodHandle methodHandle = findOwnMH_V(clazz, "set", Void.TYPE, keyClass, valueClass, Integer.TYPE);
        methodHandle = Lookup.MH.insertArguments(methodHandle, 3, NashornCallSiteDescriptor.getFlags(desc));
        return new GuardedInvocation(methodHandle, getScriptObjectGuard(callType, explicitInstanceOfCheck), (SwitchPoint)null, explicitInstanceOfCheck ? null : ClassCastException.class);
    }
    
    public GuardedInvocation noSuchMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(2);
        final FindProperty find = this.findProperty("__noSuchMethod__", true);
        final boolean scopeCall = this.isScope() && NashornCallSiteDescriptor.isScope(desc);
        if (find == null) {
            return this.noSuchProperty(desc, request);
        }
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        final Object value = find.getObjectValue();
        if (!(value instanceof ScriptFunction)) {
            return this.createEmptyGetter(desc, explicitInstanceOfCheck, name);
        }
        final ScriptFunction func = (ScriptFunction)value;
        final Object thiz = (scopeCall && func.isStrict()) ? ScriptRuntime.UNDEFINED : this;
        return new GuardedInvocation(Lookup.MH.dropArguments(Lookup.MH.constant(ScriptFunction.class, func.createBound(thiz, new Object[] { name })), 0, Object.class), NashornGuards.combineGuards(NashornGuards.getIdentityGuard(this), NashornGuards.getMapGuard(this.getMap(), true)));
    }
    
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(2);
        final FindProperty find = this.findProperty("__noSuchProperty__", true);
        final boolean scopeAccess = this.isScope() && NashornCallSiteDescriptor.isScope(desc);
        if (find != null) {
            final Object value = find.getObjectValue();
            ScriptFunction func = null;
            MethodHandle mh = null;
            if (value instanceof ScriptFunction) {
                func = (ScriptFunction)value;
                mh = getCallMethodHandle(func, desc.getMethodType(), name);
            }
            if (mh != null) {
                assert func != null;
                if (scopeAccess && func.isStrict()) {
                    mh = bindTo(mh, ScriptRuntime.UNDEFINED);
                }
                return new GuardedInvocation(mh, find.isSelf() ? getKnownFunctionPropertyGuardSelf(this.getMap(), find.getGetter(Object.class, -1, request), func) : getKnownFunctionPropertyGuardProto(this.getMap(), find.getGetter(Object.class, -1, request), find.getProtoChainLength(), func), this.getProtoSwitchPoints("__noSuchProperty__", find.getOwner()), null);
            }
        }
        if (scopeAccess) {
            throw ECMAErrors.referenceError("not.defined", name);
        }
        return this.createEmptyGetter(desc, NashornGuards.explicitInstanceOfCheck(desc, request), name);
    }
    
    protected Object invokeNoSuchProperty(final String name, final boolean isScope, final int programPoint) {
        final FindProperty find = this.findProperty("__noSuchProperty__", true);
        final Object func = (find != null) ? find.getObjectValue() : null;
        Object ret = ScriptRuntime.UNDEFINED;
        if (func instanceof ScriptFunction) {
            final ScriptFunction sfunc = (ScriptFunction)func;
            final Object self = (isScope && sfunc.isStrict()) ? ScriptRuntime.UNDEFINED : this;
            ret = ScriptRuntime.apply(sfunc, self, name);
        }
        else if (isScope) {
            throw ECMAErrors.referenceError("not.defined", name);
        }
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            throw new UnwarrantedOptimismException(ret, programPoint);
        }
        return ret;
    }
    
    private Object getNoSuchMethod(final String name, final boolean isScope, final int programPoint) {
        final FindProperty find = this.findProperty("__noSuchMethod__", true);
        if (find == null) {
            return this.invokeNoSuchProperty(name, isScope, programPoint);
        }
        final Object value = find.getObjectValue();
        if (value instanceof ScriptFunction) {
            final ScriptFunction func = (ScriptFunction)value;
            final Object self = (isScope && func.isStrict()) ? ScriptRuntime.UNDEFINED : this;
            return func.createBound(self, new Object[] { name });
        }
        if (isScope) {
            throw ECMAErrors.referenceError("not.defined", name);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    private GuardedInvocation createEmptyGetter(final CallSiteDescriptor desc, final boolean explicitInstanceOfCheck, final String name) {
        if (NashornCallSiteDescriptor.isOptimistic(desc)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, NashornCallSiteDescriptor.getProgramPoint(desc), Type.OBJECT);
        }
        return new GuardedInvocation(Lookup.emptyGetter(desc.getMethodType().returnType()), NashornGuards.getMapGuard(this.getMap(), explicitInstanceOfCheck), this.getProtoSwitchPoints(name, null), explicitInstanceOfCheck ? null : ClassCastException.class);
    }
    
    private Property addSpillProperty(final String key, final int flags, final Object value, final boolean hasInitialValue) {
        final PropertyMap propertyMap = this.getMap();
        final int fieldSlot = propertyMap.getFreeFieldSlot();
        final int propertyFlags = flags | (this.useDualFields() ? 2048 : 0);
        Property property;
        if (fieldSlot > -1) {
            property = (hasInitialValue ? new AccessorProperty(key, propertyFlags, fieldSlot, this, value) : new AccessorProperty(key, propertyFlags, this.getClass(), fieldSlot));
            property = this.addOwnProperty(property);
        }
        else {
            final int spillSlot = propertyMap.getFreeSpillSlot();
            property = (hasInitialValue ? new SpillProperty(key, propertyFlags, spillSlot, this, value) : new SpillProperty(key, propertyFlags, spillSlot));
            property = this.addOwnProperty(property);
            this.ensureSpillSize(property.getSlot());
        }
        return property;
    }
    
    MethodHandle addSpill(final Class<?> type, final String key) {
        return this.addSpillProperty(key, 0, null, false).getSetter(type, this.getMap());
    }
    
    protected static MethodHandle pairArguments(final MethodHandle methodHandle, final MethodType callType) {
        return pairArguments(methodHandle, callType, null);
    }
    
    public static MethodHandle pairArguments(final MethodHandle methodHandle, final MethodType callType, final Boolean callerVarArg) {
        final MethodType methodType = methodHandle.type();
        if (methodType.equals((Object)callType.changeReturnType(methodType.returnType()))) {
            return methodHandle;
        }
        final int parameterCount = methodType.parameterCount();
        final int callCount = callType.parameterCount();
        final boolean isCalleeVarArg = parameterCount > 0 && methodType.parameterType(parameterCount - 1).isArray();
        final boolean isCallerVarArg = (callerVarArg != null) ? callerVarArg : (callCount > 0 && callType.parameterType(callCount - 1).isArray());
        if (isCalleeVarArg) {
            return isCallerVarArg ? methodHandle : Lookup.MH.asCollector(methodHandle, Object[].class, callCount - parameterCount + 1);
        }
        if (isCallerVarArg) {
            return adaptHandleToVarArgCallSite(methodHandle, callCount);
        }
        if (callCount < parameterCount) {
            final int missingArgs = parameterCount - callCount;
            final Object[] fillers = new Object[missingArgs];
            Arrays.fill(fillers, ScriptRuntime.UNDEFINED);
            if (isCalleeVarArg) {
                fillers[missingArgs - 1] = ScriptRuntime.EMPTY_ARRAY;
            }
            return Lookup.MH.insertArguments(methodHandle, parameterCount - missingArgs, fillers);
        }
        if (callCount > parameterCount) {
            final int discardedArgs = callCount - parameterCount;
            final Class<?>[] discards = (Class<?>[])new Class[discardedArgs];
            Arrays.fill(discards, Object.class);
            return Lookup.MH.dropArguments(methodHandle, callCount - discardedArgs, discards);
        }
        return methodHandle;
    }
    
    static MethodHandle adaptHandleToVarArgCallSite(final MethodHandle mh, final int callSiteParamCount) {
        final int spreadArgs = mh.type().parameterCount() - callSiteParamCount + 1;
        return Lookup.MH.filterArguments(Lookup.MH.asSpreader(mh, Object[].class, spreadArgs), callSiteParamCount - 1, Lookup.MH.insertArguments(ScriptObject.TRUNCATINGFILTER, 0, spreadArgs));
    }
    
    private static Object[] truncatingFilter(final int n, final Object[] array) {
        final int length = (array == null) ? 0 : array.length;
        if (n == length) {
            return (array == null) ? ScriptRuntime.EMPTY_ARRAY : array;
        }
        final Object[] newArray = new Object[n];
        if (array != null) {
            System.arraycopy(array, 0, newArray, 0, Math.min(n, length));
        }
        if (length < n) {
            final Object fill = ScriptRuntime.UNDEFINED;
            for (int i = length; i < n; ++i) {
                newArray[i] = fill;
            }
        }
        return newArray;
    }
    
    public final void setLength(final long newLength) {
        final ArrayData data = this.getArray();
        final long arrayLength = data.length();
        if (newLength == arrayLength) {
            return;
        }
        if (newLength > arrayLength) {
            this.setArray(data.ensure(newLength - 1L).safeDelete(arrayLength, newLength - 1L, false));
            return;
        }
        if (newLength < arrayLength) {
            long actualLength = newLength;
            if (this.getMap().containsArrayKeys()) {
                for (long l = arrayLength - 1L; l >= newLength; --l) {
                    final FindProperty find = this.findProperty(JSType.toString((double)l), false);
                    if (find != null) {
                        if (!find.getProperty().isConfigurable()) {
                            actualLength = l + 1L;
                            break;
                        }
                        this.deleteOwnProperty(find.getProperty());
                    }
                }
            }
            this.setArray(data.shrink(actualLength));
            data.setLength(actualLength);
        }
    }
    
    private int getInt(final int index, final String key, final int programPoint) {
        if (ArrayIndex.isValidArrayIndex(index)) {
            ScriptObject object = this;
            while (true) {
                if (object.getMap().containsArrayKeys()) {
                    final FindProperty find = object.findProperty(key, false, this);
                    if (find != null) {
                        return getIntValue(find, programPoint);
                    }
                }
                if ((object = object.getProto()) == null) {
                    break;
                }
                final ArrayData array = object.getArray();
                if (array.has(index)) {
                    return UnwarrantedOptimismException.isValid(programPoint) ? array.getIntOptimistic(index, programPoint) : array.getInt(index);
                }
            }
        }
        else {
            final FindProperty find2 = this.findProperty(key, true);
            if (find2 != null) {
                return getIntValue(find2, programPoint);
            }
        }
        return JSType.toInt32(this.invokeNoSuchProperty(key, false, programPoint));
    }
    
    @Override
    public int getInt(final Object key, final int programPoint) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getIntOptimistic(index, programPoint) : array.getInt(index);
        }
        return this.getInt(index, JSType.toString(primitiveKey), programPoint);
    }
    
    @Override
    public int getInt(final double key, final int programPoint) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getIntOptimistic(index, programPoint) : array.getInt(index);
        }
        return this.getInt(index, JSType.toString(key), programPoint);
    }
    
    @Override
    public int getInt(final int key, final int programPoint) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getIntOptimistic(key, programPoint) : array.getInt(key);
        }
        return this.getInt(index, JSType.toString(key), programPoint);
    }
    
    private double getDouble(final int index, final String key, final int programPoint) {
        if (ArrayIndex.isValidArrayIndex(index)) {
            ScriptObject object = this;
            while (true) {
                if (object.getMap().containsArrayKeys()) {
                    final FindProperty find = object.findProperty(key, false, this);
                    if (find != null) {
                        return getDoubleValue(find, programPoint);
                    }
                }
                if ((object = object.getProto()) == null) {
                    break;
                }
                final ArrayData array = object.getArray();
                if (array.has(index)) {
                    return UnwarrantedOptimismException.isValid(programPoint) ? array.getDoubleOptimistic(index, programPoint) : array.getDouble(index);
                }
            }
        }
        else {
            final FindProperty find2 = this.findProperty(key, true);
            if (find2 != null) {
                return getDoubleValue(find2, programPoint);
            }
        }
        return JSType.toNumber(this.invokeNoSuchProperty(key, false, -1));
    }
    
    @Override
    public double getDouble(final Object key, final int programPoint) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getDoubleOptimistic(index, programPoint) : array.getDouble(index);
        }
        return this.getDouble(index, JSType.toString(primitiveKey), programPoint);
    }
    
    @Override
    public double getDouble(final double key, final int programPoint) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getDoubleOptimistic(index, programPoint) : array.getDouble(index);
        }
        return this.getDouble(index, JSType.toString(key), programPoint);
    }
    
    @Override
    public double getDouble(final int key, final int programPoint) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return UnwarrantedOptimismException.isValid(programPoint) ? array.getDoubleOptimistic(key, programPoint) : array.getDouble(key);
        }
        return this.getDouble(index, JSType.toString(key), programPoint);
    }
    
    private Object get(final int index, final String key) {
        if (ArrayIndex.isValidArrayIndex(index)) {
            ScriptObject object = this;
            while (true) {
                if (object.getMap().containsArrayKeys()) {
                    final FindProperty find = object.findProperty(key, false, this);
                    if (find != null) {
                        return find.getObjectValue();
                    }
                }
                if ((object = object.getProto()) == null) {
                    break;
                }
                final ArrayData array = object.getArray();
                if (array.has(index)) {
                    return array.getObject(index);
                }
            }
        }
        else {
            final FindProperty find2 = this.findProperty(key, true);
            if (find2 != null) {
                return find2.getObjectValue();
            }
        }
        return this.invokeNoSuchProperty(key, false, -1);
    }
    
    @Override
    public Object get(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return array.getObject(index);
        }
        return this.get(index, JSType.toString(primitiveKey));
    }
    
    @Override
    public Object get(final double key) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return array.getObject(index);
        }
        return this.get(index, JSType.toString(key));
    }
    
    @Override
    public Object get(final int key) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (array.has(index)) {
            return array.getObject(index);
        }
        return this.get(index, JSType.toString(key));
    }
    
    private boolean doesNotHaveCheckArrayKeys(final long longIndex, final int value, final int callSiteFlags) {
        if (this.getMap().containsArrayKeys()) {
            final String key = JSType.toString((double)longIndex);
            final FindProperty find = this.findProperty(key, true);
            if (find != null) {
                this.setObject(find, callSiteFlags, key, value);
                return true;
            }
        }
        return false;
    }
    
    private boolean doesNotHaveCheckArrayKeys(final long longIndex, final long value, final int callSiteFlags) {
        if (this.getMap().containsArrayKeys()) {
            final String key = JSType.toString((double)longIndex);
            final FindProperty find = this.findProperty(key, true);
            if (find != null) {
                this.setObject(find, callSiteFlags, key, value);
                return true;
            }
        }
        return false;
    }
    
    private boolean doesNotHaveCheckArrayKeys(final long longIndex, final double value, final int callSiteFlags) {
        if (this.getMap().containsArrayKeys()) {
            final String key = JSType.toString((double)longIndex);
            final FindProperty find = this.findProperty(key, true);
            if (find != null) {
                this.setObject(find, callSiteFlags, key, value);
                return true;
            }
        }
        return false;
    }
    
    private boolean doesNotHaveCheckArrayKeys(final long longIndex, final Object value, final int callSiteFlags) {
        if (this.getMap().containsArrayKeys()) {
            final String key = JSType.toString((double)longIndex);
            final FindProperty find = this.findProperty(key, true);
            if (find != null) {
                this.setObject(find, callSiteFlags, key, value);
                return true;
            }
        }
        return false;
    }
    
    private boolean doesNotHaveEnsureLength(final long longIndex, final long oldLength, final int callSiteFlags) {
        if (longIndex >= oldLength) {
            if (!this.isExtensible()) {
                if (NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)) {
                    throw ECMAErrors.typeError("object.non.extensible", JSType.toString((double)longIndex), ScriptRuntime.safeToString(this));
                }
                return true;
            }
            else {
                this.setArray(this.getArray().ensure(longIndex));
            }
        }
        return false;
    }
    
    private void doesNotHave(final int index, final int value, final int callSiteFlags) {
        final long oldLength = this.getArray().length();
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (!this.doesNotHaveCheckArrayKeys(longIndex, value, callSiteFlags) && !this.doesNotHaveEnsureLength(longIndex, oldLength, callSiteFlags)) {
            final boolean strict = NashornCallSiteDescriptor.isStrictFlag(callSiteFlags);
            this.setArray(this.getArray().set(index, value, strict).safeDelete(oldLength, longIndex - 1L, strict));
        }
    }
    
    private void doesNotHave(final int index, final double value, final int callSiteFlags) {
        final long oldLength = this.getArray().length();
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (!this.doesNotHaveCheckArrayKeys(longIndex, value, callSiteFlags) && !this.doesNotHaveEnsureLength(longIndex, oldLength, callSiteFlags)) {
            final boolean strict = NashornCallSiteDescriptor.isStrictFlag(callSiteFlags);
            this.setArray(this.getArray().set(index, value, strict).safeDelete(oldLength, longIndex - 1L, strict));
        }
    }
    
    private void doesNotHave(final int index, final Object value, final int callSiteFlags) {
        final long oldLength = this.getArray().length();
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (!this.doesNotHaveCheckArrayKeys(longIndex, value, callSiteFlags) && !this.doesNotHaveEnsureLength(longIndex, oldLength, callSiteFlags)) {
            final boolean strict = NashornCallSiteDescriptor.isStrictFlag(callSiteFlags);
            this.setArray(this.getArray().set(index, value, strict).safeDelete(oldLength, longIndex - 1L, strict));
        }
    }
    
    public final void setObject(final FindProperty find, final int callSiteFlags, final String key, final Object value) {
        FindProperty f = find;
        this.invalidateGlobalConstant(key);
        if (f != null && f.isInherited() && !(f.getProperty() instanceof UserAccessorProperty)) {
            final boolean isScope = NashornCallSiteDescriptor.isScopeFlag(callSiteFlags);
            if (isScope && f.getSelf() != this) {
                f.getSelf().setObject(null, 0, key, value);
                return;
            }
            if (!isScope || !f.getOwner().isScope()) {
                f = null;
            }
        }
        if (f != null) {
            if (!f.getProperty().isWritable()) {
                if (NashornCallSiteDescriptor.isScopeFlag(callSiteFlags) && f.getProperty().isLexicalBinding()) {
                    throw ECMAErrors.typeError("assign.constant", key);
                }
                if (NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)) {
                    throw ECMAErrors.typeError("property.not.writable", key, ScriptRuntime.safeToString(this));
                }
            }
            else {
                f.setValue(value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags));
            }
        }
        else if (!this.isExtensible()) {
            if (NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)) {
                throw ECMAErrors.typeError("object.non.extensible", key, ScriptRuntime.safeToString(this));
            }
        }
        else {
            ScriptObject sobj = this;
            if (this.isScope()) {
                while (sobj != null && !(sobj instanceof Global)) {
                    sobj = sobj.getProto();
                }
                assert sobj != null : "no parent global object in scope";
            }
            sobj.addSpillProperty(key, 0, value, true);
        }
    }
    
    @Override
    public void set(final Object key, final int value, final int callSiteFlags) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(primitiveKey);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final Object key, final double value, final int callSiteFlags) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(primitiveKey);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final Object key, final Object value, final int callSiteFlags) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(primitiveKey);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, value);
    }
    
    @Override
    public void set(final double key, final int value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final double key, final double value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final double key, final Object value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, value);
    }
    
    @Override
    public void set(final int key, final int value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            if (this.getArray().has(index)) {
                final ArrayData data = this.getArray();
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final int key, final double value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, JSType.toObject(value));
    }
    
    @Override
    public void set(final int key, final Object value, final int callSiteFlags) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (ArrayIndex.isValidArrayIndex(index)) {
            final ArrayData data = this.getArray();
            if (data.has(index)) {
                this.setArray(data.set(index, value, NashornCallSiteDescriptor.isStrictFlag(callSiteFlags)));
            }
            else {
                this.doesNotHave(index, value, callSiteFlags);
            }
            return;
        }
        final String propName = JSType.toString(key);
        this.setObject(this.findProperty(propName, true), callSiteFlags, propName, value);
    }
    
    @Override
    public boolean has(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasArrayProperty(index) : this.hasProperty(JSType.toString(primitiveKey), true);
    }
    
    @Override
    public boolean has(final double key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasArrayProperty(index) : this.hasProperty(JSType.toString(key), true);
    }
    
    @Override
    public boolean has(final int key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasArrayProperty(index) : this.hasProperty(JSType.toString(key), true);
    }
    
    private boolean hasArrayProperty(final int index) {
        boolean hasArrayKeys = false;
        for (ScriptObject self = this; self != null; self = self.getProto()) {
            if (self.getArray().has(index)) {
                return true;
            }
            hasArrayKeys = (hasArrayKeys || self.getMap().containsArrayKeys());
        }
        return hasArrayKeys && this.hasProperty(ArrayIndex.toKey(index), true);
    }
    
    @Override
    public boolean hasOwnProperty(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasOwnArrayProperty(index) : this.hasProperty(JSType.toString(primitiveKey), false);
    }
    
    @Override
    public boolean hasOwnProperty(final int key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasOwnArrayProperty(index) : this.hasProperty(JSType.toString(key), false);
    }
    
    @Override
    public boolean hasOwnProperty(final double key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return ArrayIndex.isValidArrayIndex(index) ? this.hasOwnArrayProperty(index) : this.hasProperty(JSType.toString(key), false);
    }
    
    private boolean hasOwnArrayProperty(final int index) {
        return this.getArray().has(index) || (this.getMap().containsArrayKeys() && this.hasProperty(ArrayIndex.toKey(index), false));
    }
    
    @Override
    public boolean delete(final int key, final boolean strict) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (!array.has(index)) {
            return this.deleteObject(JSType.toObject(key), strict);
        }
        if (array.canDelete(index, strict)) {
            this.setArray(array.delete(index));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean delete(final double key, final boolean strict) {
        final int index = ArrayIndex.getArrayIndex(key);
        final ArrayData array = this.getArray();
        if (!array.has(index)) {
            return this.deleteObject(JSType.toObject(key), strict);
        }
        if (array.canDelete(index, strict)) {
            this.setArray(array.delete(index));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        final ArrayData array = this.getArray();
        if (!array.has(index)) {
            return this.deleteObject(primitiveKey, strict);
        }
        if (array.canDelete(index, strict)) {
            this.setArray(array.delete(index));
            return true;
        }
        return false;
    }
    
    private boolean deleteObject(final Object key, final boolean strict) {
        final String propName = JSType.toString(key);
        final FindProperty find = this.findProperty(propName, false);
        if (find == null) {
            return true;
        }
        if (find.getProperty().isConfigurable()) {
            final Property prop = find.getProperty();
            this.deleteOwnProperty(prop);
            return true;
        }
        if (strict) {
            throw ECMAErrors.typeError("cant.delete.property", propName, ScriptRuntime.safeToString(this));
        }
        return false;
    }
    
    public final ScriptObject copy() {
        try {
            return this.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected ScriptObject clone() throws CloneNotSupportedException {
        final ScriptObject clone = (ScriptObject)super.clone();
        if (this.objectSpill != null) {
            clone.objectSpill = this.objectSpill.clone();
            if (this.primitiveSpill != null) {
                clone.primitiveSpill = this.primitiveSpill.clone();
            }
        }
        clone.arrayData = this.arrayData.copy();
        return clone;
    }
    
    protected final UserAccessorProperty newUserAccessors(final String key, final int propertyFlags, final ScriptFunction getter, final ScriptFunction setter) {
        final UserAccessorProperty uc = this.getMap().newUserAccessors(key, propertyFlags);
        uc.setAccessors(this, this.getMap(), new UserAccessorProperty.Accessors(getter, setter));
        return uc;
    }
    
    protected boolean useDualFields() {
        return !StructureLoader.isSingleFieldStructure(this.getClass().getName());
    }
    
    Object ensureSpillSize(final int slot) {
        final int oldLength = (this.objectSpill == null) ? 0 : this.objectSpill.length;
        if (slot < oldLength) {
            return this;
        }
        final int newLength = alignUp(slot + 1, 8);
        final Object[] newObjectSpill = new Object[newLength];
        final long[] newPrimitiveSpill = (long[])(this.useDualFields() ? new long[newLength] : null);
        if (this.objectSpill != null) {
            System.arraycopy(this.objectSpill, 0, newObjectSpill, 0, oldLength);
            if (this.primitiveSpill != null && newPrimitiveSpill != null) {
                System.arraycopy(this.primitiveSpill, 0, newPrimitiveSpill, 0, oldLength);
            }
        }
        this.primitiveSpill = newPrimitiveSpill;
        this.objectSpill = newObjectSpill;
        return this;
    }
    
    private static MethodHandle findOwnMH_V(final Class<? extends ScriptObject> clazz, final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), ScriptObject.class, name, Lookup.MH.type(rtype, types));
    }
    
    private static MethodHandle findOwnMH_V(final String name, final Class<?> rtype, final Class<?>... types) {
        return findOwnMH_V(ScriptObject.class, name, rtype, types);
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptObject.class, name, Lookup.MH.type(rtype, types));
    }
    
    private static MethodHandle getKnownFunctionPropertyGuardSelf(final PropertyMap map, final MethodHandle getter, final ScriptFunction func) {
        return Lookup.MH.insertArguments(ScriptObject.KNOWNFUNCPROPGUARDSELF, 1, map, getter, func);
    }
    
    private static boolean knownFunctionPropertyGuardSelf(final Object self, final PropertyMap map, final MethodHandle getter, final ScriptFunction func) {
        if (self instanceof ScriptObject && ((ScriptObject)self).getMap() == map) {
            try {
                return getter.invokeExact(self) == func;
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
        return false;
    }
    
    private static MethodHandle getKnownFunctionPropertyGuardProto(final PropertyMap map, final MethodHandle getter, final int depth, final ScriptFunction func) {
        return Lookup.MH.insertArguments(ScriptObject.KNOWNFUNCPROPGUARDPROTO, 1, map, getter, depth, func);
    }
    
    private static ScriptObject getProto(final ScriptObject self, final int depth) {
        ScriptObject proto = self;
        for (int d = 0; d < depth; ++d) {
            proto = proto.getProto();
            if (proto == null) {
                return null;
            }
        }
        return proto;
    }
    
    private static boolean knownFunctionPropertyGuardProto(final Object self, final PropertyMap map, final MethodHandle getter, final int depth, final ScriptFunction func) {
        if (self instanceof ScriptObject && ((ScriptObject)self).getMap() == map) {
            final ScriptObject proto = getProto((ScriptObject)self, depth);
            if (proto == null) {
                return false;
            }
            try {
                return getter.invokeExact((Object)proto) == func;
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
        return false;
    }
    
    public static long getCount() {
        return ScriptObject.count.longValue();
    }
    
    static {
        GETPROTO = findOwnMH_V("getProto", ScriptObject.class, (Class<?>[])new Class[0]);
        MEGAMORPHIC_GET = findOwnMH_V("megamorphicGet", Object.class, String.class, Boolean.TYPE, Boolean.TYPE);
        GLOBALFILTER = findOwnMH_S("globalFilter", Object.class, Object.class);
        DECLARE_AND_SET = findOwnMH_V("declareAndSet", Void.TYPE, String.class, Object.class);
        TRUNCATINGFILTER = findOwnMH_S("truncatingFilter", Object[].class, Integer.TYPE, Object[].class);
        KNOWNFUNCPROPGUARDSELF = findOwnMH_S("knownFunctionPropertyGuardSelf", Boolean.TYPE, Object.class, PropertyMap.class, MethodHandle.class, ScriptFunction.class);
        KNOWNFUNCPROPGUARDPROTO = findOwnMH_S("knownFunctionPropertyGuardProto", Boolean.TYPE, Object.class, PropertyMap.class, MethodHandle.class, Integer.TYPE, ScriptFunction.class);
        PROTO_FILTERS = new ArrayList<MethodHandle>();
        GET_ARRAY = CompilerConstants.virtualCall(MethodHandles.lookup(), ScriptObject.class, "getArray", ArrayData.class, (Class<?>[])new Class[0]);
        GET_ARGUMENT = CompilerConstants.virtualCall(MethodHandles.lookup(), ScriptObject.class, "getArgument", Object.class, Integer.TYPE);
        SET_ARGUMENT = CompilerConstants.virtualCall(MethodHandles.lookup(), ScriptObject.class, "setArgument", Void.TYPE, Integer.TYPE, Object.class);
        GET_PROTO = CompilerConstants.virtualCallNoLookup(ScriptObject.class, "getProto", ScriptObject.class, (Class<?>[])new Class[0]);
        GET_PROTO_DEPTH = CompilerConstants.virtualCallNoLookup(ScriptObject.class, "getProto", ScriptObject.class, Integer.TYPE);
        SET_GLOBAL_OBJECT_PROTO = CompilerConstants.staticCallNoLookup(ScriptObject.class, "setGlobalObjectProto", Void.TYPE, ScriptObject.class);
        SET_PROTO_FROM_LITERAL = CompilerConstants.virtualCallNoLookup(ScriptObject.class, "setProtoFromLiteral", Void.TYPE, Object.class);
        SET_USER_ACCESSORS = CompilerConstants.virtualCall(MethodHandles.lookup(), ScriptObject.class, "setUserAccessors", Void.TYPE, String.class, ScriptFunction.class, ScriptFunction.class);
        SET_SLOW = new MethodHandle[] { findOwnMH_V("set", Void.TYPE, Object.class, Integer.TYPE, Integer.TYPE), findOwnMH_V("set", Void.TYPE, Object.class, Double.TYPE, Integer.TYPE), findOwnMH_V("set", Void.TYPE, Object.class, Object.class, Integer.TYPE) };
        SET_MAP = CompilerConstants.virtualCallNoLookup(ScriptObject.class, "setMap", Void.TYPE, PropertyMap.class);
        CAS_MAP = findOwnMH_V("compareAndSetMap", Boolean.TYPE, PropertyMap.class, PropertyMap.class);
        EXTENSION_CHECK = findOwnMH_V("extensionCheck", Boolean.TYPE, Boolean.TYPE, String.class);
        ENSURE_SPILL_SIZE = findOwnMH_V("ensureSpillSize", Object.class, Integer.TYPE);
        if (Context.DEBUG) {
            ScriptObject.count = new LongAdder();
        }
    }
    
    private abstract static class ScriptObjectIterator<T> implements Iterator<T>
    {
        protected T[] values;
        protected final ScriptObject object;
        private int index;
        
        ScriptObjectIterator(final ScriptObject object) {
            this.object = object;
        }
        
        protected abstract void init();
        
        @Override
        public boolean hasNext() {
            if (this.values == null) {
                this.init();
            }
            return this.index < this.values.length;
        }
        
        @Override
        public T next() {
            if (this.values == null) {
                this.init();
            }
            return this.values[this.index++];
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
    
    private static class KeyIterator extends ScriptObjectIterator<String>
    {
        KeyIterator(final ScriptObject object) {
            super(object);
        }
        
        @Override
        protected void init() {
            final Set<String> keys = new LinkedHashSet<String>();
            final Set<String> nonEnumerable = new HashSet<String>();
            for (ScriptObject self = this.object; self != null; self = self.getProto()) {
                keys.addAll(Arrays.asList(self.getOwnKeys(false, nonEnumerable)));
            }
            this.values = keys.toArray((T[])new String[keys.size()]);
        }
    }
    
    private static class ValueIterator extends ScriptObjectIterator<Object>
    {
        ValueIterator(final ScriptObject object) {
            super(object);
        }
        
        @Override
        protected void init() {
            final ArrayList<Object> valueList = new ArrayList<Object>();
            final Set<String> nonEnumerable = new HashSet<String>();
            for (ScriptObject self = this.object; self != null; self = self.getProto()) {
                for (final String key : self.getOwnKeys(false, nonEnumerable)) {
                    valueList.add(self.get(key));
                }
            }
            this.values = valueList.toArray(new Object[valueList.size()]);
        }
    }
}
