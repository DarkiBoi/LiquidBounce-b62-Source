// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public final class SetSplitState extends Statement
{
    private static final long serialVersionUID = 1L;
    private final int state;
    
    public SetSplitState(final int state, final int lineNumber) {
        super(lineNumber, 0L, 0);
        this.state = state;
    }
    
    public int getState() {
        return this.state;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return visitor.enterSetSplitState(this) ? visitor.leaveSetSplitState(this) : this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(CompilerConstants.SCOPE.symbolName()).append('.').append(Scope.SET_SPLIT_STATE.name()).append('(').append(this.state).append(");");
    }
}
