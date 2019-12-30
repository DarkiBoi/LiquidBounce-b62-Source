// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b¨\u0006\u0003" }, d2 = { "toRegex", "Lkotlin/text/Regex;", "Ljava/util/regex/Pattern;", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt__RegexExtensionsJVMKt extends StringsKt__IndentKt
{
    @InlineOnly
    private static final Regex toRegex(@NotNull final Pattern $receiver) {
        return new Regex($receiver);
    }
    
    public StringsKt__RegexExtensionsJVMKt() {
    }
}
