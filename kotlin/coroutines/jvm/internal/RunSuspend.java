// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.ResultKt;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.Nullable;
import kotlin.Result;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010\u000e\u001a\u00020\u0002J\u001e\u0010\u000f\u001a\u00020\u00022\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\tH\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0010R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R%\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\tX\u0086\u000e\u00f8\u0001\u0000¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011" }, d2 = { "Lkotlin/coroutines/jvm/internal/RunSuspend;", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "result", "Lkotlin/Result;", "getResult", "()Lkotlin/Result;", "setResult", "(Lkotlin/Result;)V", "await", "resumeWith", "(Ljava/lang/Object;)V", "kotlin-stdlib" })
final class RunSuspend implements Continuation<Unit>
{
    @Nullable
    private Result<Unit> result;
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
    
    @Nullable
    public final Result<Unit> getResult() {
        return this.result;
    }
    
    public final void setResult(@Nullable final Result<Unit> <set-?>) {
        this.result = <set-?>;
    }
    
    @Override
    public void resumeWith(@NotNull final Object result) {
        synchronized (this) {
            this.result = (Result<Unit>)Result.box-impl(result);
            if (this == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
            }
            this.notifyAll();
            final Unit instance = Unit.INSTANCE;
        }
    }
    
    public final void await() {
        synchronized (this) {
            while (true) {
                final Result result = this.result;
                if (result != null) {
                    ResultKt.throwOnFailure(result.unbox-impl());
                    return;
                }
                if (this == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                }
                this.wait();
            }
        }
    }
    
    public RunSuspend() {
    }
}
