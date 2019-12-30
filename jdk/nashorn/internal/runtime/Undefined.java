// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandle;

public final class Undefined extends DefaultPropertyAccess
{
    private static final Undefined UNDEFINED;
    private static final Undefined EMPTY;
    private static final MethodHandle UNDEFINED_GUARD;
    private static final MethodHandle GET_METHOD;
    private static final MethodHandle SET_METHOD;
    
    private Undefined() {
    }
    
    public static Undefined getUndefined() {
        return Undefined.UNDEFINED;
    }
    
    public static Undefined getEmpty() {
        return Undefined.EMPTY;
    }
    
    public String getClassName() {
        return "Undefined";
    }
    
    @Override
    public String toString() {
        return "undefined";
    }
    
    public static GuardedInvocation lookup(final CallSiteDescriptor desc) {
        final String s;
        final String operator = s = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        switch (s) {
            case "new":
            case "call": {
                final String name = NashornCallSiteDescriptor.getFunctionDescription(desc);
                final String msg = (name != null) ? "not.a.function" : "cant.call.undefined";
                throw ECMAErrors.typeError(msg, name);
            }
            case "callMethod": {
                throw lookupTypeError("cant.read.property.of.undefined", desc);
            }
            case "getProp":
            case "getElem":
            case "getMethod": {
                if (desc.getNameTokenCount() < 3) {
                    return findGetIndexMethod(desc);
                }
                return findGetMethod(desc);
            }
            case "setProp":
            case "setElem": {
                if (desc.getNameTokenCount() < 3) {
                    return findSetIndexMethod(desc);
                }
                return findSetMethod(desc);
            }
            default: {
                return null;
            }
        }
    }
    
    private static ECMAException lookupTypeError(final String msg, final CallSiteDescriptor desc) {
        final String name = desc.getNameToken(2);
        return ECMAErrors.typeError(msg, (name != null && !name.isEmpty()) ? name : null);
    }
    
    private static GuardedInvocation findGetMethod(final CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(Undefined.GET_METHOD, 1, desc.getNameToken(2)), Undefined.UNDEFINED_GUARD).asType(desc);
    }
    
    private static GuardedInvocation findGetIndexMethod(final CallSiteDescriptor desc) {
        return new GuardedInvocation(Undefined.GET_METHOD, Undefined.UNDEFINED_GUARD).asType(desc);
    }
    
    private static GuardedInvocation findSetMethod(final CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(Undefined.SET_METHOD, 1, desc.getNameToken(2)), Undefined.UNDEFINED_GUARD).asType(desc);
    }
    
    private static GuardedInvocation findSetIndexMethod(final CallSiteDescriptor desc) {
        return new GuardedInvocation(Undefined.SET_METHOD, Undefined.UNDEFINED_GUARD).asType(desc);
    }
    
    @Override
    public Object get(final Object key) {
        throw ECMAErrors.typeError("cant.read.property.of.undefined", ScriptRuntime.safeToString(key));
    }
    
    @Override
    public void set(final Object key, final Object value, final int flags) {
        throw ECMAErrors.typeError("cant.set.property.of.undefined", ScriptRuntime.safeToString(key));
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        throw ECMAErrors.typeError("cant.delete.property.of.undefined", ScriptRuntime.safeToString(key));
    }
    
    @Override
    public boolean has(final Object key) {
        return false;
    }
    
    @Override
    public boolean hasOwnProperty(final Object key) {
        return false;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), Undefined.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        UNDEFINED = new Undefined();
        EMPTY = new Undefined();
        UNDEFINED_GUARD = Guards.getIdentityGuard(Undefined.UNDEFINED);
        GET_METHOD = findOwnMH("get", Object.class, Object.class);
        SET_METHOD = Lookup.MH.insertArguments(findOwnMH("set", Void.TYPE, Object.class, Object.class, Integer.TYPE), 3, 2);
    }
}
