// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.codegen.FunctionSignature;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.TreeMap;
import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.codegen.TypeMap;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.ir.Symbol;
import java.util.IdentityHashMap;
import jdk.nashorn.internal.codegen.Namespace;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import jdk.nashorn.internal.runtime.options.Options;
import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.parser.TokenType;
import jdk.internal.dynalink.support.NameCodec;
import jdk.nashorn.internal.parser.Token;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.Set;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "recompile")
public final class RecompilableScriptFunctionData extends ScriptFunctionData implements Loggable
{
    public static final String RECOMPILATION_PREFIX = "Recompilation$";
    private static final ExecutorService astSerializerExecutorService;
    private final int functionNodeId;
    private final String functionName;
    private final int lineNumber;
    private transient Source source;
    private volatile Object cachedAst;
    private final long token;
    private final AllocationStrategy allocationStrategy;
    private final Object endParserState;
    private transient CodeInstaller installer;
    private final Map<Integer, RecompilableScriptFunctionData> nestedFunctions;
    private RecompilableScriptFunctionData parent;
    private final int functionFlags;
    private static final MethodHandles.Lookup LOOKUP;
    private transient DebugLogger log;
    private final Map<String, Integer> externalScopeDepths;
    private final Set<String> internalSymbols;
    private static final int GET_SET_PREFIX_LENGTH;
    private static final long serialVersionUID = 4914839316174633726L;
    
    public RecompilableScriptFunctionData(final FunctionNode functionNode, final CodeInstaller installer, final AllocationStrategy allocationStrategy, final Map<Integer, RecompilableScriptFunctionData> nestedFunctions, final Map<String, Integer> externalScopeDepths, final Set<String> internalSymbols) {
        super(functionName(functionNode), Math.min(functionNode.getParameters().size(), 250), getDataFlags(functionNode));
        this.functionName = functionNode.getName();
        this.lineNumber = functionNode.getLineNumber();
        this.functionFlags = (functionNode.getFlags() | (functionNode.needsCallee() ? 67108864 : 0));
        this.functionNodeId = functionNode.getId();
        this.source = functionNode.getSource();
        this.endParserState = functionNode.getEndParserState();
        this.token = tokenFor(functionNode);
        this.installer = installer;
        this.allocationStrategy = allocationStrategy;
        this.nestedFunctions = smallMap(nestedFunctions);
        this.externalScopeDepths = smallMap(externalScopeDepths);
        this.internalSymbols = smallSet(new HashSet<String>(internalSymbols));
        for (final RecompilableScriptFunctionData nfn : nestedFunctions.values()) {
            assert nfn.getParent() == null;
            nfn.setParent(this);
        }
        this.createLogger();
    }
    
    private static <K, V> Map<K, V> smallMap(final Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        }
        if (map.size() == 1) {
            final Map.Entry<K, V> entry = map.entrySet().iterator().next();
            return Collections.singletonMap(entry.getKey(), entry.getValue());
        }
        return map;
    }
    
    private static <T> Set<T> smallSet(final Set<T> set) {
        if (set == null || set.isEmpty()) {
            return Collections.emptySet();
        }
        if (set.size() == 1) {
            return Collections.singleton(set.iterator().next());
        }
        return set;
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context ctxt) {
        return ctxt.getLogger(this.getClass());
    }
    
    public boolean hasInternalSymbol(final String symbolName) {
        return this.internalSymbols.contains(symbolName);
    }
    
    public int getExternalSymbolDepth(final String symbolName) {
        final Integer depth = this.externalScopeDepths.get(symbolName);
        return (depth == null) ? -1 : depth;
    }
    
    public Set<String> getExternalSymbolNames() {
        return Collections.unmodifiableSet((Set<? extends String>)this.externalScopeDepths.keySet());
    }
    
    public Object getEndParserState() {
        return this.endParserState;
    }
    
    public RecompilableScriptFunctionData getParent() {
        return this.parent;
    }
    
    void setParent(final RecompilableScriptFunctionData parent) {
        this.parent = parent;
    }
    
    @Override
    String toSource() {
        if (this.source != null && this.token != 0L) {
            return this.source.getString(Token.descPosition(this.token), Token.descLength(this.token));
        }
        return "function " + ((this.name == null) ? "" : this.name) + "() { [native code] }";
    }
    
    public void initTransients(final Source src, final CodeInstaller inst) {
        if (this.source == null && this.installer == null) {
            this.source = src;
            this.installer = inst;
        }
        else if (this.source != src || !this.installer.isCompatibleWith(inst)) {
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + '@' + this.functionNodeId;
    }
    
    @Override
    public String toStringVerbose() {
        final StringBuilder sb = new StringBuilder();
        sb.append("fnId=").append(this.functionNodeId).append(' ');
        if (this.source != null) {
            sb.append(this.source.getName()).append(':').append(this.lineNumber).append(' ');
        }
        return sb.toString() + super.toString();
    }
    
    public String getFunctionName() {
        return this.functionName;
    }
    
    @Override
    public boolean inDynamicContext() {
        return this.getFunctionFlag(65536);
    }
    
    private static String functionName(final FunctionNode fn) {
        if (fn.isAnonymous()) {
            return "";
        }
        final FunctionNode.Kind kind = fn.getKind();
        if (kind == FunctionNode.Kind.GETTER || kind == FunctionNode.Kind.SETTER) {
            final String name = NameCodec.decode(fn.getIdent().getName());
            return name.substring(RecompilableScriptFunctionData.GET_SET_PREFIX_LENGTH);
        }
        return fn.getIdent().getName();
    }
    
    private static long tokenFor(final FunctionNode fn) {
        final int position = Token.descPosition(fn.getFirstToken());
        final long lastToken = Token.withDelimiter(fn.getLastToken());
        final int length = Token.descPosition(lastToken) - position + ((Token.descType(lastToken) == TokenType.EOL) ? 0 : Token.descLength(lastToken));
        return Token.toDesc(TokenType.FUNCTION, position, length);
    }
    
    private static int getDataFlags(final FunctionNode functionNode) {
        int flags = 4;
        if (functionNode.isStrict()) {
            flags |= 0x1;
        }
        if (functionNode.needsCallee()) {
            flags |= 0x8;
        }
        if (functionNode.usesThis() || functionNode.hasEval()) {
            flags |= 0x10;
        }
        if (functionNode.isVarArg()) {
            flags |= 0x20;
        }
        if (functionNode.getKind() == FunctionNode.Kind.GETTER || functionNode.getKind() == FunctionNode.Kind.SETTER) {
            flags |= 0x40;
        }
        return flags;
    }
    
    @Override
    PropertyMap getAllocatorMap(final ScriptObject prototype) {
        return this.allocationStrategy.getAllocatorMap(prototype);
    }
    
    @Override
    ScriptObject allocate(final PropertyMap map) {
        return this.allocationStrategy.allocate(map);
    }
    
    FunctionNode reparse() {
        final FunctionNode cachedFunction = this.getCachedAst();
        if (cachedFunction == null) {
            final int descPosition = Token.descPosition(this.token);
            final Context context = Context.getContextTrusted();
            final Parser parser = new Parser(context.getEnv(), this.source, new Context.ThrowErrorManager(), this.isStrict(), this.lineNumber - 1, context.getLogger(Parser.class));
            if (this.getFunctionFlag(1)) {
                parser.setFunctionName(this.functionName);
            }
            parser.setReparsedFunction(this);
            final FunctionNode program = parser.parse(CompilerConstants.PROGRAM.symbolName(), descPosition, Token.descLength(this.token), this.isPropertyAccessor());
            return (this.isProgram() ? program : this.extractFunctionFromScript(program)).setName(null, this.functionName);
        }
        assert cachedFunction.isCached();
        return cachedFunction;
    }
    
    private FunctionNode getCachedAst() {
        final Object lCachedAst = this.cachedAst;
        if (lCachedAst instanceof Reference) {
            final FunctionNode fn = ((Reference)lCachedAst).get();
            if (fn != null) {
                return this.cloneSymbols(fn);
            }
        }
        else if (lCachedAst instanceof SerializedAst) {
            final SerializedAst serializedAst = (SerializedAst)lCachedAst;
            final FunctionNode cachedFn = serializedAst.cachedAst.get();
            if (cachedFn != null) {
                return this.cloneSymbols(cachedFn);
            }
            final FunctionNode deserializedFn = this.deserialize(serializedAst.serializedAst);
            serializedAst.cachedAst = (Reference<FunctionNode>)new SoftReference(deserializedFn);
            return deserializedFn;
        }
        return null;
    }
    
    public void setCachedAst(final FunctionNode astToCache) {
        assert astToCache.getId() == this.functionNodeId;
        assert !(this.cachedAst instanceof SerializedAst);
        final boolean isSplit = astToCache.isSplit();
        assert this.cachedAst == null;
        final FunctionNode symbolClonedAst = this.cloneSymbols(astToCache);
        final Reference<FunctionNode> ref = new SoftReference<FunctionNode>(symbolClonedAst);
        this.cachedAst = ref;
        if (isSplit) {
            RecompilableScriptFunctionData.astSerializerExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    RecompilableScriptFunctionData.this.cachedAst = new SerializedAst(symbolClonedAst, ref);
                }
            });
        }
    }
    
    private static ExecutorService createAstSerializerExecutorService() {
        final int threads = Math.max(1, Options.getIntProperty("nashorn.serialize.threads", Runtime.getRuntime().availableProcessors() / 2));
        final ThreadPoolExecutor service = new ThreadPoolExecutor(threads, threads, 1L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                final Thread t = new Thread(r, "Nashorn AST Serializer");
                t.setDaemon(true);
                t.setPriority(4);
                return t;
            }
        });
        service.allowCoreThreadTimeOut(true);
        return service;
    }
    
    private FunctionNode deserialize(final byte[] serializedAst) {
        final ScriptEnvironment env = this.installer.getContext().getEnv();
        final Timing timing = env._timing;
        final long t1 = System.nanoTime();
        try {
            return AstDeserializer.deserialize(serializedAst).initializeDeserialized(this.source, new Namespace(env.getNamespace()));
        }
        finally {
            timing.accumulateTime("'Deserialize'", System.nanoTime() - t1);
        }
    }
    
    private FunctionNode cloneSymbols(final FunctionNode fn) {
        final IdentityHashMap<Symbol, Symbol> symbolReplacements = new IdentityHashMap<Symbol, Symbol>();
        final boolean cached = fn.isCached();
        final Set<Symbol> blockDefinedSymbols = (fn.isSplit() && !cached) ? Collections.newSetFromMap(new IdentityHashMap<Symbol, Boolean>()) : null;
        FunctionNode newFn = (FunctionNode)fn.accept(new SimpleNodeVisitor() {
            private Symbol getReplacement(final Symbol original) {
                if (original == null) {
                    return null;
                }
                final Symbol existingReplacement = symbolReplacements.get(original);
                if (existingReplacement != null) {
                    return existingReplacement;
                }
                final Symbol newReplacement = original.clone();
                symbolReplacements.put(original, newReplacement);
                return newReplacement;
            }
            
            @Override
            public Node leaveIdentNode(final IdentNode identNode) {
                final Symbol oldSymbol = identNode.getSymbol();
                if (oldSymbol != null) {
                    final Symbol replacement = this.getReplacement(oldSymbol);
                    return identNode.setSymbol(replacement);
                }
                return identNode;
            }
            
            @Override
            public Node leaveForNode(final ForNode forNode) {
                return this.ensureUniqueLabels(forNode.setIterator(this.lc, this.getReplacement(forNode.getIterator())));
            }
            
            @Override
            public Node leaveSwitchNode(final SwitchNode switchNode) {
                return this.ensureUniqueLabels(switchNode.setTag(this.lc, this.getReplacement(switchNode.getTag())));
            }
            
            @Override
            public Node leaveTryNode(final TryNode tryNode) {
                return this.ensureUniqueLabels(tryNode.setException(this.lc, this.getReplacement(tryNode.getException())));
            }
            
            @Override
            public boolean enterBlock(final Block block) {
                for (final Symbol symbol : block.getSymbols()) {
                    final Symbol replacement = this.getReplacement(symbol);
                    if (blockDefinedSymbols != null) {
                        blockDefinedSymbols.add(replacement);
                    }
                }
                return true;
            }
            
            @Override
            public Node leaveBlock(final Block block) {
                return this.ensureUniqueLabels(block.replaceSymbols(this.lc, symbolReplacements));
            }
            
            @Override
            public Node leaveFunctionNode(final FunctionNode functionNode) {
                return functionNode.setParameters(this.lc, functionNode.visitParameters(this));
            }
            
            @Override
            protected Node leaveDefault(final Node node) {
                return this.ensureUniqueLabels(node);
            }
            
            private Node ensureUniqueLabels(final Node node) {
                return cached ? node.ensureUniqueLabels(this.lc) : node;
            }
        });
        if (blockDefinedSymbols != null) {
            Block newBody = null;
            for (final Symbol symbol : symbolReplacements.values()) {
                if (!blockDefinedSymbols.contains(symbol)) {
                    assert symbol.isScope();
                    assert this.externalScopeDepths.containsKey(symbol.getName());
                    symbol.setFlags((symbol.getFlags() & 0xFFFFFFFC) | 0x1);
                    if (newBody == null) {
                        newBody = newFn.getBody().copyWithNewSymbols();
                        newFn = newFn.setBody(null, newBody);
                    }
                    assert newBody.getExistingSymbol(symbol.getName()) == null;
                    newBody.putSymbol(symbol);
                }
            }
        }
        return newFn.setCached(null);
    }
    
    private boolean getFunctionFlag(final int flag) {
        return (this.functionFlags & flag) != 0x0;
    }
    
    private boolean isProgram() {
        return this.getFunctionFlag(8192);
    }
    
    TypeMap typeMap(final MethodType fnCallSiteType) {
        if (fnCallSiteType == null) {
            return null;
        }
        if (CompiledFunction.isVarArgsType(fnCallSiteType)) {
            return null;
        }
        return new TypeMap(this.functionNodeId, this.explicitParams(fnCallSiteType), this.needsCallee());
    }
    
    private static ScriptObject newLocals(final ScriptObject runtimeScope) {
        final ScriptObject locals = Global.newEmptyInstance();
        locals.setProto(runtimeScope);
        return locals;
    }
    
    private Compiler getCompiler(final FunctionNode fn, final MethodType actualCallSiteType, final ScriptObject runtimeScope) {
        return this.getCompiler(fn, actualCallSiteType, newLocals(runtimeScope), null, null);
    }
    
    private CodeInstaller getInstallerForNewCode() {
        final ScriptEnvironment env = this.installer.getContext().getEnv();
        return (env._optimistic_types || env._loader_per_compile) ? this.installer.withNewLoader() : this.installer;
    }
    
    Compiler getCompiler(final FunctionNode functionNode, final MethodType actualCallSiteType, final ScriptObject runtimeScope, final Map<Integer, Type> invalidatedProgramPoints, final int[] continuationEntryPoints) {
        final TypeMap typeMap = this.typeMap(actualCallSiteType);
        final Type[] paramTypes = (Type[])((typeMap == null) ? null : typeMap.getParameterTypes(this.functionNodeId));
        final Object typeInformationFile = OptimisticTypesPersistence.getLocationDescriptor(this.source, this.functionNodeId, paramTypes);
        return Compiler.forOnDemandCompilation(this.getInstallerForNewCode(), functionNode.getSource(), this.isStrict() | functionNode.isStrict(), this, typeMap, getEffectiveInvalidatedProgramPoints(invalidatedProgramPoints, typeInformationFile), typeInformationFile, continuationEntryPoints, runtimeScope);
    }
    
    private static Map<Integer, Type> getEffectiveInvalidatedProgramPoints(final Map<Integer, Type> invalidatedProgramPoints, final Object typeInformationFile) {
        if (invalidatedProgramPoints != null) {
            return invalidatedProgramPoints;
        }
        final Map<Integer, Type> loadedProgramPoints = OptimisticTypesPersistence.load(typeInformationFile);
        return (loadedProgramPoints != null) ? loadedProgramPoints : new TreeMap<Integer, Type>();
    }
    
    private FunctionInitializer compileTypeSpecialization(final MethodType actualCallSiteType, final ScriptObject runtimeScope, final boolean persist) {
        if (this.log.isEnabled()) {
            this.log.info("Parameter type specialization of '", this.functionName, "' signature: ", actualCallSiteType);
        }
        final boolean persistentCache = persist && this.usePersistentCodeCache();
        String cacheKey = null;
        if (persistentCache) {
            final TypeMap typeMap = this.typeMap(actualCallSiteType);
            final Type[] paramTypes = (Type[])((typeMap == null) ? null : typeMap.getParameterTypes(this.functionNodeId));
            cacheKey = CodeStore.getCacheKey(this.functionNodeId, paramTypes);
            final CodeInstaller newInstaller = this.getInstallerForNewCode();
            final StoredScript script = newInstaller.loadScript(this.source, cacheKey);
            if (script != null) {
                Compiler.updateCompilationId(script.getCompilationId());
                return script.installFunction(this, newInstaller);
            }
        }
        final FunctionNode fn = this.reparse();
        final Compiler compiler = this.getCompiler(fn, actualCallSiteType, runtimeScope);
        final FunctionNode compiledFn = compiler.compile(fn, fn.isCached() ? Compiler.CompilationPhases.COMPILE_ALL_CACHED : Compiler.CompilationPhases.COMPILE_ALL);
        if (persist && !compiledFn.hasApplyToCallSpecialization()) {
            compiler.persistClassInfo(cacheKey, compiledFn);
        }
        return new FunctionInitializer(compiledFn, compiler.getInvalidatedProgramPoints());
    }
    
    boolean usePersistentCodeCache() {
        return this.installer != null && this.installer.getContext().getEnv()._persistent_cache;
    }
    
    private MethodType explicitParams(final MethodType callSiteType) {
        if (CompiledFunction.isVarArgsType(callSiteType)) {
            return null;
        }
        final MethodType noCalleeThisType = callSiteType.dropParameterTypes(0, 2);
        final int callSiteParamCount = noCalleeThisType.parameterCount();
        final Class<?>[] paramTypes = noCalleeThisType.parameterArray();
        boolean changed = false;
        for (int i = 0; i < paramTypes.length; ++i) {
            final Class<?> paramType = paramTypes[i];
            if (!paramType.isPrimitive() && paramType != Object.class) {
                paramTypes[i] = Object.class;
                changed = true;
            }
        }
        final MethodType generalized = changed ? MethodType.methodType(noCalleeThisType.returnType(), paramTypes) : noCalleeThisType;
        if (callSiteParamCount < this.getArity()) {
            return generalized.appendParameterTypes((List<Class<?>>)Collections.nCopies(this.getArity() - callSiteParamCount, Object.class));
        }
        return generalized;
    }
    
    private FunctionNode extractFunctionFromScript(final FunctionNode script) {
        final Set<FunctionNode> fns = new HashSet<FunctionNode>();
        script.getBody().accept(new SimpleNodeVisitor() {
            @Override
            public boolean enterFunctionNode(final FunctionNode fn) {
                fns.add(fn);
                return false;
            }
        });
        assert fns.size() == 1 : "got back more than one method in recompilation";
        final FunctionNode f = fns.iterator().next();
        assert f.getId() == this.functionNodeId;
        if (!this.getFunctionFlag(2) && f.isDeclared()) {
            return f.clearFlag(null, 2);
        }
        return f;
    }
    
    private void logLookup(final boolean shouldLog, final MethodType targetType) {
        if (shouldLog && this.log.isEnabled()) {
            this.log.info("Looking up ", DebugLogger.quote(this.functionName), " type=", targetType);
        }
    }
    
    private MethodHandle lookup(final FunctionInitializer fnInit, final boolean shouldLog) {
        final MethodType type = fnInit.getMethodType();
        this.logLookup(shouldLog, type);
        return this.lookupCodeMethod(fnInit.getCode(), type);
    }
    
    MethodHandle lookup(final FunctionNode fn) {
        final MethodType type = new FunctionSignature(fn).getMethodType();
        this.logLookup(true, type);
        return this.lookupCodeMethod(fn.getCompileUnit().getCode(), type);
    }
    
    MethodHandle lookupCodeMethod(final Class<?> codeClass, final MethodType targetType) {
        return Lookup.MH.findStatic(RecompilableScriptFunctionData.LOOKUP, codeClass, this.functionName, targetType);
    }
    
    public void initializeCode(final FunctionNode functionNode) {
        if (!this.code.isEmpty() || functionNode.getId() != this.functionNodeId || !functionNode.getCompileUnit().isInitializing(this, functionNode)) {
            throw new IllegalStateException(this.name);
        }
        this.addCode(this.lookup(functionNode), null, null, functionNode.getFlags());
    }
    
    void initializeCode(final FunctionInitializer initializer) {
        this.addCode(this.lookup(initializer, true), null, null, initializer.getFlags());
    }
    
    private CompiledFunction addCode(final MethodHandle target, final Map<Integer, Type> invalidatedProgramPoints, final MethodType callSiteType, final int fnFlags) {
        final CompiledFunction cfn = new CompiledFunction(target, this, invalidatedProgramPoints, callSiteType, fnFlags);
        assert this.noDuplicateCode(cfn) : "duplicate code";
        this.code.add(cfn);
        return cfn;
    }
    
    private CompiledFunction addCode(final FunctionInitializer fnInit, final MethodType callSiteType) {
        if (this.isVariableArity()) {
            return this.addCode(this.lookup(fnInit, true), fnInit.getInvalidatedProgramPoints(), callSiteType, fnInit.getFlags());
        }
        final MethodHandle handle = this.lookup(fnInit, true);
        final MethodType fromType = handle.type();
        MethodType toType = ScriptFunctionData.needsCallee(fromType) ? callSiteType.changeParameterType(0, (Class<?>)ScriptFunction.class) : callSiteType.dropParameterTypes(0, 1);
        toType = toType.changeReturnType(fromType.returnType());
        final int toCount = toType.parameterCount();
        final int fromCount = fromType.parameterCount();
        for (int minCount = Math.min(fromCount, toCount), i = 0; i < minCount; ++i) {
            final Class<?> fromParam = fromType.parameterType(i);
            final Class<?> toParam = toType.parameterType(i);
            if (fromParam != toParam && !fromParam.isPrimitive() && !toParam.isPrimitive()) {
                assert fromParam.isAssignableFrom(toParam);
                toType = toType.changeParameterType(i, fromParam);
            }
        }
        if (fromCount > toCount) {
            toType = toType.appendParameterTypes(fromType.parameterList().subList(toCount, fromCount));
        }
        else if (fromCount < toCount) {
            toType = toType.dropParameterTypes(fromCount, toCount);
        }
        return this.addCode(this.lookup(fnInit, false).asType(toType), fnInit.getInvalidatedProgramPoints(), callSiteType, fnInit.getFlags());
    }
    
    public Class<?> getReturnType(final MethodType callSiteType, final ScriptObject runtimeScope) {
        return this.getBest(callSiteType, runtimeScope, CompiledFunction.NO_FUNCTIONS).type().returnType();
    }
    
    @Override
    synchronized CompiledFunction getBest(final MethodType callSiteType, final ScriptObject runtimeScope, final Collection<CompiledFunction> forbidden, final boolean linkLogicOkay) {
        assert this.isValidCallSite(callSiteType) : callSiteType;
        CompiledFunction existingBest = this.pickFunction(callSiteType, false);
        if (existingBest == null) {
            existingBest = this.pickFunction(callSiteType, true);
        }
        if (existingBest == null) {
            existingBest = this.addCode(this.compileTypeSpecialization(callSiteType, runtimeScope, true), callSiteType);
        }
        assert existingBest != null;
        if (existingBest.isApplyToCall()) {
            final CompiledFunction best = this.lookupExactApplyToCall(callSiteType);
            if (best != null) {
                return best;
            }
            existingBest = this.addCode(this.compileTypeSpecialization(callSiteType, runtimeScope, false), callSiteType);
        }
        return existingBest;
    }
    
    public boolean needsCallee() {
        return this.getFunctionFlag(67108864);
    }
    
    public int getFunctionFlags() {
        return this.functionFlags;
    }
    
    @Override
    MethodType getGenericType() {
        if (this.isVariableArity()) {
            return MethodType.genericMethodType(2, true);
        }
        return MethodType.genericMethodType(2 + this.getArity());
    }
    
    public int getFunctionNodeId() {
        return this.functionNodeId;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public RecompilableScriptFunctionData getScriptFunctionData(final int functionId) {
        if (functionId == this.functionNodeId) {
            return this;
        }
        RecompilableScriptFunctionData data = (this.nestedFunctions == null) ? null : this.nestedFunctions.get(functionId);
        if (data != null) {
            return data;
        }
        for (final RecompilableScriptFunctionData ndata : this.nestedFunctions.values()) {
            data = ndata.getScriptFunctionData(functionId);
            if (data != null) {
                return data;
            }
        }
        return null;
    }
    
    public boolean isGlobalSymbol(final FunctionNode functionNode, final String symbolName) {
        RecompilableScriptFunctionData data = this.getScriptFunctionData(functionNode.getId());
        assert data != null;
        while (!data.hasInternalSymbol(symbolName)) {
            data = data.getParent();
            if (data == null) {
                return true;
            }
        }
        return false;
    }
    
    public FunctionNode restoreFlags(final LexicalContext lc, final FunctionNode fn) {
        assert fn.getId() == this.functionNodeId;
        FunctionNode newFn = fn.setFlags(lc, this.functionFlags);
        if (newFn.hasNestedEval()) {
            assert newFn.hasScopeBlock();
            newFn = newFn.setBody(lc, newFn.getBody().setNeedsScope(null));
        }
        return newFn;
    }
    
    private boolean noDuplicateCode(final CompiledFunction compiledFunction) {
        for (final CompiledFunction cf : this.code) {
            if (cf.type().equals((Object)compiledFunction.type())) {
                return false;
            }
        }
        return true;
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.createLogger();
    }
    
    private void createLogger() {
        this.log = this.initLogger(Context.getContextTrusted());
    }
    
    static {
        astSerializerExecutorService = createAstSerializerExecutorService();
        LOOKUP = MethodHandles.lookup();
        GET_SET_PREFIX_LENGTH = "*et ".length();
    }
    
    private static class SerializedAst
    {
        private final byte[] serializedAst;
        private volatile Reference<FunctionNode> cachedAst;
        
        SerializedAst(final FunctionNode fn, final Reference<FunctionNode> cachedAst) {
            this.serializedAst = AstSerializer.serialize(fn);
            this.cachedAst = cachedAst;
        }
    }
}
