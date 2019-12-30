// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;

public abstract class JumpStatement extends Statement implements JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private final String labelName;
    private final LocalVariableConversion conversion;
    
    protected JumpStatement(final int lineNumber, final long token, final int finish, final String labelName) {
        super(lineNumber, token, finish);
        this.labelName = labelName;
        this.conversion = null;
    }
    
    protected JumpStatement(final JumpStatement jumpStatement, final LocalVariableConversion conversion) {
        super(jumpStatement);
        this.labelName = jumpStatement.labelName;
        this.conversion = conversion;
    }
    
    @Override
    public boolean hasGoto() {
        return true;
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(this.getStatementName());
        if (this.labelName != null) {
            sb.append(' ').append(this.labelName);
        }
    }
    
    abstract String getStatementName();
    
    public abstract BreakableNode getTarget(final LexicalContext p0);
    
    abstract Label getTargetLabel(final BreakableNode p0);
    
    public Label getTargetLabel(final LexicalContext lc) {
        return this.getTargetLabel(this.getTarget(lc));
    }
    
    public LexicalContextNode getPopScopeLimit(final LexicalContext lc) {
        return this.getTarget(lc);
    }
    
    @Override
    public JumpStatement setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return this.createNewJumpStatement(conversion);
    }
    
    abstract JumpStatement createNewJumpStatement(final LocalVariableConversion p0);
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
