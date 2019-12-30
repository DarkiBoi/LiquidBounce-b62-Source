// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.codegen.types.Type;

abstract class ArrayFilter extends ArrayData
{
    protected ArrayData underlying;
    
    ArrayFilter(final ArrayData underlying) {
        super(underlying.length());
        this.underlying = underlying;
    }
    
    protected ArrayData getUnderlying() {
        return this.underlying;
    }
    
    @Override
    public void setLength(final long length) {
        super.setLength(length);
        this.underlying.setLength(length);
    }
    
    @Override
    public Object[] asObjectArray() {
        return this.underlying.asObjectArray();
    }
    
    @Override
    public Object asArrayOfType(final Class<?> componentType) {
        return this.underlying.asArrayOfType(componentType);
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        this.underlying.shiftLeft(by);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        this.underlying = this.underlying.shiftRight(by);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        this.underlying = this.underlying.ensure(safeIndex);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        this.underlying = this.underlying.shrink(newLength);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        this.underlying = this.underlying.set(index, value, strict);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.underlying = this.underlying.set(index, value, strict);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        this.underlying = this.underlying.set(index, value, strict);
        this.setLength(this.underlying.length());
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
        return this.underlying.getInt(index);
    }
    
    @Override
    public int getIntOptimistic(final int index, final int programPoint) {
        return this.underlying.getIntOptimistic(index, programPoint);
    }
    
    @Override
    public double getDouble(final int index) {
        return this.underlying.getDouble(index);
    }
    
    @Override
    public double getDoubleOptimistic(final int index, final int programPoint) {
        return this.underlying.getDoubleOptimistic(index, programPoint);
    }
    
    @Override
    public Object getObject(final int index) {
        return this.underlying.getObject(index);
    }
    
    @Override
    public boolean has(final int index) {
        return this.underlying.has(index);
    }
    
    @Override
    public ArrayData delete(final int index) {
        this.underlying = this.underlying.delete(index);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData delete(final long from, final long to) {
        this.underlying = this.underlying.delete(from, to);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public ArrayData convert(final Class<?> type) {
        this.underlying = this.underlying.convert(type);
        this.setLength(this.underlying.length());
        return this;
    }
    
    @Override
    public Object pop() {
        final Object value = this.underlying.pop();
        this.setLength(this.underlying.length());
        return value;
    }
    
    public long nextIndex(final long index) {
        return this.underlying.nextIndex(index);
    }
    
    static Object convertUndefinedValue(final Class<?> targetType) {
        return ArrayData.invoke(Bootstrap.getLinkerServices().getTypeConverter(Undefined.class, targetType), ScriptRuntime.UNDEFINED);
    }
}
