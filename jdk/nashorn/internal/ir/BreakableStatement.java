// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
abstract class BreakableStatement extends LexicalContextStatement implements BreakableNode
{
    private static final long serialVersionUID = 1L;
    protected final Label breakLabel;
    final LocalVariableConversion conversion;
    
    protected BreakableStatement(final int lineNumber, final long token, final int finish, final Label breakLabel) {
        super(lineNumber, token, finish);
        this.breakLabel = breakLabel;
        this.conversion = null;
    }
    
    protected BreakableStatement(final BreakableStatement breakableNode, final LocalVariableConversion conversion) {
        super(breakableNode);
        this.breakLabel = new Label(breakableNode.getBreakLabel());
        this.conversion = conversion;
    }
    
    @Override
    public boolean isBreakableWithoutLabel() {
        return true;
    }
    
    @Override
    public Label getBreakLabel() {
        return this.breakLabel;
    }
    
    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList((List<? extends Label>)Collections.singletonList((T)this.breakLabel));
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return this.setLocalVariableConversionChanged(lc, conversion);
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
    
    abstract JoinPredecessor setLocalVariableConversionChanged(final LexicalContext p0, final LocalVariableConversion p1);
}
