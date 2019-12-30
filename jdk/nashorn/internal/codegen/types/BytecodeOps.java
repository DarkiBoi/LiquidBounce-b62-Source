// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

interface BytecodeOps
{
    Type dup(final MethodVisitor p0, final int p1);
    
    Type pop(final MethodVisitor p0);
    
    Type swap(final MethodVisitor p0, final Type p1);
    
    Type add(final MethodVisitor p0, final int p1);
    
    Type load(final MethodVisitor p0, final int p1);
    
    void store(final MethodVisitor p0, final int p1);
    
    Type ldc(final MethodVisitor p0, final Object p1);
    
    Type loadUndefined(final MethodVisitor p0);
    
    Type loadForcedInitializer(final MethodVisitor p0);
    
    Type loadEmpty(final MethodVisitor p0);
    
    Type convert(final MethodVisitor p0, final Type p1);
    
    void _return(final MethodVisitor p0);
}
