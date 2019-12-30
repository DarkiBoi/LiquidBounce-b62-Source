// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class CatchNode extends Statement
{
    private static final long serialVersionUID = 1L;
    private final IdentNode exception;
    private final Expression exceptionCondition;
    private final Block body;
    private final boolean isSyntheticRethrow;
    
    public CatchNode(final int lineNumber, final long token, final int finish, final IdentNode exception, final Expression exceptionCondition, final Block body, final boolean isSyntheticRethrow) {
        super(lineNumber, token, finish);
        this.exception = ((exception == null) ? null : exception.setIsInitializedHere());
        this.exceptionCondition = exceptionCondition;
        this.body = body;
        this.isSyntheticRethrow = isSyntheticRethrow;
    }
    
    private CatchNode(final CatchNode catchNode, final IdentNode exception, final Expression exceptionCondition, final Block body, final boolean isSyntheticRethrow) {
        super(catchNode);
        this.exception = exception;
        this.exceptionCondition = exceptionCondition;
        this.body = body;
        this.isSyntheticRethrow = isSyntheticRethrow;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterCatchNode(this)) {
            return visitor.leaveCatchNode(this.setException((IdentNode)this.exception.accept(visitor)).setExceptionCondition((this.exceptionCondition == null) ? null : ((Expression)this.exceptionCondition.accept(visitor))).setBody((Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append(" catch (");
        this.exception.toString(sb, printTypes);
        if (this.exceptionCondition != null) {
            sb.append(" if ");
            this.exceptionCondition.toString(sb, printTypes);
        }
        sb.append(')');
    }
    
    public IdentNode getException() {
        return this.exception;
    }
    
    public Expression getExceptionCondition() {
        return this.exceptionCondition;
    }
    
    public CatchNode setExceptionCondition(final Expression exceptionCondition) {
        if (this.exceptionCondition == exceptionCondition) {
            return this;
        }
        return new CatchNode(this, this.exception, exceptionCondition, this.body, this.isSyntheticRethrow);
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public CatchNode setException(final IdentNode exception) {
        if (this.exception == exception) {
            return this;
        }
        return new CatchNode(this, exception, this.exceptionCondition, this.body, this.isSyntheticRethrow);
    }
    
    private CatchNode setBody(final Block body) {
        if (this.body == body) {
            return this;
        }
        return new CatchNode(this, this.exception, this.exceptionCondition, body, this.isSyntheticRethrow);
    }
    
    public boolean isSyntheticRethrow() {
        return this.isSyntheticRethrow;
    }
}
