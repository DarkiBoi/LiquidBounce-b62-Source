// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

interface BytecodeArrayOps
{
    Type aload(final MethodVisitor p0);
    
    void astore(final MethodVisitor p0);
    
    Type arraylength(final MethodVisitor p0);
    
    Type newarray(final MethodVisitor p0);
    
    Type newarray(final MethodVisitor p0, final int p1);
}
