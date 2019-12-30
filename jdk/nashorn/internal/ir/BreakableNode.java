// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;

public interface BreakableNode extends LexicalContextNode, JoinPredecessor, Labels
{
    Node ensureUniqueLabels(final LexicalContext p0);
    
    boolean isBreakableWithoutLabel();
    
    Label getBreakLabel();
}
