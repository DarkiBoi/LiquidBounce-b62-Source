// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.internal.InlineOnly;
import kotlin.sequences.Sequence;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004¨\u0006\r" }, d2 = { "minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib" }, xs = "kotlin/collections/SetsKt")
class SetsKt___SetsKt extends SetsKt__SetsKt
{
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> $receiver, final T element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final LinkedHashSet result = new LinkedHashSet(MapsKt__MapsKt.mapCapacity($receiver.size()));
        boolean removed = false;
        final Iterable $receiver$iv = $receiver;
        for (final Object it : $receiver$iv) {
            final Object element$iv = it;
            boolean b;
            if (!removed && Intrinsics.areEqual(it, element)) {
                removed = true;
                b = false;
            }
            else {
                b = true;
            }
            if (b) {
                result.add(element$iv);
            }
        }
        return (Set<T>)result;
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> $receiver, @NotNull final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final LinkedHashSet result = new LinkedHashSet((Collection<? extends E>)$receiver);
        CollectionsKt__MutableCollectionsKt.removeAll(result, elements);
        return (Set<T>)result;
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> $receiver, @NotNull final Iterable<? extends T> elements) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* elements */
        //     7: ldc             "elements"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_1         /* elements */
        //    13: aload_0         /* $receiver */
        //    14: checkcast       Ljava/lang/Iterable;
        //    17: invokestatic    kotlin/collections/CollectionsKt.convertToSetForSetOperationWith:(Ljava/lang/Iterable;Ljava/lang/Iterable;)Ljava/util/Collection;
        //    20: astore_2        /* other */
        //    21: aload_2         /* other */
        //    22: invokeinterface java/util/Collection.isEmpty:()Z
        //    27: ifeq            38
        //    30: aload_0         /* $receiver */
        //    31: checkcast       Ljava/lang/Iterable;
        //    34: invokestatic    kotlin/collections/CollectionsKt.toSet:(Ljava/lang/Iterable;)Ljava/util/Set;
        //    37: areturn        
        //    38: aload_2         /* other */
        //    39: instanceof      Ljava/util/Set;
        //    42: ifeq            123
        //    45: aload_0         /* $receiver */
        //    46: checkcast       Ljava/lang/Iterable;
        //    49: astore_3       
        //    50: new             Ljava/util/LinkedHashSet;
        //    53: dup            
        //    54: invokespecial   java/util/LinkedHashSet.<init>:()V
        //    57: checkcast       Ljava/util/Collection;
        //    60: astore          destination$iv
        //    62: aload_3         /* $receiver$iv */
        //    63: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    68: astore          5
        //    70: aload           5
        //    72: invokeinterface java/util/Iterator.hasNext:()Z
        //    77: ifeq            117
        //    80: aload           5
        //    82: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    87: astore          element$iv
        //    89: aload           element$iv
        //    91: astore          it
        //    93: aload_2         /* other */
        //    94: aload           it
        //    96: invokeinterface java/util/Collection.contains:(Ljava/lang/Object;)Z
        //   101: ifne            70
        //   104: aload           destination$iv
        //   106: aload           element$iv
        //   108: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   113: pop            
        //   114: goto            70
        //   117: aload           destination$iv
        //   119: checkcast       Ljava/util/Set;
        //   122: areturn        
        //   123: new             Ljava/util/LinkedHashSet;
        //   126: dup            
        //   127: aload_0         /* $receiver */
        //   128: checkcast       Ljava/util/Collection;
        //   131: invokespecial   java/util/LinkedHashSet.<init>:(Ljava/util/Collection;)V
        //   134: astore_3        /* result */
        //   135: aload_3         /* result */
        //   136: aload_2         /* other */
        //   137: invokevirtual   java/util/LinkedHashSet.removeAll:(Ljava/util/Collection;)Z
        //   140: pop            
        //   141: aload_3         /* result */
        //   142: checkcast       Ljava/util/Set;
        //   145: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/util/Set<+TT;>;Ljava/lang/Iterable<+TT;>;)Ljava/util/Set<TT;>;
        //    StackMapTable: 00 04 FC 00 26 07 00 53 FE 00 1F 07 00 3D 07 00 53 07 00 45 2E F8 00 05
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> $receiver, @NotNull final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final LinkedHashSet result = new LinkedHashSet((Collection<? extends E>)$receiver);
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)result, (Sequence<?>)elements);
        return (Set<T>)result;
    }
    
    @InlineOnly
    private static final <T> Set<T> minusElement(@NotNull final Set<? extends T> $receiver, final T element) {
        return (Set<T>)minus($receiver, (Object)element);
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> $receiver, final T element) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final LinkedHashSet result = new LinkedHashSet(MapsKt__MapsKt.mapCapacity($receiver.size() + 1));
        result.addAll($receiver);
        result.add(element);
        return (Set<T>)result;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> $receiver, @NotNull final T[] elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final LinkedHashSet result = new LinkedHashSet(MapsKt__MapsKt.mapCapacity($receiver.size() + elements.length));
        result.addAll($receiver);
        CollectionsKt__MutableCollectionsKt.addAll(result, elements);
        return (Set<T>)result;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> $receiver, @NotNull final Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final Integer collectionSizeOrNull = CollectionsKt__IterablesKt.collectionSizeOrNull((Iterable<?>)elements);
        int expectedSize;
        if (collectionSizeOrNull != null) {
            final int it = collectionSizeOrNull.intValue();
            expectedSize = $receiver.size() + it;
        }
        else {
            expectedSize = $receiver.size() * 2;
        }
        final LinkedHashSet result = new LinkedHashSet(MapsKt__MapsKt.mapCapacity(expectedSize));
        result.addAll($receiver);
        CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)result, (Iterable<?>)elements);
        return (Set<T>)result;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> $receiver, @NotNull final Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        final LinkedHashSet result = new LinkedHashSet(MapsKt__MapsKt.mapCapacity($receiver.size() * 2));
        result.addAll($receiver);
        CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)result, (Sequence<?>)elements);
        return (Set<T>)result;
    }
    
    @InlineOnly
    private static final <T> Set<T> plusElement(@NotNull final Set<? extends T> $receiver, final T element) {
        return (Set<T>)plus($receiver, (Object)element);
    }
    
    public SetsKt___SetsKt() {
    }
}
