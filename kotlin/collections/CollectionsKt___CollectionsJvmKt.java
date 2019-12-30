// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000>\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001aA\u0010\u0006\u001a\u0002H\u0007\"\u0010\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\t\u001a\u0002H\u00072\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\n\u001a\u0016\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u000e\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\r0\u0010\"\u000e\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u0011*\b\u0012\u0004\u0012\u0002H\r0\u0003\u001a8\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\r0\u0010\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u00032\u001a\u0010\u0012\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\r0\u0013j\n\u0012\u0006\b\u0000\u0012\u0002H\r`\u0014¨\u0006\u0015" }, d2 = { "filterIsInstance", "", "R", "", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Ljava/lang/Iterable;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "reverse", "", "T", "", "toSortedSet", "Ljava/util/SortedSet;", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "kotlin-stdlib" }, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt___CollectionsJvmKt extends CollectionsKt__ReversedViewsKt
{
    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull final Iterable<?> $receiver, @NotNull final Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        return filterIsInstanceTo($receiver, new ArrayList<R>(), klass);
    }
    
    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull final Iterable<?> $receiver, @NotNull final C destination, @NotNull final Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        for (final Object element : $receiver) {
            if (klass.isInstance(element)) {
                destination.add((Object)element);
            }
        }
        return destination;
    }
    
    public static final <T> void reverse(@NotNull final List<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Collections.reverse($receiver);
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull final Iterable<? extends T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.toCollection((Iterable<?>)$receiver, (TreeSet<T>)new TreeSet<T>());
    }
    
    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull final Iterable<? extends T> $receiver, @NotNull final Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return CollectionsKt___CollectionsKt.toCollection((Iterable<?>)$receiver, (TreeSet<T>)new TreeSet<T>(comparator));
    }
    
    public CollectionsKt___CollectionsJvmKt() {
    }
}
