// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0000H\u0002J\u0013\u0010\u000e\u001a\u00020\u000b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J5\u0010\u0011\u001a\u0002H\u0012\"\u0004\b\u0000\u0010\u00122\u0006\u0010\u0013\u001a\u0002H\u00122\u0018\u0010\u0014\u001a\u0014\u0012\u0004\u0012\u0002H\u0012\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u0002H\u00120\u0015H\u0016¢\u0006\u0002\u0010\u0016J(\u0010\u0017\u001a\u0004\u0018\u0001H\u0018\"\b\b\u0000\u0010\u0018*\u00020\u00042\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001aH\u0096\u0002¢\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0016J\u0014\u0010\u001e\u001a\u00020\u00012\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aH\u0016J\b\u0010\u001f\u001a\u00020\u001dH\u0002J\b\u0010 \u001a\u00020!H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\"" }, d2 = { "Lkotlin/coroutines/experimental/CombinedContext;", "Lkotlin/coroutines/experimental/CoroutineContext;", "left", "element", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/coroutines/experimental/CoroutineContext$Element;)V", "getElement", "()Lkotlin/coroutines/experimental/CoroutineContext$Element;", "getLeft", "()Lkotlin/coroutines/experimental/CoroutineContext;", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "(Lkotlin/coroutines/experimental/CoroutineContext$Key;)Lkotlin/coroutines/experimental/CoroutineContext$Element;", "hashCode", "", "minusKey", "size", "toString", "", "kotlin-stdlib_coroutinesExperimental" })
public final class CombinedContext implements CoroutineContext
{
    @NotNull
    private final CoroutineContext left;
    @NotNull
    private final Element element;
    
    @Nullable
    @Override
    public <E extends Element> E get(@NotNull final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        CombinedContext cur = this;
        while (true) {
            final Element value = cur.element.get(key);
            if (value != null) {
                final Element it = value;
                return (E)it;
            }
            final CoroutineContext next = cur.left;
            if (!(next instanceof CombinedContext)) {
                return next.get(key);
            }
            cur = (CombinedContext)next;
        }
    }
    
    @Override
    public <R> R fold(final R initial, @NotNull final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R)operation.invoke(this.left.fold(initial, operation), this.element);
    }
    
    @NotNull
    @Override
    public CoroutineContext minusKey(@NotNull final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        final Element value = this.element.get(key);
        if (value != null) {
            final Element it = value;
            return this.left;
        }
        final CoroutineContext newLeft = this.left.minusKey(key);
        return (CoroutineContext)((newLeft == this.left) ? ((CombinedContext)this) : ((newLeft == EmptyCoroutineContext.INSTANCE) ? ((Element)this.element) : ((CombinedContext)new CombinedContext(newLeft, this.element))));
    }
    
    private final int size() {
        return (this.left instanceof CombinedContext) ? (((CombinedContext)this.left).size() + 1) : 2;
    }
    
    private final boolean contains(final Element element) {
        return Intrinsics.areEqual(this.get(element.getKey()), element);
    }
    
    private final boolean containsAll(final CombinedContext context) {
        CombinedContext cur = context;
        while (this.contains(cur.element)) {
            final CoroutineContext next = cur.left;
            if (next instanceof CombinedContext) {
                cur = (CombinedContext)next;
            }
            else {
                final CoroutineContext coroutineContext = next;
                if (coroutineContext == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.CoroutineContext.Element");
                }
                return this.contains((Element)coroutineContext);
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return this == other || (other instanceof CombinedContext && ((CombinedContext)other).size() == this.size() && ((CombinedContext)other).containsAll(this));
    }
    
    @Override
    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }
    
    @NotNull
    @Override
    public String toString() {
        return "[" + this.fold("", (Function2<? super String, ? super Element, ? extends String>)CombinedContext$toString.CombinedContext$toString$1.INSTANCE) + "]";
    }
    
    @NotNull
    public final CoroutineContext getLeft() {
        return this.left;
    }
    
    @NotNull
    public final Element getElement() {
        return this.element;
    }
    
    public CombinedContext(@NotNull final CoroutineContext left, @NotNull final Element element) {
        Intrinsics.checkParameterIsNotNull(left, "left");
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.left = left;
        this.element = element;
    }
    
    @NotNull
    @Override
    public CoroutineContext plus(@NotNull final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus(this, context);
    }
}
