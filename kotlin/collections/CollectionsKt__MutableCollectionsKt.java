// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.SinceKotlin;
import kotlin.random.Random;
import java.util.HashSet;
import kotlin.jvm.functions.Function1;
import java.util.Iterator;
import kotlin.sequences.Sequence;
import kotlin.jvm.internal.Intrinsics;
import kotlin.DeprecationLevel;
import kotlin.ReplaceWith;
import kotlin.Deprecated;
import java.util.List;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000^\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a(\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0016\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0018\u001a&\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0087\b¢\u0006\u0002\u0010\u001b\u001a-\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001c\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a\u0015\u0010\u001f\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b \u001a \u0010!\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\"\u001a\u00020#H\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00020%\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00072\u0006\u0010\"\u001a\u00020#H\u0007¨\u0006&" }, d2 = { "addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "shuffle", "random", "Lkotlin/random/Random;", "shuffled", "", "kotlin-stdlib" }, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt
{
    @InlineOnly
    private static final <T> boolean remove(@NotNull final Collection<? extends T> $receiver, final T element) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection($receiver).remove(element);
    }
    
    @InlineOnly
    private static final <T> boolean removeAll(@NotNull final Collection<? extends T> $receiver, final Collection<? extends T> elements) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection($receiver).removeAll(elements);
    }
    
    @InlineOnly
    private static final <T> boolean retainAll(@NotNull final Collection<? extends T> $receiver, final Collection<? extends T> elements) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection($receiver).retainAll(elements);
    }
    
    @Deprecated(message = "Use removeAt(index) instead.", replaceWith = @ReplaceWith(imports = {}, expression = "removeAt(index)"), level = DeprecationLevel.ERROR)
    @InlineOnly
    @java.lang.Deprecated
    private static final <T> T remove(@NotNull final List<T> $receiver, final int index) {
        return $receiver.remove(index);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> $receiver, final T element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        $receiver.add(element);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> $receiver, final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        addAll((Collection<? super Object>)$receiver, (Iterable<?>)elements);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> $receiver, final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        addAll($receiver, elements);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> $receiver, final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        addAll((Collection<? super Object>)$receiver, (Sequence<?>)elements);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> $receiver, final T element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        $receiver.remove(element);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> $receiver, final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        removeAll((Collection<? super Object>)$receiver, (Iterable<?>)elements);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> $receiver, final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        removeAll($receiver, elements);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> $receiver, final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        removeAll((Collection<? super Object>)$receiver, (Sequence<?>)elements);
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> $receiver, @NotNull final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        if (elements instanceof Collection) {
            return $receiver.addAll((Collection<? extends T>)elements);
        }
        boolean result = false;
        for (final Object item : elements) {
            if ($receiver.add((Object)item)) {
                result = true;
            }
        }
        return result;
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> $receiver, @NotNull final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        boolean result = false;
        for (final Object item : elements) {
            if ($receiver.add((Object)item)) {
                result = true;
            }
        }
        return result;
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> $receiver, @NotNull final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return $receiver.addAll((Collection<? extends T>)ArraysKt___ArraysJvmKt.asList(elements));
    }
    
    public static final <T> boolean removeAll(@NotNull final Iterable<? extends T> $receiver, @NotNull final Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable<?>)$receiver, (Function1<? super Object, Boolean>)predicate, true);
    }
    
    public static final <T> boolean retainAll(@NotNull final Iterable<? extends T> $receiver, @NotNull final Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable<?>)$receiver, (Function1<? super Object, Boolean>)predicate, false);
    }
    
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull final Iterable<? extends T> $receiver, final Function1<? super T, Boolean> predicate, final boolean predicateResultToRemove) {
        boolean result = false;
        final Iterator $receiver2 = $receiver.iterator();
        while ($receiver2.hasNext()) {
            if (predicate.invoke($receiver2.next()) == predicateResultToRemove) {
                $receiver2.remove();
                result = true;
            }
        }
        return result;
    }
    
    public static final <T> boolean removeAll(@NotNull final List<T> $receiver, @NotNull final Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt($receiver, predicate, true);
    }
    
    public static final <T> boolean retainAll(@NotNull final List<T> $receiver, @NotNull final Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt($receiver, predicate, false);
    }
    
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull final List<T> $receiver, final Function1<? super T, Boolean> predicate, final boolean predicateResultToRemove) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Ljava/util/RandomAccess;
        //     4: ifne            31
        //     7: aload_0         /* $receiver */
        //     8: dup            
        //     9: ifnonnull       22
        //    12: new             Lkotlin/TypeCastException;
        //    15: dup            
        //    16: ldc             "null cannot be cast to non-null type kotlin.collections.MutableIterable<T>"
        //    18: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    21: athrow         
        //    22: invokestatic    kotlin/jvm/internal/TypeIntrinsics.asMutableIterable:(Ljava/lang/Object;)Ljava/lang/Iterable;
        //    25: aload_1         /* predicate */
        //    26: iload_2         /* predicateResultToRemove */
        //    27: invokestatic    kotlin/collections/CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt:(Ljava/lang/Iterable;Lkotlin/jvm/functions/Function1;Z)Z
        //    30: ireturn        
        //    31: iconst_0       
        //    32: istore_3        /* writeIndex */
        //    33: iconst_0       
        //    34: istore          4
        //    36: aload_0         /* $receiver */
        //    37: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //    40: istore          5
        //    42: iload           4
        //    44: iload           5
        //    46: if_icmpgt       112
        //    49: aload_0         /* $receiver */
        //    50: iload           readIndex
        //    52: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    57: astore          element
        //    59: aload_1         /* predicate */
        //    60: aload           element
        //    62: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    67: checkcast       Ljava/lang/Boolean;
        //    70: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    73: iload_2         /* predicateResultToRemove */
        //    74: if_icmpne       80
        //    77: goto            99
        //    80: iload_3         /* writeIndex */
        //    81: iload           readIndex
        //    83: if_icmpeq       96
        //    86: aload_0         /* $receiver */
        //    87: iload_3         /* writeIndex */
        //    88: aload           element
        //    90: invokeinterface java/util/List.set:(ILjava/lang/Object;)Ljava/lang/Object;
        //    95: pop            
        //    96: iinc            writeIndex, 1
        //    99: iload           readIndex
        //   101: iload           5
        //   103: if_icmpeq       112
        //   106: iinc            readIndex, 1
        //   109: goto            49
        //   112: iload_3         /* writeIndex */
        //   113: aload_0         /* $receiver */
        //   114: invokeinterface java/util/List.size:()I
        //   119: if_icmpge       162
        //   122: aload_0         /* $receiver */
        //   123: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //   126: istore          4
        //   128: iload_3         /* writeIndex */
        //   129: istore          5
        //   131: iload           4
        //   133: iload           5
        //   135: if_icmplt       160
        //   138: aload_0         /* $receiver */
        //   139: iload           removeIndex
        //   141: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
        //   146: pop            
        //   147: iload           removeIndex
        //   149: iload           5
        //   151: if_icmpeq       160
        //   154: iinc            removeIndex, -1
        //   157: goto            138
        //   160: iconst_1       
        //   161: ireturn        
        //   162: iconst_0       
        //   163: ireturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/util/List<TT;>;Lkotlin/jvm/functions/Function1<-TT;Ljava/lang/Boolean;>;Z)Z
        //    StackMapTable: 00 0A 56 07 00 61 08 FE 00 11 01 01 01 FC 00 1E 07 00 9A 0F 02 FA 00 0C 19 15 01
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> $receiver, @NotNull final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return TypeIntrinsics.asMutableCollection($receiver).removeAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith((Iterable<?>)elements, (Iterable<?>)$receiver));
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> $receiver, @NotNull final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final HashSet set = SequencesKt___SequencesKt.toHashSet((Sequence<?>)elements);
        return !set.isEmpty() && $receiver.removeAll(set);
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> $receiver, @NotNull final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return elements.length != 0 && $receiver.removeAll(ArraysKt___ArraysKt.toHashSet(elements));
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> $receiver, @NotNull final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return TypeIntrinsics.asMutableCollection($receiver).retainAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith((Iterable<?>)elements, (Iterable<?>)$receiver));
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> $receiver, @NotNull final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        if (elements.length != 0) {
            return $receiver.retainAll(ArraysKt___ArraysKt.toHashSet(elements));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt($receiver);
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> $receiver, @NotNull final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final HashSet set = SequencesKt___SequencesKt.toHashSet((Sequence<?>)elements);
        if (!set.isEmpty()) {
            return $receiver.retainAll(set);
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt($receiver);
    }
    
    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(@NotNull final Collection<?> $receiver) {
        final boolean result = !$receiver.isEmpty();
        $receiver.clear();
        return result;
    }
    
    @SinceKotlin(version = "1.3")
    public static final <T> void shuffle(@NotNull final List<T> $receiver, @NotNull final Random random) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* random */
        //     7: ldc             "random"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //    16: istore_2       
        //    17: iconst_1       
        //    18: istore_3       
        //    19: iload_2        
        //    20: iload_3        
        //    21: if_icmplt       75
        //    24: aload_1         /* random */
        //    25: iload_2         /* i */
        //    26: iconst_1       
        //    27: iadd           
        //    28: invokevirtual   kotlin/random/Random.nextInt:(I)I
        //    31: istore          j
        //    33: aload_0         /* $receiver */
        //    34: iload_2         /* i */
        //    35: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    40: astore          copy
        //    42: aload_0         /* $receiver */
        //    43: iload_2         /* i */
        //    44: aload_0         /* $receiver */
        //    45: iload           j
        //    47: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    52: invokeinterface java/util/List.set:(ILjava/lang/Object;)Ljava/lang/Object;
        //    57: pop            
        //    58: aload_0         /* $receiver */
        //    59: iload           j
        //    61: aload           copy
        //    63: invokeinterface java/util/List.set:(ILjava/lang/Object;)Ljava/lang/Object;
        //    68: pop            
        //    69: iinc            i, -1
        //    72: goto            19
        //    75: return         
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/util/List<TT;>;Lkotlin/random/Random;)V
        //    StackMapTable: 00 02 FD 00 13 01 01 37
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull final Iterable<? extends T> $receiver, @NotNull final Random random) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(random, "random");
        final List $receiver2;
        final List list = $receiver2 = CollectionsKt___CollectionsKt.toMutableList((Iterable<?>)$receiver);
        shuffle((List<Object>)$receiver2, random);
        return (List<T>)list;
    }
    
    public CollectionsKt__MutableCollectionsKt() {
    }
}
