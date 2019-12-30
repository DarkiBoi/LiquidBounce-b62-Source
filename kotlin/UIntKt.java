// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.internal.InlineOnly;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0004H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0006H\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n" }, d2 = { "toUInt", "Lkotlin/UInt;", "", "(B)I", "", "(I)I", "", "(J)I", "", "(S)I", "kotlin-stdlib" })
public final class UIntKt
{
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int toUInt(final byte $receiver) {
        return UInt.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int toUInt(final short $receiver) {
        return UInt.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int toUInt(final int $receiver) {
        return UInt.constructor-impl($receiver);
    }
    
    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int toUInt(final long $receiver) {
        return UInt.constructor-impl((int)$receiver);
    }
}
