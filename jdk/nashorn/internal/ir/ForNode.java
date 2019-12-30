// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class ForNode extends LoopNode
{
    private static final long serialVersionUID = 1L;
    private final Expression init;
    private final JoinPredecessorExpression modify;
    private final Symbol iterator;
    public static final int IS_FOR_IN = 1;
    public static final int IS_FOR_EACH = 2;
    public static final int PER_ITERATION_SCOPE = 4;
    private final int flags;
    
    public ForNode(final int lineNumber, final long token, final int finish, final Block body, final int flags) {
        super(lineNumber, token, finish, body, false);
        this.flags = flags;
        this.init = null;
        this.modify = null;
        this.iterator = null;
    }
    
    private ForNode(final ForNode forNode, final Expression init, final JoinPredecessorExpression test, final Block body, final JoinPredecessorExpression modify, final int flags, final boolean controlFlowEscapes, final LocalVariableConversion conversion, final Symbol iterator) {
        super(forNode, test, body, controlFlowEscapes, conversion);
        this.init = init;
        this.modify = modify;
        this.flags = flags;
        this.iterator = iterator;
    }
    
    @Override
    public Node ensureUniqueLabels(final LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterForNode(this)) {
            return visitor.leaveForNode(this.setInit(lc, (this.init == null) ? null : ((Expression)this.init.accept(visitor))).setTest(lc, (this.test == null) ? null : ((JoinPredecessorExpression)this.test.accept(visitor))).setModify(lc, (this.modify == null) ? null : ((JoinPredecessorExpression)this.modify.accept(visitor))).setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append("for");
        LocalVariableConversion.toString(this.conversion, sb).append(' ');
        if (this.isForIn()) {
            this.init.toString(sb, printTypes);
            sb.append(" in ");
            this.modify.toString(sb, printTypes);
        }
        else {
            if (this.init != null) {
                this.init.toString(sb, printTypes);
            }
            sb.append("; ");
            if (this.test != null) {
                this.test.toString(sb, printTypes);
            }
            sb.append("; ");
            if (this.modify != null) {
                this.modify.toString(sb, printTypes);
            }
        }
        sb.append(')');
    }
    
    @Override
    public boolean hasGoto() {
        return !this.isForIn() && this.test == null;
    }
    
    @Override
    public boolean mustEnter() {
        return !this.isForIn() && this.test == null;
    }
    
    public Expression getInit() {
        return this.init;
    }
    
    public ForNode setInit(final LexicalContext lc, final Expression init) {
        if (this.init == init) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    public boolean isForIn() {
        return (this.flags & 0x1) != 0x0;
    }
    
    public ForNode setIsForIn(final LexicalContext lc) {
        return this.setFlags(lc, this.flags | 0x1);
    }
    
    public boolean isForEach() {
        return (this.flags & 0x2) != 0x0;
    }
    
    public ForNode setIsForEach(final LexicalContext lc) {
        return this.setFlags(lc, this.flags | 0x2);
    }
    
    public Symbol getIterator() {
        return this.iterator;
    }
    
    public ForNode setIterator(final LexicalContext lc, final Symbol iterator) {
        if (this.iterator == iterator) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, iterator));
    }
    
    public JoinPredecessorExpression getModify() {
        return this.modify;
    }
    
    public ForNode setModify(final LexicalContext lc, final JoinPredecessorExpression modify) {
        if (this.modify == modify) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    @Override
    public ForNode setTest(final LexicalContext lc, final JoinPredecessorExpression test) {
        if (this.test == test) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    @Override
    public Block getBody() {
        return this.body;
    }
    
    @Override
    public ForNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    @Override
    public ForNode setControlFlowEscapes(final LexicalContext lc, final boolean controlFlowEscapes) {
        if (this.controlFlowEscapes == controlFlowEscapes) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, controlFlowEscapes, this.conversion, this.iterator));
    }
    
    private ForNode setFlags(final LexicalContext lc, final int flags) {
        if (this.flags == flags) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }
    
    @Override
    JoinPredecessor setLocalVariableConversionChanged(final LexicalContext lc, final LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, conversion, this.iterator));
    }
    
    @Override
    public boolean hasPerIterationScope() {
        return (this.flags & 0x4) != 0x0;
    }
    
    public ForNode setPerIterationScope(final LexicalContext lc) {
        return this.setFlags(lc, this.flags | 0x4);
    }
}
