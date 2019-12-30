// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.util.Objects;
import java.io.Serializable;

public class StaticClass implements Serializable
{
    private static final ClassValue<StaticClass> staticClasses;
    private static final long serialVersionUID = 1L;
    private final Class<?> clazz;
    
    StaticClass(final Class<?> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }
    
    public static StaticClass forClass(final Class<?> clazz) {
        return StaticClass.staticClasses.get(clazz);
    }
    
    public Class<?> getRepresentedClass() {
        return this.clazz;
    }
    
    @Override
    public String toString() {
        return "JavaClassStatics[" + this.clazz.getName() + "]";
    }
    
    private Object readResolve() {
        return forClass(this.clazz);
    }
    
    static {
        staticClasses = new ClassValue<StaticClass>() {
            @Override
            protected StaticClass computeValue(final Class<?> type) {
                return new StaticClass(type);
            }
        };
    }
}
