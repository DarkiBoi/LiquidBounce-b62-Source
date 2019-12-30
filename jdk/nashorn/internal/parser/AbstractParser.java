// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;

public abstract class AbstractParser
{
    protected final Source source;
    protected final ErrorManager errors;
    protected TokenStream stream;
    protected int k;
    protected long previousToken;
    protected long token;
    protected TokenType type;
    protected TokenType last;
    protected int start;
    protected int finish;
    protected int line;
    protected int linePosition;
    protected Lexer lexer;
    protected boolean isStrictMode;
    protected final int lineOffset;
    private final Map<String, String> canonicalNames;
    private static final String SOURCE_URL_PREFIX = "sourceURL=";
    
    protected AbstractParser(final Source source, final ErrorManager errors, final boolean strict, final int lineOffset) {
        this.canonicalNames = new HashMap<String, String>();
        this.source = source;
        this.errors = errors;
        this.k = -1;
        this.token = Token.toDesc(TokenType.EOL, 0, 1);
        this.type = TokenType.EOL;
        this.last = TokenType.EOL;
        this.isStrictMode = strict;
        this.lineOffset = lineOffset;
    }
    
    protected final long getToken(final int i) {
        while (i > this.stream.last()) {
            if (this.stream.isFull()) {
                this.stream.grow();
            }
            this.lexer.lexify();
        }
        return this.stream.get(i);
    }
    
    protected final TokenType T(final int i) {
        return Token.descType(this.getToken(i));
    }
    
    protected final TokenType next() {
        do {
            this.nextOrEOL();
        } while (this.type == TokenType.EOL || this.type == TokenType.COMMENT);
        return this.type;
    }
    
    protected final TokenType nextOrEOL() {
        do {
            this.nextToken();
            if (this.type == TokenType.DIRECTIVE_COMMENT) {
                this.checkDirectiveComment();
            }
        } while (this.type == TokenType.COMMENT || this.type == TokenType.DIRECTIVE_COMMENT);
        return this.type;
    }
    
    private void checkDirectiveComment() {
        if (this.source.getExplicitURL() != null) {
            return;
        }
        final String comment = (String)this.lexer.getValueOf(this.token, this.isStrictMode);
        final int len = comment.length();
        if (len > 4 && comment.substring(4).startsWith("sourceURL=")) {
            this.source.setExplicitURL(comment.substring(4 + "sourceURL=".length()));
        }
    }
    
    private TokenType nextToken() {
        if (this.type != TokenType.COMMENT) {
            this.last = this.type;
        }
        if (this.type != TokenType.EOF) {
            ++this.k;
            final long lastToken = this.token;
            this.previousToken = this.token;
            this.token = this.getToken(this.k);
            this.type = Token.descType(this.token);
            if (this.last != TokenType.EOL) {
                this.finish = this.start + Token.descLength(lastToken);
            }
            if (this.type == TokenType.EOL) {
                this.line = Token.descLength(this.token);
                this.linePosition = Token.descPosition(this.token);
            }
            else {
                this.start = Token.descPosition(this.token);
            }
        }
        return this.type;
    }
    
    protected static String message(final String msgId, final String... args) {
        return ECMAErrors.getMessage("parser.error." + msgId, args);
    }
    
    protected final ParserException error(final String message, final long errorToken) {
        return this.error(JSErrorType.SYNTAX_ERROR, message, errorToken);
    }
    
    protected final ParserException error(final JSErrorType errorType, final String message, final long errorToken) {
        final int position = Token.descPosition(errorToken);
        final int lineNum = this.source.getLine(position);
        final int columnNum = this.source.getColumn(position);
        final String formatted = ErrorManager.format(message, this.source, lineNum, columnNum, errorToken);
        return new ParserException(errorType, formatted, this.source, lineNum, columnNum, errorToken);
    }
    
    protected final ParserException error(final String message) {
        return this.error(JSErrorType.SYNTAX_ERROR, message);
    }
    
    protected final ParserException error(final JSErrorType errorType, final String message) {
        final int position = Token.descPosition(this.token);
        final int column = position - this.linePosition;
        final String formatted = ErrorManager.format(message, this.source, this.line, column, this.token);
        return new ParserException(errorType, formatted, this.source, this.line, column, this.token);
    }
    
    protected final void warning(final JSErrorType errorType, final String message, final long errorToken) {
        this.errors.warning(this.error(errorType, message, errorToken));
    }
    
    protected final String expectMessage(final TokenType expected) {
        final String tokenString = Token.toString(this.source, this.token);
        String msg;
        if (expected == null) {
            msg = message("expected.stmt", tokenString);
        }
        else {
            final String expectedName = expected.getNameOrType();
            msg = message("expected", expectedName, tokenString);
        }
        return msg;
    }
    
    protected final void expect(final TokenType expected) throws ParserException {
        this.expectDontAdvance(expected);
        this.next();
    }
    
    protected final void expectDontAdvance(final TokenType expected) throws ParserException {
        if (this.type != expected) {
            throw this.error(this.expectMessage(expected));
        }
    }
    
    protected final Object expectValue(final TokenType expected) throws ParserException {
        if (this.type != expected) {
            throw this.error(this.expectMessage(expected));
        }
        final Object value = this.getValue();
        this.next();
        return value;
    }
    
    protected final Object getValue() {
        return this.getValue(this.token);
    }
    
    protected final Object getValue(final long valueToken) {
        try {
            return this.lexer.getValueOf(valueToken, this.isStrictMode);
        }
        catch (ParserException e) {
            this.errors.error(e);
            return null;
        }
    }
    
    protected final boolean isNonStrictModeIdent() {
        return !this.isStrictMode && this.type.getKind() == TokenKind.FUTURESTRICT;
    }
    
    protected final IdentNode getIdent() {
        long identToken = this.token;
        if (this.isNonStrictModeIdent()) {
            identToken = Token.recast(this.token, TokenType.IDENT);
            final String ident = (String)this.getValue(identToken);
            this.next();
            return this.createIdentNode(identToken, this.finish, ident).setIsFutureStrictName();
        }
        final String ident = (String)this.expectValue(TokenType.IDENT);
        if (ident == null) {
            return null;
        }
        return this.createIdentNode(identToken, this.finish, ident);
    }
    
    protected IdentNode createIdentNode(final long identToken, final int identFinish, final String name) {
        final String existingName = this.canonicalNames.putIfAbsent(name, name);
        final String canonicalName = (existingName != null) ? existingName : name;
        return new IdentNode(identToken, identFinish, canonicalName);
    }
    
    protected final boolean isIdentifierName() {
        final TokenKind kind = this.type.getKind();
        if (kind == TokenKind.KEYWORD || kind == TokenKind.FUTURE || kind == TokenKind.FUTURESTRICT) {
            return true;
        }
        if (kind != TokenKind.LITERAL) {
            final long identToken = Token.recast(this.token, TokenType.IDENT);
            final String ident = (String)this.getValue(identToken);
            return !ident.isEmpty() && Character.isJavaIdentifierStart(ident.charAt(0));
        }
        switch (this.type) {
            case FALSE:
            case NULL:
            case TRUE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    protected final IdentNode getIdentifierName() {
        if (this.type == TokenType.IDENT) {
            return this.getIdent();
        }
        if (this.isIdentifierName()) {
            final long identToken = Token.recast(this.token, TokenType.IDENT);
            final String ident = (String)this.getValue(identToken);
            this.next();
            return this.createIdentNode(identToken, this.finish, ident);
        }
        this.expect(TokenType.IDENT);
        return null;
    }
    
    protected final LiteralNode<?> getLiteral() throws ParserException {
        final long literalToken = this.token;
        final Object value = this.getValue();
        this.next();
        LiteralNode<?> node = null;
        if (value == null) {
            node = LiteralNode.newInstance(literalToken, this.finish);
        }
        else if (value instanceof Number) {
            node = LiteralNode.newInstance(literalToken, this.finish, (Number)value);
        }
        else if (value instanceof String) {
            node = LiteralNode.newInstance(literalToken, this.finish, (String)value);
        }
        else if (value instanceof Lexer.LexerToken) {
            if (value instanceof Lexer.RegexToken) {
                final Lexer.RegexToken regex = (Lexer.RegexToken)value;
                try {
                    RegExpFactory.validate(regex.getExpression(), regex.getOptions());
                }
                catch (ParserException e) {
                    throw this.error(e.getMessage());
                }
            }
            node = LiteralNode.newInstance(literalToken, this.finish, (Lexer.LexerToken)value);
        }
        else {
            assert false : "unknown type for LiteralNode: " + value.getClass();
        }
        return node;
    }
}
