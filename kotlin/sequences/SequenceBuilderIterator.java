// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.CoroutineContext;
import kotlin.ResultKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.NotNull;
import java.util.NoSuchElementException;
import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import java.util.Iterator;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001bJ\u001e\u0010\u001d\u001a\u00020\u00052\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u001fH\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010 J\u0019\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00028\u0000H\u0096@\u00f8\u0001\u0000¢\u0006\u0002\u0010#J\u001f\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@\u00f8\u0001\u0000¢\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006'" }, d2 = { "Lkotlin/sequences/SequenceBuilderIterator;", "T", "Lkotlin/sequences/SequenceScope;", "", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/Continuation;", "setNextStep", "(Lkotlin/coroutines/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Lkotlin/sequences/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "yield", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib" })
final class SequenceBuilderIterator<T> extends SequenceScope<T> implements Iterator<T>, Continuation<Unit>, KMappedMarker
{
    private int state;
    private T nextValue;
    private Iterator<? extends T> nextIterator;
    @Nullable
    private Continuation<? super Unit> nextStep;
    
    @Nullable
    public final Continuation<Unit> getNextStep() {
        return (Continuation<Unit>)this.nextStep;
    }
    
    public final void setNextStep(@Nullable final Continuation<? super Unit> <set-?>) {
        this.nextStep = <set-?>;
    }
    
    @Override
    public boolean hasNext() {
        while (true) {
            switch (this.state) {
                case 0: {
                    break;
                }
                case 1: {
                    final Iterator<? extends T> nextIterator = this.nextIterator;
                    if (nextIterator == null) {
                        Intrinsics.throwNpe();
                    }
                    if (nextIterator.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                    break;
                }
                case 4: {
                    return false;
                }
                case 2:
                case 3: {
                    return true;
                }
                default: {
                    throw this.exceptionalState();
                }
            }
            this.state = 5;
            final Continuation<? super Unit> nextStep = this.nextStep;
            if (nextStep == null) {
                Intrinsics.throwNpe();
            }
            final Continuation step = nextStep;
            this.nextStep = null;
            final Continuation continuation = step;
            final Unit instance = Unit.INSTANCE;
            final Continuation continuation2 = continuation;
            final Result.Companion companion = Result.Companion;
            continuation2.resumeWith(Result.constructor-impl(instance));
        }
    }
    
    @Override
    public T next() {
        switch (this.state) {
            case 0:
            case 1: {
                return this.nextNotReady();
            }
            case 2: {
                this.state = 1;
                final Iterator<? extends T> nextIterator = this.nextIterator;
                if (nextIterator == null) {
                    Intrinsics.throwNpe();
                }
                return (T)nextIterator.next();
            }
            case 3: {
                this.state = 0;
                final Object result = this.nextValue;
                this.nextValue = null;
                return (T)result;
            }
            default: {
                throw this.exceptionalState();
            }
        }
    }
    
    private final T nextNotReady() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.next();
    }
    
    private final Throwable exceptionalState() {
        RuntimeException ex = null;
        switch (this.state) {
            case 4: {
                ex = new NoSuchElementException();
                break;
            }
            case 5: {
                ex = new IllegalStateException("Iterator has failed.");
                break;
            }
            default: {
                ex = new IllegalStateException("Unexpected state of the iterator: " + this.state);
                break;
            }
        }
        return ex;
    }
    
    @Nullable
    @Override
    public Object yield(final T value, @NotNull final Continuation<? super Unit> frame) {
        this.nextValue = value;
        this.state = 3;
        final Continuation c = frame;
        this.setNextStep(c);
        final Object coroutine_SUSPENDED = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_SUSPENDED == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(frame);
        }
        return coroutine_SUSPENDED;
    }
    
    @Nullable
    @Override
    public Object yieldAll(@NotNull final Iterator<? extends T> iterator, @NotNull final Continuation<? super Unit> frame) {
        if (!iterator.hasNext()) {
            return Unit.INSTANCE;
        }
        this.nextIterator = iterator;
        this.state = 2;
        final Continuation c = frame;
        this.setNextStep(c);
        final Object coroutine_SUSPENDED = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (coroutine_SUSPENDED == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(frame);
        }
        return coroutine_SUSPENDED;
    }
    
    @Override
    public void resumeWith(@NotNull final Object result) {
        ResultKt.throwOnFailure(result);
        this.state = 4;
    }
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
    
    public SequenceBuilderIterator() {
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
