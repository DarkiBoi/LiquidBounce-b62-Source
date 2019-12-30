// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;
import jdk.nashorn.internal.runtime.JSType;
import java.io.PrintWriter;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.PropertyListeners;
import java.util.Objects;
import java.security.Permission;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeDebug extends ScriptObject
{
    private static PropertyMap $nasgenmap$;
    private static final String EVENT_QUEUE = "__eventQueue__";
    private static final String EVENT_QUEUE_CAPACITY = "__eventQueueCapacity__";
    
    private NativeDebug() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getClassName() {
        return "Debug";
    }
    
    public static Object getArrayDataClass(final Object self, final Object obj) {
        try {
            return ((ScriptObject)obj).getArray().getClass();
        }
        catch (ClassCastException e) {
            return ScriptRuntime.UNDEFINED;
        }
    }
    
    public static Object getArrayData(final Object self, final Object obj) {
        try {
            return ((ScriptObject)obj).getArray();
        }
        catch (ClassCastException e) {
            return ScriptRuntime.UNDEFINED;
        }
    }
    
    public static Object getContext(final Object self) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.getContext"));
        }
        return Global.getThisContext();
    }
    
    public static Object map(final Object self, final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getMap();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static boolean identical(final Object self, final Object obj1, final Object obj2) {
        return obj1 == obj2;
    }
    
    public static Object equalWithoutType(final Object self, final Object m1, final Object m2) {
        return ((PropertyMap)m1).equalsWithoutType((PropertyMap)m2);
    }
    
    public static Object diffPropertyMaps(final Object self, final Object m1, final Object m2) {
        return PropertyMap.diff((PropertyMap)m1, (PropertyMap)m2);
    }
    
    public static Object getClass(final Object self, final Object obj) {
        if (obj != null) {
            return obj.getClass();
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    public static boolean equals(final Object self, final Object obj1, final Object obj2) {
        return Objects.equals(obj1, obj2);
    }
    
    public static String toJavaString(final Object self, final Object obj) {
        return Objects.toString(obj);
    }
    
    public static String toIdentString(final Object self, final Object obj) {
        if (obj == null) {
            return "null";
        }
        final int hash = System.identityHashCode(obj);
        return obj.getClass() + "@" + Integer.toHexString(hash);
    }
    
    public static int getListenerCount(final Object self, final Object obj) {
        return (obj instanceof ScriptObject) ? PropertyListeners.getListenerCount((ScriptObject)obj) : 0;
    }
    
    public static Object dumpCounters(final Object self) {
        final PrintWriter out = Context.getCurrentErr();
        out.println("ScriptObject count " + ScriptObject.getCount());
        out.println("Scope count " + Scope.getScopeCount());
        out.println("ScriptObject listeners added " + PropertyListeners.getListenersAdded());
        out.println("ScriptObject listeners removed " + PropertyListeners.getListenersRemoved());
        out.println("ScriptFunction constructor calls " + ScriptFunction.getConstructorCount());
        out.println("ScriptFunction invokes " + ScriptFunction.getInvokes());
        out.println("ScriptFunction allocations " + ScriptFunction.getAllocations());
        out.println("PropertyMap count " + PropertyMap.getCount());
        out.println("PropertyMap cloned " + PropertyMap.getClonedCount());
        out.println("PropertyMap history hit " + PropertyMap.getHistoryHit());
        out.println("PropertyMap proto invalidations " + PropertyMap.getProtoInvalidations());
        out.println("PropertyMap proto history hit " + PropertyMap.getProtoHistoryHit());
        out.println("PropertyMap setProtoNewMapCount " + PropertyMap.getSetProtoNewMapCount());
        out.println("Callsite count " + LinkerCallSite.getCount());
        out.println("Callsite misses " + LinkerCallSite.getMissCount());
        out.println("Callsite misses by site at " + LinkerCallSite.getMissSamplingPercentage() + "%");
        LinkerCallSite.getMissCounts(out);
        return ScriptRuntime.UNDEFINED;
    }
    
    public static Object getEventQueueCapacity(final Object self) {
        final ScriptObject sobj = (ScriptObject)self;
        Integer cap;
        if (sobj.has("__eventQueueCapacity__")) {
            cap = JSType.toInt32(sobj.get("__eventQueueCapacity__"));
        }
        else {
            setEventQueueCapacity(self, cap = RuntimeEvent.RUNTIME_EVENT_QUEUE_SIZE);
        }
        return cap;
    }
    
    public static void setEventQueueCapacity(final Object self, final Object newCapacity) {
        ((ScriptObject)self).set("__eventQueueCapacity__", newCapacity, 2);
    }
    
    public static void addRuntimeEvent(final Object self, final Object event) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        final int cap = (int)getEventQueueCapacity(self);
        while (q.size() >= cap) {
            q.removeFirst();
        }
        q.addLast(getEvent(event));
    }
    
    public static void expandEventQueueCapacity(final Object self, final Object newCapacity) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        final int nc = JSType.toInt32(newCapacity);
        while (q.size() > nc) {
            q.removeFirst();
        }
        setEventQueueCapacity(self, nc);
    }
    
    public static void clearRuntimeEvents(final Object self) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        q.clear();
    }
    
    public static void removeRuntimeEvent(final Object self, final Object event) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        final RuntimeEvent<?> re = getEvent(event);
        if (!q.remove(re)) {
            throw new IllegalStateException("runtime event " + re + " was not in event queue");
        }
    }
    
    public static Object getRuntimeEvents(final Object self) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        return q.toArray(new RuntimeEvent[q.size()]);
    }
    
    public static Object getLastRuntimeEvent(final Object self) {
        final LinkedList<RuntimeEvent<?>> q = getEventQueue(self);
        return q.isEmpty() ? null : q.getLast();
    }
    
    private static LinkedList<RuntimeEvent<?>> getEventQueue(final Object self) {
        final ScriptObject sobj = (ScriptObject)self;
        LinkedList<RuntimeEvent<?>> q;
        if (sobj.has("__eventQueue__")) {
            q = (LinkedList<RuntimeEvent<?>>)((ScriptObject)self).get("__eventQueue__");
        }
        else {
            ((ScriptObject)self).set("__eventQueue__", q = new LinkedList<RuntimeEvent<?>>(), 2);
        }
        return q;
    }
    
    private static RuntimeEvent<?> getEvent(final Object event) {
        return (RuntimeEvent<?>)event;
    }
    
    static {
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeDebug.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
