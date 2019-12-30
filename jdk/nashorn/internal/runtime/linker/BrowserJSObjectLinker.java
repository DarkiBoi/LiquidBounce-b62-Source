// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.lookup.MethodHandleFactory;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.JSType;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class BrowserJSObjectLinker implements TypeBasedGuardingDynamicLinker
{
    private static final ClassLoader myLoader;
    private static final String JSOBJECT_CLASS = "netscape.javascript.JSObject";
    private static volatile Class<?> jsObjectClass;
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle IS_JSOBJECT_GUARD;
    private static final MethodHandle JSOBJECTLINKER_GET;
    private static final MethodHandle JSOBJECTLINKER_PUT;
    
    BrowserJSObjectLinker(final NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return canLinkTypeStatic(type);
    }
    
    static boolean canLinkTypeStatic(final Class<?> type) {
        if (BrowserJSObjectLinker.jsObjectClass != null && BrowserJSObjectLinker.jsObjectClass.isAssignableFrom(type)) {
            return true;
        }
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.getClassLoader() == BrowserJSObjectLinker.myLoader && clazz.getName().equals("netscape.javascript.JSObject")) {
                BrowserJSObjectLinker.jsObjectClass = clazz;
                return true;
            }
        }
        return false;
    }
    
    private static void checkJSObjectClass() {
        assert BrowserJSObjectLinker.jsObjectClass != null : "netscape.javascript.JSObject not found!";
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        final Object self = requestWithoutContext.getReceiver();
        final CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        checkJSObjectClass();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        if (BrowserJSObjectLinker.jsObjectClass.isInstance(self)) {
            GuardedInvocation inv = this.lookup(desc, request, linkerServices);
            inv = inv.replaceMethods(linkerServices.filterInternalObjects(inv.getInvocation()), inv.getGuard());
            return Bootstrap.asTypeSafeReturn(inv, linkerServices, desc);
        }
        throw new AssertionError();
    }
    
    private GuardedInvocation lookup(final CallSiteDescriptor desc, final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        final int c = desc.getNameTokenCount();
        GuardedInvocation inv;
        try {
            inv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
        }
        catch (Throwable th) {
            inv = null;
        }
        final String s = operator;
        switch (s) {
            case "getProp":
            case "getElem":
            case "getMethod": {
                return (c > 2) ? findGetMethod(desc, inv) : findGetIndexMethod(inv);
            }
            case "setProp":
            case "setElem": {
                return (c > 2) ? findSetMethod(desc, inv) : findSetIndexMethod();
            }
            case "call": {
                return findCallMethod(desc);
            }
            default: {
                return null;
            }
        }
    }
    
    private static GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        final String name = desc.getNameToken(2);
        final MethodHandle getter = BrowserJSObjectLinker.MH.insertArguments(JSObjectHandles.JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, BrowserJSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findGetIndexMethod(final GuardedInvocation inv) {
        final MethodHandle getter = BrowserJSObjectLinker.MH.insertArguments(BrowserJSObjectLinker.JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }
    
    private static GuardedInvocation findSetMethod(final CallSiteDescriptor desc, final GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        final MethodHandle getter = BrowserJSObjectLinker.MH.insertArguments(JSObjectHandles.JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, BrowserJSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(BrowserJSObjectLinker.JSOBJECTLINKER_PUT, BrowserJSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findCallMethod(final CallSiteDescriptor desc) {
        final MethodHandle call = BrowserJSObjectLinker.MH.insertArguments(JSObjectHandles.JSOBJECT_CALL, 1, "call");
        return new GuardedInvocation(BrowserJSObjectLinker.MH.asCollector(call, Object[].class, desc.getMethodType().parameterCount() - 1), BrowserJSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static boolean isJSObject(final Object self) {
        return BrowserJSObjectLinker.jsObjectClass.isInstance(self);
    }
    
    private static Object get(final MethodHandle fallback, final Object jsobj, final Object key) throws Throwable {
        if (key instanceof Integer) {
            return JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, (int)key);
        }
        if (key instanceof Number) {
            final int index = getIndex((Number)key);
            if (index > -1) {
                return JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, index);
            }
        }
        else if (JSType.isString(key)) {
            final String name = key.toString();
            if (name.indexOf(40) != -1) {
                return fallback.invokeExact(jsobj, (Object)name);
            }
            return JSObjectHandles.JSOBJECT_GETMEMBER.invokeExact(jsobj, name);
        }
        return null;
    }
    
    private static void put(final Object jsobj, final Object key, final Object value) throws Throwable {
        if (key instanceof Integer) {
            JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, (int)key, value);
        }
        else if (key instanceof Number) {
            JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, getIndex((Number)key), value);
        }
        else if (JSType.isString(key)) {
            JSObjectHandles.JSOBJECT_SETMEMBER.invokeExact(jsobj, key.toString(), value);
        }
    }
    
    private static int getIndex(final Number n) {
        final double value = n.doubleValue();
        return JSType.isRepresentableAsInt(value) ? ((int)value) : -1;
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return BrowserJSObjectLinker.MH.findStatic(MethodHandles.lookup(), BrowserJSObjectLinker.class, name, BrowserJSObjectLinker.MH.type(rtype, types));
    }
    
    static {
        myLoader = BrowserJSObjectLinker.class.getClassLoader();
        MH = MethodHandleFactory.getFunctionality();
        IS_JSOBJECT_GUARD = findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
        JSOBJECTLINKER_GET = findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
        JSOBJECTLINKER_PUT = findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
    }
    
    static class JSObjectHandles
    {
        static final MethodHandle JSOBJECT_GETMEMBER;
        static final MethodHandle JSOBJECT_GETSLOT;
        static final MethodHandle JSOBJECT_SETMEMBER;
        static final MethodHandle JSOBJECT_SETSLOT;
        static final MethodHandle JSOBJECT_CALL;
        
        private static MethodHandle findJSObjectMH_V(final String name, final Class<?> rtype, final Class<?>... types) {
            checkJSObjectClass();
            return BrowserJSObjectLinker.MH.findVirtual(MethodHandles.publicLookup(), BrowserJSObjectLinker.jsObjectClass, name, BrowserJSObjectLinker.MH.type(rtype, types));
        }
        
        static {
            JSOBJECT_GETMEMBER = findJSObjectMH_V("getMember", Object.class, String.class).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, String.class));
            JSOBJECT_GETSLOT = findJSObjectMH_V("getSlot", Object.class, Integer.TYPE).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, Integer.TYPE));
            JSOBJECT_SETMEMBER = findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class).asType(BrowserJSObjectLinker.MH.type(Void.TYPE, Object.class, String.class, Object.class));
            JSOBJECT_SETSLOT = findJSObjectMH_V("setSlot", Void.TYPE, Integer.TYPE, Object.class).asType(BrowserJSObjectLinker.MH.type(Void.TYPE, Object.class, Integer.TYPE, Object.class));
            JSOBJECT_CALL = findJSObjectMH_V("call", Object.class, String.class, Object[].class).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, String.class, Object[].class));
        }
    }
}
