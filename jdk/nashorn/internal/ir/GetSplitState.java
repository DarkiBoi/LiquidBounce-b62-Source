// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.types.Type;

public final class GetSplitState extends Expression
{
    private static final long serialVersionUID = 1L;
    public static final GetSplitState INSTANCE;
    
    private GetSplitState() {
        super(0L, 0);
    }
    
    @Override
    public Type getType() {
        return Type.INT;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return visitor.enterGetSplitState(this) ? visitor.leaveGetSplitState(this) : this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        if (printType) {
            sb.append("{I}");
        }
        sb.append(CompilerConstants.SCOPE.symbolName()).append('.').append(Scope.GET_SPLIT_STATE.name()).append("()");
    }
    
    private Object readResolve() {
        return GetSplitState.INSTANCE;
    }
    
    static {
        INSTANCE = new GetSplitState();
    }
}
