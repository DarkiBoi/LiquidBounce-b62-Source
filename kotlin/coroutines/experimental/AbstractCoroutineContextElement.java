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

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b'\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Lkotlin/coroutines/experimental/AbstractCoroutineContextElement;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "(Lkotlin/coroutines/experimental/CoroutineContext$Key;)V", "getKey", "()Lkotlin/coroutines/experimental/CoroutineContext$Key;", "kotlin-stdlib_coroutinesExperimental" })
@SinceKotlin(version = "1.1")
public abstract class AbstractCoroutineContextElement implements Element
{
    @NotNull
    private final Key<?> key;
    
    @NotNull
    @Override
    public Key<?> getKey() {
        return this.key;
    }
    
    public AbstractCoroutineContextElement(@NotNull final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        this.key = key;
    }
    
    @Nullable
    @Override
    public <E extends Element> E get(@NotNull final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get((CoroutineContext.Element)this, key);
    }
    
    @Override
    public <R> R fold(final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return DefaultImpls.fold((CoroutineContext.Element)this, initial, operation);
    }
    
    @NotNull
    @Override
    public CoroutineContext minusKey(@NotNull final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey((CoroutineContext.Element)this, key);
    }
    
    @NotNull
    @Override
    public CoroutineContext plus(@NotNull final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus((CoroutineContext.Element)this, context);
    }
}
