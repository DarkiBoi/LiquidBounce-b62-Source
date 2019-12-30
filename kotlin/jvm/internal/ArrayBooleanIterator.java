// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.BooleanIterator;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0018\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\n" }, d2 = { "Lkotlin/jvm/internal/ArrayBooleanIterator;", "Lkotlin/collections/BooleanIterator;", "array", "", "([Z)V", "index", "", "hasNext", "", "nextBoolean", "kotlin-stdlib" })
final class ArrayBooleanIterator extends BooleanIterator
{
    private int index;
    private final boolean[] array;
    
    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }
    
    @Override
    public boolean nextBoolean() {
        boolean b;
        try {
            final boolean[] array = this.array;
            final int index;
            this.index = (index = this.index) + 1;
            b = array[index];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            --this.index;
            throw new NoSuchElementException(e.getMessage());
        }
        return b;
    }
    
    public ArrayBooleanIterator(@NotNull final boolean[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        this.array = array;
    }
}
