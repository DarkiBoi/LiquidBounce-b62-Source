// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.ranges.IntRange;
import java.util.regex.Matcher;
import java.util.Collections;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.functions.Function1;
import java.util.EnumSet;
import kotlin.jvm.internal.Intrinsics;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014" }, d2 = { "fromInt", "", "T", "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib" })
public final class RegexKt
{
    private static final int toInt(@NotNull final Iterable<? extends FlagEnum> $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: iconst_0       
        //     3: istore_2        /* initial$iv */
        //     4: iload_2         /* initial$iv */
        //     5: istore_3        /* accumulator$iv */
        //     6: aload_1         /* $receiver$iv */
        //     7: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    12: astore          4
        //    14: aload           4
        //    16: invokeinterface java/util/Iterator.hasNext:()Z
        //    21: ifeq            57
        //    24: aload           4
        //    26: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    31: astore          element$iv
        //    33: iload_3         /* accumulator$iv */
        //    34: aload           element$iv
        //    36: checkcast       Lkotlin/text/FlagEnum;
        //    39: astore          6
        //    41: istore          value
        //    43: iload           value
        //    45: aload           option
        //    47: invokeinterface kotlin/text/FlagEnum.getValue:()I
        //    52: ior            
        //    53: istore_3        /* accumulator$iv */
        //    54: goto            14
        //    57: iload_3         /* accumulator$iv */
        //    58: ireturn        
        //    Signature:
        //  (Ljava/lang/Iterable<+Lkotlin/text/FlagEnum;>;)I
        //    StackMapTable: 00 02 FF 00 0E 00 05 07 00 26 07 00 26 01 01 07 00 2C 00 00 2A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final <T extends Enum> Set<T> fromInt(final int value) {
        Intrinsics.reifiedOperationMarker(4, "T");
        final EnumSet $receiver;
        final EnumSet set = $receiver = EnumSet.allOf(java.lang.Enum.class);
        CollectionsKt__MutableCollectionsKt.retainAll((Iterable<?>)$receiver, (Function1<? super Object, Boolean>)new Function1<T, Boolean>(value) {
            @Override
            public final boolean invoke(final T it) {
                return (this.$value$inlined & ((FlagEnum)it).getMask()) == ((FlagEnum)it).getValue();
            }
        });
        final Set<Object> unmodifiableSet = (Set<Object>)Collections.unmodifiableSet((Set<? extends T>)set);
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableSet, "Collections.unmodifiable\u2026mask == it.value }\n    })");
        return (Set<T>)unmodifiableSet;
    }
    
    private static final MatchResult findNext(@NotNull final Matcher $receiver, final int from, final CharSequence input) {
        return $receiver.find(from) ? new MatcherMatchResult($receiver, input) : null;
    }
    
    private static final MatchResult matchEntire(@NotNull final Matcher $receiver, final CharSequence input) {
        return $receiver.matches() ? new MatcherMatchResult($receiver, input) : null;
    }
    
    private static final IntRange range(@NotNull final java.util.regex.MatchResult $receiver) {
        return RangesKt___RangesKt.until($receiver.start(), $receiver.end());
    }
    
    private static final IntRange range(@NotNull final java.util.regex.MatchResult $receiver, final int groupIndex) {
        return RangesKt___RangesKt.until($receiver.start(groupIndex), $receiver.end(groupIndex));
    }
}
