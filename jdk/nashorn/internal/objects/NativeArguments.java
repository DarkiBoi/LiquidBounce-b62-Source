// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.Collection;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import java.util.ArrayList;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptFunction;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import java.util.BitSet;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeArguments extends ScriptObject
{
    private static final MethodHandle G$LENGTH;
    private static final MethodHandle S$LENGTH;
    private static final MethodHandle G$CALLEE;
    private static final MethodHandle S$CALLEE;
    private static final PropertyMap map$;
    private Object length;
    private Object callee;
    private final int numMapped;
    private final int numParams;
    private ArrayData unmappedArgs;
    private BitSet deleted;
    
    static PropertyMap getInitialMap() {
        return NativeArguments.map$;
    }
    
    NativeArguments(final Object[] arguments, final Object callee, final int numParams, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.setIsArguments();
        this.setArray(ArrayData.allocate(arguments));
        this.length = arguments.length;
        this.callee = callee;
        this.numMapped = Math.min(numParams, arguments.length);
        this.numParams = numParams;
    }
    
    @Override
    public String getClassName() {
        return "Arguments";
    }
    
    @Override
    public Object getArgument(final int key) {
        assert key >= 0 && key < this.numParams : "invalid argument index";
        return this.isMapped(key) ? this.getArray().getObject(key) : this.getUnmappedArg(key);
    }
    
    @Override
    public void setArgument(final int key, final Object value) {
        assert key >= 0 && key < this.numParams : "invalid argument index";
        if (this.isMapped(key)) {
            this.setArray(this.getArray().set(key, value, false));
        }
        else {
            this.setUnmappedArg(key, value);
        }
    }
    
    @Override
    public boolean delete(final int key, final boolean strict) {
        final int index = ArrayIndex.getArrayIndex(key);
        return this.isMapped(index) ? this.deleteMapped(index, strict) : super.delete(key, strict);
    }
    
    @Override
    public boolean delete(final double key, final boolean strict) {
        final int index = ArrayIndex.getArrayIndex(key);
        return this.isMapped(index) ? this.deleteMapped(index, strict) : super.delete(key, strict);
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return this.isMapped(index) ? this.deleteMapped(index, strict) : super.delete(primitiveKey, strict);
    }
    
    @Override
    public boolean defineOwnProperty(final String key, final Object propertyDesc, final boolean reject) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (index < 0) {
            return super.defineOwnProperty(key, propertyDesc, reject);
        }
        final boolean isMapped = this.isMapped(index);
        final Object oldValue = isMapped ? this.getArray().getObject(index) : null;
        if (super.defineOwnProperty(key, propertyDesc, false)) {
            if (isMapped) {
                final PropertyDescriptor desc = ScriptObject.toPropertyDescriptor(Global.instance(), propertyDesc);
                if (desc.type() == 2) {
                    this.setDeleted(index, oldValue);
                }
                else if (desc.has("writable") && !desc.isWritable()) {
                    this.setDeleted(index, desc.has("value") ? desc.getValue() : oldValue);
                }
                else if (desc.has("value")) {
                    this.setArray(this.getArray().set(index, desc.getValue(), false));
                }
            }
            return true;
        }
        if (reject) {
            throw ECMAErrors.typeError("cant.redefine.property", key, ScriptRuntime.safeToString(this));
        }
        return false;
    }
    
    private boolean isDeleted(final int index) {
        return this.deleted != null && this.deleted.get(index);
    }
    
    private void setDeleted(final int index, final Object unmappedValue) {
        if (this.deleted == null) {
            this.deleted = new BitSet(this.numMapped);
        }
        this.deleted.set(index, true);
        this.setUnmappedArg(index, unmappedValue);
    }
    
    private boolean deleteMapped(final int index, final boolean strict) {
        final Object value = this.getArray().getObject(index);
        final boolean success = super.delete(index, strict);
        if (success) {
            this.setDeleted(index, value);
        }
        return success;
    }
    
    private Object getUnmappedArg(final int key) {
        assert key >= 0 && key < this.numParams;
        return (this.unmappedArgs == null) ? ScriptRuntime.UNDEFINED : this.unmappedArgs.getObject(key);
    }
    
    private void setUnmappedArg(final int key, final Object value) {
        assert key >= 0 && key < this.numParams;
        if (this.unmappedArgs == null) {
            final Object[] newValues = new Object[this.numParams];
            System.arraycopy(this.getArray().asObjectArray(), 0, newValues, 0, this.numMapped);
            if (this.numMapped < this.numParams) {
                Arrays.fill(newValues, this.numMapped, this.numParams, ScriptRuntime.UNDEFINED);
            }
            this.unmappedArgs = ArrayData.allocate(newValues);
        }
        this.unmappedArgs = this.unmappedArgs.set(key, value, false);
    }
    
    private boolean isMapped(final int index) {
        return index >= 0 && index < this.numMapped && !this.isDeleted(index);
    }
    
    public static ScriptObject allocate(final Object[] arguments, final ScriptFunction callee, final int numParams) {
        final boolean isStrict = callee == null || callee.isStrict();
        final Global global = Global.instance();
        final ScriptObject proto = global.getObjectPrototype();
        if (isStrict) {
            return new NativeStrictArguments(arguments, numParams, proto, NativeStrictArguments.getInitialMap());
        }
        return new NativeArguments(arguments, callee, numParams, proto, getInitialMap());
    }
    
    public static Object G$length(final Object self) {
        if (self instanceof NativeArguments) {
            return ((NativeArguments)self).getArgumentsLength();
        }
        return 0;
    }
    
    public static void S$length(final Object self, final Object value) {
        if (self instanceof NativeArguments) {
            ((NativeArguments)self).setArgumentsLength(value);
        }
    }
    
    public static Object G$callee(final Object self) {
        if (self instanceof NativeArguments) {
            return ((NativeArguments)self).getCallee();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static void S$callee(final Object self, final Object value) {
        if (self instanceof NativeArguments) {
            ((NativeArguments)self).setCallee(value);
        }
    }
    
    @Override
    public Object getLength() {
        return this.length;
    }
    
    private Object getArgumentsLength() {
        return this.length;
    }
    
    private void setArgumentsLength(final Object length) {
        this.length = length;
    }
    
    private Object getCallee() {
        return this.callee;
    }
    
    private void setCallee(final Object callee) {
        this.callee = callee;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeArguments.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        G$LENGTH = findOwnMH("G$length", Object.class, Object.class);
        S$LENGTH = findOwnMH("S$length", Void.TYPE, Object.class, Object.class);
        G$CALLEE = findOwnMH("G$callee", Object.class, Object.class);
        S$CALLEE = findOwnMH("S$callee", Void.TYPE, Object.class, Object.class);
        final ArrayList<Property> properties = new ArrayList<Property>(2);
        properties.add(AccessorProperty.create("length", 2, NativeArguments.G$LENGTH, NativeArguments.S$LENGTH));
        properties.add(AccessorProperty.create("callee", 2, NativeArguments.G$CALLEE, NativeArguments.S$CALLEE));
        map$ = PropertyMap.newMap(properties);
    }
}
