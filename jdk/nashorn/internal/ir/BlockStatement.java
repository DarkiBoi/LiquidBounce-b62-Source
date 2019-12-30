// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.List;

public class BlockStatement extends Statement
{
    private static final long serialVersionUID = 1L;
    private final Block block;
    
    public BlockStatement(final Block block) {
        this(block.getFirstStatementLineNumber(), block);
    }
    
    public BlockStatement(final int lineNumber, final Block block) {
        super(lineNumber, block.getToken(), block.getFinish());
        this.block = block;
    }
    
    private BlockStatement(final BlockStatement blockStatement, final Block block) {
        super(blockStatement);
        this.block = block;
    }
    
    public static BlockStatement createReplacement(final Statement stmt, final List<Statement> newStmts) {
        return createReplacement(stmt, stmt.getFinish(), newStmts);
    }
    
    public static BlockStatement createReplacement(final Statement stmt, final int finish, final List<Statement> newStmts) {
        return new BlockStatement(stmt.getLineNumber(), new Block(stmt.getToken(), finish, newStmts));
    }
    
    @Override
    public boolean isTerminal() {
        return this.block.isTerminal();
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBlockStatement(this)) {
            return visitor.leaveBlockStatement(this.setBlock((Block)this.block.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        this.block.toString(sb, printType);
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public BlockStatement setBlock(final Block block) {
        if (this.block == block) {
            return this;
        }
        return new BlockStatement(this, block);
    }
}
