// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import java.util.Set;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

public abstract class Node implements NodeType
{
    public Node parent;
    
    public abstract int getType();
    
    public final int getType2Bit() {
        return 1 << this.getType();
    }
    
    protected void setChild(final Node tgt) {
    }
    
    protected Node getChild() {
        return null;
    }
    
    public void swap(final Node with) {
        if (this.parent != null) {
            this.parent.setChild(with);
        }
        if (with.parent != null) {
            with.parent.setChild(this);
        }
        final Node tmp = this.parent;
        this.parent = with.parent;
        with.parent = tmp;
    }
    
    public void verifyTree(final Set<Node> set, final WarnCallback warnings) {
        if (!set.contains(this) && this.getChild() != null) {
            set.add(this);
            if (this.getChild().parent != this) {
                warnings.warn("broken link to child: " + this.getAddressName() + " -> " + this.getChild().getAddressName());
            }
            this.getChild().verifyTree(set, warnings);
        }
    }
    
    public abstract String getName();
    
    protected abstract String toString(final int p0);
    
    public String getAddressName() {
        return this.getName() + ":0x" + Integer.toHexString(System.identityHashCode(this));
    }
    
    @Override
    public final String toString() {
        final StringBuilder s = new StringBuilder();
        s.append("<" + this.getAddressName() + " (" + ((this.parent == null) ? "NULL" : this.parent.getAddressName()) + ")>");
        return (Object)s + this.toString(0);
    }
    
    protected static String pad(final Object value, final int level) {
        if (value == null) {
            return "NULL";
        }
        final StringBuilder pad = new StringBuilder("  ");
        for (int i = 0; i < level; ++i) {
            pad.append((CharSequence)pad);
        }
        return value.toString().replace("\n", "\n" + (Object)pad);
    }
    
    public final boolean isInvalidQuantifier() {
        return false;
    }
    
    public final boolean isAllowedInLookBehind() {
        return (this.getType2Bit() & 0x7EF) != 0x0;
    }
    
    public final boolean isSimple() {
        return (this.getType2Bit() & 0x1F) != 0x0;
    }
}
