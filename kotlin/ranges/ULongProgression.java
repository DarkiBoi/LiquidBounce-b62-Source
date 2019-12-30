// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.internal.UProgressionUtilKt;
import org.jetbrains.annotations.Nullable;
import kotlin.UnsignedKt;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import kotlin.collections.ULongIterator;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ULong;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0017\u0018\u0000 \u001a2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001aB\"\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016J\t\u0010\u0016\u001a\u00020\u0017H\u0096\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0016\u0010\b\u001a\u00020\u0002\u00f8\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u0002\u00f8\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001b" }, d2 = { "Lkotlin/ranges/ULongProgression;", "", "Lkotlin/ULong;", "start", "endInclusive", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "first", "getFirst", "()J", "J", "last", "getLast", "getStep", "equals", "", "other", "", "hashCode", "", "isEmpty", "iterator", "Lkotlin/collections/ULongIterator;", "toString", "", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public class ULongProgression implements Iterable<ULong>, KMappedMarker
{
    private final long first;
    private final long last;
    private final long step;
    public static final Companion Companion;
    
    public final long getFirst() {
        return this.first;
    }
    
    public final long getLast() {
        return this.last;
    }
    
    public final long getStep() {
        return this.step;
    }
    
    @NotNull
    @Override
    public ULongIterator iterator() {
        return new ULongProgressionIterator(this.first, this.last, this.step, null);
    }
    
    public boolean isEmpty() {
        return (this.step > 0L) ? (UnsignedKt.ulongCompare(this.first, this.last) > 0) : (UnsignedKt.ulongCompare(this.first, this.last) < 0);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof ULongProgression && ((this.isEmpty() && ((ULongProgression)other).isEmpty()) || (this.first == ((ULongProgression)other).first && this.last == ((ULongProgression)other).last && this.step == ((ULongProgression)other).step));
    }
    
    @Override
    public int hashCode() {
        return this.isEmpty() ? -1 : (31 * (31 * (int)ULong.constructor-impl(this.first ^ ULong.constructor-impl(this.first >>> 32)) + (int)ULong.constructor-impl(this.last ^ ULong.constructor-impl(this.last >>> 32))) + (int)(this.step ^ this.step >>> 32));
    }
    
    @NotNull
    @Override
    public String toString() {
        return (this.step > 0L) ? (ULong.toString-impl(this.first) + ".." + ULong.toString-impl(this.last) + " step " + this.step) : (ULong.toString-impl(this.first) + " downTo " + ULong.toString-impl(this.last) + " step " + -this.step);
    }
    
    private ULongProgression(final long start, final long endInclusive, final long step) {
        if (step == 0L) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Long.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = UProgressionUtilKt.getProgressionLastElement-7ftBX0g(start, endInclusive, step);
        this.step = step;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t\u00f8\u0001\u0000¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f" }, d2 = { "Lkotlin/ranges/ULongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/ULongProgression;", "rangeStart", "Lkotlin/ULong;", "rangeEnd", "step", "", "fromClosedRange-7ftBX0g", "(JJJ)Lkotlin/ranges/ULongProgression;", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final ULongProgression fromClosedRange-7ftBX0g(final long rangeStart, final long rangeEnd, final long step) {
            return new ULongProgression(rangeStart, rangeEnd, step, null);
        }
        
        private Companion() {
        }
    }
}
