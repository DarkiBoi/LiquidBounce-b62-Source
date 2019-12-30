// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class PropertyNode extends Node
{
    private static final long serialVersionUID = 1L;
    private final PropertyKey key;
    private final Expression value;
    private final FunctionNode getter;
    private final FunctionNode setter;
    
    public PropertyNode(final long token, final int finish, final PropertyKey key, final Expression value, final FunctionNode getter, final FunctionNode setter) {
        super(token, finish);
        this.key = key;
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }
    
    private PropertyNode(final PropertyNode propertyNode, final PropertyKey key, final Expression value, final FunctionNode getter, final FunctionNode setter) {
        super(propertyNode);
        this.key = key;
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }
    
    public String getKeyName() {
        return this.key.getPropertyName();
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterPropertyNode(this)) {
            return visitor.leavePropertyNode(this.setKey((PropertyKey)((Node)this.key).accept(visitor)).setValue((this.value == null) ? null : ((Expression)this.value.accept(visitor))).setGetter((this.getter == null) ? null : ((FunctionNode)this.getter.accept(visitor))).setSetter((this.setter == null) ? null : ((FunctionNode)this.setter.accept(visitor))));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        if (this.value instanceof FunctionNode && ((FunctionNode)this.value).getIdent() != null) {
            this.value.toString(sb);
        }
        if (this.value != null) {
            ((Node)this.key).toString(sb, printType);
            sb.append(": ");
            this.value.toString(sb, printType);
        }
        if (this.getter != null) {
            sb.append(' ');
            this.getter.toString(sb, printType);
        }
        if (this.setter != null) {
            sb.append(' ');
            this.setter.toString(sb, printType);
        }
    }
    
    public FunctionNode getGetter() {
        return this.getter;
    }
    
    public PropertyNode setGetter(final FunctionNode getter) {
        if (this.getter == getter) {
            return this;
        }
        return new PropertyNode(this, this.key, this.value, getter, this.setter);
    }
    
    public Expression getKey() {
        return (Expression)this.key;
    }
    
    private PropertyNode setKey(final PropertyKey key) {
        if (this.key == key) {
            return this;
        }
        return new PropertyNode(this, key, this.value, this.getter, this.setter);
    }
    
    public FunctionNode getSetter() {
        return this.setter;
    }
    
    public PropertyNode setSetter(final FunctionNode setter) {
        if (this.setter == setter) {
            return this;
        }
        return new PropertyNode(this, this.key, this.value, this.getter, setter);
    }
    
    public Expression getValue() {
        return this.value;
    }
    
    public PropertyNode setValue(final Expression value) {
        if (this.value == value) {
            return this;
        }
        return new PropertyNode(this, this.key, value, this.getter, this.setter);
    }
}
