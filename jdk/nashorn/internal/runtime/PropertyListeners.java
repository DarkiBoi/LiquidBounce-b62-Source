// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Set;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;

public class PropertyListeners
{
    private Map<String, WeakPropertyMapSet> listeners;
    private static LongAdder listenersAdded;
    private static LongAdder listenersRemoved;
    
    PropertyListeners(final PropertyListeners listener) {
        if (listener != null && listener.listeners != null) {
            this.listeners = new WeakHashMap<String, WeakPropertyMapSet>();
            synchronized (listener) {
                for (final Map.Entry<String, WeakPropertyMapSet> entry : listener.listeners.entrySet()) {
                    this.listeners.put(entry.getKey(), new WeakPropertyMapSet(entry.getValue()));
                }
            }
        }
    }
    
    public static long getListenersAdded() {
        return PropertyListeners.listenersAdded.longValue();
    }
    
    public static long getListenersRemoved() {
        return PropertyListeners.listenersRemoved.longValue();
    }
    
    public static int getListenerCount(final ScriptObject obj) {
        return obj.getMap().getListenerCount();
    }
    
    public int getListenerCount() {
        return (this.listeners == null) ? 0 : this.listeners.size();
    }
    
    public static PropertyListeners addListener(final PropertyListeners listeners, final String key, final PropertyMap propertyMap) {
        if (listeners == null || !listeners.containsListener(key, propertyMap)) {
            final PropertyListeners newListeners = new PropertyListeners(listeners);
            newListeners.addListener(key, propertyMap);
            return newListeners;
        }
        return listeners;
    }
    
    synchronized boolean containsListener(final String key, final PropertyMap propertyMap) {
        if (this.listeners == null) {
            return false;
        }
        final WeakPropertyMapSet set = this.listeners.get(key);
        return set != null && set.contains(propertyMap);
    }
    
    final synchronized void addListener(final String key, final PropertyMap propertyMap) {
        if (Context.DEBUG) {
            PropertyListeners.listenersAdded.increment();
        }
        if (this.listeners == null) {
            this.listeners = new WeakHashMap<String, WeakPropertyMapSet>();
        }
        WeakPropertyMapSet set = this.listeners.get(key);
        if (set == null) {
            set = new WeakPropertyMapSet();
            this.listeners.put(key, set);
        }
        if (!set.contains(propertyMap)) {
            set.add(propertyMap);
        }
    }
    
    public synchronized void propertyAdded(final Property prop) {
        if (this.listeners != null) {
            final WeakPropertyMapSet set = this.listeners.get(prop.getKey());
            if (set != null) {
                for (final PropertyMap propertyMap : set.elements()) {
                    propertyMap.propertyAdded(prop, false);
                }
                this.listeners.remove(prop.getKey());
                if (Context.DEBUG) {
                    PropertyListeners.listenersRemoved.increment();
                }
            }
        }
    }
    
    public synchronized void propertyDeleted(final Property prop) {
        if (this.listeners != null) {
            final WeakPropertyMapSet set = this.listeners.get(prop.getKey());
            if (set != null) {
                for (final PropertyMap propertyMap : set.elements()) {
                    propertyMap.propertyDeleted(prop, false);
                }
                this.listeners.remove(prop.getKey());
                if (Context.DEBUG) {
                    PropertyListeners.listenersRemoved.increment();
                }
            }
        }
    }
    
    public synchronized void propertyModified(final Property oldProp, final Property newProp) {
        if (this.listeners != null) {
            final WeakPropertyMapSet set = this.listeners.get(oldProp.getKey());
            if (set != null) {
                for (final PropertyMap propertyMap : set.elements()) {
                    propertyMap.propertyModified(oldProp, newProp, false);
                }
                this.listeners.remove(oldProp.getKey());
                if (Context.DEBUG) {
                    PropertyListeners.listenersRemoved.increment();
                }
            }
        }
    }
    
    public synchronized void protoChanged() {
        if (this.listeners != null) {
            for (final WeakPropertyMapSet set : this.listeners.values()) {
                for (final PropertyMap propertyMap : set.elements()) {
                    propertyMap.protoChanged(false);
                }
            }
            this.listeners.clear();
        }
    }
    
    static {
        if (Context.DEBUG) {
            PropertyListeners.listenersAdded = new LongAdder();
            PropertyListeners.listenersRemoved = new LongAdder();
        }
    }
    
    private static class WeakPropertyMapSet
    {
        private final WeakHashMap<PropertyMap, Boolean> map;
        
        WeakPropertyMapSet() {
            this.map = new WeakHashMap<PropertyMap, Boolean>();
        }
        
        WeakPropertyMapSet(final WeakPropertyMapSet set) {
            this.map = new WeakHashMap<PropertyMap, Boolean>(set.map);
        }
        
        void add(final PropertyMap propertyMap) {
            this.map.put(propertyMap, Boolean.TRUE);
        }
        
        boolean contains(final PropertyMap propertyMap) {
            return this.map.containsKey(propertyMap);
        }
        
        Set<PropertyMap> elements() {
            return this.map.keySet();
        }
    }
}
