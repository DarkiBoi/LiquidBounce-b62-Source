// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class AccessNode extends BaseNode
{
    private static final long serialVersionUID = 1L;
    private final String property;
    
    public AccessNode(final long token, final int finish, final Expression base, final String property) {
        super(token, finish, base, false);
        this.property = property;
    }
    
    private AccessNode(final AccessNode accessNode, final Expression base, final String property, final boolean isFunction, final Type type, final int id) {
        super(accessNode, base, isFunction, type, id);
        this.property = property;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterAccessNode(this)) {
            return visitor.leaveAccessNode(this.setBase((Expression)this.base.accept(visitor)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        final boolean needsParen = this.tokenType().needsParens(this.getBase().tokenType(), true);
        if (printType) {
            this.optimisticTypeToString(sb);
        }
        if (needsParen) {
            sb.append('(');
        }
        this.base.toString(sb, printType);
        if (needsParen) {
            sb.append(')');
        }
        sb.append('.');
        sb.append(this.property);
    }
    
    public String getProperty() {
        return this.property;
    }
    
    private AccessNode setBase(final Expression base) {
        if (this.base == base) {
            return this;
        }
        return new AccessNode(this, base, this.property, this.isFunction(), this.type, this.programPoint);
    }
    
    @Override
    public AccessNode setType(final Type type) {
        if (this.type == type) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, this.isFunction(), type, this.programPoint);
    }
    
    @Override
    public AccessNode setProgramPoint(final int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, this.isFunction(), this.type, programPoint);
    }
    
    @Override
    public AccessNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, true, this.type, this.programPoint);
    }
}
