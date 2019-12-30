// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class JavaSuperAdapterLinker implements TypeBasedGuardingDynamicLinker
{
    private static final String GET_METHOD = "getMethod";
    private static final String DYN_GET_METHOD = "dyn:getMethod";
    private static final String DYN_GET_METHOD_FIXED = "dyn:getMethod:super$";
    private static final MethodHandle ADD_PREFIX_TO_METHOD_NAME;
    private static final MethodHandle BIND_DYNAMIC_METHOD;
    private static final MethodHandle GET_ADAPTER;
    private static final MethodHandle IS_ADAPTER_OF_CLASS;
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return type == JavaSuperAdapter.class;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final Object objSuperAdapter = linkRequest.getReceiver();
        if (!(objSuperAdapter instanceof JavaSuperAdapter)) {
            return null;
        }
        final CallSiteDescriptor descriptor = linkRequest.getCallSiteDescriptor();
        if (!CallSiteDescriptorFactory.tokenizeOperators(descriptor).contains("getMethod")) {
            return null;
        }
        final Object adapter = ((JavaSuperAdapter)objSuperAdapter).getAdapter();
        final Object[] args = linkRequest.getArguments();
        args[0] = adapter;
        final MethodType type = descriptor.getMethodType();
        final Class<?> adapterClass = adapter.getClass();
        final boolean hasFixedName = descriptor.getNameTokenCount() > 2;
        final String opName = hasFixedName ? ("dyn:getMethod:super$" + descriptor.getNameToken(2)) : "dyn:getMethod";
        final CallSiteDescriptor newDescriptor = NashornCallSiteDescriptor.get(descriptor.getLookup(), opName, type.changeParameterType(0, adapterClass), 0);
        final GuardedInvocation guardedInv = NashornBeansLinker.getGuardedInvocation(BeansLinker.getLinkerForClass(adapterClass), linkRequest.replaceArguments(newDescriptor, args), linkerServices);
        final MethodHandle guard = JavaSuperAdapterLinker.IS_ADAPTER_OF_CLASS.bindTo(adapterClass);
        if (guardedInv == null) {
            return new GuardedInvocation(MethodHandles.dropArguments(Lookup.EMPTY_GETTER, 1, type.parameterList().subList(1, type.parameterCount())), guard).asType(descriptor);
        }
        final MethodHandle invocation = guardedInv.getInvocation();
        final MethodType invType = invocation.type();
        final MethodHandle typedBinder = JavaSuperAdapterLinker.BIND_DYNAMIC_METHOD.asType(MethodType.methodType(Object.class, invType.returnType(), invType.parameterType(0)));
        final MethodHandle droppingBinder = MethodHandles.dropArguments(typedBinder, 2, invType.parameterList().subList(1, invType.parameterCount()));
        final MethodHandle bindingInvocation = MethodHandles.foldArguments(droppingBinder, invocation);
        final MethodHandle typedGetAdapter = asFilterType(JavaSuperAdapterLinker.GET_ADAPTER, 0, invType, type);
        MethodHandle adaptedInvocation;
        if (hasFixedName) {
            adaptedInvocation = MethodHandles.filterArguments(bindingInvocation, 0, typedGetAdapter);
        }
        else {
            final MethodHandle typedAddPrefix = asFilterType(JavaSuperAdapterLinker.ADD_PREFIX_TO_METHOD_NAME, 1, invType, type);
            adaptedInvocation = MethodHandles.filterArguments(bindingInvocation, 0, typedGetAdapter, typedAddPrefix);
        }
        return guardedInv.replaceMethods(adaptedInvocation, guard).asType(descriptor);
    }
    
    private static MethodHandle asFilterType(final MethodHandle filter, final int pos, final MethodType targetType, final MethodType sourceType) {
        return filter.asType(MethodType.methodType(targetType.parameterType(pos), sourceType.parameterType(pos)));
    }
    
    private static Object addPrefixToMethodName(final Object name) {
        return "super$".concat(String.valueOf(name));
    }
    
    private static Object bindDynamicMethod(final Object dynamicMethod, final Object boundThis) {
        return (dynamicMethod == null) ? ScriptRuntime.UNDEFINED : Bootstrap.bindCallable(dynamicMethod, boundThis, null);
    }
    
    private static boolean isAdapterOfClass(final Class<?> clazz, final Object obj) {
        return obj instanceof JavaSuperAdapter && clazz == ((JavaSuperAdapter)obj).getAdapter().getClass();
    }
    
    static {
        final jdk.internal.dynalink.support.Lookup lookup = new jdk.internal.dynalink.support.Lookup(MethodHandles.lookup());
        ADD_PREFIX_TO_METHOD_NAME = lookup.findOwnStatic("addPrefixToMethodName", Object.class, Object.class);
        BIND_DYNAMIC_METHOD = lookup.findOwnStatic("bindDynamicMethod", Object.class, Object.class, Object.class);
        GET_ADAPTER = lookup.findVirtual(JavaSuperAdapter.class, "getAdapter", MethodType.methodType(Object.class));
        IS_ADAPTER_OF_CLASS = lookup.findOwnStatic("isAdapterOfClass", Boolean.TYPE, Class.class, Object.class);
    }
}
