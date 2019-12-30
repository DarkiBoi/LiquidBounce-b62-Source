// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.functions.Function1;
import java.math.MathContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000X\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\u0082\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\r\u0010\u0012\u001a\u00020\u0013*\u00020\u0003H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u0017*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0019\u001a\r\u0010\u001a\u001a\u00020\u001b*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u001b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001d\u001a\r\u0010\u001e\u001a\u00020\u0010*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001e\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u001f\u001a\u00020 *\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010!\u001a\u00020\"*\u00020\u0003H\u0087\b\u001a\u0015\u0010!\u001a\u00020\"*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020 2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\"2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b¨\u0006$" }, d2 = { "screenFloatValue", "T", "str", "", "parse", "Lkotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsJVMKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toByte", "", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toLong", "", "toShort", "", "toString", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt
{
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final byte $receiver, final int radix) {
        final String string = Integer.toString($receiver, CharsKt__CharJVMKt.checkRadix(CharsKt__CharJVMKt.checkRadix(radix)));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final short $receiver, final int radix) {
        final String string = Integer.toString($receiver, CharsKt__CharJVMKt.checkRadix(CharsKt__CharJVMKt.checkRadix(radix)));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final int $receiver, final int radix) {
        final String string = Integer.toString($receiver, CharsKt__CharJVMKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final long $receiver, final int radix) {
        final String string = Long.toString($receiver, CharsKt__CharJVMKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Long.toString(this, checkRadix(radix))");
        return string;
    }
    
    @InlineOnly
    private static final boolean toBoolean(@NotNull final String $receiver) {
        return Boolean.parseBoolean($receiver);
    }
    
    @InlineOnly
    private static final byte toByte(@NotNull final String $receiver) {
        return Byte.parseByte($receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte toByte(@NotNull final String $receiver, final int radix) {
        return Byte.parseByte($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @InlineOnly
    private static final short toShort(@NotNull final String $receiver) {
        return Short.parseShort($receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short toShort(@NotNull final String $receiver, final int radix) {
        return Short.parseShort($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @InlineOnly
    private static final int toInt(@NotNull final String $receiver) {
        return Integer.parseInt($receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int toInt(@NotNull final String $receiver, final int radix) {
        return Integer.parseInt($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @InlineOnly
    private static final long toLong(@NotNull final String $receiver) {
        return Long.parseLong($receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long toLong(@NotNull final String $receiver, final int radix) {
        return Long.parseLong($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @InlineOnly
    private static final float toFloat(@NotNull final String $receiver) {
        return Float.parseFloat($receiver);
    }
    
    @InlineOnly
    private static final double toDouble(@NotNull final String $receiver) {
        return Double.parseDouble($receiver);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Float toFloatOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Float n;
        try {
            Float value;
            if (ScreenFloatValueRegEx.value.matches($receiver)) {
                final String p1 = $receiver;
                value = Float.parseFloat(p1);
            }
            else {
                value = null;
            }
            n = value;
        }
        catch (NumberFormatException e$iv) {
            n = null;
        }
        return n;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Double toDoubleOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Double n;
        try {
            Double value;
            if (ScreenFloatValueRegEx.value.matches($receiver)) {
                final String p1 = $receiver;
                value = Double.parseDouble(p1);
            }
            else {
                value = null;
            }
            n = value;
        }
        catch (NumberFormatException e$iv) {
            n = null;
        }
        return n;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull final String $receiver) {
        return new BigInteger($receiver);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull final String $receiver, final int radix) {
        return new BigInteger($receiver, CharsKt__CharJVMKt.checkRadix(radix));
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toBigIntegerOrNull($receiver, 10);
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull final String $receiver, final int radix) {
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
        //    17: tableswitch {
        //                0: 40
        //                1: 42
        //          default: 59
        //        }
        //    40: aconst_null    
        //    41: areturn        
        //    42: aload_0         /* $receiver */
        //    43: iconst_0       
        //    44: invokevirtual   java/lang/String.charAt:(I)C
        //    47: iload_1         /* radix */
        //    48: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //    51: ifge            109
        //    54: aconst_null    
        //    55: areturn        
        //    56: nop            
        //    57: nop            
        //    58: athrow         
        //    59: aload_0         /* $receiver */
        //    60: iconst_0       
        //    61: invokevirtual   java/lang/String.charAt:(I)C
        //    64: bipush          45
        //    66: if_icmpne       73
        //    69: iconst_1       
        //    70: goto            74
        //    73: iconst_0       
        //    74: istore_3        /* start */
        //    75: iload_3         /* start */
        //    76: istore          4
        //    78: iload_2         /* length */
        //    79: istore          5
        //    81: iload           4
        //    83: iload           5
        //    85: if_icmpge       109
        //    88: aload_0         /* $receiver */
        //    89: iload           index
        //    91: invokevirtual   java/lang/String.charAt:(I)C
        //    94: iload_1         /* radix */
        //    95: invokestatic    kotlin/text/CharsKt.digitOf:(CI)I
        //    98: ifge            103
        //   101: aconst_null    
        //   102: areturn        
        //   103: iinc            index, 1
        //   106: goto            81
        //   109: aload_0         /* $receiver */
        //   110: astore_3       
        //   111: new             Ljava/math/BigInteger;
        //   114: dup            
        //   115: aload_3        
        //   116: iload_1         /* radix */
        //   117: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //   120: invokespecial   java/math/BigInteger.<init>:(Ljava/lang/String;I)V
        //   123: areturn        
        //    StackMapTable: 00 09 FC 00 28 01 01 FF 00 0D 00 00 00 01 07 00 D5 FE 00 02 07 00 C7 01 01 0D 40 01 FE 00 06 01 01 01 15 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final String $receiver) {
        return new BigDecimal($receiver);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final String $receiver, final MathContext mathContext) {
        return new BigDecimal($receiver, mathContext);
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        BigDecimal bigDecimal2;
        try {
            BigDecimal bigDecimal;
            if (ScreenFloatValueRegEx.value.matches($receiver)) {
                final String it = $receiver;
                bigDecimal = new BigDecimal(it);
            }
            else {
                bigDecimal = null;
            }
            bigDecimal2 = bigDecimal;
        }
        catch (NumberFormatException e$iv) {
            bigDecimal2 = null;
        }
        return bigDecimal2;
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull final String $receiver, @NotNull final MathContext mathContext) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(mathContext, "mathContext");
        BigDecimal bigDecimal2;
        try {
            BigDecimal bigDecimal;
            if (ScreenFloatValueRegEx.value.matches($receiver)) {
                final String it = $receiver;
                bigDecimal = new BigDecimal(it, mathContext);
            }
            else {
                bigDecimal = null;
            }
            bigDecimal2 = bigDecimal;
        }
        catch (NumberFormatException e$iv) {
            bigDecimal2 = null;
        }
        return bigDecimal2;
    }
    
    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsJVMKt(final String str, final Function1<? super String, ? extends T> parse) {
        T t;
        try {
            t = (ScreenFloatValueRegEx.value.matches(str) ? parse.invoke(str) : null);
        }
        catch (NumberFormatException e) {
            t = null;
        }
        return t;
    }
    
    public StringsKt__StringNumberConversionsJVMKt() {
    }
}
