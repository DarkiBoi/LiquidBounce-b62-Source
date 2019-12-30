// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

class DynamicMethodLinker implements TypeBasedGuardingDynamicLinker
{
    @Override
    public boolean canLinkType(final Class<?> type) {
        return DynamicMethod.class.isAssignableFrom(type);
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) {
        final Object receiver = linkRequest.getReceiver();
        if (!(receiver instanceof DynamicMethod)) {
            return null;
        }
        final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (desc.getNameTokenCount() != 2 && desc.getNameToken(0) != "dyn") {
            return null;
        }
        final String operator = desc.getNameToken(1);
        final DynamicMethod dynMethod = (DynamicMethod)receiver;
        final boolean constructor = dynMethod.isConstructor();
        MethodHandle invocation;
        if (operator == "call" && !constructor) {
            invocation = dynMethod.getInvocation(CallSiteDescriptorFactory.dropParameterTypes(desc, 0, 1), linkerServices);
        }
        else {
            if (operator != "new" || !constructor) {
                return null;
            }
            final MethodHandle ctorInvocation = dynMethod.getInvocation(desc, linkerServices);
            if (ctorInvocation == null) {
                return null;
            }
            invocation = MethodHandles.insertArguments(ctorInvocation, 0, null);
        }
        if (invocation != null) {
            return new GuardedInvocation(MethodHandles.dropArguments(invocation, 0, desc.getMethodType().parameterType(0)), Guards.getIdentityGuard(receiver));
        }
        return null;
    }
}
