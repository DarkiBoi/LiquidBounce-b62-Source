// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.linker.PrimitiveLookup;
import java.util.Locale;
import java.util.LinkedList;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.text.Collator;
import jdk.nashorn.internal.runtime.ConsString;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.runtime.linker.NashornGuards;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.OptimisticBuiltins;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeString extends ScriptObject implements OptimisticBuiltins
{
    private final CharSequence value;
    static final MethodHandle WRAPFILTER;
    private static final MethodHandle PROTOFILTER;
    private static PropertyMap $nasgenmap$;
    
    private NativeString(final CharSequence value) {
        this(value, Global.instance());
    }
    
    NativeString(final CharSequence value, final Global global) {
        this(value, global.getStringPrototype(), NativeString.$nasgenmap$);
    }
    
    private NativeString(final CharSequence value, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        assert JSType.isString(value);
        this.value = value;
    }
    
    @Override
    public String safeToString() {
        return "[String " + this.toString() + "]";
    }
    
    @Override
    public String toString() {
        return this.getStringValue();
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof NativeString && this.getStringValue().equals(((NativeString)other).getStringValue());
    }
    
    @Override
    public int hashCode() {
        return this.getStringValue().hashCode();
    }
    
    private String getStringValue() {
        return (String)((this.value instanceof String) ? this.value : this.value.toString());
    }
    
    private CharSequence getValue() {
        return this.value;
    }
    
    @Override
    public String getClassName() {
        return "String";
    }
    
    @Override
    public Object getLength() {
        return this.value.length();
    }
    
    @Override
    protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request, final String operator) {
        final String name = desc.getNameToken(2);
        if ("length".equals(name) && "getMethod".equals(operator)) {
            return null;
        }
        return super.findGetMethod(desc, request, operator);
    }
    
    @Override
    protected GuardedInvocation findGetIndexMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        final Object self = request.getReceiver();
        final Class<?> returnType = desc.getMethodType().returnType();
        if (returnType == Object.class && JSType.isString(self)) {
            try {
                return new GuardedInvocation(Lookup.MH.findStatic(MethodHandles.lookup(), NativeString.class, "get", desc.getMethodType()), NashornGuards.getStringGuard());
            }
            catch (MethodHandleFactory.LookupException ex) {}
        }
        return super.findGetIndexMethod(desc, request);
    }
    
    private static Object get(final Object self, final Object key) {
        final CharSequence cs = JSType.toCharSequence(self);
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        if (index >= 0 && index < cs.length()) {
            return String.valueOf(cs.charAt(index));
        }
        return ((ScriptObject)Global.toObject(self)).get(primitiveKey);
    }
    
    private static Object get(final Object self, final double key) {
        if (JSType.isRepresentableAsInt(key)) {
            return get(self, (int)key);
        }
        return ((ScriptObject)Global.toObject(self)).get(key);
    }
    
    private static Object get(final Object self, final long key) {
        final CharSequence cs = JSType.toCharSequence(self);
        if (key >= 0L && key < cs.length()) {
            return String.valueOf(cs.charAt((int)key));
        }
        return ((ScriptObject)Global.toObject(self)).get((double)key);
    }
    
    private static Object get(final Object self, final int key) {
        final CharSequence cs = JSType.toCharSequence(self);
        if (key >= 0 && key < cs.length()) {
            return String.valueOf(cs.charAt(key));
        }
        return ((ScriptObject)Global.toObject(self)).get(key);
    }
    
    @Override
    public Object get(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        if (index >= 0 && index < this.value.length()) {
            return String.valueOf(this.value.charAt(index));
        }
        return super.get(primitiveKey);
    }
    
    @Override
    public Object get(final double key) {
        if (JSType.isRepresentableAsInt(key)) {
            return this.get((int)key);
        }
        return super.get(key);
    }
    
    @Override
    public Object get(final int key) {
        if (key >= 0 && key < this.value.length()) {
            return String.valueOf(this.value.charAt(key));
        }
        return super.get(key);
    }
    
    @Override
    public int getInt(final Object key, final int programPoint) {
        return JSType.toInt32MaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public int getInt(final double key, final int programPoint) {
        return JSType.toInt32MaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public int getInt(final int key, final int programPoint) {
        return JSType.toInt32MaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public double getDouble(final Object key, final int programPoint) {
        return JSType.toNumberMaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public double getDouble(final double key, final int programPoint) {
        return JSType.toNumberMaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public double getDouble(final int key, final int programPoint) {
        return JSType.toNumberMaybeOptimistic(this.get(key), programPoint);
    }
    
    @Override
    public boolean has(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return this.isValidStringIndex(index) || super.has(primitiveKey);
    }
    
    @Override
    public boolean has(final int key) {
        return this.isValidStringIndex(key) || super.has(key);
    }
    
    @Override
    public boolean has(final double key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return this.isValidStringIndex(index) || super.has(key);
    }
    
    @Override
    public boolean hasOwnProperty(final Object key) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return this.isValidStringIndex(index) || super.hasOwnProperty(primitiveKey);
    }
    
    @Override
    public boolean hasOwnProperty(final int key) {
        return this.isValidStringIndex(key) || super.hasOwnProperty(key);
    }
    
    @Override
    public boolean hasOwnProperty(final double key) {
        final int index = ArrayIndex.getArrayIndex(key);
        return this.isValidStringIndex(index) || super.hasOwnProperty(key);
    }
    
    @Override
    public boolean delete(final int key, final boolean strict) {
        return !this.checkDeleteIndex(key, strict) && super.delete(key, strict);
    }
    
    @Override
    public boolean delete(final double key, final boolean strict) {
        final int index = ArrayIndex.getArrayIndex(key);
        return !this.checkDeleteIndex(index, strict) && super.delete(key, strict);
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        final Object primitiveKey = JSType.toPrimitive(key, String.class);
        final int index = ArrayIndex.getArrayIndex(primitiveKey);
        return !this.checkDeleteIndex(index, strict) && super.delete(primitiveKey, strict);
    }
    
    private boolean checkDeleteIndex(final int index, final boolean strict) {
        if (!this.isValidStringIndex(index)) {
            return false;
        }
        if (strict) {
            throw ECMAErrors.typeError("cant.delete.property", Integer.toString(index), ScriptRuntime.safeToString(this));
        }
        return true;
    }
    
    @Override
    public Object getOwnPropertyDescriptor(final String key) {
        final int index = ArrayIndex.getArrayIndex(key);
        if (index >= 0 && index < this.value.length()) {
            final Global global = Global.instance();
            return global.newDataDescriptor(String.valueOf(this.value.charAt(index)), false, true, false);
        }
        return super.getOwnPropertyDescriptor(key);
    }
    
    @Override
    protected String[] getOwnKeys(final boolean all, final Set<String> nonEnumerable) {
        final List<Object> keys = new ArrayList<Object>();
        for (int i = 0; i < this.value.length(); ++i) {
            keys.add(JSType.toString(i));
        }
        keys.addAll(Arrays.asList(super.getOwnKeys(all, nonEnumerable)));
        return keys.toArray(new String[keys.size()]);
    }
    
    public static Object length(final Object self) {
        return getCharSequence(self).length();
    }
    
    public static String fromCharCode(final Object self, final Object... args) {
        final char[] buf = new char[args.length];
        int index = 0;
        for (final Object arg : args) {
            buf[index++] = (char)JSType.toUint16(arg);
        }
        return new String(buf);
    }
    
    public static Object fromCharCode(final Object self, final Object value) {
        if (value instanceof Integer) {
            return fromCharCode(self, (int)value);
        }
        return Character.toString((char)JSType.toUint16(value));
    }
    
    public static String fromCharCode(final Object self, final int value) {
        return Character.toString((char)(value & 0xFFFF));
    }
    
    public static Object fromCharCode(final Object self, final int ch1, final int ch2) {
        return Character.toString((char)(ch1 & 0xFFFF)) + Character.toString((char)(ch2 & 0xFFFF));
    }
    
    public static Object fromCharCode(final Object self, final int ch1, final int ch2, final int ch3) {
        return Character.toString((char)(ch1 & 0xFFFF)) + Character.toString((char)(ch2 & 0xFFFF)) + Character.toString((char)(ch3 & 0xFFFF));
    }
    
    public static String fromCharCode(final Object self, final int ch1, final int ch2, final int ch3, final int ch4) {
        return Character.toString((char)(ch1 & 0xFFFF)) + Character.toString((char)(ch2 & 0xFFFF)) + Character.toString((char)(ch3 & 0xFFFF)) + Character.toString((char)(ch4 & 0xFFFF));
    }
    
    public static String fromCharCode(final Object self, final double value) {
        return Character.toString((char)JSType.toUint16(value));
    }
    
    public static String toString(final Object self) {
        return getString(self);
    }
    
    public static String valueOf(final Object self) {
        return getString(self);
    }
    
    public static String charAt(final Object self, final Object pos) {
        return charAtImpl(checkObjectToString(self), JSType.toInteger(pos));
    }
    
    public static String charAt(final Object self, final double pos) {
        return charAt(self, (int)pos);
    }
    
    public static String charAt(final Object self, final int pos) {
        return charAtImpl(checkObjectToString(self), pos);
    }
    
    private static String charAtImpl(final String str, final int pos) {
        return (pos < 0 || pos >= str.length()) ? "" : String.valueOf(str.charAt(pos));
    }
    
    private static int getValidChar(final Object self, final int pos) {
        try {
            return ((CharSequence)self).charAt(pos);
        }
        catch (IndexOutOfBoundsException e) {
            throw new ClassCastException();
        }
    }
    
    public static double charCodeAt(final Object self, final Object pos) {
        final String str = checkObjectToString(self);
        final int idx = JSType.toInteger(pos);
        return (idx < 0 || idx >= str.length()) ? Double.NaN : str.charAt(idx);
    }
    
    public static int charCodeAt(final Object self, final double pos) {
        return charCodeAt(self, (int)pos);
    }
    
    public static int charCodeAt(final Object self, final long pos) {
        return charCodeAt(self, (int)pos);
    }
    
    public static int charCodeAt(final Object self, final int pos) {
        return getValidChar(self, pos);
    }
    
    public static Object concat(final Object self, final Object... args) {
        CharSequence cs = checkObjectToString(self);
        if (args != null) {
            for (final Object obj : args) {
                cs = new ConsString(cs, JSType.toCharSequence(obj));
            }
        }
        return cs;
    }
    
    public static int indexOf(final Object self, final Object search, final Object pos) {
        final String str = checkObjectToString(self);
        return str.indexOf(JSType.toString(search), JSType.toInteger(pos));
    }
    
    public static int indexOf(final Object self, final Object search) {
        return indexOf(self, search, 0);
    }
    
    public static int indexOf(final Object self, final Object search, final double pos) {
        return indexOf(self, search, (int)pos);
    }
    
    public static int indexOf(final Object self, final Object search, final int pos) {
        return checkObjectToString(self).indexOf(JSType.toString(search), pos);
    }
    
    public static int lastIndexOf(final Object self, final Object search, final Object pos) {
        final String str = checkObjectToString(self);
        final String searchStr = JSType.toString(search);
        final int length = str.length();
        int end;
        if (pos == ScriptRuntime.UNDEFINED) {
            end = length;
        }
        else {
            final double numPos = JSType.toNumber(pos);
            end = (Double.isNaN(numPos) ? length : ((int)numPos));
            if (end < 0) {
                end = 0;
            }
            else if (end > length) {
                end = length;
            }
        }
        return str.lastIndexOf(searchStr, end);
    }
    
    public static double localeCompare(final Object self, final Object that) {
        final String str = checkObjectToString(self);
        final Collator collator = Collator.getInstance(Global.getEnv()._locale);
        collator.setStrength(3);
        collator.setDecomposition(1);
        return collator.compare(str, JSType.toString(that));
    }
    
    public static ScriptObject match(final Object self, final Object regexp) {
        final String str = checkObjectToString(self);
        NativeRegExp nativeRegExp;
        if (regexp == ScriptRuntime.UNDEFINED) {
            nativeRegExp = new NativeRegExp("");
        }
        else {
            nativeRegExp = Global.toRegExp(regexp);
        }
        if (!nativeRegExp.getGlobal()) {
            return nativeRegExp.exec(str);
        }
        nativeRegExp.setLastIndex(0);
        int previousLastIndex = 0;
        final List<Object> matches = new ArrayList<Object>();
        Object result;
        while ((result = nativeRegExp.exec(str)) != null) {
            final int thisIndex = nativeRegExp.getLastIndex();
            if (thisIndex == previousLastIndex) {
                nativeRegExp.setLastIndex(thisIndex + 1);
                previousLastIndex = thisIndex + 1;
            }
            else {
                previousLastIndex = thisIndex;
            }
            matches.add(((ScriptObject)result).get(0));
        }
        if (matches.isEmpty()) {
            return null;
        }
        return new NativeArray(matches.toArray());
    }
    
    public static String replace(final Object self, final Object string, final Object replacement) throws Throwable {
        final String str = checkObjectToString(self);
        NativeRegExp nativeRegExp;
        if (string instanceof NativeRegExp) {
            nativeRegExp = (NativeRegExp)string;
        }
        else {
            nativeRegExp = NativeRegExp.flatRegExp(JSType.toString(string));
        }
        if (Bootstrap.isCallable(replacement)) {
            return nativeRegExp.replace(str, "", replacement);
        }
        return nativeRegExp.replace(str, JSType.toString(replacement), null);
    }
    
    public static int search(final Object self, final Object string) {
        final String str = checkObjectToString(self);
        final NativeRegExp nativeRegExp = Global.toRegExp((string == ScriptRuntime.UNDEFINED) ? "" : string);
        return nativeRegExp.search(str);
    }
    
    public static String slice(final Object self, final Object start, final Object end) {
        final String str = checkObjectToString(self);
        if (end == ScriptRuntime.UNDEFINED) {
            return slice(str, JSType.toInteger(start));
        }
        return slice(str, JSType.toInteger(start), JSType.toInteger(end));
    }
    
    public static String slice(final Object self, final int start) {
        final String str = checkObjectToString(self);
        final int from = (start < 0) ? Math.max(str.length() + start, 0) : Math.min(start, str.length());
        return str.substring(from);
    }
    
    public static String slice(final Object self, final double start) {
        return slice(self, (int)start);
    }
    
    public static String slice(final Object self, final int start, final int end) {
        final String str = checkObjectToString(self);
        final int len = str.length();
        final int from = (start < 0) ? Math.max(len + start, 0) : Math.min(start, len);
        final int to = (end < 0) ? Math.max(len + end, 0) : Math.min(end, len);
        return str.substring(Math.min(from, to), to);
    }
    
    public static String slice(final Object self, final double start, final double end) {
        return slice(self, (int)start, (int)end);
    }
    
    public static ScriptObject split(final Object self, final Object separator, final Object limit) {
        final String str = checkObjectToString(self);
        final long lim = (limit == ScriptRuntime.UNDEFINED) ? 4294967295L : JSType.toUint32(limit);
        if (separator == ScriptRuntime.UNDEFINED) {
            return (lim == 0L) ? new NativeArray() : new NativeArray(new Object[] { str });
        }
        if (separator instanceof NativeRegExp) {
            return ((NativeRegExp)separator).split(str, lim);
        }
        return splitString(str, JSType.toString(separator), lim);
    }
    
    private static ScriptObject splitString(final String str, final String separator, final long limit) {
        if (separator.isEmpty()) {
            final int length = (int)Math.min(str.length(), limit);
            final Object[] array = new Object[length];
            for (int i = 0; i < length; ++i) {
                array[i] = String.valueOf(str.charAt(i));
            }
            return new NativeArray(array);
        }
        final List<String> elements = new LinkedList<String>();
        int strLength;
        int sepLength;
        int pos;
        int n;
        int found;
        for (strLength = str.length(), sepLength = separator.length(), pos = 0, n = 0; pos < strLength && n < limit; ++n, pos = found + sepLength) {
            found = str.indexOf(separator, pos);
            if (found == -1) {
                break;
            }
            elements.add(str.substring(pos, found));
        }
        if (pos <= strLength && n < limit) {
            elements.add(str.substring(pos));
        }
        return new NativeArray(elements.toArray());
    }
    
    public static String substr(final Object self, final Object start, final Object length) {
        final String str = JSType.toString(self);
        final int strLength = str.length();
        int intStart = JSType.toInteger(start);
        if (intStart < 0) {
            intStart = Math.max(intStart + strLength, 0);
        }
        final int intLen = Math.min(Math.max((length == ScriptRuntime.UNDEFINED) ? Integer.MAX_VALUE : JSType.toInteger(length), 0), strLength - intStart);
        return (intLen <= 0) ? "" : str.substring(intStart, intStart + intLen);
    }
    
    public static String substring(final Object self, final Object start, final Object end) {
        final String str = checkObjectToString(self);
        if (end == ScriptRuntime.UNDEFINED) {
            return substring(str, JSType.toInteger(start));
        }
        return substring(str, JSType.toInteger(start), JSType.toInteger(end));
    }
    
    public static String substring(final Object self, final int start) {
        final String str = checkObjectToString(self);
        if (start < 0) {
            return str;
        }
        if (start >= str.length()) {
            return "";
        }
        return str.substring(start);
    }
    
    public static String substring(final Object self, final double start) {
        return substring(self, (int)start);
    }
    
    public static String substring(final Object self, final int start, final int end) {
        final String str = checkObjectToString(self);
        final int len = str.length();
        final int validStart = (start < 0) ? 0 : ((start > len) ? len : start);
        final int validEnd = (end < 0) ? 0 : ((end > len) ? len : end);
        if (validStart < validEnd) {
            return str.substring(validStart, validEnd);
        }
        return str.substring(validEnd, validStart);
    }
    
    public static String substring(final Object self, final double start, final double end) {
        return substring(self, (int)start, (int)end);
    }
    
    public static String toLowerCase(final Object self) {
        return checkObjectToString(self).toLowerCase(Locale.ROOT);
    }
    
    public static String toLocaleLowerCase(final Object self) {
        return checkObjectToString(self).toLowerCase(Global.getEnv()._locale);
    }
    
    public static String toUpperCase(final Object self) {
        return checkObjectToString(self).toUpperCase(Locale.ROOT);
    }
    
    public static String toLocaleUpperCase(final Object self) {
        return checkObjectToString(self).toUpperCase(Global.getEnv()._locale);
    }
    
    public static String trim(final Object self) {
        String str;
        int start;
        int end;
        for (str = checkObjectToString(self), start = 0, end = str.length() - 1; start <= end && ScriptRuntime.isJSWhitespace(str.charAt(start)); ++start) {}
        while (end > start && ScriptRuntime.isJSWhitespace(str.charAt(end))) {
            --end;
        }
        return str.substring(start, end + 1);
    }
    
    public static String trimLeft(final Object self) {
        String str;
        int start;
        int end;
        for (str = checkObjectToString(self), start = 0, end = str.length() - 1; start <= end && ScriptRuntime.isJSWhitespace(str.charAt(start)); ++start) {}
        return str.substring(start, end + 1);
    }
    
    public static String trimRight(final Object self) {
        final String str = checkObjectToString(self);
        final int start = 0;
        int end;
        for (end = str.length() - 1; end >= 0 && ScriptRuntime.isJSWhitespace(str.charAt(end)); --end) {}
        return str.substring(0, end + 1);
    }
    
    private static ScriptObject newObj(final CharSequence str) {
        return new NativeString(str);
    }
    
    public static Object constructor(final boolean newObj, final Object self, final Object... args) {
        final CharSequence str = (args.length > 0) ? JSType.toCharSequence(args[0]) : "";
        return newObj ? newObj(str) : str.toString();
    }
    
    public static Object constructor(final boolean newObj, final Object self) {
        return newObj ? newObj("") : "";
    }
    
    public static Object constructor(final boolean newObj, final Object self, final Object arg) {
        final CharSequence str = JSType.toCharSequence(arg);
        return newObj ? newObj(str) : str.toString();
    }
    
    public static Object constructor(final boolean newObj, final Object self, final int arg) {
        final String str = Integer.toString(arg);
        return newObj ? newObj(str) : str;
    }
    
    public static Object constructor(final boolean newObj, final Object self, final long arg) {
        final String str = Long.toString(arg);
        return newObj ? newObj(str) : str;
    }
    
    public static Object constructor(final boolean newObj, final Object self, final double arg) {
        final String str = JSType.toString(arg);
        return newObj ? newObj(str) : str;
    }
    
    public static Object constructor(final boolean newObj, final Object self, final boolean arg) {
        final String str = Boolean.toString(arg);
        return newObj ? newObj(str) : str;
    }
    
    public static GuardedInvocation lookupPrimitive(final LinkRequest request, final Object receiver) {
        return PrimitiveLookup.lookupPrimitive(request, NashornGuards.getStringGuard(), new NativeString((CharSequence)receiver), NativeString.WRAPFILTER, NativeString.PROTOFILTER);
    }
    
    private static NativeString wrapFilter(final Object receiver) {
        return new NativeString((CharSequence)receiver);
    }
    
    private static Object protoFilter(final Object object) {
        return Global.instance().getStringPrototype();
    }
    
    private static CharSequence getCharSequence(final Object self) {
        if (JSType.isString(self)) {
            return (CharSequence)self;
        }
        if (self instanceof NativeString) {
            return ((NativeString)self).getValue();
        }
        if (self != null && self == Global.instance().getStringPrototype()) {
            return "";
        }
        throw ECMAErrors.typeError("not.a.string", ScriptRuntime.safeToString(self));
    }
    
    private static String getString(final Object self) {
        if (self instanceof String) {
            return (String)self;
        }
        if (self instanceof ConsString) {
            return self.toString();
        }
        if (self instanceof NativeString) {
            return ((NativeString)self).getStringValue();
        }
        if (self != null && self == Global.instance().getStringPrototype()) {
            return "";
        }
        throw ECMAErrors.typeError("not.a.string", ScriptRuntime.safeToString(self));
    }
    
    private static String checkObjectToString(final Object self) {
        if (self instanceof String) {
            return (String)self;
        }
        if (self instanceof ConsString) {
            return self.toString();
        }
        Global.checkObjectCoercible(self);
        return JSType.toString(self);
    }
    
    private boolean isValidStringIndex(final int key) {
        return key >= 0 && key < this.value.length();
    }
    
    private static MethodHandle findOwnMH(final String name, final MethodType type) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeString.class, name, type);
    }
    
    @Override
    public SpecializedFunction.LinkLogic getLinkLogic(final Class<? extends SpecializedFunction.LinkLogic> clazz) {
        if (clazz == CharCodeAtLinkLogic.class) {
            return CharCodeAtLinkLogic.INSTANCE;
        }
        return null;
    }
    
    @Override
    public boolean hasPerInstanceAssumptions() {
        return false;
    }
    
    static {
        WRAPFILTER = findOwnMH("wrapFilter", Lookup.MH.type(NativeString.class, Object.class));
        PROTOFILTER = findOwnMH("protoFilter", Lookup.MH.type(Object.class, Object.class));
        $clinit$();
    }
    
    public static void $clinit$() {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeString.$clinit$:()V'.
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:66)
        //     at com.strobel.assembler.metadata.MethodDefinition.tryLoadBody(MethodDefinition.java:729)
        //     at com.strobel.assembler.metadata.MethodDefinition.getBody(MethodDefinition.java:83)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:202)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // Caused by: java.lang.ClassCastException: class com.strobel.assembler.ir.ConstantPool$MethodHandleEntry cannot be cast to class com.strobel.assembler.ir.ConstantPool$ConstantEntry (com.strobel.assembler.ir.ConstantPool$MethodHandleEntry and com.strobel.assembler.ir.ConstantPool$ConstantEntry are in unnamed module of loader 'app')
        //     at com.strobel.assembler.ir.ConstantPool.lookupConstant(ConstantPool.java:120)
        //     at com.strobel.assembler.metadata.ClassFileReader$Scope.lookupConstant(ClassFileReader.java:1319)
        //     at com.strobel.assembler.metadata.MethodReader.readBodyCore(MethodReader.java:293)
        //     at com.strobel.assembler.metadata.MethodReader.readBody(MethodReader.java:62)
        //     ... 16 more
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final class CharCodeAtLinkLogic extends SpecializedFunction.LinkLogic
    {
        private static final CharCodeAtLinkLogic INSTANCE;
        
        @Override
        public boolean canLink(final Object self, final CallSiteDescriptor desc, final LinkRequest request) {
            try {
                final CharSequence cs = (CharSequence)self;
                final int intIndex = JSType.toInteger(request.getArguments()[2]);
                return intIndex >= 0 && intIndex < cs.length();
            }
            catch (ClassCastException | IndexOutOfBoundsException ex) {
                return false;
            }
        }
        
        @Override
        public Class<? extends Throwable> getRelinkException() {
            return ClassCastException.class;
        }
        
        static {
            INSTANCE = new CharCodeAtLinkLogic();
        }
    }
}
