// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.system;

import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b¨\u0006\u0004" }, d2 = { "exitProcess", "", "status", "", "kotlin-stdlib" })
@JvmName(name = "ProcessKt")
public final class ProcessKt
{
    @InlineOnly
    private static final Void exitProcess(final int status) {
        System.exit(status);
        throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
    }
}
