// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.BitVector;

final class DeletedArrayFilter extends ArrayFilter
{
    private final BitVector deleted;
    
    DeletedArrayFilter(final ArrayData underlying) {
        super(underlying);
        this.deleted = new BitVector(underlying.length());
    }
    
    @Override
    public ArrayData copy() {
        final DeletedArrayFilter copy = new DeletedArrayFilter(this.underlying.copy());
        copy.getDeleted().copy(this.deleted);
        return copy;
    }
    
    @Override
    public Object[] asObjectArray() {
        final Object[] value = super.asObjectArray();
        for (int i = 0; i < value.length; ++i) {
            if (this.deleted.isSet(i)) {
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
            if (this.deleted.isSet(i)) {
                Array.set(value, i, undefValue);
            }
        }
        return value;
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        super.shiftLeft(by);
        this.deleted.shiftLeft(by, this.length());
        return this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        super.shiftRight(by);
        this.deleted.shiftRight(by, this.length());
        return this;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex >= 131072L && safeIndex >= this.length()) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        super.ensure(safeIndex);
        this.deleted.resize(this.length());
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        super.shrink(newLength);
        this.deleted.resize(this.length());
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        this.deleted.clear(ArrayIndex.toLongIndex(index));
        return super.set(index, value, strict);
    }
    
    @Override
    public boolean has(final int index) {
        return super.has(index) && this.deleted.isClear(ArrayIndex.toLongIndex(index));
    }
    
    @Override
    public ArrayData delete(final int index) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        assert longIndex >= 0L && longIndex < this.length();
        this.deleted.set(longIndex);
        this.underlying.setEmpty(index);
        return this;
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        assert fromIndex >= 0L && fromIndex <= toIndex && toIndex < this.length();
        this.deleted.setRange(fromIndex, toIndex + 1L);
        this.underlying.setEmpty(fromIndex, toIndex);
        return this;
    }
    
    @Override
    public Object pop() {
        final long index = this.length() - 1L;
        if (super.has((int)index)) {
            final boolean isDeleted = this.deleted.isSet(index);
            final Object value = super.pop();
            return isDeleted ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        final ArrayData newArray = this.underlying.slice(from, to);
        final DeletedArrayFilter newFilter = new DeletedArrayFilter(newArray);
        newFilter.getDeleted().copy(this.deleted);
        newFilter.getDeleted().shiftLeft(from, newFilter.length());
        return newFilter;
    }
    
    private BitVector getDeleted() {
        return this.deleted;
    }
}
