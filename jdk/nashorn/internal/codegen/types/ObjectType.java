// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.runtime.JSType;
import java.lang.invoke.MethodHandle;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.internal.org.objectweb.asm.MethodVisitor;

class ObjectType extends Type
{
    private static final long serialVersionUID = 1L;
    
    protected ObjectType() {
        this(Object.class);
    }
    
    protected ObjectType(final Class<?> clazz) {
        super("object", clazz, (clazz == Object.class) ? 20 : 10, 1);
    }
    
    @Override
    public String toString() {
        return "object" + ((this.getTypeClass() != Object.class) ? ("<type=" + this.getTypeClass().getSimpleName() + '>') : "");
    }
    
    @Override
    public String getShortDescriptor() {
        return (this.getTypeClass() == Object.class) ? "Object" : this.getTypeClass().getSimpleName();
    }
    
    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        Type.invokestatic(method, ScriptRuntime.ADD);
        return Type.OBJECT;
    }
    
    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(25, slot);
        return this;
    }
    
    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(58, slot);
    }
    
    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitFieldInsn(178, CompilerConstants.className(ScriptRuntime.class), "UNDEFINED", CompilerConstants.typeDescriptor(Undefined.class));
        return ObjectType.UNDEFINED;
    }
    
    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(1);
        return ObjectType.OBJECT;
    }
    
    @Override
    public Type loadEmpty(final MethodVisitor method) {
        method.visitFieldInsn(178, CompilerConstants.className(ScriptRuntime.class), "EMPTY", CompilerConstants.typeDescriptor(Undefined.class));
        return ObjectType.UNDEFINED;
    }
    
    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        if (c == null) {
            method.visitInsn(1);
            return Type.OBJECT;
        }
        if (c instanceof Undefined) {
            return this.loadUndefined(method);
        }
        if (c instanceof String) {
            method.visitLdcInsn(c);
            return ObjectType.STRING;
        }
        if (c instanceof Handle) {
            method.visitLdcInsn(c);
            return Type.typeFor(MethodHandle.class);
        }
        throw new UnsupportedOperationException("implementation missing for class " + c.getClass() + " value=" + c);
    }
    
    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        final boolean toString = to.isString();
        if (!toString) {
            if (to.isArray()) {
                final Type elemType = ((ArrayType)to).getElementType();
                if (elemType.isString()) {
                    method.visitTypeInsn(192, CompilerConstants.className(String[].class));
                }
                else if (elemType.isNumber()) {
                    method.visitTypeInsn(192, CompilerConstants.className(double[].class));
                }
                else if (elemType.isLong()) {
                    method.visitTypeInsn(192, CompilerConstants.className(long[].class));
                }
                else if (elemType.isInteger()) {
                    method.visitTypeInsn(192, CompilerConstants.className(int[].class));
                }
                else {
                    method.visitTypeInsn(192, CompilerConstants.className(Object[].class));
                }
                return to;
            }
            if (to.isObject()) {
                final Class<?> toClass = to.getTypeClass();
                if (!toClass.isAssignableFrom(this.getTypeClass())) {
                    method.visitTypeInsn(192, CompilerConstants.className(toClass));
                }
                return to;
            }
        }
        else if (this.isString()) {
            return to;
        }
        if (to.isInteger()) {
            Type.invokestatic(method, JSType.TO_INT32);
        }
        else if (to.isNumber()) {
            Type.invokestatic(method, JSType.TO_NUMBER);
        }
        else if (to.isLong()) {
            Type.invokestatic(method, JSType.TO_LONG);
        }
        else if (to.isBoolean()) {
            Type.invokestatic(method, JSType.TO_BOOLEAN);
        }
        else if (to.isString()) {
            Type.invokestatic(method, JSType.TO_PRIMITIVE_TO_STRING);
        }
        else {
            if (!to.isCharSequence()) {
                throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to + " " + this.isString() + " " + toString);
            }
            Type.invokestatic(method, JSType.TO_PRIMITIVE_TO_CHARSEQUENCE);
        }
        return to;
    }
    
    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(176);
    }
    
    @Override
    public char getBytecodeStackType() {
        return 'A';
    }
}
