// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

public abstract class Statement extends Node implements Terminal
{
    private static final long serialVersionUID = 1L;
    private final int lineNumber;
    
    public Statement(final int lineNumber, final long token, final int finish) {
        super(token, finish);
        this.lineNumber = lineNumber;
    }
    
    protected Statement(final int lineNumber, final long token, final int start, final int finish) {
        super(token, start, finish);
        this.lineNumber = lineNumber;
    }
    
    protected Statement(final Statement node) {
        super(node);
        this.lineNumber = node.lineNumber;
    }
    
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    @Override
    public boolean isTerminal() {
        return false;
    }
    
    public boolean hasGoto() {
        return false;
    }
    
    public final boolean hasTerminalFlags() {
        return this.isTerminal() || this.hasGoto();
    }
}
