// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.types.Type;

public class JoinPredecessorExpression extends Expression implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final LocalVariableConversion conversion;
    
    public JoinPredecessorExpression() {
        this(null);
    }
    
    public JoinPredecessorExpression(final Expression expression) {
        this(expression, null);
    }
    
    private JoinPredecessorExpression(final Expression expression, final LocalVariableConversion conversion) {
        super((expression == null) ? 0L : expression.getToken(), (expression == null) ? 0 : expression.getStart(), (expression == null) ? 0 : expression.getFinish());
        this.expression = expression;
        this.conversion = conversion;
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (conversion == this.conversion) {
            return this;
        }
        return new JoinPredecessorExpression(this.expression, conversion);
    }
    
    @Override
    public Type getType() {
        return this.expression.getType();
    }
    
    @Override
    public boolean isAlwaysFalse() {
        return this.expression != null && this.expression.isAlwaysFalse();
    }
    
    @Override
    public boolean isAlwaysTrue() {
        return this.expression != null && this.expression.isAlwaysTrue();
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public JoinPredecessorExpression setExpression(final Expression expression) {
        if (expression == this.expression) {
            return this;
        }
        return new JoinPredecessorExpression(expression, this.conversion);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJoinPredecessorExpression(this)) {
            final Expression expr = this.getExpression();
            return visitor.leaveJoinPredecessorExpression((expr == null) ? this : this.setExpression((Expression)expr.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        if (this.expression != null) {
            this.expression.toString(sb, printType);
        }
        if (this.conversion != null) {
            this.conversion.toString(sb);
        }
    }
}
