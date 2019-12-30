// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.concurrent;

import kotlin.PublishedApi;
import org.jetbrains.annotations.Nullable;
import java.util.Date;
import kotlin.internal.InlineOnly;
import kotlin.Unit;
import java.util.TimerTask;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import java.util.Timer;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001aJ\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001aL\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a\u001a\u0010\u0010\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001aJ\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001aL\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a$\u0010\u0011\u001a\u00020\f2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a0\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a8\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a0\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a8\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a8\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b\u001a8\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\b¨\u0006\u0016" }, d2 = { "fixedRateTimer", "Ljava/util/Timer;", "name", "", "daemon", "", "startAt", "Ljava/util/Date;", "period", "", "action", "Lkotlin/Function1;", "Ljava/util/TimerTask;", "", "Lkotlin/ExtensionFunctionType;", "initialDelay", "timer", "timerTask", "schedule", "time", "delay", "scheduleAtFixedRate", "kotlin-stdlib" })
@JvmName(name = "TimersKt")
public final class TimersKt
{
    @InlineOnly
    private static final TimerTask schedule(@NotNull final Timer $receiver, final long delay, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.schedule(task, delay);
        return task;
    }
    
    @InlineOnly
    private static final TimerTask schedule(@NotNull final Timer $receiver, final Date time, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.schedule(task, time);
        return task;
    }
    
    @InlineOnly
    private static final TimerTask schedule(@NotNull final Timer $receiver, final long delay, final long period, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.schedule(task, delay, period);
        return task;
    }
    
    @InlineOnly
    private static final TimerTask schedule(@NotNull final Timer $receiver, final Date time, final long period, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.schedule(task, time, period);
        return task;
    }
    
    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(@NotNull final Timer $receiver, final long delay, final long period, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.scheduleAtFixedRate(task, delay, period);
        return task;
    }
    
    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(@NotNull final Timer $receiver, final Date time, final long period, final Function1<? super TimerTask, Unit> action) {
        final TimerTask task = (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
        $receiver.scheduleAtFixedRate(task, time, period);
        return task;
    }
    
    @PublishedApi
    @NotNull
    public static final Timer timer(@Nullable final String name, final boolean daemon) {
        return (name == null) ? new Timer(daemon) : new Timer(name, daemon);
    }
    
    @InlineOnly
    private static final Timer timer(final String name, final boolean daemon, final long initialDelay, final long period, final Function1<? super TimerTask, Unit> action) {
        final Timer timer = timer(name, daemon);
        timer.schedule((TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action), initialDelay, period);
        return timer;
    }
    
    @InlineOnly
    private static final Timer timer(final String name, final boolean daemon, final Date startAt, final long period, final Function1<? super TimerTask, Unit> action) {
        final Timer timer = timer(name, daemon);
        timer.schedule((TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action), startAt, period);
        return timer;
    }
    
    @InlineOnly
    private static final Timer fixedRateTimer(final String name, final boolean daemon, final long initialDelay, final long period, final Function1<? super TimerTask, Unit> action) {
        final Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate((TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action), initialDelay, period);
        return timer;
    }
    
    @InlineOnly
    private static final Timer fixedRateTimer(final String name, final boolean daemon, final Date startAt, final long period, final Function1<? super TimerTask, Unit> action) {
        final Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate((TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action), startAt, period);
        return timer;
    }
    
    @InlineOnly
    private static final TimerTask timerTask(final Function1<? super TimerTask, Unit> action) {
        return (TimerTask)new TimersKt$timerTask.TimersKt$timerTask$1((Function1)action);
    }
}
