// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.ReturnNode;
import java.util.Iterator;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.Splittable;
import java.util.List;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.Node;
import java.util.Map;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor;

final class WeighNodes extends NodeOperatorVisitor<LexicalContext>
{
    static final long FUNCTION_WEIGHT = 40L;
    static final long AASTORE_WEIGHT = 2L;
    static final long ACCESS_WEIGHT = 4L;
    static final long ADD_WEIGHT = 10L;
    static final long BREAK_WEIGHT = 1L;
    static final long CALL_WEIGHT = 10L;
    static final long CATCH_WEIGHT = 10L;
    static final long COMPARE_WEIGHT = 6L;
    static final long CONTINUE_WEIGHT = 1L;
    static final long IF_WEIGHT = 2L;
    static final long LITERAL_WEIGHT = 10L;
    static final long LOOP_WEIGHT = 4L;
    static final long NEW_WEIGHT = 6L;
    static final long FUNC_EXPR_WEIGHT = 20L;
    static final long RETURN_WEIGHT = 2L;
    static final long SPLIT_WEIGHT = 40L;
    static final long SWITCH_WEIGHT = 8L;
    static final long THROW_WEIGHT = 2L;
    static final long VAR_WEIGHT = 40L;
    static final long WITH_WEIGHT = 8L;
    static final long OBJECT_WEIGHT = 16L;
    static final long SETPROP_WEIGHT = 5L;
    private long weight;
    private final Map<Node, Long> weightCache;
    private final FunctionNode topFunction;
    
    private WeighNodes(final FunctionNode topFunction, final Map<Node, Long> weightCache) {
        super(new LexicalContext());
        this.topFunction = topFunction;
        this.weightCache = weightCache;
    }
    
    static long weigh(final Node node) {
        return weigh(node, null);
    }
    
    static long weigh(final Node node, final Map<Node, Long> weightCache) {
        final WeighNodes weighNodes = new WeighNodes((node instanceof FunctionNode) ? ((FunctionNode)node) : null, weightCache);
        node.accept(weighNodes);
        return weighNodes.weight;
    }
    
    @Override
    public Node leaveAccessNode(final AccessNode accessNode) {
        this.weight += 4L;
        return accessNode;
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        if (this.weightCache != null && this.weightCache.containsKey(block)) {
            this.weight += this.weightCache.get(block);
            return false;
        }
        return true;
    }
    
    @Override
    public Node leaveBreakNode(final BreakNode breakNode) {
        ++this.weight;
        return breakNode;
    }
    
    @Override
    public Node leaveCallNode(final CallNode callNode) {
        this.weight += 10L;
        return callNode;
    }
    
    @Override
    public Node leaveCatchNode(final CatchNode catchNode) {
        this.weight += 10L;
        return catchNode;
    }
    
    @Override
    public Node leaveContinueNode(final ContinueNode continueNode) {
        ++this.weight;
        return continueNode;
    }
    
    @Override
    public Node leaveExpressionStatement(final ExpressionStatement expressionStatement) {
        return expressionStatement;
    }
    
    @Override
    public Node leaveForNode(final ForNode forNode) {
        this.weight += 4L;
        return forNode;
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (functionNode == this.topFunction) {
            return true;
        }
        this.weight += 20L;
        return false;
    }
    
    @Override
    public Node leaveIdentNode(final IdentNode identNode) {
        this.weight += 4L;
        return identNode;
    }
    
    @Override
    public Node leaveIfNode(final IfNode ifNode) {
        this.weight += 2L;
        return ifNode;
    }
    
    @Override
    public Node leaveIndexNode(final IndexNode indexNode) {
        this.weight += 4L;
        return indexNode;
    }
    
    @Override
    public Node leaveJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        ++this.weight;
        return jumpToInlinedFinally;
    }
    
    @Override
    public boolean enterLiteralNode(final LiteralNode literalNode) {
        this.weight += 10L;
        if (literalNode instanceof LiteralNode.ArrayLiteralNode) {
            final LiteralNode.ArrayLiteralNode arrayLiteralNode = (LiteralNode.ArrayLiteralNode)literalNode;
            final Node[] value = ((LiteralNode<Node[]>)arrayLiteralNode).getValue();
            final int[] postsets = arrayLiteralNode.getPostsets();
            final List<Splittable.SplitRange> units = arrayLiteralNode.getSplitRanges();
            if (units == null) {
                for (final int postset : postsets) {
                    this.weight += 2L;
                    final Node element = value[postset];
                    if (element != null) {
                        element.accept(this);
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public boolean enterObjectNode(final ObjectNode objectNode) {
        this.weight += 16L;
        final List<PropertyNode> properties = objectNode.getElements();
        final boolean isSpillObject = properties.size() > CodeGenerator.OBJECT_SPILL_THRESHOLD;
        for (final PropertyNode property : properties) {
            if (!LiteralNode.isConstant(property.getValue())) {
                this.weight += 5L;
                property.getValue().accept(this);
            }
            else {
                if (isSpillObject) {
                    continue;
                }
                this.weight += 5L;
            }
        }
        return false;
    }
    
    @Override
    public Node leavePropertyNode(final PropertyNode propertyNode) {
        this.weight += 10L;
        return propertyNode;
    }
    
    @Override
    public Node leaveReturnNode(final ReturnNode returnNode) {
        this.weight += 2L;
        return returnNode;
    }
    
    @Override
    public Node leaveRuntimeNode(final RuntimeNode runtimeNode) {
        this.weight += 10L;
        return runtimeNode;
    }
    
    @Override
    public boolean enterSplitNode(final SplitNode splitNode) {
        this.weight += 40L;
        return false;
    }
    
    @Override
    public Node leaveSwitchNode(final SwitchNode switchNode) {
        this.weight += 8L;
        return switchNode;
    }
    
    @Override
    public Node leaveThrowNode(final ThrowNode throwNode) {
        this.weight += 2L;
        return throwNode;
    }
    
    @Override
    public Node leaveTryNode(final TryNode tryNode) {
        this.weight += 2L;
        return tryNode;
    }
    
    @Override
    public Node leaveVarNode(final VarNode varNode) {
        this.weight += 40L;
        return varNode;
    }
    
    @Override
    public Node leaveWhileNode(final WhileNode whileNode) {
        this.weight += 4L;
        return whileNode;
    }
    
    @Override
    public Node leaveWithNode(final WithNode withNode) {
        this.weight += 8L;
        return withNode;
    }
    
    @Override
    public Node leaveADD(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveBIT_NOT(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveDECINC(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveDELETE(final UnaryNode unaryNode) {
        return this.runtimeNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveNEW(final UnaryNode unaryNode) {
        this.weight += 6L;
        return unaryNode;
    }
    
    @Override
    public Node leaveNOT(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveSUB(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveTYPEOF(final UnaryNode unaryNode) {
        return this.runtimeNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveVOID(final UnaryNode unaryNode) {
        return this.unaryNodeWeight(unaryNode);
    }
    
    @Override
    public Node leaveADD(final BinaryNode binaryNode) {
        this.weight += 10L;
        return binaryNode;
    }
    
    @Override
    public Node leaveAND(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_ADD(final BinaryNode binaryNode) {
        this.weight += 10L;
        return binaryNode;
    }
    
    @Override
    public Node leaveASSIGN_BIT_AND(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_BIT_OR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_BIT_XOR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_DIV(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_MOD(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_MUL(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_SAR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_SHL(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_SHR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveASSIGN_SUB(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveBIND(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveBIT_AND(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveBIT_OR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveBIT_XOR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveCOMMALEFT(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveCOMMARIGHT(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveDIV(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveEQ(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveEQ_STRICT(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveGE(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveGT(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveIN(final BinaryNode binaryNode) {
        this.weight += 10L;
        return binaryNode;
    }
    
    @Override
    public Node leaveINSTANCEOF(final BinaryNode binaryNode) {
        this.weight += 10L;
        return binaryNode;
    }
    
    @Override
    public Node leaveLE(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveLT(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveMOD(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveMUL(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveNE(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveNE_STRICT(final BinaryNode binaryNode) {
        return this.compareWeight(binaryNode);
    }
    
    @Override
    public Node leaveOR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveSAR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveSHL(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveSHR(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    @Override
    public Node leaveSUB(final BinaryNode binaryNode) {
        return this.binaryNodeWeight(binaryNode);
    }
    
    private Node unaryNodeWeight(final UnaryNode unaryNode) {
        ++this.weight;
        return unaryNode;
    }
    
    private Node binaryNodeWeight(final BinaryNode binaryNode) {
        ++this.weight;
        return binaryNode;
    }
    
    private Node runtimeNodeWeight(final UnaryNode unaryNode) {
        this.weight += 10L;
        return unaryNode;
    }
    
    private Node compareWeight(final BinaryNode binaryNode) {
        this.weight += 6L;
        return binaryNode;
    }
}
