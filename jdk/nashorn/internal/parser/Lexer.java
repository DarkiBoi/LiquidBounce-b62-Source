// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

import java.io.Serializable;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Source;

public class Lexer extends Scanner
{
    private static final long MIN_INT_L = -2147483648L;
    private static final long MAX_INT_L = 2147483647L;
    private static final boolean XML_LITERALS;
    private final Source source;
    private final TokenStream stream;
    private final boolean scripting;
    private final boolean nested;
    int pendingLine;
    private int linePosition;
    private TokenType last;
    private final boolean pauseOnFunctionBody;
    private boolean pauseOnNextLeftBrace;
    private static final String SPACETAB = " \t";
    private static final String LFCR = "\n\r";
    private static final String JAVASCRIPT_WHITESPACE_EOL = "\n\r\u2028\u2029";
    private static final String JAVASCRIPT_WHITESPACE = " \t\n\r\u2028\u2029\u000b\f \u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\ufeff";
    private static final String JAVASCRIPT_WHITESPACE_IN_REGEXP = "\\u000a\\u000d\\u2028\\u2029\\u0009\\u0020\\u000b\\u000c\\u00a0\\u1680\\u180e\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200a\\u202f\\u205f\\u3000\\ufeff";
    
    static String unicodeEscape(final char ch) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\\u");
        final String hex = Integer.toHexString(ch);
        for (int i = hex.length(); i < 4; ++i) {
            sb.append('0');
        }
        sb.append(hex);
        return sb.toString();
    }
    
    public Lexer(final Source source, final TokenStream stream) {
        this(source, stream, false);
    }
    
    public Lexer(final Source source, final TokenStream stream, final boolean scripting) {
        this(source, 0, source.getLength(), stream, scripting, false);
    }
    
    public Lexer(final Source source, final int start, final int len, final TokenStream stream, final boolean scripting, final boolean pauseOnFunctionBody) {
        super(source.getContent(), 1, start, len);
        this.source = source;
        this.stream = stream;
        this.scripting = scripting;
        this.nested = false;
        this.pendingLine = 1;
        this.last = TokenType.EOL;
        this.pauseOnFunctionBody = pauseOnFunctionBody;
    }
    
    private Lexer(final Lexer lexer, final State state) {
        super(lexer, state);
        this.source = lexer.source;
        this.stream = lexer.stream;
        this.scripting = lexer.scripting;
        this.nested = true;
        this.pendingLine = state.pendingLine;
        this.linePosition = state.linePosition;
        this.last = TokenType.EOL;
        this.pauseOnFunctionBody = false;
    }
    
    @Override
    State saveState() {
        return new State(this.position, this.limit, this.line, this.pendingLine, this.linePosition, this.last);
    }
    
    void restoreState(final State state) {
        super.restoreState(state);
        this.pendingLine = state.pendingLine;
        this.linePosition = state.linePosition;
        this.last = state.last;
    }
    
    protected void add(final TokenType type, final int start, final int end) {
        this.last = type;
        if (type == TokenType.EOL) {
            this.pendingLine = end;
            this.linePosition = start;
        }
        else {
            if (this.pendingLine != -1) {
                this.stream.put(Token.toDesc(TokenType.EOL, this.linePosition, this.pendingLine));
                this.pendingLine = -1;
            }
            this.stream.put(Token.toDesc(type, start, end - start));
        }
    }
    
    protected void add(final TokenType type, final int start) {
        this.add(type, start, this.position);
    }
    
    public static String getWhitespaceRegExp() {
        return "\\u000a\\u000d\\u2028\\u2029\\u0009\\u0020\\u000b\\u000c\\u00a0\\u1680\\u180e\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200a\\u202f\\u205f\\u3000\\ufeff";
    }
    
    private void skipEOL(final boolean addEOL) {
        if (this.ch0 == '\r') {
            this.skip(1);
            if (this.ch0 == '\n') {
                this.skip(1);
            }
        }
        else {
            this.skip(1);
        }
        ++this.line;
        if (addEOL) {
            this.add(TokenType.EOL, this.position, this.line);
        }
    }
    
    private void skipLine(final boolean addEOL) {
        while (!this.isEOL(this.ch0) && !this.atEOF()) {
            this.skip(1);
        }
        this.skipEOL(addEOL);
    }
    
    public static boolean isJSWhitespace(final char ch) {
        return " \t\n\r\u2028\u2029\u000b\f \u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\ufeff".indexOf(ch) != -1;
    }
    
    public static boolean isJSEOL(final char ch) {
        return "\n\r\u2028\u2029".indexOf(ch) != -1;
    }
    
    protected boolean isStringDelimiter(final char ch) {
        return ch == '\'' || ch == '\"' || (this.scripting && ch == '`');
    }
    
    protected boolean isWhitespace(final char ch) {
        return isJSWhitespace(ch);
    }
    
    protected boolean isEOL(final char ch) {
        return isJSEOL(ch);
    }
    
    private void skipWhitespace(final boolean addEOL) {
        while (this.isWhitespace(this.ch0)) {
            if (this.isEOL(this.ch0)) {
                this.skipEOL(addEOL);
            }
            else {
                this.skip(1);
            }
        }
    }
    
    protected boolean skipComments() {
        final int start = this.position;
        if (this.ch0 == '/') {
            if (this.ch1 == '/') {
                this.skip(2);
                boolean directiveComment = false;
                if ((this.ch0 == '#' || this.ch0 == '@') && this.ch1 == ' ') {
                    directiveComment = true;
                }
                while (!this.atEOF() && !this.isEOL(this.ch0)) {
                    this.skip(1);
                }
                this.add(directiveComment ? TokenType.DIRECTIVE_COMMENT : TokenType.COMMENT, start);
                return true;
            }
            if (this.ch1 == '*') {
                this.skip(2);
                while (!this.atEOF() && (this.ch0 != '*' || this.ch1 != '/')) {
                    if (this.isEOL(this.ch0)) {
                        this.skipEOL(true);
                    }
                    else {
                        this.skip(1);
                    }
                }
                if (this.atEOF()) {
                    this.add(TokenType.ERROR, start);
                }
                else {
                    this.skip(2);
                }
                this.add(TokenType.COMMENT, start);
                return true;
            }
        }
        else if (this.ch0 == '#') {
            assert this.scripting;
            this.skip(1);
            while (!this.atEOF() && !this.isEOL(this.ch0)) {
                this.skip(1);
            }
            this.add(TokenType.COMMENT, start);
            return true;
        }
        return false;
    }
    
    public RegexToken valueOfPattern(final int start, final int length) {
        final int savePosition = this.position;
        this.reset(start);
        final StringBuilder sb = new StringBuilder(length);
        this.skip(1);
        boolean inBrackets = false;
        while ((!this.atEOF() && this.ch0 != '/' && !this.isEOL(this.ch0)) || inBrackets) {
            if (this.ch0 == '\\') {
                sb.append(this.ch0);
                sb.append(this.ch1);
                this.skip(2);
            }
            else {
                if (this.ch0 == '[') {
                    inBrackets = true;
                }
                else if (this.ch0 == ']') {
                    inBrackets = false;
                }
                sb.append(this.ch0);
                this.skip(1);
            }
        }
        final String regex = sb.toString();
        this.skip(1);
        final String options = this.source.getString(this.position, this.scanIdentifier());
        this.reset(savePosition);
        return new RegexToken(regex, options);
    }
    
    public boolean canStartLiteral(final TokenType token) {
        return token.startsWith('/') || ((this.scripting || Lexer.XML_LITERALS) && token.startsWith('<'));
    }
    
    protected boolean scanLiteral(final long token, final TokenType startTokenType, final LineInfoReceiver lir) {
        if (!this.canStartLiteral(startTokenType)) {
            return false;
        }
        if (this.stream.get(this.stream.last()) != token) {
            return false;
        }
        this.reset(Token.descPosition(token));
        if (this.ch0 == '/') {
            return this.scanRegEx();
        }
        if (this.ch0 == '<') {
            if (this.ch1 == '<') {
                return this.scanHereString(lir);
            }
            if (Character.isJavaIdentifierStart(this.ch1)) {
                return this.scanXMLLiteral();
            }
        }
        return false;
    }
    
    private boolean scanRegEx() {
        assert this.ch0 == '/';
        if (this.ch1 != '/' && this.ch1 != '*') {
            final int start = this.position;
            this.skip(1);
            boolean inBrackets = false;
            while (!this.atEOF() && (this.ch0 != '/' || inBrackets) && !this.isEOL(this.ch0)) {
                if (this.ch0 == '\\') {
                    this.skip(1);
                    if (this.isEOL(this.ch0)) {
                        this.reset(start);
                        return false;
                    }
                    this.skip(1);
                }
                else {
                    if (this.ch0 == '[') {
                        inBrackets = true;
                    }
                    else if (this.ch0 == ']') {
                        inBrackets = false;
                    }
                    this.skip(1);
                }
            }
            if (this.ch0 == '/') {
                this.skip(1);
                while ((!this.atEOF() && Character.isJavaIdentifierPart(this.ch0)) || (this.ch0 == '\\' && this.ch1 == 'u')) {
                    this.skip(1);
                }
                this.add(TokenType.REGEX, start);
                return true;
            }
            this.reset(start);
        }
        return false;
    }
    
    protected static int convertDigit(final char ch, final int base) {
        int digit;
        if ('0' <= ch && ch <= '9') {
            digit = ch - '0';
        }
        else if ('A' <= ch && ch <= 'Z') {
            digit = ch - 'A' + 10;
        }
        else {
            if ('a' > ch || ch > 'z') {
                return -1;
            }
            digit = ch - 'a' + 10;
        }
        return (digit < base) ? digit : -1;
    }
    
    private int hexSequence(final int length, final TokenType type) {
        int value = 0;
        for (int i = 0; i < length; ++i) {
            final int digit = convertDigit(this.ch0, 16);
            if (digit == -1) {
                this.error(message("invalid.hex", new String[0]), type, this.position, this.limit);
                return (i == 0) ? -1 : value;
            }
            value = (digit | value << 4);
            this.skip(1);
        }
        return value;
    }
    
    private int octalSequence() {
        int value = 0;
        for (int i = 0; i < 3; ++i) {
            final int digit = convertDigit(this.ch0, 8);
            if (digit == -1) {
                break;
            }
            value = (digit | value << 3);
            this.skip(1);
            if (i == 1 && value >= 32) {
                break;
            }
        }
        return value;
    }
    
    private String valueOfIdent(final int start, final int length) throws RuntimeException {
        final int savePosition = this.position;
        final int end = start + length;
        this.reset(start);
        final StringBuilder sb = new StringBuilder(length);
        while (!this.atEOF() && this.position < end && !this.isEOL(this.ch0)) {
            if (this.ch0 == '\\' && this.ch1 == 'u') {
                this.skip(2);
                final int ch = this.hexSequence(4, TokenType.IDENT);
                if (this.isWhitespace((char)ch)) {
                    return null;
                }
                if (ch < 0) {
                    sb.append('\\');
                    sb.append('u');
                }
                else {
                    sb.append((char)ch);
                }
            }
            else {
                sb.append(this.ch0);
                this.skip(1);
            }
        }
        this.reset(savePosition);
        return sb.toString();
    }
    
    private void scanIdentifierOrKeyword() {
        final int start = this.position;
        final int length = this.scanIdentifier();
        final TokenType type = TokenLookup.lookupKeyword(this.content, start, length);
        if (type == TokenType.FUNCTION && this.pauseOnFunctionBody) {
            this.pauseOnNextLeftBrace = true;
        }
        this.add(type, start);
    }
    
    private String valueOfString(final int start, final int length, final boolean strict) throws RuntimeException {
        final int savePosition = this.position;
        final int end = start + length;
        this.reset(start);
        final StringBuilder sb = new StringBuilder(length);
        while (this.position < end) {
            if (this.ch0 == '\\') {
                this.skip(1);
                final char next = this.ch0;
                final int afterSlash = this.position;
                this.skip(1);
                switch (next) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7': {
                        if (strict && (next != '0' || (this.ch0 >= '0' && this.ch0 <= '9'))) {
                            this.error(message("strict.no.octal", new String[0]), TokenType.STRING, this.position, this.limit);
                        }
                        this.reset(afterSlash);
                        final int ch = this.octalSequence();
                        if (ch < 0) {
                            sb.append('\\');
                            sb.append('x');
                            continue;
                        }
                        sb.append((char)ch);
                        continue;
                    }
                    case 'n': {
                        sb.append('\n');
                        continue;
                    }
                    case 't': {
                        sb.append('\t');
                        continue;
                    }
                    case 'b': {
                        sb.append('\b');
                        continue;
                    }
                    case 'f': {
                        sb.append('\f');
                        continue;
                    }
                    case 'r': {
                        sb.append('\r');
                        continue;
                    }
                    case '\'': {
                        sb.append('\'');
                        continue;
                    }
                    case '\"': {
                        sb.append('\"');
                        continue;
                    }
                    case '\\': {
                        sb.append('\\');
                        continue;
                    }
                    case '\r': {
                        if (this.ch0 == '\n') {
                            this.skip(1);
                            continue;
                        }
                        continue;
                    }
                    case '\n':
                    case '\u2028':
                    case '\u2029': {
                        continue;
                    }
                    case 'x': {
                        final int ch = this.hexSequence(2, TokenType.STRING);
                        if (ch < 0) {
                            sb.append('\\');
                            sb.append('x');
                        }
                        else {
                            sb.append((char)ch);
                        }
                        continue;
                    }
                    case 'u': {
                        final int ch = this.hexSequence(4, TokenType.STRING);
                        if (ch < 0) {
                            sb.append('\\');
                            sb.append('u');
                        }
                        else {
                            sb.append((char)ch);
                        }
                        continue;
                    }
                    case 'v': {
                        sb.append('\u000b');
                        continue;
                    }
                    default: {
                        sb.append(next);
                        continue;
                    }
                }
            }
            else {
                sb.append(this.ch0);
                this.skip(1);
            }
        }
        this.reset(savePosition);
        return sb.toString();
    }
    
    protected void scanString(final boolean add) {
        TokenType type = TokenType.STRING;
        final char quote = this.ch0;
        this.skip(1);
        final State stringState = this.saveState();
        while (!this.atEOF() && this.ch0 != quote && !this.isEOL(this.ch0)) {
            if (this.ch0 == '\\') {
                type = TokenType.ESCSTRING;
                this.skip(1);
                if (!this.isEscapeCharacter(this.ch0)) {
                    this.error(message("invalid.escape.char", new String[0]), TokenType.STRING, this.position, this.limit);
                }
                if (this.isEOL(this.ch0)) {
                    this.skipEOL(false);
                    continue;
                }
            }
            this.skip(1);
        }
        if (this.ch0 == quote) {
            this.skip(1);
        }
        else {
            this.error(message("missing.close.quote", new String[0]), TokenType.STRING, this.position, this.limit);
        }
        if (add) {
            stringState.setLimit(this.position - 1);
            if (this.scripting && !stringState.isEmpty()) {
                switch (quote) {
                    case '`': {
                        this.add(TokenType.EXECSTRING, stringState.position, stringState.limit);
                        this.add(TokenType.LBRACE, stringState.position, stringState.position);
                        this.editString(type, stringState);
                        this.add(TokenType.RBRACE, stringState.limit, stringState.limit);
                        break;
                    }
                    case '\"': {
                        this.editString(type, stringState);
                        break;
                    }
                    case '\'': {
                        this.add(type, stringState.position, stringState.limit);
                        break;
                    }
                }
            }
            else {
                this.add(type, stringState.position, stringState.limit);
            }
        }
    }
    
    protected boolean isEscapeCharacter(final char ch) {
        return true;
    }
    
    private static Number valueOf(final String valueString, final int radix) throws NumberFormatException {
        try {
            return Integer.parseInt(valueString, radix);
        }
        catch (NumberFormatException e) {
            if (radix == 10) {
                return Double.valueOf(valueString);
            }
            double value = 0.0;
            for (int i = 0; i < valueString.length(); ++i) {
                final char ch = valueString.charAt(i);
                final int digit = convertDigit(ch, radix);
                value *= radix;
                value += digit;
            }
            return value;
        }
    }
    
    protected void scanNumber() {
        final int start = this.position;
        TokenType type = TokenType.DECIMAL;
        int digit = convertDigit(this.ch0, 10);
        if (digit == 0 && (this.ch1 == 'x' || this.ch1 == 'X') && convertDigit(this.ch2, 16) != -1) {
            this.skip(3);
            while (convertDigit(this.ch0, 16) != -1) {
                this.skip(1);
            }
            type = TokenType.HEXADECIMAL;
        }
        else {
            boolean octal = digit == 0;
            if (digit != -1) {
                this.skip(1);
            }
            while ((digit = convertDigit(this.ch0, 10)) != -1) {
                octal = (octal && digit < 8);
                this.skip(1);
            }
            if (octal && this.position - start > 1) {
                type = TokenType.OCTAL;
            }
            else if (this.ch0 == '.' || this.ch0 == 'E' || this.ch0 == 'e') {
                if (this.ch0 == '.') {
                    this.skip(1);
                    while (convertDigit(this.ch0, 10) != -1) {
                        this.skip(1);
                    }
                }
                if (this.ch0 == 'E' || this.ch0 == 'e') {
                    this.skip(1);
                    if (this.ch0 == '+' || this.ch0 == '-') {
                        this.skip(1);
                    }
                    while (convertDigit(this.ch0, 10) != -1) {
                        this.skip(1);
                    }
                }
                type = TokenType.FLOATING;
            }
        }
        if (Character.isJavaIdentifierStart(this.ch0)) {
            this.error(message("missing.space.after.number", new String[0]), type, this.position, 1);
        }
        this.add(type, start);
    }
    
    XMLToken valueOfXML(final int start, final int length) {
        return new XMLToken(this.source.getString(start, length));
    }
    
    private boolean scanXMLLiteral() {
        assert this.ch0 == '<' && Character.isJavaIdentifierStart(this.ch1);
        if (Lexer.XML_LITERALS) {
            final int start = this.position;
            int openCount = 0;
            do {
                if (this.ch0 == '<') {
                    if (this.ch1 == '/' && Character.isJavaIdentifierStart(this.ch2)) {
                        this.skip(3);
                        --openCount;
                    }
                    else if (Character.isJavaIdentifierStart(this.ch1)) {
                        this.skip(2);
                        ++openCount;
                    }
                    else if (this.ch1 == '?') {
                        this.skip(2);
                    }
                    else {
                        if (this.ch1 != '!' || this.ch2 != '-' || this.ch3 != '-') {
                            this.reset(start);
                            return false;
                        }
                        this.skip(4);
                    }
                    while (!this.atEOF() && this.ch0 != '>') {
                        if (this.ch0 == '/' && this.ch1 == '>') {
                            --openCount;
                            this.skip(1);
                            break;
                        }
                        if (this.ch0 == '\"' || this.ch0 == '\'') {
                            this.scanString(false);
                        }
                        else {
                            this.skip(1);
                        }
                    }
                    if (this.ch0 != '>') {
                        this.reset(start);
                        return false;
                    }
                    this.skip(1);
                }
                else {
                    if (this.atEOF()) {
                        this.reset(start);
                        return false;
                    }
                    this.skip(1);
                }
            } while (openCount > 0);
            this.add(TokenType.XML, start);
            return true;
        }
        return false;
    }
    
    private int scanIdentifier() {
        final int start = this.position;
        if (this.ch0 == '\\' && this.ch1 == 'u') {
            this.skip(2);
            final int ch = this.hexSequence(4, TokenType.IDENT);
            if (!Character.isJavaIdentifierStart(ch)) {
                this.error(message("illegal.identifier.character", new String[0]), TokenType.IDENT, start, this.position);
            }
        }
        else if (!Character.isJavaIdentifierStart(this.ch0)) {
            return 0;
        }
        while (!this.atEOF()) {
            if (this.ch0 == '\\' && this.ch1 == 'u') {
                this.skip(2);
                final int ch = this.hexSequence(4, TokenType.IDENT);
                if (Character.isJavaIdentifierPart(ch)) {
                    continue;
                }
                this.error(message("illegal.identifier.character", new String[0]), TokenType.IDENT, start, this.position);
            }
            else {
                if (!Character.isJavaIdentifierPart(this.ch0)) {
                    break;
                }
                this.skip(1);
            }
        }
        return this.position - start;
    }
    
    private boolean identifierEqual(final int aStart, final int aLength, final int bStart, final int bLength) {
        if (aLength == bLength) {
            for (int i = 0; i < aLength; ++i) {
                if (this.content[aStart + i] != this.content[bStart + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean hasHereMarker(final int identStart, final int identLength) {
        this.skipWhitespace(false);
        return this.identifierEqual(identStart, identLength, this.position, this.scanIdentifier());
    }
    
    private void editString(final TokenType stringType, final State stringState) {
        final EditStringLexer lexer = new EditStringLexer(this, stringType, stringState);
        lexer.lexify();
        this.last = stringType;
    }
    
    private boolean scanHereString(final LineInfoReceiver lir) {
        assert this.ch0 == '<' && this.ch1 == '<';
        if (!this.scripting) {
            return false;
        }
        final State saved = this.saveState();
        final boolean excludeLastEOL = this.ch2 != '<';
        if (excludeLastEOL) {
            this.skip(2);
        }
        else {
            this.skip(3);
        }
        final char quoteChar = this.ch0;
        final boolean noStringEditing = quoteChar == '\"' || quoteChar == '\'';
        if (noStringEditing) {
            this.skip(1);
        }
        final int identStart = this.position;
        final int identLength = this.scanIdentifier();
        if (noStringEditing) {
            if (this.ch0 != quoteChar) {
                this.error(message("here.non.matching.delimiter", new String[0]), this.last, this.position, this.position);
                this.restoreState(saved);
                return false;
            }
            this.skip(1);
        }
        if (identLength == 0) {
            this.restoreState(saved);
            return false;
        }
        final State restState = this.saveState();
        int lastLine = this.line;
        this.skipLine(false);
        ++lastLine;
        int lastLinePosition = this.position;
        restState.setLimit(this.position);
        final State stringState = this.saveState();
        int stringEnd = this.position;
        while (!this.atEOF()) {
            this.skipWhitespace(false);
            if (this.hasHereMarker(identStart, identLength)) {
                break;
            }
            this.skipLine(false);
            ++lastLine;
            lastLinePosition = this.position;
            stringEnd = this.position;
        }
        lir.lineInfo(lastLine, lastLinePosition);
        stringState.setLimit(stringEnd);
        if (stringState.isEmpty() || this.atEOF()) {
            this.error(message("here.missing.end.marker", this.source.getString(identStart, identLength)), this.last, this.position, this.position);
            this.restoreState(saved);
            return false;
        }
        if (excludeLastEOL) {
            if (this.content[stringEnd - 1] == '\n') {
                --stringEnd;
            }
            if (this.content[stringEnd - 1] == '\r') {
                --stringEnd;
            }
            stringState.setLimit(stringEnd);
        }
        if (!noStringEditing && !stringState.isEmpty()) {
            this.editString(TokenType.STRING, stringState);
        }
        else {
            this.add(TokenType.STRING, stringState.position, stringState.limit);
        }
        final Lexer restLexer = new Lexer(this, restState);
        restLexer.lexify();
        return true;
    }
    
    public void lexify() {
        while (!this.stream.isFull() || this.nested) {
            this.skipWhitespace(true);
            if (this.atEOF()) {
                if (!this.nested) {
                    this.add(TokenType.EOF, this.position);
                    break;
                }
                break;
            }
            else {
                if (this.ch0 == '/' && this.skipComments()) {
                    continue;
                }
                if (this.scripting && this.ch0 == '#' && this.skipComments()) {
                    continue;
                }
                if (this.ch0 == '.' && convertDigit(this.ch1, 10) != -1) {
                    this.scanNumber();
                }
                else {
                    final TokenType type;
                    if ((type = TokenLookup.lookupOperator(this.ch0, this.ch1, this.ch2, this.ch3)) != null) {
                        final int typeLength = type.getLength();
                        this.skip(typeLength);
                        this.add(type, this.position - typeLength);
                        if (this.canStartLiteral(type)) {
                            break;
                        }
                        if (type == TokenType.LBRACE && this.pauseOnNextLeftBrace) {
                            this.pauseOnNextLeftBrace = false;
                            break;
                        }
                        continue;
                    }
                    else if (Character.isJavaIdentifierStart(this.ch0) || (this.ch0 == '\\' && this.ch1 == 'u')) {
                        this.scanIdentifierOrKeyword();
                    }
                    else if (this.isStringDelimiter(this.ch0)) {
                        this.scanString(true);
                    }
                    else if (Character.isDigit(this.ch0)) {
                        this.scanNumber();
                    }
                    else {
                        this.skip(1);
                        this.add(TokenType.ERROR, this.position - 1);
                    }
                }
            }
        }
    }
    
    Object getValueOf(final long token, final boolean strict) {
        final int start = Token.descPosition(token);
        final int len = Token.descLength(token);
        switch (Token.descType(token)) {
            case DECIMAL: {
                return valueOf(this.source.getString(start, len), 10);
            }
            case OCTAL: {
                return valueOf(this.source.getString(start, len), 8);
            }
            case HEXADECIMAL: {
                return valueOf(this.source.getString(start + 2, len - 2), 16);
            }
            case FLOATING: {
                final String str = this.source.getString(start, len);
                final double value = Double.valueOf(str);
                if (str.indexOf(46) != -1) {
                    return value;
                }
                if (JSType.isStrictlyRepresentableAsInt(value)) {
                    return (int)value;
                }
                return value;
            }
            case STRING: {
                return this.source.getString(start, len);
            }
            case ESCSTRING: {
                return this.valueOfString(start, len, strict);
            }
            case IDENT: {
                return this.valueOfIdent(start, len);
            }
            case REGEX: {
                return this.valueOfPattern(start, len);
            }
            case XML: {
                return this.valueOfXML(start, len);
            }
            case DIRECTIVE_COMMENT: {
                return this.source.getString(start, len);
            }
            default: {
                return null;
            }
        }
    }
    
    protected static String message(final String msgId, final String... args) {
        return ECMAErrors.getMessage("lexer.error." + msgId, args);
    }
    
    protected void error(final String message, final TokenType type, final int start, final int length) throws ParserException {
        final long token = Token.toDesc(type, start, length);
        final int pos = Token.descPosition(token);
        final int lineNum = this.source.getLine(pos);
        final int columnNum = this.source.getColumn(pos);
        final String formatted = ErrorManager.format(message, this.source, lineNum, columnNum, token);
        throw new ParserException(JSErrorType.SYNTAX_ERROR, formatted, this.source, lineNum, columnNum, token);
    }
    
    static {
        XML_LITERALS = Options.getBooleanProperty("nashorn.lexer.xmlliterals");
    }
    
    static class State extends Scanner.State
    {
        public final int pendingLine;
        public final int linePosition;
        public final TokenType last;
        
        State(final int position, final int limit, final int line, final int pendingLine, final int linePosition, final TokenType last) {
            super(position, limit, line);
            this.pendingLine = pendingLine;
            this.linePosition = linePosition;
            this.last = last;
        }
    }
    
    private static class EditStringLexer extends Lexer
    {
        final TokenType stringType;
        
        EditStringLexer(final Lexer lexer, final TokenType stringType, final State stringState) {
            super(lexer, stringState, null);
            this.stringType = stringType;
        }
        
        @Override
        public void lexify() {
            int stringStart = this.position;
            boolean primed = false;
            while (!this.atEOF()) {
                if (this.ch0 == '\\' && this.stringType == TokenType.ESCSTRING) {
                    this.skip(2);
                }
                else if (this.ch0 == '$' && this.ch1 == '{') {
                    if (!primed || stringStart != this.position) {
                        if (primed) {
                            this.add(TokenType.ADD, stringStart, stringStart + 1);
                        }
                        this.add(this.stringType, stringStart, this.position);
                        primed = true;
                    }
                    this.skip(2);
                    final State expressionState = this.saveState();
                    int braceCount = 1;
                    while (!this.atEOF()) {
                        if (this.ch0 == '}') {
                            if (--braceCount == 0) {
                                break;
                            }
                        }
                        else if (this.ch0 == '{') {
                            ++braceCount;
                        }
                        this.skip(1);
                    }
                    if (braceCount != 0) {
                        this.error(Lexer.message("edit.string.missing.brace", new String[0]), TokenType.LBRACE, expressionState.position - 1, 1);
                    }
                    expressionState.setLimit(this.position);
                    this.skip(1);
                    stringStart = this.position;
                    this.add(TokenType.ADD, expressionState.position, expressionState.position + 1);
                    this.add(TokenType.LPAREN, expressionState.position, expressionState.position + 1);
                    final Lexer lexer = new Lexer(this, expressionState, null);
                    lexer.lexify();
                    this.add(TokenType.RPAREN, this.position - 1, this.position);
                }
                else {
                    this.skip(1);
                }
            }
            if (stringStart != this.limit) {
                if (primed) {
                    this.add(TokenType.ADD, stringStart, 1);
                }
                this.add(this.stringType, stringStart, this.limit);
            }
        }
    }
    
    public abstract static class LexerToken implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String expression;
        
        protected LexerToken(final String expression) {
            this.expression = expression;
        }
        
        public String getExpression() {
            return this.expression;
        }
    }
    
    public static class RegexToken extends LexerToken
    {
        private static final long serialVersionUID = 1L;
        private final String options;
        
        public RegexToken(final String expression, final String options) {
            super(expression);
            this.options = options;
        }
        
        public String getOptions() {
            return this.options;
        }
        
        @Override
        public String toString() {
            return '/' + this.getExpression() + '/' + this.options;
        }
    }
    
    public static class XMLToken extends LexerToken
    {
        private static final long serialVersionUID = 1L;
        
        public XMLToken(final String expression) {
            super(expression);
        }
    }
    
    protected interface LineInfoReceiver
    {
        void lineInfo(final int p0, final int p1);
    }
}
