// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import java.util.Arrays;
import java.lang.invoke.MethodHandle;

final class IntArrayData extends ContinuousArrayData implements IntElements
{
    private int[] array;
    private static final MethodHandle HAS_GET_ELEM;
    private static final MethodHandle SET_ELEM;
    
    IntArrayData() {
        this(new int[32], 0);
    }
    
    IntArrayData(final int length) {
        super(length);
        this.array = new int[ArrayData.nextSize(length)];
    }
    
    IntArrayData(final int[] array, final int length) {
        super(length);
        assert array.length >= length;
        this.array = array;
    }
    
    @Override
    public final Class<?> getElementType() {
        return Integer.TYPE;
    }
    
    @Override
    public final Class<?> getBoxedElementType() {
        return Integer.class;
    }
    
    @Override
    public final int getElementWeight() {
        return 1;
    }
    
    @Override
    public final ContinuousArrayData widest(final ContinuousArrayData otherData) {
        return otherData;
    }
    
    @Override
    public Object[] asObjectArray() {
        return this.toObjectArray(true);
    }
    
    private int getElem(final int index) {
        if (this.has(index)) {
            return this.array[index];
        }
        throw new ClassCastException();
    }
    
    private void setElem(final int index, final int elem) {
        if (this.hasRoomFor(index)) {
            this.array[index] = elem;
            return;
        }
        throw new ClassCastException();
    }
    
    @Override
    public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
        return this.getContinuousElementGetter(IntArrayData.HAS_GET_ELEM, returnType, programPoint);
    }
    
    @Override
    public MethodHandle getElementSetter(final Class<?> elementType) {
        return (elementType == Integer.TYPE) ? this.getContinuousElementSetter(IntArrayData.SET_ELEM, elementType) : null;
    }
    
    @Override
    public IntArrayData copy() {
        return new IntArrayData(this.array.clone(), (int)this.length());
    }
    
    @Override
    public Object asArrayOfType(final Class<?> componentType) {
        if (componentType == Integer.TYPE) {
            final int len = (int)this.length();
            return (this.array.length == len) ? this.array.clone() : Arrays.copyOf(this.array, len);
        }
        return super.asArrayOfType(componentType);
    }
    
    private Object[] toObjectArray(final boolean trim) {
        assert this.length() <= this.array.length : "length exceeds internal array size";
        final int len = (int)this.length();
        final Object[] oarray = new Object[trim ? len : this.array.length];
        for (int index = 0; index < len; ++index) {
            oarray[index] = this.array[index];
        }
        return oarray;
    }
    
    private double[] toDoubleArray() {
        assert this.length() <= this.array.length : "length exceeds internal array size";
        final int len = (int)this.length();
        final double[] darray = new double[this.array.length];
        for (int index = 0; index < len; ++index) {
            darray[index] = this.array[index];
        }
        return darray;
    }
    
    private NumberArrayData convertToDouble() {
        return new NumberArrayData(this.toDoubleArray(), (int)this.length());
    }
    
    private ObjectArrayData convertToObject() {
        return new ObjectArrayData(this.toObjectArray(false), (int)this.length());
    }
    
    @Override
    public ArrayData convert(final Class<?> type) {
        if (type == Integer.class || type == Byte.class || type == Short.class) {
            return this;
        }
        if (type == Double.class || type == Float.class) {
            return this.convertToDouble();
        }
        return this.convertToObject();
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
        Arrays.fill(this.array, (int)newLength, this.array.length, 0);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (JSType.isRepresentableAsInt(value)) {
            return this.set(index, JSType.toInt32(value), strict);
        }
        if (value == ScriptRuntime.UNDEFINED) {
            return new UndefinedArrayFilter(this).set(index, value, strict);
        }
        final ArrayData newData = this.convert((value == null) ? Object.class : value.getClass());
        return newData.set(index, value, strict);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max(index + 1, this.length()));
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        if (JSType.isRepresentableAsInt(value)) {
            this.array[index] = (int)value;
            this.setLength(Math.max(index + 1, this.length()));
            return this;
        }
        return this.convert(Double.class).set(index, value, strict);
    }
    
    @Override
    public int getInt(final int index) {
        return this.array[index];
    }
    
    @Override
    public int getIntOptimistic(final int index, final int programPoint) {
        return this.array[index];
    }
    
    @Override
    public double getDouble(final int index) {
        return this.array[index];
    }
    
    @Override
    public double getDoubleOptimistic(final int index, final int programPoint) {
        return this.array[index];
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
        return new DeletedRangeArrayFilter(this, index, index);
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        return new DeletedRangeArrayFilter(this, fromIndex, toIndex);
    }
    
    @Override
    public Object pop() {
        final int len = (int)this.length();
        if (len == 0) {
            return ScriptRuntime.UNDEFINED;
        }
        final int newLength = len - 1;
        final int elem = this.array[newLength];
        this.array[newLength] = 0;
        this.setLength(newLength);
        return elem;
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        return new IntArrayData(Arrays.copyOfRange(this.array, (int)from, (int)to), (int)(to - ((from < 0L) ? (from + this.length()) : from)));
    }
    
    @Override
    public ArrayData fastSplice(final int start, final int removed, final int added) throws UnsupportedOperationException {
        final long oldLength = this.length();
        final long newLength = oldLength - removed + added;
        if (newLength > 131072L && newLength > this.array.length) {
            throw new UnsupportedOperationException();
        }
        final ArrayData returnValue = (removed == 0) ? IntArrayData.EMPTY_ARRAY : new IntArrayData(Arrays.copyOfRange(this.array, start, start + removed), removed);
        if (newLength != oldLength) {
            int[] newArray;
            if (newLength > this.array.length) {
                newArray = new int[ArrayData.nextSize((int)newLength)];
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
    public double fastPush(final int arg) {
        final int len = (int)this.length();
        if (len == this.array.length) {
            this.array = Arrays.copyOf(this.array, ArrayData.nextSize(len));
        }
        this.array[len] = arg;
        return (double)this.increaseLength();
    }
    
    @Override
    public int fastPopInt() {
        if (this.length() == 0L) {
            throw new ClassCastException();
        }
        final int newLength = (int)this.decreaseLength();
        final int elem = this.array[newLength];
        this.array[newLength] = 0;
        return elem;
    }
    
    @Override
    public double fastPopDouble() {
        return this.fastPopInt();
    }
    
    @Override
    public Object fastPopObject() {
        return this.fastPopInt();
    }
    
    @Override
    public ContinuousArrayData fastConcat(final ContinuousArrayData otherData) {
        final int otherLength = (int)otherData.length();
        final int thisLength = (int)this.length();
        assert otherLength > 0 && thisLength > 0;
        final int[] otherArray = ((IntArrayData)otherData).array;
        final int newLength = otherLength + thisLength;
        final int[] newArray = new int[ArrayData.alignUp(newLength)];
        System.arraycopy(this.array, 0, newArray, 0, thisLength);
        System.arraycopy(otherArray, 0, newArray, thisLength, otherLength);
        return new IntArrayData(newArray, newLength);
    }
    
    @Override
    public String toString() {
        assert this.length() <= this.array.length : this.length() + " > " + this.array.length;
        return this.getClass().getSimpleName() + ':' + Arrays.toString(Arrays.copyOf(this.array, (int)this.length()));
    }
    
    static {
        HAS_GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), IntArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
        SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), IntArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
    }
}
