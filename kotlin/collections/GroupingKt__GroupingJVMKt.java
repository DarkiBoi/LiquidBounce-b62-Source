// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.internal.InlineOnly;
import kotlin.PublishedApi;
import java.util.Iterator;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.SinceKotlin;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aW\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\b¨\u0006\r" }, d2 = { "eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", "V", "f", "Lkotlin/Function1;", "", "kotlin-stdlib" }, xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingJVMKt
{
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K> Map<K, Integer> eachCount(@NotNull final Grouping<T, ? extends K> $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: astore_1       
        //     8: new             Ljava/util/LinkedHashMap;
        //    11: dup            
        //    12: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    15: checkcast       Ljava/util/Map;
        //    18: astore_2       
        //    19: nop            
        //    20: aload_1         /* $receiver$iv */
        //    21: astore_3        /* $receiver$iv$iv */
        //    22: aload_3         /* $receiver$iv$iv */
        //    23: invokeinterface kotlin/collections/Grouping.sourceIterator:()Ljava/util/Iterator;
        //    28: astore          4
        //    30: aload           4
        //    32: astore          5
        //    34: aload           5
        //    36: invokeinterface java/util/Iterator.hasNext:()Z
        //    41: ifeq            196
        //    44: aload           5
        //    46: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    51: astore          e$iv$iv
        //    53: aload_3         /* $receiver$iv$iv */
        //    54: aload           e$iv$iv
        //    56: invokeinterface kotlin/collections/Grouping.keyOf:(Ljava/lang/Object;)Ljava/lang/Object;
        //    61: astore          key$iv$iv
        //    63: aload_2         /* destination$iv */
        //    64: aload           key$iv$iv
        //    66: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    71: astore          accumulator$iv$iv
        //    73: aload_2         /* destination$iv */
        //    74: astore          8
        //    76: aload           key$iv$iv
        //    78: aload           accumulator$iv$iv
        //    80: aload           e$iv$iv
        //    82: aload           accumulator$iv$iv
        //    84: ifnonnull       102
        //    87: aload_2         /* destination$iv */
        //    88: aload           key$iv$iv
        //    90: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    95: ifne            102
        //    98: iconst_1       
        //    99: goto            103
        //   102: iconst_0       
        //   103: istore          9
        //   105: astore          10
        //   107: astore          11
        //   109: astore          key$iv
        //   111: aload           key$iv
        //   113: iload           first$iv
        //   115: ifeq            144
        //   118: aload           key$iv
        //   120: aload           e$iv
        //   122: astore          13
        //   124: astore          14
        //   126: astore          23
        //   128: new             Lkotlin/jvm/internal/Ref$IntRef;
        //   131: dup            
        //   132: invokespecial   kotlin/jvm/internal/Ref$IntRef.<init>:()V
        //   135: astore          24
        //   137: aload           23
        //   139: aload           24
        //   141: goto            146
        //   144: aload           acc$iv
        //   146: aload           e$iv
        //   148: astore          15
        //   150: checkcast       Lkotlin/jvm/internal/Ref$IntRef;
        //   153: astore          13
        //   155: astore          $noName_0
        //   157: aload           acc
        //   159: astore          16
        //   161: aload           16
        //   163: astore          $receiver
        //   165: aload           $receiver
        //   167: dup            
        //   168: getfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //   171: iconst_1       
        //   172: iadd           
        //   173: putfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //   176: aload           16
        //   178: nop            
        //   179: astore          null
        //   181: aload           8
        //   183: aload           key$iv$iv
        //   185: aload           12
        //   187: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   192: pop            
        //   193: goto            34
        //   196: aload_2         /* destination$iv */
        //   197: nop            
        //   198: astore_1        /* $receiver$iv */
        //   199: aload_1        
        //   200: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   205: checkcast       Ljava/lang/Iterable;
        //   208: astore_2       
        //   209: aload_2        
        //   210: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   215: astore_3       
        //   216: aload_3        
        //   217: invokeinterface java/util/Iterator.hasNext:()Z
        //   222: ifeq            296
        //   225: aload_3        
        //   226: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   231: astore          4
        //   233: aload           4
        //   235: checkcast       Ljava/util/Map$Entry;
        //   238: astore          5
        //   240: aload           5
        //   242: dup            
        //   243: ifnonnull       256
        //   246: new             Lkotlin/TypeCastException;
        //   249: dup            
        //   250: ldc             "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K, R>"
        //   252: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   255: athrow         
        //   256: invokestatic    kotlin/jvm/internal/TypeIntrinsics.asMutableMapEntry:(Ljava/lang/Object;)Ljava/util/Map$Entry;
        //   259: aload           5
        //   261: astore          6
        //   263: astore          23
        //   265: aload           it
        //   267: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   272: checkcast       Lkotlin/jvm/internal/Ref$IntRef;
        //   275: getfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //   278: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   281: astore          24
        //   283: aload           23
        //   285: aload           24
        //   287: invokeinterface java/util/Map$Entry.setValue:(Ljava/lang/Object;)Ljava/lang/Object;
        //   292: pop            
        //   293: goto            216
        //   296: aload_1        
        //   297: invokestatic    kotlin/jvm/internal/TypeIntrinsics.asMutableMap:(Ljava/lang/Object;)Ljava/util/Map;
        //   300: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;K:Ljava/lang/Object;>(Lkotlin/collections/Grouping<TT;+TK;>;)Ljava/util/Map<TK;Ljava/lang/Integer;>;
        //    StackMapTable: 00 09 FF 00 22 00 06 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 00 00 FF 00 43 00 09 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 07 00 04 07 00 04 07 00 34 00 03 07 00 04 07 00 04 07 00 04 FF 00 00 00 09 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 07 00 04 07 00 04 07 00 34 00 04 07 00 04 07 00 04 07 00 04 01 FF 00 28 00 0D 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 07 00 04 07 00 04 07 00 34 01 07 00 04 07 00 04 07 00 04 00 01 07 00 04 FF 00 01 00 0D 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 07 00 04 07 00 04 07 00 34 01 07 00 04 07 00 04 07 00 04 00 02 07 00 04 07 00 04 FF 00 31 00 06 07 00 36 07 00 36 07 00 34 07 00 36 07 00 04 07 00 3C 00 00 FF 00 13 00 06 07 00 36 07 00 34 07 00 60 07 00 3C 07 00 04 07 00 04 00 00 FF 00 27 00 06 07 00 36 07 00 34 07 00 60 07 00 3C 07 00 04 07 00 65 00 01 07 00 65 FF 00 27 00 06 07 00 36 07 00 34 07 00 60 07 00 3C 07 00 04 07 00 04 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @PublishedApi
    @InlineOnly
    private static final <K, V, R> Map<K, R> mapValuesInPlace(@NotNull final Map<K, V> $receiver, final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> f) {
        final Iterable $receiver$iv = $receiver.entrySet();
        for (final Object element$iv : $receiver$iv) {
            final Map.Entry obj;
            final Map.Entry it = obj = (Map.Entry)element$iv;
            if (obj == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K, R>");
            }
            TypeIntrinsics.asMutableMapEntry(obj).setValue(f.invoke((Object)it));
        }
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, R>");
        }
        return (Map<K, R>)TypeIntrinsics.asMutableMap($receiver);
    }
    
    public GroupingKt__GroupingJVMKt() {
    }
}
