// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.utils.ClientUtils;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
    private final Map<Class<? extends Event>, List<InvokableEventTarget>> registry;
    
    public EventManager() {
        this.registry = new HashMap<Class<? extends Event>, List<InvokableEventTarget>>();
    }
    
    public void registerListener(final EventListener eventListener) {
        for (final Method method : eventListener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class) && method.getParameterTypes().length == 1) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                final Class<? extends Event> eventClass = (Class<? extends Event>)method.getParameterTypes()[0];
                final EventTarget eventTarget = method.getAnnotation(EventTarget.class);
                final List<InvokableEventTarget> invokableEventTargets = this.registry.getOrDefault(eventClass, new ArrayList<InvokableEventTarget>());
                invokableEventTargets.add(new InvokableEventTarget(eventListener, method, eventTarget));
                this.registry.put(eventClass, invokableEventTargets);
            }
        }
    }
    
    public void unregisterListener(final EventListener eventListener) {
        for (final Map.Entry<Class<? extends Event>, List<InvokableEventTarget>> entry : this.registry.entrySet()) {
            final List<InvokableEventTarget> invokableEventTargetList = entry.getValue();
            invokableEventTargetList.removeIf(invokableEventTarget -> invokableEventTarget.getEventClass() == eventListener);
            this.registry.put(entry.getKey(), invokableEventTargetList);
        }
    }
    
    public void callEvent(final Event event) {
        final List<InvokableEventTarget> invokableEventTargets = this.registry.getOrDefault(event.getClass(), null);
        if (invokableEventTargets == null) {
            return;
        }
        for (final InvokableEventTarget invokableEventTarget : invokableEventTargets) {
            try {
                if (!invokableEventTarget.getEventClass().handleEvents() && !invokableEventTarget.isIgnoreCondition()) {
                    continue;
                }
                invokableEventTarget.getMethod().invoke(invokableEventTarget.getEventClass(), event);
            }
            catch (Throwable throwable) {
                ClientUtils.getLogger().error((Object)throwable);
            }
        }
    }
}
