// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

import java.util.Locale;
import java.util.HashMap;

public class DateParser
{
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;
    public static final int MILLISECOND = 6;
    public static final int TIMEZONE = 7;
    private final String string;
    private final int length;
    private final Integer[] fields;
    private int pos;
    private Token token;
    private int tokenLength;
    private Name nameValue;
    private int numValue;
    private int currentField;
    private int yearSign;
    private boolean namedMonth;
    private static final HashMap<String, Name> names;
    
    public DateParser(final String string) {
        this.pos = 0;
        this.currentField = 0;
        this.yearSign = 0;
        this.namedMonth = false;
        this.string = string;
        this.length = string.length();
        this.fields = new Integer[8];
    }
    
    public boolean parse() {
        return this.parseEcmaDate() || this.parseLegacyDate();
    }
    
    public boolean parseEcmaDate() {
        if (this.token == null) {
            this.token = this.next();
        }
        while (this.token != Token.END) {
            Label_0315: {
                switch (this.token) {
                    case NUMBER: {
                        if (this.currentField == 0 && this.yearSign != 0) {
                            if (this.tokenLength != 6) {
                                return false;
                            }
                            this.numValue *= this.yearSign;
                        }
                        else if (!this.checkEcmaField(this.currentField, this.numValue)) {
                            return false;
                        }
                        if (!this.skipEcmaDelimiter()) {
                            return false;
                        }
                        if (this.currentField < 7) {
                            this.set(this.currentField++, this.numValue);
                            break;
                        }
                        break;
                    }
                    case NAME: {
                        if (this.nameValue == null) {
                            return false;
                        }
                        switch (this.nameValue.type) {
                            case 3: {
                                if (this.currentField == 0 || this.currentField > 3) {
                                    return false;
                                }
                                this.currentField = 3;
                                break Label_0315;
                            }
                            case 2: {
                                if (!this.nameValue.key.equals("z") || !this.setTimezone(this.nameValue.value, false)) {
                                    return false;
                                }
                                break Label_0315;
                            }
                            default: {
                                return false;
                            }
                        }
                        break;
                    }
                    case SIGN: {
                        if (this.peek() == -1) {
                            return false;
                        }
                        if (this.currentField == 0) {
                            this.yearSign = this.numValue;
                            break;
                        }
                        if (this.currentField < 5 || !this.setTimezone(this.readTimeZoneOffset(), true)) {
                            return false;
                        }
                        break;
                    }
                    default: {
                        return false;
                    }
                }
            }
            this.token = this.next();
        }
        return this.patchResult(true);
    }
    
    public boolean parseLegacyDate() {
        if (this.yearSign != 0 || this.currentField > 2) {
            return false;
        }
        if (this.token == null) {
            this.token = this.next();
        }
        while (this.token != Token.END) {
            switch (this.token) {
                case NUMBER: {
                    if (this.skipDelimiter(':')) {
                        if (!this.setTimeField(this.numValue)) {
                            return false;
                        }
                        do {
                            this.token = this.next();
                            if (this.token != Token.NUMBER || !this.setTimeField(this.numValue)) {
                                return false;
                            }
                        } while (this.skipDelimiter(this.isSet(5) ? '.' : ':'));
                        break;
                    }
                    else {
                        if (!this.setDateField(this.numValue)) {
                            return false;
                        }
                        this.skipDelimiter('-');
                        break;
                    }
                    break;
                }
                case NAME: {
                    if (this.nameValue == null) {
                        return false;
                    }
                    switch (this.nameValue.type) {
                        case 1: {
                            if (!this.setAmPm(this.nameValue.value)) {
                                return false;
                            }
                            break;
                        }
                        case 0: {
                            if (!this.setMonth(this.nameValue.value)) {
                                return false;
                            }
                            break;
                        }
                        case 2: {
                            if (!this.setTimezone(this.nameValue.value, false)) {
                                return false;
                            }
                            break;
                        }
                        case 3: {
                            return false;
                        }
                    }
                    if (this.nameValue.type != 2) {
                        this.skipDelimiter('-');
                        break;
                    }
                    break;
                }
                case SIGN: {
                    if (this.peek() == -1) {
                        return false;
                    }
                    if (!this.setTimezone(this.readTimeZoneOffset(), true)) {
                        return false;
                    }
                    break;
                }
                case PARENTHESIS: {
                    if (!this.skipParentheses()) {
                        return false;
                    }
                    break;
                }
                case SEPARATOR: {
                    break;
                }
                default: {
                    return false;
                }
            }
            this.token = this.next();
        }
        return this.patchResult(false);
    }
    
    public Integer[] getDateFields() {
        return this.fields;
    }
    
    private boolean isSet(final int field) {
        return this.fields[field] != null;
    }
    
    private Integer get(final int field) {
        return this.fields[field];
    }
    
    private void set(final int field, final int value) {
        this.fields[field] = value;
    }
    
    private int peek() {
        return (this.pos < this.length) ? this.string.charAt(this.pos) : -1;
    }
    
    private boolean skipNumberDelimiter(final char c) {
        if (this.pos < this.length - 1 && this.string.charAt(this.pos) == c && Character.getType(this.string.charAt(this.pos + 1)) == 9) {
            this.token = null;
            ++this.pos;
            return true;
        }
        return false;
    }
    
    private boolean skipDelimiter(final char c) {
        if (this.pos < this.length && this.string.charAt(this.pos) == c) {
            this.token = null;
            ++this.pos;
            return true;
        }
        return false;
    }
    
    private Token next() {
        if (this.pos >= this.length) {
            this.tokenLength = 0;
            return Token.END;
        }
        final char c = this.string.charAt(this.pos);
        if (c > '\u0080') {
            this.tokenLength = 1;
            ++this.pos;
            return Token.UNKNOWN;
        }
        final int type = Character.getType(c);
        switch (type) {
            case 9: {
                this.numValue = this.readNumber(6);
                return Token.NUMBER;
            }
            case 12:
            case 24: {
                this.tokenLength = 1;
                ++this.pos;
                return Token.SEPARATOR;
            }
            case 1:
            case 2: {
                this.nameValue = this.readName();
                return Token.NAME;
            }
            default: {
                this.tokenLength = 1;
                ++this.pos;
                switch (c) {
                    case '(': {
                        return Token.PARENTHESIS;
                    }
                    case '+':
                    case '-': {
                        this.numValue = ((c == '-') ? -1 : 1);
                        return Token.SIGN;
                    }
                    default: {
                        return Token.UNKNOWN;
                    }
                }
                break;
            }
        }
    }
    
    private static boolean checkLegacyField(final int field, final int value) {
        switch (field) {
            case 3: {
                return isHour(value);
            }
            case 4:
            case 5: {
                return isMinuteOrSecond(value);
            }
            case 6: {
                return isMillisecond(value);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean checkEcmaField(final int field, final int value) {
        switch (field) {
            case 0: {
                return this.tokenLength == 4;
            }
            case 1: {
                return this.tokenLength == 2 && isMonth(value);
            }
            case 2: {
                return this.tokenLength == 2 && isDay(value);
            }
            case 3: {
                return this.tokenLength == 2 && isHour(value);
            }
            case 4:
            case 5: {
                return this.tokenLength == 2 && isMinuteOrSecond(value);
            }
            case 6: {
                return this.tokenLength < 4 && isMillisecond(value);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean skipEcmaDelimiter() {
        switch (this.currentField) {
            case 0:
            case 1: {
                return this.skipNumberDelimiter('-') || this.peek() == 84 || this.peek() == -1;
            }
            case 2: {
                return this.peek() == 84 || this.peek() == -1;
            }
            case 3:
            case 4: {
                return this.skipNumberDelimiter(':') || this.endOfTime();
            }
            case 5: {
                return this.skipNumberDelimiter('.') || this.endOfTime();
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean endOfTime() {
        final int c = this.peek();
        return c == -1 || c == 90 || c == 45 || c == 43 || c == 32;
    }
    
    private static boolean isAsciiLetter(final char ch) {
        return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z');
    }
    
    private static boolean isAsciiDigit(final char ch) {
        return '0' <= ch && ch <= '9';
    }
    
    private int readNumber(final int maxDigits) {
        final int start = this.pos;
        int n = 0;
        final int max = Math.min(this.length, this.pos + maxDigits);
        while (this.pos < max && isAsciiDigit(this.string.charAt(this.pos))) {
            n = n * 10 + this.string.charAt(this.pos++) - 48;
        }
        this.tokenLength = this.pos - start;
        return n;
    }
    
    private Name readName() {
        final int start = this.pos;
        final int limit = Math.min(this.pos + 3, this.length);
        while (this.pos < limit && isAsciiLetter(this.string.charAt(this.pos))) {
            ++this.pos;
        }
        final String key = this.string.substring(start, this.pos).toLowerCase(Locale.ENGLISH);
        final Name name = DateParser.names.get(key);
        while (this.pos < this.length && isAsciiLetter(this.string.charAt(this.pos))) {
            ++this.pos;
        }
        this.tokenLength = this.pos - start;
        if (name != null && name.matches(this.string, start, this.tokenLength)) {
            return name;
        }
        return null;
    }
    
    private int readTimeZoneOffset() {
        final int sign = (this.string.charAt(this.pos - 1) == '+') ? 1 : -1;
        int offset = this.readNumber(2);
        this.skipDelimiter(':');
        offset = offset * 60 + this.readNumber(2);
        return sign * offset;
    }
    
    private boolean skipParentheses() {
        int parenCount = 1;
        while (this.pos < this.length && parenCount != 0) {
            final char c = this.string.charAt(this.pos++);
            if (c == '(') {
                ++parenCount;
            }
            else {
                if (c != ')') {
                    continue;
                }
                --parenCount;
            }
        }
        return true;
    }
    
    private static int getDefaultValue(final int field) {
        switch (field) {
            case 1:
            case 2: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static boolean isDay(final int n) {
        return 1 <= n && n <= 31;
    }
    
    private static boolean isMonth(final int n) {
        return 1 <= n && n <= 12;
    }
    
    private static boolean isHour(final int n) {
        return 0 <= n && n <= 24;
    }
    
    private static boolean isMinuteOrSecond(final int n) {
        return 0 <= n && n < 60;
    }
    
    private static boolean isMillisecond(final int n) {
        return 0 <= n && n < 1000;
    }
    
    private boolean setMonth(final int m) {
        if (!this.isSet(1)) {
            this.namedMonth = true;
            this.set(1, m);
            return true;
        }
        return false;
    }
    
    private boolean setDateField(final int n) {
        for (int field = 0; field != 3; ++field) {
            if (!this.isSet(field)) {
                this.set(field, n);
                return true;
            }
        }
        return false;
    }
    
    private boolean setTimeField(final int n) {
        int field = 3;
        while (field != 7) {
            if (!this.isSet(field)) {
                if (checkLegacyField(field, n)) {
                    this.set(field, n);
                    return true;
                }
                return false;
            }
            else {
                ++field;
            }
        }
        return false;
    }
    
    private boolean setTimezone(final int offset, final boolean asNumericOffset) {
        if (!this.isSet(7) || (asNumericOffset && this.get(7) == 0)) {
            this.set(7, offset);
            return true;
        }
        return false;
    }
    
    private boolean setAmPm(final int offset) {
        if (!this.isSet(3)) {
            return false;
        }
        final int hour = this.get(3);
        if (hour >= 0 && hour <= 12) {
            this.set(3, hour + offset);
        }
        return true;
    }
    
    private boolean patchResult(final boolean strict) {
        if (!this.isSet(0) && !this.isSet(3)) {
            return false;
        }
        if (this.isSet(3) && !this.isSet(4)) {
            return false;
        }
        for (int field = 0; field <= 7; ++field) {
            if (this.get(field) == null) {
                if (field != 7 || strict) {
                    final int value = getDefaultValue(field);
                    this.set(field, value);
                }
            }
        }
        if (!strict) {
            if (isDay(this.get(0))) {
                final int d = this.get(0);
                this.set(0, this.get(2));
                if (this.namedMonth) {
                    this.set(2, d);
                }
                else {
                    final int d2 = this.get(1);
                    this.set(1, d);
                    this.set(2, d2);
                }
            }
            if (!isMonth(this.get(1)) || !isDay(this.get(2))) {
                return false;
            }
            final int year = this.get(0);
            if (year >= 0 && year < 100) {
                this.set(0, (year >= 50) ? (1900 + year) : (2000 + year));
            }
        }
        else if (this.get(3) == 24 && (this.get(4) != 0 || this.get(5) != 0 || this.get(6) != 0)) {
            return false;
        }
        this.set(1, this.get(1) - 1);
        return true;
    }
    
    private static void addName(final String str, final int type, final int value) {
        final Name name = new Name(str, type, value);
        DateParser.names.put(name.key, name);
    }
    
    static {
        names = new HashMap<String, Name>();
        addName("monday", -1, 0);
        addName("tuesday", -1, 0);
        addName("wednesday", -1, 0);
        addName("thursday", -1, 0);
        addName("friday", -1, 0);
        addName("saturday", -1, 0);
        addName("sunday", -1, 0);
        addName("january", 0, 1);
        addName("february", 0, 2);
        addName("march", 0, 3);
        addName("april", 0, 4);
        addName("may", 0, 5);
        addName("june", 0, 6);
        addName("july", 0, 7);
        addName("august", 0, 8);
        addName("september", 0, 9);
        addName("october", 0, 10);
        addName("november", 0, 11);
        addName("december", 0, 12);
        addName("am", 1, 0);
        addName("pm", 1, 12);
        addName("z", 2, 0);
        addName("gmt", 2, 0);
        addName("ut", 2, 0);
        addName("utc", 2, 0);
        addName("est", 2, -300);
        addName("edt", 2, -240);
        addName("cst", 2, -360);
        addName("cdt", 2, -300);
        addName("mst", 2, -420);
        addName("mdt", 2, -360);
        addName("pst", 2, -480);
        addName("pdt", 2, -420);
        addName("t", 3, 0);
    }
    
    private enum Token
    {
        UNKNOWN, 
        NUMBER, 
        SEPARATOR, 
        PARENTHESIS, 
        NAME, 
        SIGN, 
        END;
    }
    
    private static class Name
    {
        final String name;
        final String key;
        final int value;
        final int type;
        static final int DAY_OF_WEEK = -1;
        static final int MONTH_NAME = 0;
        static final int AM_PM = 1;
        static final int TIMEZONE_ID = 2;
        static final int TIME_SEPARATOR = 3;
        
        Name(final String name, final int type, final int value) {
            assert name != null;
            assert name.equals(name.toLowerCase(Locale.ENGLISH));
            this.name = name;
            this.key = name.substring(0, Math.min(3, name.length()));
            this.type = type;
            this.value = value;
        }
        
        public boolean matches(final String str, final int offset, final int len) {
            return this.name.regionMatches(true, 0, str, offset, len);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
