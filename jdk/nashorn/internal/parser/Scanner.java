// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

public class Scanner
{
    protected final char[] content;
    protected int position;
    protected final int limit;
    protected int line;
    protected char ch0;
    protected char ch1;
    protected char ch2;
    protected char ch3;
    
    protected Scanner(final char[] content, final int line, final int start, final int length) {
        this.content = content;
        this.position = start;
        this.limit = start + length;
        this.line = line;
        this.reset(this.position);
    }
    
    protected Scanner(final String content) {
        this(content.toCharArray(), 0, 0, content.length());
    }
    
    Scanner(final Scanner scanner, final State state) {
        this.content = scanner.content;
        this.position = state.position;
        this.limit = state.limit;
        this.line = state.line;
        this.reset(this.position);
    }
    
    State saveState() {
        return new State(this.position, this.limit, this.line);
    }
    
    void restoreState(final State state) {
        this.position = state.position;
        this.line = state.line;
        this.reset(this.position);
    }
    
    protected final boolean atEOF() {
        return this.position == this.limit;
    }
    
    protected final char charAt(final int i) {
        return (i < this.limit) ? this.content[i] : '\0';
    }
    
    protected final void reset(final int i) {
        this.ch0 = this.charAt(i);
        this.ch1 = this.charAt(i + 1);
        this.ch2 = this.charAt(i + 2);
        this.ch3 = this.charAt(i + 3);
        this.position = ((i < this.limit) ? i : this.limit);
    }
    
    protected final void skip(final int n) {
        if (n == 1 && !this.atEOF()) {
            this.ch0 = this.ch1;
            this.ch1 = this.ch2;
            this.ch2 = this.ch3;
            this.ch3 = this.charAt(this.position + 4);
            ++this.position;
        }
        else if (n != 0) {
            this.reset(this.position + n);
        }
    }
    
    static class State
    {
        public final int position;
        public int limit;
        public final int line;
        
        State(final int position, final int limit, final int line) {
            this.position = position;
            this.limit = limit;
            this.line = line;
        }
        
        void setLimit(final int limit) {
            this.limit = limit;
        }
        
        boolean isEmpty() {
            return this.position == this.limit;
        }
    }
}
