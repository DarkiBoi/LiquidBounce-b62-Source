// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\bf\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\nH\u0016R\u0012\u0010\u0004\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006¨\u0006\u000e" }, d2 = { "Lkotlin/ranges/ClosedRange;", "T", "", "", "endInclusive", "getEndInclusive", "()Ljava/lang/Comparable;", "start", "getStart", "contains", "", "value", "(Ljava/lang/Comparable;)Z", "isEmpty", "kotlin-stdlib" })
public interface ClosedRange<T extends Comparable<? super T>>
{
    @NotNull
    T getStart();
    
    @NotNull
    T getEndInclusive();
    
    boolean contains(@NotNull final T p0);
    
    boolean isEmpty();
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 3)
    public static final class DefaultImpls
    {
        public static <T extends Comparable<? super T>> boolean contains(final ClosedRange<T> $this, @NotNull final T value) {
            Intrinsics.checkParameterIsNotNull(value, "value");
            return value.compareTo((Object)$this.getStart()) >= 0 && value.compareTo((Object)$this.getEndInclusive()) <= 0;
        }
        
        public static <T extends Comparable<? super T>> boolean isEmpty(final ClosedRange<T> $this) {
            return $this.getStart().compareTo((Object)$this.getEndInclusive()) > 0;
        }
    }
}
