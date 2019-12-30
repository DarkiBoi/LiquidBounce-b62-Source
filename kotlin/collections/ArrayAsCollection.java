// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.ArrayIteratorKt;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00062\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0016J\b\u0010\u0015\u001a\u00020\u0006H\u0016J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u0017H\u0096\u0002J\u0015\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00190\u0004¢\u0006\u0002\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00028\u00000\u0004¢\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a" }, d2 = { "Lkotlin/collections/ArrayAsCollection;", "T", "", "values", "", "isVarargs", "", "([Ljava/lang/Object;Z)V", "()Z", "size", "", "getSize", "()I", "getValues", "()[Ljava/lang/Object;", "[Ljava/lang/Object;", "contains", "element", "(Ljava/lang/Object;)Z", "containsAll", "elements", "isEmpty", "iterator", "", "toArray", "", "kotlin-stdlib" })
final class ArrayAsCollection<T> implements Collection<T>, KMappedMarker
{
    @NotNull
    private final T[] values;
    private final boolean isVarargs;
    
    public int getSize() {
        return this.values.length;
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @Override
    public boolean isEmpty() {
        return this.values.length == 0;
    }
    
    @Override
    public boolean contains(final Object element) {
        return ArraysKt___ArraysKt.contains(this.values, element);
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection<?> elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final Iterable $receiver$iv = elements;
        boolean b;
        if (((Collection)$receiver$iv).isEmpty()) {
            b = true;
        }
        else {
            for (final Object it : $receiver$iv) {
                final Object element$iv = it;
                if (!this.contains(it)) {
                    b = false;
                    return b;
                }
            }
            b = true;
        }
        return b;
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return ArrayIteratorKt.iterator(this.values);
    }
    
    @NotNull
    @Override
    public final Object[] toArray() {
        return CollectionsKt__CollectionsJVMKt.copyToArrayOfAny(this.values, this.isVarargs);
    }
    
    @NotNull
    public final T[] getValues() {
        return this.values;
    }
    
    public final boolean isVarargs() {
        return this.isVarargs;
    }
    
    public ArrayAsCollection(@NotNull final T[] values, final boolean isVarargs) {
        Intrinsics.checkParameterIsNotNull(values, "values");
        this.values = values;
        this.isVarargs = isVarargs;
    }
    
    @Override
    public boolean add(final T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> collection) {
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
    public <T> T[] toArray(final T[] a) {
        return (T[])CollectionToArray.toArray(this, a);
    }
}
