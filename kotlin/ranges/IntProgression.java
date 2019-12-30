// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.internal.ProgressionUtilKt;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import kotlin.collections.IntIterator;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0017B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0002¢\u0006\u0002\u0010\u0006J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0002H\u0016J\b\u0010\u0012\u001a\u00020\u000eH\u0016J\t\u0010\u0013\u001a\u00020\u0014H\u0096\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u0011\u0010\u0007\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018" }, d2 = { "Lkotlin/ranges/IntProgression;", "", "", "start", "endInclusive", "step", "(III)V", "first", "getFirst", "()I", "last", "getLast", "getStep", "equals", "", "other", "", "hashCode", "isEmpty", "iterator", "Lkotlin/collections/IntIterator;", "toString", "", "Companion", "kotlin-stdlib" })
public class IntProgression implements Iterable<Integer>, KMappedMarker
{
    private final int first;
    private final int last;
    private final int step;
    public static final Companion Companion;
    
    public final int getFirst() {
        return this.first;
    }
    
    public final int getLast() {
        return this.last;
    }
    
    public final int getStep() {
        return this.step;
    }
    
    @NotNull
    @Override
    public IntIterator iterator() {
        return new IntProgressionIterator(this.first, this.last, this.step);
    }
    
    public boolean isEmpty() {
        return (this.step > 0) ? (this.first > this.last) : (this.first < this.last);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof IntProgression && ((this.isEmpty() && ((IntProgression)other).isEmpty()) || (this.first == ((IntProgression)other).first && this.last == ((IntProgression)other).last && this.step == ((IntProgression)other).step));
    }
    
    @Override
    public int hashCode() {
        return this.isEmpty() ? -1 : (31 * (31 * this.first + this.last) + this.step);
    }
    
    @NotNull
    @Override
    public String toString() {
        return (this.step > 0) ? (this.first + ".." + this.last + " step " + this.step) : (this.first + " downTo " + this.last + " step " + -this.step);
    }
    
    public IntProgression(final int start, final int endInclusive, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = ProgressionUtilKt.getProgressionLastElement(start, endInclusive, step);
        this.step = step;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\t" }, d2 = { "Lkotlin/ranges/IntProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/IntProgression;", "rangeStart", "", "rangeEnd", "step", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final IntProgression fromClosedRange(final int rangeStart, final int rangeEnd, final int step) {
            return new IntProgression(rangeStart, rangeEnd, step);
        }
        
        private Companion() {
        }
    }
}
