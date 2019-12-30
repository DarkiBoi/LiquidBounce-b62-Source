// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.sequences.SequenceScope;
import kotlin.jvm.functions.Function2;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.sequences.Sequence;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f" }, d2 = { "checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib" })
public final class SlidingWindowKt
{
    public static final void checkWindowSizeStep(final int size, final int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(((size != step) ? ("Both size " + size + " and step " + step + " must be greater than zero.") : ("size " + size + " must be greater than zero.")).toString());
        }
    }
    
    @NotNull
    public static final <T> Sequence<List<T>> windowedSequence(@NotNull final Sequence<? extends T> $receiver, final int size, final int step, final boolean partialWindows, final boolean reuseBuffer) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        checkWindowSizeStep(size, step);
        return (Sequence<List<T>>)new Sequence<List<? extends T>>($receiver, size, step, partialWindows, reuseBuffer) {
            @NotNull
            @Override
            public Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(this.$this_windowedSequence$inlined.iterator(), this.$size$inlined, this.$step$inlined, this.$partialWindows$inlined, this.$reuseBuffer$inlined);
            }
        };
    }
    
    @NotNull
    public static final <T> Iterator<List<T>> windowedIterator(@NotNull final Iterator<? extends T> iterator, final int size, final int step, final boolean partialWindows, final boolean reuseBuffer) {
        Intrinsics.checkParameterIsNotNull(iterator, "iterator");
        if (!iterator.hasNext()) {
            return (Iterator<List<T>>)EmptyIterator.INSTANCE;
        }
        return SequencesKt__SequenceBuilderKt.iterator((Function2<? super SequenceScope<? super List<T>>, ? super Continuation<? super Unit>, ?>)new SlidingWindowKt$windowedIterator.SlidingWindowKt$windowedIterator$1(step, size, (Iterator)iterator, reuseBuffer, partialWindows, (Continuation)null));
    }
}
