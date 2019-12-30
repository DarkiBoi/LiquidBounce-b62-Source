// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import java.nio.charset.Charset;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b¨\u0006\u0004" }, d2 = { "charset", "Ljava/nio/charset/Charset;", "charsetName", "", "kotlin-stdlib" })
@JvmName(name = "CharsetsKt")
public final class CharsetsKt
{
    @InlineOnly
    private static final Charset charset(final String charsetName) {
        final Charset forName = Charset.forName(charsetName);
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(charsetName)");
        return forName;
    }
}
