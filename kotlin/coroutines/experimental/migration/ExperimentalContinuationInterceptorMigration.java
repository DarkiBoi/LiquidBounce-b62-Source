// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental.migration;

import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import kotlin.coroutines.experimental.ContinuationInterceptor;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\r0\f\"\u0004\b\u0000\u0010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\r0\fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u000f" }, d2 = { "Lkotlin/coroutines/experimental/migration/ExperimentalContinuationInterceptorMigration;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "interceptor", "Lkotlin/coroutines/ContinuationInterceptor;", "(Lkotlin/coroutines/ContinuationInterceptor;)V", "getInterceptor", "()Lkotlin/coroutines/ContinuationInterceptor;", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/experimental/CoroutineContext$Key;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "kotlin-stdlib_coroutinesExperimental" })
final class ExperimentalContinuationInterceptorMigration implements ContinuationInterceptor
{
    @NotNull
    private final kotlin.coroutines.ContinuationInterceptor interceptor;
    
    @NotNull
    @Override
    public CoroutineContext.Key<?> getKey() {
        return ContinuationInterceptor.Key;
    }
    
    @NotNull
    @Override
    public <T> Continuation<T> interceptContinuation(@NotNull final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return CoroutinesMigrationKt.toExperimentalContinuation(this.interceptor.interceptContinuation(CoroutinesMigrationKt.toContinuation(continuation)));
    }
    
    @NotNull
    public final kotlin.coroutines.ContinuationInterceptor getInterceptor() {
        return this.interceptor;
    }
    
    public ExperimentalContinuationInterceptorMigration(@NotNull final kotlin.coroutines.ContinuationInterceptor interceptor) {
        Intrinsics.checkParameterIsNotNull(interceptor, "interceptor");
        this.interceptor = interceptor;
    }
    
    @Nullable
    @Override
    public <E extends Element> E get(@NotNull final CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get(this, key);
    }
    
    @Override
    public <R> R fold(final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return DefaultImpls.fold(this, initial, operation);
    }
    
    @NotNull
    @Override
    public CoroutineContext minusKey(@NotNull final CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey(this, key);
    }
    
    @NotNull
    @Override
    public CoroutineContext plus(@NotNull final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus(this, context);
    }
}
