// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodType;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.util.concurrent.Callable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public final class UserAccessorProperty extends SpillProperty
{
    private static final long serialVersionUID = -5928687246526840321L;
    private static final MethodHandles.Lookup LOOKUP;
    private static final MethodHandle INVOKE_OBJECT_GETTER;
    private static final MethodHandle INVOKE_INT_GETTER;
    private static final MethodHandle INVOKE_NUMBER_GETTER;
    private static final MethodHandle INVOKE_OBJECT_SETTER;
    private static final MethodHandle INVOKE_INT_SETTER;
    private static final MethodHandle INVOKE_NUMBER_SETTER;
    private static final Object OBJECT_GETTER_INVOKER_KEY;
    private static final Object OBJECT_SETTER_INVOKER_KEY;
    
    private static MethodHandle getObjectGetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(UserAccessorProperty.OBJECT_GETTER_INVOKER_KEY, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_GETTER(Object.class, -1);
            }
        });
    }
    
    static MethodHandle getINVOKE_UA_GETTER(final Class<?> returnType, final int programPoint) {
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            final int flags = 0x8 | programPoint << 11;
            return Bootstrap.createDynamicInvoker("dyn:call", flags, returnType, Object.class, Object.class);
        }
        return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class);
    }
    
    private static MethodHandle getObjectSetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(UserAccessorProperty.OBJECT_SETTER_INVOKER_KEY, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_SETTER(Object.class);
            }
        });
    }
    
    static MethodHandle getINVOKE_UA_SETTER(final Class<?> valueType) {
        return Bootstrap.createDynamicInvoker("dyn:call", Void.TYPE, Object.class, Object.class, valueType);
    }
    
    UserAccessorProperty(final String key, final int flags, final int slot) {
        super(key, flags, slot);
    }
    
    private UserAccessorProperty(final UserAccessorProperty property) {
        super(property);
    }
    
    private UserAccessorProperty(final UserAccessorProperty property, final Class<?> newType) {
        super(property, newType);
    }
    
    @Override
    public Property copy() {
        return new UserAccessorProperty(this);
    }
    
    @Override
    public Property copy(final Class<?> newType) {
        return new UserAccessorProperty(this, newType);
    }
    
    void setAccessors(final ScriptObject sobj, final PropertyMap map, final Accessors gs) {
        try {
            super.getSetter(Object.class, map).invokeExact((Object)sobj, (Object)gs);
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
    
    Accessors getAccessors(final ScriptObject sobj) {
        try {
            final Object gs = super.getGetter(Object.class).invokeExact((Object)sobj);
            return (Accessors)gs;
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
    
    @Override
    protected Class<?> getLocalType() {
        return Object.class;
    }
    
    @Override
    public boolean hasGetterFunction(final ScriptObject sobj) {
        return this.getAccessors(sobj).getter != null;
    }
    
    @Override
    public boolean hasSetterFunction(final ScriptObject sobj) {
        return this.getAccessors(sobj).setter != null;
    }
    
    @Override
    public int getIntValue(final ScriptObject self, final ScriptObject owner) {
        return (int)this.getObjectValue(self, owner);
    }
    
    @Override
    public double getDoubleValue(final ScriptObject self, final ScriptObject owner) {
        return (double)this.getObjectValue(self, owner);
    }
    
    @Override
    public Object getObjectValue(final ScriptObject self, final ScriptObject owner) {
        try {
            return invokeObjectGetter(this.getAccessors((owner != null) ? owner : self), getObjectGetterInvoker(), self);
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
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final int value, final boolean strict) {
        this.setValue(self, owner, (Object)value, strict);
    }
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final double value, final boolean strict) {
        this.setValue(self, owner, (Object)value, strict);
    }
    
    @Override
    public void setValue(final ScriptObject self, final ScriptObject owner, final Object value, final boolean strict) {
        try {
            invokeObjectSetter(this.getAccessors((owner != null) ? owner : self), getObjectSetterInvoker(), strict ? this.getKey() : null, self, value);
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
    
    @Override
    public MethodHandle getGetter(final Class<?> type) {
        return Lookup.filterReturnType(UserAccessorProperty.INVOKE_OBJECT_GETTER, type);
    }
    
    @Override
    public MethodHandle getOptimisticGetter(final Class<?> type, final int programPoint) {
        if (type == Integer.TYPE) {
            return UserAccessorProperty.INVOKE_INT_GETTER;
        }
        if (type == Double.TYPE) {
            return UserAccessorProperty.INVOKE_NUMBER_GETTER;
        }
        assert type == Object.class;
        return UserAccessorProperty.INVOKE_OBJECT_GETTER;
    }
    
    @Override
    void initMethodHandles(final Class<?> structure) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ScriptFunction getGetterFunction(final ScriptObject sobj) {
        final Object value = this.getAccessors(sobj).getter;
        return (value instanceof ScriptFunction) ? ((ScriptFunction)value) : null;
    }
    
    @Override
    public MethodHandle getSetter(final Class<?> type, final PropertyMap currentMap) {
        if (type == Integer.TYPE) {
            return UserAccessorProperty.INVOKE_INT_SETTER;
        }
        if (type == Double.TYPE) {
            return UserAccessorProperty.INVOKE_NUMBER_SETTER;
        }
        assert type == Object.class;
        return UserAccessorProperty.INVOKE_OBJECT_SETTER;
    }
    
    @Override
    public ScriptFunction getSetterFunction(final ScriptObject sobj) {
        final Object value = this.getAccessors(sobj).setter;
        return (value instanceof ScriptFunction) ? ((ScriptFunction)value) : null;
    }
    
    MethodHandle getAccessorsGetter() {
        return super.getGetter(Object.class).asType(MethodType.methodType(Accessors.class, Object.class));
    }
    
    private static Object invokeObjectGetter(final Accessors gs, final MethodHandle invoker, final Object self) throws Throwable {
        final Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    private static int invokeIntGetter(final Accessors gs, final MethodHandle invoker, final int programPoint, final Object self) throws Throwable {
        final Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }
    
    private static double invokeNumberGetter(final Accessors gs, final MethodHandle invoker, final int programPoint, final Object self) throws Throwable {
        final Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }
    
    private static void invokeObjectSetter(final Accessors gs, final MethodHandle invoker, final String name, final Object self, final Object value) throws Throwable {
        final Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        }
        else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }
    
    private static void invokeIntSetter(final Accessors gs, final MethodHandle invoker, final String name, final Object self, final int value) throws Throwable {
        final Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        }
        else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }
    
    private static void invokeNumberSetter(final Accessors gs, final MethodHandle invoker, final String name, final Object self, final double value) throws Throwable {
        final Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        }
        else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(UserAccessorProperty.LOOKUP, UserAccessorProperty.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        INVOKE_OBJECT_GETTER = findOwnMH_S("invokeObjectGetter", Object.class, Accessors.class, MethodHandle.class, Object.class);
        INVOKE_INT_GETTER = findOwnMH_S("invokeIntGetter", Integer.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
        INVOKE_NUMBER_GETTER = findOwnMH_S("invokeNumberGetter", Double.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
        INVOKE_OBJECT_SETTER = findOwnMH_S("invokeObjectSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Object.class);
        INVOKE_INT_SETTER = findOwnMH_S("invokeIntSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Integer.TYPE);
        INVOKE_NUMBER_SETTER = findOwnMH_S("invokeNumberSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Double.TYPE);
        OBJECT_GETTER_INVOKER_KEY = new Object();
        OBJECT_SETTER_INVOKER_KEY = new Object();
    }
    
    static final class Accessors
    {
        Object getter;
        Object setter;
        
        Accessors(final Object getter, final Object setter) {
            this.set(getter, setter);
        }
        
        final void set(final Object getter, final Object setter) {
            this.getter = getter;
            this.setter = setter;
        }
        
        @Override
        public String toString() {
            return "[getter=" + this.getter + " setter=" + this.setter + ']';
        }
    }
}
