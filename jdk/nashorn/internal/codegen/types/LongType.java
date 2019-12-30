// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.runtime.JSType;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;

class LongType extends Type
{
    private static final long serialVersionUID = 1L;
    private static final CompilerConstants.Call VALUE_OF;
    
    protected LongType(final String name) {
        super(name, Long.TYPE, 3, 2);
    }
    
    protected LongType() {
        this("long");
    }
    
    @Override
    public Type nextWider() {
        return LongType.NUMBER;
    }
    
    @Override
    public Class<?> getBoxedType() {
        return Long.class;
    }
    
    @Override
    public char getBytecodeStackType() {
        return 'J';
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(22, slot);
        return LongType.LONG;
    }
    
    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(55, slot);
    }
    
    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Long;
        final long value = (long)c;
        if (value == 0L) {
            method.visitInsn(9);
        }
        else if (value == 1L) {
            method.visitInsn(10);
        }
        else {
            method.visitLdcInsn(c);
        }
        return Type.LONG;
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (this.isEquivalentTo(to)) {
            return to;
        }
        if (to.isNumber()) {
            method.visitInsn(138);
        }
        else if (to.isInteger()) {
            Type.invokestatic(method, JSType.TO_INT32_L);
        }
        else if (to.isBoolean()) {
            method.visitInsn(136);
        }
        else if (to.isObject()) {
            Type.invokestatic(method, LongType.VALUE_OF);
        }
        else {
            assert false : "Illegal conversion " + this + " -> " + to;
        }
        return to;
    }
    
    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        if (programPoint == -1) {
            method.visitInsn(97);
        }
        else {
            method.visitInvokeDynamicInsn("ladd", "(JJ)J", LongType.MATHBOOTSTRAP, programPoint);
        }
        return LongType.LONG;
    }
    
    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(173);
    }
    
    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(0L);
        return LongType.LONG;
    }
    
    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(9);
        return LongType.LONG;
    }
    
    static {
        VALUE_OF = CompilerConstants.staticCallNoLookup(Long.class, "valueOf", Long.class, Long.TYPE);
    }
}
