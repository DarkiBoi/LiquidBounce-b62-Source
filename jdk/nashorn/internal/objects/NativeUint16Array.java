// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import java.lang.invoke.MethodHandle;
import java.nio.CharBuffer;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.PropertyMap;

public final class NativeUint16Array extends ArrayBufferView
{
    public static final int BYTES_PER_ELEMENT = 2;
    private static PropertyMap $nasgenmap$;
    private static final Factory FACTORY;
    
    public static NativeUint16Array constructor(final boolean newObj, final Object self, final Object... args) {
        return (NativeUint16Array)ArrayBufferView.constructorImpl(newObj, args, NativeUint16Array.FACTORY);
    }
    
    NativeUint16Array(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
        super(buffer, byteOffset, length);
    }
    
    @Override
    protected Factory factory() {
        return NativeUint16Array.FACTORY;
    }
    
    protected static Object set(final Object self, final Object array, final Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }
    
    protected static NativeUint16Array subarray(final Object self, final Object begin, final Object end) {
        return (NativeUint16Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }
    
    @Override
    protected ScriptObject getPrototype(final Global global) {
        return global.getUint16ArrayPrototype();
    }
    
    static {
        FACTORY = new Factory(2) {
            @Override
            public ArrayBufferView construct(final NativeArrayBuffer buffer, final int byteOffset, final int length) {
                return new NativeUint16Array(buffer, byteOffset, length);
            }
            
            @Override
            public Uint16ArrayData createArrayData(final ByteBuffer nb, final int start, final int end) {
                return new Uint16ArrayData(nb.asCharBuffer(), start, end);
            }
            
            @Override
            public String getClassName() {
                return "Uint16Array";
            }
        };
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeUint16Array.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static final class Uint16ArrayData extends TypedArrayData<CharBuffer>
    {
        private static final MethodHandle GET_ELEM;
        private static final MethodHandle SET_ELEM;
        
        private Uint16ArrayData(final CharBuffer nb, final int start, final int end) {
            super(((CharBuffer)nb.position(start).limit(end)).slice(), end - start);
        }
        
        @Override
        protected MethodHandle getGetElem() {
            return Uint16ArrayData.GET_ELEM;
        }
        
        @Override
        protected MethodHandle getSetElem() {
            return Uint16ArrayData.SET_ELEM;
        }
        
        private int getElem(final int index) {
            try {
                return ((CharBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }
        
        private void setElem(final int index, final int elem) {
            try {
                if (index < ((CharBuffer)this.nb).limit()) {
                    ((CharBuffer)this.nb).put(index, (char)elem);
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
            return Integer.TYPE;
        }
        
        @Override
        public Class<?> getBoxedElementType() {
            return Integer.class;
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
            GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint16ArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
            SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint16ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
        }
    }
}
