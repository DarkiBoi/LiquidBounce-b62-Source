// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.util.HashMap;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.util.Map;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

final class NashornBottomLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory
{
    private static final MethodHandle EMPTY_PROP_GETTER;
    private static final MethodHandle EMPTY_ELEM_GETTER;
    private static final MethodHandle EMPTY_PROP_SETTER;
    private static final MethodHandle EMPTY_ELEM_SETTER;
    private static final Map<Class<?>, MethodHandle> CONVERTERS;
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final Object self = linkRequest.getReceiver();
        if (self == null) {
            return linkNull(linkRequest);
        }
        assert isExpectedObject(self) : "Couldn't link " + linkRequest.getCallSiteDescriptor() + " for " + self.getClass().getName();
        return linkBean(linkRequest, linkerServices);
    }
    
    private static GuardedInvocation linkBean(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final NashornCallSiteDescriptor desc = (NashornCallSiteDescriptor)linkRequest.getCallSiteDescriptor();
        final Object self = linkRequest.getReceiver();
        final String firstOperator;
        final String operator = firstOperator = desc.getFirstOperator();
        switch (firstOperator) {
            case "new": {
                if (BeansLinker.isDynamicConstructor(self)) {
                    throw ECMAErrors.typeError("no.constructor.matches.args", ScriptRuntime.safeToString(self));
                }
                if (BeansLinker.isDynamicMethod(self)) {
                    throw ECMAErrors.typeError("method.not.constructor", ScriptRuntime.safeToString(self));
                }
                throw ECMAErrors.typeError("not.a.function", desc.getFunctionErrorMessage(self));
            }
            case "call": {
                if (BeansLinker.isDynamicConstructor(self)) {
                    throw ECMAErrors.typeError("constructor.requires.new", ScriptRuntime.safeToString(self));
                }
                if (BeansLinker.isDynamicMethod(self)) {
                    throw ECMAErrors.typeError("no.method.matches.args", ScriptRuntime.safeToString(self));
                }
                throw ECMAErrors.typeError("not.a.function", desc.getFunctionErrorMessage(self));
            }
            case "callMethod": {
                throw ECMAErrors.typeError("no.such.function", getArgument(linkRequest), ScriptRuntime.safeToString(self));
            }
            case "getMethod": {
                return getInvocation(Lookup.MH.dropArguments(JSType.GET_UNDEFINED.get(2), 0, Object.class), self, linkerServices, desc);
            }
            case "getProp":
            case "getElem": {
                if (NashornCallSiteDescriptor.isOptimistic(desc)) {
                    throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, NashornCallSiteDescriptor.getProgramPoint(desc), Type.OBJECT);
                }
                if (desc.getOperand() != null) {
                    return getInvocation(NashornBottomLinker.EMPTY_PROP_GETTER, self, linkerServices, desc);
                }
                return getInvocation(NashornBottomLinker.EMPTY_ELEM_GETTER, self, linkerServices, desc);
            }
            case "setProp":
            case "setElem": {
                final boolean strict = NashornCallSiteDescriptor.isStrict(desc);
                if (strict) {
                    throw ECMAErrors.typeError("cant.set.property", getArgument(linkRequest), ScriptRuntime.safeToString(self));
                }
                if (desc.getOperand() != null) {
                    return getInvocation(NashornBottomLinker.EMPTY_PROP_SETTER, self, linkerServices, desc);
                }
                return getInvocation(NashornBottomLinker.EMPTY_ELEM_SETTER, self, linkerServices, desc);
            }
            default: {
                throw new AssertionError((Object)("unknown call type " + desc));
            }
        }
    }
    
    @Override
    public GuardedTypeConversion convertToType(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        final GuardedInvocation gi = convertToTypeNoCast(sourceType, targetType);
        return (gi == null) ? null : new GuardedTypeConversion(gi.asType(Lookup.MH.type(targetType, sourceType)), true);
    }
    
    private static GuardedInvocation convertToTypeNoCast(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        final MethodHandle mh = NashornBottomLinker.CONVERTERS.get(targetType);
        if (mh != null) {
            return new GuardedInvocation(mh);
        }
        return null;
    }
    
    private static GuardedInvocation getInvocation(final MethodHandle handle, final Object self, final LinkerServices linkerServices, final CallSiteDescriptor desc) {
        return Bootstrap.asTypeSafeReturn(new GuardedInvocation(handle, Guards.getClassGuard(self.getClass())), linkerServices, desc);
    }
    
    private static boolean isExpectedObject(final Object obj) {
        return !NashornLinker.canLinkTypeStatic(obj.getClass());
    }
    
    private static GuardedInvocation linkNull(final LinkRequest linkRequest) {
        final NashornCallSiteDescriptor desc = (NashornCallSiteDescriptor)linkRequest.getCallSiteDescriptor();
        final String firstOperator;
        final String operator = firstOperator = desc.getFirstOperator();
        switch (firstOperator) {
            case "new":
            case "call": {
                throw ECMAErrors.typeError("not.a.function", "null");
            }
            case "callMethod":
            case "getMethod": {
                throw ECMAErrors.typeError("no.such.function", getArgument(linkRequest), "null");
            }
            case "getProp":
            case "getElem": {
                throw ECMAErrors.typeError("cant.get.property", getArgument(linkRequest), "null");
            }
            case "setProp":
            case "setElem": {
                throw ECMAErrors.typeError("cant.set.property", getArgument(linkRequest), "null");
            }
            default: {
                throw new AssertionError((Object)("unknown call type " + desc));
            }
        }
    }
    
    private static String getArgument(final LinkRequest linkRequest) {
        final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (desc.getNameTokenCount() > 2) {
            return desc.getNameToken(2);
        }
        return ScriptRuntime.safeToString(linkRequest.getArguments()[1]);
    }
    
    static {
        EMPTY_PROP_GETTER = Lookup.MH.dropArguments(Lookup.MH.constant(Object.class, ScriptRuntime.UNDEFINED), 0, Object.class);
        EMPTY_ELEM_GETTER = Lookup.MH.dropArguments(NashornBottomLinker.EMPTY_PROP_GETTER, 0, Object.class);
        EMPTY_PROP_SETTER = Lookup.MH.asType(NashornBottomLinker.EMPTY_ELEM_GETTER, NashornBottomLinker.EMPTY_ELEM_GETTER.type().changeReturnType((Class<?>)Void.TYPE));
        EMPTY_ELEM_SETTER = Lookup.MH.dropArguments(NashornBottomLinker.EMPTY_PROP_SETTER, 0, Object.class);
        (CONVERTERS = new HashMap<Class<?>, MethodHandle>()).put(Boolean.TYPE, JSType.TO_BOOLEAN.methodHandle());
        NashornBottomLinker.CONVERTERS.put(Double.TYPE, JSType.TO_NUMBER.methodHandle());
        NashornBottomLinker.CONVERTERS.put(Integer.TYPE, JSType.TO_INTEGER.methodHandle());
        NashornBottomLinker.CONVERTERS.put(Long.TYPE, JSType.TO_LONG.methodHandle());
        NashornBottomLinker.CONVERTERS.put(String.class, JSType.TO_STRING.methodHandle());
    }
}
