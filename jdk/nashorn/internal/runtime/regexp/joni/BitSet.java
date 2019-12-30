// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

public final class BitSet
{
    static final int BITS_PER_BYTE = 8;
    public static final int SINGLE_BYTE_SIZE = 256;
    private static final int BITS_IN_ROOM = 32;
    static final int BITSET_SIZE = 8;
    static final int ROOM_SHIFT;
    final int[] bits;
    private static final int BITS_TO_STRING_WRAP = 4;
    
    public BitSet() {
        this.bits = new int[8];
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("BitSet");
        for (int i = 0; i < 256; ++i) {
            if (i % 64 == 0) {
                buffer.append("\n  ");
            }
            buffer.append(this.at(i) ? "1" : "0");
        }
        return buffer.toString();
    }
    
    public boolean at(final int pos) {
        return (this.bits[pos >>> BitSet.ROOM_SHIFT] & bit(pos)) != 0x0;
    }
    
    public void set(final int pos) {
        final int[] bits = this.bits;
        final int n = pos >>> BitSet.ROOM_SHIFT;
        bits[n] |= bit(pos);
    }
    
    public void clear(final int pos) {
        final int[] bits = this.bits;
        final int n = pos >>> BitSet.ROOM_SHIFT;
        bits[n] &= ~bit(pos);
    }
    
    public void clear() {
        for (int i = 0; i < 8; ++i) {
            this.bits[i] = 0;
        }
    }
    
    public boolean isEmpty() {
        for (int i = 0; i < 8; ++i) {
            if (this.bits[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public void setRange(final int from, final int to) {
        for (int i = from; i <= to && i < 256; ++i) {
            this.set(i);
        }
    }
    
    public void invert() {
        for (int i = 0; i < 8; ++i) {
            this.bits[i] ^= -1;
        }
    }
    
    public void invertTo(final BitSet to) {
        for (int i = 0; i < 8; ++i) {
            to.bits[i] = ~this.bits[i];
        }
    }
    
    public void and(final BitSet other) {
        for (int i = 0; i < 8; ++i) {
            final int[] bits = this.bits;
            final int n = i;
            bits[n] &= other.bits[i];
        }
    }
    
    public void or(final BitSet other) {
        for (int i = 0; i < 8; ++i) {
            final int[] bits = this.bits;
            final int n = i;
            bits[n] |= other.bits[i];
        }
    }
    
    public void copy(final BitSet other) {
        for (int i = 0; i < 8; ++i) {
            this.bits[i] = other.bits[i];
        }
    }
    
    public int numOn() {
        int num = 0;
        for (int i = 0; i < 256; ++i) {
            if (this.at(i)) {
                ++num;
            }
        }
        return num;
    }
    
    static int bit(final int pos) {
        return 1 << pos % 256;
    }
    
    private static int log2(final int np) {
        int log = 0;
        int n = np;
        while ((n >>>= 1) != 0) {
            ++log;
        }
        return log;
    }
    
    static {
        ROOM_SHIFT = log2(32);
    }
}
