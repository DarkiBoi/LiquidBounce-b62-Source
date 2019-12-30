// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import org.jetbrains.annotations.Nullable;
import kotlin.ULong;
import kotlin.UInt;
import kotlin.UShort;
import kotlin.UByte;
import kotlin.UnsignedKt;
import org.jetbrains.annotations.NotNull;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006 " }, d2 = { "toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib" })
@JvmName(name = "UStringsKt")
public final class UStringsKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String toString-LxnNnR4(final byte $receiver, final int radix) {
        final String string = Integer.toString($receiver & 0xFF, CharsKt__CharJVMKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String toString-olVBNx4(final short $receiver, final int radix) {
        final String string = Integer.toString($receiver & 0xFFFF, CharsKt__CharJVMKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String toString-V7xB4Y4(final int $receiver, final int radix) {
        final String string = Long.toString((long)$receiver & 0xFFFFFFFFL, CharsKt__CharJVMKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Long.toString(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final String toString-JSWoG40(final long $receiver, final int radix) {
        return UnsignedKt.ulongToString($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final byte toUByte(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UByte uByteOrNull = toUByteOrNull($receiver);
        if (uByteOrNull != null) {
            return uByteOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final byte toUByte(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UByte uByteOrNull = toUByteOrNull($receiver, radix);
        if (uByteOrNull != null) {
            return uByteOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final short toUShort(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UShort uShortOrNull = toUShortOrNull($receiver);
        if (uShortOrNull != null) {
            return uShortOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final short toUShort(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UShort uShortOrNull = toUShortOrNull($receiver, radix);
        if (uShortOrNull != null) {
            return uShortOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int toUInt(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UInt uIntOrNull = toUIntOrNull($receiver);
        if (uIntOrNull != null) {
            return uIntOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int toUInt(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UInt uIntOrNull = toUIntOrNull($receiver, radix);
        if (uIntOrNull != null) {
            return uIntOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long toULong(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final ULong uLongOrNull = toULongOrNull($receiver);
        if (uLongOrNull != null) {
            return uLongOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final long toULong(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final ULong uLongOrNull = toULongOrNull($receiver, radix);
        if (uLongOrNull != null) {
            return uLongOrNull.unbox-impl();
        }
        StringsKt__StringNumberConversionsKt.numberFormatError($receiver);
        throw null;
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UByte toUByteOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toUByteOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UByte toUByteOrNull(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UInt uIntOrNull = toUIntOrNull($receiver, radix);
        if (uIntOrNull == null) {
            return null;
        }
        final int int1 = uIntOrNull.unbox-impl();
        if (UnsignedKt.uintCompare(int1, UInt.constructor-impl(-1 & 0xFF)) > 0) {
            return null;
        }
        return UByte.box-impl(UByte.constructor-impl((byte)int1));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UShort toUShortOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toUShortOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UShort toUShortOrNull(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final UInt uIntOrNull = toUIntOrNull($receiver, radix);
        if (uIntOrNull == null) {
            return null;
        }
        final int int1 = uIntOrNull.unbox-impl();
        if (UnsignedKt.uintCompare(int1, UInt.constructor-impl(-1 & 0xFFFF)) > 0) {
            return null;
        }
        return UShort.box-impl(UShort.constructor-impl((short)int1));
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UInt toUIntOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toUIntOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final UInt toUIntOrNull(@NotNull final String $receiver, final int radix) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: iload_1         /* radix */
        //     7: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //    10: pop            
        //    11: aload_0         /* $receiver */
        //    12: invokevirtual   java/lang/String.length:()I
        //    15: istore_2        /* length */
        //    16: iload_2         /* length */
        //    17: ifne            22
        //    20: aconst_null    
        //    21: areturn        
        //    22: iconst_m1      
        //    23: istore_3        /* limit */
        //    24: aload_0         /* $receiver */
        //    25: iconst_0       
        //    26: invokevirtual   java/lang/String.charAt:(I)C
        //    29: istore          firstChar
        //    31: iload           firstChar
        //    33: bipush          48
        //    35: if_icmpge       58
        //    38: iload_2         /* length */
        //    39: iconst_1       
        //    40: if_icmpeq       50
        //    43: iload           firstChar
        //    45: bipush          43
        //    47: if_icmpeq       52
        //    50: aconst_null    
        //    51: areturn        
        //    52: iconst_1       
        //    53: istore          start
        //    55: goto            61
        //    58: iconst_0       
        //    59: istore          start
        //    61: iload_1         /* radix */
        //    62: istore          7
        //    64: iload           7
        //    66: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //    69: istore          uradix
        //    71: iload_3         /* limit */
        //    72: istore          8
        //    74: iload           8
        //    76: iload           uradix
        //    78: invokestatic    kotlin/UnsignedKt.uintDivide-J1ME1BU:(II)I
        //    81: istore          limitBeforeMul
        //    83: iconst_0       
        //    84: istore          result
        //    86: iload           start
        //    88: istore          9
        //    90: iload_2         /* length */
        //    91: istore          10
        //    93: iload           9
        //    95: iload           10
        //    97: if_icmpge       200
        //   100: aload_0         /* $receiver */
        //   101: iload           i
        //   103: invokevirtual   java/lang/String.charAt:(I)C
        //   106: iload_1         /* radix */
        //   107: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //   110: istore          digit
        //   112: iload           digit
        //   114: ifge            119
        //   117: aconst_null    
        //   118: areturn        
        //   119: iload           result
        //   121: istore          12
        //   123: iload           12
        //   125: iload           limitBeforeMul
        //   127: invokestatic    kotlin/UnsignedKt.uintCompare:(II)I
        //   130: ifle            135
        //   133: aconst_null    
        //   134: areturn        
        //   135: iload           result
        //   137: istore          12
        //   139: iload           12
        //   141: iload           uradix
        //   143: imul           
        //   144: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //   147: istore          result
        //   149: iload           result
        //   151: istore          beforeAdding
        //   153: iload           result
        //   155: istore          13
        //   157: iload           digit
        //   159: istore          14
        //   161: iload           14
        //   163: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //   166: istore          14
        //   168: iload           13
        //   170: iload           14
        //   172: iadd           
        //   173: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //   176: istore          result
        //   178: iload           result
        //   180: istore          13
        //   182: iload           13
        //   184: iload           beforeAdding
        //   186: invokestatic    kotlin/UnsignedKt.uintCompare:(II)I
        //   189: ifge            194
        //   192: aconst_null    
        //   193: areturn        
        //   194: iinc            i, 1
        //   197: goto            93
        //   200: iload           result
        //   202: invokestatic    kotlin/UInt.box-impl:(I)Lkotlin/UInt;
        //   205: areturn        
        //    StackMapTable: 00 0A FC 00 16 01 FE 00 1B 01 00 01 01 05 FF 00 02 00 06 07 00 A1 01 01 01 01 01 00 00 FF 00 1F 00 0B 07 00 A1 01 01 01 01 01 01 01 01 01 01 00 00 FC 00 19 01 FC 00 0F 01 FD 00 3A 01 01 FF 00 05 00 0B 07 00 A1 01 01 01 01 01 01 01 01 01 01 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final ULong toULongOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toULongOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @Nullable
    public static final ULong toULongOrNull(@NotNull final String $receiver, final int radix) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: iload_1         /* radix */
        //     7: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //    10: pop            
        //    11: aload_0         /* $receiver */
        //    12: invokevirtual   java/lang/String.length:()I
        //    15: istore_2        /* length */
        //    16: iload_2         /* length */
        //    17: ifne            22
        //    20: aconst_null    
        //    21: areturn        
        //    22: ldc2_w          -1
        //    25: lstore_3        /* limit */
        //    26: aload_0         /* $receiver */
        //    27: iconst_0       
        //    28: invokevirtual   java/lang/String.charAt:(I)C
        //    31: istore          firstChar
        //    33: iload           firstChar
        //    35: bipush          48
        //    37: if_icmpge       60
        //    40: iload_2         /* length */
        //    41: iconst_1       
        //    42: if_icmpeq       52
        //    45: iload           firstChar
        //    47: bipush          43
        //    49: if_icmpeq       54
        //    52: aconst_null    
        //    53: areturn        
        //    54: iconst_1       
        //    55: istore          start
        //    57: goto            63
        //    60: iconst_0       
        //    61: istore          start
        //    63: iload_1         /* radix */
        //    64: istore          8
        //    66: iload           8
        //    68: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //    71: istore          uradix
        //    73: lload_3         /* limit */
        //    74: lstore          10
        //    76: lload           10
        //    78: lstore          12
        //    80: iload           uradix
        //    82: istore          14
        //    84: iload           14
        //    86: i2l            
        //    87: ldc2_w          4294967295
        //    90: land           
        //    91: invokestatic    kotlin/ULong.constructor-impl:(J)J
        //    94: lstore          15
        //    96: lload           12
        //    98: lload           15
        //   100: invokestatic    kotlin/UnsignedKt.ulongDivide-eb3DHEI:(JJ)J
        //   103: lstore          limitBeforeMul
        //   105: lconst_0       
        //   106: lstore          result
        //   108: iload           start
        //   110: istore          12
        //   112: iload_2         /* length */
        //   113: istore          13
        //   115: iload           12
        //   117: iload           13
        //   119: if_icmpge       262
        //   122: aload_0         /* $receiver */
        //   123: iload           i
        //   125: invokevirtual   java/lang/String.charAt:(I)C
        //   128: iload_1         /* radix */
        //   129: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //   132: istore          digit
        //   134: iload           digit
        //   136: ifge            141
        //   139: aconst_null    
        //   140: areturn        
        //   141: lload           result
        //   143: lstore          15
        //   145: lload           15
        //   147: lload           limitBeforeMul
        //   149: invokestatic    kotlin/UnsignedKt.ulongCompare:(JJ)I
        //   152: ifle            157
        //   155: aconst_null    
        //   156: areturn        
        //   157: lload           result
        //   159: lstore          15
        //   161: lload           15
        //   163: lstore          17
        //   165: iload           uradix
        //   167: istore          19
        //   169: iload           19
        //   171: i2l            
        //   172: ldc2_w          4294967295
        //   175: land           
        //   176: invokestatic    kotlin/ULong.constructor-impl:(J)J
        //   179: lstore          20
        //   181: lload           17
        //   183: lload           20
        //   185: lmul           
        //   186: invokestatic    kotlin/ULong.constructor-impl:(J)J
        //   189: lstore          result
        //   191: lload           result
        //   193: lstore          beforeAdding
        //   195: lload           result
        //   197: lstore          17
        //   199: iload           digit
        //   201: istore          19
        //   203: iload           19
        //   205: invokestatic    kotlin/UInt.constructor-impl:(I)I
        //   208: istore          19
        //   210: lload           17
        //   212: lstore          20
        //   214: iload           19
        //   216: istore          22
        //   218: iload           22
        //   220: i2l            
        //   221: ldc2_w          4294967295
        //   224: land           
        //   225: invokestatic    kotlin/ULong.constructor-impl:(J)J
        //   228: lstore          23
        //   230: lload           20
        //   232: lload           23
        //   234: ladd           
        //   235: invokestatic    kotlin/ULong.constructor-impl:(J)J
        //   238: lstore          result
        //   240: lload           result
        //   242: lstore          17
        //   244: lload           17
        //   246: lload           beforeAdding
        //   248: invokestatic    kotlin/UnsignedKt.ulongCompare:(JJ)I
        //   251: ifge            256
        //   254: aconst_null    
        //   255: areturn        
        //   256: iinc            i, 1
        //   259: goto            115
        //   262: lload           result
        //   264: invokestatic    kotlin/ULong.box-impl:(J)Lkotlin/ULong;
        //   267: areturn        
        //    StackMapTable: 00 0A FC 00 16 01 FE 00 1D 04 00 01 01 05 FF 00 02 00 06 07 00 A1 01 01 04 01 01 00 00 FF 00 33 00 0D 07 00 A1 01 01 04 01 01 01 04 04 01 01 01 04 00 00 19 0F FF 00 62 00 12 07 00 A1 01 01 04 01 01 01 04 04 01 01 01 04 04 01 04 01 04 00 00 FF 00 05 00 0D 07 00 A1 01 01 04 01 01 01 04 04 01 01 01 04 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
