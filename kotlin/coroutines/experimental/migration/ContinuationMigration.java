// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental.migration;

import kotlin.jvm.internal.Intrinsics;
import kotlin.Result;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u001e\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0010R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011" }, d2 = { "Lkotlin/coroutines/experimental/migration/ContinuationMigration;", "T", "Lkotlin/coroutines/Continuation;", "continuation", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlin/coroutines/experimental/Continuation;)V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getContinuation", "()Lkotlin/coroutines/experimental/Continuation;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "kotlin-stdlib_coroutinesExperimental" })
final class ContinuationMigration<T> implements Continuation<T>
{
    @NotNull
    private final CoroutineContext context;
    @NotNull
    private final kotlin.coroutines.experimental.Continuation<T> continuation;
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        return this.context;
    }
    
    @Override
    public void resumeWith(@NotNull final Object result) {
        if (Result.isSuccess-impl(result)) {
            final Object it = result;
            this.continuation.resume((T)it);
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(result);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            this.continuation.resumeWithException(it2);
        }
    }
    
    @NotNull
    public final kotlin.coroutines.experimental.Continuation<T> getContinuation() {
        return this.continuation;
    }
    
    public ContinuationMigration(@NotNull final kotlin.coroutines.experimental.Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.continuation = (kotlin.coroutines.experimental.Continuation<T>)continuation;
        this.context = CoroutinesMigrationKt.toCoroutineContext(this.continuation.getContext());
    }
}
