// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.collections.CharIterator;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.IntProgression;
import kotlin.collections.SlidingWindowKt;
import kotlin.sequences.Sequence;
import java.util.Comparator;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function0;
import kotlin.collections.IndexingIterable;
import kotlin.collections.IndexedValue;
import kotlin.collections.Grouping;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Pair;
import java.util.Iterator;
import kotlin.ranges.IntRange;
import kotlin.jvm.functions.Function2;
import kotlin.TypeCastException;
import kotlin.SinceKotlin;
import kotlin.random.Random;
import org.jetbrains.annotations.Nullable;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u00dc\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001a3\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b\u001aN\u0010\u001d\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\u000e\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0005\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b¢\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u001a\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010$\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\r\u0010%\u001a\u00020\"*\u00020\u0002H\u0087\b\u001a!\u0010%\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010&\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a!\u0010)\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010)\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0015\u0010+\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"H\u0087\b\u001a)\u0010-\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u001c\u0010/\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"H\u0087\b¢\u0006\u0002\u00100\u001a!\u00101\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u00101\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u00102\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000103H\u0086\b\u001a6\u00102\u001a\u00020 *\u00020 2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000103H\u0086\b\u001aQ\u00106\u001a\u0002H7\"\f\b\u0000\u00107*\u000608j\u0002`9*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000103H\u0086\b¢\u0006\u0002\u0010:\u001a!\u0010;\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010;\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a<\u0010<\u001a\u0002H7\"\f\b\u0000\u00107*\u000608j\u0002`9*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a<\u0010>\u001a\u0002H7\"\f\b\u0000\u00107*\u000608j\u0002`9*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a(\u0010?\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010@\u001a(\u0010A\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010@\u001a\n\u0010B\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010B\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010C\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010D\u001a(\u0010C\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010@\u001a3\u0010E\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b\u001aL\u0010F\u001a\u0002H7\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00107*\n\u0012\u0006\b\u0000\u0012\u0002H#0G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b¢\u0006\u0002\u0010H\u001aI\u0010I\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010J\u001a\u0002H#2'\u0010K\u001a#\u0012\u0013\u0012\u0011H#¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#03H\u0086\b¢\u0006\u0002\u0010M\u001a^\u0010N\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010J\u001a\u0002H#2<\u0010K\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0013\u0012\u0011H#¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0OH\u0086\b¢\u0006\u0002\u0010P\u001aI\u0010Q\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010J\u001a\u0002H#2'\u0010K\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u0002H#03H\u0086\b¢\u0006\u0002\u0010M\u001a^\u0010R\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010J\u001a\u0002H#2<\u0010K\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u0002H#0OH\u0086\b¢\u0006\u0002\u0010P\u001a!\u0010S\u001a\u00020T*\u00020\u00022\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020T0\u0004H\u0086\b\u001a6\u0010V\u001a\u00020T*\u00020\u00022'\u0010U\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020T03H\u0086\b\u001a)\u0010W\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0019\u0010X\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"¢\u0006\u0002\u00100\u001a9\u0010Y\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001f0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aS\u0010Y\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001f0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aR\u0010Z\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050[0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001al\u0010Z\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0[0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a5\u0010\\\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0]\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0087\b\u001a!\u0010^\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010_\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010`\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010`\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010a\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010D\u001a(\u0010a\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010@\u001a-\u0010b\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u001aB\u0010c\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#03H\u0086\b\u001aH\u0010d\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020e*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#03H\u0086\b\u001aa\u0010f\u001a\u0002H7\"\b\b\u0000\u0010#*\u00020e\"\u0010\b\u0001\u00107*\n\u0012\u0006\b\u0000\u0012\u0002H#0G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#03H\u0086\b¢\u0006\u0002\u0010g\u001a[\u0010h\u001a\u0002H7\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00107*\n\u0012\u0006\b\u0000\u0012\u0002H#0G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#03H\u0086\b¢\u0006\u0002\u0010g\u001a3\u0010i\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020e*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b\u001aL\u0010j\u001a\u0002H7\"\b\b\u0000\u0010#*\u00020e\"\u0010\b\u0001\u00107*\n\u0012\u0006\b\u0000\u0012\u0002H#0G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b¢\u0006\u0002\u0010H\u001aF\u0010k\u001a\u0002H7\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00107*\n\u0012\u0006\b\u0000\u0012\u0002H#0G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H72\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010H\u001a\u0011\u0010l\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010D\u001a8\u0010m\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0n*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010@\u001a-\u0010p\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010q\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050rj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`s¢\u0006\u0002\u0010t\u001a\u0011\u0010u\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010D\u001a8\u0010v\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0n*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010@\u001a-\u0010w\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010q\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050rj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`s¢\u0006\u0002\u0010t\u001a\n\u0010x\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010x\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a0\u0010y\u001a\u0002Hz\"\b\b\u0000\u0010z*\u00020\u0002*\u0002Hz2\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020T0\u0004H\u0087\b¢\u0006\u0002\u0010{\u001a-\u0010|\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a-\u0010|\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020 0\u0010*\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\r\u0010}\u001a\u00020\u0005*\u00020\u0002H\u0087\b\u001a\u0014\u0010}\u001a\u00020\u0005*\u00020\u00022\u0006\u0010}\u001a\u00020~H\u0007\u001a6\u0010\u007f\u001a\u00020\u0005*\u00020\u00022'\u0010K\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000503H\u0086\b\u001aL\u0010\u0080\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010K\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050OH\u0086\b\u001a7\u0010\u0081\u0001\u001a\u00020\u0005*\u00020\u00022'\u0010K\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u000503H\u0086\b\u001aL\u0010\u0082\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010K\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b4\u0012\b\b5\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020\u00050OH\u0086\b\u001a\u000b\u0010\u0083\u0001\u001a\u00020\u0002*\u00020\u0002\u001a\u000e\u0010\u0083\u0001\u001a\u00020 *\u00020 H\u0087\b\u001a\u000b\u0010\u0084\u0001\u001a\u00020\u0005*\u00020\u0002\u001a\"\u0010\u0084\u0001\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010D\u001a)\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010@\u001a\u001a\u0010\u0086\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0087\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\b\u001a\u0015\u0010\u0086\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0087\u0001\u001a\u00030\u0088\u0001\u001a\u001d\u0010\u0086\u0001\u001a\u00020 *\u00020 2\r\u0010\u0087\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\bH\u0087\b\u001a\u0015\u0010\u0086\u0001\u001a\u00020 *\u00020 2\b\u0010\u0087\u0001\u001a\u00030\u0088\u0001\u001a\"\u0010\u0089\u0001\u001a\u00020\"*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004H\u0086\b\u001a$\u0010\u008a\u0001\u001a\u00030\u008b\u0001*\u00020\u00022\u0013\u0010o\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u008b\u00010\u0004H\u0086\b\u001a\u0013\u0010\u008c\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008c\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008d\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u008d\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\"\u0010\u008e\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008e\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008f\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008f\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a+\u0010\u0090\u0001\u001a\u0002H7\"\u0010\b\u0000\u00107*\n\u0012\u0006\b\u0000\u0012\u00020\u00050G*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H7¢\u0006\u0003\u0010\u0091\u0001\u001a\u001d\u0010\u0092\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u0093\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u0094\u0001*\u00020\u0002\u001a\u0011\u0010\u0095\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u0002\u001a\u0011\u0010\u0096\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050[*\u00020\u0002\u001a\u0012\u0010\u0097\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0098\u0001*\u00020\u0002\u001a1\u0010\u0099\u0001\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009b\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0099\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009b\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a1\u0010\u009c\u0001\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009b\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u009c\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u009a\u0001\u001a\u00020\"2\t\b\u0002\u0010\u009b\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u0018\u0010\u009d\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050\u009e\u00010\b*\u00020\u0002\u001a)\u0010\u009f\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u00022\u0007\u0010 \u0001\u001a\u00020\u0002H\u0086\u0004\u001a]\u0010\u009f\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010 \u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b4\u0012\t\b5\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b4\u0012\t\b5\u0012\u0005\b\b(¢\u0001\u0012\u0004\u0012\u0002H\u000e03H\u0086\b\u001a\u001f\u0010£\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u0002H\u0007\u001aT\u0010£\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b4\u0012\t\b5\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b4\u0012\t\b5\u0012\u0005\b\b(¢\u0001\u0012\u0004\u0012\u0002H#03H\u0087\b¨\u0006¤\u0001" }, d2 = { "all", "", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", "V", "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", "destination", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "associateWith", "valueSelector", "associateWithTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAt", "index", "elementAtOrElse", "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", "S", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "random", "Lkotlin/random/Random;", "reduce", "reduceIndexed", "reduceRight", "reduceRightIndexed", "reversed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", "step", "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt___StringsKt extends StringsKt___StringsJvmKt
{
    @InlineOnly
    private static final char elementAt(@NotNull final CharSequence $receiver, final int index) {
        return $receiver.charAt(index);
    }
    
    @InlineOnly
    private static final char elementAtOrElse(@NotNull final CharSequence $receiver, final int index, final Function1<? super Integer, Character> defaultValue) {
        return (index >= 0 && index <= StringsKt__StringsKt.getLastIndex($receiver)) ? $receiver.charAt(index) : defaultValue.invoke(index);
    }
    
    @InlineOnly
    private static final Character elementAtOrNull(@NotNull final CharSequence $receiver, final int index) {
        return getOrNull($receiver, index);
    }
    
    @InlineOnly
    private static final Character find(@NotNull final CharSequence $receiver, final Function1<? super Character, Boolean> predicate) {
        final CharSequence charSequence;
        final CharSequence $receiver$iv = charSequence = $receiver;
        for (int i = 0; i < charSequence.length(); ++i) {
            final char element$iv = charSequence.charAt(i);
            if (predicate.invoke(element$iv)) {
                return element$iv;
            }
        }
        return null;
    }
    
    @InlineOnly
    private static final Character findLast(@NotNull final CharSequence $receiver, final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3        /* $receiver$iv */
        //     2: aload_3         /* $receiver$iv */
        //     3: invokeinterface java/lang/CharSequence.length:()I
        //     8: istore          4
        //    10: iinc            4, -1
        //    13: iconst_0       
        //    14: istore          5
        //    16: iload           4
        //    18: iflt            65
        //    21: aload_3         /* $receiver$iv */
        //    22: iload           index$iv
        //    24: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    29: istore          element$iv
        //    31: aload_1         /* predicate */
        //    32: iload           element$iv
        //    34: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    37: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    42: checkcast       Ljava/lang/Boolean;
        //    45: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    48: ifeq            59
        //    51: iload           element$iv
        //    53: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    56: goto            66
        //    59: iinc            index$iv, -1
        //    62: goto            16
        //    65: aconst_null    
        //    66: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/Character;
        //    StackMapTable: 00 04 FF 00 10 00 06 07 00 BB 07 00 D1 00 07 00 BB 01 01 00 00 FC 00 2A 01 FA 00 05 40 07 00 D7
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final char first(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return $receiver.charAt(0);
    }
    
    public static final char first(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                return element;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }
    
    @Nullable
    public static final Character firstOrNull(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ($receiver.length() == 0) ? null : Character.valueOf($receiver.charAt(0));
    }
    
    @Nullable
    public static final Character firstOrNull(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                return element;
            }
        }
        return null;
    }
    
    @InlineOnly
    private static final char getOrElse(@NotNull final CharSequence $receiver, final int index, final Function1<? super Integer, Character> defaultValue) {
        return (index >= 0 && index <= StringsKt__StringsKt.getLastIndex($receiver)) ? $receiver.charAt(index) : defaultValue.invoke(index);
    }
    
    @Nullable
    public static final Character getOrNull(@NotNull final CharSequence $receiver, final int index) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return (index >= 0 && index <= StringsKt__StringsKt.getLastIndex($receiver)) ? Character.valueOf($receiver.charAt(index)) : null;
    }
    
    public static final int indexOfFirst(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: iconst_0       
        //    14: istore_3       
        //    15: aload_0         /* $receiver */
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: istore          4
        //    23: iload_3        
        //    24: iload           4
        //    26: if_icmpge       62
        //    29: aload_1         /* predicate */
        //    30: aload_0         /* $receiver */
        //    31: iload_3         /* index */
        //    32: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    37: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    40: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    45: checkcast       Ljava/lang/Boolean;
        //    48: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    51: ifeq            56
        //    54: iload_3         /* index */
        //    55: ireturn        
        //    56: iinc            index, 1
        //    59: goto            23
        //    62: iconst_m1      
        //    63: ireturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)I
        //    StackMapTable: 00 03 FE 00 17 00 01 01 20 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final int indexOfLast(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: istore_3       
        //    20: iinc            3, -1
        //    23: iconst_0       
        //    24: istore          4
        //    26: iload_3        
        //    27: iflt            63
        //    30: aload_1         /* predicate */
        //    31: aload_0         /* $receiver */
        //    32: iload_3         /* index */
        //    33: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    38: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    41: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    46: checkcast       Ljava/lang/Boolean;
        //    49: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    52: ifeq            57
        //    55: iload_3         /* index */
        //    56: ireturn        
        //    57: iinc            index, -1
        //    60: goto            26
        //    63: iconst_m1      
        //    64: ireturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)I
        //    StackMapTable: 00 03 FE 00 1A 00 01 01 1E 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final char last(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return $receiver.charAt(StringsKt__StringsKt.getLastIndex($receiver));
    }
    
    public static final char last(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: istore_3       
        //    20: iinc            3, -1
        //    23: iconst_0       
        //    24: istore          4
        //    26: iload_3        
        //    27: iflt            68
        //    30: aload_0         /* $receiver */
        //    31: iload_3         /* index */
        //    32: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    37: istore          element
        //    39: aload_1         /* predicate */
        //    40: iload           element
        //    42: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    45: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    50: checkcast       Ljava/lang/Boolean;
        //    53: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    56: ifeq            62
        //    59: iload           element
        //    61: ireturn        
        //    62: iinc            index, -1
        //    65: goto            26
        //    68: new             Ljava/util/NoSuchElementException;
        //    71: dup            
        //    72: ldc_w           "Char sequence contains no character matching the predicate."
        //    75: invokespecial   java/util/NoSuchElementException.<init>:(Ljava/lang/String;)V
        //    78: checkcast       Ljava/lang/Throwable;
        //    81: athrow         
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)C
        //    StackMapTable: 00 03 FE 00 1A 00 01 01 FC 00 23 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final Character lastOrNull(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ($receiver.length() == 0) ? null : Character.valueOf($receiver.charAt($receiver.length() - 1));
    }
    
    @Nullable
    public static final Character lastOrNull(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: istore_3       
        //    20: iinc            3, -1
        //    23: iconst_0       
        //    24: istore          4
        //    26: iload_3        
        //    27: iflt            71
        //    30: aload_0         /* $receiver */
        //    31: iload_3         /* index */
        //    32: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    37: istore          element
        //    39: aload_1         /* predicate */
        //    40: iload           element
        //    42: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    45: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    50: checkcast       Ljava/lang/Boolean;
        //    53: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    56: ifeq            65
        //    59: iload           element
        //    61: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    64: areturn        
        //    65: iinc            index, -1
        //    68: goto            26
        //    71: aconst_null    
        //    72: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/Character;
        //    StackMapTable: 00 03 FE 00 1A 00 01 01 FC 00 26 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final char random(@NotNull final CharSequence $receiver) {
        return random($receiver, Random.Default);
    }
    
    @SinceKotlin(version = "1.3")
    public static final char random(@NotNull final CharSequence $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if ($receiver.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return $receiver.charAt(random.nextInt($receiver.length()));
    }
    
    public static final char single(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        switch ($receiver.length()) {
            case 0: {
                throw new NoSuchElementException("Char sequence is empty.");
            }
            case 1: {
                return $receiver.charAt(0);
            }
            default: {
                throw new IllegalArgumentException("Char sequence has more than one element.");
            }
        }
    }
    
    public static final char single(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                if (found) {
                    throw new IllegalArgumentException("Char sequence contains more than one matching element.");
                }
                single = element;
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
        }
        final Character c = single;
        if (c == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
        }
        return c;
    }
    
    @Nullable
    public static final Character singleOrNull(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ($receiver.length() == 1) ? Character.valueOf($receiver.charAt(0)) : null;
    }
    
    @Nullable
    public static final Character singleOrNull(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                if (found) {
                    return null;
                }
                single = element;
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        return single;
    }
    
    @NotNull
    public static final CharSequence drop(@NotNull final CharSequence $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        return $receiver.subSequence(RangesKt___RangesKt.coerceAtMost(n, $receiver.length()), $receiver.length());
    }
    
    @NotNull
    public static final String drop(@NotNull final String $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        final String substring = $receiver.substring(RangesKt___RangesKt.coerceAtMost(n, $receiver.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence dropLast(@NotNull final CharSequence $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        return take($receiver, RangesKt___RangesKt.coerceAtLeast($receiver.length() - n, 0));
    }
    
    @NotNull
    public static final String dropLast(@NotNull final String $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        return take($receiver, RangesKt___RangesKt.coerceAtLeast($receiver.length() - n, 0));
    }
    
    @NotNull
    public static final CharSequence dropLastWhile(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    17: istore_3       
        //    18: iconst_0       
        //    19: istore          4
        //    21: iload_3        
        //    22: iflt            67
        //    25: aload_1         /* predicate */
        //    26: aload_0         /* $receiver */
        //    27: iload_3         /* index */
        //    28: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    33: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    36: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    41: checkcast       Ljava/lang/Boolean;
        //    44: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    47: ifne            61
        //    50: aload_0         /* $receiver */
        //    51: iconst_0       
        //    52: iload_3         /* index */
        //    53: iconst_1       
        //    54: iadd           
        //    55: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    60: areturn        
        //    61: iinc            index, -1
        //    64: goto            21
        //    67: ldc_w           ""
        //    70: checkcast       Ljava/lang/CharSequence;
        //    73: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FE 00 15 00 01 01 27 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String dropLastWhile(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    20: istore_3       
        //    21: iconst_0       
        //    22: istore          4
        //    24: iload_3        
        //    25: iflt            85
        //    28: aload_1         /* predicate */
        //    29: aload_0         /* $receiver */
        //    30: iload_3         /* index */
        //    31: invokevirtual   java/lang/String.charAt:(I)C
        //    34: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    37: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    42: checkcast       Ljava/lang/Boolean;
        //    45: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    48: ifne            79
        //    51: aload_0         /* $receiver */
        //    52: astore          5
        //    54: iconst_0       
        //    55: istore          6
        //    57: iload_3         /* index */
        //    58: iconst_1       
        //    59: iadd           
        //    60: istore          7
        //    62: aload           5
        //    64: iload           6
        //    66: iload           7
        //    68: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    71: dup            
        //    72: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    75: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    78: areturn        
        //    79: iinc            index, -1
        //    82: goto            24
        //    85: ldc_w           ""
        //    88: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FE 00 18 00 01 01 36 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence dropWhile(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: iconst_0       
        //    14: istore_3       
        //    15: aload_0         /* $receiver */
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: istore          4
        //    23: iload_3        
        //    24: iload           4
        //    26: if_icmpge       74
        //    29: aload_1         /* predicate */
        //    30: aload_0         /* $receiver */
        //    31: iload_3         /* index */
        //    32: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    37: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    40: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    45: checkcast       Ljava/lang/Boolean;
        //    48: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    51: ifne            68
        //    54: aload_0         /* $receiver */
        //    55: iload_3         /* index */
        //    56: aload_0         /* $receiver */
        //    57: invokeinterface java/lang/CharSequence.length:()I
        //    62: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    67: areturn        
        //    68: iinc            index, 1
        //    71: goto            23
        //    74: ldc_w           ""
        //    77: checkcast       Ljava/lang/CharSequence;
        //    80: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FE 00 17 00 01 01 2C 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String dropWhile(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: iconst_0       
        //    14: istore_3       
        //    15: aload_0         /* $receiver */
        //    16: checkcast       Ljava/lang/CharSequence;
        //    19: invokeinterface java/lang/CharSequence.length:()I
        //    24: istore          4
        //    26: iload_3        
        //    27: iload           4
        //    29: if_icmpge       78
        //    32: aload_1         /* predicate */
        //    33: aload_0         /* $receiver */
        //    34: iload_3         /* index */
        //    35: invokevirtual   java/lang/String.charAt:(I)C
        //    38: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    41: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    46: checkcast       Ljava/lang/Boolean;
        //    49: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    52: ifne            72
        //    55: aload_0         /* $receiver */
        //    56: astore          5
        //    58: aload           5
        //    60: iload_3         /* index */
        //    61: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    64: dup            
        //    65: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //    68: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    71: areturn        
        //    72: iinc            index, 1
        //    75: goto            26
        //    78: ldc_w           ""
        //    81: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FE 00 1A 00 01 01 2D 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence filter(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/lang/StringBuilder;
        //    18: dup            
        //    19: invokespecial   java/lang/StringBuilder.<init>:()V
        //    22: checkcast       Ljava/lang/Appendable;
        //    25: astore          destination$iv
        //    27: iconst_0       
        //    28: istore          5
        //    30: aload_3         /* $receiver$iv */
        //    31: invokeinterface java/lang/CharSequence.length:()I
        //    36: istore          6
        //    38: iload           5
        //    40: iload           6
        //    42: if_icmpge       91
        //    45: aload_3         /* $receiver$iv */
        //    46: iload           index$iv
        //    48: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    53: istore          element$iv
        //    55: aload_1         /* predicate */
        //    56: iload           element$iv
        //    58: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    61: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    66: checkcast       Ljava/lang/Boolean;
        //    69: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    72: ifeq            85
        //    75: aload           destination$iv
        //    77: iload           element$iv
        //    79: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //    84: pop            
        //    85: iinc            index$iv, 1
        //    88: goto            38
        //    91: aload           destination$iv
        //    93: checkcast       Ljava/lang/CharSequence;
        //    96: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FF 00 26 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 01 7B 01 01 00 00 FC 00 2E 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String filter(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: astore_3       
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: checkcast       Ljava/lang/Appendable;
        //    28: astore          destination$iv
        //    30: iconst_0       
        //    31: istore          5
        //    33: aload_3         /* $receiver$iv */
        //    34: invokeinterface java/lang/CharSequence.length:()I
        //    39: istore          6
        //    41: iload           5
        //    43: iload           6
        //    45: if_icmpge       94
        //    48: aload_3         /* $receiver$iv */
        //    49: iload           index$iv
        //    51: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    56: istore          element$iv
        //    58: aload_1         /* predicate */
        //    59: iload           element$iv
        //    61: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    64: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    69: checkcast       Ljava/lang/Boolean;
        //    72: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    75: ifeq            88
        //    78: aload           destination$iv
        //    80: iload           element$iv
        //    82: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //    87: pop            
        //    88: iinc            index$iv, 1
        //    91: goto            41
        //    94: aload           destination$iv
        //    96: checkcast       Ljava/lang/StringBuilder;
        //    99: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   102: dup            
        //   103: ldc_w           "filterTo(StringBuilder(), predicate).toString()"
        //   106: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   109: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FF 00 29 00 07 07 01 5C 07 00 D1 00 07 00 BB 07 01 7B 01 01 00 00 FC 00 2E 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence filterIndexed(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Integer, ? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/lang/StringBuilder;
        //    18: dup            
        //    19: invokespecial   java/lang/StringBuilder.<init>:()V
        //    22: checkcast       Ljava/lang/Appendable;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          $receiver$iv$iv
        //    30: iconst_0       
        //    31: istore          index$iv$iv
        //    33: aload           $receiver$iv$iv
        //    35: astore          7
        //    37: iconst_0       
        //    38: istore          8
        //    40: iload           8
        //    42: aload           7
        //    44: invokeinterface java/lang/CharSequence.length:()I
        //    49: if_icmpge       115
        //    52: aload           7
        //    54: iload           8
        //    56: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    61: istore          item$iv$iv
        //    63: iload           index$iv$iv
        //    65: iinc            index$iv$iv, 1
        //    68: iload           item$iv$iv
        //    70: istore          10
        //    72: istore          index$iv
        //    74: aload_1         /* predicate */
        //    75: iload           index$iv
        //    77: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    80: iload           element$iv
        //    82: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    85: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    90: checkcast       Ljava/lang/Boolean;
        //    93: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    96: ifeq            109
        //    99: aload           destination$iv
        //   101: iload           element$iv
        //   103: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //   108: pop            
        //   109: iinc            8, 1
        //   112: goto            40
        //   115: nop            
        //   116: aload           destination$iv
        //   118: checkcast       Ljava/lang/CharSequence;
        //   121: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FF 00 28 00 09 07 00 BB 07 01 86 00 07 00 BB 07 01 7B 07 00 BB 01 07 00 BB 01 00 00 FE 00 44 01 01 01 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String filterIndexed(@NotNull final String $receiver, @NotNull final Function2<? super Integer, ? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: astore_3       
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: checkcast       Ljava/lang/Appendable;
        //    28: astore          destination$iv
        //    30: aload_3         /* $receiver$iv */
        //    31: astore          $receiver$iv$iv
        //    33: iconst_0       
        //    34: istore          index$iv$iv
        //    36: aload           $receiver$iv$iv
        //    38: astore          7
        //    40: iconst_0       
        //    41: istore          8
        //    43: iload           8
        //    45: aload           7
        //    47: invokeinterface java/lang/CharSequence.length:()I
        //    52: if_icmpge       118
        //    55: aload           7
        //    57: iload           8
        //    59: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    64: istore          item$iv$iv
        //    66: iload           index$iv$iv
        //    68: iinc            index$iv$iv, 1
        //    71: iload           item$iv$iv
        //    73: istore          10
        //    75: istore          index$iv
        //    77: aload_1         /* predicate */
        //    78: iload           index$iv
        //    80: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    83: iload           element$iv
        //    85: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    88: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    93: checkcast       Ljava/lang/Boolean;
        //    96: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    99: ifeq            112
        //   102: aload           destination$iv
        //   104: iload           element$iv
        //   106: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //   111: pop            
        //   112: iinc            8, 1
        //   115: goto            43
        //   118: nop            
        //   119: aload           destination$iv
        //   121: checkcast       Ljava/lang/StringBuilder;
        //   124: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   127: dup            
        //   128: ldc_w           "filterIndexedTo(StringBu\u2026(), predicate).toString()"
        //   131: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   134: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FF 00 2B 00 09 07 01 5C 07 01 86 00 07 00 BB 07 01 7B 07 00 BB 01 07 00 BB 01 00 00 FE 00 44 01 01 01 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <C extends Appendable> C filterIndexedTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function2<? super Integer, ? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc_w           "destination"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* predicate */
        //    14: ldc_w           "predicate"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_0         /* $receiver */
        //    21: astore          $receiver$iv
        //    23: iconst_0       
        //    24: istore          index$iv
        //    26: aload           $receiver$iv
        //    28: astore          6
        //    30: iconst_0       
        //    31: istore          7
        //    33: iload           7
        //    35: aload           6
        //    37: invokeinterface java/lang/CharSequence.length:()I
        //    42: if_icmpge       107
        //    45: aload           6
        //    47: iload           7
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          item$iv
        //    56: iload           index$iv
        //    58: iinc            index$iv, 1
        //    61: iload           item$iv
        //    63: istore          9
        //    65: istore          index
        //    67: aload_2         /* predicate */
        //    68: iload           index
        //    70: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    73: iload           element
        //    75: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    78: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    83: checkcast       Ljava/lang/Boolean;
        //    86: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    89: ifeq            101
        //    92: aload_1         /* destination */
        //    93: iload           element
        //    95: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //   100: pop            
        //   101: iinc            7, 1
        //   104: goto            33
        //   107: nop            
        //   108: aload_1         /* destination */
        //   109: areturn        
        //    Signature:
        //  <C::Ljava/lang/Appendable;>(Ljava/lang/CharSequence;TC;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;Ljava/lang/Boolean;>;)TC;
        //    StackMapTable: 00 03 FF 00 21 00 08 07 00 BB 07 01 7B 07 01 86 00 07 00 BB 01 07 00 BB 01 00 00 FE 00 43 01 01 01 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence filterNot(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/lang/StringBuilder;
        //    18: dup            
        //    19: invokespecial   java/lang/StringBuilder.<init>:()V
        //    22: checkcast       Ljava/lang/Appendable;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          5
        //    30: iconst_0       
        //    31: istore          6
        //    33: iload           6
        //    35: aload           5
        //    37: invokeinterface java/lang/CharSequence.length:()I
        //    42: if_icmpge       92
        //    45: aload           5
        //    47: iload           6
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          element$iv
        //    56: aload_1         /* predicate */
        //    57: iload           element$iv
        //    59: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    62: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    67: checkcast       Ljava/lang/Boolean;
        //    70: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    73: ifne            86
        //    76: aload           destination$iv
        //    78: iload           element$iv
        //    80: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //    85: pop            
        //    86: iinc            6, 1
        //    89: goto            33
        //    92: aload           destination$iv
        //    94: checkcast       Ljava/lang/CharSequence;
        //    97: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FF 00 21 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 01 7B 07 00 BB 01 00 00 FC 00 34 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String filterNot(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: astore_3       
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: checkcast       Ljava/lang/Appendable;
        //    28: astore          destination$iv
        //    30: aload_3         /* $receiver$iv */
        //    31: astore          5
        //    33: iconst_0       
        //    34: istore          6
        //    36: iload           6
        //    38: aload           5
        //    40: invokeinterface java/lang/CharSequence.length:()I
        //    45: if_icmpge       95
        //    48: aload           5
        //    50: iload           6
        //    52: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    57: istore          element$iv
        //    59: aload_1         /* predicate */
        //    60: iload           element$iv
        //    62: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    65: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    70: checkcast       Ljava/lang/Boolean;
        //    73: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    76: ifne            89
        //    79: aload           destination$iv
        //    81: iload           element$iv
        //    83: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //    88: pop            
        //    89: iinc            6, 1
        //    92: goto            36
        //    95: aload           destination$iv
        //    97: checkcast       Ljava/lang/StringBuilder;
        //   100: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   103: dup            
        //   104: ldc_w           "filterNotTo(StringBuilder(), predicate).toString()"
        //   107: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   110: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FF 00 24 00 07 07 01 5C 07 00 D1 00 07 00 BB 07 01 7B 07 00 BB 01 00 00 FC 00 34 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <C extends Appendable> C filterNotTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (!predicate.invoke(element)) {
                destination.append(element);
            }
        }
        return destination;
    }
    
    @NotNull
    public static final <C extends Appendable> C filterTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc_w           "destination"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* predicate */
        //    14: ldc_w           "predicate"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: iconst_0       
        //    21: istore          4
        //    23: aload_0         /* $receiver */
        //    24: invokeinterface java/lang/CharSequence.length:()I
        //    29: istore          5
        //    31: iload           4
        //    33: iload           5
        //    35: if_icmpge       83
        //    38: aload_0         /* $receiver */
        //    39: iload           index
        //    41: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    46: istore          element
        //    48: aload_2         /* predicate */
        //    49: iload           element
        //    51: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    54: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    59: checkcast       Ljava/lang/Boolean;
        //    62: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    65: ifeq            77
        //    68: aload_1         /* destination */
        //    69: iload           element
        //    71: invokeinterface java/lang/Appendable.append:(C)Ljava/lang/Appendable;
        //    76: pop            
        //    77: iinc            index, 1
        //    80: goto            31
        //    83: aload_1         /* destination */
        //    84: areturn        
        //    Signature:
        //  <C::Ljava/lang/Appendable;>(Ljava/lang/CharSequence;TC;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)TC;
        //    StackMapTable: 00 03 FE 00 1F 00 01 01 FC 00 2D 01 FA 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence slice(@NotNull final CharSequence $receiver, @NotNull final IntRange indices) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt__StringsKt.subSequence($receiver, indices);
    }
    
    @NotNull
    public static final String slice(@NotNull final String $receiver, @NotNull final IntRange indices) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt__StringsKt.substring($receiver, indices);
    }
    
    @NotNull
    public static final CharSequence slice(@NotNull final CharSequence $receiver, @NotNull final Iterable<Integer> indices) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        final int size = CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)indices, 10);
        if (size == 0) {
            return "";
        }
        final StringBuilder result = new StringBuilder(size);
        final Iterator<? extends T> iterator = indices.iterator();
        while (iterator.hasNext()) {
            final int i = ((Number)iterator.next()).intValue();
            result.append($receiver.charAt(i));
        }
        return result;
    }
    
    @InlineOnly
    private static final String slice(@NotNull final String $receiver, final Iterable<Integer> indices) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return slice((CharSequence)$receiver, indices).toString();
    }
    
    @NotNull
    public static final CharSequence take(@NotNull final CharSequence $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        return $receiver.subSequence(0, RangesKt___RangesKt.coerceAtMost(n, $receiver.length()));
    }
    
    @NotNull
    public static final String take(@NotNull final String $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        final String substring = $receiver.substring(0, RangesKt___RangesKt.coerceAtMost(n, $receiver.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence takeLast(@NotNull final CharSequence $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        final int length = $receiver.length();
        return $receiver.subSequence(length - RangesKt___RangesKt.coerceAtMost(n, length), length);
    }
    
    @NotNull
    public static final String takeLast(@NotNull final String $receiver, final int n) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (n < 0) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        final int length = $receiver.length();
        final String substring = $receiver.substring(length - RangesKt___RangesKt.coerceAtMost(n, length));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence takeLastWhile(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    17: istore_3       
        //    18: iconst_0       
        //    19: istore          4
        //    21: iload_3        
        //    22: iflt            72
        //    25: aload_1         /* predicate */
        //    26: aload_0         /* $receiver */
        //    27: iload_3         /* index */
        //    28: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    33: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    36: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    41: checkcast       Ljava/lang/Boolean;
        //    44: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    47: ifne            66
        //    50: aload_0         /* $receiver */
        //    51: iload_3         /* index */
        //    52: iconst_1       
        //    53: iadd           
        //    54: aload_0         /* $receiver */
        //    55: invokeinterface java/lang/CharSequence.length:()I
        //    60: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    65: areturn        
        //    66: iinc            index, -1
        //    69: goto            21
        //    72: aload_0         /* $receiver */
        //    73: iconst_0       
        //    74: aload_0         /* $receiver */
        //    75: invokeinterface java/lang/CharSequence.length:()I
        //    80: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    85: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FE 00 15 00 01 01 2C 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String takeLastWhile(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    20: istore_3       
        //    21: iconst_0       
        //    22: istore          4
        //    24: iload_3        
        //    25: iflt            80
        //    28: aload_1         /* predicate */
        //    29: aload_0         /* $receiver */
        //    30: iload_3         /* index */
        //    31: invokevirtual   java/lang/String.charAt:(I)C
        //    34: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    37: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    42: checkcast       Ljava/lang/Boolean;
        //    45: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    48: ifne            74
        //    51: aload_0         /* $receiver */
        //    52: astore          5
        //    54: iload_3         /* index */
        //    55: iconst_1       
        //    56: iadd           
        //    57: istore          6
        //    59: aload           5
        //    61: iload           6
        //    63: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    66: dup            
        //    67: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //    70: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    73: areturn        
        //    74: iinc            index, -1
        //    77: goto            24
        //    80: aload_0         /* $receiver */
        //    81: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FE 00 18 00 01 01 31 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence takeWhile(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: iconst_0       
        //    14: istore_3       
        //    15: aload_0         /* $receiver */
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: istore          4
        //    23: iload_3        
        //    24: iload           4
        //    26: if_icmpge       69
        //    29: aload_1         /* predicate */
        //    30: aload_0         /* $receiver */
        //    31: iload_3         /* index */
        //    32: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    37: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    40: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    45: checkcast       Ljava/lang/Boolean;
        //    48: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    51: ifne            63
        //    54: aload_0         /* $receiver */
        //    55: iconst_0       
        //    56: iload_3         /* index */
        //    57: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    62: areturn        
        //    63: iinc            index, 1
        //    66: goto            23
        //    69: aload_0         /* $receiver */
        //    70: iconst_0       
        //    71: aload_0         /* $receiver */
        //    72: invokeinterface java/lang/CharSequence.length:()I
        //    77: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //    82: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/CharSequence;
        //    StackMapTable: 00 03 FE 00 17 00 01 01 27 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String takeWhile(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* predicate */
        //     7: ldc_w           "predicate"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: iconst_0       
        //    14: istore_3       
        //    15: aload_0         /* $receiver */
        //    16: invokevirtual   java/lang/String.length:()I
        //    19: istore          4
        //    21: iload_3        
        //    22: iload           4
        //    24: if_icmpge       78
        //    27: aload_1         /* predicate */
        //    28: aload_0         /* $receiver */
        //    29: iload_3         /* index */
        //    30: invokevirtual   java/lang/String.charAt:(I)C
        //    33: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    36: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    41: checkcast       Ljava/lang/Boolean;
        //    44: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    47: ifne            72
        //    50: aload_0         /* $receiver */
        //    51: astore          5
        //    53: iconst_0       
        //    54: istore          6
        //    56: aload           5
        //    58: iload           6
        //    60: iload_3         /* index */
        //    61: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    64: dup            
        //    65: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    68: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    71: areturn        
        //    72: iinc            index, 1
        //    75: goto            21
        //    78: aload_0         /* $receiver */
        //    79: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;Ljava/lang/Boolean;>;)Ljava/lang/String;
        //    StackMapTable: 00 03 FE 00 15 00 01 01 32 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final CharSequence reversed(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final StringBuilder reverse = new StringBuilder($receiver).reverse();
        Intrinsics.checkExpressionValueIsNotNull(reverse, "StringBuilder(this).reverse()");
        return reverse;
    }
    
    @InlineOnly
    private static final String reversed(@NotNull final String $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return reversed((CharSequence)$receiver).toString();
    }
    
    @NotNull
    public static final <K, V> Map<K, V> associate(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: invokestatic    kotlin/collections/MapsKt.mapCapacity:(I)I
        //    22: bipush          16
        //    24: invokestatic    kotlin/ranges/RangesKt.coerceAtLeast:(II)I
        //    27: istore_3        /* capacity */
        //    28: aload_0         /* $receiver */
        //    29: astore          4
        //    31: new             Ljava/util/LinkedHashMap;
        //    34: dup            
        //    35: iload_3         /* capacity */
        //    36: invokespecial   java/util/LinkedHashMap.<init>:(I)V
        //    39: checkcast       Ljava/util/Map;
        //    42: astore          destination$iv
        //    44: aload           $receiver$iv
        //    46: astore          6
        //    48: iconst_0       
        //    49: istore          7
        //    51: iload           7
        //    53: aload           6
        //    55: invokeinterface java/lang/CharSequence.length:()I
        //    60: if_icmpge       118
        //    63: aload           6
        //    65: iload           7
        //    67: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    72: istore          element$iv
        //    74: aload           destination$iv
        //    76: astore          9
        //    78: aload_1         /* transform */
        //    79: iload           element$iv
        //    81: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    84: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    89: checkcast       Lkotlin/Pair;
        //    92: astore          10
        //    94: aload           9
        //    96: aload           10
        //    98: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   101: aload           10
        //   103: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   106: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   111: pop            
        //   112: iinc            7, 1
        //   115: goto            51
        //   118: aload           destination$iv
        //   120: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+Lkotlin/Pair<+TK;+TV;>;>;)Ljava/util/Map<TK;TV;>;
        //    StackMapTable: 00 02 FF 00 33 00 08 07 00 BB 07 00 D1 00 01 07 00 BB 07 01 ED 07 00 BB 01 00 00 FB 00 42
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K> Map<K, Character> associateBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* keySelector */
        //     7: ldc_w           "keySelector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: invokestatic    kotlin/collections/MapsKt.mapCapacity:(I)I
        //    22: bipush          16
        //    24: invokestatic    kotlin/ranges/RangesKt.coerceAtLeast:(II)I
        //    27: istore_3        /* capacity */
        //    28: aload_0         /* $receiver */
        //    29: astore          4
        //    31: new             Ljava/util/LinkedHashMap;
        //    34: dup            
        //    35: iload_3         /* capacity */
        //    36: invokespecial   java/util/LinkedHashMap.<init>:(I)V
        //    39: checkcast       Ljava/util/Map;
        //    42: astore          destination$iv
        //    44: aload           $receiver$iv
        //    46: astore          6
        //    48: iconst_0       
        //    49: istore          7
        //    51: iload           7
        //    53: aload           6
        //    55: invokeinterface java/lang/CharSequence.length:()I
        //    60: if_icmpge       104
        //    63: aload           6
        //    65: iload           7
        //    67: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    72: istore          element$iv
        //    74: aload           destination$iv
        //    76: aload_1         /* keySelector */
        //    77: iload           element$iv
        //    79: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    82: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    87: iload           element$iv
        //    89: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    92: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    97: pop            
        //    98: iinc            7, 1
        //   101: goto            51
        //   104: aload           destination$iv
        //   106: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TK;>;)Ljava/util/Map<TK;Ljava/lang/Character;>;
        //    StackMapTable: 00 02 FF 00 33 00 08 07 00 BB 07 00 D1 00 01 07 00 BB 07 01 ED 07 00 BB 01 00 00 34
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, V> Map<K, V> associateBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends K> keySelector, @NotNull final Function1<? super Character, ? extends V> valueTransform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* keySelector */
        //     7: ldc_w           "keySelector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* valueTransform */
        //    14: ldc_w           "valueTransform"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_0         /* $receiver */
        //    21: invokeinterface java/lang/CharSequence.length:()I
        //    26: invokestatic    kotlin/collections/MapsKt.mapCapacity:(I)I
        //    29: bipush          16
        //    31: invokestatic    kotlin/ranges/RangesKt.coerceAtLeast:(II)I
        //    34: istore          capacity
        //    36: aload_0         /* $receiver */
        //    37: astore          5
        //    39: new             Ljava/util/LinkedHashMap;
        //    42: dup            
        //    43: iload           capacity
        //    45: invokespecial   java/util/LinkedHashMap.<init>:(I)V
        //    48: checkcast       Ljava/util/Map;
        //    51: astore          destination$iv
        //    53: aload           $receiver$iv
        //    55: astore          7
        //    57: iconst_0       
        //    58: istore          8
        //    60: iload           8
        //    62: aload           7
        //    64: invokeinterface java/lang/CharSequence.length:()I
        //    69: if_icmpge       119
        //    72: aload           7
        //    74: iload           8
        //    76: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    81: istore          element$iv
        //    83: aload           destination$iv
        //    85: aload_1         /* keySelector */
        //    86: iload           element$iv
        //    88: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    91: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    96: aload_2         /* valueTransform */
        //    97: iload           element$iv
        //    99: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   102: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   107: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   112: pop            
        //   113: iinc            8, 1
        //   116: goto            60
        //   119: aload           destination$iv
        //   121: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TK;>;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TV;>;)Ljava/util/Map<TK;TV;>;
        //    StackMapTable: 00 02 FF 00 3C 00 09 07 00 BB 07 00 D1 07 00 D1 00 01 07 00 BB 07 01 ED 07 00 BB 01 00 00 3A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            ((Map<? super K, Character>)destination).put((Object)keySelector.invoke(element), Character.valueOf(element));
        }
        return destination;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends K> keySelector, @NotNull final Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            destination.put((Object)keySelector.invoke(element), (Object)valueTransform.invoke(element));
        }
        return destination;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            final Pair pair = (Pair)transform.invoke(element);
            destination.put(pair.getFirst(), (Object)pair.getSecond());
        }
        return destination;
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <V> Map<Character, V> associateWith(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends V> valueSelector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        final LinkedHashMap result = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsKt.mapCapacity($receiver.length()), 16));
        final CharSequence charSequence;
        final CharSequence $receiver$iv = charSequence = $receiver;
        for (int i = 0; i < charSequence.length(); ++i) {
            final char element$iv = charSequence.charAt(i);
            result.put(element$iv, valueSelector.invoke(element$iv));
        }
        return (Map<Character, V>)result;
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <V, M extends Map<? super Character, ? super V>> M associateWithTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends V> valueSelector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            ((Map<Character, ? super V>)destination).put(Character.valueOf(element), (Object)valueSelector.invoke(element));
        }
        return destination;
    }
    
    @NotNull
    public static final <C extends Collection<? super Character>> C toCollection(@NotNull final CharSequence $receiver, @NotNull final C destination) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char item = $receiver.charAt(i);
            ((Collection<Character>)destination).add(item);
        }
        return destination;
    }
    
    @NotNull
    public static final HashSet<Character> toHashSet(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toCollection($receiver, (HashSet<Character>)new HashSet<Object>(MapsKt__MapsKt.mapCapacity($receiver.length())));
    }
    
    @NotNull
    public static final List<Character> toList(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        List<Character> list = null;
        switch ($receiver.length()) {
            case 0: {
                list = CollectionsKt__CollectionsKt.emptyList();
                break;
            }
            case 1: {
                list = CollectionsKt__CollectionsJVMKt.listOf($receiver.charAt(0));
                break;
            }
            default: {
                list = toMutableList($receiver);
                break;
            }
        }
        return list;
    }
    
    @NotNull
    public static final List<Character> toMutableList(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toCollection($receiver, (List<Character>)new ArrayList<Object>($receiver.length()));
    }
    
    @NotNull
    public static final Set<Character> toSet(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Set<Character> set = null;
        switch ($receiver.length()) {
            case 0: {
                set = SetsKt__SetsKt.emptySet();
                break;
            }
            case 1: {
                set = SetsKt__SetsJVMKt.setOf($receiver.charAt(0));
                break;
            }
            default: {
                set = toCollection($receiver, (LinkedHashSet<Character>)new LinkedHashSet<Object>(MapsKt__MapsKt.mapCapacity($receiver.length())));
                break;
            }
        }
        return set;
    }
    
    @NotNull
    public static final <R> List<R> flatMap(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: invokespecial   java/util/ArrayList.<init>:()V
        //    22: checkcast       Ljava/util/Collection;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          5
        //    30: iconst_0       
        //    31: istore          6
        //    33: iload           6
        //    35: aload           5
        //    37: invokeinterface java/lang/CharSequence.length:()I
        //    42: if_icmpge       86
        //    45: aload           5
        //    47: iload           6
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          element$iv
        //    56: aload_1         /* transform */
        //    57: iload           element$iv
        //    59: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    62: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    67: checkcast       Ljava/lang/Iterable;
        //    70: astore          list$iv
        //    72: aload           destination$iv
        //    74: aload           list$iv
        //    76: invokestatic    kotlin/collections/CollectionsKt.addAll:(Ljava/util/Collection;Ljava/lang/Iterable;)Z
        //    79: pop            
        //    80: iinc            6, 1
        //    83: goto            33
        //    86: aload           destination$iv
        //    88: checkcast       Ljava/util/List;
        //    91: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+Ljava/lang/Iterable<+TR;>;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 02 FF 00 21 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 02 07 07 00 BB 01 00 00 34
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C flatMapTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            final Iterable list = (Iterable)transform.invoke(element);
            CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)destination, (Iterable<?>)list);
        }
        return destination;
    }
    
    @NotNull
    public static final <K> Map<K, List<Character>> groupBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* keySelector */
        //     7: ldc_w           "keySelector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/LinkedHashMap;
        //    18: dup            
        //    19: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    22: checkcast       Ljava/util/Map;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          5
        //    30: iconst_0       
        //    31: istore          6
        //    33: iload           6
        //    35: aload           5
        //    37: invokeinterface java/lang/CharSequence.length:()I
        //    42: if_icmpge       143
        //    45: aload           5
        //    47: iload           6
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          element$iv
        //    56: aload_1         /* keySelector */
        //    57: iload           element$iv
        //    59: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    62: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    67: astore          key$iv
        //    69: aload           destination$iv
        //    71: astore          $receiver$iv$iv
        //    73: aload           $receiver$iv$iv
        //    75: aload           key$iv
        //    77: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    82: astore          value$iv$iv
        //    84: aload           value$iv$iv
        //    86: ifnonnull       116
        //    89: nop            
        //    90: new             Ljava/util/ArrayList;
        //    93: dup            
        //    94: invokespecial   java/util/ArrayList.<init>:()V
        //    97: astore          answer$iv$iv
        //    99: aload           $receiver$iv$iv
        //   101: aload           key$iv
        //   103: aload           answer$iv$iv
        //   105: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   110: pop            
        //   111: aload           answer$iv$iv
        //   113: goto            118
        //   116: aload           value$iv$iv
        //   118: nop            
        //   119: checkcast       Ljava/util/List;
        //   122: astore          list$iv
        //   124: aload           list$iv
        //   126: iload           element$iv
        //   128: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   131: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   136: pop            
        //   137: iinc            6, 1
        //   140: goto            33
        //   143: aload           destination$iv
        //   145: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TK;>;)Ljava/util/Map<TK;Ljava/util/List<Ljava/lang/Character;>;>;
        //    StackMapTable: 00 04 FF 00 21 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 00 00 FF 00 52 00 0B 07 00 BB 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 01 07 01 4D 07 01 ED 07 01 4D 00 00 41 07 01 4D FF 00 18 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, V> Map<K, List<V>> groupBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends K> keySelector, @NotNull final Function1<? super Character, ? extends V> valueTransform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* keySelector */
        //     7: ldc_w           "keySelector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* valueTransform */
        //    14: ldc_w           "valueTransform"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_0         /* $receiver */
        //    21: astore          4
        //    23: new             Ljava/util/LinkedHashMap;
        //    26: dup            
        //    27: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    30: checkcast       Ljava/util/Map;
        //    33: astore          destination$iv
        //    35: aload           $receiver$iv
        //    37: astore          6
        //    39: iconst_0       
        //    40: istore          7
        //    42: iload           7
        //    44: aload           6
        //    46: invokeinterface java/lang/CharSequence.length:()I
        //    51: if_icmpge       158
        //    54: aload           6
        //    56: iload           7
        //    58: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    63: istore          element$iv
        //    65: aload_1         /* keySelector */
        //    66: iload           element$iv
        //    68: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    71: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    76: astore          key$iv
        //    78: aload           destination$iv
        //    80: astore          $receiver$iv$iv
        //    82: aload           $receiver$iv$iv
        //    84: aload           key$iv
        //    86: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    91: astore          value$iv$iv
        //    93: aload           value$iv$iv
        //    95: ifnonnull       125
        //    98: nop            
        //    99: new             Ljava/util/ArrayList;
        //   102: dup            
        //   103: invokespecial   java/util/ArrayList.<init>:()V
        //   106: astore          answer$iv$iv
        //   108: aload           $receiver$iv$iv
        //   110: aload           key$iv
        //   112: aload           answer$iv$iv
        //   114: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   119: pop            
        //   120: aload           answer$iv$iv
        //   122: goto            127
        //   125: aload           value$iv$iv
        //   127: nop            
        //   128: checkcast       Ljava/util/List;
        //   131: astore          list$iv
        //   133: aload           list$iv
        //   135: aload_2         /* valueTransform */
        //   136: iload           element$iv
        //   138: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   141: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   146: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   151: pop            
        //   152: iinc            7, 1
        //   155: goto            42
        //   158: aload           destination$iv
        //   160: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TK;>;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TV;>;)Ljava/util/Map<TK;Ljava/util/List<TV;>;>;
        //    StackMapTable: 00 04 FF 00 2A 00 08 07 00 BB 07 00 D1 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 00 00 FF 00 52 00 0C 07 00 BB 07 00 D1 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 01 07 01 4D 07 01 ED 07 01 4D 00 00 41 07 01 4D FF 00 1E 00 08 07 00 BB 07 00 D1 07 00 D1 00 07 00 BB 07 01 ED 07 00 BB 01 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            final Object key = keySelector.invoke(element);
            final Map $receiver$iv = destination;
            final Object value$iv = $receiver$iv.get(key);
            Object o;
            if (value$iv == null) {
                final Object answer$iv = new ArrayList();
                $receiver$iv.put(key, answer$iv);
                o = answer$iv;
            }
            else {
                o = value$iv;
            }
            final List list = (List)o;
            list.add(element);
        }
        return destination;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull final CharSequence $receiver, @NotNull final M destination, @NotNull final Function1<? super Character, ? extends K> keySelector, @NotNull final Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            final Object key = keySelector.invoke(element);
            final Map $receiver$iv = destination;
            final Object value$iv = $receiver$iv.get(key);
            Object o;
            if (value$iv == null) {
                final Object answer$iv = new ArrayList();
                $receiver$iv.put(key, answer$iv);
                o = answer$iv;
            }
            else {
                o = value$iv;
            }
            final List list = (List)o;
            list.add(valueTransform.invoke(element));
        }
        return destination;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K> Grouping<Character, K> groupingBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        return (Grouping<Character, K>)new StringsKt___StringsKt$groupingBy.StringsKt___StringsKt$groupingBy$1($receiver, (Function1)keySelector);
    }
    
    @NotNull
    public static final <R> List<R> map(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: aload_0         /* $receiver */
        //    20: invokeinterface java/lang/CharSequence.length:()I
        //    25: invokespecial   java/util/ArrayList.<init>:(I)V
        //    28: checkcast       Ljava/util/Collection;
        //    31: astore          destination$iv
        //    33: aload_3         /* $receiver$iv */
        //    34: astore          5
        //    36: iconst_0       
        //    37: istore          6
        //    39: iload           6
        //    41: aload           5
        //    43: invokeinterface java/lang/CharSequence.length:()I
        //    48: if_icmpge       87
        //    51: aload           5
        //    53: iload           6
        //    55: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    60: istore          item$iv
        //    62: aload           destination$iv
        //    64: aload_1         /* transform */
        //    65: iload           item$iv
        //    67: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    70: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    75: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    80: pop            
        //    81: iinc            6, 1
        //    84: goto            39
        //    87: aload           destination$iv
        //    89: checkcast       Ljava/util/List;
        //    92: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 02 FF 00 27 00 07 07 00 BB 07 00 D1 00 07 00 BB 07 02 07 07 00 BB 01 00 00 2F
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R> List<R> mapIndexed(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Integer, ? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: aload_0         /* $receiver */
        //    20: invokeinterface java/lang/CharSequence.length:()I
        //    25: invokespecial   java/util/ArrayList.<init>:(I)V
        //    28: checkcast       Ljava/util/Collection;
        //    31: astore          destination$iv
        //    33: iconst_0       
        //    34: istore          index$iv
        //    36: aload_3         /* $receiver$iv */
        //    37: astore          6
        //    39: iconst_0       
        //    40: istore          7
        //    42: iload           7
        //    44: aload           6
        //    46: invokeinterface java/lang/CharSequence.length:()I
        //    51: if_icmpge       98
        //    54: aload           6
        //    56: iload           7
        //    58: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    63: istore          item$iv
        //    65: aload           destination$iv
        //    67: aload_1         /* transform */
        //    68: iload           index$iv
        //    70: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    73: iinc            index$iv, 1
        //    76: iload           item$iv
        //    78: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    81: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    86: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    91: pop            
        //    92: iinc            7, 1
        //    95: goto            42
        //    98: aload           destination$iv
        //   100: checkcast       Ljava/util/List;
        //   103: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 02 FF 00 2A 00 08 07 00 BB 07 01 86 00 07 00 BB 07 02 07 01 07 00 BB 01 00 00 37
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R> List<R> mapIndexedNotNull(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Integer, ? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: invokespecial   java/util/ArrayList.<init>:()V
        //    22: checkcast       Ljava/util/Collection;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          $receiver$iv$iv
        //    30: iconst_0       
        //    31: istore          index$iv$iv
        //    33: aload           $receiver$iv$iv
        //    35: astore          7
        //    37: iconst_0       
        //    38: istore          8
        //    40: iload           8
        //    42: aload           7
        //    44: invokeinterface java/lang/CharSequence.length:()I
        //    49: if_icmpge       120
        //    52: aload           7
        //    54: iload           8
        //    56: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    61: istore          item$iv$iv
        //    63: iload           index$iv$iv
        //    65: iinc            index$iv$iv, 1
        //    68: iload           item$iv$iv
        //    70: istore          10
        //    72: istore          index$iv
        //    74: aload_1         /* transform */
        //    75: iload           index$iv
        //    77: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    80: iload           element$iv
        //    82: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    85: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    90: dup            
        //    91: ifnull          113
        //    94: astore          12
        //    96: aload           12
        //    98: astore          it$iv
        //   100: aload           destination$iv
        //   102: aload           it$iv
        //   104: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   109: pop            
        //   110: goto            114
        //   113: pop            
        //   114: iinc            8, 1
        //   117: goto            40
        //   120: nop            
        //   121: aload           destination$iv
        //   123: checkcast       Ljava/util/List;
        //   126: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 04 FF 00 28 00 09 07 00 BB 07 01 86 00 07 00 BB 07 02 07 07 00 BB 01 07 00 BB 01 00 00 FF 00 48 00 0C 07 00 BB 07 01 86 00 07 00 BB 07 02 07 07 00 BB 01 07 00 BB 01 01 01 01 00 01 07 01 4D 00 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function2<? super Integer, ? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc_w           "destination"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* transform */
        //    14: ldc_w           "transform"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_0         /* $receiver */
        //    21: astore          $receiver$iv
        //    23: iconst_0       
        //    24: istore          index$iv
        //    26: aload           $receiver$iv
        //    28: astore          6
        //    30: iconst_0       
        //    31: istore          7
        //    33: iload           7
        //    35: aload           6
        //    37: invokeinterface java/lang/CharSequence.length:()I
        //    42: if_icmpge       112
        //    45: aload           6
        //    47: iload           7
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          item$iv
        //    56: iload           index$iv
        //    58: iinc            index$iv, 1
        //    61: iload           item$iv
        //    63: istore          9
        //    65: istore          index
        //    67: aload_2         /* transform */
        //    68: iload           index
        //    70: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    73: iload           element
        //    75: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    78: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    83: dup            
        //    84: ifnull          105
        //    87: astore          11
        //    89: aload           11
        //    91: astore          it
        //    93: aload_1         /* destination */
        //    94: aload           it
        //    96: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   101: pop            
        //   102: goto            106
        //   105: pop            
        //   106: iinc            7, 1
        //   109: goto            33
        //   112: nop            
        //   113: aload_1         /* destination */
        //   114: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;C::Ljava/util/Collection<-TR;>;>(Ljava/lang/CharSequence;TC;Lkotlin/jvm/functions/Function2<-Ljava/lang/Integer;-Ljava/lang/Character;+TR;>;)TC;
        //    StackMapTable: 00 04 FF 00 21 00 08 07 00 BB 07 02 07 07 01 86 00 07 00 BB 01 07 00 BB 01 00 00 FF 00 47 00 0B 07 00 BB 07 02 07 07 01 86 00 07 00 BB 01 07 00 BB 01 01 01 01 00 01 07 01 4D 00 F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int index = 0;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char item = $receiver.charAt(i);
            final Integer value = index;
            ++index;
            destination.add((Object)transform.invoke(value, item));
        }
        return destination;
    }
    
    @NotNull
    public static final <R> List<R> mapNotNull(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: invokespecial   java/util/ArrayList.<init>:()V
        //    22: checkcast       Ljava/util/Collection;
        //    25: astore          destination$iv
        //    27: aload_3         /* $receiver$iv */
        //    28: astore          $receiver$iv$iv
        //    30: aload           $receiver$iv$iv
        //    32: astore          6
        //    34: iconst_0       
        //    35: istore          7
        //    37: iload           7
        //    39: aload           6
        //    41: invokeinterface java/lang/CharSequence.length:()I
        //    46: if_icmpge       105
        //    49: aload           6
        //    51: iload           7
        //    53: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    58: istore          element$iv$iv
        //    60: iload           element$iv$iv
        //    62: istore          element$iv
        //    64: aload_1         /* transform */
        //    65: iload           element$iv
        //    67: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    70: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    75: dup            
        //    76: ifnull          98
        //    79: astore          10
        //    81: aload           10
        //    83: astore          it$iv
        //    85: aload           destination$iv
        //    87: aload           it$iv
        //    89: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    94: pop            
        //    95: goto            99
        //    98: pop            
        //    99: iinc            7, 1
        //   102: goto            37
        //   105: nop            
        //   106: aload           destination$iv
        //   108: checkcast       Ljava/util/List;
        //   111: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 04 FF 00 25 00 08 07 00 BB 07 00 D1 00 07 00 BB 07 02 07 07 00 BB 07 00 BB 01 00 00 FF 00 3C 00 0A 07 00 BB 07 00 D1 00 07 00 BB 07 02 07 07 00 BB 07 00 BB 01 01 01 00 01 07 01 4D 00 F9 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapNotNullTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        final CharSequence charSequence;
        final CharSequence $receiver$iv = charSequence = $receiver;
        for (int i = 0; i < charSequence.length(); ++i) {
            final char element;
            final char element$iv = element = charSequence.charAt(i);
            final R invoke = (R)transform.invoke(element);
            if (invoke != null) {
                final Object it = invoke;
                destination.add((Object)it);
            }
        }
        return destination;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapTo(@NotNull final CharSequence $receiver, @NotNull final C destination, @NotNull final Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char item = $receiver.charAt(i);
            destination.add((Object)transform.invoke(item));
        }
        return destination;
    }
    
    @NotNull
    public static final Iterable<IndexedValue<Character>> withIndex(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return (Iterable<IndexedValue<Character>>)new IndexingIterable((Function0<? extends Iterator<?>>)new StringsKt___StringsKt$withIndex.StringsKt___StringsKt$withIndex$1($receiver));
    }
    
    public static final boolean all(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (!predicate.invoke(element)) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean any(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.length() != 0;
    }
    
    public static final boolean any(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                return true;
            }
        }
        return false;
    }
    
    @InlineOnly
    private static final int count(@NotNull final CharSequence $receiver) {
        return $receiver.length();
    }
    
    public static final int count(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int count = 0;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                ++count;
            }
        }
        return count;
    }
    
    public static final <R> R fold(@NotNull final CharSequence $receiver, final R initial, @NotNull final Function2<? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Object accumulator = initial;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            accumulator = operation.invoke((Object)accumulator, Character.valueOf(element));
        }
        return (R)accumulator;
    }
    
    public static final <R> R foldIndexed(@NotNull final CharSequence $receiver, final R initial, @NotNull final Function3<? super Integer, ? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = 0;
        Object accumulator = initial;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            final Integer value = index;
            ++index;
            accumulator = operation.invoke(value, (Object)accumulator, Character.valueOf(element));
        }
        return (R)accumulator;
    }
    
    public static final <R> R foldRight(@NotNull final CharSequence $receiver, final R initial, @NotNull final Function2<? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index;
        Object accumulator;
        for (index = StringsKt__StringsKt.getLastIndex($receiver), accumulator = initial; index >= 0; accumulator = operation.invoke(Character.valueOf($receiver.charAt(index--)), (Object)accumulator)) {}
        return (R)accumulator;
    }
    
    public static final <R> R foldRightIndexed(@NotNull final CharSequence $receiver, final R initial, @NotNull final Function3<? super Integer, ? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt__StringsKt.getLastIndex($receiver);
        Object accumulator = initial;
        while (index >= 0) {
            accumulator = operation.invoke(Integer.valueOf(index), Character.valueOf($receiver.charAt(index)), (Object)accumulator);
            --index;
        }
        return (R)accumulator;
    }
    
    public static final void forEach(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            action.invoke(element);
        }
    }
    
    public static final void forEachIndexed(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Integer, ? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        int index = 0;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char item = $receiver.charAt(i);
            final Integer value = index;
            ++index;
            action.invoke(value, item);
        }
    }
    
    @Nullable
    public static final Character max(@NotNull final CharSequence $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: astore_1       
        //     8: aload_1        
        //     9: invokeinterface java/lang/CharSequence.length:()I
        //    14: ifne            21
        //    17: iconst_1       
        //    18: goto            22
        //    21: iconst_0       
        //    22: ifeq            27
        //    25: aconst_null    
        //    26: areturn        
        //    27: aload_0         /* $receiver */
        //    28: iconst_0       
        //    29: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    34: istore_1        /* max */
        //    35: iconst_1       
        //    36: istore_2       
        //    37: aload_0         /* $receiver */
        //    38: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    41: istore_3       
        //    42: iload_2        
        //    43: iload_3        
        //    44: if_icmpgt       76
        //    47: aload_0         /* $receiver */
        //    48: iload_2         /* i */
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          e
        //    56: iload_1         /* max */
        //    57: iload           e
        //    59: if_icmpge       65
        //    62: iload           e
        //    64: istore_1        /* max */
        //    65: iload_2         /* i */
        //    66: iload_3        
        //    67: if_icmpeq       76
        //    70: iinc            i, 1
        //    73: goto            47
        //    76: iload_1         /* max */
        //    77: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    80: areturn        
        //    StackMapTable: 00 06 FC 00 15 07 00 BB 40 01 04 FF 00 13 00 04 07 00 BB 01 01 01 00 00 FC 00 11 01 FA 00 0A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final <R extends Comparable<? super R>> Character maxBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends R> selector) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* selector */
        //     7: ldc_w           "selector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: aload_3        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            34
        //    32: aconst_null    
        //    33: areturn        
        //    34: aload_0         /* $receiver */
        //    35: iconst_0       
        //    36: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    41: istore_3        /* maxElem */
        //    42: aload_1         /* selector */
        //    43: iload_3         /* maxElem */
        //    44: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    47: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    52: checkcast       Ljava/lang/Comparable;
        //    55: astore          maxValue
        //    57: iconst_1       
        //    58: istore          5
        //    60: aload_0         /* $receiver */
        //    61: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    64: istore          6
        //    66: iload           5
        //    68: iload           6
        //    70: if_icmpgt       131
        //    73: aload_0         /* $receiver */
        //    74: iload           i
        //    76: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    81: istore          e
        //    83: aload_1         /* selector */
        //    84: iload           e
        //    86: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    89: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    94: checkcast       Ljava/lang/Comparable;
        //    97: astore          v
        //    99: aload           maxValue
        //   101: aload           v
        //   103: invokeinterface java/lang/Comparable.compareTo:(Ljava/lang/Object;)I
        //   108: ifge            118
        //   111: iload           e
        //   113: istore_3        /* maxElem */
        //   114: aload           v
        //   116: astore          maxValue
        //   118: iload           i
        //   120: iload           6
        //   122: if_icmpeq       131
        //   125: iinc            i, 1
        //   128: goto            73
        //   131: iload_3         /* maxElem */
        //   132: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   135: areturn        
        //    Signature:
        //  <R::Ljava/lang/Comparable<-TR;>;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TR;>;)Ljava/lang/Character;
        //    StackMapTable: 00 06 FD 00 1C 00 07 00 BB 40 01 04 FF 00 26 00 07 07 00 BB 07 00 D1 00 01 07 02 86 01 01 00 00 FD 00 2C 01 07 02 86 F9 00 0C
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final Character maxWith(@NotNull final CharSequence $receiver, @NotNull final Comparator<? super Character> comparator) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* comparator */
        //     7: ldc_w           "comparator"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_2       
        //    15: aload_2        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            34
        //    32: aconst_null    
        //    33: areturn        
        //    34: aload_0         /* $receiver */
        //    35: iconst_0       
        //    36: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    41: istore_2        /* max */
        //    42: iconst_1       
        //    43: istore_3       
        //    44: aload_0         /* $receiver */
        //    45: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    48: istore          4
        //    50: iload_3        
        //    51: iload           4
        //    53: if_icmpgt       98
        //    56: aload_0         /* $receiver */
        //    57: iload_3         /* i */
        //    58: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    63: istore          e
        //    65: aload_1         /* comparator */
        //    66: iload_2         /* max */
        //    67: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    70: iload           e
        //    72: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    75: invokeinterface java/util/Comparator.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    80: ifge            86
        //    83: iload           e
        //    85: istore_2        /* max */
        //    86: iload_3         /* i */
        //    87: iload           4
        //    89: if_icmpeq       98
        //    92: iinc            i, 1
        //    95: goto            56
        //    98: iload_2         /* max */
        //    99: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   102: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Ljava/util/Comparator<-Ljava/lang/Character;>;)Ljava/lang/Character;
        //    StackMapTable: 00 06 FC 00 1C 07 00 BB 40 01 04 FF 00 15 00 05 07 00 BB 07 02 92 01 01 01 00 00 FC 00 1D 01 FA 00 0B
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final Character min(@NotNull final CharSequence $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: astore_1       
        //     8: aload_1        
        //     9: invokeinterface java/lang/CharSequence.length:()I
        //    14: ifne            21
        //    17: iconst_1       
        //    18: goto            22
        //    21: iconst_0       
        //    22: ifeq            27
        //    25: aconst_null    
        //    26: areturn        
        //    27: aload_0         /* $receiver */
        //    28: iconst_0       
        //    29: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    34: istore_1        /* min */
        //    35: iconst_1       
        //    36: istore_2       
        //    37: aload_0         /* $receiver */
        //    38: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    41: istore_3       
        //    42: iload_2        
        //    43: iload_3        
        //    44: if_icmpgt       76
        //    47: aload_0         /* $receiver */
        //    48: iload_2         /* i */
        //    49: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    54: istore          e
        //    56: iload_1         /* min */
        //    57: iload           e
        //    59: if_icmple       65
        //    62: iload           e
        //    64: istore_1        /* min */
        //    65: iload_2         /* i */
        //    66: iload_3        
        //    67: if_icmpeq       76
        //    70: iinc            i, 1
        //    73: goto            47
        //    76: iload_1         /* min */
        //    77: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    80: areturn        
        //    StackMapTable: 00 06 FC 00 15 07 00 BB 40 01 04 FF 00 13 00 04 07 00 BB 01 01 01 00 00 FC 00 11 01 FA 00 0A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final <R extends Comparable<? super R>> Character minBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, ? extends R> selector) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* selector */
        //     7: ldc_w           "selector"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: aload_3        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            34
        //    32: aconst_null    
        //    33: areturn        
        //    34: aload_0         /* $receiver */
        //    35: iconst_0       
        //    36: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    41: istore_3        /* minElem */
        //    42: aload_1         /* selector */
        //    43: iload_3         /* minElem */
        //    44: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    47: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    52: checkcast       Ljava/lang/Comparable;
        //    55: astore          minValue
        //    57: iconst_1       
        //    58: istore          5
        //    60: aload_0         /* $receiver */
        //    61: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    64: istore          6
        //    66: iload           5
        //    68: iload           6
        //    70: if_icmpgt       131
        //    73: aload_0         /* $receiver */
        //    74: iload           i
        //    76: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    81: istore          e
        //    83: aload_1         /* selector */
        //    84: iload           e
        //    86: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    89: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    94: checkcast       Ljava/lang/Comparable;
        //    97: astore          v
        //    99: aload           minValue
        //   101: aload           v
        //   103: invokeinterface java/lang/Comparable.compareTo:(Ljava/lang/Object;)I
        //   108: ifle            118
        //   111: iload           e
        //   113: istore_3        /* minElem */
        //   114: aload           v
        //   116: astore          minValue
        //   118: iload           i
        //   120: iload           6
        //   122: if_icmpeq       131
        //   125: iinc            i, 1
        //   128: goto            73
        //   131: iload_3         /* minElem */
        //   132: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   135: areturn        
        //    Signature:
        //  <R::Ljava/lang/Comparable<-TR;>;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1<-Ljava/lang/Character;+TR;>;)Ljava/lang/Character;
        //    StackMapTable: 00 06 FD 00 1C 00 07 00 BB 40 01 04 FF 00 26 00 07 07 00 BB 07 00 D1 00 01 07 02 86 01 01 00 00 FD 00 2C 01 07 02 86 F9 00 0C
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final Character minWith(@NotNull final CharSequence $receiver, @NotNull final Comparator<? super Character> comparator) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* comparator */
        //     7: ldc_w           "comparator"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_2       
        //    15: aload_2        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            34
        //    32: aconst_null    
        //    33: areturn        
        //    34: aload_0         /* $receiver */
        //    35: iconst_0       
        //    36: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    41: istore_2        /* min */
        //    42: iconst_1       
        //    43: istore_3       
        //    44: aload_0         /* $receiver */
        //    45: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    48: istore          4
        //    50: iload_3        
        //    51: iload           4
        //    53: if_icmpgt       98
        //    56: aload_0         /* $receiver */
        //    57: iload_3         /* i */
        //    58: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    63: istore          e
        //    65: aload_1         /* comparator */
        //    66: iload_2         /* min */
        //    67: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    70: iload           e
        //    72: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    75: invokeinterface java/util/Comparator.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    80: ifle            86
        //    83: iload           e
        //    85: istore_2        /* min */
        //    86: iload_3         /* i */
        //    87: iload           4
        //    89: if_icmpeq       98
        //    92: iinc            i, 1
        //    95: goto            56
        //    98: iload_2         /* min */
        //    99: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   102: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Ljava/util/Comparator<-Ljava/lang/Character;>;)Ljava/lang/Character;
        //    StackMapTable: 00 06 FC 00 1C 07 00 BB 40 01 04 FF 00 15 00 05 07 00 BB 07 02 92 01 01 01 00 00 FC 00 1D 01 FA 00 0B
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final boolean none(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.length() == 0;
    }
    
    public static final boolean none(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                return false;
            }
        }
        return true;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S extends CharSequence> S onEach(@NotNull final S $receiver, @NotNull final Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        final CharSequence charSequence;
        final CharSequence $receiver2 = charSequence = $receiver;
        for (int i = 0; i < charSequence.length(); ++i) {
            final char element = charSequence.charAt(i);
            action.invoke(element);
        }
        return $receiver;
    }
    
    public static final char reduce(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Character, ? super Character, Character> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* operation */
        //     7: ldc_w           "operation"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: aload_3        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            46
        //    32: new             Ljava/lang/UnsupportedOperationException;
        //    35: dup            
        //    36: ldc_w           "Empty char sequence can't be reduced."
        //    39: invokespecial   java/lang/UnsupportedOperationException.<init>:(Ljava/lang/String;)V
        //    42: checkcast       Ljava/lang/Throwable;
        //    45: athrow         
        //    46: aload_0         /* $receiver */
        //    47: iconst_0       
        //    48: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    53: istore_3        /* accumulator */
        //    54: iconst_1       
        //    55: istore          4
        //    57: aload_0         /* $receiver */
        //    58: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    61: istore          5
        //    63: iload           4
        //    65: iload           5
        //    67: if_icmpgt       111
        //    70: aload_1         /* operation */
        //    71: iload_3         /* accumulator */
        //    72: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    75: aload_0         /* $receiver */
        //    76: iload           index
        //    78: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    83: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    86: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    91: checkcast       Ljava/lang/Character;
        //    94: invokevirtual   java/lang/Character.charValue:()C
        //    97: istore_3        /* accumulator */
        //    98: iload           index
        //   100: iload           5
        //   102: if_icmpeq       111
        //   105: iinc            index, 1
        //   108: goto            70
        //   111: iload_3         /* accumulator */
        //   112: ireturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Character;-Ljava/lang/Character;Ljava/lang/Character;>;)C
        //    StackMapTable: 00 05 FD 00 1C 00 07 00 BB 40 01 10 FF 00 17 00 06 07 00 BB 07 01 86 00 01 01 01 00 00 28
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final char reduceIndexed(@NotNull final CharSequence $receiver, @NotNull final Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* operation */
        //     7: ldc_w           "operation"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_3       
        //    15: aload_3        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            46
        //    32: new             Ljava/lang/UnsupportedOperationException;
        //    35: dup            
        //    36: ldc_w           "Empty char sequence can't be reduced."
        //    39: invokespecial   java/lang/UnsupportedOperationException.<init>:(Ljava/lang/String;)V
        //    42: checkcast       Ljava/lang/Throwable;
        //    45: athrow         
        //    46: aload_0         /* $receiver */
        //    47: iconst_0       
        //    48: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    53: istore_3        /* accumulator */
        //    54: iconst_1       
        //    55: istore          4
        //    57: aload_0         /* $receiver */
        //    58: invokestatic    kotlin/text/StringsKt.getLastIndex:(Ljava/lang/CharSequence;)I
        //    61: istore          5
        //    63: iload           4
        //    65: iload           5
        //    67: if_icmpgt       116
        //    70: aload_1         /* operation */
        //    71: iload           index
        //    73: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    76: iload_3         /* accumulator */
        //    77: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    80: aload_0         /* $receiver */
        //    81: iload           index
        //    83: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    88: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    91: invokeinterface kotlin/jvm/functions/Function3.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    96: checkcast       Ljava/lang/Character;
        //    99: invokevirtual   java/lang/Character.charValue:()C
        //   102: istore_3        /* accumulator */
        //   103: iload           index
        //   105: iload           5
        //   107: if_icmpeq       116
        //   110: iinc            index, 1
        //   113: goto            70
        //   116: iload_3         /* accumulator */
        //   117: ireturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function3<-Ljava/lang/Integer;-Ljava/lang/Character;-Ljava/lang/Character;Ljava/lang/Character;>;)C
        //    StackMapTable: 00 05 FD 00 1C 00 07 00 BB 40 01 10 FF 00 17 00 06 07 00 BB 07 02 79 00 01 01 01 00 00 2D
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final char reduceRight(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt__StringsKt.getLastIndex($receiver);
        if (index < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator;
        for (accumulator = $receiver.charAt(index--); index >= 0; accumulator = operation.invoke($receiver.charAt(index--), accumulator)) {}
        return accumulator;
    }
    
    public static final char reduceRightIndexed(@NotNull final CharSequence $receiver, @NotNull final Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt__StringsKt.getLastIndex($receiver);
        if (index < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator;
        for (accumulator = $receiver.charAt(index--); index >= 0; --index) {
            accumulator = operation.invoke(index, $receiver.charAt(index), accumulator);
        }
        return accumulator;
    }
    
    public static final int sumBy(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Integer> selector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int sum = 0;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            sum += selector.invoke(element).intValue();
        }
        return sum;
    }
    
    public static final double sumByDouble(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Double> selector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        double sum = 0.0;
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            sum += selector.invoke(element).doubleValue();
        }
        return sum;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> chunked(@NotNull final CharSequence $receiver, final int size) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return windowed($receiver, size, size, true);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> chunked(@NotNull final CharSequence $receiver, final int size, @NotNull final Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return windowed($receiver, size, size, true, transform);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> chunkedSequence(@NotNull final CharSequence $receiver, final int size) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return chunkedSequence($receiver, size, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$chunkedSequence.StringsKt___StringsKt$chunkedSequence$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> chunkedSequence(@NotNull final CharSequence $receiver, final int size, @NotNull final Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return windowedSequence($receiver, size, size, true, transform);
    }
    
    @NotNull
    public static final Pair<CharSequence, CharSequence> partition(@NotNull final CharSequence $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        final StringBuilder first = new StringBuilder();
        final StringBuilder second = new StringBuilder();
        for (int i = 0; i < $receiver.length(); ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                first.append(element);
            }
            else {
                second.append(element);
            }
        }
        return new Pair<CharSequence, CharSequence>(first, second);
    }
    
    @NotNull
    public static final Pair<String, String> partition(@NotNull final String $receiver, @NotNull final Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        final StringBuilder first = new StringBuilder();
        final StringBuilder second = new StringBuilder();
        for (int length = $receiver.length(), i = 0; i < length; ++i) {
            final char element = $receiver.charAt(i);
            if (predicate.invoke(element)) {
                first.append(element);
            }
            else {
                second.append(element);
            }
        }
        return new Pair<String, String>(first.toString(), second.toString());
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> windowed(@NotNull final CharSequence $receiver, final int size, final int step, final boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return windowed($receiver, size, step, partialWindows, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$windowed.StringsKt___StringsKt$windowed$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> windowed(@NotNull final CharSequence $receiver, final int size, final int step, final boolean partialWindows, @NotNull final Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        final int thisSize = $receiver.length();
        final ArrayList result = new ArrayList((thisSize + step - 1) / step);
        for (int index = 0; index < thisSize; index += step) {
            final int end = index + size;
            int n;
            if (end > thisSize) {
                if (!partialWindows) {
                    break;
                }
                n = thisSize;
            }
            else {
                n = end;
            }
            final int coercedEnd = n;
            result.add(transform.invoke($receiver.subSequence(index, coercedEnd)));
        }
        return (List<R>)result;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> windowedSequence(@NotNull final CharSequence $receiver, final int size, final int step, final boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return windowedSequence($receiver, size, step, partialWindows, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$windowedSequence.StringsKt___StringsKt$windowedSequence$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> windowedSequence(@NotNull final CharSequence $receiver, final int size, final int step, final boolean partialWindows, @NotNull final Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        final IntProgression windows = RangesKt___RangesKt.step(partialWindows ? StringsKt__StringsKt.getIndices($receiver) : RangesKt___RangesKt.until(0, $receiver.length() - size + 1), step);
        return SequencesKt___SequencesKt.map(CollectionsKt___CollectionsKt.asSequence((Iterable<?>)windows), (Function1<? super Object, ? extends R>)new StringsKt___StringsKt$windowedSequence.StringsKt___StringsKt$windowedSequence$2($receiver, (Function1)transform, size));
    }
    
    @NotNull
    public static final List<Pair<Character, Character>> zip(@NotNull final CharSequence $receiver, @NotNull final CharSequence other) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* other */
        //     7: ldc_w           "other"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: astore_2        /* $receiver$iv */
        //    15: aload_2         /* $receiver$iv */
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: istore_3       
        //    22: aload_1         /* other */
        //    23: invokeinterface java/lang/CharSequence.length:()I
        //    28: istore          4
        //    30: iload_3        
        //    31: iload           4
        //    33: invokestatic    java/lang/Math.min:(II)I
        //    36: istore          length$iv
        //    38: new             Ljava/util/ArrayList;
        //    41: dup            
        //    42: iload           length$iv
        //    44: invokespecial   java/util/ArrayList.<init>:(I)V
        //    47: astore_3        /* list$iv */
        //    48: iconst_0       
        //    49: istore          4
        //    51: iload           length$iv
        //    53: istore          6
        //    55: iload           4
        //    57: iload           6
        //    59: if_icmpge       114
        //    62: aload_3         /* list$iv */
        //    63: aload_2         /* $receiver$iv */
        //    64: iload           i$iv
        //    66: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    71: aload_1         /* other */
        //    72: iload           i$iv
        //    74: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    79: istore          7
        //    81: istore          8
        //    83: astore          11
        //    85: iload           c1
        //    87: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    90: iload           c2
        //    92: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    95: invokestatic    kotlin/TuplesKt.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //    98: astore          12
        //   100: aload           11
        //   102: aload           12
        //   104: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   107: pop            
        //   108: iinc            i$iv, 1
        //   111: goto            55
        //   114: aload_3         /* list$iv */
        //   115: checkcast       Ljava/util/List;
        //   118: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/util/List<Lkotlin/Pair<Ljava/lang/Character;Ljava/lang/Character;>;>;
        //    StackMapTable: 00 02 FF 00 37 00 07 07 00 BB 07 00 BB 07 00 BB 07 02 22 01 01 01 00 00 3A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <V> List<V> zip(@NotNull final CharSequence $receiver, @NotNull final CharSequence other, @NotNull final Function2<? super Character, ? super Character, ? extends V> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* other */
        //     7: ldc_w           "other"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_2         /* transform */
        //    14: ldc_w           "transform"
        //    17: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    20: aload_0         /* $receiver */
        //    21: invokeinterface java/lang/CharSequence.length:()I
        //    26: istore          5
        //    28: aload_1         /* other */
        //    29: invokeinterface java/lang/CharSequence.length:()I
        //    34: istore          6
        //    36: iload           5
        //    38: iload           6
        //    40: invokestatic    java/lang/Math.min:(II)I
        //    43: istore          length
        //    45: new             Ljava/util/ArrayList;
        //    48: dup            
        //    49: iload           length
        //    51: invokespecial   java/util/ArrayList.<init>:(I)V
        //    54: astore          list
        //    56: iconst_0       
        //    57: istore          6
        //    59: iload           length
        //    61: istore          7
        //    63: iload           6
        //    65: iload           7
        //    67: if_icmpge       110
        //    70: aload           list
        //    72: aload_2         /* transform */
        //    73: aload_0         /* $receiver */
        //    74: iload           i
        //    76: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    81: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    84: aload_1         /* other */
        //    85: iload           i
        //    87: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    92: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    95: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   100: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   103: pop            
        //   104: iinc            i, 1
        //   107: goto            63
        //   110: aload           list
        //   112: checkcast       Ljava/util/List;
        //   115: areturn        
        //    Signature:
        //  <V:Ljava/lang/Object;>(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Character;-Ljava/lang/Character;+TV;>;)Ljava/util/List<TV;>;
        //    StackMapTable: 00 02 FF 00 3F 00 08 07 00 BB 07 00 BB 07 01 86 00 01 07 02 22 01 01 00 00 2E
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<Pair<Character, Character>> zipWithNext(@NotNull final CharSequence $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: astore_1        /* $receiver$iv */
        //     8: aload_1         /* $receiver$iv */
        //     9: invokeinterface java/lang/CharSequence.length:()I
        //    14: iconst_1       
        //    15: isub           
        //    16: istore_2        /* size$iv */
        //    17: iload_2         /* size$iv */
        //    18: iconst_1       
        //    19: if_icmpge       28
        //    22: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //    25: goto            108
        //    28: new             Ljava/util/ArrayList;
        //    31: dup            
        //    32: iload_2         /* size$iv */
        //    33: invokespecial   java/util/ArrayList.<init>:(I)V
        //    36: astore_3        /* result$iv */
        //    37: iconst_0       
        //    38: istore          4
        //    40: iload_2         /* size$iv */
        //    41: istore          5
        //    43: iload           4
        //    45: iload           5
        //    47: if_icmpge       104
        //    50: aload_3         /* result$iv */
        //    51: aload_1         /* $receiver$iv */
        //    52: iload           index$iv
        //    54: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    59: aload_1         /* $receiver$iv */
        //    60: iload           index$iv
        //    62: iconst_1       
        //    63: iadd           
        //    64: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    69: istore          6
        //    71: istore          7
        //    73: astore          10
        //    75: iload           a
        //    77: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    80: iload           b
        //    82: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    85: invokestatic    kotlin/TuplesKt.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //    88: astore          11
        //    90: aload           10
        //    92: aload           11
        //    94: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    97: pop            
        //    98: iinc            index$iv, 1
        //   101: goto            43
        //   104: aload_3         /* result$iv */
        //   105: checkcast       Ljava/util/List;
        //   108: areturn        
        //    Signature:
        //  (Ljava/lang/CharSequence;)Ljava/util/List<Lkotlin/Pair<Ljava/lang/Character;Ljava/lang/Character;>;>;
        //    StackMapTable: 00 04 FD 00 1C 07 00 BB 01 FE 00 0E 07 02 22 01 01 3C FF 00 03 00 03 07 00 BB 07 00 BB 01 00 01 07 02 20
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> zipWithNext(@NotNull final CharSequence $receiver, @NotNull final Function2<? super Character, ? super Character, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc_w           "transform"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokeinterface java/lang/CharSequence.length:()I
        //    19: iconst_1       
        //    20: isub           
        //    21: istore_3        /* size */
        //    22: iload_3         /* size */
        //    23: iconst_1       
        //    24: if_icmpge       31
        //    27: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //    30: areturn        
        //    31: new             Ljava/util/ArrayList;
        //    34: dup            
        //    35: iload_3         /* size */
        //    36: invokespecial   java/util/ArrayList.<init>:(I)V
        //    39: astore          result
        //    41: iconst_0       
        //    42: istore          5
        //    44: iload_3         /* size */
        //    45: istore          6
        //    47: iload           5
        //    49: iload           6
        //    51: if_icmpge       96
        //    54: aload           result
        //    56: aload_1         /* transform */
        //    57: aload_0         /* $receiver */
        //    58: iload           index
        //    60: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    65: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    68: aload_0         /* $receiver */
        //    69: iload           index
        //    71: iconst_1       
        //    72: iadd           
        //    73: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    78: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //    81: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    86: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    89: pop            
        //    90: iinc            index, 1
        //    93: goto            47
        //    96: aload           result
        //    98: checkcast       Ljava/util/List;
        //   101: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2<-Ljava/lang/Character;-Ljava/lang/Character;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 03 FD 00 1F 00 01 FE 00 0F 07 02 22 01 01 30
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final Iterable<Character> asIterable(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver instanceof String && $receiver.length() == 0) {
            return (Iterable<Character>)CollectionsKt__CollectionsKt.emptyList();
        }
        return new Iterable<Character>($receiver) {
            @NotNull
            @Override
            public Iterator<Character> iterator() {
                return StringsKt__StringsKt.iterator(this.$this_asIterable$inlined);
            }
        };
    }
    
    @NotNull
    public static final Sequence<Character> asSequence(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver instanceof String && $receiver.length() == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        }
        return new Sequence<Character>($receiver) {
            @NotNull
            @Override
            public Iterator<Character> iterator() {
                return StringsKt__StringsKt.iterator(this.$this_asSequence$inlined);
            }
        };
    }
    
    public StringsKt___StringsKt() {
    }
}
