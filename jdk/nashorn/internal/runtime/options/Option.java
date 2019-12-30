// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.options;

public class Option<T>
{
    protected T value;
    
    Option(final T value) {
        this.value = value;
    }
    
    public T getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.getValue() + " [" + this.getValue().getClass() + "]";
    }
}
