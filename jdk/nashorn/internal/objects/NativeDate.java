// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import jdk.nashorn.internal.parser.DateParser;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.TimeZone;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeDate extends ScriptObject
{
    private static final String INVALID_DATE = "Invalid Date";
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 3;
    private static final int MINUTE = 4;
    private static final int SECOND = 5;
    private static final int MILLISECOND = 6;
    private static final int FORMAT_DATE_TIME = 0;
    private static final int FORMAT_DATE = 1;
    private static final int FORMAT_TIME = 2;
    private static final int FORMAT_LOCAL_DATE_TIME = 3;
    private static final int FORMAT_LOCAL_DATE = 4;
    private static final int FORMAT_LOCAL_TIME = 5;
    private static final int hoursPerDay = 24;
    private static final int minutesPerHour = 60;
    private static final int secondsPerMinute = 60;
    private static final int msPerSecond = 1000;
    private static final int msPerMinute = 60000;
    private static final double msPerHour = 3600000.0;
    private static final double msPerDay = 8.64E7;
    private static int[][] firstDayInMonth;
    private static String[] weekDays;
    private static String[] months;
    private static final Object TO_ISO_STRING;
    private double time;
    private final TimeZone timezone;
    private static PropertyMap $nasgenmap$;
    
    private static InvokeByName getTO_ISO_STRING() {
        return Global.instance().getInvokeByName(NativeDate.TO_ISO_STRING, new Callable<InvokeByName>() {
            @Override
            public InvokeByName call() {
                return new InvokeByName("toISOString", ScriptObject.class, Object.class, (Class<?>[])new Class[] { Object.class });
            }
        });
    }
    
    private NativeDate(final double time, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        final ScriptEnvironment env = Global.getEnv();
        this.time = time;
        this.timezone = env._timezone;
    }
    
    NativeDate(final double time, final ScriptObject proto) {
        this(time, proto, NativeDate.$nasgenmap$);
    }
    
    NativeDate(final double time, final Global global) {
        this(time, global.getDatePrototype(), NativeDate.$nasgenmap$);
    }
    
    private NativeDate(final double time) {
        this(time, Global.instance());
    }
    
    private NativeDate() {
        this((double)System.currentTimeMillis());
    }
    
    @Override
    public String getClassName() {
        return "Date";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return super.getDefaultValue((hint == null) ? String.class : hint);
    }
    
    public static Object construct(final boolean isNew, final Object self) {
        final NativeDate result = new NativeDate();
        return isNew ? result : toStringImpl(result, 0);
    }
    
    public static Object construct(final boolean isNew, final Object self, final Object... args) {
        if (!isNew) {
            return toStringImpl(new NativeDate(), 0);
        }
        NativeDate result = null;
        switch (args.length) {
            case 0: {
                result = new NativeDate();
                break;
            }
            case 1: {
                final Object arg = JSType.toPrimitive(args[0]);
                double num;
                if (JSType.isString(arg)) {
                    num = parseDateString(arg.toString());
                }
                else {
                    num = timeClip(JSType.toNumber(args[0]));
                }
                result = new NativeDate(num);
                break;
            }
            default: {
                result = new NativeDate(0.0);
                final double[] d = convertCtorArgs(args);
                if (d == null) {
                    result.setTime(Double.NaN);
                    break;
                }
                final double time = timeClip(utc(makeDate(d), result.getTimeZone()));
                result.setTime(time);
                break;
            }
        }
        return result;
    }
    
    @Override
    public String safeToString() {
        final String str = this.isValidDate() ? toISOStringImpl(this) : "Invalid Date";
        return "[Date " + str + "]";
    }
    
    @Override
    public String toString() {
        return this.isValidDate() ? toString(this).toString() : "Invalid Date";
    }
    
    public static double parse(final Object self, final Object string) {
        return parseDateString(JSType.toString(string));
    }
    
    public static double UTC(final Object self, final Object... args) {
        final NativeDate nd = new NativeDate(0.0);
        final double[] d = convertCtorArgs(args);
        final double time = (d == null) ? Double.NaN : timeClip(makeDate(d));
        nd.setTime(time);
        return time;
    }
    
    public static double now(final Object self) {
        return (double)System.currentTimeMillis();
    }
    
    public static String toString(final Object self) {
        return toStringImpl(self, 0);
    }
    
    public static String toDateString(final Object self) {
        return toStringImpl(self, 1);
    }
    
    public static String toTimeString(final Object self) {
        return toStringImpl(self, 2);
    }
    
    public static String toLocaleString(final Object self) {
        return toStringImpl(self, 3);
    }
    
    public static String toLocaleDateString(final Object self) {
        return toStringImpl(self, 4);
    }
    
    public static String toLocaleTimeString(final Object self) {
        return toStringImpl(self, 5);
    }
    
    public static double valueOf(final Object self) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null) ? nd.getTime() : Double.NaN;
    }
    
    public static double getTime(final Object self) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null) ? nd.getTime() : Double.NaN;
    }
    
    public static Object getFullYear(final Object self) {
        return getField(self, 0);
    }
    
    public static double getUTCFullYear(final Object self) {
        return getUTCField(self, 0);
    }
    
    public static double getYear(final Object self) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null && nd.isValidDate()) ? (yearFromTime(nd.getLocalTime()) - 1900) : Double.NaN;
    }
    
    public static double getMonth(final Object self) {
        return getField(self, 1);
    }
    
    public static double getUTCMonth(final Object self) {
        return getUTCField(self, 1);
    }
    
    public static double getDate(final Object self) {
        return getField(self, 2);
    }
    
    public static double getUTCDate(final Object self) {
        return getUTCField(self, 2);
    }
    
    public static double getDay(final Object self) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null && nd.isValidDate()) ? weekDay(nd.getLocalTime()) : Double.NaN;
    }
    
    public static double getUTCDay(final Object self) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null && nd.isValidDate()) ? weekDay(nd.getTime()) : Double.NaN;
    }
    
    public static double getHours(final Object self) {
        return getField(self, 3);
    }
    
    public static double getUTCHours(final Object self) {
        return getUTCField(self, 3);
    }
    
    public static double getMinutes(final Object self) {
        return getField(self, 4);
    }
    
    public static double getUTCMinutes(final Object self) {
        return getUTCField(self, 4);
    }
    
    public static double getSeconds(final Object self) {
        return getField(self, 5);
    }
    
    public static double getUTCSeconds(final Object self) {
        return getUTCField(self, 5);
    }
    
    public static double getMilliseconds(final Object self) {
        return getField(self, 6);
    }
    
    public static double getUTCMilliseconds(final Object self) {
        return getUTCField(self, 6);
    }
    
    public static double getTimezoneOffset(final Object self) {
        final NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            final long msec = (long)nd.getTime();
            return -nd.getTimeZone().getOffset(msec) / 60000;
        }
        return Double.NaN;
    }
    
    public static double setTime(final Object self, final Object time) {
        final NativeDate nd = getNativeDate(self);
        final double num = timeClip(JSType.toNumber(time));
        nd.setTime(num);
        return num;
    }
    
    public static double setMilliseconds(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 6, args, true);
        return nd.getTime();
    }
    
    public static double setUTCMilliseconds(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 6, args, false);
        return nd.getTime();
    }
    
    public static double setSeconds(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 5, args, true);
        return nd.getTime();
    }
    
    public static double setUTCSeconds(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 5, args, false);
        return nd.getTime();
    }
    
    public static double setMinutes(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 4, args, true);
        return nd.getTime();
    }
    
    public static double setUTCMinutes(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 4, args, false);
        return nd.getTime();
    }
    
    public static double setHours(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 3, args, true);
        return nd.getTime();
    }
    
    public static double setUTCHours(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 3, args, false);
        return nd.getTime();
    }
    
    public static double setDate(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 2, args, true);
        return nd.getTime();
    }
    
    public static double setUTCDate(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 2, args, false);
        return nd.getTime();
    }
    
    public static double setMonth(final Object self, final Object... args) {
        final NativeDate nd = getNativeDate(self);
        setFields(nd, 1, args, true);
        return nd.getTime();
    }
    
    public static double setUTCMonth(final Object self, final Object... args) {
        final NativeDate nd = ensureNativeDate(self);
        setFields(nd, 1, args, false);
        return nd.getTime();
    }
    
    public static double setFullYear(final Object self, final Object... args) {
        final NativeDate nd = ensureNativeDate(self);
        if (nd.isValidDate()) {
            setFields(nd, 0, args, true);
        }
        else {
            final double[] d = convertArgs(args, 0.0, 0, 0, 3);
            if (d != null) {
                nd.setTime(timeClip(utc(makeDate(makeDay(d[0], d[1], d[2]), 0.0), nd.getTimeZone())));
            }
            else {
                nd.setTime(Double.NaN);
            }
        }
        return nd.getTime();
    }
    
    public static double setUTCFullYear(final Object self, final Object... args) {
        final NativeDate nd = ensureNativeDate(self);
        if (nd.isValidDate()) {
            setFields(nd, 0, args, false);
        }
        else {
            final double[] d = convertArgs(args, 0.0, 0, 0, 3);
            nd.setTime(timeClip(makeDate(makeDay(d[0], d[1], d[2]), 0.0)));
        }
        return nd.getTime();
    }
    
    public static double setYear(final Object self, final Object year) {
        final NativeDate nd = getNativeDate(self);
        if (Double.isNaN(nd.getTime())) {
            nd.setTime(utc(0.0, nd.getTimeZone()));
        }
        final double yearNum = JSType.toNumber(year);
        if (Double.isNaN(yearNum)) {
            nd.setTime(Double.NaN);
            return nd.getTime();
        }
        int yearInt = (int)yearNum;
        if (0 <= yearInt && yearInt <= 99) {
            yearInt += 1900;
        }
        setFields(nd, 0, new Object[] { yearInt }, true);
        return nd.getTime();
    }
    
    public static String toUTCString(final Object self) {
        return toGMTStringImpl(self);
    }
    
    public static String toGMTString(final Object self) {
        return toGMTStringImpl(self);
    }
    
    public static String toISOString(final Object self) {
        return toISOStringImpl(self);
    }
    
    public static Object toJSON(final Object self, final Object key) {
        final Object selfObj = Global.toObject(self);
        if (!(selfObj instanceof ScriptObject)) {
            return null;
        }
        final ScriptObject sobj = (ScriptObject)selfObj;
        final Object value = sobj.getDefaultValue(Number.class);
        if (value instanceof Number) {
            final double num = ((Number)value).doubleValue();
            if (Double.isInfinite(num) || Double.isNaN(num)) {
                return null;
            }
        }
        try {
            final InvokeByName toIsoString = getTO_ISO_STRING();
            final Object func = toIsoString.getGetter().invokeExact(sobj);
            if (Bootstrap.isCallable(func)) {
                return toIsoString.getInvoker().invokeExact(func, sobj, key);
            }
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(func));
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private static double parseDateString(final String str) {
        final DateParser parser = new DateParser(str);
        if (parser.parse()) {
            final Integer[] fields = parser.getDateFields();
            double d = makeDate(fields);
            if (fields[7] != null) {
                d -= fields[7] * 60000;
            }
            else {
                d = utc(d, Global.getEnv()._timezone);
            }
            d = timeClip(d);
            return d;
        }
        return Double.NaN;
    }
    
    private static void zeroPad(final StringBuilder sb, final int n, final int length) {
        for (int l = 1, d = 10; l < length; ++l, d *= 10) {
            if (n < d) {
                sb.append('0');
            }
        }
        sb.append(n);
    }
    
    private static String toStringImpl(final Object self, final int format) {
        final NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            final StringBuilder sb = new StringBuilder(40);
            final double t = nd.getLocalTime();
            switch (format) {
                case 0:
                case 1:
                case 3: {
                    sb.append(NativeDate.weekDays[weekDay(t)]).append(' ').append(NativeDate.months[monthFromTime(t)]).append(' ');
                    zeroPad(sb, dayFromTime(t), 2);
                    sb.append(' ');
                    zeroPad(sb, yearFromTime(t), 4);
                    if (format == 1) {
                        break;
                    }
                    sb.append(' ');
                }
                case 2: {
                    final TimeZone tz = nd.getTimeZone();
                    final double utcTime = nd.getTime();
                    int offset = tz.getOffset((long)utcTime) / 60000;
                    final boolean inDaylightTime = offset != tz.getRawOffset() / 60000;
                    offset = offset / 60 * 100 + offset % 60;
                    zeroPad(sb, hourFromTime(t), 2);
                    sb.append(':');
                    zeroPad(sb, minFromTime(t), 2);
                    sb.append(':');
                    zeroPad(sb, secFromTime(t), 2);
                    sb.append(" GMT").append((offset < 0) ? '-' : '+');
                    zeroPad(sb, Math.abs(offset), 4);
                    sb.append(" (").append(tz.getDisplayName(inDaylightTime, 0, Locale.US)).append(')');
                    break;
                }
                case 4: {
                    zeroPad(sb, yearFromTime(t), 4);
                    sb.append('-');
                    zeroPad(sb, monthFromTime(t) + 1, 2);
                    sb.append('-');
                    zeroPad(sb, dayFromTime(t), 2);
                    break;
                }
                case 5: {
                    zeroPad(sb, hourFromTime(t), 2);
                    sb.append(':');
                    zeroPad(sb, minFromTime(t), 2);
                    sb.append(':');
                    zeroPad(sb, secFromTime(t), 2);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("format: " + format);
                }
            }
            return sb.toString();
        }
        return "Invalid Date";
    }
    
    private static String toGMTStringImpl(final Object self) {
        final NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            final StringBuilder sb = new StringBuilder(29);
            final double t = nd.getTime();
            sb.append(NativeDate.weekDays[weekDay(t)]).append(", ");
            zeroPad(sb, dayFromTime(t), 2);
            sb.append(' ').append(NativeDate.months[monthFromTime(t)]).append(' ');
            zeroPad(sb, yearFromTime(t), 4);
            sb.append(' ');
            zeroPad(sb, hourFromTime(t), 2);
            sb.append(':');
            zeroPad(sb, minFromTime(t), 2);
            sb.append(':');
            zeroPad(sb, secFromTime(t), 2);
            sb.append(" GMT");
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }
    
    private static String toISOStringImpl(final Object self) {
        final NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            final StringBuilder sb = new StringBuilder(24);
            final double t = nd.getTime();
            zeroPad(sb, yearFromTime(t), 4);
            sb.append('-');
            zeroPad(sb, monthFromTime(t) + 1, 2);
            sb.append('-');
            zeroPad(sb, dayFromTime(t), 2);
            sb.append('T');
            zeroPad(sb, hourFromTime(t), 2);
            sb.append(':');
            zeroPad(sb, minFromTime(t), 2);
            sb.append(':');
            zeroPad(sb, secFromTime(t), 2);
            sb.append('.');
            zeroPad(sb, msFromTime(t), 3);
            sb.append("Z");
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }
    
    private static double day(final double t) {
        return Math.floor(t / 8.64E7);
    }
    
    private static double timeWithinDay(final double t) {
        final double val = t % 8.64E7;
        return (val < 0.0) ? (val + 8.64E7) : val;
    }
    
    private static boolean isLeapYear(final int y) {
        return y % 4 == 0 && (y % 100 != 0 || y % 400 == 0);
    }
    
    private static int daysInYear(final int y) {
        return isLeapYear(y) ? 366 : 365;
    }
    
    private static double dayFromYear(final double y) {
        return 365.0 * (y - 1970.0) + Math.floor((y - 1969.0) / 4.0) - Math.floor((y - 1901.0) / 100.0) + Math.floor((y - 1601.0) / 400.0);
    }
    
    private static double timeFromYear(final int y) {
        return dayFromYear(y) * 8.64E7;
    }
    
    private static int yearFromTime(final double t) {
        int y = (int)Math.floor(t / 3.1556952E10) + 1970;
        final double t2 = timeFromYear(y);
        if (t2 > t) {
            --y;
        }
        else if (t2 + 8.64E7 * daysInYear(y) <= t) {
            ++y;
        }
        return y;
    }
    
    private static int dayWithinYear(final double t, final int year) {
        return (int)(day(t) - dayFromYear(year));
    }
    
    private static int monthFromTime(final double t) {
        final int year = yearFromTime(t);
        int day;
        int[] firstDay;
        int month;
        for (day = dayWithinYear(t, year), firstDay = NativeDate.firstDayInMonth[isLeapYear(year)], month = 0; month < 11 && firstDay[month + 1] <= day; ++month) {}
        return month;
    }
    
    private static int dayFromTime(final double t) {
        final int year = yearFromTime(t);
        int day;
        int[] firstDay;
        int month;
        for (day = dayWithinYear(t, year), firstDay = NativeDate.firstDayInMonth[isLeapYear(year)], month = 0; month < 11 && firstDay[month + 1] <= day; ++month) {}
        return 1 + day - firstDay[month];
    }
    
    private static int dayFromMonth(final int month, final int year) {
        assert month >= 0 && month <= 11;
        final int[] firstDay = NativeDate.firstDayInMonth[isLeapYear(year)];
        return firstDay[month];
    }
    
    private static int weekDay(final double time) {
        final int day = (int)(day(time) + 4.0) % 7;
        return (day < 0) ? (day + 7) : day;
    }
    
    private static double localTime(final double time, final TimeZone tz) {
        return time + tz.getOffset((long)time);
    }
    
    private static double utc(final double time, final TimeZone tz) {
        return time - tz.getOffset((long)(time - tz.getRawOffset()));
    }
    
    private static int hourFromTime(final double t) {
        final int h = (int)(Math.floor(t / 3600000.0) % 24.0);
        return (h < 0) ? (h + 24) : h;
    }
    
    private static int minFromTime(final double t) {
        final int m = (int)(Math.floor(t / 60000.0) % 60.0);
        return (m < 0) ? (m + 60) : m;
    }
    
    private static int secFromTime(final double t) {
        final int s = (int)(Math.floor(t / 1000.0) % 60.0);
        return (s < 0) ? (s + 60) : s;
    }
    
    private static int msFromTime(final double t) {
        final int m = (int)(t % 1000.0);
        return (m < 0) ? (m + 1000) : m;
    }
    
    private static int valueFromTime(final int unit, final double t) {
        switch (unit) {
            case 0: {
                return yearFromTime(t);
            }
            case 1: {
                return monthFromTime(t);
            }
            case 2: {
                return dayFromTime(t);
            }
            case 3: {
                return hourFromTime(t);
            }
            case 4: {
                return minFromTime(t);
            }
            case 5: {
                return secFromTime(t);
            }
            case 6: {
                return msFromTime(t);
            }
            default: {
                throw new IllegalArgumentException(Integer.toString(unit));
            }
        }
    }
    
    private static double makeTime(final double hour, final double min, final double sec, final double ms) {
        return hour * 3600000.0 + min * 60000.0 + sec * 1000.0 + ms;
    }
    
    private static double makeDay(final double year, final double month, final double date) {
        final double y = year + Math.floor(month / 12.0);
        int m = (int)(month % 12.0);
        if (m < 0) {
            m += 12;
        }
        double d = dayFromYear(y);
        d += dayFromMonth(m, (int)y);
        return d + date - 1.0;
    }
    
    private static double makeDate(final double day, final double time) {
        return day * 8.64E7 + time;
    }
    
    private static double makeDate(final Integer[] d) {
        final double time = makeDay(d[0], d[1], d[2]) * 8.64E7;
        return time + makeTime(d[3], d[4], d[5], d[6]);
    }
    
    private static double makeDate(final double[] d) {
        final double time = makeDay(d[0], d[1], d[2]) * 8.64E7;
        return time + makeTime(d[3], d[4], d[5], d[6]);
    }
    
    private static double[] convertCtorArgs(final Object[] args) {
        final double[] d = new double[7];
        boolean nullReturn = false;
        for (int i = 0; i < d.length; ++i) {
            if (i < args.length) {
                final double darg = JSType.toNumber(args[i]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d[i] = (double)(long)darg;
            }
            else {
                d[i] = ((i == 2) ? 1.0 : 0.0);
            }
        }
        if (0.0 <= d[0] && d[0] <= 99.0) {
            final double[] array = d;
            final int n = 0;
            array[n] += 1900.0;
        }
        return (double[])(nullReturn ? null : d);
    }
    
    private static double[] convertArgs(final Object[] args, final double time, final int fieldId, final int start, final int length) {
        final double[] d = new double[length];
        boolean nullReturn = false;
        for (int i = start; i < start + length; ++i) {
            if (fieldId <= i && i < fieldId + args.length) {
                final double darg = JSType.toNumber(args[i - fieldId]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d[i - start] = (double)(long)darg;
            }
            else {
                if (i == fieldId) {
                    nullReturn = true;
                }
                if (!nullReturn && !Double.isNaN(time)) {
                    d[i - start] = valueFromTime(i, time);
                }
            }
        }
        return (double[])(nullReturn ? null : d);
    }
    
    private static double timeClip(final double time) {
        if (Double.isInfinite(time) || Double.isNaN(time) || Math.abs(time) > 8.64E15) {
            return Double.NaN;
        }
        return (double)(long)time;
    }
    
    private static NativeDate ensureNativeDate(final Object self) {
        return getNativeDate(self);
    }
    
    private static NativeDate getNativeDate(final Object self) {
        if (self instanceof NativeDate) {
            return (NativeDate)self;
        }
        if (self != null && self == Global.instance().getDatePrototype()) {
            return Global.instance().getDefaultDate();
        }
        throw ECMAErrors.typeError("not.a.date", ScriptRuntime.safeToString(self));
    }
    
    private static double getField(final Object self, final int field) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null && nd.isValidDate()) ? valueFromTime(field, nd.getLocalTime()) : Double.NaN;
    }
    
    private static double getUTCField(final Object self, final int field) {
        final NativeDate nd = getNativeDate(self);
        return (nd != null && nd.isValidDate()) ? valueFromTime(field, nd.getTime()) : Double.NaN;
    }
    
    private static void setFields(final NativeDate nd, final int fieldId, final Object[] args, final boolean local) {
        int start;
        int length;
        if (fieldId < 3) {
            start = 0;
            length = 3;
        }
        else {
            start = 3;
            length = 4;
        }
        final double time = local ? nd.getLocalTime() : nd.getTime();
        final double[] d = convertArgs(args, time, fieldId, start, length);
        if (!nd.isValidDate()) {
            return;
        }
        double newTime;
        if (d == null) {
            newTime = Double.NaN;
        }
        else {
            if (start == 0) {
                newTime = makeDate(makeDay(d[0], d[1], d[2]), timeWithinDay(time));
            }
            else {
                newTime = makeDate(day(time), makeTime(d[0], d[1], d[2], d[3]));
            }
            if (local) {
                newTime = utc(newTime, nd.getTimeZone());
            }
            newTime = timeClip(newTime);
        }
        nd.setTime(newTime);
    }
    
    private boolean isValidDate() {
        return !Double.isNaN(this.time);
    }
    
    private double getLocalTime() {
        return localTime(this.time, this.timezone);
    }
    
    private double getTime() {
        return this.time;
    }
    
    private void setTime(final double time) {
        this.time = time;
    }
    
    private TimeZone getTimeZone() {
        return this.timezone;
    }
    
    static {
        NativeDate.firstDayInMonth = new int[][] { { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 }, { 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 } };
        NativeDate.weekDays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        NativeDate.months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        TO_ISO_STRING = new Object();
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeDate.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
