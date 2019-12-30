// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Iterator;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.Collection;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import java.util.LinkedList;
import java.io.Serializable;

public abstract class ScriptFunctionData implements Serializable
{
    static final int MAX_ARITY = 250;
    protected final String name;
    protected transient LinkedList<CompiledFunction> code;
    protected int flags;
    private int arity;
    private transient volatile GenericInvokers genericInvokers;
    private static final MethodHandle BIND_VAR_ARGS;
    public static final int IS_STRICT = 1;
    public static final int IS_BUILTIN = 2;
    public static final int IS_CONSTRUCTOR = 4;
    public static final int NEEDS_CALLEE = 8;
    public static final int USES_THIS = 16;
    public static final int IS_VARIABLE_ARITY = 32;
    public static final int IS_PROPERTY_ACCESSOR = 64;
    public static final int IS_STRICT_OR_BUILTIN = 3;
    public static final int IS_BUILTIN_CONSTRUCTOR = 6;
    private static final long serialVersionUID = 4252901245508769114L;
    
    ScriptFunctionData(final String name, final int arity, final int flags) {
        this.code = new LinkedList<CompiledFunction>();
        this.name = name;
        this.flags = flags;
        this.setArity(arity);
    }
    
    final int getArity() {
        return this.arity;
    }
    
    final boolean isVariableArity() {
        return (this.flags & 0x20) != 0x0;
    }
    
    final boolean isPropertyAccessor() {
        return (this.flags & 0x40) != 0x0;
    }
    
    void setArity(final int arity) {
        if (arity < 0 || arity > 250) {
            throw new IllegalArgumentException(String.valueOf(arity));
        }
        this.arity = arity;
    }
    
    CompiledFunction bind(final CompiledFunction originalInv, final ScriptFunction fn, final Object self, final Object[] args) {
        final MethodHandle boundInvoker = this.bindInvokeHandle(originalInv.createComposableInvoker(), fn, self, args);
        if (this.isConstructor()) {
            return new CompiledFunction(boundInvoker, bindConstructHandle(originalInv.createComposableConstructor(), fn, args), null);
        }
        return new CompiledFunction(boundInvoker);
    }
    
    public final boolean isStrict() {
        return (this.flags & 0x1) != 0x0;
    }
    
    protected String getFunctionName() {
        return this.getName();
    }
    
    final boolean isBuiltin() {
        return (this.flags & 0x2) != 0x0;
    }
    
    final boolean isConstructor() {
        return (this.flags & 0x4) != 0x0;
    }
    
    abstract boolean needsCallee();
    
    final boolean needsWrappedThis() {
        return (this.flags & 0x10) != 0x0 && (this.flags & 0x3) == 0x0;
    }
    
    String toSource() {
        return "function " + ((this.name == null) ? "" : this.name) + "() { [native code] }";
    }
    
    String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.name.isEmpty() ? "<anonymous>" : this.name;
    }
    
    public String toStringVerbose() {
        final StringBuilder sb = new StringBuilder();
        sb.append("name='").append(this.name.isEmpty() ? "<anonymous>" : this.name).append("' ").append(this.code.size()).append(" invokers=").append(this.code);
        return sb.toString();
    }
    
    final CompiledFunction getBestInvoker(final MethodType callSiteType, final ScriptObject runtimeScope) {
        return this.getBestInvoker(callSiteType, runtimeScope, CompiledFunction.NO_FUNCTIONS);
    }
    
    final CompiledFunction getBestInvoker(final MethodType callSiteType, final ScriptObject runtimeScope, final Collection<CompiledFunction> forbidden) {
        final CompiledFunction cf = this.getBest(callSiteType, runtimeScope, forbidden);
        assert cf != null;
        return cf;
    }
    
    final CompiledFunction getBestConstructor(final MethodType callSiteType, final ScriptObject runtimeScope, final Collection<CompiledFunction> forbidden) {
        if (!this.isConstructor()) {
            throw ECMAErrors.typeError("not.a.constructor", this.toSource());
        }
        final CompiledFunction cf = this.getBest(callSiteType.insertParameterTypes(1, (Class<?>[])new Class[] { Object.class }), runtimeScope, forbidden);
        return cf;
    }
    
    protected void ensureCompiled() {
    }
    
    final MethodHandle getGenericInvoker(final ScriptObject runtimeScope) {
        final GenericInvokers lgenericInvokers = this.ensureGenericInvokers();
        MethodHandle invoker = lgenericInvokers.invoker;
        if (invoker == null) {
            invoker = (lgenericInvokers.invoker = this.createGenericInvoker(runtimeScope));
        }
        return invoker;
    }
    
    private MethodHandle createGenericInvoker(final ScriptObject runtimeScope) {
        return makeGenericMethod(this.getGeneric(runtimeScope).createComposableInvoker());
    }
    
    final MethodHandle getGenericConstructor(final ScriptObject runtimeScope) {
        final GenericInvokers lgenericInvokers = this.ensureGenericInvokers();
        MethodHandle constructor = lgenericInvokers.constructor;
        if (constructor == null) {
            constructor = (lgenericInvokers.constructor = this.createGenericConstructor(runtimeScope));
        }
        return constructor;
    }
    
    private MethodHandle createGenericConstructor(final ScriptObject runtimeScope) {
        return makeGenericMethod(this.getGeneric(runtimeScope).createComposableConstructor());
    }
    
    private GenericInvokers ensureGenericInvokers() {
        GenericInvokers lgenericInvokers = this.genericInvokers;
        if (lgenericInvokers == null) {
            lgenericInvokers = (this.genericInvokers = new GenericInvokers());
        }
        return lgenericInvokers;
    }
    
    private static MethodType widen(final MethodType cftype) {
        final Class<?>[] paramTypes = (Class<?>[])new Class[cftype.parameterCount()];
        for (int i = 0; i < cftype.parameterCount(); ++i) {
            paramTypes[i] = (cftype.parameterType(i).isPrimitive() ? cftype.parameterType(i) : Object.class);
        }
        return Lookup.MH.type(cftype.returnType(), paramTypes);
    }
    
    CompiledFunction lookupExactApplyToCall(final MethodType type) {
        for (final CompiledFunction cf : this.code) {
            if (!cf.isApplyToCall()) {
                continue;
            }
            final MethodType cftype = cf.type();
            if (cftype.parameterCount() != type.parameterCount()) {
                continue;
            }
            if (widen(cftype).equals((Object)widen(type))) {
                return cf;
            }
        }
        return null;
    }
    
    CompiledFunction pickFunction(final MethodType callSiteType, final boolean canPickVarArg) {
        for (final CompiledFunction candidate : this.code) {
            if (candidate.matchesCallSite(callSiteType, canPickVarArg)) {
                return candidate;
            }
        }
        return null;
    }
    
    abstract CompiledFunction getBest(final MethodType p0, final ScriptObject p1, final Collection<CompiledFunction> p2, final boolean p3);
    
    final CompiledFunction getBest(final MethodType callSiteType, final ScriptObject runtimeScope, final Collection<CompiledFunction> forbidden) {
        return this.getBest(callSiteType, runtimeScope, forbidden, true);
    }
    
    boolean isValidCallSite(final MethodType callSiteType) {
        return callSiteType.parameterCount() >= 2 && callSiteType.parameterType(0).isAssignableFrom(ScriptFunction.class);
    }
    
    CompiledFunction getGeneric(final ScriptObject runtimeScope) {
        return this.getBest(this.getGenericType(), runtimeScope, CompiledFunction.NO_FUNCTIONS, false);
    }
    
    abstract MethodType getGenericType();
    
    ScriptObject allocate(final PropertyMap map) {
        return null;
    }
    
    PropertyMap getAllocatorMap(final ScriptObject prototype) {
        return null;
    }
    
    ScriptFunctionData makeBoundFunctionData(final ScriptFunction fn, final Object self, final Object[] args) {
        final Object[] allArgs = (args == null) ? ScriptRuntime.EMPTY_ARRAY : args;
        final int length = (args == null) ? 0 : args.length;
        final int boundFlags = this.flags & 0xFFFFFFF7 & 0xFFFFFFEF;
        final List<CompiledFunction> boundList = new LinkedList<CompiledFunction>();
        final ScriptObject runtimeScope = fn.getScope();
        final CompiledFunction bindTarget = new CompiledFunction(this.getGenericInvoker(runtimeScope), this.getGenericConstructor(runtimeScope), null);
        boundList.add(this.bind(bindTarget, fn, self, allArgs));
        return new FinalScriptFunctionData(this.name, Math.max(0, this.getArity() - length), boundList, boundFlags);
    }
    
    private Object convertThisObject(final Object thiz) {
        return this.needsWrappedThis() ? wrapThis(thiz) : thiz;
    }
    
    static Object wrapThis(final Object thiz) {
        if (!(thiz instanceof ScriptObject)) {
            if (JSType.nullOrUndefined(thiz)) {
                return Context.getGlobal();
            }
            if (isPrimitiveThis(thiz)) {
                return Context.getGlobal().wrapAsObject(thiz);
            }
        }
        return thiz;
    }
    
    static boolean isPrimitiveThis(final Object obj) {
        return JSType.isString(obj) || obj instanceof Number || obj instanceof Boolean;
    }
    
    private MethodHandle bindInvokeHandle(final MethodHandle originalInvoker, final ScriptFunction targetFn, final Object self, final Object[] args) {
        final boolean isTargetBound = targetFn.isBoundFunction();
        final boolean needsCallee = needsCallee(originalInvoker);
        assert needsCallee == this.needsCallee() : "callee contract violation 2";
        assert !needsCallee;
        final Object boundSelf = isTargetBound ? null : this.convertThisObject(self);
        MethodHandle boundInvoker;
        if (isVarArg(originalInvoker)) {
            MethodHandle noArgBoundInvoker;
            if (isTargetBound) {
                noArgBoundInvoker = originalInvoker;
            }
            else if (needsCallee) {
                noArgBoundInvoker = Lookup.MH.insertArguments(originalInvoker, 0, targetFn, boundSelf);
            }
            else {
                noArgBoundInvoker = Lookup.MH.bindTo(originalInvoker, boundSelf);
            }
            if (args.length > 0) {
                boundInvoker = varArgBinder(noArgBoundInvoker, args);
            }
            else {
                boundInvoker = noArgBoundInvoker;
            }
        }
        else {
            final int argInsertPos = isTargetBound ? 1 : 0;
            final Object[] boundArgs = new Object[Math.min(originalInvoker.type().parameterCount() - argInsertPos, args.length + (isTargetBound ? 0 : (needsCallee ? 2 : 1)))];
            int next = 0;
            if (!isTargetBound) {
                if (needsCallee) {
                    boundArgs[next++] = targetFn;
                }
                boundArgs[next++] = boundSelf;
            }
            System.arraycopy(args, 0, boundArgs, next, boundArgs.length - next);
            boundInvoker = Lookup.MH.insertArguments(originalInvoker, argInsertPos, boundArgs);
        }
        if (isTargetBound) {
            return boundInvoker;
        }
        return Lookup.MH.dropArguments(boundInvoker, 0, Object.class);
    }
    
    private static MethodHandle bindConstructHandle(final MethodHandle originalConstructor, final ScriptFunction fn, final Object[] args) {
        assert originalConstructor != null;
        final MethodHandle calleeBoundConstructor = fn.isBoundFunction() ? originalConstructor : Lookup.MH.dropArguments(Lookup.MH.bindTo(originalConstructor, fn), 0, ScriptFunction.class);
        if (args.length == 0) {
            return calleeBoundConstructor;
        }
        if (isVarArg(calleeBoundConstructor)) {
            return varArgBinder(calleeBoundConstructor, args);
        }
        final int maxArgCount = calleeBoundConstructor.type().parameterCount() - 1;
        Object[] boundArgs;
        if (args.length <= maxArgCount) {
            boundArgs = args;
        }
        else {
            boundArgs = new Object[maxArgCount];
            System.arraycopy(args, 0, boundArgs, 0, maxArgCount);
        }
        return Lookup.MH.insertArguments(calleeBoundConstructor, 1, boundArgs);
    }
    
    private static MethodHandle makeGenericMethod(final MethodHandle mh) {
        final MethodType type = mh.type();
        final MethodType newType = makeGenericType(type);
        return type.equals((Object)newType) ? mh : mh.asType(newType);
    }
    
    private static MethodType makeGenericType(final MethodType type) {
        MethodType newType = type.generic();
        if (isVarArg(type)) {
            newType = newType.changeParameterType(type.parameterCount() - 1, (Class<?>)Object[].class);
        }
        if (needsCallee(type)) {
            newType = newType.changeParameterType(0, (Class<?>)ScriptFunction.class);
        }
        return newType;
    }
    
    Object invoke(final ScriptFunction fn, final Object self, final Object... arguments) throws Throwable {
        final MethodHandle mh = this.getGenericInvoker(fn.getScope());
        final Object selfObj = this.convertThisObject(self);
        final Object[] args = (arguments == null) ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (isVarArg(mh)) {
            if (needsCallee(mh)) {
                return mh.invokeExact(fn, selfObj, args);
            }
            return mh.invokeExact(selfObj, args);
        }
        else {
            final int paramCount = mh.type().parameterCount();
            if (needsCallee(mh)) {
                switch (paramCount) {
                    case 2: {
                        return mh.invokeExact(fn, selfObj);
                    }
                    case 3: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0));
                    }
                    case 4: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1));
                    }
                    case 5: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2));
                    }
                    case 6: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                    }
                    case 7: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                    }
                    case 8: {
                        return mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                    }
                    default: {
                        return mh.invokeWithArguments(withArguments(fn, selfObj, paramCount, args));
                    }
                }
            }
            else {
                switch (paramCount) {
                    case 1: {
                        return mh.invokeExact(selfObj);
                    }
                    case 2: {
                        return mh.invokeExact(selfObj, getArg(args, 0));
                    }
                    case 3: {
                        return mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1));
                    }
                    case 4: {
                        return mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2));
                    }
                    case 5: {
                        return mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                    }
                    case 6: {
                        return mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                    }
                    case 7: {
                        return mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                    }
                    default: {
                        return mh.invokeWithArguments(withArguments(null, selfObj, paramCount, args));
                    }
                }
            }
        }
    }
    
    Object construct(final ScriptFunction fn, final Object... arguments) throws Throwable {
        final MethodHandle mh = this.getGenericConstructor(fn.getScope());
        final Object[] args = (arguments == null) ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (isVarArg(mh)) {
            if (needsCallee(mh)) {
                return mh.invokeExact(fn, args);
            }
            return mh.invokeExact(args);
        }
        else {
            final int paramCount = mh.type().parameterCount();
            if (needsCallee(mh)) {
                switch (paramCount) {
                    case 1: {
                        return mh.invokeExact(fn);
                    }
                    case 2: {
                        return mh.invokeExact(fn, getArg(args, 0));
                    }
                    case 3: {
                        return mh.invokeExact(fn, getArg(args, 0), getArg(args, 1));
                    }
                    case 4: {
                        return mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2));
                    }
                    case 5: {
                        return mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                    }
                    case 6: {
                        return mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                    }
                    case 7: {
                        return mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                    }
                    default: {
                        return mh.invokeWithArguments(withArguments(fn, paramCount, args));
                    }
                }
            }
            else {
                switch (paramCount) {
                    case 0: {
                        return mh.invokeExact();
                    }
                    case 1: {
                        return mh.invokeExact(getArg(args, 0));
                    }
                    case 2: {
                        return mh.invokeExact(getArg(args, 0), getArg(args, 1));
                    }
                    case 3: {
                        return mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2));
                    }
                    case 4: {
                        return mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                    }
                    case 5: {
                        return mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                    }
                    case 6: {
                        return mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                    }
                    default: {
                        return mh.invokeWithArguments(withArguments(null, paramCount, args));
                    }
                }
            }
        }
    }
    
    private static Object getArg(final Object[] args, final int i) {
        return (i < args.length) ? args[i] : ScriptRuntime.UNDEFINED;
    }
    
    private static Object[] withArguments(final ScriptFunction fn, final int argCount, final Object[] args) {
        final Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            finalArgs[nextArg++] = fn;
        }
        for (int i = 0; i < args.length && nextArg < argCount; finalArgs[nextArg++] = args[i++]) {}
        while (nextArg < argCount) {
            finalArgs[nextArg++] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }
    
    private static Object[] withArguments(final ScriptFunction fn, final Object self, final int argCount, final Object[] args) {
        final Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            finalArgs[nextArg++] = fn;
        }
        finalArgs[nextArg++] = self;
        for (int i = 0; i < args.length && nextArg < argCount; finalArgs[nextArg++] = args[i++]) {}
        while (nextArg < argCount) {
            finalArgs[nextArg++] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }
    
    private static MethodHandle varArgBinder(final MethodHandle mh, final Object[] args) {
        assert args != null;
        assert args.length > 0;
        return Lookup.MH.filterArguments(mh, mh.type().parameterCount() - 1, Lookup.MH.bindTo(ScriptFunctionData.BIND_VAR_ARGS, args));
    }
    
    protected static boolean needsCallee(final MethodHandle mh) {
        return needsCallee(mh.type());
    }
    
    static boolean needsCallee(final MethodType type) {
        final int length = type.parameterCount();
        if (length == 0) {
            return false;
        }
        final Class<?> param0 = type.parameterType(0);
        return param0 == ScriptFunction.class || (param0 == Boolean.TYPE && length > 1 && type.parameterType(1) == ScriptFunction.class);
    }
    
    protected static boolean isVarArg(final MethodHandle mh) {
        return isVarArg(mh.type());
    }
    
    static boolean isVarArg(final MethodType type) {
        return type.parameterType(type.parameterCount() - 1).isArray();
    }
    
    public boolean inDynamicContext() {
        return false;
    }
    
    private static Object[] bindVarArgs(final Object[] array1, final Object[] array2) {
        if (array2 == null) {
            return array1.clone();
        }
        final int l2 = array2.length;
        if (l2 == 0) {
            return array1.clone();
        }
        final int l3 = array1.length;
        final Object[] concat = new Object[l3 + l2];
        System.arraycopy(array1, 0, concat, 0, l3);
        System.arraycopy(array2, 0, concat, l3, l2);
        return concat;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, name, Lookup.MH.type(rtype, types));
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.code = new LinkedList<CompiledFunction>();
    }
    
    static {
        BIND_VAR_ARGS = findOwnMH("bindVarArgs", Object[].class, Object[].class, Object[].class);
    }
    
    private static final class GenericInvokers
    {
        volatile MethodHandle invoker;
        volatile MethodHandle constructor;
    }
}
