// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0010\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B7\b\u0000\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010" }, d2 = { "Lkotlin/random/XorWowRandom;", "Lkotlin/random/Random;", "seed1", "", "seed2", "(II)V", "x", "y", "z", "w", "v", "addend", "(IIIIII)V", "nextBits", "bitCount", "nextInt", "kotlin-stdlib" })
public final class XorWowRandom extends Random
{
    private int x;
    private int y;
    private int z;
    private int w;
    private int v;
    private int addend;
    
    @Override
    public int nextInt() {
        int t = this.x;
        t ^= t >>> 2;
        this.x = this.y;
        this.y = this.z;
        this.z = this.w;
        final int v0 = this.v;
        this.w = v0;
        t = (t ^ t << 1 ^ v0 ^ v0 << 4);
        this.v = t;
        this.addend += 362437;
        return t + this.addend;
    }
    
    @Override
    public int nextBits(final int bitCount) {
        return RandomKt.takeUpperBits(this.nextInt(), bitCount);
    }
    
    public XorWowRandom(final int x, final int y, final int z, final int w, final int v, final int addend) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.v = v;
        this.addend = addend;
        if ((this.x | this.y | this.z | this.w | this.v) == 0x0) {
            throw new IllegalArgumentException("Initial state must have at least one non-zero element.".toString());
        }
        for (int n = 64, i = 0; i < n; ++i) {
            final int it = i;
            this.nextInt();
        }
    }
    
    public XorWowRandom(final int seed1, final int seed2) {
        this(seed1, seed2, 0, 0, ~seed1, seed1 << 10 ^ seed2 >>> 4);
    }
}
