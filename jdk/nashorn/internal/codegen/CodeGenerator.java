// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.LinkedList;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;
import jdk.nashorn.internal.objects.Global;
import java.util.BitSet;
import jdk.nashorn.internal.AssertsEnabled;
import jdk.nashorn.internal.runtime.RewriteException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import java.io.PrintWriter;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.ir.ThrowNode;
import java.util.Arrays;
import jdk.nashorn.internal.ir.CaseNode;
import java.util.TreeMap;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitReturn;
import java.util.Collection;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.ir.PropertyNode;
import java.util.function.Supplier;
import java.util.EnumSet;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import java.util.List;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.ir.LexicalContext;
import java.util.Iterator;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.runtime.Context;
import java.util.ArrayDeque;
import java.util.HashSet;
import jdk.nashorn.internal.IntDeque;
import java.util.Deque;
import java.util.Set;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor;

@Logger(name = "codegen")
final class CodeGenerator extends NodeOperatorVisitor<CodeGeneratorLexicalContext> implements Loggable
{
    private static final Type SCOPE_TYPE;
    private static final String GLOBAL_OBJECT;
    private static final CompilerConstants.Call CREATE_REWRITE_EXCEPTION;
    private static final CompilerConstants.Call CREATE_REWRITE_EXCEPTION_REST_OF;
    private static final CompilerConstants.Call ENSURE_INT;
    private static final CompilerConstants.Call ENSURE_NUMBER;
    private static final CompilerConstants.Call CREATE_FUNCTION_OBJECT;
    private static final CompilerConstants.Call CREATE_FUNCTION_OBJECT_NO_SCOPE;
    private static final CompilerConstants.Call TO_NUMBER_FOR_EQ;
    private static final CompilerConstants.Call TO_NUMBER_FOR_STRICT_EQ;
    private static final Class<?> ITERATOR_CLASS;
    private static final Type ITERATOR_TYPE;
    private static final Type EXCEPTION_TYPE;
    private static final Integer INT_ZERO;
    private final Compiler compiler;
    private final boolean evalCode;
    private final int callSiteFlags;
    private int regexFieldCount;
    private int lastLineNumber;
    private static final int MAX_REGEX_FIELDS = 2048;
    private MethodEmitter method;
    private CompileUnit unit;
    private final DebugLogger log;
    static final int OBJECT_SPILL_THRESHOLD;
    private final Set<String> emittedMethods;
    private ContinuationInfo continuationInfo;
    private final Deque<Label> scopeEntryLabels;
    private static final Label METHOD_BOUNDARY;
    private final Deque<Label> catchLabels;
    private final IntDeque labeledBlockBreakLiveLocals;
    private final int[] continuationEntryPoints;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    CodeGenerator(final Compiler compiler, final int[] continuationEntryPoints) {
        super(new CodeGeneratorLexicalContext());
        this.lastLineNumber = -1;
        this.emittedMethods = new HashSet<String>();
        this.scopeEntryLabels = new ArrayDeque<Label>();
        this.catchLabels = new ArrayDeque<Label>();
        this.labeledBlockBreakLiveLocals = new IntDeque();
        this.compiler = compiler;
        this.evalCode = compiler.getSource().isEvalCode();
        this.continuationEntryPoints = continuationEntryPoints;
        this.callSiteFlags = compiler.getScriptEnvironment()._callsite_flags;
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
    
    int getCallSiteFlags() {
        return ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().getCallSiteFlags() | this.callSiteFlags;
    }
    
    private int getScopeCallSiteFlags(final Symbol symbol) {
        assert symbol.isScope();
        final int flags = this.getCallSiteFlags() | 0x1;
        if (this.isEvalCode() && symbol.isGlobal()) {
            return flags;
        }
        return this.isFastScope(symbol) ? (flags | 0x4) : flags;
    }
    
    boolean isEvalCode() {
        return this.evalCode;
    }
    
    boolean useDualFields() {
        return this.compiler.getContext().useDualFields();
    }
    
    private MethodEmitter loadIdent(final IdentNode identNode, final TypeBounds resultBounds) {
        this.checkTemporalDeadZone(identNode);
        final Symbol symbol = identNode.getSymbol();
        if (!symbol.isScope()) {
            final Type type = identNode.getType();
            if (type == Type.UNDEFINED) {
                return this.method.loadUndefined(resultBounds.widest);
            }
            assert symbol.hasSlot() || symbol.isParam();
            return this.method.load(identNode);
        }
        else {
            assert identNode.getSymbol().isScope() : identNode + " is not in scope!";
            final int flags = this.getScopeCallSiteFlags(symbol);
            if (this.isFastScope(symbol)) {
                if (symbol.getUseCount() > 200 && !identNode.isOptimistic()) {
                    new OptimisticOperation(identNode, TypeBounds.OBJECT) {
                        @Override
                        void loadStack() {
                            CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        }
                        
                        @Override
                        void consumeStack() {
                            CodeGenerator.this.loadSharedScopeVar(resultBounds.widest, symbol, flags);
                        }
                    }.emit();
                }
                else {
                    new LoadFastScopeVar(identNode, resultBounds, flags).emit();
                }
            }
            else {
                new LoadScopeVar(identNode, resultBounds, flags).emit();
            }
            return this.method;
        }
    }
    
    private void checkTemporalDeadZone(final IdentNode identNode) {
        if (identNode.isDead()) {
            this.method.load(identNode.getSymbol().getName()).invoke(ScriptRuntime.THROW_REFERENCE_ERROR);
        }
    }
    
    private void checkAssignTarget(final Expression expression) {
        if (expression instanceof IdentNode && ((IdentNode)expression).getSymbol().isConst()) {
            this.method.load(((IdentNode)expression).getSymbol().getName()).invoke(ScriptRuntime.THROW_CONST_TYPE_ERROR);
        }
    }
    
    private boolean isRestOf() {
        return this.continuationEntryPoints != null;
    }
    
    private boolean isCurrentContinuationEntryPoint(final int programPoint) {
        return this.isRestOf() && this.getCurrentContinuationEntryPoint() == programPoint;
    }
    
    private int[] getContinuationEntryPoints() {
        return (int[])(this.isRestOf() ? this.continuationEntryPoints : null);
    }
    
    private int getCurrentContinuationEntryPoint() {
        return this.isRestOf() ? this.continuationEntryPoints[0] : -1;
    }
    
    private boolean isContinuationEntryPoint(final int programPoint) {
        if (this.isRestOf()) {
            assert this.continuationEntryPoints != null;
            for (final int cep : this.continuationEntryPoints) {
                if (cep == programPoint) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isFastScope(final Symbol symbol) {
        if (!symbol.isScope()) {
            return false;
        }
        if (!((CodeGeneratorLexicalContext)this.lc).inDynamicScope()) {
            assert symbol.isGlobal() || ((CodeGeneratorLexicalContext)this.lc).getDefiningBlock(symbol).needsScope() : symbol.getName();
            return true;
        }
        else {
            if (symbol.isGlobal()) {
                return false;
            }
            final String name = symbol.getName();
            boolean previousWasBlock = false;
            final Iterator<LexicalContextNode> it = ((CodeGeneratorLexicalContext)this.lc).getAllNodes();
            while (it.hasNext()) {
                final LexicalContextNode node = it.next();
                if (node instanceof Block) {
                    final Block block = (Block)node;
                    if (block.getExistingSymbol(name) == symbol) {
                        assert block.needsScope();
                        return true;
                    }
                    else {
                        previousWasBlock = true;
                    }
                }
                else {
                    if ((node instanceof WithNode && previousWasBlock) || (node instanceof FunctionNode && ((FunctionNode)node).needsDynamicScope())) {
                        return false;
                    }
                    previousWasBlock = false;
                }
            }
            throw new AssertionError();
        }
    }
    
    private MethodEmitter loadSharedScopeVar(final Type valueType, final Symbol symbol, final int flags) {
        assert this.isFastScope(symbol);
        this.method.load(this.getScopeProtoDepth(((CodeGeneratorLexicalContext)this.lc).getCurrentBlock(), symbol));
        return ((CodeGeneratorLexicalContext)this.lc).getScopeGet(this.unit, symbol, valueType, flags).generateInvoke(this.method);
    }
    
    private MethodEmitter storeFastScopeVar(final Symbol symbol, final int flags) {
        this.loadFastScopeProto(symbol, true);
        this.method.dynamicSet(symbol.getName(), flags, false);
        return this.method;
    }
    
    private int getScopeProtoDepth(final Block startingBlock, final Symbol symbol) {
        final FunctionNode fn = ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction();
        final int externalDepth = this.compiler.getScriptFunctionData(fn.getId()).getExternalSymbolDepth(symbol.getName());
        final int internalDepth = FindScopeDepths.findInternalDepth(this.lc, fn, startingBlock, symbol);
        final int scopesToStart = FindScopeDepths.findScopesToStart(this.lc, fn, startingBlock);
        int depth = 0;
        if (internalDepth == -1) {
            depth = scopesToStart + externalDepth;
        }
        else {
            assert internalDepth <= scopesToStart;
            depth = internalDepth;
        }
        return depth;
    }
    
    private void loadFastScopeProto(final Symbol symbol, final boolean swap) {
        final int depth = this.getScopeProtoDepth(((CodeGeneratorLexicalContext)this.lc).getCurrentBlock(), symbol);
        assert depth != -1 : "Couldn't find scope depth for symbol " + symbol.getName() + " in " + ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction();
        if (depth > 0) {
            if (swap) {
                this.method.swap();
            }
            for (int i = 0; i < depth; ++i) {
                this.method.invoke(ScriptObject.GET_PROTO);
            }
            if (swap) {
                this.method.swap();
            }
        }
    }
    
    private MethodEmitter loadExpressionUnbounded(final Expression expr) {
        return this.loadExpression(expr, TypeBounds.UNBOUNDED);
    }
    
    private MethodEmitter loadExpressionAsObject(final Expression expr) {
        return this.loadExpression(expr, TypeBounds.OBJECT);
    }
    
    MethodEmitter loadExpressionAsBoolean(final Expression expr) {
        return this.loadExpression(expr, TypeBounds.BOOLEAN);
    }
    
    private static boolean noToPrimitiveConversion(final Type source, final Type target) {
        return source.isJSPrimitive() || !target.isJSPrimitive() || target.isBoolean();
    }
    
    MethodEmitter loadBinaryOperands(final BinaryNode binaryNode) {
        return this.loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), TypeBounds.UNBOUNDED.notWiderThan(binaryNode.getWidestOperandType()), false, false);
    }
    
    private MethodEmitter loadBinaryOperands(final Expression lhs, final Expression rhs, final TypeBounds explicitOperandBounds, final boolean baseAlreadyOnStack, final boolean forceConversionSeparation) {
        final Type lhsType = undefinedToNumber(lhs.getType());
        final Type rhsType = undefinedToNumber(rhs.getType());
        final Type narrowestOperandType = Type.narrowest(Type.widest(lhsType, rhsType), explicitOperandBounds.widest);
        final TypeBounds operandBounds = explicitOperandBounds.notNarrowerThan(narrowestOperandType);
        if (noToPrimitiveConversion(lhsType, explicitOperandBounds.widest) || rhs.isLocal()) {
            if (forceConversionSeparation) {
                final TypeBounds safeConvertBounds = TypeBounds.UNBOUNDED.notNarrowerThan(narrowestOperandType);
                this.loadExpression(lhs, safeConvertBounds, baseAlreadyOnStack);
                this.method.convert(operandBounds.within(this.method.peekType()));
                this.loadExpression(rhs, safeConvertBounds, false);
                this.method.convert(operandBounds.within(this.method.peekType()));
            }
            else {
                this.loadExpression(lhs, operandBounds, baseAlreadyOnStack);
                this.loadExpression(rhs, operandBounds, false);
            }
        }
        else {
            final TypeBounds safeConvertBounds = TypeBounds.UNBOUNDED.notNarrowerThan(narrowestOperandType);
            this.loadExpression(lhs, safeConvertBounds, baseAlreadyOnStack);
            final Type lhsLoadedType = this.method.peekType();
            this.loadExpression(rhs, safeConvertBounds, false);
            final Type convertedLhsType = operandBounds.within(this.method.peekType());
            if (convertedLhsType != lhsLoadedType) {
                this.method.swap().convert(convertedLhsType).swap();
            }
            this.method.convert(operandBounds.within(this.method.peekType()));
        }
        assert Type.generic(this.method.peekType()) == operandBounds.narrowest;
        assert Type.generic(this.method.peekType(1)) == operandBounds.narrowest;
        return this.method;
    }
    
    MethodEmitter loadComparisonOperands(final BinaryNode cmp) {
        final Expression lhs = cmp.lhs();
        final Expression rhs = cmp.rhs();
        final Type lhsType = lhs.getType();
        final Type rhsType = rhs.getType();
        assert !rhsType.isObject();
        if (lhsType.isObject() || rhsType.isObject()) {
            final boolean canReorder = lhsType.isPrimitive() || rhs.isLocal();
            final boolean canCombineLoadAndConvert = canReorder && cmp.isRelational();
            this.loadExpression(lhs, (canCombineLoadAndConvert && !lhs.isOptimistic()) ? TypeBounds.NUMBER : TypeBounds.UNBOUNDED);
            final Type lhsLoadedType = this.method.peekType();
            final TokenType tt = cmp.tokenType();
            if (canReorder) {
                emitObjectToNumberComparisonConversion(this.method, tt);
                this.loadExpression(rhs, (canCombineLoadAndConvert && !rhs.isOptimistic()) ? TypeBounds.NUMBER : TypeBounds.UNBOUNDED);
            }
            else {
                this.loadExpression(rhs, TypeBounds.UNBOUNDED);
                if (lhsLoadedType != Type.NUMBER) {
                    this.method.swap();
                    emitObjectToNumberComparisonConversion(this.method, tt);
                    this.method.swap();
                }
            }
            emitObjectToNumberComparisonConversion(this.method, tt);
            return this.method;
        }
        return this.loadBinaryOperands(cmp);
    }
    
    private static void emitObjectToNumberComparisonConversion(final MethodEmitter method, final TokenType tt) {
        switch (tt) {
            case EQ:
            case NE: {
                if (method.peekType().isObject()) {
                    CodeGenerator.TO_NUMBER_FOR_EQ.invoke(method);
                    return;
                }
                break;
            }
            case EQ_STRICT:
            case NE_STRICT: {
                if (method.peekType().isObject()) {
                    CodeGenerator.TO_NUMBER_FOR_STRICT_EQ.invoke(method);
                    return;
                }
                break;
            }
        }
        method.convert(Type.NUMBER);
    }
    
    private static final Type undefinedToNumber(final Type type) {
        return (type == Type.UNDEFINED) ? Type.NUMBER : type;
    }
    
    private static Type booleanToInt(final Type t) {
        return (t == Type.BOOLEAN) ? Type.INT : t;
    }
    
    private static Type objectToNumber(final Type t) {
        return t.isObject() ? Type.NUMBER : t;
    }
    
    MethodEmitter loadExpressionAsType(final Expression expr, final Type type) {
        if (type == Type.BOOLEAN) {
            return this.loadExpressionAsBoolean(expr);
        }
        if (type != Type.UNDEFINED) {
            return this.loadExpression(expr, TypeBounds.UNBOUNDED.notNarrowerThan(type)).convert(type);
        }
        assert expr.getType() == Type.UNDEFINED;
        return this.loadExpressionAsObject(expr);
    }
    
    private MethodEmitter loadExpression(final Expression expr, final TypeBounds resultBounds) {
        return this.loadExpression(expr, resultBounds, false);
    }
    
    private MethodEmitter loadExpression(final Expression expr, final TypeBounds resultBounds, final boolean baseAlreadyOnStack) {
        final CodeGenerator codegen = this;
        final boolean isCurrentDiscard = ((CodeGeneratorLexicalContext)codegen.lc).isCurrentDiscard(expr);
        expr.accept(new NodeOperatorVisitor<LexicalContext>(new LexicalContext()) {
            @Override
            public boolean enterIdentNode(final IdentNode identNode) {
                CodeGenerator.this.loadIdent(identNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterAccessNode(final AccessNode accessNode) {
                new OptimisticOperation(accessNode, resultBounds) {
                    @Override
                    void loadStack() {
                        if (!baseAlreadyOnStack) {
                            CodeGenerator.this.loadExpressionAsObject(accessNode.getBase());
                        }
                        assert CodeGenerator.this.method.peekType().isObject();
                    }
                    
                    @Override
                    void consumeStack() {
                        final int flags = CodeGenerator.this.getCallSiteFlags();
                        this.dynamicGet(accessNode.getProperty(), flags, accessNode.isFunction(), accessNode.isIndex());
                    }
                }.emit(baseAlreadyOnStack ? 1 : 0);
                return false;
            }
            
            @Override
            public boolean enterIndexNode(final IndexNode indexNode) {
                new OptimisticOperation(indexNode, resultBounds) {
                    @Override
                    void loadStack() {
                        if (!baseAlreadyOnStack) {
                            CodeGenerator.this.loadExpressionAsObject(indexNode.getBase());
                            CodeGenerator.this.loadExpressionUnbounded(indexNode.getIndex());
                        }
                    }
                    
                    @Override
                    void consumeStack() {
                        final int flags = CodeGenerator.this.getCallSiteFlags();
                        this.dynamicGetIndex(flags, indexNode.isFunction());
                    }
                }.emit(baseAlreadyOnStack ? 2 : 0);
                return false;
            }
            
            @Override
            public boolean enterFunctionNode(final FunctionNode functionNode) {
                this.lc.pop(functionNode);
                functionNode.accept(codegen);
                this.lc.push(functionNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_ADD(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_ADD(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_BIT_AND(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_AND(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_BIT_OR(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_OR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_BIT_XOR(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_XOR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_DIV(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_DIV(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_MOD(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_MOD(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_MUL(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_MUL(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_SAR(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SAR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_SHL(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SHL(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_SHR(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SHR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterASSIGN_SUB(final BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SUB(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterCallNode(final CallNode callNode) {
                return CodeGenerator.this.loadCallNode(callNode, resultBounds);
            }
            
            @Override
            public boolean enterLiteralNode(final LiteralNode<?> literalNode) {
                CodeGenerator.this.loadLiteral(literalNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterTernaryNode(final TernaryNode ternaryNode) {
                CodeGenerator.this.loadTernaryNode(ternaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterADD(final BinaryNode binaryNode) {
                CodeGenerator.this.loadADD(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterSUB(final UnaryNode unaryNode) {
                CodeGenerator.this.loadSUB(unaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterSUB(final BinaryNode binaryNode) {
                CodeGenerator.this.loadSUB(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterMUL(final BinaryNode binaryNode) {
                CodeGenerator.this.loadMUL(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterDIV(final BinaryNode binaryNode) {
                CodeGenerator.this.loadDIV(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterMOD(final BinaryNode binaryNode) {
                CodeGenerator.this.loadMOD(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterSAR(final BinaryNode binaryNode) {
                CodeGenerator.this.loadSAR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterSHL(final BinaryNode binaryNode) {
                CodeGenerator.this.loadSHL(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterSHR(final BinaryNode binaryNode) {
                CodeGenerator.this.loadSHR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterCOMMALEFT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCOMMALEFT(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterCOMMARIGHT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCOMMARIGHT(binaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterAND(final BinaryNode binaryNode) {
                CodeGenerator.this.loadAND_OR(binaryNode, resultBounds, true);
                return false;
            }
            
            @Override
            public boolean enterOR(final BinaryNode binaryNode) {
                CodeGenerator.this.loadAND_OR(binaryNode, resultBounds, false);
                return false;
            }
            
            @Override
            public boolean enterNOT(final UnaryNode unaryNode) {
                CodeGenerator.this.loadNOT(unaryNode);
                return false;
            }
            
            @Override
            public boolean enterADD(final UnaryNode unaryNode) {
                CodeGenerator.this.loadADD(unaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterBIT_NOT(final UnaryNode unaryNode) {
                CodeGenerator.this.loadBIT_NOT(unaryNode);
                return false;
            }
            
            @Override
            public boolean enterBIT_AND(final BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_AND(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterBIT_OR(final BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_OR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterBIT_XOR(final BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_XOR(binaryNode);
                return false;
            }
            
            @Override
            public boolean enterVOID(final UnaryNode unaryNode) {
                CodeGenerator.this.loadVOID(unaryNode, resultBounds);
                return false;
            }
            
            @Override
            public boolean enterEQ(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.EQ);
                return false;
            }
            
            @Override
            public boolean enterEQ_STRICT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.EQ);
                return false;
            }
            
            @Override
            public boolean enterGE(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.GE);
                return false;
            }
            
            @Override
            public boolean enterGT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.GT);
                return false;
            }
            
            @Override
            public boolean enterLE(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.LE);
                return false;
            }
            
            @Override
            public boolean enterLT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.LT);
                return false;
            }
            
            @Override
            public boolean enterNE(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.NE);
                return false;
            }
            
            @Override
            public boolean enterNE_STRICT(final BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.NE);
                return false;
            }
            
            @Override
            public boolean enterObjectNode(final ObjectNode objectNode) {
                CodeGenerator.this.loadObjectNode(objectNode);
                return false;
            }
            
            @Override
            public boolean enterRuntimeNode(final RuntimeNode runtimeNode) {
                CodeGenerator.this.loadRuntimeNode(runtimeNode);
                return false;
            }
            
            @Override
            public boolean enterNEW(final UnaryNode unaryNode) {
                CodeGenerator.this.loadNEW(unaryNode);
                return false;
            }
            
            @Override
            public boolean enterDECINC(final UnaryNode unaryNode) {
                CodeGenerator.this.checkAssignTarget(unaryNode.getExpression());
                CodeGenerator.this.loadDECINC(unaryNode);
                return false;
            }
            
            @Override
            public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression joinExpr) {
                CodeGenerator.this.loadMaybeDiscard(joinExpr, joinExpr.getExpression(), resultBounds);
                return false;
            }
            
            @Override
            public boolean enterGetSplitState(final GetSplitState getSplitState) {
                CodeGenerator.this.method.loadScope();
                CodeGenerator.this.method.invoke(Scope.GET_SPLIT_STATE);
                return false;
            }
            
            public boolean enterDefault(final Node otherNode) {
                throw new AssertionError((Object)otherNode.getClass().getName());
            }
        });
        if (!isCurrentDiscard) {
            this.coerceStackTop(resultBounds);
        }
        return this.method;
    }
    
    private MethodEmitter coerceStackTop(final TypeBounds typeBounds) {
        return this.method.convert(typeBounds.within(this.method.peekType()));
    }
    
    private void closeBlockVariables(final Block block) {
        for (final Symbol symbol : block.getSymbols()) {
            if (symbol.isBytecodeLocal()) {
                this.method.closeLocalVariable(symbol, block.getBreakLabel());
            }
        }
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        final Label entryLabel = block.getEntryLabel();
        if (entryLabel.isBreakTarget()) {
            assert !this.method.isReachable();
            this.method.breakLabel(entryLabel, ((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount());
        }
        else {
            this.method.label(entryLabel);
        }
        if (!this.method.isReachable()) {
            return false;
        }
        if (((CodeGeneratorLexicalContext)this.lc).isFunctionBody() && this.emittedMethods.contains(((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().getName())) {
            return false;
        }
        this.initLocals(block);
        assert ((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount() == this.method.getFirstTemp();
        return true;
    }
    
    boolean useOptimisticTypes() {
        return !((CodeGeneratorLexicalContext)this.lc).inSplitNode() && this.compiler.useOptimisticTypes();
    }
    
    @Override
    public Node leaveBlock(final Block block) {
        this.popBlockScope(block);
        this.method.beforeJoinPoint(block);
        this.closeBlockVariables(block);
        ((CodeGeneratorLexicalContext)this.lc).releaseSlots();
        assert (((CodeGeneratorLexicalContext)this.lc).isFunctionBody() ? 0 : ((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount()) == this.method.getFirstTemp() : "reachable=" + this.method.isReachable() + " isFunctionBody=" + ((CodeGeneratorLexicalContext)this.lc).isFunctionBody() + " usedSlotCount=" + ((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount() + " firstTemp=" + this.method.getFirstTemp();
        return block;
    }
    
    private void popBlockScope(final Block block) {
        final Label breakLabel = block.getBreakLabel();
        if (!block.needsScope() || ((CodeGeneratorLexicalContext)this.lc).isFunctionBody()) {
            this.emitBlockBreakLabel(breakLabel);
            return;
        }
        final Label beginTryLabel = this.scopeEntryLabels.pop();
        final Label recoveryLabel = new Label("block_popscope_catch");
        this.emitBlockBreakLabel(breakLabel);
        final boolean bodyCanThrow = breakLabel.isAfter(beginTryLabel);
        if (bodyCanThrow) {
            this.method._try(beginTryLabel, breakLabel, recoveryLabel);
        }
        Label afterCatchLabel = null;
        if (this.method.isReachable()) {
            this.popScope();
            if (bodyCanThrow) {
                afterCatchLabel = new Label("block_after_catch");
                this.method._goto(afterCatchLabel);
            }
        }
        if (bodyCanThrow) {
            assert !this.method.isReachable();
            this.method._catch(recoveryLabel);
            this.popScopeException();
            this.method.athrow();
        }
        if (afterCatchLabel != null) {
            this.method.label(afterCatchLabel);
        }
    }
    
    private void emitBlockBreakLabel(final Label breakLabel) {
        final LabelNode labelNode = ((CodeGeneratorLexicalContext)this.lc).getCurrentBlockLabelNode();
        if (labelNode != null) {
            assert !(!this.method.isReachable());
            this.method.beforeJoinPoint(labelNode);
            this.method.breakLabel(breakLabel, this.labeledBlockBreakLiveLocals.pop());
        }
        else {
            this.method.label(breakLabel);
        }
    }
    
    private void popScope() {
        this.popScopes(1);
    }
    
    private void popScopeException() {
        this.popScope();
        final ContinuationInfo ci = this.getContinuationInfo();
        if (ci != null) {
            final Label catchLabel = ci.catchLabel;
            if (catchLabel != CodeGenerator.METHOD_BOUNDARY && catchLabel == this.catchLabels.peek()) {
                ++ci.exceptionScopePops;
            }
        }
    }
    
    private void popScopesUntil(final LexicalContextNode until) {
        this.popScopes(((CodeGeneratorLexicalContext)this.lc).getScopeNestingLevelTo(until));
    }
    
    private void popScopes(final int count) {
        if (count == 0) {
            return;
        }
        assert count > 0;
        if (!this.method.hasScope()) {
            return;
        }
        this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        for (int i = 0; i < count; ++i) {
            this.method.invoke(ScriptObject.GET_PROTO);
        }
        this.method.storeCompilerConstant(CompilerConstants.SCOPE);
    }
    
    @Override
    public boolean enterBreakNode(final BreakNode breakNode) {
        return this.enterJumpStatement(breakNode);
    }
    
    @Override
    public boolean enterJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        return this.enterJumpStatement(jumpToInlinedFinally);
    }
    
    private boolean enterJumpStatement(final JumpStatement jump) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(jump);
        this.method.beforeJoinPoint(jump);
        this.popScopesUntil(jump.getPopScopeLimit(this.lc));
        final Label targetLabel = jump.getTargetLabel(this.lc);
        targetLabel.markAsBreakTarget();
        this.method._goto(targetLabel);
        return false;
    }
    
    private int loadArgs(final List<Expression> args) {
        final int argCount = args.size();
        if (argCount > 250) {
            this.loadArgsArray(args);
            return 1;
        }
        for (final Expression arg : args) {
            assert arg != null;
            this.loadExpressionUnbounded(arg);
        }
        return argCount;
    }
    
    private boolean loadCallNode(final CallNode callNode, final TypeBounds resultBounds) {
        this.lineNumber(callNode.getLineNumber());
        final List<Expression> args = callNode.getArgs();
        final Expression function = callNode.getFunction();
        final Block currentBlock = ((CodeGeneratorLexicalContext)this.lc).getCurrentBlock();
        final CodeGeneratorLexicalContext codegenLexicalContext = (CodeGeneratorLexicalContext)this.lc;
        function.accept(new SimpleNodeVisitor() {
            private MethodEmitter sharedScopeCall(final IdentNode identNode, final int flags) {
                final Symbol symbol = identNode.getSymbol();
                final boolean isFastScope = CodeGenerator.this.isFastScope(symbol);
                new OptimisticOperation(callNode, resultBounds) {
                    @Override
                    void loadStack() {
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        if (isFastScope) {
                            CodeGenerator.this.method.load(CodeGenerator.this.getScopeProtoDepth(currentBlock, symbol));
                        }
                        else {
                            CodeGenerator.this.method.load(-1);
                        }
                        CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        final Type[] paramTypes = CodeGenerator.this.method.getTypesFromStack(args.size());
                        for (int i = 0; i < paramTypes.length; ++i) {
                            paramTypes[i] = Type.generic(paramTypes[i]);
                        }
                        final SharedScopeCall scopeCall = codegenLexicalContext.getScopeCall(CodeGenerator.this.unit, symbol, identNode.getType(), resultBounds.widest, paramTypes, flags);
                        scopeCall.generateInvoke(CodeGenerator.this.method);
                    }
                }.emit();
                return CodeGenerator.this.method;
            }
            
            private void scopeCall(final IdentNode ident, final int flags) {
                new OptimisticOperation(callNode, resultBounds) {
                    int argsCount;
                    
                    @Override
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(ident);
                        CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        this.dynamicCall(2 + this.argsCount, flags, ident.getName());
                    }
                }.emit();
            }
            
            private void evalCall(final IdentNode ident, final int flags) {
                final Label invoke_direct_eval = new Label("invoke_direct_eval");
                final Label is_not_eval = new Label("is_not_eval");
                final Label eval_done = new Label("eval_done");
                new OptimisticOperation(callNode, resultBounds) {
                    int argsCount;
                    
                    @Override
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(ident.setIsNotFunction());
                        CodeGenerator.this.globalIsEval();
                        CodeGenerator.this.method.ifeq(is_not_eval);
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        final List<Expression> evalArgs = callNode.getEvalArgs().getArgs();
                        CodeGenerator.this.loadExpressionAsObject(evalArgs.get(0));
                        for (int numArgs = evalArgs.size(), i = 1; i < numArgs; ++i) {
                            CodeGenerator.this.loadAndDiscard(evalArgs.get(i));
                        }
                        CodeGenerator.this.method._goto(invoke_direct_eval);
                        CodeGenerator.this.method.label(is_not_eval);
                        CodeGenerator.this.loadExpressionAsObject(ident);
                        CodeGenerator.this.method.loadNull();
                        this.argsCount = CodeGenerator.this.loadArgs(callNode.getArgs());
                    }
                    
                    @Override
                    void consumeStack() {
                        this.dynamicCall(2 + this.argsCount, flags, "eval");
                        CodeGenerator.this.method._goto(eval_done);
                        CodeGenerator.this.method.label(invoke_direct_eval);
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.THIS);
                        CodeGenerator.this.method.load(callNode.getEvalArgs().getLocation());
                        CodeGenerator.this.method.load(((CodeGeneratorLexicalContext)CodeGenerator.this.lc).getCurrentFunction().isStrict());
                        CodeGenerator.this.globalDirectEval();
                        this.convertOptimisticReturnValue();
                        CodeGenerator.this.coerceStackTop(resultBounds);
                    }
                }.emit();
                CodeGenerator.this.method.label(eval_done);
            }
            
            @Override
            public boolean enterIdentNode(final IdentNode node) {
                final Symbol symbol = node.getSymbol();
                if (symbol.isScope()) {
                    final int flags = CodeGenerator.this.getScopeCallSiteFlags(symbol);
                    final int useCount = symbol.getUseCount();
                    if (callNode.isEval()) {
                        this.evalCall(node, flags);
                    }
                    else if (useCount <= 4 || (!CodeGenerator.this.isFastScope(symbol) && useCount <= 500) || ((CodeGeneratorLexicalContext)CodeGenerator.this.lc).inDynamicScope() || callNode.isOptimistic()) {
                        this.scopeCall(node, flags);
                    }
                    else {
                        this.sharedScopeCall(node, flags);
                    }
                    assert CodeGenerator.this.method.peekType().equals(resultBounds.within(callNode.getType())) : CodeGenerator.this.method.peekType() + " != " + resultBounds + "(" + callNode.getType() + ")";
                }
                else {
                    this.enterDefault(node);
                }
                return false;
            }
            
            @Override
            public boolean enterAccessNode(final AccessNode node) {
                final int flags = CodeGenerator.this.getCallSiteFlags() | (callNode.isApplyToCall() ? 16 : 0);
                new OptimisticOperation(callNode, resultBounds) {
                    int argCount;
                    
                    @Override
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(node.getBase());
                        CodeGenerator.this.method.dup();
                        assert !node.isOptimistic();
                        CodeGenerator.this.method.dynamicGet(node.getType(), node.getProperty(), flags, true, node.isIndex());
                        CodeGenerator.this.method.swap();
                        this.argCount = CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        this.dynamicCall(2 + this.argCount, flags, node.toString(false));
                    }
                }.emit();
                return false;
            }
            
            @Override
            public boolean enterFunctionNode(final FunctionNode origCallee) {
                new OptimisticOperation(callNode, resultBounds) {
                    FunctionNode callee;
                    int argsCount;
                    
                    @Override
                    void loadStack() {
                        this.callee = (FunctionNode)origCallee.accept(CodeGenerator.this);
                        if (this.callee.isStrict()) {
                            CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        }
                        else {
                            CodeGenerator.this.globalInstance();
                        }
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        this.dynamicCall(2 + this.argsCount, CodeGenerator.this.getCallSiteFlags(), null);
                    }
                }.emit();
                return false;
            }
            
            @Override
            public boolean enterIndexNode(final IndexNode node) {
                new OptimisticOperation(callNode, resultBounds) {
                    int argsCount;
                    
                    @Override
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(node.getBase());
                        CodeGenerator.this.method.dup();
                        final Type indexType = node.getIndex().getType();
                        if (indexType.isObject() || indexType.isBoolean()) {
                            CodeGenerator.this.loadExpressionAsObject(node.getIndex());
                        }
                        else {
                            CodeGenerator.this.loadExpressionUnbounded(node.getIndex());
                        }
                        assert !node.isOptimistic();
                        CodeGenerator.this.method.dynamicGetIndex(node.getType(), CodeGenerator.this.getCallSiteFlags(), true);
                        CodeGenerator.this.method.swap();
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        this.dynamicCall(2 + this.argsCount, CodeGenerator.this.getCallSiteFlags(), node.toString(false));
                    }
                }.emit();
                return false;
            }
            
            @Override
            protected boolean enterDefault(final Node node) {
                new OptimisticOperation(callNode, resultBounds) {
                    int argsCount;
                    
                    @Override
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(function);
                        CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }
                    
                    @Override
                    void consumeStack() {
                        final int flags = CodeGenerator.this.getCallSiteFlags() | 0x1;
                        this.dynamicCall(2 + this.argsCount, flags, node.toString(false));
                    }
                }.emit();
                return false;
            }
        });
        return false;
    }
    
    static int nonOptimisticFlags(final int flags) {
        return flags & 0x7F7;
    }
    
    @Override
    public boolean enterContinueNode(final ContinueNode continueNode) {
        return this.enterJumpStatement(continueNode);
    }
    
    @Override
    public boolean enterEmptyNode(final EmptyNode emptyNode) {
        return false;
    }
    
    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(expressionStatement);
        this.loadAndDiscard(expressionStatement.getExpression());
        assert this.method.getStackSize() == 0 : "stack not empty in " + expressionStatement;
        return false;
    }
    
    @Override
    public boolean enterBlockStatement(final BlockStatement blockStatement) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(blockStatement);
        blockStatement.getBlock().accept(this);
        return false;
    }
    
    @Override
    public boolean enterForNode(final ForNode forNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(forNode);
        if (forNode.isForIn()) {
            this.enterForIn(forNode);
        }
        else {
            final Expression init = forNode.getInit();
            if (init != null) {
                this.loadAndDiscard(init);
            }
            this.enterForOrWhile(forNode, forNode.getModify());
        }
        return false;
    }
    
    private void enterForIn(final ForNode forNode) {
        this.loadExpression(forNode.getModify(), TypeBounds.OBJECT);
        this.method.invoke(forNode.isForEach() ? ScriptRuntime.TO_VALUE_ITERATOR : ScriptRuntime.TO_PROPERTY_ITERATOR);
        final Symbol iterSymbol = forNode.getIterator();
        final int iterSlot = iterSymbol.getSlot(Type.OBJECT);
        this.method.store(iterSymbol, CodeGenerator.ITERATOR_TYPE);
        this.method.beforeJoinPoint(forNode);
        final Label continueLabel = forNode.getContinueLabel();
        final Label breakLabel = forNode.getBreakLabel();
        this.method.label(continueLabel);
        this.method.load(CodeGenerator.ITERATOR_TYPE, iterSlot);
        this.method.invoke(CompilerConstants.interfaceCallNoLookup(CodeGenerator.ITERATOR_CLASS, "hasNext", Boolean.TYPE, (Class<?>[])new Class[0]));
        final JoinPredecessorExpression test = forNode.getTest();
        final Block body = forNode.getBody();
        if (LocalVariableConversion.hasLiveConversion(test)) {
            final Label afterConversion = new Label("for_in_after_test_conv");
            this.method.ifne(afterConversion);
            this.method.beforeJoinPoint(test);
            this.method._goto(breakLabel);
            this.method.label(afterConversion);
        }
        else {
            this.method.ifeq(breakLabel);
        }
        new Store<Expression>(forNode.getInit()) {
            @Override
            protected void storeNonDiscard() {
            }
            
            @Override
            protected void evaluate() {
                new OptimisticOperation((Optimistic)forNode.getInit(), TypeBounds.UNBOUNDED) {
                    @Override
                    void loadStack() {
                        CodeGenerator.this.method.load(CodeGenerator.ITERATOR_TYPE, iterSlot);
                    }
                    
                    @Override
                    void consumeStack() {
                        CodeGenerator.this.method.invoke(CompilerConstants.interfaceCallNoLookup(CodeGenerator.ITERATOR_CLASS, "next", Object.class, (Class<?>[])new Class[0]));
                        this.convertOptimisticReturnValue();
                    }
                }.emit();
            }
        }.store();
        body.accept(this);
        if (this.method.isReachable()) {
            this.method._goto(continueLabel);
        }
        this.method.label(breakLabel);
    }
    
    private void initLocals(final Block block) {
        ((CodeGeneratorLexicalContext)this.lc).onEnterBlock(block);
        final boolean isFunctionBody = ((CodeGeneratorLexicalContext)this.lc).isFunctionBody();
        final FunctionNode function = ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction();
        if (isFunctionBody) {
            this.initializeMethodParameters(function);
            if (!function.isVarArg()) {
                this.expandParameterSlots(function);
            }
            if (this.method.hasScope()) {
                if (function.needsParentScope()) {
                    this.method.loadCompilerConstant(CompilerConstants.CALLEE);
                    this.method.invoke(ScriptFunction.GET_SCOPE);
                }
                else {
                    assert function.hasScopeBlock();
                    this.method.loadNull();
                }
                this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            }
            if (function.needsArguments()) {
                this.initArguments(function);
            }
        }
        if (block.needsScope()) {
            final boolean varsInScope = function.allVarsInScope();
            final boolean hasArguments = function.needsArguments();
            final List<MapTuple<Symbol>> tuples = new ArrayList<MapTuple<Symbol>>();
            final Iterator<IdentNode> paramIter = function.getParameters().iterator();
            for (final Symbol symbol : block.getSymbols()) {
                if (!symbol.isInternal()) {
                    if (symbol.isThis()) {
                        continue;
                    }
                    if (symbol.isVar()) {
                        assert !(!symbol.isScope());
                        if (varsInScope || symbol.isScope()) {
                            assert symbol.isScope() : "scope for " + symbol + " should have been set in Lower already " + function.getName();
                            assert !symbol.hasSlot() : "slot for " + symbol + " should have been removed in Lower already" + function.getName();
                            tuples.add(new MapTuple<Symbol>(symbol.getName(), symbol, null));
                        }
                        else {
                            if (!CodeGenerator.$assertionsDisabled && !symbol.hasSlot() && symbol.slotCount() != 0) {
                                throw new AssertionError((Object)(symbol + " should have a slot only, no scope"));
                            }
                            continue;
                        }
                    }
                    else {
                        if (!symbol.isParam() || (!varsInScope && !hasArguments && !symbol.isScope())) {
                            continue;
                        }
                        assert symbol.isScope() : "scope for " + symbol + " should have been set in AssignSymbols already " + function.getName() + " varsInScope=" + varsInScope + " hasArguments=" + hasArguments + " symbol.isScope()=" + symbol.isScope();
                        assert !symbol.hasSlot() : "slot for " + symbol + " should have been removed in Lower already " + function.getName();
                        Symbol paramSymbol;
                        Type paramType;
                        if (hasArguments) {
                            assert !symbol.hasSlot() : "slot for " + symbol + " should have been removed in Lower already ";
                            paramSymbol = null;
                            paramType = null;
                        }
                        else {
                            paramSymbol = symbol;
                            IdentNode nextParam;
                            do {
                                nextParam = paramIter.next();
                            } while (!nextParam.getName().equals(symbol.getName()));
                            paramType = nextParam.getType();
                        }
                        tuples.add(new MapTuple<Symbol>(symbol.getName(), symbol, paramType, paramSymbol) {
                            @Override
                            public Class<?> getValueType() {
                                if (!CodeGenerator.this.useDualFields() || this.value == null || paramType == null || paramType.isBoolean()) {
                                    return Object.class;
                                }
                                return paramType.getTypeClass();
                            }
                        });
                    }
                }
            }
            new FieldObjectCreator<Symbol>(this, tuples, true, hasArguments) {
                @Override
                protected void loadValue(final Symbol value, final Type type) {
                    CodeGenerator.this.method.load(value, type);
                }
            }.makeObject(this.method);
            if (isFunctionBody && function.isProgram()) {
                this.method.invoke(ScriptRuntime.MERGE_SCOPE);
            }
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            if (!isFunctionBody) {
                final Label scopeEntryLabel = new Label("scope_entry");
                this.scopeEntryLabels.push(scopeEntryLabel);
                this.method.label(scopeEntryLabel);
            }
        }
        else if (isFunctionBody && function.isVarArg()) {
            int nextParam2 = 0;
            for (final IdentNode param : function.getParameters()) {
                param.getSymbol().setFieldIndex(nextParam2++);
            }
        }
        this.printSymbols(block, function, (isFunctionBody ? "Function " : "Block in ") + ((function.getIdent() == null) ? "<anonymous>" : function.getIdent().getName()));
    }
    
    private void initializeMethodParameters(final FunctionNode function) {
        final Label functionStart = new Label("fn_start");
        this.method.label(functionStart);
        int nextSlot = 0;
        if (function.needsCallee()) {
            this.initializeInternalFunctionParameter(CompilerConstants.CALLEE, function, functionStart, nextSlot++);
        }
        this.initializeInternalFunctionParameter(CompilerConstants.THIS, function, functionStart, nextSlot++);
        if (function.isVarArg()) {
            this.initializeInternalFunctionParameter(CompilerConstants.VARARGS, function, functionStart, nextSlot++);
        }
        else {
            for (final IdentNode param : function.getParameters()) {
                final Symbol symbol = param.getSymbol();
                if (symbol.isBytecodeLocal()) {
                    this.method.initializeMethodParameter(symbol, param.getType(), functionStart);
                }
            }
        }
    }
    
    private void initializeInternalFunctionParameter(final CompilerConstants cc, final FunctionNode fn, final Label functionStart, final int slot) {
        final Symbol symbol = this.initializeInternalFunctionOrSplitParameter(cc, fn, functionStart, slot);
        assert symbol.getFirstSlot() == slot;
    }
    
    private Symbol initializeInternalFunctionOrSplitParameter(final CompilerConstants cc, final FunctionNode fn, final Label functionStart, final int slot) {
        final Symbol symbol = fn.getBody().getExistingSymbol(cc.symbolName());
        final Type type = Type.typeFor(cc.type());
        this.method.initializeMethodParameter(symbol, type, functionStart);
        this.method.onLocalStore(type, slot);
        return symbol;
    }
    
    private void expandParameterSlots(final FunctionNode function) {
        final List<IdentNode> parameters = function.getParameters();
        int currentIncomingSlot = function.needsCallee() ? 2 : 1;
        for (final IdentNode parameter : parameters) {
            currentIncomingSlot += parameter.getType().getSlots();
        }
        int i = parameters.size();
        while (i-- > 0) {
            final IdentNode parameter = parameters.get(i);
            final Type parameterType = parameter.getType();
            final int typeWidth = parameterType.getSlots();
            currentIncomingSlot -= typeWidth;
            final Symbol symbol = parameter.getSymbol();
            final int slotCount = symbol.slotCount();
            assert slotCount > 0;
            if (!CodeGenerator.$assertionsDisabled && !symbol.isBytecodeLocal() && slotCount != typeWidth) {
                throw new AssertionError();
            }
            this.method.onLocalStore(parameterType, currentIncomingSlot);
            if (currentIncomingSlot == symbol.getSlot(parameterType)) {
                continue;
            }
            this.method.load(parameterType, currentIncomingSlot);
            this.method.store(symbol, parameterType);
        }
    }
    
    private void initArguments(final FunctionNode function) {
        this.method.loadCompilerConstant(CompilerConstants.VARARGS);
        if (function.needsCallee()) {
            this.method.loadCompilerConstant(CompilerConstants.CALLEE);
        }
        else {
            assert function.isStrict();
            this.method.loadNull();
        }
        this.method.load(function.getParameters().size());
        this.globalAllocateArguments();
        this.method.storeCompilerConstant(CompilerConstants.ARGUMENTS);
    }
    
    private boolean skipFunction(final FunctionNode functionNode) {
        final ScriptEnvironment env = this.compiler.getScriptEnvironment();
        final boolean lazy = env._lazy_compilation;
        final boolean onDemand = this.compiler.isOnDemandCompilation();
        return ((onDemand || lazy) && ((CodeGeneratorLexicalContext)this.lc).getOutermostFunction() != functionNode) || (!onDemand && lazy && env._optimistic_types && functionNode.isProgram());
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (this.skipFunction(functionNode)) {
            this.newFunctionObject(functionNode, false);
            return false;
        }
        final String fnName = functionNode.getName();
        if (!this.emittedMethods.contains(fnName)) {
            this.log.info("=== BEGIN ", fnName);
            assert functionNode.getCompileUnit() != null : "no compile unit for " + fnName + " " + Debug.id(functionNode);
            this.unit = ((CodeGeneratorLexicalContext)this.lc).pushCompileUnit(functionNode.getCompileUnit());
            assert ((CodeGeneratorLexicalContext)this.lc).hasCompileUnits();
            final ClassEmitter classEmitter = this.unit.getClassEmitter();
            this.pushMethodEmitter(this.isRestOf() ? classEmitter.restOfMethod(functionNode) : classEmitter.method(functionNode));
            this.method.setPreventUndefinedLoad();
            if (this.useOptimisticTypes()) {
                ((CodeGeneratorLexicalContext)this.lc).pushUnwarrantedOptimismHandlers();
            }
            this.lastLineNumber = -1;
            this.method.begin();
            if (this.isRestOf()) {
                assert this.continuationInfo == null;
                this.continuationInfo = new ContinuationInfo();
                this.method.gotoLoopStart(this.continuationInfo.getHandlerLabel());
            }
        }
        return true;
    }
    
    private void pushMethodEmitter(final MethodEmitter newMethod) {
        this.method = ((CodeGeneratorLexicalContext)this.lc).pushMethodEmitter(newMethod);
        this.catchLabels.push(CodeGenerator.METHOD_BOUNDARY);
    }
    
    private void popMethodEmitter() {
        this.method = ((CodeGeneratorLexicalContext)this.lc).popMethodEmitter(this.method);
        assert this.catchLabels.peek() == CodeGenerator.METHOD_BOUNDARY;
        this.catchLabels.pop();
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        try {
            boolean markOptimistic;
            if (this.emittedMethods.add(functionNode.getName())) {
                markOptimistic = this.generateUnwarrantedOptimismExceptionHandlers(functionNode);
                this.generateContinuationHandler();
                this.method.end();
                this.unit = ((CodeGeneratorLexicalContext)this.lc).popCompileUnit(functionNode.getCompileUnit());
                this.popMethodEmitter();
                this.log.info("=== END ", functionNode.getName());
            }
            else {
                markOptimistic = false;
            }
            FunctionNode newFunctionNode = functionNode;
            if (markOptimistic) {
                newFunctionNode = newFunctionNode.setFlag(this.lc, 2048);
            }
            this.newFunctionObject(newFunctionNode, true);
            return newFunctionNode;
        }
        catch (Throwable t) {
            Context.printStackTrace(t);
            final VerifyError e = new VerifyError("Code generation bug in \"" + functionNode.getName() + "\": likely stack misaligned: " + t + " " + functionNode.getSource().getName());
            e.initCause(t);
            throw e;
        }
    }
    
    @Override
    public boolean enterIfNode(final IfNode ifNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(ifNode);
        final Expression test = ifNode.getTest();
        final Block pass = ifNode.getPass();
        final Block fail = ifNode.getFail();
        if (Expression.isAlwaysTrue(test)) {
            this.loadAndDiscard(test);
            pass.accept(this);
            return false;
        }
        if (Expression.isAlwaysFalse(test)) {
            this.loadAndDiscard(test);
            if (fail != null) {
                fail.accept(this);
            }
            return false;
        }
        final boolean hasFailConversion = LocalVariableConversion.hasLiveConversion(ifNode);
        final Label failLabel = new Label("if_fail");
        final Label afterLabel = (fail == null && !hasFailConversion) ? null : new Label("if_done");
        this.emitBranch(test, failLabel, false);
        pass.accept(this);
        if (this.method.isReachable() && afterLabel != null) {
            this.method._goto(afterLabel);
        }
        this.method.label(failLabel);
        if (fail != null) {
            fail.accept(this);
        }
        else if (hasFailConversion) {
            this.method.beforeJoinPoint(ifNode);
        }
        if (afterLabel != null && afterLabel.isReachable()) {
            this.method.label(afterLabel);
        }
        return false;
    }
    
    private void emitBranch(final Expression test, final Label label, final boolean jumpWhenTrue) {
        new BranchOptimizer(this, this.method).execute(test, label, jumpWhenTrue);
    }
    
    private void enterStatement(final Statement statement) {
        this.lineNumber(statement);
    }
    
    private void lineNumber(final Statement statement) {
        this.lineNumber(statement.getLineNumber());
    }
    
    private void lineNumber(final int lineNumber) {
        if (lineNumber != this.lastLineNumber && lineNumber != -1) {
            this.method.lineNumber(lineNumber);
            this.lastLineNumber = lineNumber;
        }
    }
    
    int getLastLineNumber() {
        return this.lastLineNumber;
    }
    
    private void loadArray(final LiteralNode.ArrayLiteralNode arrayLiteralNode, final ArrayType arrayType) {
        assert arrayType == Type.OBJECT_ARRAY;
        final Expression[] nodes = arrayLiteralNode.getValue();
        final Object presets = arrayLiteralNode.getPresets();
        final int[] postsets = arrayLiteralNode.getPostsets();
        final List<Splittable.SplitRange> ranges = arrayLiteralNode.getSplitRanges();
        this.loadConstant(presets);
        final Type elementType = arrayType.getElementType();
        if (ranges != null) {
            this.loadSplitLiteral(new SplitLiteralCreator() {
                @Override
                public void populateRange(final MethodEmitter method, final Type type, final int slot, final int start, final int end) {
                    for (int i = start; i < end; ++i) {
                        method.load(type, slot);
                        CodeGenerator.this.storeElement(nodes, elementType, postsets[i]);
                    }
                    method.load(type, slot);
                }
            }, ranges, arrayType);
            return;
        }
        if (postsets.length > 0) {
            final int arraySlot = this.method.getUsedSlotsWithLiveTemporaries();
            this.method.storeTemp(arrayType, arraySlot);
            for (final int postset : postsets) {
                this.method.load(arrayType, arraySlot);
                this.storeElement(nodes, elementType, postset);
            }
            this.method.load(arrayType, arraySlot);
        }
    }
    
    private void storeElement(final Expression[] nodes, final Type elementType, final int index) {
        this.method.load(index);
        final Expression element = nodes[index];
        if (element == null) {
            this.method.loadEmpty(elementType);
        }
        else {
            this.loadExpressionAsType(element, elementType);
        }
        this.method.arraystore();
    }
    
    private MethodEmitter loadArgsArray(final List<Expression> args) {
        final Object[] array = new Object[args.size()];
        this.loadConstant(array);
        for (int i = 0; i < args.size(); ++i) {
            this.method.dup();
            this.method.load(i);
            this.loadExpression(args.get(i), TypeBounds.OBJECT);
            this.method.arraystore();
        }
        return this.method;
    }
    
    void loadConstant(final String string) {
        final String unitClassName = this.unit.getUnitClassName();
        final ClassEmitter classEmitter = this.unit.getClassEmitter();
        final int index = this.compiler.getConstantData().add(string);
        this.method.load(index);
        this.method.invokestatic(unitClassName, CompilerConstants.GET_STRING.symbolName(), CompilerConstants.methodDescriptor(String.class, Integer.TYPE));
        classEmitter.needGetConstantMethod(String.class);
    }
    
    void loadConstant(final Object object) {
        this.loadConstant(object, this.unit, this.method);
    }
    
    private void loadConstant(final Object object, final CompileUnit compileUnit, final MethodEmitter methodEmitter) {
        final String unitClassName = compileUnit.getUnitClassName();
        final ClassEmitter classEmitter = compileUnit.getClassEmitter();
        final int index = this.compiler.getConstantData().add(object);
        final Class<?> cls = object.getClass();
        if (cls == PropertyMap.class) {
            methodEmitter.load(index);
            methodEmitter.invokestatic(unitClassName, CompilerConstants.GET_MAP.symbolName(), CompilerConstants.methodDescriptor(PropertyMap.class, Integer.TYPE));
            classEmitter.needGetConstantMethod(PropertyMap.class);
        }
        else if (cls.isArray()) {
            methodEmitter.load(index);
            final String methodName = ClassEmitter.getArrayMethodName(cls);
            methodEmitter.invokestatic(unitClassName, methodName, CompilerConstants.methodDescriptor(cls, Integer.TYPE));
            classEmitter.needGetConstantMethod(cls);
        }
        else {
            methodEmitter.loadConstants().load(index).arrayload();
            if (object instanceof ArrayData) {
                methodEmitter.checkcast(ArrayData.class);
                methodEmitter.invoke(CompilerConstants.virtualCallNoLookup(ArrayData.class, "copy", ArrayData.class, (Class<?>[])new Class[0]));
            }
            else if (cls != Object.class) {
                methodEmitter.checkcast(cls);
            }
        }
    }
    
    private void loadConstantsAndIndex(final Object object, final MethodEmitter methodEmitter) {
        methodEmitter.loadConstants().load(this.compiler.getConstantData().add(object));
    }
    
    private void loadLiteral(final LiteralNode<?> node, final TypeBounds resultBounds) {
        final Object value = node.getValue();
        if (value == null) {
            this.method.loadNull();
        }
        else if (value instanceof Undefined) {
            this.method.loadUndefined(resultBounds.within(Type.OBJECT));
        }
        else if (value instanceof String) {
            final String string = (String)value;
            if (string.length() > 10922) {
                this.loadConstant(string);
            }
            else {
                this.method.load(string);
            }
        }
        else if (value instanceof Lexer.RegexToken) {
            this.loadRegex((Lexer.RegexToken)value);
        }
        else if (value instanceof Boolean) {
            this.method.load((boolean)value);
        }
        else if (value instanceof Integer) {
            if (!resultBounds.canBeNarrowerThan(Type.OBJECT)) {
                this.method.load((int)value);
                this.method.convert(Type.OBJECT);
            }
            else if (!resultBounds.canBeNarrowerThan(Type.NUMBER)) {
                this.method.load((double)value);
            }
            else {
                this.method.load((int)value);
            }
        }
        else if (value instanceof Double) {
            if (!resultBounds.canBeNarrowerThan(Type.OBJECT)) {
                this.method.load((double)value);
                this.method.convert(Type.OBJECT);
            }
            else {
                this.method.load((double)value);
            }
        }
        else {
            if (!(node instanceof LiteralNode.ArrayLiteralNode)) {
                throw new UnsupportedOperationException("Unknown literal for " + node.getClass() + " " + value.getClass() + " " + value);
            }
            final LiteralNode.ArrayLiteralNode arrayLiteral = (LiteralNode.ArrayLiteralNode)node;
            final ArrayType atype = arrayLiteral.getArrayType();
            this.loadArray(arrayLiteral, atype);
            this.globalAllocateArray(atype);
        }
    }
    
    private MethodEmitter loadRegexToken(final Lexer.RegexToken value) {
        this.method.load(value.getExpression());
        this.method.load(value.getOptions());
        return this.globalNewRegExp();
    }
    
    private MethodEmitter loadRegex(final Lexer.RegexToken regexToken) {
        if (this.regexFieldCount > 2048) {
            return this.loadRegexToken(regexToken);
        }
        final String regexName = ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().uniqueName(CompilerConstants.REGEX_PREFIX.symbolName());
        final ClassEmitter classEmitter = this.unit.getClassEmitter();
        classEmitter.field(EnumSet.of(ClassEmitter.Flag.PRIVATE, ClassEmitter.Flag.STATIC), regexName, Object.class);
        ++this.regexFieldCount;
        this.method.getStatic(this.unit.getUnitClassName(), regexName, CompilerConstants.typeDescriptor(Object.class));
        this.method.dup();
        final Label cachedLabel = new Label("cached");
        this.method.ifnonnull(cachedLabel);
        this.method.pop();
        this.loadRegexToken(regexToken);
        this.method.dup();
        this.method.putStatic(this.unit.getUnitClassName(), regexName, CompilerConstants.typeDescriptor(Object.class));
        this.method.label(cachedLabel);
        this.globalRegExpCopy();
        return this.method;
    }
    
    private static boolean propertyValueContains(final Expression value, final int pp) {
        return new Supplier<Boolean>() {
            boolean contains;
            
            @Override
            public Boolean get() {
                value.accept(new SimpleNodeVisitor() {
                    @Override
                    public boolean enterFunctionNode(final FunctionNode functionNode) {
                        return false;
                    }
                    
                    public boolean enterDefault(final Node node) {
                        if (Supplier.this.contains) {
                            return false;
                        }
                        if (node instanceof Optimistic && ((Optimistic)node).getProgramPoint() == pp) {
                            Supplier.this.contains = true;
                            return false;
                        }
                        return true;
                    }
                });
                return this.contains;
            }
        }.get();
    }
    
    private void loadObjectNode(final ObjectNode objectNode) {
        final List<PropertyNode> elements = objectNode.getElements();
        final List<MapTuple<Expression>> tuples = new ArrayList<MapTuple<Expression>>();
        final List<PropertyNode> gettersSetters = new ArrayList<PropertyNode>();
        final int ccp = this.getCurrentContinuationEntryPoint();
        final List<Splittable.SplitRange> ranges = objectNode.getSplitRanges();
        Expression protoNode = null;
        boolean restOfProperty = false;
        for (final PropertyNode propertyNode : elements) {
            final Expression value = propertyNode.getValue();
            final String key = propertyNode.getKeyName();
            final Symbol symbol = (value == null) ? null : new Symbol(key, 0);
            if (value == null) {
                gettersSetters.add(propertyNode);
            }
            else if (propertyNode.getKey() instanceof IdentNode && key.equals("__proto__")) {
                protoNode = value;
                continue;
            }
            restOfProperty |= (value != null && UnwarrantedOptimismException.isValid(ccp) && propertyValueContains(value, ccp));
            final Class<?> valueType = (!this.useDualFields() || value == null || value.getType().isBoolean()) ? Object.class : value.getType().getTypeClass();
            tuples.add(new MapTuple<Expression>(key, symbol, Type.typeFor(valueType), value) {
                @Override
                public Class<?> getValueType() {
                    return this.type.getTypeClass();
                }
            });
        }
        ObjectCreator<?> oc;
        if (elements.size() > CodeGenerator.OBJECT_SPILL_THRESHOLD) {
            oc = new SpillObjectCreator(this, tuples);
        }
        else {
            oc = new FieldObjectCreator<Expression>(this, tuples) {
                @Override
                protected void loadValue(final Expression node, final Type type) {
                    CodeGenerator.this.loadExpressionAsType(node, Type.generic(type));
                }
            };
        }
        if (ranges != null) {
            oc.createObject(this.method);
            this.loadSplitLiteral(oc, ranges, Type.typeFor(oc.getAllocatorClass()));
        }
        else {
            oc.makeObject(this.method);
        }
        if (restOfProperty) {
            final ContinuationInfo ci = this.getContinuationInfo();
            ci.setObjectLiteralMap(this.method.getStackSize(), oc.getMap());
        }
        this.method.dup();
        if (protoNode != null) {
            this.loadExpressionAsObject(protoNode);
            this.method.convert(Type.OBJECT);
            this.method.invoke(ScriptObject.SET_PROTO_FROM_LITERAL);
        }
        else {
            this.method.invoke(ScriptObject.SET_GLOBAL_OBJECT_PROTO);
        }
        for (final PropertyNode propertyNode2 : gettersSetters) {
            final FunctionNode getter = propertyNode2.getGetter();
            final FunctionNode setter = propertyNode2.getSetter();
            assert setter != null;
            this.method.dup().loadKey(propertyNode2.getKey());
            if (getter == null) {
                this.method.loadNull();
            }
            else {
                getter.accept(this);
            }
            if (setter == null) {
                this.method.loadNull();
            }
            else {
                setter.accept(this);
            }
            this.method.invoke(ScriptObject.SET_USER_ACCESSORS);
        }
    }
    
    @Override
    public boolean enterReturnNode(final ReturnNode returnNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(returnNode);
        final Type returnType = ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().getReturnType();
        final Expression expression = returnNode.getExpression();
        if (expression != null) {
            this.loadExpressionUnbounded(expression);
        }
        else {
            this.method.loadUndefined(returnType);
        }
        this.method._return(returnType);
        return false;
    }
    
    private boolean undefinedCheck(final RuntimeNode runtimeNode, final List<Expression> args) {
        final RuntimeNode.Request request = runtimeNode.getRequest();
        if (!RuntimeNode.Request.isUndefinedCheck(request)) {
            return false;
        }
        final Expression lhs = args.get(0);
        final Expression rhs = args.get(1);
        final Symbol lhsSymbol = (lhs instanceof IdentNode) ? ((IdentNode)lhs).getSymbol() : null;
        final Symbol rhsSymbol = (rhs instanceof IdentNode) ? ((IdentNode)rhs).getSymbol() : null;
        assert rhsSymbol != null;
        Symbol undefinedSymbol;
        if (isUndefinedSymbol(lhsSymbol)) {
            undefinedSymbol = lhsSymbol;
        }
        else {
            assert isUndefinedSymbol(rhsSymbol);
            undefinedSymbol = rhsSymbol;
        }
        assert undefinedSymbol != null;
        if (!undefinedSymbol.isScope()) {
            return false;
        }
        if (lhsSymbol == undefinedSymbol && lhs.getType().isPrimitive()) {
            return false;
        }
        if (this.isDeoptimizedExpression(lhs)) {
            return false;
        }
        if (!this.compiler.isGlobalSymbol(((CodeGeneratorLexicalContext)this.lc).getCurrentFunction(), "undefined")) {
            return false;
        }
        final boolean isUndefinedCheck = request == RuntimeNode.Request.IS_UNDEFINED;
        final Expression expr = (undefinedSymbol == lhsSymbol) ? rhs : lhs;
        if (expr.getType().isPrimitive()) {
            this.loadAndDiscard(expr);
            this.method.load(!isUndefinedCheck);
        }
        else {
            final Label checkTrue = new Label("ud_check_true");
            final Label end = new Label("end");
            this.loadExpressionAsObject(expr);
            this.method.loadUndefined(Type.OBJECT);
            this.method.if_acmpeq(checkTrue);
            this.method.load(!isUndefinedCheck);
            this.method._goto(end);
            this.method.label(checkTrue);
            this.method.load(isUndefinedCheck);
            this.method.label(end);
        }
        return true;
    }
    
    private static boolean isUndefinedSymbol(final Symbol symbol) {
        return symbol != null && "undefined".equals(symbol.getName());
    }
    
    private static boolean isNullLiteral(final Node node) {
        return node instanceof LiteralNode && ((LiteralNode)node).isNull();
    }
    
    private boolean nullCheck(final RuntimeNode runtimeNode, final List<Expression> args) {
        final RuntimeNode.Request request = runtimeNode.getRequest();
        if (!RuntimeNode.Request.isEQ(request) && !RuntimeNode.Request.isNE(request)) {
            return false;
        }
        assert args.size() == 2 : "EQ or NE or TYPEOF need two args";
        Expression lhs = args.get(0);
        Expression rhs = args.get(1);
        if (isNullLiteral(lhs)) {
            final Expression tmp = lhs;
            lhs = rhs;
            rhs = tmp;
        }
        if (!isNullLiteral(rhs)) {
            return false;
        }
        if (!lhs.getType().isObject()) {
            return false;
        }
        if (this.isDeoptimizedExpression(lhs)) {
            return false;
        }
        final Label trueLabel = new Label("trueLabel");
        final Label falseLabel = new Label("falseLabel");
        final Label endLabel = new Label("end");
        this.loadExpressionUnbounded(lhs);
        Label popLabel;
        if (!RuntimeNode.Request.isStrict(request)) {
            this.method.dup();
            popLabel = new Label("pop");
        }
        else {
            popLabel = null;
        }
        if (RuntimeNode.Request.isEQ(request)) {
            this.method.ifnull(RuntimeNode.Request.isStrict(request) ? trueLabel : popLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.loadUndefined(Type.OBJECT);
                this.method.if_acmpeq(trueLabel);
            }
            this.method.label(falseLabel);
            this.method.load(false);
            this.method._goto(endLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.label(popLabel);
                this.method.pop();
            }
            this.method.label(trueLabel);
            this.method.load(true);
            this.method.label(endLabel);
        }
        else if (RuntimeNode.Request.isNE(request)) {
            this.method.ifnull(RuntimeNode.Request.isStrict(request) ? falseLabel : popLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.loadUndefined(Type.OBJECT);
                this.method.if_acmpeq(falseLabel);
            }
            this.method.label(trueLabel);
            this.method.load(true);
            this.method._goto(endLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.label(popLabel);
                this.method.pop();
            }
            this.method.label(falseLabel);
            this.method.load(false);
            this.method.label(endLabel);
        }
        assert runtimeNode.getType().isBoolean();
        this.method.convert(runtimeNode.getType());
        return true;
    }
    
    private boolean isDeoptimizedExpression(final Expression rootExpr) {
        return this.isRestOf() && new Supplier<Boolean>() {
            boolean contains;
            
            @Override
            public Boolean get() {
                rootExpr.accept(new SimpleNodeVisitor() {
                    @Override
                    public boolean enterFunctionNode(final FunctionNode functionNode) {
                        return false;
                    }
                    
                    public boolean enterDefault(final Node node) {
                        if (!Supplier.this.contains && node instanceof Optimistic) {
                            final int pp = ((Optimistic)node).getProgramPoint();
                            Supplier.this.contains = (UnwarrantedOptimismException.isValid(pp) && CodeGenerator.this.isContinuationEntryPoint(pp));
                        }
                        return !Supplier.this.contains;
                    }
                });
                return this.contains;
            }
        }.get();
    }
    
    private void loadRuntimeNode(final RuntimeNode runtimeNode) {
        final List<Expression> args = new ArrayList<Expression>(runtimeNode.getArgs());
        if (this.nullCheck(runtimeNode, args)) {
            return;
        }
        if (this.undefinedCheck(runtimeNode, args)) {
            return;
        }
        final RuntimeNode.Request request = runtimeNode.getRequest();
        RuntimeNode newRuntimeNode;
        if (RuntimeNode.Request.isUndefinedCheck(request)) {
            newRuntimeNode = runtimeNode.setRequest((request == RuntimeNode.Request.IS_UNDEFINED) ? RuntimeNode.Request.EQ_STRICT : RuntimeNode.Request.NE_STRICT);
        }
        else {
            newRuntimeNode = runtimeNode;
        }
        for (final Expression arg : args) {
            this.loadExpression(arg, TypeBounds.OBJECT);
        }
        this.method.invokestatic(CompilerConstants.className(ScriptRuntime.class), newRuntimeNode.getRequest().toString(), new FunctionSignature(false, false, newRuntimeNode.getType(), args.size()).toString());
        this.method.convert(newRuntimeNode.getType());
    }
    
    private void defineCommonSplitMethodParameters() {
        this.defineSplitMethodParameter(0, CompilerConstants.CALLEE);
        this.defineSplitMethodParameter(1, CompilerConstants.THIS);
        this.defineSplitMethodParameter(2, CompilerConstants.SCOPE);
    }
    
    private void defineSplitMethodParameter(final int slot, final CompilerConstants cc) {
        this.defineSplitMethodParameter(slot, Type.typeFor(cc.type()));
    }
    
    private void defineSplitMethodParameter(final int slot, final Type type) {
        this.method.defineBlockLocalVariable(slot, slot + type.getSlots());
        this.method.onLocalStore(type, slot);
    }
    
    private void loadSplitLiteral(final SplitLiteralCreator creator, final List<Splittable.SplitRange> ranges, final Type literalType) {
        assert ranges != null;
        final MethodEmitter savedMethod = this.method;
        final FunctionNode currentFunction = ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction();
        for (final Splittable.SplitRange splitRange : ranges) {
            this.unit = ((CodeGeneratorLexicalContext)this.lc).pushCompileUnit(splitRange.getCompileUnit());
            assert this.unit != null;
            final String className = this.unit.getUnitClassName();
            final String name = currentFunction.uniqueName(CompilerConstants.SPLIT_PREFIX.symbolName());
            final Class<?> clazz = literalType.getTypeClass();
            final String signature = CompilerConstants.methodDescriptor(clazz, ScriptFunction.class, Object.class, ScriptObject.class, clazz);
            this.pushMethodEmitter(this.unit.getClassEmitter().method(EnumSet.of(ClassEmitter.Flag.PUBLIC, ClassEmitter.Flag.STATIC), name, signature));
            this.method.setFunctionNode(currentFunction);
            this.method.begin();
            this.defineCommonSplitMethodParameters();
            this.defineSplitMethodParameter(CompilerConstants.SPLIT_ARRAY_ARG.slot(), literalType);
            final int literalSlot = this.fixScopeSlot(currentFunction, 3);
            ((CodeGeneratorLexicalContext)this.lc).enterSplitNode();
            creator.populateRange(this.method, literalType, literalSlot, splitRange.getLow(), splitRange.getHigh());
            this.method._return();
            ((CodeGeneratorLexicalContext)this.lc).exitSplitNode();
            this.method.end();
            ((CodeGeneratorLexicalContext)this.lc).releaseSlots();
            this.popMethodEmitter();
            assert this.method == savedMethod;
            this.method.loadCompilerConstant(CompilerConstants.CALLEE).swap();
            this.method.loadCompilerConstant(CompilerConstants.THIS).swap();
            this.method.loadCompilerConstant(CompilerConstants.SCOPE).swap();
            this.method.invokestatic(className, name, signature);
            this.unit = ((CodeGeneratorLexicalContext)this.lc).popCompileUnit(this.unit);
        }
    }
    
    private int fixScopeSlot(final FunctionNode functionNode, final int extraSlot) {
        final int actualScopeSlot = functionNode.compilerConstant(CompilerConstants.SCOPE).getSlot(CodeGenerator.SCOPE_TYPE);
        final int defaultScopeSlot = CompilerConstants.SCOPE.slot();
        int newExtraSlot = extraSlot;
        if (actualScopeSlot != defaultScopeSlot) {
            if (actualScopeSlot == extraSlot) {
                newExtraSlot = extraSlot + 1;
                this.method.defineBlockLocalVariable(newExtraSlot, newExtraSlot + 1);
                this.method.load(Type.OBJECT, extraSlot);
                this.method.storeHidden(Type.OBJECT, newExtraSlot);
            }
            else {
                this.method.defineBlockLocalVariable(actualScopeSlot, actualScopeSlot + 1);
            }
            this.method.load(CodeGenerator.SCOPE_TYPE, defaultScopeSlot);
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
        }
        return newExtraSlot;
    }
    
    @Override
    public boolean enterSplitReturn(final SplitReturn splitReturn) {
        if (this.method.isReachable()) {
            this.method.loadUndefined(((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().getReturnType())._return();
        }
        return false;
    }
    
    @Override
    public boolean enterSetSplitState(final SetSplitState setSplitState) {
        if (this.method.isReachable()) {
            this.method.setSplitState(setSplitState.getState());
        }
        return false;
    }
    
    @Override
    public boolean enterSwitchNode(final SwitchNode switchNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(switchNode);
        final Expression expression = switchNode.getExpression();
        final List<CaseNode> cases = switchNode.getCases();
        if (cases.isEmpty()) {
            this.loadAndDiscard(expression);
            return false;
        }
        final CaseNode defaultCase = switchNode.getDefaultCase();
        final Label breakLabel = switchNode.getBreakLabel();
        final int liveLocalsOnBreak = this.method.getUsedSlotsWithLiveTemporaries();
        if (defaultCase != null && cases.size() == 1) {
            assert cases.get(0) == defaultCase;
            this.loadAndDiscard(expression);
            defaultCase.getBody().accept(this);
            this.method.breakLabel(breakLabel, liveLocalsOnBreak);
            return false;
        }
        else {
            Label defaultLabel = (defaultCase != null) ? defaultCase.getEntry() : breakLabel;
            final boolean hasSkipConversion = LocalVariableConversion.hasLiveConversion(switchNode);
            if (switchNode.isUniqueInteger()) {
                final TreeMap<Integer, Label> tree = new TreeMap<Integer, Label>();
                for (final CaseNode caseNode : cases) {
                    final Node test = caseNode.getTest();
                    if (test != null) {
                        final Integer value = ((LiteralNode)test).getValue();
                        final Label entry = caseNode.getEntry();
                        if (tree.containsKey(value)) {
                            continue;
                        }
                        tree.put(value, entry);
                    }
                }
                final int size = tree.size();
                final Integer[] values = tree.keySet().toArray(new Integer[size]);
                final Label[] labels = tree.values().toArray(new Label[size]);
                final int lo = values[0];
                final int hi = values[size - 1];
                final long range = hi - (long)lo + 1L;
                int deflt = Integer.MIN_VALUE;
                for (final int value2 : values) {
                    if (deflt == value2) {
                        ++deflt;
                    }
                    else if (deflt < value2) {
                        break;
                    }
                }
                this.loadExpressionUnbounded(expression);
                final Type type = expression.getType();
                if (!type.isInteger()) {
                    this.method.load(deflt);
                    final Class<?> exprClass = type.getTypeClass();
                    this.method.invoke(CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "switchTagAsInt", Integer.TYPE, exprClass.isPrimitive() ? exprClass : Object.class, Integer.TYPE));
                }
                if (hasSkipConversion) {
                    assert defaultLabel == breakLabel;
                    defaultLabel = new Label("switch_skip");
                }
                if (range + 1L <= size * 2 && range <= 2147483647L) {
                    final Label[] table = new Label[(int)range];
                    Arrays.fill(table, defaultLabel);
                    for (int i = 0; i < size; ++i) {
                        final int value2 = values[i];
                        table[value2 - lo] = labels[i];
                    }
                    this.method.tableswitch(lo, hi, defaultLabel, table);
                }
                else {
                    final int[] ints = new int[size];
                    for (int i = 0; i < size; ++i) {
                        ints[i] = values[i];
                    }
                    this.method.lookupswitch(defaultLabel, ints, labels);
                }
                if (hasSkipConversion) {
                    this.method.label(defaultLabel);
                    this.method.beforeJoinPoint(switchNode);
                    this.method._goto(breakLabel);
                }
            }
            else {
                final Symbol tagSymbol = switchNode.getTag();
                final int tagSlot = tagSymbol.getSlot(Type.OBJECT);
                this.loadExpressionAsObject(expression);
                this.method.store(tagSymbol, Type.OBJECT);
                for (final CaseNode caseNode2 : cases) {
                    final Expression test2 = caseNode2.getTest();
                    if (test2 != null) {
                        this.method.load(Type.OBJECT, tagSlot);
                        this.loadExpressionAsObject(test2);
                        this.method.invoke(ScriptRuntime.EQ_STRICT);
                        this.method.ifne(caseNode2.getEntry());
                    }
                }
                if (defaultCase != null) {
                    this.method._goto(defaultLabel);
                }
                else {
                    this.method.beforeJoinPoint(switchNode);
                    this.method._goto(breakLabel);
                }
            }
            assert !this.method.isReachable();
            for (final CaseNode caseNode3 : cases) {
                Label fallThroughLabel;
                if (caseNode3.getLocalVariableConversion() != null && this.method.isReachable()) {
                    fallThroughLabel = new Label("fallthrough");
                    this.method._goto(fallThroughLabel);
                }
                else {
                    fallThroughLabel = null;
                }
                this.method.label(caseNode3.getEntry());
                this.method.beforeJoinPoint(caseNode3);
                if (fallThroughLabel != null) {
                    this.method.label(fallThroughLabel);
                }
                caseNode3.getBody().accept(this);
            }
            this.method.breakLabel(breakLabel, liveLocalsOnBreak);
            return false;
        }
    }
    
    @Override
    public boolean enterThrowNode(final ThrowNode throwNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(throwNode);
        if (throwNode.isSyntheticRethrow()) {
            this.method.beforeJoinPoint(throwNode);
            final IdentNode exceptionExpr = (IdentNode)throwNode.getExpression();
            final Symbol exceptionSymbol = exceptionExpr.getSymbol();
            this.method.load(exceptionSymbol, CodeGenerator.EXCEPTION_TYPE);
            this.method.checkcast(CodeGenerator.EXCEPTION_TYPE.getTypeClass());
            this.method.athrow();
            return false;
        }
        final Source source = this.getCurrentSource();
        final Expression expression = throwNode.getExpression();
        final int position = throwNode.position();
        final int line = throwNode.getLineNumber();
        final int column = source.getColumn(position);
        this.loadExpressionAsObject(expression);
        this.method.load(source.getName());
        this.method.load(line);
        this.method.load(column);
        this.method.invoke(ECMAException.CREATE);
        this.method.beforeJoinPoint(throwNode);
        this.method.athrow();
        return false;
    }
    
    private Source getCurrentSource() {
        return ((CodeGeneratorLexicalContext)this.lc).getCurrentFunction().getSource();
    }
    
    @Override
    public boolean enterTryNode(final TryNode tryNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(tryNode);
        final Block body = tryNode.getBody();
        final List<Block> catchBlocks = tryNode.getCatchBlocks();
        final Symbol vmException = tryNode.getException();
        final Label entry = new Label("try");
        final Label recovery = new Label("catch");
        final Label exit = new Label("end_try");
        final Label skip = new Label("skip");
        this.method.canThrow(recovery);
        this.method.beforeTry(tryNode, recovery);
        this.method.label(entry);
        this.catchLabels.push(recovery);
        try {
            body.accept(this);
        }
        finally {
            assert this.catchLabels.peek() == recovery;
            this.catchLabels.pop();
        }
        this.method.label(exit);
        final boolean bodyCanThrow = exit.isAfter(entry);
        if (!bodyCanThrow) {
            return false;
        }
        this.method._try(entry, exit, recovery, Throwable.class);
        if (this.method.isReachable()) {
            this.method._goto(skip);
        }
        for (final Block inlinedFinally : tryNode.getInlinedFinallies()) {
            TryNode.getLabelledInlinedFinallyBlock(inlinedFinally).accept(this);
            assert !this.method.isReachable();
        }
        this.method._catch(recovery);
        this.method.store(vmException, CodeGenerator.EXCEPTION_TYPE);
        final int catchBlockCount = catchBlocks.size();
        final Label afterCatch = new Label("after_catch");
        for (int i = 0; i < catchBlockCount; ++i) {
            assert this.method.isReachable();
            final Block catchBlock = catchBlocks.get(i);
            ((CodeGeneratorLexicalContext)this.lc).push(catchBlock);
            this.enterBlock(catchBlock);
            final CatchNode catchNode = catchBlocks.get(i).getStatements().get(0);
            final IdentNode exception = catchNode.getException();
            final Expression exceptionCondition = catchNode.getExceptionCondition();
            final Block catchBody = catchNode.getBody();
            new Store<IdentNode>(exception) {
                @Override
                protected void storeNonDiscard() {
                }
                
                @Override
                protected void evaluate() {
                    if (catchNode.isSyntheticRethrow()) {
                        CodeGenerator.this.method.load(vmException, CodeGenerator.EXCEPTION_TYPE);
                        return;
                    }
                    final Label notEcmaException = new Label("no_ecma_exception");
                    CodeGenerator.this.method.load(vmException, CodeGenerator.EXCEPTION_TYPE).dup()._instanceof(ECMAException.class).ifeq(notEcmaException);
                    CodeGenerator.this.method.checkcast(ECMAException.class);
                    CodeGenerator.this.method.getField(ECMAException.THROWN);
                    CodeGenerator.this.method.label(notEcmaException);
                }
            }.store();
            final boolean isConditionalCatch = exceptionCondition != null;
            Label nextCatch;
            if (isConditionalCatch) {
                this.loadExpressionAsBoolean(exceptionCondition);
                nextCatch = new Label("next_catch");
                nextCatch.markAsBreakTarget();
                this.method.ifeq(nextCatch);
            }
            else {
                nextCatch = null;
            }
            catchBody.accept(this);
            this.leaveBlock(catchBlock);
            ((CodeGeneratorLexicalContext)this.lc).pop(catchBlock);
            if (nextCatch != null) {
                if (this.method.isReachable()) {
                    this.method._goto(afterCatch);
                }
                this.method.breakLabel(nextCatch, ((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount());
            }
        }
        this.method.label(afterCatch);
        if (this.method.isReachable()) {
            this.method.markDeadLocalVariable(vmException);
        }
        this.method.label(skip);
        assert tryNode.getFinallyBody() == null;
        return false;
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        final Expression init = varNode.getInit();
        final IdentNode identNode = varNode.getName();
        final Symbol identSymbol = identNode.getSymbol();
        assert identSymbol != null : "variable node " + varNode + " requires a name with a symbol";
        final boolean needsScope = identSymbol.isScope();
        if (init == null) {
            if (needsScope && varNode.isBlockScoped()) {
                this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                this.method.loadUndefined(Type.OBJECT);
                final int flags = this.getScopeCallSiteFlags(identSymbol) | (varNode.isBlockScoped() ? 32 : 0);
                assert this.isFastScope(identSymbol);
                this.storeFastScopeVar(identSymbol, flags);
            }
            return false;
        }
        this.enterStatement(varNode);
        assert this.method != null;
        if (needsScope) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        }
        if (needsScope) {
            this.loadExpressionUnbounded(init);
            final int flags = this.getScopeCallSiteFlags(identSymbol) | (varNode.isBlockScoped() ? 32 : 0);
            if (this.isFastScope(identSymbol)) {
                this.storeFastScopeVar(identSymbol, flags);
            }
            else {
                this.method.dynamicSet(identNode.getName(), flags, false);
            }
        }
        else {
            final Type identType = identNode.getType();
            if (identType == Type.UNDEFINED) {
                assert identNode.getSymbol().slotCount() == 0;
                this.loadAndDiscard(init);
                return false;
            }
            else {
                this.loadExpressionAsType(init, identType);
                this.storeIdentWithCatchConversion(identNode, identType);
            }
        }
        return false;
    }
    
    private void storeIdentWithCatchConversion(final IdentNode identNode, final Type type) {
        final LocalVariableConversion conversion = identNode.getLocalVariableConversion();
        final Symbol symbol = identNode.getSymbol();
        if (conversion != null && conversion.isLive()) {
            assert symbol == conversion.getSymbol();
            assert symbol.isBytecodeLocal();
            assert conversion.getNext() == null;
            assert conversion.getFrom() == type;
            final Label catchLabel = this.catchLabels.peek();
            assert catchLabel != CodeGenerator.METHOD_BOUNDARY;
            assert catchLabel.isReachable();
            final Type joinType = conversion.getTo();
            final Label.Stack catchStack = catchLabel.getStack();
            final int joinSlot = symbol.getSlot(joinType);
            if (catchStack.getUsedSlotsWithLiveTemporaries() > joinSlot) {
                this.method.dup();
                this.method.convert(joinType);
                this.method.store(symbol, joinType);
                catchLabel.getStack().onLocalStore(joinType, joinSlot, true);
                this.method.canThrow(catchLabel);
                this.method.store(symbol, type, false);
                return;
            }
        }
        this.method.store(symbol, type, true);
    }
    
    @Override
    public boolean enterWhileNode(final WhileNode whileNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        if (whileNode.isDoWhile()) {
            this.enterDoWhile(whileNode);
        }
        else {
            this.enterStatement(whileNode);
            this.enterForOrWhile(whileNode, null);
        }
        return false;
    }
    
    private void enterForOrWhile(final LoopNode loopNode, final JoinPredecessorExpression modify) {
        final int liveLocalsOnBreak = this.method.getUsedSlotsWithLiveTemporaries();
        final JoinPredecessorExpression test = loopNode.getTest();
        if (Expression.isAlwaysFalse(test)) {
            this.loadAndDiscard(test);
            return;
        }
        this.method.beforeJoinPoint(loopNode);
        final Label continueLabel = loopNode.getContinueLabel();
        final Label repeatLabel = (modify != null) ? new Label("for_repeat") : continueLabel;
        this.method.label(repeatLabel);
        final int liveLocalsOnContinue = this.method.getUsedSlotsWithLiveTemporaries();
        final Block body = loopNode.getBody();
        final Label breakLabel = loopNode.getBreakLabel();
        final boolean testHasLiveConversion = test != null && LocalVariableConversion.hasLiveConversion(test);
        if (Expression.isAlwaysTrue(test)) {
            if (test != null) {
                this.loadAndDiscard(test);
                if (testHasLiveConversion) {
                    this.method.beforeJoinPoint(test);
                }
            }
        }
        else if (test != null) {
            if (testHasLiveConversion) {
                this.emitBranch(test.getExpression(), body.getEntryLabel(), true);
                this.method.beforeJoinPoint(test);
                this.method._goto(breakLabel);
            }
            else {
                this.emitBranch(test.getExpression(), breakLabel, false);
            }
        }
        body.accept(this);
        if (repeatLabel != continueLabel) {
            this.emitContinueLabel(continueLabel, liveLocalsOnContinue);
        }
        if (loopNode.hasPerIterationScope() && ((CodeGeneratorLexicalContext)this.lc).getCurrentBlock().needsScope()) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            this.method.invoke(CompilerConstants.virtualCallNoLookup(ScriptObject.class, "copy", ScriptObject.class, (Class<?>[])new Class[0]));
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
        }
        if (this.method.isReachable()) {
            if (modify != null) {
                this.lineNumber(loopNode);
                this.loadAndDiscard(modify);
                this.method.beforeJoinPoint(modify);
            }
            this.method._goto(repeatLabel);
        }
        this.method.breakLabel(breakLabel, liveLocalsOnBreak);
    }
    
    private void emitContinueLabel(final Label continueLabel, final int liveLocals) {
        final boolean reachable = this.method.isReachable();
        this.method.breakLabel(continueLabel, liveLocals);
        if (!reachable) {
            this.method.undefineLocalVariables(((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount(), false);
        }
    }
    
    private void enterDoWhile(final WhileNode whileNode) {
        final int liveLocalsOnContinueOrBreak = this.method.getUsedSlotsWithLiveTemporaries();
        this.method.beforeJoinPoint(whileNode);
        final Block body = whileNode.getBody();
        body.accept(this);
        this.emitContinueLabel(whileNode.getContinueLabel(), liveLocalsOnContinueOrBreak);
        if (this.method.isReachable()) {
            this.lineNumber(whileNode);
            final JoinPredecessorExpression test = whileNode.getTest();
            final Label bodyEntryLabel = body.getEntryLabel();
            final boolean testHasLiveConversion = LocalVariableConversion.hasLiveConversion(test);
            if (Expression.isAlwaysFalse(test)) {
                this.loadAndDiscard(test);
                if (testHasLiveConversion) {
                    this.method.beforeJoinPoint(test);
                }
            }
            else if (testHasLiveConversion) {
                final Label beforeExit = new Label("do_while_preexit");
                this.emitBranch(test.getExpression(), beforeExit, false);
                this.method.beforeJoinPoint(test);
                this.method._goto(bodyEntryLabel);
                this.method.label(beforeExit);
                this.method.beforeJoinPoint(test);
            }
            else {
                this.emitBranch(test.getExpression(), bodyEntryLabel, true);
            }
        }
        this.method.breakLabel(whileNode.getBreakLabel(), liveLocalsOnContinueOrBreak);
    }
    
    @Override
    public boolean enterWithNode(final WithNode withNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        this.enterStatement(withNode);
        final Expression expression = withNode.getExpression();
        final Block body = withNode.getBody();
        final boolean hasScope = this.method.hasScope();
        if (hasScope) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        }
        this.loadExpressionAsObject(expression);
        Label tryLabel;
        if (hasScope) {
            this.method.invoke(ScriptRuntime.OPEN_WITH);
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            tryLabel = new Label("with_try");
            this.method.label(tryLabel);
        }
        else {
            this.globalCheckObjectCoercible();
            tryLabel = null;
        }
        body.accept(this);
        if (hasScope) {
            final Label endLabel = new Label("with_end");
            final Label catchLabel = new Label("with_catch");
            final Label exitLabel = new Label("with_exit");
            this.method.label(endLabel);
            final boolean bodyCanThrow = endLabel.isAfter(tryLabel);
            if (bodyCanThrow) {
                this.method._try(tryLabel, endLabel, catchLabel);
            }
            final boolean reachable = this.method.isReachable();
            if (reachable) {
                this.popScope();
                if (bodyCanThrow) {
                    this.method._goto(exitLabel);
                }
            }
            if (bodyCanThrow) {
                this.method._catch(catchLabel);
                this.popScopeException();
                this.method.athrow();
                if (reachable) {
                    this.method.label(exitLabel);
                }
            }
        }
        return false;
    }
    
    private void loadADD(final UnaryNode unaryNode, final TypeBounds resultBounds) {
        this.loadExpression(unaryNode.getExpression(), resultBounds.booleanToInt().notWiderThan(Type.NUMBER));
        if (this.method.peekType() == Type.BOOLEAN) {
            this.method.convert(Type.INT);
        }
    }
    
    private void loadBIT_NOT(final UnaryNode unaryNode) {
        this.loadExpression(unaryNode.getExpression(), TypeBounds.INT).load(-1).xor();
    }
    
    private void loadDECINC(final UnaryNode unaryNode) {
        final Expression operand = unaryNode.getExpression();
        final Type type = unaryNode.getType();
        final TypeBounds typeBounds = new TypeBounds(type, Type.NUMBER);
        final TokenType tokenType = unaryNode.tokenType();
        final boolean isPostfix = tokenType == TokenType.DECPOSTFIX || tokenType == TokenType.INCPOSTFIX;
        final boolean isIncrement = tokenType == TokenType.INCPREFIX || tokenType == TokenType.INCPOSTFIX;
        assert !type.isObject();
        new SelfModifyingStore<UnaryNode>(unaryNode, operand) {
            private void loadRhs() {
                CodeGenerator.this.loadExpression(operand, typeBounds, true);
            }
            
            @Override
            protected void evaluate() {
                if (isPostfix) {
                    this.loadRhs();
                }
                else {
                    new OptimisticOperation(unaryNode, typeBounds) {
                        @Override
                        void loadStack() {
                            CodeGenerator$13.this.loadRhs();
                            CodeGenerator$13.this.loadMinusOne();
                        }
                        
                        @Override
                        void consumeStack() {
                            CodeGenerator$13.this.doDecInc(this.getProgramPoint());
                        }
                    }.emit(getOptimisticIgnoreCountForSelfModifyingExpression(operand));
                }
            }
            
            @Override
            protected void storeNonDiscard() {
                super.storeNonDiscard();
                if (isPostfix) {
                    new OptimisticOperation(unaryNode, typeBounds) {
                        @Override
                        void loadStack() {
                            CodeGenerator$13.this.loadMinusOne();
                        }
                        
                        @Override
                        void consumeStack() {
                            CodeGenerator$13.this.doDecInc(this.getProgramPoint());
                        }
                    }.emit(1);
                }
            }
            
            private void loadMinusOne() {
                if (type.isInteger()) {
                    CodeGenerator.this.method.load(isIncrement ? 1 : -1);
                }
                else {
                    CodeGenerator.this.method.load(isIncrement ? 1.0 : -1.0);
                }
            }
            
            private void doDecInc(final int programPoint) {
                CodeGenerator.this.method.add(programPoint);
            }
        }.store();
    }
    
    private static int getOptimisticIgnoreCountForSelfModifyingExpression(final Expression target) {
        return (target instanceof AccessNode) ? 1 : ((target instanceof IndexNode) ? 2 : 0);
    }
    
    private void loadAndDiscard(final Expression expr) {
        if (!(expr instanceof LiteralNode.PrimitiveLiteralNode | isLocalVariable(expr))) {
            ((CodeGeneratorLexicalContext)this.lc).pushDiscard(expr);
            this.loadExpression(expr, TypeBounds.UNBOUNDED);
            if (((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(expr)) {
                assert !expr.isAssignment();
                this.method.pop();
            }
            return;
        }
        assert !((CodeGeneratorLexicalContext)this.lc).isCurrentDiscard(expr);
    }
    
    private void loadMaybeDiscard(final Expression parent, final Expression expr, final TypeBounds resultBounds) {
        this.loadMaybeDiscard(((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(parent), expr, resultBounds);
    }
    
    private void loadMaybeDiscard(final boolean discard, final Expression expr, final TypeBounds resultBounds) {
        if (discard) {
            this.loadAndDiscard(expr);
        }
        else {
            this.loadExpression(expr, resultBounds);
        }
    }
    
    private void loadNEW(final UnaryNode unaryNode) {
        final CallNode callNode = (CallNode)unaryNode.getExpression();
        final List<Expression> args = callNode.getArgs();
        final Expression func = callNode.getFunction();
        this.loadExpressionAsObject(func);
        this.method.dynamicNew(1 + this.loadArgs(args), this.getCallSiteFlags(), func.toString(false));
    }
    
    private void loadNOT(final UnaryNode unaryNode) {
        final Expression expr = unaryNode.getExpression();
        if (expr instanceof UnaryNode && expr.isTokenType(TokenType.NOT)) {
            this.loadExpressionAsBoolean(((UnaryNode)expr).getExpression());
        }
        else {
            final Label trueLabel = new Label("true");
            final Label afterLabel = new Label("after");
            this.emitBranch(expr, trueLabel, true);
            this.method.load(true);
            this.method._goto(afterLabel);
            this.method.label(trueLabel);
            this.method.load(false);
            this.method.label(afterLabel);
        }
    }
    
    private void loadSUB(final UnaryNode unaryNode, final TypeBounds resultBounds) {
        final Type type = unaryNode.getType();
        assert type.isNumeric();
        final TypeBounds numericBounds = resultBounds.booleanToInt();
        new OptimisticOperation(unaryNode, numericBounds) {
            @Override
            void loadStack() {
                final Expression expr = unaryNode.getExpression();
                CodeGenerator.this.loadExpression(expr, numericBounds.notWiderThan(Type.NUMBER));
            }
            
            @Override
            void consumeStack() {
                if (type.isNumber()) {
                    CodeGenerator.this.method.convert(type);
                }
                CodeGenerator.this.method.neg(this.getProgramPoint());
            }
        }.emit();
    }
    
    public void loadVOID(final UnaryNode unaryNode, final TypeBounds resultBounds) {
        this.loadAndDiscard(unaryNode.getExpression());
        if (!((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(unaryNode)) {
            this.method.loadUndefined(resultBounds.widest);
        }
    }
    
    public void loadADD(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new OptimisticOperation(binaryNode, resultBounds) {
            @Override
            void loadStack() {
                final boolean isOptimistic = UnwarrantedOptimismException.isValid(this.getProgramPoint());
                boolean forceConversionSeparation = false;
                TypeBounds operandBounds;
                if (isOptimistic) {
                    operandBounds = new TypeBounds(binaryNode.getType(), Type.OBJECT);
                }
                else {
                    final Type widestOperationType = binaryNode.getWidestOperationType();
                    operandBounds = new TypeBounds(Type.narrowest(binaryNode.getWidestOperandType(), resultBounds.widest), widestOperationType);
                    forceConversionSeparation = widestOperationType.narrowerThan(resultBounds.widest);
                }
                CodeGenerator.this.loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), operandBounds, false, forceConversionSeparation);
            }
            
            @Override
            void consumeStack() {
                CodeGenerator.this.method.add(this.getProgramPoint());
            }
        }.emit();
    }
    
    private void loadAND_OR(final BinaryNode binaryNode, final TypeBounds resultBounds, final boolean isAnd) {
        final Type narrowestOperandType = Type.widestReturnType(binaryNode.lhs().getType(), binaryNode.rhs().getType());
        final boolean isCurrentDiscard = ((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(binaryNode);
        final Label skip = new Label("skip");
        if (narrowestOperandType == Type.BOOLEAN) {
            final Label onTrue = new Label("andor_true");
            this.emitBranch(binaryNode, onTrue, true);
            if (isCurrentDiscard) {
                this.method.label(onTrue);
            }
            else {
                this.method.load(false);
                this.method._goto(skip);
                this.method.label(onTrue);
                this.method.load(true);
                this.method.label(skip);
            }
            return;
        }
        final TypeBounds outBounds = resultBounds.notNarrowerThan(narrowestOperandType);
        final JoinPredecessorExpression lhs = (JoinPredecessorExpression)binaryNode.lhs();
        final boolean lhsConvert = LocalVariableConversion.hasLiveConversion(lhs);
        final Label evalRhs = lhsConvert ? new Label("eval_rhs") : null;
        this.loadExpression(lhs, outBounds);
        if (!isCurrentDiscard) {
            this.method.dup();
        }
        this.method.convert(Type.BOOLEAN);
        if (isAnd) {
            if (lhsConvert) {
                this.method.ifne(evalRhs);
            }
            else {
                this.method.ifeq(skip);
            }
        }
        else if (lhsConvert) {
            this.method.ifeq(evalRhs);
        }
        else {
            this.method.ifne(skip);
        }
        if (lhsConvert) {
            this.method.beforeJoinPoint(lhs);
            this.method._goto(skip);
            this.method.label(evalRhs);
        }
        if (!isCurrentDiscard) {
            this.method.pop();
        }
        final JoinPredecessorExpression rhs = (JoinPredecessorExpression)binaryNode.rhs();
        this.loadMaybeDiscard(isCurrentDiscard, rhs, outBounds);
        this.method.beforeJoinPoint(rhs);
        this.method.label(skip);
    }
    
    private static boolean isLocalVariable(final Expression lhs) {
        return lhs instanceof IdentNode && isLocalVariable((IdentNode)lhs);
    }
    
    private static boolean isLocalVariable(final IdentNode lhs) {
        return lhs.getSymbol().isBytecodeLocal();
    }
    
    private void loadASSIGN(final BinaryNode binaryNode) {
        final Expression lhs = binaryNode.lhs();
        final Expression rhs = binaryNode.rhs();
        final Type rhsType = rhs.getType();
        if (lhs instanceof IdentNode) {
            final Symbol symbol = ((IdentNode)lhs).getSymbol();
            if (!symbol.isScope() && !symbol.hasSlotFor(rhsType) && ((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(binaryNode)) {
                this.loadAndDiscard(rhs);
                this.method.markDeadLocalVariable(symbol);
                return;
            }
        }
        new Store<BinaryNode>(binaryNode, lhs) {
            @Override
            protected void evaluate() {
                CodeGenerator.this.loadExpressionAsType(rhs, rhsType);
            }
        }.store();
    }
    
    private void loadASSIGN_ADD(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) {
            @Override
            protected void op(final OptimisticOperation oo) {
                assert !oo.isOptimistic;
                CodeGenerator.this.method.add(oo.getProgramPoint());
            }
        }.store();
    }
    
    private void loadASSIGN_BIT_AND(final BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) {
            @Override
            protected void op() {
                CodeGenerator.this.method.and();
            }
        }.store();
    }
    
    private void loadASSIGN_BIT_OR(final BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) {
            @Override
            protected void op() {
                CodeGenerator.this.method.or();
            }
        }.store();
    }
    
    private void loadASSIGN_BIT_XOR(final BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) {
            @Override
            protected void op() {
                CodeGenerator.this.method.xor();
            }
        }.store();
    }
    
    private void loadASSIGN_DIV(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) {
            @Override
            protected void op(final OptimisticOperation oo) {
                CodeGenerator.this.method.div(oo.getProgramPoint());
            }
        }.store();
    }
    
    private void loadASSIGN_MOD(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) {
            @Override
            protected void op(final OptimisticOperation oo) {
                CodeGenerator.this.method.rem(oo.getProgramPoint());
            }
        }.store();
    }
    
    private void loadASSIGN_MUL(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) {
            @Override
            protected void op(final OptimisticOperation oo) {
                CodeGenerator.this.method.mul(oo.getProgramPoint());
            }
        }.store();
    }
    
    private void loadASSIGN_SAR(final BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) {
            @Override
            protected void op() {
                CodeGenerator.this.method.sar();
            }
        }.store();
    }
    
    private void loadASSIGN_SHL(final BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) {
            @Override
            protected void op() {
                CodeGenerator.this.method.shl();
            }
        }.store();
    }
    
    private void loadASSIGN_SHR(final BinaryNode binaryNode) {
        new SelfModifyingStore<BinaryNode>(binaryNode, binaryNode.lhs()) {
            @Override
            protected void evaluate() {
                new OptimisticOperation((Optimistic)this.assignNode, new TypeBounds(Type.INT, Type.NUMBER)) {
                    @Override
                    void loadStack() {
                        assert ((BinaryNode)SelfModifyingStore.this.assignNode).getWidestOperandType() == Type.INT;
                        if (isRhsZero(binaryNode)) {
                            CodeGenerator.this.loadExpression(binaryNode.lhs(), TypeBounds.INT, true);
                        }
                        else {
                            CodeGenerator.this.loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), TypeBounds.INT, true, false);
                            CodeGenerator.this.method.shr();
                        }
                    }
                    
                    @Override
                    void consumeStack() {
                        if (isOptimistic(binaryNode)) {
                            CodeGenerator.this.toUint32Optimistic(binaryNode.getProgramPoint());
                        }
                        else {
                            CodeGenerator.this.toUint32Double();
                        }
                    }
                }.emit(getOptimisticIgnoreCountForSelfModifyingExpression(binaryNode.lhs()));
                CodeGenerator.this.method.convert(((BinaryNode)this.assignNode).getType());
            }
        }.store();
    }
    
    private void doSHR(final BinaryNode binaryNode) {
        new OptimisticOperation(binaryNode, new TypeBounds(Type.INT, Type.NUMBER)) {
            @Override
            void loadStack() {
                if (isRhsZero(binaryNode)) {
                    CodeGenerator.this.loadExpressionAsType(binaryNode.lhs(), Type.INT);
                }
                else {
                    CodeGenerator.this.loadBinaryOperands(binaryNode);
                    CodeGenerator.this.method.shr();
                }
            }
            
            @Override
            void consumeStack() {
                if (isOptimistic(binaryNode)) {
                    CodeGenerator.this.toUint32Optimistic(binaryNode.getProgramPoint());
                }
                else {
                    CodeGenerator.this.toUint32Double();
                }
            }
        }.emit();
    }
    
    private void toUint32Optimistic(final int programPoint) {
        this.method.load(programPoint);
        JSType.TO_UINT32_OPTIMISTIC.invoke(this.method);
    }
    
    private void toUint32Double() {
        JSType.TO_UINT32_DOUBLE.invoke(this.method);
    }
    
    private void loadASSIGN_SUB(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) {
            @Override
            protected void op(final OptimisticOperation oo) {
                CodeGenerator.this.method.sub(oo.getProgramPoint());
            }
        }.store();
    }
    
    private void loadBIT_AND(final BinaryNode binaryNode) {
        this.loadBinaryOperands(binaryNode);
        this.method.and();
    }
    
    private void loadBIT_OR(final BinaryNode binaryNode) {
        if (isRhsZero(binaryNode)) {
            this.loadExpressionAsType(binaryNode.lhs(), Type.INT);
        }
        else {
            this.loadBinaryOperands(binaryNode);
            this.method.or();
        }
    }
    
    private static boolean isRhsZero(final BinaryNode binaryNode) {
        final Expression rhs = binaryNode.rhs();
        return rhs instanceof LiteralNode && CodeGenerator.INT_ZERO.equals(((LiteralNode)rhs).getValue());
    }
    
    private void loadBIT_XOR(final BinaryNode binaryNode) {
        this.loadBinaryOperands(binaryNode);
        this.method.xor();
    }
    
    private void loadCOMMARIGHT(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        this.loadAndDiscard(binaryNode.lhs());
        this.loadMaybeDiscard(binaryNode, binaryNode.rhs(), resultBounds);
    }
    
    private void loadCOMMALEFT(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        this.loadMaybeDiscard(binaryNode, binaryNode.lhs(), resultBounds);
        this.loadAndDiscard(binaryNode.rhs());
    }
    
    private void loadDIV(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new BinaryArith() {
            @Override
            protected void op(final int programPoint) {
                CodeGenerator.this.method.div(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }
    
    private void loadCmp(final BinaryNode binaryNode, final Condition cond) {
        this.loadComparisonOperands(binaryNode);
        final Label trueLabel = new Label("trueLabel");
        final Label afterLabel = new Label("skip");
        this.method.conditionalJump(cond, trueLabel);
        this.method.load(Boolean.FALSE);
        this.method._goto(afterLabel);
        this.method.label(trueLabel);
        this.method.load(Boolean.TRUE);
        this.method.label(afterLabel);
    }
    
    private void loadMOD(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new BinaryArith() {
            @Override
            protected void op(final int programPoint) {
                CodeGenerator.this.method.rem(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }
    
    private void loadMUL(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new BinaryArith() {
            @Override
            protected void op(final int programPoint) {
                CodeGenerator.this.method.mul(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }
    
    private void loadSAR(final BinaryNode binaryNode) {
        this.loadBinaryOperands(binaryNode);
        this.method.sar();
    }
    
    private void loadSHL(final BinaryNode binaryNode) {
        this.loadBinaryOperands(binaryNode);
        this.method.shl();
    }
    
    private void loadSHR(final BinaryNode binaryNode) {
        this.doSHR(binaryNode);
    }
    
    private void loadSUB(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new BinaryArith() {
            @Override
            protected void op(final int programPoint) {
                CodeGenerator.this.method.sub(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }
    
    @Override
    public boolean enterLabelNode(final LabelNode labelNode) {
        this.labeledBlockBreakLiveLocals.push(((CodeGeneratorLexicalContext)this.lc).getUsedSlotCount());
        return true;
    }
    
    @Override
    protected boolean enterDefault(final Node node) {
        throw new AssertionError((Object)("Code generator entered node of type " + node.getClass().getName()));
    }
    
    private void loadTernaryNode(final TernaryNode ternaryNode, final TypeBounds resultBounds) {
        final Expression test = ternaryNode.getTest();
        final JoinPredecessorExpression trueExpr = ternaryNode.getTrueExpression();
        final JoinPredecessorExpression falseExpr = ternaryNode.getFalseExpression();
        final Label falseLabel = new Label("ternary_false");
        final Label exitLabel = new Label("ternary_exit");
        final Type outNarrowest = Type.narrowest(resultBounds.widest, Type.generic(Type.widestReturnType(trueExpr.getType(), falseExpr.getType())));
        final TypeBounds outBounds = resultBounds.notNarrowerThan(outNarrowest);
        this.emitBranch(test, falseLabel, false);
        final boolean isCurrentDiscard = ((CodeGeneratorLexicalContext)this.lc).popDiscardIfCurrent(ternaryNode);
        this.loadMaybeDiscard(isCurrentDiscard, trueExpr.getExpression(), outBounds);
        if (!CodeGenerator.$assertionsDisabled && !isCurrentDiscard && Type.generic(this.method.peekType()) != outBounds.narrowest) {
            throw new AssertionError();
        }
        this.method.beforeJoinPoint(trueExpr);
        this.method._goto(exitLabel);
        this.method.label(falseLabel);
        this.loadMaybeDiscard(isCurrentDiscard, falseExpr.getExpression(), outBounds);
        if (!CodeGenerator.$assertionsDisabled && !isCurrentDiscard && Type.generic(this.method.peekType()) != outBounds.narrowest) {
            throw new AssertionError();
        }
        this.method.beforeJoinPoint(falseExpr);
        this.method.label(exitLabel);
    }
    
    void generateScopeCalls() {
        for (final SharedScopeCall scopeAccess : ((CodeGeneratorLexicalContext)this.lc).getScopeCalls()) {
            scopeAccess.generateScopeCall();
        }
    }
    
    private void printSymbols(final Block block, final FunctionNode function, final String ident) {
        if (this.compiler.getScriptEnvironment()._print_symbols || function.getFlag(2097152)) {
            final PrintWriter out = this.compiler.getScriptEnvironment().getErr();
            out.println("[BLOCK in '" + ident + "']");
            if (!block.printSymbols(out)) {
                out.println("<no symbols>");
            }
            out.println();
        }
    }
    
    private void newFunctionObject(final FunctionNode functionNode, final boolean addInitializer) {
        assert ((CodeGeneratorLexicalContext)this.lc).peek() == functionNode;
        final RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(functionNode.getId());
        if (functionNode.isProgram() && !this.compiler.isOnDemandCompilation()) {
            final MethodEmitter createFunction = functionNode.getCompileUnit().getClassEmitter().method(EnumSet.of(ClassEmitter.Flag.PUBLIC, ClassEmitter.Flag.STATIC), CompilerConstants.CREATE_PROGRAM_FUNCTION.symbolName(), ScriptFunction.class, ScriptObject.class);
            createFunction.begin();
            this.loadConstantsAndIndex(data, createFunction);
            createFunction.load(CodeGenerator.SCOPE_TYPE, 0);
            createFunction.invoke(CodeGenerator.CREATE_FUNCTION_OBJECT);
            createFunction._return();
            createFunction.end();
        }
        if (addInitializer && !this.compiler.isOnDemandCompilation()) {
            functionNode.getCompileUnit().addFunctionInitializer(data, functionNode);
        }
        if (((CodeGeneratorLexicalContext)this.lc).getOutermostFunction() == functionNode) {
            return;
        }
        this.loadConstantsAndIndex(data, this.method);
        if (functionNode.needsParentScope()) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            this.method.invoke(CodeGenerator.CREATE_FUNCTION_OBJECT);
        }
        else {
            this.method.invoke(CodeGenerator.CREATE_FUNCTION_OBJECT_NO_SCOPE);
        }
    }
    
    private MethodEmitter globalInstance() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "instance", "()L" + CodeGenerator.GLOBAL_OBJECT + ';');
    }
    
    private MethodEmitter globalAllocateArguments() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "allocateArguments", CompilerConstants.methodDescriptor(ScriptObject.class, Object[].class, Object.class, Integer.TYPE));
    }
    
    private MethodEmitter globalNewRegExp() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "newRegExp", CompilerConstants.methodDescriptor(Object.class, String.class, String.class));
    }
    
    private MethodEmitter globalRegExpCopy() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "regExpCopy", CompilerConstants.methodDescriptor(Object.class, Object.class));
    }
    
    private MethodEmitter globalAllocateArray(final ArrayType type) {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "allocate", "(" + type.getDescriptor() + ")Ljdk/nashorn/internal/objects/NativeArray;");
    }
    
    private MethodEmitter globalIsEval() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "isEval", CompilerConstants.methodDescriptor(Boolean.TYPE, Object.class));
    }
    
    private MethodEmitter globalReplaceLocationPropertyPlaceholder() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "replaceLocationPropertyPlaceholder", CompilerConstants.methodDescriptor(Object.class, Object.class, Object.class));
    }
    
    private MethodEmitter globalCheckObjectCoercible() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "checkObjectCoercible", CompilerConstants.methodDescriptor(Void.TYPE, Object.class));
    }
    
    private MethodEmitter globalDirectEval() {
        return this.method.invokestatic(CodeGenerator.GLOBAL_OBJECT, "directEval", CompilerConstants.methodDescriptor(Object.class, Object.class, Object.class, Object.class, Object.class, Boolean.TYPE));
    }
    
    private static boolean isOptimistic(final Optimistic optimistic) {
        if (!optimistic.canBeOptimistic()) {
            return false;
        }
        final Expression expr = (Expression)optimistic;
        return expr.getType().narrowerThan(expr.getWidestOperationType());
    }
    
    private static boolean everyLocalLoadIsValid(final int[] loads, final int localCount) {
        for (final int load : loads) {
            if (load < 0 || load >= localCount) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean everyStackValueIsLocalLoad(final int[] loads) {
        for (final int load : loads) {
            if (load == -1) {
                return false;
            }
        }
        return true;
    }
    
    private String getLvarTypesDescriptor(final List<Type> localVarTypes) {
        final int count = localVarTypes.size();
        final StringBuilder desc = new StringBuilder(count);
        for (int i = 0; i < count; i += appendType(desc, localVarTypes.get(i))) {}
        return this.method.markSymbolBoundariesInLvarTypesDescriptor(desc.toString());
    }
    
    private static int appendType(final StringBuilder b, final Type t) {
        b.append(t.getBytecodeStackType());
        return t.getSlots();
    }
    
    private static int countSymbolsInLvarTypeDescriptor(final String lvarTypeDescriptor) {
        int count = 0;
        for (int i = 0; i < lvarTypeDescriptor.length(); ++i) {
            if (Character.isUpperCase(lvarTypeDescriptor.charAt(i))) {
                ++count;
            }
        }
        return count;
    }
    
    private boolean generateUnwarrantedOptimismExceptionHandlers(final FunctionNode fn) {
        if (!this.useOptimisticTypes()) {
            return false;
        }
        final Map<String, Collection<Label>> unwarrantedOptimismHandlers = ((CodeGeneratorLexicalContext)this.lc).popUnwarrantedOptimismHandlers();
        if (unwarrantedOptimismHandlers.isEmpty()) {
            return false;
        }
        this.method.lineNumber(0);
        final List<OptimismExceptionHandlerSpec> handlerSpecs = new ArrayList<OptimismExceptionHandlerSpec>(unwarrantedOptimismHandlers.size() * 4 / 3);
        for (final String spec : unwarrantedOptimismHandlers.keySet()) {
            handlerSpecs.add(new OptimismExceptionHandlerSpec(spec, true));
        }
        Collections.sort(handlerSpecs, Collections.reverseOrder());
        final Map<String, Label> delegationLabels = new HashMap<String, Label>();
        for (int handlerIndex = 0; handlerIndex < handlerSpecs.size(); ++handlerIndex) {
            final OptimismExceptionHandlerSpec spec2 = handlerSpecs.get(handlerIndex);
            final String lvarSpec = spec2.lvarSpec;
            if (spec2.catchTarget) {
                assert !this.method.isReachable();
                this.method._catch(unwarrantedOptimismHandlers.get(lvarSpec));
                this.method.load(countSymbolsInLvarTypeDescriptor(lvarSpec));
                this.method.newarray(Type.OBJECT_ARRAY);
            }
            if (spec2.delegationTarget) {
                this.method.label(delegationLabels.get(lvarSpec));
            }
            final boolean lastHandler = handlerIndex == handlerSpecs.size() - 1;
            int lvarIndex;
            int firstLvarIndex;
            int firstArrayIndex;
            Label delegationLabel;
            String commonLvarSpec;
            if (lastHandler) {
                lvarIndex = 0;
                firstLvarIndex = 0;
                firstArrayIndex = 0;
                delegationLabel = null;
                commonLvarSpec = null;
            }
            else {
                final int nextHandlerIndex = handlerIndex + 1;
                final String nextLvarSpec = handlerSpecs.get(nextHandlerIndex).lvarSpec;
                commonLvarSpec = commonPrefix(lvarSpec, nextLvarSpec);
                assert Character.isUpperCase(commonLvarSpec.charAt(commonLvarSpec.length() - 1));
                boolean addNewHandler = true;
                int commonHandlerIndex;
                for (commonHandlerIndex = nextHandlerIndex; commonHandlerIndex < handlerSpecs.size(); ++commonHandlerIndex) {
                    final OptimismExceptionHandlerSpec forwardHandlerSpec = handlerSpecs.get(commonHandlerIndex);
                    final String forwardLvarSpec = forwardHandlerSpec.lvarSpec;
                    if (forwardLvarSpec.equals(commonLvarSpec)) {
                        addNewHandler = false;
                        forwardHandlerSpec.delegationTarget = true;
                        break;
                    }
                    if (!forwardLvarSpec.startsWith(commonLvarSpec)) {
                        break;
                    }
                }
                if (addNewHandler) {
                    handlerSpecs.add(commonHandlerIndex, new OptimismExceptionHandlerSpec(commonLvarSpec, false));
                }
                firstArrayIndex = countSymbolsInLvarTypeDescriptor(commonLvarSpec);
                lvarIndex = 0;
                for (int j = 0; j < commonLvarSpec.length(); ++j) {
                    lvarIndex += CodeGeneratorLexicalContext.getTypeForSlotDescriptor(commonLvarSpec.charAt(j)).getSlots();
                }
                firstLvarIndex = lvarIndex;
                delegationLabel = delegationLabels.get(commonLvarSpec);
                if (delegationLabel == null) {
                    delegationLabel = new Label("uo_pa_" + commonLvarSpec);
                    delegationLabels.put(commonLvarSpec, delegationLabel);
                }
            }
            int args = 0;
            boolean symbolHadValue = false;
            for (int typeIndex = (commonLvarSpec == null) ? 0 : commonLvarSpec.length(); typeIndex < lvarSpec.length(); ++typeIndex) {
                final char typeDesc = lvarSpec.charAt(typeIndex);
                final Type lvarType = CodeGeneratorLexicalContext.getTypeForSlotDescriptor(typeDesc);
                if (!lvarType.isUnknown()) {
                    this.method.load(lvarType, lvarIndex);
                    symbolHadValue = true;
                    ++args;
                }
                else if (typeDesc == 'U' && !symbolHadValue) {
                    if (this.method.peekType() == Type.UNDEFINED) {
                        this.method.dup();
                    }
                    else {
                        this.method.loadUndefined(Type.OBJECT);
                    }
                    ++args;
                }
                if (Character.isUpperCase(typeDesc)) {
                    symbolHadValue = false;
                }
                lvarIndex += lvarType.getSlots();
            }
            assert args > 0;
            this.method.dynamicArrayPopulatorCall(args + 1, firstArrayIndex);
            if (delegationLabel != null) {
                assert !lastHandler;
                assert commonLvarSpec != null;
                this.method.undefineLocalVariables(firstLvarIndex, true);
                final OptimismExceptionHandlerSpec nextSpec = handlerSpecs.get(handlerIndex + 1);
                if (!nextSpec.lvarSpec.equals(commonLvarSpec) || nextSpec.catchTarget) {
                    this.method._goto(delegationLabel);
                }
            }
            else {
                assert lastHandler;
                this.loadConstant(getByteCodeSymbolNames(fn));
                if (this.isRestOf()) {
                    this.loadConstant(this.getContinuationEntryPoints());
                    this.method.invoke(CodeGenerator.CREATE_REWRITE_EXCEPTION_REST_OF);
                }
                else {
                    this.method.invoke(CodeGenerator.CREATE_REWRITE_EXCEPTION);
                }
                this.method.athrow();
            }
        }
        return true;
    }
    
    private static String[] getByteCodeSymbolNames(final FunctionNode fn) {
        final List<String> names = new ArrayList<String>();
        for (final Symbol symbol : fn.getBody().getSymbols()) {
            if (symbol.hasSlot()) {
                if (symbol.isScope()) {
                    assert symbol.isParam();
                    names.add(null);
                }
                else {
                    names.add(symbol.getName());
                }
            }
        }
        return names.toArray(new String[names.size()]);
    }
    
    private static String commonPrefix(final String s1, final String s2) {
        final int l1 = s1.length();
        final int i = Math.min(l1, s2.length());
        int lms = -1;
        for (int j = 0; j < i; ++j) {
            final char c1 = s1.charAt(j);
            if (c1 != s2.charAt(j)) {
                return s1.substring(0, lms + 1);
            }
            if (Character.isUpperCase(c1)) {
                lms = j;
            }
        }
        return (i == l1) ? s1 : s2;
    }
    
    private ContinuationInfo getContinuationInfo() {
        return this.continuationInfo;
    }
    
    private void generateContinuationHandler() {
        if (!this.isRestOf()) {
            return;
        }
        final ContinuationInfo ci = this.getContinuationInfo();
        this.method.label(ci.getHandlerLabel());
        this.method.lineNumber(0);
        final Label.Stack stack = ci.getTargetLabel().getStack();
        final List<Type> lvarTypes = stack.getLocalVariableTypesCopy();
        final BitSet symbolBoundary = stack.getSymbolBoundaryCopy();
        final int lvarCount = ci.lvarCount;
        final Type rewriteExceptionType = Type.typeFor(RewriteException.class);
        this.method.load(rewriteExceptionType, 0);
        this.method.storeTemp(rewriteExceptionType, lvarCount);
        this.method.load(rewriteExceptionType, 0);
        this.method.invoke(RewriteException.GET_BYTECODE_SLOTS);
        int arrayIndex = 0;
        int nextLvarIndex;
        for (int lvarIndex = 0; lvarIndex < lvarCount; lvarIndex = nextLvarIndex) {
            final Type lvarType = lvarTypes.get(lvarIndex);
            if (!lvarType.isUnknown()) {
                this.method.dup();
                this.method.load(arrayIndex).arrayload();
                final Class<?> typeClass = lvarType.getTypeClass();
                if (typeClass == long[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_LONG_ARRAY);
                }
                else if (typeClass == double[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_DOUBLE_ARRAY);
                }
                else if (typeClass == Object[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_OBJECT_ARRAY);
                }
                else {
                    if (!typeClass.isPrimitive() && typeClass != Object.class) {
                        this.method.loadType(Type.getInternalName(typeClass));
                        this.method.invoke(RewriteException.INSTANCE_OR_NULL);
                    }
                    this.method.convert(lvarType);
                }
                this.method.storeHidden(lvarType, lvarIndex, false);
            }
            nextLvarIndex = lvarIndex + lvarType.getSlots();
            if (symbolBoundary.get(nextLvarIndex - 1)) {
                ++arrayIndex;
            }
        }
        if (AssertsEnabled.assertsEnabled()) {
            this.method.load(arrayIndex);
            this.method.invoke(RewriteException.ASSERT_ARRAY_LENGTH);
        }
        else {
            this.method.pop();
        }
        final int[] stackStoreSpec = ci.getStackStoreSpec();
        final Type[] stackTypes = ci.getStackTypes();
        final boolean isStackEmpty = stackStoreSpec.length == 0;
        int replacedObjectLiteralMaps = 0;
        if (!isStackEmpty) {
            for (int i = 0; i < stackStoreSpec.length; ++i) {
                final int slot = stackStoreSpec[i];
                this.method.load(lvarTypes.get(slot), slot);
                this.method.convert(stackTypes[i]);
                final PropertyMap map = ci.getObjectLiteralMap(i);
                if (map != null) {
                    this.method.dup();
                    assert ScriptObject.class.isAssignableFrom(this.method.peekType().getTypeClass()) : this.method.peekType().getTypeClass() + " is not a script object";
                    this.loadConstant(map);
                    this.method.invoke(ScriptObject.SET_MAP);
                    ++replacedObjectLiteralMaps;
                }
            }
        }
        assert ci.objectLiteralMaps.size() == replacedObjectLiteralMaps;
        this.method.load(rewriteExceptionType, lvarCount);
        this.method.loadNull();
        this.method.storeHidden(Type.OBJECT, lvarCount);
        this.method.markDeadSlots(lvarCount, Type.OBJECT.getSlots());
        this.method.invoke(RewriteException.GET_RETURN_VALUE);
        final Type returnValueType = ci.getReturnValueType();
        boolean needsCatch = false;
        final Label targetCatchLabel = ci.catchLabel;
        Label _try = null;
        if (returnValueType.isPrimitive()) {
            this.method.lineNumber(ci.lineNumber);
            if (targetCatchLabel != CodeGenerator.METHOD_BOUNDARY) {
                _try = new Label("");
                this.method.label(_try);
                needsCatch = true;
            }
        }
        this.method.convert(returnValueType);
        final int scopePopCount = needsCatch ? ci.exceptionScopePops : 0;
        final Label catchLabel = (scopePopCount > 0) ? new Label("") : targetCatchLabel;
        if (needsCatch) {
            final Label _end_try = new Label("");
            this.method.label(_end_try);
            this.method._try(_try, _end_try, catchLabel);
        }
        this.method._goto(ci.getTargetLabel());
        if (catchLabel != targetCatchLabel) {
            this.method.lineNumber(0);
            assert scopePopCount > 0;
            this.method._catch(catchLabel);
            this.popScopes(scopePopCount);
            this.method.uncheckedGoto(targetCatchLabel);
        }
    }
    
    static {
        SCOPE_TYPE = Type.typeFor(ScriptObject.class);
        GLOBAL_OBJECT = Type.getInternalName(Global.class);
        CREATE_REWRITE_EXCEPTION = CompilerConstants.staticCallNoLookup(RewriteException.class, "create", RewriteException.class, UnwarrantedOptimismException.class, Object[].class, String[].class);
        CREATE_REWRITE_EXCEPTION_REST_OF = CompilerConstants.staticCallNoLookup(RewriteException.class, "create", RewriteException.class, UnwarrantedOptimismException.class, Object[].class, String[].class, int[].class);
        ENSURE_INT = CompilerConstants.staticCallNoLookup(OptimisticReturnFilters.class, "ensureInt", Integer.TYPE, Object.class, Integer.TYPE);
        ENSURE_NUMBER = CompilerConstants.staticCallNoLookup(OptimisticReturnFilters.class, "ensureNumber", Double.TYPE, Object.class, Integer.TYPE);
        CREATE_FUNCTION_OBJECT = CompilerConstants.staticCallNoLookup(ScriptFunction.class, "create", ScriptFunction.class, Object[].class, Integer.TYPE, ScriptObject.class);
        CREATE_FUNCTION_OBJECT_NO_SCOPE = CompilerConstants.staticCallNoLookup(ScriptFunction.class, "create", ScriptFunction.class, Object[].class, Integer.TYPE);
        TO_NUMBER_FOR_EQ = CompilerConstants.staticCallNoLookup(JSType.class, "toNumberForEq", Double.TYPE, Object.class);
        TO_NUMBER_FOR_STRICT_EQ = CompilerConstants.staticCallNoLookup(JSType.class, "toNumberForStrictEq", Double.TYPE, Object.class);
        ITERATOR_CLASS = Iterator.class;
        assert CodeGenerator.ITERATOR_CLASS == CompilerConstants.ITERATOR_PREFIX.type();
        ITERATOR_TYPE = Type.typeFor(CodeGenerator.ITERATOR_CLASS);
        EXCEPTION_TYPE = Type.typeFor(CompilerConstants.EXCEPTION_PREFIX.type());
        INT_ZERO = 0;
        OBJECT_SPILL_THRESHOLD = Options.getIntProperty("nashorn.spill.threshold", 256);
        METHOD_BOUNDARY = new Label("");
    }
    
    private class LoadScopeVar extends OptimisticOperation
    {
        final IdentNode identNode;
        private final int flags;
        
        LoadScopeVar(final IdentNode identNode, final TypeBounds resultBounds, final int flags) {
            super(identNode, resultBounds);
            this.identNode = identNode;
            this.flags = flags;
        }
        
        @Override
        void loadStack() {
            CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            this.getProto();
        }
        
        void getProto() {
        }
        
        @Override
        void consumeStack() {
            if (this.identNode.isCompileTimePropertyName()) {
                CodeGenerator.this.method.dynamicGet(Type.OBJECT, this.identNode.getSymbol().getName(), this.flags, this.identNode.isFunction(), false);
                this.replaceCompileTimeProperty();
            }
            else {
                this.dynamicGet(this.identNode.getSymbol().getName(), this.flags, this.identNode.isFunction(), false);
            }
        }
    }
    
    private class LoadFastScopeVar extends LoadScopeVar
    {
        LoadFastScopeVar(final IdentNode identNode, final TypeBounds resultBounds, final int flags) {
            super(identNode, resultBounds, flags);
        }
        
        @Override
        void getProto() {
            CodeGenerator.this.loadFastScopeProto(this.identNode.getSymbol(), false);
        }
    }
    
    private static final class TypeBounds
    {
        final Type narrowest;
        final Type widest;
        static final TypeBounds UNBOUNDED;
        static final TypeBounds INT;
        static final TypeBounds NUMBER;
        static final TypeBounds OBJECT;
        static final TypeBounds BOOLEAN;
        
        static TypeBounds exact(final Type type) {
            return new TypeBounds(type, type);
        }
        
        TypeBounds(final Type narrowest, final Type widest) {
            assert widest != null && widest != Type.UNDEFINED && widest != Type.UNKNOWN : widest;
            assert narrowest != null && narrowest != Type.UNDEFINED : narrowest;
            assert !narrowest.widerThan(widest) : narrowest + " wider than " + widest;
            assert !widest.narrowerThan(narrowest);
            this.narrowest = Type.generic(narrowest);
            this.widest = Type.generic(widest);
        }
        
        TypeBounds notNarrowerThan(final Type type) {
            return this.maybeNew(Type.narrowest(Type.widest(this.narrowest, type), this.widest), this.widest);
        }
        
        TypeBounds notWiderThan(final Type type) {
            return this.maybeNew(Type.narrowest(this.narrowest, type), Type.narrowest(this.widest, type));
        }
        
        boolean canBeNarrowerThan(final Type type) {
            return this.narrowest.narrowerThan(type);
        }
        
        TypeBounds maybeNew(final Type newNarrowest, final Type newWidest) {
            if (newNarrowest == this.narrowest && newWidest == this.widest) {
                return this;
            }
            return new TypeBounds(newNarrowest, newWidest);
        }
        
        TypeBounds booleanToInt() {
            return this.maybeNew(booleanToInt(this.narrowest), booleanToInt(this.widest));
        }
        
        TypeBounds objectToNumber() {
            return this.maybeNew(objectToNumber(this.narrowest), objectToNumber(this.widest));
        }
        
        Type within(final Type type) {
            if (type.narrowerThan(this.narrowest)) {
                return this.narrowest;
            }
            if (type.widerThan(this.widest)) {
                return this.widest;
            }
            return type;
        }
        
        @Override
        public String toString() {
            return "[" + this.narrowest + ", " + this.widest + "]";
        }
        
        static {
            UNBOUNDED = new TypeBounds(Type.UNKNOWN, Type.OBJECT);
            INT = exact(Type.INT);
            NUMBER = exact(Type.NUMBER);
            OBJECT = exact(Type.OBJECT);
            BOOLEAN = exact(Type.BOOLEAN);
        }
    }
    
    private abstract class BinaryOptimisticSelfAssignment extends SelfModifyingStore<BinaryNode>
    {
        BinaryOptimisticSelfAssignment(final BinaryNode node) {
            super(node, node.lhs());
        }
        
        protected abstract void op(final OptimisticOperation p0);
        
        @Override
        protected void evaluate() {
            final Expression lhs = ((BinaryNode)this.assignNode).lhs();
            final Expression rhs = ((BinaryNode)this.assignNode).rhs();
            final Type widestOperationType = ((BinaryNode)this.assignNode).getWidestOperationType();
            final TypeBounds bounds = new TypeBounds(((BinaryNode)this.assignNode).getType(), widestOperationType);
            new OptimisticOperation((Optimistic)this.assignNode, bounds) {
                @Override
                void loadStack() {
                    boolean forceConversionSeparation;
                    if (UnwarrantedOptimismException.isValid(this.getProgramPoint()) || widestOperationType == Type.NUMBER) {
                        forceConversionSeparation = false;
                    }
                    else {
                        final Type operandType = Type.widest(booleanToInt(objectToNumber(lhs.getType())), booleanToInt(objectToNumber(rhs.getType())));
                        forceConversionSeparation = operandType.narrowerThan(widestOperationType);
                    }
                    CodeGenerator.this.loadBinaryOperands(lhs, rhs, bounds, true, forceConversionSeparation);
                }
                
                @Override
                void consumeStack() {
                    BinaryOptimisticSelfAssignment.this.op(this);
                }
            }.emit(getOptimisticIgnoreCountForSelfModifyingExpression(lhs));
            CodeGenerator.this.method.convert(((BinaryNode)this.assignNode).getType());
        }
    }
    
    private abstract class BinarySelfAssignment extends SelfModifyingStore<BinaryNode>
    {
        BinarySelfAssignment(final BinaryNode node) {
            super(node, node.lhs());
        }
        
        protected abstract void op();
        
        @Override
        protected void evaluate() {
            CodeGenerator.this.loadBinaryOperands(((BinaryNode)this.assignNode).lhs(), ((BinaryNode)this.assignNode).rhs(), TypeBounds.UNBOUNDED.notWiderThan(((BinaryNode)this.assignNode).getWidestOperandType()), true, false);
            this.op();
        }
    }
    
    private abstract class BinaryArith
    {
        protected abstract void op(final int p0);
        
        protected void evaluate(final BinaryNode node, final TypeBounds resultBounds) {
            final TypeBounds numericBounds = resultBounds.booleanToInt().objectToNumber();
            new OptimisticOperation(node, numericBounds) {
                @Override
                void loadStack() {
                    boolean forceConversionSeparation = false;
                    TypeBounds operandBounds;
                    if (numericBounds.narrowest == Type.NUMBER) {
                        assert numericBounds.widest == Type.NUMBER;
                        operandBounds = numericBounds;
                    }
                    else {
                        final boolean isOptimistic = UnwarrantedOptimismException.isValid(this.getProgramPoint());
                        if (isOptimistic || node.isTokenType(TokenType.DIV) || node.isTokenType(TokenType.MOD)) {
                            operandBounds = new TypeBounds(node.getType(), Type.NUMBER);
                        }
                        else {
                            operandBounds = new TypeBounds(Type.narrowest(node.getWidestOperandType(), numericBounds.widest), Type.NUMBER);
                            forceConversionSeparation = true;
                        }
                    }
                    CodeGenerator.this.loadBinaryOperands(node.lhs(), node.rhs(), operandBounds, false, forceConversionSeparation);
                }
                
                @Override
                void consumeStack() {
                    BinaryArith.this.op(this.getProgramPoint());
                }
            }.emit();
        }
    }
    
    private abstract class SelfModifyingStore<T extends Expression> extends Store<T>
    {
        protected SelfModifyingStore(final T assignNode, final Expression target) {
            super(assignNode, target);
        }
        
        @Override
        protected boolean isSelfModifying() {
            return true;
        }
    }
    
    private abstract class Store<T extends Expression>
    {
        protected final T assignNode;
        private final Expression target;
        private int depth;
        private IdentNode quick;
        
        protected Store(final T assignNode, final Expression target) {
            this.assignNode = assignNode;
            this.target = target;
        }
        
        protected Store(final CodeGenerator codeGenerator, final T assignNode) {
            this(assignNode, assignNode);
        }
        
        protected boolean isSelfModifying() {
            return false;
        }
        
        private void prologue() {
            this.target.accept(new SimpleNodeVisitor() {
                @Override
                public boolean enterIdentNode(final IdentNode node) {
                    if (node.getSymbol().isScope()) {
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        Store.this.depth += Type.SCOPE.getSlots();
                        assert Store.this.depth == 1;
                    }
                    return false;
                }
                
                private void enterBaseNode() {
                    assert Store.this.target instanceof BaseNode : "error - base node " + Store.this.target + " must be instanceof BaseNode";
                    final BaseNode baseNode = (BaseNode)Store.this.target;
                    final Expression base = baseNode.getBase();
                    CodeGenerator.this.loadExpressionAsObject(base);
                    Store.this.depth += Type.OBJECT.getSlots();
                    assert Store.this.depth == 1;
                    if (Store.this.isSelfModifying()) {
                        CodeGenerator.this.method.dup();
                    }
                }
                
                @Override
                public boolean enterAccessNode(final AccessNode node) {
                    this.enterBaseNode();
                    return false;
                }
                
                @Override
                public boolean enterIndexNode(final IndexNode node) {
                    this.enterBaseNode();
                    final Expression index = node.getIndex();
                    if (!index.getType().isNumeric()) {
                        CodeGenerator.this.loadExpressionAsObject(index);
                    }
                    else {
                        CodeGenerator.this.loadExpressionUnbounded(index);
                    }
                    Store.this.depth += index.getType().getSlots();
                    if (Store.this.isSelfModifying()) {
                        CodeGenerator.this.method.dup(1);
                    }
                    return false;
                }
            });
        }
        
        private IdentNode quickLocalVariable(final Type type) {
            final String name = ((CodeGeneratorLexicalContext)CodeGenerator.this.lc).getCurrentFunction().uniqueName(CompilerConstants.QUICK_PREFIX.symbolName());
            final Symbol symbol = new Symbol(name, 1088);
            symbol.setHasSlotFor(type);
            symbol.setFirstSlot(((CodeGeneratorLexicalContext)CodeGenerator.this.lc).quickSlot(type));
            final IdentNode quickIdent = IdentNode.createInternalIdentifier(symbol).setType(type);
            return quickIdent;
        }
        
        protected void storeNonDiscard() {
            if (!((CodeGeneratorLexicalContext)CodeGenerator.this.lc).popDiscardIfCurrent(this.assignNode)) {
                if (CodeGenerator.this.method.dup(this.depth) == null) {
                    CodeGenerator.this.method.dup();
                    final Type quickType = CodeGenerator.this.method.peekType();
                    this.quick = this.quickLocalVariable(quickType);
                    final Symbol quickSymbol = this.quick.getSymbol();
                    CodeGenerator.this.method.storeTemp(quickType, quickSymbol.getFirstSlot());
                }
                return;
            }
            assert this.assignNode.isAssignment();
        }
        
        private void epilogue() {
            this.target.accept(new SimpleNodeVisitor() {
                @Override
                protected boolean enterDefault(final Node node) {
                    throw new AssertionError((Object)("Unexpected node " + node + " in store epilogue"));
                }
                
                @Override
                public boolean enterIdentNode(final IdentNode node) {
                    final Symbol symbol = node.getSymbol();
                    assert symbol != null;
                    if (symbol.isScope()) {
                        final int flags = CodeGenerator.this.getScopeCallSiteFlags(symbol);
                        if (CodeGenerator.this.isFastScope(symbol)) {
                            CodeGenerator.this.storeFastScopeVar(symbol, flags);
                        }
                        else {
                            CodeGenerator.this.method.dynamicSet(node.getName(), flags, false);
                        }
                    }
                    else {
                        final Type storeType = Store.this.assignNode.getType();
                        assert storeType != Type.LONG;
                        if (symbol.hasSlotFor(storeType)) {
                            CodeGenerator.this.method.convert(storeType);
                        }
                        CodeGenerator.this.storeIdentWithCatchConversion(node, storeType);
                    }
                    return false;
                }
                
                @Override
                public boolean enterAccessNode(final AccessNode node) {
                    CodeGenerator.this.method.dynamicSet(node.getProperty(), CodeGenerator.this.getCallSiteFlags(), node.isIndex());
                    return false;
                }
                
                @Override
                public boolean enterIndexNode(final IndexNode node) {
                    CodeGenerator.this.method.dynamicSetIndex(CodeGenerator.this.getCallSiteFlags());
                    return false;
                }
            });
        }
        
        protected abstract void evaluate();
        
        void store() {
            if (this.target instanceof IdentNode) {
                CodeGenerator.this.checkTemporalDeadZone((IdentNode)this.target);
            }
            this.prologue();
            this.evaluate();
            this.storeNonDiscard();
            this.epilogue();
            if (this.quick != null) {
                CodeGenerator.this.method.load(this.quick);
            }
        }
    }
    
    private abstract class OptimisticOperation
    {
        private final boolean isOptimistic;
        private final Expression expression;
        private final Optimistic optimistic;
        private final TypeBounds resultBounds;
        
        OptimisticOperation(final Optimistic optimistic, final TypeBounds resultBounds) {
            this.optimistic = optimistic;
            this.expression = (Expression)optimistic;
            this.resultBounds = resultBounds;
            this.isOptimistic = (isOptimistic(optimistic) && CodeGenerator.this.useOptimisticTypes() && resultBounds.within(Type.generic(((Expression)optimistic).getType())).narrowerThan(resultBounds.widest));
        }
        
        MethodEmitter emit() {
            return this.emit(0);
        }
        
        MethodEmitter emit(final int ignoredArgCount) {
            final int programPoint = this.optimistic.getProgramPoint();
            final boolean optimisticOrContinuation = this.isOptimistic || CodeGenerator.this.isContinuationEntryPoint(programPoint);
            final boolean currentContinuationEntryPoint = CodeGenerator.this.isCurrentContinuationEntryPoint(programPoint);
            final int stackSizeOnEntry = CodeGenerator.this.method.getStackSize() - ignoredArgCount;
            this.storeStack(ignoredArgCount, optimisticOrContinuation);
            this.loadStack();
            final int liveLocalsCount = this.storeStack(CodeGenerator.this.method.getStackSize() - stackSizeOnEntry, optimisticOrContinuation);
            assert optimisticOrContinuation == (liveLocalsCount != -1);
            final Label afterConsumeStack = (this.isOptimistic || currentContinuationEntryPoint) ? new Label("after_consume_stack") : null;
            Label beginTry;
            Label catchLabel;
            if (this.isOptimistic) {
                beginTry = new Label("try_optimistic");
                final String catchLabelName = ((afterConsumeStack == null) ? "" : afterConsumeStack.toString()) + "_handler";
                catchLabel = new Label(catchLabelName);
                CodeGenerator.this.method.label(beginTry);
            }
            else {
                catchLabel = (beginTry = null);
            }
            this.consumeStack();
            if (this.isOptimistic) {
                CodeGenerator.this.method._try(beginTry, afterConsumeStack, catchLabel, UnwarrantedOptimismException.class);
            }
            if (this.isOptimistic || currentContinuationEntryPoint) {
                CodeGenerator.this.method.label(afterConsumeStack);
                final int[] localLoads = CodeGenerator.this.method.getLocalLoadsOnStack(0, stackSizeOnEntry);
                assert everyStackValueIsLocalLoad(localLoads) : Arrays.toString(localLoads) + ", " + stackSizeOnEntry + ", " + ignoredArgCount;
                final List<Type> localTypesList = CodeGenerator.this.method.getLocalVariableTypes();
                final int usedLocals = CodeGenerator.this.method.getUsedSlotsWithLiveTemporaries();
                final List<Type> localTypes = CodeGenerator.this.method.getWidestLiveLocals(localTypesList.subList(0, usedLocals));
                assert everyLocalLoadIsValid(localLoads, usedLocals) : Arrays.toString(localLoads) + " ~ " + localTypes;
                if (this.isOptimistic) {
                    this.addUnwarrantedOptimismHandlerLabel(localTypes, catchLabel);
                }
                if (currentContinuationEntryPoint) {
                    final ContinuationInfo ci = CodeGenerator.this.getContinuationInfo();
                    assert ci != null : "no continuation info found for " + ((CodeGeneratorLexicalContext)CodeGenerator.this.lc).getCurrentFunction();
                    assert !ci.hasTargetLabel();
                    ci.setTargetLabel(afterConsumeStack);
                    ci.getHandlerLabel().markAsOptimisticContinuationHandlerFor(afterConsumeStack);
                    ci.lvarCount = localTypes.size();
                    ci.setStackStoreSpec(localLoads);
                    ci.setStackTypes(Arrays.copyOf(CodeGenerator.this.method.getTypesFromStack(CodeGenerator.this.method.getStackSize()), stackSizeOnEntry));
                    assert ci.getStackStoreSpec().length == ci.getStackTypes().length;
                    ci.setReturnValueType(CodeGenerator.this.method.peekType());
                    ci.lineNumber = CodeGenerator.this.getLastLineNumber();
                    ci.catchLabel = CodeGenerator.this.catchLabels.peek();
                }
            }
            return CodeGenerator.this.method;
        }
        
        private int storeStack(final int ignoreArgCount, final boolean optimisticOrContinuation) {
            if (!optimisticOrContinuation) {
                return -1;
            }
            final int stackSize = CodeGenerator.this.method.getStackSize();
            final Type[] stackTypes = CodeGenerator.this.method.getTypesFromStack(stackSize);
            final int[] localLoadsOnStack = CodeGenerator.this.method.getLocalLoadsOnStack(0, stackSize);
            final int usedSlots = CodeGenerator.this.method.getUsedSlotsWithLiveTemporaries();
            int firstIgnored;
            int firstNonLoad;
            for (firstIgnored = stackSize - ignoreArgCount, firstNonLoad = 0; firstNonLoad < firstIgnored && localLoadsOnStack[firstNonLoad] != -1; ++firstNonLoad) {}
            if (firstNonLoad >= firstIgnored) {
                return usedSlots;
            }
            int tempSlotsNeeded = 0;
            for (int i = firstNonLoad; i < stackSize; ++i) {
                if (localLoadsOnStack[i] == -1) {
                    tempSlotsNeeded += stackTypes[i].getSlots();
                }
            }
            int lastTempSlot = usedSlots + tempSlotsNeeded;
            int ignoreSlotCount = 0;
            int j = stackSize;
            while (j-- > firstNonLoad) {
                final int loadSlot = localLoadsOnStack[j];
                if (loadSlot == -1) {
                    final Type type = stackTypes[j];
                    final int slots = type.getSlots();
                    lastTempSlot -= slots;
                    if (j >= firstIgnored) {
                        ignoreSlotCount += slots;
                    }
                    CodeGenerator.this.method.storeTemp(type, lastTempSlot);
                }
                else {
                    CodeGenerator.this.method.pop();
                }
            }
            assert lastTempSlot == usedSlots;
            final List<Type> localTypesList = CodeGenerator.this.method.getLocalVariableTypes();
            for (int k = firstNonLoad; k < stackSize; ++k) {
                final int loadSlot2 = localLoadsOnStack[k];
                final Type stackType = stackTypes[k];
                final boolean isLoad = loadSlot2 != -1;
                final int lvarSlot = isLoad ? loadSlot2 : lastTempSlot;
                final Type lvarType = localTypesList.get(lvarSlot);
                CodeGenerator.this.method.load(lvarType, lvarSlot);
                if (isLoad) {
                    CodeGenerator.this.method.convert(stackType);
                }
                else {
                    assert lvarType == stackType;
                    lastTempSlot += lvarType.getSlots();
                }
            }
            assert lastTempSlot == usedSlots + tempSlotsNeeded;
            return lastTempSlot - ignoreSlotCount;
        }
        
        private void addUnwarrantedOptimismHandlerLabel(final List<Type> localTypes, final Label label) {
            final String lvarTypesDescriptor = CodeGenerator.this.getLvarTypesDescriptor(localTypes);
            final Map<String, Collection<Label>> unwarrantedOptimismHandlers = ((CodeGeneratorLexicalContext)CodeGenerator.this.lc).getUnwarrantedOptimismHandlers();
            Collection<Label> labels = unwarrantedOptimismHandlers.get(lvarTypesDescriptor);
            if (labels == null) {
                labels = new LinkedList<Label>();
                unwarrantedOptimismHandlers.put(lvarTypesDescriptor, labels);
            }
            CodeGenerator.this.method.markLabelAsOptimisticCatchHandler(label, localTypes.size());
            labels.add(label);
        }
        
        abstract void loadStack();
        
        abstract void consumeStack();
        
        MethodEmitter dynamicGet(final String name, final int flags, final boolean isMethod, final boolean isIndex) {
            if (this.isOptimistic) {
                return CodeGenerator.this.method.dynamicGet(this.getOptimisticCoercedType(), name, this.getOptimisticFlags(flags), isMethod, isIndex);
            }
            return CodeGenerator.this.method.dynamicGet(this.resultBounds.within(this.expression.getType()), name, CodeGenerator.nonOptimisticFlags(flags), isMethod, isIndex);
        }
        
        MethodEmitter dynamicGetIndex(final int flags, final boolean isMethod) {
            if (this.isOptimistic) {
                return CodeGenerator.this.method.dynamicGetIndex(this.getOptimisticCoercedType(), this.getOptimisticFlags(flags), isMethod);
            }
            return CodeGenerator.this.method.dynamicGetIndex(this.resultBounds.within(this.expression.getType()), CodeGenerator.nonOptimisticFlags(flags), isMethod);
        }
        
        MethodEmitter dynamicCall(final int argCount, final int flags, final String msg) {
            if (this.isOptimistic) {
                return CodeGenerator.this.method.dynamicCall(this.getOptimisticCoercedType(), argCount, this.getOptimisticFlags(flags), msg);
            }
            return CodeGenerator.this.method.dynamicCall(this.resultBounds.within(this.expression.getType()), argCount, CodeGenerator.nonOptimisticFlags(flags), msg);
        }
        
        int getOptimisticFlags(final int flags) {
            return flags | 0x8 | this.optimistic.getProgramPoint() << 11;
        }
        
        int getProgramPoint() {
            return this.isOptimistic ? this.optimistic.getProgramPoint() : -1;
        }
        
        void convertOptimisticReturnValue() {
            if (this.isOptimistic) {
                final Type optimisticType = this.getOptimisticCoercedType();
                if (!optimisticType.isObject()) {
                    CodeGenerator.this.method.load(this.optimistic.getProgramPoint());
                    if (optimisticType.isInteger()) {
                        CodeGenerator.this.method.invoke(CodeGenerator.ENSURE_INT);
                    }
                    else {
                        if (!optimisticType.isNumber()) {
                            throw new AssertionError(optimisticType);
                        }
                        CodeGenerator.this.method.invoke(CodeGenerator.ENSURE_NUMBER);
                    }
                }
            }
        }
        
        void replaceCompileTimeProperty() {
            final IdentNode identNode = (IdentNode)this.expression;
            final String name = identNode.getSymbol().getName();
            if (CompilerConstants.__FILE__.name().equals(name)) {
                this.replaceCompileTimeProperty(CodeGenerator.this.getCurrentSource().getName());
            }
            else if (CompilerConstants.__DIR__.name().equals(name)) {
                this.replaceCompileTimeProperty(CodeGenerator.this.getCurrentSource().getBase());
            }
            else if (CompilerConstants.__LINE__.name().equals(name)) {
                this.replaceCompileTimeProperty(CodeGenerator.this.getCurrentSource().getLine(identNode.position()));
            }
        }
        
        private void replaceCompileTimeProperty(final Object propertyValue) {
            assert CodeGenerator.this.method.peekType().isObject();
            if (propertyValue instanceof String || propertyValue == null) {
                CodeGenerator.this.method.load((String)propertyValue);
            }
            else {
                if (!(propertyValue instanceof Integer)) {
                    throw new AssertionError();
                }
                CodeGenerator.this.method.load((int)propertyValue);
                CodeGenerator.this.method.convert(Type.OBJECT);
            }
            CodeGenerator.this.globalReplaceLocationPropertyPlaceholder();
            this.convertOptimisticReturnValue();
        }
        
        private Type getOptimisticCoercedType() {
            final Type optimisticType = this.expression.getType();
            assert this.resultBounds.widest.widerThan(optimisticType);
            final Type narrowest = this.resultBounds.narrowest;
            if (narrowest.isBoolean() || narrowest.narrowerThan(optimisticType)) {
                assert !optimisticType.isObject();
                return optimisticType;
            }
            else {
                assert !narrowest.isObject();
                return narrowest;
            }
        }
    }
    
    private static class OptimismExceptionHandlerSpec implements Comparable<OptimismExceptionHandlerSpec>
    {
        private final String lvarSpec;
        private final boolean catchTarget;
        private boolean delegationTarget;
        
        OptimismExceptionHandlerSpec(final String lvarSpec, final boolean catchTarget) {
            this.lvarSpec = lvarSpec;
            if (!(this.catchTarget = catchTarget)) {
                this.delegationTarget = true;
            }
        }
        
        @Override
        public int compareTo(final OptimismExceptionHandlerSpec o) {
            return this.lvarSpec.compareTo(o.lvarSpec);
        }
        
        @Override
        public String toString() {
            final StringBuilder b = new StringBuilder(64).append("[HandlerSpec ").append(this.lvarSpec);
            if (this.catchTarget) {
                b.append(", catchTarget");
            }
            if (this.delegationTarget) {
                b.append(", delegationTarget");
            }
            return b.append("]").toString();
        }
    }
    
    private static class ContinuationInfo
    {
        private final Label handlerLabel;
        private Label targetLabel;
        int lvarCount;
        private int[] stackStoreSpec;
        private Type[] stackTypes;
        private Type returnValueType;
        private Map<Integer, PropertyMap> objectLiteralMaps;
        private int lineNumber;
        private Label catchLabel;
        private int exceptionScopePops;
        
        ContinuationInfo() {
            this.handlerLabel = new Label("continuation_handler");
        }
        
        Label getHandlerLabel() {
            return this.handlerLabel;
        }
        
        boolean hasTargetLabel() {
            return this.targetLabel != null;
        }
        
        Label getTargetLabel() {
            return this.targetLabel;
        }
        
        void setTargetLabel(final Label targetLabel) {
            this.targetLabel = targetLabel;
        }
        
        int[] getStackStoreSpec() {
            return this.stackStoreSpec.clone();
        }
        
        void setStackStoreSpec(final int[] stackStoreSpec) {
            this.stackStoreSpec = stackStoreSpec;
        }
        
        Type[] getStackTypes() {
            return this.stackTypes.clone();
        }
        
        void setStackTypes(final Type[] stackTypes) {
            this.stackTypes = stackTypes;
        }
        
        Type getReturnValueType() {
            return this.returnValueType;
        }
        
        void setReturnValueType(final Type returnValueType) {
            this.returnValueType = returnValueType;
        }
        
        void setObjectLiteralMap(final int objectLiteralStackDepth, final PropertyMap objectLiteralMap) {
            if (this.objectLiteralMaps == null) {
                this.objectLiteralMaps = new HashMap<Integer, PropertyMap>();
            }
            this.objectLiteralMaps.put(objectLiteralStackDepth, objectLiteralMap);
        }
        
        PropertyMap getObjectLiteralMap(final int stackDepth) {
            return (this.objectLiteralMaps == null) ? null : this.objectLiteralMaps.get(stackDepth);
        }
        
        @Override
        public String toString() {
            return "[localVariableTypes=" + this.targetLabel.getStack().getLocalVariableTypesCopy() + ", stackStoreSpec=" + Arrays.toString(this.stackStoreSpec) + ", returnValueType=" + this.returnValueType + "]";
        }
    }
    
    interface SplitLiteralCreator
    {
        void populateRange(final MethodEmitter p0, final Type p1, final int p2, final int p3, final int p4);
    }
}
