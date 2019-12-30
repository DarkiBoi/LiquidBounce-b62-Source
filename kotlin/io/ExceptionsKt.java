// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0002¨\u0006\u0006" }, d2 = { "constructMessage", "", "file", "Ljava/io/File;", "other", "reason", "kotlin-stdlib" })
public final class ExceptionsKt
{
    private static final String constructMessage(final File file, final File other, final String reason) {
        final StringBuilder sb = new StringBuilder(file.toString());
        if (other != null) {
            sb.append(" -> " + other);
        }
        if (reason != null) {
            sb.append(": " + reason);
        }
        final String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "sb.toString()");
        return string;
    }
}
