// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class SplitNode extends LexicalContextStatement implements CompileUnitHolder
{
    private static final long serialVersionUID = 1L;
    private final String name;
    private final CompileUnit compileUnit;
    private final Block body;
    
    public SplitNode(final String name, final Block body, final CompileUnit compileUnit) {
        super(body.getFirstStatementLineNumber(), body.getToken(), body.getFinish());
        this.name = name;
        this.body = body;
        this.compileUnit = compileUnit;
    }
    
    private SplitNode(final SplitNode splitNode, final Block body, final CompileUnit compileUnit) {
        super(splitNode);
        this.name = splitNode.name;
        this.body = body;
        this.compileUnit = compileUnit;
    }
    
    public Block getBody() {
        return this.body;
    }
    
    private SplitNode setBody(final LexicalContext lc, final Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SplitNode(this, body, this.compileUnit));
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterSplitNode(this)) {
            return visitor.leaveSplitNode(this.setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("<split>(");
        sb.append(this.compileUnit.getClass().getSimpleName());
        sb.append(") ");
        this.body.toString(sb, printType);
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public CompileUnit getCompileUnit() {
        return this.compileUnit;
    }
    
    public SplitNode setCompileUnit(final LexicalContext lc, final CompileUnit compileUnit) {
        if (this.compileUnit == compileUnit) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SplitNode(this, this.body, compileUnit));
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        throw new NotSerializableException(this.getClass().getName());
    }
}
