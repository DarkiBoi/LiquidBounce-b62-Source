// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import kotlin.ranges.LongRange;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0014\u0010\r\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u001a\u0014\u0010\u0010\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H\u0000¨\u0006\u0014" }, d2 = { "Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib" })
public final class RandomKt
{
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final Random Random(final int seed) {
        return new XorWowRandom(seed, seed >> 31);
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final Random Random(final long seed) {
        return new XorWowRandom((int)seed, (int)(seed >> 32));
    }
    
    @SinceKotlin(version = "1.3")
    public static final int nextInt(@NotNull final Random $receiver, @NotNull final IntRange range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return (range.getLast() < Integer.MAX_VALUE) ? $receiver.nextInt(range.getFirst(), range.getLast() + 1) : ((range.getFirst() > Integer.MIN_VALUE) ? ($receiver.nextInt(range.getFirst() - 1, range.getLast()) + 1) : $receiver.nextInt());
    }
    
    @SinceKotlin(version = "1.3")
    public static final long nextLong(@NotNull final Random $receiver, @NotNull final LongRange range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return (range.getLast() < Long.MAX_VALUE) ? $receiver.nextLong(range.getStart(), range.getEndInclusive() + 1L) : ((range.getStart() > Long.MIN_VALUE) ? ($receiver.nextLong(range.getStart() - 1L, range.getEndInclusive()) + 1L) : $receiver.nextLong());
    }
    
    public static final int takeUpperBits(final int $receiver, final int bitCount) {
        return $receiver >>> 32 - bitCount & -bitCount >> 31;
    }
    
    public static final void checkRangeBounds(final int from, final int until) {
        if (until <= from) {
            throw new IllegalArgumentException(boundsErrorMessage(from, until).toString());
        }
    }
    
    public static final void checkRangeBounds(final long from, final long until) {
        if (until <= from) {
            throw new IllegalArgumentException(boundsErrorMessage(from, until).toString());
        }
    }
    
    public static final void checkRangeBounds(final double from, final double until) {
        if (until <= from) {
            throw new IllegalArgumentException(boundsErrorMessage(from, until).toString());
        }
    }
    
    @NotNull
    public static final String boundsErrorMessage(@NotNull final Object from, @NotNull final Object until) {
        Intrinsics.checkParameterIsNotNull(from, "from");
        Intrinsics.checkParameterIsNotNull(until, "until");
        return "Random range is empty: [" + from + ", " + until + ").";
    }
}
