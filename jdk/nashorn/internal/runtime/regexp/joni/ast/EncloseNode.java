// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;

public final class EncloseNode extends StateNode implements EncloseType
{
    public final int type;
    public int regNum;
    public int option;
    public Node target;
    public int callAddr;
    public int minLength;
    public int maxLength;
    public int charLength;
    public int optCount;
    
    public EncloseNode(final int type) {
        this.type = type;
        this.callAddr = -1;
    }
    
    public EncloseNode() {
        this(1);
    }
    
    public EncloseNode(final int option, final int i) {
        this(2);
        this.option = option;
    }
    
    @Override
    public int getType() {
        return 6;
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
        return "Enclose";
    }
    
    @Override
    public String toString(final int level) {
        final StringBuilder value = new StringBuilder(super.toString(level));
        value.append("\n  type: " + this.typeToString());
        value.append("\n  regNum: " + this.regNum);
        value.append("\n  option: " + Option.toString(this.option));
        value.append("\n  target: " + Node.pad(this.target, level + 1));
        value.append("\n  callAddr: " + this.callAddr);
        value.append("\n  minLength: " + this.minLength);
        value.append("\n  maxLength: " + this.maxLength);
        value.append("\n  charLength: " + this.charLength);
        value.append("\n  optCount: " + this.optCount);
        return value.toString();
    }
    
    public String typeToString() {
        final StringBuilder types = new StringBuilder();
        if (this.isStopBacktrack()) {
            types.append("STOP_BACKTRACK ");
        }
        if (this.isMemory()) {
            types.append("MEMORY ");
        }
        if (this.isOption()) {
            types.append("OPTION ");
        }
        return types.toString();
    }
    
    public boolean isMemory() {
        return (this.type & 0x1) != 0x0;
    }
    
    public boolean isOption() {
        return (this.type & 0x2) != 0x0;
    }
    
    public boolean isStopBacktrack() {
        return (this.type & 0x4) != 0x0;
    }
}
