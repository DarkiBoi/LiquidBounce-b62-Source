// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class SpillProperty extends AccessorProperty
{
    private static final long serialVersionUID = 3028496245198669460L;
    private static final MethodHandles.Lookup LOOKUP;
    private static final MethodHandle PARRAY_GETTER;
    private static final MethodHandle OARRAY_GETTER;
    private static final MethodHandle OBJECT_GETTER;
    private static final MethodHandle PRIMITIVE_GETTER;
    private static final MethodHandle OBJECT_SETTER;
    private static final MethodHandle PRIMITIVE_SETTER;
    
    private static MethodHandle primitiveGetter(final int slot, final int flags) {
        return ((flags & 0x800) == 0x800) ? Accessors.getCached(slot, true, true) : null;
    }
    
    private static MethodHandle primitiveSetter(final int slot, final int flags) {
        return ((flags & 0x800) == 0x800) ? Accessors.getCached(slot, true, false) : null;
    }
    
    private static MethodHandle objectGetter(final int slot) {
        return Accessors.getCached(slot, false, true);
    }
    
    private static MethodHandle objectSetter(final int slot) {
        return Accessors.getCached(slot, false, false);
    }
    
    public SpillProperty(final String key, final int flags, final int slot) {
        super(key, flags, slot, primitiveGetter(slot, flags), primitiveSetter(slot, flags), objectGetter(slot), objectSetter(slot));
    }
    
    public SpillProperty(final String key, final int flags, final int slot, final Class<?> initialType) {
        this(key, flags, slot);
        this.setType(this.hasDualFields() ? initialType : Object.class);
    }
    
    SpillProperty(final String key, final int flags, final int slot, final ScriptObject owner, final Object initialValue) {
        this(key, flags, slot);
        this.setInitialValue(owner, initialValue);
    }
    
    protected SpillProperty(final SpillProperty property) {
        super(property);
    }
    
    protected SpillProperty(final SpillProperty property, final Class<?> newType) {
        super(property, newType);
    }
    
    @Override
    public Property copy() {
        return new SpillProperty(this);
    }
    
    @Override
    public Property copy(final Class<?> newType) {
        return new SpillProperty(this, newType);
    }
    
    @Override
    public boolean isSpill() {
        return true;
    }
    
    @Override
    void initMethodHandles(final Class<?> structure) {
        final int slot = this.getSlot();
        this.primitiveGetter = primitiveGetter(slot, this.getFlags());
        this.primitiveSetter = primitiveSetter(slot, this.getFlags());
        this.objectGetter = objectGetter(slot);
        this.objectSetter = objectSetter(slot);
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        PARRAY_GETTER = Lookup.MH.asType(Lookup.MH.getter(SpillProperty.LOOKUP, ScriptObject.class, "primitiveSpill", long[].class), Lookup.MH.type(long[].class, Object.class));
        OARRAY_GETTER = Lookup.MH.asType(Lookup.MH.getter(SpillProperty.LOOKUP, ScriptObject.class, "objectSpill", Object[].class), Lookup.MH.type(Object[].class, Object.class));
        OBJECT_GETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementGetter(Object[].class), 0, SpillProperty.OARRAY_GETTER);
        PRIMITIVE_GETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementGetter(long[].class), 0, SpillProperty.PARRAY_GETTER);
        OBJECT_SETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementSetter(Object[].class), 0, SpillProperty.OARRAY_GETTER);
        PRIMITIVE_SETTER = Lookup.MH.filterArguments(Lookup.MH.arrayElementSetter(long[].class), 0, SpillProperty.PARRAY_GETTER);
    }
    
    private static class Accessors
    {
        private MethodHandle objectGetter;
        private MethodHandle objectSetter;
        private MethodHandle primitiveGetter;
        private MethodHandle primitiveSetter;
        private final int slot;
        private final MethodHandle ensureSpillSize;
        private static Accessors[] ACCESSOR_CACHE;
        
        Accessors(final int slot) {
            assert slot >= 0;
            this.slot = slot;
            this.ensureSpillSize = Lookup.MH.asType(Lookup.MH.insertArguments(ScriptObject.ENSURE_SPILL_SIZE, 1, slot), Lookup.MH.type(Object.class, Object.class));
        }
        
        private static void ensure(final int slot) {
            int len = Accessors.ACCESSOR_CACHE.length;
            if (slot >= len) {
                do {
                    len *= 2;
                } while (slot >= len);
                final Accessors[] newCache = new Accessors[len];
                System.arraycopy(Accessors.ACCESSOR_CACHE, 0, newCache, 0, Accessors.ACCESSOR_CACHE.length);
                Accessors.ACCESSOR_CACHE = newCache;
            }
        }
        
        static MethodHandle getCached(final int slot, final boolean isPrimitive, final boolean isGetter) {
            ensure(slot);
            Accessors acc = Accessors.ACCESSOR_CACHE[slot];
            if (acc == null) {
                acc = new Accessors(slot);
                Accessors.ACCESSOR_CACHE[slot] = acc;
            }
            return acc.getOrCreate(isPrimitive, isGetter);
        }
        
        private static MethodHandle primordial(final boolean isPrimitive, final boolean isGetter) {
            if (isPrimitive) {
                return isGetter ? SpillProperty.PRIMITIVE_GETTER : SpillProperty.PRIMITIVE_SETTER;
            }
            return isGetter ? SpillProperty.OBJECT_GETTER : SpillProperty.OBJECT_SETTER;
        }
        
        MethodHandle getOrCreate(final boolean isPrimitive, final boolean isGetter) {
            MethodHandle accessor = this.getInner(isPrimitive, isGetter);
            if (accessor != null) {
                return accessor;
            }
            accessor = primordial(isPrimitive, isGetter);
            accessor = Lookup.MH.insertArguments(accessor, 1, this.slot);
            if (!isGetter) {
                accessor = Lookup.MH.filterArguments(accessor, 0, this.ensureSpillSize);
            }
            this.setInner(isPrimitive, isGetter, accessor);
            return accessor;
        }
        
        void setInner(final boolean isPrimitive, final boolean isGetter, final MethodHandle mh) {
            if (isPrimitive) {
                if (isGetter) {
                    this.primitiveGetter = mh;
                }
                else {
                    this.primitiveSetter = mh;
                }
            }
            else if (isGetter) {
                this.objectGetter = mh;
            }
            else {
                this.objectSetter = mh;
            }
        }
        
        MethodHandle getInner(final boolean isPrimitive, final boolean isGetter) {
            if (isPrimitive) {
                return isGetter ? this.primitiveGetter : this.primitiveSetter;
            }
            return isGetter ? this.objectGetter : this.objectSetter;
        }
        
        static {
            Accessors.ACCESSOR_CACHE = new Accessors[512];
        }
    }
}
