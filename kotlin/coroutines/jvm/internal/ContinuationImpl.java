// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import org.jetbrains.annotations.Nullable;
import kotlin.coroutines.ContinuationInterceptor;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.Continuation;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b!\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005B!\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003J\b\u0010\r\u001a\u00020\u000eH\u0014R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f" }, d2 = { "Lkotlin/coroutines/jvm/internal/ContinuationImpl;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "_context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/Continuation;Lkotlin/coroutines/CoroutineContext;)V", "context", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "intercepted", "releaseIntercepted", "", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class ContinuationImpl extends BaseContinuationImpl
{
    private transient Continuation<Object> intercepted;
    private final CoroutineContext _context;
    
    @NotNull
    @Override
    public CoroutineContext getContext() {
        final CoroutineContext context = this._context;
        if (context == null) {
            Intrinsics.throwNpe();
        }
        return context;
    }
    
    @NotNull
    public final Continuation<Object> intercepted() {
        Continuation<Object> intercepted;
        if ((intercepted = this.intercepted) == null) {
            final ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor)this.getContext().get((CoroutineContext.Key<CoroutineContext.Element>)ContinuationInterceptor.Key);
            Continuation<Object> interceptContinuation;
            if (continuationInterceptor == null || (interceptContinuation = continuationInterceptor.interceptContinuation(this)) == null) {
                interceptContinuation = this;
            }
            final Continuation it;
            final Continuation continuation = it = interceptContinuation;
            this.intercepted = (Continuation<Object>)it;
            intercepted = (Continuation<Object>)continuation;
        }
        return intercepted;
    }
    
    @Override
    protected void releaseIntercepted() {
        final Continuation intercepted = this.intercepted;
        if (intercepted != null && intercepted != this) {
            final ContinuationInterceptor value = this.getContext().get((CoroutineContext.Key<ContinuationInterceptor>)ContinuationInterceptor.Key);
            if (value == null) {
                Intrinsics.throwNpe();
            }
            value.releaseInterceptedContinuation(intercepted);
        }
        this.intercepted = CompletedContinuation.INSTANCE;
    }
    
    public ContinuationImpl(@Nullable final Continuation<Object> completion, @Nullable final CoroutineContext _context) {
        super(completion);
        this._context = _context;
    }
    
    public ContinuationImpl(@Nullable final Continuation<Object> completion) {
        this(completion, (completion != null) ? completion.getContext() : null);
    }
}
