// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015" }, d2 = { "numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt
{
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toByteOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Integer intOrNull = toIntOrNull($receiver, radix);
        if (intOrNull == null) {
            return null;
        }
        final int int1 = intOrNull;
        if (int1 < -128 || int1 > 127) {
            return null;
        }
        return (byte)int1;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toShortOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull final String $receiver, final int radix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Integer intOrNull = toIntOrNull($receiver, radix);
        if (intOrNull == null) {
            return null;
        }
        final int int1 = intOrNull;
        if (int1 < -32768 || int1 > 32767) {
            return null;
        }
        return (short)int1;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toIntOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull final String $receiver, final int radix) {
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
        //    22: aload_0         /* $receiver */
        //    23: iconst_0       
        //    24: invokevirtual   java/lang/String.charAt:(I)C
        //    27: istore          firstChar
        //    29: iload           firstChar
        //    31: bipush          48
        //    33: if_icmpge       84
        //    36: iload_2         /* length */
        //    37: iconst_1       
        //    38: if_icmpne       43
        //    41: aconst_null    
        //    42: areturn        
        //    43: iconst_1       
        //    44: istore_3        /* start */
        //    45: iload           firstChar
        //    47: bipush          45
        //    49: if_icmpne       62
        //    52: iconst_1       
        //    53: istore          isNegative
        //    55: ldc             -2147483648
        //    57: istore          limit
        //    59: goto            93
        //    62: iload           firstChar
        //    64: bipush          43
        //    66: if_icmpne       79
        //    69: iconst_0       
        //    70: istore          isNegative
        //    72: ldc             -2147483647
        //    74: istore          limit
        //    76: goto            93
        //    79: aconst_null    
        //    80: areturn        
        //    81: nop            
        //    82: nop            
        //    83: athrow         
        //    84: iconst_0       
        //    85: istore_3        /* start */
        //    86: iconst_0       
        //    87: istore          isNegative
        //    89: ldc             -2147483647
        //    91: istore          limit
        //    93: iload           limit
        //    95: iload_1         /* radix */
        //    96: idiv           
        //    97: istore          limitBeforeMul
        //    99: iconst_0       
        //   100: istore          result
        //   102: iload_3         /* start */
        //   103: istore          9
        //   105: iload_2         /* length */
        //   106: iconst_1       
        //   107: isub           
        //   108: istore          10
        //   110: iload           9
        //   112: iload           10
        //   114: if_icmpgt       183
        //   117: aload_0         /* $receiver */
        //   118: iload           i
        //   120: invokevirtual   java/lang/String.charAt:(I)C
        //   123: iload_1         /* radix */
        //   124: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //   127: istore          digit
        //   129: iload           digit
        //   131: ifge            136
        //   134: aconst_null    
        //   135: areturn        
        //   136: iload           result
        //   138: iload           limitBeforeMul
        //   140: if_icmpge       145
        //   143: aconst_null    
        //   144: areturn        
        //   145: iload           result
        //   147: iload_1         /* radix */
        //   148: imul           
        //   149: istore          result
        //   151: iload           result
        //   153: iload           limit
        //   155: iload           digit
        //   157: iadd           
        //   158: if_icmpge       163
        //   161: aconst_null    
        //   162: areturn        
        //   163: iload           result
        //   165: iload           digit
        //   167: isub           
        //   168: istore          result
        //   170: iload           i
        //   172: iload           10
        //   174: if_icmpeq       183
        //   177: iinc            i, 1
        //   180: goto            117
        //   183: iload           isNegative
        //   185: ifeq            196
        //   188: iload           result
        //   190: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   193: goto            202
        //   196: iload           result
        //   198: ineg           
        //   199: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   202: areturn        
        //    StackMapTable: 00 0E FC 00 16 01 FF 00 14 00 07 07 00 56 01 01 00 00 00 01 00 00 FF 00 12 00 07 07 00 56 01 01 01 00 00 01 00 00 10 FF 00 01 00 00 00 01 07 00 61 FF 00 02 00 07 07 00 56 01 01 00 00 00 01 00 00 FF 00 08 00 07 07 00 56 01 01 01 01 01 01 00 00 FF 00 17 00 0B 07 00 56 01 01 01 01 01 01 01 01 01 01 00 00 FC 00 12 01 08 11 FA 00 13 0C 45 07 00 3B
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toLongOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull final String $receiver, final int radix) {
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
        //    22: aload_0         /* $receiver */
        //    23: iconst_0       
        //    24: invokevirtual   java/lang/String.charAt:(I)C
        //    27: istore          firstChar
        //    29: iload           firstChar
        //    31: bipush          48
        //    33: if_icmpge       86
        //    36: iload_2         /* length */
        //    37: iconst_1       
        //    38: if_icmpne       43
        //    41: aconst_null    
        //    42: areturn        
        //    43: iconst_1       
        //    44: istore_3        /* start */
        //    45: iload           firstChar
        //    47: bipush          45
        //    49: if_icmpne       63
        //    52: iconst_1       
        //    53: istore          isNegative
        //    55: ldc2_w          -9223372036854775808
        //    58: lstore          limit
        //    60: goto            96
        //    63: iload           firstChar
        //    65: bipush          43
        //    67: if_icmpne       81
        //    70: iconst_0       
        //    71: istore          isNegative
        //    73: ldc2_w          -9223372036854775807
        //    76: lstore          limit
        //    78: goto            96
        //    81: aconst_null    
        //    82: areturn        
        //    83: nop            
        //    84: nop            
        //    85: athrow         
        //    86: iconst_0       
        //    87: istore_3        /* start */
        //    88: iconst_0       
        //    89: istore          isNegative
        //    91: ldc2_w          -9223372036854775807
        //    94: lstore          limit
        //    96: lload           limit
        //    98: iload_1         /* radix */
        //    99: i2l            
        //   100: ldiv           
        //   101: lstore          limitBeforeMul
        //   103: lconst_0       
        //   104: lstore          result
        //   106: iload_3         /* start */
        //   107: istore          12
        //   109: iload_2         /* length */
        //   110: iconst_1       
        //   111: isub           
        //   112: istore          13
        //   114: iload           12
        //   116: iload           13
        //   118: if_icmpgt       192
        //   121: aload_0         /* $receiver */
        //   122: iload           i
        //   124: invokevirtual   java/lang/String.charAt:(I)C
        //   127: iload_1         /* radix */
        //   128: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //   131: istore          digit
        //   133: iload           digit
        //   135: ifge            140
        //   138: aconst_null    
        //   139: areturn        
        //   140: lload           result
        //   142: lload           limitBeforeMul
        //   144: lcmp           
        //   145: ifge            150
        //   148: aconst_null    
        //   149: areturn        
        //   150: lload           result
        //   152: iload_1         /* radix */
        //   153: i2l            
        //   154: lmul           
        //   155: lstore          result
        //   157: lload           result
        //   159: lload           limit
        //   161: iload           digit
        //   163: i2l            
        //   164: ladd           
        //   165: lcmp           
        //   166: ifge            171
        //   169: aconst_null    
        //   170: areturn        
        //   171: lload           result
        //   173: iload           digit
        //   175: i2l            
        //   176: lsub           
        //   177: lstore          result
        //   179: iload           i
        //   181: iload           13
        //   183: if_icmpeq       192
        //   186: iinc            i, 1
        //   189: goto            121
        //   192: iload           isNegative
        //   194: ifeq            205
        //   197: lload           result
        //   199: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   202: goto            211
        //   205: lload           result
        //   207: lneg           
        //   208: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   211: areturn        
        //    StackMapTable: 00 0E FC 00 16 01 FF 00 14 00 08 07 00 56 01 01 00 00 00 00 01 00 00 FF 00 13 00 08 07 00 56 01 01 01 00 00 00 01 00 00 11 FF 00 01 00 00 00 01 07 00 61 FF 00 02 00 08 07 00 56 01 01 00 00 00 00 01 00 00 FF 00 09 00 07 07 00 56 01 01 01 01 04 01 00 00 FF 00 18 00 0B 07 00 56 01 01 01 01 04 01 04 04 01 01 00 00 FC 00 12 01 09 14 FA 00 14 0C 45 07 00 7A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final Void numberFormatError(@NotNull final String input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        throw new NumberFormatException("Invalid number format: '" + input + '\'');
    }
    
    public StringsKt__StringNumberConversionsKt() {
    }
}
