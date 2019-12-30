// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.concurrent;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aJ\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u001a0\u0010\u000e\u001a\u0002H\u000f\"\b\b\u0000\u0010\u000f*\u00020\u0010*\b\u0012\u0004\u0012\u0002H\u000f0\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000f0\fH\u0087\b¢\u0006\u0002\u0010\u0013¨\u0006\u0014" }, d2 = { "thread", "Ljava/lang/Thread;", "start", "", "isDaemon", "contextClassLoader", "Ljava/lang/ClassLoader;", "name", "", "priority", "", "block", "Lkotlin/Function0;", "", "getOrSet", "T", "", "Ljava/lang/ThreadLocal;", "default", "(Ljava/lang/ThreadLocal;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib" })
@JvmName(name = "ThreadsKt")
public final class ThreadsKt
{
    @NotNull
    public static final Thread thread(final boolean start, final boolean isDaemon, @Nullable final ClassLoader contextClassLoader, @Nullable final String name, final int priority, @NotNull final Function0<Unit> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        final ThreadsKt$thread$thread.ThreadsKt$thread$thread$1 thread = new ThreadsKt$thread$thread.ThreadsKt$thread$thread$1((Function0)block);
        if (isDaemon) {
            thread.setDaemon(true);
        }
        if (priority > 0) {
            thread.setPriority(priority);
        }
        if (name != null) {
            thread.setName(name);
        }
        if (contextClassLoader != null) {
            thread.setContextClassLoader(contextClassLoader);
        }
        if (start) {
            thread.start();
        }
        return (Thread)thread;
    }
    
    @InlineOnly
    private static final <T> T getOrSet(@NotNull final ThreadLocal<T> $receiver, final Function0<? extends T> default) {
        Object value;
        if ((value = $receiver.get()) == null) {
            final Object p1;
            final Object o = p1 = default.invoke();
            $receiver.set((T)p1);
            value = o;
        }
        return (T)value;
    }
}
