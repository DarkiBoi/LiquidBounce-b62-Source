// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.Expression;

final class BranchOptimizer
{
    private final CodeGenerator codegen;
    private final MethodEmitter method;
    
    BranchOptimizer(final CodeGenerator codegen, final MethodEmitter method) {
        this.codegen = codegen;
        this.method = method;
    }
    
    void execute(final Expression node, final Label label, final boolean state) {
        this.branchOptimizer(node, label, state);
    }
    
    private void branchOptimizer(final UnaryNode unaryNode, final Label label, final boolean state) {
        if (unaryNode.isTokenType(TokenType.NOT)) {
            this.branchOptimizer(unaryNode.getExpression(), label, !state);
        }
        else {
            this.loadTestAndJump(unaryNode, label, state);
        }
    }
    
    private void branchOptimizer(final BinaryNode binaryNode, final Label label, final boolean state) {
        final Expression lhs = binaryNode.lhs();
        final Expression rhs = binaryNode.rhs();
        switch (binaryNode.tokenType()) {
            case AND: {
                if (state) {
                    final Label skip = new Label("skip");
                    this.optimizeLogicalOperand(lhs, skip, false, false);
                    this.optimizeLogicalOperand(rhs, label, true, true);
                    this.method.label(skip);
                }
                else {
                    this.optimizeLogicalOperand(lhs, label, false, false);
                    this.optimizeLogicalOperand(rhs, label, false, true);
                }
            }
            case OR: {
                if (state) {
                    this.optimizeLogicalOperand(lhs, label, true, false);
                    this.optimizeLogicalOperand(rhs, label, true, true);
                }
                else {
                    final Label skip = new Label("skip");
                    this.optimizeLogicalOperand(lhs, skip, true, false);
                    this.optimizeLogicalOperand(rhs, label, false, true);
                    this.method.label(skip);
                }
            }
            case EQ:
            case EQ_STRICT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.EQ : Condition.NE, true, label);
            }
            case NE:
            case NE_STRICT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.NE : Condition.EQ, true, label);
            }
            case GE: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GE : Condition.LT, false, label);
            }
            case GT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GT : Condition.LE, false, label);
            }
            case LE: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LE : Condition.GT, true, label);
            }
            case LT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LT : Condition.GE, true, label);
            }
            default: {
                this.loadTestAndJump(binaryNode, label, state);
            }
        }
    }
    
    private void optimizeLogicalOperand(final Expression expr, final Label label, final boolean state, final boolean isRhs) {
        final JoinPredecessorExpression jpexpr = (JoinPredecessorExpression)expr;
        if (LocalVariableConversion.hasLiveConversion(jpexpr)) {
            final Label after = new Label("after");
            this.branchOptimizer(jpexpr.getExpression(), after, !state);
            this.method.beforeJoinPoint(jpexpr);
            this.method._goto(label);
            this.method.label(after);
            if (isRhs) {
                this.method.beforeJoinPoint(jpexpr);
            }
        }
        else {
            this.branchOptimizer(jpexpr.getExpression(), label, state);
        }
    }
    
    private void branchOptimizer(final Expression node, final Label label, final boolean state) {
        if (node instanceof BinaryNode) {
            this.branchOptimizer((BinaryNode)node, label, state);
            return;
        }
        if (node instanceof UnaryNode) {
            this.branchOptimizer((UnaryNode)node, label, state);
            return;
        }
        this.loadTestAndJump(node, label, state);
    }
    
    private void loadTestAndJump(final Expression node, final Label label, final boolean state) {
        this.codegen.loadExpressionAsBoolean(node);
        if (state) {
            this.method.ifne(label);
        }
        else {
            this.method.ifeq(label);
        }
    }
}
