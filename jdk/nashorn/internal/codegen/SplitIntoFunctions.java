// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Objects;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.CaseNode;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.BinaryNode;
import java.util.List;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.IdentNode;
import java.util.Arrays;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.LexicalContext;
import java.util.Collections;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.ArrayDeque;
import java.util.Iterator;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Block;
import java.util.Deque;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

final class SplitIntoFunctions extends NodeVisitor<BlockLexicalContext>
{
    private static final int FALLTHROUGH_STATE = -1;
    private static final int RETURN_STATE = 0;
    private static final int BREAK_STATE = 1;
    private static final int FIRST_JUMP_STATE = 2;
    private static final String THIS_NAME;
    private static final String RETURN_NAME;
    private static final String RETURN_PARAM_NAME;
    private final Deque<FunctionState> functionStates;
    private final Deque<SplitState> splitStates;
    private final Namespace namespace;
    private boolean artificialBlock;
    private int nextFunctionId;
    
    public SplitIntoFunctions(final Compiler compiler) {
        super(new BlockLexicalContext() {
            @Override
            protected Block afterSetStatements(final Block block) {
                for (final Statement stmt : block.getStatements()) {
                    assert !(stmt instanceof SplitNode);
                }
                return block;
            }
        });
        this.functionStates = new ArrayDeque<FunctionState>();
        this.splitStates = new ArrayDeque<SplitState>();
        this.artificialBlock = false;
        this.nextFunctionId = -2;
        this.namespace = new Namespace(compiler.getScriptEnvironment().getNamespace());
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        this.functionStates.push(new FunctionState(functionNode));
        return true;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        this.functionStates.pop();
        return functionNode;
    }
    
    @Override
    protected Node leaveDefault(final Node node) {
        if (node instanceof Statement) {
            this.appendStatement((Statement)node);
        }
        return node;
    }
    
    @Override
    public boolean enterSplitNode(final SplitNode splitNode) {
        final FunctionState currentFunctionState = this.getCurrentFunctionState();
        ++currentFunctionState.splitDepth;
        this.splitStates.push(new SplitState(splitNode));
        return true;
    }
    
    @Override
    public Node leaveSplitNode(final SplitNode splitNode) {
        final FunctionState fnState = this.getCurrentFunctionState();
        final String name = splitNode.getName();
        final Block body = splitNode.getBody();
        final int firstLineNumber = body.getFirstStatementLineNumber();
        final long token = body.getToken();
        final int finish = body.getFinish();
        final FunctionNode originalFn = fnState.fn;
        assert originalFn == ((BlockLexicalContext)this.lc).getCurrentFunction();
        final boolean isProgram = originalFn.isProgram();
        final long newFnToken = Token.toDesc(TokenType.FUNCTION, this.nextFunctionId--, 0);
        final FunctionNode fn = new FunctionNode(originalFn.getSource(), body.getFirstStatementLineNumber(), newFnToken, finish, 0L, this.namespace, createIdent(name), originalFn.getName() + "$" + name, isProgram ? Collections.singletonList(createReturnParamIdent()) : Collections.emptyList(), FunctionNode.Kind.NORMAL, 529).setBody(this.lc, body).setCompileUnit(this.lc, splitNode.getCompileUnit());
        final IdentNode thisIdent = createIdent(SplitIntoFunctions.THIS_NAME);
        final CallNode callNode = new CallNode(firstLineNumber, token, finish, new AccessNode(0L, 0, fn, "call"), (List<Expression>)(isProgram ? Arrays.asList(thisIdent, createReturnIdent()) : Collections.singletonList(thisIdent)), false);
        final SplitState splitState = this.splitStates.pop();
        final FunctionState functionState = fnState;
        --functionState.splitDepth;
        final boolean hasReturn = splitState.hasReturn;
        if (hasReturn && fnState.splitDepth > 0) {
            final SplitState parentSplit = this.splitStates.peek();
            if (parentSplit != null) {
                parentSplit.hasReturn = true;
            }
        }
        Expression callWithReturn;
        if (hasReturn || isProgram) {
            callWithReturn = new BinaryNode(Token.recast(token, TokenType.ASSIGN), createReturnIdent(), callNode);
        }
        else {
            callWithReturn = callNode;
        }
        this.appendStatement(new ExpressionStatement(firstLineNumber, token, finish, callWithReturn));
        final List<JumpStatement> jumpStatements = splitState.jumpStatements;
        final int jumpCount = jumpStatements.size();
        Statement splitStateHandler;
        if (jumpCount > 0) {
            final List<CaseNode> cases = new ArrayList<CaseNode>(jumpCount + (hasReturn ? 1 : 0));
            if (hasReturn) {
                addCase(cases, 0, createReturnFromSplit());
            }
            int i = 2;
            for (final JumpStatement jump : jumpStatements) {
                addCase(cases, i++, this.enblockAndVisit(jump));
            }
            splitStateHandler = new SwitchNode(-1, token, finish, GetSplitState.INSTANCE, cases, null);
        }
        else {
            splitStateHandler = null;
        }
        if (splitState.hasBreak) {
            splitStateHandler = makeIfStateEquals(firstLineNumber, token, finish, 1, this.enblockAndVisit(new BreakNode(-1, token, finish, null)), splitStateHandler);
        }
        if (hasReturn && jumpCount == 0) {
            splitStateHandler = makeIfStateEquals(-1, token, finish, 0, createReturnFromSplit(), splitStateHandler);
        }
        if (splitStateHandler != null) {
            this.appendStatement(splitStateHandler);
        }
        return splitNode;
    }
    
    private static void addCase(final List<CaseNode> cases, final int i, final Block body) {
        cases.add(new CaseNode(0L, 0, intLiteral(i), body));
    }
    
    private static LiteralNode<Number> intLiteral(final int i) {
        return LiteralNode.newInstance(0L, 0, i);
    }
    
    private static Block createReturnFromSplit() {
        return new Block(0L, 0, new Statement[] { createReturnReturn() });
    }
    
    private static ReturnNode createReturnReturn() {
        return new ReturnNode(-1, 0L, 0, createReturnIdent());
    }
    
    private static IdentNode createReturnIdent() {
        return createIdent(SplitIntoFunctions.RETURN_NAME);
    }
    
    private static IdentNode createReturnParamIdent() {
        return createIdent(SplitIntoFunctions.RETURN_PARAM_NAME);
    }
    
    private static IdentNode createIdent(final String name) {
        return new IdentNode(0L, 0, name);
    }
    
    private Block enblockAndVisit(final JumpStatement jump) {
        this.artificialBlock = true;
        final Block block = (Block)new Block(0L, 0, new Statement[] { jump }).accept(this);
        this.artificialBlock = false;
        return block;
    }
    
    private static IfNode makeIfStateEquals(final int lineNumber, final long token, final int finish, final int value, final Block pass, final Statement fail) {
        return new IfNode(lineNumber, token, finish, new BinaryNode(Token.recast(token, TokenType.EQ_STRICT), GetSplitState.INSTANCE, intLiteral(value)), pass, (fail == null) ? null : new Block(0L, 0, new Statement[] { fail }));
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        if (!this.inSplitNode()) {
            return super.enterVarNode(varNode);
        }
        assert !varNode.isBlockScoped();
        final Expression init = varNode.getInit();
        this.getCurrentFunctionState().varStatements.add(varNode.setInit(null));
        if (init != null) {
            final long token = Token.recast(varNode.getToken(), TokenType.ASSIGN);
            new ExpressionStatement(varNode.getLineNumber(), token, varNode.getFinish(), new BinaryNode(token, varNode.getName(), varNode.getInit())).accept(this);
        }
        return false;
    }
    
    @Override
    public Node leaveBlock(final Block block) {
        if (!this.artificialBlock) {
            if (((BlockLexicalContext)this.lc).isFunctionBody()) {
                ((BlockLexicalContext)this.lc).prependStatements(this.getCurrentFunctionState().varStatements);
            }
            else if (((BlockLexicalContext)this.lc).isSplitBody()) {
                this.appendSplitReturn(-1, -1);
                if (this.getCurrentFunctionState().fn.isProgram()) {
                    ((BlockLexicalContext)this.lc).prependStatement(new ExpressionStatement(-1, 0L, 0, new BinaryNode(Token.toDesc(TokenType.ASSIGN, 0, 0), createReturnIdent(), createReturnParamIdent())));
                }
            }
        }
        return block;
    }
    
    @Override
    public Node leaveBreakNode(final BreakNode breakNode) {
        return this.leaveJumpNode(breakNode);
    }
    
    @Override
    public Node leaveContinueNode(final ContinueNode continueNode) {
        return this.leaveJumpNode(continueNode);
    }
    
    @Override
    public Node leaveJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally) {
        return this.leaveJumpNode(jumpToInlinedFinally);
    }
    
    private JumpStatement leaveJumpNode(final JumpStatement jump) {
        if (this.inSplitNode()) {
            final SplitState splitState = this.getCurrentSplitState();
            final SplitNode splitNode = splitState.splitNode;
            if (((BlockLexicalContext)this.lc).isExternalTarget(splitNode, jump.getTarget(this.lc))) {
                this.appendSplitReturn(splitState.getSplitStateIndex(jump), jump.getLineNumber());
                return jump;
            }
        }
        this.appendStatement(jump);
        return jump;
    }
    
    private void appendSplitReturn(final int splitState, final int lineNumber) {
        this.appendStatement(new SetSplitState(splitState, lineNumber));
        if (this.getCurrentFunctionState().fn.isProgram()) {
            this.appendStatement(createReturnReturn());
        }
        else {
            this.appendStatement(SplitReturn.INSTANCE);
        }
    }
    
    @Override
    public Node leaveReturnNode(final ReturnNode returnNode) {
        if (this.inSplitNode()) {
            this.appendStatement(new SetSplitState(0, returnNode.getLineNumber()));
            this.getCurrentSplitState().hasReturn = true;
        }
        this.appendStatement(returnNode);
        return returnNode;
    }
    
    private void appendStatement(final Statement statement) {
        ((BlockLexicalContext)this.lc).appendStatement(statement);
    }
    
    private boolean inSplitNode() {
        return this.getCurrentFunctionState().splitDepth > 0;
    }
    
    private FunctionState getCurrentFunctionState() {
        return this.functionStates.peek();
    }
    
    private SplitState getCurrentSplitState() {
        return this.splitStates.peek();
    }
    
    static {
        THIS_NAME = CompilerConstants.THIS.symbolName();
        RETURN_NAME = CompilerConstants.RETURN.symbolName();
        RETURN_PARAM_NAME = SplitIntoFunctions.RETURN_NAME + "-in";
    }
    
    private static class FunctionState
    {
        final FunctionNode fn;
        final List<Statement> varStatements;
        int splitDepth;
        
        FunctionState(final FunctionNode fn) {
            this.varStatements = new ArrayList<Statement>();
            this.fn = fn;
        }
    }
    
    private static class SplitState
    {
        final SplitNode splitNode;
        boolean hasReturn;
        boolean hasBreak;
        final List<JumpStatement> jumpStatements;
        
        int getSplitStateIndex(final JumpStatement jump) {
            if (jump instanceof BreakNode && jump.getLabelName() == null) {
                this.hasBreak = true;
                return 1;
            }
            int i = 0;
            for (final JumpStatement exJump : this.jumpStatements) {
                if (jump.getClass() == exJump.getClass() && Objects.equals(jump.getLabelName(), exJump.getLabelName())) {
                    return i + 2;
                }
                ++i;
            }
            this.jumpStatements.add(jump);
            return i + 2;
        }
        
        SplitState(final SplitNode splitNode) {
            this.jumpStatements = new ArrayList<JumpStatement>();
            this.splitNode = splitNode;
        }
    }
}
