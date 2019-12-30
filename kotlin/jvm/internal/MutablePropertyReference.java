// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.reflect.KMutableProperty;

public abstract class MutablePropertyReference extends PropertyReference implements KMutableProperty
{
    public MutablePropertyReference() {
    }
    
    @SinceKotlin(version = "1.1")
    public MutablePropertyReference(final Object receiver) {
        super(receiver);
    }
}
