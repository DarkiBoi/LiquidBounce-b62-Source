// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import java.util.Comparator;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import java.util.Collection;
import kotlin.ranges.IntRange;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Locale;
import kotlin.jvm.internal.StringCompanionObject;
import java.util.Arrays;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import kotlin.internal.InlineOnly;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000x\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a)\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a\n\u0010\u0016\u001a\u00020\u0002*\u00020\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u001a\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u001c\u0010\u001d\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0087\b\u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\"\u001a\u00020#H\u0087\b\u001a\n\u0010$\u001a\u00020\u0002*\u00020\u0002\u001a\u001c\u0010%\u001a\u00020 *\u00020\u00022\u0006\u0010&\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a \u0010'\u001a\u00020 *\u0004\u0018\u00010\u00022\b\u0010\u001e\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a2\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*2\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010.\u001a*\u0010(\u001a\u00020\u0002*\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010/\u001a:\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010)\u001a\u00020*2\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00100\u001a2\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00101\u001a\r\u00102\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u00103\u001a\u00020 *\u00020#\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u0010:\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00102\u0006\u0010;\u001a\u00020\u0010H\u0087\b\u001a4\u0010<\u001a\u00020 *\u00020#2\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020#2\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a4\u0010<\u001a\u00020 *\u00020\u00022\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0012\u0010?\u001a\u00020\u0002*\u00020#2\u0006\u0010@\u001a\u00020\u0010\u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\"\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00020H*\u00020#2\u0006\u0010I\u001a\u00020J2\b\b\u0002\u0010K\u001a\u00020\u0010\u001a\u001c\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u0017\u0010P\u001a\u00020\f*\u00020\u00022\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\r\u0010Q\u001a\u00020\u0013*\u00020\u0002H\u0087\b\u001a3\u0010Q\u001a\u00020\u0013*\u00020\u00022\u0006\u0010R\u001a\u00020\u00132\b\b\u0002\u0010S\u001a\u00020\u00102\b\b\u0002\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\r\u0010T\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010T\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\u001a\u0017\u0010U\u001a\u00020J*\u00020\u00022\b\b\u0002\u0010V\u001a\u00020\u0010H\u0087\b\u001a\r\u0010W\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010W\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006X" }, d2 = { "CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "contentEquals", "charSequence", "", "decapitalize", "endsWith", "suffix", "equals", "format", "locale", "Ljava/util/Locale;", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "startIndex", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt
{
    @InlineOnly
    private static final int nativeIndexOf(@NotNull final String $receiver, final char ch, final int fromIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.indexOf(ch, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeIndexOf(@NotNull final String $receiver, final String str, final int fromIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.indexOf(str, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull final String $receiver, final char ch, final int fromIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.lastIndexOf(ch, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull final String $receiver, final String str, final int fromIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.lastIndexOf(str, fromIndex);
    }
    
    public static final boolean equals(@Nullable final String $receiver, @Nullable final String other, final boolean ignoreCase) {
        if ($receiver == null) {
            return other == null;
        }
        return ignoreCase ? $receiver.equalsIgnoreCase(other) : $receiver.equals(other);
    }
    
    @NotNull
    public static final String replace(@NotNull final String $receiver, final char oldChar, final char newChar, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if (!ignoreCase) {
            final String replace = $receiver.replace(oldChar, newChar);
            Intrinsics.checkExpressionValueIsNotNull(replace, "(this as java.lang.Strin\u2026replace(oldChar, newChar)");
            return replace;
        }
        return SequencesKt___SequencesKt.joinToString$default(StringsKt__StringsKt.splitToSequence$default($receiver, new char[] { oldChar }, ignoreCase, 0, 4, null), String.valueOf(newChar), null, null, 0, null, null, 62, null);
    }
    
    @NotNull
    public static final String replace(@NotNull final String $receiver, @NotNull final String oldValue, @NotNull final String newValue, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(oldValue, "oldValue");
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        return SequencesKt___SequencesKt.joinToString$default(StringsKt__StringsKt.splitToSequence$default($receiver, new String[] { oldValue }, ignoreCase, 0, 4, null), newValue, null, null, 0, null, null, 62, null);
    }
    
    @NotNull
    public static final String replaceFirst(@NotNull final String $receiver, final char oldChar, final char newChar, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final int index = StringsKt__StringsKt.indexOf$default($receiver, oldChar, 0, ignoreCase, 2, null);
        return (index < 0) ? $receiver : StringsKt__StringsKt.replaceRange((CharSequence)$receiver, index, index + 1, String.valueOf(newChar)).toString();
    }
    
    @NotNull
    public static final String replaceFirst(@NotNull final String $receiver, @NotNull final String oldValue, @NotNull final String newValue, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(oldValue, "oldValue");
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        final int index = StringsKt__StringsKt.indexOf$default($receiver, oldValue, 0, ignoreCase, 2, null);
        return (index < 0) ? $receiver : StringsKt__StringsKt.replaceRange((CharSequence)$receiver, index, index + oldValue.length(), newValue).toString();
    }
    
    @InlineOnly
    private static final String toUpperCase(@NotNull final String $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String upperCase = $receiver.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
        return upperCase;
    }
    
    @InlineOnly
    private static final String toLowerCase(@NotNull final String $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String lowerCase = $receiver.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
        return lowerCase;
    }
    
    @InlineOnly
    private static final char[] toCharArray(@NotNull final String $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final char[] charArray = $receiver.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
        return charArray;
    }
    
    @InlineOnly
    private static final char[] toCharArray(@NotNull final String $receiver, final char[] destination, final int destinationOffset, final int startIndex, final int endIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        $receiver.getChars(startIndex, endIndex, destination, destinationOffset);
        return destination;
    }
    
    @InlineOnly
    private static final String format(@NotNull final String $receiver, final Object... args) {
        final String format = String.format($receiver, Arrays.copyOf(args, args.length));
        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(this, *args)");
        return format;
    }
    
    @InlineOnly
    private static final String format(@NotNull final StringCompanionObject $receiver, final String format, final Object... args) {
        final String format2 = String.format(format, Arrays.copyOf(args, args.length));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(format, *args)");
        return format2;
    }
    
    @InlineOnly
    private static final String format(@NotNull final String $receiver, final Locale locale, final Object... args) {
        final String format = String.format(locale, $receiver, Arrays.copyOf(args, args.length));
        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(locale, this, *args)");
        return format;
    }
    
    @InlineOnly
    private static final String format(@NotNull final StringCompanionObject $receiver, final Locale locale, final String format, final Object... args) {
        final String format2 = String.format(locale, format, Arrays.copyOf(args, args.length));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(locale, format, *args)");
        return format2;
    }
    
    @NotNull
    public static final List<String> split(@NotNull final CharSequence $receiver, @NotNull final Pattern regex, final int limit) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(regex, "regex");
        if (limit < 0) {
            throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + '.').toString());
        }
        final String[] split = regex.split($receiver, (limit == 0) ? -1 : limit);
        Intrinsics.checkExpressionValueIsNotNull(split, "regex.split(this, if (limit == 0) -1 else limit)");
        return ArraysKt___ArraysJvmKt.asList(split);
    }
    
    @InlineOnly
    private static final String substring(@NotNull final String $receiver, final int startIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = $receiver.substring(startIndex);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @InlineOnly
    private static final String substring(@NotNull final String $receiver, final int startIndex, final int endIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = $receiver.substring(startIndex, endIndex);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    public static final boolean startsWith(@NotNull final String $receiver, @NotNull final String prefix, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!ignoreCase) {
            return $receiver.startsWith(prefix);
        }
        return regionMatches($receiver, 0, prefix, 0, prefix.length(), ignoreCase);
    }
    
    public static final boolean startsWith(@NotNull final String $receiver, @NotNull final String prefix, final int startIndex, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!ignoreCase) {
            return $receiver.startsWith(prefix, startIndex);
        }
        return regionMatches($receiver, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }
    
    public static final boolean endsWith(@NotNull final String $receiver, @NotNull final String suffix, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!ignoreCase) {
            return $receiver.endsWith(suffix);
        }
        return regionMatches($receiver, $receiver.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final int offset, final int length, final Charset charset) {
        return new String(bytes, offset, length, charset);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final Charset charset) {
        return new String(bytes, charset);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final int offset, final int length) {
        return new String(bytes, offset, length, Charsets.UTF_8);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_8);
    }
    
    @InlineOnly
    private static final String String(final char[] chars) {
        return new String(chars);
    }
    
    @InlineOnly
    private static final String String(final char[] chars, final int offset, final int length) {
        return new String(chars, offset, length);
    }
    
    @InlineOnly
    private static final String String(final int[] codePoints, final int offset, final int length) {
        return new String(codePoints, offset, length);
    }
    
    @InlineOnly
    private static final String String(final StringBuffer stringBuffer) {
        return new String(stringBuffer);
    }
    
    @InlineOnly
    private static final String String(final StringBuilder stringBuilder) {
        return new String(stringBuilder);
    }
    
    @InlineOnly
    private static final int codePointAt(@NotNull final String $receiver, final int index) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.codePointAt(index);
    }
    
    @InlineOnly
    private static final int codePointBefore(@NotNull final String $receiver, final int index) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.codePointBefore(index);
    }
    
    @InlineOnly
    private static final int codePointCount(@NotNull final String $receiver, final int beginIndex, final int endIndex) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.codePointCount(beginIndex, endIndex);
    }
    
    public static final int compareTo(@NotNull final String $receiver, @NotNull final String other, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (ignoreCase) {
            return $receiver.compareToIgnoreCase(other);
        }
        return $receiver.compareTo(other);
    }
    
    @InlineOnly
    private static final boolean contentEquals(@NotNull final String $receiver, final CharSequence charSequence) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.contentEquals(charSequence);
    }
    
    @InlineOnly
    private static final boolean contentEquals(@NotNull final String $receiver, final StringBuffer stringBuilder) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.contentEquals(stringBuilder);
    }
    
    @InlineOnly
    private static final String intern(@NotNull final String $receiver) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String intern = $receiver.intern();
        Intrinsics.checkExpressionValueIsNotNull(intern, "(this as java.lang.String).intern()");
        return intern;
    }
    
    public static final boolean isBlank(@NotNull final CharSequence $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        if ($receiver.length() != 0) {
            final Iterable $receiver$iv = StringsKt__StringsKt.getIndices($receiver);
            boolean b = false;
            Label_0095: {
                if ($receiver$iv instanceof Collection && ((Collection)$receiver$iv).isEmpty()) {
                    b = true;
                }
                else {
                    final Iterator iterator = $receiver$iv.iterator();
                    while (iterator.hasNext()) {
                        final int it;
                        final int element$iv = it = ((IntIterator)iterator).nextInt();
                        if (!CharsKt__CharJVMKt.isWhitespace($receiver.charAt(it))) {
                            b = false;
                            break Label_0095;
                        }
                    }
                    b = true;
                }
            }
            if (!b) {
                return false;
            }
        }
        return true;
    }
    
    @InlineOnly
    private static final int offsetByCodePoints(@NotNull final String $receiver, final int index, final int codePointOffset) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return $receiver.offsetByCodePoints(index, codePointOffset);
    }
    
    public static final boolean regionMatches(@NotNull final CharSequence $receiver, final int thisOffset, @NotNull final CharSequence other, final int otherOffset, final int length, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if ($receiver instanceof String && other instanceof String) {
            return regionMatches((String)$receiver, thisOffset, (String)other, otherOffset, length, ignoreCase);
        }
        return StringsKt__StringsKt.regionMatchesImpl($receiver, thisOffset, other, otherOffset, length, ignoreCase);
    }
    
    public static final boolean regionMatches(@NotNull final String $receiver, final int thisOffset, @NotNull final String other, final int otherOffset, final int length, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return ignoreCase ? $receiver.regionMatches(ignoreCase, thisOffset, other, otherOffset, length) : $receiver.regionMatches(thisOffset, other, otherOffset, length);
    }
    
    @InlineOnly
    private static final String toLowerCase(@NotNull final String $receiver, final Locale locale) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String lowerCase = $receiver.toLowerCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
        return lowerCase;
    }
    
    @InlineOnly
    private static final String toUpperCase(@NotNull final String $receiver, final Locale locale) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String upperCase = $receiver.toUpperCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase(locale)");
        return upperCase;
    }
    
    @InlineOnly
    private static final byte[] toByteArray(@NotNull final String $receiver, final Charset charset) {
        if ($receiver == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final byte[] bytes = $receiver.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }
    
    @InlineOnly
    private static final Pattern toPattern(@NotNull final String $receiver, final int flags) {
        final Pattern compile = Pattern.compile($receiver, flags);
        Intrinsics.checkExpressionValueIsNotNull(compile, "java.util.regex.Pattern.compile(this, flags)");
        return compile;
    }
    
    @NotNull
    public static final String capitalize(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        String string;
        if ($receiver.length() > 0 && Character.isLowerCase($receiver.charAt(0))) {
            final StringBuilder sb = new StringBuilder();
            final int beginIndex = 0;
            final int endIndex = 1;
            final StringBuilder sb2 = sb;
            final String substring = $receiver.substring(beginIndex, endIndex);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            final String s = substring;
            final StringBuilder sb3 = sb2;
            final String s2 = s;
            final StringBuilder sb4 = sb3;
            final String s3 = s2;
            if (s3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            final String upperCase = s3.toUpperCase();
            Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
            final StringBuilder append = sb4.append(upperCase);
            final int beginIndex2 = 1;
            final StringBuilder sb5 = append;
            final String substring2 = $receiver.substring(beginIndex2);
            Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
            string = sb5.append(substring2).toString();
        }
        else {
            string = $receiver;
        }
        return string;
    }
    
    @NotNull
    public static final String decapitalize(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        String string;
        if ($receiver.length() > 0 && Character.isUpperCase($receiver.charAt(0))) {
            final StringBuilder sb = new StringBuilder();
            final int beginIndex = 0;
            final int endIndex = 1;
            final StringBuilder sb2 = sb;
            final String substring = $receiver.substring(beginIndex, endIndex);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            final String s = substring;
            final StringBuilder sb3 = sb2;
            final String s2 = s;
            final StringBuilder sb4 = sb3;
            final String s3 = s2;
            if (s3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            final String lowerCase = s3.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
            final StringBuilder append = sb4.append(lowerCase);
            final int beginIndex2 = 1;
            final StringBuilder sb5 = append;
            final String substring2 = $receiver.substring(beginIndex2);
            Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
            string = sb5.append(substring2).toString();
        }
        else {
            string = $receiver;
        }
        return string;
    }
    
    @NotNull
    public static final String repeat(@NotNull final CharSequence $receiver, final int n) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: iload_1         /* n */
        //     7: iflt            14
        //    10: iconst_1       
        //    11: goto            15
        //    14: iconst_0       
        //    15: istore_2       
        //    16: iload_2        
        //    17: ifne            63
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: invokespecial   java/lang/StringBuilder.<init>:()V
        //    27: ldc_w           "Count 'n' must be non-negative, but was "
        //    30: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    33: iload_1         /* n */
        //    34: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    37: bipush          46
        //    39: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: astore          4
        //    47: new             Ljava/lang/IllegalArgumentException;
        //    50: dup            
        //    51: aload           4
        //    53: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    56: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    59: checkcast       Ljava/lang/Throwable;
        //    62: athrow         
        //    63: iload_1         /* n */
        //    64: tableswitch {
        //                0: 88
        //                1: 94
        //          default: 101
        //        }
        //    88: ldc_w           ""
        //    91: goto            268
        //    94: aload_0         /* $receiver */
        //    95: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    98: goto            268
        //   101: aload_0         /* $receiver */
        //   102: invokeinterface java/lang/CharSequence.length:()I
        //   107: tableswitch {
        //                0: 128
        //                1: 134
        //          default: 212
        //        }
        //   128: ldc_w           ""
        //   131: goto            268
        //   134: aload_0         /* $receiver */
        //   135: iconst_0       
        //   136: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   141: istore_2       
        //   142: iload_2        
        //   143: istore_3        /* char */
        //   144: iload_1         /* n */
        //   145: istore          size$iv
        //   147: iload           size$iv
        //   149: newarray        C
        //   151: astore          result$iv
        //   153: iconst_0       
        //   154: istore          6
        //   156: aload           result$iv
        //   158: arraylength    
        //   159: istore          7
        //   161: iload           6
        //   163: iload           7
        //   165: if_icmpge       196
        //   168: aload           result$iv
        //   170: iload           i$iv
        //   172: iload           i$iv
        //   174: istore          8
        //   176: istore          9
        //   178: astore          10
        //   180: iload_3         /* char */
        //   181: istore          11
        //   183: aload           10
        //   185: iload           9
        //   187: iload           11
        //   189: castore        
        //   190: iinc            i$iv, 1
        //   193: goto            161
        //   196: aload           result$iv
        //   198: astore          null
        //   200: new             Ljava/lang/String;
        //   203: dup            
        //   204: aload           4
        //   206: invokespecial   java/lang/String.<init>:([C)V
        //   209: goto            268
        //   212: new             Ljava/lang/StringBuilder;
        //   215: dup            
        //   216: iload_1         /* n */
        //   217: aload_0         /* $receiver */
        //   218: invokeinterface java/lang/CharSequence.length:()I
        //   223: imul           
        //   224: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   227: astore_2        /* sb */
        //   228: iconst_1       
        //   229: istore_3       
        //   230: iload_1         /* n */
        //   231: istore          4
        //   233: iload_3        
        //   234: iload           4
        //   236: if_icmpgt       257
        //   239: aload_2         /* sb */
        //   240: aload_0         /* $receiver */
        //   241: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;
        //   244: pop            
        //   245: iload_3         /* i */
        //   246: iload           4
        //   248: if_icmpeq       257
        //   251: iinc            i, 1
        //   254: goto            239
        //   257: aload_2         /* sb */
        //   258: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   261: dup            
        //   262: ldc_w           "sb.toString()"
        //   265: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   268: areturn        
        //    StackMapTable: 00 0E 0E 40 01 FC 00 2F 01 18 05 06 1A 05 FF 00 1A 00 08 07 00 A5 01 01 01 01 07 02 0B 01 01 00 00 22 FF 00 0F 00 03 07 00 A5 01 01 00 00 FF 00 1A 00 05 07 00 A5 01 07 01 0D 01 01 00 00 11 FF 00 0A 00 02 07 00 A5 01 00 01 07 00 70
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(@NotNull final StringCompanionObject $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final Comparator<String> case_INSENSITIVE_ORDER = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkExpressionValueIsNotNull(case_INSENSITIVE_ORDER, "java.lang.String.CASE_INSENSITIVE_ORDER");
        return case_INSENSITIVE_ORDER;
    }
    
    public StringsKt__StringsJVMKt() {
    }
}
