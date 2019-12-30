// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class ExpressionStatement extends Statement
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    
    public ExpressionStatement(final int lineNumber, final long token, final int finish, final Expression expression) {
        super(lineNumber, token, finish);
        this.expression = expression;
    }
    
    private ExpressionStatement(final ExpressionStatement expressionStatement, final Expression expression) {
        super(expressionStatement);
        this.expression = expression;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterExpressionStatement(this)) {
            return visitor.leaveExpressionStatement(this.setExpression((Expression)this.expression.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        this.expression.toString(sb, printTypes);
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public ExpressionStatement setExpression(final Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return new ExpressionStatement(this, expression);
    }
}
