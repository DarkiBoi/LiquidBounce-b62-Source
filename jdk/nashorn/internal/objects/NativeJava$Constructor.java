// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

final class NativeJava$Constructor extends ScriptObject
{
    private Object isType;
    private Object synchronizedFunc;
    private Object isJavaMethod;
    private Object isJavaFunction;
    private Object isJavaObject;
    private Object isScriptObject;
    private Object isScriptFunction;
    private Object type;
    private Object typeName;
    private Object to;
    private Object from;
    private Object extend;
    private Object _super;
    private Object asJSONCompatible;
    private static final PropertyMap $nasgenmap$;
    
    public Object G$isType() {
        return this.isType;
    }
    
    public void S$isType(final Object isType) {
        this.isType = isType;
    }
    
    public Object G$synchronizedFunc() {
        return this.synchronizedFunc;
    }
    
    public void S$synchronizedFunc(final Object synchronizedFunc) {
        this.synchronizedFunc = synchronizedFunc;
    }
    
    public Object G$isJavaMethod() {
        return this.isJavaMethod;
    }
    
    public void S$isJavaMethod(final Object isJavaMethod) {
        this.isJavaMethod = isJavaMethod;
    }
    
    public Object G$isJavaFunction() {
        return this.isJavaFunction;
    }
    
    public void S$isJavaFunction(final Object isJavaFunction) {
        this.isJavaFunction = isJavaFunction;
    }
    
    public Object G$isJavaObject() {
        return this.isJavaObject;
    }
    
    public void S$isJavaObject(final Object isJavaObject) {
        this.isJavaObject = isJavaObject;
    }
    
    public Object G$isScriptObject() {
        return this.isScriptObject;
    }
    
    public void S$isScriptObject(final Object isScriptObject) {
        this.isScriptObject = isScriptObject;
    }
    
    public Object G$isScriptFunction() {
        return this.isScriptFunction;
    }
    
    public void S$isScriptFunction(final Object isScriptFunction) {
        this.isScriptFunction = isScriptFunction;
    }
    
    public Object G$type() {
        return this.type;
    }
    
    public void S$type(final Object type) {
        this.type = type;
    }
    
    public Object G$typeName() {
        return this.typeName;
    }
    
    public void S$typeName(final Object typeName) {
        this.typeName = typeName;
    }
    
    public Object G$to() {
        return this.to;
    }
    
    public void S$to(final Object to) {
        this.to = to;
    }
    
    public Object G$from() {
        return this.from;
    }
    
    public void S$from(final Object from) {
        this.from = from;
    }
    
    public Object G$extend() {
        return this.extend;
    }
    
    public void S$extend(final Object extend) {
        this.extend = extend;
    }
    
    public Object G$_super() {
        return this._super;
    }
    
    public void S$_super(final Object super1) {
        this._super = super1;
    }
    
    public Object G$asJSONCompatible() {
        return this.asJSONCompatible;
    }
    
    public void S$asJSONCompatible(final Object asJSONCompatible) {
        this.asJSONCompatible = asJSONCompatible;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeJava$Constructor.<clinit>:()V'.
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
        //     at com.strobel.assembler.metadata.MethodReader.readBodyCore(MethodReader.java:286)
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:62)
        //     ... 16 more
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    NativeJava$Constructor() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeJava$Constructor.<init>:()V'.
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:66)
        //     at com.strobel.assembler.metadata.MethodDefinition.tryLoadBody(MethodDefinition.java:729)
        //     at com.strobel.assembler.metadata.MethodDefinition.getBody(MethodDefinition.java:83)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:202)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
        //     at com.strobel.assembler.metadata.MethodReader.readBodyCore(MethodReader.java:286)
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:62)
        //     ... 16 more
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public String getClassName() {
        return "Java";
    }
}
