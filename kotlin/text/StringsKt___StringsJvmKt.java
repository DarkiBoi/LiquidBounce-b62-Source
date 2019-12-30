// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import java.util.TreeSet;
import kotlin.jvm.internal.Intrinsics;
import java.util.SortedSet;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003¨\u0006\u0004" }, d2 = { "toSortedSet", "Ljava/util/SortedSet;", "", "", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt___StringsJvmKt extends StringsKt__StringsKt
{
    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return StringsKt___StringsKt.toCollection($receiver, new TreeSet<Character>());
    }
    
    public StringsKt___StringsJvmKt() {
    }
}
