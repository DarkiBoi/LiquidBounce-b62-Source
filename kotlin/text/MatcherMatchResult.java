// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import java.util.regex.Matcher;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\n\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001b¨\u0006\u001d" }, d2 = { "Lkotlin/text/MatcherMatchResult;", "Lkotlin/text/MatchResult;", "matcher", "Ljava/util/regex/Matcher;", "input", "", "(Ljava/util/regex/Matcher;Ljava/lang/CharSequence;)V", "groupValues", "", "", "getGroupValues", "()Ljava/util/List;", "groupValues_", "groups", "Lkotlin/text/MatchGroupCollection;", "getGroups", "()Lkotlin/text/MatchGroupCollection;", "matchResult", "Ljava/util/regex/MatchResult;", "getMatchResult", "()Ljava/util/regex/MatchResult;", "range", "Lkotlin/ranges/IntRange;", "getRange", "()Lkotlin/ranges/IntRange;", "value", "getValue", "()Ljava/lang/String;", "next", "kotlin-stdlib" })
final class MatcherMatchResult implements MatchResult
{
    @NotNull
    private final MatchGroupCollection groups;
    private List<String> groupValues_;
    private final Matcher matcher;
    private final CharSequence input;
    
    private final java.util.regex.MatchResult getMatchResult() {
        return this.matcher;
    }
    
    @NotNull
    @Override
    public IntRange getRange() {
        return RegexKt.access$range(this.getMatchResult());
    }
    
    @NotNull
    @Override
    public String getValue() {
        final String group = this.getMatchResult().group();
        Intrinsics.checkExpressionValueIsNotNull(group, "matchResult.group()");
        return group;
    }
    
    @NotNull
    @Override
    public MatchGroupCollection getGroups() {
        return this.groups;
    }
    
    @NotNull
    @Override
    public List<String> getGroupValues() {
        if (this.groupValues_ == null) {
            this.groupValues_ = (List<String>)new MatcherMatchResult$groupValues.MatcherMatchResult$groupValues$1(this);
        }
        final List<String> groupValues_ = this.groupValues_;
        if (groupValues_ == null) {
            Intrinsics.throwNpe();
        }
        return groupValues_;
    }
    
    @Nullable
    @Override
    public MatchResult next() {
        final int nextIndex = this.getMatchResult().end() + ((this.getMatchResult().end() == this.getMatchResult().start()) ? 1 : 0);
        MatchResult access$findNext;
        if (nextIndex <= this.input.length()) {
            final Matcher matcher = this.matcher.pattern().matcher(this.input);
            Intrinsics.checkExpressionValueIsNotNull(matcher, "matcher.pattern().matcher(input)");
            access$findNext = RegexKt.access$findNext(matcher, nextIndex, this.input);
        }
        else {
            access$findNext = null;
        }
        return access$findNext;
    }
    
    public MatcherMatchResult(@NotNull final Matcher matcher, @NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(matcher, "matcher");
        Intrinsics.checkParameterIsNotNull(input, "input");
        this.matcher = matcher;
        this.input = input;
        this.groups = (MatchGroupCollection)new MatcherMatchResult$groups.MatcherMatchResult$groups$1(this);
    }
    
    @NotNull
    @Override
    public Destructured getDestructured() {
        return DefaultImpls.getDestructured(this);
    }
}
