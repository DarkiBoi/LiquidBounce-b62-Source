// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.TuplesKt;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.SinceKotlin;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;
import java.util.Iterator;
import kotlin.jvm.functions.Function0;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000b\u001a=\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\b\u0010\f\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000bH\u0007¢\u0006\u0002\u0010\r\u001a+\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010\"\u0002H\u0002¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u00050\u000bH\u0002¢\u0006\u0002\b\u0016\u001a)\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00170\u0001H\u0007¢\u0006\u0002\b\u0018\u001a\"\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a@\u0010\u001c\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001e0\u001d\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00150\u001d0\u0001¨\u0006\u001f" }, d2 = { "Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "generateSequence", "", "nextFunction", "seedFunction", "Lkotlin/Function1;", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "R", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib" }, xs = "kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt
{
    @InlineOnly
    private static final <T> Sequence<T> Sequence(final Function0<? extends Iterator<? extends T>> iterator) {
        return (Sequence<T>)new SequencesKt__SequencesKt$Sequence.SequencesKt__SequencesKt$Sequence$1((Function0)iterator);
    }
    
    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull final Iterator<? extends T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return constrainOnce((Sequence<? extends T>)new Sequence<T>($receiver) {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return (Iterator<T>)this.$this_asSequence$inlined;
            }
        });
    }
    
    @NotNull
    public static final <T> Sequence<T> sequenceOf(@NotNull final T... elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return (elements.length == 0) ? emptySequence() : ArraysKt___ArraysKt.asSequence(elements);
    }
    
    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return (Sequence<T>)EmptySequence.INSTANCE;
    }
    
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <T> Sequence<T> orEmpty(@Nullable final Sequence<? extends T> $receiver) {
        Sequence<T> emptySequence = (Sequence<T>)$receiver;
        if ($receiver == null) {
            emptySequence = emptySequence();
        }
        return emptySequence;
    }
    
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> Sequence<T> ifEmpty(@NotNull final Sequence<? extends T> $receiver, @NotNull final Function0<? extends Sequence<? extends T>> defaultValue) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        return SequencesKt__SequenceBuilderKt.sequence((Function2<? super SequenceScope<? super T>, ? super Continuation<? super Unit>, ?>)new SequencesKt__SequencesKt$ifEmpty.SequencesKt__SequencesKt$ifEmpty$1((Sequence)$receiver, (Function0)defaultValue, (Continuation)null));
    }
    
    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull final Sequence<? extends Sequence<? extends T>> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return flatten$SequencesKt__SequencesKt((Sequence<?>)$receiver, (Function1<? super Object, ? extends Iterator<? extends T>>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$1.INSTANCE);
    }
    
    @JvmName(name = "flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull final Sequence<? extends Iterable<? extends T>> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return flatten$SequencesKt__SequencesKt((Sequence<?>)$receiver, (Function1<? super Object, ? extends Iterator<? extends T>>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$2.INSTANCE);
    }
    
    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(@NotNull final Sequence<? extends T> $receiver, final Function1<? super T, ? extends Iterator<? extends R>> iterator) {
        if ($receiver instanceof TransformingSequence) {
            return ((TransformingSequence<Object, Object>)$receiver).flatten$kotlin_stdlib((Function1<? super Object, ? extends Iterator<? extends R>>)iterator);
        }
        return new FlatteningSequence<Object, Object, R>($receiver, (Function1<? super Object, ?>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$3.INSTANCE, (Function1<? super Object, ? extends Iterator<? extends R>>)iterator);
    }
    
    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull final Sequence<? extends Pair<? extends T, ? extends R>> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final ArrayList listT = new ArrayList();
        final ArrayList listR = new ArrayList();
        for (final Pair pair : $receiver) {
            listT.add(pair.getFirst());
            listR.add(pair.getSecond());
        }
        return TuplesKt.to((List<T>)listT, (List<R>)listR);
    }
    
    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull final Sequence<? extends T> $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return ($receiver instanceof ConstrainedOnceSequence) ? $receiver : ((ConstrainedOnceSequence<T>)new ConstrainedOnceSequence<T>($receiver));
    }
    
    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull final Function0<? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return constrainOnce((Sequence<? extends T>)new GeneratorSequence<Object>(nextFunction, (Function1<? super Object, ?>)new SequencesKt__SequencesKt$generateSequence.SequencesKt__SequencesKt$generateSequence$1((Function0)nextFunction)));
    }
    
    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable final T seed, @NotNull final Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return (Sequence<T>)((seed == null) ? ((EmptySequence)EmptySequence.INSTANCE) : ((GeneratorSequence)new GeneratorSequence<T>((Function0)new SequencesKt__SequencesKt$generateSequence.SequencesKt__SequencesKt$generateSequence$2((Object)seed), nextFunction)));
    }
    
    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull final Function0<? extends T> seedFunction, @NotNull final Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(seedFunction, "seedFunction");
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return new GeneratorSequence<T>(seedFunction, nextFunction);
    }
    
    public SequencesKt__SequencesKt() {
    }
}
