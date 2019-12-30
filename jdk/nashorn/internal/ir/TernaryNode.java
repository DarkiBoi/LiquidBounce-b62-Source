// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class TernaryNode extends Expression
{
    private static final long serialVersionUID = 1L;
    private final Expression test;
    private final JoinPredecessorExpression trueExpr;
    private final JoinPredecessorExpression falseExpr;
    
    public TernaryNode(final long token, final Expression test, final JoinPredecessorExpression trueExpr, final JoinPredecessorExpression falseExpr) {
        super(token, falseExpr.getFinish());
        this.test = test;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }
    
    private TernaryNode(final TernaryNode ternaryNode, final Expression test, final JoinPredecessorExpression trueExpr, final JoinPredecessorExpression falseExpr) {
        super(ternaryNode);
        this.test = test;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterTernaryNode(this)) {
            final Expression newTest = (Expression)this.getTest().accept(visitor);
            final JoinPredecessorExpression newTrueExpr = (JoinPredecessorExpression)this.trueExpr.accept(visitor);
            final JoinPredecessorExpression newFalseExpr = (JoinPredecessorExpression)this.falseExpr.accept(visitor);
            return visitor.leaveTernaryNode(this.setTest(newTest).setTrueExpression(newTrueExpr).setFalseExpression(newFalseExpr));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        final TokenType tokenType = this.tokenType();
        final boolean testParen = tokenType.needsParens(this.getTest().tokenType(), true);
        final boolean trueParen = tokenType.needsParens(this.getTrueExpression().tokenType(), false);
        final boolean falseParen = tokenType.needsParens(this.getFalseExpression().tokenType(), false);
        if (testParen) {
            sb.append('(');
        }
        this.getTest().toString(sb, printType);
        if (testParen) {
            sb.append(')');
        }
        sb.append(" ? ");
        if (trueParen) {
            sb.append('(');
        }
        this.getTrueExpression().toString(sb, printType);
        if (trueParen) {
            sb.append(')');
        }
        sb.append(" : ");
        if (falseParen) {
            sb.append('(');
        }
        this.getFalseExpression().toString(sb, printType);
        if (falseParen) {
            sb.append(')');
        }
    }
    
    @Override
    public boolean isLocal() {
        return this.getTest().isLocal() && this.getTrueExpression().isLocal() && this.getFalseExpression().isLocal();
    }
    
    @Override
    public Type getType() {
        return Type.widestReturnType(this.getTrueExpression().getType(), this.getFalseExpression().getType());
    }
    
    public Expression getTest() {
        return this.test;
    }
    
    public JoinPredecessorExpression getTrueExpression() {
        return this.trueExpr;
    }
    
    public JoinPredecessorExpression getFalseExpression() {
        return this.falseExpr;
    }
    
    public TernaryNode setTest(final Expression test) {
        if (this.test == test) {
            return this;
        }
        return new TernaryNode(this, test, this.trueExpr, this.falseExpr);
    }
    
    public TernaryNode setTrueExpression(final JoinPredecessorExpression trueExpr) {
        if (this.trueExpr == trueExpr) {
            return this;
        }
        return new TernaryNode(this, this.test, trueExpr, this.falseExpr);
    }
    
    public TernaryNode setFalseExpression(final JoinPredecessorExpression falseExpr) {
        if (this.falseExpr == falseExpr) {
            return this;
        }
        return new TernaryNode(this, this.test, this.trueExpr, falseExpr);
    }
}
