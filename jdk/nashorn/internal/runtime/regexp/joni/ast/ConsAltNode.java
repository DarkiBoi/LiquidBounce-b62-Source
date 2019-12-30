// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import java.util.Set;

public final class ConsAltNode extends Node
{
    public Node car;
    public ConsAltNode cdr;
    private int type;
    
    private ConsAltNode(final Node car, final ConsAltNode cdr, final int type) {
        this.car = car;
        if (car != null) {
            car.parent = this;
        }
        if ((this.cdr = cdr) != null) {
            cdr.parent = this;
        }
        this.type = type;
    }
    
    public static ConsAltNode newAltNode(final Node left, final ConsAltNode right) {
        return new ConsAltNode(left, right, 9);
    }
    
    public static ConsAltNode newListNode(final Node left, final ConsAltNode right) {
        return new ConsAltNode(left, right, 8);
    }
    
    public static ConsAltNode listAdd(final ConsAltNode listp, final Node x) {
        final ConsAltNode n = newListNode(x, null);
        ConsAltNode list = listp;
        if (list != null) {
            while (list.cdr != null) {
                list = list.cdr;
            }
            list.setCdr(n);
        }
        return n;
    }
    
    public void toListNode() {
        this.type = 8;
    }
    
    public void toAltNode() {
        this.type = 9;
    }
    
    @Override
    public int getType() {
        return this.type;
    }
    
    @Override
    protected void setChild(final Node newChild) {
        this.car = newChild;
    }
    
    @Override
    protected Node getChild() {
        return this.car;
    }
    
    @Override
    public void swap(final Node with) {
        if (this.cdr != null) {
            this.cdr.parent = with;
            if (with instanceof ConsAltNode) {
                final ConsAltNode withCan = (ConsAltNode)with;
                withCan.cdr.parent = this;
                final ConsAltNode tmp = this.cdr;
                this.cdr = withCan.cdr;
                withCan.cdr = tmp;
            }
        }
        super.swap(with);
    }
    
    @Override
    public void verifyTree(final Set<Node> set, final WarnCallback warnings) {
        if (!set.contains(this)) {
            set.add(this);
            if (this.car != null) {
                if (this.car.parent != this) {
                    warnings.warn("broken list car: " + this.getAddressName() + " -> " + this.car.getAddressName());
                }
                this.car.verifyTree(set, warnings);
            }
            if (this.cdr != null) {
                if (this.cdr.parent != this) {
                    warnings.warn("broken list cdr: " + this.getAddressName() + " -> " + this.cdr.getAddressName());
                }
                this.cdr.verifyTree(set, warnings);
            }
        }
    }
    
    public Node setCar(final Node ca) {
        this.car = ca;
        ca.parent = this;
        return this.car;
    }
    
    public ConsAltNode setCdr(final ConsAltNode cd) {
        this.cdr = cd;
        cd.parent = this;
        return this.cdr;
    }
    
    @Override
    public String getName() {
        switch (this.type) {
            case 9: {
                return "Alt";
            }
            case 8: {
                return "List";
            }
            default: {
                throw new InternalException("internal parser error (bug)");
            }
        }
    }
    
    public String toString(final int level) {
        final StringBuilder value = new StringBuilder();
        value.append("\n  car: " + Node.pad(this.car, level + 1));
        value.append("\n  cdr: " + ((this.cdr == null) ? "NULL" : this.cdr.toString()));
        return value.toString();
    }
}
