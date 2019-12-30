// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007" }, d2 = { "walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib" }, xs = "kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt extends FilesKt__FileReadWriteKt
{
    @NotNull
    public static final FileTreeWalk walk(@NotNull final File $receiver, @NotNull final FileWalkDirection direction) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(direction, "direction");
        return new FileTreeWalk($receiver, direction);
    }
    
    @NotNull
    public static final FileTreeWalk walkTopDown(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return walk($receiver, FileWalkDirection.TOP_DOWN);
    }
    
    @NotNull
    public static final FileTreeWalk walkBottomUp(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return walk($receiver, FileWalkDirection.BOTTOM_UP);
    }
    
    public FilesKt__FileTreeWalkKt() {
    }
}
