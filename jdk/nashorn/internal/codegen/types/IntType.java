// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.runtime.JSType;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;

class IntType extends BitwiseType
{
    private static final long serialVersionUID = 1L;
    private static final CompilerConstants.Call TO_STRING;
    private static final CompilerConstants.Call VALUE_OF;
    
    protected IntType() {
        super("int", Integer.TYPE, 2, 1);
    }
    
    @Override
    public Type nextWider() {
        return IntType.NUMBER;
    }
    
    @Override
    public Class<?> getBoxedType() {
        return Integer.class;
    }
    
    @Override
    public char getBytecodeStackType() {
        return 'I';
    }
    
    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Integer;
        final int value = (int)c;
        switch (value) {
            case -1: {
                method.visitInsn(2);
                break;
            }
            case 0: {
                method.visitInsn(3);
                break;
            }
            case 1: {
                method.visitInsn(4);
                break;
            }
            case 2: {
                method.visitInsn(5);
                break;
            }
            case 3: {
                method.visitInsn(6);
                break;
            }
            case 4: {
                method.visitInsn(7);
                break;
            }
            case 5: {
                method.visitInsn(8);
                break;
            }
            default: {
                if (value == (byte)value) {
                    method.visitIntInsn(16, value);
                    break;
                }
                if (value == (short)value) {
                    method.visitIntInsn(17, value);
                    break;
                }
                method.visitLdcInsn(c);
                break;
            }
        }
        return Type.INT;
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (to.isEquivalentTo(this)) {
            return to;
        }
        if (to.isNumber()) {
            method.visitInsn(135);
        }
        else if (to.isLong()) {
            method.visitInsn(133);
        }
        else if (!to.isBoolean()) {
            if (to.isString()) {
                Type.invokestatic(method, IntType.TO_STRING);
            }
            else {
                if (!to.isObject()) {
                    throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
                }
                Type.invokestatic(method, IntType.VALUE_OF);
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
            method.visitInvokeDynamicInsn("iadd", "(II)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public Type shr(final MethodVisitor method) {
        method.visitInsn(124);
        return IntType.INT;
    }
    
    @Override
    public Type sar(final MethodVisitor method) {
        method.visitInsn(122);
        return IntType.INT;
    }
    
    @Override
    public Type shl(final MethodVisitor method) {
        method.visitInsn(120);
        return IntType.INT;
    }
    
    @Override
    public Type and(final MethodVisitor method) {
        method.visitInsn(126);
        return IntType.INT;
    }
    
    @Override
    public Type or(final MethodVisitor method) {
        method.visitInsn(128);
        return IntType.INT;
    }
    
    @Override
    public Type xor(final MethodVisitor method) {
        method.visitInsn(130);
        return IntType.INT;
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(21, slot);
        return IntType.INT;
    }
    
    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(54, slot);
    }
    
    @Override
    public Type sub(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(100);
        }
        else {
            method.visitInvokeDynamicInsn("isub", "(II)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public Type mul(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(104);
        }
        else {
            method.visitInvokeDynamicInsn("imul", "(II)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public Type div(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            JSType.DIV_ZERO.invoke(method);
        }
        else {
            method.visitInvokeDynamicInsn("idiv", "(II)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public Type rem(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            JSType.REM_ZERO.invoke(method);
        }
        else {
            method.visitInvokeDynamicInsn("irem", "(II)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public Type neg(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(116);
        }
        else {
            method.visitInvokeDynamicInsn("ineg", "(I)I", IntType.MATHBOOTSTRAP, programPoint);
        }
        return IntType.INT;
    }
    
    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(172);
    }
    
    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(0);
        return IntType.INT;
    }
    
    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(3);
        return IntType.INT;
    }
    
    @Override
    public Type cmp(final MethodVisitor method, final boolean isCmpG) {
        throw new UnsupportedOperationException("cmp" + (isCmpG ? 'g' : 'l'));
    }
    
    @Override
    public Type cmp(final MethodVisitor method) {
        throw new UnsupportedOperationException("cmp");
    }
    
    static {
        TO_STRING = CompilerConstants.staticCallNoLookup(Integer.class, "toString", String.class, Integer.TYPE);
        VALUE_OF = CompilerConstants.staticCallNoLookup(Integer.class, "valueOf", Integer.class, Integer.TYPE);
    }
}
