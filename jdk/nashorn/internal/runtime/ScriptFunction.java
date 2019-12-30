// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.support.Guards;
import java.util.Collections;
import jdk.nashorn.internal.codegen.ApplySpecialization;
import jdk.nashorn.internal.objects.NativeFunction;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.codegen.Compiler;
import java.util.ArrayList;
import java.util.HashSet;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import java.util.concurrent.atomic.LongAdder;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandle;

public class ScriptFunction extends ScriptObject
{
    public static final MethodHandle G$PROTOTYPE;
    public static final MethodHandle S$PROTOTYPE;
    public static final MethodHandle G$LENGTH;
    public static final MethodHandle G$NAME;
    public static final MethodHandle INVOKE_SYNC;
    static final MethodHandle ALLOCATE;
    private static final MethodHandle WRAPFILTER;
    private static final MethodHandle SCRIPTFUNCTION_GLOBALFILTER;
    public static final CompilerConstants.Call GET_SCOPE;
    private static final MethodHandle IS_FUNCTION_MH;
    private static final MethodHandle IS_APPLY_FUNCTION;
    private static final MethodHandle IS_NONSTRICT_FUNCTION;
    private static final MethodHandle ADD_ZEROTH_ELEMENT;
    private static final MethodHandle WRAP_THIS;
    private static final PropertyMap anonmap$;
    private static final PropertyMap strictmodemap$;
    private static final PropertyMap boundfunctionmap$;
    private static final PropertyMap map$;
    private static final Object LAZY_PROTOTYPE;
    private final ScriptObject scope;
    private final ScriptFunctionData data;
    protected PropertyMap allocatorMap;
    protected Object prototype;
    private static LongAdder constructorCount;
    private static LongAdder invokes;
    private static LongAdder allocations;
    
    private static PropertyMap createStrictModeMap(final PropertyMap map) {
        final int flags = 6;
        PropertyMap newMap = map;
        newMap = newMap.addPropertyNoHistory(map.newUserAccessors("arguments", 6));
        newMap = newMap.addPropertyNoHistory(map.newUserAccessors("caller", 6));
        return newMap;
    }
    
    private static PropertyMap createBoundFunctionMap(final PropertyMap strictModeMap) {
        return strictModeMap.deleteProperty(strictModeMap.findProperty("prototype"));
    }
    
    private static boolean isStrict(final int flags) {
        return (flags & 0x1) != 0x0;
    }
    
    private static PropertyMap getMap(final boolean strict) {
        return strict ? ScriptFunction.strictmodemap$ : ScriptFunction.map$;
    }
    
    private ScriptFunction(final ScriptFunctionData data, final PropertyMap map, final ScriptObject scope, final Global global) {
        super(map);
        if (Context.DEBUG) {
            ScriptFunction.constructorCount.increment();
        }
        this.data = data;
        this.scope = scope;
        this.setInitialProto(global.getFunctionPrototype());
        this.prototype = ScriptFunction.LAZY_PROTOTYPE;
        assert this.objectSpill == null;
        if (this.isStrict() || this.isBoundFunction()) {
            final ScriptFunction typeErrorThrower = global.getTypeErrorThrower();
            this.initUserAccessors("arguments", 6, typeErrorThrower, typeErrorThrower);
            this.initUserAccessors("caller", 6, typeErrorThrower, typeErrorThrower);
        }
    }
    
    private ScriptFunction(final String name, final MethodHandle methodHandle, final PropertyMap map, final ScriptObject scope, final Specialization[] specs, final int flags, final Global global) {
        this(new FinalScriptFunctionData(name, methodHandle, specs, flags), map, scope, global);
    }
    
    private ScriptFunction(final String name, final MethodHandle methodHandle, final ScriptObject scope, final Specialization[] specs, final int flags) {
        this(name, methodHandle, getMap(isStrict(flags)), scope, specs, flags, Global.instance());
    }
    
    protected ScriptFunction(final String name, final MethodHandle invokeHandle, final Specialization[] specs) {
        this(name, invokeHandle, ScriptFunction.map$, null, specs, 6, Global.instance());
    }
    
    protected ScriptFunction(final String name, final MethodHandle invokeHandle, final PropertyMap map, final Specialization[] specs) {
        this(name, invokeHandle, map.addAll(ScriptFunction.map$), null, specs, 6, Global.instance());
    }
    
    public static ScriptFunction create(final Object[] constants, final int index, final ScriptObject scope) {
        final RecompilableScriptFunctionData data = (RecompilableScriptFunctionData)constants[index];
        return new ScriptFunction(data, getMap(data.isStrict()), scope, Global.instance());
    }
    
    public static ScriptFunction create(final Object[] constants, final int index) {
        return create(constants, index, null);
    }
    
    public static ScriptFunction createAnonymous() {
        return new ScriptFunction("", GlobalFunctions.ANONYMOUS, ScriptFunction.anonmap$, null);
    }
    
    private static ScriptFunction createBuiltin(final String name, final MethodHandle methodHandle, final Specialization[] specs, final int flags) {
        final ScriptFunction func = new ScriptFunction(name, methodHandle, null, specs, flags);
        func.setPrototype(ScriptRuntime.UNDEFINED);
        func.deleteOwnProperty(func.getMap().findProperty("prototype"));
        return func;
    }
    
    public static ScriptFunction createBuiltin(final String name, final MethodHandle methodHandle, final Specialization[] specs) {
        return createBuiltin(name, methodHandle, specs, 2);
    }
    
    public static ScriptFunction createBuiltin(final String name, final MethodHandle methodHandle) {
        return createBuiltin(name, methodHandle, null);
    }
    
    public static ScriptFunction createStrictBuiltin(final String name, final MethodHandle methodHandle) {
        return createBuiltin(name, methodHandle, null, 3);
    }
    
    public final ScriptFunction createBound(final Object self, final Object[] args) {
        return new Bound(this.data.makeBoundFunctionData(this, self, args), this.getTargetFunction());
    }
    
    public final ScriptFunction createSynchronized(final Object sync) {
        final MethodHandle mh = Lookup.MH.insertArguments(ScriptFunction.INVOKE_SYNC, 0, this, sync);
        return createBuiltin(this.getName(), mh);
    }
    
    @Override
    public String getClassName() {
        return "Function";
    }
    
    @Override
    public boolean isInstance(final ScriptObject instance) {
        final Object basePrototype = this.getTargetFunction().getPrototype();
        if (!(basePrototype instanceof ScriptObject)) {
            throw ECMAErrors.typeError("prototype.not.an.object", ScriptRuntime.safeToString(this.getTargetFunction()), ScriptRuntime.safeToString(basePrototype));
        }
        for (ScriptObject proto = instance.getProto(); proto != null; proto = proto.getProto()) {
            if (proto == basePrototype) {
                return true;
            }
        }
        return false;
    }
    
    protected ScriptFunction getTargetFunction() {
        return this;
    }
    
    final boolean isBoundFunction() {
        return this.getTargetFunction() != this;
    }
    
    public final void setArity(final int arity) {
        this.data.setArity(arity);
    }
    
    public final boolean isStrict() {
        return this.data.isStrict();
    }
    
    public final boolean needsWrappedThis() {
        return this.data.needsWrappedThis();
    }
    
    private static boolean needsWrappedThis(final Object fn) {
        return fn instanceof ScriptFunction && ((ScriptFunction)fn).needsWrappedThis();
    }
    
    final Object invoke(final Object self, final Object... arguments) throws Throwable {
        if (Context.DEBUG) {
            ScriptFunction.invokes.increment();
        }
        return this.data.invoke(this, self, arguments);
    }
    
    final Object construct(final Object... arguments) throws Throwable {
        return this.data.construct(this, arguments);
    }
    
    private Object allocate() {
        if (Context.DEBUG) {
            ScriptFunction.allocations.increment();
        }
        assert !this.isBoundFunction();
        final ScriptObject prototype = this.getAllocatorPrototype();
        final ScriptObject object = this.data.allocate(this.getAllocatorMap(prototype));
        if (object != null) {
            object.setInitialProto(prototype);
        }
        return object;
    }
    
    private synchronized PropertyMap getAllocatorMap(final ScriptObject prototype) {
        if (this.allocatorMap == null || this.allocatorMap.isInvalidSharedMapFor(prototype)) {
            this.allocatorMap = this.data.getAllocatorMap(prototype);
        }
        return this.allocatorMap;
    }
    
    private ScriptObject getAllocatorPrototype() {
        final Object prototype = this.getPrototype();
        if (prototype instanceof ScriptObject) {
            return (ScriptObject)prototype;
        }
        return Global.objectPrototype();
    }
    
    @Override
    public final String safeToString() {
        return this.toSource();
    }
    
    @Override
    public final String toString() {
        return this.data.toString();
    }
    
    public final String toSource() {
        return this.data.toSource();
    }
    
    public final Object getPrototype() {
        if (this.prototype == ScriptFunction.LAZY_PROTOTYPE) {
            this.prototype = new PrototypeObject(this);
        }
        return this.prototype;
    }
    
    public final synchronized void setPrototype(final Object newPrototype) {
        if (newPrototype instanceof ScriptObject && newPrototype != this.prototype && this.allocatorMap != null) {
            this.allocatorMap = null;
        }
        this.prototype = newPrototype;
    }
    
    public final MethodHandle getBoundInvokeHandle(final Object self) {
        return Lookup.MH.bindTo(this.bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), self);
    }
    
    private MethodHandle bindToCalleeIfNeeded(final MethodHandle methodHandle) {
        return ScriptFunctionData.needsCallee(methodHandle) ? Lookup.MH.bindTo(methodHandle, this) : methodHandle;
    }
    
    public final String getName() {
        return this.data.getName();
    }
    
    public final ScriptObject getScope() {
        return this.scope;
    }
    
    public static Object G$prototype(final Object self) {
        return (self instanceof ScriptFunction) ? ((ScriptFunction)self).getPrototype() : ScriptRuntime.UNDEFINED;
    }
    
    public static void S$prototype(final Object self, final Object prototype) {
        if (self instanceof ScriptFunction) {
            ((ScriptFunction)self).setPrototype(prototype);
        }
    }
    
    public static int G$length(final Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction)self).data.getArity();
        }
        return 0;
    }
    
    public static Object G$name(final Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction)self).getName();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static ScriptObject getPrototype(final ScriptFunction constructor) {
        if (constructor != null) {
            final Object proto = constructor.getPrototype();
            if (proto instanceof ScriptObject) {
                return (ScriptObject)proto;
            }
        }
        return null;
    }
    
    public static long getConstructorCount() {
        return ScriptFunction.constructorCount.longValue();
    }
    
    public static long getInvokes() {
        return ScriptFunction.invokes.longValue();
    }
    
    public static long getAllocations() {
        return ScriptFunction.allocations.longValue();
    }
    
    @Override
    protected GuardedInvocation findNewMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType type = desc.getMethodType();
        assert desc.getMethodType().returnType() == Object.class && !NashornCallSiteDescriptor.isOptimistic(desc);
        final CompiledFunction cf = this.data.getBestConstructor(type, this.scope, CompiledFunction.NO_FUNCTIONS);
        final GuardedInvocation bestCtorInv = cf.createConstructorInvocation();
        return new GuardedInvocation(ScriptObject.pairArguments(bestCtorInv.getInvocation(), type), getFunctionGuard(this, cf.getFlags()), bestCtorInv.getSwitchPoints(), null);
    }
    
    private static Object wrapFilter(final Object obj) {
        if (obj instanceof ScriptObject || !ScriptFunctionData.isPrimitiveThis(obj)) {
            return obj;
        }
        return Context.getGlobal().wrapAsObject(obj);
    }
    
    private static Object globalFilter(final Object object) {
        return Context.getGlobal();
    }
    
    private static SpecializedFunction.LinkLogic getLinkLogic(final Object self, final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass) {
        if (linkLogicClass == null) {
            return SpecializedFunction.LinkLogic.EMPTY_INSTANCE;
        }
        if (!Context.getContextTrusted().getEnv()._optimistic_types) {
            return null;
        }
        final Object wrappedSelf = wrapFilter(self);
        if (!(wrappedSelf instanceof OptimisticBuiltins)) {
            return null;
        }
        if (wrappedSelf != self && ((OptimisticBuiltins)wrappedSelf).hasPerInstanceAssumptions()) {
            return null;
        }
        return ((OptimisticBuiltins)wrappedSelf).getLinkLogic(linkLogicClass);
    }
    
    @Override
    protected GuardedInvocation findCallMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType type = desc.getMethodType();
        final String name = this.getName();
        final boolean isUnstable = request.isCallSiteUnstable();
        final boolean scopeCall = NashornCallSiteDescriptor.isScope(desc);
        final boolean isCall = !scopeCall && this.data.isBuiltin() && "call".equals(name);
        final boolean isApply = !scopeCall && this.data.isBuiltin() && "apply".equals(name);
        final boolean isApplyOrCall = isCall | isApply;
        if (isUnstable && !isApplyOrCall) {
            MethodHandle handle;
            if (type.parameterCount() == 3 && type.parameterType(2) == Object[].class) {
                handle = ScriptRuntime.APPLY.methodHandle();
            }
            else {
                handle = Lookup.MH.asCollector(ScriptRuntime.APPLY.methodHandle(), Object[].class, type.parameterCount() - 2);
            }
            return new GuardedInvocation(handle, null, (SwitchPoint)null, ClassCastException.class);
        }
        MethodHandle guard = null;
        if (isApplyOrCall && !isUnstable) {
            final Object[] args = request.getArguments();
            if (Bootstrap.isCallable(args[1])) {
                return this.createApplyOrCallCall(isApply, desc, request, args);
            }
        }
        int programPoint = -1;
        if (NashornCallSiteDescriptor.isOptimistic(desc)) {
            programPoint = NashornCallSiteDescriptor.getProgramPoint(desc);
        }
        CompiledFunction cf = this.data.getBestInvoker(type, this.scope, CompiledFunction.NO_FUNCTIONS);
        final Object self = request.getArguments()[1];
        final Collection<CompiledFunction> forbidden = new HashSet<CompiledFunction>();
        final List<SwitchPoint> sps = new ArrayList<SwitchPoint>();
        Class<? extends Throwable> exceptionGuard = null;
        while (cf.isSpecialization()) {
            final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = cf.getLinkLogicClass();
            final SpecializedFunction.LinkLogic linkLogic = getLinkLogic(self, linkLogicClass);
            if (linkLogic != null && linkLogic.checkLinkable(self, desc, request)) {
                final DebugLogger log = Context.getContextTrusted().getLogger(Compiler.class);
                if (log.isEnabled()) {
                    log.info("Linking optimistic builtin function: '", name, "' args=", Arrays.toString(request.getArguments()), " desc=", desc);
                }
                exceptionGuard = linkLogic.getRelinkException();
                break;
            }
            forbidden.add(cf);
            final CompiledFunction oldCf = cf;
            cf = this.data.getBestInvoker(type, this.scope, forbidden);
            assert oldCf != cf;
        }
        final GuardedInvocation bestInvoker = cf.createFunctionInvocation(type.returnType(), programPoint);
        final MethodHandle callHandle = bestInvoker.getInvocation();
        MethodHandle boundHandle;
        if (this.data.needsCallee()) {
            if (scopeCall && this.needsWrappedThis()) {
                boundHandle = Lookup.MH.filterArguments(callHandle, 1, ScriptFunction.SCRIPTFUNCTION_GLOBALFILTER);
            }
            else {
                boundHandle = callHandle;
            }
        }
        else if (this.data.isBuiltin() && "extend".equals(this.data.getName())) {
            boundHandle = Lookup.MH.dropArguments(Lookup.MH.bindTo(callHandle, desc.getLookup()), 0, type.parameterType(0), type.parameterType(1));
        }
        else if (scopeCall && this.needsWrappedThis()) {
            boundHandle = Lookup.MH.filterArguments(callHandle, 0, ScriptFunction.SCRIPTFUNCTION_GLOBALFILTER);
            boundHandle = Lookup.MH.dropArguments(boundHandle, 0, type.parameterType(0));
        }
        else {
            boundHandle = Lookup.MH.dropArguments(callHandle, 0, type.parameterType(0));
        }
        if (!scopeCall && this.needsWrappedThis()) {
            if (ScriptFunctionData.isPrimitiveThis(request.getArguments()[1])) {
                boundHandle = Lookup.MH.filterArguments(boundHandle, 1, ScriptFunction.WRAPFILTER);
            }
            else {
                guard = getNonStrictFunctionGuard(this);
            }
        }
        if (isUnstable && NashornCallSiteDescriptor.isApplyToCall(desc)) {
            boundHandle = Lookup.MH.asCollector(boundHandle, Object[].class, type.parameterCount() - 2);
        }
        boundHandle = ScriptObject.pairArguments(boundHandle, type);
        if (bestInvoker.getSwitchPoints() != null) {
            sps.addAll(Arrays.asList(bestInvoker.getSwitchPoints()));
        }
        final SwitchPoint[] spsArray = (SwitchPoint[])(sps.isEmpty() ? null : ((SwitchPoint[])sps.toArray(new SwitchPoint[sps.size()])));
        return new GuardedInvocation(boundHandle, (guard == null) ? getFunctionGuard(this, cf.getFlags()) : guard, spsArray, exceptionGuard);
    }
    
    private GuardedInvocation createApplyOrCallCall(final boolean isApply, final CallSiteDescriptor desc, final LinkRequest request, final Object[] args) {
        final MethodType descType = desc.getMethodType();
        final int paramCount = descType.parameterCount();
        if (descType.parameterType(paramCount - 1).isArray()) {
            return this.createVarArgApplyOrCallCall(isApply, desc, request, args);
        }
        final boolean passesThis = paramCount > 2;
        final boolean passesArgs = paramCount > 3;
        final int realArgCount = passesArgs ? (paramCount - 3) : 0;
        final Object appliedFn = args[1];
        final boolean appliedFnNeedsWrappedThis = needsWrappedThis(appliedFn);
        CallSiteDescriptor appliedDesc = desc;
        final SwitchPoint applyToCallSwitchPoint = Global.getBuiltinFunctionApplySwitchPoint();
        final boolean isApplyToCall = NashornCallSiteDescriptor.isApplyToCall(desc);
        final boolean isFailedApplyToCall = isApplyToCall && applyToCallSwitchPoint.hasBeenInvalidated();
        MethodType appliedType = descType.dropParameterTypes(0, 1);
        if (!passesThis) {
            appliedType = appliedType.insertParameterTypes(1, (Class<?>[])new Class[] { Object.class });
        }
        else if (appliedFnNeedsWrappedThis) {
            appliedType = appliedType.changeParameterType(1, (Class<?>)Object.class);
        }
        MethodType dropArgs = Lookup.MH.type(Void.TYPE, (Class<?>[])new Class[0]);
        if (isApply && !isFailedApplyToCall) {
            final int pc = appliedType.parameterCount();
            for (int i = 3; i < pc; ++i) {
                dropArgs = dropArgs.appendParameterTypes(appliedType.parameterType(i));
            }
            if (pc > 3) {
                appliedType = appliedType.dropParameterTypes(3, pc);
            }
        }
        if (isApply || isFailedApplyToCall) {
            if (passesArgs) {
                appliedType = appliedType.changeParameterType(2, (Class<?>)Object[].class);
                if (isFailedApplyToCall) {
                    appliedType = appliedType.dropParameterTypes(3, paramCount - 1);
                }
            }
            else {
                appliedType = appliedType.insertParameterTypes(2, (Class<?>[])new Class[] { Object[].class });
            }
        }
        appliedDesc = appliedDesc.changeMethodType(appliedType);
        final Object[] appliedArgs = new Object[isApply ? 3 : appliedType.parameterCount()];
        appliedArgs[0] = appliedFn;
        appliedArgs[1] = (passesThis ? (appliedFnNeedsWrappedThis ? ScriptFunctionData.wrapThis(args[2]) : args[2]) : ScriptRuntime.UNDEFINED);
        if (isApply && !isFailedApplyToCall) {
            appliedArgs[2] = (passesArgs ? NativeFunction.toApplyArgs(args[3]) : ScriptRuntime.EMPTY_ARRAY);
        }
        else if (passesArgs) {
            if (isFailedApplyToCall) {
                final Object[] tmp = new Object[args.length - 3];
                System.arraycopy(args, 3, tmp, 0, tmp.length);
                appliedArgs[2] = NativeFunction.toApplyArgs(tmp);
            }
            else {
                assert !isApply;
                System.arraycopy(args, 3, appliedArgs, 2, args.length - 3);
            }
        }
        else if (isFailedApplyToCall) {
            appliedArgs[2] = ScriptRuntime.EMPTY_ARRAY;
        }
        final LinkRequest appliedRequest = request.replaceArguments(appliedDesc, appliedArgs);
        GuardedInvocation appliedInvocation;
        try {
            appliedInvocation = Bootstrap.getLinkerServices().getGuardedInvocation(appliedRequest);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t;
            final Throwable e = t;
            throw e;
        }
        catch (Exception e2) {
            throw new RuntimeException(e2);
        }
        assert appliedRequest != null;
        final Class<?> applyFnType = descType.parameterType(0);
        MethodHandle inv = appliedInvocation.getInvocation();
        if (isApply && !isFailedApplyToCall) {
            if (passesArgs) {
                inv = Lookup.MH.filterArguments(inv, 2, NativeFunction.TO_APPLY_ARGS);
            }
            else {
                inv = Lookup.MH.insertArguments(inv, 2, ScriptRuntime.EMPTY_ARRAY);
            }
        }
        if (isApplyToCall) {
            if (isFailedApplyToCall) {
                Context.getContextTrusted().getLogger(ApplySpecialization.class).info("Collection arguments to revert call to apply in " + appliedFn);
                inv = Lookup.MH.asCollector(inv, Object[].class, realArgCount);
            }
            else {
                appliedInvocation = appliedInvocation.addSwitchPoint(applyToCallSwitchPoint);
            }
        }
        if (!passesThis) {
            inv = bindImplicitThis(appliedFn, inv);
        }
        else if (appliedFnNeedsWrappedThis) {
            inv = Lookup.MH.filterArguments(inv, 1, ScriptFunction.WRAP_THIS);
        }
        inv = Lookup.MH.dropArguments(inv, 0, applyFnType);
        for (int j = 0; j < dropArgs.parameterCount(); ++j) {
            inv = Lookup.MH.dropArguments(inv, 4 + j, dropArgs.parameterType(j));
        }
        MethodHandle guard = appliedInvocation.getGuard();
        if (!passesThis && guard.type().parameterCount() > 1) {
            guard = bindImplicitThis(appliedFn, guard);
        }
        final MethodType guardType = guard.type();
        guard = Lookup.MH.dropArguments(guard, 0, descType.parameterType(0));
        MethodHandle applyFnGuard = Lookup.MH.insertArguments(ScriptFunction.IS_APPLY_FUNCTION, 2, this);
        applyFnGuard = Lookup.MH.dropArguments(applyFnGuard, 2, guardType.parameterArray());
        guard = Lookup.MH.foldArguments(applyFnGuard, guard);
        return appliedInvocation.replaceMethods(inv, guard);
    }
    
    private GuardedInvocation createVarArgApplyOrCallCall(final boolean isApply, final CallSiteDescriptor desc, final LinkRequest request, final Object[] args) {
        final MethodType descType = desc.getMethodType();
        final int paramCount = descType.parameterCount();
        final Object[] varArgs = (Object[])args[paramCount - 1];
        final int copiedArgCount = args.length - 1;
        final int varArgCount = varArgs.length;
        final Object[] spreadArgs = new Object[copiedArgCount + varArgCount];
        System.arraycopy(args, 0, spreadArgs, 0, copiedArgCount);
        System.arraycopy(varArgs, 0, spreadArgs, copiedArgCount, varArgCount);
        final MethodType spreadType = descType.dropParameterTypes(paramCount - 1, paramCount).appendParameterTypes((List<Class<?>>)Collections.nCopies(varArgCount, Object.class));
        final CallSiteDescriptor spreadDesc = desc.changeMethodType(spreadType);
        final LinkRequest spreadRequest = request.replaceArguments(spreadDesc, spreadArgs);
        final GuardedInvocation spreadInvocation = this.createApplyOrCallCall(isApply, spreadDesc, spreadRequest, spreadArgs);
        return spreadInvocation.replaceMethods(ScriptObject.pairArguments(spreadInvocation.getInvocation(), descType), spreadGuardArguments(spreadInvocation.getGuard(), descType));
    }
    
    private static MethodHandle spreadGuardArguments(final MethodHandle guard, final MethodType descType) {
        final MethodType guardType = guard.type();
        final int guardParamCount = guardType.parameterCount();
        final int descParamCount = descType.parameterCount();
        final int spreadCount = guardParamCount - descParamCount + 1;
        if (spreadCount <= 0) {
            return guard;
        }
        MethodHandle arrayConvertingGuard;
        if (guardType.parameterType(guardParamCount - 1).isArray()) {
            arrayConvertingGuard = Lookup.MH.filterArguments(guard, guardParamCount - 1, NativeFunction.TO_APPLY_ARGS);
        }
        else {
            arrayConvertingGuard = guard;
        }
        return ScriptObject.adaptHandleToVarArgCallSite(arrayConvertingGuard, descParamCount);
    }
    
    private static MethodHandle bindImplicitThis(final Object fn, final MethodHandle mh) {
        MethodHandle bound;
        if (fn instanceof ScriptFunction && ((ScriptFunction)fn).needsWrappedThis()) {
            bound = Lookup.MH.filterArguments(mh, 1, ScriptFunction.SCRIPTFUNCTION_GLOBALFILTER);
        }
        else {
            bound = mh;
        }
        return Lookup.MH.insertArguments(bound, 1, ScriptRuntime.UNDEFINED);
    }
    
    MethodHandle getCallMethodHandle(final MethodType type, final String bindName) {
        return ScriptObject.pairArguments(bindToNameIfNeeded(this.bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), bindName), type);
    }
    
    private static MethodHandle bindToNameIfNeeded(final MethodHandle methodHandle, final String bindName) {
        if (bindName == null) {
            return methodHandle;
        }
        final MethodType methodType = methodHandle.type();
        final int parameterCount = methodType.parameterCount();
        final boolean isVarArg = parameterCount > 0 && methodType.parameterType(parameterCount - 1).isArray();
        if (isVarArg) {
            return Lookup.MH.filterArguments(methodHandle, 1, Lookup.MH.insertArguments(ScriptFunction.ADD_ZEROTH_ELEMENT, 1, bindName));
        }
        return Lookup.MH.insertArguments(methodHandle, 1, bindName);
    }
    
    private static MethodHandle getFunctionGuard(final ScriptFunction function, final int flags) {
        assert function.data != null;
        if (function.data.isBuiltin()) {
            return Guards.getIdentityGuard(function);
        }
        return Lookup.MH.insertArguments(ScriptFunction.IS_FUNCTION_MH, 1, function.data);
    }
    
    private static MethodHandle getNonStrictFunctionGuard(final ScriptFunction function) {
        assert function.data != null;
        return Lookup.MH.insertArguments(ScriptFunction.IS_NONSTRICT_FUNCTION, 2, function.data);
    }
    
    private static boolean isFunctionMH(final Object self, final ScriptFunctionData data) {
        return self instanceof ScriptFunction && ((ScriptFunction)self).data == data;
    }
    
    private static boolean isNonStrictFunction(final Object self, final Object arg, final ScriptFunctionData data) {
        return self instanceof ScriptFunction && ((ScriptFunction)self).data == data && arg instanceof ScriptObject;
    }
    
    private static boolean isApplyFunction(final boolean appliedFnCondition, final Object self, final Object expectedSelf) {
        return appliedFnCondition && self == expectedSelf;
    }
    
    private static Object[] addZerothElement(final Object[] args, final Object value) {
        final Object[] src = (args == null) ? ScriptRuntime.EMPTY_ARRAY : args;
        final Object[] result = new Object[src.length + 1];
        System.arraycopy(src, 0, result, 1, src.length);
        result[0] = value;
        return result;
    }
    
    private static Object invokeSync(final ScriptFunction func, final Object sync, final Object self, final Object... args) throws Throwable {
        final Object syncObj = (sync == ScriptRuntime.UNDEFINED) ? self : sync;
        synchronized (syncObj) {
            return func.invoke(self, args);
        }
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }
    
    private static MethodHandle findOwnMH_V(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        G$PROTOTYPE = findOwnMH_S("G$prototype", Object.class, Object.class);
        S$PROTOTYPE = findOwnMH_S("S$prototype", Void.TYPE, Object.class, Object.class);
        G$LENGTH = findOwnMH_S("G$length", Integer.TYPE, Object.class);
        G$NAME = findOwnMH_S("G$name", Object.class, Object.class);
        INVOKE_SYNC = findOwnMH_S("invokeSync", Object.class, ScriptFunction.class, Object.class, Object.class, Object[].class);
        ALLOCATE = findOwnMH_V("allocate", Object.class, (Class<?>[])new Class[0]);
        WRAPFILTER = findOwnMH_S("wrapFilter", Object.class, Object.class);
        SCRIPTFUNCTION_GLOBALFILTER = findOwnMH_S("globalFilter", Object.class, Object.class);
        GET_SCOPE = CompilerConstants.virtualCallNoLookup(ScriptFunction.class, "getScope", ScriptObject.class, (Class<?>[])new Class[0]);
        IS_FUNCTION_MH = findOwnMH_S("isFunctionMH", Boolean.TYPE, Object.class, ScriptFunctionData.class);
        IS_APPLY_FUNCTION = findOwnMH_S("isApplyFunction", Boolean.TYPE, Boolean.TYPE, Object.class, Object.class);
        IS_NONSTRICT_FUNCTION = findOwnMH_S("isNonStrictFunction", Boolean.TYPE, Object.class, Object.class, ScriptFunctionData.class);
        ADD_ZEROTH_ELEMENT = findOwnMH_S("addZerothElement", Object[].class, Object[].class, Object.class);
        WRAP_THIS = Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, "wrapThis", Lookup.MH.type(Object.class, Object.class));
        LAZY_PROTOTYPE = new Object();
        anonmap$ = PropertyMap.newMap();
        final ArrayList<Property> properties = new ArrayList<Property>(3);
        properties.add(AccessorProperty.create("prototype", 6, ScriptFunction.G$PROTOTYPE, ScriptFunction.S$PROTOTYPE));
        properties.add(AccessorProperty.create("length", 7, ScriptFunction.G$LENGTH, null));
        properties.add(AccessorProperty.create("name", 7, ScriptFunction.G$NAME, null));
        map$ = PropertyMap.newMap(properties);
        strictmodemap$ = createStrictModeMap(ScriptFunction.map$);
        boundfunctionmap$ = createBoundFunctionMap(ScriptFunction.strictmodemap$);
        if (Context.DEBUG) {
            ScriptFunction.constructorCount = new LongAdder();
            ScriptFunction.invokes = new LongAdder();
            ScriptFunction.allocations = new LongAdder();
        }
    }
    
    private static class Bound extends ScriptFunction
    {
        private final ScriptFunction target;
        
        Bound(final ScriptFunctionData boundData, final ScriptFunction target) {
            super(boundData, ScriptFunction.boundfunctionmap$, null, Global.instance(), null);
            this.setPrototype(ScriptRuntime.UNDEFINED);
            this.target = target;
        }
        
        @Override
        protected ScriptFunction getTargetFunction() {
            return this.target;
        }
    }
}
