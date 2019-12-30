// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.ScriptRuntime;

final class DeletedRangeArrayFilter extends ArrayFilter
{
    private long lo;
    private long hi;
    
    DeletedRangeArrayFilter(final ArrayData underlying, final long lo, final long hi) {
        super(maybeSparse(underlying, hi));
        this.lo = lo;
        this.hi = hi;
    }
    
    private static ArrayData maybeSparse(final ArrayData underlying, final long hi) {
        if (hi < 131072L || underlying instanceof SparseArrayData) {
            return underlying;
        }
        return new SparseArrayData(underlying, underlying.length());
    }
    
    private boolean isEmpty() {
        return this.lo > this.hi;
    }
    
    private boolean isDeleted(final int index) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        return this.lo <= longIndex && longIndex <= this.hi;
    }
    
    @Override
    public ArrayData copy() {
        return new DeletedRangeArrayFilter(this.underlying.copy(), this.lo, this.hi);
    }
    
    @Override
    public Object[] asObjectArray() {
        final Object[] value = super.asObjectArray();
        if (this.lo < 2147483647L) {
            for (int end = (int)Math.min(this.hi + 1L, 2147483647L), i = (int)this.lo; i < end; ++i) {
                value[i] = ScriptRuntime.UNDEFINED;
            }
        }
        return value;
    }
    
    @Override
    public Object asArrayOfType(final Class<?> componentType) {
        final Object value = super.asArrayOfType(componentType);
        final Object undefValue = ArrayFilter.convertUndefinedValue(componentType);
        if (this.lo < 2147483647L) {
            for (int end = (int)Math.min(this.hi + 1L, 2147483647L), i = (int)this.lo; i < end; ++i) {
                Array.set(value, i, undefValue);
            }
        }
        return value;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex >= 131072L && safeIndex >= this.length()) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        return super.ensure(safeIndex);
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        super.shiftLeft(by);
        this.lo = Math.max(0L, this.lo - by);
        this.hi = Math.max(-1L, this.hi - by);
        return this.isEmpty() ? this.getUnderlying() : this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        super.shiftRight(by);
        final long len = this.length();
        this.lo = Math.min(len, this.lo + by);
        this.hi = Math.min(len - 1L, this.hi + by);
        return this.isEmpty() ? this.getUnderlying() : this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        super.shrink(newLength);
        this.lo = Math.min(newLength, this.lo);
        this.hi = Math.min(newLength - 1L, this.hi);
        return this.isEmpty() ? this.getUnderlying() : this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return this.getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            ++this.lo;
        }
        else {
            assert longIndex == this.hi;
            --this.hi;
        }
        return this.isEmpty() ? this.getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return this.getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            ++this.lo;
        }
        else {
            assert longIndex == this.hi;
            --this.hi;
        }
        return this.isEmpty() ? this.getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return this.getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            ++this.lo;
        }
        else {
            assert longIndex == this.hi;
            --this.hi;
        }
        return this.isEmpty() ? this.getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }
    
    @Override
    public boolean has(final int index) {
        return super.has(index) && !this.isDeleted(index);
    }
    
    private ArrayData getDeletedArrayFilter() {
        final ArrayData deleteFilter = new DeletedArrayFilter(this.getUnderlying());
        deleteFilter.delete(this.lo, this.hi);
        return deleteFilter;
    }
    
    @Override
    public ArrayData delete(final int index) {
        final long longIndex = ArrayIndex.toLongIndex(index);
        this.underlying.setEmpty(index);
        if (longIndex + 1L == this.lo) {
            this.lo = longIndex;
        }
        else if (longIndex - 1L == this.hi) {
            this.hi = longIndex;
        }
        else if (longIndex < this.lo || this.hi < longIndex) {
            return this.getDeletedArrayFilter().delete(index);
        }
        return this;
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        if (fromIndex > this.hi + 1L || toIndex < this.lo - 1L) {
            return this.getDeletedArrayFilter().delete(fromIndex, toIndex);
        }
        this.lo = Math.min(fromIndex, this.lo);
        this.hi = Math.max(toIndex, this.hi);
        this.underlying.setEmpty(this.lo, this.hi);
        return this;
    }
    
    @Override
    public Object pop() {
        final int index = (int)this.length() - 1;
        if (super.has(index)) {
            final boolean isDeleted = this.isDeleted(index);
            final Object value = super.pop();
            this.lo = Math.min(index + 1, this.lo);
            this.hi = Math.min(index, this.hi);
            return isDeleted ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        return new DeletedRangeArrayFilter(this.underlying.slice(from, to), Math.max(0L, this.lo - from), Math.max(0L, this.hi - from));
    }
}
