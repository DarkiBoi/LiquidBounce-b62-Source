// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class WithNode extends LexicalContextStatement
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final Block body;
    
    public WithNode(final int lineNumber, final long token, final int finish) {
        super(lineNumber, token, finish);
        this.expression = null;
        this.body = null;
    }
    
    private WithNode(final WithNode node, final Expression expression, final Block body) {
        super(node);
        this.expression = expression;
        this.body = body;
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterWithNode(this)) {
            return visitor.leaveWithNode(this.setExpression(lc, (Expression)this.expression.accept(visitor)).setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("with (");
        this.expression.toString(sb, printType);
        sb.append(')');
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public WithNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WithNode(this, this.expression, body));
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public WithNode setExpression(final LexicalContext lc, final Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WithNode(this, expression, this.body));
    }
}
