// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.objects.Global;
import java.nio.ByteBuffer;

final class ByteBufferArrayData extends ArrayData
{
    private final ByteBuffer buf;
    
    ByteBufferArrayData(final int length) {
        super(length);
        this.buf = ByteBuffer.allocateDirect(length);
    }
    
    ByteBufferArrayData(final ByteBuffer buf) {
        super(buf.capacity());
        this.buf = buf;
    }
    
    @Override
    public PropertyDescriptor getDescriptor(final Global global, final int index) {
        return global.newDataDescriptor(this.getObject(index), false, true, true);
    }
    
    @Override
    public ArrayData copy() {
        throw unsupported("copy");
    }
    
    @Override
    public Object[] asObjectArray() {
        throw unsupported("asObjectArray");
    }
    
    @Override
    public void setLength(final long length) {
        throw new UnsupportedOperationException("setLength");
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        throw unsupported("shiftLeft");
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        throw unsupported("shiftRight");
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex < this.buf.capacity()) {
            return this;
        }
        throw unsupported("ensure");
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        throw unsupported("shrink");
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (value instanceof Number) {
            this.buf.put(index, ((Number)value).byteValue());
            return this;
        }
        throw ECMAErrors.typeError("not.a.number", ScriptRuntime.safeToString(value));
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.buf.put(index, (byte)value);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        this.buf.put(index, (byte)value);
        return this;
    }
    
    @Override
    public int getInt(final int index) {
        return 0xFF & this.buf.get(index);
    }
    
    @Override
    public double getDouble(final int index) {
        return 0xFF & this.buf.get(index);
    }
    
    @Override
    public Object getObject(final int index) {
        return 0xFF & this.buf.get(index);
    }
    
    @Override
    public boolean has(final int index) {
        return index > -1 && index < this.buf.capacity();
    }
    
    @Override
    public boolean canDelete(final int index, final boolean strict) {
        return false;
    }
    
    @Override
    public boolean canDelete(final long longIndex, final boolean strict) {
        return false;
    }
    
    @Override
    public ArrayData delete(final int index) {
        throw unsupported("delete");
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        throw unsupported("delete");
    }
    
    @Override
    public ArrayData push(final boolean strict, final Object... items) {
        throw unsupported("push");
    }
    
    @Override
    public Object pop() {
        throw unsupported("pop");
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        throw unsupported("slice");
    }
    
    @Override
    public ArrayData convert(final Class<?> type) {
        throw unsupported("convert");
    }
    
    private static UnsupportedOperationException unsupported(final String method) {
        return new UnsupportedOperationException(method);
    }
}
