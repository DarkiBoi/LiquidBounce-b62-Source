// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.JvmName;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000(\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0001\u001a\"\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\"\u0010\b\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\t\u0010\u0007\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000bH\u0001\u001a\"\u0010\f\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a\"\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0001\u00f8\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u000f\u001a\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bH\u0000\u001a\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u0001H\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0016" }, d2 = { "uintCompare", "", "v1", "v2", "uintDivide", "Lkotlin/UInt;", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "ulongCompare", "", "ulongDivide", "Lkotlin/ULong;", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToString", "", "v", "base", "kotlin-stdlib" })
@JvmName(name = "UnsignedKt")
public final class UnsignedKt
{
    @PublishedApi
    public static final int uintCompare(final int v1, final int v2) {
        return Intrinsics.compare(v1 ^ Integer.MIN_VALUE, v2 ^ Integer.MIN_VALUE);
    }
    
    @PublishedApi
    public static final int ulongCompare(final long v1, final long v2) {
        return lcmp(v1 ^ Long.MIN_VALUE, v2 ^ Long.MIN_VALUE);
    }
    
    @PublishedApi
    public static final int uintDivide-J1ME1BU(final int v1, final int v2) {
        return UInt.constructor-impl((int)(((long)v1 & 0xFFFFFFFFL) / ((long)v2 & 0xFFFFFFFFL)));
    }
    
    @PublishedApi
    public static final int uintRemainder-J1ME1BU(final int v1, final int v2) {
        return UInt.constructor-impl((int)(((long)v1 & 0xFFFFFFFFL) % ((long)v2 & 0xFFFFFFFFL)));
    }
    
    @PublishedApi
    public static final long ulongDivide-eb3DHEI(final long v1, final long v2) {
        final long dividend = v1;
        final long divisor = v2;
        if (divisor < 0L) {
            return (ulongCompare(v1, v2) < 0) ? ULong.constructor-impl(0L) : ULong.constructor-impl(1L);
        }
        if (dividend >= 0L) {
            return ULong.constructor-impl(dividend / divisor);
        }
        final long quotient = (dividend >>> 1) / divisor << 1;
        final long rem = dividend - quotient * divisor;
        return ULong.constructor-impl(quotient + (long)((ulongCompare(ULong.constructor-impl(rem), ULong.constructor-impl(divisor)) >= 0) ? 1 : 0));
    }
    
    @PublishedApi
    public static final long ulongRemainder-eb3DHEI(final long v1, final long v2) {
        final long dividend = v1;
        final long divisor = v2;
        if (divisor < 0L) {
            return (ulongCompare(v1, v2) < 0) ? v1 : ULong.constructor-impl(v1 - v2);
        }
        if (dividend >= 0L) {
            return ULong.constructor-impl(dividend % divisor);
        }
        final long quotient = (dividend >>> 1) / divisor << 1;
        final long rem = dividend - quotient * divisor;
        return ULong.constructor-impl(rem - ((ulongCompare(ULong.constructor-impl(rem), ULong.constructor-impl(divisor)) >= 0) ? divisor : 0L));
    }
    
    @NotNull
    public static final String ulongToString(final long v) {
        return ulongToString(v, 10);
    }
    
    @NotNull
    public static final String ulongToString(final long v, final int base) {
        if (v >= 0L) {
            final String string = Long.toString(v, CharsKt__CharJVMKt.checkRadix(base));
            Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Long.toString(this, checkRadix(radix))");
            return string;
        }
        long quotient = (v >>> 1) / base << 1;
        long rem = v - quotient * base;
        if (rem >= base) {
            rem -= base;
            ++quotient;
        }
        final StringBuilder sb = new StringBuilder();
        final long i = quotient;
        final StringBuilder sb2 = sb;
        final String string2 = Long.toString(i, CharsKt__CharJVMKt.checkRadix(base));
        Intrinsics.checkExpressionValueIsNotNull(string2, "java.lang.Long.toString(this, checkRadix(radix))");
        final StringBuilder append = sb2.append(string2);
        final long j = rem;
        final StringBuilder sb3 = append;
        final String string3 = Long.toString(j, CharsKt__CharJVMKt.checkRadix(base));
        Intrinsics.checkExpressionValueIsNotNull(string3, "java.lang.Long.toString(this, checkRadix(radix))");
        return sb3.append(string3).toString();
    }
}
