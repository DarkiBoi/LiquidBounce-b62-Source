// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u0010H\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u0001*\u00020\u0001H\u0087\n¨\u0006\u0012" }, d2 = { "dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "mod", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib" }, xs = "kotlin/MathKt")
class MathKt__BigDecimalsKt
{
    @InlineOnly
    private static final BigDecimal plus(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal add = $receiver.add(other);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(other)");
        return add;
    }
    
    @InlineOnly
    private static final BigDecimal minus(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal subtract = $receiver.subtract(other);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(other)");
        return subtract;
    }
    
    @InlineOnly
    private static final BigDecimal times(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal multiply = $receiver.multiply(other);
        Intrinsics.checkExpressionValueIsNotNull(multiply, "this.multiply(other)");
        return multiply;
    }
    
    @InlineOnly
    private static final BigDecimal div(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal divide = $receiver.divide(other, RoundingMode.HALF_EVEN);
        Intrinsics.checkExpressionValueIsNotNull(divide, "this.divide(other, RoundingMode.HALF_EVEN)");
        return divide;
    }
    
    @Deprecated(message = "Use rem(other) instead", replaceWith = @ReplaceWith(imports = {}, expression = "rem(other)"), level = DeprecationLevel.ERROR)
    @InlineOnly
    @java.lang.Deprecated
    private static final BigDecimal mod(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal remainder = $receiver.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @InlineOnly
    private static final BigDecimal rem(@NotNull final BigDecimal $receiver, final BigDecimal other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal remainder = $receiver.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @InlineOnly
    private static final BigDecimal unaryMinus(@NotNull final BigDecimal $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal negate = $receiver.negate();
        Intrinsics.checkExpressionValueIsNotNull(negate, "this.negate()");
        return negate;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal inc(@NotNull final BigDecimal $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal add = $receiver.add(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(BigDecimal.ONE)");
        return add;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal dec(@NotNull final BigDecimal $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigDecimal subtract = $receiver.subtract(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(BigDecimal.ONE)");
        return subtract;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final int $receiver) {
        final BigDecimal value = BigDecimal.valueOf($receiver);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigDecimal.valueOf(this.toLong())");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final int $receiver, final MathContext mathContext) {
        return new BigDecimal($receiver, mathContext);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final long $receiver) {
        final BigDecimal value = BigDecimal.valueOf($receiver);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigDecimal.valueOf(this)");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final long $receiver, final MathContext mathContext) {
        return new BigDecimal($receiver, mathContext);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final float $receiver) {
        return new BigDecimal(String.valueOf($receiver));
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final float $receiver, final MathContext mathContext) {
        return new BigDecimal(String.valueOf($receiver), mathContext);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final double $receiver) {
        return new BigDecimal(String.valueOf($receiver));
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final double $receiver, final MathContext mathContext) {
        return new BigDecimal(String.valueOf($receiver), mathContext);
    }
    
    public MathKt__BigDecimalsKt() {
    }
}
