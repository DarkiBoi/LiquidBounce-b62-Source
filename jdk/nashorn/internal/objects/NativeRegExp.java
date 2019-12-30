// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeRegExp extends ScriptObject
{
    public Object lastIndex;
    private RegExp regexp;
    private final Global globalObject;
    private static PropertyMap $nasgenmap$;
    private static final Object REPLACE_VALUE;
    
    private NativeRegExp(final Global global) {
        super(global.getRegExpPrototype(), NativeRegExp.$nasgenmap$);
        this.globalObject = global;
    }
    
    NativeRegExp(final String input, final String flagString, final Global global, final ScriptObject proto) {
        super(proto, NativeRegExp.$nasgenmap$);
        try {
            this.regexp = RegExpFactory.create(input, flagString);
        }
        catch (ParserException e) {
            e.throwAsEcmaException();
            throw new AssertionError();
        }
        this.globalObject = global;
        this.setLastIndex(0);
    }
    
    NativeRegExp(final String input, final String flagString, final Global global) {
        this(input, flagString, global, global.getRegExpPrototype());
    }
    
    NativeRegExp(final String input, final String flagString) {
        this(input, flagString, Global.instance());
    }
    
    NativeRegExp(final String string, final Global global) {
        this(string, "", global);
    }
    
    NativeRegExp(final String string) {
        this(string, Global.instance());
    }
    
    NativeRegExp(final NativeRegExp regExp) {
        this(Global.instance());
        this.lastIndex = regExp.getLastIndexObject();
        this.regexp = regExp.getRegExp();
    }
    
    @Override
    public String getClassName() {
        return "RegExp";
    }
    
    public static NativeRegExp constructor(final boolean isNew, final Object self, final Object... args) {
        if (args.length > 1) {
            return newRegExp(args[0], args[1]);
        }
        if (args.length > 0) {
            return newRegExp(args[0], ScriptRuntime.UNDEFINED);
        }
        return newRegExp(ScriptRuntime.UNDEFINED, ScriptRuntime.UNDEFINED);
    }
    
    public static NativeRegExp constructor(final boolean isNew, final Object self) {
        return new NativeRegExp("", "");
    }
    
    public static NativeRegExp constructor(final boolean isNew, final Object self, final Object pattern) {
        return newRegExp(pattern, ScriptRuntime.UNDEFINED);
    }
    
    public static NativeRegExp constructor(final boolean isNew, final Object self, final Object pattern, final Object flags) {
        return newRegExp(pattern, flags);
    }
    
    public static NativeRegExp newRegExp(final Object regexp, final Object flags) {
        String patternString = "";
        String flagString = "";
        if (regexp != ScriptRuntime.UNDEFINED) {
            if (regexp instanceof NativeRegExp) {
                if (flags != ScriptRuntime.UNDEFINED) {
                    throw ECMAErrors.typeError("regex.cant.supply.flags", new String[0]);
                }
                return (NativeRegExp)regexp;
            }
            else {
                patternString = JSType.toString(regexp);
            }
        }
        if (flags != ScriptRuntime.UNDEFINED) {
            flagString = JSType.toString(flags);
        }
        return new NativeRegExp(patternString, flagString);
    }
    
    static NativeRegExp flatRegExp(final String string) {
        StringBuilder sb = null;
        for (int length = string.length(), i = 0; i < length; ++i) {
            final char c = string.charAt(i);
            switch (c) {
                case '$':
                case '(':
                case ')':
                case '*':
                case '+':
                case '.':
                case '?':
                case '[':
                case '\\':
                case '^':
                case '{':
                case '|': {
                    if (sb == null) {
                        sb = new StringBuilder(length * 2);
                        sb.append(string, 0, i);
                    }
                    sb.append('\\');
                    sb.append(c);
                    break;
                }
                default: {
                    if (sb != null) {
                        sb.append(c);
                        break;
                    }
                    break;
                }
            }
        }
        return new NativeRegExp((sb == null) ? string : sb.toString(), "");
    }
    
    private String getFlagString() {
        final StringBuilder sb = new StringBuilder(3);
        if (this.regexp.isGlobal()) {
            sb.append('g');
        }
        if (this.regexp.isIgnoreCase()) {
            sb.append('i');
        }
        if (this.regexp.isMultiline()) {
            sb.append('m');
        }
        return sb.toString();
    }
    
    @Override
    public String safeToString() {
        return "[RegExp " + this.toString() + "]";
    }
    
    @Override
    public String toString() {
        return "/" + this.regexp.getSource() + "/" + this.getFlagString();
    }
    
    public static ScriptObject compile(final Object self, final Object pattern, final Object flags) {
        final NativeRegExp regExp = checkRegExp(self);
        final NativeRegExp compiled = newRegExp(pattern, flags);
        regExp.setRegExp(compiled.getRegExp());
        return regExp;
    }
    
    public static ScriptObject exec(final Object self, final Object string) {
        return checkRegExp(self).exec(JSType.toString(string));
    }
    
    public static boolean test(final Object self, final Object string) {
        return checkRegExp(self).test(JSType.toString(string));
    }
    
    public static String toString(final Object self) {
        return checkRegExp(self).toString();
    }
    
    public static Object source(final Object self) {
        return checkRegExp(self).getRegExp().getSource();
    }
    
    public static Object global(final Object self) {
        return checkRegExp(self).getRegExp().isGlobal();
    }
    
    public static Object ignoreCase(final Object self) {
        return checkRegExp(self).getRegExp().isIgnoreCase();
    }
    
    public static Object multiline(final Object self) {
        return checkRegExp(self).getRegExp().isMultiline();
    }
    
    public static Object getLastInput(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getInput();
    }
    
    public static Object getLastMultiline(final Object self) {
        return false;
    }
    
    public static Object getLastMatch(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(0);
    }
    
    public static Object getLastParen(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getLastParen();
    }
    
    public static Object getLeftContext(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getInput().substring(0, match.getIndex());
    }
    
    public static Object getRightContext(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getInput().substring(match.getIndex() + match.length());
    }
    
    public static Object getGroup1(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(1);
    }
    
    public static Object getGroup2(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(2);
    }
    
    public static Object getGroup3(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(3);
    }
    
    public static Object getGroup4(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(4);
    }
    
    public static Object getGroup5(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(5);
    }
    
    public static Object getGroup6(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(6);
    }
    
    public static Object getGroup7(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(7);
    }
    
    public static Object getGroup8(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(8);
    }
    
    public static Object getGroup9(final Object self) {
        final RegExpResult match = Global.instance().getLastRegExpResult();
        return (match == null) ? "" : match.getGroup(9);
    }
    
    private RegExpResult execInner(final String string) {
        final boolean isGlobal = this.regexp.isGlobal();
        int start = this.getLastIndex();
        if (!isGlobal) {
            start = 0;
        }
        if (start < 0 || start > string.length()) {
            if (isGlobal) {
                this.setLastIndex(0);
            }
            return null;
        }
        final RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null || !matcher.search(start)) {
            if (isGlobal) {
                this.setLastIndex(0);
            }
            return null;
        }
        if (isGlobal) {
            this.setLastIndex(matcher.end());
        }
        final RegExpResult match = new RegExpResult(string, matcher.start(), this.groups(matcher));
        this.globalObject.setLastRegExpResult(match);
        return match;
    }
    
    private RegExpResult execSplit(final String string, final int start) {
        if (start < 0 || start > string.length()) {
            return null;
        }
        final RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null || !matcher.search(start)) {
            return null;
        }
        final RegExpResult match = new RegExpResult(string, matcher.start(), this.groups(matcher));
        this.globalObject.setLastRegExpResult(match);
        return match;
    }
    
    private Object[] groups(final RegExpMatcher matcher) {
        final int groupCount = matcher.groupCount();
        final Object[] groups = new Object[groupCount + 1];
        final BitVector groupsInNegativeLookahead = this.regexp.getGroupsInNegativeLookahead();
        int i = 0;
        int lastGroupStart = matcher.start();
        while (i <= groupCount) {
            final int groupStart = matcher.start(i);
            if (lastGroupStart > groupStart || (groupsInNegativeLookahead != null && groupsInNegativeLookahead.isSet(i))) {
                groups[i] = ScriptRuntime.UNDEFINED;
            }
            else {
                final String group = matcher.group(i);
                groups[i] = ((group == null) ? ScriptRuntime.UNDEFINED : group);
                lastGroupStart = groupStart;
            }
            ++i;
        }
        return groups;
    }
    
    public NativeRegExpExecResult exec(final String string) {
        final RegExpResult match = this.execInner(string);
        if (match == null) {
            return null;
        }
        return new NativeRegExpExecResult(match, this.globalObject);
    }
    
    public boolean test(final String string) {
        return this.execInner(string) != null;
    }
    
    String replace(final String string, final String replacement, final Object function) throws Throwable {
        final RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null) {
            return string;
        }
        if (!this.regexp.isGlobal()) {
            if (!matcher.search(0)) {
                return string;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(string, 0, matcher.start());
            if (function != null) {
                final Object self = Bootstrap.isStrictCallable(function) ? ScriptRuntime.UNDEFINED : Global.instance();
                sb.append(this.callReplaceValue(getReplaceValueInvoker(), function, self, matcher, string));
            }
            else {
                this.appendReplacement(matcher, string, replacement, sb);
            }
            sb.append(string, matcher.end(), string.length());
            return sb.toString();
        }
        else {
            this.setLastIndex(0);
            if (!matcher.search(0)) {
                return string;
            }
            int thisIndex = 0;
            int previousLastIndex = 0;
            final StringBuilder sb2 = new StringBuilder();
            final MethodHandle invoker = (function == null) ? null : getReplaceValueInvoker();
            final Object self2 = (function == null || Bootstrap.isStrictCallable(function)) ? ScriptRuntime.UNDEFINED : Global.instance();
            do {
                sb2.append(string, thisIndex, matcher.start());
                if (function != null) {
                    sb2.append(this.callReplaceValue(invoker, function, self2, matcher, string));
                }
                else {
                    this.appendReplacement(matcher, string, replacement, sb2);
                }
                thisIndex = matcher.end();
                if (thisIndex == string.length() && matcher.start() == matcher.end()) {
                    break;
                }
                if (thisIndex == previousLastIndex) {
                    this.setLastIndex(thisIndex + 1);
                    previousLastIndex = thisIndex + 1;
                }
                else {
                    previousLastIndex = thisIndex;
                }
            } while (previousLastIndex <= string.length() && matcher.search(previousLastIndex));
            sb2.append(string, thisIndex, string.length());
            return sb2.toString();
        }
    }
    
    private void appendReplacement(final RegExpMatcher matcher, final String text, final String replacement, final StringBuilder sb) {
        int cursor = 0;
        Object[] groups = null;
        while (cursor < replacement.length()) {
            char nextChar = replacement.charAt(cursor);
            if (nextChar == '$') {
                if (++cursor == replacement.length()) {
                    sb.append('$');
                    break;
                }
                nextChar = replacement.charAt(cursor);
                final int firstDigit = nextChar - '0';
                if (firstDigit >= 0 && firstDigit <= 9 && firstDigit <= matcher.groupCount()) {
                    int refNum = firstDigit;
                    if (++cursor < replacement.length() && firstDigit < matcher.groupCount()) {
                        final int secondDigit = replacement.charAt(cursor) - '0';
                        if (secondDigit >= 0 && secondDigit <= 9) {
                            final int newRefNum = firstDigit * 10 + secondDigit;
                            if (newRefNum <= matcher.groupCount() && newRefNum > 0) {
                                refNum = newRefNum;
                                ++cursor;
                            }
                        }
                    }
                    if (refNum > 0) {
                        if (groups == null) {
                            groups = this.groups(matcher);
                        }
                        if (groups[refNum] == ScriptRuntime.UNDEFINED) {
                            continue;
                        }
                        sb.append((String)groups[refNum]);
                    }
                    else {
                        assert refNum == 0;
                        sb.append("$0");
                    }
                }
                else if (nextChar == '$') {
                    sb.append('$');
                    ++cursor;
                }
                else if (nextChar == '&') {
                    sb.append(matcher.group());
                    ++cursor;
                }
                else if (nextChar == '`') {
                    sb.append(text, 0, matcher.start());
                    ++cursor;
                }
                else if (nextChar == '\'') {
                    sb.append(text, matcher.end(), text.length());
                    ++cursor;
                }
                else {
                    sb.append('$');
                }
            }
            else {
                sb.append(nextChar);
                ++cursor;
            }
        }
    }
    
    private static final MethodHandle getReplaceValueInvoker() {
        return Global.instance().getDynamicInvoker(NativeRegExp.REPLACE_VALUE, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", String.class, Object.class, Object.class, Object[].class);
            }
        });
    }
    
    private String callReplaceValue(final MethodHandle invoker, final Object function, final Object self, final RegExpMatcher matcher, final String string) throws Throwable {
        final Object[] groups = this.groups(matcher);
        final Object[] args = Arrays.copyOf(groups, groups.length + 2);
        args[groups.length] = matcher.start();
        args[groups.length + 1] = string;
        return invoker.invokeExact(function, self, args);
    }
    
    NativeArray split(final String string, final long limit) {
        if (limit == 0L) {
            return new NativeArray();
        }
        final List<Object> matches = new ArrayList<Object>();
        final int inputLength = string.length();
        int splitLastLength = -1;
        int splitLastIndex = 0;
        int splitLastLastIndex = 0;
        RegExpResult match;
        while ((match = this.execSplit(string, splitLastIndex)) != null) {
            splitLastIndex = match.getIndex() + match.length();
            if (splitLastIndex > splitLastLastIndex) {
                matches.add(string.substring(splitLastLastIndex, match.getIndex()));
                final Object[] groups = match.getGroups();
                if (groups.length > 1 && match.getIndex() < inputLength) {
                    for (int index = 1; index < groups.length && matches.size() < limit; ++index) {
                        matches.add(groups[index]);
                    }
                }
                splitLastLength = match.length();
                if (matches.size() >= limit) {
                    break;
                }
            }
            if (splitLastIndex == splitLastLastIndex) {
                ++splitLastIndex;
            }
            else {
                splitLastLastIndex = splitLastIndex;
            }
        }
        if (matches.size() < limit) {
            if (splitLastLastIndex == string.length()) {
                if (splitLastLength > 0 || this.execSplit("", 0) == null) {
                    matches.add("");
                }
            }
            else {
                matches.add(string.substring(splitLastLastIndex, inputLength));
            }
        }
        return new NativeArray(matches.toArray());
    }
    
    int search(final String string) {
        final RegExpResult match = this.execInner(string);
        if (match == null) {
            return -1;
        }
        return match.getIndex();
    }
    
    public int getLastIndex() {
        return JSType.toInteger(this.lastIndex);
    }
    
    public Object getLastIndexObject() {
        return this.lastIndex;
    }
    
    public void setLastIndex(final int lastIndex) {
        this.lastIndex = JSType.toObject(lastIndex);
    }
    
    private static NativeRegExp checkRegExp(final Object self) {
        if (self instanceof NativeRegExp) {
            return (NativeRegExp)self;
        }
        if (self != null && self == Global.instance().getRegExpPrototype()) {
            return Global.instance().getDefaultRegExp();
        }
        throw ECMAErrors.typeError("not.a.regexp", ScriptRuntime.safeToString(self));
    }
    
    boolean getGlobal() {
        return this.regexp.isGlobal();
    }
    
    private RegExp getRegExp() {
        return this.regexp;
    }
    
    private void setRegExp(final RegExp regexp) {
        this.regexp = regexp;
    }
    
    static {
        REPLACE_VALUE = new Object();
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
        // com.strobel.assembler.metadata.MethodBodyParseException: An error occurred while parsing the bytecode of method 'jdk/nashorn/internal/objects/NativeRegExp.$clinit$:()V'.
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
    
    public Object G$lastIndex() {
        return this.lastIndex;
    }
    
    public void S$lastIndex(final Object lastIndex) {
        this.lastIndex = lastIndex;
    }
}
