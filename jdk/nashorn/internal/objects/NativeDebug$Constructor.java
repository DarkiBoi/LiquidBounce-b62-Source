// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

final class NativeDebug$Constructor extends ScriptObject
{
    private Object getArrayDataClass;
    private Object getArrayData;
    private Object getContext;
    private Object map;
    private Object identical;
    private Object equalWithoutType;
    private Object diffPropertyMaps;
    private Object getClass;
    private Object equals;
    private Object toJavaString;
    private Object toIdentString;
    private Object getListenerCount;
    private Object dumpCounters;
    private Object getEventQueueCapacity;
    private Object setEventQueueCapacity;
    private Object addRuntimeEvent;
    private Object expandEventQueueCapacity;
    private Object clearRuntimeEvents;
    private Object removeRuntimeEvent;
    private Object getRuntimeEvents;
    private Object getLastRuntimeEvent;
    private static final PropertyMap $nasgenmap$;
    
    public Object G$getArrayDataClass() {
        return this.getArrayDataClass;
    }
    
    public void S$getArrayDataClass(final Object getArrayDataClass) {
        this.getArrayDataClass = getArrayDataClass;
    }
    
    public Object G$getArrayData() {
        return this.getArrayData;
    }
    
    public void S$getArrayData(final Object getArrayData) {
        this.getArrayData = getArrayData;
    }
    
    public Object G$getContext() {
        return this.getContext;
    }
    
    public void S$getContext(final Object getContext) {
        this.getContext = getContext;
    }
    
    public Object G$map() {
        return this.map;
    }
    
    public void S$map(final Object map) {
        this.map = map;
    }
    
    public Object G$identical() {
        return this.identical;
    }
    
    public void S$identical(final Object identical) {
        this.identical = identical;
    }
    
    public Object G$equalWithoutType() {
        return this.equalWithoutType;
    }
    
    public void S$equalWithoutType(final Object equalWithoutType) {
        this.equalWithoutType = equalWithoutType;
    }
    
    public Object G$diffPropertyMaps() {
        return this.diffPropertyMaps;
    }
    
    public void S$diffPropertyMaps(final Object diffPropertyMaps) {
        this.diffPropertyMaps = diffPropertyMaps;
    }
    
    public Object G$getClass() {
        return this.getClass;
    }
    
    public void S$getClass(final Object getClass) {
        this.getClass = getClass;
    }
    
    public Object G$equals() {
        return this.equals;
    }
    
    public void S$equals(final Object equals) {
        this.equals = equals;
    }
    
    public Object G$toJavaString() {
        return this.toJavaString;
    }
    
    public void S$toJavaString(final Object toJavaString) {
        this.toJavaString = toJavaString;
    }
    
    public Object G$toIdentString() {
        return this.toIdentString;
    }
    
    public void S$toIdentString(final Object toIdentString) {
        this.toIdentString = toIdentString;
    }
    
    public Object G$getListenerCount() {
        return this.getListenerCount;
    }
    
    public void S$getListenerCount(final Object getListenerCount) {
        this.getListenerCount = getListenerCount;
    }
    
    public Object G$dumpCounters() {
        return this.dumpCounters;
    }
    
    public void S$dumpCounters(final Object dumpCounters) {
        this.dumpCounters = dumpCounters;
    }
    
    public Object G$getEventQueueCapacity() {
        return this.getEventQueueCapacity;
    }
    
    public void S$getEventQueueCapacity(final Object getEventQueueCapacity) {
        this.getEventQueueCapacity = getEventQueueCapacity;
    }
    
    public Object G$setEventQueueCapacity() {
        return this.setEventQueueCapacity;
    }
    
    public void S$setEventQueueCapacity(final Object setEventQueueCapacity) {
        this.setEventQueueCapacity = setEventQueueCapacity;
    }
    
    public Object G$addRuntimeEvent() {
        return this.addRuntimeEvent;
    }
    
    public void S$addRuntimeEvent(final Object addRuntimeEvent) {
        this.addRuntimeEvent = addRuntimeEvent;
    }
    
    public Object G$expandEventQueueCapacity() {
        return this.expandEventQueueCapacity;
    }
    
    public void S$expandEventQueueCapacity(final Object expandEventQueueCapacity) {
        this.expandEventQueueCapacity = expandEventQueueCapacity;
    }
    
    public Object G$clearRuntimeEvents() {
        return this.clearRuntimeEvents;
    }
    
    public void S$clearRuntimeEvents(final Object clearRuntimeEvents) {
        this.clearRuntimeEvents = clearRuntimeEvents;
    }
    
    public Object G$removeRuntimeEvent() {
        return this.removeRuntimeEvent;
    }
    
    public void S$removeRuntimeEvent(final Object removeRuntimeEvent) {
        this.removeRuntimeEvent = removeRuntimeEvent;
    }
    
    public Object G$getRuntimeEvents() {
        return this.getRuntimeEvents;
    }
    
    public void S$getRuntimeEvents(final Object getRuntimeEvents) {
        this.getRuntimeEvents = getRuntimeEvents;
    }
    
    public Object G$getLastRuntimeEvent() {
        return this.getLastRuntimeEvent;
    }
    
    public void S$getLastRuntimeEvent(final Object getLastRuntimeEvent) {
        this.getLastRuntimeEvent = getLastRuntimeEvent;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeDebug$Constructor.<clinit>:()V'.
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
    
    NativeDebug$Constructor() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeDebug$Constructor.<init>:()V'.
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
        //     at com.strobel.assembler.metadata.MethodReader.readBodyCore(MethodReader.java:293)
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:62)
        //     ... 16 more
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public String getClassName() {
        return "Debug";
    }
}
