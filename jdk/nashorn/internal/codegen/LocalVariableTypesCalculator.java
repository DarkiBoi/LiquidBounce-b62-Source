// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.LinkedList;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.ForNode;
import java.util.List;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.Expression;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.ReturnNode;
import java.util.Deque;
import java.util.Set;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.Map;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

final class LocalVariableTypesCalculator extends SimpleNodeVisitor
{
    private static final Map<Type, LvarType> TO_LVAR_TYPE;
    private final Compiler compiler;
    private final Map<Label, JumpTarget> jumpTargets;
    private Map<Symbol, LvarType> localVariableTypes;
    private final Set<Symbol> invalidatedSymbols;
    private final Deque<LvarType> typeStack;
    private boolean reachable;
    private Type returnType;
    private ReturnNode syntheticReturn;
    private boolean alreadyEnteredTopLevelFunction;
    private final Map<JoinPredecessor, LocalVariableConversion> localVariableConversions;
    private final Map<IdentNode, LvarType> identifierLvarTypes;
    private final Map<Symbol, SymbolConversions> symbolConversions;
    private final Deque<Label> catchLabels;
    
    private static HashMap<Symbol, LvarType> cloneMap(final Map<Symbol, LvarType> map) {
        return (HashMap<Symbol, LvarType>)((HashMap)map).clone();
    }
    
    private LocalVariableConversion createConversion(final Symbol symbol, final LvarType branchLvarType, final Map<Symbol, LvarType> joinLvarTypes, final LocalVariableConversion next) {
        if (this.invalidatedSymbols.contains(symbol)) {
            return next;
        }
        final LvarType targetType = joinLvarTypes.get(symbol);
        assert targetType != null;
        if (targetType == branchLvarType) {
            return next;
        }
        this.symbolIsConverted(symbol, branchLvarType, targetType);
        return new LocalVariableConversion(symbol, branchLvarType.type, targetType.type, next);
    }
    
    private Map<Symbol, LvarType> getUnionTypes(final Map<Symbol, LvarType> types1, final Map<Symbol, LvarType> types2) {
        if (types1 == types2 || types1.isEmpty()) {
            return types2;
        }
        if (types2.isEmpty()) {
            return types1;
        }
        final Set<Symbol> commonSymbols = new HashSet<Symbol>(types1.keySet());
        commonSymbols.retainAll(types2.keySet());
        final int commonSize = commonSymbols.size();
        final int types1Size = types1.size();
        final int types2Size = types2.size();
        if (commonSize == types1Size && commonSize == types2Size) {
            boolean matches1 = true;
            boolean matches2 = true;
            Map<Symbol, LvarType> union = null;
            for (final Symbol symbol : commonSymbols) {
                final LvarType type1 = types1.get(symbol);
                final LvarType type2 = types2.get(symbol);
                final LvarType widest = widestLvarType(type1, type2);
                if (widest != type1 && matches1) {
                    matches1 = false;
                    if (!matches2) {
                        union = cloneMap(types1);
                    }
                }
                if (widest != type2 && matches2) {
                    matches2 = false;
                    if (!matches1) {
                        union = cloneMap(types2);
                    }
                }
                if (!matches1 && !matches2) {
                    assert union != null;
                    union.put(symbol, widest);
                }
            }
            return matches1 ? types1 : (matches2 ? types2 : union);
        }
        Map<Symbol, LvarType> union2;
        if (types1Size > types2Size) {
            union2 = cloneMap(types1);
            union2.putAll(types2);
        }
        else {
            union2 = cloneMap(types2);
            union2.putAll(types1);
        }
        for (final Symbol symbol2 : commonSymbols) {
            final LvarType type3 = types1.get(symbol2);
            final LvarType type4 = types2.get(symbol2);
            union2.put(symbol2, widestLvarType(type3, type4));
        }
        union2.keySet().removeAll(this.invalidatedSymbols);
        return union2;
    }
    
    private static void symbolIsUsed(final Symbol symbol, final LvarType type) {
        if (type != LvarType.UNDEFINED) {
            symbol.setHasSlotFor(type.type);
        }
    }
    
    private void symbolIsConverted(final Symbol symbol, final LvarType from, final LvarType to) {
        SymbolConversions conversions = this.symbolConversions.get(symbol);
        if (conversions == null) {
            conversions = new SymbolConversions();
            this.symbolConversions.put(symbol, conversions);
        }
        conversions.recordConversion(from, to);
    }
    
    private static LvarType toLvarType(final Type type) {
        assert type != null;
        final LvarType lvarType = LocalVariableTypesCalculator.TO_LVAR_TYPE.get(type);
        if (lvarType != null) {
            return lvarType;
        }
        assert type.isObject() : "Unsupported primitive type: " + type;
        return LvarType.OBJECT;
    }
    
    private static LvarType widestLvarType(final LvarType t1, final LvarType t2) {
        if (t1 == t2) {
            return t1;
        }
        if (t1.ordinal() < LvarType.INT.ordinal() || t2.ordinal() < LvarType.INT.ordinal()) {
            return LvarType.OBJECT;
        }
        return LvarType.values()[Math.max(t1.ordinal(), t2.ordinal())];
    }
    
    LocalVariableTypesCalculator(final Compiler compiler) {
        this.jumpTargets = new IdentityHashMap<Label, JumpTarget>();
        this.localVariableTypes = Collections.emptyMap();
        this.invalidatedSymbols = new HashSet<Symbol>();
        this.typeStack = new ArrayDeque<LvarType>();
        this.reachable = true;
        this.returnType = Type.UNKNOWN;
        this.localVariableConversions = new IdentityHashMap<JoinPredecessor, LocalVariableConversion>();
        this.identifierLvarTypes = new IdentityHashMap<IdentNode, LvarType>();
        this.symbolConversions = new IdentityHashMap<Symbol, SymbolConversions>();
        this.catchLabels = new ArrayDeque<Label>();
        this.compiler = compiler;
    }
    
    private JumpTarget createJumpTarget(final Label label) {
        assert !this.jumpTargets.containsKey(label);
        final JumpTarget jumpTarget = new JumpTarget();
        this.jumpTargets.put(label, jumpTarget);
        return jumpTarget;
    }
    
    private void doesNotContinueSequentially() {
        this.reachable = false;
        this.localVariableTypes = Collections.emptyMap();
        this.assertTypeStackIsEmpty();
    }
    
    private boolean pushExpressionType(final Expression expr) {
        this.typeStack.push(toLvarType(expr.getType()));
        return false;
    }
    
    @Override
    public boolean enterAccessNode(final AccessNode accessNode) {
        this.visitExpression(accessNode.getBase());
        return this.pushExpressionType(accessNode);
    }
    
    @Override
    public boolean enterBinaryNode(final BinaryNode binaryNode) {
        final Expression lhs = binaryNode.lhs();
        LvarType lhsType;
        if (!(lhs instanceof IdentNode) || !binaryNode.isTokenType(TokenType.ASSIGN)) {
            lhsType = this.visitExpression(lhs);
        }
        else {
            lhsType = LvarType.UNDEFINED;
        }
        final boolean isLogical = binaryNode.isLogical();
        final Label joinLabel = isLogical ? new Label("") : null;
        if (isLogical) {
            this.jumpToLabel((JoinPredecessor)lhs, joinLabel);
        }
        final Expression rhs = binaryNode.rhs();
        final LvarType rhsType = this.visitExpression(rhs);
        if (isLogical) {
            this.jumpToLabel((JoinPredecessor)rhs, joinLabel);
        }
        this.joinOnLabel(joinLabel);
        final LvarType type = toLvarType(binaryNode.setOperands(lhsType.typeExpression, rhsType.typeExpression).getType());
        if (binaryNode.isAssignment() && lhs instanceof IdentNode) {
            if (binaryNode.isSelfModifying()) {
                this.onSelfAssignment((IdentNode)lhs, type);
            }
            else {
                this.onAssignment((IdentNode)lhs, type);
            }
        }
        this.typeStack.push(type);
        return false;
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        boolean cloned = false;
        for (final Symbol symbol : block.getSymbols()) {
            if (symbol.isBytecodeLocal()) {
                if (this.getLocalVariableTypeOrNull(symbol) == null) {
                    if (!cloned) {
                        this.cloneOrNewLocalVariableTypes();
                        cloned = true;
                    }
                    this.localVariableTypes.put(symbol, LvarType.UNDEFINED);
                }
                this.invalidatedSymbols.remove(symbol);
            }
        }
        return true;
    }
    
    @Override
    public boolean enterBreakNode(final BreakNode breakNode) {
        return this.enterJumpStatement(breakNode);
    }
    
    @Override
    public boolean enterCallNode(final CallNode callNode) {
        this.visitExpression(callNode.getFunction());
        this.visitExpressions(callNode.getArgs());
        final CallNode.EvalArgs evalArgs = callNode.getEvalArgs();
        if (evalArgs != null) {
            this.visitExpressions(evalArgs.getArgs());
        }
        return this.pushExpressionType(callNode);
    }
    
    @Override
    public boolean enterContinueNode(final ContinueNode continueNode) {
        return this.enterJumpStatement(continueNode);
    }
    
    private boolean enterJumpStatement(final JumpStatement jump) {
        if (!this.reachable) {
            return false;
        }
        this.assertTypeStackIsEmpty();
        this.jumpToLabel(jump, jump.getTargetLabel(this.lc), this.getBreakTargetTypes(jump.getPopScopeLimit(this.lc)));
        this.doesNotContinueSequentially();
        return false;
    }
    
    @Override
    protected boolean enterDefault(final Node node) {
        return this.reachable;
    }
    
    private void enterDoWhileLoop(final WhileNode loopNode) {
        this.assertTypeStackIsEmpty();
        final JoinPredecessorExpression test = loopNode.getTest();
        final Block body = loopNode.getBody();
        final Label continueLabel = loopNode.getContinueLabel();
        final Label breakLabel = loopNode.getBreakLabel();
        final Map<Symbol, LvarType> beforeLoopTypes = this.localVariableTypes;
        final Label repeatLabel = new Label("");
        while (true) {
            this.jumpToLabel(loopNode, repeatLabel, beforeLoopTypes);
            final Map<Symbol, LvarType> beforeRepeatTypes = this.localVariableTypes;
            body.accept(this);
            if (this.reachable) {
                this.jumpToLabel(body, continueLabel);
            }
            this.joinOnLabel(continueLabel);
            if (!this.reachable) {
                break;
            }
            this.visitExpressionOnEmptyStack(test);
            this.jumpToLabel(test, breakLabel);
            if (Expression.isAlwaysFalse(test)) {
                break;
            }
            this.jumpToLabel(test, repeatLabel);
            this.joinOnLabel(repeatLabel);
            if (this.localVariableTypes.equals(beforeRepeatTypes)) {
                break;
            }
            this.resetJoinPoint(continueLabel);
            this.resetJoinPoint(breakLabel);
            this.resetJoinPoint(repeatLabel);
        }
        if (Expression.isAlwaysTrue(test)) {
            this.doesNotContinueSequentially();
        }
        this.leaveBreakable(loopNode);
    }
    
    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        if (this.reachable) {
            this.visitExpressionOnEmptyStack(expressionStatement.getExpression());
        }
        return false;
    }
    
    private void assertTypeStackIsEmpty() {
        assert this.typeStack.isEmpty();
    }
    
    @Override
    protected Node leaveDefault(final Node node) {
        assert !(node instanceof Expression);
        assert !(!this.typeStack.isEmpty());
        return node;
    }
    
    private LvarType visitExpressionOnEmptyStack(final Expression expr) {
        this.assertTypeStackIsEmpty();
        return this.visitExpression(expr);
    }
    
    private LvarType visitExpression(final Expression expr) {
        final int stackSize = this.typeStack.size();
        expr.accept(this);
        assert this.typeStack.size() == stackSize + 1;
        return this.typeStack.pop();
    }
    
    private void visitExpressions(final List<Expression> exprs) {
        for (final Expression expr : exprs) {
            if (expr != null) {
                this.visitExpression(expr);
            }
        }
    }
    
    @Override
    public boolean enterForNode(final ForNode forNode) {
        if (!this.reachable) {
            return false;
        }
        final Expression init = forNode.getInit();
        if (forNode.isForIn()) {
            final JoinPredecessorExpression iterable = forNode.getModify();
            this.visitExpression(iterable);
            this.enterTestFirstLoop(forNode, null, init, !this.compiler.useOptimisticTypes() || (!forNode.isForEach() && this.compiler.hasStringPropertyIterator(iterable.getExpression())));
        }
        else {
            if (init != null) {
                this.visitExpressionOnEmptyStack(init);
            }
            this.enterTestFirstLoop(forNode, forNode.getModify(), null, false);
        }
        this.assertTypeStackIsEmpty();
        return false;
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (this.alreadyEnteredTopLevelFunction) {
            this.typeStack.push(LvarType.OBJECT);
            return false;
        }
        int pos = 0;
        if (!functionNode.isVarArg()) {
            for (final IdentNode param : functionNode.getParameters()) {
                final Symbol symbol = param.getSymbol();
                assert symbol.hasSlot();
                final Type callSiteParamType = this.compiler.getParamType(functionNode, pos);
                final LvarType paramType = (callSiteParamType == null) ? LvarType.OBJECT : toLvarType(callSiteParamType);
                this.setType(symbol, paramType);
                this.symbolIsUsed(symbol);
                this.setIdentifierLvarType(param, paramType);
                ++pos;
            }
        }
        this.setCompilerConstantAsObject(functionNode, CompilerConstants.THIS);
        if (functionNode.hasScopeBlock() || functionNode.needsParentScope()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.SCOPE);
        }
        if (functionNode.needsCallee()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.CALLEE);
        }
        if (functionNode.needsArguments()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.ARGUMENTS);
        }
        return this.alreadyEnteredTopLevelFunction = true;
    }
    
    @Override
    public boolean enterGetSplitState(final GetSplitState getSplitState) {
        return this.pushExpressionType(getSplitState);
    }
    
    @Override
    public boolean enterIdentNode(final IdentNode identNode) {
        final Symbol symbol = identNode.getSymbol();
        if (symbol.isBytecodeLocal()) {
            this.symbolIsUsed(symbol);
            final LvarType type = this.getLocalVariableType(symbol);
            this.setIdentifierLvarType(identNode, type);
            this.typeStack.push(type);
        }
        else {
            this.pushExpressionType(identNode);
        }
        return false;
    }
    
    @Override
    public boolean enterIfNode(final IfNode ifNode) {
        this.processIfNode(ifNode);
        return false;
    }
    
    private void processIfNode(final IfNode ifNode) {
        if (!this.reachable) {
            return;
        }
        final Expression test = ifNode.getTest();
        final Block pass = ifNode.getPass();
        final Block fail = ifNode.getFail();
        this.visitExpressionOnEmptyStack(test);
        final boolean isTestAlwaysTrue = Expression.isAlwaysTrue(test);
        Map<Symbol, LvarType> passLvarTypes;
        boolean reachableFromPass;
        if (Expression.isAlwaysFalse(test)) {
            passLvarTypes = null;
            reachableFromPass = false;
        }
        else {
            final Map<Symbol, LvarType> afterTestLvarTypes = this.localVariableTypes;
            pass.accept(this);
            this.assertTypeStackIsEmpty();
            if (isTestAlwaysTrue) {
                return;
            }
            passLvarTypes = this.localVariableTypes;
            reachableFromPass = this.reachable;
            this.localVariableTypes = afterTestLvarTypes;
            this.reachable = true;
        }
        assert !isTestAlwaysTrue;
        if (fail != null) {
            fail.accept(this);
            this.assertTypeStackIsEmpty();
        }
        if (this.reachable) {
            if (reachableFromPass) {
                final Map<Symbol, LvarType> failLvarTypes = this.localVariableTypes;
                this.setConversion(pass, passLvarTypes, this.localVariableTypes = this.getUnionTypes(passLvarTypes, failLvarTypes));
                this.setConversion((JoinPredecessor)((fail != null) ? fail : ifNode), failLvarTypes, this.localVariableTypes);
            }
        }
        else if (reachableFromPass) {
            assert passLvarTypes != null;
            this.localVariableTypes = passLvarTypes;
            this.reachable = true;
        }
    }
    
    @Override
    public boolean enterIndexNode(final IndexNode indexNode) {
        this.visitExpression(indexNode.getBase());
        this.visitExpression(indexNode.getIndex());
        return this.pushExpressionType(indexNode);
    }
    
    @Override
    public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression joinExpr) {
        final Expression expr = joinExpr.getExpression();
        if (expr != null) {
            expr.accept(this);
        }
        else {
            this.typeStack.push(LvarType.UNDEFINED);
        }
        return false;
    }
    
    @Override
    public boolean enterJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        return this.enterJumpStatement(jumpToInlinedFinally);
    }
    
    @Override
    public boolean enterLiteralNode(final LiteralNode<?> literalNode) {
        if (literalNode instanceof LiteralNode.ArrayLiteralNode) {
            final List<Expression> expressions = ((LiteralNode.ArrayLiteralNode)literalNode).getElementExpressions();
            if (expressions != null) {
                this.visitExpressions(expressions);
            }
        }
        this.pushExpressionType(literalNode);
        return false;
    }
    
    @Override
    public boolean enterObjectNode(final ObjectNode objectNode) {
        for (final PropertyNode propertyNode : objectNode.getElements()) {
            final Expression value = propertyNode.getValue();
            if (value != null) {
                this.visitExpression(value);
            }
        }
        return this.pushExpressionType(objectNode);
    }
    
    @Override
    public boolean enterPropertyNode(final PropertyNode propertyNode) {
        throw new AssertionError();
    }
    
    @Override
    public boolean enterReturnNode(final ReturnNode returnNode) {
        if (!this.reachable) {
            return false;
        }
        final Expression returnExpr = returnNode.getExpression();
        Type returnExprType;
        if (returnExpr != null) {
            returnExprType = this.visitExpressionOnEmptyStack(returnExpr).type;
        }
        else {
            this.assertTypeStackIsEmpty();
            returnExprType = Type.UNDEFINED;
        }
        this.returnType = Type.widestReturnType(this.returnType, returnExprType);
        this.doesNotContinueSequentially();
        return false;
    }
    
    @Override
    public boolean enterRuntimeNode(final RuntimeNode runtimeNode) {
        this.visitExpressions(runtimeNode.getArgs());
        return this.pushExpressionType(runtimeNode);
    }
    
    @Override
    public boolean enterSplitReturn(final SplitReturn splitReturn) {
        this.doesNotContinueSequentially();
        return false;
    }
    
    @Override
    public boolean enterSwitchNode(final SwitchNode switchNode) {
        if (!this.reachable) {
            return false;
        }
        this.visitExpressionOnEmptyStack(switchNode.getExpression());
        final List<CaseNode> cases = switchNode.getCases();
        if (cases.isEmpty()) {
            return false;
        }
        final boolean isInteger = switchNode.isUniqueInteger();
        final Label breakLabel = switchNode.getBreakLabel();
        final boolean hasDefault = switchNode.getDefaultCase() != null;
        boolean tagUsed = false;
        for (final CaseNode caseNode : cases) {
            final Expression test = caseNode.getTest();
            if (!isInteger && test != null) {
                this.visitExpressionOnEmptyStack(test);
                if (!tagUsed) {
                    symbolIsUsed(switchNode.getTag(), LvarType.OBJECT);
                    tagUsed = true;
                }
            }
            this.jumpToLabel(caseNode, caseNode.getBody().getEntryLabel());
        }
        if (!hasDefault) {
            this.jumpToLabel(switchNode, breakLabel);
        }
        this.doesNotContinueSequentially();
        Block previousBlock = null;
        for (final CaseNode caseNode2 : cases) {
            final Block body = caseNode2.getBody();
            final Label entryLabel = body.getEntryLabel();
            if (previousBlock != null && this.reachable) {
                this.jumpToLabel(previousBlock, entryLabel);
            }
            this.joinOnLabel(entryLabel);
            assert this.reachable;
            body.accept(this);
            previousBlock = body;
        }
        if (previousBlock != null && this.reachable) {
            this.jumpToLabel(previousBlock, breakLabel);
        }
        this.leaveBreakable(switchNode);
        return false;
    }
    
    @Override
    public boolean enterTernaryNode(final TernaryNode ternaryNode) {
        final Expression test = ternaryNode.getTest();
        final Expression trueExpr = ternaryNode.getTrueExpression();
        final Expression falseExpr = ternaryNode.getFalseExpression();
        this.visitExpression(test);
        final Map<Symbol, LvarType> testExitLvarTypes = this.localVariableTypes;
        LvarType trueType;
        if (!Expression.isAlwaysFalse(test)) {
            trueType = this.visitExpression(trueExpr);
        }
        else {
            trueType = null;
        }
        final Map<Symbol, LvarType> trueExitLvarTypes = this.localVariableTypes;
        this.localVariableTypes = testExitLvarTypes;
        LvarType falseType;
        if (!Expression.isAlwaysTrue(test)) {
            falseType = this.visitExpression(falseExpr);
        }
        else {
            falseType = null;
        }
        final Map<Symbol, LvarType> falseExitLvarTypes = this.localVariableTypes;
        this.localVariableTypes = this.getUnionTypes(trueExitLvarTypes, falseExitLvarTypes);
        this.setConversion((JoinPredecessor)trueExpr, trueExitLvarTypes, this.localVariableTypes);
        this.setConversion((JoinPredecessor)falseExpr, falseExitLvarTypes, this.localVariableTypes);
        this.typeStack.push((trueType != null) ? ((falseType != null) ? widestLvarType(trueType, falseType) : trueType) : assertNotNull(falseType));
        return false;
    }
    
    private static <T> T assertNotNull(final T t) {
        assert t != null;
        return t;
    }
    
    private void enterTestFirstLoop(final LoopNode loopNode, final JoinPredecessorExpression modify, final Expression iteratorValues, final boolean iteratorValuesAreObject) {
        final JoinPredecessorExpression test = loopNode.getTest();
        if (Expression.isAlwaysFalse(test)) {
            this.visitExpressionOnEmptyStack(test);
            return;
        }
        final Label continueLabel = loopNode.getContinueLabel();
        final Label breakLabel = loopNode.getBreakLabel();
        final Label repeatLabel = (modify == null) ? continueLabel : new Label("");
        final Map<Symbol, LvarType> beforeLoopTypes = this.localVariableTypes;
        while (true) {
            this.jumpToLabel(loopNode, repeatLabel, beforeLoopTypes);
            final Map<Symbol, LvarType> beforeRepeatTypes = this.localVariableTypes;
            if (test != null) {
                this.visitExpressionOnEmptyStack(test);
            }
            if (!Expression.isAlwaysTrue(test)) {
                this.jumpToLabel(test, breakLabel);
            }
            if (iteratorValues instanceof IdentNode) {
                final IdentNode ident = (IdentNode)iteratorValues;
                this.onAssignment(ident, iteratorValuesAreObject ? LvarType.OBJECT : toLvarType(this.compiler.getOptimisticType(ident)));
            }
            final Block body = loopNode.getBody();
            body.accept(this);
            if (this.reachable) {
                this.jumpToLabel(body, continueLabel);
            }
            this.joinOnLabel(continueLabel);
            if (!this.reachable) {
                break;
            }
            if (modify != null) {
                this.visitExpressionOnEmptyStack(modify);
                this.jumpToLabel(modify, repeatLabel);
                this.joinOnLabel(repeatLabel);
            }
            if (this.localVariableTypes.equals(beforeRepeatTypes)) {
                break;
            }
            this.resetJoinPoint(continueLabel);
            this.resetJoinPoint(breakLabel);
            this.resetJoinPoint(repeatLabel);
        }
        if (Expression.isAlwaysTrue(test) && iteratorValues == null) {
            this.doesNotContinueSequentially();
        }
        this.leaveBreakable(loopNode);
    }
    
    @Override
    public boolean enterThrowNode(final ThrowNode throwNode) {
        if (!this.reachable) {
            return false;
        }
        this.visitExpressionOnEmptyStack(throwNode.getExpression());
        this.jumpToCatchBlock(throwNode);
        this.doesNotContinueSequentially();
        return false;
    }
    
    @Override
    public boolean enterTryNode(final TryNode tryNode) {
        if (!this.reachable) {
            return false;
        }
        final Label catchLabel = new Label("");
        this.catchLabels.push(catchLabel);
        this.jumpToLabel(tryNode, catchLabel);
        final Block body = tryNode.getBody();
        body.accept(this);
        this.catchLabels.pop();
        final Label endLabel = new Label("");
        boolean canExit = false;
        if (this.reachable) {
            this.jumpToLabel(body, endLabel);
            canExit = true;
        }
        this.doesNotContinueSequentially();
        for (final Block inlinedFinally : tryNode.getInlinedFinallies()) {
            final Block finallyBody = TryNode.getLabelledInlinedFinallyBlock(inlinedFinally);
            this.joinOnLabel(finallyBody.getEntryLabel());
            if (this.reachable) {
                finallyBody.accept(this);
                assert !this.reachable;
                continue;
            }
        }
        this.joinOnLabel(catchLabel);
        for (final CatchNode catchNode : tryNode.getCatches()) {
            final IdentNode exception = catchNode.getException();
            this.onAssignment(exception, LvarType.OBJECT);
            final Expression condition = catchNode.getExceptionCondition();
            if (condition != null) {
                this.visitExpression(condition);
            }
            final Map<Symbol, LvarType> afterConditionTypes = this.localVariableTypes;
            final Block catchBody = catchNode.getBody();
            this.reachable = true;
            catchBody.accept(this);
            if (this.reachable) {
                this.jumpToLabel(catchBody, endLabel);
                canExit = true;
            }
            this.localVariableTypes = afterConditionTypes;
        }
        this.doesNotContinueSequentially();
        if (canExit) {
            this.joinOnLabel(endLabel);
        }
        return false;
    }
    
    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        final Expression expr = unaryNode.getExpression();
        final LvarType unaryType = toLvarType(unaryNode.setExpression(this.visitExpression(expr).typeExpression).getType());
        if (unaryNode.isSelfModifying() && expr instanceof IdentNode) {
            this.onSelfAssignment((IdentNode)expr, unaryType);
        }
        this.typeStack.push(unaryType);
        return false;
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        if (!this.reachable) {
            return false;
        }
        final Expression init = varNode.getInit();
        if (init != null) {
            this.onAssignment(varNode.getName(), this.visitExpression(init));
        }
        return false;
    }
    
    @Override
    public boolean enterWhileNode(final WhileNode whileNode) {
        if (!this.reachable) {
            return false;
        }
        if (whileNode.isDoWhile()) {
            this.enterDoWhileLoop(whileNode);
        }
        else {
            this.enterTestFirstLoop(whileNode, null, null, false);
        }
        return false;
    }
    
    @Override
    public boolean enterWithNode(final WithNode withNode) {
        if (this.reachable) {
            this.visitExpression(withNode.getExpression());
            withNode.getBody().accept(this);
        }
        return false;
    }
    
    private Map<Symbol, LvarType> getBreakTargetTypes(final LexicalContextNode target) {
        Map<Symbol, LvarType> types = this.localVariableTypes;
        final Iterator<LexicalContextNode> it = this.lc.getAllNodes();
        while (it.hasNext()) {
            final LexicalContextNode node = it.next();
            if (node instanceof Block) {
                for (final Symbol symbol : ((Block)node).getSymbols()) {
                    if (this.localVariableTypes.containsKey(symbol)) {
                        if (types == this.localVariableTypes) {
                            types = cloneMap(this.localVariableTypes);
                        }
                        types.remove(symbol);
                    }
                }
            }
            if (node == target) {
                break;
            }
        }
        return types;
    }
    
    private LvarType getLocalVariableType(final Symbol symbol) {
        final LvarType type = this.getLocalVariableTypeOrNull(symbol);
        assert type != null;
        return type;
    }
    
    private LvarType getLocalVariableTypeOrNull(final Symbol symbol) {
        return this.localVariableTypes.get(symbol);
    }
    
    private JumpTarget getOrCreateJumpTarget(final Label label) {
        JumpTarget jumpTarget = this.jumpTargets.get(label);
        if (jumpTarget == null) {
            jumpTarget = this.createJumpTarget(label);
        }
        return jumpTarget;
    }
    
    private void joinOnLabel(final Label label) {
        final JumpTarget jumpTarget = this.jumpTargets.remove(label);
        if (jumpTarget == null) {
            return;
        }
        assert !jumpTarget.origins.isEmpty();
        this.reachable = true;
        this.localVariableTypes = this.getUnionTypes(jumpTarget.types, this.localVariableTypes);
        for (final JumpOrigin jumpOrigin : jumpTarget.origins) {
            this.setConversion(jumpOrigin.node, jumpOrigin.types, this.localVariableTypes);
        }
    }
    
    private void jumpToCatchBlock(final JoinPredecessor jumpOrigin) {
        final Label currentCatchLabel = this.catchLabels.peek();
        if (currentCatchLabel != null) {
            this.jumpToLabel(jumpOrigin, currentCatchLabel);
        }
    }
    
    private void jumpToLabel(final JoinPredecessor jumpOrigin, final Label label) {
        this.jumpToLabel(jumpOrigin, label, this.localVariableTypes);
    }
    
    private void jumpToLabel(final JoinPredecessor jumpOrigin, final Label label, final Map<Symbol, LvarType> types) {
        this.getOrCreateJumpTarget(label).addOrigin(jumpOrigin, types, this);
    }
    
    @Override
    public Node leaveBlock(final Block block) {
        if (this.lc.isFunctionBody()) {
            if (this.reachable) {
                this.createSyntheticReturn(block);
                assert !this.reachable;
            }
            this.calculateReturnType();
        }
        boolean cloned = false;
        for (final Symbol symbol : block.getSymbols()) {
            if (symbol.hasSlot()) {
                if (symbol.isBytecodeLocal()) {
                    if (this.localVariableTypes.containsKey(symbol) && !cloned) {
                        this.localVariableTypes = cloneMap(this.localVariableTypes);
                        cloned = true;
                    }
                    this.invalidateSymbol(symbol);
                }
                final SymbolConversions conversions = this.symbolConversions.get(symbol);
                if (conversions != null) {
                    conversions.calculateTypeLiveness(symbol);
                }
                if (symbol.slotCount() != 0) {
                    continue;
                }
                symbol.setNeedsSlot(false);
            }
        }
        if (this.reachable) {
            final LabelNode labelNode = this.lc.getCurrentBlockLabelNode();
            if (labelNode != null) {
                this.jumpToLabel(labelNode, block.getBreakLabel());
            }
        }
        this.leaveBreakable(block);
        return block;
    }
    
    private void calculateReturnType() {
        if (this.returnType.isUnknown()) {
            this.returnType = Type.OBJECT;
        }
    }
    
    private void createSyntheticReturn(final Block body) {
        final FunctionNode functionNode = this.lc.getCurrentFunction();
        final long token = functionNode.getToken();
        final int finish = functionNode.getFinish();
        final List<Statement> statements = body.getStatements();
        final int lineNumber = statements.isEmpty() ? functionNode.getLineNumber() : statements.get(statements.size() - 1).getLineNumber();
        IdentNode returnExpr;
        if (functionNode.isProgram()) {
            returnExpr = new IdentNode(token, finish, CompilerConstants.RETURN.symbolName()).setSymbol(getCompilerConstantSymbol(functionNode, CompilerConstants.RETURN));
        }
        else {
            returnExpr = null;
        }
        (this.syntheticReturn = new ReturnNode(lineNumber, token, finish, returnExpr)).accept(this);
    }
    
    private void leaveBreakable(final BreakableNode breakable) {
        this.joinOnLabel(breakable.getBreakLabel());
        this.assertTypeStackIsEmpty();
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        FunctionNode newFunction = functionNode;
        final SimpleNodeVisitor applyChangesVisitor = new SimpleNodeVisitor() {
            private boolean inOuterFunction = true;
            private final Deque<JoinPredecessor> joinPredecessors = new ArrayDeque<JoinPredecessor>();
            
            @Override
            protected boolean enterDefault(final Node node) {
                if (!this.inOuterFunction) {
                    return false;
                }
                if (node instanceof JoinPredecessor) {
                    this.joinPredecessors.push((JoinPredecessor)node);
                }
                return this.inOuterFunction;
            }
            
            @Override
            public boolean enterFunctionNode(final FunctionNode fn) {
                if (LocalVariableTypesCalculator.this.compiler.isOnDemandCompilation()) {
                    return false;
                }
                this.inOuterFunction = false;
                return true;
            }
            
            @Override
            public Node leaveBinaryNode(final BinaryNode binaryNode) {
                if (binaryNode.isComparison()) {
                    final Expression lhs = binaryNode.lhs();
                    final Expression rhs = binaryNode.rhs();
                    final TokenType tt = binaryNode.tokenType();
                    switch (tt) {
                        case EQ_STRICT:
                        case NE_STRICT: {
                            final Expression undefinedNode = createIsUndefined(binaryNode, lhs, rhs, (tt == TokenType.EQ_STRICT) ? RuntimeNode.Request.IS_UNDEFINED : RuntimeNode.Request.IS_NOT_UNDEFINED);
                            if (undefinedNode != binaryNode) {
                                return undefinedNode;
                            }
                            if (lhs.getType().isBoolean() != rhs.getType().isBoolean()) {
                                return new RuntimeNode(binaryNode);
                            }
                            break;
                        }
                    }
                    if (lhs.getType().isObject() && rhs.getType().isObject()) {
                        return new RuntimeNode(binaryNode);
                    }
                }
                else if (binaryNode.isOptimisticUndecidedType()) {
                    return binaryNode.decideType();
                }
                return binaryNode;
            }
            
            @Override
            protected Node leaveDefault(final Node node) {
                if (!(node instanceof JoinPredecessor)) {
                    return node;
                }
                final JoinPredecessor original = this.joinPredecessors.pop();
                assert original.getClass() == node.getClass() : original.getClass().getName() + "!=" + node.getClass().getName();
                final JoinPredecessor newNode = this.setLocalVariableConversion(original, (JoinPredecessor)node);
                if (newNode instanceof LexicalContextNode) {
                    this.lc.replace((LexicalContextNode)node, (LexicalContextNode)newNode);
                }
                return (Node)newNode;
            }
            
            @Override
            public Node leaveBlock(final Block block) {
                if (this.inOuterFunction && LocalVariableTypesCalculator.this.syntheticReturn != null && this.lc.isFunctionBody()) {
                    final ArrayList<Statement> stmts = new ArrayList<Statement>(block.getStatements());
                    stmts.add((ReturnNode)LocalVariableTypesCalculator.this.syntheticReturn.accept(this));
                    return block.setStatements(this.lc, stmts);
                }
                return super.leaveBlock(block);
            }
            
            @Override
            public Node leaveFunctionNode(final FunctionNode nestedFunctionNode) {
                this.inOuterFunction = true;
                final FunctionNode newNestedFunction = (FunctionNode)nestedFunctionNode.accept(new LocalVariableTypesCalculator(LocalVariableTypesCalculator.this.compiler));
                this.lc.replace(nestedFunctionNode, newNestedFunction);
                return newNestedFunction;
            }
            
            @Override
            public Node leaveIdentNode(final IdentNode identNode) {
                final IdentNode original = this.joinPredecessors.pop();
                final Symbol symbol = identNode.getSymbol();
                if (symbol != null) {
                    if (symbol.hasSlot()) {
                        assert !(!symbol.isParam());
                        assert original.getName().equals(identNode.getName());
                        final LvarType lvarType = LocalVariableTypesCalculator.this.identifierLvarTypes.remove(original);
                        if (lvarType != null) {
                            return this.setLocalVariableConversion(original, identNode.setType(lvarType.type));
                        }
                        assert LocalVariableTypesCalculator.this.localVariableConversions.get(original) == null;
                    }
                    else {
                        assert LocalVariableTypesCalculator.this.identIsDeadAndHasNoLiveConversions(original);
                    }
                    return identNode;
                }
                assert identNode.isPropertyName();
                return identNode;
            }
            
            @Override
            public Node leaveLiteralNode(final LiteralNode<?> literalNode) {
                return literalNode.initialize(this.lc);
            }
            
            @Override
            public Node leaveRuntimeNode(final RuntimeNode runtimeNode) {
                final RuntimeNode.Request request = runtimeNode.getRequest();
                final boolean isEqStrict = request == RuntimeNode.Request.EQ_STRICT;
                if (isEqStrict || request == RuntimeNode.Request.NE_STRICT) {
                    return createIsUndefined(runtimeNode, runtimeNode.getArgs().get(0), runtimeNode.getArgs().get(1), isEqStrict ? RuntimeNode.Request.IS_UNDEFINED : RuntimeNode.Request.IS_NOT_UNDEFINED);
                }
                return runtimeNode;
            }
            
            private <T extends JoinPredecessor> T setLocalVariableConversion(final JoinPredecessor original, final T jp) {
                return (T)jp.setLocalVariableConversion(this.lc, LocalVariableTypesCalculator.this.localVariableConversions.get(original));
            }
        };
        newFunction = newFunction.setBody(this.lc, (Block)newFunction.getBody().accept(applyChangesVisitor));
        newFunction = newFunction.setReturnType(this.lc, this.returnType);
        newFunction = newFunction.setParameters(this.lc, newFunction.visitParameters(applyChangesVisitor));
        return newFunction;
    }
    
    private static Expression createIsUndefined(final Expression parent, final Expression lhs, final Expression rhs, final RuntimeNode.Request request) {
        if (isUndefinedIdent(lhs) || isUndefinedIdent(rhs)) {
            return new RuntimeNode(parent, request, new Expression[] { lhs, rhs });
        }
        return parent;
    }
    
    private static boolean isUndefinedIdent(final Expression expr) {
        return expr instanceof IdentNode && "undefined".equals(((IdentNode)expr).getName());
    }
    
    private boolean identIsDeadAndHasNoLiveConversions(final IdentNode identNode) {
        final LocalVariableConversion conv = this.localVariableConversions.get(identNode);
        return conv == null || !conv.isLive();
    }
    
    private void onAssignment(final IdentNode identNode, final LvarType type) {
        final Symbol symbol = identNode.getSymbol();
        assert symbol != null : identNode.getName();
        if (!symbol.isBytecodeLocal()) {
            return;
        }
        assert type != null;
        LvarType finalType;
        if (type == LvarType.UNDEFINED && this.getLocalVariableType(symbol) != LvarType.UNDEFINED) {
            finalType = LvarType.OBJECT;
            symbol.setFlag(8192);
        }
        else {
            finalType = type;
        }
        this.setType(symbol, finalType);
        this.setIdentifierLvarType(identNode, finalType);
        this.jumpToCatchBlock(identNode);
    }
    
    private void onSelfAssignment(final IdentNode identNode, final LvarType type) {
        final Symbol symbol = identNode.getSymbol();
        assert symbol != null : identNode.getName();
        if (!symbol.isBytecodeLocal()) {
            return;
        }
        assert type != null && type != LvarType.UNDEFINED && type != LvarType.BOOLEAN;
        this.setType(symbol, type);
        this.jumpToCatchBlock(identNode);
    }
    
    private void resetJoinPoint(final Label label) {
        this.jumpTargets.remove(label);
    }
    
    private void setCompilerConstantAsObject(final FunctionNode functionNode, final CompilerConstants cc) {
        final Symbol symbol = getCompilerConstantSymbol(functionNode, cc);
        this.setType(symbol, LvarType.OBJECT);
        this.symbolIsUsed(symbol);
    }
    
    private static Symbol getCompilerConstantSymbol(final FunctionNode functionNode, final CompilerConstants cc) {
        return functionNode.getBody().getExistingSymbol(cc.symbolName());
    }
    
    private void setConversion(final JoinPredecessor node, final Map<Symbol, LvarType> branchLvarTypes, final Map<Symbol, LvarType> joinLvarTypes) {
        if (node == null) {
            return;
        }
        if (branchLvarTypes.isEmpty() || joinLvarTypes.isEmpty()) {
            this.localVariableConversions.remove(node);
        }
        LocalVariableConversion conversion = null;
        if (node instanceof IdentNode) {
            final Symbol symbol = ((IdentNode)node).getSymbol();
            conversion = this.createConversion(symbol, branchLvarTypes.get(symbol), joinLvarTypes, null);
        }
        else {
            for (final Map.Entry<Symbol, LvarType> entry : branchLvarTypes.entrySet()) {
                final Symbol symbol2 = entry.getKey();
                final LvarType branchLvarType = entry.getValue();
                conversion = this.createConversion(symbol2, branchLvarType, joinLvarTypes, conversion);
            }
        }
        if (conversion != null) {
            this.localVariableConversions.put(node, conversion);
        }
        else {
            this.localVariableConversions.remove(node);
        }
    }
    
    private void setIdentifierLvarType(final IdentNode identNode, final LvarType type) {
        assert type != null;
        this.identifierLvarTypes.put(identNode, type);
    }
    
    private void setType(final Symbol symbol, final LvarType type) {
        if (this.getLocalVariableTypeOrNull(symbol) == type) {
            return;
        }
        assert symbol.hasSlot();
        assert !symbol.isGlobal();
        this.cloneOrNewLocalVariableTypes();
        this.localVariableTypes.put(symbol, type);
    }
    
    private void cloneOrNewLocalVariableTypes() {
        this.localVariableTypes = (this.localVariableTypes.isEmpty() ? new HashMap<Symbol, LvarType>() : cloneMap(this.localVariableTypes));
    }
    
    private void invalidateSymbol(final Symbol symbol) {
        this.localVariableTypes.remove(symbol);
        this.invalidatedSymbols.add(symbol);
    }
    
    private void symbolIsUsed(final Symbol symbol) {
        symbolIsUsed(symbol, this.getLocalVariableType(symbol));
    }
    
    static {
        TO_LVAR_TYPE = new IdentityHashMap<Type, LvarType>();
        for (final LvarType lvarType : LvarType.values()) {
            LocalVariableTypesCalculator.TO_LVAR_TYPE.put(lvarType.type, lvarType);
        }
    }
    
    private static class JumpOrigin
    {
        final JoinPredecessor node;
        final Map<Symbol, LvarType> types;
        
        JumpOrigin(final JoinPredecessor node, final Map<Symbol, LvarType> types) {
            this.node = node;
            this.types = types;
        }
    }
    
    private static class JumpTarget
    {
        private final List<JumpOrigin> origins;
        private Map<Symbol, LvarType> types;
        
        private JumpTarget() {
            this.origins = new LinkedList<JumpOrigin>();
            this.types = Collections.emptyMap();
        }
        
        void addOrigin(final JoinPredecessor originNode, final Map<Symbol, LvarType> originTypes, final LocalVariableTypesCalculator calc) {
            this.origins.add(new JumpOrigin(originNode, originTypes));
            this.types = calc.getUnionTypes(this.types, originTypes);
        }
    }
    
    private enum LvarType
    {
        UNDEFINED(Type.UNDEFINED), 
        BOOLEAN(Type.BOOLEAN), 
        INT((Type)Type.INT), 
        DOUBLE((Type)Type.NUMBER), 
        OBJECT(Type.OBJECT);
        
        private final Type type;
        private final TypeHolderExpression typeExpression;
        
        private LvarType(final Type type) {
            this.type = type;
            this.typeExpression = new TypeHolderExpression(type);
        }
    }
    
    private static class TypeHolderExpression extends Expression
    {
        private static final long serialVersionUID = 1L;
        private final Type type;
        
        TypeHolderExpression(final Type type) {
            super(0L, 0, 0);
            this.type = type;
        }
        
        @Override
        public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
            throw new AssertionError();
        }
        
        @Override
        public Type getType() {
            return this.type;
        }
        
        @Override
        public void toString(final StringBuilder sb, final boolean printType) {
            throw new AssertionError();
        }
    }
    
    private static class SymbolConversions
    {
        private static final byte I2D = 1;
        private static final byte I2O = 2;
        private static final byte D2O = 4;
        private byte conversions;
        
        void recordConversion(final LvarType from, final LvarType to) {
            switch (from) {
                case UNDEFINED: {}
                case INT:
                case BOOLEAN: {
                    switch (to) {
                        case DOUBLE: {
                            this.recordConversion((byte)1);
                            return;
                        }
                        case OBJECT: {
                            this.recordConversion((byte)2);
                            return;
                        }
                        default: {
                            illegalConversion(from, to);
                            return;
                        }
                    }
                    break;
                }
                case DOUBLE: {
                    if (to == LvarType.OBJECT) {
                        this.recordConversion((byte)4);
                    }
                }
                default: {
                    illegalConversion(from, to);
                }
            }
        }
        
        private static void illegalConversion(final LvarType from, final LvarType to) {
            throw new AssertionError((Object)("Invalid conversion from " + from + " to " + to));
        }
        
        void recordConversion(final byte convFlag) {
            this.conversions |= convFlag;
        }
        
        boolean hasConversion(final byte convFlag) {
            return (this.conversions & convFlag) != 0x0;
        }
        
        void calculateTypeLiveness(final Symbol symbol) {
            if (symbol.hasSlotFor(Type.OBJECT)) {
                if (this.hasConversion((byte)4)) {
                    symbol.setHasSlotFor(Type.NUMBER);
                }
                if (this.hasConversion((byte)2)) {
                    symbol.setHasSlotFor(Type.INT);
                }
            }
            if (symbol.hasSlotFor(Type.NUMBER) && this.hasConversion((byte)1)) {
                symbol.setHasSlotFor(Type.INT);
            }
        }
    }
}
