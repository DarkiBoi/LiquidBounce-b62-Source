// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeArrayBuffer extends ScriptObject
{
    private final ByteBuffer nb;
    private static PropertyMap $nasgenmap$;
    
    protected NativeArrayBuffer(final ByteBuffer nb, final Global global) {
        super(global.getArrayBufferPrototype(), NativeArrayBuffer.$nasgenmap$);
        this.nb = nb;
    }
    
    protected NativeArrayBuffer(final ByteBuffer nb) {
        this(nb, Global.instance());
    }
    
    protected NativeArrayBuffer(final int byteLength) {
        this(ByteBuffer.allocateDirect(byteLength));
    }
    
    protected NativeArrayBuffer(final NativeArrayBuffer other, final int begin, final int end) {
        this(cloneBuffer(other.getNioBuffer(), begin, end));
    }
    
    public static NativeArrayBuffer constructor(final boolean newObj, final Object self, final Object... args) {
        if (!newObj) {
            throw ECMAErrors.typeError("constructor.requires.new", "ArrayBuffer");
        }
        if (args.length == 0) {
            return new NativeArrayBuffer(0);
        }
        return new NativeArrayBuffer(JSType.toInt32(args[0]));
    }
    
    private static ByteBuffer cloneBuffer(final ByteBuffer original, final int begin, final int end) {
        final ByteBuffer clone = ByteBuffer.allocateDirect(original.capacity());
        original.rewind();
        clone.put(original);
        original.rewind();
        clone.flip();
        clone.position(begin);
        clone.limit(end);
        return clone.slice();
    }
    
    ByteBuffer getNioBuffer() {
        return this.nb;
    }
    
    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }
    
    public static int byteLength(final Object self) {
        return ((NativeArrayBuffer)self).getByteLength();
    }
    
    public static boolean isView(final Object self, final Object obj) {
        return obj instanceof ArrayBufferView;
    }
    
    public static NativeArrayBuffer slice(final Object self, final Object begin0, final Object end0) {
        final NativeArrayBuffer arrayBuffer = (NativeArrayBuffer)self;
        final int byteLength = arrayBuffer.getByteLength();
        final int begin = adjustIndex(JSType.toInt32(begin0), byteLength);
        final int end = adjustIndex((end0 != ScriptRuntime.UNDEFINED) ? JSType.toInt32(end0) : byteLength, byteLength);
        return new NativeArrayBuffer(arrayBuffer, begin, Math.max(end, begin));
    }
    
    public static Object slice(final Object self, final int begin, final int end) {
        final NativeArrayBuffer arrayBuffer = (NativeArrayBuffer)self;
        final int byteLength = arrayBuffer.getByteLength();
        return new NativeArrayBuffer(arrayBuffer, adjustIndex(begin, byteLength), Math.max(adjustIndex(end, byteLength), begin));
    }
    
    public static Object slice(final Object self, final int begin) {
        return slice(self, begin, ((NativeArrayBuffer)self).getByteLength());
    }
    
    static int adjustIndex(final int index, final int length) {
        return (index < 0) ? clamp(index + length, length) : clamp(index, length);
    }
    
    private static int clamp(final int index, final int length) {
        if (index < 0) {
            return 0;
        }
        if (index > length) {
            return length;
        }
        return index;
    }
    
    int getByteLength() {
        return this.nb.limit();
    }
    
    ByteBuffer getBuffer() {
        return this.nb;
    }
    
    ByteBuffer getBuffer(final int offset) {
        return (ByteBuffer)this.nb.duplicate().position(offset);
    }
    
    ByteBuffer getBuffer(final int offset, final int length) {
        return (ByteBuffer)this.getBuffer(offset).limit(length);
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeArrayBuffer.$clinit$:()V'.
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
        // Caused by: java.lang.ClassCastException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
