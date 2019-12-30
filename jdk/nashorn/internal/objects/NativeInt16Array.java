// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import java.lang.invoke.MethodHandle;
import java.nio.ShortBuffer;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeInt16Array extends ArrayBufferView
{
    private static PropertyMap $nasgenmap$;
    public static final int BYTES_PER_ELEMENT = 2;
    private static final Factory FACTORY;
    
    public static NativeInt16Array constructor(final boolean newObj, final Object self, final Object... args) {
        return (NativeInt16Array)ArrayBufferView.constructorImpl(newObj, args, NativeInt16Array.FACTORY);
    }
    
    NativeInt16Array(final NativeArrayBuffer buffer, final int byteOffset, final int byteLength) {
        super(buffer, byteOffset, byteLength);
    }
    
    @Override
    protected Factory factory() {
        return NativeInt16Array.FACTORY;
    }
    
    protected static Object set(final Object self, final Object array, final Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }
    
    protected static NativeInt16Array subarray(final Object self, final Object begin, final Object end) {
        return (NativeInt16Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }
    
    @Override
    protected ScriptObject getPrototype(final Global global) {
        return global.getInt16ArrayPrototype();
    }
    
    static {
        FACTORY = new Factory(2) {
            @Override
            public ArrayBufferView construct(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
                return new NativeInt16Array(buffer, byteOffset, length);
            }
            
            @Override
            public Int16ArrayData createArrayData(final ByteBuffer nb, final int start, final int end) {
                return new Int16ArrayData(nb.asShortBuffer(), start, end);
            }
            
            @Override
            public String getClassName() {
                return "Int16Array";
            }
        };
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeInt16Array.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static final class Int16ArrayData extends TypedArrayData<ShortBuffer>
    {
        private static final MethodHandle GET_ELEM;
        private static final MethodHandle SET_ELEM;
        
        private Int16ArrayData(final ShortBuffer nb, final int start, final int end) {
            super(((ShortBuffer)nb.position(start).limit(end)).slice(), end - start);
        }
        
        @Override
        protected MethodHandle getGetElem() {
            return Int16ArrayData.GET_ELEM;
        }
        
        @Override
        protected MethodHandle getSetElem() {
            return Int16ArrayData.SET_ELEM;
        }
        
        @Override
        public Class<?> getElementType() {
            return Integer.TYPE;
        }
        
        @Override
        public Class<?> getBoxedElementType() {
            return Integer.class;
        }
        
        private int getElem(final int index) {
            try {
                return ((ShortBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        private void setElem(final int index, final int elem) {
            try {
                if (index < ((ShortBuffer)this.nb).limit()) {
                    ((ShortBuffer)this.nb).put(index, (short)elem);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        @Override
        public int getInt(final int index) {
            return this.getElem(index);
        }
        
        @Override
        public int getIntOptimistic(final int index, final int programPoint) {
            return this.getElem(index);
        }
        
        @Override
        public double getDouble(final int index) {
            return this.getInt(index);
        }
        
        @Override
        public double getDoubleOptimistic(final int index, final int programPoint) {
            return this.getElem(index);
        }
        
        @Override
        public Object getObject(final int index) {
            return this.getInt(index);
        }
        
        @Override
        public ArrayData set(final int index, final Object value, final boolean strict) {
            return this.set(index, JSType.toInt32(value), strict);
        }
        
        @Override
        public ArrayData set(final int index, final int value, final boolean strict) {
            this.setElem(index, value);
            return this;
        }
        
        @Override
        public ArrayData set(final int index, final double value, final boolean strict) {
            return this.set(index, (int)value, strict);
        }
        
        static {
            GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int16ArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
            SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int16ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
        }
    }
}
