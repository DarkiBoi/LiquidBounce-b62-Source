// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.nio.ByteOrder;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.nio.ByteBuffer;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public class NativeDataView extends ScriptObject
{
    private static PropertyMap $nasgenmap$;
    public final Object buffer;
    public final int byteOffset;
    public final int byteLength;
    private final ByteBuffer buf;
    
    private NativeDataView(final NativeArrayBuffer arrBuf) {
        this(arrBuf, arrBuf.getBuffer(), 0);
    }
    
    private NativeDataView(final NativeArrayBuffer arrBuf, final int offset) {
        this(arrBuf, bufferFrom(arrBuf, offset), offset);
    }
    
    private NativeDataView(final NativeArrayBuffer arrBuf, final int offset, final int length) {
        this(arrBuf, bufferFrom(arrBuf, offset, length), offset, length);
    }
    
    private NativeDataView(final NativeArrayBuffer arrBuf, final ByteBuffer buf, final int offset) {
        this(arrBuf, buf, offset, buf.capacity() - offset);
    }
    
    private NativeDataView(final NativeArrayBuffer arrBuf, final ByteBuffer buf, final int offset, final int length) {
        super(Global.instance().getDataViewPrototype(), NativeDataView.$nasgenmap$);
        this.buffer = arrBuf;
        this.byteOffset = offset;
        this.byteLength = length;
        this.buf = buf;
    }
    
    public static NativeDataView constructor(final boolean newObj, final Object self, final Object... args) {
        if (args.length == 0 || !(args[0] instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        final NativeArrayBuffer arrBuf = (NativeArrayBuffer)args[0];
        switch (args.length) {
            case 1: {
                return new NativeDataView(arrBuf);
            }
            case 2: {
                return new NativeDataView(arrBuf, JSType.toInt32(args[1]));
            }
            default: {
                return new NativeDataView(arrBuf, JSType.toInt32(args[1]), JSType.toInt32(args[2]));
            }
        }
    }
    
    public static NativeDataView constructor(final boolean newObj, final Object self, final Object arrBuf, final int offset) {
        if (!(arrBuf instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        return new NativeDataView((NativeArrayBuffer)arrBuf, offset);
    }
    
    public static NativeDataView constructor(final boolean newObj, final Object self, final Object arrBuf, final int offset, final int length) {
        if (!(arrBuf instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        return new NativeDataView((NativeArrayBuffer)arrBuf, offset, length);
    }
    
    public static int getInt8(final Object self, final Object byteOffset) {
        try {
            return getBuffer(self).get(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt8(final Object self, final int byteOffset) {
        try {
            return getBuffer(self).get(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getUint8(final Object self, final Object byteOffset) {
        try {
            return 0xFF & getBuffer(self).get(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getUint8(final Object self, final int byteOffset) {
        try {
            return 0xFF & getBuffer(self).get(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt16(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return getBuffer(self, littleEndian).getShort(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt16(final Object self, final int byteOffset) {
        try {
            return getBuffer(self, false).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt16(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return getBuffer(self, littleEndian).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getUint16(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return 0xFFFF & getBuffer(self, littleEndian).getShort(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getUint16(final Object self, final int byteOffset) {
        try {
            return 0xFFFF & getBuffer(self, false).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getUint16(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return 0xFFFF & getBuffer(self, littleEndian).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt32(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt32(final Object self, final int byteOffset) {
        try {
            return getBuffer(self, false).getInt(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static int getInt32(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return getBuffer(self, littleEndian).getInt(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getUint32(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return (double)(0xFFFFFFFFL & (long)getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset)));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getUint32(final Object self, final int byteOffset) {
        try {
            return (double)JSType.toUint32(getBuffer(self, false).getInt(JSType.toInt32(byteOffset)));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getUint32(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return (double)JSType.toUint32(getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset)));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat32(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return getBuffer(self, littleEndian).getFloat(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat32(final Object self, final int byteOffset) {
        try {
            return getBuffer(self, false).getFloat(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat32(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return getBuffer(self, littleEndian).getFloat(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat64(final Object self, final Object byteOffset, final Object littleEndian) {
        try {
            return getBuffer(self, littleEndian).getDouble(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat64(final Object self, final int byteOffset) {
        try {
            return getBuffer(self, false).getDouble(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static double getFloat64(final Object self, final int byteOffset, final boolean littleEndian) {
        try {
            return getBuffer(self, littleEndian).getDouble(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt8(final Object self, final Object byteOffset, final Object value) {
        try {
            getBuffer(self).put(JSType.toInt32(byteOffset), (byte)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt8(final Object self, final int byteOffset, final int value) {
        try {
            getBuffer(self).put(byteOffset, (byte)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint8(final Object self, final Object byteOffset, final Object value) {
        try {
            getBuffer(self).put(JSType.toInt32(byteOffset), (byte)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint8(final Object self, final int byteOffset, final int value) {
        try {
            getBuffer(self).put(byteOffset, (byte)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt16(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putShort(JSType.toInt32(byteOffset), (short)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt16(final Object self, final int byteOffset, final int value) {
        try {
            getBuffer(self, false).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt16(final Object self, final int byteOffset, final int value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint16(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putShort(JSType.toInt32(byteOffset), (short)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint16(final Object self, final int byteOffset, final int value) {
        try {
            getBuffer(self, false).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint16(final Object self, final int byteOffset, final int value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt32(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putInt(JSType.toInt32(byteOffset), JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt32(final Object self, final int byteOffset, final int value) {
        try {
            getBuffer(self, false).putInt(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setInt32(final Object self, final int byteOffset, final int value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putInt(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint32(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putInt(JSType.toInt32(byteOffset), (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint32(final Object self, final int byteOffset, final double value) {
        try {
            getBuffer(self, false).putInt(byteOffset, (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setUint32(final Object self, final int byteOffset, final double value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putInt(byteOffset, (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat32(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putFloat((int)JSType.toUint32(byteOffset), (float)JSType.toNumber(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat32(final Object self, final int byteOffset, final double value) {
        try {
            getBuffer(self, false).putFloat(byteOffset, (float)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat32(final Object self, final int byteOffset, final double value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putFloat(byteOffset, (float)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat64(final Object self, final Object byteOffset, final Object value, final Object littleEndian) {
        try {
            getBuffer(self, littleEndian).putDouble((int)JSType.toUint32(byteOffset), JSType.toNumber(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat64(final Object self, final int byteOffset, final double value) {
        try {
            getBuffer(self, false).putDouble(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    public static Object setFloat64(final Object self, final int byteOffset, final double value, final boolean littleEndian) {
        try {
            getBuffer(self, littleEndian).putDouble(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }
    
    private static ByteBuffer bufferFrom(final NativeArrayBuffer nab, final int offset) {
        try {
            return nab.getBuffer(offset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.constructor.offset", new String[0]);
        }
    }
    
    private static ByteBuffer bufferFrom(final NativeArrayBuffer nab, final int offset, final int length) {
        try {
            return nab.getBuffer(offset, length);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.constructor.offset", new String[0]);
        }
    }
    
    private static NativeDataView checkSelf(final Object self) {
        if (!(self instanceof NativeDataView)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", ScriptRuntime.safeToString(self));
        }
        return (NativeDataView)self;
    }
    
    private static ByteBuffer getBuffer(final Object self) {
        return checkSelf(self).buf;
    }
    
    private static ByteBuffer getBuffer(final Object self, final Object littleEndian) {
        return getBuffer(self, JSType.toBoolean(littleEndian));
    }
    
    private static ByteBuffer getBuffer(final Object self, final boolean littleEndian) {
        return getBuffer(self).order(littleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeDataView.$clinit$:()V'.
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
    
    public Object G$buffer() {
        return this.buffer;
    }
    
    public int G$byteOffset() {
        return this.byteOffset;
    }
    
    public int G$byteLength() {
        return this.byteLength;
    }
}
