// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import java.util.function.Supplier;
import java.util.logging.Level;
import jdk.nashorn.internal.objects.Global;
import java.lang.invoke.SwitchPoint;
import java.io.IOException;
import java.io.ObjectInputStream;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class AccessorProperty extends Property
{
    private static final MethodHandles.Lookup LOOKUP;
    private static final MethodHandle REPLACE_MAP;
    private static final MethodHandle INVALIDATE_SP;
    private static final int NOOF_TYPES;
    private static final long serialVersionUID = 3371720170182154920L;
    private static ClassValue<Accessors> GETTERS_SETTERS;
    private transient MethodHandle[] GETTER_CACHE;
    transient MethodHandle primitiveGetter;
    transient MethodHandle primitiveSetter;
    transient MethodHandle objectGetter;
    transient MethodHandle objectSetter;
    
    public static AccessorProperty create(final String key, final int propertyFlags, final MethodHandle getter, final MethodHandle setter) {
        return new AccessorProperty(key, propertyFlags, -1, getter, setter);
    }
    
    AccessorProperty(final AccessorProperty property, final Object delegate) {
        super(property, property.getFlags() | 0x100);
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        this.primitiveGetter = bindTo(property.primitiveGetter, delegate);
        this.primitiveSetter = bindTo(property.primitiveSetter, delegate);
        this.objectGetter = bindTo(property.objectGetter, delegate);
        this.objectSetter = bindTo(property.objectSetter, delegate);
        property.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        this.setType(property.getType());
    }
    
    protected AccessorProperty(final String key, final int flags, final int slot, final MethodHandle primitiveGetter, final MethodHandle primitiveSetter, final MethodHandle objectGetter, final MethodHandle objectSetter) {
        super(key, flags, slot);
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        assert this.getClass() != AccessorProperty.class;
        this.primitiveGetter = primitiveGetter;
        this.primitiveSetter = primitiveSetter;
        this.objectGetter = objectGetter;
        this.objectSetter = objectSetter;
        this.initializeType();
    }
    
    private AccessorProperty(final String key, final int flags, final int slot, final MethodHandle getter, final MethodHandle setter) {
        super(key, flags | 0x80 | 0x800 | (getter.type().returnType().isPrimitive() ? 64 : 0), slot);
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        assert !this.isSpill();
        final Class<?> getterType = getter.type().returnType();
        final Class<?> setterType = (setter == null) ? null : setter.type().parameterType(1);
        assert setterType == getterType;
        if (getterType == Integer.TYPE) {
            this.primitiveGetter = Lookup.MH.asType(getter, Lookup.GET_PRIMITIVE_TYPE);
            this.primitiveSetter = ((setter == null) ? null : Lookup.MH.asType(setter, Lookup.SET_PRIMITIVE_TYPE));
        }
        else if (getterType == Double.TYPE) {
            this.primitiveGetter = Lookup.MH.asType(Lookup.MH.filterReturnValue(getter, ObjectClassGenerator.PACK_DOUBLE), Lookup.GET_PRIMITIVE_TYPE);
            this.primitiveSetter = ((setter == null) ? null : Lookup.MH.asType(Lookup.MH.filterArguments(setter, 1, ObjectClassGenerator.UNPACK_DOUBLE), Lookup.SET_PRIMITIVE_TYPE));
        }
        else {
            final MethodHandle methodHandle = null;
            this.primitiveSetter = methodHandle;
            this.primitiveGetter = methodHandle;
        }
        assert this.primitiveGetter.type() == Lookup.GET_PRIMITIVE_TYPE : this.primitiveGetter + "!=" + Lookup.GET_PRIMITIVE_TYPE;
        assert this.primitiveSetter.type() == Lookup.SET_PRIMITIVE_TYPE : this.primitiveSetter;
        this.objectGetter = ((getter.type() != Lookup.GET_OBJECT_TYPE) ? Lookup.MH.asType(getter, Lookup.GET_OBJECT_TYPE) : getter);
        this.objectSetter = ((setter != null && setter.type() != Lookup.SET_OBJECT_TYPE) ? Lookup.MH.asType(setter, Lookup.SET_OBJECT_TYPE) : setter);
        this.setType(getterType);
    }
    
    public AccessorProperty(final String key, final int flags, final Class<?> structure, final int slot) {
        super(key, flags, slot);
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        this.initGetterSetter(structure);
        this.initializeType();
    }
    
    private void initGetterSetter(final Class<?> structure) {
        final int slot = this.getSlot();
        if (this.isParameter() && this.hasArguments()) {
            final MethodHandle arguments = Lookup.MH.getter(AccessorProperty.LOOKUP, structure, "arguments", ScriptObject.class);
            this.objectGetter = Lookup.MH.asType(Lookup.MH.insertArguments(Lookup.MH.filterArguments(ScriptObject.GET_ARGUMENT.methodHandle(), 0, arguments), 1, slot), Lookup.GET_OBJECT_TYPE);
            this.objectSetter = Lookup.MH.asType(Lookup.MH.insertArguments(Lookup.MH.filterArguments(ScriptObject.SET_ARGUMENT.methodHandle(), 0, arguments), 1, slot), Lookup.SET_OBJECT_TYPE);
            this.primitiveGetter = null;
            this.primitiveSetter = null;
        }
        else {
            final Accessors gs = AccessorProperty.GETTERS_SETTERS.get(structure);
            this.objectGetter = gs.objectGetters[slot];
            this.primitiveGetter = gs.primitiveGetters[slot];
            this.objectSetter = gs.objectSetters[slot];
            this.primitiveSetter = gs.primitiveSetters[slot];
        }
        assert this.hasDualFields() != StructureLoader.isSingleFieldStructure(structure.getName());
    }
    
    protected AccessorProperty(final String key, final int flags, final int slot, final ScriptObject owner, final Object initialValue) {
        this(key, flags, owner.getClass(), slot);
        this.setInitialValue(owner, initialValue);
    }
    
    public AccessorProperty(final String key, final int flags, final Class<?> structure, final int slot, final Class<?> initialType) {
        this(key, flags, structure, slot);
        this.setType(this.hasDualFields() ? initialType : Object.class);
    }
    
    protected AccessorProperty(final AccessorProperty property, final Class<?> newType) {
        super(property, property.getFlags());
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
        this.GETTER_CACHE = ((newType != property.getLocalType()) ? new MethodHandle[AccessorProperty.NOOF_TYPES] : property.GETTER_CACHE);
        this.primitiveGetter = property.primitiveGetter;
        this.primitiveSetter = property.primitiveSetter;
        this.objectGetter = property.objectGetter;
        this.objectSetter = property.objectSetter;
        this.setType(newType);
    }
    
    protected AccessorProperty(final AccessorProperty property) {
        this(property, property.getLocalType());
    }
    
    protected final void setInitialValue(final ScriptObject owner, final Object initialValue) {
        this.setType(this.hasDualFields() ? JSType.unboxedFieldType(initialValue) : Object.class);
        if (initialValue instanceof Integer) {
            this.invokeSetter(owner, (int)initialValue);
        }
        else if (initialValue instanceof Double) {
            this.invokeSetter(owner, (double)initialValue);
        }
        else {
            this.invokeSetter(owner, initialValue);
        }
    }
    
    protected final void initializeType() {
        this.setType(this.hasDualFields() ? null : Object.class);
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.GETTER_CACHE = new MethodHandle[AccessorProperty.NOOF_TYPES];
    }
    
    private static MethodHandle bindTo(final MethodHandle mh, final Object receiver) {
        if (mh == null) {
            return null;
        }
        return Lookup.MH.dropArguments(Lookup.MH.bindTo(mh, receiver), 0, Object.class);
    }
    
    @Override
    public Property copy() {
        return new AccessorProperty(this);
    }
    
    @Override
    public Property copy(final Class<?> newType) {
        return new AccessorProperty(this, newType);
    }
    
    @Override
    public int getIntValue(final ScriptObject self, final ScriptObject owner) {
        try {
            return this.getGetter(Integer.TYPE).invokeExact((Object)self);
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
    
    @Override
    public double getDoubleValue(final ScriptObject self, final ScriptObject owner) {
        try {
            return this.getGetter(Double.TYPE).invokeExact((Object)self);
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
    
    @Override
    public Object getObjectValue(final ScriptObject self, final ScriptObject owner) {
        try {
            return this.getGetter(Object.class).invokeExact((Object)self);
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
    
    protected final void invokeSetter(final ScriptObject self, final int value) {
        try {
            this.getSetter(Integer.TYPE, self.getMap()).invokeExact((Object)self, value);
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
    
    protected final void invokeSetter(final ScriptObject self, final double value) {
        try {
            this.getSetter(Double.TYPE, self.getMap()).invokeExact((Object)self, value);
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
    
    protected final void invokeSetter(final ScriptObject self, final Object value) {
        try {
            this.getSetter(Object.class, self.getMap()).invokeExact((Object)self, value);
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
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final int value, final boolean strict) {
        assert this.isConfigurable() || this.isWritable() : this.getKey() + " is not writable or configurable";
        this.invokeSetter(self, value);
    }
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final double value, final boolean strict) {
        assert this.isConfigurable() || this.isWritable() : this.getKey() + " is not writable or configurable";
        this.invokeSetter(self, value);
    }
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final Object value, final boolean strict) {
        this.invokeSetter(self, value);
    }
    
    @Override
    void initMethodHandles(final Class<?> structure) {
        if (!ScriptObject.class.isAssignableFrom(structure) || !StructureLoader.isStructureClass(structure.getName())) {
            throw new IllegalArgumentException();
        }
        assert !this.isSpill();
        this.initGetterSetter(structure);
    }
    
    @Override
    public MethodHandle getGetter(final Class<?> type) {
        final int i = JSType.getAccessorTypeIndex(type);
        assert type == Object.class : "invalid getter type " + type + " for " + this.getKey();
        this.checkUndeclared();
        final MethodHandle[] getterCache = this.GETTER_CACHE;
        final MethodHandle cachedGetter = getterCache[i];
        MethodHandle getter;
        if (cachedGetter != null) {
            getter = cachedGetter;
        }
        else {
            getter = this.debug(ObjectClassGenerator.createGetter(this.getLocalType(), type, this.primitiveGetter, this.objectGetter, -1), this.getLocalType(), type, "get");
            getterCache[i] = getter;
        }
        assert getter.type().returnType() == type && getter.type().parameterType(0) == Object.class;
        return getter;
    }
    
    @Override
    public MethodHandle getOptimisticGetter(final Class<?> type, final int programPoint) {
        if (this.objectGetter == null) {
            return this.getOptimisticPrimitiveGetter(type, programPoint);
        }
        this.checkUndeclared();
        return this.debug(ObjectClassGenerator.createGetter(this.getLocalType(), type, this.primitiveGetter, this.objectGetter, programPoint), this.getLocalType(), type, "get");
    }
    
    private MethodHandle getOptimisticPrimitiveGetter(final Class<?> type, final int programPoint) {
        final MethodHandle g = this.getGetter(this.getLocalType());
        return Lookup.MH.asType(OptimisticReturnFilters.filterOptimisticReturnValue(g, type, programPoint), g.type().changeReturnType(type));
    }
    
    private Property getWiderProperty(final Class<?> type) {
        return this.copy(type);
    }
    
    private PropertyMap getWiderMap(final PropertyMap oldMap, final Property newProperty) {
        final PropertyMap newMap = oldMap.replaceProperty(this, newProperty);
        assert oldMap.size() > 0;
        assert newMap.size() == oldMap.size();
        return newMap;
    }
    
    private void checkUndeclared() {
        if ((this.getFlags() & 0x200) != 0x0) {
            throw ECMAErrors.referenceError("not.defined", this.getKey());
        }
    }
    
    private static Object replaceMap(final Object sobj, final PropertyMap newMap) {
        ((ScriptObject)sobj).setMap(newMap);
        return sobj;
    }
    
    private static Object invalidateSwitchPoint(final AccessorProperty property, final Object obj) {
        if (!property.builtinSwitchPoint.hasBeenInvalidated()) {
            SwitchPoint.invalidateAll(new SwitchPoint[] { property.builtinSwitchPoint });
        }
        return obj;
    }
    
    private MethodHandle generateSetter(final Class<?> forType, final Class<?> type) {
        return this.debug(ObjectClassGenerator.createSetter(forType, type, this.primitiveSetter, this.objectSetter), this.getLocalType(), type, "set");
    }
    
    protected final boolean isUndefined() {
        return this.getLocalType() == null;
    }
    
    @Override
    public MethodHandle getSetter(final Class<?> type, final PropertyMap currentMap) {
        this.checkUndeclared();
        final int typeIndex = JSType.getAccessorTypeIndex(type);
        final int currentTypeIndex = JSType.getAccessorTypeIndex(this.getLocalType());
        MethodHandle mh;
        if (this.needsInvalidator(typeIndex, currentTypeIndex)) {
            final Property newProperty = this.getWiderProperty(type);
            final PropertyMap newMap = this.getWiderMap(currentMap, newProperty);
            final MethodHandle widerSetter = newProperty.getSetter(type, newMap);
            final Class<?> ct = this.getLocalType();
            mh = Lookup.MH.filterArguments(widerSetter, 0, Lookup.MH.insertArguments(this.debugReplace(ct, type, currentMap, newMap), 1, newMap));
            if (ct != null && ct.isPrimitive() && !type.isPrimitive()) {
                mh = ObjectClassGenerator.createGuardBoxedPrimitiveSetter(ct, this.generateSetter(ct, ct), mh);
            }
        }
        else {
            final Class<?> forType = this.isUndefined() ? type : this.getLocalType();
            mh = this.generateSetter(forType.isPrimitive() ? forType : Object.class, type);
        }
        if (this.isBuiltin()) {
            mh = Lookup.MH.filterArguments(mh, 0, debugInvalidate(Lookup.MH.insertArguments(AccessorProperty.INVALIDATE_SP, 0, this), this.getKey()));
        }
        assert mh.type().returnType() == Void.TYPE : mh.type();
        return mh;
    }
    
    @Override
    public final boolean canChangeType() {
        return this.hasDualFields() && (this.getLocalType() == null || (this.getLocalType() != Object.class && (this.isConfigurable() || this.isWritable())));
    }
    
    private boolean needsInvalidator(final int typeIndex, final int currentTypeIndex) {
        return this.canChangeType() && typeIndex > currentTypeIndex;
    }
    
    private MethodHandle debug(final MethodHandle mh, final Class<?> forType, final Class<?> type, final String tag) {
        if (!Context.DEBUG || !Global.hasInstance()) {
            return mh;
        }
        final Context context = Context.getContextTrusted();
        assert context != null;
        return context.addLoggingToHandle(ObjectClassGenerator.class, Level.INFO, mh, 0, true, new Supplier<String>() {
            @Override
            public String get() {
                return tag + " '" + AccessorProperty.this.getKey() + "' (property=" + Debug.id(this) + ", slot=" + AccessorProperty.this.getSlot() + " " + this.getClass().getSimpleName() + " forType=" + MethodHandleFactory.stripName(forType) + ", type=" + MethodHandleFactory.stripName(type) + ')';
            }
        });
    }
    
    private MethodHandle debugReplace(final Class<?> oldType, final Class<?> newType, final PropertyMap oldMap, final PropertyMap newMap) {
        if (!Context.DEBUG || !Global.hasInstance()) {
            return AccessorProperty.REPLACE_MAP;
        }
        final Context context = Context.getContextTrusted();
        assert context != null;
        MethodHandle mh = context.addLoggingToHandle(ObjectClassGenerator.class, AccessorProperty.REPLACE_MAP, new Supplier<String>() {
            @Override
            public String get() {
                return "Type change for '" + AccessorProperty.this.getKey() + "' " + oldType + "=>" + newType;
            }
        });
        mh = context.addLoggingToHandle(ObjectClassGenerator.class, Level.FINEST, mh, Integer.MAX_VALUE, false, new Supplier<String>() {
            @Override
            public String get() {
                return "Setting map " + Debug.id(oldMap) + " => " + Debug.id(newMap) + " " + oldMap + " => " + newMap;
            }
        });
        return mh;
    }
    
    private static MethodHandle debugInvalidate(final MethodHandle invalidator, final String key) {
        if (!Context.DEBUG || !Global.hasInstance()) {
            return invalidator;
        }
        final Context context = Context.getContextTrusted();
        assert context != null;
        return context.addLoggingToHandle(ObjectClassGenerator.class, invalidator, new Supplier<String>() {
            @Override
            public String get() {
                return "Field change callback for " + key + " triggered ";
            }
        });
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(AccessorProperty.LOOKUP, AccessorProperty.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        REPLACE_MAP = findOwnMH_S("replaceMap", Object.class, Object.class, PropertyMap.class);
        INVALIDATE_SP = findOwnMH_S("invalidateSwitchPoint", Object.class, AccessorProperty.class, Object.class);
        NOOF_TYPES = JSType.getNumberOfAccessorTypes();
        AccessorProperty.GETTERS_SETTERS = new ClassValue<Accessors>() {
            @Override
            protected Accessors computeValue(final Class<?> structure) {
                return new Accessors(structure);
            }
        };
    }
    
    private static class Accessors
    {
        final MethodHandle[] objectGetters;
        final MethodHandle[] objectSetters;
        final MethodHandle[] primitiveGetters;
        final MethodHandle[] primitiveSetters;
        
        Accessors(final Class<?> structure) {
            final int fieldCount = ObjectClassGenerator.getFieldCount(structure);
            this.objectGetters = new MethodHandle[fieldCount];
            this.objectSetters = new MethodHandle[fieldCount];
            this.primitiveGetters = new MethodHandle[fieldCount];
            this.primitiveSetters = new MethodHandle[fieldCount];
            for (int i = 0; i < fieldCount; ++i) {
                final String fieldName = ObjectClassGenerator.getFieldName(i, Type.OBJECT);
                final Class<?> typeClass = Type.OBJECT.getTypeClass();
                this.objectGetters[i] = Lookup.MH.asType(Lookup.MH.getter(AccessorProperty.LOOKUP, structure, fieldName, typeClass), Lookup.GET_OBJECT_TYPE);
                this.objectSetters[i] = Lookup.MH.asType(Lookup.MH.setter(AccessorProperty.LOOKUP, structure, fieldName, typeClass), Lookup.SET_OBJECT_TYPE);
            }
            if (!StructureLoader.isSingleFieldStructure(structure.getName())) {
                for (int i = 0; i < fieldCount; ++i) {
                    final String fieldNamePrimitive = ObjectClassGenerator.getFieldName(i, ObjectClassGenerator.PRIMITIVE_FIELD_TYPE);
                    final Class<?> typeClass = ObjectClassGenerator.PRIMITIVE_FIELD_TYPE.getTypeClass();
                    this.primitiveGetters[i] = Lookup.MH.asType(Lookup.MH.getter(AccessorProperty.LOOKUP, structure, fieldNamePrimitive, typeClass), Lookup.GET_PRIMITIVE_TYPE);
                    this.primitiveSetters[i] = Lookup.MH.asType(Lookup.MH.setter(AccessorProperty.LOOKUP, structure, fieldNamePrimitive, typeClass), Lookup.SET_PRIMITIVE_TYPE);
                }
            }
        }
    }
}
