// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class IndexNode extends BaseNode
{
    private static final long serialVersionUID = 1L;
    private final Expression index;
    
    public IndexNode(final long token, final int finish, final Expression base, final Expression index) {
        super(token, finish, base, false);
        this.index = index;
    }
    
    private IndexNode(final IndexNode indexNode, final Expression base, final Expression index, final boolean isFunction, final Type type, final int programPoint) {
        super(indexNode, base, isFunction, type, programPoint);
        this.index = index;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIndexNode(this)) {
            return visitor.leaveIndexNode(this.setBase((Expression)this.base.accept(visitor)).setIndex((Expression)this.index.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        final boolean needsParen = this.tokenType().needsParens(this.base.tokenType(), true);
        if (needsParen) {
            sb.append('(');
        }
        if (printType) {
            this.optimisticTypeToString(sb);
        }
        this.base.toString(sb, printType);
        if (needsParen) {
            sb.append(')');
        }
        sb.append('[');
        this.index.toString(sb, printType);
        sb.append(']');
    }
    
    public Expression getIndex() {
        return this.index;
    }
    
    private IndexNode setBase(final Expression base) {
        if (this.base == base) {
            return this;
        }
        return new IndexNode(this, base, this.index, this.isFunction(), this.type, this.programPoint);
    }
    
    public IndexNode setIndex(final Expression index) {
        if (this.index == index) {
            return this;
        }
        return new IndexNode(this, this.base, index, this.isFunction(), this.type, this.programPoint);
    }
    
    @Override
    public IndexNode setType(final Type type) {
        if (this.type == type) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, this.isFunction(), type, this.programPoint);
    }
    
    @Override
    public IndexNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, true, this.type, this.programPoint);
    }
    
    @Override
    public IndexNode setProgramPoint(final int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, this.isFunction(), this.type, programPoint);
    }
}
