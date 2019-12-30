// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u001f\u0010\b\u001a\u00020\u00012\n\u0010\t\u001a\u00020\u0001\"\u00020\u0006H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f" }, d2 = { "ULongArray", "Lkotlin/ULongArray;", "size", "", "init", "Lkotlin/Function1;", "Lkotlin/ULong;", "(ILkotlin/jvm/functions/Function1;)[J", "ulongArrayOf", "elements", "ulongArrayOf-QwZRm1k", "([J)[J", "kotlin-stdlib" })
public final class ULongArrayKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long[] ULongArray(final int size, final Function1<? super Integer, ULong> init) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iload_0         /* size */
        //     2: newarray        J
        //     4: astore_3        /* result$iv */
        //     5: iconst_0       
        //     6: istore          4
        //     8: aload_3         /* result$iv */
        //     9: arraylength    
        //    10: istore          5
        //    12: iload           4
        //    14: iload           5
        //    16: if_icmpge       66
        //    19: aload_3         /* result$iv */
        //    20: iload           i$iv
        //    22: iload           i$iv
        //    24: istore          6
        //    26: istore          12
        //    28: astore          11
        //    30: aload_1         /* init */
        //    31: iload           index
        //    33: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    36: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    41: checkcast       Lkotlin/ULong;
        //    44: invokevirtual   kotlin/ULong.unbox-impl:()J
        //    47: lstore          7
        //    49: lload           7
        //    51: lstore          13
        //    53: aload           11
        //    55: iload           12
        //    57: lload           13
        //    59: lastore        
        //    60: iinc            i$iv, 1
        //    63: goto            12
        //    66: aload_3         /* result$iv */
        //    67: invokestatic    kotlin/ULongArray.constructor-impl:([J)[J
        //    70: areturn        
        //    Signature:
        //  (ILkotlin/jvm/functions/Function1<-Ljava/lang/Integer;Lkotlin/ULong;>;)[J
        //    StackMapTable: 00 02 FF 00 0C 00 06 01 07 00 25 00 07 00 27 01 01 00 00 35
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
    private static final long[] ulongArrayOf-QwZRm1k(final long... elements) {
        return elements;
    }
}
