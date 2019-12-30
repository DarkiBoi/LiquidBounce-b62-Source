// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.CodeInstaller;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.FunctionNode;

abstract class CompilationPhase
{
    static final CompilationPhase CONSTANT_FOLDING_PHASE;
    static final CompilationPhase LOWERING_PHASE;
    static final CompilationPhase APPLY_SPECIALIZATION_PHASE;
    static final CompilationPhase SPLITTING_PHASE;
    static final CompilationPhase PROGRAM_POINT_PHASE;
    static final CompilationPhase CACHE_AST_PHASE;
    static final CompilationPhase SYMBOL_ASSIGNMENT_PHASE;
    static final CompilationPhase SCOPE_DEPTH_COMPUTATION_PHASE;
    static final CompilationPhase DECLARE_LOCAL_SYMBOLS_PHASE;
    static final CompilationPhase OPTIMISTIC_TYPE_ASSIGNMENT_PHASE;
    static final CompilationPhase LOCAL_VARIABLE_TYPE_CALCULATION_PHASE;
    static final CompilationPhase REUSE_COMPILE_UNITS_PHASE;
    static final CompilationPhase REINITIALIZE_CACHED;
    static final CompilationPhase BYTECODE_GENERATION_PHASE;
    static final CompilationPhase INSTALL_PHASE;
    private long startTime;
    private long endTime;
    private boolean isFinished;
    
    private CompilationPhase() {
    }
    
    protected FunctionNode begin(final Compiler compiler, final FunctionNode functionNode) {
        compiler.getLogger().indent();
        this.startTime = System.nanoTime();
        return functionNode;
    }
    
    protected FunctionNode end(final Compiler compiler, final FunctionNode functionNode) {
        compiler.getLogger().unindent();
        this.endTime = System.nanoTime();
        compiler.getScriptEnvironment()._timing.accumulateTime(this.toString(), this.endTime - this.startTime);
        this.isFinished = true;
        return functionNode;
    }
    
    boolean isFinished() {
        return this.isFinished;
    }
    
    long getStartTime() {
        return this.startTime;
    }
    
    long getEndTime() {
        return this.endTime;
    }
    
    abstract FunctionNode transform(final Compiler p0, final Compiler.CompilationPhases p1, final FunctionNode p2) throws CompilationException;
    
    final FunctionNode apply(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode functionNode) throws CompilationException {
        assert phases.contains(this);
        return this.end(compiler, this.transform(compiler, phases, this.begin(compiler, functionNode)));
    }
    
    private static FunctionNode transformFunction(final FunctionNode fn, final NodeVisitor<?> visitor) {
        return (FunctionNode)fn.accept((NodeVisitor)visitor);
    }
    
    private static CompileUnit createNewCompileUnit(final Compiler compiler, final Compiler.CompilationPhases phases) {
        final StringBuilder sb = new StringBuilder(compiler.nextCompileUnitName());
        if (phases.isRestOfCompilation()) {
            sb.append("$restOf");
        }
        return compiler.createCompileUnit(sb.toString(), 0L);
    }
    
    static {
        CONSTANT_FOLDING_PHASE = new ConstantFoldingPhase();
        LOWERING_PHASE = new LoweringPhase();
        APPLY_SPECIALIZATION_PHASE = new ApplySpecializationPhase();
        SPLITTING_PHASE = new SplittingPhase();
        PROGRAM_POINT_PHASE = new ProgramPointPhase();
        CACHE_AST_PHASE = new CacheAstPhase();
        SYMBOL_ASSIGNMENT_PHASE = new SymbolAssignmentPhase();
        SCOPE_DEPTH_COMPUTATION_PHASE = new ScopeDepthComputationPhase();
        DECLARE_LOCAL_SYMBOLS_PHASE = new DeclareLocalSymbolsPhase();
        OPTIMISTIC_TYPE_ASSIGNMENT_PHASE = new OptimisticTypeAssignmentPhase();
        LOCAL_VARIABLE_TYPE_CALCULATION_PHASE = new LocalVariableTypeCalculationPhase();
        REUSE_COMPILE_UNITS_PHASE = new ReuseCompileUnitsPhase();
        REINITIALIZE_CACHED = new ReinitializeCachedPhase();
        BYTECODE_GENERATION_PHASE = new BytecodeGenerationPhase();
        INSTALL_PHASE = new InstallPhase();
    }
    
    private static final class ConstantFoldingPhase extends CompilationPhase
    {
        private ConstantFoldingPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new FoldConstants(compiler));
        }
        
        @Override
        public String toString() {
            return "'Constant Folding'";
        }
    }
    
    private static final class LoweringPhase extends CompilationPhase
    {
        private LoweringPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new Lower(compiler));
        }
        
        @Override
        public String toString() {
            return "'Control Flow Lowering'";
        }
    }
    
    private static final class ApplySpecializationPhase extends CompilationPhase
    {
        private ApplySpecializationPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new ApplySpecialization(compiler));
        }
        
        @Override
        public String toString() {
            return "'Builtin Replacement'";
        }
    }
    
    private static final class SplittingPhase extends CompilationPhase
    {
        private SplittingPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            final CompileUnit outermostCompileUnit = compiler.addCompileUnit(0L);
            FunctionNode newFunctionNode = transformFunction(fn, new SimpleNodeVisitor() {
                @Override
                public LiteralNode<?> leaveLiteralNode(final LiteralNode<?> literalNode) {
                    return literalNode.initialize(this.lc);
                }
            });
            newFunctionNode = new Splitter(compiler, newFunctionNode, outermostCompileUnit).split(newFunctionNode, true);
            newFunctionNode = transformFunction(newFunctionNode, new SplitIntoFunctions(compiler));
            assert newFunctionNode.getCompileUnit() == outermostCompileUnit : "fn=" + fn.getName() + ", fn.compileUnit (" + newFunctionNode.getCompileUnit() + ") != " + outermostCompileUnit;
            assert newFunctionNode.isStrict() == compiler.isStrict() : "functionNode.isStrict() != compiler.isStrict() for " + DebugLogger.quote(newFunctionNode.getName());
            return newFunctionNode;
        }
        
        @Override
        public String toString() {
            return "'Code Splitting'";
        }
    }
    
    private static final class ProgramPointPhase extends CompilationPhase
    {
        private ProgramPointPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new ProgramPoints());
        }
        
        @Override
        public String toString() {
            return "'Program Point Calculation'";
        }
    }
    
    private static final class CacheAstPhase extends CompilationPhase
    {
        private CacheAstPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            if (!compiler.isOnDemandCompilation()) {
                transformFunction(fn, new CacheAst(compiler));
            }
            return fn;
        }
        
        @Override
        public String toString() {
            return "'Cache ASTs'";
        }
    }
    
    private static final class SymbolAssignmentPhase extends CompilationPhase
    {
        private SymbolAssignmentPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new AssignSymbols(compiler));
        }
        
        @Override
        public String toString() {
            return "'Symbol Assignment'";
        }
    }
    
    private static final class ScopeDepthComputationPhase extends CompilationPhase
    {
        private ScopeDepthComputationPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            return transformFunction(fn, new FindScopeDepths(compiler));
        }
        
        @Override
        public String toString() {
            return "'Scope Depth Computation'";
        }
    }
    
    private static final class DeclareLocalSymbolsPhase extends CompilationPhase
    {
        private DeclareLocalSymbolsPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            if (compiler.useOptimisticTypes() && compiler.isOnDemandCompilation()) {
                fn.getBody().accept(new SimpleNodeVisitor() {
                    @Override
                    public boolean enterFunctionNode(final FunctionNode functionNode) {
                        return false;
                    }
                    
                    @Override
                    public boolean enterBlock(final Block block) {
                        for (final Symbol symbol : block.getSymbols()) {
                            if (!symbol.isScope()) {
                                compiler.declareLocalSymbol(symbol.getName());
                            }
                        }
                        return true;
                    }
                });
            }
            return fn;
        }
        
        @Override
        public String toString() {
            return "'Local Symbols Declaration'";
        }
    }
    
    private static final class OptimisticTypeAssignmentPhase extends CompilationPhase
    {
        private OptimisticTypeAssignmentPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            if (compiler.useOptimisticTypes()) {
                return transformFunction(fn, new OptimisticTypesCalculator(compiler));
            }
            return fn;
        }
        
        @Override
        public String toString() {
            return "'Optimistic Type Assignment'";
        }
    }
    
    private static final class LocalVariableTypeCalculationPhase extends CompilationPhase
    {
        private LocalVariableTypeCalculationPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            final FunctionNode newFunctionNode = transformFunction(fn, new LocalVariableTypesCalculator(compiler));
            final ScriptEnvironment senv = compiler.getScriptEnvironment();
            final PrintWriter err = senv.getErr();
            if (senv._print_lower_ast || fn.getFlag(1048576)) {
                err.println("Lower AST for: " + DebugLogger.quote(newFunctionNode.getName()));
                err.println(new ASTWriter(newFunctionNode));
            }
            if (senv._print_lower_parse || fn.getFlag(262144)) {
                err.println("Lower AST for: " + DebugLogger.quote(newFunctionNode.getName()));
                err.println(new PrintVisitor(newFunctionNode));
            }
            return newFunctionNode;
        }
        
        @Override
        public String toString() {
            return "'Local Variable Type Calculation'";
        }
    }
    
    private static final class ReuseCompileUnitsPhase extends CompilationPhase
    {
        private ReuseCompileUnitsPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            assert phases.isRestOfCompilation() : "reuse compile units currently only used for Rest-Of methods";
            final Map<CompileUnit, CompileUnit> map = new HashMap<CompileUnit, CompileUnit>();
            final Set<CompileUnit> newUnits = CompileUnit.createCompileUnitSet();
            final DebugLogger log = compiler.getLogger();
            log.fine("Clearing bytecode cache");
            compiler.clearBytecode();
            for (final CompileUnit oldUnit : compiler.getCompileUnits()) {
                assert map.get(oldUnit) == null;
                final CompileUnit newUnit = createNewCompileUnit(compiler, phases);
                log.fine("Creating new compile unit ", oldUnit, " => ", newUnit);
                map.put(oldUnit, newUnit);
                assert newUnit != null;
                newUnits.add(newUnit);
            }
            log.fine("Replacing compile units in Compiler...");
            compiler.replaceCompileUnits(newUnits);
            log.fine("Done");
            final FunctionNode newFunctionNode = transformFunction(fn, new ReplaceCompileUnits() {
                @Override
                CompileUnit getReplacement(final CompileUnit original) {
                    return map.get(original);
                }
                
                public Node leaveDefault(final Node node) {
                    return node.ensureUniqueLabels(this.lc);
                }
            });
            return newFunctionNode;
        }
        
        @Override
        public String toString() {
            return "'Reuse Compile Units'";
        }
    }
    
    private static final class ReinitializeCachedPhase extends CompilationPhase
    {
        private ReinitializeCachedPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            final Set<CompileUnit> unitSet = CompileUnit.createCompileUnitSet();
            final Map<CompileUnit, CompileUnit> unitMap = new HashMap<CompileUnit, CompileUnit>();
            this.createCompileUnit(fn.getCompileUnit(), unitSet, unitMap, compiler, phases);
            final FunctionNode newFn = transformFunction(fn, new ReplaceCompileUnits() {
                @Override
                CompileUnit getReplacement(final CompileUnit oldUnit) {
                    final CompileUnit existing = unitMap.get(oldUnit);
                    if (existing != null) {
                        return existing;
                    }
                    return ReinitializeCachedPhase.this.createCompileUnit(oldUnit, unitSet, unitMap, compiler, phases);
                }
                
                @Override
                public Node leaveFunctionNode(final FunctionNode fn2) {
                    return super.leaveFunctionNode(compiler.getScriptFunctionData(fn2.getId()).restoreFlags(this.lc, fn2));
                }
            });
            compiler.replaceCompileUnits(unitSet);
            return newFn;
        }
        
        private CompileUnit createCompileUnit(final CompileUnit oldUnit, final Set<CompileUnit> unitSet, final Map<CompileUnit, CompileUnit> unitMap, final Compiler compiler, final Compiler.CompilationPhases phases) {
            final CompileUnit newUnit = createNewCompileUnit(compiler, phases);
            unitMap.put(oldUnit, newUnit);
            unitSet.add(newUnit);
            return newUnit;
        }
        
        @Override
        public String toString() {
            return "'Reinitialize cached'";
        }
    }
    
    private static final class BytecodeGenerationPhase extends CompilationPhase
    {
        private BytecodeGenerationPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            final ScriptEnvironment senv = compiler.getScriptEnvironment();
            FunctionNode newFunctionNode = fn;
            fn.getCompileUnit().setUsed();
            compiler.getLogger().fine("Starting bytecode generation for ", DebugLogger.quote(fn.getName()), " - restOf=", phases.isRestOfCompilation());
            final CodeGenerator codegen = new CodeGenerator(compiler, (int[])(phases.isRestOfCompilation() ? compiler.getContinuationEntryPoints() : null));
            try {
                newFunctionNode = transformFunction(newFunctionNode, codegen);
                codegen.generateScopeCalls();
            }
            catch (VerifyError e) {
                if (!senv._verify_code && !senv._print_code) {
                    throw e;
                }
                senv.getErr().println(e.getClass().getSimpleName() + ": " + e.getMessage());
                if (senv._dump_on_error) {
                    e.printStackTrace(senv.getErr());
                }
            }
            catch (Throwable e2) {
                throw new AssertionError("Failed generating bytecode for " + fn.getSourceName() + ":" + codegen.getLastLineNumber(), e2);
            }
            for (final CompileUnit compileUnit : compiler.getCompileUnits()) {
                final ClassEmitter classEmitter = compileUnit.getClassEmitter();
                classEmitter.end();
                if (!compileUnit.isUsed()) {
                    compiler.getLogger().fine("Skipping unused compile unit ", compileUnit);
                }
                else {
                    final byte[] bytecode = classEmitter.toByteArray();
                    assert bytecode != null;
                    final String className = compileUnit.getUnitClassName();
                    compiler.addClass(className, bytecode);
                    CompileUnit.increaseEmitCount();
                    if (senv._verify_code) {
                        compiler.getCodeInstaller().verify(bytecode);
                    }
                    DumpBytecode.dumpBytecode(senv, compiler.getLogger(), bytecode, className);
                }
            }
            return newFunctionNode;
        }
        
        @Override
        public String toString() {
            return "'Bytecode Generation'";
        }
    }
    
    private static final class InstallPhase extends CompilationPhase
    {
        private InstallPhase() {
            super(null);
        }
        
        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, final FunctionNode fn) {
            final DebugLogger log = compiler.getLogger();
            final Map<String, Class<?>> installedClasses = new LinkedHashMap<String, Class<?>>();
            boolean first = true;
            Class<?> rootClass = null;
            long length = 0L;
            final CodeInstaller codeInstaller = compiler.getCodeInstaller();
            final Map<String, byte[]> bytecode = compiler.getBytecode();
            for (final Map.Entry<String, byte[]> entry : bytecode.entrySet()) {
                final String className = entry.getKey();
                final byte[] code = entry.getValue();
                length += code.length;
                final Class<?> clazz = codeInstaller.install(className, code);
                if (first) {
                    rootClass = clazz;
                    first = false;
                }
                installedClasses.put(className, clazz);
            }
            if (rootClass == null) {
                throw new CompilationException("Internal compiler error: root class not found!");
            }
            final Object[] constants = compiler.getConstantData().toArray();
            codeInstaller.initialize(installedClasses.values(), compiler.getSource(), constants);
            for (final Object constant : constants) {
                if (constant instanceof RecompilableScriptFunctionData) {
                    ((RecompilableScriptFunctionData)constant).initTransients(compiler.getSource(), codeInstaller);
                }
            }
            for (final CompileUnit unit : compiler.getCompileUnits()) {
                if (!unit.isUsed()) {
                    continue;
                }
                unit.setCode(installedClasses.get(unit.getUnitClassName()));
                unit.initializeFunctionsCode();
            }
            if (log.isEnabled()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Installed class '").append(rootClass.getSimpleName()).append('\'').append(" [").append(rootClass.getName()).append(", size=").append(length).append(" bytes, ").append(compiler.getCompileUnits().size()).append(" compile unit(s)]");
                log.fine(sb.toString());
            }
            return fn.setRootClass(null, rootClass);
        }
        
        @Override
        public String toString() {
            return "'Class Installation'";
        }
    }
}
