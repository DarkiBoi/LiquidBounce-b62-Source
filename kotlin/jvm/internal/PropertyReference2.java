// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.SinceKotlin;
import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty2;

public abstract class PropertyReference2 extends PropertyReference implements KProperty2
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.property2(this);
    }
    
    @Override
    public Object invoke(final Object receiver1, final Object receiver2) {
        return this.get(receiver1, receiver2);
    }
    
    @Override
    public KProperty2.Getter getGetter() {
        return ((KProperty2)this.getReflected()).getGetter();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object receiver1, final Object receiver2) {
        return ((KProperty2)this.getReflected()).getDelegate(receiver1, receiver2);
    }
}
