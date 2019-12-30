// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodType;
import java.util.Iterator;
import java.lang.invoke.MethodHandle;
import java.util.Collection;
import java.util.List;

final class FinalScriptFunctionData extends ScriptFunctionData
{
    private static final long serialVersionUID = -930632846167768864L;
    
    FinalScriptFunctionData(final String name, final int arity, final List<CompiledFunction> functions, final int flags) {
        super(name, arity, flags);
        this.code.addAll(functions);
        assert !this.needsCallee();
    }
    
    FinalScriptFunctionData(final String name, final MethodHandle mh, final Specialization[] specs, final int flags) {
        super(name, methodHandleArity(mh), flags);
        this.addInvoker(mh);
        if (specs != null) {
            for (final Specialization spec : specs) {
                this.addInvoker(spec.getMethodHandle(), spec);
            }
        }
    }
    
    protected boolean needsCallee() {
        final boolean needsCallee = this.code.getFirst().needsCallee();
        assert this.allNeedCallee(needsCallee);
        return needsCallee;
    }
    
    private boolean allNeedCallee(final boolean needCallee) {
        for (final CompiledFunction inv : this.code) {
            if (inv.needsCallee() != needCallee) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    CompiledFunction getBest(final MethodType callSiteType, final ScriptObject runtimeScope, final Collection<CompiledFunction> forbidden, final boolean linkLogicOkay) {
        assert this.isValidCallSite(callSiteType) : callSiteType;
        CompiledFunction best = null;
        for (final CompiledFunction candidate : this.code) {
            if (!linkLogicOkay && candidate.hasLinkLogic()) {
                continue;
            }
            if (forbidden.contains(candidate) || !candidate.betterThanFinal(best, callSiteType)) {
                continue;
            }
            best = candidate;
        }
        return best;
    }
    
    @Override
    MethodType getGenericType() {
        int max = 0;
        for (final CompiledFunction fn : this.code) {
            final MethodType t = fn.type();
            if (ScriptFunctionData.isVarArg(t)) {
                return MethodType.genericMethodType(2, true);
            }
            final int paramCount = t.parameterCount() - (ScriptFunctionData.needsCallee(t) ? 1 : 0);
            if (paramCount <= max) {
                continue;
            }
            max = paramCount;
        }
        return MethodType.genericMethodType(max + 1);
    }
    
    private CompiledFunction addInvoker(final MethodHandle mh, final Specialization specialization) {
        assert !ScriptFunctionData.needsCallee(mh);
        CompiledFunction invoker;
        if (isConstructor(mh)) {
            assert this.isConstructor();
            invoker = CompiledFunction.createBuiltInConstructor(mh);
        }
        else {
            invoker = new CompiledFunction(mh, null, specialization);
        }
        this.code.add(invoker);
        return invoker;
    }
    
    private CompiledFunction addInvoker(final MethodHandle mh) {
        return this.addInvoker(mh, null);
    }
    
    private static int methodHandleArity(final MethodHandle mh) {
        if (ScriptFunctionData.isVarArg(mh)) {
            return 250;
        }
        return mh.type().parameterCount() - 1 - (ScriptFunctionData.needsCallee(mh) ? 1 : 0) - (isConstructor(mh) ? 1 : 0);
    }
    
    private static boolean isConstructor(final MethodHandle mh) {
        return mh.type().parameterCount() >= 1 && mh.type().parameterType(0) == Boolean.TYPE;
    }
}
