// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u0001\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\n¨\u0006\b" }, d2 = { "equals", "", "", "other", "ignoreCase", "isSurrogate", "plus", "", "kotlin-stdlib" }, xs = "kotlin/text/CharsKt")
class CharsKt__CharKt extends CharsKt__CharJVMKt
{
    @InlineOnly
    private static final String plus(final char $receiver, final String other) {
        return String.valueOf($receiver) + other;
    }
    
    public static final boolean equals(final char $receiver, final char other, final boolean ignoreCase) {
        return $receiver == other || (ignoreCase && (Character.toUpperCase($receiver) == Character.toUpperCase(other) || Character.toLowerCase($receiver) == Character.toLowerCase(other)));
    }
    
    public static final boolean isSurrogate(final char $receiver) {
        final char c = '\udfff';
        if ('\ud800' <= $receiver) {
            if (c >= $receiver) {
                return true;
            }
        }
        return false;
    }
    
    public CharsKt__CharKt() {
    }
}
