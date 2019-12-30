// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.internal.InlineOnly;
import java.util.Properties;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.SortedMap;
import kotlin.jvm.functions.Function0;
import java.util.concurrent.ConcurrentMap;
import java.util.Collections;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Pair;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000D\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005\u001aY\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u00032*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00050\n\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005¢\u0006\u0002\u0010\u000b\u001a@\u0010\f\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\r2\u0006\u0010\u000e\u001a\u0002H\u00022\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0010H\u0086\b¢\u0006\u0002\u0010\u0011\u001a\u0019\u0010\u0012\u001a\u00020\u0013*\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140\u0001H\u0087\b\u001a2\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0000\u001a1\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b\u001a:\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\u001a@\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u000e\u0010\u0018\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0019¨\u0006\u001a" }, d2 = { "mapOf", "", "K", "V", "pair", "Lkotlin/Pair;", "sortedMapOf", "Ljava/util/SortedMap;", "", "pairs", "", "([Lkotlin/Pair;)Ljava/util/SortedMap;", "getOrPut", "Ljava/util/concurrent/ConcurrentMap;", "key", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/concurrent/ConcurrentMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "toProperties", "Ljava/util/Properties;", "", "toSingletonMap", "toSingletonMapOrSelf", "toSortedMap", "comparator", "Ljava/util/Comparator;", "kotlin-stdlib" }, xs = "kotlin/collections/MapsKt")
class MapsKt__MapsJVMKt extends MapsKt__MapWithDefaultKt
{
    @NotNull
    public static final <K, V> Map<K, V> mapOf(@NotNull final Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkParameterIsNotNull(pair, "pair");
        final Map<? extends K, ? extends V> singletonMap = Collections.singletonMap(pair.getFirst(), pair.getSecond());
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "java.util.Collections.si\u2026(pair.first, pair.second)");
        return (Map<K, V>)singletonMap;
    }
    
    public static final <K, V> V getOrPut(@NotNull final ConcurrentMap<K, V> $receiver, final K key, @NotNull final Function0<? extends V> defaultValue) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        Object o;
        if ((o = $receiver.get(key)) == null) {
            final Object default1 = defaultValue.invoke();
            if ((o = $receiver.putIfAbsent((K)key, (V)default1)) == null) {
                o = default1;
            }
        }
        return (V)o;
    }
    
    @NotNull
    public static final <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return new TreeMap<K, V>($receiver);
    }
    
    @NotNull
    public static final <K, V> SortedMap<K, V> toSortedMap(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Comparator<? super K> comparator) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        final TreeMap $receiver2;
        final TreeMap treeMap = $receiver2 = new TreeMap((Comparator<? super K>)comparator);
        $receiver2.putAll($receiver);
        return (TreeMap<K, V>)treeMap;
    }
    
    @NotNull
    public static final <K extends Comparable<? super K>, V> SortedMap<K, V> sortedMapOf(@NotNull final Pair<? extends K, ? extends V>... pairs) {
        Intrinsics.checkParameterIsNotNull(pairs, "pairs");
        final TreeMap $receiver;
        final TreeMap treeMap = $receiver = new TreeMap();
        MapsKt__MapsKt.putAll((Map<? super Object, ? super Object>)$receiver, (Pair<?, ?>[])pairs);
        return (TreeMap<K, V>)treeMap;
    }
    
    @InlineOnly
    private static final Properties toProperties(@NotNull final Map<String, String> $receiver) {
        final Properties $receiver2 = new Properties();
        $receiver2.putAll($receiver);
        return $receiver2;
    }
    
    @InlineOnly
    private static final <K, V> Map<K, V> toSingletonMapOrSelf(@NotNull final Map<K, ? extends V> $receiver) {
        return (Map<K, V>)toSingletonMap((Map<?, ?>)$receiver);
    }
    
    @NotNull
    public static final <K, V> Map<K, V> toSingletonMap(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Map.Entry $receiver2 = (Map.Entry)$receiver.entrySet().iterator().next();
        final Map<K, V> singletonMap = Collections.singletonMap($receiver2.getKey(), $receiver2.getValue());
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "java.util.Collections.singletonMap(key, value)");
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "with(entries.iterator().\u2026ingletonMap(key, value) }");
        return singletonMap;
    }
    
    public MapsKt__MapsJVMKt() {
    }
}
