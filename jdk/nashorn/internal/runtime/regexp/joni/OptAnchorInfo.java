// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.constants.AnchorType;

final class OptAnchorInfo implements AnchorType
{
    int leftAnchor;
    int rightAnchor;
    
    void clear() {
        final int n = 0;
        this.rightAnchor = n;
        this.leftAnchor = n;
    }
    
    void copy(final OptAnchorInfo other) {
        this.leftAnchor = other.leftAnchor;
        this.rightAnchor = other.rightAnchor;
    }
    
    void concat(final OptAnchorInfo left, final OptAnchorInfo right, final int leftLength, final int rightLength) {
        this.leftAnchor = left.leftAnchor;
        if (leftLength == 0) {
            this.leftAnchor |= right.leftAnchor;
        }
        this.rightAnchor = right.rightAnchor;
        if (rightLength == 0) {
            this.rightAnchor |= left.rightAnchor;
        }
    }
    
    boolean isSet(final int anchor) {
        return (this.leftAnchor & anchor) != 0x0 || (this.rightAnchor & anchor) != 0x0;
    }
    
    void add(final int anchor) {
        if (isLeftAnchor(anchor)) {
            this.leftAnchor |= anchor;
        }
        else {
            this.rightAnchor |= anchor;
        }
    }
    
    void remove(final int anchor) {
        if (isLeftAnchor(anchor)) {
            this.leftAnchor &= ~anchor;
        }
        else {
            this.rightAnchor &= ~anchor;
        }
    }
    
    void altMerge(final OptAnchorInfo other) {
        this.leftAnchor &= other.leftAnchor;
        this.rightAnchor &= other.rightAnchor;
    }
    
    static boolean isLeftAnchor(final int anchor) {
        return anchor != 8 && anchor != 16 && anchor != 32 && anchor != 1024 && anchor != 2048;
    }
    
    static String anchorToString(final int anchor) {
        final StringBuffer s = new StringBuffer("[");
        if ((anchor & 0x1) != 0x0) {
            s.append("begin-buf ");
        }
        if ((anchor & 0x2) != 0x0) {
            s.append("begin-line ");
        }
        if ((anchor & 0x4) != 0x0) {
            s.append("begin-pos ");
        }
        if ((anchor & 0x8) != 0x0) {
            s.append("end-buf ");
        }
        if ((anchor & 0x10) != 0x0) {
            s.append("semi-end-buf ");
        }
        if ((anchor & 0x20) != 0x0) {
            s.append("end-line ");
        }
        if ((anchor & 0x4000) != 0x0) {
            s.append("anychar-star ");
        }
        if ((anchor & 0x8000) != 0x0) {
            s.append("anychar-star-pl ");
        }
        s.append("]");
        return s.toString();
    }
}
