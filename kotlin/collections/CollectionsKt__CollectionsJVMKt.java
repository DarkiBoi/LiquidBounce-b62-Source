// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import kotlin.internal.PlatformImplementationsKt;
import java.util.Arrays;
import kotlin.TypeCastException;
import kotlin.jvm.internal.CollectionToArray;
import java.util.Collection;
import kotlin.internal.InlineOnly;
import java.util.ArrayList;
import java.util.Enumeration;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import java.util.Collections;
import java.util.List;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u00002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0081\b\u001a\u0011\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0081\b\u001a\"\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0081\b¢\u0006\u0002\u0010\n\u001a4\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0006\"\u0004\b\u0000\u0010\u000b2\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\t2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0006H\u0081\b¢\u0006\u0002\u0010\r\u001a\u001f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u000f\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0010\u001a\u0002H\u000b¢\u0006\u0002\u0010\u0011\u001a1\u0010\u0012\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00070\u0006\"\u0004\b\u0000\u0010\u000b*\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u00062\u0006\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\u0010\u0015\u001a\u001f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u000f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u0017H\u0087\b¨\u0006\u0018" }, d2 = { "checkCountOverflow", "", "count", "checkIndexOverflow", "index", "copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "listOf", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib" }, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsJVMKt
{
    @NotNull
    public static final <T> List<T> listOf(final T element) {
        final List<T> singletonList = Collections.singletonList(element);
        Intrinsics.checkExpressionValueIsNotNull(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }
    
    @InlineOnly
    private static final <T> List<T> toList(@NotNull final Enumeration<T> $receiver) {
        final ArrayList<T> list = Collections.list($receiver);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.list(this)");
        return list;
    }
    
    @InlineOnly
    private static final Object[] copyToArrayImpl(final Collection<?> collection) {
        return CollectionToArray.toArray(collection);
    }
    
    @InlineOnly
    private static final <T> T[] copyToArrayImpl(final Collection<?> collection, final T[] array) {
        if (array == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        final Object[] array2 = CollectionToArray.toArray(collection, array);
        if (array2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (T[])array2;
    }
    
    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull final T[] $receiver, final boolean isVarargs) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Object[] copy;
        if (isVarargs && Intrinsics.areEqual($receiver.getClass(), Object[].class)) {
            copy = $receiver;
        }
        else {
            Intrinsics.checkExpressionValueIsNotNull(copy = Arrays.copyOf($receiver, $receiver.length, (Class<? extends Object[]>)Object[].class), "java.util.Arrays.copyOf(\u2026 Array<Any?>::class.java)");
        }
        return copy;
    }
    
    @PublishedApi
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final int checkIndexOverflow(final int index) {
        if (index < 0) {
            if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                throw new ArithmeticException("Index overflow has happened.");
            }
            CollectionsKt.throwIndexOverflow();
        }
        return index;
    }
    
    @PublishedApi
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final int checkCountOverflow(final int count) {
        if (count < 0) {
            if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                throw new ArithmeticException("Count overflow has happened.");
            }
            CollectionsKt.throwCountOverflow();
        }
        return count;
    }
    
    public CollectionsKt__CollectionsJVMKt() {
    }
}
