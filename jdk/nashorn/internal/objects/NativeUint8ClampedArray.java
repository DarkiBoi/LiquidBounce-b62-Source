// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeUint8ClampedArray extends ArrayBufferView
{
    public static final int BYTES_PER_ELEMENT = 1;
    private static PropertyMap $nasgenmap$;
    private static final Factory FACTORY;
    
    public static NativeUint8ClampedArray constructor(final boolean newObj, final Object self, final Object... args) {
        return (NativeUint8ClampedArray)ArrayBufferView.constructorImpl(newObj, args, NativeUint8ClampedArray.FACTORY);
    }
    
    NativeUint8ClampedArray(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
        super(buffer, byteOffset, length);
    }
    
    @Override
    protected Factory factory() {
        return NativeUint8ClampedArray.FACTORY;
    }
    
    protected static Object set(final Object self, final Object array, final Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }
    
    protected static NativeUint8ClampedArray subarray(final Object self, final Object begin, final Object end) {
        return (NativeUint8ClampedArray)ArrayBufferView.subarrayImpl(self, begin, end);
    }
    
    @Override
    protected ScriptObject getPrototype(final Global global) {
        return global.getUint8ClampedArrayPrototype();
    }
    
    static {
        FACTORY = new Factory(1) {
            @Override
            public ArrayBufferView construct(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
                return new NativeUint8ClampedArray(buffer, byteOffset, length);
            }
            
            @Override
            public Uint8ClampedArrayData createArrayData(final ByteBuffer nb, final int start, final int end) {
                return new Uint8ClampedArrayData(nb, start, end);
            }
            
            @Override
            public String getClassName() {
                return "Uint8ClampedArray";
            }
        };
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeUint8ClampedArray.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static final class Uint8ClampedArrayData extends TypedArrayData<ByteBuffer>
    {
        private static final MethodHandle GET_ELEM;
        private static final MethodHandle SET_ELEM;
        private static final MethodHandle RINT_D;
        private static final MethodHandle RINT_O;
        private static final MethodHandle CLAMP_LONG;
        
        private Uint8ClampedArrayData(final ByteBuffer nb, final int start, final int end) {
            super(((ByteBuffer)nb.position(start).limit(end)).slice(), end - start);
        }
        
        @Override
        protected MethodHandle getGetElem() {
            return Uint8ClampedArrayData.GET_ELEM;
        }
        
        @Override
        protected MethodHandle getSetElem() {
            return Uint8ClampedArrayData.SET_ELEM;
        }
        
        @Override
        public Class<?> getElementType() {
            return Integer.TYPE;
        }
        
        @Override
        public Class<?> getBoxedElementType() {
            return Integer.TYPE;
        }
        
        private int getElem(final int index) {
            try {
                return ((ByteBuffer)this.nb).get(index) & 0xFF;
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        @Override
        public MethodHandle getElementSetter(final Class<?> elementType) {
            final MethodHandle setter = super.getElementSetter(elementType);
            if (setter != null) {
                if (elementType == Object.class) {
                    return Lookup.MH.filterArguments(setter, 2, Uint8ClampedArrayData.RINT_O);
                }
                if (elementType == Double.TYPE) {
                    return Lookup.MH.filterArguments(setter, 2, Uint8ClampedArrayData.RINT_D);
                }
                if (elementType == Long.TYPE) {
                    return Lookup.MH.filterArguments(setter, 2, Uint8ClampedArrayData.CLAMP_LONG);
                }
            }
            return setter;
        }
        
        private void setElem(final int index, final int elem) {
            try {
                if (index < ((ByteBuffer)this.nb).limit()) {
                    byte clamped;
                    if ((elem & 0xFFFFFF00) == 0x0) {
                        clamped = (byte)elem;
                    }
                    else {
                        clamped = (byte)((elem < 0) ? 0 : -1);
                    }
                    ((ByteBuffer)this.nb).put(index, clamped);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        @Override
        public boolean isClamped() {
            return true;
        }
        
        @Override
        public boolean isUnsigned() {
            return true;
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
            return this.set(index, JSType.toNumber(value), strict);
        }
        
        @Override
        public ArrayData set(final int index, final int value, final boolean strict) {
            this.setElem(index, value);
            return this;
        }
        
        @Override
        public ArrayData set(final int index, final double value, final boolean strict) {
            return this.set(index, rint(value), strict);
        }
        
        private static double rint(final double rint) {
            return (int)Math.rint(rint);
        }
        
        private static Object rint(final Object rint) {
            return rint(JSType.toNumber(rint));
        }
        
        private static long clampLong(final long l) {
            if (l < 0L) {
                return 0L;
            }
            if (l > 255L) {
                return 255L;
            }
            return l;
        }
        
        static {
            GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
            SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
            RINT_D = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "rint", Double.TYPE, Double.TYPE).methodHandle();
            RINT_O = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "rint", Object.class, Object.class).methodHandle();
            CLAMP_LONG = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "clampLong", Long.TYPE, Long.TYPE).methodHandle();
        }
    }
}
