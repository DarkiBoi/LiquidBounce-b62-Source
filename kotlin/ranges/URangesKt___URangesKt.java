// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.UnsignedKt;
import kotlin.ULong;
import kotlin.UInt;
import java.util.NoSuchElementException;
import kotlin.random.URandomKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000N\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\n\u00f8\u0001\u0000¢\u0006\u0002\b\u0005\u001a\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00062\b\u0010\u0003\u001a\u0004\u0018\u00010\u0007H\u0087\n\u00f8\u0001\u0000¢\u0006\u0002\b\b\u001a\u001f\u0010\t\u001a\u00020\n*\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a\u001f\u0010\t\u001a\u00020\n*\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a\u001f\u0010\t\u001a\u00020\u0011*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a\u001f\u0010\t\u001a\u00020\n*\u00020\u00142\u0006\u0010\f\u001a\u00020\u0014H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a\u0015\u0010\u0017\u001a\u00020\u0004*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0018\u001a\u001c\u0010\u0017\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0019H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001a\u001a\u0015\u0010\u0017\u001a\u00020\u0007*\u00020\u0006H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001b\u001a\u001c\u0010\u0017\u001a\u00020\u0007*\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0019H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a\f\u0010\u001d\u001a\u00020\n*\u00020\nH\u0007\u001a\f\u0010\u001d\u001a\u00020\u0011*\u00020\u0011H\u0007\u001a\u0015\u0010\u001e\u001a\u00020\n*\u00020\n2\u0006\u0010\u001e\u001a\u00020\u001fH\u0087\u0004\u001a\u0015\u0010\u001e\u001a\u00020\u0011*\u00020\u00112\u0006\u0010\u001e\u001a\u00020 H\u0087\u0004\u001a\u001f\u0010!\u001a\u00020\u0002*\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\"\u0010#\u001a\u001f\u0010!\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b$\u0010%\u001a\u001f\u0010!\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b&\u0010'\u001a\u001f\u0010!\u001a\u00020\u0002*\u00020\u00142\u0006\u0010\f\u001a\u00020\u0014H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b(\u0010)\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006*" }, d2 = { "contains", "", "Lkotlin/ranges/UIntRange;", "element", "Lkotlin/UInt;", "contains-biwQdVI", "Lkotlin/ranges/ULongRange;", "Lkotlin/ULong;", "contains-GYNo2lE", "downTo", "Lkotlin/ranges/UIntProgression;", "Lkotlin/UByte;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "Lkotlin/UShort;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "reversed", "step", "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib" }, xs = "kotlin/ranges/URangesKt")
class URangesKt___URangesKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int random(@NotNull final UIntRange $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long random(@NotNull final ULongRange $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int random(@NotNull final UIntRange $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextUInt(random, $receiver);
        }
        catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long random(@NotNull final ULongRange $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextULong(random, $receiver);
        }
        catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final boolean contains-biwQdVI(@NotNull final UIntRange $receiver, final UInt element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return element != null && $receiver.contains-WZ4Q5Ns(element.unbox-impl());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final boolean contains-GYNo2lE(@NotNull final ULongRange $receiver, final ULong element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return element != null && $receiver.contains-VKZWuLQ(element.unbox-impl());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntProgression downTo-Kr8caGY(final byte $receiver, final byte to) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(UInt.constructor-impl($receiver & 0xFF), UInt.constructor-impl(to & 0xFF), -1);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntProgression downTo-J1ME1BU(final int $receiver, final int to) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs($receiver, to, -1);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULongProgression downTo-eb3DHEI(final long $receiver, final long to) {
        return ULongProgression.Companion.fromClosedRange-7ftBX0g($receiver, to, -1L);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntProgression downTo-5PvTz6A(final short $receiver, final short to) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(UInt.constructor-impl($receiver & 0xFFFF), UInt.constructor-impl(to & 0xFFFF), -1);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntProgression reversed(@NotNull final UIntProgression $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs($receiver.getLast(), $receiver.getFirst(), -$receiver.getStep());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULongProgression reversed(@NotNull final ULongProgression $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ULongProgression.Companion.fromClosedRange-7ftBX0g($receiver.getLast(), $receiver.getFirst(), -$receiver.getStep());
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntProgression step(@NotNull final UIntProgression $receiver, final int step) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        RangesKt__RangesKt.checkStepIsPositive(step > 0, step);
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs($receiver.getFirst(), $receiver.getLast(), ($receiver.getStep() > 0) ? step : (-step));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULongProgression step(@NotNull final ULongProgression $receiver, final long step) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        RangesKt__RangesKt.checkStepIsPositive(step > 0L, step);
        return ULongProgression.Companion.fromClosedRange-7ftBX0g($receiver.getFirst(), $receiver.getLast(), ($receiver.getStep() > 0L) ? step : (-step));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntRange until-Kr8caGY(final byte $receiver, final byte to) {
        return new UIntRange(UInt.constructor-impl($receiver & 0xFF), UInt.constructor-impl(UInt.constructor-impl(to & 0xFF) - 1), null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntRange until-J1ME1BU(final int $receiver, final int to) {
        if (UnsignedKt.uintCompare(to, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange($receiver, UInt.constructor-impl(to - 1), null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULongRange until-eb3DHEI(final long $receiver, final long to) {
        if (UnsignedKt.ulongCompare(to, 0L) <= 0) {
            return ULongRange.Companion.getEMPTY();
        }
        return new ULongRange($receiver, ULong.constructor-impl(to - ULong.constructor-impl((long)1 & 0xFFFFFFFFL)), null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UIntRange until-5PvTz6A(final short $receiver, final short to) {
        return new UIntRange(UInt.constructor-impl($receiver & 0xFFFF), UInt.constructor-impl(UInt.constructor-impl(to & 0xFFFF) - 1), null);
    }
    
    public URangesKt___URangesKt() {
    }
}
