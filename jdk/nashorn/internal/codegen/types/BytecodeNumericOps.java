// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

interface BytecodeNumericOps
{
    Type neg(final MethodVisitor p0, final int p1);
    
    Type sub(final MethodVisitor p0, final int p1);
    
    Type mul(final MethodVisitor p0, final int p1);
    
    Type div(final MethodVisitor p0, final int p1);
    
    Type rem(final MethodVisitor p0, final int p1);
    
    Type cmp(final MethodVisitor p0, final boolean p1);
}
