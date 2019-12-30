// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.LexicalContext;

public abstract class NodeVisitor<T extends LexicalContext>
{
    protected final T lc;
    
    public NodeVisitor(final T lc) {
        this.lc = lc;
    }
    
    public T getLexicalContext() {
        return this.lc;
    }
    
    protected boolean enterDefault(final Node node) {
        return true;
    }
    
    protected Node leaveDefault(final Node node) {
        return node;
    }
    
    public boolean enterAccessNode(final AccessNode accessNode) {
        return this.enterDefault(accessNode);
    }
    
    public Node leaveAccessNode(final AccessNode accessNode) {
        return this.leaveDefault(accessNode);
    }
    
    public boolean enterBlock(final Block block) {
        return this.enterDefault(block);
    }
    
    public Node leaveBlock(final Block block) {
        return this.leaveDefault(block);
    }
    
    public boolean enterBinaryNode(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveBinaryNode(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterBreakNode(final BreakNode breakNode) {
        return this.enterDefault(breakNode);
    }
    
    public Node leaveBreakNode(final BreakNode breakNode) {
        return this.leaveDefault(breakNode);
    }
    
    public boolean enterCallNode(final CallNode callNode) {
        return this.enterDefault(callNode);
    }
    
    public Node leaveCallNode(final CallNode callNode) {
        return this.leaveDefault(callNode);
    }
    
    public boolean enterCaseNode(final CaseNode caseNode) {
        return this.enterDefault(caseNode);
    }
    
    public Node leaveCaseNode(final CaseNode caseNode) {
        return this.leaveDefault(caseNode);
    }
    
    public boolean enterCatchNode(final CatchNode catchNode) {
        return this.enterDefault(catchNode);
    }
    
    public Node leaveCatchNode(final CatchNode catchNode) {
        return this.leaveDefault(catchNode);
    }
    
    public boolean enterContinueNode(final ContinueNode continueNode) {
        return this.enterDefault(continueNode);
    }
    
    public Node leaveContinueNode(final ContinueNode continueNode) {
        return this.leaveDefault(continueNode);
    }
    
    public boolean enterEmptyNode(final EmptyNode emptyNode) {
        return this.enterDefault(emptyNode);
    }
    
    public Node leaveEmptyNode(final EmptyNode emptyNode) {
        return this.leaveDefault(emptyNode);
    }
    
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        return this.enterDefault(expressionStatement);
    }
    
    public Node leaveExpressionStatement(final ExpressionStatement expressionStatement) {
        return this.leaveDefault(expressionStatement);
    }
    
    public boolean enterBlockStatement(final BlockStatement blockStatement) {
        return this.enterDefault(blockStatement);
    }
    
    public Node leaveBlockStatement(final BlockStatement blockStatement) {
        return this.leaveDefault(blockStatement);
    }
    
    public boolean enterForNode(final ForNode forNode) {
        return this.enterDefault(forNode);
    }
    
    public Node leaveForNode(final ForNode forNode) {
        return this.leaveDefault(forNode);
    }
    
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        return this.enterDefault(functionNode);
    }
    
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        return this.leaveDefault(functionNode);
    }
    
    public boolean enterGetSplitState(final GetSplitState getSplitState) {
        return this.enterDefault(getSplitState);
    }
    
    public Node leaveGetSplitState(final GetSplitState getSplitState) {
        return this.leaveDefault(getSplitState);
    }
    
    public boolean enterIdentNode(final IdentNode identNode) {
        return this.enterDefault(identNode);
    }
    
    public Node leaveIdentNode(final IdentNode identNode) {
        return this.leaveDefault(identNode);
    }
    
    public boolean enterIfNode(final IfNode ifNode) {
        return this.enterDefault(ifNode);
    }
    
    public Node leaveIfNode(final IfNode ifNode) {
        return this.leaveDefault(ifNode);
    }
    
    public boolean enterIndexNode(final IndexNode indexNode) {
        return this.enterDefault(indexNode);
    }
    
    public Node leaveIndexNode(final IndexNode indexNode) {
        return this.leaveDefault(indexNode);
    }
    
    public boolean enterJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        return this.enterDefault(jumpToInlinedFinally);
    }
    
    public Node leaveJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        return this.leaveDefault(jumpToInlinedFinally);
    }
    
    public boolean enterLabelNode(final LabelNode labelNode) {
        return this.enterDefault(labelNode);
    }
    
    public Node leaveLabelNode(final LabelNode labelNode) {
        return this.leaveDefault(labelNode);
    }
    
    public boolean enterLiteralNode(final LiteralNode<?> literalNode) {
        return this.enterDefault(literalNode);
    }
    
    public Node leaveLiteralNode(final LiteralNode<?> literalNode) {
        return this.leaveDefault(literalNode);
    }
    
    public boolean enterObjectNode(final ObjectNode objectNode) {
        return this.enterDefault(objectNode);
    }
    
    public Node leaveObjectNode(final ObjectNode objectNode) {
        return this.leaveDefault(objectNode);
    }
    
    public boolean enterPropertyNode(final PropertyNode propertyNode) {
        return this.enterDefault(propertyNode);
    }
    
    public Node leavePropertyNode(final PropertyNode propertyNode) {
        return this.leaveDefault(propertyNode);
    }
    
    public boolean enterReturnNode(final ReturnNode returnNode) {
        return this.enterDefault(returnNode);
    }
    
    public Node leaveReturnNode(final ReturnNode returnNode) {
        return this.leaveDefault(returnNode);
    }
    
    public boolean enterRuntimeNode(final RuntimeNode runtimeNode) {
        return this.enterDefault(runtimeNode);
    }
    
    public Node leaveRuntimeNode(final RuntimeNode runtimeNode) {
        return this.leaveDefault(runtimeNode);
    }
    
    public boolean enterSetSplitState(final SetSplitState setSplitState) {
        return this.enterDefault(setSplitState);
    }
    
    public Node leaveSetSplitState(final SetSplitState setSplitState) {
        return this.leaveDefault(setSplitState);
    }
    
    public boolean enterSplitNode(final SplitNode splitNode) {
        return this.enterDefault(splitNode);
    }
    
    public Node leaveSplitNode(final SplitNode splitNode) {
        return this.leaveDefault(splitNode);
    }
    
    public boolean enterSplitReturn(final SplitReturn splitReturn) {
        return this.enterDefault(splitReturn);
    }
    
    public Node leaveSplitReturn(final SplitReturn splitReturn) {
        return this.leaveDefault(splitReturn);
    }
    
    public boolean enterSwitchNode(final SwitchNode switchNode) {
        return this.enterDefault(switchNode);
    }
    
    public Node leaveSwitchNode(final SwitchNode switchNode) {
        return this.leaveDefault(switchNode);
    }
    
    public boolean enterTernaryNode(final TernaryNode ternaryNode) {
        return this.enterDefault(ternaryNode);
    }
    
    public Node leaveTernaryNode(final TernaryNode ternaryNode) {
        return this.leaveDefault(ternaryNode);
    }
    
    public boolean enterThrowNode(final ThrowNode throwNode) {
        return this.enterDefault(throwNode);
    }
    
    public Node leaveThrowNode(final ThrowNode throwNode) {
        return this.leaveDefault(throwNode);
    }
    
    public boolean enterTryNode(final TryNode tryNode) {
        return this.enterDefault(tryNode);
    }
    
    public Node leaveTryNode(final TryNode tryNode) {
        return this.leaveDefault(tryNode);
    }
    
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveUnaryNode(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression expr) {
        return this.enterDefault(expr);
    }
    
    public Node leaveJoinPredecessorExpression(final JoinPredecessorExpression expr) {
        return this.leaveDefault(expr);
    }
    
    public boolean enterVarNode(final VarNode varNode) {
        return this.enterDefault(varNode);
    }
    
    public Node leaveVarNode(final VarNode varNode) {
        return this.leaveDefault(varNode);
    }
    
    public boolean enterWhileNode(final WhileNode whileNode) {
        return this.enterDefault(whileNode);
    }
    
    public Node leaveWhileNode(final WhileNode whileNode) {
        return this.leaveDefault(whileNode);
    }
    
    public boolean enterWithNode(final WithNode withNode) {
        return this.enterDefault(withNode);
    }
    
    public Node leaveWithNode(final WithNode withNode) {
        return this.leaveDefault(withNode);
    }
}
