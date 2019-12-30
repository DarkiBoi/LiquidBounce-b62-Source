// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.experimental;

import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0005\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f¨\u0006\u0007" }, d2 = { "and", "", "other", "", "inv", "or", "xor", "kotlin-stdlib" })
public final class BitwiseOperationsKt
{
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte and(final byte $receiver, final byte other) {
        return (byte)($receiver & other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte or(final byte $receiver, final byte other) {
        return (byte)($receiver | other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte xor(final byte $receiver, final byte other) {
        return (byte)($receiver ^ other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte inv(final byte $receiver) {
        return (byte)~$receiver;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short and(final short $receiver, final short other) {
        return (short)($receiver & other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short or(final short $receiver, final short other) {
        return (short)($receiver | other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short xor(final short $receiver, final short other) {
        return (short)($receiver ^ other);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short inv(final short $receiver) {
        return (short)~$receiver;
    }
}
