// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Comparator;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.Label;
import java.util.Map;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class Block extends Node implements BreakableNode, Terminal, Flags<Block>
{
    private static final long serialVersionUID = 1L;
    protected final List<Statement> statements;
    protected final Map<String, Symbol> symbols;
    private final Label entryLabel;
    private final Label breakLabel;
    protected final int flags;
    private final LocalVariableConversion conversion;
    public static final int NEEDS_SCOPE = 1;
    public static final int IS_TERMINAL = 4;
    public static final int IS_GLOBAL_SCOPE = 8;
    
    public Block(final long token, final int finish, final Statement... statements) {
        super(token, finish);
        this.statements = Arrays.asList(statements);
        this.symbols = new LinkedHashMap<String, Symbol>();
        this.entryLabel = new Label("block_entry");
        this.breakLabel = new Label("block_break");
        final int len = statements.length;
        this.flags = ((len > 0 && statements[len - 1].hasTerminalFlags()) ? 4 : 0);
        this.conversion = null;
    }
    
    public Block(final long token, final int finish, final List<Statement> statements) {
        this(token, finish, (Statement[])statements.toArray(new Statement[statements.size()]));
    }
    
    private Block(final Block block, final int finish, final List<Statement> statements, final int flags, final Map<String, Symbol> symbols, final LocalVariableConversion conversion) {
        super(block);
        this.statements = statements;
        this.flags = flags;
        this.symbols = new LinkedHashMap<String, Symbol>(symbols);
        this.entryLabel = new Label(block.entryLabel);
        this.breakLabel = new Label(block.breakLabel);
        this.finish = finish;
        this.conversion = conversion;
    }
    
    public boolean isGlobalScope() {
        return this.getFlag(8);
    }
    
    public boolean hasSymbols() {
        return !this.symbols.isEmpty();
    }
    
    public Block replaceSymbols(final LexicalContext lc, final Map<Symbol, Symbol> replacements) {
        if (this.symbols.isEmpty()) {
            return this;
        }
        final LinkedHashMap<String, Symbol> newSymbols = new LinkedHashMap<String, Symbol>(this.symbols);
        for (final Map.Entry<String, Symbol> entry : newSymbols.entrySet()) {
            final Symbol newSymbol = replacements.get(entry.getValue());
            assert newSymbol != null : "Missing replacement for " + entry.getKey();
            entry.setValue(newSymbol);
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, newSymbols, this.conversion));
    }
    
    public Block copyWithNewSymbols() {
        return new Block(this, this.finish, this.statements, this.flags, new LinkedHashMap<String, Symbol>(this.symbols), this.conversion);
    }
    
    @Override
    public Node ensureUniqueLabels(final LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, this.symbols, this.conversion));
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBlock(this)) {
            return visitor.leaveBlock(this.setStatements(lc, Node.accept(visitor, this.statements)));
        }
        return this;
    }
    
    public List<Symbol> getSymbols() {
        return this.symbols.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList((List<? extends Symbol>)new ArrayList<Symbol>(this.symbols.values()));
    }
    
    public Symbol getExistingSymbol(final String name) {
        return this.symbols.get(name);
    }
    
    public boolean isCatchBlock() {
        return this.statements.size() == 1 && this.statements.get(0) instanceof CatchNode;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        for (final Node statement : this.statements) {
            statement.toString(sb, printType);
            sb.append(';');
        }
    }
    
    public boolean printSymbols(final PrintWriter stream) {
        final List<Symbol> values = new ArrayList<Symbol>(this.symbols.values());
        Collections.sort(values, new Comparator<Symbol>() {
            @Override
            public int compare(final Symbol s0, final Symbol s1) {
                return s0.getName().compareTo(s1.getName());
            }
        });
        for (final Symbol symbol : values) {
            symbol.print(stream);
        }
        return !values.isEmpty();
    }
    
    public Block setIsTerminal(final LexicalContext lc, final boolean isTerminal) {
        return isTerminal ? this.setFlag(lc, 4) : this.clearFlag(lc, 4);
    }
    
    @Override
    public int getFlags() {
        return this.flags;
    }
    
    @Override
    public boolean isTerminal() {
        return this.getFlag(4);
    }
    
    public Label getEntryLabel() {
        return this.entryLabel;
    }
    
    @Override
    public Label getBreakLabel() {
        return this.breakLabel;
    }
    
    @Override
    public Block setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, this.symbols, conversion));
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
    
    public List<Statement> getStatements() {
        return Collections.unmodifiableList((List<? extends Statement>)this.statements);
    }
    
    public int getStatementCount() {
        return this.statements.size();
    }
    
    public int getFirstStatementLineNumber() {
        if (this.statements == null || this.statements.isEmpty()) {
            return -1;
        }
        return this.statements.get(0).getLineNumber();
    }
    
    public Statement getLastStatement() {
        return this.statements.isEmpty() ? null : this.statements.get(this.statements.size() - 1);
    }
    
    public Block setStatements(final LexicalContext lc, final List<Statement> statements) {
        if (this.statements == statements) {
            return this;
        }
        int lastFinish = 0;
        if (!statements.isEmpty()) {
            lastFinish = statements.get(statements.size() - 1).getFinish();
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, Math.max(this.finish, lastFinish), statements, this.flags, this.symbols, this.conversion));
    }
    
    public void putSymbol(final Symbol symbol) {
        this.symbols.put(symbol.getName(), symbol);
    }
    
    public boolean needsScope() {
        return (this.flags & 0x1) == 0x1;
    }
    
    @Override
    public Block setFlags(final LexicalContext lc, final int flags) {
        if (this.flags == flags) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, flags, this.symbols, this.conversion));
    }
    
    @Override
    public Block clearFlag(final LexicalContext lc, final int flag) {
        return this.setFlags(lc, this.flags & ~flag);
    }
    
    @Override
    public Block setFlag(final LexicalContext lc, final int flag) {
        return this.setFlags(lc, this.flags | flag);
    }
    
    @Override
    public boolean getFlag(final int flag) {
        return (this.flags & flag) == flag;
    }
    
    public Block setNeedsScope(final LexicalContext lc) {
        if (this.needsScope()) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags | 0x1, this.symbols, this.conversion));
    }
    
    public int nextSlot() {
        int next = 0;
        for (final Symbol symbol : this.getSymbols()) {
            if (symbol.hasSlot()) {
                next += symbol.slotCount();
            }
        }
        return next;
    }
    
    @Override
    public boolean isBreakableWithoutLabel() {
        return false;
    }
    
    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList((List<? extends Label>)Arrays.asList(this.entryLabel, this.breakLabel));
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return LexicalContextNode.Acceptor.accept(this, visitor);
    }
}
