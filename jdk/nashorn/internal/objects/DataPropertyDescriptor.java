// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.Objects;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class DataPropertyDescriptor extends ScriptObject implements PropertyDescriptor
{
    public Object configurable;
    public Object enumerable;
    public Object writable;
    public Object value;
    private static PropertyMap $nasgenmap$;
    
    DataPropertyDescriptor(final boolean configurable, final boolean enumerable, final boolean writable, final Object value, final Global global) {
        super(global.getObjectPrototype(), DataPropertyDescriptor.$nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
        this.writable = writable;
        this.value = value;
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
        return JSType.toBoolean(this.writable);
    }
    
    @Override
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public ScriptFunction getGetter() {
        throw new UnsupportedOperationException("getter");
    }
    
    @Override
    public ScriptFunction getSetter() {
        throw new UnsupportedOperationException("setter");
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
        this.writable = flag;
    }
    
    @Override
    public void setValue(final Object value) {
        this.value = value;
    }
    
    @Override
    public void setGetter(final Object getter) {
        throw new UnsupportedOperationException("set getter");
    }
    
    @Override
    public void setSetter(final Object setter) {
        throw new UnsupportedOperationException("set setter");
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
        if (sobj.has("writable")) {
            this.writable = JSType.toBoolean(sobj.get("writable"));
        }
        else {
            this.delete("writable", false);
        }
        if (sobj.has("value")) {
            this.value = sobj.get("value");
        }
        else {
            this.delete("value", false);
        }
        return this;
    }
    
    @Override
    public int type() {
        return 1;
    }
    
    @Override
    public boolean hasAndEquals(final PropertyDescriptor otherDesc) {
        if (!(otherDesc instanceof DataPropertyDescriptor)) {
            return false;
        }
        final DataPropertyDescriptor other = (DataPropertyDescriptor)otherDesc;
        return (!this.has("configurable") || ScriptRuntime.sameValue(this.configurable, other.configurable)) && (!this.has("enumerable") || ScriptRuntime.sameValue(this.enumerable, other.enumerable)) && (!this.has("writable") || ScriptRuntime.sameValue(this.writable, other.writable)) && (!this.has("value") || ScriptRuntime.sameValue(this.value, other.value));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataPropertyDescriptor)) {
            return false;
        }
        final DataPropertyDescriptor other = (DataPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable) && ScriptRuntime.sameValue(this.writable, other.writable) && ScriptRuntime.sameValue(this.value, other.value);
    }
    
    @Override
    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + " writable=" + this.writable + " value=" + this.value + "}]";
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.configurable);
        hash = 43 * hash + Objects.hashCode(this.enumerable);
        hash = 43 * hash + Objects.hashCode(this.writable);
        hash = 43 * hash + Objects.hashCode(this.value);
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/DataPropertyDescriptor.$clinit$:()V'.
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
    
    public Object G$writable() {
        return this.writable;
    }
    
    public void S$writable(final Object writable) {
        this.writable = writable;
    }
    
    public Object G$value() {
        return this.value;
    }
    
    public void S$value(final Object value) {
        this.value = value;
    }
}
