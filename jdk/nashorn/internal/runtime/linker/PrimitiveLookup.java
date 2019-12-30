// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.UserAccessorProperty;
import jdk.nashorn.internal.runtime.GlobalConstants;
import jdk.nashorn.internal.runtime.Context;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.invoke.MethodHandle;

public final class PrimitiveLookup
{
    private static final MethodHandle PRIMITIVE_SETTER;
    
    private PrimitiveLookup() {
    }
    
    public static GuardedInvocation lookupPrimitive(final LinkRequest request, final Class<?> receiverClass, final ScriptObject wrappedReceiver, final MethodHandle wrapFilter, final MethodHandle protoFilter) {
        return lookupPrimitive(request, Guards.getInstanceOfGuard(receiverClass), wrappedReceiver, wrapFilter, protoFilter);
    }
    
    public static GuardedInvocation lookupPrimitive(final LinkRequest request, final MethodHandle guard, final ScriptObject wrappedReceiver, final MethodHandle wrapFilter, final MethodHandle protoFilter) {
        final CallSiteDescriptor desc = request.getCallSiteDescriptor();
        String name;
        FindProperty find;
        if (desc.getNameTokenCount() > 2) {
            name = desc.getNameToken(2);
            find = wrappedReceiver.findProperty(name, true);
        }
        else {
            name = null;
            find = null;
        }
        final String s;
        final String firstOp = s = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        switch (s) {
            case "getProp":
            case "getElem":
            case "getMethod": {
                if (name == null) {
                    break;
                }
                if (find == null) {
                    return null;
                }
                final SwitchPoint sp = find.getProperty().getBuiltinSwitchPoint();
                if (sp instanceof Context.BuiltinSwitchPoint && !sp.hasBeenInvalidated()) {
                    return new GuardedInvocation(GlobalConstants.staticConstantGetter(find.getObjectValue()), guard, sp, null);
                }
                if (find.isInherited() && !(find.getProperty() instanceof UserAccessorProperty)) {
                    final ScriptObject proto = wrappedReceiver.getProto();
                    final GuardedInvocation link = proto.lookup(desc, request);
                    if (link != null) {
                        final MethodHandle invocation = link.getInvocation();
                        final MethodHandle adaptedInvocation = Lookup.MH.asType(invocation, invocation.type().changeParameterType(0, (Class<?>)Object.class));
                        final MethodHandle method = Lookup.MH.filterArguments(adaptedInvocation, 0, protoFilter);
                        final MethodHandle protoGuard = Lookup.MH.filterArguments(link.getGuard(), 0, protoFilter);
                        return new GuardedInvocation(method, NashornGuards.combineGuards(guard, protoGuard));
                    }
                }
                break;
            }
            case "setProp":
            case "setElem": {
                return getPrimitiveSetter(name, guard, wrapFilter, NashornCallSiteDescriptor.isStrict(desc));
            }
        }
        final GuardedInvocation link2 = wrappedReceiver.lookup(desc, request);
        if (link2 != null) {
            MethodHandle method2 = link2.getInvocation();
            final Class<?> receiverType = method2.type().parameterType(0);
            if (receiverType != Object.class) {
                final MethodType wrapType = wrapFilter.type();
                assert receiverType.isAssignableFrom(wrapType.returnType());
                method2 = Lookup.MH.filterArguments(method2, 0, Lookup.MH.asType(wrapFilter, wrapType.changeReturnType(receiverType)));
            }
            return new GuardedInvocation(method2, guard, link2.getSwitchPoints(), null);
        }
        return null;
    }
    
    private static GuardedInvocation getPrimitiveSetter(final String name, final MethodHandle guard, final MethodHandle wrapFilter, final boolean isStrict) {
        MethodHandle filter = Lookup.MH.asType(wrapFilter, wrapFilter.type().changeReturnType((Class<?>)ScriptObject.class));
        MethodHandle target;
        if (name == null) {
            filter = Lookup.MH.dropArguments(filter, 1, Object.class, Object.class);
            target = Lookup.MH.insertArguments(PrimitiveLookup.PRIMITIVE_SETTER, 3, isStrict);
        }
        else {
            filter = Lookup.MH.dropArguments(filter, 1, Object.class);
            target = Lookup.MH.insertArguments(PrimitiveLookup.PRIMITIVE_SETTER, 2, name, isStrict);
        }
        return new GuardedInvocation(Lookup.MH.foldArguments(target, filter), guard);
    }
    
    private static void primitiveSetter(final ScriptObject wrappedSelf, final Object self, final Object key, final boolean strict, final Object value) {
        final String name = JSType.toString(key);
        final FindProperty find = wrappedSelf.findProperty(name, true);
        if (find != null && find.getProperty() instanceof UserAccessorProperty && find.getProperty().isWritable()) {
            find.setValue(value, strict);
            return;
        }
        if (strict) {
            throw ECMAErrors.typeError("property.not.writable", name, ScriptRuntime.safeToString(self));
        }
    }
    
    private static MethodHandle findOwnMH(final String name, final MethodType type) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), PrimitiveLookup.class, name, type);
    }
    
    static {
        PRIMITIVE_SETTER = findOwnMH("primitiveSetter", Lookup.MH.type(Void.TYPE, ScriptObject.class, Object.class, Object.class, Boolean.TYPE, Object.class));
    }
}
