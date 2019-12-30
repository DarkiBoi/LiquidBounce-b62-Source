// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.HashSet;
import jdk.nashorn.internal.ir.Node;
import java.util.Set;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

class ProgramPoints extends SimpleNodeVisitor
{
    private final IntDeque nextProgramPoint;
    private final Set<Node> noProgramPoint;
    
    ProgramPoints() {
        this.nextProgramPoint = new IntDeque();
        this.noProgramPoint = new HashSet<Node>();
    }
    
    private int next() {
        final int next = this.nextProgramPoint.getAndIncrement();
        if (next > 2097151) {
            throw new AssertionError((Object)"Function has more than 2097151 program points");
        }
        return next;
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        this.nextProgramPoint.push(1);
        return true;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        this.nextProgramPoint.pop();
        return functionNode;
    }
    
    private Expression setProgramPoint(final Optimistic optimistic) {
        if (this.noProgramPoint.contains(optimistic)) {
            return (Expression)optimistic;
        }
        return (Expression)(optimistic.canBeOptimistic() ? optimistic.setProgramPoint(this.next()) : optimistic);
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        this.noProgramPoint.add(varNode.getName());
        return true;
    }
    
    @Override
    public boolean enterIdentNode(final IdentNode identNode) {
        if (identNode.isInternal()) {
            this.noProgramPoint.add(identNode);
        }
        return true;
    }
    
    @Override
    public Node leaveIdentNode(final IdentNode identNode) {
        if (identNode.isPropertyName()) {
            return identNode;
        }
        return this.setProgramPoint(identNode);
    }
    
    @Override
    public Node leaveCallNode(final CallNode callNode) {
        return this.setProgramPoint(callNode);
    }
    
    @Override
    public Node leaveAccessNode(final AccessNode accessNode) {
        return this.setProgramPoint(accessNode);
    }
    
    @Override
    public Node leaveIndexNode(final IndexNode indexNode) {
        return this.setProgramPoint(indexNode);
    }
    
    @Override
    public Node leaveBinaryNode(final BinaryNode binaryNode) {
        return this.setProgramPoint(binaryNode);
    }
    
    @Override
    public Node leaveUnaryNode(final UnaryNode unaryNode) {
        return this.setProgramPoint(unaryNode);
    }
}
