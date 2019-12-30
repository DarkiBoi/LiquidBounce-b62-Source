// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007¢\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\n¨\u0006\u000b" }, d2 = { "asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", "index", "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib" }, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt extends CollectionsKt__MutableCollectionsKt
{
    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(@NotNull final List<?> $receiver, final int index) {
        final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex($receiver);
        if (0 <= index) {
            if (lastIndex >= index) {
                return CollectionsKt__CollectionsKt.getLastIndex($receiver) - index;
            }
        }
        throw new IndexOutOfBoundsException("Element index " + index + " must be in range [" + new IntRange(0, CollectionsKt__CollectionsKt.getLastIndex($receiver)) + "].");
    }
    
    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(@NotNull final List<?> $receiver, final int index) {
        final int size = $receiver.size();
        if (0 <= index) {
            if (size >= index) {
                return $receiver.size() - index;
            }
        }
        throw new IndexOutOfBoundsException("Position index " + index + " must be in range [" + new IntRange(0, $receiver.size()) + "].");
    }
    
    @NotNull
    public static final <T> List<T> asReversed(@NotNull final List<? extends T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return new ReversedListReadOnly<T>($receiver);
    }
    
    @JvmName(name = "asReversedMutable")
    @NotNull
    public static final <T> List<T> asReversedMutable(@NotNull final List<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return new ReversedList<T>($receiver);
    }
    
    public CollectionsKt__ReversedViewsKt() {
    }
}
