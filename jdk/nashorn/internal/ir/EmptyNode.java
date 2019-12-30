// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class EmptyNode extends Statement
{
    private static final long serialVersionUID = 1L;
    
    public EmptyNode(final Statement node) {
        super(node);
    }
    
    public EmptyNode(final int lineNumber, final long token, final int finish) {
        super(lineNumber, token, finish);
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterEmptyNode(this)) {
            return visitor.leaveEmptyNode(this);
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append(';');
    }
}
