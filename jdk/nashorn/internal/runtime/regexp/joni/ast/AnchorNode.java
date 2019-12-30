// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.constants.AnchorType;

public final class AnchorNode extends Node implements AnchorType
{
    public int type;
    public Node target;
    public int charLength;
    
    public AnchorNode(final int type) {
        this.type = type;
        this.charLength = -1;
    }
    
    @Override
    public int getType() {
        return 7;
    }
    
    @Override
    protected void setChild(final Node newChild) {
        this.target = newChild;
    }
    
    @Override
    protected Node getChild() {
        return this.target;
    }
    
    public void setTarget(final Node tgt) {
        this.target = tgt;
        tgt.parent = this;
    }
    
    @Override
    public String getName() {
        return "Anchor";
    }
    
    public String toString(final int level) {
        final StringBuilder value = new StringBuilder();
        value.append("\n  type: " + this.typeToString());
        value.append("\n  target: " + Node.pad(this.target, level + 1));
        return value.toString();
    }
    
    public String typeToString() {
        final StringBuilder sb = new StringBuilder();
        if (this.isType(1)) {
            sb.append("BEGIN_BUF ");
        }
        if (this.isType(2)) {
            sb.append("BEGIN_LINE ");
        }
        if (this.isType(4)) {
            sb.append("BEGIN_POSITION ");
        }
        if (this.isType(8)) {
            sb.append("END_BUF ");
        }
        if (this.isType(16)) {
            sb.append("SEMI_END_BUF ");
        }
        if (this.isType(32)) {
            sb.append("END_LINE ");
        }
        if (this.isType(64)) {
            sb.append("WORD_BOUND ");
        }
        if (this.isType(128)) {
            sb.append("NOT_WORD_BOUND ");
        }
        if (this.isType(256)) {
            sb.append("WORD_BEGIN ");
        }
        if (this.isType(512)) {
            sb.append("WORD_END ");
        }
        if (this.isType(1024)) {
            sb.append("PREC_READ ");
        }
        if (this.isType(2048)) {
            sb.append("PREC_READ_NOT ");
        }
        if (this.isType(4096)) {
            sb.append("LOOK_BEHIND ");
        }
        if (this.isType(8192)) {
            sb.append("LOOK_BEHIND_NOT ");
        }
        if (this.isType(16384)) {
            sb.append("ANYCHAR_STAR ");
        }
        if (this.isType(32768)) {
            sb.append("ANYCHAR_STAR_ML ");
        }
        return sb.toString();
    }
    
    private boolean isType(final int t) {
        return (this.type & t) != 0x0;
    }
}
