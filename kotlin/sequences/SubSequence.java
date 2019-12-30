// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011" }, d2 = { "Lkotlin/sequences/SubSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib" })
public final class SubSequence<T> implements Sequence<T>, DropTakeSequence<T>
{
    private final Sequence<T> sequence;
    private final int startIndex;
    private final int endIndex;
    
    private final int getCount() {
        return this.endIndex - this.startIndex;
    }
    
    @NotNull
    @Override
    public Sequence<T> drop(final int n) {
        return (n >= this.getCount()) ? SequencesKt__SequencesKt.emptySequence() : ((SubSequence)new SubSequence(this.sequence, this.startIndex + n, this.endIndex));
    }
    
    @NotNull
    @Override
    public Sequence<T> take(final int n) {
        return (n >= this.getCount()) ? this : new SubSequence(this.sequence, this.startIndex, this.startIndex + n);
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)new SubSequence$iterator.SubSequence$iterator$1(this);
    }
    
    public SubSequence(@NotNull final Sequence<? extends T> sequence, final int startIndex, final int endIndex) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        this.sequence = (Sequence<T>)sequence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        if (this.startIndex < 0) {
            throw new IllegalArgumentException(("startIndex should be non-negative, but is " + this.startIndex).toString());
        }
        if (this.endIndex < 0) {
            throw new IllegalArgumentException(("endIndex should be non-negative, but is " + this.endIndex).toString());
        }
        if (this.endIndex < this.startIndex) {
            throw new IllegalArgumentException(("endIndex should be not less than startIndex, but was " + this.endIndex + " < " + this.startIndex).toString());
        }
    }
}
