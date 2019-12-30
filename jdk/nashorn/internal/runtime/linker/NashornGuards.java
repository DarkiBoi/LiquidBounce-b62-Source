// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.runtime.options.Options;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;
import java.lang.ref.WeakReference;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandle;

public final class NashornGuards
{
    private static final MethodHandle IS_MAP;
    private static final MethodHandle IS_MAP_SCRIPTOBJECT;
    private static final MethodHandle IS_SCRIPTOBJECT;
    private static final MethodHandle IS_NOT_JSOBJECT;
    private static final MethodHandle SAME_OBJECT;
    private static final boolean CCE_ONLY;
    
    private NashornGuards() {
    }
    
    public static boolean explicitInstanceOfCheck(final CallSiteDescriptor desc, final LinkRequest request) {
        return !NashornGuards.CCE_ONLY;
    }
    
    public static MethodHandle getScriptObjectGuard() {
        return NashornGuards.IS_SCRIPTOBJECT;
    }
    
    public static MethodHandle getNotJSObjectGuard() {
        return NashornGuards.IS_NOT_JSOBJECT;
    }
    
    public static MethodHandle getScriptObjectGuard(final boolean explicitInstanceOfCheck) {
        return explicitInstanceOfCheck ? NashornGuards.IS_SCRIPTOBJECT : null;
    }
    
    public static MethodHandle getMapGuard(final PropertyMap map, final boolean explicitInstanceOfCheck) {
        return Lookup.MH.insertArguments(explicitInstanceOfCheck ? NashornGuards.IS_MAP_SCRIPTOBJECT : NashornGuards.IS_MAP, 1, map);
    }
    
    static boolean needsGuard(final Property property, final CallSiteDescriptor desc) {
        return property == null || property.isConfigurable() || property.isBound() || property.hasDualFields() || !NashornCallSiteDescriptor.isFastScope(desc) || property.canChangeType();
    }
    
    public static MethodHandle getGuard(final ScriptObject sobj, final Property property, final CallSiteDescriptor desc, final boolean explicitInstanceOfCheck) {
        if (!needsGuard(property, desc)) {
            return null;
        }
        if (NashornCallSiteDescriptor.isScope(desc)) {
            if (property != null && property.isBound() && !property.canChangeType()) {
                return getIdentityGuard(sobj);
            }
            if (!(sobj instanceof Global) && (property == null || property.isConfigurable())) {
                return combineGuards(getIdentityGuard(sobj), getMapGuard(sobj.getMap(), explicitInstanceOfCheck));
            }
        }
        return getMapGuard(sobj.getMap(), explicitInstanceOfCheck);
    }
    
    public static MethodHandle getIdentityGuard(final ScriptObject sobj) {
        return Lookup.MH.insertArguments(NashornGuards.SAME_OBJECT, 1, new WeakReference(sobj));
    }
    
    public static MethodHandle getStringGuard() {
        return JSType.IS_STRING.methodHandle();
    }
    
    public static MethodHandle getNumberGuard() {
        return JSType.IS_NUMBER.methodHandle();
    }
    
    public static MethodHandle combineGuards(final MethodHandle guard1, final MethodHandle guard2) {
        if (guard1 == null) {
            return guard2;
        }
        if (guard2 == null) {
            return guard1;
        }
        return Lookup.MH.guardWithTest(guard1, guard2, Lookup.MH.dropArguments(Lookup.MH.constant(Boolean.TYPE, false), 0, Object.class));
    }
    
    private static boolean isScriptObject(final Object self) {
        return self instanceof ScriptObject;
    }
    
    private static boolean isScriptObject(final Class<? extends ScriptObject> clazz, final Object self) {
        return clazz.isInstance(self);
    }
    
    private static boolean isMap(final ScriptObject self, final PropertyMap map) {
        return self.getMap() == map;
    }
    
    private static boolean isNotJSObject(final Object self) {
        return !(self instanceof JSObject);
    }
    
    private static boolean isMap(final Object self, final PropertyMap map) {
        return self instanceof ScriptObject && ((ScriptObject)self).getMap() == map;
    }
    
    private static boolean sameObject(final Object self, final WeakReference<ScriptObject> ref) {
        return self == ref.get();
    }
    
    private static boolean isScriptFunction(final Object self) {
        return self instanceof ScriptFunction;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NashornGuards.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        IS_MAP = findOwnMH("isMap", Boolean.TYPE, ScriptObject.class, PropertyMap.class);
        IS_MAP_SCRIPTOBJECT = findOwnMH("isMap", Boolean.TYPE, Object.class, PropertyMap.class);
        IS_SCRIPTOBJECT = findOwnMH("isScriptObject", Boolean.TYPE, Object.class);
        IS_NOT_JSOBJECT = findOwnMH("isNotJSObject", Boolean.TYPE, Object.class);
        SAME_OBJECT = findOwnMH("sameObject", Boolean.TYPE, Object.class, WeakReference.class);
        CCE_ONLY = Options.getBooleanProperty("nashorn.cce");
    }
}
