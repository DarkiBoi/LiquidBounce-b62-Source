// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

public abstract class NumericType extends Type implements BytecodeNumericOps
{
    private static final long serialVersionUID = 1L;
    
    protected NumericType(final String name, final Class<?> clazz, final int weight, final int slots) {
        super(name, clazz, weight, slots);
    }
}
