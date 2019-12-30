// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import jdk.nashorn.internal.codegen.Label;

public abstract class LoopNode extends BreakableStatement
{
    private static final long serialVersionUID = 1L;
    protected final Label continueLabel;
    protected final JoinPredecessorExpression test;
    protected final Block body;
    protected final boolean controlFlowEscapes;
    
    protected LoopNode(final int lineNumber, final long token, final int finish, final Block body, final boolean controlFlowEscapes) {
        super(lineNumber, token, finish, new Label("while_break"));
        this.continueLabel = new Label("while_continue");
        this.test = null;
        this.body = body;
        this.controlFlowEscapes = controlFlowEscapes;
    }
    
    protected LoopNode(final LoopNode loopNode, final JoinPredecessorExpression test, final Block body, final boolean controlFlowEscapes, final LocalVariableConversion conversion) {
        super(loopNode, conversion);
        this.continueLabel = new Label(loopNode.continueLabel);
        this.test = test;
        this.body = body;
        this.controlFlowEscapes = controlFlowEscapes;
    }
    
    @Override
    public abstract Node ensureUniqueLabels(final LexicalContext p0);
    
    public boolean controlFlowEscapes() {
        return this.controlFlowEscapes;
    }
    
    @Override
    public boolean isTerminal() {
        return this.mustEnter() && !this.controlFlowEscapes && (this.body.isTerminal() || this.test == null);
    }
    
    public abstract boolean mustEnter();
    
    public Label getContinueLabel() {
        return this.continueLabel;
    }
    
    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList((List<? extends Label>)Arrays.asList(this.breakLabel, this.continueLabel));
    }
    
    @Override
    public boolean isLoop() {
        return true;
    }
    
    public abstract Block getBody();
    
    public abstract LoopNode setBody(final LexicalContext p0, final Block p1);
    
    public final JoinPredecessorExpression getTest() {
        return this.test;
    }
    
    public abstract LoopNode setTest(final LexicalContext p0, final JoinPredecessorExpression p1);
    
    public abstract LoopNode setControlFlowEscapes(final LexicalContext p0, final boolean p1);
    
    public abstract boolean hasPerIterationScope();
}
