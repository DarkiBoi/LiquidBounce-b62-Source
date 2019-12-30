// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

final class NativeMath$Constructor extends ScriptObject
{
    private Object abs;
    private Object acos;
    private Object asin;
    private Object atan;
    private Object atan2;
    private Object ceil;
    private Object cos;
    private Object exp;
    private Object floor;
    private Object log;
    private Object max;
    private Object min;
    private Object pow;
    private Object random;
    private Object round;
    private Object sin;
    private Object sqrt;
    private Object tan;
    private static final PropertyMap $nasgenmap$;
    
    public double G$E() {
        return NativeMath.E;
    }
    
    public double G$LN10() {
        return NativeMath.LN10;
    }
    
    public double G$LN2() {
        return NativeMath.LN2;
    }
    
    public double G$LOG2E() {
        return NativeMath.LOG2E;
    }
    
    public double G$LOG10E() {
        return NativeMath.LOG10E;
    }
    
    public double G$PI() {
        return NativeMath.PI;
    }
    
    public double G$SQRT1_2() {
        return NativeMath.SQRT1_2;
    }
    
    public double G$SQRT2() {
        return NativeMath.SQRT2;
    }
    
    public Object G$abs() {
        return this.abs;
    }
    
    public void S$abs(final Object abs) {
        this.abs = abs;
    }
    
    public Object G$acos() {
        return this.acos;
    }
    
    public void S$acos(final Object acos) {
        this.acos = acos;
    }
    
    public Object G$asin() {
        return this.asin;
    }
    
    public void S$asin(final Object asin) {
        this.asin = asin;
    }
    
    public Object G$atan() {
        return this.atan;
    }
    
    public void S$atan(final Object atan) {
        this.atan = atan;
    }
    
    public Object G$atan2() {
        return this.atan2;
    }
    
    public void S$atan2(final Object atan2) {
        this.atan2 = atan2;
    }
    
    public Object G$ceil() {
        return this.ceil;
    }
    
    public void S$ceil(final Object ceil) {
        this.ceil = ceil;
    }
    
    public Object G$cos() {
        return this.cos;
    }
    
    public void S$cos(final Object cos) {
        this.cos = cos;
    }
    
    public Object G$exp() {
        return this.exp;
    }
    
    public void S$exp(final Object exp) {
        this.exp = exp;
    }
    
    public Object G$floor() {
        return this.floor;
    }
    
    public void S$floor(final Object floor) {
        this.floor = floor;
    }
    
    public Object G$log() {
        return this.log;
    }
    
    public void S$log(final Object log) {
        this.log = log;
    }
    
    public Object G$max() {
        return this.max;
    }
    
    public void S$max(final Object max) {
        this.max = max;
    }
    
    public Object G$min() {
        return this.min;
    }
    
    public void S$min(final Object min) {
        this.min = min;
    }
    
    public Object G$pow() {
        return this.pow;
    }
    
    public void S$pow(final Object pow) {
        this.pow = pow;
    }
    
    public Object G$random() {
        return this.random;
    }
    
    public void S$random(final Object random) {
        this.random = random;
    }
    
    public Object G$round() {
        return this.round;
    }
    
    public void S$round(final Object round) {
        this.round = round;
    }
    
    public Object G$sin() {
        return this.sin;
    }
    
    public void S$sin(final Object sin) {
        this.sin = sin;
    }
    
    public Object G$sqrt() {
        return this.sqrt;
    }
    
    public void S$sqrt(final Object sqrt) {
        this.sqrt = sqrt;
    }
    
    public Object G$tan() {
        return this.tan;
    }
    
    public void S$tan(final Object tan) {
        this.tan = tan;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeMath$Constructor.<clinit>:()V'.
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
    
    NativeMath$Constructor() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeMath$Constructor.<init>:()V'.
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
        return "Math";
    }
}
