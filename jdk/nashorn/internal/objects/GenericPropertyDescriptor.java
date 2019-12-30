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

public final class GenericPropertyDescriptor extends ScriptObject implements PropertyDescriptor
{
    public Object configurable;
    public Object enumerable;
    private static PropertyMap $nasgenmap$;
    
    GenericPropertyDescriptor(final boolean configurable, final boolean enumerable, final Global global) {
        super(global.getObjectPrototype(), GenericPropertyDescriptor.$nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
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
        return false;
    }
    
    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("value");
    }
    
    @Override
    public ScriptFunction getGetter() {
        throw new UnsupportedOperationException("get");
    }
    
    @Override
    public ScriptFunction getSetter() {
        throw new UnsupportedOperationException("set");
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
        return this;
    }
    
    @Override
    public int type() {
        return 0;
    }
    
    @Override
    public boolean hasAndEquals(final PropertyDescriptor other) {
        return (!this.has("configurable") || !other.has("configurable") || this.isConfigurable() == other.isConfigurable()) && (!this.has("enumerable") || !other.has("enumerable") || this.isEnumerable() == other.isEnumerable());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericPropertyDescriptor)) {
            return false;
        }
        final GenericPropertyDescriptor other = (GenericPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable);
    }
    
    @Override
    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + "}]";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.configurable);
        hash = 97 * hash + Objects.hashCode(this.enumerable);
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/GenericPropertyDescriptor.$clinit$:()V'.
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
}
