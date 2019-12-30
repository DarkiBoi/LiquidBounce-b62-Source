// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.lookup.MethodHandleFactory;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.api.scripting.JSObject;
import javax.script.Bindings;
import java.util.Map;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class JSObjectLinker implements TypeBasedGuardingDynamicLinker
{
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle IS_JSOBJECT_GUARD;
    private static final MethodHandle JSOBJECTLINKER_GET;
    private static final MethodHandle JSOBJECTLINKER_PUT;
    private static final MethodHandle JSOBJECT_GETMEMBER;
    private static final MethodHandle JSOBJECT_SETMEMBER;
    private static final MethodHandle JSOBJECT_CALL;
    private static final MethodHandle JSOBJECT_SCOPE_CALL;
    private static final MethodHandle JSOBJECT_CALL_TO_APPLY;
    private static final MethodHandle JSOBJECT_NEW;
    
    JSObjectLinker(final NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return canLinkTypeStatic(type);
    }
    
    static boolean canLinkTypeStatic(final Class<?> type) {
        return Map.class.isAssignableFrom(type) || Bindings.class.isAssignableFrom(type) || JSObject.class.isAssignableFrom(type);
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        final Object self = requestWithoutContext.getReceiver();
        final CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        GuardedInvocation inv;
        if (self instanceof JSObject) {
            inv = this.lookup(desc, request, linkerServices);
            inv = inv.replaceMethods(linkerServices.filterInternalObjects(inv.getInvocation()), inv.getGuard());
        }
        else {
            if (!(self instanceof Map) && !(self instanceof Bindings)) {
                throw new AssertionError();
            }
            final GuardedInvocation beanInv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
            inv = new GuardedInvocation(beanInv.getInvocation(), NashornGuards.combineGuards(beanInv.getGuard(), NashornGuards.getNotJSObjectGuard()));
        }
        return Bootstrap.asTypeSafeReturn(inv, linkerServices, desc);
    }
    
    private GuardedInvocation lookup(final CallSiteDescriptor desc, final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        final int c = desc.getNameTokenCount();
        final String s = operator;
        switch (s) {
            case "getProp":
            case "getElem":
            case "getMethod": {
                if (c > 2) {
                    return findGetMethod(desc);
                }
                return findGetIndexMethod(this.nashornBeansLinker.getGuardedInvocation(request, linkerServices));
            }
            case "setProp":
            case "setElem": {
                return (c > 2) ? findSetMethod(desc) : findSetIndexMethod();
            }
            case "call": {
                return findCallMethod(desc);
            }
            case "new": {
                return findNewMethod(desc);
            }
            default: {
                return null;
            }
        }
    }
    
    private static GuardedInvocation findGetMethod(final CallSiteDescriptor desc) {
        final String name = desc.getNameToken(2);
        final MethodHandle getter = JSObjectLinker.MH.insertArguments(JSObjectLinker.JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, JSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findGetIndexMethod(final GuardedInvocation inv) {
        final MethodHandle getter = JSObjectLinker.MH.insertArguments(JSObjectLinker.JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }
    
    private static GuardedInvocation findSetMethod(final CallSiteDescriptor desc) {
        final MethodHandle getter = JSObjectLinker.MH.insertArguments(JSObjectLinker.JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, JSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(JSObjectLinker.JSOBJECTLINKER_PUT, JSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findCallMethod(final CallSiteDescriptor desc) {
        MethodHandle mh = NashornCallSiteDescriptor.isScope(desc) ? JSObjectLinker.JSOBJECT_SCOPE_CALL : JSObjectLinker.JSOBJECT_CALL;
        if (NashornCallSiteDescriptor.isApplyToCall(desc)) {
            mh = JSObjectLinker.MH.insertArguments(JSObjectLinker.JSOBJECT_CALL_TO_APPLY, 0, mh);
        }
        final MethodType type = desc.getMethodType();
        mh = ((type.parameterType(type.parameterCount() - 1) == Object[].class) ? mh : JSObjectLinker.MH.asCollector(mh, Object[].class, type.parameterCount() - 2));
        return new GuardedInvocation(mh, JSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static GuardedInvocation findNewMethod(final CallSiteDescriptor desc) {
        final MethodHandle func = JSObjectLinker.MH.asCollector(JSObjectLinker.JSOBJECT_NEW, Object[].class, desc.getMethodType().parameterCount() - 1);
        return new GuardedInvocation(func, JSObjectLinker.IS_JSOBJECT_GUARD);
    }
    
    private static boolean isJSObject(final Object self) {
        return self instanceof JSObject;
    }
    
    private static Object get(final MethodHandle fallback, final Object jsobj, final Object key) throws Throwable {
        if (key instanceof Integer) {
            return ((JSObject)jsobj).getSlot((int)key);
        }
        if (key instanceof Number) {
            final int index = getIndex((Number)key);
            if (index > -1) {
                return ((JSObject)jsobj).getSlot(index);
            }
            return ((JSObject)jsobj).getMember(JSType.toString(key));
        }
        else {
            if (!JSType.isString(key)) {
                return null;
            }
            final String name = key.toString();
            if (name.indexOf(40) != -1) {
                return fallback.invokeExact(jsobj, (Object)name);
            }
            return ((JSObject)jsobj).getMember(name);
        }
    }
    
    private static void put(final Object jsobj, final Object key, final Object value) {
        if (key instanceof Integer) {
            ((JSObject)jsobj).setSlot((int)key, value);
        }
        else if (key instanceof Number) {
            final int index = getIndex((Number)key);
            if (index > -1) {
                ((JSObject)jsobj).setSlot(index, value);
            }
            else {
                ((JSObject)jsobj).setMember(JSType.toString(key), value);
            }
        }
        else if (JSType.isString(key)) {
            ((JSObject)jsobj).setMember(key.toString(), value);
        }
    }
    
    private static int getIndex(final Number n) {
        final double value = n.doubleValue();
        return JSType.isRepresentableAsInt(value) ? ((int)value) : -1;
    }
    
    private static Object callToApply(final MethodHandle mh, final JSObject obj, final Object thiz, final Object... args) {
        assert args.length >= 2;
        final Object receiver = args[0];
        final Object[] arguments = new Object[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, arguments.length);
        try {
            return mh.invokeExact(obj, thiz, new Object[] { receiver, arguments });
        }
        catch (RuntimeException | Error ex) {
            final Throwable t;
            final Throwable e = t;
            throw e;
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Object jsObjectScopeCall(final JSObject jsObj, final Object thiz, final Object[] args) {
        Object modifiedThiz;
        if (thiz == ScriptRuntime.UNDEFINED && !jsObj.isStrictFunction()) {
            final Global global = Context.getGlobal();
            modifiedThiz = ScriptObjectMirror.wrap(global, global);
        }
        else {
            modifiedThiz = thiz;
        }
        return jsObj.call(modifiedThiz, args);
    }
    
    private static MethodHandle findJSObjectMH_V(final String name, final Class<?> rtype, final Class<?>... types) {
        return JSObjectLinker.MH.findVirtual(MethodHandles.lookup(), JSObject.class, name, JSObjectLinker.MH.type(rtype, types));
    }
    
    private static MethodHandle findOwnMH_S(final String name, final Class<?> rtype, final Class<?>... types) {
        return JSObjectLinker.MH.findStatic(MethodHandles.lookup(), JSObjectLinker.class, name, JSObjectLinker.MH.type(rtype, types));
    }
    
    static {
        MH = MethodHandleFactory.getFunctionality();
        IS_JSOBJECT_GUARD = findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
        JSOBJECTLINKER_GET = findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
        JSOBJECTLINKER_PUT = findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
        JSOBJECT_GETMEMBER = findJSObjectMH_V("getMember", Object.class, String.class);
        JSOBJECT_SETMEMBER = findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class);
        JSOBJECT_CALL = findJSObjectMH_V("call", Object.class, Object.class, Object[].class);
        JSOBJECT_SCOPE_CALL = findOwnMH_S("jsObjectScopeCall", Object.class, JSObject.class, Object.class, Object[].class);
        JSOBJECT_CALL_TO_APPLY = findOwnMH_S("callToApply", Object.class, MethodHandle.class, JSObject.class, Object.class, Object[].class);
        JSOBJECT_NEW = findJSObjectMH_V("newObject", Object.class, Object[].class);
    }
}
