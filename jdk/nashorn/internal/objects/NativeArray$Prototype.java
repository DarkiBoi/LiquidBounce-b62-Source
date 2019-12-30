// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;

final class NativeArray$Prototype extends PrototypeObject
{
    private Object toString;
    private Object assertNumeric;
    private Object toLocaleString;
    private Object concat;
    private Object join;
    private Object pop;
    private Object push;
    private Object reverse;
    private Object shift;
    private Object slice;
    private Object sort;
    private Object splice;
    private Object unshift;
    private Object indexOf;
    private Object lastIndexOf;
    private Object every;
    private Object some;
    private Object forEach;
    private Object map;
    private Object filter;
    private Object reduce;
    private Object reduceRight;
    private static final PropertyMap $nasgenmap$;
    
    public Object G$toString() {
        return this.toString;
    }
    
    public void S$toString(final Object toString) {
        this.toString = toString;
    }
    
    public Object G$assertNumeric() {
        return this.assertNumeric;
    }
    
    public void S$assertNumeric(final Object assertNumeric) {
        this.assertNumeric = assertNumeric;
    }
    
    public Object G$toLocaleString() {
        return this.toLocaleString;
    }
    
    public void S$toLocaleString(final Object toLocaleString) {
        this.toLocaleString = toLocaleString;
    }
    
    public Object G$concat() {
        return this.concat;
    }
    
    public void S$concat(final Object concat) {
        this.concat = concat;
    }
    
    public Object G$join() {
        return this.join;
    }
    
    public void S$join(final Object join) {
        this.join = join;
    }
    
    public Object G$pop() {
        return this.pop;
    }
    
    public void S$pop(final Object pop) {
        this.pop = pop;
    }
    
    public Object G$push() {
        return this.push;
    }
    
    public void S$push(final Object push) {
        this.push = push;
    }
    
    public Object G$reverse() {
        return this.reverse;
    }
    
    public void S$reverse(final Object reverse) {
        this.reverse = reverse;
    }
    
    public Object G$shift() {
        return this.shift;
    }
    
    public void S$shift(final Object shift) {
        this.shift = shift;
    }
    
    public Object G$slice() {
        return this.slice;
    }
    
    public void S$slice(final Object slice) {
        this.slice = slice;
    }
    
    public Object G$sort() {
        return this.sort;
    }
    
    public void S$sort(final Object sort) {
        this.sort = sort;
    }
    
    public Object G$splice() {
        return this.splice;
    }
    
    public void S$splice(final Object splice) {
        this.splice = splice;
    }
    
    public Object G$unshift() {
        return this.unshift;
    }
    
    public void S$unshift(final Object unshift) {
        this.unshift = unshift;
    }
    
    public Object G$indexOf() {
        return this.indexOf;
    }
    
    public void S$indexOf(final Object indexOf) {
        this.indexOf = indexOf;
    }
    
    public Object G$lastIndexOf() {
        return this.lastIndexOf;
    }
    
    public void S$lastIndexOf(final Object lastIndexOf) {
        this.lastIndexOf = lastIndexOf;
    }
    
    public Object G$every() {
        return this.every;
    }
    
    public void S$every(final Object every) {
        this.every = every;
    }
    
    public Object G$some() {
        return this.some;
    }
    
    public void S$some(final Object some) {
        this.some = some;
    }
    
    public Object G$forEach() {
        return this.forEach;
    }
    
    public void S$forEach(final Object forEach) {
        this.forEach = forEach;
    }
    
    public Object G$map() {
        return this.map;
    }
    
    public void S$map(final Object map) {
        this.map = map;
    }
    
    public Object G$filter() {
        return this.filter;
    }
    
    public void S$filter(final Object filter) {
        this.filter = filter;
    }
    
    public Object G$reduce() {
        return this.reduce;
    }
    
    public void S$reduce(final Object reduce) {
        this.reduce = reduce;
    }
    
    public Object G$reduceRight() {
        return this.reduceRight;
    }
    
    public void S$reduceRight(final Object reduceRight) {
        this.reduceRight = reduceRight;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeArray$Prototype.<clinit>:()V'.
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
    
    NativeArray$Prototype() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeArray$Prototype.<init>:()V'.
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
        // Caused by: java.lang.ClassCastException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public String getClassName() {
        return "Array";
    }
}
