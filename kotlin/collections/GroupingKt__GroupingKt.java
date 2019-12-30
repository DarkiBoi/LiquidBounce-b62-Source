// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.SinceKotlin;
import java.util.Map;
import kotlin.jvm.functions.Function4;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000@\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u009b\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b\u001a´\u0001\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b¢\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0016\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00150\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u0010H\u0007¢\u0006\u0002\u0010\u0016\u001a¼\u0001\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u000526\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b\u001a|\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b¢\u0006\u0002\u0010\u001c\u001a\u00d5\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u001026\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b¢\u0006\u0002\u0010\u001e\u001a\u0090\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b¢\u0006\u0002\u0010\u001f\u001a\u0088\u0001\u0010 \u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0001\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b\u001a¡\u0001\u0010\"\u001a\u0002H\u0010\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b¢\u0006\u0002\u0010#¨\u0006$" }, d2 = { "aggregate", "", "K", "R", "T", "Lkotlin/collections/Grouping;", "operation", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "key", "accumulator", "element", "", "first", "aggregateTo", "M", "", "destination", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function4;)Ljava/util/Map;", "eachCountTo", "", "(Lkotlin/collections/Grouping;Ljava/util/Map;)Ljava/util/Map;", "fold", "initialValueSelector", "Lkotlin/Function2;", "Lkotlin/Function3;", "initialValue", "(Lkotlin/collections/Grouping;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "foldTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "(Lkotlin/collections/Grouping;Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "reduce", "S", "reduceTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "kotlin-stdlib" }, xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingKt extends GroupingKt__GroupingJVMKt
{
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> aggregate(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* operation */
        //     7: ldc             "operation"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3       
        //    14: new             Ljava/util/LinkedHashMap;
        //    17: dup            
        //    18: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    21: checkcast       Ljava/util/Map;
        //    24: astore          4
        //    26: nop            
        //    27: aload_3         /* $receiver$iv */
        //    28: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    33: astore          5
        //    35: aload           5
        //    37: astore          6
        //    39: aload           6
        //    41: invokeinterface java/util/Iterator.hasNext:()Z
        //    46: ifeq            137
        //    49: aload           6
        //    51: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    56: astore          e$iv
        //    58: aload_3         /* $receiver$iv */
        //    59: aload           e$iv
        //    61: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    66: astore          key$iv
        //    68: aload           destination$iv
        //    70: aload           key$iv
        //    72: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    77: astore          accumulator$iv
        //    79: aload           destination$iv
        //    81: astore          9
        //    83: aload_1         /* operation */
        //    84: aload           key$iv
        //    86: aload           accumulator$iv
        //    88: aload           e$iv
        //    90: aload           accumulator$iv
        //    92: ifnonnull       111
        //    95: aload           destination$iv
        //    97: aload           key$iv
        //    99: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   104: ifne            111
        //   107: iconst_1       
        //   108: goto            112
        //   111: iconst_0       
        //   112: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   115: invokeinterface kotlin/jvm/functions/Function4.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   120: astore          10
        //   122: aload           9
        //   124: aload           key$iv
        //   126: aload           10
        //   128: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   133: pop            
        //   134: goto            39
        //   137: aload           destination$iv
        //   139: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;R:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;Lkotlin/jvm/functions/Function4<-TK;-TR;-TT;-Ljava/lang/Boolean;+TR;>;)Ljava/util/Map<TK;TR;>;
        //    StackMapTable: 00 04 FF 00 27 00 07 07 00 4E 07 00 54 00 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00 FF 00 47 00 0A 07 00 4E 07 00 54 00 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 54 07 00 56 07 00 56 07 00 56 FF 00 00 00 0A 07 00 4E 07 00 54 00 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 05 07 00 54 07 00 56 07 00 56 07 00 56 01 F8 00 18
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M aggregateTo(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final M destination, @NotNull final Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        final Iterator<T> sourceIterator = $receiver.sourceIterator();
        while (sourceIterator.hasNext()) {
            final Object e = sourceIterator.next();
            final Object key = $receiver.keyOf((T)e);
            final Object accumulator = ((Map<K, Object>)destination).get(key);
            destination.put((Object)key, (R)operation.invoke((Object)key, (Object)accumulator, (Object)e, Boolean.valueOf(accumulator == null && !destination.containsKey(key))));
        }
        return destination;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final Function2<? super K, ? super T, ? extends R> initialValueSelector, @NotNull final Function3<? super K, ? super R, ? super T, ? extends R> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* initialValueSelector */
        //     7: ldc             "initialValueSelector"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* operation */
        //    13: ldc             "operation"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: aload_0         /* $receiver */
        //    19: astore          $receiver$iv
        //    21: aload           $receiver$iv
        //    23: astore          5
        //    25: new             Ljava/util/LinkedHashMap;
        //    28: dup            
        //    29: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    32: checkcast       Ljava/util/Map;
        //    35: astore          6
        //    37: nop            
        //    38: aload           $receiver$iv$iv
        //    40: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    45: astore          7
        //    47: aload           7
        //    49: astore          8
        //    51: aload           8
        //    53: invokeinterface java/util/Iterator.hasNext:()Z
        //    58: ifeq            179
        //    61: aload           8
        //    63: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    68: astore          e$iv$iv
        //    70: aload           $receiver$iv$iv
        //    72: aload           e$iv$iv
        //    74: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    79: astore          key$iv$iv
        //    81: aload           destination$iv$iv
        //    83: aload           key$iv$iv
        //    85: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    90: astore          accumulator$iv$iv
        //    92: aload           destination$iv$iv
        //    94: astore          11
        //    96: aload           key$iv$iv
        //    98: aload           accumulator$iv$iv
        //   100: aload           e$iv$iv
        //   102: aload           accumulator$iv$iv
        //   104: ifnonnull       123
        //   107: aload           destination$iv$iv
        //   109: aload           key$iv$iv
        //   111: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   116: ifne            123
        //   119: iconst_1       
        //   120: goto            124
        //   123: iconst_0       
        //   124: istore          12
        //   126: astore          13
        //   128: astore          14
        //   130: astore          key
        //   132: aload_2         /* operation */
        //   133: aload           key
        //   135: iload           first
        //   137: ifeq            153
        //   140: aload_1         /* initialValueSelector */
        //   141: aload           key
        //   143: aload           e
        //   145: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   150: goto            155
        //   153: aload           acc
        //   155: aload           e
        //   157: invokeinterface kotlin/jvm/functions/Function3.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   162: astore          17
        //   164: aload           11
        //   166: aload           key$iv$iv
        //   168: aload           17
        //   170: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   175: pop            
        //   176: goto            51
        //   179: aload           destination$iv$iv
        //   181: nop            
        //   182: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;R:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;Lkotlin/jvm/functions/Function2<-TK;-TT;+TR;>;Lkotlin/jvm/functions/Function3<-TK;-TR;-TT;+TR;>;)Ljava/util/Map<TK;TR;>;
        //    StackMapTable: 00 06 FF 00 33 00 09 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00 FF 00 47 00 0C 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0C 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 1C 00 10 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 02 07 00 8D 07 00 56 FF 00 01 00 10 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 03 07 00 8D 07 00 56 07 00 56 FF 00 17 00 09 07 00 4E 07 00 8B 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final M destination, @NotNull final Function2<? super K, ? super T, ? extends R> initialValueSelector, @NotNull final Function3<? super K, ? super R, ? super T, ? extends R> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc             "destination"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* initialValueSelector */
        //    13: ldc             "initialValueSelector"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: aload_3         /* operation */
        //    19: ldc             "operation"
        //    21: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    24: aload_0         /* $receiver */
        //    25: astore          $receiver$iv
        //    27: aload           $receiver$iv
        //    29: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    34: astore          6
        //    36: aload           6
        //    38: astore          7
        //    40: aload           7
        //    42: invokeinterface java/util/Iterator.hasNext:()Z
        //    47: ifeq            165
        //    50: aload           7
        //    52: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    57: astore          e$iv
        //    59: aload           $receiver$iv
        //    61: aload           e$iv
        //    63: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    68: astore          key$iv
        //    70: aload_1         /* destination */
        //    71: aload           key$iv
        //    73: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    78: astore          accumulator$iv
        //    80: aload_1         /* destination */
        //    81: astore          10
        //    83: aload           key$iv
        //    85: aload           accumulator$iv
        //    87: aload           e$iv
        //    89: aload           accumulator$iv
        //    91: ifnonnull       109
        //    94: aload_1         /* destination */
        //    95: aload           key$iv
        //    97: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   102: ifne            109
        //   105: iconst_1       
        //   106: goto            110
        //   109: iconst_0       
        //   110: istore          11
        //   112: astore          12
        //   114: astore          13
        //   116: astore          key
        //   118: aload_3         /* operation */
        //   119: aload           key
        //   121: iload           first
        //   123: ifeq            139
        //   126: aload_2         /* initialValueSelector */
        //   127: aload           key
        //   129: aload           e
        //   131: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   136: goto            141
        //   139: aload           acc
        //   141: aload           e
        //   143: invokeinterface kotlin/jvm/functions/Function3.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   148: astore          null
        //   150: aload           10
        //   152: aload           key$iv
        //   154: aload           14
        //   156: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   161: pop            
        //   162: goto            40
        //   165: aload_1         /* destination */
        //   166: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;R:Ljava/lang/Object;M::Ljava/util/Map<-TK;TR;>;>(Lkotlin/collections/Grouping<TT;+TK;>;TM;Lkotlin/jvm/functions/Function2<-TK;-TT;+TR;>;Lkotlin/jvm/functions/Function3<-TK;-TR;-TT;+TR;>;)TM;
        //    StackMapTable: 00 06 FF 00 28 00 08 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 00 00 FF 00 44 00 0B 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0B 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 1C 00 0F 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 02 07 00 8D 07 00 56 FF 00 01 00 0F 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 03 07 00 8D 07 00 56 07 00 56 FF 00 17 00 08 07 00 4E 07 00 4C 07 00 8B 07 00 8D 00 07 00 4E 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull final Grouping<T, ? extends K> $receiver, final R initialValue, @NotNull final Function2<? super R, ? super T, ? extends R> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_2         /* operation */
        //     7: ldc             "operation"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore          $receiver$iv
        //    15: aload           $receiver$iv
        //    17: astore          5
        //    19: new             Ljava/util/LinkedHashMap;
        //    22: dup            
        //    23: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    26: checkcast       Ljava/util/Map;
        //    29: astore          6
        //    31: nop            
        //    32: aload           $receiver$iv$iv
        //    34: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    39: astore          7
        //    41: aload           7
        //    43: astore          8
        //    45: aload           8
        //    47: invokeinterface java/util/Iterator.hasNext:()Z
        //    52: ifeq            162
        //    55: aload           8
        //    57: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    62: astore          e$iv$iv
        //    64: aload           $receiver$iv$iv
        //    66: aload           e$iv$iv
        //    68: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    73: astore          key$iv$iv
        //    75: aload           destination$iv$iv
        //    77: aload           key$iv$iv
        //    79: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    84: astore          accumulator$iv$iv
        //    86: aload           destination$iv$iv
        //    88: astore          11
        //    90: aload           key$iv$iv
        //    92: aload           accumulator$iv$iv
        //    94: aload           e$iv$iv
        //    96: aload           accumulator$iv$iv
        //    98: ifnonnull       117
        //   101: aload           destination$iv$iv
        //   103: aload           key$iv$iv
        //   105: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   110: ifne            117
        //   113: iconst_1       
        //   114: goto            118
        //   117: iconst_0       
        //   118: istore          12
        //   120: astore          13
        //   122: astore          14
        //   124: astore          $noName_0
        //   126: aload_2         /* operation */
        //   127: iload           first
        //   129: ifeq            136
        //   132: aload_1         /* initialValue */
        //   133: goto            138
        //   136: aload           acc
        //   138: aload           e
        //   140: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   145: astore          17
        //   147: aload           11
        //   149: aload           key$iv$iv
        //   151: aload           17
        //   153: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   158: pop            
        //   159: goto            45
        //   162: aload           destination$iv$iv
        //   164: nop            
        //   165: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;R:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;TR;Lkotlin/jvm/functions/Function2<-TR;-TT;+TR;>;)Ljava/util/Map<TK;TR;>;
        //    StackMapTable: 00 06 FF 00 2D 00 09 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00 FF 00 47 00 0C 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0C 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 11 00 10 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 01 07 00 8B FF 00 01 00 10 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 02 07 00 8B 07 00 56 FF 00 17 00 09 07 00 4E 07 00 56 07 00 8B 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final M destination, final R initialValue, @NotNull final Function2<? super R, ? super T, ? extends R> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc             "destination"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_3         /* operation */
        //    13: ldc             "operation"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: aload_0         /* $receiver */
        //    19: astore          $receiver$iv
        //    21: aload           $receiver$iv
        //    23: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    28: astore          6
        //    30: aload           6
        //    32: astore          7
        //    34: aload           7
        //    36: invokeinterface java/util/Iterator.hasNext:()Z
        //    41: ifeq            148
        //    44: aload           7
        //    46: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    51: astore          e$iv
        //    53: aload           $receiver$iv
        //    55: aload           e$iv
        //    57: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    62: astore          key$iv
        //    64: aload_1         /* destination */
        //    65: aload           key$iv
        //    67: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    72: astore          accumulator$iv
        //    74: aload_1         /* destination */
        //    75: astore          10
        //    77: aload           key$iv
        //    79: aload           accumulator$iv
        //    81: aload           e$iv
        //    83: aload           accumulator$iv
        //    85: ifnonnull       103
        //    88: aload_1         /* destination */
        //    89: aload           key$iv
        //    91: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    96: ifne            103
        //    99: iconst_1       
        //   100: goto            104
        //   103: iconst_0       
        //   104: istore          11
        //   106: astore          12
        //   108: astore          13
        //   110: astore          $noName_0
        //   112: aload_3         /* operation */
        //   113: iload           first
        //   115: ifeq            122
        //   118: aload_2         /* initialValue */
        //   119: goto            124
        //   122: aload           acc
        //   124: aload           e
        //   126: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   131: astore          null
        //   133: aload           10
        //   135: aload           key$iv
        //   137: aload           14
        //   139: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   144: pop            
        //   145: goto            34
        //   148: aload_1         /* destination */
        //   149: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;R:Ljava/lang/Object;M::Ljava/util/Map<-TK;TR;>;>(Lkotlin/collections/Grouping<TT;+TK;>;TM;TR;Lkotlin/jvm/functions/Function2<-TR;-TT;+TR;>;)TM;
        //    StackMapTable: 00 06 FF 00 22 00 08 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 00 00 FF 00 44 00 0B 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0B 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 11 00 0F 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 01 07 00 8B FF 00 01 00 0F 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 02 07 00 8B 07 00 56 FF 00 17 00 08 07 00 4E 07 00 4C 07 00 56 07 00 8B 00 07 00 4E 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S, T extends S, K> Map<K, S> reduce(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final Function3<? super K, ? super S, ? super T, ? extends S> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* operation */
        //     7: ldc             "operation"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3        /* $receiver$iv */
        //    14: aload_3         /* $receiver$iv */
        //    15: astore          4
        //    17: new             Ljava/util/LinkedHashMap;
        //    20: dup            
        //    21: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    24: checkcast       Ljava/util/Map;
        //    27: astore          5
        //    29: nop            
        //    30: aload           $receiver$iv$iv
        //    32: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    37: astore          6
        //    39: aload           6
        //    41: astore          7
        //    43: aload           7
        //    45: invokeinterface java/util/Iterator.hasNext:()Z
        //    50: ifeq            163
        //    53: aload           7
        //    55: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    60: astore          e$iv$iv
        //    62: aload           $receiver$iv$iv
        //    64: aload           e$iv$iv
        //    66: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    71: astore          key$iv$iv
        //    73: aload           destination$iv$iv
        //    75: aload           key$iv$iv
        //    77: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    82: astore          accumulator$iv$iv
        //    84: aload           destination$iv$iv
        //    86: astore          10
        //    88: aload           key$iv$iv
        //    90: aload           accumulator$iv$iv
        //    92: aload           e$iv$iv
        //    94: aload           accumulator$iv$iv
        //    96: ifnonnull       115
        //    99: aload           destination$iv$iv
        //   101: aload           key$iv$iv
        //   103: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   108: ifne            115
        //   111: iconst_1       
        //   112: goto            116
        //   115: iconst_0       
        //   116: istore          11
        //   118: astore          12
        //   120: astore          13
        //   122: astore          key
        //   124: iload           first
        //   126: ifeq            134
        //   129: aload           e
        //   131: goto            146
        //   134: aload_1         /* operation */
        //   135: aload           key
        //   137: aload           acc
        //   139: aload           e
        //   141: invokeinterface kotlin/jvm/functions/Function3.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   146: astore          16
        //   148: aload           10
        //   150: aload           key$iv$iv
        //   152: aload           16
        //   154: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   159: pop            
        //   160: goto            43
        //   163: aload           destination$iv$iv
        //   165: nop            
        //   166: areturn        
        //    Signature:
        //  <S:Ljava/lang/Object;T:TS;K:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;Lkotlin/jvm/functions/Function3<-TK;-TS;-TT;+TS;>;)Ljava/util/Map<TK;TS;>; [from metadata: <S:Ljava/lang/Object;T::TS;K:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;Lkotlin/jvm/functions/Function3<-TK;-TS;-TT;+TS;>;)Ljava/util/Map<TK;TS;>;]
        //  
        //    StackMapTable: 00 06 FF 00 2B 00 08 07 00 4E 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00 FF 00 47 00 0B 07 00 4E 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0B 07 00 4E 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 11 00 0F 07 00 4E 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 00 4B 07 00 56 FF 00 10 00 08 07 00 4E 07 00 8D 00 07 00 4E 07 00 4E 07 00 4C 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S, T extends S, K, M extends Map<? super K, S>> M reduceTo(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final M destination, @NotNull final Function3<? super K, ? super S, ? super T, ? extends S> operation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc             "destination"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* operation */
        //    13: ldc             "operation"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: aload_0         /* $receiver */
        //    19: astore          $receiver$iv
        //    21: aload           $receiver$iv
        //    23: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    28: astore          5
        //    30: aload           5
        //    32: astore          6
        //    34: aload           6
        //    36: invokeinterface java/util/Iterator.hasNext:()Z
        //    41: ifeq            151
        //    44: aload           6
        //    46: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    51: astore          e$iv
        //    53: aload           $receiver$iv
        //    55: aload           e$iv
        //    57: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    62: astore          key$iv
        //    64: aload_1         /* destination */
        //    65: aload           key$iv
        //    67: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    72: astore          accumulator$iv
        //    74: aload_1         /* destination */
        //    75: astore          9
        //    77: aload           key$iv
        //    79: aload           accumulator$iv
        //    81: aload           e$iv
        //    83: aload           accumulator$iv
        //    85: ifnonnull       103
        //    88: aload_1         /* destination */
        //    89: aload           key$iv
        //    91: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    96: ifne            103
        //    99: iconst_1       
        //   100: goto            104
        //   103: iconst_0       
        //   104: istore          10
        //   106: astore          11
        //   108: astore          12
        //   110: astore          key
        //   112: iload           first
        //   114: ifeq            122
        //   117: aload           e
        //   119: goto            134
        //   122: aload_2         /* operation */
        //   123: aload           key
        //   125: aload           acc
        //   127: aload           e
        //   129: invokeinterface kotlin/jvm/functions/Function3.invoke:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   134: astore          null
        //   136: aload           9
        //   138: aload           key$iv
        //   140: aload           13
        //   142: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   147: pop            
        //   148: goto            34
        //   151: aload_1         /* destination */
        //   152: areturn        
        //    Signature:
        //  <S:Ljava/lang/Object;T:TS;K:Ljava/lang/Object;M::Ljava/util/Map<-TK;TS;>;>(Lkotlin/collections/Grouping<TT;+TK;>;TM;Lkotlin/jvm/functions/Function3<-TK;-TS;-TT;+TS;>;)TM; [from metadata: <S:Ljava/lang/Object;T::TS;K:Ljava/lang/Object;M::Ljava/util/Map<-TK;TS;>;>(Lkotlin/collections/Grouping<TT;+TK;>;TM;Lkotlin/jvm/functions/Function3<-TK;-TS;-TT;+TS;>;)TM;]
        //  
        //    StackMapTable: 00 06 FF 00 22 00 07 07 00 4E 07 00 4C 07 00 8D 00 07 00 4E 07 00 56 07 00 58 00 00 FF 00 44 00 0A 07 00 4E 07 00 4C 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0A 07 00 4E 07 00 4C 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 11 00 0E 07 00 4E 07 00 4C 07 00 8D 00 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 00 4B 07 00 56 FF 00 10 00 07 07 00 4E 07 00 4C 07 00 8D 00 07 00 4E 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, M extends Map<? super K, Integer>> M eachCountTo(@NotNull final Grouping<T, ? extends K> $receiver, @NotNull final M destination) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* destination */
        //     7: ldc             "destination"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_2       
        //    14: iconst_0       
        //    15: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    18: astore_3        /* initialValue$iv */
        //    19: aload_2         /* $receiver$iv */
        //    20: astore          $receiver$iv$iv
        //    22: aload           $receiver$iv$iv
        //    24: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    29: astore          5
        //    31: aload           5
        //    33: astore          6
        //    35: aload           6
        //    37: invokeinterface java/util/Iterator.hasNext:()Z
        //    42: ifeq            160
        //    45: aload           6
        //    47: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    52: astore          e$iv$iv
        //    54: aload           $receiver$iv$iv
        //    56: aload           e$iv$iv
        //    58: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    63: astore          key$iv$iv
        //    65: aload_1         /* destination */
        //    66: aload           key$iv$iv
        //    68: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    73: astore          accumulator$iv$iv
        //    75: aload_1         /* destination */
        //    76: astore          9
        //    78: aload           key$iv$iv
        //    80: aload           accumulator$iv$iv
        //    82: aload           e$iv$iv
        //    84: aload           accumulator$iv$iv
        //    86: ifnonnull       104
        //    89: aload_1         /* destination */
        //    90: aload           key$iv$iv
        //    92: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    97: ifne            104
        //   100: iconst_1       
        //   101: goto            105
        //   104: iconst_0       
        //   105: istore          10
        //   107: astore          11
        //   109: astore          12
        //   111: astore          $noName_0$iv
        //   113: iload           first$iv
        //   115: ifeq            122
        //   118: aload_3         /* initialValue$iv */
        //   119: goto            124
        //   122: aload           acc$iv
        //   124: aload           e$iv
        //   126: astore          14
        //   128: checkcast       Ljava/lang/Number;
        //   131: invokevirtual   java/lang/Number.intValue:()I
        //   134: istore          acc
        //   136: iload           acc
        //   138: iconst_1       
        //   139: iadd           
        //   140: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   143: astore          null
        //   145: aload           9
        //   147: aload           key$iv$iv
        //   149: aload           13
        //   151: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   156: pop            
        //   157: goto            35
        //   160: aload_1         /* destination */
        //   161: nop            
        //   162: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;M::Ljava/util/Map<-TK;Ljava/lang/Integer;>;>(Lkotlin/collections/Grouping<TT;+TK;>;TM;)TM;
        //    StackMapTable: 00 06 FF 00 23 00 07 07 00 4E 07 00 4C 07 00 4E 07 00 A7 07 00 4E 07 00 56 07 00 58 00 00 FF 00 44 00 0A 07 00 4E 07 00 4C 07 00 4E 07 00 A7 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 03 07 00 56 07 00 56 07 00 56 FF 00 00 00 0A 07 00 4E 07 00 4C 07 00 4E 07 00 A7 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 00 04 07 00 56 07 00 56 07 00 56 01 FF 00 10 00 0E 07 00 4E 07 00 4C 07 00 4E 07 00 A7 07 00 4E 07 00 56 07 00 58 07 00 56 07 00 56 07 00 4C 01 07 00 56 07 00 56 07 00 56 00 00 41 07 00 56 FF 00 23 00 07 07 00 4E 07 00 4C 07 00 4E 07 00 A7 07 00 4E 07 00 56 07 00 58 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public GroupingKt__GroupingKt() {
    }
}
