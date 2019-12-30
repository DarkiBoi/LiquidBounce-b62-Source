// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.Locale;
import java.lang.invoke.MethodHandle;

public final class GlobalFunctions
{
    public static final MethodHandle PARSEINT;
    public static final MethodHandle PARSEINT_OI;
    public static final MethodHandle PARSEINT_Z;
    public static final MethodHandle PARSEINT_I;
    public static final MethodHandle PARSEINT_O;
    public static final MethodHandle PARSEFLOAT;
    public static final MethodHandle IS_NAN_I;
    public static final MethodHandle IS_NAN_J;
    public static final MethodHandle IS_NAN_D;
    public static final MethodHandle IS_NAN;
    public static final MethodHandle IS_FINITE;
    public static final MethodHandle ENCODE_URI;
    public static final MethodHandle ENCODE_URICOMPONENT;
    public static final MethodHandle DECODE_URI;
    public static final MethodHandle DECODE_URICOMPONENT;
    public static final MethodHandle ESCAPE;
    public static final MethodHandle UNESCAPE;
    public static final MethodHandle ANONYMOUS;
    private static final String UNESCAPED = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@*_+-./";
    
    private GlobalFunctions() {
    }
    
    public static double parseInt(final Object self, final Object string, final Object rad) {
        return parseIntInternal(JSType.trimLeft(JSType.toString(string)), JSType.toInt32(rad));
    }
    
    public static double parseInt(final Object self, final Object string, final int rad) {
        return parseIntInternal(JSType.trimLeft(JSType.toString(string)), rad);
    }
    
    public static double parseInt(final Object self, final Object string) {
        return parseIntInternal(JSType.trimLeft(JSType.toString(string)), 0);
    }
    
    private static double parseIntInternal(final String str, final int rad) {
        final int length = str.length();
        int radix = rad;
        if (length == 0) {
            return Double.NaN;
        }
        boolean negative = false;
        int idx = 0;
        final char firstChar = str.charAt(idx);
        if (firstChar < '0') {
            if (firstChar == '-') {
                negative = true;
            }
            else if (firstChar != '+') {
                return Double.NaN;
            }
            ++idx;
        }
        boolean stripPrefix = true;
        if (radix != 0) {
            if (radix < 2 || radix > 36) {
                return Double.NaN;
            }
            if (radix != 16) {
                stripPrefix = false;
            }
        }
        else {
            radix = 10;
        }
        if (stripPrefix && idx + 1 < length) {
            final char c1 = str.charAt(idx);
            final char c2 = str.charAt(idx + 1);
            if (c1 == '0' && (c2 == 'x' || c2 == 'X')) {
                radix = 16;
                idx += 2;
            }
        }
        double result = 0.0;
        boolean entered = false;
        while (idx < length) {
            final int digit = fastDigit(str.charAt(idx++), radix);
            if (digit < 0) {
                break;
            }
            entered = true;
            result *= radix;
            result += digit;
        }
        return entered ? (negative ? (-result) : result) : Double.NaN;
    }
    
    public static double parseFloat(final Object self, final Object string) {
        final String str = JSType.trimLeft(JSType.toString(string));
        final int length = str.length();
        if (length == 0) {
            return Double.NaN;
        }
        int start = 0;
        boolean negative = false;
        char ch = str.charAt(0);
        if (ch == '-') {
            ++start;
            negative = true;
        }
        else if (ch == '+') {
            ++start;
        }
        else if (ch == 'N' && str.startsWith("NaN")) {
            return Double.NaN;
        }
        if (start == length) {
            return Double.NaN;
        }
        ch = str.charAt(start);
        if (ch == 'I' && str.substring(start).startsWith("Infinity")) {
            return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        boolean dotSeen = false;
        boolean exponentOk = false;
        int exponentOffset = -1;
        int end = 0;
    Label_0485:
        for (end = start; end < length; ++end) {
            ch = str.charAt(end);
            switch (ch) {
                case '.': {
                    if (exponentOffset != -1) {
                        break Label_0485;
                    }
                    if (dotSeen) {
                        break Label_0485;
                    }
                    dotSeen = true;
                    break;
                }
                case 'E':
                case 'e': {
                    if (exponentOffset != -1) {
                        break Label_0485;
                    }
                    exponentOffset = end;
                    break;
                }
                case '+':
                case '-': {
                    if (exponentOffset != end - 1) {
                        break Label_0485;
                    }
                    break;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    if (exponentOffset != -1) {
                        exponentOk = true;
                        break;
                    }
                    break;
                }
                default: {
                    break Label_0485;
                }
            }
        }
        if (exponentOffset != -1 && !exponentOk) {
            end = exponentOffset;
        }
        if (start == end) {
            return Double.NaN;
        }
        try {
            final double result = Double.valueOf(str.substring(start, end));
            return negative ? (-result) : result;
        }
        catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    
    public static boolean isNaN(final Object self, final Object number) {
        return Double.isNaN(JSType.toNumber(number));
    }
    
    public static boolean isFinite(final Object self, final Object number) {
        final double value = JSType.toNumber(number);
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }
    
    public static Object encodeURI(final Object self, final Object uri) {
        return URIUtils.encodeURI(self, JSType.toString(uri));
    }
    
    public static Object encodeURIComponent(final Object self, final Object uri) {
        return URIUtils.encodeURIComponent(self, JSType.toString(uri));
    }
    
    public static Object decodeURI(final Object self, final Object uri) {
        return URIUtils.decodeURI(self, JSType.toString(uri));
    }
    
    public static Object decodeURIComponent(final Object self, final Object uri) {
        return URIUtils.decodeURIComponent(self, JSType.toString(uri));
    }
    
    public static String escape(final Object self, final Object string) {
        final String str = JSType.toString(string);
        final int length = str.length();
        if (length == 0) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        for (int k = 0; k < length; ++k) {
            final char ch = str.charAt(k);
            if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@*_+-./".indexOf(ch) != -1) {
                sb.append(ch);
            }
            else if (ch < '\u0100') {
                sb.append('%');
                if (ch < '\u0010') {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
            }
            else {
                sb.append("%u");
                if (ch < '\u1000') {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
            }
        }
        return sb.toString();
    }
    
    public static String unescape(final Object self, final Object string) {
        final String str = JSType.toString(string);
        final int length = str.length();
        if (length == 0) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        for (int k = 0; k < length; ++k) {
            char ch = str.charAt(k);
            if (ch != '%') {
                sb.append(ch);
            }
            else {
                if (k < length - 5 && str.charAt(k + 1) == 'u') {
                    try {
                        ch = (char)Integer.parseInt(str.substring(k + 2, k + 6), 16);
                        sb.append(ch);
                        k += 5;
                        continue;
                    }
                    catch (NumberFormatException ex) {}
                }
                if (k < length - 2) {
                    try {
                        ch = (char)Integer.parseInt(str.substring(k + 1, k + 3), 16);
                        sb.append(ch);
                        k += 2;
                        continue;
                    }
                    catch (NumberFormatException ex2) {}
                }
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    public static Object anonymous(final Object self) {
        return ScriptRuntime.UNDEFINED;
    }
    
    private static int fastDigit(final int ch, final int radix) {
        int n = -1;
        if (ch >= 48 && ch <= 57) {
            n = ch - 48;
        }
        else if (radix > 10) {
            if (ch >= 97 && ch <= 122) {
                n = ch - 97 + 10;
            }
            else if (ch >= 65 && ch <= 90) {
                n = ch - 65 + 10;
            }
        }
        return (n < radix) ? n : -1;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), GlobalFunctions.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        PARSEINT = findOwnMH("parseInt", Double.TYPE, Object.class, Object.class, Object.class);
        PARSEINT_OI = findOwnMH("parseInt", Double.TYPE, Object.class, Object.class, Integer.TYPE);
        PARSEINT_Z = Lookup.MH.dropArguments(Lookup.MH.dropArguments(Lookup.MH.constant(Double.TYPE, Double.NaN), 0, Boolean.TYPE), 0, Object.class);
        PARSEINT_I = Lookup.MH.dropArguments(Lookup.MH.identity(Integer.TYPE), 0, Object.class);
        PARSEINT_O = findOwnMH("parseInt", Double.TYPE, Object.class, Object.class);
        PARSEFLOAT = findOwnMH("parseFloat", Double.TYPE, Object.class, Object.class);
        IS_NAN_I = Lookup.MH.dropArguments(Lookup.MH.constant(Boolean.TYPE, false), 0, Object.class);
        IS_NAN_J = Lookup.MH.dropArguments(Lookup.MH.constant(Boolean.TYPE, false), 0, Object.class);
        IS_NAN_D = Lookup.MH.dropArguments(Lookup.MH.findStatic(MethodHandles.lookup(), Double.class, "isNaN", Lookup.MH.type(Boolean.TYPE, Double.TYPE)), 0, Object.class);
        IS_NAN = findOwnMH("isNaN", Boolean.TYPE, Object.class, Object.class);
        IS_FINITE = findOwnMH("isFinite", Boolean.TYPE, Object.class, Object.class);
        ENCODE_URI = findOwnMH("encodeURI", Object.class, Object.class, Object.class);
        ENCODE_URICOMPONENT = findOwnMH("encodeURIComponent", Object.class, Object.class, Object.class);
        DECODE_URI = findOwnMH("decodeURI", Object.class, Object.class, Object.class);
        DECODE_URICOMPONENT = findOwnMH("decodeURIComponent", Object.class, Object.class, Object.class);
        ESCAPE = findOwnMH("escape", String.class, Object.class, Object.class);
        UNESCAPE = findOwnMH("unescape", String.class, Object.class, Object.class);
        ANONYMOUS = findOwnMH("anonymous", Object.class, Object.class);
    }
}
