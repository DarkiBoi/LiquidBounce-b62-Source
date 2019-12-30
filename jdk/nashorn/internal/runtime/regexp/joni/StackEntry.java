// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

final class StackEntry
{
    int type;
    private int E1;
    private int E2;
    private int E3;
    private int E4;
    
    void setStatePCode(final int pcode) {
        this.E1 = pcode;
    }
    
    int getStatePCode() {
        return this.E1;
    }
    
    void setStatePStr(final int pstr) {
        this.E2 = pstr;
    }
    
    int getStatePStr() {
        return this.E2;
    }
    
    void setStatePStrPrev(final int pstrPrev) {
        this.E3 = pstrPrev;
    }
    
    int getStatePStrPrev() {
        return this.E3;
    }
    
    void setStateCheck(final int check) {
        this.E4 = check;
    }
    
    int getStateCheck() {
        return this.E4;
    }
    
    void setRepeatCount(final int count) {
        this.E1 = count;
    }
    
    int getRepeatCount() {
        return this.E1;
    }
    
    void decreaseRepeatCount() {
        --this.E1;
    }
    
    void increaseRepeatCount() {
        ++this.E1;
    }
    
    void setRepeatPCode(final int pcode) {
        this.E2 = pcode;
    }
    
    int getRepeatPCode() {
        return this.E2;
    }
    
    void setRepeatNum(final int num) {
        this.E3 = num;
    }
    
    int getRepeatNum() {
        return this.E3;
    }
    
    void setSi(final int si) {
        this.E1 = si;
    }
    
    int getSi() {
        return this.E1;
    }
    
    void setMemNum(final int num) {
        this.E1 = num;
    }
    
    int getMemNum() {
        return this.E1;
    }
    
    void setMemPstr(final int pstr) {
        this.E2 = pstr;
    }
    
    int getMemPStr() {
        return this.E2;
    }
    
    void setMemStart(final int start) {
        this.E3 = start;
    }
    
    int getMemStart() {
        return this.E3;
    }
    
    void setMemEnd(final int end) {
        this.E4 = end;
    }
    
    int getMemEnd() {
        return this.E4;
    }
    
    void setNullCheckNum(final int num) {
        this.E1 = num;
    }
    
    int getNullCheckNum() {
        return this.E1;
    }
    
    void setNullCheckPStr(final int pstr) {
        this.E2 = pstr;
    }
    
    int getNullCheckPStr() {
        return this.E2;
    }
    
    void setCallFrameRetAddr(final int addr) {
        this.E1 = addr;
    }
    
    int getCallFrameRetAddr() {
        return this.E1;
    }
    
    void setCallFrameNum(final int num) {
        this.E2 = num;
    }
    
    int getCallFrameNum() {
        return this.E2;
    }
    
    void setCallFramePStr(final int pstr) {
        this.E3 = pstr;
    }
    
    int getCallFramePStr() {
        return this.E3;
    }
}
