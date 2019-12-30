// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class ArrayBufferView extends ScriptObject
{
    private final NativeArrayBuffer buffer;
    private final int byteOffset;
    private static PropertyMap $nasgenmap$;
    
    private ArrayBufferView(final NativeArrayBuffer buffer, final int byteOffset, final int elementLength, final Global global) {
        super(ArrayBufferView.$nasgenmap$);
        final int bytesPerElement = this.bytesPerElement();
        checkConstructorArgs(buffer.getByteLength(), bytesPerElement, byteOffset, elementLength);
        this.setProto(this.getPrototype(global));
        this.buffer = buffer;
        this.byteOffset = byteOffset;
        assert byteOffset % bytesPerElement == 0;
        final int start = byteOffset / bytesPerElement;
        final ByteBuffer newNioBuffer = buffer.getNioBuffer().duplicate().order(ByteOrder.nativeOrder());
        final ArrayData data = this.factory().createArrayData(newNioBuffer, start, start + elementLength);
        this.setArray(data);
    }
    
    protected ArrayBufferView(final NativeArrayBuffer buffer, final int byteOffset, final int elementLength) {
        this(buffer, byteOffset, elementLength, Global.instance());
    }
    
    private static void checkConstructorArgs(final int byteLength, final int bytesPerElement, final int byteOffset, final int elementLength) {
        if (byteOffset < 0 || elementLength < 0) {
            throw new RuntimeException("byteOffset or length must not be negative, byteOffset=" + byteOffset + ", elementLength=" + elementLength + ", bytesPerElement=" + bytesPerElement);
        }
        if (byteOffset + elementLength * bytesPerElement > byteLength) {
            throw new RuntimeException("byteOffset + byteLength out of range, byteOffset=" + byteOffset + ", elementLength=" + elementLength + ", bytesPerElement=" + bytesPerElement);
        }
        if (byteOffset % bytesPerElement != 0) {
            throw new RuntimeException("byteOffset must be a multiple of the element size, byteOffset=" + byteOffset + " bytesPerElement=" + bytesPerElement);
        }
    }
    
    private int bytesPerElement() {
        return this.factory().bytesPerElement;
    }
    
    public static Object buffer(final Object self) {
        return ((ArrayBufferView)self).buffer;
    }
    
    public static int byteOffset(final Object self) {
        return ((ArrayBufferView)self).byteOffset;
    }
    
    public static int byteLength(final Object self) {
        final ArrayBufferView view = (ArrayBufferView)self;
        return ((TypedArrayData)view.getArray()).getElementLength() * view.bytesPerElement();
    }
    
    public static int length(final Object self) {
        return ((ArrayBufferView)self).elementLength();
    }
    
    @Override
    public final Object getLength() {
        return this.elementLength();
    }
    
    private int elementLength() {
        return ((TypedArrayData)this.getArray()).getElementLength();
    }
    
    protected abstract Factory factory();
    
    protected abstract ScriptObject getPrototype(final Global p0);
    
    @Override
    public final String getClassName() {
        return this.factory().getClassName();
    }
    
    protected boolean isFloatArray() {
        return false;
    }
    
    protected static ArrayBufferView constructorImpl(final boolean newObj, final Object[] args, final Factory factory) {
        final Object arg0 = (args.length != 0) ? args[0] : Integer.valueOf(0);
        if (!newObj) {
            throw ECMAErrors.typeError("constructor.requires.new", factory.getClassName());
        }
        if (arg0 instanceof NativeArrayBuffer) {
            final NativeArrayBuffer buffer = (NativeArrayBuffer)arg0;
            final int byteOffset = (args.length > 1) ? JSType.toInt32(args[1]) : 0;
            int length;
            if (args.length > 2) {
                length = JSType.toInt32(args[2]);
            }
            else {
                if ((buffer.getByteLength() - byteOffset) % factory.bytesPerElement != 0) {
                    throw new RuntimeException("buffer.byteLength - byteOffset must be a multiple of the element size");
                }
                length = (buffer.getByteLength() - byteOffset) / factory.bytesPerElement;
            }
            return factory.construct(buffer, byteOffset, length);
        }
        int length;
        ArrayBufferView dest;
        if (arg0 instanceof ArrayBufferView) {
            length = ((ArrayBufferView)arg0).elementLength();
            dest = factory.construct(length);
        }
        else {
            if (!(arg0 instanceof NativeArray)) {
                final double dlen = JSType.toNumber(arg0);
                length = lengthToInt(Double.isInfinite(dlen) ? 0L : JSType.toLong(dlen));
                return factory.construct(length);
            }
            length = lengthToInt(((NativeArray)arg0).getArray().length());
            dest = factory.construct(length);
        }
        copyElements(dest, length, (ScriptObject)arg0, 0);
        return dest;
    }
    
    protected static Object setImpl(final Object self, final Object array, final Object offset0) {
        final ArrayBufferView dest = (ArrayBufferView)self;
        int length;
        if (array instanceof ArrayBufferView) {
            length = ((ArrayBufferView)array).elementLength();
        }
        else {
            if (!(array instanceof NativeArray)) {
                throw new RuntimeException("argument is not of array type");
            }
            length = (int)(((NativeArray)array).getArray().length() & 0x7FFFFFFFL);
        }
        final ScriptObject source = (ScriptObject)array;
        final int offset = JSType.toInt32(offset0);
        if (dest.elementLength() < length + offset || offset < 0) {
            throw new RuntimeException("offset or array length out of bounds");
        }
        copyElements(dest, length, source, offset);
        return ScriptRuntime.UNDEFINED;
    }
    
    private static void copyElements(final ArrayBufferView dest, final int length, final ScriptObject source, final int offset) {
        if (!dest.isFloatArray()) {
            for (int i = 0, j = offset; i < length; ++i, ++j) {
                dest.set(j, source.getInt(i, -1), 0);
            }
        }
        else {
            for (int i = 0, j = offset; i < length; ++i, ++j) {
                dest.set(j, source.getDouble(i, -1), 0);
            }
        }
    }
    
    private static int lengthToInt(final long length) {
        if (length > 2147483647L || length < 0L) {
            throw ECMAErrors.rangeError("inappropriate.array.buffer.length", JSType.toString((double)length));
        }
        return (int)(length & 0x7FFFFFFFL);
    }
    
    protected static ScriptObject subarrayImpl(final Object self, final Object begin0, final Object end0) {
        final ArrayBufferView arrayView = (ArrayBufferView)self;
        final int byteOffset = arrayView.byteOffset;
        final int bytesPerElement = arrayView.bytesPerElement();
        final int elementLength = arrayView.elementLength();
        final int begin = NativeArrayBuffer.adjustIndex(JSType.toInt32(begin0), elementLength);
        final int end = NativeArrayBuffer.adjustIndex((end0 != ScriptRuntime.UNDEFINED) ? JSType.toInt32(end0) : elementLength, elementLength);
        final int length = Math.max(end - begin, 0);
        assert byteOffset % bytesPerElement == 0;
        return arrayView.factory().construct(arrayView.buffer, begin * bytesPerElement + byteOffset, length);
    }
    
    @Override
    protected GuardedInvocation findGetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = this.getArray().findFastGetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findGetIndexMethod(desc, request);
    }
    
    @Override
    protected GuardedInvocation findSetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = this.getArray().findFastSetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findSetIndexMethod(desc, request);
    }
    
    static {
        $clinit$();
    }
    
    public static void $clinit$() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/ArrayBufferView.$clinit$:()V'.
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:66)
        //     at com.strobel.assembler.metadata.MethodDefinition.tryLoadBody(MethodDefinition.java:729)
        //     at com.strobel.assembler.metadata.MethodDefinition.getBody(MethodDefinition.java:83)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:202)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // Caused by: java.lang.ClassCastException: class com.strobel.assembler.ir.ConstantPool$MethodHandleEntry cannot be cast to class com.strobel.assembler.ir.ConstantPool$ConstantEntry (com.strobel.assembler.ir.ConstantPool$MethodHandleEntry and com.strobel.assembler.ir.ConstantPool$ConstantEntry are in unnamed module of loader 'app')
        //     at com.strobel.assembler.ir.ConstantPool.lookupConstant(ConstantPool.java:120)
        //     at com.strobel.assembler.metadata.ClassFileReader$Scope.lookupConstant(ClassFileReader.java:1319)
        //     at com.strobel.assembler.metadata.MethodReader.readBodyCore(MethodReader.java:293)
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:62)
        //     ... 16 more
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected abstract static class Factory
    {
        final int bytesPerElement;
        final int maxElementLength;
        
        public Factory(final int bytesPerElement) {
            this.bytesPerElement = bytesPerElement;
            this.maxElementLength = Integer.MAX_VALUE / bytesPerElement;
        }
        
        public final ArrayBufferView construct(final int elementLength) {
            if (elementLength > this.maxElementLength) {
                throw ECMAErrors.rangeError("inappropriate.array.buffer.length", JSType.toString(elementLength));
            }
            return this.construct(new NativeArrayBuffer(elementLength * this.bytesPerElement), 0, elementLength);
        }
        
        public abstract ArrayBufferView construct(final NativeArrayBuffer p0, final int p1, final int p2);
        
        public abstract TypedArrayData<?> createArrayData(final ByteBuffer p0, final int p1, final int p2);
        
        public abstract String getClassName();
    }
}
