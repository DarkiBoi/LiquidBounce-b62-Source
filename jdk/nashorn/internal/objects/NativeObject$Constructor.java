// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeObject$Constructor extends ScriptFunction
{
    private Object setIndexedPropertiesToExternalArrayData;
    private Object getPrototypeOf;
    private Object setPrototypeOf;
    private Object getOwnPropertyDescriptor;
    private Object getOwnPropertyNames;
    private Object create;
    private Object defineProperty;
    private Object defineProperties;
    private Object seal;
    private Object freeze;
    private Object preventExtensions;
    private Object isSealed;
    private Object isFrozen;
    private Object isExtensible;
    private Object keys;
    private Object bindProperties;
    private static final PropertyMap $nasgenmap$;
    
    public Object G$setIndexedPropertiesToExternalArrayData() {
        return this.setIndexedPropertiesToExternalArrayData;
    }
    
    public void S$setIndexedPropertiesToExternalArrayData(final Object setIndexedPropertiesToExternalArrayData) {
        this.setIndexedPropertiesToExternalArrayData = setIndexedPropertiesToExternalArrayData;
    }
    
    public Object G$getPrototypeOf() {
        return this.getPrototypeOf;
    }
    
    public void S$getPrototypeOf(final Object getPrototypeOf) {
        this.getPrototypeOf = getPrototypeOf;
    }
    
    public Object G$setPrototypeOf() {
        return this.setPrototypeOf;
    }
    
    public void S$setPrototypeOf(final Object setPrototypeOf) {
        this.setPrototypeOf = setPrototypeOf;
    }
    
    public Object G$getOwnPropertyDescriptor() {
        return this.getOwnPropertyDescriptor;
    }
    
    public void S$getOwnPropertyDescriptor(final Object getOwnPropertyDescriptor) {
        this.getOwnPropertyDescriptor = getOwnPropertyDescriptor;
    }
    
    public Object G$getOwnPropertyNames() {
        return this.getOwnPropertyNames;
    }
    
    public void S$getOwnPropertyNames(final Object getOwnPropertyNames) {
        this.getOwnPropertyNames = getOwnPropertyNames;
    }
    
    public Object G$create() {
        return this.create;
    }
    
    public void S$create(final Object create) {
        this.create = create;
    }
    
    public Object G$defineProperty() {
        return this.defineProperty;
    }
    
    public void S$defineProperty(final Object defineProperty) {
        this.defineProperty = defineProperty;
    }
    
    public Object G$defineProperties() {
        return this.defineProperties;
    }
    
    public void S$defineProperties(final Object defineProperties) {
        this.defineProperties = defineProperties;
    }
    
    public Object G$seal() {
        return this.seal;
    }
    
    public void S$seal(final Object seal) {
        this.seal = seal;
    }
    
    public Object G$freeze() {
        return this.freeze;
    }
    
    public void S$freeze(final Object freeze) {
        this.freeze = freeze;
    }
    
    public Object G$preventExtensions() {
        return this.preventExtensions;
    }
    
    public void S$preventExtensions(final Object preventExtensions) {
        this.preventExtensions = preventExtensions;
    }
    
    public Object G$isSealed() {
        return this.isSealed;
    }
    
    public void S$isSealed(final Object isSealed) {
        this.isSealed = isSealed;
    }
    
    public Object G$isFrozen() {
        return this.isFrozen;
    }
    
    public void S$isFrozen(final Object isFrozen) {
        this.isFrozen = isFrozen;
    }
    
    public Object G$isExtensible() {
        return this.isExtensible;
    }
    
    public void S$isExtensible(final Object isExtensible) {
        this.isExtensible = isExtensible;
    }
    
    public Object G$keys() {
        return this.keys;
    }
    
    public void S$keys(final Object keys) {
        this.keys = keys;
    }
    
    public Object G$bindProperties() {
        return this.bindProperties;
    }
    
    public void S$bindProperties(final Object bindProperties) {
        this.bindProperties = bindProperties;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeObject$Constructor.<clinit>:()V'.
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
    
    NativeObject$Constructor() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeObject$Constructor.<init>:()V'.
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
}
