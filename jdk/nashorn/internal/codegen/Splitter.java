// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.SplitNode;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Statement;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.LexicalContextNode;
import java.util.List;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.Context;
import java.util.HashMap;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.ir.Node;
import java.util.Map;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

@Logger(name = "splitter")
final class Splitter extends SimpleNodeVisitor implements Loggable
{
    private final Compiler compiler;
    private final FunctionNode outermost;
    private final CompileUnit outermostCompileUnit;
    private final Map<Node, Long> weightCache;
    public static final long SPLIT_THRESHOLD;
    private final DebugLogger log;
    
    public Splitter(final Compiler compiler, final FunctionNode functionNode, final CompileUnit outermostCompileUnit) {
        this.weightCache = new HashMap<Node, Long>();
        this.compiler = compiler;
        this.outermost = functionNode;
        this.outermostCompileUnit = outermostCompileUnit;
        this.log = this.initLogger(compiler.getContext());
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    FunctionNode split(final FunctionNode fn, final boolean top) {
        FunctionNode functionNode = fn;
        this.log.fine("Initiating split of '", functionNode.getName(), "'");
        long weight = WeighNodes.weigh(functionNode);
        assert this.lc.isEmpty() : "LexicalContext not empty";
        if (weight >= Splitter.SPLIT_THRESHOLD) {
            this.log.info("Splitting '", functionNode.getName(), "' as its weight ", weight, " exceeds split threshold ", Splitter.SPLIT_THRESHOLD);
            functionNode = (FunctionNode)functionNode.accept(this);
            if (functionNode.isSplit()) {
                weight = WeighNodes.weigh(functionNode, this.weightCache);
                functionNode = functionNode.setBody(null, functionNode.getBody().setNeedsScope(null));
            }
            if (weight >= Splitter.SPLIT_THRESHOLD) {
                functionNode = functionNode.setBody(null, this.splitBlock(functionNode.getBody(), functionNode));
                functionNode = functionNode.setFlag(null, 16);
                weight = WeighNodes.weigh(functionNode.getBody(), this.weightCache);
            }
        }
        assert functionNode.getCompileUnit() == null : "compile unit already set for " + functionNode.getName();
        if (top) {
            assert this.outermostCompileUnit != null : "outermost compile unit is null";
            functionNode = functionNode.setCompileUnit(null, this.outermostCompileUnit);
            this.outermostCompileUnit.addWeight(weight + 40L);
        }
        else {
            functionNode = functionNode.setCompileUnit(null, this.findUnit(weight));
        }
        final Block body = functionNode.getBody();
        final List<FunctionNode> dc = directChildren(functionNode);
        final Block newBody = (Block)body.accept(new SimpleNodeVisitor() {
            @Override
            public boolean enterFunctionNode(final FunctionNode nestedFunction) {
                return dc.contains(nestedFunction);
            }
            
            @Override
            public Node leaveFunctionNode(final FunctionNode nestedFunction) {
                final FunctionNode split = new Splitter(Splitter.this.compiler, nestedFunction, Splitter.this.outermostCompileUnit).split(nestedFunction, false);
                this.lc.replace(nestedFunction, split);
                return split;
            }
        });
        functionNode = functionNode.setBody(null, newBody);
        assert functionNode.getCompileUnit() != null;
        return functionNode;
    }
    
    private static List<FunctionNode> directChildren(final FunctionNode functionNode) {
        final List<FunctionNode> dc = new ArrayList<FunctionNode>();
        functionNode.accept(new SimpleNodeVisitor() {
            @Override
            public boolean enterFunctionNode(final FunctionNode child) {
                if (child == functionNode) {
                    return true;
                }
                if (this.lc.getParentFunction(child) == functionNode) {
                    dc.add(child);
                }
                return false;
            }
        });
        return dc;
    }
    
    protected CompileUnit findUnit(final long weight) {
        return this.compiler.findUnit(weight);
    }
    
    private Block splitBlock(final Block block, final FunctionNode function) {
        final List<Statement> splits = new ArrayList<Statement>();
        List<Statement> statements = new ArrayList<Statement>();
        long statementsWeight = 0L;
        for (final Statement statement : block.getStatements()) {
            final long weight = WeighNodes.weigh(statement, this.weightCache);
            if ((statementsWeight + weight >= Splitter.SPLIT_THRESHOLD || statement.isTerminal()) && !statements.isEmpty()) {
                splits.add(this.createBlockSplitNode(block, function, statements, statementsWeight));
                statements = new ArrayList<Statement>();
                statementsWeight = 0L;
            }
            if (statement.isTerminal()) {
                splits.add(statement);
            }
            else {
                statements.add(statement);
                statementsWeight += weight;
            }
        }
        if (!statements.isEmpty()) {
            splits.add(this.createBlockSplitNode(block, function, statements, statementsWeight));
        }
        return block.setStatements(this.lc, splits);
    }
    
    private SplitNode createBlockSplitNode(final Block parent, final FunctionNode function, final List<Statement> statements, final long weight) {
        final long token = parent.getToken();
        final int finish = parent.getFinish();
        final String name = function.uniqueName(CompilerConstants.SPLIT_PREFIX.symbolName());
        final Block newBlock = new Block(token, finish, statements);
        return new SplitNode(name, newBlock, this.compiler.findUnit(weight + 40L));
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        if (block.isCatchBlock()) {
            return false;
        }
        final long weight = WeighNodes.weigh(block, this.weightCache);
        if (weight < Splitter.SPLIT_THRESHOLD) {
            this.weightCache.put(block, weight);
            return false;
        }
        return true;
    }
    
    @Override
    public Node leaveBlock(final Block block) {
        assert !block.isCatchBlock();
        Block newBlock = block;
        long weight = WeighNodes.weigh(block, this.weightCache);
        if (weight >= Splitter.SPLIT_THRESHOLD) {
            final FunctionNode currentFunction = this.lc.getCurrentFunction();
            newBlock = this.splitBlock(block, currentFunction);
            weight = WeighNodes.weigh(newBlock, this.weightCache);
            this.lc.setFlag(currentFunction, 16);
        }
        this.weightCache.put(newBlock, weight);
        return newBlock;
    }
    
    @Override
    public Node leaveLiteralNode(final LiteralNode literal) {
        long weight = WeighNodes.weigh(literal);
        if (weight < Splitter.SPLIT_THRESHOLD) {
            return literal;
        }
        final FunctionNode functionNode = this.lc.getCurrentFunction();
        this.lc.setFlag(functionNode, 16);
        if (literal instanceof LiteralNode.ArrayLiteralNode) {
            final LiteralNode.ArrayLiteralNode arrayLiteralNode = (LiteralNode.ArrayLiteralNode)literal;
            final Node[] value = ((LiteralNode<Node[]>)arrayLiteralNode).getValue();
            final int[] postsets = arrayLiteralNode.getPostsets();
            final List<Splittable.SplitRange> ranges = new ArrayList<Splittable.SplitRange>();
            long totalWeight = 0L;
            int lo = 0;
            for (int i = 0; i < postsets.length; ++i) {
                final int postset = postsets[i];
                final Node element = value[postset];
                weight = WeighNodes.weigh(element);
                totalWeight += 2L + weight;
                if (totalWeight >= Splitter.SPLIT_THRESHOLD) {
                    final CompileUnit unit = this.compiler.findUnit(totalWeight - weight);
                    ranges.add(new Splittable.SplitRange(unit, lo, i));
                    lo = i;
                    totalWeight = weight;
                }
            }
            if (lo != postsets.length) {
                final CompileUnit unit2 = this.compiler.findUnit(totalWeight);
                ranges.add(new Splittable.SplitRange(unit2, lo, postsets.length));
            }
            return arrayLiteralNode.setSplitRanges(this.lc, ranges);
        }
        return literal;
    }
    
    @Override
    public Node leaveObjectNode(final ObjectNode objectNode) {
        long weight = WeighNodes.weigh(objectNode);
        if (weight < Splitter.SPLIT_THRESHOLD) {
            return objectNode;
        }
        final FunctionNode functionNode = this.lc.getCurrentFunction();
        this.lc.setFlag(functionNode, 16);
        final List<Splittable.SplitRange> ranges = new ArrayList<Splittable.SplitRange>();
        final List<PropertyNode> properties = objectNode.getElements();
        final boolean isSpillObject = properties.size() > CodeGenerator.OBJECT_SPILL_THRESHOLD;
        long totalWeight = 0L;
        int lo = 0;
        for (int i = 0; i < properties.size(); ++i) {
            final PropertyNode property = properties.get(i);
            final boolean isConstant = LiteralNode.isConstant(property.getValue());
            if (!isConstant || !isSpillObject) {
                weight = (isConstant ? 0L : WeighNodes.weigh(property.getValue()));
                totalWeight += 2L + weight;
                if (totalWeight >= Splitter.SPLIT_THRESHOLD) {
                    final CompileUnit unit = this.compiler.findUnit(totalWeight - weight);
                    ranges.add(new Splittable.SplitRange(unit, lo, i));
                    lo = i;
                    totalWeight = weight;
                }
            }
        }
        if (lo != properties.size()) {
            final CompileUnit unit2 = this.compiler.findUnit(totalWeight);
            ranges.add(new Splittable.SplitRange(unit2, lo, properties.size()));
        }
        return objectNode.setSplitRanges(this.lc, ranges);
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode node) {
        return node == this.outermost;
    }
    
    static {
        SPLIT_THRESHOLD = Options.getIntProperty("nashorn.compiler.splitter.threshold", 32768);
    }
}
