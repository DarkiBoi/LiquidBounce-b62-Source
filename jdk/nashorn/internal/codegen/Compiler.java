// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jdk.nashorn.internal.ir.debug.ClassHistogramElement;
import java.util.Comparator;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import java.util.Collection;
import jdk.nashorn.internal.runtime.FunctionInitializer;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import jdk.nashorn.internal.runtime.ParserException;
import java.util.logging.Level;
import java.lang.invoke.MethodType;
import java.util.TreeMap;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Optimistic;
import java.util.function.Consumer;
import jdk.internal.dynalink.support.NameCodec;
import java.io.File;
import java.util.HashMap;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.LinkedHashMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.CodeInstaller;
import java.util.Set;
import java.util.Map;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "compiler")
public final class Compiler implements Loggable
{
    public static final String SCRIPTS_PACKAGE = "jdk/nashorn/internal/scripts";
    public static final String OBJECTS_PACKAGE = "jdk/nashorn/internal/objects";
    private final ScriptEnvironment env;
    private final Source source;
    private final String sourceName;
    private final ErrorManager errors;
    private final boolean optimistic;
    private final Map<String, byte[]> bytecode;
    private final Set<CompileUnit> compileUnits;
    private final ConstantData constantData;
    private final CodeInstaller installer;
    private final DebugLogger log;
    private final Context context;
    private final TypeMap types;
    private final TypeEvaluator typeEvaluator;
    private final boolean strict;
    private final boolean onDemand;
    private final Map<Integer, Type> invalidatedProgramPoints;
    private final Object typeInformationFile;
    private final String firstCompileUnitName;
    private final int[] continuationEntryPoints;
    private RecompilableScriptFunctionData compiledFunction;
    private static final int COMPILE_UNIT_NAME_BUFFER_SIZE = 32;
    private static String[] RESERVED_NAMES;
    private final int compilationId;
    private final AtomicInteger nextCompileUnitId;
    private static final AtomicInteger COMPILATION_ID;
    private static final String DANGEROUS_CHARS = "\\/.;:$[]<>";
    
    public static Compiler forInitialCompilation(final CodeInstaller installer, final Source source, final ErrorManager errors, final boolean isStrict) {
        return new Compiler(installer.getContext(), installer, source, errors, isStrict);
    }
    
    public static Compiler forNoInstallerCompilation(final Context context, final Source source, final boolean isStrict) {
        return new Compiler(context, null, source, context.getErrorManager(), isStrict);
    }
    
    public static Compiler forOnDemandCompilation(final CodeInstaller installer, final Source source, final boolean isStrict, final RecompilableScriptFunctionData compiledFunction, final TypeMap types, final Map<Integer, Type> invalidatedProgramPoints, final Object typeInformationFile, final int[] continuationEntryPoints, final ScriptObject runtimeScope) {
        final Context context = installer.getContext();
        return new Compiler(context, installer, source, context.getErrorManager(), isStrict, true, compiledFunction, types, invalidatedProgramPoints, typeInformationFile, continuationEntryPoints, runtimeScope);
    }
    
    private Compiler(final Context context, final CodeInstaller installer, final Source source, final ErrorManager errors, final boolean isStrict) {
        this(context, installer, source, errors, isStrict, false, null, null, null, null, null, null);
    }
    
    private Compiler(final Context context, final CodeInstaller installer, final Source source, final ErrorManager errors, final boolean isStrict, final boolean isOnDemand, final RecompilableScriptFunctionData compiledFunction, final TypeMap types, final Map<Integer, Type> invalidatedProgramPoints, final Object typeInformationFile, final int[] continuationEntryPoints, final ScriptObject runtimeScope) {
        this.compilationId = Compiler.COMPILATION_ID.getAndIncrement();
        this.nextCompileUnitId = new AtomicInteger(0);
        this.context = context;
        this.env = context.getEnv();
        this.installer = installer;
        this.constantData = new ConstantData();
        this.compileUnits = CompileUnit.createCompileUnitSet();
        this.bytecode = new LinkedHashMap<String, byte[]>();
        this.log = this.initLogger(context);
        this.source = source;
        this.errors = errors;
        this.sourceName = FunctionNode.getSourceName(source);
        this.onDemand = isOnDemand;
        this.compiledFunction = compiledFunction;
        this.types = types;
        this.invalidatedProgramPoints = ((invalidatedProgramPoints == null) ? new HashMap<Integer, Type>() : invalidatedProgramPoints);
        this.typeInformationFile = typeInformationFile;
        this.continuationEntryPoints = (int[])((continuationEntryPoints == null) ? null : ((int[])continuationEntryPoints.clone()));
        this.typeEvaluator = new TypeEvaluator(this, runtimeScope);
        this.firstCompileUnitName = this.firstCompileUnitName();
        this.strict = isStrict;
        this.optimistic = this.env._optimistic_types;
    }
    
    private String safeSourceName() {
        String baseName = new File(this.source.getName()).getName();
        final int index = baseName.lastIndexOf(".js");
        if (index != -1) {
            baseName = baseName.substring(0, index);
        }
        baseName = baseName.replace('.', '_').replace('-', '_');
        if (!this.env._loader_per_compile) {
            baseName += this.installer.getUniqueScriptId();
        }
        final String mangled = this.env._verify_code ? replaceDangerChars(baseName) : NameCodec.encode(baseName);
        return (mangled != null) ? mangled : baseName;
    }
    
    private static String replaceDangerChars(final String name) {
        final int len = name.length();
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            final char ch = name.charAt(i);
            if ("\\/.;:$[]<>".indexOf(ch) != -1) {
                buf.append('_');
            }
            else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
    private String firstCompileUnitName() {
        final StringBuilder sb = new StringBuilder("jdk/nashorn/internal/scripts").append('/').append(CompilerConstants.DEFAULT_SCRIPT_NAME.symbolName()).append('$');
        if (this.isOnDemandCompilation()) {
            sb.append("Recompilation$");
        }
        if (this.compilationId > 0) {
            sb.append(this.compilationId).append('$');
        }
        if (this.types != null && this.compiledFunction.getFunctionNodeId() > 0) {
            sb.append(this.compiledFunction.getFunctionNodeId());
            final Type[] parameterTypes;
            final Type[] paramTypes = parameterTypes = this.types.getParameterTypes(this.compiledFunction.getFunctionNodeId());
            for (final Type t : parameterTypes) {
                sb.append(Type.getShortSignatureDescriptor(t));
            }
            sb.append('$');
        }
        sb.append(this.safeSourceName());
        return sb.toString();
    }
    
    void declareLocalSymbol(final String symbolName) {
        this.typeEvaluator.declareLocalSymbol(symbolName);
    }
    
    void setData(final RecompilableScriptFunctionData data) {
        assert this.compiledFunction == null : data;
        this.compiledFunction = data;
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context ctxt) {
        final boolean optimisticTypes = this.env._optimistic_types;
        final boolean lazyCompilation = this.env._lazy_compilation;
        return ctxt.getLogger(this.getClass(), new Consumer<DebugLogger>() {
            @Override
            public void accept(final DebugLogger newLogger) {
                if (!lazyCompilation) {
                    newLogger.warning("WARNING: Running with lazy compilation switched off. This is not a default setting.");
                }
                newLogger.warning("Optimistic types are ", optimisticTypes ? "ENABLED." : "DISABLED.");
            }
        });
    }
    
    ScriptEnvironment getScriptEnvironment() {
        return this.env;
    }
    
    boolean isOnDemandCompilation() {
        return this.onDemand;
    }
    
    boolean useOptimisticTypes() {
        return this.optimistic;
    }
    
    Context getContext() {
        return this.context;
    }
    
    Type getOptimisticType(final Optimistic node) {
        return this.typeEvaluator.getOptimisticType(node);
    }
    
    boolean hasStringPropertyIterator(final Expression expr) {
        return this.typeEvaluator.hasStringPropertyIterator(expr);
    }
    
    void addInvalidatedProgramPoint(final int programPoint, final Type type) {
        this.invalidatedProgramPoints.put(programPoint, type);
    }
    
    public Map<Integer, Type> getInvalidatedProgramPoints() {
        return this.invalidatedProgramPoints.isEmpty() ? null : new TreeMap<Integer, Type>(this.invalidatedProgramPoints);
    }
    
    TypeMap getTypeMap() {
        return this.types;
    }
    
    MethodType getCallSiteType(final FunctionNode fn) {
        if (this.types == null || !this.isOnDemandCompilation()) {
            return null;
        }
        return this.types.getCallSiteType(fn);
    }
    
    Type getParamType(final FunctionNode fn, final int pos) {
        return (this.types == null) ? null : this.types.get(fn, pos);
    }
    
    public FunctionNode compile(final FunctionNode functionNode, final CompilationPhases phases) throws CompilationException {
        if (this.log.isEnabled()) {
            this.log.info(">> Starting compile job for ", DebugLogger.quote(functionNode.getName()), " phases=", DebugLogger.quote(phases.getDesc()));
            this.log.indent();
        }
        final String name = DebugLogger.quote(functionNode.getName());
        FunctionNode newFunctionNode = functionNode;
        for (final String reservedName : Compiler.RESERVED_NAMES) {
            newFunctionNode.uniqueName(reservedName);
        }
        final boolean info = this.log.isLoggable(Level.INFO);
        final DebugLogger timeLogger = this.env.isTimingEnabled() ? this.env._timing.getLogger() : null;
        long time = 0L;
        for (final CompilationPhase phase : phases) {
            this.log.fine(phase, " starting for ", name);
            try {
                newFunctionNode = phase.apply(this, phases, newFunctionNode);
            }
            catch (ParserException error) {
                this.errors.error(error);
                if (this.env._dump_on_error) {
                    error.printStackTrace(this.env.getErr());
                }
                return null;
            }
            this.log.fine(phase, " done for function ", DebugLogger.quote(name));
            if (this.env._print_mem_usage) {
                this.printMemoryUsage(functionNode, phase.toString());
            }
            time += (this.env.isTimingEnabled() ? (phase.getEndTime() - phase.getStartTime()) : 0L);
        }
        if (this.typeInformationFile != null && !phases.isRestOfCompilation()) {
            OptimisticTypesPersistence.store(this.typeInformationFile, this.invalidatedProgramPoints);
        }
        this.log.unindent();
        if (info) {
            final StringBuilder sb = new StringBuilder("<< Finished compile job for ");
            sb.append(newFunctionNode.getSource()).append(':').append(DebugLogger.quote(newFunctionNode.getName()));
            if (time > 0L && timeLogger != null) {
                assert this.env.isTimingEnabled();
                sb.append(" in ").append(TimeUnit.NANOSECONDS.toMillis(time)).append(" ms");
            }
            this.log.info(sb);
        }
        return newFunctionNode;
    }
    
    Source getSource() {
        return this.source;
    }
    
    Map<String, byte[]> getBytecode() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends byte[]>)this.bytecode);
    }
    
    void clearBytecode() {
        this.bytecode.clear();
    }
    
    CompileUnit getFirstCompileUnit() {
        assert !this.compileUnits.isEmpty();
        return this.compileUnits.iterator().next();
    }
    
    Set<CompileUnit> getCompileUnits() {
        return this.compileUnits;
    }
    
    ConstantData getConstantData() {
        return this.constantData;
    }
    
    CodeInstaller getCodeInstaller() {
        return this.installer;
    }
    
    void addClass(final String name, final byte[] code) {
        this.bytecode.put(name, code);
    }
    
    String nextCompileUnitName() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append(this.firstCompileUnitName);
        final int cuid = this.nextCompileUnitId.getAndIncrement();
        if (cuid > 0) {
            sb.append("$cu").append(cuid);
        }
        return sb.toString();
    }
    
    public void persistClassInfo(final String cacheKey, final FunctionNode functionNode) {
        if (cacheKey != null && this.env._persistent_cache) {
            final Map<Integer, FunctionInitializer> initializers = new HashMap<Integer, FunctionInitializer>();
            if (this.isOnDemandCompilation()) {
                initializers.put(functionNode.getId(), new FunctionInitializer(functionNode, this.getInvalidatedProgramPoints()));
            }
            else {
                for (final CompileUnit compileUnit : this.getCompileUnits()) {
                    for (final FunctionNode fn : compileUnit.getFunctionNodes()) {
                        initializers.put(fn.getId(), new FunctionInitializer(fn));
                    }
                }
            }
            final String mainClassName = this.getFirstCompileUnit().getUnitClassName();
            this.installer.storeScript(cacheKey, this.source, mainClassName, this.bytecode, initializers, this.constantData.toArray(), this.compilationId);
        }
    }
    
    public static void updateCompilationId(final int value) {
        if (value >= Compiler.COMPILATION_ID.get()) {
            Compiler.COMPILATION_ID.set(value + 1);
        }
    }
    
    CompileUnit addCompileUnit(final long initialWeight) {
        final CompileUnit compileUnit = this.createCompileUnit(initialWeight);
        this.compileUnits.add(compileUnit);
        this.log.fine("Added compile unit ", compileUnit);
        return compileUnit;
    }
    
    CompileUnit createCompileUnit(final String unitClassName, final long initialWeight) {
        final ClassEmitter classEmitter = new ClassEmitter(this.context, this.sourceName, unitClassName, this.isStrict());
        final CompileUnit compileUnit = new CompileUnit(unitClassName, classEmitter, initialWeight);
        classEmitter.begin();
        return compileUnit;
    }
    
    private CompileUnit createCompileUnit(final long initialWeight) {
        return this.createCompileUnit(this.nextCompileUnitName(), initialWeight);
    }
    
    boolean isStrict() {
        return this.strict;
    }
    
    void replaceCompileUnits(final Set<CompileUnit> newUnits) {
        this.compileUnits.clear();
        this.compileUnits.addAll(newUnits);
    }
    
    CompileUnit findUnit(final long weight) {
        for (final CompileUnit unit : this.compileUnits) {
            if (unit.canHold(weight)) {
                unit.addWeight(weight);
                return unit;
            }
        }
        return this.addCompileUnit(weight);
    }
    
    public static String binaryName(final String name) {
        return name.replace('/', '.');
    }
    
    RecompilableScriptFunctionData getScriptFunctionData(final int functionId) {
        assert this.compiledFunction != null;
        final RecompilableScriptFunctionData fn = this.compiledFunction.getScriptFunctionData(functionId);
        assert fn != null : functionId;
        return fn;
    }
    
    boolean isGlobalSymbol(final FunctionNode fn, final String name) {
        return this.getScriptFunctionData(fn.getId()).isGlobalSymbol(fn, name);
    }
    
    int[] getContinuationEntryPoints() {
        return this.continuationEntryPoints;
    }
    
    Type getInvalidatedProgramPointType(final int programPoint) {
        return this.invalidatedProgramPoints.get(programPoint);
    }
    
    private void printMemoryUsage(final FunctionNode functionNode, final String phaseName) {
        if (!this.log.isEnabled()) {
            return;
        }
        this.log.info(phaseName, "finished. Doing IR size calculation...");
        final ObjectSizeCalculator osc = new ObjectSizeCalculator(ObjectSizeCalculator.getEffectiveMemoryLayoutSpecification());
        osc.calculateObjectSize(functionNode);
        final List<ClassHistogramElement> list = osc.getClassHistogram();
        final StringBuilder sb = new StringBuilder();
        final long totalSize = osc.calculateObjectSize(functionNode);
        sb.append(phaseName).append(" Total size = ").append(totalSize / 1024L / 1024L).append("MB");
        this.log.info(sb);
        Collections.sort(list, new Comparator<ClassHistogramElement>() {
            @Override
            public int compare(final ClassHistogramElement o1, final ClassHistogramElement o2) {
                final long diff = o1.getBytes() - o2.getBytes();
                if (diff < 0L) {
                    return 1;
                }
                if (diff > 0L) {
                    return -1;
                }
                return 0;
            }
        });
        for (final ClassHistogramElement e : list) {
            final String line = String.format("    %-48s %10d bytes (%8d instances)", e.getClazz(), e.getBytes(), e.getInstances());
            this.log.info(line);
            if (e.getBytes() < totalSize / 200L) {
                this.log.info("    ...");
                break;
            }
        }
    }
    
    static {
        Compiler.RESERVED_NAMES = new String[] { CompilerConstants.SCOPE.symbolName(), CompilerConstants.THIS.symbolName(), CompilerConstants.RETURN.symbolName(), CompilerConstants.CALLEE.symbolName(), CompilerConstants.VARARGS.symbolName(), CompilerConstants.ARGUMENTS.symbolName() };
        COMPILATION_ID = new AtomicInteger(0);
    }
    
    public static class CompilationPhases implements Iterable<CompilationPhase>
    {
        private static final CompilationPhases COMPILE_UPTO_CACHED;
        private static final CompilationPhases COMPILE_CACHED_UPTO_BYTECODE;
        public static final CompilationPhases RECOMPILE_CACHED_UPTO_BYTECODE;
        public static final CompilationPhases GENERATE_BYTECODE_AND_INSTALL;
        public static final CompilationPhases COMPILE_UPTO_BYTECODE;
        public static final CompilationPhases COMPILE_ALL_NO_INSTALL;
        public static final CompilationPhases COMPILE_ALL;
        public static final CompilationPhases COMPILE_ALL_CACHED;
        public static final CompilationPhases GENERATE_BYTECODE_AND_INSTALL_RESTOF;
        public static final CompilationPhases COMPILE_ALL_RESTOF;
        public static final CompilationPhases COMPILE_CACHED_RESTOF;
        private final List<CompilationPhase> phases;
        private final String desc;
        
        private CompilationPhases(final String desc, final CompilationPhase... phases) {
            this(desc, Arrays.asList(phases));
        }
        
        private CompilationPhases(final String desc, final CompilationPhases base, final CompilationPhase... phases) {
            this(desc, concat(base.phases, (List<CompilationPhase>)Arrays.asList((T[])phases)));
        }
        
        private CompilationPhases(final String desc, final CompilationPhase first, final CompilationPhases rest) {
            this(desc, concat((List<CompilationPhase>)Collections.singletonList((T)first), rest.phases));
        }
        
        private CompilationPhases(final String desc, final CompilationPhases base) {
            this(desc, base.phases);
        }
        
        private CompilationPhases(final String desc, final CompilationPhases... bases) {
            this(desc, concatPhases(bases));
        }
        
        private CompilationPhases(final String desc, final List<CompilationPhase> phases) {
            this.desc = desc;
            this.phases = phases;
        }
        
        private static List<CompilationPhase> concatPhases(final CompilationPhases[] bases) {
            final ArrayList<CompilationPhase> l = new ArrayList<CompilationPhase>();
            for (final CompilationPhases base : bases) {
                l.addAll(base.phases);
            }
            l.trimToSize();
            return l;
        }
        
        private static <T> List<T> concat(final List<T> l1, final List<T> l2) {
            final ArrayList<T> i = new ArrayList<T>((Collection<? extends T>)l1);
            i.addAll((Collection<? extends T>)l2);
            i.trimToSize();
            return i;
        }
        
        @Override
        public String toString() {
            return "'" + this.desc + "' " + this.phases.toString();
        }
        
        boolean contains(final CompilationPhase phase) {
            return this.phases.contains(phase);
        }
        
        @Override
        public Iterator<CompilationPhase> iterator() {
            return this.phases.iterator();
        }
        
        boolean isRestOfCompilation() {
            return this == CompilationPhases.COMPILE_ALL_RESTOF || this == CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF || this == CompilationPhases.COMPILE_CACHED_RESTOF;
        }
        
        String getDesc() {
            return this.desc;
        }
        
        String toString(final String prefix) {
            final StringBuilder sb = new StringBuilder();
            for (final CompilationPhase phase : this.phases) {
                sb.append(prefix).append(phase).append('\n');
            }
            return sb.toString();
        }
        
        static {
            COMPILE_UPTO_CACHED = new CompilationPhases("Common initial phases", new CompilationPhase[] { CompilationPhase.CONSTANT_FOLDING_PHASE, CompilationPhase.LOWERING_PHASE, CompilationPhase.APPLY_SPECIALIZATION_PHASE, CompilationPhase.SPLITTING_PHASE, CompilationPhase.PROGRAM_POINT_PHASE, CompilationPhase.SYMBOL_ASSIGNMENT_PHASE, CompilationPhase.SCOPE_DEPTH_COMPUTATION_PHASE, CompilationPhase.CACHE_AST_PHASE });
            COMPILE_CACHED_UPTO_BYTECODE = new CompilationPhases("After common phases, before bytecode generator", new CompilationPhase[] { CompilationPhase.OPTIMISTIC_TYPE_ASSIGNMENT_PHASE, CompilationPhase.LOCAL_VARIABLE_TYPE_CALCULATION_PHASE });
            RECOMPILE_CACHED_UPTO_BYTECODE = new CompilationPhases("Recompile cached function up to bytecode", CompilationPhase.REINITIALIZE_CACHED, CompilationPhases.COMPILE_CACHED_UPTO_BYTECODE);
            GENERATE_BYTECODE_AND_INSTALL = new CompilationPhases("Generate bytecode and install", new CompilationPhase[] { CompilationPhase.BYTECODE_GENERATION_PHASE, CompilationPhase.INSTALL_PHASE });
            COMPILE_UPTO_BYTECODE = new CompilationPhases("Compile upto bytecode", new CompilationPhases[] { CompilationPhases.COMPILE_UPTO_CACHED, CompilationPhases.COMPILE_CACHED_UPTO_BYTECODE });
            COMPILE_ALL_NO_INSTALL = new CompilationPhases("Compile without install", CompilationPhases.COMPILE_UPTO_BYTECODE, new CompilationPhase[] { CompilationPhase.BYTECODE_GENERATION_PHASE });
            COMPILE_ALL = new CompilationPhases("Full eager compilation", new CompilationPhases[] { CompilationPhases.COMPILE_UPTO_BYTECODE, CompilationPhases.GENERATE_BYTECODE_AND_INSTALL });
            COMPILE_ALL_CACHED = new CompilationPhases("Eager compilation from serializaed state", new CompilationPhases[] { CompilationPhases.RECOMPILE_CACHED_UPTO_BYTECODE, CompilationPhases.GENERATE_BYTECODE_AND_INSTALL });
            GENERATE_BYTECODE_AND_INSTALL_RESTOF = new CompilationPhases("Generate bytecode and install - RestOf method", CompilationPhase.REUSE_COMPILE_UNITS_PHASE, CompilationPhases.GENERATE_BYTECODE_AND_INSTALL);
            COMPILE_ALL_RESTOF = new CompilationPhases("Compile all, rest of", new CompilationPhases[] { CompilationPhases.COMPILE_UPTO_BYTECODE, CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF });
            COMPILE_CACHED_RESTOF = new CompilationPhases("Compile serialized, rest of", new CompilationPhases[] { CompilationPhases.RECOMPILE_CACHED_UPTO_BYTECODE, CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF });
        }
    }
}
