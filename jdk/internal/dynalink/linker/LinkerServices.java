// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.support.TypeUtilities;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

public interface LinkerServices
{
    MethodHandle asType(final MethodHandle p0, final MethodType p1);
    
    MethodHandle asTypeLosslessReturn(final MethodHandle p0, final MethodType p1);
    
    MethodHandle getTypeConverter(final Class<?> p0, final Class<?> p1);
    
    boolean canConvert(final Class<?> p0, final Class<?> p1);
    
    GuardedInvocation getGuardedInvocation(final LinkRequest p0) throws Exception;
    
    ConversionComparator.Comparison compareConversion(final Class<?> p0, final Class<?> p1, final Class<?> p2);
    
    MethodHandle filterInternalObjects(final MethodHandle p0);
    
    public static class Implementation
    {
        public static MethodHandle asTypeLosslessReturn(final LinkerServices linkerServices, final MethodHandle handle, final MethodType fromType) {
            final Class<?> handleReturnType = handle.type().returnType();
            return linkerServices.asType(handle, TypeUtilities.isConvertibleWithoutLoss(handleReturnType, fromType.returnType()) ? fromType : fromType.changeReturnType(handleReturnType));
        }
    }
}
