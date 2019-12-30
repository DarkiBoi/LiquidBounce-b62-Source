// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import java.lang.invoke.MethodHandle;
import java.nio.IntBuffer;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeUint32Array extends ArrayBufferView
{
    public static final int BYTES_PER_ELEMENT = 4;
    private static PropertyMap $nasgenmap$;
    private static final Factory FACTORY;
    
    public static NativeUint32Array constructor(final boolean newObj, final Object self, final Object... args) {
        return (NativeUint32Array)ArrayBufferView.constructorImpl(newObj, args, NativeUint32Array.FACTORY);
    }
    
    NativeUint32Array(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
        super(buffer, byteOffset, length);
    }
    
    @Override
    protected Factory factory() {
        return NativeUint32Array.FACTORY;
    }
    
    protected static Object set(final Object self, final Object array, final Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }
    
    protected static NativeUint32Array subarray(final Object self, final Object begin, final Object end) {
        return (NativeUint32Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }
    
    @Override
    protected ScriptObject getPrototype(final Global global) {
        return global.getUint32ArrayPrototype();
    }
    
    static {
        FACTORY = new Factory(4) {
            @Override
            public ArrayBufferView construct(final NativeArrayBuffer buffer, final int byteBegin, final int length) {
                return new NativeUint32Array(buffer, byteBegin, length);
            }
            
            @Override
            public Uint32ArrayData createArrayData(final ByteBuffer nb, final int start, final int end) {
                return new Uint32ArrayData(nb.order(ByteOrder.nativeOrder()).asIntBuffer(), start, end);
            }
            
            @Override
            public String getClassName() {
                return "Uint32Array";
            }
        };
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeUint32Array.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static final class Uint32ArrayData extends TypedArrayData<IntBuffer>
    {
        private static final MethodHandle GET_ELEM;
        private static final MethodHandle SET_ELEM;
        
        private Uint32ArrayData(final IntBuffer nb, final int start, final int end) {
            super(((IntBuffer)nb.position(start).limit(end)).slice(), end - start);
        }
        
        @Override
        protected MethodHandle getGetElem() {
            return Uint32ArrayData.GET_ELEM;
        }
        
        @Override
        protected MethodHandle getSetElem() {
            return Uint32ArrayData.SET_ELEM;
        }
        
        @Override
        public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
            if (returnType == Integer.TYPE) {
                return null;
            }
            return this.getContinuousElementGetter(this.getClass(), Uint32ArrayData.GET_ELEM, returnType, programPoint);
        }
        
        private int getRawElem(final int index) {
            try {
                return ((IntBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        private double getElem(final int index) {
            return (double)JSType.toUint32(this.getRawElem(index));
        }
        
        private void setElem(final int index, final int elem) {
            try {
                if (index < ((IntBuffer)this.nb).limit()) {
                    ((IntBuffer)this.nb).put(index, elem);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        @Override
        public boolean isUnsigned() {
            return true;
        }
        
        @Override
        public Class<?> getElementType() {
            return Double.TYPE;
        }
        
        @Override
        public Class<?> getBoxedElementType() {
            return Double.class;
        }
        
        @Override
        public int getInt(final int index) {
            return this.getRawElem(index);
        }
        
        @Override
        public int getIntOptimistic(final int index, final int programPoint) {
            return JSType.toUint32Optimistic(this.getRawElem(index), programPoint);
        }
        
        @Override
        public double getDouble(final int index) {
            return this.getElem(index);
        }
        
        @Override
        public double getDoubleOptimistic(final int index, final int programPoint) {
            return this.getElem(index);
        }
        
        @Override
        public Object getObject(final int index) {
            return this.getElem(index);
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
            GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint32ArrayData.class, "getElem", Double.TYPE, Integer.TYPE).methodHandle();
            SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint32ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
        }
    }
}
