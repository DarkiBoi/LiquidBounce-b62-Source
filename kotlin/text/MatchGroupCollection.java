// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001J\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0005H¦\u0002¨\u0006\u0006" }, d2 = { "Lkotlin/text/MatchGroupCollection;", "", "Lkotlin/text/MatchGroup;", "get", "index", "", "kotlin-stdlib" })
public interface MatchGroupCollection extends Collection<MatchGroup>, KMappedMarker
{
    @Nullable
    MatchGroup get(final int p0);
}
