// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty;

public abstract class PropertyReference extends CallableReference implements KProperty
{
    public PropertyReference() {
    }
    
    @SinceKotlin(version = "1.1")
    public PropertyReference(final Object receiver) {
        super(receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    protected KProperty getReflected() {
        return (KProperty)super.getReflected();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isLateinit() {
        return this.getReflected().isLateinit();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isConst() {
        return this.getReflected().isConst();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PropertyReference) {
            final PropertyReference other = (PropertyReference)obj;
            return this.getOwner().equals(other.getOwner()) && this.getName().equals(other.getName()) && this.getSignature().equals(other.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), other.getBoundReceiver());
        }
        return obj instanceof KProperty && obj.equals(this.compute());
    }
    
    @Override
    public int hashCode() {
        return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }
    
    @Override
    public String toString() {
        final KCallable reflected = this.compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "property " + this.getName() + " (Kotlin reflection is not available)";
    }
}
