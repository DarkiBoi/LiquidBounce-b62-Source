// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class TryNode extends LexicalContextStatement implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final Block body;
    private final List<Block> catchBlocks;
    private final Block finallyBody;
    private final List<Block> inlinedFinallies;
    private final Symbol exception;
    private final LocalVariableConversion conversion;
    
    public TryNode(final int lineNumber, final long token, final int finish, final Block body, final List<Block> catchBlocks, final Block finallyBody) {
        super(lineNumber, token, finish);
        this.body = body;
        this.catchBlocks = catchBlocks;
        this.finallyBody = finallyBody;
        this.conversion = null;
        this.inlinedFinallies = Collections.emptyList();
        this.exception = null;
    }
    
    private TryNode(final TryNode tryNode, final Block body, final List<Block> catchBlocks, final Block finallyBody, final LocalVariableConversion conversion, final List<Block> inlinedFinallies, final Symbol exception) {
        super(tryNode);
        this.body = body;
        this.catchBlocks = catchBlocks;
        this.finallyBody = finallyBody;
        this.conversion = conversion;
        this.inlinedFinallies = inlinedFinallies;
        this.exception = exception;
    }
    
    @Override
    public Node ensureUniqueLabels(final LexicalContext lc) {
        return new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception);
    }
    
    @Override
    public boolean isTerminal() {
        if (this.body.isTerminal()) {
            for (final Block catchBlock : this.getCatchBlocks()) {
                if (!catchBlock.isTerminal()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterTryNode(this)) {
            final Block newFinallyBody = (this.finallyBody == null) ? null : ((Block)this.finallyBody.accept(visitor));
            final Block newBody = (Block)this.body.accept(visitor);
            return visitor.leaveTryNode(this.setBody(lc, newBody).setFinallyBody(lc, newFinallyBody).setCatchBlocks(lc, Node.accept(visitor, this.catchBlocks)).setInlinedFinallies(lc, Node.accept(visitor, this.inlinedFinallies)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("try ");
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public TryNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }
    
    public List<CatchNode> getCatches() {
        final List<CatchNode> catches = new ArrayList<CatchNode>(this.catchBlocks.size());
        for (final Block catchBlock : this.catchBlocks) {
            catches.add(getCatchNodeFromBlock(catchBlock));
        }
        return Collections.unmodifiableList((List<? extends CatchNode>)catches);
    }
    
    private static CatchNode getCatchNodeFromBlock(final Block catchBlock) {
        return catchBlock.getStatements().get(0);
    }
    
    public List<Block> getCatchBlocks() {
        return Collections.unmodifiableList((List<? extends Block>)this.catchBlocks);
    }
    
    public TryNode setCatchBlocks(final LexicalContext lc, final List<Block> catchBlocks) {
        if (this.catchBlocks == catchBlocks) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }
    
    public Symbol getException() {
        return this.exception;
    }
    
    public TryNode setException(final LexicalContext lc, final Symbol exception) {
        if (this.exception == exception) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, exception));
    }
    
    public Block getFinallyBody() {
        return this.finallyBody;
    }
    
    public Block getInlinedFinally(final String labelName) {
        for (final Block inlinedFinally : this.inlinedFinallies) {
            final LabelNode labelNode = getInlinedFinallyLabelNode(inlinedFinally);
            if (labelNode.getLabelName().equals(labelName)) {
                return labelNode.getBody();
            }
        }
        return null;
    }
    
    private static LabelNode getInlinedFinallyLabelNode(final Block inlinedFinally) {
        return inlinedFinally.getStatements().get(0);
    }
    
    public static Block getLabelledInlinedFinallyBlock(final Block inlinedFinally) {
        return getInlinedFinallyLabelNode(inlinedFinally).getBody();
    }
    
    public List<Block> getInlinedFinallies() {
        return Collections.unmodifiableList((List<? extends Block>)this.inlinedFinallies);
    }
    
    public TryNode setFinallyBody(final LexicalContext lc, final Block finallyBody) {
        if (this.finallyBody == finallyBody) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }
    
    public TryNode setInlinedFinallies(final LexicalContext lc, final List<Block> inlinedFinallies) {
        if (this.inlinedFinallies == inlinedFinallies) {
            return this;
        }
        assert checkInlinedFinallies(inlinedFinallies);
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, inlinedFinallies, this.exception));
    }
    
    private static boolean checkInlinedFinallies(final List<Block> inlinedFinallies) {
        if (!inlinedFinallies.isEmpty()) {
            final Set<String> labels = new HashSet<String>();
            for (final Block inlinedFinally : inlinedFinallies) {
                final List<Statement> stmts = inlinedFinally.getStatements();
                assert stmts.size() == 1;
                final LabelNode ln = getInlinedFinallyLabelNode(inlinedFinally);
                assert labels.add(ln.getLabelName());
            }
        }
        return true;
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new TryNode(this, this.body, this.catchBlocks, this.finallyBody, conversion, this.inlinedFinallies, this.exception);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
