// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import org.jetbrains.annotations.Nullable;
import kotlin.ranges.UIntRange;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 b2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001bB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0000H\u0097\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0005J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u000fJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u000bJ\u001b\u0010\u0019\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u0016J\u0013\u0010\u001f\u001a\u00020 2\b\u0010\t\u001a\u0004\u0018\u00010!H\u00d6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001J\u0013\u0010#\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b$\u0010\u0005J\u0013\u0010%\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b&\u0010\u0005J\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b(\u0010\u000fJ\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b)\u0010\u000bJ\u001b\u0010'\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b*\u0010\u001dJ\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b+\u0010\u0016J\u001b\u0010,\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b-\u0010\u000bJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b/\u0010\u000fJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b0\u0010\u000bJ\u001b\u0010.\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b1\u0010\u001dJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b2\u0010\u0016J\u001b\u00103\u001a\u0002042\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b5\u00106J\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b8\u0010\u000fJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b9\u0010\u000bJ\u001b\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b:\u0010\u001dJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b;\u0010\u0016J\u001b\u0010<\u001a\u00020\u00002\u0006\u0010=\u001a\u00020\u0003H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00002\u0006\u0010=\u001a\u00020\u0003H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b@\u0010\u000bJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bB\u0010\u000fJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bC\u0010\u000bJ\u001b\u0010A\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bD\u0010\u001dJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bE\u0010\u0016J\u0010\u0010F\u001a\u00020GH\u0087\b¢\u0006\u0004\bH\u0010IJ\u0010\u0010J\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bK\u0010\u0005J\u0010\u0010L\u001a\u00020MH\u0087\b¢\u0006\u0004\bN\u0010OJ\u0010\u0010P\u001a\u00020QH\u0087\b¢\u0006\u0004\bR\u0010SJ\u000f\u0010T\u001a\u00020UH\u0016¢\u0006\u0004\bV\u0010WJ\u0013\u0010X\u001a\u00020\rH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bY\u0010IJ\u0013\u0010Z\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b[\u0010\u0005J\u0013\u0010\\\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b]\u0010OJ\u0013\u0010^\u001a\u00020\u0014H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b_\u0010SJ\u001b\u0010`\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\ba\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006c" }, d2 = { "Lkotlin/UInt;", "", "data", "", "constructor-impl", "(I)I", "data$annotations", "()V", "and", "other", "and-WZ4Q5Ns", "(II)I", "compareTo", "Lkotlin/UByte;", "compareTo-7apg3OU", "(IB)I", "compareTo-WZ4Q5Ns", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(IJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(IS)I", "dec", "dec-impl", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(IJ)J", "div-xj2QHRw", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-WZ4Q5Ns", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-WZ4Q5Ns", "(II)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-impl", "shr", "shr-impl", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(I)B", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(I)J", "toShort", "", "toShort-impl", "(I)S", "toString", "", "toString-impl", "(I)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-WZ4Q5Ns", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public final class UInt implements Comparable<UInt>
{
    private final int data = data;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = -1;
    public static final int SIZE_BYTES = 4;
    public static final int SIZE_BITS = 32;
    public static final Companion Companion;
    
    @InlineOnly
    private int compareTo-WZ4Q5Ns(final int other) {
        return compareTo-WZ4Q5Ns(this.data, other);
    }
    
    @NotNull
    @Override
    public String toString() {
        return toString-impl(this.data);
    }
    
    @InlineOnly
    private static final int compareTo-7apg3OU(final int $this, final byte other) {
        return UnsignedKt.uintCompare($this, constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int compareTo-xj2QHRw(final int $this, final short other) {
        return UnsignedKt.uintCompare($this, constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static int compareTo-WZ4Q5Ns(final int $this, final int other) {
        return UnsignedKt.uintCompare($this, other);
    }
    
    @InlineOnly
    private static final int compareTo-VKZWuLQ(final int $this, final long other) {
        return UnsignedKt.ulongCompare(ULong.constructor-impl((long)$this & 0xFFFFFFFFL), other);
    }
    
    @InlineOnly
    private static final int plus-7apg3OU(final int $this, final byte other) {
        return constructor-impl($this + constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int plus-xj2QHRw(final int $this, final short other) {
        return constructor-impl($this + constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int plus-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this + other);
    }
    
    @InlineOnly
    private static final long plus-VKZWuLQ(final int $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFFFFFL) + other);
    }
    
    @InlineOnly
    private static final int minus-7apg3OU(final int $this, final byte other) {
        return constructor-impl($this - constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int minus-xj2QHRw(final int $this, final short other) {
        return constructor-impl($this - constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int minus-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this - other);
    }
    
    @InlineOnly
    private static final long minus-VKZWuLQ(final int $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFFFFFL) - other);
    }
    
    @InlineOnly
    private static final int times-7apg3OU(final int $this, final byte other) {
        return constructor-impl($this * constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int times-xj2QHRw(final int $this, final short other) {
        return constructor-impl($this * constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int times-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this * other);
    }
    
    @InlineOnly
    private static final long times-VKZWuLQ(final int $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFFFFFL) * other);
    }
    
    @InlineOnly
    private static final int div-7apg3OU(final int $this, final byte other) {
        return UnsignedKt.uintDivide-J1ME1BU($this, constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int div-xj2QHRw(final int $this, final short other) {
        return UnsignedKt.uintDivide-J1ME1BU($this, constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int div-WZ4Q5Ns(final int $this, final int other) {
        return UnsignedKt.uintDivide-J1ME1BU($this, other);
    }
    
    @InlineOnly
    private static final long div-VKZWuLQ(final int $this, final long other) {
        return UnsignedKt.ulongDivide-eb3DHEI(ULong.constructor-impl((long)$this & 0xFFFFFFFFL), other);
    }
    
    @InlineOnly
    private static final int rem-7apg3OU(final int $this, final byte other) {
        return UnsignedKt.uintRemainder-J1ME1BU($this, constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int rem-xj2QHRw(final int $this, final short other) {
        return UnsignedKt.uintRemainder-J1ME1BU($this, constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int rem-WZ4Q5Ns(final int $this, final int other) {
        return UnsignedKt.uintRemainder-J1ME1BU($this, other);
    }
    
    @InlineOnly
    private static final long rem-VKZWuLQ(final int $this, final long other) {
        return UnsignedKt.ulongRemainder-eb3DHEI(ULong.constructor-impl((long)$this & 0xFFFFFFFFL), other);
    }
    
    @InlineOnly
    private static final int inc-impl(final int $this) {
        return constructor-impl($this + 1);
    }
    
    @InlineOnly
    private static final int dec-impl(final int $this) {
        return constructor-impl($this - 1);
    }
    
    @InlineOnly
    private static final UIntRange rangeTo-WZ4Q5Ns(final int $this, final int other) {
        return new UIntRange($this, other, null);
    }
    
    @InlineOnly
    private static final int shl-impl(final int $this, final int bitCount) {
        return constructor-impl($this << bitCount);
    }
    
    @InlineOnly
    private static final int shr-impl(final int $this, final int bitCount) {
        return constructor-impl($this >>> bitCount);
    }
    
    @InlineOnly
    private static final int and-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this & other);
    }
    
    @InlineOnly
    private static final int or-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this | other);
    }
    
    @InlineOnly
    private static final int xor-WZ4Q5Ns(final int $this, final int other) {
        return constructor-impl($this ^ other);
    }
    
    @InlineOnly
    private static final int inv-impl(final int $this) {
        return constructor-impl(~$this);
    }
    
    @InlineOnly
    private static final byte toByte-impl(final int $this) {
        return (byte)$this;
    }
    
    @InlineOnly
    private static final short toShort-impl(final int $this) {
        return (short)$this;
    }
    
    @InlineOnly
    private static final int toInt-impl(final int $this) {
        return $this;
    }
    
    @InlineOnly
    private static final long toLong-impl(final int $this) {
        return (long)$this & 0xFFFFFFFFL;
    }
    
    @InlineOnly
    private static final byte toUByte-impl(final int $this) {
        return UByte.constructor-impl((byte)$this);
    }
    
    @InlineOnly
    private static final short toUShort-impl(final int $this) {
        return UShort.constructor-impl((short)$this);
    }
    
    @InlineOnly
    private static final int toUInt-impl(final int $this) {
        return $this;
    }
    
    @InlineOnly
    private static final long toULong-impl(final int $this) {
        return ULong.constructor-impl((long)$this & 0xFFFFFFFFL);
    }
    
    @NotNull
    public static String toString-impl(final int $this) {
        return String.valueOf((long)$this & 0xFFFFFFFFL);
    }
    
    @PublishedApi
    public static int constructor-impl(final int data) {
        return data;
    }
    
    public static int hashCode-impl(final int n) {
        return n;
    }
    
    public static boolean equals-impl(final int n, @Nullable final Object o) {
        return o instanceof UInt && n == ((UInt)o).unbox-impl();
    }
    
    public static final boolean equals-impl0(final int p1, final int p2) {
        throw null;
    }
    
    public final /* synthetic */ int unbox-impl() {
        return this.data;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Override
    public int hashCode() {
        return hashCode-impl(this.data);
    }
    
    @Override
    public boolean equals(final Object o) {
        return equals-impl(this.data, o);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n" }, d2 = { "Lkotlin/UInt$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UInt;", "I", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib" })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
