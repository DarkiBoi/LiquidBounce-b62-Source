// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.SpillProperty;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.List;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.scripts.JO;
import jdk.nashorn.internal.scripts.JD;
import jdk.nashorn.internal.objects.Global;

public class JSONParser
{
    private final String source;
    private final Global global;
    private final boolean dualFields;
    final int length;
    int pos;
    private static final int EOF = -1;
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NULL = "null";
    private static final int STATE_EMPTY = 0;
    private static final int STATE_ELEMENT_PARSED = 1;
    private static final int STATE_COMMA_PARSED = 2;
    
    public JSONParser(final String source, final Global global, final boolean dualFields) {
        this.pos = 0;
        this.source = source;
        this.global = global;
        this.length = source.length();
        this.dualFields = dualFields;
    }
    
    public static String quote(final String value) {
        final StringBuilder product = new StringBuilder();
        product.append("\"");
        for (final char ch : value.toCharArray()) {
            switch (ch) {
                case '\\': {
                    product.append("\\\\");
                    break;
                }
                case '\"': {
                    product.append("\\\"");
                    break;
                }
                case '\b': {
                    product.append("\\b");
                    break;
                }
                case '\f': {
                    product.append("\\f");
                    break;
                }
                case '\n': {
                    product.append("\\n");
                    break;
                }
                case '\r': {
                    product.append("\\r");
                    break;
                }
                case '\t': {
                    product.append("\\t");
                    break;
                }
                default: {
                    if (ch < ' ') {
                        product.append(Lexer.unicodeEscape(ch));
                        break;
                    }
                    product.append(ch);
                    break;
                }
            }
        }
        product.append("\"");
        return product.toString();
    }
    
    public Object parse() {
        final Object value = this.parseLiteral();
        this.skipWhiteSpace();
        if (this.pos < this.length) {
            throw this.expectedError(this.pos, "eof", toString(this.peek()));
        }
        return value;
    }
    
    private Object parseLiteral() {
        this.skipWhiteSpace();
        final int c = this.peek();
        if (c == -1) {
            throw this.expectedError(this.pos, "json literal", "eof");
        }
        switch (c) {
            case 123: {
                return this.parseObject();
            }
            case 91: {
                return this.parseArray();
            }
            case 34: {
                return this.parseString();
            }
            case 102: {
                return this.parseKeyword("false", Boolean.FALSE);
            }
            case 116: {
                return this.parseKeyword("true", Boolean.TRUE);
            }
            case 110: {
                return this.parseKeyword("null", null);
            }
            default: {
                if (this.isDigit(c) || c == 45) {
                    return this.parseNumber();
                }
                if (c == 46) {
                    throw this.numberError(this.pos);
                }
                throw this.expectedError(this.pos, "json literal", toString(c));
            }
        }
    }
    
    private Object parseObject() {
        PropertyMap propertyMap = this.dualFields ? JD.getInitialMap() : JO.getInitialMap();
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        final ArrayList<Object> values = new ArrayList<Object>();
        int state = 0;
        assert this.peek() == 123;
        ++this.pos;
        while (this.pos < this.length) {
            this.skipWhiteSpace();
            final int c = this.peek();
            switch (c) {
                case 34: {
                    if (state == 1) {
                        throw this.expectedError(this.pos - 1, ", or }", toString(c));
                    }
                    final String id = this.parseString();
                    this.expectColon();
                    final Object value = this.parseLiteral();
                    final int index = ArrayIndex.getArrayIndex(id);
                    if (ArrayIndex.isValidArrayIndex(index)) {
                        arrayData = addArrayElement(arrayData, index, value);
                    }
                    else {
                        propertyMap = this.addObjectProperty(propertyMap, values, id, value);
                    }
                    state = 1;
                    continue;
                }
                case 44: {
                    if (state != 1) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    ++this.pos;
                    continue;
                }
                case 125: {
                    if (state == 2) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    ++this.pos;
                    return this.createObject(propertyMap, values, arrayData);
                }
                default: {
                    throw this.expectedError(this.pos, ", or }", toString(c));
                }
            }
        }
        throw this.expectedError(this.pos, ", or }", "eof");
    }
    
    private static ArrayData addArrayElement(final ArrayData arrayData, final int index, final Object value) {
        final long oldLength = arrayData.length();
        final long longIndex = ArrayIndex.toLongIndex(index);
        ArrayData newArrayData = arrayData;
        if (longIndex >= oldLength) {
            newArrayData = newArrayData.ensure(longIndex);
            if (longIndex > oldLength) {
                newArrayData = newArrayData.delete(oldLength, longIndex - 1L);
            }
        }
        return newArrayData.set(index, value, false);
    }
    
    private PropertyMap addObjectProperty(final PropertyMap propertyMap, final List<Object> values, final String id, final Object value) {
        final Property oldProperty = propertyMap.findProperty(id);
        Class<?> type;
        int flags;
        if (this.dualFields) {
            type = getType(value);
            flags = 2048;
        }
        else {
            type = Object.class;
            flags = 0;
        }
        PropertyMap newMap;
        if (oldProperty != null) {
            values.set(oldProperty.getSlot(), value);
            newMap = propertyMap.replaceProperty(oldProperty, new SpillProperty(id, flags, oldProperty.getSlot(), type));
        }
        else {
            values.add(value);
            newMap = propertyMap.addProperty(new SpillProperty(id, flags, propertyMap.size(), type));
        }
        return newMap;
    }
    
    private Object createObject(final PropertyMap propertyMap, final List<Object> values, final ArrayData arrayData) {
        final long[] primitiveSpill = (long[])(this.dualFields ? new long[values.size()] : null);
        final Object[] objectSpill = new Object[values.size()];
        for (final Property property : propertyMap.getProperties()) {
            if (!this.dualFields || property.getType() == Object.class) {
                objectSpill[property.getSlot()] = values.get(property.getSlot());
            }
            else {
                primitiveSpill[property.getSlot()] = ObjectClassGenerator.pack(values.get(property.getSlot()));
            }
        }
        final ScriptObject object = this.dualFields ? new JD(propertyMap, primitiveSpill, objectSpill) : new JO(propertyMap, null, objectSpill);
        object.setInitialProto(this.global.getObjectPrototype());
        object.setArray(arrayData);
        return object;
    }
    
    private static Class<?> getType(final Object value) {
        if (value instanceof Integer) {
            return Integer.TYPE;
        }
        if (value instanceof Double) {
            return Double.TYPE;
        }
        return Object.class;
    }
    
    private void expectColon() {
        this.skipWhiteSpace();
        final int n = this.next();
        if (n != 58) {
            throw this.expectedError(this.pos - 1, ":", toString(n));
        }
    }
    
    private Object parseArray() {
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        int state = 0;
        assert this.peek() == 91;
        ++this.pos;
        while (this.pos < this.length) {
            this.skipWhiteSpace();
            final int c = this.peek();
            switch (c) {
                case 44: {
                    if (state != 1) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    ++this.pos;
                    continue;
                }
                case 93: {
                    if (state == 2) {
                        throw this.error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    ++this.pos;
                    return this.global.wrapAsObject(arrayData);
                }
                default: {
                    if (state == 1) {
                        throw this.expectedError(this.pos, ", or ]", toString(c));
                    }
                    final long index = arrayData.length();
                    arrayData = arrayData.ensure(index).set((int)index, this.parseLiteral(), true);
                    state = 1;
                    continue;
                }
            }
        }
        throw this.expectedError(this.pos, ", or ]", "eof");
    }
    
    private String parseString() {
        int start = ++this.pos;
        StringBuilder sb = null;
        while (this.pos < this.length) {
            final int c = this.next();
            if (c <= 31) {
                throw this.syntaxError(this.pos, "String contains control character");
            }
            if (c == 92) {
                if (sb == null) {
                    sb = new StringBuilder(this.pos - start + 16);
                }
                sb.append(this.source, start, this.pos - 1);
                sb.append(this.parseEscapeSequence());
                start = this.pos;
            }
            else {
                if (c != 34) {
                    continue;
                }
                if (sb != null) {
                    sb.append(this.source, start, this.pos - 1);
                    return sb.toString();
                }
                return this.source.substring(start, this.pos - 1);
            }
        }
        throw this.error(Lexer.message("missing.close.quote", new String[0]), this.pos, this.length);
    }
    
    private char parseEscapeSequence() {
        final int c = this.next();
        switch (c) {
            case 34: {
                return '\"';
            }
            case 92: {
                return '\\';
            }
            case 47: {
                return '/';
            }
            case 98: {
                return '\b';
            }
            case 102: {
                return '\f';
            }
            case 110: {
                return '\n';
            }
            case 114: {
                return '\r';
            }
            case 116: {
                return '\t';
            }
            case 117: {
                return this.parseUnicodeEscape();
            }
            default: {
                throw this.error(Lexer.message("invalid.escape.char", new String[0]), this.pos - 1, this.length);
            }
        }
    }
    
    private char parseUnicodeEscape() {
        return (char)(this.parseHexDigit() << 12 | this.parseHexDigit() << 8 | this.parseHexDigit() << 4 | this.parseHexDigit());
    }
    
    private int parseHexDigit() {
        final int c = this.next();
        if (c >= 48 && c <= 57) {
            return c - 48;
        }
        if (c >= 65 && c <= 70) {
            return c + 10 - 65;
        }
        if (c >= 97 && c <= 102) {
            return c + 10 - 97;
        }
        throw this.error(Lexer.message("invalid.hex", new String[0]), this.pos - 1, this.length);
    }
    
    private boolean isDigit(final int c) {
        return c >= 48 && c <= 57;
    }
    
    private void skipDigits() {
        while (this.pos < this.length) {
            final int c = this.peek();
            if (!this.isDigit(c)) {
                break;
            }
            ++this.pos;
        }
    }
    
    private Number parseNumber() {
        final int start = this.pos;
        int c = this.next();
        if (c == 45) {
            c = this.next();
        }
        if (!this.isDigit(c)) {
            throw this.numberError(start);
        }
        if (c != 48) {
            this.skipDigits();
        }
        if (this.peek() == 46) {
            ++this.pos;
            if (!this.isDigit(this.next())) {
                throw this.numberError(this.pos - 1);
            }
            this.skipDigits();
        }
        c = this.peek();
        if (c == 101 || c == 69) {
            ++this.pos;
            c = this.next();
            if (c == 45 || c == 43) {
                c = this.next();
            }
            if (!this.isDigit(c)) {
                throw this.numberError(this.pos - 1);
            }
            this.skipDigits();
        }
        final double d = Double.parseDouble(this.source.substring(start, this.pos));
        if (JSType.isRepresentableAsInt(d)) {
            return (int)d;
        }
        return d;
    }
    
    private Object parseKeyword(final String keyword, final Object value) {
        if (!this.source.regionMatches(this.pos, keyword, 0, keyword.length())) {
            throw this.expectedError(this.pos, "json literal", "ident");
        }
        this.pos += keyword.length();
        return value;
    }
    
    private int peek() {
        if (this.pos >= this.length) {
            return -1;
        }
        return this.source.charAt(this.pos);
    }
    
    private int next() {
        final int next = this.peek();
        ++this.pos;
        return next;
    }
    
    private void skipWhiteSpace() {
        while (this.pos < this.length) {
            switch (this.peek()) {
                case 9:
                case 10:
                case 13:
                case 32: {
                    ++this.pos;
                    continue;
                }
                default: {}
            }
        }
    }
    
    private static String toString(final int c) {
        return (c == -1) ? "eof" : String.valueOf((char)c);
    }
    
    ParserException error(final String message, final int start, final int length) throws ParserException {
        final long token = Token.toDesc(TokenType.STRING, start, length);
        final int pos = Token.descPosition(token);
        final Source src = Source.sourceFor("<json>", this.source);
        final int lineNum = src.getLine(pos);
        final int columnNum = src.getColumn(pos);
        final String formatted = ErrorManager.format(message, src, lineNum, columnNum, token);
        return new ParserException(JSErrorType.SYNTAX_ERROR, formatted, src, lineNum, columnNum, token);
    }
    
    private ParserException error(final String message, final int start) {
        return this.error(message, start, this.length);
    }
    
    private ParserException numberError(final int start) {
        return this.error(Lexer.message("json.invalid.number", new String[0]), start);
    }
    
    private ParserException expectedError(final int start, final String expected, final String found) {
        return this.error(AbstractParser.message("expected", expected, found), start);
    }
    
    private ParserException syntaxError(final int start, final String reason) {
        final String message = ECMAErrors.getMessage("syntax.error.invalid.json", reason);
        return this.error(message, start);
    }
}
