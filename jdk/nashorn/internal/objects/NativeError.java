// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeError extends ScriptObject
{
    static final MethodHandle GET_COLUMNNUMBER;
    static final MethodHandle SET_COLUMNNUMBER;
    static final MethodHandle GET_LINENUMBER;
    static final MethodHandle SET_LINENUMBER;
    static final MethodHandle GET_FILENAME;
    static final MethodHandle SET_FILENAME;
    static final MethodHandle GET_STACK;
    static final MethodHandle SET_STACK;
    static final String MESSAGE = "message";
    static final String NAME = "name";
    static final String STACK = "__stack__";
    static final String LINENUMBER = "__lineNumber__";
    static final String COLUMNNUMBER = "__columnNumber__";
    static final String FILENAME = "__fileName__";
    public Object instMessage;
    public Object nashornException;
    private static PropertyMap $nasgenmap$;
    
    private NativeError(final Object msg, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        if (msg != ScriptRuntime.UNDEFINED) {
            this.instMessage = JSType.toString(msg);
        }
        else {
            this.delete("message", false);
        }
        initException(this);
    }
    
    NativeError(final Object msg, final Global global) {
        this(msg, global.getErrorPrototype(), NativeError.$nasgenmap$);
    }
    
    private NativeError(final Object msg) {
        this(msg, Global.instance());
    }
    
    @Override
    public String getClassName() {
        return "Error";
    }
    
    public static NativeError constructor(final boolean newObj, final Object self, final Object msg) {
        return new NativeError(msg);
    }
    
    static void initException(final ScriptObject self) {
        new ECMAException(self, null);
    }
    
    public static Object captureStackTrace(final Object self, final Object errorObj) {
        final ScriptObject sobj = Global.checkObject(errorObj);
        initException(sobj);
        sobj.delete("__stack__", false);
        if (!sobj.has("stack")) {
            final ScriptFunction getStack = ScriptFunction.createBuiltin("getStack", NativeError.GET_STACK);
            final ScriptFunction setStack = ScriptFunction.createBuiltin("setStack", NativeError.SET_STACK);
            sobj.addOwnProperty("stack", 2, getStack, setStack);
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object dumpStack(final Object self) {
        Thread.dumpStack();
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object printStackTrace(final Object self) {
        return ECMAException.printStackTrace(Global.checkObject(self));
    }
    
    public static Object getStackTrace(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        final Object exception = ECMAException.getException(sobj);
        Object[] res;
        if (exception instanceof Throwable) {
            res = NashornException.getScriptFrames((Throwable)exception);
        }
        else {
            res = ScriptRuntime.EMPTY_ARRAY;
        }
        return new NativeArray(res);
    }
    
    public static Object getLineNumber(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        return sobj.has("__lineNumber__") ? sobj.get("__lineNumber__") : ECMAException.getLineNumber(sobj);
    }
    
    public static Object setLineNumber(final Object self, final Object value) {
        final ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty("__lineNumber__")) {
            sobj.put("__lineNumber__", value, false);
        }
        else {
            sobj.addOwnProperty("__lineNumber__", 2, value);
        }
        return value;
    }
    
    public static Object getColumnNumber(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        return sobj.has("__columnNumber__") ? sobj.get("__columnNumber__") : ECMAException.getColumnNumber((ScriptObject)self);
    }
    
    public static Object setColumnNumber(final Object self, final Object value) {
        final ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty("__columnNumber__")) {
            sobj.put("__columnNumber__", value, false);
        }
        else {
            sobj.addOwnProperty("__columnNumber__", 2, value);
        }
        return value;
    }
    
    public static Object getFileName(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        return sobj.has("__fileName__") ? sobj.get("__fileName__") : ECMAException.getFileName((ScriptObject)self);
    }
    
    public static Object setFileName(final Object self, final Object value) {
        final ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty("__fileName__")) {
            sobj.put("__fileName__", value, false);
        }
        else {
            sobj.addOwnProperty("__fileName__", 2, value);
        }
        return value;
    }
    
    public static Object getStack(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        if (sobj.has("__stack__")) {
            return sobj.get("__stack__");
        }
        final Object exception = ECMAException.getException(sobj);
        if (exception instanceof Throwable) {
            final Object value = getScriptStackString(sobj, (Throwable)exception);
            if (sobj.hasOwnProperty("__stack__")) {
                sobj.put("__stack__", value, false);
            }
            else {
                sobj.addOwnProperty("__stack__", 2, value);
            }
            return value;
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object setStack(final Object self, final Object value) {
        final ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty("__stack__")) {
            sobj.put("__stack__", value, false);
        }
        else {
            sobj.addOwnProperty("__stack__", 2, value);
        }
        return value;
    }
    
    public static Object toString(final Object self) {
        final ScriptObject sobj = Global.checkObject(self);
        Object name = sobj.get("name");
        if (name == ScriptRuntime.UNDEFINED) {
            name = "Error";
        }
        else {
            name = JSType.toString(name);
        }
        Object msg = sobj.get("message");
        if (msg == ScriptRuntime.UNDEFINED) {
            msg = "";
        }
        else {
            msg = JSType.toString(msg);
        }
        if (((String)name).isEmpty()) {
            return msg;
        }
        if (((String)msg).isEmpty()) {
            return name;
        }
        return name + ": " + msg;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeError.class, name, Lookup.MH.type(rtype, types));
    }
    
    private static String getScriptStackString(final ScriptObject sobj, final Throwable exp) {
        return JSType.toString(sobj) + "\n" + NashornException.getScriptStackString(exp);
    }
    
    static {
        GET_COLUMNNUMBER = findOwnMH("getColumnNumber", Object.class, Object.class);
        SET_COLUMNNUMBER = findOwnMH("setColumnNumber", Object.class, Object.class, Object.class);
        GET_LINENUMBER = findOwnMH("getLineNumber", Object.class, Object.class);
        SET_LINENUMBER = findOwnMH("setLineNumber", Object.class, Object.class, Object.class);
        GET_FILENAME = findOwnMH("getFileName", Object.class, Object.class);
        SET_FILENAME = findOwnMH("setFileName", Object.class, Object.class, Object.class);
        GET_STACK = findOwnMH("getStack", Object.class, Object.class);
        SET_STACK = findOwnMH("setStack", Object.class, Object.class, Object.class);
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeError.$clinit$:()V'.
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
    
    public Object G$instMessage() {
        return this.instMessage;
    }
    
    public void S$instMessage(final Object instMessage) {
        this.instMessage = instMessage;
    }
    
    public Object G$nashornException() {
        return this.nashornException;
    }
    
    public void S$nashornException(final Object nashornException) {
        this.nashornException = nashornException;
    }
}
