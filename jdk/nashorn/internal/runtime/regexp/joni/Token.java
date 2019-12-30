// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType;

final class Token
{
    TokenType type;
    boolean escaped;
    int backP;
    private int INT1;
    private int INT2;
    private int INT3;
    private int INT4;
    
    int getC() {
        return this.INT1;
    }
    
    void setC(final int c) {
        this.INT1 = c;
    }
    
    int getCode() {
        return this.INT1;
    }
    
    void setCode(final int code) {
        this.INT1 = code;
    }
    
    int getAnchor() {
        return this.INT1;
    }
    
    void setAnchor(final int anchor) {
        this.INT1 = anchor;
    }
    
    int getRepeatLower() {
        return this.INT1;
    }
    
    void setRepeatLower(final int lower) {
        this.INT1 = lower;
    }
    
    int getRepeatUpper() {
        return this.INT2;
    }
    
    void setRepeatUpper(final int upper) {
        this.INT2 = upper;
    }
    
    boolean getRepeatGreedy() {
        return this.INT3 != 0;
    }
    
    void setRepeatGreedy(final boolean greedy) {
        this.INT3 = (greedy ? 1 : 0);
    }
    
    boolean getRepeatPossessive() {
        return this.INT4 != 0;
    }
    
    void setRepeatPossessive(final boolean possessive) {
        this.INT4 = (possessive ? 1 : 0);
    }
    
    int getBackrefRef() {
        return this.INT2;
    }
    
    void setBackrefRef(final int ref1) {
        this.INT2 = ref1;
    }
    
    int getPropCType() {
        return this.INT1;
    }
    
    void setPropCType(final int ctype) {
        this.INT1 = ctype;
    }
    
    boolean getPropNot() {
        return this.INT2 != 0;
    }
    
    void setPropNot(final boolean not) {
        this.INT2 = (not ? 1 : 0);
    }
}
