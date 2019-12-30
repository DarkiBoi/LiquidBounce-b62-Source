// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import java.util.Random;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0014J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\bH\u0016J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\bH\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u0017H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u001a" }, d2 = { "Lkotlin/random/KotlinRandom;", "Ljava/util/Random;", "impl", "Lkotlin/random/Random;", "(Lkotlin/random/Random;)V", "getImpl", "()Lkotlin/random/Random;", "next", "", "bits", "nextBoolean", "", "nextBytes", "", "bytes", "", "nextDouble", "", "nextFloat", "", "nextInt", "bound", "nextLong", "", "setSeed", "seed", "kotlin-stdlib" })
final class KotlinRandom extends Random
{
    @NotNull
    private final kotlin.random.Random impl;
    
    @Override
    protected int next(final int bits) {
        return this.impl.nextBits(bits);
    }
    
    @Override
    public int nextInt() {
        return this.impl.nextInt();
    }
    
    @Override
    public int nextInt(final int bound) {
        return this.impl.nextInt(bound);
    }
    
    @Override
    public boolean nextBoolean() {
        return this.impl.nextBoolean();
    }
    
    @Override
    public long nextLong() {
        return this.impl.nextLong();
    }
    
    @Override
    public float nextFloat() {
        return this.impl.nextFloat();
    }
    
    @Override
    public double nextDouble() {
        return this.impl.nextDouble();
    }
    
    @Override
    public void nextBytes(@NotNull final byte[] bytes) {
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        this.impl.nextBytes(bytes);
    }
    
    @Override
    public void setSeed(final long seed) {
        throw new UnsupportedOperationException("Setting seed is not supported.");
    }
    
    @NotNull
    public final kotlin.random.Random getImpl() {
        return this.impl;
    }
    
    public KotlinRandom(@NotNull final kotlin.random.Random impl) {
        Intrinsics.checkParameterIsNotNull(impl, "impl");
        this.impl = impl;
    }
}
