// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.reflect.KMutableProperty;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty2;

public abstract class MutablePropertyReference2 extends MutablePropertyReference implements KMutableProperty2
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty2(this);
    }
    
    @Override
    public Object invoke(final Object receiver1, final Object receiver2) {
        return this.get(receiver1, receiver2);
    }
    
    @Override
    public KProperty2.Getter getGetter() {
        return ((KMutableProperty2)this.getReflected()).getGetter();
    }
    
    @Override
    public KMutableProperty2.Setter getSetter() {
        return ((KMutableProperty2)this.getReflected()).getSetter();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object receiver1, final Object receiver2) {
        return ((KMutableProperty2)this.getReflected()).getDelegate(receiver1, receiver2);
    }
}
