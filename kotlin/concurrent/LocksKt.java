// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.locks.Lock;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a&\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\u0005\u001a&\u0010\u0006\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00072\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\b\u001a&\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\u0005¨\u0006\n" }, d2 = { "read", "T", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantReadWriteLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withLock", "Ljava/util/concurrent/locks/Lock;", "(Ljava/util/concurrent/locks/Lock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "write", "kotlin-stdlib" })
@JvmName(name = "LocksKt")
public final class LocksKt
{
    @InlineOnly
    private static final <T> T withLock(@NotNull final Lock $receiver, final Function0<? extends T> action) {
        $receiver.lock();
        try {
            return (T)action.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            $receiver.unlock();
            InlineMarker.finallyEnd(1);
        }
    }
    
    @InlineOnly
    private static final <T> T read(@NotNull final ReentrantReadWriteLock $receiver, final Function0<? extends T> action) {
        final ReentrantReadWriteLock.ReadLock rl = $receiver.readLock();
        rl.lock();
        try {
            return (T)action.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            rl.unlock();
            InlineMarker.finallyEnd(1);
        }
    }
    
    @InlineOnly
    private static final <T> T write(@NotNull final ReentrantReadWriteLock $receiver, final Function0<? extends T> action) {
        final ReentrantReadWriteLock.ReadLock rl = $receiver.readLock();
        final int readCount = ($receiver.getWriteHoldCount() == 0) ? $receiver.getReadHoldCount() : 0;
        for (int i = 0; i < readCount; ++i) {
            final int it = i;
            rl.unlock();
        }
        final ReentrantReadWriteLock.WriteLock wl = $receiver.writeLock();
        wl.lock();
        try {
            return (T)action.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            for (int j = 0; j < readCount; ++j) {
                final int it2 = j;
                rl.lock();
            }
            wl.unlock();
            InlineMarker.finallyEnd(1);
        }
    }
}
