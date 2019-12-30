// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import java.util.Enumeration;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\u0002¨\u0006\u0004" }, d2 = { "iterator", "", "T", "Ljava/util/Enumeration;", "kotlin-stdlib" }, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__IteratorsJVMKt extends CollectionsKt__IterablesKt
{
    @NotNull
    public static final <T> Iterator<T> iterator(@NotNull final Enumeration<T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return (Iterator<T>)new CollectionsKt__IteratorsJVMKt$iterator.CollectionsKt__IteratorsJVMKt$iterator$1((Enumeration)$receiver);
    }
    
    public CollectionsKt__IteratorsJVMKt() {
    }
}
