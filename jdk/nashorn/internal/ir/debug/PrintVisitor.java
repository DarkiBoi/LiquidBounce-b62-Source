// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.BlockStatement;
import java.util.Iterator;
import java.util.List;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

public final class PrintVisitor extends SimpleNodeVisitor
{
    private static final int TABWIDTH = 4;
    private final StringBuilder sb;
    private int indent;
    private final String EOLN;
    private final boolean printLineNumbers;
    private final boolean printTypes;
    private int lastLineNumber;
    
    public PrintVisitor() {
        this(true, true);
    }
    
    public PrintVisitor(final boolean printLineNumbers, final boolean printTypes) {
        this.lastLineNumber = -1;
        this.EOLN = System.lineSeparator();
        this.sb = new StringBuilder();
        this.printLineNumbers = printLineNumbers;
        this.printTypes = printTypes;
    }
    
    public PrintVisitor(final Node root) {
        this(root, true, true);
    }
    
    public PrintVisitor(final Node root, final boolean printLineNumbers, final boolean printTypes) {
        this(printLineNumbers, printTypes);
        this.visit(root);
    }
    
    private void visit(final Node root) {
        root.accept(this);
    }
    
    @Override
    public String toString() {
        return this.sb.append(this.EOLN).toString();
    }
    
    private void indent() {
        for (int i = this.indent; i > 0; --i) {
            this.sb.append(' ');
        }
    }
    
    public boolean enterDefault(final Node node) {
        node.toString(this.sb, this.printTypes);
        return false;
    }
    
    @Override
    public boolean enterContinueNode(final ContinueNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }
    
    @Override
    public boolean enterBreakNode(final BreakNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }
    
    @Override
    public boolean enterThrowNode(final ThrowNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        this.sb.append(' ');
        this.sb.append('{');
        this.indent += 4;
        final List<Statement> statements = block.getStatements();
        for (final Statement statement : statements) {
            if (this.printLineNumbers) {
                final int lineNumber = statement.getLineNumber();
                this.sb.append('\n');
                if (lineNumber != this.lastLineNumber) {
                    this.indent();
                    this.sb.append("[|").append(lineNumber).append("|];").append('\n');
                }
                this.lastLineNumber = lineNumber;
            }
            this.indent();
            statement.accept(this);
            int lastIndex;
            char lastChar;
            for (lastIndex = this.sb.length() - 1, lastChar = this.sb.charAt(lastIndex); Character.isWhitespace(lastChar) && lastIndex >= 0; lastChar = this.sb.charAt(--lastIndex)) {}
            if (lastChar != '}' && lastChar != ';') {
                this.sb.append(';');
            }
            if (statement.hasGoto()) {
                this.sb.append(" [GOTO]");
            }
            if (statement.isTerminal()) {
                this.sb.append(" [TERMINAL]");
            }
        }
        this.indent -= 4;
        this.sb.append(this.EOLN);
        this.indent();
        this.sb.append('}');
        this.printLocalVariableConversion(block);
        return false;
    }
    
    @Override
    public boolean enterBlockStatement(final BlockStatement statement) {
        statement.getBlock().accept(this);
        return false;
    }
    
    @Override
    public boolean enterBinaryNode(final BinaryNode binaryNode) {
        binaryNode.lhs().accept(this);
        this.sb.append(' ');
        this.sb.append(binaryNode.tokenType());
        this.sb.append(' ');
        binaryNode.rhs().accept(this);
        return false;
    }
    
    @Override
    public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression expr) {
        expr.getExpression().accept(this);
        this.printLocalVariableConversion(expr);
        return false;
    }
    
    @Override
    public boolean enterIdentNode(final IdentNode identNode) {
        identNode.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(identNode);
        return true;
    }
    
    private void printLocalVariableConversion(final JoinPredecessor joinPredecessor) {
        LocalVariableConversion.toString(joinPredecessor.getLocalVariableConversion(), this.sb);
    }
    
    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        unaryNode.toString(this.sb, new Runnable() {
            @Override
            public void run() {
                unaryNode.getExpression().accept(PrintVisitor.this);
            }
        }, this.printTypes);
        return false;
    }
    
    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        expressionStatement.getExpression().accept(this);
        return false;
    }
    
    @Override
    public boolean enterForNode(final ForNode forNode) {
        forNode.toString(this.sb, this.printTypes);
        forNode.getBody().accept(this);
        return false;
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        functionNode.toString(this.sb, this.printTypes);
        this.enterBlock(functionNode.getBody());
        return false;
    }
    
    @Override
    public boolean enterIfNode(final IfNode ifNode) {
        ifNode.toString(this.sb, this.printTypes);
        ifNode.getPass().accept(this);
        final Block fail = ifNode.getFail();
        if (fail != null) {
            this.sb.append(" else ");
            fail.accept(this);
        }
        if (ifNode.getLocalVariableConversion() != null) {
            assert fail == null;
            this.sb.append(" else ");
            this.printLocalVariableConversion(ifNode);
            this.sb.append(";");
        }
        return false;
    }
    
    @Override
    public boolean enterLabelNode(final LabelNode labeledNode) {
        this.indent -= 4;
        this.indent();
        this.indent += 4;
        labeledNode.toString(this.sb, this.printTypes);
        labeledNode.getBody().accept(this);
        this.printLocalVariableConversion(labeledNode);
        return false;
    }
    
    @Override
    public boolean enterSplitNode(final SplitNode splitNode) {
        splitNode.toString(this.sb, this.printTypes);
        this.sb.append(this.EOLN);
        this.indent += 4;
        this.indent();
        return true;
    }
    
    @Override
    public Node leaveSplitNode(final SplitNode splitNode) {
        this.sb.append("</split>");
        this.sb.append(this.EOLN);
        this.indent -= 4;
        this.indent();
        return splitNode;
    }
    
    @Override
    public boolean enterSwitchNode(final SwitchNode switchNode) {
        switchNode.toString(this.sb, this.printTypes);
        this.sb.append(" {");
        final List<CaseNode> cases = switchNode.getCases();
        for (final CaseNode caseNode : cases) {
            this.sb.append(this.EOLN);
            this.indent();
            caseNode.toString(this.sb, this.printTypes);
            this.printLocalVariableConversion(caseNode);
            this.indent += 4;
            caseNode.getBody().accept(this);
            this.indent -= 4;
            this.sb.append(this.EOLN);
        }
        if (switchNode.getLocalVariableConversion() != null) {
            this.sb.append(this.EOLN);
            this.indent();
            this.sb.append("default: ");
            this.printLocalVariableConversion(switchNode);
            this.sb.append("{}");
        }
        this.sb.append(this.EOLN);
        this.indent();
        this.sb.append("}");
        return false;
    }
    
    @Override
    public boolean enterTryNode(final TryNode tryNode) {
        tryNode.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(tryNode);
        tryNode.getBody().accept(this);
        final List<Block> catchBlocks = tryNode.getCatchBlocks();
        for (final Block catchBlock : catchBlocks) {
            final CatchNode catchNode = catchBlock.getStatements().get(0);
            catchNode.toString(this.sb, this.printTypes);
            catchNode.getBody().accept(this);
        }
        final Block finallyBody = tryNode.getFinallyBody();
        if (finallyBody != null) {
            this.sb.append(" finally ");
            finallyBody.accept(this);
        }
        for (final Block inlinedFinally : tryNode.getInlinedFinallies()) {
            inlinedFinally.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        this.sb.append(varNode.isConst() ? "const " : (varNode.isLet() ? "let " : "var "));
        varNode.getName().toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(varNode.getName());
        final Node init = varNode.getInit();
        if (init != null) {
            this.sb.append(" = ");
            init.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean enterWhileNode(final WhileNode whileNode) {
        this.printLocalVariableConversion(whileNode);
        if (whileNode.isDoWhile()) {
            this.sb.append("do");
            whileNode.getBody().accept(this);
            this.sb.append(' ');
            whileNode.toString(this.sb, this.printTypes);
        }
        else {
            whileNode.toString(this.sb, this.printTypes);
            whileNode.getBody().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean enterWithNode(final WithNode withNode) {
        withNode.toString(this.sb, this.printTypes);
        withNode.getBody().accept(this);
        return false;
    }
}
