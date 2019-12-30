// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.util.Arrays;
import java.lang.invoke.MethodHandle;

final class ObjectArrayData extends ContinuousArrayData implements AnyElements
{
    private Object[] array;
    private static final MethodHandle HAS_GET_ELEM;
    private static final MethodHandle SET_ELEM;
    
    ObjectArrayData(final Object[] array, final int length) {
        super(length);
        assert array.length >= length;
        this.array = array;
    }
    
    @Override
    public final Class<?> getElementType() {
        return Object.class;
    }
    
    @Override
    public final Class<?> getBoxedElementType() {
        return this.getElementType();
    }
    
    @Override
    public final int getElementWeight() {
        return 4;
    }
    
    @Override
    public final ContinuousArrayData widest(final ContinuousArrayData otherData) {
        return (otherData instanceof NumericElements) ? this : otherData;
    }
    
    @Override
    public ObjectArrayData copy() {
        return new ObjectArrayData(this.array.clone(), (int)this.length());
    }
    
    @Override
    public Object[] asObjectArray() {
        return (this.array.length == this.length()) ? this.array.clone() : this.asObjectArrayCopy();
    }
    
    private Object[] asObjectArrayCopy() {
        final long len = this.length();
        assert len <= 2147483647L;
        final Object[] copy = new Object[(int)len];
        System.arraycopy(this.array, 0, copy, 0, (int)len);
        return copy;
    }
    
    @Override
    public ObjectArrayData convert(final Class<?> type) {
        return this;
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        if (by >= this.length()) {
            this.shrink(0L);
        }
        else {
            System.arraycopy(this.array, by, this.array, 0, this.array.length - by);
        }
        this.setLength(Math.max(0L, this.length() - by));
        return this;
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        final ArrayData newData = this.ensure(by + this.length() - 1L);
        if (newData != this) {
            newData.shiftRight(by);
            return newData;
        }
        System.arraycopy(this.array, 0, this.array, by, this.array.length - by);
        return this;
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        if (safeIndex >= 131072L) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        final int alen = this.array.length;
        if (safeIndex >= alen) {
            final int newLength = ArrayData.nextSize((int)safeIndex);
            this.array = Arrays.copyOf(this.array, newLength);
        }
        if (safeIndex >= this.length()) {
            this.setLength(safeIndex + 1L);
        }
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        Arrays.fill(this.array, (int)newLength, this.array.length, ScriptRuntime.UNDEFINED);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max(index + 1, this.length()));
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max(index + 1, this.length()));
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max(index + 1, this.length()));
        return this;
    }
    
    @Override
    public ArrayData setEmpty(final int index) {
        this.array[index] = ScriptRuntime.EMPTY;
        return this;
    }
    
    @Override
    public ArrayData setEmpty(final long lo, final long hi) {
        Arrays.fill(this.array, (int)Math.max(lo, 0L), (int)Math.min(hi + 1L, 2147483647L), ScriptRuntime.EMPTY);
        return this;
    }
    
    private Object getElem(final int index) {
        if (this.has(index)) {
            return this.array[index];
        }
        throw new ClassCastException();
    }
    
    private void setElem(final int index, final Object elem) {
        if (this.hasRoomFor(index)) {
            this.array[index] = elem;
            return;
        }
        throw new ClassCastException();
    }
    
    @Override
    public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
        if (returnType.isPrimitive()) {
            return null;
        }
        return this.getContinuousElementGetter(ObjectArrayData.HAS_GET_ELEM, returnType, programPoint);
    }
    
    @Override
    public MethodHandle getElementSetter(final Class<?> elementType) {
        return this.getContinuousElementSetter(ObjectArrayData.SET_ELEM, Object.class);
    }
    
    @Override
    public int getInt(final int index) {
        return JSType.toInt32(this.array[index]);
    }
    
    @Override
    public double getDouble(final int index) {
        return JSType.toNumber(this.array[index]);
    }
    
    @Override
    public Object getObject(final int index) {
        return this.array[index];
    }
    
    @Override
    public boolean has(final int index) {
        return 0 <= index && index < this.length();
    }
    
    @Override
    public ArrayData delete(final int index) {
        this.setEmpty(index);
        return new DeletedRangeArrayFilter(this, index, index);
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        this.setEmpty(fromIndex, toIndex);
        return new DeletedRangeArrayFilter(this, fromIndex, toIndex);
    }
    
    @Override
    public double fastPush(final int arg) {
        return this.fastPush((Object)arg);
    }
    
    @Override
    public double fastPush(final long arg) {
        return this.fastPush((Object)arg);
    }
    
    @Override
    public double fastPush(final double arg) {
        return this.fastPush((Object)arg);
    }
    
    @Override
    public double fastPush(final Object arg) {
        final int len = (int)this.length();
        if (len == this.array.length) {
            this.array = Arrays.copyOf(this.array, ArrayData.nextSize(len));
        }
        this.array[len] = arg;
        return (double)this.increaseLength();
    }
    
    @Override
    public Object fastPopObject() {
        if (this.length() == 0L) {
            return ScriptRuntime.UNDEFINED;
        }
        final int newLength = (int)this.decreaseLength();
        final Object elem = this.array[newLength];
        this.array[newLength] = ScriptRuntime.EMPTY;
        return elem;
    }
    
    @Override
    public Object pop() {
        if (this.length() == 0L) {
            return ScriptRuntime.UNDEFINED;
        }
        final int newLength = (int)this.length() - 1;
        final Object elem = this.array[newLength];
        this.setEmpty(newLength);
        this.setLength(newLength);
        return elem;
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        final long start = (from < 0L) ? (from + this.length()) : from;
        final long newLength = to - start;
        return new ObjectArrayData(Arrays.copyOfRange(this.array, (int)from, (int)to), (int)newLength);
    }
    
    @Override
    public ArrayData push(final boolean strict, final Object item) {
        final long len = this.length();
        final ArrayData newData = this.ensure(len);
        if (newData == this) {
            this.array[(int)len] = item;
            return this;
        }
        return newData.set((int)len, item, strict);
    }
    
    @Override
    public ArrayData fastSplice(final int start, final int removed, final int added) throws UnsupportedOperationException {
        final long oldLength = this.length();
        final long newLength = oldLength - removed + added;
        if (newLength > 131072L && newLength > this.array.length) {
            throw new UnsupportedOperationException();
        }
        final ArrayData returnValue = (removed == 0) ? ObjectArrayData.EMPTY_ARRAY : new ObjectArrayData(Arrays.copyOfRange(this.array, start, start + removed), removed);
        if (newLength != oldLength) {
            Object[] newArray;
            if (newLength > this.array.length) {
                newArray = new Object[ArrayData.nextSize((int)newLength)];
                System.arraycopy(this.array, 0, newArray, 0, start);
            }
            else {
                newArray = this.array;
            }
            System.arraycopy(this.array, start + removed, newArray, start + added, (int)(oldLength - start - removed));
            this.array = newArray;
            this.setLength(newLength);
        }
        return returnValue;
    }
    
    @Override
    public ContinuousArrayData fastConcat(final ContinuousArrayData otherData) {
        final int otherLength = (int)otherData.length();
        final int thisLength = (int)this.length();
        assert otherLength > 0 && thisLength > 0;
        final Object[] otherArray = ((ObjectArrayData)otherData).array;
        final int newLength = otherLength + thisLength;
        final Object[] newArray = new Object[ArrayData.alignUp(newLength)];
        System.arraycopy(this.array, 0, newArray, 0, thisLength);
        System.arraycopy(otherArray, 0, newArray, thisLength, otherLength);
        return new ObjectArrayData(newArray, newLength);
    }
    
    @Override
    public String toString() {
        assert this.length() <= this.array.length : this.length() + " > " + this.array.length;
        return this.getClass().getSimpleName() + ':' + Arrays.toString(Arrays.copyOf(this.array, (int)this.length()));
    }
    
    static {
        HAS_GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), ObjectArrayData.class, "getElem", Object.class, Integer.TYPE).methodHandle();
        SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), ObjectArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Object.class).methodHandle();
    }
}
