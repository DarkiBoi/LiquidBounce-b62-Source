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

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H&¨\u0006\u0007" }, d2 = { "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "Key", "kotlin-stdlib_coroutinesExperimental" })
@SinceKotlin(version = "1.1")
public interface ContinuationInterceptor extends Element
{
    public static final Key Key = ContinuationInterceptor.Key.$$INSTANCE;
    
    @NotNull
     <T> Continuation<T> interceptContinuation(@NotNull final Continuation<? super T> p0);
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004" }, d2 = { "Lkotlin/coroutines/experimental/ContinuationInterceptor$Key;", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "()V", "kotlin-stdlib_coroutinesExperimental" })
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        private Key() {
        }
        
        static {
            $$INSTANCE = new Key();
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 3)
    public static final class DefaultImpls
    {
        @Nullable
        public static <E extends Element> E get(final ContinuationInterceptor $this, @NotNull final CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.get((CoroutineContext.Element)$this, key);
        }
        
        public static <R> R fold(final ContinuationInterceptor $this, final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
            Intrinsics.checkParameterIsNotNull(operation, "operation");
            return Element.DefaultImpls.fold((CoroutineContext.Element)$this, initial, operation);
        }
        
        @NotNull
        public static CoroutineContext minusKey(final ContinuationInterceptor $this, @NotNull final CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.minusKey((CoroutineContext.Element)$this, key);
        }
        
        @NotNull
        public static CoroutineContext plus(final ContinuationInterceptor $this, @NotNull final CoroutineContext context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return Element.DefaultImpls.plus((CoroutineContext.Element)$this, context);
        }
    }
}
