// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import java.lang.invoke.MethodHandle;
import java.nio.FloatBuffer;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeFloat32Array extends ArrayBufferView
{
    public static final int BYTES_PER_ELEMENT = 4;
    private static PropertyMap $nasgenmap$;
    private static final Factory FACTORY;
    
    public static NativeFloat32Array constructor(final boolean newObj, final Object self, final Object... args) {
        return (NativeFloat32Array)ArrayBufferView.constructorImpl(newObj, args, NativeFloat32Array.FACTORY);
    }
    
    NativeFloat32Array(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
        super(buffer, byteOffset, length);
    }
    
    @Override
    protected Factory factory() {
        return NativeFloat32Array.FACTORY;
    }
    
    @Override
    protected boolean isFloatArray() {
        return true;
    }
    
    protected static Object set(final Object self, final Object array, final Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }
    
    protected static NativeFloat32Array subarray(final Object self, final Object begin, final Object end) {
        return (NativeFloat32Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }
    
    @Override
    protected ScriptObject getPrototype(final Global global) {
        return global.getFloat32ArrayPrototype();
    }
    
    static {
        FACTORY = new Factory(4) {
            @Override
            public ArrayBufferView construct(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
                return new NativeFloat32Array(buffer, byteOffset, length);
            }
            
            @Override
            public Float32ArrayData createArrayData(final ByteBuffer nb, final int start, final int end) {
                return new Float32ArrayData(nb.asFloatBuffer(), start, end);
            }
            
            @Override
            public String getClassName() {
                return "Float32Array";
            }
        };
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeFloat32Array.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static final class Float32ArrayData extends TypedArrayData<FloatBuffer>
    {
        private static final MethodHandle GET_ELEM;
        private static final MethodHandle SET_ELEM;
        
        private Float32ArrayData(final FloatBuffer nb, final int start, final int end) {
            super(((FloatBuffer)nb.position(start).limit(end)).slice(), end - start);
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
        protected MethodHandle getGetElem() {
            return Float32ArrayData.GET_ELEM;
        }
        
        @Override
        protected MethodHandle getSetElem() {
            return Float32ArrayData.SET_ELEM;
        }
        
        private double getElem(final int index) {
            try {
                return ((FloatBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        private void setElem(final int index, final double elem) {
            try {
                if (index < ((FloatBuffer)this.nb).limit()) {
                    ((FloatBuffer)this.nb).put(index, (float)elem);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        @Override
        public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
            if (returnType == Integer.TYPE) {
                return null;
            }
            return this.getContinuousElementGetter(this.getClass(), Float32ArrayData.GET_ELEM, returnType, programPoint);
        }
        
        @Override
        public int getInt(final int index) {
            return (int)this.getDouble(index);
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
            return this.getDouble(index);
        }
        
        @Override
        public ArrayData set(final int index, final Object value, final boolean strict) {
            return this.set(index, JSType.toNumber(value), strict);
        }
        
        @Override
        public ArrayData set(final int index, final int value, final boolean strict) {
            return this.set(index, (double)value, strict);
        }
        
        @Override
        public ArrayData set(final int index, final double value, final boolean strict) {
            this.setElem(index, value);
            return this;
        }
        
        static {
            GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Float32ArrayData.class, "getElem", Double.TYPE, Integer.TYPE).methodHandle();
            SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Float32ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Double.TYPE).methodHandle();
        }
    }
}
