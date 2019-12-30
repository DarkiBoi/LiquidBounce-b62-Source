// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Iterator;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.runtime.Context;
import java.util.HashSet;
import java.util.HashMap;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.ir.Block;
import java.util.Set;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import java.util.Map;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

@Logger(name = "scopedepths")
final class FindScopeDepths extends SimpleNodeVisitor implements Loggable
{
    private final Compiler compiler;
    private final Map<Integer, Map<Integer, RecompilableScriptFunctionData>> fnIdToNestedFunctions;
    private final Map<Integer, Map<String, Integer>> externalSymbolDepths;
    private final Map<Integer, Set<String>> internalSymbols;
    private final Set<Block> withBodies;
    private final DebugLogger log;
    private int dynamicScopeCount;
    
    FindScopeDepths(final Compiler compiler) {
        this.fnIdToNestedFunctions = new HashMap<Integer, Map<Integer, RecompilableScriptFunctionData>>();
        this.externalSymbolDepths = new HashMap<Integer, Map<String, Integer>>();
        this.internalSymbols = new HashMap<Integer, Set<String>>();
        this.withBodies = new HashSet<Block>();
        this.compiler = compiler;
        this.log = this.initLogger(compiler.getContext());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    static int findScopesToStart(final LexicalContext lc, final FunctionNode fn, final Block block) {
        final Block bodyBlock = findBodyBlock(lc, fn, block);
        final Iterator<Block> iter = lc.getBlocks(block);
        Block b = iter.next();
        int scopesToStart = 0;
        while (true) {
            if (b.needsScope()) {
                ++scopesToStart;
            }
            if (b == bodyBlock) {
                break;
            }
            b = iter.next();
        }
        return scopesToStart;
    }
    
    static int findInternalDepth(final LexicalContext lc, final FunctionNode fn, final Block block, final Symbol symbol) {
        final Block bodyBlock = findBodyBlock(lc, fn, block);
        final Iterator<Block> iter = lc.getBlocks(block);
        Block b = iter.next();
        int scopesToStart = 0;
        while (!definedInBlock(b, symbol)) {
            if (b.needsScope()) {
                ++scopesToStart;
            }
            if (b == bodyBlock) {
                return -1;
            }
            b = iter.next();
        }
        return scopesToStart;
    }
    
    private static boolean definedInBlock(final Block block, final Symbol symbol) {
        if (symbol.isGlobal()) {
            return block.isGlobalScope();
        }
        return block.getExistingSymbol(symbol.getName()) == symbol;
    }
    
    static Block findBodyBlock(final LexicalContext lc, final FunctionNode fn, final Block block) {
        final Iterator<Block> iter = lc.getBlocks(block);
        while (iter.hasNext()) {
            final Block next = iter.next();
            if (fn.getBody() == next) {
                return next;
            }
        }
        return null;
    }
    
    private static Block findGlobalBlock(final LexicalContext lc, final Block block) {
        final Iterator<Block> iter = lc.getBlocks(block);
        Block globalBlock = null;
        while (iter.hasNext()) {
            globalBlock = iter.next();
        }
        return globalBlock;
    }
    
    private static boolean isDynamicScopeBoundary(final FunctionNode fn) {
        return fn.needsDynamicScope();
    }
    
    private boolean isDynamicScopeBoundary(final Block block) {
        return this.withBodies.contains(block);
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (isDynamicScopeBoundary(functionNode)) {
            this.increaseDynamicScopeCount(functionNode);
        }
        final int fnId = functionNode.getId();
        Map<Integer, RecompilableScriptFunctionData> nestedFunctions = this.fnIdToNestedFunctions.get(fnId);
        if (nestedFunctions == null) {
            nestedFunctions = new HashMap<Integer, RecompilableScriptFunctionData>();
            this.fnIdToNestedFunctions.put(fnId, nestedFunctions);
        }
        return true;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        final String name = functionNode.getName();
        FunctionNode newFunctionNode = functionNode;
        if (this.compiler.isOnDemandCompilation()) {
            final RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(newFunctionNode.getId());
            if (data.inDynamicContext()) {
                this.log.fine("Reviving scriptfunction ", DebugLogger.quote(name), " as defined in previous (now lost) dynamic scope.");
                newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
            }
            if (newFunctionNode == this.lc.getOutermostFunction() && !newFunctionNode.hasApplyToCallSpecialization()) {
                data.setCachedAst(newFunctionNode);
            }
            return newFunctionNode;
        }
        if (this.inDynamicScope()) {
            this.log.fine("Tagging ", DebugLogger.quote(name), " as defined in dynamic scope");
            newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
        }
        final int fnId = newFunctionNode.getId();
        final Map<Integer, RecompilableScriptFunctionData> nestedFunctions = this.fnIdToNestedFunctions.remove(fnId);
        assert nestedFunctions != null;
        final RecompilableScriptFunctionData data2 = new RecompilableScriptFunctionData(newFunctionNode, this.compiler.getCodeInstaller(), ObjectClassGenerator.createAllocationStrategy(newFunctionNode.getThisProperties(), this.compiler.getContext().useDualFields()), nestedFunctions, this.externalSymbolDepths.get(fnId), this.internalSymbols.get(fnId));
        if (this.lc.getOutermostFunction() != newFunctionNode) {
            final FunctionNode parentFn = this.lc.getParentFunction(newFunctionNode);
            if (parentFn != null) {
                this.fnIdToNestedFunctions.get(parentFn.getId()).put(fnId, data2);
            }
        }
        else {
            this.compiler.setData(data2);
        }
        if (isDynamicScopeBoundary(functionNode)) {
            this.decreaseDynamicScopeCount(functionNode);
        }
        return newFunctionNode;
    }
    
    private boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }
    
    private void increaseDynamicScopeCount(final Node node) {
        assert this.dynamicScopeCount >= 0;
        ++this.dynamicScopeCount;
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " ++dynamicScopeCount = ", this.dynamicScopeCount, " at: ", node, node.getClass());
        }
    }
    
    private void decreaseDynamicScopeCount(final Node node) {
        --this.dynamicScopeCount;
        assert this.dynamicScopeCount >= 0;
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " --dynamicScopeCount = ", this.dynamicScopeCount, " at: ", node, node.getClass());
        }
    }
    
    @Override
    public boolean enterWithNode(final WithNode node) {
        this.withBodies.add(node.getBody());
        return true;
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (this.isDynamicScopeBoundary(block)) {
            this.increaseDynamicScopeCount(block);
        }
        if (!this.lc.isFunctionBody()) {
            return true;
        }
        final FunctionNode fn = this.lc.getCurrentFunction();
        final Set<Symbol> symbols = new HashSet<Symbol>();
        block.accept(new SimpleNodeVisitor() {
            @Override
            public boolean enterIdentNode(final IdentNode identNode) {
                final Symbol symbol = identNode.getSymbol();
                if (symbol != null && symbol.isScope()) {
                    symbols.add(symbol);
                }
                return true;
            }
        });
        final Map<String, Integer> internals = new HashMap<String, Integer>();
        final Block globalBlock = findGlobalBlock(this.lc, block);
        final Block bodyBlock = findBodyBlock(this.lc, fn, block);
        assert globalBlock != null;
        assert bodyBlock != null;
        for (final Symbol symbol : symbols) {
            final int internalDepth = findInternalDepth(this.lc, fn, block, symbol);
            final boolean internal = internalDepth >= 0;
            if (internal) {
                internals.put(symbol.getName(), internalDepth);
            }
            if (!internal) {
                int depthAtStart = 0;
                final Iterator<Block> iter = this.lc.getAncestorBlocks(bodyBlock);
                while (iter.hasNext()) {
                    final Block b2 = iter.next();
                    if (definedInBlock(b2, symbol)) {
                        this.addExternalSymbol(fn, symbol, depthAtStart);
                        break;
                    }
                    if (!b2.needsScope()) {
                        continue;
                    }
                    ++depthAtStart;
                }
            }
        }
        this.addInternalSymbols(fn, internals.keySet());
        if (this.log.isEnabled()) {
            this.log.info(fn.getName() + " internals=" + internals + " externals=" + this.externalSymbolDepths.get(fn.getId()));
        }
        return true;
    }
    
    @Override
    public Node leaveBlock(final Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return block;
        }
        if (this.isDynamicScopeBoundary(block)) {
            this.decreaseDynamicScopeCount(block);
        }
        return block;
    }
    
    private void addInternalSymbols(final FunctionNode functionNode, final Set<String> symbols) {
        final int fnId = functionNode.getId();
        assert !(!this.internalSymbols.get(fnId).equals(symbols));
        this.internalSymbols.put(fnId, symbols);
    }
    
    private void addExternalSymbol(final FunctionNode functionNode, final Symbol symbol, final int depthAtStart) {
        final int fnId = functionNode.getId();
        Map<String, Integer> depths = this.externalSymbolDepths.get(fnId);
        if (depths == null) {
            depths = new HashMap<String, Integer>();
            this.externalSymbolDepths.put(fnId, depths);
        }
        depths.put(symbol.getName(), depthAtStart);
    }
}
