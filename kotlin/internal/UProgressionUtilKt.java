// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import kotlin.ULong;
import kotlin.UInt;
import kotlin.UnsignedKt;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002\u00f8\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012" }, d2 = { "differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib" })
public final class UProgressionUtilKt
{
    private static final int differenceModulo-WZ9TVnA(final int a, final int b, final int c) {
        final int ac = UnsignedKt.uintRemainder-J1ME1BU(a, c);
        final int bc = UnsignedKt.uintRemainder-J1ME1BU(b, c);
        return (UnsignedKt.uintCompare(ac, bc) >= 0) ? UInt.constructor-impl(ac - bc) : UInt.constructor-impl(UInt.constructor-impl(ac - bc) + c);
    }
    
    private static final long differenceModulo-sambcqE(final long a, final long b, final long c) {
        final long ac = UnsignedKt.ulongRemainder-eb3DHEI(a, c);
        final long bc = UnsignedKt.ulongRemainder-eb3DHEI(b, c);
        return (UnsignedKt.ulongCompare(ac, bc) >= 0) ? ULong.constructor-impl(ac - bc) : ULong.constructor-impl(ULong.constructor-impl(ac - bc) + c);
    }
    
    @PublishedApi
    @SinceKotlin(version = "1.3")
    public static final int getProgressionLastElement-Nkh28Cs(final int start, final int end, final int step) {
        int n;
        if (step > 0) {
            n = ((UnsignedKt.uintCompare(start, end) >= 0) ? end : UInt.constructor-impl(end - differenceModulo-WZ9TVnA(end, start, UInt.constructor-impl(step))));
        }
        else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            n = ((UnsignedKt.uintCompare(start, end) <= 0) ? end : UInt.constructor-impl(end + differenceModulo-WZ9TVnA(start, end, UInt.constructor-impl(-step))));
        }
        return n;
    }
    
    @PublishedApi
    @SinceKotlin(version = "1.3")
    public static final long getProgressionLastElement-7ftBX0g(final long start, final long end, final long step) {
        long n;
        if (step > 0L) {
            n = ((UnsignedKt.ulongCompare(start, end) >= 0) ? end : ULong.constructor-impl(end - differenceModulo-sambcqE(end, start, ULong.constructor-impl(step))));
        }
        else {
            if (step >= 0L) {
                throw new IllegalArgumentException("Step is zero.");
            }
            n = ((UnsignedKt.ulongCompare(start, end) <= 0) ? end : ULong.constructor-impl(end + differenceModulo-sambcqE(start, end, ULong.constructor-impl(-step))));
        }
        return n;
    }
}
