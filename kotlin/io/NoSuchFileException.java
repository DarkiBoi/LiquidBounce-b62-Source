// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007¨\u0006\b" }, d2 = { "Lkotlin/io/NoSuchFileException;", "Lkotlin/io/FileSystemException;", "file", "Ljava/io/File;", "other", "reason", "", "(Ljava/io/File;Ljava/io/File;Ljava/lang/String;)V", "kotlin-stdlib" })
public final class NoSuchFileException extends FileSystemException
{
    public NoSuchFileException(@NotNull final File file, @Nullable final File other, @Nullable final String reason) {
        Intrinsics.checkParameterIsNotNull(file, "file");
        super(file, other, reason);
    }
}
