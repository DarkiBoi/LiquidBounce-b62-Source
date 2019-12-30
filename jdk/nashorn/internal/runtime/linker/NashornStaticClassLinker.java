// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.lang.reflect.Modifier;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class NashornStaticClassLinker implements TypeBasedGuardingDynamicLinker
{
    private static final GuardingDynamicLinker staticClassLinker;
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return type == StaticClass.class;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final LinkRequest request = linkRequest.withoutRuntimeContext();
        final Object self = request.getReceiver();
        if (self.getClass() != StaticClass.class) {
            return null;
        }
        final Class<?> receiverClass = ((StaticClass)self).getRepresentedClass();
        Bootstrap.checkReflectionAccess(receiverClass, true);
        final CallSiteDescriptor desc = request.getCallSiteDescriptor();
        if (!"new".equals(desc.getNameToken(1))) {
            return delegate(linkerServices, request);
        }
        if (!Modifier.isPublic(receiverClass.getModifiers())) {
            throw ECMAErrors.typeError("new.on.nonpublic.javatype", receiverClass.getName());
        }
        Context.checkPackageAccess(receiverClass);
        if (NashornLinker.isAbstractClass(receiverClass)) {
            final Object[] args = request.getArguments();
            args[0] = JavaAdapterFactory.getAdapterClassFor(new Class[] { receiverClass }, null, linkRequest.getCallSiteDescriptor().getLookup());
            final LinkRequest adapterRequest = request.replaceArguments(request.getCallSiteDescriptor(), args);
            final GuardedInvocation gi = checkNullConstructor(delegate(linkerServices, adapterRequest), receiverClass);
            return gi.replaceMethods(gi.getInvocation(), Guards.getIdentityGuard(self));
        }
        return checkNullConstructor(delegate(linkerServices, request), receiverClass);
    }
    
    private static GuardedInvocation delegate(final LinkerServices linkerServices, final LinkRequest request) throws Exception {
        return NashornBeansLinker.getGuardedInvocation(NashornStaticClassLinker.staticClassLinker, request, linkerServices);
    }
    
    private static GuardedInvocation checkNullConstructor(final GuardedInvocation ctorInvocation, final Class<?> receiverClass) {
        if (ctorInvocation == null) {
            throw ECMAErrors.typeError("no.constructor.matches.args", receiverClass.getName());
        }
        return ctorInvocation;
    }
    
    static {
        staticClassLinker = BeansLinker.getLinkerForClass(StaticClass.class);
    }
}
