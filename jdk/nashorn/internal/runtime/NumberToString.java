// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.math.BigInteger;

public final class NumberToString
{
    private final boolean isNaN;
    private boolean isNegative;
    private int decimalExponent;
    private char[] digits;
    private int nDigits;
    private static final int expMask = 2047;
    private static final long fractMask = 4503599627370495L;
    private static final int expShift = 52;
    private static final int expBias = 1023;
    private static final long fractHOB = 4503599627370496L;
    private static final long expOne = 4607182418800017408L;
    private static final int maxSmallBinExp = 62;
    private static final int minSmallBinExp = -21;
    private static final long[] powersOf5;
    private static final int[] nBitsPowerOf5;
    private static final char[] infinityDigits;
    private static final char[] nanDigits;
    private static final char[] zeroes;
    private static BigInteger[] powerOf5Cache;
    
    public static String stringFor(final double value) {
        return new NumberToString(value).toString();
    }
    
    private NumberToString(final double value) {
        long bits = Double.doubleToLongBits(value);
        final int upper = (int)(bits >> 32);
        this.isNegative = (upper < 0);
        int exponent = upper >> 20 & 0x7FF;
        bits &= 0xFFFFFFFFFFFFFL;
        if (exponent == 2047) {
            this.isNaN = true;
            if (bits == 0L) {
                this.digits = NumberToString.infinityDigits;
            }
            else {
                this.digits = NumberToString.nanDigits;
                this.isNegative = false;
            }
            this.nDigits = this.digits.length;
            return;
        }
        this.isNaN = false;
        int nSignificantBits;
        if (exponent == 0) {
            if (bits == 0L) {
                this.decimalExponent = 0;
                this.digits = NumberToString.zeroes;
                this.nDigits = 1;
                return;
            }
            while ((bits & 0x10000000000000L) == 0x0L) {
                bits <<= 1;
                --exponent;
            }
            nSignificantBits = 52 + exponent + 1;
            ++exponent;
        }
        else {
            bits |= 0x10000000000000L;
            nSignificantBits = 53;
        }
        exponent -= 1023;
        final int nFractBits = countSignificantBits(bits);
        final int nTinyBits = Math.max(0, nFractBits - exponent - 1);
        if (exponent <= 62 && exponent >= -21 && nTinyBits < NumberToString.powersOf5.length && nFractBits + NumberToString.nBitsPowerOf5[nTinyBits] < 64 && nTinyBits == 0) {
            long halfULP;
            if (exponent > nSignificantBits) {
                halfULP = 1L << exponent - nSignificantBits - 1;
            }
            else {
                halfULP = 0L;
            }
            if (exponent >= 52) {
                bits <<= exponent - 52;
            }
            else {
                bits >>>= 52 - exponent;
            }
            int i;
            for (i = 0; halfULP >= 10L; halfULP /= 10L, ++i) {}
            int decExp = 0;
            if (i != 0) {
                final long powerOf10 = NumberToString.powersOf5[i] << i;
                final long residue = bits % powerOf10;
                bits /= powerOf10;
                decExp += i;
                if (residue >= powerOf10 >> 1) {
                    ++bits;
                }
            }
            int ndigits = 20;
            final char[] digits0 = new char[26];
            int digitno = ndigits - 1;
            int c;
            for (c = (int)(bits % 10L), bits /= 10L; c == 0; c = (int)(bits % 10L), bits /= 10L) {
                ++decExp;
            }
            while (bits != 0L) {
                digits0[digitno--] = (char)(c + 48);
                ++decExp;
                c = (int)(bits % 10L);
                bits /= 10L;
            }
            digits0[digitno] = (char)(c + 48);
            ndigits -= digitno;
            final char[] result = new char[ndigits];
            System.arraycopy(digits0, digitno, result, 0, ndigits);
            this.digits = result;
            this.decimalExponent = decExp + 1;
            this.nDigits = ndigits;
            return;
        }
        final double d2 = Double.longBitsToDouble(0x3FF0000000000000L | (bits & 0xFFEFFFFFFFFFFFFFL));
        int decExponent = (int)Math.floor((d2 - 1.5) * 0.289529654 + 0.176091259 + exponent * 0.301029995663981);
        final int B5 = Math.max(0, -decExponent);
        int B6 = B5 + nTinyBits + exponent;
        final int S5 = Math.max(0, decExponent);
        int S6 = S5 + nTinyBits;
        final int M5 = B5;
        int M6 = B6 - nSignificantBits;
        bits >>>= 53 - nFractBits;
        B6 -= nFractBits - 1;
        final int common2factor = Math.min(B6, S6);
        B6 -= common2factor;
        S6 -= common2factor;
        M6 -= common2factor;
        if (nFractBits == 1) {
            --M6;
        }
        if (M6 < 0) {
            B6 -= M6;
            S6 -= M6;
            M6 = 0;
        }
        final char[] digits3 = new char[32];
        this.digits = digits3;
        final char[] digits2 = digits3;
        final int Bbits = nFractBits + B6 + ((B5 < NumberToString.nBitsPowerOf5.length) ? NumberToString.nBitsPowerOf5[B5] : (B5 * 3));
        final int tenSbits = S6 + 1 + ((S5 + 1 < NumberToString.nBitsPowerOf5.length) ? NumberToString.nBitsPowerOf5[S5 + 1] : ((S5 + 1) * 3));
        int ndigit;
        boolean low;
        boolean high;
        long lowDigitDifference;
        if (Bbits < 64 && tenSbits < 64) {
            long b = bits * NumberToString.powersOf5[B5] << B6;
            final long s = NumberToString.powersOf5[S5] << S6;
            long m = NumberToString.powersOf5[M5] << M6;
            final long tens = s * 10L;
            ndigit = 0;
            int q = (int)(b / s);
            b = 10L * (b % s);
            m *= 10L;
            low = (b < m);
            high = (b + m > tens);
            if (q == 0 && !high) {
                --decExponent;
            }
            else {
                digits2[ndigit++] = (char)(48 + q);
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = (high = false);
            }
            while (!low && !high) {
                q = (int)(b / s);
                b = 10L * (b % s);
                m *= 10L;
                if (m > 0L) {
                    low = (b < m);
                    high = (b + m > tens);
                }
                else {
                    low = true;
                    high = true;
                }
                if (low && q == 0) {
                    break;
                }
                digits2[ndigit++] = (char)(48 + q);
            }
            lowDigitDifference = (b << 1) - tens;
        }
        else {
            BigInteger Bval = multiplyPowerOf5And2(BigInteger.valueOf(bits), B5, B6);
            BigInteger Sval = constructPowerOf5And2(S5, S6);
            BigInteger Mval = constructPowerOf5And2(M5, M6);
            final int shiftBias = Long.numberOfLeadingZeros(bits) - 4;
            Bval = Bval.shiftLeft(shiftBias);
            Mval = Mval.shiftLeft(shiftBias);
            Sval = Sval.shiftLeft(shiftBias);
            final BigInteger tenSval = Sval.multiply(BigInteger.TEN);
            ndigit = 0;
            BigInteger[] quoRem = Bval.divideAndRemainder(Sval);
            int q = quoRem[0].intValue();
            Bval = quoRem[1].multiply(BigInteger.TEN);
            Mval = Mval.multiply(BigInteger.TEN);
            low = (Bval.compareTo(Mval) < 0);
            high = (Bval.add(Mval).compareTo(tenSval) > 0);
            if (q == 0 && !high) {
                --decExponent;
            }
            else {
                digits2[ndigit++] = (char)(48 + q);
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = (high = false);
            }
            while (!low && !high) {
                quoRem = Bval.divideAndRemainder(Sval);
                q = quoRem[0].intValue();
                Bval = quoRem[1].multiply(BigInteger.TEN);
                Mval = Mval.multiply(BigInteger.TEN);
                low = (Bval.compareTo(Mval) < 0);
                high = (Bval.add(Mval).compareTo(tenSval) > 0);
                if (low && q == 0) {
                    break;
                }
                digits2[ndigit++] = (char)(48 + q);
            }
            if (high && low) {
                Bval = Bval.shiftLeft(1);
                lowDigitDifference = Bval.compareTo(tenSval);
            }
            else {
                lowDigitDifference = 0L;
            }
        }
        this.decimalExponent = decExponent + 1;
        this.digits = digits2;
        this.nDigits = ndigit;
        if (high) {
            if (low) {
                if (lowDigitDifference == 0L) {
                    if ((digits2[this.nDigits - 1] & '\u0001') != 0x0) {
                        this.roundup();
                    }
                }
                else if (lowDigitDifference > 0L) {
                    this.roundup();
                }
            }
            else {
                this.roundup();
            }
        }
    }
    
    private static int countSignificantBits(final long bits) {
        if (bits != 0L) {
            return 64 - Long.numberOfLeadingZeros(bits) - Long.numberOfTrailingZeros(bits);
        }
        return 0;
    }
    
    private static BigInteger bigPowerOf5(final int power) {
        if (NumberToString.powerOf5Cache == null) {
            NumberToString.powerOf5Cache = new BigInteger[power + 1];
        }
        else if (NumberToString.powerOf5Cache.length <= power) {
            final BigInteger[] t = new BigInteger[power + 1];
            System.arraycopy(NumberToString.powerOf5Cache, 0, t, 0, NumberToString.powerOf5Cache.length);
            NumberToString.powerOf5Cache = t;
        }
        if (NumberToString.powerOf5Cache[power] != null) {
            return NumberToString.powerOf5Cache[power];
        }
        if (power < NumberToString.powersOf5.length) {
            return NumberToString.powerOf5Cache[power] = BigInteger.valueOf(NumberToString.powersOf5[power]);
        }
        final int q = power >> 1;
        final int r = power - q;
        BigInteger bigQ = NumberToString.powerOf5Cache[q];
        if (bigQ == null) {
            bigQ = bigPowerOf5(q);
        }
        if (r < NumberToString.powersOf5.length) {
            return NumberToString.powerOf5Cache[power] = bigQ.multiply(BigInteger.valueOf(NumberToString.powersOf5[r]));
        }
        BigInteger bigR = NumberToString.powerOf5Cache[r];
        if (bigR == null) {
            bigR = bigPowerOf5(r);
        }
        return NumberToString.powerOf5Cache[power] = bigQ.multiply(bigR);
    }
    
    private static BigInteger multiplyPowerOf5And2(final BigInteger value, final int p5, final int p2) {
        BigInteger returnValue = value;
        if (p5 != 0) {
            returnValue = returnValue.multiply(bigPowerOf5(p5));
        }
        if (p2 != 0) {
            returnValue = returnValue.shiftLeft(p2);
        }
        return returnValue;
    }
    
    private static BigInteger constructPowerOf5And2(final int p5, final int p2) {
        BigInteger v = bigPowerOf5(p5);
        if (p2 != 0) {
            v = v.shiftLeft(p2);
        }
        return v;
    }
    
    private void roundup() {
        int i;
        int q;
        for (q = this.digits[i = this.nDigits - 1]; q == 57 && i > 0; q = this.digits[--i]) {
            if (this.decimalExponent < 0) {
                --this.nDigits;
            }
            else {
                this.digits[i] = '0';
            }
        }
        if (q == 57) {
            ++this.decimalExponent;
            this.digits[0] = '1';
            return;
        }
        this.digits[i] = (char)(q + 1);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        if (this.isNegative) {
            sb.append('-');
        }
        if (this.isNaN) {
            sb.append(this.digits, 0, this.nDigits);
        }
        else if (this.decimalExponent > 0 && this.decimalExponent <= 21) {
            final int charLength = Math.min(this.nDigits, this.decimalExponent);
            sb.append(this.digits, 0, charLength);
            if (charLength < this.decimalExponent) {
                sb.append(NumberToString.zeroes, 0, this.decimalExponent - charLength);
            }
            else if (charLength < this.nDigits) {
                sb.append('.');
                sb.append(this.digits, charLength, this.nDigits - charLength);
            }
        }
        else if (this.decimalExponent <= 0 && this.decimalExponent > -6) {
            sb.append('0');
            sb.append('.');
            if (this.decimalExponent != 0) {
                sb.append(NumberToString.zeroes, 0, -this.decimalExponent);
            }
            sb.append(this.digits, 0, this.nDigits);
        }
        else {
            sb.append(this.digits[0]);
            if (this.nDigits > 1) {
                sb.append('.');
                sb.append(this.digits, 1, this.nDigits - 1);
            }
            sb.append('e');
            int exponent;
            int e;
            if (this.decimalExponent <= 0) {
                sb.append('-');
                e = (exponent = -this.decimalExponent + 1);
            }
            else {
                sb.append('+');
                e = (exponent = this.decimalExponent - 1);
            }
            if (exponent > 99) {
                sb.append((char)(e / 100 + 48));
                e %= 100;
            }
            if (exponent > 9) {
                sb.append((char)(e / 10 + 48));
                e %= 10;
            }
            sb.append((char)(e + 48));
        }
        return sb.toString();
    }
    
    static {
        powersOf5 = new long[] { 1L, 5L, 25L, 125L, 625L, 3125L, 15625L, 78125L, 390625L, 1953125L, 9765625L, 48828125L, 244140625L, 1220703125L, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L };
        nBitsPowerOf5 = new int[] { 0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61 };
        infinityDigits = new char[] { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };
        nanDigits = new char[] { 'N', 'a', 'N' };
        zeroes = new char[] { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0' };
    }
}
