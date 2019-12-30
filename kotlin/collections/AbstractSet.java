// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Set;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b'\u0018\u0000 \u000b*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u000bB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016¨\u0006\f" }, d2 = { "Lkotlin/collections/AbstractSet;", "E", "Lkotlin/collections/AbstractCollection;", "", "()V", "equals", "", "other", "", "hashCode", "", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E>, KMappedMarker
{
    public static final Companion Companion;
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other == this || (other instanceof Set && AbstractSet.Companion.setEquals$kotlin_stdlib(this, (Set<?>)other));
    }
    
    @Override
    public int hashCode() {
        return AbstractSet.Companion.unorderedHashCode$kotlin_stdlib(this);
    }
    
    protected AbstractSet() {
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0010\u001e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\bJ\u0019\u0010\t\u001a\u00020\n2\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0000¢\u0006\u0002\b\f¨\u0006\r" }, d2 = { "Lkotlin/collections/AbstractSet$Companion;", "", "()V", "setEquals", "", "c", "", "other", "setEquals$kotlin_stdlib", "unorderedHashCode", "", "", "unorderedHashCode$kotlin_stdlib", "kotlin-stdlib" })
    public static final class Companion
    {
        public final int unorderedHashCode$kotlin_stdlib(@NotNull final Collection<?> c) {
            Intrinsics.checkParameterIsNotNull(c, "c");
            int hashCode = 0;
            for (final Object element : c) {
                final int n = hashCode;
                final Object o = element;
                hashCode = n + ((o != null) ? o.hashCode() : 0);
            }
            return hashCode;
        }
        
        public final boolean setEquals$kotlin_stdlib(@NotNull final Set<?> c, @NotNull final Set<?> other) {
            Intrinsics.checkParameterIsNotNull(c, "c");
            Intrinsics.checkParameterIsNotNull(other, "other");
            return c.size() == other.size() && c.containsAll(other);
        }
        
        private Companion() {
        }
    }
}
