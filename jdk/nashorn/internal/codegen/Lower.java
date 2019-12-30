// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.Symbol;
import java.util.Collections;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.LexicalContextNode;
import java.util.Arrays;
import java.util.Collection;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.runtime.Context;
import java.util.ListIterator;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.Block;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.JumpStatement;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.Statement;
import java.util.List;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor;

@Logger(name = "lower")
final class Lower extends NodeOperatorVisitor<BlockLexicalContext> implements Loggable
{
    private final DebugLogger log;
    private static Pattern SAFE_PROPERTY_NAME;
    
    Lower(final Compiler compiler) {
        super(new BlockLexicalContext() {
            public List<Statement> popStatements() {
                final List<Statement> newStatements = new ArrayList<Statement>();
                boolean terminated = false;
                final List<Statement> statements = super.popStatements();
                for (final Statement statement : statements) {
                    if (!terminated) {
                        newStatements.add(statement);
                        if (!statement.isTerminal() && !(statement instanceof JumpStatement)) {
                            continue;
                        }
                        terminated = true;
                    }
                    else {
                        FoldConstants.extractVarNodesFromDeadCode(statement, newStatements);
                    }
                }
                return newStatements;
            }
            
            @Override
            protected Block afterSetStatements(final Block block) {
                final List<Statement> stmts = block.getStatements();
                final ListIterator<Statement> li = stmts.listIterator(stmts.size());
                while (li.hasPrevious()) {
                    final Statement stmt = li.previous();
                    if (!(stmt instanceof VarNode) || ((VarNode)stmt).getInit() != null) {
                        return block.setIsTerminal(this, stmt.isTerminal());
                    }
                }
                return block.setIsTerminal(this, false);
            }
        });
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
    
    @Override
    public boolean enterBreakNode(final BreakNode breakNode) {
        this.addStatement(breakNode);
        return false;
    }
    
    @Override
    public Node leaveCallNode(final CallNode callNode) {
        return this.checkEval(callNode.setFunction(markerFunction(callNode.getFunction())));
    }
    
    @Override
    public Node leaveCatchNode(final CatchNode catchNode) {
        return this.addStatement(catchNode);
    }
    
    @Override
    public boolean enterContinueNode(final ContinueNode continueNode) {
        this.addStatement(continueNode);
        return false;
    }
    
    @Override
    public boolean enterJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        this.addStatement(jumpToInlinedFinally);
        return false;
    }
    
    @Override
    public boolean enterEmptyNode(final EmptyNode emptyNode) {
        return false;
    }
    
    @Override
    public Node leaveIndexNode(final IndexNode indexNode) {
        final String name = getConstantPropertyName(indexNode.getIndex());
        if (name == null) {
            return super.leaveIndexNode(indexNode);
        }
        assert indexNode.isIndex();
        return new AccessNode(indexNode.getToken(), indexNode.getFinish(), indexNode.getBase(), name);
    }
    
    private static String getConstantPropertyName(final Expression expression) {
        if (expression instanceof LiteralNode.PrimitiveLiteralNode) {
            final Object value = ((LiteralNode)expression).getValue();
            if (value instanceof String && Lower.SAFE_PROPERTY_NAME.matcher((CharSequence)value).matches()) {
                return (String)value;
            }
        }
        return null;
    }
    
    @Override
    public Node leaveExpressionStatement(final ExpressionStatement expressionStatement) {
        final Expression expr = expressionStatement.getExpression();
        ExpressionStatement node = expressionStatement;
        final FunctionNode currentFunction = ((BlockLexicalContext)this.lc).getCurrentFunction();
        if (currentFunction.isProgram() && !isInternalExpression(expr) && !isEvalResultAssignment(expr)) {
            node = expressionStatement.setExpression(new BinaryNode(Token.recast(expressionStatement.getToken(), TokenType.ASSIGN), this.compilerConstant(CompilerConstants.RETURN), expr));
        }
        return this.addStatement(node);
    }
    
    @Override
    public Node leaveBlockStatement(final BlockStatement blockStatement) {
        return this.addStatement(blockStatement);
    }
    
    @Override
    public Node leaveForNode(final ForNode forNode) {
        ForNode newForNode = forNode;
        final Expression test = forNode.getTest();
        if (!forNode.isForIn() && Expression.isAlwaysTrue(test)) {
            newForNode = forNode.setTest(this.lc, null);
        }
        newForNode = this.checkEscape(newForNode);
        if (newForNode.isForIn()) {
            this.addStatementEnclosedInBlock(newForNode);
        }
        else {
            this.addStatement(newForNode);
        }
        return newForNode;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        this.log.info("END FunctionNode: ", functionNode.getName());
        return functionNode;
    }
    
    @Override
    public Node leaveIfNode(final IfNode ifNode) {
        return this.addStatement(ifNode);
    }
    
    @Override
    public Node leaveIN(final BinaryNode binaryNode) {
        return new RuntimeNode(binaryNode);
    }
    
    @Override
    public Node leaveINSTANCEOF(final BinaryNode binaryNode) {
        return new RuntimeNode(binaryNode);
    }
    
    @Override
    public Node leaveLabelNode(final LabelNode labelNode) {
        return this.addStatement(labelNode);
    }
    
    @Override
    public Node leaveReturnNode(final ReturnNode returnNode) {
        this.addStatement(returnNode);
        return returnNode;
    }
    
    @Override
    public Node leaveCaseNode(final CaseNode caseNode) {
        final Node test = caseNode.getTest();
        if (test instanceof LiteralNode) {
            final LiteralNode<?> lit = (LiteralNode<?>)test;
            if (lit.isNumeric() && !(lit.getValue() instanceof Integer) && JSType.isRepresentableAsInt(lit.getNumber())) {
                return caseNode.setTest((Expression)LiteralNode.newInstance(lit, lit.getInt32()).accept(this));
            }
        }
        return caseNode;
    }
    
    @Override
    public Node leaveSwitchNode(final SwitchNode switchNode) {
        if (!switchNode.isUniqueInteger()) {
            this.addStatementEnclosedInBlock(switchNode);
        }
        else {
            this.addStatement(switchNode);
        }
        return switchNode;
    }
    
    @Override
    public Node leaveThrowNode(final ThrowNode throwNode) {
        return this.addStatement(throwNode);
    }
    
    private static <T extends Node> T ensureUniqueNamesIn(final T node) {
        return (T)node.accept(new SimpleNodeVisitor() {
            @Override
            public Node leaveFunctionNode(final FunctionNode functionNode) {
                final String name = functionNode.getName();
                return functionNode.setName(this.lc, this.lc.getCurrentFunction().uniqueName(name));
            }
            
            public Node leaveDefault(final Node labelledNode) {
                return labelledNode.ensureUniqueLabels(this.lc);
            }
        });
    }
    
    private static Block createFinallyBlock(final Block finallyBody) {
        final List<Statement> newStatements = new ArrayList<Statement>();
        for (final Statement statement : finallyBody.getStatements()) {
            newStatements.add(statement);
            if (statement.hasTerminalFlags()) {
                break;
            }
        }
        return finallyBody.setStatements(null, newStatements);
    }
    
    private Block catchAllBlock(final TryNode tryNode) {
        final int lineNumber = tryNode.getLineNumber();
        final long token = tryNode.getToken();
        final int finish = tryNode.getFinish();
        final IdentNode exception = new IdentNode(token, finish, ((BlockLexicalContext)this.lc).getCurrentFunction().uniqueName(CompilerConstants.EXCEPTION_PREFIX.symbolName()));
        final Block catchBody = new Block(token, finish, new Statement[] { new ThrowNode(lineNumber, token, finish, new IdentNode(exception), true) });
        assert catchBody.isTerminal();
        final CatchNode catchAllNode = new CatchNode(lineNumber, token, finish, new IdentNode(exception), null, catchBody, true);
        final Block catchAllBlock = new Block(token, finish, new Statement[] { catchAllNode });
        return (Block)catchAllBlock.accept(this);
    }
    
    private IdentNode compilerConstant(final CompilerConstants cc) {
        final FunctionNode functionNode = ((BlockLexicalContext)this.lc).getCurrentFunction();
        return new IdentNode(functionNode.getToken(), functionNode.getFinish(), cc.symbolName());
    }
    
    private static boolean isTerminalFinally(final Block finallyBlock) {
        return finallyBlock.getLastStatement().hasTerminalFlags();
    }
    
    private TryNode spliceFinally(final TryNode tryNode, final ThrowNode rethrow, final Block finallyBody) {
        assert tryNode.getFinallyBody() == null;
        final Block finallyBlock = createFinallyBlock(finallyBody);
        final ArrayList<Block> inlinedFinallies = new ArrayList<Block>();
        final FunctionNode fn = ((BlockLexicalContext)this.lc).getCurrentFunction();
        final TryNode newTryNode = (TryNode)tryNode.accept(new SimpleNodeVisitor() {
            @Override
            public boolean enterFunctionNode(final FunctionNode functionNode) {
                return false;
            }
            
            @Override
            public Node leaveThrowNode(final ThrowNode throwNode) {
                if (rethrow == throwNode) {
                    return new BlockStatement(prependFinally(finallyBlock, throwNode));
                }
                return throwNode;
            }
            
            @Override
            public Node leaveBreakNode(final BreakNode breakNode) {
                return this.leaveJumpStatement(breakNode);
            }
            
            @Override
            public Node leaveContinueNode(final ContinueNode continueNode) {
                return this.leaveJumpStatement(continueNode);
            }
            
            private Node leaveJumpStatement(final JumpStatement jump) {
                if (jump.getTarget(this.lc) == null) {
                    return createJumpToInlinedFinally(fn, inlinedFinallies, prependFinally(finallyBlock, jump));
                }
                return jump;
            }
            
            @Override
            public Node leaveReturnNode(final ReturnNode returnNode) {
                final Expression expr = returnNode.getExpression();
                if (isTerminalFinally(finallyBlock)) {
                    if (expr == null) {
                        return createJumpToInlinedFinally(fn, inlinedFinallies, (Block)ensureUniqueNamesIn(finallyBlock));
                    }
                    final List<Statement> newStatements = new ArrayList<Statement>(2);
                    final int retLineNumber = returnNode.getLineNumber();
                    final long retToken = returnNode.getToken();
                    newStatements.add(new ExpressionStatement(retLineNumber, retToken, returnNode.getFinish(), expr));
                    newStatements.add(createJumpToInlinedFinally(fn, inlinedFinallies, (Block)ensureUniqueNamesIn(finallyBlock)));
                    return new BlockStatement(retLineNumber, new Block(retToken, finallyBlock.getFinish(), newStatements));
                }
                else {
                    if (expr == null || expr instanceof LiteralNode.PrimitiveLiteralNode || (expr instanceof IdentNode && CompilerConstants.RETURN.symbolName().equals(((IdentNode)expr).getName()))) {
                        return createJumpToInlinedFinally(fn, inlinedFinallies, prependFinally(finallyBlock, returnNode));
                    }
                    final List<Statement> newStatements = new ArrayList<Statement>();
                    final int retLineNumber = returnNode.getLineNumber();
                    final long retToken = returnNode.getToken();
                    final int retFinish = returnNode.getFinish();
                    final Expression resultNode = new IdentNode(expr.getToken(), expr.getFinish(), CompilerConstants.RETURN.symbolName());
                    newStatements.add(new ExpressionStatement(retLineNumber, retToken, retFinish, new BinaryNode(Token.recast(returnNode.getToken(), TokenType.ASSIGN), resultNode, expr)));
                    newStatements.add(createJumpToInlinedFinally(fn, inlinedFinallies, prependFinally(finallyBlock, returnNode.setExpression(resultNode))));
                    return new BlockStatement(retLineNumber, new Block(retToken, retFinish, newStatements));
                }
            }
        });
        this.addStatement(inlinedFinallies.isEmpty() ? newTryNode : newTryNode.setInlinedFinallies(this.lc, inlinedFinallies));
        this.addStatement(new BlockStatement(finallyBlock));
        return newTryNode;
    }
    
    private static JumpToInlinedFinally createJumpToInlinedFinally(final FunctionNode fn, final List<Block> inlinedFinallies, final Block finallyBlock) {
        final String labelName = fn.uniqueName(":finally");
        final long token = finallyBlock.getToken();
        final int finish = finallyBlock.getFinish();
        inlinedFinallies.add(new Block(token, finish, new Statement[] { new LabelNode(finallyBlock.getFirstStatementLineNumber(), token, finish, labelName, finallyBlock) }));
        return new JumpToInlinedFinally(labelName);
    }
    
    private static Block prependFinally(final Block finallyBlock, final Statement statement) {
        final Block inlinedFinally = ensureUniqueNamesIn(finallyBlock);
        if (isTerminalFinally(finallyBlock)) {
            return inlinedFinally;
        }
        final List<Statement> stmts = inlinedFinally.getStatements();
        final List<Statement> newStmts = new ArrayList<Statement>(stmts.size() + 1);
        newStmts.addAll(stmts);
        newStmts.add(statement);
        return new Block(inlinedFinally.getToken(), statement.getFinish(), newStmts);
    }
    
    @Override
    public Node leaveTryNode(final TryNode tryNode) {
        final Block finallyBody = tryNode.getFinallyBody();
        TryNode newTryNode = tryNode.setFinallyBody(this.lc, null);
        if (finallyBody == null || finallyBody.getStatementCount() == 0) {
            final List<CatchNode> catches = newTryNode.getCatches();
            if (catches == null || catches.isEmpty()) {
                return this.addStatement(new BlockStatement(tryNode.getBody()));
            }
            return this.addStatement(this.ensureUnconditionalCatch(newTryNode));
        }
        else {
            final Block catchAll = this.catchAllBlock(tryNode);
            final List<ThrowNode> rethrows = new ArrayList<ThrowNode>(1);
            catchAll.accept(new SimpleNodeVisitor() {
                @Override
                public boolean enterThrowNode(final ThrowNode throwNode) {
                    rethrows.add(throwNode);
                    return true;
                }
            });
            assert rethrows.size() == 1;
            if (!tryNode.getCatchBlocks().isEmpty()) {
                final Block outerBody = new Block(newTryNode.getToken(), newTryNode.getFinish(), new Statement[] { this.ensureUnconditionalCatch(newTryNode) });
                newTryNode = newTryNode.setBody(this.lc, outerBody).setCatchBlocks(this.lc, null);
            }
            newTryNode = newTryNode.setCatchBlocks(this.lc, Arrays.asList(catchAll));
            return (TryNode)((BlockLexicalContext)this.lc).replace(tryNode, this.spliceFinally(newTryNode, rethrows.get(0), finallyBody));
        }
    }
    
    private TryNode ensureUnconditionalCatch(final TryNode tryNode) {
        final List<CatchNode> catches = tryNode.getCatches();
        if (catches == null || catches.isEmpty() || catches.get(catches.size() - 1).getExceptionCondition() == null) {
            return tryNode;
        }
        final List<Block> newCatchBlocks = new ArrayList<Block>(tryNode.getCatchBlocks());
        newCatchBlocks.add(this.catchAllBlock(tryNode));
        return tryNode.setCatchBlocks(this.lc, newCatchBlocks);
    }
    
    @Override
    public Node leaveVarNode(final VarNode varNode) {
        this.addStatement(varNode);
        if (varNode.getFlag(4) && ((BlockLexicalContext)this.lc).getCurrentFunction().isProgram()) {
            new ExpressionStatement(varNode.getLineNumber(), varNode.getToken(), varNode.getFinish(), new IdentNode(varNode.getName())).accept(this);
        }
        return varNode;
    }
    
    @Override
    public Node leaveWhileNode(final WhileNode whileNode) {
        final Expression test = whileNode.getTest();
        final Block body = whileNode.getBody();
        if (Expression.isAlwaysTrue(test)) {
            final ForNode forNode = (ForNode)new ForNode(whileNode.getLineNumber(), whileNode.getToken(), whileNode.getFinish(), body, 0).accept(this);
            ((BlockLexicalContext)this.lc).replace(whileNode, forNode);
            return forNode;
        }
        return this.addStatement(this.checkEscape(whileNode));
    }
    
    @Override
    public Node leaveWithNode(final WithNode withNode) {
        return this.addStatement(withNode);
    }
    
    private static Expression markerFunction(final Expression function) {
        if (function instanceof IdentNode) {
            return ((IdentNode)function).setIsFunction();
        }
        if (function instanceof BaseNode) {
            return ((BaseNode)function).setIsFunction();
        }
        return function;
    }
    
    private String evalLocation(final IdentNode node) {
        final Source source = ((BlockLexicalContext)this.lc).getCurrentFunction().getSource();
        final int pos = node.position();
        return source.getName() + '#' + source.getLine(pos) + ':' + source.getColumn(pos) + "<eval>";
    }
    
    private CallNode checkEval(final CallNode callNode) {
        if (callNode.getFunction() instanceof IdentNode) {
            final List<Expression> args = callNode.getArgs();
            final IdentNode callee = (IdentNode)callNode.getFunction();
            if (args.size() >= 1 && CompilerConstants.EVAL.symbolName().equals(callee.getName())) {
                final List<Expression> evalArgs = new ArrayList<Expression>(args.size());
                for (final Expression arg : args) {
                    evalArgs.add((Expression)ensureUniqueNamesIn(arg).accept(this));
                }
                return callNode.setEvalArgs(new CallNode.EvalArgs(evalArgs, this.evalLocation(callee)));
            }
        }
        return callNode;
    }
    
    private static boolean controlFlowEscapes(final LexicalContext lex, final Block loopBody) {
        final List<Node> escapes = new ArrayList<Node>();
        loopBody.accept(new SimpleNodeVisitor() {
            @Override
            public Node leaveBreakNode(final BreakNode node) {
                escapes.add(node);
                return node;
            }
            
            @Override
            public Node leaveContinueNode(final ContinueNode node) {
                if (lex.contains(node.getTarget(lex))) {
                    escapes.add(node);
                }
                return node;
            }
        });
        return !escapes.isEmpty();
    }
    
    private <T extends LoopNode> T checkEscape(final T loopNode) {
        final boolean escapes = controlFlowEscapes(this.lc, loopNode.getBody());
        if (escapes) {
            return (T)loopNode.setBody(this.lc, loopNode.getBody().setIsTerminal(this.lc, false)).setControlFlowEscapes(this.lc, escapes);
        }
        return loopNode;
    }
    
    private Node addStatement(final Statement statement) {
        ((BlockLexicalContext)this.lc).appendStatement(statement);
        return statement;
    }
    
    private void addStatementEnclosedInBlock(final Statement stmt) {
        BlockStatement b = BlockStatement.createReplacement(stmt, Collections.singletonList(stmt));
        if (stmt.isTerminal()) {
            b = b.setBlock(b.getBlock().setIsTerminal(null, true));
        }
        this.addStatement(b);
    }
    
    private static boolean isInternalExpression(final Expression expression) {
        if (!(expression instanceof IdentNode)) {
            return false;
        }
        final Symbol symbol = ((IdentNode)expression).getSymbol();
        return symbol != null && symbol.isInternal();
    }
    
    private static boolean isEvalResultAssignment(final Node expression) {
        final Node e = expression;
        if (e instanceof BinaryNode) {
            final Node lhs = ((BinaryNode)e).lhs();
            if (lhs instanceof IdentNode) {
                return ((IdentNode)lhs).getName().equals(CompilerConstants.RETURN.symbolName());
            }
        }
        return false;
    }
    
    static {
        Lower.SAFE_PROPERTY_NAME = Pattern.compile("[a-zA-Z_$][\\w$]*");
    }
}
