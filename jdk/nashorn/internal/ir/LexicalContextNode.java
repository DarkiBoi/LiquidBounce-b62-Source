// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public interface LexicalContextNode
{
    Node accept(final LexicalContext p0, final NodeVisitor<? extends LexicalContext> p1);
    
    public static class Acceptor
    {
        static Node accept(final LexicalContextNode node, final NodeVisitor<? extends LexicalContext> visitor) {
            final LexicalContext lc = (LexicalContext)visitor.getLexicalContext();
            lc.push(node);
            final Node newNode = node.accept(lc, visitor);
            return lc.pop(newNode);
        }
    }
}
