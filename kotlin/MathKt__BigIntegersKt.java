// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import java.math.MathContext;
import java.math.BigDecimal;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.math.BigInteger;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f¨\u0006\u0019" }, d2 = { "and", "Ljava/math/BigInteger;", "other", "dec", "div", "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib" }, xs = "kotlin/MathKt")
class MathKt__BigIntegersKt extends MathKt__BigDecimalsKt
{
    @InlineOnly
    private static final BigInteger plus(@NotNull final BigInteger $receiver, final BigInteger other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger add = $receiver.add(other);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(other)");
        return add;
    }
    
    @InlineOnly
    private static final BigInteger minus(@NotNull final BigInteger $receiver, final BigInteger other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger subtract = $receiver.subtract(other);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(other)");
        return subtract;
    }
    
    @InlineOnly
    private static final BigInteger times(@NotNull final BigInteger $receiver, final BigInteger other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger multiply = $receiver.multiply(other);
        Intrinsics.checkExpressionValueIsNotNull(multiply, "this.multiply(other)");
        return multiply;
    }
    
    @InlineOnly
    private static final BigInteger div(@NotNull final BigInteger $receiver, final BigInteger other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger divide = $receiver.divide(other);
        Intrinsics.checkExpressionValueIsNotNull(divide, "this.divide(other)");
        return divide;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final BigInteger rem(@NotNull final BigInteger $receiver, final BigInteger other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger remainder = $receiver.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @InlineOnly
    private static final BigInteger unaryMinus(@NotNull final BigInteger $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger negate = $receiver.negate();
        Intrinsics.checkExpressionValueIsNotNull(negate, "this.negate()");
        return negate;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger inc(@NotNull final BigInteger $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger add = $receiver.add(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(BigInteger.ONE)");
        return add;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger dec(@NotNull final BigInteger $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final BigInteger subtract = $receiver.subtract(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(BigInteger.ONE)");
        return subtract;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger inv(@NotNull final BigInteger $receiver) {
        final BigInteger not = $receiver.not();
        Intrinsics.checkExpressionValueIsNotNull(not, "this.not()");
        return not;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger and(@NotNull final BigInteger $receiver, final BigInteger other) {
        final BigInteger and = $receiver.and(other);
        Intrinsics.checkExpressionValueIsNotNull(and, "this.and(other)");
        return and;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger or(@NotNull final BigInteger $receiver, final BigInteger other) {
        final BigInteger or = $receiver.or(other);
        Intrinsics.checkExpressionValueIsNotNull(or, "this.or(other)");
        return or;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger xor(@NotNull final BigInteger $receiver, final BigInteger other) {
        final BigInteger xor = $receiver.xor(other);
        Intrinsics.checkExpressionValueIsNotNull(xor, "this.xor(other)");
        return xor;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger shl(@NotNull final BigInteger $receiver, final int n) {
        final BigInteger shiftLeft = $receiver.shiftLeft(n);
        Intrinsics.checkExpressionValueIsNotNull(shiftLeft, "this.shiftLeft(n)");
        return shiftLeft;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger shr(@NotNull final BigInteger $receiver, final int n) {
        final BigInteger shiftRight = $receiver.shiftRight(n);
        Intrinsics.checkExpressionValueIsNotNull(shiftRight, "this.shiftRight(n)");
        return shiftRight;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(final int $receiver) {
        final BigInteger value = BigInteger.valueOf($receiver);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigInteger.valueOf(this.toLong())");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(final long $receiver) {
        final BigInteger value = BigInteger.valueOf($receiver);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigInteger.valueOf(this)");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final BigInteger $receiver) {
        return new BigDecimal($receiver);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final BigInteger $receiver, final int scale, final MathContext mathContext) {
        return new BigDecimal($receiver, scale, mathContext);
    }
    
    public MathKt__BigIntegersKt() {
    }
}
