// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.coroutines.ContinuationKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a.\u0010\u0000\u001a\u00020\u00012\u001c\u0010\u0002\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0001\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007" }, d2 = { "runSuspend", "", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib" })
public final class RunSuspendKt
{
    @SinceKotlin(version = "1.3")
    public static final void runSuspend(@NotNull final Function1<? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        final RunSuspend run = new RunSuspend();
        ContinuationKt.startCoroutine((Function1<? super Continuation<? super Object>, ?>)block, (Continuation<? super Object>)run);
        run.await();
    }
}
