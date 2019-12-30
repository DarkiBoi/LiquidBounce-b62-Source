// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.internal.dynalink.support.TypeUtilities;
import java.util.Arrays;
import java.lang.invoke.MethodHandle;

final class NumberArrayData extends ContinuousArrayData implements NumericElements
{
    private double[] array;
    private static final MethodHandle HAS_GET_ELEM;
    private static final MethodHandle SET_ELEM;
    
    NumberArrayData(final double[] array, final int length) {
        super(length);
        assert array.length >= length;
        this.array = array;
    }
    
    @Override
    public final Class<?> getElementType() {
        return Double.TYPE;
    }
    
    @Override
    public final Class<?> getBoxedElementType() {
        return Double.class;
    }
    
    @Override
    public final int getElementWeight() {
        return 3;
    }
    
    @Override
    public final ContinuousArrayData widest(final ContinuousArrayData otherData) {
        return (otherData instanceof IntOrLongElements) ? this : otherData;
    }
    
    @Override
    public NumberArrayData copy() {
        return new NumberArrayData(this.array.clone(), (int)this.length());
    }
    
    @Override
    public Object[] asObjectArray() {
        return this.toObjectArray(true);
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
    
    @Override
    public Object asArrayOfType(final Class<?> componentType) {
        if (componentType == Double.TYPE) {
            final int len = (int)this.length();
            return (this.array.length == len) ? this.array.clone() : Arrays.copyOf(this.array, len);
        }
        return super.asArrayOfType(componentType);
    }
    
    private static boolean canWiden(final Class<?> type) {
        return TypeUtilities.isWrapperType(type) && type != Boolean.class && type != Character.class;
    }
    
    @Override
    public ContinuousArrayData convert(final Class<?> type) {
        if (!canWiden(type)) {
            final int len = (int)this.length();
            return new ObjectArrayData(this.toObjectArray(false), len);
        }
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
        Arrays.fill(this.array, (int)newLength, this.array.length, 0.0);
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (value instanceof Double || (value != null && canWiden(value.getClass()))) {
            return this.set(index, ((Number)value).doubleValue(), strict);
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
        this.array[index] = value;
        this.setLength(Math.max(index + 1, this.length()));
        return this;
    }
    
    private double getElem(final int index) {
        if (this.has(index)) {
            return this.array[index];
        }
        throw new ClassCastException();
    }
    
    private void setElem(final int index, final double elem) {
        if (this.hasRoomFor(index)) {
            this.array[index] = elem;
            return;
        }
        throw new ClassCastException();
    }
    
    @Override
    public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
        if (returnType == Integer.TYPE) {
            return null;
        }
        return this.getContinuousElementGetter(NumberArrayData.HAS_GET_ELEM, returnType, programPoint);
    }
    
    @Override
    public MethodHandle getElementSetter(final Class<?> elementType) {
        return elementType.isPrimitive() ? this.getContinuousElementSetter(Lookup.MH.asType(NumberArrayData.SET_ELEM, NumberArrayData.SET_ELEM.type().changeParameterType(2, elementType)), elementType) : null;
    }
    
    @Override
    public int getInt(final int index) {
        return JSType.toInt32(this.array[index]);
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
        final double elem = this.array[newLength];
        this.array[newLength] = 0.0;
        this.setLength(newLength);
        return elem;
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        final long start = (from < 0L) ? (from + this.length()) : from;
        final long newLength = to - start;
        return new NumberArrayData(Arrays.copyOfRange(this.array, (int)from, (int)to), (int)newLength);
    }
    
    @Override
    public ArrayData fastSplice(final int start, final int removed, final int added) throws UnsupportedOperationException {
        final long oldLength = this.length();
        final long newLength = oldLength - removed + added;
        if (newLength > 131072L && newLength > this.array.length) {
            throw new UnsupportedOperationException();
        }
        final ArrayData returnValue = (removed == 0) ? NumberArrayData.EMPTY_ARRAY : new NumberArrayData(Arrays.copyOfRange(this.array, start, start + removed), removed);
        if (newLength != oldLength) {
            double[] newArray;
            if (newLength > this.array.length) {
                newArray = new double[ArrayData.nextSize((int)newLength)];
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
        return this.fastPush((double)arg);
    }
    
    @Override
    public double fastPush(final long arg) {
        return this.fastPush((double)arg);
    }
    
    @Override
    public double fastPush(final double arg) {
        final int len = (int)this.length();
        if (len == this.array.length) {
            this.array = Arrays.copyOf(this.array, ArrayData.nextSize(len));
        }
        this.array[len] = arg;
        return (double)this.increaseLength();
    }
    
    @Override
    public double fastPopDouble() {
        if (this.length() == 0L) {
            throw new ClassCastException();
        }
        final int newLength = (int)this.decreaseLength();
        final double elem = this.array[newLength];
        this.array[newLength] = 0.0;
        return elem;
    }
    
    @Override
    public Object fastPopObject() {
        return this.fastPopDouble();
    }
    
    @Override
    public ContinuousArrayData fastConcat(final ContinuousArrayData otherData) {
        final int otherLength = (int)otherData.length();
        final int thisLength = (int)this.length();
        assert otherLength > 0 && thisLength > 0;
        final double[] otherArray = ((NumberArrayData)otherData).array;
        final int newLength = otherLength + thisLength;
        final double[] newArray = new double[ArrayData.alignUp(newLength)];
        System.arraycopy(this.array, 0, newArray, 0, thisLength);
        System.arraycopy(otherArray, 0, newArray, thisLength, otherLength);
        return new NumberArrayData(newArray, newLength);
    }
    
    @Override
    public String toString() {
        assert this.length() <= this.array.length : this.length() + " > " + this.array.length;
        return this.getClass().getSimpleName() + ':' + Arrays.toString(Arrays.copyOf(this.array, (int)this.length()));
    }
    
    static {
        HAS_GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), NumberArrayData.class, "getElem", Double.TYPE, Integer.TYPE).methodHandle();
        SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), NumberArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Double.TYPE).methodHandle();
    }
}
