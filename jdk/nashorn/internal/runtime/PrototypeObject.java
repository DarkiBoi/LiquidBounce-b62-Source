// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Collection;
import java.util.ArrayList;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import java.lang.invoke.MethodHandle;

public class PrototypeObject extends ScriptObject
{
    private static final PropertyMap map$;
    private Object constructor;
    private static final MethodHandle GET_CONSTRUCTOR;
    private static final MethodHandle SET_CONSTRUCTOR;
    
    private PrototypeObject(final Global global, final PropertyMap map) {
        super(global.getObjectPrototype(), (map != PrototypeObject.map$) ? map.addAll(PrototypeObject.map$) : PrototypeObject.map$);
    }
    
    protected PrototypeObject() {
        this(Global.instance(), PrototypeObject.map$);
    }
    
    protected PrototypeObject(final PropertyMap map) {
        this(Global.instance(), map);
    }
    
    protected PrototypeObject(final ScriptFunction func) {
        this(Global.instance(), PrototypeObject.map$);
        this.constructor = func;
    }
    
    public static Object getConstructor(final Object self) {
        return (self instanceof PrototypeObject) ? ((PrototypeObject)self).getConstructor() : ScriptRuntime.UNDEFINED;
    }
    
    public static void setConstructor(final Object self, final Object constructor) {
        if (self instanceof PrototypeObject) {
            ((PrototypeObject)self).setConstructor(constructor);
        }
    }
    
    private Object getConstructor() {
        return this.constructor;
    }
    
    private void setConstructor(final Object constructor) {
        this.constructor = constructor;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), PrototypeObject.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        GET_CONSTRUCTOR = findOwnMH("getConstructor", Object.class, Object.class);
        SET_CONSTRUCTOR = findOwnMH("setConstructor", Void.TYPE, Object.class, Object.class);
        final ArrayList<Property> properties = new ArrayList<Property>(1);
        properties.add(AccessorProperty.create("constructor", 2, PrototypeObject.GET_CONSTRUCTOR, PrototypeObject.SET_CONSTRUCTOR));
        map$ = PropertyMap.newMap(properties);
    }
}
