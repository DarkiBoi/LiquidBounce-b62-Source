// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J5\u0010\u0003\u001a\u0002H\u0004\"\u0004\b\u0000\u0010\u00042\u0006\u0010\u0005\u001a\u0002H\u00042\u0018\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u0002H\u00040\u0007H\u0016¢\u0006\u0002\u0010\tJ(\u0010\n\u001a\u0004\u0018\u0001H\u000b\"\b\b\u0000\u0010\u000b*\u00020\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\rH\u0096\u0002¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0014\u0010\u0011\u001a\u00020\u00012\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\rH\u0016J\u0011\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0001H\u0096\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016¨\u0006\u0016" }, d2 = { "Lkotlin/coroutines/experimental/EmptyCoroutineContext;", "Lkotlin/coroutines/experimental/CoroutineContext;", "()V", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "(Lkotlin/coroutines/experimental/CoroutineContext$Key;)Lkotlin/coroutines/experimental/CoroutineContext$Element;", "hashCode", "", "minusKey", "plus", "context", "toString", "", "kotlin-stdlib_coroutinesExperimental" })
@SinceKotlin(version = "1.1")
public final class EmptyCoroutineContext implements CoroutineContext
{
    public static final EmptyCoroutineContext INSTANCE;
    
    @Nullable
    @Override
    public <E extends Element> E get(@NotNull final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return null;
    }
    
    @Override
    public <R> R fold(final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return initial;
    }
    
    @NotNull
    @Override
    public CoroutineContext plus(@NotNull final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context;
    }
    
    @NotNull
    @Override
    public CoroutineContext minusKey(@NotNull final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return this;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @NotNull
    @Override
    public String toString() {
        return "EmptyCoroutineContext";
    }
    
    private EmptyCoroutineContext() {
    }
    
    static {
        INSTANCE = new EmptyCoroutineContext();
    }
}
