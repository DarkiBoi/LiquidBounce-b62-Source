// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import kotlin.reflect.KVisibility;
import java.util.Map;
import kotlin.reflect.KTypeParameter;
import java.lang.annotation.Annotation;
import kotlin.reflect.KType;
import kotlin.reflect.KParameter;
import java.util.List;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.SinceKotlin;
import java.io.Serializable;
import kotlin.reflect.KCallable;

public abstract class CallableReference implements KCallable, Serializable
{
    private transient KCallable reflected;
    @SinceKotlin(version = "1.1")
    protected final Object receiver;
    @SinceKotlin(version = "1.1")
    public static final Object NO_RECEIVER;
    
    public CallableReference() {
        this(CallableReference.NO_RECEIVER);
    }
    
    @SinceKotlin(version = "1.1")
    protected CallableReference(final Object receiver) {
        this.receiver = receiver;
    }
    
    protected abstract KCallable computeReflected();
    
    @SinceKotlin(version = "1.1")
    public Object getBoundReceiver() {
        return this.receiver;
    }
    
    @SinceKotlin(version = "1.1")
    public KCallable compute() {
        KCallable result = this.reflected;
        if (result == null) {
            result = this.computeReflected();
            this.reflected = result;
        }
        return result;
    }
    
    @SinceKotlin(version = "1.1")
    protected KCallable getReflected() {
        final KCallable result = this.compute();
        if (result == this) {
            throw new KotlinReflectionNotSupportedError();
        }
        return result;
    }
    
    public KDeclarationContainer getOwner() {
        throw new AbstractMethodError();
    }
    
    @Override
    public String getName() {
        throw new AbstractMethodError();
    }
    
    public String getSignature() {
        throw new AbstractMethodError();
    }
    
    @Override
    public List<KParameter> getParameters() {
        return (List<KParameter>)this.getReflected().getParameters();
    }
    
    @Override
    public KType getReturnType() {
        return this.getReflected().getReturnType();
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return (List<Annotation>)this.getReflected().getAnnotations();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public List<KTypeParameter> getTypeParameters() {
        return (List<KTypeParameter>)this.getReflected().getTypeParameters();
    }
    
    @Override
    public Object call(final Object... args) {
        return this.getReflected().call(args);
    }
    
    @Override
    public Object callBy(final Map args) {
        return this.getReflected().callBy(args);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public KVisibility getVisibility() {
        return this.getReflected().getVisibility();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isFinal() {
        return this.getReflected().isFinal();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isOpen() {
        return this.getReflected().isOpen();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isAbstract() {
        return this.getReflected().isAbstract();
    }
    
    @SinceKotlin(version = "1.3")
    @Override
    public boolean isSuspend() {
        return this.getReflected().isSuspend();
    }
    
    static {
        NO_RECEIVER = NoReceiver.INSTANCE;
    }
    
    @SinceKotlin(version = "1.2")
    private static class NoReceiver implements Serializable
    {
        private static final NoReceiver INSTANCE;
        
        private Object readResolve() throws ObjectStreamException {
            return NoReceiver.INSTANCE;
        }
        
        static {
            INSTANCE = new NoReceiver();
        }
    }
}
