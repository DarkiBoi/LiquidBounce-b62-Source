// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.internal.InlineOnly;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0001\n\u0002\b\u0004\u001a\u001c\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a=\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a*\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a=\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n¨\u0006\u0010" }, d2 = { "check", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "checkNotNull", "T", "(Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "error", "", "message", "require", "requireNotNull", "kotlin-stdlib" }, xs = "kotlin/PreconditionsKt")
class PreconditionsKt__PreconditionsKt extends PreconditionsKt__AssertionsJVMKt
{
    @InlineOnly
    private static final void require(final boolean value) {
        if (!value) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }
    
    @InlineOnly
    private static final void require(final boolean value, final Function0<?> lazyMessage) {
        if (!value) {
            final Object message = lazyMessage.invoke();
            throw new IllegalArgumentException(message.toString());
        }
    }
    
    @InlineOnly
    private static final <T> T requireNotNull(final T value) {
        if (value == null) {
            throw new IllegalArgumentException("Required value was null.".toString());
        }
        return value;
    }
    
    @InlineOnly
    private static final <T> T requireNotNull(final T value, final Function0<?> lazyMessage) {
        if (value == null) {
            final Object message = lazyMessage.invoke();
            throw new IllegalArgumentException(message.toString());
        }
        return value;
    }
    
    @InlineOnly
    private static final void check(final boolean value) {
        if (!value) {
            throw new IllegalStateException("Check failed.".toString());
        }
    }
    
    @InlineOnly
    private static final void check(final boolean value, final Function0<?> lazyMessage) {
        if (!value) {
            final Object message = lazyMessage.invoke();
            throw new IllegalStateException(message.toString());
        }
    }
    
    @InlineOnly
    private static final <T> T checkNotNull(final T value) {
        if (value == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        return value;
    }
    
    @InlineOnly
    private static final <T> T checkNotNull(final T value, final Function0<?> lazyMessage) {
        if (value == null) {
            final Object message = lazyMessage.invoke();
            throw new IllegalStateException(message.toString());
        }
        return value;
    }
    
    @InlineOnly
    private static final Void error(final Object message) {
        throw new IllegalStateException(message.toString());
    }
    
    public PreconditionsKt__PreconditionsKt() {
    }
}
