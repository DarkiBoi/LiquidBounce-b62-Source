// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.PublishedApi;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.Nullable;
import java.util.regex.Matcher;
import java.util.Collections;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.functions.Function1;
import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import java.util.regex.Pattern;
import java.util.Set;
import kotlin.Metadata;
import java.io.Serializable;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 ,2\u00060\u0001j\u0002`\u0002:\u0002,-B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bB\u001d\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n¢\u0006\u0002\u0010\u000bB\u000f\b\u0001\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u001a\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00190\u001d2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u0017J\u0011\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086\u0004J\"\u0010 \u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00170\"J\u0016\u0010 \u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004J\u0016\u0010$\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004J\u001e\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040&2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010'\u001a\u00020\u001bJ\u0006\u0010(\u001a\u00020\rJ\b\u0010)\u001a\u00020\u0004H\u0016J\b\u0010*\u001a\u00020+H\u0002R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006." }, d2 = { "Lkotlin/text/Regex;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "(Ljava/lang/String;)V", "option", "Lkotlin/text/RegexOption;", "(Ljava/lang/String;Lkotlin/text/RegexOption;)V", "options", "", "(Ljava/lang/String;Ljava/util/Set;)V", "nativePattern", "Ljava/util/regex/Pattern;", "(Ljava/util/regex/Pattern;)V", "_options", "getOptions", "()Ljava/util/Set;", "getPattern", "()Ljava/lang/String;", "containsMatchIn", "", "input", "", "find", "Lkotlin/text/MatchResult;", "startIndex", "", "findAll", "Lkotlin/sequences/Sequence;", "matchEntire", "matches", "replace", "transform", "Lkotlin/Function1;", "replacement", "replaceFirst", "split", "", "limit", "toPattern", "toString", "writeReplace", "", "Companion", "Serialized", "kotlin-stdlib" })
public final class Regex implements Serializable
{
    private Set<? extends RegexOption> _options;
    private final Pattern nativePattern;
    public static final Companion Companion;
    
    @NotNull
    public final String getPattern() {
        final String pattern = this.nativePattern.pattern();
        Intrinsics.checkExpressionValueIsNotNull(pattern, "nativePattern.pattern()");
        return pattern;
    }
    
    @NotNull
    public final Set<RegexOption> getOptions() {
        Set<? extends RegexOption> options;
        if ((options = this._options) == null) {
            final int value$iv = this.nativePattern.flags();
            final EnumSet $receiver$iv;
            final EnumSet set = $receiver$iv = EnumSet.allOf(RegexOption.class);
            CollectionsKt__MutableCollectionsKt.retainAll((Iterable<?>)$receiver$iv, (Function1<? super Object, Boolean>)new Function1<T, Boolean>(value$iv) {
                @Override
                public final boolean invoke(final T it) {
                    return (this.$value$inlined & ((FlagEnum)it).getMask()) == ((FlagEnum)it).getValue();
                }
            });
            final Set<Object> unmodifiableSet = Collections.unmodifiableSet((Set<?>)set);
            Intrinsics.checkExpressionValueIsNotNull(unmodifiableSet, "Collections.unmodifiable\u2026mask == it.value }\n    })");
            final Set it;
            final Set set2 = it = unmodifiableSet;
            this._options = (Set<? extends RegexOption>)it;
            options = (Set<? extends RegexOption>)set2;
        }
        return (Set<RegexOption>)options;
    }
    
    public final boolean matches(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        return this.nativePattern.matcher(input).matches();
    }
    
    public final boolean containsMatchIn(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        return this.nativePattern.matcher(input).find();
    }
    
    @Nullable
    public final MatchResult find(@NotNull final CharSequence input, final int startIndex) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        final Matcher matcher = this.nativePattern.matcher(input);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.access$findNext(matcher, startIndex, input);
    }
    
    @Nullable
    public static /* synthetic */ MatchResult find$default(final Regex regex, final CharSequence input, int startIndex, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            startIndex = 0;
        }
        return regex.find(input, startIndex);
    }
    
    @NotNull
    public final Sequence<MatchResult> findAll(@NotNull final CharSequence input, final int startIndex) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        return SequencesKt__SequencesKt.generateSequence((Function0<? extends MatchResult>)new Regex$findAll.Regex$findAll$1(this, input, startIndex), (Function1<? super MatchResult, ? extends MatchResult>)Regex$findAll.Regex$findAll$2.INSTANCE);
    }
    
    @Nullable
    public final MatchResult matchEntire(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        final Matcher matcher = this.nativePattern.matcher(input);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.access$matchEntire(matcher, input);
    }
    
    @NotNull
    public final String replace(@NotNull final CharSequence input, @NotNull final String replacement) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        final String replaceAll = this.nativePattern.matcher(input).replaceAll(replacement);
        Intrinsics.checkExpressionValueIsNotNull(replaceAll, "nativePattern.matcher(in\u2026).replaceAll(replacement)");
        return replaceAll;
    }
    
    @NotNull
    public final String replace(@NotNull final CharSequence input, @NotNull final Function1<? super MatchResult, ? extends CharSequence> transform) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        final MatchResult find$default = find$default(this, input, 0, 2, null);
        if (find$default != null) {
            MatchResult match = find$default;
            int lastStart = 0;
            final int length = input.length();
            final StringBuilder sb = new StringBuilder(length);
            do {
                final MatchResult matchResult = match;
                if (matchResult == null) {
                    Intrinsics.throwNpe();
                }
                final MatchResult foundMatch = matchResult;
                sb.append(input, lastStart, foundMatch.getRange().getStart());
                sb.append((CharSequence)transform.invoke(foundMatch));
                lastStart = foundMatch.getRange().getEndInclusive() + 1;
                match = foundMatch.next();
            } while (lastStart < length && match != null);
            if (lastStart < length) {
                sb.append(input, lastStart, length);
            }
            final String string = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "sb.toString()");
            return string;
        }
        return input.toString();
    }
    
    @NotNull
    public final String replaceFirst(@NotNull final CharSequence input, @NotNull final String replacement) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        final String replaceFirst = this.nativePattern.matcher(input).replaceFirst(replacement);
        Intrinsics.checkExpressionValueIsNotNull(replaceFirst, "nativePattern.matcher(in\u2026replaceFirst(replacement)");
        return replaceFirst;
    }
    
    @NotNull
    public final List<String> split(@NotNull final CharSequence input, final int limit) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        if (limit < 0) {
            throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + '.').toString());
        }
        final Matcher matcher = this.nativePattern.matcher(input);
        if (!matcher.find() || limit == 1) {
            return CollectionsKt__CollectionsJVMKt.listOf(input.toString());
        }
        final ArrayList result = new ArrayList((limit > 0) ? RangesKt___RangesKt.coerceAtMost(limit, 10) : 10);
        int lastStart = 0;
        final int lastSplit = limit - 1;
        do {
            result.add(input.subSequence(lastStart, matcher.start()).toString());
            lastStart = matcher.end();
            if (lastSplit >= 0 && result.size() == lastSplit) {
                break;
            }
        } while (matcher.find());
        result.add(input.subSequence(lastStart, input.length()).toString());
        return (List<String>)result;
    }
    
    @NotNull
    @Override
    public String toString() {
        final String string = this.nativePattern.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "nativePattern.toString()");
        return string;
    }
    
    @NotNull
    public final Pattern toPattern() {
        return this.nativePattern;
    }
    
    private final Object writeReplace() {
        final String pattern = this.nativePattern.pattern();
        Intrinsics.checkExpressionValueIsNotNull(pattern, "nativePattern.pattern()");
        return new Serialized(pattern, this.nativePattern.flags());
    }
    
    @PublishedApi
    public Regex(@NotNull final Pattern nativePattern) {
        Intrinsics.checkParameterIsNotNull(nativePattern, "nativePattern");
        this.nativePattern = nativePattern;
    }
    
    public Regex(@NotNull final String pattern) {
        Intrinsics.checkParameterIsNotNull(pattern, "pattern");
        final Pattern compile = Pattern.compile(pattern);
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern)");
        this(compile);
    }
    
    public Regex(@NotNull final String pattern, @NotNull final RegexOption option) {
        Intrinsics.checkParameterIsNotNull(pattern, "pattern");
        Intrinsics.checkParameterIsNotNull(option, "option");
        final Pattern compile = Pattern.compile(pattern, Regex.Companion.ensureUnicodeCase(option.getValue()));
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern,\u2026nicodeCase(option.value))");
        this(compile);
    }
    
    public Regex(@NotNull final String pattern, @NotNull final Set<? extends RegexOption> options) {
        Intrinsics.checkParameterIsNotNull(pattern, "pattern");
        Intrinsics.checkParameterIsNotNull(options, "options");
        final Pattern compile = Pattern.compile(pattern, Regex.Companion.ensureUnicodeCase(RegexKt.access$toInt(options)));
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern,\u2026odeCase(options.toInt()))");
        this(compile);
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \u000e2\u00060\u0001j\u0002`\u0002:\u0001\u000eB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0002R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f" }, d2 = { "Lkotlin/text/Regex$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "flags", "", "(Ljava/lang/String;I)V", "getFlags", "()I", "getPattern", "()Ljava/lang/String;", "readResolve", "", "Companion", "kotlin-stdlib" })
    private static final class Serialized implements Serializable
    {
        @NotNull
        private final String pattern;
        private final int flags;
        private static final long serialVersionUID = 0L;
        public static final Companion Companion;
        
        private final Object readResolve() {
            final Pattern compile = Pattern.compile(this.pattern, this.flags);
            Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern, flags)");
            return new Regex(compile);
        }
        
        @NotNull
        public final String getPattern() {
            return this.pattern;
        }
        
        public final int getFlags() {
            return this.flags;
        }
        
        public Serialized(@NotNull final String pattern, final int flags) {
            Intrinsics.checkParameterIsNotNull(pattern, "pattern");
            this.pattern = pattern;
            this.flags = flags;
        }
        
        static {
            Companion = new Companion(null);
        }
        
        @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Lkotlin/text/Regex$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib" })
        public static final class Companion
        {
            private Companion() {
            }
        }
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u0007¨\u0006\f" }, d2 = { "Lkotlin/text/Regex$Companion;", "", "()V", "ensureUnicodeCase", "", "flags", "escape", "", "literal", "escapeReplacement", "fromLiteral", "Lkotlin/text/Regex;", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final Regex fromLiteral(@NotNull final String literal) {
            Intrinsics.checkParameterIsNotNull(literal, "literal");
            return new Regex(literal, RegexOption.LITERAL);
        }
        
        @NotNull
        public final String escape(@NotNull final String literal) {
            Intrinsics.checkParameterIsNotNull(literal, "literal");
            final String quote = Pattern.quote(literal);
            Intrinsics.checkExpressionValueIsNotNull(quote, "Pattern.quote(literal)");
            return quote;
        }
        
        @NotNull
        public final String escapeReplacement(@NotNull final String literal) {
            Intrinsics.checkParameterIsNotNull(literal, "literal");
            final String quoteReplacement = Matcher.quoteReplacement(literal);
            Intrinsics.checkExpressionValueIsNotNull(quoteReplacement, "Matcher.quoteReplacement(literal)");
            return quoteReplacement;
        }
        
        private final int ensureUnicodeCase(final int flags) {
            return ((flags & 0x2) != 0x0) ? (flags | 0x40) : flags;
        }
        
        private Companion() {
        }
    }
}
