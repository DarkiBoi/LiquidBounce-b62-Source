// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import java.util.ListIterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.List;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010*\n\u0002\b\b\b'\u0018\u0000 \u001c*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0004\u001c\u001d\u001e\u001fB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0096\u0002J\u0016\u0010\r\u001a\u00028\u00002\u0006\u0010\u000e\u001a\u00020\u0006H¦\u0002¢\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0006H\u0016J\u0015\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0096\u0002J\u0015\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0018H\u0016J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00182\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u0006H\u0016R\u0012\u0010\u0005\u001a\u00020\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006 " }, d2 = { "Lkotlin/collections/AbstractList;", "E", "Lkotlin/collections/AbstractCollection;", "", "()V", "size", "", "getSize", "()I", "equals", "", "other", "", "get", "index", "(I)Ljava/lang/Object;", "hashCode", "indexOf", "element", "(Ljava/lang/Object;)I", "iterator", "", "lastIndexOf", "listIterator", "", "subList", "fromIndex", "toIndex", "Companion", "IteratorImpl", "ListIteratorImpl", "SubList", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E>, KMappedMarker
{
    public static final Companion Companion;
    
    @Override
    public abstract int getSize();
    
    @Override
    public abstract E get(final int p0);
    
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl();
    }
    
    @Override
    public int indexOf(final Object element) {
        final List $receiver$iv = this;
        int index$iv = 0;
        for (final Object it : $receiver$iv) {
            final Object item$iv = it;
            if (Intrinsics.areEqual(it, element)) {
                return index$iv;
            }
            ++index$iv;
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object element) {
        final List $receiver$iv = this;
        final ListIterator iterator$iv = $receiver$iv.listIterator($receiver$iv.size());
        while (iterator$iv.hasPrevious()) {
            final Object it = iterator$iv.previous();
            if (Intrinsics.areEqual(it, element)) {
                return iterator$iv.nextIndex();
            }
        }
        return -1;
    }
    
    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }
    
    @NotNull
    @Override
    public ListIterator<E> listIterator(final int index) {
        return new ListIteratorImpl(index);
    }
    
    @NotNull
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return new SubList<E>((AbstractList<? extends E>)this, fromIndex, toIndex);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other == this || (other instanceof List && AbstractList.Companion.orderedEquals$kotlin_stdlib(this, (Collection<?>)other));
    }
    
    @Override
    public int hashCode() {
        return AbstractList.Companion.orderedHashCode$kotlin_stdlib(this);
    }
    
    protected AbstractList() {
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Override
    public void add(final int n, final E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final int n, final Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public E remove(final int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public E set(final int n, final E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B#\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0016\u0010\u000e\u001a\u00028\u00012\u0006\u0010\u000f\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u0010R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0011" }, d2 = { "Lkotlin/collections/AbstractList$SubList;", "E", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "list", "fromIndex", "", "toIndex", "(Lkotlin/collections/AbstractList;II)V", "_size", "size", "getSize", "()I", "get", "index", "(I)Ljava/lang/Object;", "kotlin-stdlib" })
    private static final class SubList<E> extends AbstractList<E> implements RandomAccess
    {
        private int _size;
        private final AbstractList<E> list;
        private final int fromIndex;
        
        @Override
        public E get(final int index) {
            AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this._size);
            return this.list.get(this.fromIndex + index);
        }
        
        @Override
        public int getSize() {
            return this._size;
        }
        
        public SubList(@NotNull final AbstractList<? extends E> list, final int fromIndex, final int toIndex) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = (AbstractList<E>)list;
            this.fromIndex = fromIndex;
            AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(this.fromIndex, toIndex, this.list.size());
            this._size = toIndex - this.fromIndex;
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0092\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\t\u001a\u00020\nH\u0096\u0002J\u000e\u0010\u000b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\fR\u001a\u0010\u0003\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\r" }, d2 = { "Lkotlin/collections/AbstractList$IteratorImpl;", "", "(Lkotlin/collections/AbstractList;)V", "index", "", "getIndex", "()I", "setIndex", "(I)V", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib" })
    private class IteratorImpl implements Iterator<E>, KMappedMarker
    {
        private int index;
        
        protected final int getIndex() {
            return this.index;
        }
        
        protected final void setIndex(final int <set-?>) {
            this.index = <set-?>;
        }
        
        @Override
        public boolean hasNext() {
            return this.index < AbstractList.this.size();
        }
        
        @Override
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final AbstractList this$0 = AbstractList.this;
            final int index;
            this.index = (index = this.index) + 1;
            return this$0.get(index);
        }
        
        public IteratorImpl() {
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010*\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0092\u0004\u0018\u00002\f0\u0001R\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0005H\u0016J\r\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\u0005H\u0016¨\u0006\r" }, d2 = { "Lkotlin/collections/AbstractList$ListIteratorImpl;", "Lkotlin/collections/AbstractList$IteratorImpl;", "Lkotlin/collections/AbstractList;", "", "index", "", "(Lkotlin/collections/AbstractList;I)V", "hasPrevious", "", "nextIndex", "previous", "()Ljava/lang/Object;", "previousIndex", "kotlin-stdlib" })
    private class ListIteratorImpl extends IteratorImpl implements ListIterator<E>, KMappedMarker
    {
        @Override
        public boolean hasPrevious() {
            return ((IteratorImpl)this).getIndex() > 0;
        }
        
        @Override
        public int nextIndex() {
            return ((IteratorImpl)this).getIndex();
        }
        
        @Override
        public E previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final AbstractList this$0 = AbstractList.this;
            ((IteratorImpl)this).setIndex(((IteratorImpl)this).getIndex() - 1);
            return this$0.get(((IteratorImpl)this).getIndex());
        }
        
        @Override
        public int previousIndex() {
            return ((IteratorImpl)this).getIndex() - 1;
        }
        
        public ListIteratorImpl(final int index) {
            AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, AbstractList.this.size());
            ((IteratorImpl)this).setIndex(index);
        }
        
        @Override
        public void add(final E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
        
        @Override
        public void set(final E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\bJ\u001d\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\nJ%\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\u000eJ%\u0010\u000f\u001a\u00020\u00102\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u00122\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0012H\u0000¢\u0006\u0002\b\u0014J\u0019\u0010\u0015\u001a\u00020\u00062\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0012H\u0000¢\u0006\u0002\b\u0016¨\u0006\u0017" }, d2 = { "Lkotlin/collections/AbstractList$Companion;", "", "()V", "checkElementIndex", "", "index", "", "size", "checkElementIndex$kotlin_stdlib", "checkPositionIndex", "checkPositionIndex$kotlin_stdlib", "checkRangeIndexes", "fromIndex", "toIndex", "checkRangeIndexes$kotlin_stdlib", "orderedEquals", "", "c", "", "other", "orderedEquals$kotlin_stdlib", "orderedHashCode", "orderedHashCode$kotlin_stdlib", "kotlin-stdlib" })
    public static final class Companion
    {
        public final void checkElementIndex$kotlin_stdlib(final int index, final int size) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
            }
        }
        
        public final void checkPositionIndex$kotlin_stdlib(final int index, final int size) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
            }
        }
        
        public final void checkRangeIndexes$kotlin_stdlib(final int fromIndex, final int toIndex, final int size) {
            if (fromIndex < 0 || toIndex > size) {
                throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
            }
            if (fromIndex > toIndex) {
                throw new IllegalArgumentException("fromIndex: " + fromIndex + " > toIndex: " + toIndex);
            }
        }
        
        public final int orderedHashCode$kotlin_stdlib(@NotNull final Collection<?> c) {
            Intrinsics.checkParameterIsNotNull(c, "c");
            int hashCode = 1;
            for (final Object e : c) {
                final int n = 31 * hashCode;
                final Object o = e;
                hashCode = n + ((o != null) ? o.hashCode() : 0);
            }
            return hashCode;
        }
        
        public final boolean orderedEquals$kotlin_stdlib(@NotNull final Collection<?> c, @NotNull final Collection<?> other) {
            Intrinsics.checkParameterIsNotNull(c, "c");
            Intrinsics.checkParameterIsNotNull(other, "other");
            if (c.size() != other.size()) {
                return false;
            }
            final Iterator otherIterator = other.iterator();
            for (final Object elem : c) {
                final Object elemOther = otherIterator.next();
                if (Intrinsics.areEqual(elem, elemOther) ^ true) {
                    return false;
                }
            }
            return true;
        }
        
        private Companion() {
        }
    }
}
