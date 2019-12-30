// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.util.Collections;
import java.util.Iterator;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.Namespace;
import java.util.List;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class FunctionNode extends LexicalContextExpression implements Flags<FunctionNode>, CompileUnitHolder
{
    private static final long serialVersionUID = 1L;
    public static final Type FUNCTION_TYPE;
    private final transient Source source;
    private final Object endParserState;
    @Ignore
    private final IdentNode ident;
    private final Block body;
    private final String name;
    private final CompileUnit compileUnit;
    private final Kind kind;
    private final List<IdentNode> parameters;
    private final long firstToken;
    private final long lastToken;
    private final transient Namespace namespace;
    @Ignore
    private final int thisProperties;
    private final int flags;
    private final int lineNumber;
    private final Class<?> rootClass;
    public static final int IS_ANONYMOUS = 1;
    public static final int IS_DECLARED = 2;
    public static final int IS_STRICT = 4;
    public static final int USES_ARGUMENTS = 8;
    public static final int IS_SPLIT = 16;
    public static final int HAS_EVAL = 32;
    public static final int HAS_NESTED_EVAL = 64;
    public static final int HAS_SCOPE_BLOCK = 128;
    public static final int DEFINES_ARGUMENTS = 256;
    public static final int USES_ANCESTOR_SCOPE = 512;
    public static final int HAS_FUNCTION_DECLARATIONS = 1024;
    public static final int IS_DEOPTIMIZABLE = 2048;
    public static final int HAS_APPLY_TO_CALL_SPECIALIZATION = 4096;
    public static final int IS_PROGRAM = 8192;
    public static final int USES_SELF_SYMBOL = 16384;
    public static final int USES_THIS = 32768;
    public static final int IN_DYNAMIC_CONTEXT = 65536;
    public static final int IS_PRINT_PARSE = 131072;
    public static final int IS_PRINT_LOWER_PARSE = 262144;
    public static final int IS_PRINT_AST = 524288;
    public static final int IS_PRINT_LOWER_AST = 1048576;
    public static final int IS_PRINT_SYMBOLS = 2097152;
    public static final int IS_PROFILE = 4194304;
    public static final int IS_TRACE_ENTEREXIT = 8388608;
    public static final int IS_TRACE_MISSES = 16777216;
    public static final int IS_TRACE_VALUES = 33554432;
    public static final int NEEDS_CALLEE = 67108864;
    public static final int IS_CACHED = 134217728;
    public static final int EXTENSION_CALLSITE_FLAGS = 66977792;
    private static final int HAS_DEEP_EVAL = 96;
    private static final int HAS_ALL_VARS_IN_SCOPE = 96;
    private static final int MAYBE_NEEDS_ARGUMENTS = 40;
    public static final int NEEDS_PARENT_SCOPE = 8800;
    private Type returnType;
    
    public FunctionNode(final Source source, final int lineNumber, final long token, final int finish, final long firstToken, final Namespace namespace, final IdentNode ident, final String name, final List<IdentNode> parameters, final Kind kind, final int flags) {
        super(token, finish);
        this.returnType = Type.UNKNOWN;
        this.source = source;
        this.lineNumber = lineNumber;
        this.ident = ident;
        this.name = name;
        this.kind = kind;
        this.parameters = parameters;
        this.firstToken = firstToken;
        this.lastToken = token;
        this.namespace = namespace;
        this.flags = flags;
        this.compileUnit = null;
        this.body = null;
        this.thisProperties = 0;
        this.rootClass = null;
        this.endParserState = null;
    }
    
    private FunctionNode(final FunctionNode functionNode, final long lastToken, final Object endParserState, final int flags, final String name, final Type returnType, final CompileUnit compileUnit, final Block body, final List<IdentNode> parameters, final int thisProperties, final Class<?> rootClass, final Source source, final Namespace namespace) {
        super(functionNode);
        this.returnType = Type.UNKNOWN;
        this.endParserState = endParserState;
        this.lineNumber = functionNode.lineNumber;
        this.flags = flags;
        this.name = name;
        this.returnType = returnType;
        this.compileUnit = compileUnit;
        this.lastToken = lastToken;
        this.body = body;
        this.parameters = parameters;
        this.thisProperties = thisProperties;
        this.rootClass = rootClass;
        this.source = source;
        this.namespace = namespace;
        this.ident = functionNode.ident;
        this.kind = functionNode.kind;
        this.firstToken = functionNode.firstToken;
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterFunctionNode(this)) {
            return visitor.leaveFunctionNode(this.setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    public List<IdentNode> visitParameters(final NodeVisitor<? extends LexicalContext> visitor) {
        return Node.accept(visitor, this.parameters);
    }
    
    public int getCallSiteFlags() {
        int callsiteFlags = 0;
        if (this.getFlag(4)) {
            callsiteFlags |= 0x2;
        }
        if ((this.flags & 0x3FE0000) == 0x0) {
            return callsiteFlags;
        }
        if (this.getFlag(4194304)) {
            callsiteFlags |= 0x40;
        }
        if (this.getFlag(16777216)) {
            callsiteFlags |= 0x180;
        }
        if (this.getFlag(33554432)) {
            callsiteFlags |= 0x680;
        }
        if (this.getFlag(8388608)) {
            callsiteFlags |= 0x280;
        }
        return callsiteFlags;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public FunctionNode initializeDeserialized(final Source source, final Namespace namespace) {
        if (source == null || namespace == null) {
            throw new IllegalArgumentException();
        }
        if (this.source == source && this.namespace == namespace) {
            return this;
        }
        if (this.source != null || this.namespace != null) {
            throw new IllegalStateException();
        }
        return new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, source, namespace);
    }
    
    public int getId() {
        return this.position();
    }
    
    public String getSourceName() {
        return getSourceName(this.source);
    }
    
    public static String getSourceName(final Source source) {
        final String explicitURL = source.getExplicitURL();
        return (explicitURL != null) ? explicitURL : source.getName();
    }
    
    public static int getDirectiveFlag(final String directive) {
        switch (directive) {
            case "nashorn callsite trace enterexit": {
                return 8388608;
            }
            case "nashorn callsite trace misses": {
                return 16777216;
            }
            case "nashorn callsite trace objects": {
                return 33554432;
            }
            case "nashorn callsite profile": {
                return 4194304;
            }
            case "nashorn print parse": {
                return 131072;
            }
            case "nashorn print lower parse": {
                return 262144;
            }
            case "nashorn print ast": {
                return 524288;
            }
            case "nashorn print lower ast": {
                return 1048576;
            }
            case "nashorn print symbols": {
                return 2097152;
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public String uniqueName(final String base) {
        return this.namespace.uniqueName(base);
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printTypes) {
        sb.append('[').append(this.returnType).append(']').append(' ');
        sb.append("function");
        if (this.ident != null) {
            sb.append(' ');
            this.ident.toString(sb, printTypes);
        }
        sb.append('(');
        final Iterator<IdentNode> iter = this.parameters.iterator();
        while (iter.hasNext()) {
            final IdentNode parameter = iter.next();
            if (parameter.getSymbol() != null) {
                sb.append('[').append(parameter.getType()).append(']').append(' ');
            }
            parameter.toString(sb, printTypes);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
    }
    
    @Override
    public int getFlags() {
        return this.flags;
    }
    
    @Override
    public boolean getFlag(final int flag) {
        return (this.flags & flag) != 0x0;
    }
    
    @Override
    public FunctionNode setFlags(final LexicalContext lc, final int flags) {
        if (this.flags == flags) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    @Override
    public FunctionNode clearFlag(final LexicalContext lc, final int flag) {
        return this.setFlags(lc, this.flags & ~flag);
    }
    
    @Override
    public FunctionNode setFlag(final LexicalContext lc, final int flag) {
        return this.setFlags(lc, this.flags | flag);
    }
    
    public boolean isProgram() {
        return this.getFlag(8192);
    }
    
    public boolean canBeDeoptimized() {
        return this.getFlag(2048);
    }
    
    public boolean hasEval() {
        return this.getFlag(32);
    }
    
    public boolean hasNestedEval() {
        return this.getFlag(64);
    }
    
    public long getFirstToken() {
        return this.firstToken;
    }
    
    public boolean hasDeclaredFunctions() {
        return this.getFlag(1024);
    }
    
    public boolean needsCallee() {
        return this.needsParentScope() || this.usesSelfSymbol() || this.isSplit() || (this.needsArguments() && !this.isStrict()) || this.hasApplyToCallSpecialization();
    }
    
    public boolean usesThis() {
        return this.getFlag(32768);
    }
    
    public boolean hasApplyToCallSpecialization() {
        return this.getFlag(4096);
    }
    
    public IdentNode getIdent() {
        return this.ident;
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public FunctionNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags | (body.needsScope() ? 128 : 0), this.name, this.returnType, this.compileUnit, body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public boolean isVarArg() {
        return this.needsArguments() || this.parameters.size() > 250;
    }
    
    public boolean inDynamicContext() {
        return this.getFlag(65536);
    }
    
    public boolean needsDynamicScope() {
        return this.hasEval() && !this.isStrict();
    }
    
    public FunctionNode setInDynamicContext(final LexicalContext lc) {
        return this.setFlag(lc, 65536);
    }
    
    public boolean needsArguments() {
        return this.getFlag(40) && !this.getFlag(256) && !this.isProgram();
    }
    
    public boolean needsParentScope() {
        return this.getFlag(8800);
    }
    
    public FunctionNode setThisProperties(final LexicalContext lc, final int thisProperties) {
        if (this.thisProperties == thisProperties) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public int getThisProperties() {
        return this.thisProperties;
    }
    
    public boolean hasScopeBlock() {
        return this.getFlag(128);
    }
    
    public Kind getKind() {
        return this.kind;
    }
    
    public long getLastToken() {
        return this.lastToken;
    }
    
    public FunctionNode setLastToken(final LexicalContext lc, final long lastToken) {
        if (this.lastToken == lastToken) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, lastToken, this.endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public Object getEndParserState() {
        return this.endParserState;
    }
    
    public FunctionNode setEndParserState(final LexicalContext lc, final Object endParserState) {
        if (this.endParserState == endParserState) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public String getName() {
        return this.name;
    }
    
    public FunctionNode setName(final LexicalContext lc, final String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public boolean allVarsInScope() {
        return this.getFlag(96);
    }
    
    public boolean isSplit() {
        return this.getFlag(16);
    }
    
    public List<IdentNode> getParameters() {
        return Collections.unmodifiableList((List<? extends IdentNode>)this.parameters);
    }
    
    public int getNumOfParams() {
        return this.parameters.size();
    }
    
    public IdentNode getParameter(final int index) {
        return this.parameters.get(index);
    }
    
    public FunctionNode setParameters(final LexicalContext lc, final List<IdentNode> parameters) {
        if (this.parameters == parameters) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public boolean isDeclared() {
        return this.getFlag(2);
    }
    
    public boolean isAnonymous() {
        return this.getFlag(1);
    }
    
    public boolean usesSelfSymbol() {
        return this.getFlag(16384);
    }
    
    public boolean isNamedFunctionExpression() {
        return !this.getFlag(8195);
    }
    
    @Override
    public Type getType() {
        return FunctionNode.FUNCTION_TYPE;
    }
    
    @Override
    public Type getWidestOperationType() {
        return FunctionNode.FUNCTION_TYPE;
    }
    
    public Type getReturnType() {
        return this.returnType;
    }
    
    public FunctionNode setReturnType(final LexicalContext lc, final Type returnType) {
        final Type type = returnType.isObject() ? Type.OBJECT : returnType;
        if (this.returnType == type) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, type, this.compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public boolean isStrict() {
        return this.getFlag(4);
    }
    
    public boolean isCached() {
        return this.getFlag(134217728);
    }
    
    public FunctionNode setCached(final LexicalContext lc) {
        return this.setFlag(lc, 134217728);
    }
    
    @Override
    public CompileUnit getCompileUnit() {
        return this.compileUnit;
    }
    
    public FunctionNode setCompileUnit(final LexicalContext lc, final CompileUnit compileUnit) {
        if (this.compileUnit == compileUnit) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, this.returnType, compileUnit, this.body, this.parameters, this.thisProperties, this.rootClass, this.source, this.namespace));
    }
    
    public Symbol compilerConstant(final CompilerConstants cc) {
        return this.body.getExistingSymbol(cc.symbolName());
    }
    
    public Class<?> getRootClass() {
        return this.rootClass;
    }
    
    public FunctionNode setRootClass(final LexicalContext lc, final Class<?> rootClass) {
        if (this.rootClass == rootClass) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new FunctionNode(this, this.lastToken, this.endParserState, this.flags, this.name, this.returnType, this.compileUnit, this.body, this.parameters, this.thisProperties, rootClass, this.source, this.namespace));
    }
    
    static {
        FUNCTION_TYPE = Type.typeFor(ScriptFunction.class);
    }
    
    public enum Kind
    {
        NORMAL, 
        SCRIPT, 
        GETTER, 
        SETTER;
    }
}
