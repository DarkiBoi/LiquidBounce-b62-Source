// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.UShort;
import kotlin.UByte;
import kotlin.ULong;
import kotlin.UInt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import java.util.Arrays;
import kotlin.UShortArray;
import kotlin.UByteArray;
import kotlin.ULongArray;
import java.util.NoSuchElementException;
import kotlin.UIntArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000z\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b/\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0011\n\u0002\b\r\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u0017\u0010\u0005\u001a\u00020\u0006*\u00020\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u0017\u0010\n\u001a\u00020\u000b*\u00020\fH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a\u0017\u0010\u000f\u001a\u00020\u0010*\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a\u0015\u0010\u0014\u001a\u00020\u0002*\u00020\u0001H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0015\u0010\u0015\u001a\u00020\u0007*\u00020\u0006H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\t\u001a\u0015\u0010\u0016\u001a\u00020\f*\u00020\u000bH\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a\u0015\u0010\u0017\u001a\u00020\u0011*\u00020\u0010H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a\u001f\u0010\u0018\u001a\u00020\u0019*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a\u001f\u0010\u0018\u001a\u00020\u0019*\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u0018\u001a\u00020\u0019*\u00020\f2\u0006\u0010\u001a\u001a\u00020\fH\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b\u001f\u0010 \u001a\u001f\u0010\u0018\u001a\u00020\u0019*\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u0011H\u0087\u0004\u00f8\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u0016\u0010#\u001a\u00020$*\u00020\u0002H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u0016\u0010#\u001a\u00020$*\u00020\u0007H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a\u0016\u0010#\u001a\u00020$*\u00020\fH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b)\u0010*\u001a\u0016\u0010#\u001a\u00020$*\u00020\u0011H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b+\u0010,\u001a\u0016\u0010-\u001a\u00020.*\u00020\u0002H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b/\u00100\u001a\u0016\u0010-\u001a\u00020.*\u00020\u0007H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b1\u00102\u001a\u0016\u0010-\u001a\u00020.*\u00020\fH\u0007\u00f8\u0001\u0000¢\u0006\u0004\b3\u00104\u001a\u0016\u0010-\u001a\u00020.*\u00020\u0011H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b5\u00106\u001a=\u00107\u001a\u00020\u0002*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\b\b\u0002\u00109\u001a\u00020$2\b\b\u0002\u0010:\u001a\u00020$2\b\b\u0002\u0010;\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b<\u0010=\u001a=\u00107\u001a\u00020\u0007*\u00020\u00072\u0006\u00108\u001a\u00020\u00072\b\b\u0002\u00109\u001a\u00020$2\b\b\u0002\u0010:\u001a\u00020$2\b\b\u0002\u0010;\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b>\u0010?\u001a=\u00107\u001a\u00020\f*\u00020\f2\u0006\u00108\u001a\u00020\f2\b\b\u0002\u00109\u001a\u00020$2\b\b\u0002\u0010:\u001a\u00020$2\b\b\u0002\u0010;\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b@\u0010A\u001a=\u00107\u001a\u00020\u0011*\u00020\u00112\u0006\u00108\u001a\u00020\u00112\b\b\u0002\u00109\u001a\u00020$2\b\b\u0002\u0010:\u001a\u00020$2\b\b\u0002\u0010;\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bB\u0010C\u001a\u0017\u0010D\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bE\u0010\u0004\u001a\u001f\u0010D\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bG\u0010H\u001a\u0017\u0010D\u001a\u00020\u0007*\u00020\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bI\u0010\t\u001a\u001f\u0010D\u001a\u00020\u0007*\u00020\u00072\u0006\u0010F\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bJ\u0010K\u001a\u0017\u0010D\u001a\u00020\f*\u00020\fH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bL\u0010\u000e\u001a\u001f\u0010D\u001a\u00020\f*\u00020\f2\u0006\u0010F\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bM\u0010N\u001a\u0017\u0010D\u001a\u00020\u0011*\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bO\u0010\u0013\u001a\u001f\u0010D\u001a\u00020\u0011*\u00020\u00112\u0006\u0010F\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bP\u0010Q\u001a'\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010S\u001a\u00020$2\u0006\u0010T\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bU\u0010V\u001a'\u0010R\u001a\u00020\u0007*\u00020\u00072\u0006\u0010S\u001a\u00020$2\u0006\u0010T\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bW\u0010X\u001a'\u0010R\u001a\u00020\f*\u00020\f2\u0006\u0010S\u001a\u00020$2\u0006\u0010T\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bY\u0010Z\u001a'\u0010R\u001a\u00020\u0011*\u00020\u00112\u0006\u0010S\u001a\u00020$2\u0006\u0010T\u001a\u00020$H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b[\u0010\\\u001a\u0017\u0010]\u001a\u00020^*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b_\u0010`\u001a\u001e\u0010]\u001a\u00020^*\u00020\u00022\u0006\u0010]\u001a\u00020aH\u0007\u00f8\u0001\u0000¢\u0006\u0004\bb\u0010c\u001a\u0017\u0010]\u001a\u00020d*\u00020\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\be\u0010(\u001a\u001e\u0010]\u001a\u00020d*\u00020\u00072\u0006\u0010]\u001a\u00020aH\u0007\u00f8\u0001\u0000¢\u0006\u0004\bf\u0010g\u001a\u0017\u0010]\u001a\u00020h*\u00020\fH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bi\u0010j\u001a\u001e\u0010]\u001a\u00020h*\u00020\f2\u0006\u0010]\u001a\u00020aH\u0007\u00f8\u0001\u0000¢\u0006\u0004\bk\u0010l\u001a\u0017\u0010]\u001a\u00020m*\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bn\u0010o\u001a\u001e\u0010]\u001a\u00020m*\u00020\u00112\u0006\u0010]\u001a\u00020aH\u0007\u00f8\u0001\u0000¢\u0006\u0004\bp\u0010q\u001a\u0017\u0010r\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bs\u0010\u0004\u001a\u0017\u0010t\u001a\u00020\u0006*\u00020\u0007H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bu\u0010\t\u001a\u0017\u0010v\u001a\u00020\u000b*\u00020\fH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bw\u0010\u000e\u001a\u0017\u0010x\u001a\u00020\u0010*\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\by\u0010\u0013\u001a\u001c\u0010z\u001a\b\u0012\u0004\u0012\u00020^0{*\u00020\u0002H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b|\u0010}\u001a\u001c\u0010z\u001a\b\u0012\u0004\u0012\u00020d0{*\u00020\u0007H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b~\u0010\u007f\u001a\u001e\u0010z\u001a\b\u0012\u0004\u0012\u00020h0{*\u00020\fH\u0007\u00f8\u0001\u0000¢\u0006\u0006\b\u0080\u0001\u0010\u0081\u0001\u001a\u001e\u0010z\u001a\b\u0012\u0004\u0012\u00020m0{*\u00020\u0011H\u0007\u00f8\u0001\u0000¢\u0006\u0006\b\u0082\u0001\u0010\u0083\u0001\u001a\u0016\u0010\u0084\u0001\u001a\u00020\u0002*\u00020\u0001H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0016\u0010\u0085\u0001\u001a\u00020\u0007*\u00020\u0006H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\t\u001a\u0016\u0010\u0086\u0001\u001a\u00020\f*\u00020\u000bH\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a\u0016\u0010\u0087\u0001\u001a\u00020\u0011*\u00020\u0010H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0088\u0001" }, d2 = { "asByteArray", "", "Lkotlin/UByteArray;", "asByteArray-GBYM_sE", "([B)[B", "asIntArray", "", "Lkotlin/UIntArray;", "asIntArray--ajY-9A", "([I)[I", "asLongArray", "", "Lkotlin/ULongArray;", "asLongArray-QwZRm1k", "([J)[J", "asShortArray", "", "Lkotlin/UShortArray;", "asShortArray-rL5Bavg", "([S)[S", "asUByteArray", "asUIntArray", "asULongArray", "asUShortArray", "contentEquals", "", "other", "contentEquals-kdPth3s", "([B[B)Z", "contentEquals-ctEhBpI", "([I[I)Z", "contentEquals-us8wMrg", "([J[J)Z", "contentEquals-mazbYpA", "([S[S)Z", "contentHashCode", "", "contentHashCode-GBYM_sE", "([B)I", "contentHashCode--ajY-9A", "([I)I", "contentHashCode-QwZRm1k", "([J)I", "contentHashCode-rL5Bavg", "([S)I", "contentToString", "", "contentToString-GBYM_sE", "([B)Ljava/lang/String;", "contentToString--ajY-9A", "([I)Ljava/lang/String;", "contentToString-QwZRm1k", "([J)Ljava/lang/String;", "contentToString-rL5Bavg", "([S)Ljava/lang/String;", "copyInto", "destination", "destinationOffset", "startIndex", "endIndex", "copyInto-FUQE5sA", "([B[BIII)[B", "copyInto-sIZ3KeM", "([I[IIII)[I", "copyInto--B0-L2c", "([J[JIII)[J", "copyInto-9-ak10g", "([S[SIII)[S", "copyOf", "copyOf-GBYM_sE", "newSize", "copyOf-PpDY95g", "([BI)[B", "copyOf--ajY-9A", "copyOf-qFRl0hI", "([II)[I", "copyOf-QwZRm1k", "copyOf-r7IrZao", "([JI)[J", "copyOf-rL5Bavg", "copyOf-nggk6HY", "([SI)[S", "copyOfRange", "fromIndex", "toIndex", "copyOfRange-4UcCI2c", "([BII)[B", "copyOfRange-oBK06Vg", "([III)[I", "copyOfRange--nroSd4", "([JII)[J", "copyOfRange-Aa5vz7o", "([SII)[S", "random", "Lkotlin/UByte;", "random-GBYM_sE", "([B)B", "Lkotlin/random/Random;", "random-oSF2wD8", "([BLkotlin/random/Random;)B", "Lkotlin/UInt;", "random--ajY-9A", "random-2D5oskM", "([ILkotlin/random/Random;)I", "Lkotlin/ULong;", "random-QwZRm1k", "([J)J", "random-JzugnMA", "([JLkotlin/random/Random;)J", "Lkotlin/UShort;", "random-rL5Bavg", "([S)S", "random-s5X_as8", "([SLkotlin/random/Random;)S", "toByteArray", "toByteArray-GBYM_sE", "toIntArray", "toIntArray--ajY-9A", "toLongArray", "toLongArray-QwZRm1k", "toShortArray", "toShortArray-rL5Bavg", "toTypedArray", "", "toTypedArray-GBYM_sE", "([B)[Lkotlin/UByte;", "toTypedArray--ajY-9A", "([I)[Lkotlin/UInt;", "toTypedArray-QwZRm1k", "([J)[Lkotlin/ULong;", "toTypedArray-rL5Bavg", "([S)[Lkotlin/UShort;", "toUByteArray", "toUIntArray", "toULongArray", "toUShortArray", "kotlin-stdlib" }, xs = "kotlin/collections/UArraysKt")
class UArraysKt___UArraysKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int random--ajY-9A(@NotNull final int[] $receiver) {
        return random-2D5oskM($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long random-QwZRm1k(@NotNull final long[] $receiver) {
        return random-JzugnMA($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte random-GBYM_sE(@NotNull final byte[] $receiver) {
        return random-oSF2wD8($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short random-rL5Bavg(@NotNull final short[] $receiver) {
        return random-s5X_as8($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int random-2D5oskM(@NotNull final int[] $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UIntArray.isEmpty-impl($receiver)) {
            throw new NoSuchElementException("Array is empty.");
        }
        return UIntArray.get-impl($receiver, random.nextInt(UIntArray.getSize-impl($receiver)));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long random-JzugnMA(@NotNull final long[] $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (ULongArray.isEmpty-impl($receiver)) {
            throw new NoSuchElementException("Array is empty.");
        }
        return ULongArray.get-impl($receiver, random.nextInt(ULongArray.getSize-impl($receiver)));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final byte random-oSF2wD8(@NotNull final byte[] $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UByteArray.isEmpty-impl($receiver)) {
            throw new NoSuchElementException("Array is empty.");
        }
        return UByteArray.get-impl($receiver, random.nextInt(UByteArray.getSize-impl($receiver)));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final short random-s5X_as8(@NotNull final short[] $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (UShortArray.isEmpty-impl($receiver)) {
            throw new NoSuchElementException("Array is empty.");
        }
        return UShortArray.get-impl($receiver, random.nextInt(UShortArray.getSize-impl($receiver)));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] asByteArray-GBYM_sE(@NotNull final byte[] $receiver) {
        return $receiver;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] asIntArray--ajY-9A(@NotNull final int[] $receiver) {
        return $receiver;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] asLongArray-QwZRm1k(@NotNull final long[] $receiver) {
        return $receiver;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] asShortArray-rL5Bavg(@NotNull final short[] $receiver) {
        return $receiver;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] asUByteArray(@NotNull final byte[] $receiver) {
        return UByteArray.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] asUIntArray(@NotNull final int[] $receiver) {
        return UIntArray.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] asULongArray(@NotNull final long[] $receiver) {
        return ULongArray.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] asUShortArray(@NotNull final short[] $receiver) {
        return UShortArray.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-ctEhBpI(@NotNull final int[] $receiver, @NotNull final int[] other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($receiver, other);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-us8wMrg(@NotNull final long[] $receiver, @NotNull final long[] other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($receiver, other);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-kdPth3s(@NotNull final byte[] $receiver, @NotNull final byte[] other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($receiver, other);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final boolean contentEquals-mazbYpA(@NotNull final short[] $receiver, @NotNull final short[] other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($receiver, other);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int contentHashCode--ajY-9A(@NotNull final int[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return Arrays.hashCode($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-QwZRm1k(@NotNull final long[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return Arrays.hashCode($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-GBYM_sE(@NotNull final byte[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return Arrays.hashCode($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int contentHashCode-rL5Bavg(@NotNull final short[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return Arrays.hashCode($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString--ajY-9A(@NotNull final int[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.joinToString$default(UIntArray.box-impl($receiver), ", ", "[", "]", 0, null, null, 56, null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-QwZRm1k(@NotNull final long[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.joinToString$default(ULongArray.box-impl($receiver), ", ", "[", "]", 0, null, null, 56, null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-GBYM_sE(@NotNull final byte[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.joinToString$default(UByteArray.box-impl($receiver), ", ", "[", "]", 0, null, null, 56, null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String contentToString-rL5Bavg(@NotNull final short[] $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.joinToString$default(UShortArray.box-impl($receiver), ", ", "[", "]", 0, null, null, 56, null);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] copyInto-sIZ3KeM(@NotNull final int[] $receiver, final int[] destination, final int destinationOffset, final int startIndex, final int endIndex) {
        ArraysKt___ArraysJvmKt.copyInto($receiver, destination, destinationOffset, startIndex, endIndex);
        return destination;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] copyInto--B0-L2c(@NotNull final long[] $receiver, final long[] destination, final int destinationOffset, final int startIndex, final int endIndex) {
        ArraysKt___ArraysJvmKt.copyInto($receiver, destination, destinationOffset, startIndex, endIndex);
        return destination;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] copyInto-FUQE5sA(@NotNull final byte[] $receiver, final byte[] destination, final int destinationOffset, final int startIndex, final int endIndex) {
        ArraysKt___ArraysJvmKt.copyInto($receiver, destination, destinationOffset, startIndex, endIndex);
        return destination;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] copyInto-9-ak10g(@NotNull final short[] $receiver, final short[] destination, final int destinationOffset, final int startIndex, final int endIndex) {
        ArraysKt___ArraysJvmKt.copyInto($receiver, destination, destinationOffset, startIndex, endIndex);
        return destination;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] copyOf--ajY-9A(@NotNull final int[] $receiver) {
        final int[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UIntArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] copyOf-QwZRm1k(@NotNull final long[] $receiver) {
        final long[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return ULongArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] copyOf-GBYM_sE(@NotNull final byte[] $receiver) {
        final byte[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UByteArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] copyOf-rL5Bavg(@NotNull final short[] $receiver) {
        final short[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UShortArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] copyOf-qFRl0hI(@NotNull final int[] $receiver, final int newSize) {
        final int[] copy = Arrays.copyOf($receiver, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return UIntArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] copyOf-r7IrZao(@NotNull final long[] $receiver, final int newSize) {
        final long[] copy = Arrays.copyOf($receiver, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return ULongArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] copyOf-PpDY95g(@NotNull final byte[] $receiver, final int newSize) {
        final byte[] copy = Arrays.copyOf($receiver, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return UByteArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] copyOf-nggk6HY(@NotNull final short[] $receiver, final int newSize) {
        final short[] copy = Arrays.copyOf($receiver, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return UShortArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] copyOfRange-oBK06Vg(@NotNull final int[] $receiver, final int fromIndex, final int toIndex) {
        int[] storage;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            storage = ArraysKt___ArraysJvmKt.copyOfRange($receiver, fromIndex, toIndex);
        }
        else {
            if (toIndex > $receiver.length) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $receiver.length);
            }
            Intrinsics.checkExpressionValueIsNotNull(storage = Arrays.copyOfRange($receiver, fromIndex, toIndex), "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        }
        return UIntArray.constructor-impl(storage);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] copyOfRange--nroSd4(@NotNull final long[] $receiver, final int fromIndex, final int toIndex) {
        long[] storage;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            storage = ArraysKt___ArraysJvmKt.copyOfRange($receiver, fromIndex, toIndex);
        }
        else {
            if (toIndex > $receiver.length) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $receiver.length);
            }
            Intrinsics.checkExpressionValueIsNotNull(storage = Arrays.copyOfRange($receiver, fromIndex, toIndex), "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        }
        return ULongArray.constructor-impl(storage);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] copyOfRange-4UcCI2c(@NotNull final byte[] $receiver, final int fromIndex, final int toIndex) {
        byte[] storage;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            storage = ArraysKt___ArraysJvmKt.copyOfRange($receiver, fromIndex, toIndex);
        }
        else {
            if (toIndex > $receiver.length) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $receiver.length);
            }
            Intrinsics.checkExpressionValueIsNotNull(storage = Arrays.copyOfRange($receiver, fromIndex, toIndex), "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        }
        return UByteArray.constructor-impl(storage);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] copyOfRange-Aa5vz7o(@NotNull final short[] $receiver, final int fromIndex, final int toIndex) {
        short[] storage;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            storage = ArraysKt___ArraysJvmKt.copyOfRange($receiver, fromIndex, toIndex);
        }
        else {
            if (toIndex > $receiver.length) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $receiver.length);
            }
            Intrinsics.checkExpressionValueIsNotNull(storage = Arrays.copyOfRange($receiver, fromIndex, toIndex), "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        }
        return UShortArray.constructor-impl(storage);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] toByteArray-GBYM_sE(@NotNull final byte[] $receiver) {
        final byte[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] toIntArray--ajY-9A(@NotNull final int[] $receiver) {
        final int[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] toLongArray-QwZRm1k(@NotNull final long[] $receiver) {
        final long[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] toShortArray-rL5Bavg(@NotNull final short[] $receiver) {
        final short[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UInt[] toTypedArray--ajY-9A(@NotNull final int[] $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/UIntArray.getSize-impl:([I)I
        //    10: istore_1        /* size$iv */
        //    11: iload_1         /* size$iv */
        //    12: anewarray       Lkotlin/UInt;
        //    15: astore_2        /* result$iv */
        //    16: iconst_0       
        //    17: istore_3       
        //    18: aload_2         /* result$iv */
        //    19: arraylength    
        //    20: istore          4
        //    22: iload_3        
        //    23: iload           4
        //    25: if_icmpge       61
        //    28: aload_2         /* result$iv */
        //    29: iload_3         /* i$iv */
        //    30: iload_3         /* i$iv */
        //    31: istore          5
        //    33: istore          9
        //    35: astore          8
        //    37: aload_0         /* $receiver */
        //    38: iload           index
        //    40: invokestatic    kotlin/UIntArray.get-impl:([II)I
        //    43: invokestatic    kotlin/UInt.box-impl:(I)Lkotlin/UInt;
        //    46: astore          10
        //    48: aload           8
        //    50: iload           9
        //    52: aload           10
        //    54: aastore        
        //    55: iinc            i$iv, 1
        //    58: goto            22
        //    61: aload_2         /* result$iv */
        //    62: areturn        
        //    StackMapTable: 00 02 FF 00 16 00 05 07 01 6E 01 07 01 A0 01 01 00 00 26
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final ULong[] toTypedArray-QwZRm1k(@NotNull final long[] $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/ULongArray.getSize-impl:([J)I
        //    10: istore_1        /* size$iv */
        //    11: iload_1         /* size$iv */
        //    12: anewarray       Lkotlin/ULong;
        //    15: astore_2        /* result$iv */
        //    16: iconst_0       
        //    17: istore_3       
        //    18: aload_2         /* result$iv */
        //    19: arraylength    
        //    20: istore          4
        //    22: iload_3        
        //    23: iload           4
        //    25: if_icmpge       61
        //    28: aload_2         /* result$iv */
        //    29: iload_3         /* i$iv */
        //    30: iload_3         /* i$iv */
        //    31: istore          5
        //    33: istore          9
        //    35: astore          8
        //    37: aload_0         /* $receiver */
        //    38: iload           index
        //    40: invokestatic    kotlin/ULongArray.get-impl:([JI)J
        //    43: invokestatic    kotlin/ULong.box-impl:(J)Lkotlin/ULong;
        //    46: astore          10
        //    48: aload           8
        //    50: iload           9
        //    52: aload           10
        //    54: aastore        
        //    55: iinc            i$iv, 1
        //    58: goto            22
        //    61: aload_2         /* result$iv */
        //    62: areturn        
        //    StackMapTable: 00 02 FF 00 16 00 05 07 01 8C 01 07 01 AE 01 01 00 00 26
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UByte[] toTypedArray-GBYM_sE(@NotNull final byte[] $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/UByteArray.getSize-impl:([B)I
        //    10: istore_1        /* size$iv */
        //    11: iload_1         /* size$iv */
        //    12: anewarray       Lkotlin/UByte;
        //    15: astore_2        /* result$iv */
        //    16: iconst_0       
        //    17: istore_3       
        //    18: aload_2         /* result$iv */
        //    19: arraylength    
        //    20: istore          4
        //    22: iload_3        
        //    23: iload           4
        //    25: if_icmpge       61
        //    28: aload_2         /* result$iv */
        //    29: iload_3         /* i$iv */
        //    30: iload_3         /* i$iv */
        //    31: istore          5
        //    33: istore          9
        //    35: astore          8
        //    37: aload_0         /* $receiver */
        //    38: iload           index
        //    40: invokestatic    kotlin/UByteArray.get-impl:([BI)B
        //    43: invokestatic    kotlin/UByte.box-impl:(B)Lkotlin/UByte;
        //    46: astore          10
        //    48: aload           8
        //    50: iload           9
        //    52: aload           10
        //    54: aastore        
        //    55: iinc            i$iv, 1
        //    58: goto            22
        //    61: aload_2         /* result$iv */
        //    62: areturn        
        //    StackMapTable: 00 02 FF 00 16 00 05 07 01 91 01 07 01 B6 01 01 00 00 26
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final UShort[] toTypedArray-rL5Bavg(@NotNull final short[] $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/UShortArray.getSize-impl:([S)I
        //    10: istore_1        /* size$iv */
        //    11: iload_1         /* size$iv */
        //    12: anewarray       Lkotlin/UShort;
        //    15: astore_2        /* result$iv */
        //    16: iconst_0       
        //    17: istore_3       
        //    18: aload_2         /* result$iv */
        //    19: arraylength    
        //    20: istore          4
        //    22: iload_3        
        //    23: iload           4
        //    25: if_icmpge       61
        //    28: aload_2         /* result$iv */
        //    29: iload_3         /* i$iv */
        //    30: iload_3         /* i$iv */
        //    31: istore          5
        //    33: istore          9
        //    35: astore          8
        //    37: aload_0         /* $receiver */
        //    38: iload           index
        //    40: invokestatic    kotlin/UShortArray.get-impl:([SI)S
        //    43: invokestatic    kotlin/UShort.box-impl:(S)Lkotlin/UShort;
        //    46: astore          10
        //    48: aload           8
        //    50: iload           9
        //    52: aload           10
        //    54: aastore        
        //    55: iinc            i$iv, 1
        //    58: goto            22
        //    61: aload_2         /* result$iv */
        //    62: areturn        
        //    StackMapTable: 00 02 FF 00 16 00 05 07 01 96 01 07 01 BE 01 01 00 00 26
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte[] toUByteArray(@NotNull final byte[] $receiver) {
        final byte[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UByteArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int[] toUIntArray(@NotNull final int[] $receiver) {
        final int[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UIntArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] toULongArray(@NotNull final long[] $receiver) {
        final long[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return ULongArray.constructor-impl(copy);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short[] toUShortArray(@NotNull final short[] $receiver) {
        final short[] copy = Arrays.copyOf($receiver, $receiver.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return UShortArray.constructor-impl(copy);
    }
    
    public UArraysKt___UArraysKt() {
    }
}
