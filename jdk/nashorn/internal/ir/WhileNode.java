// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class WhileNode extends LoopNode
{
    private static final long serialVersionUID = 1L;
    private final boolean isDoWhile;
    
    public WhileNode(final int lineNumber, final long token, final int finish, final boolean isDoWhile) {
        super(lineNumber, token, finish, null, false);
        this.isDoWhile = isDoWhile;
    }
    
    private WhileNode(final WhileNode whileNode, final JoinPredecessorExpression test, final Block body, final boolean controlFlowEscapes, final LocalVariableConversion conversion) {
        super(whileNode, test, body, controlFlowEscapes, conversion);
        this.isDoWhile = whileNode.isDoWhile;
    }
    
    @Override
    public Node ensureUniqueLabels(final LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, this.controlFlowEscapes, this.conversion));
    }
    
    @Override
    public boolean hasGoto() {
        return this.test == null;
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (!visitor.enterWhileNode(this)) {
            return this;
        }
        if (this.isDoWhile()) {
            return visitor.leaveWhileNode(this.setBody(lc, (Block)this.body.accept(visitor)).setTest(lc, (JoinPredecessorExpression)this.test.accept(visitor)));
        }
        return visitor.leaveWhileNode(this.setTest(lc, (JoinPredecessorExpression)this.test.accept(visitor)).setBody(lc, (Block)this.body.accept(visitor)));
    }
    
    @Override
    public WhileNode setTest(final LexicalContext lc, final JoinPredecessorExpression test) {
        if (this.test == test) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, test, this.body, this.controlFlowEscapes, this.conversion));
    }
    
    @Override
    public Block getBody() {
        return this.body;
    }
    
    @Override
    public WhileNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, body, this.controlFlowEscapes, this.conversion));
    }
    
    @Override
    public WhileNode setControlFlowEscapes(final LexicalContext lc, final boolean controlFlowEscapes) {
        if (this.controlFlowEscapes == controlFlowEscapes) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, controlFlowEscapes, this.conversion));
    }
    
    @Override
    JoinPredecessor setLocalVariableConversionChanged(final LexicalContext lc, final LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, this.controlFlowEscapes, conversion));
    }
    
    public boolean isDoWhile() {
        return this.isDoWhile;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("while (");
        this.test.toString(sb, printType);
        sb.append(')');
    }
    
    @Override
    public boolean mustEnter() {
        return this.isDoWhile() || this.test == null;
    }
    
    @Override
    public boolean hasPerIterationScope() {
        return false;
    }
}
