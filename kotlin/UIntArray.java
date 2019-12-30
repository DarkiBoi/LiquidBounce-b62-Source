// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import java.util.NoSuchElementException;
import kotlin.jvm.internal.CollectionToArray;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;
import kotlin.collections.UIntIterator;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001-B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003J\u001b\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\t\u0010\u001e\u001a\u00020\u0004H\u00d6\u0001J\u000f\u0010\u001f\u001a\u00020\u000fH\u0016¢\u0006\u0004\b \u0010!J\u0010\u0010\"\u001a\u00020#H\u0096\u0002¢\u0006\u0004\b$\u0010%J#\u0010&\u001a\u00020'2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000¢\u0006\u0004\b)\u0010*J\t\u0010+\u001a\u00020,H\u00d6\u0001R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006." }, d2 = { "Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "storage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "get", "index", "get-impl", "([II)I", "hashCode", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "Lkotlin/collections/UIntIterator;", "iterator-impl", "([I)Lkotlin/collections/UIntIterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "Iterator", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public final class UIntArray implements Collection<UInt>, KMappedMarker
{
    @NotNull
    private final int[] storage = storage;
    
    public int getSize() {
        return getSize-impl(this.storage);
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @NotNull
    @Override
    public UIntIterator iterator() {
        return iterator-impl(this.storage);
    }
    
    public boolean contains-WZ4Q5Ns(final int element) {
        return contains-WZ4Q5Ns(this.storage, element);
    }
    
    @Override
    public final /* bridge */ boolean contains(final Object o) {
        return o instanceof UInt && this.contains-WZ4Q5Ns(((UInt)o).unbox-impl());
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection<?> elements) {
        return containsAll-impl(this.storage, (Collection<UInt>)elements);
    }
    
    @Override
    public boolean isEmpty() {
        return isEmpty-impl(this.storage);
    }
    
    public static final int get-impl(final int[] $this, final int index) {
        return UInt.constructor-impl($this[index]);
    }
    
    public static final void set-VXSXFK8(final int[] $this, final int index, final int value) {
        $this[index] = value;
    }
    
    public static int getSize-impl(final int[] $this) {
        return $this.length;
    }
    
    @NotNull
    public static UIntIterator iterator-impl(final int[] $this) {
        return new Iterator($this);
    }
    
    public static boolean contains-WZ4Q5Ns(final int[] $this, final int element) {
        return ArraysKt___ArraysKt.contains($this, element);
    }
    
    public static boolean containsAll-impl(final int[] $this, @NotNull final Collection<UInt> elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final Iterable $receiver$iv = elements;
        boolean b;
        if (((Collection)$receiver$iv).isEmpty()) {
            b = true;
        }
        else {
            for (final Object element$iv : $receiver$iv) {
                final int it = ((UInt)element$iv).unbox-impl();
                if (!ArraysKt___ArraysKt.contains($this, it)) {
                    b = false;
                    return b;
                }
            }
            b = true;
        }
        return b;
    }
    
    public static boolean isEmpty-impl(final int[] $this) {
        return $this.length == 0;
    }
    
    @PublishedApi
    @NotNull
    public static int[] constructor-impl(@NotNull final int[] storage) {
        Intrinsics.checkParameterIsNotNull(storage, "storage");
        return storage;
    }
    
    @NotNull
    public static int[] constructor-impl(final int size) {
        return constructor-impl(new int[size]);
    }
    
    @NotNull
    public static String toString-impl(final int[] a) {
        return "UIntArray(storage=" + Arrays.toString(a) + ")";
    }
    
    public static int hashCode-impl(final int[] a) {
        return (a != null) ? Arrays.hashCode(a) : 0;
    }
    
    public static boolean equals-impl(final int[] first, @Nullable final Object o) {
        return o instanceof UIntArray && Intrinsics.areEqual(first, ((UIntArray)o).unbox-impl());
    }
    
    public static final boolean equals-impl0(@NotNull final int[] p1, @NotNull final int[] p2) {
        Intrinsics.checkParameterIsNotNull(p1, "p1");
        Intrinsics.checkParameterIsNotNull(p2, "p2");
        throw null;
    }
    
    @NotNull
    public final /* synthetic */ int[] unbox-impl() {
        return this.storage;
    }
    
    @Override
    public String toString() {
        return toString-impl(this.storage);
    }
    
    @Override
    public int hashCode() {
        return hashCode-impl(this.storage);
    }
    
    @Override
    public boolean equals(final Object o) {
        return equals-impl(this.storage, o);
    }
    
    public boolean add-WZ4Q5Ns(final int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return (T[])CollectionToArray.toArray(this, a);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0010\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f" }, d2 = { "Lkotlin/UIntArray$Iterator;", "Lkotlin/collections/UIntIterator;", "array", "", "([I)V", "index", "", "hasNext", "", "nextUInt", "Lkotlin/UInt;", "()I", "kotlin-stdlib" })
    private static final class Iterator extends UIntIterator
    {
        private int index;
        private final int[] array;
        
        @Override
        public boolean hasNext() {
            return this.index < this.array.length;
        }
        
        @Override
        public int nextUInt() {
            if (this.index < this.array.length) {
                final int[] array = this.array;
                final int index;
                this.index = (index = this.index) + 1;
                return UInt.constructor-impl(array[index]);
            }
            throw new NoSuchElementException(String.valueOf(this.index));
        }
        
        public Iterator(@NotNull final int[] array) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            this.array = array;
        }
    }
}
