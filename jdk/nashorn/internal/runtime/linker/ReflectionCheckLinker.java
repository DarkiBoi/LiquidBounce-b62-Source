// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.security.Permission;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import java.lang.reflect.Modifier;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.Context;
import java.lang.reflect.Proxy;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class ReflectionCheckLinker implements TypeBasedGuardingDynamicLinker
{
    private static final Class<?> STATEMENT_CLASS;
    private static final Class<?> XMLENCODER_CLASS;
    private static final Class<?> XMLDECODER_CLASS;
    
    private static Class<?> getBeanClass(final String name) {
        try {
            return Class.forName("java.beans." + name);
        }
        catch (ClassNotFoundException cnfe) {
            return null;
        }
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return isReflectionClass(type);
    }
    
    private static boolean isReflectionClass(final Class<?> type) {
        if (type == Class.class || ClassLoader.class.isAssignableFrom(type)) {
            return true;
        }
        if ((ReflectionCheckLinker.STATEMENT_CLASS != null && ReflectionCheckLinker.STATEMENT_CLASS.isAssignableFrom(type)) || (ReflectionCheckLinker.XMLENCODER_CLASS != null && ReflectionCheckLinker.XMLENCODER_CLASS.isAssignableFrom(type)) || (ReflectionCheckLinker.XMLDECODER_CLASS != null && ReflectionCheckLinker.XMLDECODER_CLASS.isAssignableFrom(type))) {
            return true;
        }
        final String name = type.getName();
        return name.startsWith("java.lang.reflect.") || name.startsWith("java.lang.invoke.");
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest origRequest, final LinkerServices linkerServices) throws Exception {
        checkLinkRequest(origRequest);
        return null;
    }
    
    private static boolean isReflectiveCheckNeeded(final Class<?> type, final boolean isStatic) {
        if (Proxy.class.isAssignableFrom(type)) {
            return !Proxy.isProxyClass(type) || isStatic;
        }
        return isReflectionClass(type);
    }
    
    static void checkReflectionAccess(final Class<?> clazz, final boolean isStatic) {
        final Global global = Context.getGlobal();
        final ClassFilter cf = global.getClassFilter();
        if (cf != null && isReflectiveCheckNeeded(clazz, isStatic)) {
            throw ECMAErrors.typeError("no.reflection.with.classfilter", new String[0]);
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null && isReflectiveCheckNeeded(clazz, isStatic)) {
            checkReflectionPermission(sm);
        }
    }
    
    private static void checkLinkRequest(final LinkRequest origRequest) {
        final Global global = Context.getGlobal();
        final ClassFilter cf = global.getClassFilter();
        if (cf != null) {
            throw ECMAErrors.typeError("no.reflection.with.classfilter", new String[0]);
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            final LinkRequest requestWithoutContext = origRequest.withoutRuntimeContext();
            final Object self = requestWithoutContext.getReceiver();
            if (self instanceof Class && Modifier.isPublic(((Class)self).getModifiers())) {
                final CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
                if (CallSiteDescriptorFactory.tokenizeOperators(desc).contains("getProp") && desc.getNameTokenCount() > 2 && "static".equals(desc.getNameToken(2)) && Context.isAccessibleClass((Class<?>)self) && !isReflectionClass((Class<?>)self)) {
                    return;
                }
            }
            checkReflectionPermission(sm);
        }
    }
    
    private static void checkReflectionPermission(final SecurityManager sm) {
        sm.checkPermission(new RuntimePermission("nashorn.JavaReflection"));
    }
    
    static {
        STATEMENT_CLASS = getBeanClass("Statement");
        XMLENCODER_CLASS = getBeanClass("XMLEncoder");
        XMLDECODER_CLASS = getBeanClass("XMLDecoder");
    }
}
