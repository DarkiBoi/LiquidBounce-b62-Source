// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.sequences.Sequence;
import kotlin.SinceKotlin;
import org.jetbrains.annotations.Nullable;
import java.util.Comparator;
import java.util.Set;
import kotlin.internal.HidesMembers;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import java.util.Collection;
import kotlin.jvm.functions.Function1;
import java.util.Iterator;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Pair;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000h\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\u001aG\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001a$\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aG\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001a9\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\n\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001a6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001a'\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001aG\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001aY\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\n0\u0006H\u0086\b\u001ar\u0010\u0013\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\n0\u0006H\u0086\b¢\u0006\u0002\u0010\u0017\u001aG\u0010\u0018\u001a\u00020\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00190\u0006H\u0087\b\u001aS\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b\u001aY\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0011*\u00020\u001d*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00110\u0006H\u0086\b\u001ar\u0010\u001e\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0011*\u00020\u001d\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00110\u0006H\u0086\b¢\u0006\u0002\u0010\u0017\u001al\u0010\u001f\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b¢\u0006\u0002\u0010\u0017\u001ae\u0010 \u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u00110!*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\"\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0087\b\u001ai\u0010#\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u0010$\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070%j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`&H\u0087\b\u001ae\u0010'\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u00110!*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\"\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b\u001af\u0010(\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u0010$\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070%j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`&\u001a$\u0010)\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aG\u0010)\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001aV\u0010*\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010+*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002H+2\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00190\u0006H\u0087\b¢\u0006\u0002\u0010,\u001a6\u0010-\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030.0\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004¨\u0006/" }, d2 = { "all", "", "K", "V", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "count", "", "flatMap", "", "R", "transform", "flatMapTo", "C", "", "destination", "(Ljava/util/Map;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "forEach", "", "action", "map", "mapNotNull", "", "mapNotNullTo", "mapTo", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "minBy", "minWith", "none", "onEach", "M", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "toList", "Lkotlin/Pair;", "kotlin-stdlib" }, xs = "kotlin/collections/MapsKt")
class MapsKt___MapsKt extends MapsKt__MapsKt
{
    @NotNull
    public static final <K, V> List<Pair<K, V>> toList(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver.size() == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        final Iterator iterator = $receiver.entrySet().iterator();
        if (!iterator.hasNext()) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        final Map.Entry first = (Map.Entry)iterator.next();
        if (!iterator.hasNext()) {
            final Map.Entry entry = first;
            return CollectionsKt__CollectionsJVMKt.listOf(new Pair<K, V>(entry.getKey(), entry.getValue()));
        }
        final ArrayList<Pair<Object, V>> list;
        final ArrayList result = list = new ArrayList<Pair<Object, V>>($receiver.size());
        final Map.Entry entry2 = first;
        list.add((Pair<Object, V>)new Pair<Object, Object>(entry2.getKey(), entry2.getValue()));
        do {
            final ArrayList list2 = result;
            final Map.Entry<Object, V> entry3 = iterator.next();
            list2.add(new Pair<Object, Object>(entry3.getKey(), entry3.getValue()));
        } while (iterator.hasNext());
        return (List<Pair<K, V>>)result;
    }
    
    @NotNull
    public static final <K, V, R> List<R> flatMap(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc             "transform"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3       
        //    14: new             Ljava/util/ArrayList;
        //    17: dup            
        //    18: invokespecial   java/util/ArrayList.<init>:()V
        //    21: checkcast       Ljava/util/Collection;
        //    24: astore          destination$iv
        //    26: aload_3         /* $receiver$iv */
        //    27: astore          5
        //    29: aload           5
        //    31: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    36: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    41: astore          6
        //    43: aload           6
        //    45: invokeinterface java/util/Iterator.hasNext:()Z
        //    50: ifeq            89
        //    53: aload           6
        //    55: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    60: checkcast       Ljava/util/Map$Entry;
        //    63: astore          element$iv
        //    65: aload_1         /* transform */
        //    66: aload           element$iv
        //    68: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    73: checkcast       Ljava/lang/Iterable;
        //    76: astore          list$iv
        //    78: aload           destination$iv
        //    80: aload           list$iv
        //    82: invokestatic    kotlin/collections/CollectionsKt.addAll:(Ljava/util/Collection;Ljava/lang/Iterable;)Z
        //    85: pop            
        //    86: goto            43
        //    89: aload           destination$iv
        //    91: checkcast       Ljava/util/List;
        //    94: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;R:Ljava/lang/Object;>(Ljava/util/Map<+TK;+TV;>;Lkotlin/jvm/functions/Function1<-Ljava/util/Map$Entry<+TK;+TV;>;+Ljava/lang/Iterable<+TR;>;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 02 FF 00 2B 00 07 07 00 47 07 00 92 00 07 00 47 07 00 90 07 00 94 07 00 5D 00 00 2D
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapTo(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final C destination, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (final Map.Entry element : $receiver.entrySet()) {
            final Iterable list = (Iterable)transform.invoke((Object)element);
            CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)destination, (Iterable<?>)list);
        }
        return destination;
    }
    
    @NotNull
    public static final <K, V, R> List<R> map(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc             "transform"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3       
        //    14: new             Ljava/util/ArrayList;
        //    17: dup            
        //    18: aload_0         /* $receiver */
        //    19: invokeinterface java/util/Map.size:()I
        //    24: invokespecial   java/util/ArrayList.<init>:(I)V
        //    27: checkcast       Ljava/util/Collection;
        //    30: astore          destination$iv
        //    32: aload_3         /* $receiver$iv */
        //    33: astore          5
        //    35: aload           5
        //    37: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    42: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    47: astore          6
        //    49: aload           6
        //    51: invokeinterface java/util/Iterator.hasNext:()Z
        //    56: ifeq            90
        //    59: aload           6
        //    61: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    66: checkcast       Ljava/util/Map$Entry;
        //    69: astore          item$iv
        //    71: aload           destination$iv
        //    73: aload_1         /* transform */
        //    74: aload           item$iv
        //    76: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    81: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    86: pop            
        //    87: goto            49
        //    90: aload           destination$iv
        //    92: checkcast       Ljava/util/List;
        //    95: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;R:Ljava/lang/Object;>(Ljava/util/Map<+TK;+TV;>;Lkotlin/jvm/functions/Function1<-Ljava/util/Map$Entry<+TK;+TV;>;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 02 FF 00 31 00 07 07 00 47 07 00 92 00 07 00 47 07 00 90 07 00 47 07 00 5D 00 00 28
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, V, R> List<R> mapNotNull(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* transform */
        //     7: ldc             "transform"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3       
        //    14: new             Ljava/util/ArrayList;
        //    17: dup            
        //    18: invokespecial   java/util/ArrayList.<init>:()V
        //    21: checkcast       Ljava/util/Collection;
        //    24: astore          destination$iv
        //    26: aload_3         /* $receiver$iv */
        //    27: astore          $receiver$iv$iv
        //    29: aload           $receiver$iv$iv
        //    31: astore          6
        //    33: aload           6
        //    35: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    40: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    45: astore          7
        //    47: aload           7
        //    49: invokeinterface java/util/Iterator.hasNext:()Z
        //    54: ifeq            109
        //    57: aload           7
        //    59: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    64: checkcast       Ljava/util/Map$Entry;
        //    67: astore          element$iv$iv
        //    69: aload           element$iv$iv
        //    71: astore          element$iv
        //    73: aload_1         /* transform */
        //    74: aload           element$iv
        //    76: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    81: dup            
        //    82: ifnull          104
        //    85: astore          10
        //    87: aload           10
        //    89: astore          it$iv
        //    91: aload           destination$iv
        //    93: aload           it$iv
        //    95: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   100: pop            
        //   101: goto            105
        //   104: pop            
        //   105: nop            
        //   106: goto            47
        //   109: nop            
        //   110: aload           destination$iv
        //   112: checkcast       Ljava/util/List;
        //   115: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;R:Ljava/lang/Object;>(Ljava/util/Map<+TK;+TV;>;Lkotlin/jvm/functions/Function1<-Ljava/util/Map$Entry<+TK;+TV;>;+TR;>;)Ljava/util/List<TR;>;
        //    StackMapTable: 00 04 FF 00 2F 00 08 07 00 47 07 00 92 00 07 00 47 07 00 90 07 00 47 07 00 47 07 00 5D 00 00 FF 00 38 00 0A 07 00 47 07 00 92 00 07 00 47 07 00 90 07 00 47 07 00 47 07 00 5D 07 00 67 07 00 67 00 01 07 00 94 00 F9 00 03
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapNotNullTo(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final C destination, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        final Map $receiver$iv = $receiver;
        for (final Map.Entry element : $receiver$iv.entrySet()) {
            final Map.Entry element$iv = element;
            final R invoke = (R)transform.invoke((Object)element);
            if (invoke != null) {
                final Object it = invoke;
                destination.add((Object)it);
            }
        }
        return destination;
    }
    
    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapTo(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final C destination, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (final Map.Entry item : $receiver.entrySet()) {
            destination.add((Object)transform.invoke((Object)item));
        }
        return destination;
    }
    
    public static final <K, V> boolean all(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($receiver.isEmpty()) {
            return true;
        }
        for (final Map.Entry element : $receiver.entrySet()) {
            if (!predicate.invoke((Object)element)) {
                return false;
            }
        }
        return true;
    }
    
    public static final <K, V> boolean any(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return !$receiver.isEmpty();
    }
    
    public static final <K, V> boolean any(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($receiver.isEmpty()) {
            return false;
        }
        for (final Map.Entry element : $receiver.entrySet()) {
            if (predicate.invoke((Object)element)) {
                return true;
            }
        }
        return false;
    }
    
    @InlineOnly
    private static final <K, V> int count(@NotNull final Map<? extends K, ? extends V> $receiver) {
        return $receiver.size();
    }
    
    public static final <K, V> int count(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($receiver.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (final Map.Entry element : $receiver.entrySet()) {
            if (predicate.invoke((Object)element)) {
                ++count;
            }
        }
        return count;
    }
    
    @HidesMembers
    public static final <K, V> void forEach(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        for (final Map.Entry element : $receiver.entrySet()) {
            action.invoke((Object)element);
        }
    }
    
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxBy(@NotNull final Map<? extends K, ? extends V> $receiver, final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        final Iterable $receiver$iv = $receiver.entrySet();
        final Iterator iterator$iv = $receiver$iv.iterator();
        Object o;
        if (!iterator$iv.hasNext()) {
            o = null;
        }
        else {
            Object maxElem$iv = iterator$iv.next();
            Comparable maxValue$iv = (Comparable)selector.invoke((Object)maxElem$iv);
            while (iterator$iv.hasNext()) {
                final Object e$iv = iterator$iv.next();
                final Comparable v$iv = (Comparable)selector.invoke((Object)e$iv);
                if (maxValue$iv.compareTo(v$iv) < 0) {
                    maxElem$iv = e$iv;
                    maxValue$iv = v$iv;
                }
            }
            o = maxElem$iv;
        }
        return (Map.Entry<K, V>)o;
    }
    
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> maxWith(@NotNull final Map<? extends K, ? extends V> $receiver, final Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        return (Map.Entry<K, V>)CollectionsKt___CollectionsKt.maxWith((Iterable<? extends Map.Entry>)$receiver.entrySet(), (Comparator<? super Map.Entry>)comparator);
    }
    
    @Nullable
    public static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minBy(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        final Iterable $receiver$iv = $receiver.entrySet();
        final Iterator iterator$iv = $receiver$iv.iterator();
        Object o;
        if (!iterator$iv.hasNext()) {
            o = null;
        }
        else {
            Object minElem$iv = iterator$iv.next();
            Comparable minValue$iv = (Comparable)selector.invoke((Object)minElem$iv);
            while (iterator$iv.hasNext()) {
                final Object e$iv = iterator$iv.next();
                final Comparable v$iv = (Comparable)selector.invoke((Object)e$iv);
                if (minValue$iv.compareTo(v$iv) > 0) {
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                }
            }
            o = minElem$iv;
        }
        return (Map.Entry<K, V>)o;
    }
    
    @Nullable
    public static final <K, V> Map.Entry<K, V> minWith(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (Map.Entry<K, V>)CollectionsKt___CollectionsKt.minWith((Iterable<? extends Map.Entry>)$receiver.entrySet(), (Comparator<? super Map.Entry>)comparator);
    }
    
    public static final <K, V> boolean none(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return $receiver.isEmpty();
    }
    
    public static final <K, V> boolean none(@NotNull final Map<? extends K, ? extends V> $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($receiver.isEmpty()) {
            return true;
        }
        for (final Map.Entry element : $receiver.entrySet()) {
            if (predicate.invoke((Object)element)) {
                return false;
            }
        }
        return true;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEach(@NotNull final M $receiver, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        final Map $receiver2 = $receiver;
        for (final Map.Entry element : $receiver2.entrySet()) {
            action.invoke((Object)element);
        }
        return $receiver;
    }
    
    @InlineOnly
    private static final <K, V> Iterable<Map.Entry<K, V>> asIterable(@NotNull final Map<? extends K, ? extends V> $receiver) {
        return (Iterable<Map.Entry<K, V>>)$receiver.entrySet();
    }
    
    @NotNull
    public static final <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull final Map<? extends K, ? extends V> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return CollectionsKt___CollectionsKt.asSequence((Iterable<? extends Map.Entry<K, V>>)$receiver.entrySet());
    }
    
    public MapsKt___MapsKt() {
    }
}
