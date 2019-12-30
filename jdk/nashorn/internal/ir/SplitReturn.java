// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public final class SplitReturn extends Statement
{
    private static final long serialVersionUID = 1L;
    public static final SplitReturn INSTANCE;
    
    private SplitReturn() {
        super(-1, 0L, 0);
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return visitor.enterSplitReturn(this) ? visitor.leaveSplitReturn(this) : this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(":splitreturn;");
    }
    
    private Object readResolve() {
        return SplitReturn.INSTANCE;
    }
    
    static {
        INSTANCE = new SplitReturn();
    }
}
