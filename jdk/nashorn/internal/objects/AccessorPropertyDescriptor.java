// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.Objects;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class AccessorPropertyDescriptor extends ScriptObject implements PropertyDescriptor
{
    public Object configurable;
    public Object enumerable;
    public Object get;
    public Object set;
    private static PropertyMap $nasgenmap$;
    
    AccessorPropertyDescriptor(final boolean configurable, final boolean enumerable, final Object get, final Object set, final Global global) {
        super(global.getObjectPrototype(), AccessorPropertyDescriptor.$nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
        this.get = get;
        this.set = set;
    }
    
    @Override
    public boolean isConfigurable() {
        return JSType.toBoolean(this.configurable);
    }
    
    @Override
    public boolean isEnumerable() {
        return JSType.toBoolean(this.enumerable);
    }
    
    @Override
    public boolean isWritable() {
        return true;
    }
    
    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("value");
    }
    
    @Override
    public ScriptFunction getGetter() {
        return (this.get instanceof ScriptFunction) ? ((ScriptFunction)this.get) : null;
    }
    
    @Override
    public ScriptFunction getSetter() {
        return (this.set instanceof ScriptFunction) ? ((ScriptFunction)this.set) : null;
    }
    
    @Override
    public void setConfigurable(final boolean flag) {
        this.configurable = flag;
    }
    
    @Override
    public void setEnumerable(final boolean flag) {
        this.enumerable = flag;
    }
    
    @Override
    public void setWritable(final boolean flag) {
        throw new UnsupportedOperationException("set writable");
    }
    
    @Override
    public void setValue(final Object value) {
        throw new UnsupportedOperationException("set value");
    }
    
    @Override
    public void setGetter(final Object getter) {
        this.get = getter;
    }
    
    @Override
    public void setSetter(final Object setter) {
        this.set = setter;
    }
    
    @Override
    public PropertyDescriptor fillFrom(final ScriptObject sobj) {
        if (sobj.has("configurable")) {
            this.configurable = JSType.toBoolean(sobj.get("configurable"));
        }
        else {
            this.delete("configurable", false);
        }
        if (sobj.has("enumerable")) {
            this.enumerable = JSType.toBoolean(sobj.get("enumerable"));
        }
        else {
            this.delete("enumerable", false);
        }
        if (sobj.has("get")) {
            final Object getter = sobj.get("get");
            if (getter != ScriptRuntime.UNDEFINED && !(getter instanceof ScriptFunction)) {
                throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(getter));
            }
            this.get = getter;
        }
        else {
            this.delete("get", false);
        }
        if (sobj.has("set")) {
            final Object setter = sobj.get("set");
            if (setter != ScriptRuntime.UNDEFINED && !(setter instanceof ScriptFunction)) {
                throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(setter));
            }
            this.set = setter;
        }
        else {
            this.delete("set", false);
        }
        return this;
    }
    
    @Override
    public int type() {
        return 2;
    }
    
    @Override
    public boolean hasAndEquals(final PropertyDescriptor otherDesc) {
        if (!(otherDesc instanceof AccessorPropertyDescriptor)) {
            return false;
        }
        final AccessorPropertyDescriptor other = (AccessorPropertyDescriptor)otherDesc;
        return (!this.has("configurable") || ScriptRuntime.sameValue(this.configurable, other.configurable)) && (!this.has("enumerable") || ScriptRuntime.sameValue(this.enumerable, other.enumerable)) && (!this.has("get") || ScriptRuntime.sameValue(this.get, other.get)) && (!this.has("set") || ScriptRuntime.sameValue(this.set, other.set));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccessorPropertyDescriptor)) {
            return false;
        }
        final AccessorPropertyDescriptor other = (AccessorPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable) && ScriptRuntime.sameValue(this.get, other.get) && ScriptRuntime.sameValue(this.set, other.set);
    }
    
    @Override
    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + " getter=" + this.get + " setter=" + this.set + "}]";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.configurable);
        hash = 41 * hash + Objects.hashCode(this.enumerable);
        hash = 41 * hash + Objects.hashCode(this.get);
        hash = 41 * hash + Objects.hashCode(this.set);
        return hash;
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/AccessorPropertyDescriptor.$clinit$:()V'.
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
    
    public Object G$configurable() {
        return this.configurable;
    }
    
    public void S$configurable(final Object configurable) {
        this.configurable = configurable;
    }
    
    public Object G$enumerable() {
        return this.enumerable;
    }
    
    public void S$enumerable(final Object enumerable) {
        this.enumerable = enumerable;
    }
    
    public Object G$get() {
        return this.get;
    }
    
    public void S$get(final Object get) {
        this.get = get;
    }
    
    public Object G$set() {
        return this.set;
    }
    
    public void S$set(final Object set) {
        this.set = set;
    }
}
