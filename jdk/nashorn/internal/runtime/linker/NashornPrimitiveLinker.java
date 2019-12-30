// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.objects.Global;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.runtime.ConsString;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class NashornPrimitiveLinker implements TypeBasedGuardingDynamicLinker, GuardingTypeConverterFactory, ConversionComparator
{
    private static final GuardedTypeConversion VOID_TO_OBJECT;
    private static final MethodHandle GUARD_PRIMITIVE;
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return canLinkTypeStatic(type);
    }
    
    private static boolean canLinkTypeStatic(final Class<?> type) {
        return type == String.class || type == Boolean.class || type == ConsString.class || type == Integer.class || type == Double.class || type == Float.class || type == Short.class || type == Byte.class;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest origRequest, final LinkerServices linkerServices) throws Exception {
        final LinkRequest request = origRequest.withoutRuntimeContext();
        final Object self = request.getReceiver();
        final NashornCallSiteDescriptor desc = (NashornCallSiteDescriptor)request.getCallSiteDescriptor();
        return Bootstrap.asTypeSafeReturn(Global.primitiveLookup(request, self), linkerServices, desc);
    }
    
    @Override
    public GuardedTypeConversion convertToType(final Class<?> sourceType, final Class<?> targetType) {
        final MethodHandle mh = JavaArgumentConverters.getConverter(targetType);
        if (mh != null) {
            return new GuardedTypeConversion(new GuardedInvocation(mh, canLinkTypeStatic(sourceType) ? null : NashornPrimitiveLinker.GUARD_PRIMITIVE).asType(mh.type().changeParameterType(0, sourceType)), true);
        }
        if (targetType == Object.class && sourceType == Void.TYPE) {
            return NashornPrimitiveLinker.VOID_TO_OBJECT;
        }
        return null;
    }
    
    @Override
    public Comparison compareConversion(final Class<?> sourceType, final Class<?> targetType1, final Class<?> targetType2) {
        final Class<?> wrapper1 = getWrapperTypeOrSelf(targetType1);
        if (sourceType == wrapper1) {
            return Comparison.TYPE_1_BETTER;
        }
        final Class<?> wrapper2 = getWrapperTypeOrSelf(targetType2);
        if (sourceType == wrapper2) {
            return Comparison.TYPE_2_BETTER;
        }
        if (Number.class.isAssignableFrom(sourceType)) {
            if (Number.class.isAssignableFrom(wrapper1)) {
                if (!Number.class.isAssignableFrom(wrapper2)) {
                    return Comparison.TYPE_1_BETTER;
                }
            }
            else if (Number.class.isAssignableFrom(wrapper2)) {
                return Comparison.TYPE_2_BETTER;
            }
            if (Character.class == wrapper1) {
                return Comparison.TYPE_1_BETTER;
            }
            if (Character.class == wrapper2) {
                return Comparison.TYPE_2_BETTER;
            }
        }
        if (sourceType == String.class || sourceType == Boolean.class || Number.class.isAssignableFrom(sourceType)) {
            final Class<?> primitiveType1 = getPrimitiveTypeOrSelf(targetType1);
            final Class<?> primitiveType2 = getPrimitiveTypeOrSelf(targetType2);
            if (TypeUtilities.isMethodInvocationConvertible(primitiveType1, primitiveType2)) {
                return Comparison.TYPE_2_BETTER;
            }
            if (TypeUtilities.isMethodInvocationConvertible(primitiveType2, primitiveType1)) {
                return Comparison.TYPE_1_BETTER;
            }
            if (targetType1 == String.class) {
                return Comparison.TYPE_1_BETTER;
            }
            if (targetType2 == String.class) {
                return Comparison.TYPE_2_BETTER;
            }
        }
        return Comparison.INDETERMINATE;
    }
    
    private static Class<?> getPrimitiveTypeOrSelf(final Class<?> type) {
        final Class<?> primitive = TypeUtilities.getPrimitiveType(type);
        return (primitive == null) ? type : primitive;
    }
    
    private static Class<?> getWrapperTypeOrSelf(final Class<?> type) {
        final Class<?> wrapper = TypeUtilities.getWrapperType(type);
        return (wrapper == null) ? type : wrapper;
    }
    
    private static boolean isJavaScriptPrimitive(final Object o) {
        return JSType.isString(o) || o instanceof Boolean || JSType.isNumber(o) || o == null;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NashornPrimitiveLinker.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        VOID_TO_OBJECT = new GuardedTypeConversion(new GuardedInvocation(MethodHandles.constant(Object.class, ScriptRuntime.UNDEFINED)), true);
        GUARD_PRIMITIVE = findOwnMH("isJavaScriptPrimitive", Boolean.TYPE, Object.class);
    }
}
