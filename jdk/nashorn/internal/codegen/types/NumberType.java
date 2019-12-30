// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.runtime.JSType;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;

class NumberType extends NumericType
{
    private static final long serialVersionUID = 1L;
    private static final CompilerConstants.Call VALUE_OF;
    
    protected NumberType() {
        super("double", Double.TYPE, 4, 2);
    }
    
    @Override
    public Type nextWider() {
        return NumberType.OBJECT;
    }
    
    @Override
    public Class<?> getBoxedType() {
        return Double.class;
    }
    
    @Override
    public char getBytecodeStackType() {
        return 'D';
    }
    
    @Override
    public Type cmp(final MethodVisitor method, final boolean isCmpG) {
        method.visitInsn(isCmpG ? 152 : 151);
        return NumberType.INT;
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(24, slot);
        return NumberType.NUMBER;
    }
    
    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(57, slot);
    }
    
    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(Double.NaN);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(14);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Double;
        final double value = (double)c;
        if (Double.doubleToLongBits(value) == 0L) {
            method.visitInsn(14);
        }
        else if (value == 1.0) {
            method.visitInsn(15);
        }
        else {
            method.visitLdcInsn(value);
        }
        return NumberType.NUMBER;
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (this.isEquivalentTo(to)) {
            return null;
        }
        if (to.isInteger()) {
            Type.invokestatic(method, JSType.TO_INT32_D);
        }
        else if (to.isLong()) {
            Type.invokestatic(method, JSType.TO_LONG_D);
        }
        else if (to.isBoolean()) {
            Type.invokestatic(method, JSType.TO_BOOLEAN_D);
        }
        else if (to.isString()) {
            Type.invokestatic(method, JSType.TO_STRING_D);
        }
        else {
            if (!to.isObject()) {
                throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
            }
            Type.invokestatic(method, NumberType.VALUE_OF);
        }
        return to;
    }
    
    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        method.visitInsn(99);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type sub(final MethodVisitor method, final int programPoint) {
        method.visitInsn(103);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type mul(final MethodVisitor method, final int programPoint) {
        method.visitInsn(107);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type div(final MethodVisitor method, final int programPoint) {
        method.visitInsn(111);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type rem(final MethodVisitor method, final int programPoint) {
        method.visitInsn(115);
        return NumberType.NUMBER;
    }
    
    @Override
    public Type neg(final MethodVisitor method, final int programPoint) {
        method.visitInsn(119);
        return NumberType.NUMBER;
    }
    
    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(175);
    }
    
    static {
        VALUE_OF = CompilerConstants.staticCallNoLookup(Double.class, "valueOf", Double.class, Double.TYPE);
    }
}
