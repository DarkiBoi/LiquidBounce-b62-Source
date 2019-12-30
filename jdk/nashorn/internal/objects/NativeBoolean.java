// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.linker.PrimitiveLookup;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeBoolean extends ScriptObject
{
    private final boolean value;
    static final MethodHandle WRAPFILTER;
    private static final MethodHandle PROTOFILTER;
    private static PropertyMap $nasgenmap$;
    
    private NativeBoolean(final boolean value, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.value = value;
    }
    
    NativeBoolean(final boolean flag, final Global global) {
        this(flag, global.getBooleanPrototype(), NativeBoolean.$nasgenmap$);
    }
    
    NativeBoolean(final boolean flag) {
        this(flag, Global.instance());
    }
    
    @Override
    public String safeToString() {
        return "[Boolean " + this.toString() + "]";
    }
    
    @Override
    public String toString() {
        return Boolean.toString(this.getValue());
    }
    
    public boolean getValue() {
        return this.booleanValue();
    }
    
    public boolean booleanValue() {
        return this.value;
    }
    
    @Override
    public String getClassName() {
        return "Boolean";
    }
    
    public static String toString(final Object self) {
        return getBoolean(self).toString();
    }
    
    public static boolean valueOf(final Object self) {
        return getBoolean(self);
    }
    
    public static Object constructor(final boolean newObj, final Object self, final Object value) {
        final boolean flag = JSType.toBoolean(value);
        if (newObj) {
            return new NativeBoolean(flag);
        }
        return flag;
    }
    
    private static Boolean getBoolean(final Object self) {
        if (self instanceof Boolean) {
            return (Boolean)self;
        }
        if (self instanceof NativeBoolean) {
            return ((NativeBoolean)self).getValue();
        }
        if (self != null && self == Global.instance().getBooleanPrototype()) {
            return false;
        }
        throw ECMAErrors.typeError("not.a.boolean", ScriptRuntime.safeToString(self));
    }
    
    public static GuardedInvocation lookupPrimitive(final LinkRequest request, final Object receiver) {
        return PrimitiveLookup.lookupPrimitive(request, Boolean.class, new NativeBoolean((boolean)receiver), NativeBoolean.WRAPFILTER, NativeBoolean.PROTOFILTER);
    }
    
    private static NativeBoolean wrapFilter(final Object receiver) {
        return new NativeBoolean((boolean)receiver);
    }
    
    private static Object protoFilter(final Object object) {
        return Global.instance().getBooleanPrototype();
    }
    
    private static MethodHandle findOwnMH(final String name, final MethodType type) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeBoolean.class, name, type);
    }
    
    static {
        WRAPFILTER = findOwnMH("wrapFilter", Lookup.MH.type(NativeBoolean.class, Object.class));
        PROTOFILTER = findOwnMH("protoFilter", Lookup.MH.type(Object.class, Object.class));
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeBoolean.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
