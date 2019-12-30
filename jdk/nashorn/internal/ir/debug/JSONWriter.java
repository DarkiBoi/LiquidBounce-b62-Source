// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.UnaryNode;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IdentNode;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.BinaryNode;
import java.util.List;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

public final class JSONWriter extends SimpleNodeVisitor
{
    private final StringBuilder buf;
    private final boolean includeLocation;
    
    public static String parse(final Context context, final String code, final String name, final boolean includeLoc) {
        final Parser parser = new Parser(context.getEnv(), Source.sourceFor(name, code), new Context.ThrowErrorManager(), context.getEnv()._strict, context.getLogger(Parser.class));
        final JSONWriter jsonWriter = new JSONWriter(includeLoc);
        try {
            final FunctionNode functionNode = parser.parse();
            functionNode.accept(jsonWriter);
            return jsonWriter.getString();
        }
        catch (ParserException e) {
            e.throwAsEcmaException();
            return null;
        }
    }
    
    @Override
    public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression joinPredecessorExpression) {
        final Expression expr = joinPredecessorExpression.getExpression();
        if (expr != null) {
            expr.accept(this);
        }
        else {
            this.nullValue();
        }
        return false;
    }
    
    @Override
    protected boolean enterDefault(final Node node) {
        this.objectStart();
        this.location(node);
        return true;
    }
    
    private boolean leave() {
        this.objectEnd();
        return false;
    }
    
    @Override
    protected Node leaveDefault(final Node node) {
        this.objectEnd();
        return null;
    }
    
    @Override
    public boolean enterAccessNode(final AccessNode accessNode) {
        this.enterDefault(accessNode);
        this.type("MemberExpression");
        this.comma();
        this.property("object");
        accessNode.getBase().accept(this);
        this.comma();
        this.property("property", accessNode.getProperty());
        this.comma();
        this.property("computed", false);
        return this.leave();
    }
    
    @Override
    public boolean enterBlock(final Block block) {
        this.enterDefault(block);
        this.type("BlockStatement");
        this.comma();
        this.array("body", block.getStatements());
        return this.leave();
    }
    
    @Override
    public boolean enterBinaryNode(final BinaryNode binaryNode) {
        this.enterDefault(binaryNode);
        String name;
        if (binaryNode.isAssignment()) {
            name = "AssignmentExpression";
        }
        else if (binaryNode.isLogical()) {
            name = "LogicalExpression";
        }
        else {
            name = "BinaryExpression";
        }
        this.type(name);
        this.comma();
        this.property("operator", binaryNode.tokenType().getName());
        this.comma();
        this.property("left");
        binaryNode.lhs().accept(this);
        this.comma();
        this.property("right");
        binaryNode.rhs().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterBreakNode(final BreakNode breakNode) {
        this.enterDefault(breakNode);
        this.type("BreakStatement");
        this.comma();
        final String label = breakNode.getLabelName();
        if (label != null) {
            this.property("label", label);
        }
        else {
            this.property("label");
            this.nullValue();
        }
        return this.leave();
    }
    
    @Override
    public boolean enterCallNode(final CallNode callNode) {
        this.enterDefault(callNode);
        this.type("CallExpression");
        this.comma();
        this.property("callee");
        callNode.getFunction().accept(this);
        this.comma();
        this.array("arguments", callNode.getArgs());
        return this.leave();
    }
    
    @Override
    public boolean enterCaseNode(final CaseNode caseNode) {
        this.enterDefault(caseNode);
        this.type("SwitchCase");
        this.comma();
        final Node test = caseNode.getTest();
        this.property("test");
        if (test != null) {
            test.accept(this);
        }
        else {
            this.nullValue();
        }
        this.comma();
        this.array("consequent", caseNode.getBody().getStatements());
        return this.leave();
    }
    
    @Override
    public boolean enterCatchNode(final CatchNode catchNode) {
        this.enterDefault(catchNode);
        this.type("CatchClause");
        this.comma();
        this.property("param");
        catchNode.getException().accept(this);
        this.comma();
        final Node guard = catchNode.getExceptionCondition();
        if (guard != null) {
            this.property("guard");
            guard.accept(this);
            this.comma();
        }
        this.property("body");
        catchNode.getBody().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterContinueNode(final ContinueNode continueNode) {
        this.enterDefault(continueNode);
        this.type("ContinueStatement");
        this.comma();
        final String label = continueNode.getLabelName();
        if (label != null) {
            this.property("label", label);
        }
        else {
            this.property("label");
            this.nullValue();
        }
        return this.leave();
    }
    
    @Override
    public boolean enterEmptyNode(final EmptyNode emptyNode) {
        this.enterDefault(emptyNode);
        this.type("EmptyStatement");
        return this.leave();
    }
    
    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement) {
        final Node expression = expressionStatement.getExpression();
        if (expression instanceof RuntimeNode) {
            expression.accept(this);
            return false;
        }
        this.enterDefault(expressionStatement);
        this.type("ExpressionStatement");
        this.comma();
        this.property("expression");
        expression.accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterBlockStatement(final BlockStatement blockStatement) {
        this.enterDefault(blockStatement);
        this.type("BlockStatement");
        this.comma();
        this.property("block");
        blockStatement.getBlock().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterForNode(final ForNode forNode) {
        this.enterDefault(forNode);
        if (forNode.isForIn() || (forNode.isForEach() && forNode.getInit() != null)) {
            this.type("ForInStatement");
            this.comma();
            final Node init = forNode.getInit();
            assert init != null;
            this.property("left");
            init.accept(this);
            this.comma();
            final Node modify = forNode.getModify();
            assert modify != null;
            this.property("right");
            modify.accept(this);
            this.comma();
            this.property("body");
            forNode.getBody().accept(this);
            this.comma();
            this.property("each", forNode.isForEach());
        }
        else {
            this.type("ForStatement");
            this.comma();
            final Node init = forNode.getInit();
            this.property("init");
            if (init != null) {
                init.accept(this);
            }
            else {
                this.nullValue();
            }
            this.comma();
            final Node test = forNode.getTest();
            this.property("test");
            if (test != null) {
                test.accept(this);
            }
            else {
                this.nullValue();
            }
            this.comma();
            final Node update = forNode.getModify();
            this.property("update");
            if (update != null) {
                update.accept(this);
            }
            else {
                this.nullValue();
            }
            this.comma();
            this.property("body");
            forNode.getBody().accept(this);
        }
        return this.leave();
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        final boolean program = functionNode.isProgram();
        if (program) {
            return this.emitProgram(functionNode);
        }
        this.enterDefault(functionNode);
        String name;
        if (functionNode.isDeclared()) {
            name = "FunctionDeclaration";
        }
        else {
            name = "FunctionExpression";
        }
        this.type(name);
        this.comma();
        this.property("id");
        final FunctionNode.Kind kind = functionNode.getKind();
        if (functionNode.isAnonymous() || kind == FunctionNode.Kind.GETTER || kind == FunctionNode.Kind.SETTER) {
            this.nullValue();
        }
        else {
            functionNode.getIdent().accept(this);
        }
        this.comma();
        this.array("params", functionNode.getParameters());
        this.comma();
        this.arrayStart("defaults");
        this.arrayEnd();
        this.comma();
        this.property("rest");
        this.nullValue();
        this.comma();
        this.property("body");
        functionNode.getBody().accept(this);
        this.comma();
        this.property("generator", false);
        this.comma();
        this.property("expression", false);
        return this.leave();
    }
    
    private boolean emitProgram(final FunctionNode functionNode) {
        this.enterDefault(functionNode);
        this.type("Program");
        this.comma();
        final List<Statement> stats = functionNode.getBody().getStatements();
        final int size = stats.size();
        int idx = 0;
        this.arrayStart("body");
        for (final Node stat : stats) {
            stat.accept(this);
            if (idx != size - 1) {
                this.comma();
            }
            ++idx;
        }
        this.arrayEnd();
        return this.leave();
    }
    
    @Override
    public boolean enterIdentNode(final IdentNode identNode) {
        this.enterDefault(identNode);
        final String name = identNode.getName();
        if ("this".equals(name)) {
            this.type("ThisExpression");
        }
        else {
            this.type("Identifier");
            this.comma();
            this.property("name", identNode.getName());
        }
        return this.leave();
    }
    
    @Override
    public boolean enterIfNode(final IfNode ifNode) {
        this.enterDefault(ifNode);
        this.type("IfStatement");
        this.comma();
        this.property("test");
        ifNode.getTest().accept(this);
        this.comma();
        this.property("consequent");
        ifNode.getPass().accept(this);
        final Node elsePart = ifNode.getFail();
        this.comma();
        this.property("alternate");
        if (elsePart != null) {
            elsePart.accept(this);
        }
        else {
            this.nullValue();
        }
        return this.leave();
    }
    
    @Override
    public boolean enterIndexNode(final IndexNode indexNode) {
        this.enterDefault(indexNode);
        this.type("MemberExpression");
        this.comma();
        this.property("object");
        indexNode.getBase().accept(this);
        this.comma();
        this.property("property");
        indexNode.getIndex().accept(this);
        this.comma();
        this.property("computed", true);
        return this.leave();
    }
    
    @Override
    public boolean enterLabelNode(final LabelNode labelNode) {
        this.enterDefault(labelNode);
        this.type("LabeledStatement");
        this.comma();
        this.property("label", labelNode.getLabelName());
        this.comma();
        this.property("body");
        labelNode.getBody().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterLiteralNode(final LiteralNode literalNode) {
        this.enterDefault(literalNode);
        if (literalNode instanceof LiteralNode.ArrayLiteralNode) {
            this.type("ArrayExpression");
            this.comma();
            this.array("elements", ((LiteralNode.ArrayLiteralNode)literalNode).getElementExpressions());
        }
        else {
            this.type("Literal");
            this.comma();
            this.property("value");
            final Object value = literalNode.getValue();
            if (value instanceof Lexer.RegexToken) {
                final Lexer.RegexToken regex = (Lexer.RegexToken)value;
                final StringBuilder regexBuf = new StringBuilder();
                regexBuf.append('/');
                regexBuf.append(regex.getExpression());
                regexBuf.append('/');
                regexBuf.append(regex.getOptions());
                this.buf.append(quote(regexBuf.toString()));
            }
            else {
                final String str = literalNode.getString();
                this.buf.append(literalNode.isString() ? quote("$" + str) : str);
            }
        }
        return this.leave();
    }
    
    @Override
    public boolean enterObjectNode(final ObjectNode objectNode) {
        this.enterDefault(objectNode);
        this.type("ObjectExpression");
        this.comma();
        this.array("properties", objectNode.getElements());
        return this.leave();
    }
    
    @Override
    public boolean enterPropertyNode(final PropertyNode propertyNode) {
        final Node key = propertyNode.getKey();
        final Node value = propertyNode.getValue();
        if (value != null) {
            this.objectStart();
            this.location(propertyNode);
            this.property("key");
            key.accept(this);
            this.comma();
            this.property("value");
            value.accept(this);
            this.comma();
            this.property("kind", "init");
            this.objectEnd();
        }
        else {
            final Node getter = propertyNode.getGetter();
            if (getter != null) {
                this.objectStart();
                this.location(propertyNode);
                this.property("key");
                key.accept(this);
                this.comma();
                this.property("value");
                getter.accept(this);
                this.comma();
                this.property("kind", "get");
                this.objectEnd();
            }
            final Node setter = propertyNode.getSetter();
            if (setter != null) {
                if (getter != null) {
                    this.comma();
                }
                this.objectStart();
                this.location(propertyNode);
                this.property("key");
                key.accept(this);
                this.comma();
                this.property("value");
                setter.accept(this);
                this.comma();
                this.property("kind", "set");
                this.objectEnd();
            }
        }
        return false;
    }
    
    @Override
    public boolean enterReturnNode(final ReturnNode returnNode) {
        this.enterDefault(returnNode);
        this.type("ReturnStatement");
        this.comma();
        final Node arg = returnNode.getExpression();
        this.property("argument");
        if (arg != null) {
            arg.accept(this);
        }
        else {
            this.nullValue();
        }
        return this.leave();
    }
    
    @Override
    public boolean enterRuntimeNode(final RuntimeNode runtimeNode) {
        final RuntimeNode.Request req = runtimeNode.getRequest();
        if (req == RuntimeNode.Request.DEBUGGER) {
            this.enterDefault(runtimeNode);
            this.type("DebuggerStatement");
            return this.leave();
        }
        return false;
    }
    
    @Override
    public boolean enterSplitNode(final SplitNode splitNode) {
        return false;
    }
    
    @Override
    public boolean enterSwitchNode(final SwitchNode switchNode) {
        this.enterDefault(switchNode);
        this.type("SwitchStatement");
        this.comma();
        this.property("discriminant");
        switchNode.getExpression().accept(this);
        this.comma();
        this.array("cases", switchNode.getCases());
        return this.leave();
    }
    
    @Override
    public boolean enterTernaryNode(final TernaryNode ternaryNode) {
        this.enterDefault(ternaryNode);
        this.type("ConditionalExpression");
        this.comma();
        this.property("test");
        ternaryNode.getTest().accept(this);
        this.comma();
        this.property("consequent");
        ternaryNode.getTrueExpression().accept(this);
        this.comma();
        this.property("alternate");
        ternaryNode.getFalseExpression().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterThrowNode(final ThrowNode throwNode) {
        this.enterDefault(throwNode);
        this.type("ThrowStatement");
        this.comma();
        this.property("argument");
        throwNode.getExpression().accept(this);
        return this.leave();
    }
    
    @Override
    public boolean enterTryNode(final TryNode tryNode) {
        this.enterDefault(tryNode);
        this.type("TryStatement");
        this.comma();
        this.property("block");
        tryNode.getBody().accept(this);
        this.comma();
        final List<? extends Node> catches = tryNode.getCatches();
        final List<CatchNode> guarded = new ArrayList<CatchNode>();
        CatchNode unguarded = null;
        if (catches != null) {
            for (final Node n : catches) {
                final CatchNode cn = (CatchNode)n;
                if (cn.getExceptionCondition() != null) {
                    guarded.add(cn);
                }
                else {
                    assert unguarded == null : "too many unguarded?";
                    unguarded = cn;
                }
            }
        }
        this.array("guardedHandlers", guarded);
        this.comma();
        this.property("handler");
        if (unguarded != null) {
            unguarded.accept(this);
        }
        else {
            this.nullValue();
        }
        this.comma();
        this.property("finalizer");
        final Node finallyNode = tryNode.getFinallyBody();
        if (finallyNode != null) {
            finallyNode.accept(this);
        }
        else {
            this.nullValue();
        }
        return this.leave();
    }
    
    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        this.enterDefault(unaryNode);
        final TokenType tokenType = unaryNode.tokenType();
        if (tokenType == TokenType.NEW) {
            this.type("NewExpression");
            this.comma();
            final CallNode callNode = (CallNode)unaryNode.getExpression();
            this.property("callee");
            callNode.getFunction().accept(this);
            this.comma();
            this.array("arguments", callNode.getArgs());
        }
        else {
            boolean prefix = false;
            String operator = null;
            switch (tokenType) {
                case INCPOSTFIX: {
                    prefix = false;
                    operator = "++";
                    break;
                }
                case DECPOSTFIX: {
                    prefix = false;
                    operator = "--";
                    break;
                }
                case INCPREFIX: {
                    operator = "++";
                    prefix = true;
                    break;
                }
                case DECPREFIX: {
                    operator = "--";
                    prefix = true;
                    break;
                }
                default: {
                    prefix = true;
                    operator = tokenType.getName();
                    break;
                }
            }
            this.type(unaryNode.isAssignment() ? "UpdateExpression" : "UnaryExpression");
            this.comma();
            this.property("operator", operator);
            this.comma();
            this.property("prefix", prefix);
            this.comma();
            this.property("argument");
            unaryNode.getExpression().accept(this);
        }
        return this.leave();
    }
    
    @Override
    public boolean enterVarNode(final VarNode varNode) {
        final Node init = varNode.getInit();
        if (init instanceof FunctionNode && ((FunctionNode)init).isDeclared()) {
            init.accept(this);
            return false;
        }
        this.enterDefault(varNode);
        this.type("VariableDeclaration");
        this.comma();
        this.arrayStart("declarations");
        this.objectStart();
        this.location(varNode.getName());
        this.type("VariableDeclarator");
        this.comma();
        this.property("id");
        varNode.getName().accept(this);
        this.comma();
        this.property("init");
        if (init != null) {
            init.accept(this);
        }
        else {
            this.nullValue();
        }
        this.objectEnd();
        this.arrayEnd();
        return this.leave();
    }
    
    @Override
    public boolean enterWhileNode(final WhileNode whileNode) {
        this.enterDefault(whileNode);
        this.type(whileNode.isDoWhile() ? "DoWhileStatement" : "WhileStatement");
        this.comma();
        if (whileNode.isDoWhile()) {
            this.property("body");
            whileNode.getBody().accept(this);
            this.comma();
            this.property("test");
            whileNode.getTest().accept(this);
        }
        else {
            this.property("test");
            whileNode.getTest().accept(this);
            this.comma();
            this.property("body");
            whileNode.getBody().accept(this);
        }
        return this.leave();
    }
    
    @Override
    public boolean enterWithNode(final WithNode withNode) {
        this.enterDefault(withNode);
        this.type("WithStatement");
        this.comma();
        this.property("object");
        withNode.getExpression().accept(this);
        this.comma();
        this.property("body");
        withNode.getBody().accept(this);
        return this.leave();
    }
    
    private JSONWriter(final boolean includeLocation) {
        this.buf = new StringBuilder();
        this.includeLocation = includeLocation;
    }
    
    private String getString() {
        return this.buf.toString();
    }
    
    private void property(final String key, final String value, final boolean escape) {
        this.buf.append('\"');
        this.buf.append(key);
        this.buf.append("\":");
        if (value != null) {
            if (escape) {
                this.buf.append('\"');
            }
            this.buf.append(value);
            if (escape) {
                this.buf.append('\"');
            }
        }
    }
    
    private void property(final String key, final String value) {
        this.property(key, value, true);
    }
    
    private void property(final String key, final boolean value) {
        this.property(key, Boolean.toString(value), false);
    }
    
    private void property(final String key, final int value) {
        this.property(key, Integer.toString(value), false);
    }
    
    private void property(final String key) {
        this.property(key, null);
    }
    
    private void type(final String value) {
        this.property("type", value);
    }
    
    private void objectStart(final String name) {
        this.buf.append('\"');
        this.buf.append(name);
        this.buf.append("\":{");
    }
    
    private void objectStart() {
        this.buf.append('{');
    }
    
    private void objectEnd() {
        this.buf.append('}');
    }
    
    private void array(final String name, final List<? extends Node> nodes) {
        final int size = nodes.size();
        int idx = 0;
        this.arrayStart(name);
        for (final Node node : nodes) {
            if (node != null) {
                node.accept(this);
            }
            else {
                this.nullValue();
            }
            if (idx != size - 1) {
                this.comma();
            }
            ++idx;
        }
        this.arrayEnd();
    }
    
    private void arrayStart(final String name) {
        this.buf.append('\"');
        this.buf.append(name);
        this.buf.append('\"');
        this.buf.append(':');
        this.buf.append('[');
    }
    
    private void arrayEnd() {
        this.buf.append(']');
    }
    
    private void comma() {
        this.buf.append(',');
    }
    
    private void nullValue() {
        this.buf.append("null");
    }
    
    private void location(final Node node) {
        if (this.includeLocation) {
            this.objectStart("loc");
            final Source src = this.lc.getCurrentFunction().getSource();
            this.property("source", src.getName());
            this.comma();
            this.objectStart("start");
            final int start = node.getStart();
            this.property("line", src.getLine(start));
            this.comma();
            this.property("column", src.getColumn(start));
            this.objectEnd();
            this.comma();
            this.objectStart("end");
            final int end = node.getFinish();
            this.property("line", src.getLine(end));
            this.comma();
            this.property("column", src.getColumn(end));
            this.objectEnd();
            this.objectEnd();
            this.comma();
        }
    }
    
    private static String quote(final String str) {
        return JSONParser.quote(str);
    }
}
