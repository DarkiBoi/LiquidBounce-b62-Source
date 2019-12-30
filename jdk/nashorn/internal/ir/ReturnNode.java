// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class ReturnNode extends Statement
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    
    public ReturnNode(final int lineNumber, final long token, final int finish, final Expression expression) {
        super(lineNumber, token, finish);
        this.expression = expression;
    }
    
    private ReturnNode(final ReturnNode returnNode, final Expression expression) {
        super(returnNode);
        this.expression = expression;
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
    
    public boolean isReturn() {
        return this.isTokenType(TokenType.RETURN);
    }
    
    public boolean hasExpression() {
        return this.expression != null;
    }
    
    public boolean isYield() {
        return this.isTokenType(TokenType.YIELD);
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (!visitor.enterReturnNode(this)) {
            return this;
        }
        if (this.expression != null) {
            return visitor.leaveReturnNode(this.setExpression((Expression)this.expression.accept(visitor)));
        }
        return visitor.leaveReturnNode(this);
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(this.isYield() ? "yield" : "return");
        if (this.expression != null) {
            sb.append(' ');
            this.expression.toString(sb, printType);
        }
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public ReturnNode setExpression(final Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return new ReturnNode(this, expression);
    }
}
