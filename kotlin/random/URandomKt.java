// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import kotlin.UByteArray;
import kotlin.ranges.ULongRange;
import kotlin.ULong;
import kotlin.UnsignedKt;
import kotlin.ranges.UIntRange;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010(\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006)" }, d2 = { "checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib" })
public final class URandomKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt(@NotNull final Random $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return UInt.constructor-impl($receiver.nextInt());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt-qCasIEU(@NotNull final Random $receiver, final int until) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return nextUInt-a8DCA5k($receiver, 0, until);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt-a8DCA5k(@NotNull final Random $receiver, final int from, final int until) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        checkUIntRangeBounds-J1ME1BU(from, until);
        final int signedFrom = from ^ Integer.MIN_VALUE;
        final int signedUntil = until ^ Integer.MIN_VALUE;
        final int signedResult = $receiver.nextInt(signedFrom, signedUntil) ^ Integer.MIN_VALUE;
        return UInt.constructor-impl(signedResult);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt(@NotNull final Random $receiver, @NotNull final UIntRange range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return (UnsignedKt.uintCompare(range.getLast(), -1) < 0) ? nextUInt-a8DCA5k($receiver, range.getFirst(), UInt.constructor-impl(range.getLast() + 1)) : ((UnsignedKt.uintCompare(range.getFirst(), 0) > 0) ? UInt.constructor-impl(nextUInt-a8DCA5k($receiver, UInt.constructor-impl(range.getFirst() - 1), range.getLast()) + 1) : nextUInt($receiver));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong(@NotNull final Random $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ULong.constructor-impl($receiver.nextLong());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong-V1Xi4fY(@NotNull final Random $receiver, final long until) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return nextULong-jmpaW-c($receiver, 0L, until);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong-jmpaW-c(@NotNull final Random $receiver, final long from, final long until) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        checkULongRangeBounds-eb3DHEI(from, until);
        final long signedFrom = from ^ Long.MIN_VALUE;
        final long signedUntil = until ^ Long.MIN_VALUE;
        final long signedResult = $receiver.nextLong(signedFrom, signedUntil) ^ Long.MIN_VALUE;
        return ULong.constructor-impl(signedResult);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong(@NotNull final Random $receiver, @NotNull final ULongRange range) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return (UnsignedKt.ulongCompare(range.getLast(), -1L) < 0) ? nextULong-jmpaW-c($receiver, range.getFirst(), ULong.constructor-impl(range.getLast() + ULong.constructor-impl((long)1 & 0xFFFFFFFFL))) : ((UnsignedKt.ulongCompare(range.getFirst(), 0L) > 0) ? ULong.constructor-impl(nextULong-jmpaW-c($receiver, ULong.constructor-impl(range.getFirst() - ULong.constructor-impl((long)1 & 0xFFFFFFFFL)), range.getLast()) + ULong.constructor-impl((long)1 & 0xFFFFFFFFL)) : nextULong($receiver));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-EVgfTAA(@NotNull final Random $receiver, @NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "array");
        $receiver.nextBytes(array);
        return array;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes(@NotNull final Random $receiver, final int size) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return UByteArray.constructor-impl($receiver.nextBytes(size));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-Wvrt4B4(@NotNull final Random $receiver, @NotNull final byte[] array, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "array");
        $receiver.nextBytes(array, fromIndex, toIndex);
        return array;
    }
    
    @ExperimentalUnsignedTypes
    public static final void checkUIntRangeBounds-J1ME1BU(final int from, final int until) {
        if (UnsignedKt.uintCompare(until, from) <= 0) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(UInt.box-impl(from), UInt.box-impl(until)).toString());
        }
    }
    
    @ExperimentalUnsignedTypes
    public static final void checkULongRangeBounds-eb3DHEI(final long from, final long until) {
        if (UnsignedKt.ulongCompare(until, from) <= 0) {
            throw new IllegalArgumentException(RandomKt.boundsErrorMessage(ULong.box-impl(from), ULong.box-impl(until)).toString());
        }
    }
}
