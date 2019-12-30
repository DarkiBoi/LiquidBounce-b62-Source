// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.events;

import jdk.nashorn.internal.runtime.options.Options;
import java.util.logging.Level;

public class RuntimeEvent<T>
{
    public static final int RUNTIME_EVENT_QUEUE_SIZE;
    private final Level level;
    private final T value;
    
    public RuntimeEvent(final Level level, final T object) {
        this.level = level;
        this.value = object;
    }
    
    public final T getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.level).append("] ").append((this.value == null) ? "null" : this.getValueClass().getSimpleName()).append(" value=").append(this.value);
        return sb.toString();
    }
    
    public final Class<?> getValueClass() {
        return this.value.getClass();
    }
    
    static {
        RUNTIME_EVENT_QUEUE_SIZE = Options.getIntProperty("nashorn.runtime.event.queue.size", 1024);
    }
}
