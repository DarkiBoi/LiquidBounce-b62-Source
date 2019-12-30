// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import kotlin.PublishedApi;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002¨\u0006\u000b" }, d2 = { "differenceModulo", "", "a", "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "kotlin-stdlib" })
public final class ProgressionUtilKt
{
    private static final int mod(final int a, final int b) {
        final int mod = a % b;
        return (mod >= 0) ? mod : (mod + b);
    }
    
    private static final long mod(final long a, final long b) {
        final long mod = a % b;
        return (mod >= 0L) ? mod : (mod + b);
    }
    
    private static final int differenceModulo(final int a, final int b, final int c) {
        return mod(mod(a, c) - mod(b, c), c);
    }
    
    private static final long differenceModulo(final long a, final long b, final long c) {
        return mod(mod(a, c) - mod(b, c), c);
    }
    
    @PublishedApi
    public static final int getProgressionLastElement(final int start, final int end, final int step) {
        int n;
        if (step > 0) {
            n = ((start >= end) ? end : (end - differenceModulo(end, start, step)));
        }
        else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            n = ((start <= end) ? end : (end + differenceModulo(start, end, -step)));
        }
        return n;
    }
    
    @PublishedApi
    public static final long getProgressionLastElement(final long start, final long end, final long step) {
        long n;
        if (step > 0L) {
            n = ((start >= end) ? end : (end - differenceModulo(end, start, step)));
        }
        else {
            if (step >= 0L) {
                throw new IllegalArgumentException("Step is zero.");
            }
            n = ((start <= end) ? end : (end + differenceModulo(start, end, -step)));
        }
        return n;
    }
}
