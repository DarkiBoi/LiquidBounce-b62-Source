// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.JvmName;
import kotlin.PublishedApi;
import java.util.Arrays;
import kotlin.SinceKotlin;
import java.lang.reflect.Array;
import kotlin.TypeCastException;
import java.util.Collection;
import kotlin.internal.InlineOnly;
import java.nio.charset.Charset;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a/\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0000¢\u0006\u0002\u0010\u0006\u001a\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a!\u0010\n\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001H\u0001¢\u0006\u0004\b\u000b\u0010\f\u001a,\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0001H\u0086\b¢\u0006\u0002\u0010\u000e\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a&\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\b\u0012\u0004\u0012\u0002H\u00020\u0015H\u0086\b¢\u0006\u0002\u0010\u0016¨\u0006\u0017" }, d2 = { "arrayOfNulls", "", "T", "reference", "size", "", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRangeToIndexCheck", "", "toIndex", "contentDeepHashCodeImpl", "contentDeepHashCode", "([Ljava/lang/Object;)I", "orEmpty", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "", "charset", "Ljava/nio/charset/Charset;", "toTypedArray", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "kotlin-stdlib" }, xs = "kotlin/collections/ArraysKt")
class ArraysKt__ArraysJVMKt
{
    private static final <T> T[] orEmpty(@Nullable final T[] $receiver) {
        Object[] array = $receiver;
        if ($receiver == null) {
            final int n = 0;
            Intrinsics.reifiedOperationMarker(0, "T?");
            array = new Object[n];
        }
        return (T[])array;
    }
    
    @InlineOnly
    private static final String toString(@NotNull final byte[] $receiver, final Charset charset) {
        return new String($receiver, charset);
    }
    
    private static final <T> T[] toTypedArray(@NotNull final Collection<? extends T> $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
        }
        final Collection<? extends T> collection;
        final Collection thisCollection = collection = $receiver;
        final int n = 0;
        Intrinsics.reifiedOperationMarker(0, "T?");
        final T[] array = collection.toArray(new Object[n]);
        if (array == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return array;
    }
    
    @NotNull
    public static final <T> T[] arrayOfNulls(@NotNull final T[] reference, final int size) {
        Intrinsics.checkParameterIsNotNull(reference, "reference");
        final Object instance = Array.newInstance(reference.getClass().getComponentType(), size);
        if (instance == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (T[])instance;
    }
    
    @SinceKotlin(version = "1.3")
    public static final void copyOfRangeToIndexCheck(final int toIndex, final int size) {
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }
    
    @SinceKotlin(version = "1.3")
    @PublishedApi
    @JvmName(name = "contentDeepHashCode")
    public static final <T> int contentDeepHashCode(@NotNull final T[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return Arrays.deepHashCode($receiver);
    }
    
    public ArraysKt__ArraysJVMKt() {
    }
}
