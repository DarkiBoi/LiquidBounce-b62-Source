// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.BitVector;

final class UndefinedArrayFilter extends ArrayFilter
{
    private final BitVector undefined;
    
    UndefinedArrayFilter(final ArrayData underlying) {
        super(underlying);
        this.undefined = new BitVector(underlying.length());
    }
    
    @Override
    public ArrayData copy() {
        final UndefinedArrayFilter copy = new UndefinedArrayFilter(this.underlying.copy());
        copy.getUndefined().copy(this.undefined);
        return copy;
    }
    
    @Override
    public Object[] asObjectArray() {
        final Object[] value = super.asObjectArray();
        for (int i = 0; i < value.length; ++i) {
            if (this.undefined.isSet(i)) {
                value[i] = ScriptRuntime.UNDEFINED;
            }
        }
        return value;
    }
    
    @Override
    public Object asArrayOfType(final Class<?> componentType) {
        final Object value = super.asArrayOfType(componentType);
        final Object undefValue = ArrayFilter.convertUndefinedValue(componentType);
        for (int l = Array.getLength(value), i = 0; i < l; ++i) {
            if (this.undefined.isSet(i)) {
                Array.set(value, i, undefValue);
            }
        }
        return value;
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        super.shiftLeft(by);
        this.undefined.shiftLeft(by, this.length());
        return this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        super.shiftRight(by);
        this.undefined.shiftRight(by, this.length());
        return this;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex >= 131072L && safeIndex >= this.length()) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        super.ensure(safeIndex);
        this.undefined.resize(this.length());
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        super.shrink(newLength);
        this.undefined.resize(this.length());
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        this.undefined.clear(index);
        if (value == ScriptRuntime.UNDEFINED) {
            this.undefined.set(index);
            return this;
        }
        return super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }
    
    @Override
    public int getInt(final int index) {
        if (this.undefined.isSet(index)) {
            return 0;
        }
        return super.getInt(index);
    }
    
    @Override
    public int getIntOptimistic(final int index, final int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getIntOptimistic(index, programPoint);
    }
    
    @Override
    public double getDouble(final int index) {
        if (this.undefined.isSet(index)) {
            return Double.NaN;
        }
        return super.getDouble(index);
    }
    
    @Override
    public double getDoubleOptimistic(final int index, final int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getDoubleOptimistic(index, programPoint);
    }
    
    @Override
    public Object getObject(final int index) {
        if (this.undefined.isSet(index)) {
            return ScriptRuntime.UNDEFINED;
        }
        return super.getObject(index);
    }
    
    @Override
    public ArrayData delete(final int index) {
        this.undefined.clear(index);
        return super.delete(index);
    }
    
    @Override
    public Object pop() {
        final long index = this.length() - 1L;
        if (super.has((int)index)) {
            final boolean isUndefined = this.undefined.isSet(index);
            final Object value = super.pop();
            return isUndefined ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        final ArrayData newArray = this.underlying.slice(from, to);
        final UndefinedArrayFilter newFilter = new UndefinedArrayFilter(newArray);
        newFilter.getUndefined().copy(this.undefined);
        newFilter.getUndefined().shiftLeft(from, newFilter.length());
        return newFilter;
    }
    
    private BitVector getUndefined() {
        return this.undefined;
    }
}
