// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Objects;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.SwitchPoint;
import java.io.Serializable;

public abstract class Property implements Serializable
{
    public static final int WRITABLE_ENUMERABLE_CONFIGURABLE = 0;
    public static final int NOT_WRITABLE = 1;
    public static final int NOT_ENUMERABLE = 2;
    public static final int NOT_CONFIGURABLE = 4;
    private static final int MODIFY_MASK = 7;
    public static final int IS_PARAMETER = 8;
    public static final int HAS_ARGUMENTS = 16;
    public static final int IS_FUNCTION_DECLARATION = 32;
    public static final int IS_NASGEN_PRIMITIVE = 64;
    public static final int IS_BUILTIN = 128;
    public static final int IS_BOUND = 256;
    public static final int NEEDS_DECLARATION = 512;
    public static final int IS_LEXICAL_BINDING = 1024;
    public static final int DUAL_FIELDS = 2048;
    private final String key;
    private int flags;
    private final int slot;
    private Class<?> type;
    protected transient SwitchPoint builtinSwitchPoint;
    private static final long serialVersionUID = 2099814273074501176L;
    
    Property(final String key, final int flags, final int slot) {
        assert key != null;
        this.key = key;
        this.flags = flags;
        this.slot = slot;
    }
    
    Property(final Property property, final int flags) {
        this.key = property.key;
        this.slot = property.slot;
        this.builtinSwitchPoint = property.builtinSwitchPoint;
        this.flags = flags;
    }
    
    public abstract Property copy();
    
    public abstract Property copy(final Class<?> p0);
    
    static int mergeFlags(final PropertyDescriptor oldDesc, final PropertyDescriptor newDesc) {
        int propFlags = 0;
        boolean value = newDesc.has("configurable") ? newDesc.isConfigurable() : oldDesc.isConfigurable();
        if (!value) {
            propFlags |= 0x4;
        }
        value = (newDesc.has("enumerable") ? newDesc.isEnumerable() : oldDesc.isEnumerable());
        if (!value) {
            propFlags |= 0x2;
        }
        value = (newDesc.has("writable") ? newDesc.isWritable() : oldDesc.isWritable());
        if (!value) {
            propFlags |= 0x1;
        }
        return propFlags;
    }
    
    public final void setBuiltinSwitchPoint(final SwitchPoint sp) {
        this.builtinSwitchPoint = sp;
    }
    
    public final SwitchPoint getBuiltinSwitchPoint() {
        return this.builtinSwitchPoint;
    }
    
    public boolean isBuiltin() {
        return this.builtinSwitchPoint != null && !this.builtinSwitchPoint.hasBeenInvalidated();
    }
    
    static int toFlags(final PropertyDescriptor desc) {
        int propFlags = 0;
        if (!desc.isConfigurable()) {
            propFlags |= 0x4;
        }
        if (!desc.isEnumerable()) {
            propFlags |= 0x2;
        }
        if (!desc.isWritable()) {
            propFlags |= 0x1;
        }
        return propFlags;
    }
    
    public boolean hasGetterFunction(final ScriptObject obj) {
        return false;
    }
    
    public boolean hasSetterFunction(final ScriptObject obj) {
        return false;
    }
    
    public boolean isWritable() {
        return (this.flags & 0x1) == 0x0;
    }
    
    public boolean isConfigurable() {
        return (this.flags & 0x4) == 0x0;
    }
    
    public boolean isEnumerable() {
        return (this.flags & 0x2) == 0x0;
    }
    
    public boolean isParameter() {
        return (this.flags & 0x8) != 0x0;
    }
    
    public boolean hasArguments() {
        return (this.flags & 0x10) != 0x0;
    }
    
    public boolean isSpill() {
        return false;
    }
    
    public boolean isBound() {
        return (this.flags & 0x100) != 0x0;
    }
    
    public boolean needsDeclaration() {
        return (this.flags & 0x200) != 0x0;
    }
    
    public Property addFlags(final int propertyFlags) {
        if ((this.flags & propertyFlags) != propertyFlags) {
            final Property copy;
            final Property cloned = copy = this.copy();
            copy.flags |= propertyFlags;
            return cloned;
        }
        return this;
    }
    
    public int getFlags() {
        return this.flags;
    }
    
    public Property removeFlags(final int propertyFlags) {
        if ((this.flags & propertyFlags) != 0x0) {
            final Property copy;
            final Property cloned = copy = this.copy();
            copy.flags &= ~propertyFlags;
            return cloned;
        }
        return this;
    }
    
    public Property setFlags(final int propertyFlags) {
        if (this.flags != propertyFlags) {
            final Property copy;
            final Property cloned = copy = this.copy();
            copy.flags &= 0xFFFFFFF8;
            final Property property = cloned;
            property.flags |= (propertyFlags & 0x7);
            return cloned;
        }
        return this;
    }
    
    public abstract MethodHandle getGetter(final Class<?> p0);
    
    public abstract MethodHandle getOptimisticGetter(final Class<?> p0, final int p1);
    
    abstract void initMethodHandles(final Class<?> p0);
    
    public String getKey() {
        return this.key;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public abstract int getIntValue(final ScriptObject p0, final ScriptObject p1);
    
    public abstract double getDoubleValue(final ScriptObject p0, final ScriptObject p1);
    
    public abstract Object getObjectValue(final ScriptObject p0, final ScriptObject p1);
    
    public abstract void setValue(final ScriptObject p0, final ScriptObject p1, final int p2, final boolean p3);
    
    public abstract void setValue(final ScriptObject p0, final ScriptObject p1, final double p2, final boolean p3);
    
    public abstract void setValue(final ScriptObject p0, final ScriptObject p1, final Object p2, final boolean p3);
    
    public abstract MethodHandle getSetter(final Class<?> p0, final PropertyMap p1);
    
    public ScriptFunction getGetterFunction(final ScriptObject obj) {
        return null;
    }
    
    public ScriptFunction getSetterFunction(final ScriptObject obj) {
        return null;
    }
    
    @Override
    public int hashCode() {
        final Class<?> t = this.getLocalType();
        return Objects.hashCode(this.key) ^ this.flags ^ this.getSlot() ^ ((t == null) ? 0 : t.hashCode());
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final Property otherProperty = (Property)other;
        return this.equalsWithoutType(otherProperty) && this.getLocalType() == otherProperty.getLocalType();
    }
    
    boolean equalsWithoutType(final Property otherProperty) {
        return this.getFlags() == otherProperty.getFlags() && this.getSlot() == otherProperty.getSlot() && this.getKey().equals(otherProperty.getKey());
    }
    
    private static String type(final Class<?> type) {
        if (type == null) {
            return "undef";
        }
        if (type == Integer.TYPE) {
            return "i";
        }
        if (type == Double.TYPE) {
            return "d";
        }
        return "o";
    }
    
    public final String toStringShort() {
        final StringBuilder sb = new StringBuilder();
        final Class<?> t = this.getLocalType();
        sb.append(this.getKey()).append(" (").append(type(t)).append(')');
        return sb.toString();
    }
    
    private static String indent(final String str, final int indent) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < indent - str.length(); ++i) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Class<?> t = this.getLocalType();
        sb.append(indent(this.getKey(), 20)).append(" id=").append(Debug.id(this)).append(" (0x").append(indent(Integer.toHexString(this.flags), 4)).append(") ").append(this.getClass().getSimpleName()).append(" {").append(indent(type(t), 5)).append('}');
        if (this.slot != -1) {
            sb.append(" [").append("slot=").append(this.slot).append(']');
        }
        return sb.toString();
    }
    
    public final Class<?> getType() {
        return this.type;
    }
    
    public final void setType(final Class<?> type) {
        assert type != Boolean.TYPE : "no boolean storage support yet - fix this";
        this.type = ((type == null) ? null : (type.isPrimitive() ? type : Object.class));
    }
    
    protected Class<?> getLocalType() {
        return this.getType();
    }
    
    public boolean canChangeType() {
        return false;
    }
    
    public boolean isFunctionDeclaration() {
        return (this.flags & 0x20) != 0x0;
    }
    
    public boolean isLexicalBinding() {
        return (this.flags & 0x400) != 0x0;
    }
    
    public boolean hasDualFields() {
        return (this.flags & 0x800) != 0x0;
    }
}
