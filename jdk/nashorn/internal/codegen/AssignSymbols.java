// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.BinaryNode;
import java.util.HashSet;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.CatchNode;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.util.ListIterator;
import java.util.List;
import java.util.Collection;
import jdk.nashorn.internal.ir.Statement;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.runtime.Context;
import java.util.HashMap;
import java.util.ArrayDeque;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Map;
import java.util.Set;
import java.util.Deque;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

@Logger(name = "symbols")
final class AssignSymbols extends SimpleNodeVisitor implements Loggable
{
    private final DebugLogger log;
    private final boolean debug;
    private final Deque<Set<String>> thisProperties;
    private final Map<String, Symbol> globalSymbols;
    private final Compiler compiler;
    private final boolean isOnDemand;
    
    private static boolean isParamOrVar(final IdentNode identNode) {
        final Symbol symbol = identNode.getSymbol();
        return symbol.isParam() || symbol.isVar();
    }
    
    private static String name(final Node node) {
        final String cn = node.getClass().getName();
        final int lastDot = cn.lastIndexOf(46);
        if (lastDot == -1) {
            return cn;
        }
        return cn.substring(lastDot + 1);
    }
    
    private static FunctionNode removeUnusedSlots(final FunctionNode functionNode) {
        if (!functionNode.needsCallee()) {
            functionNode.compilerConstant(CompilerConstants.CALLEE).setNeedsSlot(false);
        }
        if (!functionNode.hasScopeBlock() && !functionNode.needsParentScope()) {
            functionNode.compilerConstant(CompilerConstants.SCOPE).setNeedsSlot(false);
        }
        if (functionNode.isNamedFunctionExpression() && !functionNode.usesSelfSymbol()) {
            final Symbol selfSymbol = functionNode.getBody().getExistingSymbol(functionNode.getIdent().getName());
            if (selfSymbol != null && selfSymbol.isFunctionSelf()) {
                selfSymbol.setNeedsSlot(false);
                selfSymbol.clearFlag(2);
            }
        }
        return functionNode;
    }
    
    public AssignSymbols(final Compiler compiler) {
        this.thisProperties = new ArrayDeque<Set<String>>();
        this.globalSymbols = new HashMap<String, Symbol>();
        this.compiler = compiler;
        this.log = this.initLogger(compiler.getContext());
        this.debug = this.log.isEnabled();
        this.isOnDemand = compiler.isOnDemandCompilation();
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    private void acceptDeclarations(final FunctionNode functionNode, final Block body) {
        body.accept(new SimpleNodeVisitor() {
            @Override
            protected boolean enterDefault(final Node node) {
                return !(node instanceof Expression);
            }
            
            @Override
            public Node leaveVarNode(final VarNode varNode) {
                final IdentNode ident = varNode.getName();
                final boolean blockScoped = varNode.isBlockScoped();
                if (blockScoped && this.lc.inUnprotectedSwitchContext()) {
                    AssignSymbols.this.throwUnprotectedSwitchError(varNode);
                }
                final Block block = blockScoped ? this.lc.getCurrentBlock() : body;
                final Symbol symbol = AssignSymbols.this.defineSymbol(block, ident.getName(), ident, varNode.getSymbolFlags());
                if (varNode.isFunctionDeclaration()) {
                    symbol.setIsFunctionDeclaration();
                }
                return varNode.setName(ident.setSymbol(symbol));
            }
        });
    }
    
    private IdentNode compilerConstantIdentifier(final CompilerConstants cc) {
        return this.createImplicitIdentifier(cc.symbolName()).setSymbol(this.lc.getCurrentFunction().compilerConstant(cc));
    }
    
    private IdentNode createImplicitIdentifier(final String name) {
        final FunctionNode fn = this.lc.getCurrentFunction();
        return new IdentNode(fn.getToken(), fn.getFinish(), name);
    }
    
    private Symbol createSymbol(final String name, final int flags) {
        if ((flags & 0x3) == 0x1) {
            Symbol global = this.globalSymbols.get(name);
            if (global == null) {
                global = new Symbol(name, flags);
                this.globalSymbols.put(name, global);
            }
            return global;
        }
        return new Symbol(name, flags);
    }
    
    private VarNode createSyntheticInitializer(final IdentNode name, final CompilerConstants initConstant, final FunctionNode fn) {
        final IdentNode init = this.compilerConstantIdentifier(initConstant);
        assert init.getSymbol() != null && init.getSymbol().isBytecodeLocal();
        final VarNode synthVar = new VarNode(fn.getLineNumber(), fn.getToken(), fn.getFinish(), name, init);
        final Symbol nameSymbol = fn.getBody().getExistingSymbol(name.getName());
        assert nameSymbol != null;
        return (VarNode)synthVar.setName(name.setSymbol(nameSymbol)).accept(this);
    }
    
    private FunctionNode createSyntheticInitializers(final FunctionNode functionNode) {
        final List<VarNode> syntheticInitializers = new ArrayList<VarNode>(2);
        final Block body = functionNode.getBody();
        this.lc.push(body);
        try {
            if (functionNode.usesSelfSymbol()) {
                syntheticInitializers.add(this.createSyntheticInitializer(functionNode.getIdent(), CompilerConstants.CALLEE, functionNode));
            }
            if (functionNode.needsArguments()) {
                syntheticInitializers.add(this.createSyntheticInitializer(this.createImplicitIdentifier(CompilerConstants.ARGUMENTS_VAR.symbolName()), CompilerConstants.ARGUMENTS, functionNode));
            }
            if (syntheticInitializers.isEmpty()) {
                return functionNode;
            }
            final ListIterator<VarNode> it = syntheticInitializers.listIterator();
            while (it.hasNext()) {
                it.set((VarNode)it.next().accept(this));
            }
        }
        finally {
            this.lc.pop(body);
        }
        final List<Statement> stmts = body.getStatements();
        final List<Statement> newStatements = new ArrayList<Statement>(stmts.size() + syntheticInitializers.size());
        newStatements.addAll(syntheticInitializers);
        newStatements.addAll(stmts);
        return functionNode.setBody(this.lc, body.setStatements(this.lc, newStatements));
    }
    
    private Symbol defineSymbol(final Block block, final String name, final Node origin, final int symbolFlags) {
        int flags = symbolFlags;
        final boolean isBlockScope = (flags & 0x10) != 0x0 || (flags & 0x20) != 0x0;
        final boolean isGlobal = (flags & 0x3) == 0x1;
        Symbol symbol;
        FunctionNode function;
        if (isBlockScope) {
            symbol = block.getExistingSymbol(name);
            function = this.lc.getCurrentFunction();
        }
        else {
            symbol = this.findSymbol(block, name);
            function = this.lc.getFunction(block);
        }
        if (isGlobal) {
            flags |= 0x4;
        }
        if (this.lc.getCurrentFunction().isProgram()) {
            flags |= 0x200;
        }
        final boolean isParam = (flags & 0x3) == 0x3;
        final boolean isVar = (flags & 0x3) == 0x2;
        if (symbol != null) {
            if (isParam) {
                if (!this.isLocal(function, symbol)) {
                    symbol = null;
                }
                else if (symbol.isParam()) {
                    throw new AssertionError((Object)"duplicate parameter");
                }
            }
            else if (isVar) {
                if (isBlockScope) {
                    if (symbol.hasBeenDeclared()) {
                        this.throwParserException(ECMAErrors.getMessage("syntax.error.redeclare.variable", name), origin);
                    }
                    else {
                        symbol.setHasBeenDeclared();
                        if (function.isProgram() && function.getBody() == block) {
                            symbol.setIsScope();
                        }
                    }
                }
                else if ((flags & 0x40) != 0x0) {
                    symbol = null;
                }
                else {
                    if (symbol.isBlockScoped() && this.isLocal(this.lc.getCurrentFunction(), symbol)) {
                        this.throwParserException(ECMAErrors.getMessage("syntax.error.redeclare.variable", name), origin);
                    }
                    if (!this.isLocal(function, symbol) || symbol.less(2)) {
                        symbol = null;
                    }
                }
            }
        }
        if (symbol == null) {
            Block symbolBlock;
            if (isVar && ((flags & 0x40) != 0x0 || isBlockScope)) {
                symbolBlock = block;
            }
            else if (isGlobal) {
                symbolBlock = this.lc.getOutermostFunction().getBody();
            }
            else {
                symbolBlock = this.lc.getFunctionBody(function);
            }
            symbol = this.createSymbol(name, flags);
            symbolBlock.putSymbol(symbol);
            if ((flags & 0x4) == 0x0) {
                symbol.setNeedsSlot(true);
            }
        }
        else if (symbol.less(flags)) {
            symbol.setFlags(flags);
        }
        return symbol;
    }
    
    private <T extends Node> T end(final T node) {
        return this.end(node, true);
    }
    
    private <T extends Node> T end(final T node, final boolean printNode) {
        if (this.debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[LEAVE ").append(name(node)).append("] ").append(printNode ? node.toString() : "").append(" in '").append(this.lc.getCurrentFunction().getName()).append('\'');
            if (node instanceof IdentNode) {
                final Symbol symbol = ((IdentNode)node).getSymbol();
                if (symbol == null) {
                    sb.append(" <NO SYMBOL>");
                }
                else {
                    sb.append(" <symbol=").append(symbol).append('>');
                }
            }
            this.log.unindent();
            this.log.info(sb);
        }
        return node;
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        this.start(block);
        if (this.lc.isFunctionBody()) {
            assert !block.hasSymbols();
            final FunctionNode fn = this.lc.getCurrentFunction();
            if (this.isUnparsedFunction(fn)) {
                for (final String name : this.compiler.getScriptFunctionData(fn.getId()).getExternalSymbolNames()) {
                    this.nameIsUsed(name, null);
                }
                assert block.getStatements().isEmpty();
                return false;
            }
            else {
                this.enterFunctionBody();
            }
        }
        return true;
    }
    
    private boolean isUnparsedFunction(final FunctionNode fn) {
        return this.isOnDemand && fn != this.lc.getOutermostFunction();
    }
    
    @Override
    public boolean enterCatchNode(final CatchNode catchNode) {
        final IdentNode exception = catchNode.getException();
        final Block block = this.lc.getCurrentBlock();
        this.start(catchNode);
        final String exname = exception.getName();
        final boolean isInternal = exname.startsWith(CompilerConstants.EXCEPTION_PREFIX.symbolName());
        final Symbol symbol = this.defineSymbol(block, exname, catchNode, 0x12 | (isInternal ? 64 : 0) | 0x2000);
        symbol.clearFlag(16);
        return true;
    }
    
    private void enterFunctionBody() {
        final FunctionNode functionNode = this.lc.getCurrentFunction();
        final Block body = this.lc.getCurrentBlock();
        this.initFunctionWideVariables(functionNode, body);
        this.acceptDeclarations(functionNode, body);
        this.defineFunctionSelfSymbol(functionNode, body);
    }
    
    private void defineFunctionSelfSymbol(final FunctionNode functionNode, final Block body) {
        if (!functionNode.isNamedFunctionExpression()) {
            return;
        }
        final String name = functionNode.getIdent().getName();
        assert name != null;
        if (body.getExistingSymbol(name) != null) {
            return;
        }
        this.defineSymbol(body, name, functionNode, 8322);
        if (functionNode.allVarsInScope()) {
            this.lc.setFlag(functionNode, 16384);
        }
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        this.start(functionNode, false);
        this.thisProperties.push(new HashSet<String>());
        assert functionNode.getBody() != null;
        return true;
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        this.start(varNode);
        if (varNode.isFunctionDeclaration()) {
            this.defineVarIdent(varNode);
        }
        return true;
    }
    
    @Override
    public Node leaveVarNode(final VarNode varNode) {
        if (!varNode.isFunctionDeclaration()) {
            this.defineVarIdent(varNode);
        }
        return super.leaveVarNode(varNode);
    }
    
    private void defineVarIdent(final VarNode varNode) {
        final IdentNode ident = varNode.getName();
        int flags;
        if (!varNode.isBlockScoped() && this.lc.getCurrentFunction().isProgram()) {
            flags = 4;
        }
        else {
            flags = 0;
        }
        this.defineSymbol(this.lc.getCurrentBlock(), ident.getName(), ident, varNode.getSymbolFlags() | flags);
    }
    
    private Symbol exceptionSymbol() {
        return this.newObjectInternal(CompilerConstants.EXCEPTION_PREFIX);
    }
    
    private FunctionNode finalizeParameters(final FunctionNode functionNode) {
        final List<IdentNode> newParams = new ArrayList<IdentNode>();
        final boolean isVarArg = functionNode.isVarArg();
        final Block body = functionNode.getBody();
        for (final IdentNode param : functionNode.getParameters()) {
            final Symbol paramSymbol = body.getExistingSymbol(param.getName());
            assert paramSymbol != null;
            assert paramSymbol.isParam() : paramSymbol + " " + paramSymbol.getFlags();
            newParams.add(param.setSymbol(paramSymbol));
            if (!isVarArg) {
                continue;
            }
            paramSymbol.setNeedsSlot(false);
        }
        return functionNode.setParameters(this.lc, newParams);
    }
    
    private Symbol findSymbol(final Block block, final String name) {
        final Iterator<Block> blocks = this.lc.getBlocks(block);
        while (blocks.hasNext()) {
            final Symbol symbol = blocks.next().getExistingSymbol(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }
    
    private void functionUsesGlobalSymbol() {
        final Iterator<FunctionNode> fns = this.lc.getFunctions();
        while (fns.hasNext()) {
            this.lc.setFlag(fns.next(), 512);
        }
    }
    
    private void functionUsesScopeSymbol(final Symbol symbol) {
        final String name = symbol.getName();
        final Iterator<LexicalContextNode> contextNodeIter = this.lc.getAllNodes();
        while (contextNodeIter.hasNext()) {
            final LexicalContextNode node = contextNodeIter.next();
            if (node instanceof Block) {
                final Block block = (Block)node;
                if (block.getExistingSymbol(name) == null) {
                    continue;
                }
                assert this.lc.contains(block);
                this.lc.setBlockNeedsScope(block);
                break;
            }
            else {
                if (!(node instanceof FunctionNode)) {
                    continue;
                }
                this.lc.setFlag(node, 512);
            }
        }
    }
    
    private void functionUsesSymbol(final Symbol symbol) {
        assert symbol != null;
        if (symbol.isScope()) {
            if (symbol.isGlobal()) {
                this.functionUsesGlobalSymbol();
            }
            else {
                this.functionUsesScopeSymbol(symbol);
            }
        }
        else {
            assert !symbol.isGlobal();
        }
    }
    
    private void initCompileConstant(final CompilerConstants cc, final Block block, final int flags) {
        this.defineSymbol(block, cc.symbolName(), null, flags).setNeedsSlot(true);
    }
    
    private void initFunctionWideVariables(final FunctionNode functionNode, final Block body) {
        this.initCompileConstant(CompilerConstants.CALLEE, body, 8259);
        this.initCompileConstant(CompilerConstants.THIS, body, 8203);
        if (functionNode.isVarArg()) {
            this.initCompileConstant(CompilerConstants.VARARGS, body, 8259);
            if (functionNode.needsArguments()) {
                this.initCompileConstant(CompilerConstants.ARGUMENTS, body, 8258);
                this.defineSymbol(body, CompilerConstants.ARGUMENTS_VAR.symbolName(), null, 8194);
            }
        }
        this.initParameters(functionNode, body);
        this.initCompileConstant(CompilerConstants.SCOPE, body, 8258);
        this.initCompileConstant(CompilerConstants.RETURN, body, 66);
    }
    
    private void initParameters(final FunctionNode functionNode, final Block body) {
        final boolean isVarArg = functionNode.isVarArg();
        final boolean scopeParams = functionNode.allVarsInScope() || isVarArg;
        for (final IdentNode param : functionNode.getParameters()) {
            final Symbol symbol = this.defineSymbol(body, param.getName(), param, 3);
            if (scopeParams) {
                symbol.setIsScope();
                assert symbol.hasSlot();
                if (!isVarArg) {
                    continue;
                }
                symbol.setNeedsSlot(false);
            }
        }
    }
    
    private boolean isLocal(final FunctionNode function, final Symbol symbol) {
        final FunctionNode definingFn = this.lc.getDefiningFunction(symbol);
        assert definingFn != null;
        return definingFn == function;
    }
    
    @Override
    public Node leaveBinaryNode(final BinaryNode binaryNode) {
        if (binaryNode.isTokenType(TokenType.ASSIGN)) {
            return this.leaveASSIGN(binaryNode);
        }
        return super.leaveBinaryNode(binaryNode);
    }
    
    private Node leaveASSIGN(final BinaryNode binaryNode) {
        final Expression lhs = binaryNode.lhs();
        if (lhs instanceof AccessNode) {
            final AccessNode accessNode = (AccessNode)lhs;
            final Expression base = accessNode.getBase();
            if (base instanceof IdentNode) {
                final Symbol symbol = ((IdentNode)base).getSymbol();
                if (symbol.isThis()) {
                    this.thisProperties.peek().add(accessNode.getProperty());
                }
            }
        }
        return binaryNode;
    }
    
    @Override
    public Node leaveUnaryNode(final UnaryNode unaryNode) {
        switch (unaryNode.tokenType()) {
            case DELETE: {
                return this.leaveDELETE(unaryNode);
            }
            case TYPEOF: {
                return this.leaveTYPEOF(unaryNode);
            }
            default: {
                return super.leaveUnaryNode(unaryNode);
            }
        }
    }
    
    private Node leaveDELETE(final UnaryNode unaryNode) {
        final FunctionNode currentFunctionNode = this.lc.getCurrentFunction();
        final boolean strictMode = currentFunctionNode.isStrict();
        final Expression rhs = unaryNode.getExpression();
        final Expression strictFlagNode = (Expression)LiteralNode.newInstance(unaryNode, strictMode).accept(this);
        RuntimeNode.Request request = RuntimeNode.Request.DELETE;
        final List<Expression> args = new ArrayList<Expression>();
        if (rhs instanceof IdentNode) {
            final IdentNode ident = (IdentNode)rhs;
            final String name = ident.getName();
            final Symbol symbol = ident.getSymbol();
            if (symbol.isThis()) {
                return LiteralNode.newInstance(unaryNode, true);
            }
            final Expression literalNode = LiteralNode.newInstance(unaryNode, name);
            final boolean failDelete = strictMode || (!symbol.isScope() && (symbol.isParam() || (symbol.isVar() && !symbol.isProgramLevel())));
            if (!failDelete) {
                args.add(this.compilerConstantIdentifier(CompilerConstants.SCOPE));
            }
            args.add(literalNode);
            args.add(strictFlagNode);
            if (failDelete) {
                request = RuntimeNode.Request.FAIL_DELETE;
            }
            else if ((symbol.isGlobal() && !symbol.isFunctionDeclaration()) || symbol.isProgramLevel()) {
                request = RuntimeNode.Request.SLOW_DELETE;
            }
        }
        else if (rhs instanceof AccessNode) {
            final Expression base = ((AccessNode)rhs).getBase();
            final String property = ((AccessNode)rhs).getProperty();
            args.add(base);
            args.add(LiteralNode.newInstance(unaryNode, property));
            args.add(strictFlagNode);
        }
        else {
            if (!(rhs instanceof IndexNode)) {
                return LiteralNode.newInstance(unaryNode, true);
            }
            final IndexNode indexNode = (IndexNode)rhs;
            final Expression base2 = indexNode.getBase();
            final Expression index = indexNode.getIndex();
            args.add(base2);
            args.add(index);
            args.add(strictFlagNode);
        }
        return new RuntimeNode(unaryNode, request, args);
    }
    
    @Override
    public Node leaveForNode(final ForNode forNode) {
        if (forNode.isForIn()) {
            return forNode.setIterator(this.lc, this.newObjectInternal(CompilerConstants.ITERATOR_PREFIX));
        }
        return this.end(forNode);
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        FunctionNode finalizedFunction;
        if (this.isUnparsedFunction(functionNode)) {
            finalizedFunction = functionNode;
        }
        else {
            finalizedFunction = this.markProgramBlock(removeUnusedSlots(this.createSyntheticInitializers(this.finalizeParameters(this.lc.applyTopFlags(functionNode)))).setThisProperties(this.lc, this.thisProperties.pop().size()));
        }
        return finalizedFunction;
    }
    
    @Override
    public Node leaveIdentNode(final IdentNode identNode) {
        if (identNode.isPropertyName()) {
            return identNode;
        }
        final Symbol symbol = this.nameIsUsed(identNode.getName(), identNode);
        if (!identNode.isInitializedHere()) {
            symbol.increaseUseCount();
        }
        IdentNode newIdentNode = identNode.setSymbol(symbol);
        if (symbol.isBlockScoped() && !symbol.hasBeenDeclared() && !identNode.isDeclaredHere() && this.isLocal(this.lc.getCurrentFunction(), symbol)) {
            newIdentNode = newIdentNode.markDead();
        }
        return this.end(newIdentNode);
    }
    
    private Symbol nameIsUsed(final String name, final IdentNode origin) {
        final Block block = this.lc.getCurrentBlock();
        Symbol symbol = this.findSymbol(block, name);
        if (symbol != null) {
            this.log.info("Existing symbol = ", symbol);
            if (symbol.isFunctionSelf()) {
                final FunctionNode functionNode = this.lc.getDefiningFunction(symbol);
                assert functionNode != null;
                assert this.lc.getFunctionBody(functionNode).getExistingSymbol(CompilerConstants.CALLEE.symbolName()) != null;
                this.lc.setFlag(functionNode, 16384);
            }
            this.maybeForceScope(symbol);
        }
        else {
            this.log.info("No symbol exists. Declare as global: ", name);
            symbol = this.defineSymbol(block, name, origin, 5);
        }
        this.functionUsesSymbol(symbol);
        return symbol;
    }
    
    @Override
    public Node leaveSwitchNode(final SwitchNode switchNode) {
        if (!switchNode.isUniqueInteger()) {
            return switchNode.setTag(this.lc, this.newObjectInternal(CompilerConstants.SWITCH_TAG_PREFIX));
        }
        return switchNode;
    }
    
    @Override
    public Node leaveTryNode(final TryNode tryNode) {
        assert tryNode.getFinallyBody() == null;
        this.end(tryNode);
        return tryNode.setException(this.lc, this.exceptionSymbol());
    }
    
    private Node leaveTYPEOF(final UnaryNode unaryNode) {
        final Expression rhs = unaryNode.getExpression();
        final List<Expression> args = new ArrayList<Expression>();
        if (rhs instanceof IdentNode && !isParamOrVar((IdentNode)rhs)) {
            args.add(this.compilerConstantIdentifier(CompilerConstants.SCOPE));
            args.add(LiteralNode.newInstance(rhs, ((IdentNode)rhs).getName()));
        }
        else {
            args.add(rhs);
            args.add(LiteralNode.newInstance(unaryNode));
        }
        final Node runtimeNode = new RuntimeNode(unaryNode, RuntimeNode.Request.TYPEOF, args);
        this.end(unaryNode);
        return runtimeNode;
    }
    
    private FunctionNode markProgramBlock(final FunctionNode functionNode) {
        if (this.isOnDemand || !functionNode.isProgram()) {
            return functionNode;
        }
        return functionNode.setBody(this.lc, functionNode.getBody().setFlag(this.lc, 8));
    }
    
    private void maybeForceScope(final Symbol symbol) {
        if (!symbol.isScope() && this.symbolNeedsToBeScope(symbol)) {
            Symbol.setSymbolIsScope(this.lc, symbol);
        }
    }
    
    private Symbol newInternal(final CompilerConstants cc, final int flags) {
        return this.defineSymbol(this.lc.getCurrentBlock(), this.lc.getCurrentFunction().uniqueName(cc.symbolName()), null, 0x42 | flags);
    }
    
    private Symbol newObjectInternal(final CompilerConstants cc) {
        return this.newInternal(cc, 8192);
    }
    
    private boolean start(final Node node) {
        return this.start(node, true);
    }
    
    private boolean start(final Node node, final boolean printNode) {
        if (this.debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[ENTER ").append(name(node)).append("] ").append(printNode ? node.toString() : "").append(" in '").append(this.lc.getCurrentFunction().getName()).append("'");
            this.log.info(sb);
            this.log.indent();
        }
        return true;
    }
    
    private boolean symbolNeedsToBeScope(final Symbol symbol) {
        if (symbol.isThis() || symbol.isInternal()) {
            return false;
        }
        final FunctionNode func = this.lc.getCurrentFunction();
        if (func.allVarsInScope() || (!symbol.isBlockScoped() && func.isProgram())) {
            return true;
        }
        boolean previousWasBlock = false;
        final Iterator<LexicalContextNode> it = this.lc.getAllNodes();
        while (it.hasNext()) {
            final LexicalContextNode node = it.next();
            if (node instanceof FunctionNode || isSplitLiteral(node)) {
                return true;
            }
            if (node instanceof WithNode) {
                if (previousWasBlock) {
                    return true;
                }
                previousWasBlock = false;
            }
            else if (node instanceof Block) {
                if (((Block)node).getExistingSymbol(symbol.getName()) == symbol) {
                    return false;
                }
                previousWasBlock = true;
            }
            else {
                previousWasBlock = false;
            }
        }
        throw new AssertionError();
    }
    
    private static boolean isSplitLiteral(final LexicalContextNode expr) {
        return expr instanceof Splittable && ((Splittable)expr).getSplitRanges() != null;
    }
    
    private void throwUnprotectedSwitchError(final VarNode varNode) {
        final String msg = ECMAErrors.getMessage("syntax.error.unprotected.switch.declaration", varNode.isLet() ? "let" : "const");
        this.throwParserException(msg, varNode);
    }
    
    private void throwParserException(final String message, final Node origin) {
        if (origin == null) {
            throw new ParserException(message);
        }
        final Source source = this.compiler.getSource();
        final long token = origin.getToken();
        final int line = source.getLine(origin.getStart());
        final int column = source.getColumn(origin.getStart());
        final String formatted = ErrorManager.format(message, source, line, column, token);
        throw new ParserException(JSErrorType.SYNTAX_ERROR, formatted, source, line, column, token);
    }
}
