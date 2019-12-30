// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Arrays;

public final class BitVector implements Cloneable
{
    private static final int BITSPERSLOT = 64;
    private static final int SLOTSQUANTA = 4;
    private static final int BITSHIFT = 6;
    private static final int BITMASK = 63;
    private long[] bits;
    
    public BitVector() {
        this.bits = new long[4];
    }
    
    public BitVector(final long length) {
        final int need = (int)growthNeeded(length);
        this.bits = new long[need];
    }
    
    public BitVector(final long[] bits) {
        this.bits = bits.clone();
    }
    
    public void copy(final BitVector other) {
        this.bits = other.bits.clone();
    }
    
    private static long slotsNeeded(final long length) {
        return length + 63L >> 6;
    }
    
    private static long growthNeeded(final long length) {
        return (slotsNeeded(length) + 4L - 1L) / 4L * 4L;
    }
    
    private long slot(final int index) {
        return (0 <= index && index < this.bits.length) ? this.bits[index] : 0L;
    }
    
    public void resize(final long length) {
        final int need = (int)growthNeeded(length);
        if (this.bits.length != need) {
            this.bits = Arrays.copyOf(this.bits, need);
        }
        final int shift = (int)(length & 0x3FL);
        int slot = (int)(length >> 6);
        if (shift != 0) {
            final long[] bits = this.bits;
            final int n = slot;
            bits[n] &= (1L << shift) - 1L;
            ++slot;
        }
        while (slot < this.bits.length) {
            this.bits[slot] = 0L;
            ++slot;
        }
    }
    
    public void set(final long bit) {
        final long[] bits = this.bits;
        final int n = (int)(bit >> 6);
        bits[n] |= 1L << (int)(bit & 0x3FL);
    }
    
    public void clear(final long bit) {
        final long[] bits = this.bits;
        final int n = (int)(bit >> 6);
        bits[n] &= ~(1L << (int)(bit & 0x3FL));
    }
    
    public void toggle(final long bit) {
        final long[] bits = this.bits;
        final int n = (int)(bit >> 6);
        bits[n] ^= 1L << (int)(bit & 0x3FL);
    }
    
    public void setTo(final long length) {
        if (0L < length) {
            final int lastWord = (int)(length >> 6);
            final long lastBits = (1L << (int)(length & 0x3FL)) - 1L;
            Arrays.fill(this.bits, 0, lastWord, -1L);
            if (lastBits != 0L) {
                final long[] bits = this.bits;
                final int n = lastWord;
                bits[n] |= lastBits;
            }
        }
    }
    
    public void clearAll() {
        Arrays.fill(this.bits, 0L);
    }
    
    public boolean isSet(final long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 0x3FL)) != 0x0L;
    }
    
    public boolean isClear(final long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 0x3FL)) == 0x0L;
    }
    
    public void shiftLeft(final long shift, final long length) {
        if (shift != 0L) {
            final int leftShift = (int)(shift & 0x3FL);
            final int rightShift = 64 - leftShift;
            final int slotShift = (int)(shift >> 6);
            final int slotCount = this.bits.length - slotShift;
            if (leftShift == 0) {
                for (int slot = 0, from = slotShift; slot < slotCount; ++slot, ++from) {
                    this.bits[slot] = this.slot(from);
                }
            }
            else {
                int slot = 0;
                int from = slotShift;
                while (slot < slotCount) {
                    this.bits[slot] = (this.slot(from) >>> leftShift | this.slot(++from) << rightShift);
                    ++slot;
                }
            }
        }
        this.resize(length);
    }
    
    public void shiftRight(final long shift, final long length) {
        this.resize(length);
        if (shift != 0L) {
            final int rightShift = (int)(shift & 0x3FL);
            final int leftShift = 64 - rightShift;
            final int slotShift = (int)(shift >> 6);
            if (leftShift == 0) {
                for (int slot = this.bits.length, from = slot - slotShift; slot >= slotShift; --slot, --from, this.bits[slot] = this.slot(from)) {}
            }
            else {
                for (int slot = this.bits.length, from = slot - slotShift; slot > 0; --slot, --from, this.bits[slot] = (this.slot(from - 1) >>> leftShift | this.slot(from) << rightShift)) {}
            }
        }
        this.resize(length);
    }
    
    public void setRange(final long fromIndex, final long toIndex) {
        if (fromIndex < toIndex) {
            final int firstWord = (int)(fromIndex >> 6);
            final int lastWord = (int)(toIndex - 1L >> 6);
            final long firstBits = -1L << (int)fromIndex;
            final long lastBits = -1L >>> (int)(-toIndex);
            if (firstWord == lastWord) {
                final long[] bits = this.bits;
                final int n = firstWord;
                bits[n] |= (firstBits & lastBits);
            }
            else {
                final long[] bits2 = this.bits;
                final int n2 = firstWord;
                bits2[n2] |= firstBits;
                Arrays.fill(this.bits, firstWord + 1, lastWord, -1L);
                final long[] bits3 = this.bits;
                final int n3 = lastWord;
                bits3[n3] |= lastBits;
            }
        }
    }
}
