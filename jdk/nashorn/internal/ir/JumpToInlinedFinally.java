// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.Objects;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class JumpToInlinedFinally extends JumpStatement
{
    private static final long serialVersionUID = 1L;
    
    public JumpToInlinedFinally(final String labelName) {
        super(-1, 0L, 0, Objects.requireNonNull(labelName));
    }
    
    private JumpToInlinedFinally(final JumpToInlinedFinally breakNode, final LocalVariableConversion conversion) {
        super(breakNode, conversion);
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJumpToInlinedFinally(this)) {
            return visitor.leaveJumpToInlinedFinally(this);
        }
        return this;
    }
    
    @Override
    JumpStatement createNewJumpStatement(final LocalVariableConversion conversion) {
        return new JumpToInlinedFinally(this, conversion);
    }
    
    @Override
    String getStatementName() {
        return ":jumpToInlinedFinally";
    }
    
    @Override
    public Block getTarget(final LexicalContext lc) {
        return lc.getInlinedFinally(this.getLabelName());
    }
    
    @Override
    public TryNode getPopScopeLimit(final LexicalContext lc) {
        return lc.getTryNodeForInlinedFinally(this.getLabelName());
    }
    
    @Override
    Label getTargetLabel(final BreakableNode target) {
        assert target != null;
        return ((Block)target).getEntryLabel();
    }
}
