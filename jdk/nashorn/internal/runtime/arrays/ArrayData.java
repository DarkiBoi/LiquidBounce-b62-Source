// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.lang.reflect.Array;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandle;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.JSType;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.codegen.CompilerConstants;

public abstract class ArrayData
{
    protected static final int CHUNK_SIZE = 32;
    public static final ArrayData EMPTY_ARRAY;
    private long length;
    protected static final CompilerConstants.Call THROW_UNWARRANTED;
    
    protected ArrayData(final long length) {
        this.length = length;
    }
    
    public static ArrayData initialArray() {
        return new IntArrayData();
    }
    
    protected static void throwUnwarranted(final ArrayData data, final int programPoint, final int index) {
        throw new UnwarrantedOptimismException(data.getObject(index), programPoint);
    }
    
    protected static int alignUp(final int size) {
        return size + 32 - 1 & 0xFFFFFFE0;
    }
    
    public static ArrayData allocate(final long length) {
        if (length == 0L) {
            return new IntArrayData();
        }
        if (length >= 131072L) {
            return new SparseArrayData(ArrayData.EMPTY_ARRAY, length);
        }
        return new DeletedRangeArrayFilter(new IntArrayData((int)length), 0L, length - 1L);
    }
    
    public static ArrayData allocate(final Object array) {
        final Class<?> clazz = array.getClass();
        if (clazz == int[].class) {
            return new IntArrayData((int[])array, ((int[])array).length);
        }
        if (clazz == double[].class) {
            return new NumberArrayData((double[])array, ((double[])array).length);
        }
        return new ObjectArrayData((Object[])array, ((Object[])array).length);
    }
    
    public static ArrayData allocate(final int[] array) {
        return new IntArrayData(array, array.length);
    }
    
    public static ArrayData allocate(final double[] array) {
        return new NumberArrayData(array, array.length);
    }
    
    public static ArrayData allocate(final Object[] array) {
        return new ObjectArrayData(array, array.length);
    }
    
    public static ArrayData allocate(final ByteBuffer buf) {
        return new ByteBufferArrayData(buf);
    }
    
    public static ArrayData freeze(final ArrayData underlying) {
        return new FrozenArrayFilter(underlying);
    }
    
    public static ArrayData seal(final ArrayData underlying) {
        return new SealedArrayFilter(underlying);
    }
    
    public static ArrayData preventExtension(final ArrayData underlying) {
        return new NonExtensibleArrayFilter(underlying);
    }
    
    public static ArrayData setIsLengthNotWritable(final ArrayData underlying) {
        return new LengthNotWritableFilter(underlying);
    }
    
    public final long length() {
        return this.length;
    }
    
    public abstract ArrayData copy();
    
    public abstract Object[] asObjectArray();
    
    public Object asArrayOfType(final Class<?> componentType) {
        return JSType.convertArray(this.asObjectArray(), componentType);
    }
    
    public void setLength(final long length) {
        this.length = length;
    }
    
    protected final long increaseLength() {
        return ++this.length;
    }
    
    protected final long decreaseLength() {
        return --this.length;
    }
    
    public abstract ArrayData shiftLeft(final int p0);
    
    public abstract ArrayData shiftRight(final int p0);
    
    public abstract ArrayData ensure(final long p0);
    
    public abstract ArrayData shrink(final long p0);
    
    public abstract ArrayData set(final int p0, final Object p1, final boolean p2);
    
    public abstract ArrayData set(final int p0, final int p1, final boolean p2);
    
    public abstract ArrayData set(final int p0, final double p1, final boolean p2);
    
    public ArrayData setEmpty(final int index) {
        return this;
    }
    
    public ArrayData setEmpty(final long lo, final long hi) {
        return this;
    }
    
    public abstract int getInt(final int p0);
    
    public Type getOptimisticType() {
        return Type.OBJECT;
    }
    
    public int getIntOptimistic(final int index, final int programPoint) {
        throw new UnwarrantedOptimismException(this.getObject(index), programPoint, this.getOptimisticType());
    }
    
    public abstract double getDouble(final int p0);
    
    public double getDoubleOptimistic(final int index, final int programPoint) {
        throw new UnwarrantedOptimismException(this.getObject(index), programPoint, this.getOptimisticType());
    }
    
    public abstract Object getObject(final int p0);
    
    public abstract boolean has(final int p0);
    
    public boolean canDelete(final int index, final boolean strict) {
        return true;
    }
    
    public boolean canDelete(final long longIndex, final boolean strict) {
        return true;
    }
    
    public final ArrayData safeDelete(final long fromIndex, final long toIndex, final boolean strict) {
        if (fromIndex <= toIndex && this.canDelete(fromIndex, strict)) {
            return this.delete(fromIndex, toIndex);
        }
        return this;
    }
    
    public PropertyDescriptor getDescriptor(final Global global, final int index) {
        return global.newDataDescriptor(this.getObject(index), true, true, true);
    }
    
    public abstract ArrayData delete(final int p0);
    
    public abstract ArrayData delete(final long p0, final long p1);
    
    public abstract ArrayData convert(final Class<?> p0);
    
    public ArrayData push(final boolean strict, final Object... items) {
        if (items.length == 0) {
            return this;
        }
        final Class<?> widest = widestType(items);
        ArrayData newData = this.convert(widest);
        long pos = newData.length;
        for (final Object item : items) {
            newData = newData.ensure(pos);
            newData.set((int)(pos++), item, strict);
        }
        return newData;
    }
    
    public ArrayData push(final boolean strict, final Object item) {
        return this.push(strict, new Object[] { item });
    }
    
    public abstract Object pop();
    
    public abstract ArrayData slice(final long p0, final long p1);
    
    public ArrayData fastSplice(final int start, final int removed, final int added) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    static Class<?> widestType(final Object... items) {
        assert items.length > 0;
        Class<?> widest = Integer.class;
        for (final Object item : items) {
            if (item == null) {
                return Object.class;
            }
            final Class<?> itemClass = item.getClass();
            if (itemClass == Double.class || itemClass == Float.class || itemClass == Long.class) {
                if (widest == Integer.class) {
                    widest = Double.class;
                }
            }
            else if (itemClass != Integer.class && itemClass != Short.class && itemClass != Byte.class) {
                return Object.class;
            }
        }
        return widest;
    }
    
    protected List<Long> computeIteratorKeys() {
        final List<Long> keys = new ArrayList<Long>();
        for (long len = this.length(), i = 0L; i < len; i = this.nextIndex(i)) {
            if (this.has((int)i)) {
                keys.add(i);
            }
        }
        return keys;
    }
    
    public Iterator<Long> indexIterator() {
        return this.computeIteratorKeys().iterator();
    }
    
    public static int nextSize(final int size) {
        return alignUp(size + 1) * 2;
    }
    
    long nextIndex(final long index) {
        return index + 1L;
    }
    
    static Object invoke(final MethodHandle mh, final Object arg) {
        try {
            return mh.invoke(arg);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    public GuardedInvocation findFastCallMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        return null;
    }
    
    public GuardedInvocation findFastGetMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
        return null;
    }
    
    public GuardedInvocation findFastGetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        return null;
    }
    
    public GuardedInvocation findFastSetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        return null;
    }
    
    static {
        EMPTY_ARRAY = new UntouchedArrayData();
        THROW_UNWARRANTED = CompilerConstants.staticCall(MethodHandles.lookup(), ArrayData.class, "throwUnwarranted", Void.TYPE, ArrayData.class, Integer.TYPE, Integer.TYPE);
    }
    
    private static class UntouchedArrayData extends ContinuousArrayData
    {
        private UntouchedArrayData() {
            super(0L);
        }
        
        private ArrayData toRealArrayData() {
            return new IntArrayData(0);
        }
        
        private ArrayData toRealArrayData(final int index) {
            final IntArrayData newData = new IntArrayData(index + 1);
            return new DeletedRangeArrayFilter(newData, 0L, index);
        }
        
        @Override
        public ContinuousArrayData copy() {
            assert this.length() == 0L;
            return this;
        }
        
        @Override
        public Object asArrayOfType(final Class<?> componentType) {
            return Array.newInstance(componentType, 0);
        }
        
        @Override
        public Object[] asObjectArray() {
            return ScriptRuntime.EMPTY_ARRAY;
        }
        
        @Override
        public ArrayData ensure(final long safeIndex) {
            assert safeIndex >= 0L;
            if (safeIndex >= 131072L) {
                return new SparseArrayData(this, safeIndex + 1L);
            }
            return this.toRealArrayData((int)safeIndex);
        }
        
        @Override
        public ArrayData convert(final Class<?> type) {
            return this.toRealArrayData().convert(type);
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
        public ArrayData shiftLeft(final int by) {
            return this;
        }
        
        @Override
        public ArrayData shiftRight(final int by) {
            return this;
        }
        
        @Override
        public ArrayData shrink(final long newLength) {
            return this;
        }
        
        @Override
        public ArrayData set(final int index, final Object value, final boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }
        
        @Override
        public ArrayData set(final int index, final int value, final boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }
        
        @Override
        public ArrayData set(final int index, final double value, final boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }
        
        @Override
        public int getInt(final int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        @Override
        public double getDouble(final int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        @Override
        public Object getObject(final int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        @Override
        public boolean has(final int index) {
            return false;
        }
        
        @Override
        public Object pop() {
            return ScriptRuntime.UNDEFINED;
        }
        
        @Override
        public ArrayData push(final boolean strict, final Object item) {
            return this.toRealArrayData().push(strict, item);
        }
        
        @Override
        public ArrayData slice(final long from, final long to) {
            return this;
        }
        
        @Override
        public ContinuousArrayData fastConcat(final ContinuousArrayData otherData) {
            return otherData.copy();
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
        
        @Override
        public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
            return null;
        }
        
        @Override
        public MethodHandle getElementSetter(final Class<?> elementType) {
            return null;
        }
        
        @Override
        public Class<?> getElementType() {
            return Integer.TYPE;
        }
        
        @Override
        public Class<?> getBoxedElementType() {
            return Integer.class;
        }
    }
}
