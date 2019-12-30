// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class CaseNode extends Node implements JoinPredecessor, Labels, Terminal
{
    private static final long serialVersionUID = 1L;
    private final Expression test;
    private final Block body;
    private final Label entry;
    private final LocalVariableConversion conversion;
    
    public CaseNode(final long token, final int finish, final Expression test, final Block body) {
        super(token, finish);
        this.test = test;
        this.body = body;
        this.entry = new Label("entry");
        this.conversion = null;
    }
    
    CaseNode(final CaseNode caseNode, final Expression test, final Block body, final LocalVariableConversion conversion) {
        super(caseNode);
        this.test = test;
        this.body = body;
        this.entry = new Label(caseNode.entry);
        this.conversion = conversion;
    }
    
    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterCaseNode(this)) {
            final Expression newTest = (this.test == null) ? null : ((Expression)this.test.accept(visitor));
            final Block newBody = (this.body == null) ? null : ((Block)this.body.accept(visitor));
            return visitor.leaveCaseNode(this.setTest(newTest).setBody(newBody));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        if (this.test != null) {
            sb.append("case ");
            this.test.toString(sb, printTypes);
            sb.append(':');
        }
        else {
            sb.append("default:");
        }
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public Label getEntry() {
        return this.entry;
    }
    
    public Expression getTest() {
        return this.test;
    }
    
    public CaseNode setTest(final Expression test) {
        if (this.test == test) {
            return this;
        }
        return new CaseNode(this, test, this.body, this.conversion);
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new CaseNode(this, this.test, this.body, conversion);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
    
    private CaseNode setBody(final Block body) {
        if (this.body == body) {
            return this;
        }
        return new CaseNode(this, this.test, body, this.conversion);
    }
    
    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList((List<? extends Label>)Collections.singletonList((T)this.entry));
    }
}
