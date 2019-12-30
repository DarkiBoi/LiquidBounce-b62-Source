// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;

final class NativeDate$Prototype extends PrototypeObject
{
    private Object toString;
    private Object toDateString;
    private Object toTimeString;
    private Object toLocaleString;
    private Object toLocaleDateString;
    private Object toLocaleTimeString;
    private Object valueOf;
    private Object getTime;
    private Object getFullYear;
    private Object getUTCFullYear;
    private Object getYear;
    private Object getMonth;
    private Object getUTCMonth;
    private Object getDate;
    private Object getUTCDate;
    private Object getDay;
    private Object getUTCDay;
    private Object getHours;
    private Object getUTCHours;
    private Object getMinutes;
    private Object getUTCMinutes;
    private Object getSeconds;
    private Object getUTCSeconds;
    private Object getMilliseconds;
    private Object getUTCMilliseconds;
    private Object getTimezoneOffset;
    private Object setTime;
    private Object setMilliseconds;
    private Object setUTCMilliseconds;
    private Object setSeconds;
    private Object setUTCSeconds;
    private Object setMinutes;
    private Object setUTCMinutes;
    private Object setHours;
    private Object setUTCHours;
    private Object setDate;
    private Object setUTCDate;
    private Object setMonth;
    private Object setUTCMonth;
    private Object setFullYear;
    private Object setUTCFullYear;
    private Object setYear;
    private Object toUTCString;
    private Object toGMTString;
    private Object toISOString;
    private Object toJSON;
    private static final PropertyMap $nasgenmap$;
    
    public Object G$toString() {
        return this.toString;
    }
    
    public void S$toString(final Object toString) {
        this.toString = toString;
    }
    
    public Object G$toDateString() {
        return this.toDateString;
    }
    
    public void S$toDateString(final Object toDateString) {
        this.toDateString = toDateString;
    }
    
    public Object G$toTimeString() {
        return this.toTimeString;
    }
    
    public void S$toTimeString(final Object toTimeString) {
        this.toTimeString = toTimeString;
    }
    
    public Object G$toLocaleString() {
        return this.toLocaleString;
    }
    
    public void S$toLocaleString(final Object toLocaleString) {
        this.toLocaleString = toLocaleString;
    }
    
    public Object G$toLocaleDateString() {
        return this.toLocaleDateString;
    }
    
    public void S$toLocaleDateString(final Object toLocaleDateString) {
        this.toLocaleDateString = toLocaleDateString;
    }
    
    public Object G$toLocaleTimeString() {
        return this.toLocaleTimeString;
    }
    
    public void S$toLocaleTimeString(final Object toLocaleTimeString) {
        this.toLocaleTimeString = toLocaleTimeString;
    }
    
    public Object G$valueOf() {
        return this.valueOf;
    }
    
    public void S$valueOf(final Object valueOf) {
        this.valueOf = valueOf;
    }
    
    public Object G$getTime() {
        return this.getTime;
    }
    
    public void S$getTime(final Object getTime) {
        this.getTime = getTime;
    }
    
    public Object G$getFullYear() {
        return this.getFullYear;
    }
    
    public void S$getFullYear(final Object getFullYear) {
        this.getFullYear = getFullYear;
    }
    
    public Object G$getUTCFullYear() {
        return this.getUTCFullYear;
    }
    
    public void S$getUTCFullYear(final Object getUTCFullYear) {
        this.getUTCFullYear = getUTCFullYear;
    }
    
    public Object G$getYear() {
        return this.getYear;
    }
    
    public void S$getYear(final Object getYear) {
        this.getYear = getYear;
    }
    
    public Object G$getMonth() {
        return this.getMonth;
    }
    
    public void S$getMonth(final Object getMonth) {
        this.getMonth = getMonth;
    }
    
    public Object G$getUTCMonth() {
        return this.getUTCMonth;
    }
    
    public void S$getUTCMonth(final Object getUTCMonth) {
        this.getUTCMonth = getUTCMonth;
    }
    
    public Object G$getDate() {
        return this.getDate;
    }
    
    public void S$getDate(final Object getDate) {
        this.getDate = getDate;
    }
    
    public Object G$getUTCDate() {
        return this.getUTCDate;
    }
    
    public void S$getUTCDate(final Object getUTCDate) {
        this.getUTCDate = getUTCDate;
    }
    
    public Object G$getDay() {
        return this.getDay;
    }
    
    public void S$getDay(final Object getDay) {
        this.getDay = getDay;
    }
    
    public Object G$getUTCDay() {
        return this.getUTCDay;
    }
    
    public void S$getUTCDay(final Object getUTCDay) {
        this.getUTCDay = getUTCDay;
    }
    
    public Object G$getHours() {
        return this.getHours;
    }
    
    public void S$getHours(final Object getHours) {
        this.getHours = getHours;
    }
    
    public Object G$getUTCHours() {
        return this.getUTCHours;
    }
    
    public void S$getUTCHours(final Object getUTCHours) {
        this.getUTCHours = getUTCHours;
    }
    
    public Object G$getMinutes() {
        return this.getMinutes;
    }
    
    public void S$getMinutes(final Object getMinutes) {
        this.getMinutes = getMinutes;
    }
    
    public Object G$getUTCMinutes() {
        return this.getUTCMinutes;
    }
    
    public void S$getUTCMinutes(final Object getUTCMinutes) {
        this.getUTCMinutes = getUTCMinutes;
    }
    
    public Object G$getSeconds() {
        return this.getSeconds;
    }
    
    public void S$getSeconds(final Object getSeconds) {
        this.getSeconds = getSeconds;
    }
    
    public Object G$getUTCSeconds() {
        return this.getUTCSeconds;
    }
    
    public void S$getUTCSeconds(final Object getUTCSeconds) {
        this.getUTCSeconds = getUTCSeconds;
    }
    
    public Object G$getMilliseconds() {
        return this.getMilliseconds;
    }
    
    public void S$getMilliseconds(final Object getMilliseconds) {
        this.getMilliseconds = getMilliseconds;
    }
    
    public Object G$getUTCMilliseconds() {
        return this.getUTCMilliseconds;
    }
    
    public void S$getUTCMilliseconds(final Object getUTCMilliseconds) {
        this.getUTCMilliseconds = getUTCMilliseconds;
    }
    
    public Object G$getTimezoneOffset() {
        return this.getTimezoneOffset;
    }
    
    public void S$getTimezoneOffset(final Object getTimezoneOffset) {
        this.getTimezoneOffset = getTimezoneOffset;
    }
    
    public Object G$setTime() {
        return this.setTime;
    }
    
    public void S$setTime(final Object setTime) {
        this.setTime = setTime;
    }
    
    public Object G$setMilliseconds() {
        return this.setMilliseconds;
    }
    
    public void S$setMilliseconds(final Object setMilliseconds) {
        this.setMilliseconds = setMilliseconds;
    }
    
    public Object G$setUTCMilliseconds() {
        return this.setUTCMilliseconds;
    }
    
    public void S$setUTCMilliseconds(final Object setUTCMilliseconds) {
        this.setUTCMilliseconds = setUTCMilliseconds;
    }
    
    public Object G$setSeconds() {
        return this.setSeconds;
    }
    
    public void S$setSeconds(final Object setSeconds) {
        this.setSeconds = setSeconds;
    }
    
    public Object G$setUTCSeconds() {
        return this.setUTCSeconds;
    }
    
    public void S$setUTCSeconds(final Object setUTCSeconds) {
        this.setUTCSeconds = setUTCSeconds;
    }
    
    public Object G$setMinutes() {
        return this.setMinutes;
    }
    
    public void S$setMinutes(final Object setMinutes) {
        this.setMinutes = setMinutes;
    }
    
    public Object G$setUTCMinutes() {
        return this.setUTCMinutes;
    }
    
    public void S$setUTCMinutes(final Object setUTCMinutes) {
        this.setUTCMinutes = setUTCMinutes;
    }
    
    public Object G$setHours() {
        return this.setHours;
    }
    
    public void S$setHours(final Object setHours) {
        this.setHours = setHours;
    }
    
    public Object G$setUTCHours() {
        return this.setUTCHours;
    }
    
    public void S$setUTCHours(final Object setUTCHours) {
        this.setUTCHours = setUTCHours;
    }
    
    public Object G$setDate() {
        return this.setDate;
    }
    
    public void S$setDate(final Object setDate) {
        this.setDate = setDate;
    }
    
    public Object G$setUTCDate() {
        return this.setUTCDate;
    }
    
    public void S$setUTCDate(final Object setUTCDate) {
        this.setUTCDate = setUTCDate;
    }
    
    public Object G$setMonth() {
        return this.setMonth;
    }
    
    public void S$setMonth(final Object setMonth) {
        this.setMonth = setMonth;
    }
    
    public Object G$setUTCMonth() {
        return this.setUTCMonth;
    }
    
    public void S$setUTCMonth(final Object setUTCMonth) {
        this.setUTCMonth = setUTCMonth;
    }
    
    public Object G$setFullYear() {
        return this.setFullYear;
    }
    
    public void S$setFullYear(final Object setFullYear) {
        this.setFullYear = setFullYear;
    }
    
    public Object G$setUTCFullYear() {
        return this.setUTCFullYear;
    }
    
    public void S$setUTCFullYear(final Object setUTCFullYear) {
        this.setUTCFullYear = setUTCFullYear;
    }
    
    public Object G$setYear() {
        return this.setYear;
    }
    
    public void S$setYear(final Object setYear) {
        this.setYear = setYear;
    }
    
    public Object G$toUTCString() {
        return this.toUTCString;
    }
    
    public void S$toUTCString(final Object toUTCString) {
        this.toUTCString = toUTCString;
    }
    
    public Object G$toGMTString() {
        return this.toGMTString;
    }
    
    public void S$toGMTString(final Object toGMTString) {
        this.toGMTString = toGMTString;
    }
    
    public Object G$toISOString() {
        return this.toISOString;
    }
    
    public void S$toISOString(final Object toISOString) {
        this.toISOString = toISOString;
    }
    
    public Object G$toJSON() {
        return this.toJSON;
    }
    
    public void S$toJSON(final Object toJSON) {
        this.toJSON = toJSON;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeDate$Prototype.<clinit>:()V'.
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
    
    NativeDate$Prototype() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeDate$Prototype.<init>:()V'.
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
        return "Date";
    }
}
