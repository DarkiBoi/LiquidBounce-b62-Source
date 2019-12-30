// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;

class InvokableEventTarget
{
    private final EventListener eventClass;
    private final Method method;
    private final boolean ignoreCondition;
    
    InvokableEventTarget(final EventListener eventClass, final Method method, final EventTarget eventTarget) {
        this.eventClass = eventClass;
        this.method = method;
        this.ignoreCondition = eventTarget.ignoreCondition();
    }
    
    EventListener getEventClass() {
        return this.eventClass;
    }
    
    Method getMethod() {
        return this.method;
    }
    
    public boolean isIgnoreCondition() {
        return this.ignoreCondition;
    }
}
