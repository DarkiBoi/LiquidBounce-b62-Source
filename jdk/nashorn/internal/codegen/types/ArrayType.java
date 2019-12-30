// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

public class ArrayType extends ObjectType implements BytecodeArrayOps
{
    private static final long serialVersionUID = 1L;
    
    protected ArrayType(final Class<?> clazz) {
        super(clazz);
    }
    
    public Type getElementType() {
        return Type.typeFor(this.getTypeClass().getComponentType());
    }
    
    @Override
    public void astore(final MethodVisitor method) {
        method.visitInsn(83);
    }
    
    @Override
    public Type aload(final MethodVisitor method) {
        method.visitInsn(50);
        return this.getElementType();
    }
    
    @Override
    public Type arraylength(final MethodVisitor method) {
        method.visitInsn(190);
        return ArrayType.INT;
    }
    
    @Override
    public Type newarray(final MethodVisitor method) {
        method.visitTypeInsn(189, this.getElementType().getInternalName());
        return this;
    }
    
    @Override
    public Type newarray(final MethodVisitor method, final int dims) {
        method.visitMultiANewArrayInsn(this.getInternalName(), dims);
        return this;
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(25, slot);
        return this;
    }
    
    @Override
    public String toString() {
        return "array<elementType=" + this.getElementType().getTypeClass().getSimpleName() + '>';
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        assert to.isObject();
        assert ((ArrayType)to).getElementType() == this.getElementType();
        return to;
    }
}
