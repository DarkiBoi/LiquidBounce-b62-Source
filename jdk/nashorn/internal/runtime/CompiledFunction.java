// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.TreeMap;
import jdk.nashorn.internal.codegen.TypeMap;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;
import jdk.nashorn.internal.runtime.events.RecompilationEvent;
import java.util.logging.Level;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.SwitchPoint;
import java.util.function.Supplier;
import java.lang.invoke.CallSite;
import java.lang.invoke.MutableCallSite;
import jdk.nashorn.internal.codegen.types.ArrayType;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.Map;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodType;
import java.util.Collection;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.lang.invoke.MethodHandle;

final class CompiledFunction
{
    private static final MethodHandle NEWFILTER;
    private static final MethodHandle RELINK_COMPOSABLE_INVOKER;
    private static final MethodHandle HANDLE_REWRITE_EXCEPTION;
    private static final MethodHandle RESTOF_INVOKER;
    private final DebugLogger log;
    static final Collection<CompiledFunction> NO_FUNCTIONS;
    private MethodHandle invoker;
    private MethodHandle constructor;
    private OptimismInfo optimismInfo;
    private final int flags;
    private final MethodType callSiteType;
    private final Specialization specialization;
    
    CompiledFunction(final MethodHandle invoker) {
        this(invoker, null, null);
    }
    
    static CompiledFunction createBuiltInConstructor(final MethodHandle invoker, final Specialization specialization) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), specialization);
    }
    
    CompiledFunction(final MethodHandle invoker, final MethodHandle constructor, final Specialization specialization) {
        this(invoker, constructor, 0, null, specialization, DebugLogger.DISABLED_LOGGER);
    }
    
    CompiledFunction(final MethodHandle invoker, final MethodHandle constructor, final int flags, final MethodType callSiteType, final Specialization specialization, final DebugLogger log) {
        this.specialization = specialization;
        if (specialization != null && specialization.isOptimistic()) {
            this.invoker = Lookup.MH.insertArguments(invoker, invoker.type().parameterCount() - 1, 1);
            throw new AssertionError((Object)"Optimistic (UnwarrantedOptimismException throwing) builtin functions are currently not in use");
        }
        this.invoker = invoker;
        this.constructor = constructor;
        this.flags = flags;
        this.callSiteType = callSiteType;
        this.log = log;
    }
    
    CompiledFunction(final MethodHandle invoker, final RecompilableScriptFunctionData functionData, final Map<Integer, Type> invalidatedProgramPoints, final MethodType callSiteType, final int flags) {
        this(invoker, null, flags, callSiteType, null, functionData.getLogger());
        if ((flags & 0x800) != 0x0) {
            this.optimismInfo = new OptimismInfo(functionData, invalidatedProgramPoints);
        }
        else {
            this.optimismInfo = null;
        }
    }
    
    static CompiledFunction createBuiltInConstructor(final MethodHandle invoker) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), null);
    }
    
    boolean isSpecialization() {
        return this.specialization != null;
    }
    
    boolean hasLinkLogic() {
        return this.getLinkLogicClass() != null;
    }
    
    Class<? extends SpecializedFunction.LinkLogic> getLinkLogicClass() {
        if (!this.isSpecialization()) {
            return null;
        }
        final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = this.specialization.getLinkLogicClass();
        assert !SpecializedFunction.LinkLogic.isEmpty(linkLogicClass) : "empty link logic classes should have been removed by nasgen";
        return linkLogicClass;
    }
    
    int getFlags() {
        return this.flags;
    }
    
    boolean isOptimistic() {
        return this.isSpecialization() && this.specialization.isOptimistic();
    }
    
    boolean isApplyToCall() {
        return (this.flags & 0x1000) != 0x0;
    }
    
    boolean isVarArg() {
        return isVarArgsType(this.invoker.type());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = this.getLinkLogicClass();
        sb.append("[invokerType=").append(this.invoker.type()).append(" ctor=").append(this.constructor).append(" weight=").append(this.weight()).append(" linkLogic=").append((linkLogicClass != null) ? linkLogicClass.getSimpleName() : "none");
        return sb.toString();
    }
    
    boolean needsCallee() {
        return ScriptFunctionData.needsCallee(this.invoker);
    }
    
    MethodHandle createComposableInvoker() {
        return this.createComposableInvoker(false);
    }
    
    private MethodHandle getConstructor() {
        if (this.constructor == null) {
            this.constructor = createConstructorFromInvoker(this.createInvokerForPessimisticCaller());
        }
        return this.constructor;
    }
    
    private MethodHandle createInvokerForPessimisticCaller() {
        return this.createInvoker(Object.class, -1);
    }
    
    private static MethodHandle createConstructorFromInvoker(final MethodHandle invoker) {
        final boolean needsCallee = ScriptFunctionData.needsCallee(invoker);
        final MethodHandle swapped = needsCallee ? swapCalleeAndThis(invoker) : invoker;
        final MethodHandle returnsObject = Lookup.MH.asType(swapped, swapped.type().changeReturnType((Class<?>)Object.class));
        final MethodType ctorType = returnsObject.type();
        final Class<?>[] ctorArgs = ctorType.dropParameterTypes(0, 1).parameterArray();
        final MethodHandle filtered = Lookup.MH.foldArguments(Lookup.MH.dropArguments(CompiledFunction.NEWFILTER, 2, ctorArgs), returnsObject);
        if (needsCallee) {
            return Lookup.MH.foldArguments(filtered, ScriptFunction.ALLOCATE);
        }
        return Lookup.MH.filterArguments(filtered, 0, ScriptFunction.ALLOCATE);
    }
    
    private static MethodHandle swapCalleeAndThis(final MethodHandle mh) {
        final MethodType type = mh.type();
        assert type.parameterType(0) == ScriptFunction.class : type;
        assert type.parameterType(1) == Object.class : type;
        final MethodType newType = type.changeParameterType(0, (Class<?>)Object.class).changeParameterType(1, (Class<?>)ScriptFunction.class);
        final int[] reorder = new int[type.parameterCount()];
        reorder[0] = 1;
        assert reorder[1] == 0;
        for (int i = 2; i < reorder.length; ++i) {
            reorder[i] = i;
        }
        return MethodHandles.permuteArguments(mh, newType, reorder);
    }
    
    MethodHandle createComposableConstructor() {
        return this.createComposableInvoker(true);
    }
    
    boolean hasConstructor() {
        return this.constructor != null;
    }
    
    MethodType type() {
        return this.invoker.type();
    }
    
    int weight() {
        return weight(this.type());
    }
    
    private static int weight(final MethodType type) {
        if (isVarArgsType(type)) {
            return Integer.MAX_VALUE;
        }
        int weight = Type.typeFor(type.returnType()).getWeight();
        for (int i = 0; i < type.parameterCount(); ++i) {
            final Class<?> paramType = type.parameterType(i);
            final int pweight = Type.typeFor(paramType).getWeight() * 2;
            weight += pweight;
        }
        weight += type.parameterCount();
        return weight;
    }
    
    static boolean isVarArgsType(final MethodType type) {
        assert type.parameterCount() >= 1 : type;
        return type.parameterType(type.parameterCount() - 1) == Object[].class;
    }
    
    static boolean moreGenericThan(final MethodType mt0, final MethodType mt1) {
        return weight(mt0) > weight(mt1);
    }
    
    boolean betterThanFinal(final CompiledFunction other, final MethodType callSiteMethodType) {
        return other == null || betterThanFinal(this, other, callSiteMethodType);
    }
    
    private static boolean betterThanFinal(final CompiledFunction cf, final CompiledFunction other, final MethodType callSiteMethodType) {
        final MethodType thisMethodType = cf.type();
        final MethodType otherMethodType = other.type();
        final int thisParamCount = getParamCount(thisMethodType);
        final int otherParamCount = getParamCount(otherMethodType);
        final int callSiteRawParamCount = getParamCount(callSiteMethodType);
        final boolean csVarArg = callSiteRawParamCount == Integer.MAX_VALUE;
        final int callSiteParamCount = csVarArg ? callSiteRawParamCount : (callSiteRawParamCount - 1);
        final int thisDiscardsParams = Math.max(callSiteParamCount - thisParamCount, 0);
        final int otherDiscardsParams = Math.max(callSiteParamCount - otherParamCount, 0);
        if (thisDiscardsParams < otherDiscardsParams) {
            return true;
        }
        if (thisDiscardsParams > otherDiscardsParams) {
            return false;
        }
        final boolean thisVarArg = thisParamCount == Integer.MAX_VALUE;
        final boolean otherVarArg = otherParamCount == Integer.MAX_VALUE;
        if (!thisVarArg || !otherVarArg || !csVarArg) {
            final Type[] thisType = toTypeWithoutCallee(thisMethodType, 0);
            final Type[] otherType = toTypeWithoutCallee(otherMethodType, 0);
            final Type[] callSiteType = toTypeWithoutCallee(callSiteMethodType, 1);
            int narrowWeightDelta = 0;
            int widenWeightDelta = 0;
            for (int minParamsCount = Math.min(Math.min(thisParamCount, otherParamCount), callSiteParamCount), i = 0; i < minParamsCount; ++i) {
                final int callSiteParamWeight = getParamType(i, callSiteType, csVarArg).getWeight();
                final int thisParamWeightDelta = getParamType(i, thisType, thisVarArg).getWeight() - callSiteParamWeight;
                final int otherParamWeightDelta = getParamType(i, otherType, otherVarArg).getWeight() - callSiteParamWeight;
                narrowWeightDelta += Math.max(-thisParamWeightDelta, 0) - Math.max(-otherParamWeightDelta, 0);
                widenWeightDelta += Math.max(thisParamWeightDelta, 0) - Math.max(otherParamWeightDelta, 0);
            }
            if (!thisVarArg) {
                for (int i = callSiteParamCount; i < thisParamCount; ++i) {
                    narrowWeightDelta += Math.max(Type.OBJECT.getWeight() - thisType[i].getWeight(), 0);
                }
            }
            if (!otherVarArg) {
                for (int i = callSiteParamCount; i < otherParamCount; ++i) {
                    narrowWeightDelta -= Math.max(Type.OBJECT.getWeight() - otherType[i].getWeight(), 0);
                }
            }
            if (narrowWeightDelta < 0) {
                return true;
            }
            if (narrowWeightDelta > 0) {
                return false;
            }
            if (widenWeightDelta < 0) {
                return true;
            }
            if (widenWeightDelta > 0) {
                return false;
            }
        }
        if (thisParamCount == callSiteParamCount && otherParamCount != callSiteParamCount) {
            return true;
        }
        if (thisParamCount != callSiteParamCount && otherParamCount == callSiteParamCount) {
            return false;
        }
        if (thisVarArg) {
            if (!otherVarArg) {
                return true;
            }
        }
        else if (otherVarArg) {
            return false;
        }
        final int fnParamDelta = thisParamCount - otherParamCount;
        if (fnParamDelta < 0) {
            return true;
        }
        if (fnParamDelta > 0) {
            return false;
        }
        final int callSiteRetWeight = Type.typeFor(callSiteMethodType.returnType()).getWeight();
        final int thisRetWeightDelta = Type.typeFor(thisMethodType.returnType()).getWeight() - callSiteRetWeight;
        final int otherRetWeightDelta = Type.typeFor(otherMethodType.returnType()).getWeight() - callSiteRetWeight;
        final int widenRetDelta = Math.max(thisRetWeightDelta, 0) - Math.max(otherRetWeightDelta, 0);
        if (widenRetDelta < 0) {
            return true;
        }
        if (widenRetDelta > 0) {
            return false;
        }
        final int narrowRetDelta = Math.max(-thisRetWeightDelta, 0) - Math.max(-otherRetWeightDelta, 0);
        if (narrowRetDelta < 0) {
            return true;
        }
        if (narrowRetDelta > 0) {
            return false;
        }
        if (cf.isSpecialization() != other.isSpecialization()) {
            return cf.isSpecialization();
        }
        if (cf.isSpecialization() && other.isSpecialization()) {
            return cf.getLinkLogicClass() != null;
        }
        throw new AssertionError((Object)(thisMethodType + " identically applicable to " + otherMethodType + " for " + callSiteMethodType));
    }
    
    private static Type[] toTypeWithoutCallee(final MethodType type, final int thisIndex) {
        final int paramCount = type.parameterCount();
        final Type[] t = new Type[paramCount - thisIndex];
        for (int i = thisIndex; i < paramCount; ++i) {
            t[i - thisIndex] = Type.typeFor(type.parameterType(i));
        }
        return t;
    }
    
    private static Type getParamType(final int i, final Type[] paramTypes, final boolean isVarArg) {
        final int fixParamCount = paramTypes.length - (isVarArg ? 1 : 0);
        if (i < fixParamCount) {
            return paramTypes[i];
        }
        assert isVarArg;
        return ((ArrayType)paramTypes[paramTypes.length - 1]).getElementType();
    }
    
    boolean matchesCallSite(final MethodType other, final boolean pickVarArg) {
        if (other.equals((Object)this.callSiteType)) {
            return true;
        }
        final MethodType type = this.type();
        final int fnParamCount = getParamCount(type);
        final boolean isVarArg = fnParamCount == Integer.MAX_VALUE;
        if (isVarArg) {
            return pickVarArg;
        }
        final int csParamCount = getParamCount(other);
        final boolean csIsVarArg = csParamCount == Integer.MAX_VALUE;
        if (csIsVarArg && this.isApplyToCall()) {
            return false;
        }
        final int thisThisIndex = this.needsCallee() ? 1 : 0;
        final int fnParamCountNoCallee = fnParamCount - thisThisIndex;
        final int minParams = Math.min(csParamCount - 1, fnParamCountNoCallee);
        for (int i = 0; i < minParams; ++i) {
            final Type fnType = Type.typeFor(type.parameterType(i + thisThisIndex));
            final Type csType = csIsVarArg ? Type.OBJECT : Type.typeFor(other.parameterType(i + 1));
            if (!fnType.isEquivalentTo(csType)) {
                return false;
            }
        }
        for (int i = minParams; i < fnParamCountNoCallee; ++i) {
            if (!Type.typeFor(type.parameterType(i + thisThisIndex)).isEquivalentTo(Type.OBJECT)) {
                return false;
            }
        }
        return true;
    }
    
    private static int getParamCount(final MethodType type) {
        final int paramCount = type.parameterCount();
        return type.parameterType(paramCount - 1).isArray() ? Integer.MAX_VALUE : paramCount;
    }
    
    private boolean canBeDeoptimized() {
        return this.optimismInfo != null;
    }
    
    private MethodHandle createComposableInvoker(final boolean isConstructor) {
        final MethodHandle handle = this.getInvokerOrConstructor(isConstructor);
        if (!this.canBeDeoptimized()) {
            return handle;
        }
        final CallSite cs = new MutableCallSite(handle.type());
        relinkComposableInvoker(cs, this, isConstructor);
        return cs.dynamicInvoker();
    }
    
    private synchronized HandleAndAssumptions getValidOptimisticInvocation(final Supplier<MethodHandle> invocationSupplier) {
        MethodHandle handle;
        SwitchPoint assumptions;
        while (true) {
            handle = invocationSupplier.get();
            assumptions = (this.canBeDeoptimized() ? this.optimismInfo.optimisticAssumptions : null);
            if (assumptions == null || !assumptions.hasBeenInvalidated()) {
                break;
            }
            try {
                this.wait();
            }
            catch (InterruptedException ex) {}
        }
        return new HandleAndAssumptions(handle, assumptions);
    }
    
    private static void relinkComposableInvoker(final CallSite cs, final CompiledFunction inv, final boolean constructor) {
        final HandleAndAssumptions handleAndAssumptions = inv.getValidOptimisticInvocation(new Supplier<MethodHandle>() {
            @Override
            public MethodHandle get() {
                return CompiledFunction.this.getInvokerOrConstructor(constructor);
            }
        });
        final MethodHandle handle = handleAndAssumptions.handle;
        final SwitchPoint assumptions = handleAndAssumptions.assumptions;
        MethodHandle target;
        if (assumptions == null) {
            target = handle;
        }
        else {
            final MethodHandle relink = MethodHandles.insertArguments(CompiledFunction.RELINK_COMPOSABLE_INVOKER, 0, cs, inv, constructor);
            target = assumptions.guardWithTest(handle, MethodHandles.foldArguments(cs.dynamicInvoker(), relink));
        }
        cs.setTarget(target.asType(cs.type()));
    }
    
    private MethodHandle getInvokerOrConstructor(final boolean selectCtor) {
        return selectCtor ? this.getConstructor() : this.createInvokerForPessimisticCaller();
    }
    
    GuardedInvocation createFunctionInvocation(final Class<?> callSiteReturnType, final int callerProgramPoint) {
        return this.getValidOptimisticInvocation(new Supplier<MethodHandle>() {
            @Override
            public MethodHandle get() {
                return CompiledFunction.this.createInvoker(callSiteReturnType, callerProgramPoint);
            }
        }).createInvocation();
    }
    
    GuardedInvocation createConstructorInvocation() {
        return this.getValidOptimisticInvocation(new Supplier<MethodHandle>() {
            @Override
            public MethodHandle get() {
                return CompiledFunction.this.getConstructor();
            }
        }).createInvocation();
    }
    
    private MethodHandle createInvoker(final Class<?> callSiteReturnType, final int callerProgramPoint) {
        final boolean isOptimistic = this.canBeDeoptimized();
        MethodHandle handleRewriteException = isOptimistic ? this.createRewriteExceptionHandler() : null;
        MethodHandle inv = this.invoker;
        if (UnwarrantedOptimismException.isValid(callerProgramPoint)) {
            inv = OptimisticReturnFilters.filterOptimisticReturnValue(inv, callSiteReturnType, callerProgramPoint);
            inv = changeReturnType(inv, callSiteReturnType);
            if (callSiteReturnType.isPrimitive() && handleRewriteException != null) {
                handleRewriteException = OptimisticReturnFilters.filterOptimisticReturnValue(handleRewriteException, callSiteReturnType, callerProgramPoint);
            }
        }
        else if (isOptimistic) {
            inv = changeReturnType(inv, callSiteReturnType);
        }
        if (!isOptimistic) {
            return inv;
        }
        assert handleRewriteException != null;
        final MethodHandle typedHandleRewriteException = changeReturnType(handleRewriteException, inv.type().returnType());
        return Lookup.MH.catchException(inv, RewriteException.class, typedHandleRewriteException);
    }
    
    private MethodHandle createRewriteExceptionHandler() {
        return Lookup.MH.foldArguments(CompiledFunction.RESTOF_INVOKER, Lookup.MH.insertArguments(CompiledFunction.HANDLE_REWRITE_EXCEPTION, 0, this, this.optimismInfo));
    }
    
    private static MethodHandle changeReturnType(final MethodHandle mh, final Class<?> newReturnType) {
        return Bootstrap.getLinkerServices().asType(mh, mh.type().changeReturnType(newReturnType));
    }
    
    private static MethodHandle handleRewriteException(final CompiledFunction function, final OptimismInfo oldOptimismInfo, final RewriteException re) {
        return function.handleRewriteException(oldOptimismInfo, re);
    }
    
    private static List<String> toStringInvalidations(final Map<Integer, Type> ipp) {
        if (ipp == null) {
            return Collections.emptyList();
        }
        final List<String> list = new ArrayList<String>();
        for (final Map.Entry<Integer, Type> entry : ipp.entrySet()) {
            final char bct = entry.getValue().getBytecodeStackType();
            String type = null;
            switch (entry.getValue().getBytecodeStackType()) {
                case 'A': {
                    type = "object";
                    break;
                }
                case 'I': {
                    type = "int";
                    break;
                }
                case 'J': {
                    type = "long";
                    break;
                }
                case 'D': {
                    type = "double";
                    break;
                }
                default: {
                    type = String.valueOf(bct);
                    break;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append('[').append("program point: ").append(entry.getKey()).append(" -> ").append(type).append(']');
            list.add(sb.toString());
        }
        return list;
    }
    
    private void logRecompile(final String reason, final FunctionNode fn, final MethodType type, final Map<Integer, Type> ipp) {
        if (this.log.isEnabled()) {
            this.log.info(reason, DebugLogger.quote(fn.getName()), " signature: ", type);
            this.log.indent();
            for (final String str : toStringInvalidations(ipp)) {
                this.log.fine(str);
            }
            this.log.unindent();
        }
    }
    
    private synchronized MethodHandle handleRewriteException(final OptimismInfo oldOptInfo, final RewriteException re) {
        if (this.log.isEnabled()) {
            this.log.info(new RecompilationEvent(Level.INFO, re, re.getReturnValueNonDestructive()), "caught RewriteException ", re.getMessageShort());
            this.log.indent();
        }
        final MethodType type = this.type();
        final MethodType ct = (type.parameterType(0) == ScriptFunction.class) ? type : type.insertParameterTypes(0, (Class<?>[])new Class[] { ScriptFunction.class });
        final OptimismInfo currentOptInfo = this.optimismInfo;
        final boolean shouldRecompile = currentOptInfo != null && currentOptInfo.requestRecompile(re);
        final OptimismInfo effectiveOptInfo = (currentOptInfo != null) ? currentOptInfo : oldOptInfo;
        FunctionNode fn = effectiveOptInfo.reparse();
        final boolean cached = fn.isCached();
        final Compiler compiler = effectiveOptInfo.getCompiler(fn, ct, re);
        if (!shouldRecompile) {
            this.logRecompile("Rest-of compilation [STANDALONE] ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
            return this.restOfHandle(effectiveOptInfo, compiler.compile(fn, cached ? Compiler.CompilationPhases.COMPILE_CACHED_RESTOF : Compiler.CompilationPhases.COMPILE_ALL_RESTOF), currentOptInfo != null);
        }
        this.logRecompile("Deoptimizing recompilation (up to bytecode) ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
        fn = compiler.compile(fn, cached ? Compiler.CompilationPhases.RECOMPILE_CACHED_UPTO_BYTECODE : Compiler.CompilationPhases.COMPILE_UPTO_BYTECODE);
        this.log.fine("Reusable IR generated");
        this.log.info("Generating and installing bytecode from reusable IR...");
        this.logRecompile("Rest-of compilation [CODE PIPELINE REUSE] ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
        final FunctionNode normalFn = compiler.compile(fn, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL);
        if (effectiveOptInfo.data.usePersistentCodeCache()) {
            final RecompilableScriptFunctionData data = effectiveOptInfo.data;
            final int functionNodeId = data.getFunctionNodeId();
            final TypeMap typeMap = data.typeMap(ct);
            final Type[] paramTypes = (Type[])((typeMap == null) ? null : typeMap.getParameterTypes(functionNodeId));
            final String cacheKey = CodeStore.getCacheKey(functionNodeId, paramTypes);
            compiler.persistClassInfo(cacheKey, normalFn);
        }
        final boolean canBeDeoptimized = normalFn.canBeDeoptimized();
        if (this.log.isEnabled()) {
            this.log.unindent();
            this.log.info("Done.");
            this.log.info("Recompiled '", fn.getName(), "' (", Debug.id(this), ") ", canBeDeoptimized ? "can still be deoptimized." : " is completely deoptimized.");
            this.log.finest("Looking up invoker...");
        }
        final MethodHandle newInvoker = effectiveOptInfo.data.lookup(fn);
        this.invoker = newInvoker.asType(type.changeReturnType(newInvoker.type().returnType()));
        this.constructor = null;
        this.log.info("Done: ", this.invoker);
        final MethodHandle restOf = this.restOfHandle(effectiveOptInfo, compiler.compile(fn, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF), canBeDeoptimized);
        if (canBeDeoptimized) {
            effectiveOptInfo.newOptimisticAssumptions();
        }
        else {
            this.optimismInfo = null;
        }
        this.notifyAll();
        return restOf;
    }
    
    private MethodHandle restOfHandle(final OptimismInfo info, final FunctionNode restOfFunction, final boolean canBeDeoptimized) {
        assert info != null;
        assert restOfFunction.getCompileUnit().getUnitClassName().contains("restOf");
        final MethodHandle restOf = changeReturnType(info.data.lookupCodeMethod(restOfFunction.getCompileUnit().getCode(), Lookup.MH.type(restOfFunction.getReturnType().getTypeClass(), RewriteException.class)), Object.class);
        if (!canBeDeoptimized) {
            return restOf;
        }
        return Lookup.MH.catchException(restOf, RewriteException.class, this.createRewriteExceptionHandler());
    }
    
    private static Object newFilter(final Object result, final Object allocation) {
        return (result instanceof ScriptObject || !JSType.isPrimitive(result)) ? result : allocation;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), CompiledFunction.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        NEWFILTER = findOwnMH("newFilter", Object.class, Object.class, Object.class);
        RELINK_COMPOSABLE_INVOKER = findOwnMH("relinkComposableInvoker", Void.TYPE, CallSite.class, CompiledFunction.class, Boolean.TYPE);
        HANDLE_REWRITE_EXCEPTION = findOwnMH("handleRewriteException", MethodHandle.class, CompiledFunction.class, OptimismInfo.class, RewriteException.class);
        RESTOF_INVOKER = MethodHandles.exactInvoker(MethodType.methodType(Object.class, RewriteException.class));
        NO_FUNCTIONS = Collections.emptySet();
    }
    
    private static class HandleAndAssumptions
    {
        final MethodHandle handle;
        final SwitchPoint assumptions;
        
        HandleAndAssumptions(final MethodHandle handle, final SwitchPoint assumptions) {
            this.handle = handle;
            this.assumptions = assumptions;
        }
        
        GuardedInvocation createInvocation() {
            return new GuardedInvocation(this.handle, this.assumptions);
        }
    }
    
    private static class OptimismInfo
    {
        private final RecompilableScriptFunctionData data;
        private final Map<Integer, Type> invalidatedProgramPoints;
        private SwitchPoint optimisticAssumptions;
        private final DebugLogger log;
        
        OptimismInfo(final RecompilableScriptFunctionData data, final Map<Integer, Type> invalidatedProgramPoints) {
            this.data = data;
            this.log = data.getLogger();
            this.invalidatedProgramPoints = ((invalidatedProgramPoints == null) ? new TreeMap<Integer, Type>() : invalidatedProgramPoints);
            this.newOptimisticAssumptions();
        }
        
        private void newOptimisticAssumptions() {
            this.optimisticAssumptions = new SwitchPoint();
        }
        
        boolean requestRecompile(final RewriteException e) {
            final Type retType = e.getReturnType();
            final Type previousFailedType = this.invalidatedProgramPoints.put(e.getProgramPoint(), retType);
            if (previousFailedType != null && !previousFailedType.narrowerThan(retType)) {
                final StackTraceElement[] stack = e.getStackTrace();
                final String functionId = (stack.length == 0) ? this.data.getName() : (stack[0].getClassName() + "." + stack[0].getMethodName());
                this.log.info("RewriteException for an already invalidated program point ", e.getProgramPoint(), " in ", functionId, ". This is okay for a recursive function invocation, but a bug otherwise.");
                return false;
            }
            SwitchPoint.invalidateAll(new SwitchPoint[] { this.optimisticAssumptions });
            return true;
        }
        
        Compiler getCompiler(final FunctionNode fn, final MethodType actualCallSiteType, final RewriteException e) {
            return this.data.getCompiler(fn, actualCallSiteType, e.getRuntimeScope(), this.invalidatedProgramPoints, getEntryPoints(e));
        }
        
        private static int[] getEntryPoints(final RewriteException e) {
            final int[] prevEntryPoints = e.getPreviousContinuationEntryPoints();
            int[] entryPoints;
            if (prevEntryPoints == null) {
                entryPoints = new int[] { 0 };
            }
            else {
                final int l = prevEntryPoints.length;
                entryPoints = new int[l + 1];
                System.arraycopy(prevEntryPoints, 0, entryPoints, 1, l);
            }
            entryPoints[0] = e.getProgramPoint();
            return entryPoints;
        }
        
        FunctionNode reparse() {
            return this.data.reparse();
        }
    }
}
