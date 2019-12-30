// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class BoundCallableLinker implements TypeBasedGuardingDynamicLinker
{
    @Override
    public boolean canLinkType(final Class<?> type) {
        return type == BoundCallable.class;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final Object objBoundCallable = linkRequest.getReceiver();
        if (!(objBoundCallable instanceof BoundCallable)) {
            return null;
        }
        final CallSiteDescriptor descriptor = linkRequest.getCallSiteDescriptor();
        if (descriptor.getNameTokenCount() < 2 || !"dyn".equals(descriptor.getNameToken(0))) {
            return null;
        }
        final String operation = descriptor.getNameToken(1);
        boolean isCall;
        if ("new".equals(operation)) {
            isCall = false;
        }
        else {
            if (!"call".equals(operation)) {
                return null;
            }
            isCall = true;
        }
        final BoundCallable boundCallable = (BoundCallable)objBoundCallable;
        final Object callable = boundCallable.getCallable();
        final Object boundThis = boundCallable.getBoundThis();
        final Object[] args = linkRequest.getArguments();
        final Object[] boundArgs = boundCallable.getBoundArgs();
        final int argsLen = args.length;
        final int boundArgsLen = boundArgs.length;
        final Object[] newArgs = new Object[argsLen + boundArgsLen];
        newArgs[0] = callable;
        int firstArgIndex;
        if (isCall) {
            newArgs[1] = boundThis;
            firstArgIndex = 2;
        }
        else {
            firstArgIndex = 1;
        }
        System.arraycopy(boundArgs, 0, newArgs, firstArgIndex, boundArgsLen);
        System.arraycopy(args, firstArgIndex, newArgs, firstArgIndex + boundArgsLen, argsLen - firstArgIndex);
        final MethodType type = descriptor.getMethodType();
        MethodType newMethodType = descriptor.getMethodType().changeParameterType(0, callable.getClass());
        if (isCall) {
            newMethodType = newMethodType.changeParameterType(1, (boundThis == null) ? Object.class : boundThis.getClass());
        }
        int i = boundArgs.length;
        while (i-- > 0) {
            newMethodType = newMethodType.insertParameterTypes(firstArgIndex, (Class<?>[])new Class[] { (boundArgs[i] == null) ? Object.class : boundArgs[i].getClass() });
        }
        final CallSiteDescriptor newDescriptor = descriptor.changeMethodType(newMethodType);
        final GuardedInvocation inv = linkerServices.getGuardedInvocation(linkRequest.replaceArguments(newDescriptor, newArgs));
        if (inv == null) {
            return null;
        }
        final MethodHandle boundHandle = MethodHandles.insertArguments(inv.getInvocation(), 0, Arrays.copyOf(newArgs, firstArgIndex + boundArgs.length));
        final Class<?> p0Type = type.parameterType(0);
        MethodHandle droppingHandle;
        if (isCall) {
            droppingHandle = MethodHandles.dropArguments(boundHandle, 0, p0Type, type.parameterType(1));
        }
        else {
            droppingHandle = MethodHandles.dropArguments(boundHandle, 0, p0Type);
        }
        final MethodHandle newGuard = Guards.getIdentityGuard(boundCallable);
        return inv.replaceMethods(droppingHandle, newGuard.asType(newGuard.type().changeParameterType(0, p0Type)));
    }
}
