// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.linker.PrimitiveLookup;
import jdk.nashorn.internal.runtime.linker.NashornGuards;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeNumber extends ScriptObject
{
    static final MethodHandle WRAPFILTER;
    private static final MethodHandle PROTOFILTER;
    public static final double MAX_VALUE = Double.MAX_VALUE;
    public static final double MIN_VALUE = Double.MIN_VALUE;
    public static final double NaN = Double.NaN;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    private final double value;
    private static PropertyMap $nasgenmap$;
    
    private NativeNumber(final double value, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.value = value;
    }
    
    NativeNumber(final double value, final Global global) {
        this(value, global.getNumberPrototype(), NativeNumber.$nasgenmap$);
    }
    
    private NativeNumber(final double value) {
        this(value, Global.instance());
    }
    
    @Override
    public String safeToString() {
        return "[Number " + this.toString() + "]";
    }
    
    @Override
    public String toString() {
        return Double.toString(this.getValue());
    }
    
    public double getValue() {
        return this.doubleValue();
    }
    
    public double doubleValue() {
        return this.value;
    }
    
    @Override
    public String getClassName() {
        return "Number";
    }
    
    public static Object constructor(final boolean newObj, final Object self, final Object... args) {
        final double num = (args.length > 0) ? JSType.toNumber(args[0]) : 0.0;
        return newObj ? new NativeNumber(num) : Double.valueOf(num);
    }
    
    public static String toFixed(final Object self, final Object fractionDigits) {
        return toFixed(self, JSType.toInteger(fractionDigits));
    }
    
    public static String toFixed(final Object self, final int fractionDigits) {
        if (fractionDigits < 0 || fractionDigits > 20) {
            throw ECMAErrors.rangeError("invalid.fraction.digits", "toFixed");
        }
        final double x = getNumberValue(self);
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Math.abs(x) >= 1.0E21) {
            return JSType.toString(x);
        }
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        format.setMinimumFractionDigits(fractionDigits);
        format.setMaximumFractionDigits(fractionDigits);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(x);
    }
    
    public static String toExponential(final Object self, final Object fractionDigits) {
        final double x = getNumberValue(self);
        final boolean trimZeros = fractionDigits == ScriptRuntime.UNDEFINED;
        final int f = trimZeros ? 16 : JSType.toInteger(fractionDigits);
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Double.isInfinite(x)) {
            return (x > 0.0) ? "Infinity" : "-Infinity";
        }
        if (fractionDigits != ScriptRuntime.UNDEFINED && (f < 0 || f > 20)) {
            throw ECMAErrors.rangeError("invalid.fraction.digits", "toExponential");
        }
        final String res = String.format(Locale.US, "%1." + f + "e", x);
        return fixExponent(res, trimZeros);
    }
    
    public static String toPrecision(final Object self, final Object precision) {
        final double x = getNumberValue(self);
        if (precision == ScriptRuntime.UNDEFINED) {
            return JSType.toString(x);
        }
        return toPrecision(x, JSType.toInteger(precision));
    }
    
    public static String toPrecision(final Object self, final int precision) {
        return toPrecision(getNumberValue(self), precision);
    }
    
    private static String toPrecision(final double x, final int p) {
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Double.isInfinite(x)) {
            return (x > 0.0) ? "Infinity" : "-Infinity";
        }
        if (p < 1 || p > 21) {
            throw ECMAErrors.rangeError("invalid.precision", new String[0]);
        }
        if (x == 0.0 && p <= 1) {
            return "0";
        }
        return fixExponent(String.format(Locale.US, "%." + p + "g", x), false);
    }
    
    public static String toString(final Object self, final Object radix) {
        if (radix != ScriptRuntime.UNDEFINED) {
            final int intRadix = JSType.toInteger(radix);
            if (intRadix != 10) {
                if (intRadix < 2 || intRadix > 36) {
                    throw ECMAErrors.rangeError("invalid.radix", new String[0]);
                }
                return JSType.toString(getNumberValue(self), intRadix);
            }
        }
        return JSType.toString(getNumberValue(self));
    }
    
    public static String toLocaleString(final Object self) {
        return JSType.toString(getNumberValue(self));
    }
    
    public static double valueOf(final Object self) {
        return getNumberValue(self);
    }
    
    public static GuardedInvocation lookupPrimitive(final LinkRequest request, final Object receiver) {
        return PrimitiveLookup.lookupPrimitive(request, NashornGuards.getNumberGuard(), new NativeNumber(((Number)receiver).doubleValue()), NativeNumber.WRAPFILTER, NativeNumber.PROTOFILTER);
    }
    
    private static NativeNumber wrapFilter(final Object receiver) {
        return new NativeNumber(((Number)receiver).doubleValue());
    }
    
    private static Object protoFilter(final Object object) {
        return Global.instance().getNumberPrototype();
    }
    
    private static double getNumberValue(final Object self) {
        if (self instanceof Number) {
            return ((Number)self).doubleValue();
        }
        if (self instanceof NativeNumber) {
            return ((NativeNumber)self).getValue();
        }
        if (self != null && self == Global.instance().getNumberPrototype()) {
            return 0.0;
        }
        throw ECMAErrors.typeError("not.a.number", ScriptRuntime.safeToString(self));
    }
    
    private static String fixExponent(final String str, final boolean trimZeros) {
        final int index = str.indexOf(101);
        if (index < 1) {
            return str;
        }
        final int expPadding = (str.charAt(index + 2) == '0') ? 3 : 2;
        int fractionOffset = index;
        if (trimZeros) {
            assert fractionOffset > 0;
            for (char c = str.charAt(fractionOffset - 1); fractionOffset > 1 && (c == '0' || c == '.'); c = str.charAt(--fractionOffset - 1)) {}
        }
        if (fractionOffset < index || expPadding == 3) {
            return str.substring(0, fractionOffset) + str.substring(index, index + 2) + str.substring(index + expPadding);
        }
        return str;
    }
    
    private static MethodHandle findOwnMH(final String name, final MethodType type) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeNumber.class, name, type);
    }
    
    static {
        WRAPFILTER = findOwnMH("wrapFilter", Lookup.MH.type(NativeNumber.class, Object.class));
        PROTOFILTER = findOwnMH("protoFilter", Lookup.MH.type(Object.class, Object.class));
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeNumber.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
