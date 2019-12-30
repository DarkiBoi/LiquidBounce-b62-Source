// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;

public interface Optimistic
{
    int getProgramPoint();
    
    Optimistic setProgramPoint(final int p0);
    
    boolean canBeOptimistic();
    
    Type getMostOptimisticType();
    
    Type getMostPessimisticType();
    
    Optimistic setType(final Type p0);
}
