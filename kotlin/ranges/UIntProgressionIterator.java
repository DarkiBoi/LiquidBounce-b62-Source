// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.UnsignedKt;
import kotlin.UInt;
import java.util.NoSuchElementException;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.collections.UIntIterator;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0010\u0010\r\u001a\u00020\u0003H\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0003X\u0082\u000e\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f" }, d2 = { "Lkotlin/ranges/UIntProgressionIterator;", "Lkotlin/collections/UIntIterator;", "first", "Lkotlin/UInt;", "last", "step", "", "(IIILkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "I", "hasNext", "", "next", "nextUInt", "()I", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
final class UIntProgressionIterator extends UIntIterator
{
    private final int finalElement;
    private boolean hasNext;
    private final int step;
    private int next;
    
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public int nextUInt() {
        final int value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        }
        else {
            this.next = UInt.constructor-impl(this.next + this.step);
        }
        return value;
    }
    
    private UIntProgressionIterator(final int first, final int last, final int step) {
        this.finalElement = last;
        UIntProgressionIterator uIntProgressionIterator;
        boolean hasNext;
        if (step > 0) {
            final int uintCompare = UnsignedKt.uintCompare(first, last);
            uIntProgressionIterator = this;
            hasNext = (uintCompare <= 0);
        }
        else {
            final int uintCompare2 = UnsignedKt.uintCompare(first, last);
            uIntProgressionIterator = this;
            hasNext = (uintCompare2 >= 0);
        }
        uIntProgressionIterator.hasNext = hasNext;
        this.step = UInt.constructor-impl(step);
        this.next = (this.hasNext ? first : this.finalElement);
    }
}
