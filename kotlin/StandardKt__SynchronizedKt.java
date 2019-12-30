// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function0;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a7\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\u0087\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0006¨\u0006\u0007" }, d2 = { "synchronized", "R", "lock", "", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib" }, xs = "kotlin/StandardKt")
class StandardKt__SynchronizedKt extends StandardKt__StandardKt
{
    @InlineOnly
    private static final <R> R synchronized(final Object lock, final Function0<? extends R> block) {
        // monitorenter(lock)
        try {
            return (R)block.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            // monitorexit(lock)
            InlineMarker.finallyEnd(1);
        }
    }
    
    public StandardKt__SynchronizedKt() {
    }
}
