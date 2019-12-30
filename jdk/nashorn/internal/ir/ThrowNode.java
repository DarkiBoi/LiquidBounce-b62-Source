// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class ThrowNode extends Statement implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final LocalVariableConversion conversion;
    private final boolean isSyntheticRethrow;
    
    public ThrowNode(final int lineNumber, final long token, final int finish, final Expression expression, final boolean isSyntheticRethrow) {
        super(lineNumber, token, finish);
        this.expression = expression;
        this.isSyntheticRethrow = isSyntheticRethrow;
        this.conversion = null;
    }
    
    private ThrowNode(final ThrowNode node, final Expression expression, final boolean isSyntheticRethrow, final LocalVariableConversion conversion) {
        super(node);
        this.expression = expression;
        this.isSyntheticRethrow = isSyntheticRethrow;
        this.conversion = conversion;
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterThrowNode(this)) {
            return visitor.leaveThrowNode(this.setExpression((Expression)this.expression.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("throw ");
        if (this.expression != null) {
            this.expression.toString(sb, printType);
        }
        if (this.conversion != null) {
            this.conversion.toString(sb);
        }
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public ThrowNode setExpression(final Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return new ThrowNode(this, expression, this.isSyntheticRethrow, this.conversion);
    }
    
    public boolean isSyntheticRethrow() {
        return this.isSyntheticRethrow;
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new ThrowNode(this, this.expression, this.isSyntheticRethrow, conversion);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
