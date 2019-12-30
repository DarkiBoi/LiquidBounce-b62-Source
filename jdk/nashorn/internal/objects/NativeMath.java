// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeMath extends ScriptObject
{
    private static PropertyMap $nasgenmap$;
    public static final double E = 2.718281828459045;
    public static final double LN10 = 2.302585092994046;
    public static final double LN2 = 0.6931471805599453;
    public static final double LOG2E = 1.4426950408889634;
    public static final double LOG10E = 0.4342944819032518;
    public static final double PI = 3.141592653589793;
    public static final double SQRT1_2 = 0.7071067811865476;
    public static final double SQRT2 = 1.4142135623730951;
    
    private NativeMath() {
        throw new UnsupportedOperationException();
    }
    
    public static double abs(final Object self, final Object x) {
        return Math.abs(JSType.toNumber(x));
    }
    
    public static int abs(final Object self, final int x) {
        return Math.abs(x);
    }
    
    public static long abs(final Object self, final long x) {
        return Math.abs(x);
    }
    
    public static double abs(final Object self, final double x) {
        return Math.abs(x);
    }
    
    public static double acos(final Object self, final Object x) {
        return Math.acos(JSType.toNumber(x));
    }
    
    public static double acos(final Object self, final double x) {
        return Math.acos(x);
    }
    
    public static double asin(final Object self, final Object x) {
        return Math.asin(JSType.toNumber(x));
    }
    
    public static double asin(final Object self, final double x) {
        return Math.asin(x);
    }
    
    public static double atan(final Object self, final Object x) {
        return Math.atan(JSType.toNumber(x));
    }
    
    public static double atan(final Object self, final double x) {
        return Math.atan(x);
    }
    
    public static double atan2(final Object self, final Object y, final Object x) {
        return Math.atan2(JSType.toNumber(y), JSType.toNumber(x));
    }
    
    public static double atan2(final Object self, final double y, final double x) {
        return Math.atan2(y, x);
    }
    
    public static double ceil(final Object self, final Object x) {
        return Math.ceil(JSType.toNumber(x));
    }
    
    public static int ceil(final Object self, final int x) {
        return x;
    }
    
    public static long ceil(final Object self, final long x) {
        return x;
    }
    
    public static double ceil(final Object self, final double x) {
        return Math.ceil(x);
    }
    
    public static double cos(final Object self, final Object x) {
        return Math.cos(JSType.toNumber(x));
    }
    
    public static double cos(final Object self, final double x) {
        return Math.cos(x);
    }
    
    public static double exp(final Object self, final Object x) {
        return Math.exp(JSType.toNumber(x));
    }
    
    public static double floor(final Object self, final Object x) {
        return Math.floor(JSType.toNumber(x));
    }
    
    public static int floor(final Object self, final int x) {
        return x;
    }
    
    public static long floor(final Object self, final long x) {
        return x;
    }
    
    public static double floor(final Object self, final double x) {
        return Math.floor(x);
    }
    
    public static double log(final Object self, final Object x) {
        return Math.log(JSType.toNumber(x));
    }
    
    public static double log(final Object self, final double x) {
        return Math.log(x);
    }
    
    public static double max(final Object self, final Object... args) {
        switch (args.length) {
            case 0: {
                return Double.NEGATIVE_INFINITY;
            }
            case 1: {
                return JSType.toNumber(args[0]);
            }
            default: {
                double res = JSType.toNumber(args[0]);
                for (int i = 1; i < args.length; ++i) {
                    res = Math.max(res, JSType.toNumber(args[i]));
                }
                return res;
            }
        }
    }
    
    public static double max(final Object self) {
        return Double.NEGATIVE_INFINITY;
    }
    
    public static int max(final Object self, final int x, final int y) {
        return Math.max(x, y);
    }
    
    public static long max(final Object self, final long x, final long y) {
        return Math.max(x, y);
    }
    
    public static double max(final Object self, final double x, final double y) {
        return Math.max(x, y);
    }
    
    public static double max(final Object self, final Object x, final Object y) {
        return Math.max(JSType.toNumber(x), JSType.toNumber(y));
    }
    
    public static double min(final Object self, final Object... args) {
        switch (args.length) {
            case 0: {
                return Double.POSITIVE_INFINITY;
            }
            case 1: {
                return JSType.toNumber(args[0]);
            }
            default: {
                double res = JSType.toNumber(args[0]);
                for (int i = 1; i < args.length; ++i) {
                    res = Math.min(res, JSType.toNumber(args[i]));
                }
                return res;
            }
        }
    }
    
    public static double min(final Object self) {
        return Double.POSITIVE_INFINITY;
    }
    
    public static int min(final Object self, final int x, final int y) {
        return Math.min(x, y);
    }
    
    public static long min(final Object self, final long x, final long y) {
        return Math.min(x, y);
    }
    
    public static double min(final Object self, final double x, final double y) {
        return Math.min(x, y);
    }
    
    public static double min(final Object self, final Object x, final Object y) {
        return Math.min(JSType.toNumber(x), JSType.toNumber(y));
    }
    
    public static double pow(final Object self, final Object x, final Object y) {
        return Math.pow(JSType.toNumber(x), JSType.toNumber(y));
    }
    
    public static double pow(final Object self, final double x, final double y) {
        return Math.pow(x, y);
    }
    
    public static double random(final Object self) {
        return Math.random();
    }
    
    public static double round(final Object self, final Object x) {
        final double d = JSType.toNumber(x);
        if (Math.getExponent(d) >= 52) {
            return d;
        }
        return Math.copySign(Math.floor(d + 0.5), d);
    }
    
    public static double sin(final Object self, final Object x) {
        return Math.sin(JSType.toNumber(x));
    }
    
    public static double sin(final Object self, final double x) {
        return Math.sin(x);
    }
    
    public static double sqrt(final Object self, final Object x) {
        return Math.sqrt(JSType.toNumber(x));
    }
    
    public static double sqrt(final Object self, final double x) {
        return Math.sqrt(x);
    }
    
    public static double tan(final Object self, final Object x) {
        return Math.tan(JSType.toNumber(x));
    }
    
    public static double tan(final Object self, final double x) {
        return Math.tan(x);
    }
    
    static {
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeMath.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
