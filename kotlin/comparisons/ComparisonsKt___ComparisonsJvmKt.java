// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.comparisons;

import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000(\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a\u0019\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\bH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\tH\u0087\b\u001a!\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\nH\u0087\b\u001a!\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\nH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\fH\u0087\b\u001a!\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a-\u0010\u000e\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u000e\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a\u0019\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0087\b\u001a!\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\bH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\tH\u0087\b\u001a!\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\nH\u0087\b\u001a!\u0010\u000e\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\nH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\fH\u0087\b\u001a!\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¨\u0006\u000f" }, d2 = { "maxOf", "T", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "c", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "minOf", "kotlin-stdlib" }, xs = "kotlin/comparisons/ComparisonsKt")
class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt
{
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull final T a, @NotNull final T b) {
        Intrinsics.checkParameterIsNotNull(a, "a");
        Intrinsics.checkParameterIsNotNull(b, "b");
        return (a.compareTo((Object)b) >= 0) ? a : b;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte maxOf(final byte a, final byte b) {
        return (byte)Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short maxOf(final short a, final short b) {
        return (short)Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int maxOf(final int a, final int b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long maxOf(final long a, final long b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float maxOf(final float a, final float b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double maxOf(final double a, final double b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull final T a, @NotNull final T b, @NotNull final T c) {
        Intrinsics.checkParameterIsNotNull(a, "a");
        Intrinsics.checkParameterIsNotNull(b, "b");
        Intrinsics.checkParameterIsNotNull(c, "c");
        return maxOf(a, maxOf(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte maxOf(final byte a, final byte b, final byte c) {
        return (byte)Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short maxOf(final short a, final short b, final short c) {
        return (short)Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int maxOf(final int a, final int b, final int c) {
        return Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long maxOf(final long a, final long b, final long c) {
        return Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float maxOf(final float a, final float b, final float c) {
        return Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double maxOf(final double a, final double b, final double c) {
        return Math.max(a, Math.max(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull final T a, @NotNull final T b) {
        Intrinsics.checkParameterIsNotNull(a, "a");
        Intrinsics.checkParameterIsNotNull(b, "b");
        return (a.compareTo((Object)b) <= 0) ? a : b;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte minOf(final byte a, final byte b) {
        return (byte)Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short minOf(final short a, final short b) {
        return (short)Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int minOf(final int a, final int b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long minOf(final long a, final long b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float minOf(final float a, final float b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double minOf(final double a, final double b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull final T a, @NotNull final T b, @NotNull final T c) {
        Intrinsics.checkParameterIsNotNull(a, "a");
        Intrinsics.checkParameterIsNotNull(b, "b");
        Intrinsics.checkParameterIsNotNull(c, "c");
        return minOf(a, minOf(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte minOf(final byte a, final byte b, final byte c) {
        return (byte)Math.min(a, Math.min(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short minOf(final short a, final short b, final short c) {
        return (short)Math.min(a, Math.min(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int minOf(final int a, final int b, final int c) {
        return Math.min(a, Math.min(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long minOf(final long a, final long b, final long c) {
        return Math.min(a, Math.min(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float minOf(final float a, final float b, final float c) {
        return Math.min(a, Math.min(b, c));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double minOf(final double a, final double b, final double c) {
        return Math.min(a, Math.min(b, c));
    }
    
    public ComparisonsKt___ComparisonsJvmKt() {
    }
}
