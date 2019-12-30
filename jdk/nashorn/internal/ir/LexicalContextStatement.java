// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;

abstract class LexicalContextStatement extends Statement implements LexicalContextNode
{
    private static final long serialVersionUID = 1L;
    
    protected LexicalContextStatement(final int lineNumber, final long token, final int finish) {
        super(lineNumber, token, finish);
    }
    
    protected LexicalContextStatement(final LexicalContextStatement node) {
        super(node);
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return Acceptor.accept(this, visitor);
    }
}
