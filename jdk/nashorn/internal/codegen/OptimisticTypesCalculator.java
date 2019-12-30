// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.AccessNode;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

final class OptimisticTypesCalculator extends SimpleNodeVisitor
{
    final Compiler compiler;
    final Deque<BitSet> neverOptimistic;
    
    OptimisticTypesCalculator(final Compiler compiler) {
        this.neverOptimistic = new ArrayDeque<BitSet>();
        this.compiler = compiler;
    }
    
    @Override
    public boolean enterAccessNode(final AccessNode accessNode) {
        this.tagNeverOptimistic(accessNode.getBase());
        return true;
    }
    
    @Override
    public boolean enterPropertyNode(final PropertyNode propertyNode) {
        if (propertyNode.getKeyName().equals("__proto__")) {
            this.tagNeverOptimistic(propertyNode.getValue());
        }
        return super.enterPropertyNode(propertyNode);
    }
    
    @Override
    public boolean enterBinaryNode(final BinaryNode binaryNode) {
        if (binaryNode.isAssignment()) {
            final Expression lhs = binaryNode.lhs();
            if (!binaryNode.isSelfModifying()) {
                this.tagNeverOptimistic(lhs);
            }
            if (lhs instanceof IdentNode) {
                final Symbol symbol = ((IdentNode)lhs).getSymbol();
                if (symbol.isInternal() && !binaryNode.rhs().isSelfModifying()) {
                    this.tagNeverOptimistic(binaryNode.rhs());
                }
            }
        }
        else if (binaryNode.isTokenType(TokenType.INSTANCEOF)) {
            this.tagNeverOptimistic(binaryNode.lhs());
            this.tagNeverOptimistic(binaryNode.rhs());
        }
        return true;
    }
    
    @Override
    public boolean enterCallNode(final CallNode callNode) {
        this.tagNeverOptimistic(callNode.getFunction());
        return true;
    }
    
    @Override
    public boolean enterCatchNode(final CatchNode catchNode) {
        this.tagNeverOptimistic(catchNode.getExceptionCondition());
        return true;
    }
    
    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        final Expression expr = expressionStatement.getExpression();
        if (!expr.isSelfModifying()) {
            this.tagNeverOptimistic(expr);
        }
        return true;
    }
    
    @Override
    public boolean enterForNode(final ForNode forNode) {
        if (forNode.isForIn()) {
            this.tagNeverOptimistic(forNode.getModify());
        }
        else {
            this.tagNeverOptimisticLoopTest(forNode);
        }
        return true;
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (!this.neverOptimistic.isEmpty() && this.compiler.isOnDemandCompilation()) {
            return false;
        }
        this.neverOptimistic.push(new BitSet());
        return true;
    }
    
    @Override
    public boolean enterIfNode(final IfNode ifNode) {
        this.tagNeverOptimistic(ifNode.getTest());
        return true;
    }
    
    @Override
    public boolean enterIndexNode(final IndexNode indexNode) {
        this.tagNeverOptimistic(indexNode.getBase());
        return true;
    }
    
    @Override
    public boolean enterTernaryNode(final TernaryNode ternaryNode) {
        this.tagNeverOptimistic(ternaryNode.getTest());
        return true;
    }
    
    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        if (unaryNode.isTokenType(TokenType.NOT) || unaryNode.isTokenType(TokenType.NEW)) {
            this.tagNeverOptimistic(unaryNode.getExpression());
        }
        return true;
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        this.tagNeverOptimistic(varNode.getName());
        return true;
    }
    
    @Override
    public boolean enterWhileNode(final WhileNode whileNode) {
        this.tagNeverOptimisticLoopTest(whileNode);
        return true;
    }
    
    @Override
    protected Node leaveDefault(final Node node) {
        if (node instanceof Optimistic) {
            return this.leaveOptimistic((Optimistic)node);
        }
        return node;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        this.neverOptimistic.pop();
        return functionNode;
    }
    
    @Override
    public Node leaveIdentNode(final IdentNode identNode) {
        final Symbol symbol = identNode.getSymbol();
        if (symbol == null) {
            assert identNode.isPropertyName();
            return identNode;
        }
        else {
            if (symbol.isBytecodeLocal()) {
                return identNode;
            }
            if (symbol.isParam() && this.lc.getCurrentFunction().isVarArg()) {
                return identNode.setType(identNode.getMostPessimisticType());
            }
            assert symbol.isScope();
            return this.leaveOptimistic(identNode);
        }
    }
    
    private Expression leaveOptimistic(final Optimistic opt) {
        final int pp = opt.getProgramPoint();
        if (UnwarrantedOptimismException.isValid(pp) && !this.neverOptimistic.peek().get(pp)) {
            return (Expression)opt.setType(this.compiler.getOptimisticType(opt));
        }
        return (Expression)opt;
    }
    
    private void tagNeverOptimistic(final Expression expr) {
        if (expr instanceof Optimistic) {
            final int pp = ((Optimistic)expr).getProgramPoint();
            if (UnwarrantedOptimismException.isValid(pp)) {
                this.neverOptimistic.peek().set(pp);
            }
        }
    }
    
    private void tagNeverOptimisticLoopTest(final LoopNode loopNode) {
        final JoinPredecessorExpression test = loopNode.getTest();
        if (test != null) {
            this.tagNeverOptimistic(test.getExpression());
        }
    }
}
