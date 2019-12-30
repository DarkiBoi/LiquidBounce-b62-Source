// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KFunction;

public class FunctionReference extends CallableReference implements FunctionBase, KFunction
{
    private final int arity;
    
    public FunctionReference(final int arity) {
        this.arity = arity;
    }
    
    @SinceKotlin(version = "1.1")
    public FunctionReference(final int arity, final Object receiver) {
        super(receiver);
        this.arity = arity;
    }
    
    @Override
    public int getArity() {
        return this.arity;
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    protected KFunction getReflected() {
        return (KFunction)super.getReflected();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    protected KCallable computeReflected() {
        return Reflection.function(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isInline() {
        return this.getReflected().isInline();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isExternal() {
        return this.getReflected().isExternal();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isOperator() {
        return this.getReflected().isOperator();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isInfix() {
        return this.getReflected().isInfix();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isSuspend() {
        return this.getReflected().isSuspend();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionReference) {
            final FunctionReference other = (FunctionReference)obj;
            if (this.getOwner() == null) {
                if (other.getOwner() != null) {
                    return false;
                }
            }
            else if (!this.getOwner().equals(other.getOwner())) {
                return false;
            }
            if (this.getName().equals(other.getName()) && this.getSignature().equals(other.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), other.getBoundReceiver())) {
                return true;
            }
            return false;
        }
        return obj instanceof KFunction && obj.equals(this.compute());
    }
    
    @Override
    public int hashCode() {
        return (((this.getOwner() == null) ? 0 : (this.getOwner().hashCode() * 31)) + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }
    
    @Override
    public String toString() {
        final KCallable reflected = this.compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "<init>".equals(this.getName()) ? "constructor (Kotlin reflection is not available)" : ("function " + this.getName() + " (Kotlin reflection is not available)");
    }
}
