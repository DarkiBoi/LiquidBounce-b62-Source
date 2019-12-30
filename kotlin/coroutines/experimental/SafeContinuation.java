// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental;

import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.PublishedApi;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u0001\u0018\u0000 \u0015*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0002\u0015\u0016B\u0015\b\u0011\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0004B\u001f\b\u0000\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\n\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0001J\u0015\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0011J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017" }, d2 = { "Lkotlin/coroutines/experimental/SafeContinuation;", "T", "Lkotlin/coroutines/experimental/Continuation;", "delegate", "(Lkotlin/coroutines/experimental/Continuation;)V", "initialResult", "", "(Lkotlin/coroutines/experimental/Continuation;Ljava/lang/Object;)V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "result", "getResult", "resume", "", "value", "(Ljava/lang/Object;)V", "resumeWithException", "exception", "", "Companion", "Fail", "kotlin-stdlib_coroutinesExperimental" })
@PublishedApi
public final class SafeContinuation<T> implements Continuation<T>
{
    private volatile Object result;
    private final Continuation<T> delegate;
    private static final Object UNDECIDED;
    private static final Object RESUMED;
    private static final AtomicReferenceFieldUpdater<SafeContinuation<?>, Object> RESULT;
    public static final Companion Companion;
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        return this.delegate.getContext();
    }
    
    @Override
    public void resume(final T value) {
        while (true) {
            final Object result = this.result;
            if (result == SafeContinuation.UNDECIDED) {
                if (SafeContinuation.RESULT.compareAndSet(this, SafeContinuation.UNDECIDED, value)) {
                    return;
                }
                continue;
            }
            else {
                if (result != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new IllegalStateException("Already resumed");
                }
                if (SafeContinuation.RESULT.compareAndSet(this, IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED(), SafeContinuation.RESUMED)) {
                    this.delegate.resume(value);
                    return;
                }
                continue;
            }
        }
    }
    
    @Override
    public void resumeWithException(@NotNull final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        while (true) {
            final Object result = this.result;
            if (result == SafeContinuation.UNDECIDED) {
                if (SafeContinuation.RESULT.compareAndSet(this, SafeContinuation.UNDECIDED, new Fail(exception))) {
                    return;
                }
                continue;
            }
            else {
                if (result != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new IllegalStateException("Already resumed");
                }
                if (SafeContinuation.RESULT.compareAndSet(this, IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED(), SafeContinuation.RESUMED)) {
                    this.delegate.resumeWithException(exception);
                    return;
                }
                continue;
            }
        }
    }
    
    @PublishedApi
    @Nullable
    public final Object getResult() {
        Object result = this.result;
        if (result == SafeContinuation.UNDECIDED) {
            if (SafeContinuation.RESULT.compareAndSet(this, SafeContinuation.UNDECIDED, IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED())) {
                return IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
            }
            result = this.result;
        }
        if (result == SafeContinuation.RESUMED) {
            return IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        }
        if (result instanceof Fail) {
            throw ((Fail)result).getException();
        }
        return result;
    }
    
    public SafeContinuation(@NotNull final Continuation<? super T> delegate, @Nullable final Object initialResult) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.delegate = (Continuation<T>)delegate;
        this.result = initialResult;
    }
    
    @PublishedApi
    public SafeContinuation(@NotNull final Continuation<? super T> delegate) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this(delegate, SafeContinuation.UNDECIDED);
    }
    
    static {
        Companion = new Companion(null);
        UNDECIDED = new Object();
        RESUMED = new Object();
        RESULT = AtomicReferenceFieldUpdater.newUpdater((Class<SafeContinuation<?>>)SafeContinuation.class, Object.class, "result");
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002RZ\u0010\u0003\u001aF\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0001 \u0006*\"\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u00040\u00048\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n" }, d2 = { "Lkotlin/coroutines/experimental/SafeContinuation$Companion;", "", "()V", "RESULT", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlin/coroutines/experimental/SafeContinuation;", "kotlin.jvm.PlatformType", "RESULT$annotations", "RESUMED", "UNDECIDED", "kotlin-stdlib_coroutinesExperimental" })
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Lkotlin/coroutines/experimental/SafeContinuation$Fail;", "", "exception", "", "(Ljava/lang/Throwable;)V", "getException", "()Ljava/lang/Throwable;", "kotlin-stdlib_coroutinesExperimental" })
    private static final class Fail
    {
        @NotNull
        private final Throwable exception;
        
        @NotNull
        public final Throwable getException() {
            return this.exception;
        }
        
        public Fail(@NotNull final Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            this.exception = exception;
        }
    }
}
