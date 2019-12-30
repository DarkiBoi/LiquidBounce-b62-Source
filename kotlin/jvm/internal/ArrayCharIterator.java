// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CharIterator;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b" }, d2 = { "Lkotlin/jvm/internal/ArrayCharIterator;", "Lkotlin/collections/CharIterator;", "array", "", "([C)V", "index", "", "hasNext", "", "nextChar", "", "kotlin-stdlib" })
final class ArrayCharIterator extends CharIterator
{
    private int index;
    private final char[] array;
    
    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }
    
    @Override
    public char nextChar() {
        char c;
        try {
            final char[] array = this.array;
            final int index;
            this.index = (index = this.index) + 1;
            c = array[index];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            --this.index;
            throw new NoSuchElementException(e.getMessage());
        }
        return c;
    }
    
    public ArrayCharIterator(@NotNull final char[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        this.array = array;
    }
}
