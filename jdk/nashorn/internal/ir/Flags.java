// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

public interface Flags<T extends LexicalContextNode>
{
    int getFlags();
    
    boolean getFlag(final int p0);
    
    T clearFlag(final LexicalContext p0, final int p1);
    
    T setFlag(final LexicalContext p0, final int p1);
    
    T setFlags(final LexicalContext p0, final int p1);
}
