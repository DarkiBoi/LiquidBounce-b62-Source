// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class LabelNode extends LexicalContextStatement implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final String labelName;
    private final Block body;
    private final LocalVariableConversion localVariableConversion;
    
    public LabelNode(final int lineNumber, final long token, final int finish, final String labelName, final Block body) {
        super(lineNumber, token, finish);
        this.labelName = labelName;
        this.body = body;
        this.localVariableConversion = null;
    }
    
    private LabelNode(final LabelNode labelNode, final String labelName, final Block body, final LocalVariableConversion localVariableConversion) {
        super(labelNode);
        this.labelName = labelName;
        this.body = body;
        this.localVariableConversion = localVariableConversion;
    }
    
    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterLabelNode(this)) {
            return visitor.leaveLabelNode(this.setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(this.labelName).append(':');
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public LabelNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new LabelNode(this, this.labelName, body, this.localVariableConversion));
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.localVariableConversion;
    }
    
    @Override
    public LabelNode setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion localVariableConversion) {
        if (this.localVariableConversion == localVariableConversion) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new LabelNode(this, this.labelName, this.body, localVariableConversion));
    }
}
