// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import org.jetbrains.annotations.Nullable;
import kotlin.ranges.UIntRange;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 ^2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001^B\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n\u00f8\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u0010J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0013J\u001b\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u0013\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#H\u00d6\u0003J\t\u0010$\u001a\u00020\rH\u00d6\u0001J\u0013\u0010%\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b&\u0010\u0005J\u0013\u0010'\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b(\u0010\u0005J\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b*\u0010\u0010J\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b+\u0010\u0013J\u001b\u0010)\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b,\u0010\u001fJ\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b-\u0010\u0018J\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b/\u0010\u000bJ\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b1\u0010\u0010J\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b2\u0010\u0013J\u001b\u00100\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b3\u0010\u001fJ\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b4\u0010\u0018J\u001b\u00105\u001a\u0002062\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b7\u00108J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b:\u0010\u0010J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b;\u0010\u0013J\u001b\u00109\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b<\u0010\u001fJ\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b=\u0010\u0018J\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b?\u0010\u0010J\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\b@\u0010\u0013J\u001b\u0010>\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bA\u0010\u001fJ\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000¢\u0006\u0004\bB\u0010\u0018J\u0010\u0010C\u001a\u00020DH\u0087\b¢\u0006\u0004\bE\u0010FJ\u0010\u0010G\u001a\u00020\rH\u0087\b¢\u0006\u0004\bH\u0010IJ\u0010\u0010J\u001a\u00020KH\u0087\b¢\u0006\u0004\bL\u0010MJ\u0010\u0010N\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bO\u0010\u0005J\u000f\u0010P\u001a\u00020QH\u0016¢\u0006\u0004\bR\u0010SJ\u0013\u0010T\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bU\u0010FJ\u0013\u0010V\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bW\u0010IJ\u0013\u0010X\u001a\u00020\u0014H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\bY\u0010MJ\u0013\u0010Z\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000¢\u0006\u0004\b[\u0010\u0005J\u001b\u0010\\\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000¢\u0006\u0004\b]\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006_" }, d2 = { "Lkotlin/UShort;", "", "data", "", "constructor-impl", "(S)S", "data$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-impl", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toInt", "toInt-impl", "(S)I", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-xj2QHRw", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.3")
@ExperimentalUnsignedTypes
public final class UShort implements Comparable<UShort>
{
    private final short data = data;
    public static final short MIN_VALUE = 0;
    public static final short MAX_VALUE = -1;
    public static final int SIZE_BYTES = 2;
    public static final int SIZE_BITS = 16;
    public static final Companion Companion;
    
    @InlineOnly
    private int compareTo-xj2QHRw(final short other) {
        return compareTo-xj2QHRw(this.data, other);
    }
    
    @NotNull
    @Override
    public String toString() {
        return toString-impl(this.data);
    }
    
    @InlineOnly
    private static final int compareTo-7apg3OU(final short $this, final byte other) {
        return Intrinsics.compare($this & 0xFFFF, other & 0xFF);
    }
    
    @InlineOnly
    private static int compareTo-xj2QHRw(final short $this, final short other) {
        return Intrinsics.compare($this & 0xFFFF, other & 0xFFFF);
    }
    
    @InlineOnly
    private static final int compareTo-WZ4Q5Ns(final short $this, final int other) {
        return UnsignedKt.uintCompare(UInt.constructor-impl($this & 0xFFFF), other);
    }
    
    @InlineOnly
    private static final int compareTo-VKZWuLQ(final short $this, final long other) {
        return UnsignedKt.ulongCompare(ULong.constructor-impl((long)$this & 0xFFFFL), other);
    }
    
    @InlineOnly
    private static final int plus-7apg3OU(final short $this, final byte other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) + UInt.constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int plus-xj2QHRw(final short $this, final short other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) + UInt.constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int plus-WZ4Q5Ns(final short $this, final int other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) + other);
    }
    
    @InlineOnly
    private static final long plus-VKZWuLQ(final short $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFL) + other);
    }
    
    @InlineOnly
    private static final int minus-7apg3OU(final short $this, final byte other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) - UInt.constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int minus-xj2QHRw(final short $this, final short other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) - UInt.constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int minus-WZ4Q5Ns(final short $this, final int other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) - other);
    }
    
    @InlineOnly
    private static final long minus-VKZWuLQ(final short $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFL) - other);
    }
    
    @InlineOnly
    private static final int times-7apg3OU(final short $this, final byte other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) * UInt.constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int times-xj2QHRw(final short $this, final short other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) * UInt.constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int times-WZ4Q5Ns(final short $this, final int other) {
        return UInt.constructor-impl(UInt.constructor-impl($this & 0xFFFF) * other);
    }
    
    @InlineOnly
    private static final long times-VKZWuLQ(final short $this, final long other) {
        return ULong.constructor-impl(ULong.constructor-impl((long)$this & 0xFFFFL) * other);
    }
    
    @InlineOnly
    private static final int div-7apg3OU(final short $this, final byte other) {
        return UnsignedKt.uintDivide-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), UInt.constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int div-xj2QHRw(final short $this, final short other) {
        return UnsignedKt.uintDivide-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), UInt.constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int div-WZ4Q5Ns(final short $this, final int other) {
        return UnsignedKt.uintDivide-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), other);
    }
    
    @InlineOnly
    private static final long div-VKZWuLQ(final short $this, final long other) {
        return UnsignedKt.ulongDivide-eb3DHEI(ULong.constructor-impl((long)$this & 0xFFFFL), other);
    }
    
    @InlineOnly
    private static final int rem-7apg3OU(final short $this, final byte other) {
        return UnsignedKt.uintRemainder-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), UInt.constructor-impl(other & 0xFF));
    }
    
    @InlineOnly
    private static final int rem-xj2QHRw(final short $this, final short other) {
        return UnsignedKt.uintRemainder-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), UInt.constructor-impl(other & 0xFFFF));
    }
    
    @InlineOnly
    private static final int rem-WZ4Q5Ns(final short $this, final int other) {
        return UnsignedKt.uintRemainder-J1ME1BU(UInt.constructor-impl($this & 0xFFFF), other);
    }
    
    @InlineOnly
    private static final long rem-VKZWuLQ(final short $this, final long other) {
        return UnsignedKt.ulongRemainder-eb3DHEI(ULong.constructor-impl((long)$this & 0xFFFFL), other);
    }
    
    @InlineOnly
    private static final short inc-impl(final short $this) {
        return constructor-impl((short)($this + 1));
    }
    
    @InlineOnly
    private static final short dec-impl(final short $this) {
        return constructor-impl((short)($this - 1));
    }
    
    @InlineOnly
    private static final UIntRange rangeTo-xj2QHRw(final short $this, final short other) {
        return new UIntRange(UInt.constructor-impl($this & 0xFFFF), UInt.constructor-impl(other & 0xFFFF), null);
    }
    
    @InlineOnly
    private static final short and-xj2QHRw(final short $this, final short other) {
        return constructor-impl((short)($this & other));
    }
    
    @InlineOnly
    private static final short or-xj2QHRw(final short $this, final short other) {
        return constructor-impl((short)($this | other));
    }
    
    @InlineOnly
    private static final short xor-xj2QHRw(final short $this, final short other) {
        return constructor-impl((short)($this ^ other));
    }
    
    @InlineOnly
    private static final short inv-impl(final short $this) {
        return constructor-impl((short)~$this);
    }
    
    @InlineOnly
    private static final byte toByte-impl(final short $this) {
        return (byte)$this;
    }
    
    @InlineOnly
    private static final short toShort-impl(final short $this) {
        return $this;
    }
    
    @InlineOnly
    private static final int toInt-impl(final short $this) {
        return $this & 0xFFFF;
    }
    
    @InlineOnly
    private static final long toLong-impl(final short $this) {
        return (long)$this & 0xFFFFL;
    }
    
    @InlineOnly
    private static final byte toUByte-impl(final short $this) {
        return UByte.constructor-impl((byte)$this);
    }
    
    @InlineOnly
    private static final short toUShort-impl(final short $this) {
        return $this;
    }
    
    @InlineOnly
    private static final int toUInt-impl(final short $this) {
        return UInt.constructor-impl($this & 0xFFFF);
    }
    
    @InlineOnly
    private static final long toULong-impl(final short $this) {
        return ULong.constructor-impl((long)$this & 0xFFFFL);
    }
    
    @NotNull
    public static String toString-impl(final short $this) {
        return String.valueOf($this & 0xFFFF);
    }
    
    @PublishedApi
    public static short constructor-impl(final short data) {
        return data;
    }
    
    public static int hashCode-impl(final short n) {
        return n;
    }
    
    public static boolean equals-impl(final short n, @Nullable final Object o) {
        return o instanceof UShort && n == ((UShort)o).unbox-impl();
    }
    
    public static final boolean equals-impl0(final short p1, final short p2) {
        throw null;
    }
    
    public final /* synthetic */ short unbox-impl() {
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
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n" }, d2 = { "Lkotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UShort;", "S", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib" })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
