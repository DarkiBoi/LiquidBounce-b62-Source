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
import kotlin.collections.UByteIterator;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001-B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003J\u001b\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\t\u0010\u001e\u001a\u00020\u0004H\u00d6\u0001J\u000f\u0010\u001f\u001a\u00020\u000fH\u0016¢\u0006\u0004\b \u0010!J\u0010\u0010\"\u001a\u00020#H\u0096\u0002¢\u0006\u0004\b$\u0010%J#\u0010&\u001a\u00020'2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000¢\u0006\u0004\b)\u0010*J\t\u0010+\u001a\u00020,H\u00d6\u0001R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006." }, d2 = { "Lkotlin/UByteArray;", "", "Lkotlin/UByte;", "size", "", "constructor-impl", "(I)[B", "storage", "", "([B)[B", "getSize-impl", "([B)I", "storage$annotations", "()V", "contains", "", "element", "contains-7apg3OU", "([BB)Z", "containsAll", "elements", "containsAll-impl", "([BLjava/util/Collection;)Z", "equals", "other", "", "get", "index", "get-impl", "([BI)B", "hashCode", "isEmpty", "isEmpty-impl", "([B)Z", "iterator", "Lkotlin/collections/UByteIterator;", "iterator-impl", "([B)Lkotlin/collections/UByteIterator;", "set", "", "value", "set-VurrAj0", "([BIB)V", "toString", "", "Iterator", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public final class UByteArray implements Collection<UByte>, KMappedMarker
{
    @NotNull
    private final byte[] storage = storage;
    
    public int getSize() {
        return getSize-impl(this.storage);
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @NotNull
    @Override
    public UByteIterator iterator() {
        return iterator-impl(this.storage);
    }
    
    public boolean contains-7apg3OU(final byte element) {
        return contains-7apg3OU(this.storage, element);
    }
    
    @Override
    public final /* bridge */ boolean contains(final Object o) {
        return o instanceof UByte && this.contains-7apg3OU(((UByte)o).unbox-impl());
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection<?> elements) {
        return containsAll-impl(this.storage, (Collection<UByte>)elements);
    }
    
    @Override
    public boolean isEmpty() {
        return isEmpty-impl(this.storage);
    }
    
    public static final byte get-impl(final byte[] $this, final int index) {
        return UByte.constructor-impl($this[index]);
    }
    
    public static final void set-VurrAj0(final byte[] $this, final int index, final byte value) {
        $this[index] = value;
    }
    
    public static int getSize-impl(final byte[] $this) {
        return $this.length;
    }
    
    @NotNull
    public static UByteIterator iterator-impl(final byte[] $this) {
        return new Iterator($this);
    }
    
    public static boolean contains-7apg3OU(final byte[] $this, final byte element) {
        return ArraysKt___ArraysKt.contains($this, element);
    }
    
    public static boolean containsAll-impl(final byte[] $this, @NotNull final Collection<UByte> elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final Iterable $receiver$iv = elements;
        boolean b;
        if (((Collection)$receiver$iv).isEmpty()) {
            b = true;
        }
        else {
            for (final Object element$iv : $receiver$iv) {
                final byte it = ((UByte)element$iv).unbox-impl();
                if (!ArraysKt___ArraysKt.contains($this, it)) {
                    b = false;
                    return b;
                }
            }
            b = true;
        }
        return b;
    }
    
    public static boolean isEmpty-impl(final byte[] $this) {
        return $this.length == 0;
    }
    
    @PublishedApi
    @NotNull
    public static byte[] constructor-impl(@NotNull final byte[] storage) {
        Intrinsics.checkParameterIsNotNull(storage, "storage");
        return storage;
    }
    
    @NotNull
    public static byte[] constructor-impl(final int size) {
        return constructor-impl(new byte[size]);
    }
    
    @NotNull
    public static String toString-impl(final byte[] a) {
        return "UByteArray(storage=" + Arrays.toString(a) + ")";
    }
    
    public static int hashCode-impl(final byte[] a) {
        return (a != null) ? Arrays.hashCode(a) : 0;
    }
    
    public static boolean equals-impl(final byte[] first, @Nullable final Object o) {
        return o instanceof UByteArray && Intrinsics.areEqual(first, ((UByteArray)o).unbox-impl());
    }
    
    public static final boolean equals-impl0(@NotNull final byte[] p1, @NotNull final byte[] p2) {
        Intrinsics.checkParameterIsNotNull(p1, "p1");
        Intrinsics.checkParameterIsNotNull(p2, "p2");
        throw null;
    }
    
    @NotNull
    public final /* synthetic */ byte[] unbox-impl() {
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
    
    public boolean add-7apg3OU(final byte b) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection<? extends UByte> collection) {
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
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0010\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f" }, d2 = { "Lkotlin/UByteArray$Iterator;", "Lkotlin/collections/UByteIterator;", "array", "", "([B)V", "index", "", "hasNext", "", "nextUByte", "Lkotlin/UByte;", "()B", "kotlin-stdlib" })
    private static final class Iterator extends UByteIterator
    {
        private int index;
        private final byte[] array;
        
        @Override
        public boolean hasNext() {
            return this.index < this.array.length;
        }
        
        @Override
        public byte nextUByte() {
            if (this.index < this.array.length) {
                final byte[] array = this.array;
                final int index;
                this.index = (index = this.index) + 1;
                return UByte.constructor-impl(array[index]);
            }
            throw new NoSuchElementException(String.valueOf(this.index));
        }
        
        public Iterator(@NotNull final byte[] array) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            this.array = array;
        }
    }
}
