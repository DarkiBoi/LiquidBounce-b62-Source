// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.random;

import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import java.util.Random;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0003*\u0001\b\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\t¨\u0006\n" }, d2 = { "Lkotlin/random/FallbackThreadLocalRandom;", "Lkotlin/random/AbstractPlatformRandom;", "()V", "impl", "Ljava/util/Random;", "getImpl", "()Ljava/util/Random;", "implStorage", "kotlin/random/FallbackThreadLocalRandom$implStorage$1", "Lkotlin/random/FallbackThreadLocalRandom$implStorage$1;", "kotlin-stdlib" })
public final class FallbackThreadLocalRandom extends AbstractPlatformRandom
{
    private final FallbackThreadLocalRandom$implStorage.FallbackThreadLocalRandom$implStorage$1 implStorage;
    
    @NotNull
    @Override
    public java.util.Random getImpl() {
        final Object value = this.implStorage.get();
        Intrinsics.checkExpressionValueIsNotNull(value, "implStorage.get()");
        return (java.util.Random)value;
    }
    
    public FallbackThreadLocalRandom() {
        this.implStorage = new FallbackThreadLocalRandom$implStorage.FallbackThreadLocalRandom$implStorage$1();
    }
}
