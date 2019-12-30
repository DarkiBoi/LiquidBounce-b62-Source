// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.reflect;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0016" }, d2 = { "Lkotlin/reflect/KTypeProjection;", "", "variance", "Lkotlin/reflect/KVariance;", "type", "Lkotlin/reflect/KType;", "(Lkotlin/reflect/KVariance;Lkotlin/reflect/KType;)V", "getType", "()Lkotlin/reflect/KType;", "getVariance", "()Lkotlin/reflect/KVariance;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public final class KTypeProjection
{
    @Nullable
    private final KVariance variance;
    @Nullable
    private final KType type;
    @NotNull
    private static final KTypeProjection STAR;
    public static final Companion Companion;
    
    @Nullable
    public final KVariance getVariance() {
        return this.variance;
    }
    
    @Nullable
    public final KType getType() {
        return this.type;
    }
    
    public KTypeProjection(@Nullable final KVariance variance, @Nullable final KType type) {
        this.variance = variance;
        this.type = type;
    }
    
    static {
        Companion = new Companion(null);
        STAR = new KTypeProjection(null, null);
    }
    
    public static final /* synthetic */ KTypeProjection access$getSTAR$cp() {
        return KTypeProjection.STAR;
    }
    
    @Nullable
    public final KVariance component1() {
        return this.variance;
    }
    
    @Nullable
    public final KType component2() {
        return this.type;
    }
    
    @NotNull
    public final KTypeProjection copy(@Nullable final KVariance variance, @Nullable final KType type) {
        return new KTypeProjection(variance, type);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "KTypeProjection(variance=" + this.variance + ", type=" + this.type + ")";
    }
    
    @Override
    public int hashCode() {
        final KVariance variance = this.variance;
        final int n = ((variance != null) ? variance.hashCode() : 0) * 31;
        final KType type = this.type;
        return n + ((type != null) ? type.hashCode() : 0);
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (this != o) {
            if (o instanceof KTypeProjection) {
                final KTypeProjection kTypeProjection = (KTypeProjection)o;
                if (Intrinsics.areEqual(this.variance, kTypeProjection.variance) && Intrinsics.areEqual(this.type, kTypeProjection.type)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\f" }, d2 = { "Lkotlin/reflect/KTypeProjection$Companion;", "", "()V", "STAR", "Lkotlin/reflect/KTypeProjection;", "getSTAR", "()Lkotlin/reflect/KTypeProjection;", "contravariant", "type", "Lkotlin/reflect/KType;", "covariant", "invariant", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final KTypeProjection getSTAR() {
            return KTypeProjection.access$getSTAR$cp();
        }
        
        @NotNull
        public final KTypeProjection invariant(@NotNull final KType type) {
            Intrinsics.checkParameterIsNotNull(type, "type");
            return new KTypeProjection(KVariance.INVARIANT, type);
        }
        
        @NotNull
        public final KTypeProjection contravariant(@NotNull final KType type) {
            Intrinsics.checkParameterIsNotNull(type, "type");
            return new KTypeProjection(KVariance.IN, type);
        }
        
        @NotNull
        public final KTypeProjection covariant(@NotNull final KType type) {
            Intrinsics.checkParameterIsNotNull(type, "type");
            return new KTypeProjection(KVariance.OUT, type);
        }
        
        private Companion() {
        }
    }
}
