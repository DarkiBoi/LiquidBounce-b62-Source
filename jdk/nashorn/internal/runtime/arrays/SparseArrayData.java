// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.util.SortedMap;
import java.util.TreeMap;

class SparseArrayData extends ArrayData
{
    static final int MAX_DENSE_LENGTH = 131072;
    private ArrayData underlying;
    private final long maxDenseLength;
    private TreeMap<Long, Object> sparseMap;
    
    SparseArrayData(final ArrayData underlying, final long length) {
        this(underlying, length, new TreeMap<Long, Object>());
    }
    
    private SparseArrayData(final ArrayData underlying, final long length, final TreeMap<Long, Object> sparseMap) {
        super(length);
        assert underlying.length() <= length;
        this.underlying = underlying;
        this.maxDenseLength = underlying.length();
        this.sparseMap = sparseMap;
    }
    
    @Override
    public ArrayData copy() {
        return new SparseArrayData(this.underlying.copy(), this.length(), new TreeMap<Long, Object>(this.sparseMap));
    }
    
    @Override
    public Object[] asObjectArray() {
        final int len = (int)Math.min(this.length(), 2147483647L);
        final int underlyingLength = (int)Math.min(len, this.underlying.length());
        final Object[] objArray = new Object[len];
        for (int i = 0; i < underlyingLength; ++i) {
            objArray[i] = this.underlying.getObject(i);
        }
        Arrays.fill(objArray, underlyingLength, len, ScriptRuntime.UNDEFINED);
        for (final Map.Entry<Long, Object> entry : this.sparseMap.entrySet()) {
            final long key = entry.getKey();
            if (key >= 2147483647L) {
                break;
            }
            objArray[(int)key] = entry.getValue();
        }
        return objArray;
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        this.underlying = this.underlying.shiftLeft(by);
        final TreeMap<Long, Object> newSparseMap = new TreeMap<Long, Object>();
        for (final Map.Entry<Long, Object> entry : this.sparseMap.entrySet()) {
            final long newIndex = entry.getKey() - by;
            if (newIndex >= 0L) {
                if (newIndex < this.maxDenseLength) {
                    final long oldLength = this.underlying.length();
                    this.underlying = this.underlying.ensure(newIndex).set((int)newIndex, entry.getValue(), false).safeDelete(oldLength, newIndex - 1L, false);
                }
                else {
                    newSparseMap.put(newIndex, entry.getValue());
                }
            }
        }
        this.sparseMap = newSparseMap;
        this.setLength(Math.max(this.length() - by, 0L));
        return this.sparseMap.isEmpty() ? this.underlying : this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        final TreeMap<Long, Object> newSparseMap = new TreeMap<Long, Object>();
        final long len = this.underlying.length();
        if (len + by > this.maxDenseLength) {
            long i;
            long tempLength;
            for (tempLength = (i = Math.max(0L, this.maxDenseLength - by)); i < len; ++i) {
                if (this.underlying.has((int)i)) {
                    newSparseMap.put(i + by, this.underlying.getObject((int)i));
                }
            }
            (this.underlying = this.underlying.shrink((int)tempLength)).setLength(tempLength);
        }
        this.underlying = this.underlying.shiftRight(by);
        for (final Map.Entry<Long, Object> entry : this.sparseMap.entrySet()) {
            final long newIndex = entry.getKey() + by;
            newSparseMap.put(newIndex, entry.getValue());
        }
        this.sparseMap = newSparseMap;
        this.setLength(this.length() + by);
        return this;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex >= this.length()) {
            this.setLength(safeIndex + 1L);
        }
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        if (newLength < this.underlying.length()) {
            (this.underlying = this.underlying.shrink(newLength)).setLength(newLength);
            this.sparseMap.clear();
            this.setLength(newLength);
        }
        this.sparseMap.subMap(newLength, Long.MAX_VALUE).clear();
        this.setLength(newLength);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (index >= 0 && index < this.maxDenseLength) {
            final long oldLength = this.underlying.length();
            this.underlying = this.underlying.ensure(index).set(index, value, strict).safeDelete(oldLength, index - 1, strict);
            this.setLength(Math.max(this.underlying.length(), this.length()));
        }
        else {
            final Long longIndex = indexToKey(index);
            this.sparseMap.put(longIndex, value);
            this.setLength(Math.max(longIndex + 1L, this.length()));
        }
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        if (index >= 0 && index < this.maxDenseLength) {
            final long oldLength = this.underlying.length();
            this.underlying = this.underlying.ensure(index).set(index, value, strict).safeDelete(oldLength, index - 1, strict);
            this.setLength(Math.max(this.underlying.length(), this.length()));
        }
        else {
            final Long longIndex = indexToKey(index);
            this.sparseMap.put(longIndex, value);
            this.setLength(Math.max(longIndex + 1L, this.length()));
        }
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        if (index >= 0 && index < this.maxDenseLength) {
            final long oldLength = this.underlying.length();
            this.underlying = this.underlying.ensure(index).set(index, value, strict).safeDelete(oldLength, index - 1, strict);
            this.setLength(Math.max(this.underlying.length(), this.length()));
        }
        else {
            final Long longIndex = indexToKey(index);
            this.sparseMap.put(longIndex, value);
            this.setLength(Math.max(longIndex + 1L, this.length()));
        }
        return this;
    }
    
    @Override
    public ArrayData setEmpty(final int index) {
        this.underlying.setEmpty(index);
        return this;
    }
    
    @Override
    public ArrayData setEmpty(final long lo, final long hi) {
        this.underlying.setEmpty(lo, hi);
        return this;
    }
    
    @Override
    public Type getOptimisticType() {
        return this.underlying.getOptimisticType();
    }
    
    @Override
    public int getInt(final int index) {
        if (index >= 0 && index < this.maxDenseLength) {
            return this.underlying.getInt(index);
        }
        return JSType.toInt32(this.sparseMap.get(indexToKey(index)));
    }
    
    @Override
    public int getIntOptimistic(final int index, final int programPoint) {
        if (index >= 0 && index < this.maxDenseLength) {
            return this.underlying.getIntOptimistic(index, programPoint);
        }
        return JSType.toInt32Optimistic(this.sparseMap.get(indexToKey(index)), programPoint);
    }
    
    @Override
    public double getDouble(final int index) {
        if (index >= 0 && index < this.maxDenseLength) {
            return this.underlying.getDouble(index);
        }
        return JSType.toNumber(this.sparseMap.get(indexToKey(index)));
    }
    
    @Override
    public double getDoubleOptimistic(final int index, final int programPoint) {
        if (index >= 0 && index < this.maxDenseLength) {
            return this.underlying.getDouble(index);
        }
        return JSType.toNumberOptimistic(this.sparseMap.get(indexToKey(index)), programPoint);
    }
    
    @Override
    public Object getObject(final int index) {
        if (index >= 0 && index < this.maxDenseLength) {
            return this.underlying.getObject(index);
        }
        final Long key = indexToKey(index);
        if (this.sparseMap.containsKey(key)) {
            return this.sparseMap.get(key);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    @Override
    public boolean has(final int index) {
        if (index >= 0 && index < this.maxDenseLength) {
            return index < this.underlying.length() && this.underlying.has(index);
        }
        return this.sparseMap.containsKey(indexToKey(index));
    }
    
    @Override
    public ArrayData delete(final int index) {
        if (index >= 0 && index < this.maxDenseLength) {
            if (index < this.underlying.length()) {
                this.underlying = this.underlying.delete(index);
            }
        }
        else {
            this.sparseMap.remove(indexToKey(index));
        }
        return this;
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        if (fromIndex < this.maxDenseLength && fromIndex < this.underlying.length()) {
            this.underlying = this.underlying.delete(fromIndex, Math.min(toIndex, this.underlying.length() - 1L));
        }
        if (toIndex >= this.maxDenseLength) {
            this.sparseMap.subMap(fromIndex, true, toIndex, true).clear();
        }
        return this;
    }
    
    private static Long indexToKey(final int index) {
        return ArrayIndex.toLongIndex(index);
    }
    
    @Override
    public ArrayData convert(final Class<?> type) {
        this.underlying = this.underlying.convert(type);
        return this;
    }
    
    @Override
    public Object pop() {
        final long len = this.length();
        final long underlyingLen = this.underlying.length();
        if (len == 0L) {
            return ScriptRuntime.UNDEFINED;
        }
        if (len == underlyingLen) {
            final Object result = this.underlying.pop();
            this.setLength(this.underlying.length());
            return result;
        }
        this.setLength(len - 1L);
        final Long key = len - 1L;
        return this.sparseMap.containsKey(key) ? this.sparseMap.remove(key) : ScriptRuntime.UNDEFINED;
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        assert to <= this.length();
        final long start = (from < 0L) ? (from + this.length()) : from;
        final long newLength = to - start;
        final long underlyingLength = this.underlying.length();
        if (start >= 0L && to <= this.maxDenseLength) {
            if (newLength <= underlyingLength) {
                return this.underlying.slice(from, to);
            }
            return this.underlying.slice(from, to).ensure(newLength - 1L).delete(underlyingLength, newLength);
        }
        else {
            ArrayData sliced = SparseArrayData.EMPTY_ARRAY;
            sliced = sliced.ensure(newLength - 1L);
            for (long i = start; i < to; i = this.nextIndex(i)) {
                if (this.has((int)i)) {
                    sliced = sliced.set((int)(i - start), this.getObject((int)i), false);
                }
            }
            assert sliced.length() == newLength;
            return sliced;
        }
    }
    
    public long nextIndex(final long index) {
        if (index < this.underlying.length() - 1L) {
            return this.underlying.nextIndex(index);
        }
        final Long nextKey = this.sparseMap.higherKey(index);
        if (nextKey != null) {
            return nextKey;
        }
        return this.length();
    }
}
