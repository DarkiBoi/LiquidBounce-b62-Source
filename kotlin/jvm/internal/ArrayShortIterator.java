// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.ShortIterator;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\n\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b" }, d2 = { "Lkotlin/jvm/internal/ArrayShortIterator;", "Lkotlin/collections/ShortIterator;", "array", "", "([S)V", "index", "", "hasNext", "", "nextShort", "", "kotlin-stdlib" })
final class ArrayShortIterator extends ShortIterator
{
    private int index;
    private final short[] array;
    
    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }
    
    @Override
    public short nextShort() {
        short n;
        try {
            final short[] array = this.array;
            final int index;
            this.index = (index = this.index) + 1;
            n = array[index];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            --this.index;
            throw new NoSuchElementException(e.getMessage());
        }
        return n;
    }
    
    public ArrayShortIterator(@NotNull final short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        this.array = array;
    }
}
