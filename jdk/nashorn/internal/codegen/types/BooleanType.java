// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;

public final class BooleanType extends Type
{
    private static final long serialVersionUID = 1L;
    private static final CompilerConstants.Call VALUE_OF;
    private static final CompilerConstants.Call TO_STRING;
    
    protected BooleanType() {
        super("boolean", Boolean.TYPE, 1, 1);
    }
    
    @Override
    public Type nextWider() {
        return BooleanType.INT;
    }
    
    @Override
    public Class<?> getBoxedType() {
        return Boolean.class;
    }
    
    @Override
    public char getBytecodeStackType() {
        return 'I';
    }
    
    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(0);
        return BooleanType.BOOLEAN;
    }
    
    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(3);
        return BooleanType.BOOLEAN;
    }
    
    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(172);
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(21, slot);
        return BooleanType.BOOLEAN;
    }
    
    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(54, slot);
    }
    
    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Boolean;
        method.visitInsn(((boolean)c) ? 4 : 3);
        return BooleanType.BOOLEAN;
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (this.isEquivalentTo(to)) {
            return to;
        }
        if (to.isNumber()) {
            method.visitInsn(135);
        }
        else if (to.isLong()) {
            method.visitInsn(133);
        }
        else if (!to.isInteger()) {
            if (to.isString()) {
                Type.invokestatic(method, BooleanType.TO_STRING);
            }
            else {
                if (!to.isObject()) {
                    throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
                }
                Type.invokestatic(method, BooleanType.VALUE_OF);
            }
        }
        return to;
    }
    
    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(96);
        }
        else {
            method.visitInvokeDynamicInsn("iadd", "(II)I", BooleanType.MATHBOOTSTRAP, programPoint);
        }
        return BooleanType.INT;
    }
    
    static {
        VALUE_OF = CompilerConstants.staticCallNoLookup(Boolean.class, "valueOf", Boolean.class, Boolean.TYPE);
        TO_STRING = CompilerConstants.staticCallNoLookup(Boolean.class, "toString", String.class, Boolean.TYPE);
    }
}
