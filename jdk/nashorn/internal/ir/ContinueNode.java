// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class ContinueNode extends JumpStatement
{
    private static final long serialVersionUID = 1L;
    
    public ContinueNode(final int lineNumber, final long token, final int finish, final String labelName) {
        super(lineNumber, token, finish, labelName);
    }
    
    private ContinueNode(final ContinueNode continueNode, final LocalVariableConversion conversion) {
        super(continueNode, conversion);
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterContinueNode(this)) {
            return visitor.leaveContinueNode(this);
        }
        return this;
    }
    
    @Override
    JumpStatement createNewJumpStatement(final LocalVariableConversion conversion) {
        return new ContinueNode(this, conversion);
    }
    
    @Override
    String getStatementName() {
        return "continue";
    }
    
    @Override
    public BreakableNode getTarget(final LexicalContext lc) {
        return lc.getContinueTo(this.getLabelName());
    }
    
    @Override
    Label getTargetLabel(final BreakableNode target) {
        return ((LoopNode)target).getContinueLabel();
    }
}
