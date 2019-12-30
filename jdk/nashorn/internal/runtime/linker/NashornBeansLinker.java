// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.internal.dynalink.linker.ConversionComparator;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.internal.dynalink.support.DefaultInternalObjectFilter;
import jdk.internal.dynalink.linker.MethodHandleTransformer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.beans.BeansLinker;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

public class NashornBeansLinker implements GuardingDynamicLinker
{
    private static final boolean MIRROR_ALWAYS;
    private static final MethodHandle EXPORT_ARGUMENT;
    private static final MethodHandle IMPORT_RESULT;
    private static final MethodHandle FILTER_CONSSTRING;
    private static final ClassValue<String> FUNCTIONAL_IFACE_METHOD_NAME;
    private final BeansLinker beansLinker;
    
    public NashornBeansLinker() {
        this.beansLinker = new BeansLinker();
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final Object self = linkRequest.getReceiver();
        final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (self instanceof ConsString) {
            final Object[] arguments = linkRequest.getArguments();
            arguments[0] = "";
            final LinkRequest forgedLinkRequest = linkRequest.replaceArguments(desc, arguments);
            final GuardedInvocation invocation = getGuardedInvocation(this.beansLinker, forgedLinkRequest, linkerServices);
            return (invocation == null) ? null : invocation.filterArguments(0, NashornBeansLinker.FILTER_CONSSTRING);
        }
        if (self != null && "call".equals(desc.getNameToken(1))) {
            final String name = getFunctionalInterfaceMethodName(self.getClass());
            if (name != null) {
                final MethodType callType = desc.getMethodType();
                final NashornCallSiteDescriptor newDesc = NashornCallSiteDescriptor.get(desc.getLookup(), "dyn:callMethod:" + name, desc.getMethodType().dropParameterTypes(1, 2), NashornCallSiteDescriptor.getFlags(desc));
                final GuardedInvocation gi = getGuardedInvocation(this.beansLinker, linkRequest.replaceArguments(newDesc, linkRequest.getArguments()), new NashornBeansLinkerServices(linkerServices));
                return gi.replaceMethods(Lookup.MH.dropArguments(linkerServices.filterInternalObjects(gi.getInvocation()), 1, callType.parameterType(1)), gi.getGuard());
            }
        }
        return getGuardedInvocation(this.beansLinker, linkRequest, linkerServices);
    }
    
    public static GuardedInvocation getGuardedInvocation(final GuardingDynamicLinker delegateLinker, final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        return delegateLinker.getGuardedInvocation(linkRequest, new NashornBeansLinkerServices(linkerServices));
    }
    
    private static Object exportArgument(final Object arg) {
        return exportArgument(arg, NashornBeansLinker.MIRROR_ALWAYS);
    }
    
    static Object exportArgument(final Object arg, final boolean mirrorAlways) {
        if (arg instanceof ConsString) {
            return arg.toString();
        }
        if (mirrorAlways && arg instanceof ScriptObject) {
            return ScriptUtils.wrap(arg);
        }
        return arg;
    }
    
    private static Object importResult(final Object arg) {
        return ScriptUtils.unwrap(arg);
    }
    
    private static Object consStringFilter(final Object arg) {
        return (arg instanceof ConsString) ? arg.toString() : arg;
    }
    
    private static String findFunctionalInterfaceMethodName(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        for (final Class<?> iface : clazz.getInterfaces()) {
            if (Context.isAccessibleClass(iface)) {
                if (iface.isAnnotationPresent(FunctionalInterface.class)) {
                    for (final Method m : iface.getMethods()) {
                        if (Modifier.isAbstract(m.getModifiers()) && !isOverridableObjectMethod(m)) {
                            return m.getName();
                        }
                    }
                }
            }
        }
        return findFunctionalInterfaceMethodName(clazz.getSuperclass());
    }
    
    private static boolean isOverridableObjectMethod(final Method m) {
        final String name = m.getName();
        switch (name) {
            case "equals": {
                if (m.getReturnType() == Boolean.TYPE) {
                    final Class<?>[] params = m.getParameterTypes();
                    return params.length == 1 && params[0] == Object.class;
                }
                return false;
            }
            case "hashCode": {
                return m.getReturnType() == Integer.TYPE && m.getParameterCount() == 0;
            }
            case "toString": {
                return m.getReturnType() == String.class && m.getParameterCount() == 0;
            }
            default: {
                return false;
            }
        }
    }
    
    static String getFunctionalInterfaceMethodName(final Class<?> clazz) {
        return NashornBeansLinker.FUNCTIONAL_IFACE_METHOD_NAME.get(clazz);
    }
    
    static MethodHandleTransformer createHiddenObjectFilter() {
        return new DefaultInternalObjectFilter(NashornBeansLinker.EXPORT_ARGUMENT, NashornBeansLinker.MIRROR_ALWAYS ? NashornBeansLinker.IMPORT_RESULT : null);
    }
    
    static {
        MIRROR_ALWAYS = Options.getBooleanProperty("nashorn.mirror.always", true);
        final jdk.internal.dynalink.support.Lookup lookup = new jdk.internal.dynalink.support.Lookup(MethodHandles.lookup());
        EXPORT_ARGUMENT = lookup.findOwnStatic("exportArgument", Object.class, Object.class);
        IMPORT_RESULT = lookup.findOwnStatic("importResult", Object.class, Object.class);
        FILTER_CONSSTRING = lookup.findOwnStatic("consStringFilter", Object.class, Object.class);
        FUNCTIONAL_IFACE_METHOD_NAME = new ClassValue<String>() {
            @Override
            protected String computeValue(final Class<?> type) {
                return findFunctionalInterfaceMethodName(type);
            }
        };
    }
    
    private static class NashornBeansLinkerServices implements LinkerServices
    {
        private final LinkerServices linkerServices;
        
        NashornBeansLinkerServices(final LinkerServices linkerServices) {
            this.linkerServices = linkerServices;
        }
        
        @Override
        public MethodHandle asType(final MethodHandle handle, final MethodType fromType) {
            return this.linkerServices.asType(handle, fromType);
        }
        
        @Override
        public MethodHandle asTypeLosslessReturn(final MethodHandle handle, final MethodType fromType) {
            return Implementation.asTypeLosslessReturn(this, handle, fromType);
        }
        
        @Override
        public MethodHandle getTypeConverter(final Class<?> sourceType, final Class<?> targetType) {
            return this.linkerServices.getTypeConverter(sourceType, targetType);
        }
        
        @Override
        public boolean canConvert(final Class<?> from, final Class<?> to) {
            return this.linkerServices.canConvert(from, to);
        }
        
        @Override
        public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest) throws Exception {
            return this.linkerServices.getGuardedInvocation(linkRequest);
        }
        
        @Override
        public ConversionComparator.Comparison compareConversion(final Class<?> sourceType, final Class<?> targetType1, final Class<?> targetType2) {
            if (sourceType == ConsString.class) {
                if (String.class == targetType1 || CharSequence.class == targetType1) {
                    return ConversionComparator.Comparison.TYPE_1_BETTER;
                }
                if (String.class == targetType2 || CharSequence.class == targetType2) {
                    return ConversionComparator.Comparison.TYPE_2_BETTER;
                }
            }
            return this.linkerServices.compareConversion(sourceType, targetType1, targetType2);
        }
        
        @Override
        public MethodHandle filterInternalObjects(final MethodHandle target) {
            return this.linkerServices.filterInternalObjects(target);
        }
    }
}
