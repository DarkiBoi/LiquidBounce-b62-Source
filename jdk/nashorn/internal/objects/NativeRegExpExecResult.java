// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeRegExpExecResult extends ScriptObject
{
    public Object index;
    public Object input;
    private static PropertyMap $nasgenmap$;
    
    NativeRegExpExecResult(final RegExpResult result, final Global global) {
        super(global.getArrayPrototype(), NativeRegExpExecResult.$nasgenmap$);
        this.setIsArray();
        this.setArray(ArrayData.allocate(result.getGroups().clone()));
        this.index = result.getIndex();
        this.input = result.getInput();
    }
    
    @Override
    public String getClassName() {
        return "Array";
    }
    
    public static Object length(final Object self) {
        if (self instanceof ScriptObject) {
            return JSType.toUint32((double)((ScriptObject)self).getArray().length());
        }
        return 0;
    }
    
    public static void length(final Object self, final Object length) {
        if (self instanceof ScriptObject) {
            ((ScriptObject)self).setLength(NativeArray.validLength(length));
        }
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeRegExpExecResult.$clinit$:()V'.
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
    
    public Object G$index() {
        return this.index;
    }
    
    public void S$index(final Object index) {
        this.index = index;
    }
    
    public Object G$input() {
        return this.input;
    }
    
    public void S$input(final Object input) {
        this.input = input;
    }
}
