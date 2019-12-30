// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import org.jetbrains.annotations.Nullable;
import kotlin.UnsignedKt;
import org.jetbrains.annotations.NotNull;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.UInt;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00172\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001\u0017B\u0018\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0006J\u001b\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H\u0096\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eJ\u0013\u0010\u000f\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000bH\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004\u00f8\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\u0004\u001a\u00020\u00038VX\u0096\u0004\u00f8\u0001\u0000¢\u0006\u0006\u001a\u0004\b\t\u0010\b\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0018" }, d2 = { "Lkotlin/ranges/UIntRange;", "Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/UInt;", "start", "endInclusive", "(IILkotlin/jvm/internal/DefaultConstructorMarker;)V", "getEndInclusive", "()Lkotlin/UInt;", "getStart", "contains", "", "value", "contains-WZ4Q5Ns", "(I)Z", "equals", "other", "", "hashCode", "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public final class UIntRange extends UIntProgression implements ClosedRange<UInt>
{
    @NotNull
    private static final UIntRange EMPTY;
    public static final Companion Companion;
    
    @NotNull
    @Override
    public UInt getStart() {
        return UInt.box-impl(this.getFirst());
    }
    
    @NotNull
    @Override
    public UInt getEndInclusive() {
        return UInt.box-impl(this.getLast());
    }
    
    public boolean contains-WZ4Q5Ns(final int value) {
        return UnsignedKt.uintCompare(this.getFirst(), value) <= 0 && UnsignedKt.uintCompare(value, this.getLast()) <= 0;
    }
    
    @Override
    public boolean isEmpty() {
        return UnsignedKt.uintCompare(this.getFirst(), this.getLast()) > 0;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof UIntRange && ((this.isEmpty() && ((UIntRange)other).isEmpty()) || (this.getFirst() == ((UIntRange)other).getFirst() && this.getLast() == ((UIntRange)other).getLast()));
    }
    
    @Override
    public int hashCode() {
        return this.isEmpty() ? -1 : (31 * this.getFirst() + this.getLast());
    }
    
    @NotNull
    @Override
    public String toString() {
        return UInt.toString-impl(this.getFirst()) + ".." + UInt.toString-impl(this.getLast());
    }
    
    private UIntRange(final int start, final int endInclusive) {
        super(start, endInclusive, 1, null);
    }
    
    static {
        Companion = new Companion(null);
        EMPTY = new UIntRange(-1, 0, null);
    }
    
    public static final /* synthetic */ UIntRange access$getEMPTY$cp() {
        return UIntRange.EMPTY;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Lkotlin/ranges/UIntRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/UIntRange;", "getEMPTY", "()Lkotlin/ranges/UIntRange;", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final UIntRange getEMPTY() {
            return UIntRange.access$getEMPTY$cp();
        }
        
        private Companion() {
        }
    }
}
