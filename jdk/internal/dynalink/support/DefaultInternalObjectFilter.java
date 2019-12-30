// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.MethodHandleTransformer;

public class DefaultInternalObjectFilter implements MethodHandleTransformer
{
    private static final MethodHandle FILTER_VARARGS;
    private final MethodHandle parameterFilter;
    private final MethodHandle returnFilter;
    private final MethodHandle varArgFilter;
    
    public DefaultInternalObjectFilter(final MethodHandle parameterFilter, final MethodHandle returnFilter) {
        this.parameterFilter = checkHandle(parameterFilter, "parameterFilter");
        this.returnFilter = checkHandle(returnFilter, "returnFilter");
        this.varArgFilter = ((parameterFilter == null) ? null : DefaultInternalObjectFilter.FILTER_VARARGS.bindTo(parameterFilter));
    }
    
    @Override
    public MethodHandle transform(final MethodHandle target) {
        assert target != null;
        MethodHandle[] filters = null;
        final MethodType type = target.type();
        final boolean isVarArg = target.isVarargsCollector();
        final int paramCount = type.parameterCount();
        MethodHandle paramsFiltered;
        if (this.parameterFilter != null) {
            int firstFilter = -1;
            for (int i = 1; i < paramCount; ++i) {
                final Class<?> paramType = type.parameterType(i);
                final boolean filterVarArg = isVarArg && i == paramCount - 1 && paramType == Object[].class;
                if (filterVarArg || paramType == Object.class) {
                    if (filters == null) {
                        firstFilter = i;
                        filters = new MethodHandle[paramCount - firstFilter];
                    }
                    filters[i - firstFilter] = (filterVarArg ? this.varArgFilter : this.parameterFilter);
                }
            }
            paramsFiltered = ((filters != null) ? MethodHandles.filterArguments(target, firstFilter, filters) : target);
        }
        else {
            paramsFiltered = target;
        }
        final MethodHandle returnFiltered = (this.returnFilter != null && type.returnType() == Object.class) ? MethodHandles.filterReturnValue(paramsFiltered, this.returnFilter) : paramsFiltered;
        return (isVarArg && !returnFiltered.isVarargsCollector()) ? returnFiltered.asVarargsCollector(type.parameterType(paramCount - 1)) : returnFiltered;
    }
    
    private static MethodHandle checkHandle(final MethodHandle handle, final String handleKind) {
        if (handle != null) {
            final MethodType objectObjectType = MethodType.methodType(Object.class, Object.class);
            if (!handle.type().equals((Object)objectObjectType)) {
                throw new IllegalArgumentException("Method type for " + handleKind + " must be " + objectObjectType);
            }
        }
        return handle;
    }
    
    private static Object[] filterVarArgs(final MethodHandle parameterFilter, final Object[] args) throws Throwable {
        Object[] newArgs = null;
        for (int i = 0; i < args.length; ++i) {
            final Object arg = args[i];
            final Object newArg = parameterFilter.invokeExact(arg);
            if (arg != newArg) {
                if (newArgs == null) {
                    newArgs = args.clone();
                }
                newArgs[i] = newArg;
            }
        }
        return (newArgs == null) ? args : newArgs;
    }
    
    static {
        FILTER_VARARGS = new Lookup(MethodHandles.lookup()).findStatic(DefaultInternalObjectFilter.class, "filterVarArgs", MethodType.methodType(Object[].class, MethodHandle.class, Object[].class));
    }
}
