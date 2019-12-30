// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class IfNode extends Statement implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final Expression test;
    private final Block pass;
    private final Block fail;
    private final LocalVariableConversion conversion;
    
    public IfNode(final int lineNumber, final long token, final int finish, final Expression test, final Block pass, final Block fail) {
        super(lineNumber, token, finish);
        this.test = test;
        this.pass = pass;
        this.fail = fail;
        this.conversion = null;
    }
    
    private IfNode(final IfNode ifNode, final Expression test, final Block pass, final Block fail, final LocalVariableConversion conversion) {
        super(ifNode);
        this.test = test;
        this.pass = pass;
        this.fail = fail;
        this.conversion = conversion;
    }
    
    @Override
    public boolean isTerminal() {
        return this.pass.isTerminal() && this.fail != null && this.fail.isTerminal();
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIfNode(this)) {
            return visitor.leaveIfNode(this.setTest((Expression)this.test.accept(visitor)).setPass((Block)this.pass.accept(visitor)).setFail((this.fail == null) ? null : ((Block)this.fail.accept(visitor))));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append("if (");
        this.test.toString(sb, printTypes);
        sb.append(')');
    }
    
    public Block getFail() {
        return this.fail;
    }
    
    private IfNode setFail(final Block fail) {
        if (this.fail == fail) {
            return this;
        }
        return new IfNode(this, this.test, this.pass, fail, this.conversion);
    }
    
    public Block getPass() {
        return this.pass;
    }
    
    private IfNode setPass(final Block pass) {
        if (this.pass == pass) {
            return this;
        }
        return new IfNode(this, this.test, pass, this.fail, this.conversion);
    }
    
    public Expression getTest() {
        return this.test;
    }
    
    public IfNode setTest(final Expression test) {
        if (this.test == test) {
            return this;
        }
        return new IfNode(this, test, this.pass, this.fail, this.conversion);
    }
    
    @Override
    public IfNode setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new IfNode(this, this.test, this.pass, this.fail, conversion);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
