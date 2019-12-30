// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¨\u0006\t" }, d2 = { "lazy", "Lkotlin/Lazy;", "T", "initializer", "Lkotlin/Function0;", "lock", "", "mode", "Lkotlin/LazyThreadSafetyMode;", "kotlin-stdlib" }, xs = "kotlin/LazyKt")
class LazyKt__LazyJVMKt
{
    @NotNull
    public static final <T> Lazy<T> lazy(@NotNull final Function0<? extends T> initializer) {
        Intrinsics.checkParameterIsNotNull(initializer, "initializer");
        return new SynchronizedLazyImpl<T>(initializer, null, 2, null);
    }
    
    @NotNull
    public static final <T> Lazy<T> lazy(@NotNull final LazyThreadSafetyMode mode, @NotNull final Function0<? extends T> initializer) {
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        Intrinsics.checkParameterIsNotNull(initializer, "initializer");
        Serializable s = null;
        switch (LazyKt$WhenMappings.$EnumSwitchMapping$0[mode.ordinal()]) {
            case 1: {
                s = new SynchronizedLazyImpl<Object>(initializer, null, 2, null);
                break;
            }
            case 2: {
                s = new SafePublicationLazyImpl<Object>(initializer);
                break;
            }
            case 3: {
                s = new UnsafeLazyImpl<Object>(initializer);
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return (Lazy<T>)s;
    }
    
    @NotNull
    public static final <T> Lazy<T> lazy(@Nullable final Object lock, @NotNull final Function0<? extends T> initializer) {
        Intrinsics.checkParameterIsNotNull(initializer, "initializer");
        return new SynchronizedLazyImpl<T>(initializer, lock);
    }
    
    public LazyKt__LazyJVMKt() {
    }
}
