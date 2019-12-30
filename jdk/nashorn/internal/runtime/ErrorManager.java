// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.parser.Token;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ErrorManager
{
    private final PrintWriter writer;
    private int errors;
    private int warnings;
    private int limit;
    private boolean warningsAsErrors;
    
    public ErrorManager() {
        this(new PrintWriter(System.err, true));
    }
    
    public ErrorManager(final PrintWriter writer) {
        this.writer = writer;
        this.limit = 100;
        this.warningsAsErrors = false;
    }
    
    private void checkLimit() {
        int count = this.errors;
        if (this.warningsAsErrors) {
            count += this.warnings;
        }
        if (this.limit != 0 && count > this.limit) {
            throw ECMAErrors.rangeError("too.many.errors", Integer.toString(this.limit));
        }
    }
    
    public static String format(final String message, final Source source, final int line, final int column, final long token) {
        final String eoln = System.lineSeparator();
        final int position = Token.descPosition(token);
        final StringBuilder sb = new StringBuilder();
        sb.append(source.getName()).append(':').append(line).append(':').append(column).append(' ').append(message).append(eoln);
        final String sourceLine = source.getSourceLine(position);
        sb.append(sourceLine).append(eoln);
        for (int i = 0; i < column; ++i) {
            if (i < sourceLine.length() && sourceLine.charAt(i) == '\t') {
                sb.append('\t');
            }
            else {
                sb.append(' ');
            }
        }
        sb.append('^');
        return sb.toString();
    }
    
    public void error(final ParserException e) {
        this.error(e.getMessage());
    }
    
    public void error(final String message) {
        this.writer.println(message);
        this.writer.flush();
        ++this.errors;
        this.checkLimit();
    }
    
    public void warning(final ParserException e) {
        this.warning(e.getMessage());
    }
    
    public void warning(final String message) {
        this.writer.println(message);
        this.writer.flush();
        ++this.warnings;
        this.checkLimit();
    }
    
    public boolean hasErrors() {
        return this.errors != 0;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public void setLimit(final int limit) {
        this.limit = limit;
    }
    
    public boolean isWarningsAsErrors() {
        return this.warningsAsErrors;
    }
    
    public void setWarningsAsErrors(final boolean warningsAsErrors) {
        this.warningsAsErrors = warningsAsErrors;
    }
    
    public int getNumberOfErrors() {
        return this.errors;
    }
    
    public int getNumberOfWarnings() {
        return this.warnings;
    }
    
    void reset() {
        this.warnings = 0;
        this.errors = 0;
    }
}
