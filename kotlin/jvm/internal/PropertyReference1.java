// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty1;

public abstract class PropertyReference1 extends PropertyReference implements KProperty1
{
    public PropertyReference1() {
    }
    
    @SinceKotlin(version = "1.1")
    public PropertyReference1(final Object receiver) {
        super(receiver);
    }
    
    @Override
    protected KCallable computeReflected() {
        return Reflection.property1(this);
    }
    
    @Override
    public Object invoke(final Object receiver) {
        return this.get(receiver);
    }
    
    @Override
    public KProperty1.Getter getGetter() {
        return ((KProperty1)this.getReflected()).getGetter();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object receiver) {
        return ((KProperty1)this.getReflected()).getDelegate(receiver);
    }
}
