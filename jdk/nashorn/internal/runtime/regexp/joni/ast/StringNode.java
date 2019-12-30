// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;

public final class StringNode extends Node implements StringType
{
    private static final int NODE_STR_MARGIN = 16;
    private static final int NODE_STR_BUF_SIZE = 24;
    public static final StringNode EMPTY;
    public char[] chars;
    public int p;
    public int end;
    public int flag;
    
    public StringNode() {
        this.chars = new char[24];
    }
    
    public StringNode(final char[] chars, final int p, final int end) {
        this.chars = chars;
        this.p = p;
        this.end = end;
        this.setShared();
    }
    
    public StringNode(final char c) {
        this();
        this.chars[this.end++] = c;
    }
    
    public void ensure(final int ahead) {
        final int len = this.end - this.p + ahead;
        if (len >= this.chars.length) {
            final char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.p, tmp, 0, this.end - this.p);
            this.chars = tmp;
        }
    }
    
    private void modifyEnsure(final int ahead) {
        if (this.isShared()) {
            final int len = this.end - this.p + ahead;
            final char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.p, tmp, 0, this.end - this.p);
            this.chars = tmp;
            this.end -= this.p;
            this.p = 0;
            this.clearShared();
        }
        else {
            this.ensure(ahead);
        }
    }
    
    @Override
    public int getType() {
        return 0;
    }
    
    @Override
    public String getName() {
        return "String";
    }
    
    public String toString(final int level) {
        final StringBuilder value = new StringBuilder();
        value.append("\n  bytes: '");
        for (int i = this.p; i < this.end; ++i) {
            if (this.chars[i] >= ' ' && this.chars[i] < '\u007f') {
                value.append(this.chars[i]);
            }
            else {
                value.append(String.format("[0x%04x]", (int)this.chars[i]));
            }
        }
        value.append("'");
        return value.toString();
    }
    
    public int length() {
        return this.end - this.p;
    }
    
    public StringNode splitLastChar() {
        StringNode n = null;
        if (this.end > this.p) {
            final int prev = EncodingHelper.prevCharHead(this.p, this.end);
            if (prev != -1 && prev > this.p) {
                n = new StringNode(this.chars, prev, this.end);
                if (this.isRaw()) {
                    n.setRaw();
                }
                this.end = prev;
            }
        }
        return n;
    }
    
    public boolean canBeSplit() {
        return this.end > this.p && 1 < this.end - this.p;
    }
    
    public void set(final char[] chars, final int p, final int end) {
        this.chars = chars;
        this.p = p;
        this.end = end;
        this.setShared();
    }
    
    public void cat(final char[] cat, final int catP, final int catEnd) {
        final int len = catEnd - catP;
        this.modifyEnsure(len);
        System.arraycopy(cat, catP, this.chars, this.end, len);
        this.end += len;
    }
    
    public void cat(final char c) {
        this.modifyEnsure(1);
        this.chars[this.end++] = c;
    }
    
    public void catCode(final int code) {
        this.cat((char)code);
    }
    
    public void clear() {
        if (this.chars.length > 24) {
            this.chars = new char[24];
        }
        this.flag = 0;
        final int n = 0;
        this.end = n;
        this.p = n;
    }
    
    public void setRaw() {
        this.flag |= 0x1;
    }
    
    public void clearRaw() {
        this.flag &= 0xFFFFFFFE;
    }
    
    public boolean isRaw() {
        return (this.flag & 0x1) != 0x0;
    }
    
    public void setAmbig() {
        this.flag |= 0x2;
    }
    
    public void clearAmbig() {
        this.flag &= 0xFFFFFFFD;
    }
    
    public boolean isAmbig() {
        return (this.flag & 0x2) != 0x0;
    }
    
    public void setDontGetOptInfo() {
        this.flag |= 0x4;
    }
    
    public void clearDontGetOptInfo() {
        this.flag &= 0xFFFFFFFB;
    }
    
    public boolean isDontGetOptInfo() {
        return (this.flag & 0x4) != 0x0;
    }
    
    public void setShared() {
        this.flag |= 0x8;
    }
    
    public void clearShared() {
        this.flag &= 0xFFFFFFF7;
    }
    
    public boolean isShared() {
        return (this.flag & 0x8) != 0x0;
    }
    
    static {
        EMPTY = new StringNode(null, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
