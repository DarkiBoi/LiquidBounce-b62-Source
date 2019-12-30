// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

interface BytecodeBitwiseOps
{
    Type shr(final MethodVisitor p0);
    
    Type sar(final MethodVisitor p0);
    
    Type shl(final MethodVisitor p0);
    
    Type and(final MethodVisitor p0);
    
    Type or(final MethodVisitor p0);
    
    Type xor(final MethodVisitor p0);
    
    Type cmp(final MethodVisitor p0);
}
