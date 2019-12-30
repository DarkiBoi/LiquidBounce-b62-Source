// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental.migration;

import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Result;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u0015\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0013" }, d2 = { "Lkotlin/coroutines/experimental/migration/ExperimentalContinuationMigration;", "T", "Lkotlin/coroutines/experimental/Continuation;", "continuation", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/Continuation;)V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "getContinuation", "()Lkotlin/coroutines/Continuation;", "resume", "", "value", "(Ljava/lang/Object;)V", "resumeWithException", "exception", "", "kotlin-stdlib_coroutinesExperimental" })
final class ExperimentalContinuationMigration<T> implements Continuation<T>
{
    @NotNull
    private final CoroutineContext context;
    @NotNull
    private final kotlin.coroutines.Continuation<T> continuation;
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        return this.context;
    }
    
    @Override
    public void resume(final T value) {
        final kotlin.coroutines.Continuation<T> continuation = this.continuation;
        final Result.Companion companion = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(value));
    }
    
    @Override
    public void resumeWithException(@NotNull final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        final kotlin.coroutines.Continuation<T> continuation = this.continuation;
        final Result.Companion companion = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(exception)));
    }
    
    @NotNull
    public final kotlin.coroutines.Continuation<T> getContinuation() {
        return this.continuation;
    }
    
    public ExperimentalContinuationMigration(@NotNull final kotlin.coroutines.Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.continuation = (kotlin.coroutines.Continuation<T>)continuation;
        this.context = CoroutinesMigrationKt.toExperimentalCoroutineContext(this.continuation.getContext());
    }
}
