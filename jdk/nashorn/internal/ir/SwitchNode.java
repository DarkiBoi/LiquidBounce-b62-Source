// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Collections;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.Iterator;
import java.util.ArrayList;
import jdk.nashorn.internal.codegen.Label;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class SwitchNode extends BreakableStatement
{
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final List<CaseNode> cases;
    private final int defaultCaseIndex;
    private final boolean uniqueInteger;
    private final Symbol tag;
    
    public SwitchNode(final int lineNumber, final long token, final int finish, final Expression expression, final List<CaseNode> cases, final CaseNode defaultCase) {
        super(lineNumber, token, finish, new Label("switch_break"));
        this.expression = expression;
        this.cases = cases;
        this.defaultCaseIndex = ((defaultCase == null) ? -1 : cases.indexOf(defaultCase));
        this.uniqueInteger = false;
        this.tag = null;
    }
    
    private SwitchNode(final SwitchNode switchNode, final Expression expression, final List<CaseNode> cases, final int defaultCaseIndex, final LocalVariableConversion conversion, final boolean uniqueInteger, final Symbol tag) {
        super(switchNode, conversion);
        this.expression = expression;
        this.cases = cases;
        this.defaultCaseIndex = defaultCaseIndex;
        this.tag = tag;
        this.uniqueInteger = uniqueInteger;
    }
    
    @Override
    public Node ensureUniqueLabels(final LexicalContext lc) {
        final List<CaseNode> newCases = new ArrayList<CaseNode>();
        for (final CaseNode caseNode : this.cases) {
            newCases.add(new CaseNode(caseNode, caseNode.getTest(), caseNode.getBody(), caseNode.getLocalVariableConversion()));
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, newCases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }
    
    @Override
    public boolean isTerminal() {
        if (!this.cases.isEmpty() && this.defaultCaseIndex != -1) {
            for (final CaseNode caseNode : this.cases) {
                if (!caseNode.isTerminal()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterSwitchNode(this)) {
            return visitor.leaveSwitchNode(this.setExpression(lc, (Expression)this.expression.accept(visitor)).setCases(lc, Node.accept(visitor, this.cases), this.defaultCaseIndex));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("switch (");
        this.expression.toString(sb, printType);
        sb.append(')');
    }
    
    public CaseNode getDefaultCase() {
        return (this.defaultCaseIndex == -1) ? null : this.cases.get(this.defaultCaseIndex);
    }
    
    public List<CaseNode> getCases() {
        return Collections.unmodifiableList((List<? extends CaseNode>)this.cases);
    }
    
    public SwitchNode setCases(final LexicalContext lc, final List<CaseNode> cases) {
        return this.setCases(lc, cases, this.defaultCaseIndex);
    }
    
    private SwitchNode setCases(final LexicalContext lc, final List<CaseNode> cases, final int defaultCaseIndex) {
        if (this.cases == cases) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, cases, defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }
    
    public SwitchNode setCases(final LexicalContext lc, final List<CaseNode> cases, final CaseNode defaultCase) {
        return this.setCases(lc, cases, (defaultCase == null) ? -1 : cases.indexOf(defaultCase));
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public SwitchNode setExpression(final LexicalContext lc, final Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, expression, this.cases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }
    
    public Symbol getTag() {
        return this.tag;
    }
    
    public SwitchNode setTag(final LexicalContext lc, final Symbol tag) {
        if (this.tag == tag) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, tag));
    }
    
    public boolean isUniqueInteger() {
        return this.uniqueInteger;
    }
    
    public SwitchNode setUniqueInteger(final LexicalContext lc, final boolean uniqueInteger) {
        if (this.uniqueInteger == uniqueInteger) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, this.conversion, uniqueInteger, this.tag));
    }
    
    @Override
    JoinPredecessor setLocalVariableConversionChanged(final LexicalContext lc, final LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, conversion, this.uniqueInteger, this.tag));
    }
}
