// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.util.TreeMap;
import java.util.SortedMap;

final class LengthNotWritableFilter extends ArrayFilter
{
    private final SortedMap<Long, Object> extraElements;
    
    LengthNotWritableFilter(final ArrayData underlying) {
        this(underlying, new TreeMap<Long, Object>());
    }
    
    private LengthNotWritableFilter(final ArrayData underlying, final SortedMap<Long, Object> extraElements) {
        super(underlying);
        this.extraElements = extraElements;
    }
    
    @Override
    public ArrayData copy() {
        return new LengthNotWritableFilter(this.underlying.copy(), new TreeMap<Long, Object>(this.extraElements));
    }
    
    @Override
    public boolean has(final int index) {
        return super.has(index) || this.extraElements.containsKey((long)index);
    }
    
    @Override
    public void setLength(final long length) {
    }
    
    @Override
    public ArrayData ensure(final long index) {
        return this;
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        return new LengthNotWritableFilter(this.underlying.slice(from, to), this.extraElements.subMap(from, to));
    }
    
    private boolean checkAdd(final long index, final Object value) {
        if (index >= this.length()) {
            this.extraElements.put(index, value);
            return true;
        }
        return false;
    }
    
    private Object get(final long index) {
        final Object obj = this.extraElements.get(index);
        if (obj == null) {
            return ScriptRuntime.UNDEFINED;
        }
        return obj;
    }
    
    @Override
    public int getInt(final int index) {
        if (index >= this.length()) {
            return JSType.toInt32(this.get(index));
        }
        return this.underlying.getInt(index);
    }
    
    @Override
    public int getIntOptimistic(final int index, final int programPoint) {
        if (index >= this.length()) {
            return JSType.toInt32Optimistic(this.get(index), programPoint);
        }
        return this.underlying.getIntOptimistic(index, programPoint);
    }
    
    @Override
    public double getDouble(final int index) {
        if (index >= this.length()) {
            return JSType.toNumber(this.get(index));
        }
        return this.underlying.getDouble(index);
    }
    
    @Override
    public double getDoubleOptimistic(final int index, final int programPoint) {
        if (index >= this.length()) {
            return JSType.toNumberOptimistic(this.get(index), programPoint);
        }
        return this.underlying.getDoubleOptimistic(index, programPoint);
    }
    
    @Override
    public Object getObject(final int index) {
        if (index >= this.length()) {
            return this.get(index);
        }
        return this.underlying.getObject(index);
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        if (this.checkAdd(index, value)) {
            return this;
        }
        this.underlying = this.underlying.set(index, value, strict);
        return this;
    }
    
    @Override
    public ArrayData delete(final int index) {
        this.extraElements.remove(index);
        this.underlying = this.underlying.delete(index);
        return this;
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        final Iterator<Long> iter = this.extraElements.keySet().iterator();
        while (iter.hasNext()) {
            final long next = iter.next();
            if (next >= fromIndex && next <= toIndex) {
                iter.remove();
            }
            if (next > toIndex) {
                break;
            }
        }
        this.underlying = this.underlying.delete(fromIndex, toIndex);
        return this;
    }
    
    @Override
    public Iterator<Long> indexIterator() {
        final List<Long> keys = this.computeIteratorKeys();
        keys.addAll(this.extraElements.keySet());
        return keys.iterator();
    }
}
