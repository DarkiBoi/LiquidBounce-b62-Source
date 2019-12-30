// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.security.Permission;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.ConversionComparator;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.MethodHandleTransformer;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

public class LinkerServicesImpl implements LinkerServices
{
    private static final RuntimePermission GET_CURRENT_LINK_REQUEST;
    private static final ThreadLocal<LinkRequest> threadLinkRequest;
    private final TypeConverterFactory typeConverterFactory;
    private final GuardingDynamicLinker topLevelLinker;
    private final MethodHandleTransformer internalObjectsFilter;
    
    public LinkerServicesImpl(final TypeConverterFactory typeConverterFactory, final GuardingDynamicLinker topLevelLinker, final MethodHandleTransformer internalObjectsFilter) {
        this.typeConverterFactory = typeConverterFactory;
        this.topLevelLinker = topLevelLinker;
        this.internalObjectsFilter = internalObjectsFilter;
    }
    
    @Override
    public boolean canConvert(final Class<?> from, final Class<?> to) {
        return this.typeConverterFactory.canConvert(from, to);
    }
    
    @Override
    public MethodHandle asType(final MethodHandle handle, final MethodType fromType) {
        return this.typeConverterFactory.asType(handle, fromType);
    }
    
    @Override
    public MethodHandle asTypeLosslessReturn(final MethodHandle handle, final MethodType fromType) {
        return Implementation.asTypeLosslessReturn(this, handle, fromType);
    }
    
    @Override
    public MethodHandle getTypeConverter(final Class<?> sourceType, final Class<?> targetType) {
        return this.typeConverterFactory.getTypeConverter(sourceType, targetType);
    }
    
    @Override
    public ConversionComparator.Comparison compareConversion(final Class<?> sourceType, final Class<?> targetType1, final Class<?> targetType2) {
        return this.typeConverterFactory.compareConversion(sourceType, targetType1, targetType2);
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest) throws Exception {
        final LinkRequest prevLinkRequest = LinkerServicesImpl.threadLinkRequest.get();
        LinkerServicesImpl.threadLinkRequest.set(linkRequest);
        try {
            return this.topLevelLinker.getGuardedInvocation(linkRequest, this);
        }
        finally {
            LinkerServicesImpl.threadLinkRequest.set(prevLinkRequest);
        }
    }
    
    @Override
    public MethodHandle filterInternalObjects(final MethodHandle target) {
        return (this.internalObjectsFilter != null) ? this.internalObjectsFilter.transform(target) : target;
    }
    
    public static LinkRequest getCurrentLinkRequest() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(LinkerServicesImpl.GET_CURRENT_LINK_REQUEST);
        }
        return LinkerServicesImpl.threadLinkRequest.get();
    }
    
    static {
        GET_CURRENT_LINK_REQUEST = new RuntimePermission("dynalink.getCurrentLinkRequest");
        threadLinkRequest = new ThreadLocal<LinkRequest>();
    }
}
