// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import org.jetbrains.annotations.Nullable;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.InlineMarker;
import java.io.Closeable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001a;\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000b\u0082\u0002\b\n\u0006\b\u0011(\n0\u0001¨\u0006\f" }, d2 = { "closeFinally", "", "Ljava/io/Closeable;", "cause", "", "use", "R", "T", "block", "Lkotlin/Function1;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib" })
@JvmName(name = "CloseableKt")
public final class CloseableKt
{
    @InlineOnly
    private static final <T extends Closeable, R> R use(final T $receiver, final Function1<? super T, ? extends R> block) {
        Throwable exception = null;
        try {
            final R invoke = (R)block.invoke($receiver);
            InlineMarker.finallyStart(1);
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                closeFinally($receiver, exception);
            }
            else if ($receiver != null) {
                $receiver.close();
            }
            InlineMarker.finallyEnd(1);
            return invoke;
        }
        catch (Throwable e) {
            exception = e;
            throw e;
        }
        finally {
            InlineMarker.finallyStart(1);
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                closeFinally($receiver, exception);
            }
            else if ($receiver != null) {
                if (exception == null) {
                    $receiver.close();
                }
                else {
                    try {
                        $receiver.close();
                    }
                    catch (Throwable t) {}
                }
            }
            InlineMarker.finallyEnd(1);
        }
    }
    
    @SinceKotlin(version = "1.1")
    @PublishedApi
    public static final void closeFinally(@Nullable final Closeable $receiver, @Nullable final Throwable cause) {
        if ($receiver != null) {
            if (cause == null) {
                $receiver.close();
            }
            else {
                try {
                    $receiver.close();
                }
                catch (Throwable closeException) {
                    ExceptionsKt__ExceptionsKt.addSuppressed(cause, closeException);
                }
            }
        }
    }
}
