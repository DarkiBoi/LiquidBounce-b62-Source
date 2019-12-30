// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Collection;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Deque;

public class BlockLexicalContext extends LexicalContext
{
    private final Deque<List<Statement>> sstack;
    protected Statement lastStatement;
    
    public BlockLexicalContext() {
        this.sstack = new ArrayDeque<List<Statement>>();
    }
    
    @Override
    public <T extends LexicalContextNode> T push(final T node) {
        final T pushed = super.push(node);
        if (node instanceof Block) {
            this.sstack.push(new ArrayList<Statement>());
        }
        return pushed;
    }
    
    protected List<Statement> popStatements() {
        return this.sstack.pop();
    }
    
    protected Block afterSetStatements(final Block block) {
        return block;
    }
    
    @Override
    public <T extends Node> T pop(final T node) {
        T expected = node;
        if (node instanceof Block) {
            final List<Statement> newStatements = this.popStatements();
            expected = (T)((Block)node).setStatements(this, newStatements);
            expected = (T)this.afterSetStatements((Block)expected);
            if (!this.sstack.isEmpty()) {
                this.lastStatement = lastStatement(this.sstack.peek());
            }
        }
        return super.pop(expected);
    }
    
    public void appendStatement(final Statement statement) {
        assert statement != null;
        this.sstack.peek().add(statement);
        this.lastStatement = statement;
    }
    
    public Node prependStatement(final Statement statement) {
        assert statement != null;
        this.sstack.peek().add(0, statement);
        return statement;
    }
    
    public void prependStatements(final List<Statement> statements) {
        assert statements != null;
        this.sstack.peek().addAll(0, statements);
    }
    
    public Statement getLastStatement() {
        return this.lastStatement;
    }
    
    private static Statement lastStatement(final List<Statement> statements) {
        final int s = statements.size();
        return (s == 0) ? null : statements.get(s - 1);
    }
}
