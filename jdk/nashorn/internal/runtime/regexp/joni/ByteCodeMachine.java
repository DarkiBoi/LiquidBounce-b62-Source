// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

class ByteCodeMachine extends StackMachine
{
    private int bestLen;
    private int s;
    private int range;
    private int sprev;
    private int sstart;
    private int sbegin;
    private final int[] code;
    private int ip;
    
    ByteCodeMachine(final Regex regex, final char[] chars, final int p, final int end) {
        super(regex, chars, p, end);
        this.s = 0;
        this.code = regex.code;
    }
    
    private boolean stringCmpIC(final int caseFlodFlag, final int s1p, final IntHolder ps2, final int mbLen, final int textEnd) {
        int s1 = s1p;
        int s2 = ps2.value;
        final int end1 = s1 + mbLen;
        while (s1 < end1) {
            final char c1 = EncodingHelper.toLowerCase(this.chars[s1++]);
            final char c2 = EncodingHelper.toLowerCase(this.chars[s2++]);
            if (c1 != c2) {
                return false;
            }
        }
        ps2.value = s2;
        return true;
    }
    
    private void debugMatchBegin() {
        Config.log.println("match_at: str: " + this.str + ", end: " + this.end + ", start: " + this.sstart + ", sprev: " + this.sprev);
        Config.log.println("size: " + (this.end - this.str) + ", start offset: " + (this.sstart - this.str));
    }
    
    private void debugMatchLoop() {
    }
    
    @Override
    protected final int matchAt(final int r, final int ss, final int sp) {
        this.range = r;
        this.sstart = ss;
        this.sprev = sp;
        this.stk = 0;
        this.ip = 0;
        this.init();
        this.bestLen = -1;
        this.s = ss;
        final int[] c = this.code;
        while (true) {
            this.sbegin = this.s;
            switch (c[this.ip++]) {
                case 1: {
                    if (this.opEnd()) {
                        return this.finish();
                    }
                    continue;
                }
                case 2: {
                    this.opExact1();
                    continue;
                }
                case 3: {
                    this.opExact2();
                    continue;
                }
                case 4: {
                    this.opExact3();
                    continue;
                }
                case 5: {
                    this.opExact4();
                    continue;
                }
                case 6: {
                    this.opExact5();
                    continue;
                }
                case 7: {
                    this.opExactN();
                    continue;
                }
                case 14: {
                    this.opExact1IC();
                    continue;
                }
                case 15: {
                    this.opExactNIC();
                    continue;
                }
                case 16: {
                    this.opCClass();
                    continue;
                }
                case 17: {
                    this.opCClassMB();
                    continue;
                }
                case 18: {
                    this.opCClassMIX();
                    continue;
                }
                case 19: {
                    this.opCClassNot();
                    continue;
                }
                case 20: {
                    this.opCClassMBNot();
                    continue;
                }
                case 21: {
                    this.opCClassMIXNot();
                    continue;
                }
                case 22: {
                    this.opCClassNode();
                    continue;
                }
                case 23: {
                    this.opAnyChar();
                    continue;
                }
                case 24: {
                    this.opAnyCharML();
                    continue;
                }
                case 25: {
                    this.opAnyCharStar();
                    continue;
                }
                case 26: {
                    this.opAnyCharMLStar();
                    continue;
                }
                case 27: {
                    this.opAnyCharStarPeekNext();
                    continue;
                }
                case 28: {
                    this.opAnyCharMLStarPeekNext();
                    continue;
                }
                case 29: {
                    this.opWord();
                    continue;
                }
                case 30: {
                    this.opNotWord();
                    continue;
                }
                case 31: {
                    this.opWordBound();
                    continue;
                }
                case 32: {
                    this.opNotWordBound();
                    continue;
                }
                case 33: {
                    this.opWordBegin();
                    continue;
                }
                case 34: {
                    this.opWordEnd();
                    continue;
                }
                case 35: {
                    this.opBeginBuf();
                    continue;
                }
                case 36: {
                    this.opEndBuf();
                    continue;
                }
                case 37: {
                    this.opBeginLine();
                    continue;
                }
                case 38: {
                    this.opEndLine();
                    continue;
                }
                case 39: {
                    this.opSemiEndBuf();
                    continue;
                }
                case 40: {
                    this.opBeginPosition();
                    continue;
                }
                case 49: {
                    this.opMemoryStartPush();
                    continue;
                }
                case 48: {
                    this.opMemoryStart();
                    continue;
                }
                case 50: {
                    this.opMemoryEndPush();
                    continue;
                }
                case 52: {
                    this.opMemoryEnd();
                    continue;
                }
                case 51: {
                    this.opMemoryEndPushRec();
                    continue;
                }
                case 53: {
                    this.opMemoryEndRec();
                    continue;
                }
                case 41: {
                    this.opBackRef1();
                    continue;
                }
                case 42: {
                    this.opBackRef2();
                    continue;
                }
                case 43: {
                    this.opBackRefN();
                    continue;
                }
                case 44: {
                    this.opBackRefNIC();
                    continue;
                }
                case 45: {
                    this.opBackRefMulti();
                    continue;
                }
                case 46: {
                    this.opBackRefMultiIC();
                    continue;
                }
                case 47: {
                    this.opBackRefAtLevel();
                    continue;
                }
                case 66: {
                    this.opNullCheckStart();
                    continue;
                }
                case 67: {
                    this.opNullCheckEnd();
                    continue;
                }
                case 68: {
                    this.opNullCheckEndMemST();
                    continue;
                }
                case 55: {
                    this.opJump();
                    continue;
                }
                case 56: {
                    this.opPush();
                    continue;
                }
                case 57: {
                    this.opPop();
                    continue;
                }
                case 58: {
                    this.opPushOrJumpExact1();
                    continue;
                }
                case 59: {
                    this.opPushIfPeekNext();
                    continue;
                }
                case 60: {
                    this.opRepeat();
                    continue;
                }
                case 61: {
                    this.opRepeatNG();
                    continue;
                }
                case 62: {
                    this.opRepeatInc();
                    continue;
                }
                case 64: {
                    this.opRepeatIncSG();
                    continue;
                }
                case 63: {
                    this.opRepeatIncNG();
                    continue;
                }
                case 65: {
                    this.opRepeatIncNGSG();
                    continue;
                }
                case 70: {
                    this.opPushPos();
                    continue;
                }
                case 71: {
                    this.opPopPos();
                    continue;
                }
                case 72: {
                    this.opPushPosNot();
                    continue;
                }
                case 73: {
                    this.opFailPos();
                    continue;
                }
                case 74: {
                    this.opPushStopBT();
                    continue;
                }
                case 75: {
                    this.opPopStopBT();
                    continue;
                }
                case 76: {
                    this.opLookBehind();
                    continue;
                }
                case 77: {
                    this.opPushLookBehindNot();
                    continue;
                }
                case 78: {
                    this.opFailLookBehindNot();
                    continue;
                }
                case 0: {
                    return this.finish();
                }
                case 54: {
                    this.opFail();
                    continue;
                }
                default: {
                    throw new InternalException("undefined bytecode (bug)");
                }
            }
        }
    }
    
    private boolean opEnd() {
        final int n = this.s - this.sstart;
        if (n > this.bestLen) {
            if (Option.isFindLongest(this.regex.options)) {
                if (n <= this.msaBestLen) {
                    return this.endBestLength();
                }
                this.msaBestLen = n;
                this.msaBestS = this.sstart;
            }
            this.bestLen = n;
            final Region region = this.msaRegion;
            if (region != null) {
                region.beg[0] = (this.msaBegin = this.sstart - this.str);
                region.end[0] = (this.msaEnd = this.s - this.str);
                for (int i = 1; i <= this.regex.numMem; ++i) {
                    if (this.repeatStk[this.memEndStk + i] != -1) {
                        region.beg[i] = (BitStatus.bsAt(this.regex.btMemStart, i) ? (this.stack[this.repeatStk[this.memStartStk + i]].getMemPStr() - this.str) : (this.repeatStk[this.memStartStk + i] - this.str));
                        region.end[i] = (BitStatus.bsAt(this.regex.btMemEnd, i) ? this.stack[this.repeatStk[this.memEndStk + i]].getMemPStr() : (this.repeatStk[this.memEndStk + i] - this.str));
                    }
                    else {
                        region.beg[i] = (region.end[i] = -1);
                    }
                }
            }
            else {
                this.msaBegin = this.sstart - this.str;
                this.msaEnd = this.s - this.str;
            }
        }
        else {
            final Region region = this.msaRegion;
            if (region != null) {
                region.clear();
            }
            else {
                final int n2 = 0;
                this.msaEnd = n2;
                this.msaBegin = n2;
            }
        }
        return this.endBestLength();
    }
    
    private boolean endBestLength() {
        if (Option.isFindCondition(this.regex.options)) {
            if (Option.isFindNotEmpty(this.regex.options) && this.s == this.sstart) {
                this.bestLen = -1;
                this.opFail();
                return false;
            }
            if (Option.isFindLongest(this.regex.options) && this.s < this.range) {
                this.opFail();
                return false;
            }
        }
        return true;
    }
    
    private void opExact1() {
        if (this.s >= this.range || this.code[this.ip] != this.chars[this.s++]) {
            this.opFail();
            return;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }
    
    private void opExact2() {
        if (this.s + 2 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        ++this.ip;
        ++this.s;
    }
    
    private void opExact3() {
        if (this.s + 3 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        ++this.ip;
        ++this.s;
    }
    
    private void opExact4() {
        if (this.s + 4 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        ++this.ip;
        ++this.s;
    }
    
    private void opExact5() {
        if (this.s + 5 > this.range) {
            this.opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        ++this.ip;
        ++this.s;
        if (this.code[this.ip] != this.chars[this.s]) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        ++this.ip;
        ++this.s;
    }
    
    private void opExactN() {
        int tlen = this.code[this.ip++];
        if (this.s + tlen > this.range) {
            this.opFail();
            return;
        }
        final char[] bs = this.regex.templates[this.code[this.ip++]];
        int ps = this.code[this.ip++];
        while (tlen-- > 0) {
            if (bs[ps++] != this.chars[this.s++]) {
                this.opFail();
                return;
            }
        }
        this.sprev = this.s - 1;
    }
    
    private void opExact1IC() {
        if (this.s >= this.range || this.code[this.ip] != EncodingHelper.toLowerCase(this.chars[this.s++])) {
            this.opFail();
            return;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }
    
    private void opExactNIC() {
        int tlen = this.code[this.ip++];
        if (this.s + tlen > this.range) {
            this.opFail();
            return;
        }
        final char[] bs = this.regex.templates[this.code[this.ip++]];
        int ps = this.code[this.ip++];
        while (tlen-- > 0) {
            if (bs[ps++] != EncodingHelper.toLowerCase(this.chars[this.s++])) {
                this.opFail();
                return;
            }
        }
        this.sprev = this.s - 1;
    }
    
    private boolean isInBitSet() {
        final int c = this.chars[this.s];
        return c <= 255 && (this.code[this.ip + (c >>> BitSet.ROOM_SHIFT)] & 1 << c) != 0x0;
    }
    
    private void opCClass() {
        if (this.s >= this.range || !this.isInBitSet()) {
            this.opFail();
            return;
        }
        this.ip += 8;
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private boolean isInClassMB() {
        final int tlen = this.code[this.ip++];
        if (this.s >= this.range) {
            return false;
        }
        final int ss = this.s;
        ++this.s;
        final int c = this.chars[ss];
        if (!EncodingHelper.isInCodeRange(this.code, this.ip, c)) {
            return false;
        }
        this.ip += tlen;
        return true;
    }
    
    private void opCClassMB() {
        if (this.s >= this.range || this.chars[this.s] <= '\u00ff') {
            this.opFail();
            return;
        }
        if (!this.isInClassMB()) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }
    
    private void opCClassMIX() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] > '\u00ff') {
            this.ip += 8;
            if (!this.isInClassMB()) {
                this.opFail();
                return;
            }
        }
        else {
            if (!this.isInBitSet()) {
                this.opFail();
                return;
            }
            this.ip += 8;
            final int tlen = this.code[this.ip++];
            this.ip += tlen;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }
    
    private void opCClassNot() {
        if (this.s >= this.range || this.isInBitSet()) {
            this.opFail();
            return;
        }
        this.ip += 8;
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private boolean isNotInClassMB() {
        final int tlen = this.code[this.ip++];
        if (this.s + 1 > this.range) {
            if (this.s >= this.range) {
                return false;
            }
            this.s = this.end;
            this.ip += tlen;
            return true;
        }
        else {
            final int ss = this.s;
            ++this.s;
            final int c = this.chars[ss];
            if (EncodingHelper.isInCodeRange(this.code, this.ip, c)) {
                return false;
            }
            this.ip += tlen;
            return true;
        }
    }
    
    private void opCClassMBNot() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] <= '\u00ff') {
            ++this.s;
            final int tlen = this.code[this.ip++];
            this.ip += tlen;
            this.sprev = this.sbegin;
            return;
        }
        if (!this.isNotInClassMB()) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }
    
    private void opCClassMIXNot() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (this.chars[this.s] > '\u00ff') {
            this.ip += 8;
            if (!this.isNotInClassMB()) {
                this.opFail();
                return;
            }
        }
        else {
            if (this.isInBitSet()) {
                this.opFail();
                return;
            }
            this.ip += 8;
            final int tlen = this.code[this.ip++];
            this.ip += tlen;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }
    
    private void opCClassNode() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        final CClassNode cc = (CClassNode)this.regex.operands[this.code[this.ip++]];
        final int ss = this.s;
        ++this.s;
        final int c = this.chars[ss];
        if (!cc.isCodeInCCLength(c)) {
            this.opFail();
            return;
        }
        this.sprev = this.sbegin;
    }
    
    private void opAnyChar() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        if (EncodingHelper.isNewLine(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private void opAnyCharML() {
        if (this.s >= this.range) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private void opAnyCharStar() {
        final char[] ch = this.chars;
        while (this.s < this.range) {
            this.pushAlt(this.ip, this.s, this.sprev);
            if (EncodingHelper.isNewLine(ch, this.s, this.end)) {
                this.opFail();
                return;
            }
            this.sprev = this.s;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }
    
    private void opAnyCharMLStar() {
        while (this.s < this.range) {
            this.pushAlt(this.ip, this.s, this.sprev);
            this.sprev = this.s;
            ++this.s;
        }
        this.sprev = this.sbegin;
    }
    
    private void opAnyCharStarPeekNext() {
        final char c = (char)this.code[this.ip];
        final char[] ch = this.chars;
        while (this.s < this.range) {
            final char b = ch[this.s];
            if (c == b) {
                this.pushAlt(this.ip + 1, this.s, this.sprev);
            }
            if (EncodingHelper.isNewLine(b)) {
                this.opFail();
                return;
            }
            this.sprev = this.s;
            ++this.s;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }
    
    private void opAnyCharMLStarPeekNext() {
        final char c = (char)this.code[this.ip];
        final char[] ch = this.chars;
        while (this.s < this.range) {
            if (c == ch[this.s]) {
                this.pushAlt(this.ip + 1, this.s, this.sprev);
            }
            this.sprev = this.s;
            ++this.s;
        }
        ++this.ip;
        this.sprev = this.sbegin;
    }
    
    private void opWord() {
        if (this.s >= this.range || !EncodingHelper.isWord(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private void opNotWord() {
        if (this.s >= this.range || EncodingHelper.isWord(this.chars[this.s])) {
            this.opFail();
            return;
        }
        ++this.s;
        this.sprev = this.sbegin;
    }
    
    private void opWordBound() {
        if (this.s == this.str) {
            if (this.s >= this.range || !EncodingHelper.isWord(this.chars[this.s])) {
                this.opFail();
            }
        }
        else if (this.s == this.end) {
            if (this.sprev >= this.end || !EncodingHelper.isWord(this.chars[this.sprev])) {
                this.opFail();
            }
        }
        else if (EncodingHelper.isWord(this.chars[this.s]) == EncodingHelper.isWord(this.chars[this.sprev])) {
            this.opFail();
        }
    }
    
    private void opNotWordBound() {
        if (this.s == this.str) {
            if (this.s < this.range && EncodingHelper.isWord(this.chars[this.s])) {
                this.opFail();
            }
        }
        else if (this.s == this.end) {
            if (this.sprev < this.end && EncodingHelper.isWord(this.chars[this.sprev])) {
                this.opFail();
            }
        }
        else if (EncodingHelper.isWord(this.chars[this.s]) != EncodingHelper.isWord(this.chars[this.sprev])) {
            this.opFail();
        }
    }
    
    private void opWordBegin() {
        if (this.s < this.range && EncodingHelper.isWord(this.chars[this.s]) && (this.s == this.str || !EncodingHelper.isWord(this.chars[this.sprev]))) {
            return;
        }
        this.opFail();
    }
    
    private void opWordEnd() {
        if (this.s != this.str && EncodingHelper.isWord(this.chars[this.sprev]) && (this.s == this.end || !EncodingHelper.isWord(this.chars[this.s]))) {
            return;
        }
        this.opFail();
    }
    
    private void opBeginBuf() {
        if (this.s != this.str) {
            this.opFail();
        }
    }
    
    private void opEndBuf() {
        if (this.s != this.end) {
            this.opFail();
        }
    }
    
    private void opBeginLine() {
        if (this.s == this.str) {
            if (Option.isNotBol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.sprev, this.end) && this.s != this.end) {
            return;
        }
        this.opFail();
    }
    
    private void opEndLine() {
        if (this.s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.s, this.end)) {
            return;
        }
        this.opFail();
    }
    
    private void opSemiEndBuf() {
        if (this.s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                this.opFail();
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.s, this.end) && this.s + 1 == this.end) {
            return;
        }
        this.opFail();
    }
    
    private void opBeginPosition() {
        if (this.s != this.msaStart) {
            this.opFail();
        }
    }
    
    private void opMemoryStartPush() {
        final int mem = this.code[this.ip++];
        this.pushMemStart(mem, this.s);
    }
    
    private void opMemoryStart() {
        final int mem = this.code[this.ip++];
        this.repeatStk[this.memStartStk + mem] = this.s;
    }
    
    private void opMemoryEndPush() {
        final int mem = this.code[this.ip++];
        this.pushMemEnd(mem, this.s);
    }
    
    private void opMemoryEnd() {
        final int mem = this.code[this.ip++];
        this.repeatStk[this.memEndStk + mem] = this.s;
    }
    
    private void opMemoryEndPushRec() {
        final int mem = this.code[this.ip++];
        final int stkp = this.getMemStart(mem);
        this.pushMemEnd(mem, this.s);
        this.repeatStk[this.memStartStk + mem] = stkp;
    }
    
    private void opMemoryEndRec() {
        final int mem = this.code[this.ip++];
        this.repeatStk[this.memEndStk + mem] = this.s;
        final int stkp = this.getMemStart(mem);
        if (BitStatus.bsAt(this.regex.btMemStart, mem)) {
            this.repeatStk[this.memStartStk + mem] = stkp;
        }
        else {
            this.repeatStk[this.memStartStk + mem] = this.stack[stkp].getMemPStr();
        }
        this.pushMemEndMark(mem);
    }
    
    private boolean backrefInvalid(final int mem) {
        return this.repeatStk[this.memEndStk + mem] == -1 || this.repeatStk[this.memStartStk + mem] == -1;
    }
    
    private int backrefStart(final int mem) {
        return BitStatus.bsAt(this.regex.btMemStart, mem) ? this.stack[this.repeatStk[this.memStartStk + mem]].getMemPStr() : this.repeatStk[this.memStartStk + mem];
    }
    
    private int backrefEnd(final int mem) {
        return BitStatus.bsAt(this.regex.btMemEnd, mem) ? this.stack[this.repeatStk[this.memEndStk + mem]].getMemPStr() : this.repeatStk[this.memEndStk + mem];
    }
    
    private void backref(final int mem) {
        if (mem > this.regex.numMem || this.backrefInvalid(mem)) {
            this.opFail();
            return;
        }
        int pstart = this.backrefStart(mem);
        final int pend = this.backrefEnd(mem);
        int n = pend - pstart;
        if (this.s + n > this.range) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        while (n-- > 0) {
            if (this.chars[pstart++] != this.chars[this.s++]) {
                this.opFail();
                return;
            }
        }
        if (this.sprev < this.range) {
            while (this.sprev + 1 < this.s) {
                ++this.sprev;
            }
        }
    }
    
    private void opBackRef1() {
        this.backref(1);
    }
    
    private void opBackRef2() {
        this.backref(2);
    }
    
    private void opBackRefN() {
        this.backref(this.code[this.ip++]);
    }
    
    private void opBackRefNIC() {
        final int mem = this.code[this.ip++];
        if (mem > this.regex.numMem || this.backrefInvalid(mem)) {
            this.opFail();
            return;
        }
        final int pstart = this.backrefStart(mem);
        final int pend = this.backrefEnd(mem);
        final int n = pend - pstart;
        if (this.s + n > this.range) {
            this.opFail();
            return;
        }
        this.sprev = this.s;
        this.value = this.s;
        if (!this.stringCmpIC(this.regex.caseFoldFlag, pstart, this, n, this.end)) {
            this.opFail();
            return;
        }
        this.s = this.value;
        while (this.sprev + 1 < this.s) {
            ++this.sprev;
        }
    }
    
    private void opBackRefMulti() {
        int tlen = 0;
        int i = 0;
    Label_0200:
        for (tlen = this.code[this.ip++], i = 0; i < tlen; ++i) {
            final int mem = this.code[this.ip++];
            if (!this.backrefInvalid(mem)) {
                int pstart = this.backrefStart(mem);
                final int pend = this.backrefEnd(mem);
                int n = pend - pstart;
                if (this.s + n > this.range) {
                    this.opFail();
                    return;
                }
                this.sprev = this.s;
                int swork = this.s;
                while (n-- > 0) {
                    if (this.chars[pstart++] != this.chars[swork++]) {
                        continue Label_0200;
                    }
                }
                this.s = swork;
                if (this.sprev < this.range) {
                    while (this.sprev + 1 < this.s) {
                        ++this.sprev;
                    }
                }
                this.ip += tlen - i - 1;
                break;
            }
        }
        if (i == tlen) {
            this.opFail();
        }
    }
    
    private void opBackRefMultiIC() {
        int tlen;
        int i;
        for (tlen = this.code[this.ip++], i = 0; i < tlen; ++i) {
            final int mem = this.code[this.ip++];
            if (!this.backrefInvalid(mem)) {
                final int pstart = this.backrefStart(mem);
                final int pend = this.backrefEnd(mem);
                final int n = pend - pstart;
                if (this.s + n > this.range) {
                    this.opFail();
                    return;
                }
                this.sprev = this.s;
                this.value = this.s;
                if (this.stringCmpIC(this.regex.caseFoldFlag, pstart, this, n, this.end)) {
                    this.s = this.value;
                    while (this.sprev + 1 < this.s) {
                        ++this.sprev;
                    }
                    this.ip += tlen - i - 1;
                    break;
                }
            }
        }
        if (i == tlen) {
            this.opFail();
        }
    }
    
    private boolean memIsInMemp(final int mem, final int num, final int mempp) {
        int i = 0;
        int memp = mempp;
        while (i < num) {
            final int m = this.code[memp++];
            if (mem == m) {
                return true;
            }
            ++i;
        }
        return false;
    }
    
    private boolean backrefMatchAtNestedLevel(final boolean ignoreCase, final int caseFoldFlag, final int nest, final int memNum, final int memp) {
        int pend = -1;
        int level = 0;
        for (int k = this.stk - 1; k >= 0; --k) {
            final StackEntry e = this.stack[k];
            if (e.type == 2048) {
                --level;
            }
            else if (e.type == 2304) {
                ++level;
            }
            else if (level == nest) {
                if (e.type == 256) {
                    if (this.memIsInMemp(e.getMemNum(), memNum, memp)) {
                        final int pstart = e.getMemPStr();
                        if (pend != -1) {
                            if (pend - pstart > this.end - this.s) {
                                return false;
                            }
                            int p = pstart;
                            this.value = this.s;
                            if (ignoreCase) {
                                if (!this.stringCmpIC(caseFoldFlag, pstart, this, pend - pstart, this.end)) {
                                    return false;
                                }
                            }
                            else {
                                while (p < pend) {
                                    if (this.chars[p++] != this.chars[this.value++]) {
                                        return false;
                                    }
                                }
                            }
                            this.s = this.value;
                            return true;
                        }
                    }
                }
                else if (e.type == 33280 && this.memIsInMemp(e.getMemNum(), memNum, memp)) {
                    pend = e.getMemPStr();
                }
            }
        }
        return false;
    }
    
    private void opBackRefAtLevel() {
        final int ic = this.code[this.ip++];
        final int level = this.code[this.ip++];
        final int tlen = this.code[this.ip++];
        this.sprev = this.s;
        if (this.backrefMatchAtNestedLevel(ic != 0, this.regex.caseFoldFlag, level, tlen, this.ip)) {
            while (this.sprev + 1 < this.s) {
                ++this.sprev;
            }
            this.ip += tlen;
            return;
        }
        this.opFail();
    }
    
    private void opNullCheckStart() {
        final int mem = this.code[this.ip++];
        this.pushNullCheckStart(mem, this.s);
    }
    
    private void nullCheckFound() {
        switch (this.code[this.ip++]) {
            case 55:
            case 56: {
                ++this.ip;
                break;
            }
            case 62:
            case 63:
            case 64:
            case 65: {
                ++this.ip;
                break;
            }
            default: {
                throw new InternalException("unexpected bytecode (bug)");
            }
        }
    }
    
    private void opNullCheckEnd() {
        final int mem = this.code[this.ip++];
        final int isNull = this.nullCheck(mem, this.s);
        if (isNull != 0) {
            this.nullCheckFound();
        }
    }
    
    private void opNullCheckEndMemST() {
        final int mem = this.code[this.ip++];
        final int isNull = this.nullCheckMemSt(mem, this.s);
        if (isNull != 0) {
            if (isNull == -1) {
                this.opFail();
                return;
            }
            this.nullCheckFound();
        }
    }
    
    private void opJump() {
        this.ip += this.code[this.ip] + 1;
    }
    
    private void opPush() {
        final int addr = this.code[this.ip++];
        this.pushAlt(this.ip + addr, this.s, this.sprev);
    }
    
    private void opPop() {
        this.popOne();
    }
    
    private void opPushOrJumpExact1() {
        final int addr = this.code[this.ip++];
        if (this.s < this.range && this.code[this.ip] == this.chars[this.s]) {
            ++this.ip;
            this.pushAlt(this.ip + addr, this.s, this.sprev);
            return;
        }
        this.ip += addr + 1;
    }
    
    private void opPushIfPeekNext() {
        final int addr = this.code[this.ip++];
        if (this.s < this.range && this.code[this.ip] == this.chars[this.s]) {
            ++this.ip;
            this.pushAlt(this.ip + addr, this.s, this.sprev);
            return;
        }
        ++this.ip;
    }
    
    private void opRepeat() {
        final int mem = this.code[this.ip++];
        final int addr = this.code[this.ip++];
        this.repeatStk[mem] = this.stk;
        this.pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            this.pushAlt(this.ip + addr, this.s, this.sprev);
        }
    }
    
    private void opRepeatNG() {
        final int mem = this.code[this.ip++];
        final int addr = this.code[this.ip++];
        this.repeatStk[mem] = this.stk;
        this.pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            this.pushAlt(this.ip, this.s, this.sprev);
            this.ip += addr;
        }
    }
    
    private void repeatInc(final int mem, final int si) {
        final StackEntry e = this.stack[si];
        e.increaseRepeatCount();
        if (e.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                this.pushAlt(this.ip, this.s, this.sprev);
                this.ip = e.getRepeatPCode();
            }
            else {
                this.ip = e.getRepeatPCode();
            }
        }
        this.pushRepeatInc(si);
    }
    
    private void opRepeatInc() {
        final int mem = this.code[this.ip++];
        final int si = this.repeatStk[mem];
        this.repeatInc(mem, si);
    }
    
    private void opRepeatIncSG() {
        final int mem = this.code[this.ip++];
        final int si = this.getRepeat(mem);
        this.repeatInc(mem, si);
    }
    
    private void repeatIncNG(final int mem, final int si) {
        final StackEntry e = this.stack[si];
        e.increaseRepeatCount();
        if (e.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                final int pcode = e.getRepeatPCode();
                this.pushRepeatInc(si);
                this.pushAlt(pcode, this.s, this.sprev);
            }
            else {
                this.ip = e.getRepeatPCode();
                this.pushRepeatInc(si);
            }
        }
        else if (e.getRepeatCount() == this.regex.repeatRangeHi[mem]) {
            this.pushRepeatInc(si);
        }
    }
    
    private void opRepeatIncNG() {
        final int mem = this.code[this.ip++];
        final int si = this.repeatStk[mem];
        this.repeatIncNG(mem, si);
    }
    
    private void opRepeatIncNGSG() {
        final int mem = this.code[this.ip++];
        final int si = this.getRepeat(mem);
        this.repeatIncNG(mem, si);
    }
    
    private void opPushPos() {
        this.pushPos(this.s, this.sprev);
    }
    
    private void opPopPos() {
        final StackEntry e = this.stack[this.posEnd()];
        this.s = e.getStatePStr();
        this.sprev = e.getStatePStrPrev();
    }
    
    private void opPushPosNot() {
        final int addr = this.code[this.ip++];
        this.pushPosNot(this.ip + addr, this.s, this.sprev);
    }
    
    private void opFailPos() {
        this.popTilPosNot();
        this.opFail();
    }
    
    private void opPushStopBT() {
        this.pushStopBT();
    }
    
    private void opPopStopBT() {
        this.stopBtEnd();
    }
    
    private void opLookBehind() {
        final int tlen = this.code[this.ip++];
        this.s = EncodingHelper.stepBack(this.str, this.s, tlen);
        if (this.s == -1) {
            this.opFail();
            return;
        }
        this.sprev = EncodingHelper.prevCharHead(this.str, this.s);
    }
    
    private void opPushLookBehindNot() {
        final int addr = this.code[this.ip++];
        final int tlen = this.code[this.ip++];
        final int q = EncodingHelper.stepBack(this.str, this.s, tlen);
        if (q == -1) {
            this.ip += addr;
        }
        else {
            this.pushLookBehindNot(this.ip + addr, this.s, this.sprev);
            this.s = q;
            this.sprev = EncodingHelper.prevCharHead(this.str, this.s);
        }
    }
    
    private void opFailLookBehindNot() {
        this.popTilLookBehindNot();
        this.opFail();
    }
    
    private void opFail() {
        if (this.stack == null) {
            this.ip = this.regex.codeLength - 1;
            return;
        }
        final StackEntry e = this.pop();
        this.ip = e.getStatePCode();
        this.s = e.getStatePStr();
        this.sprev = e.getStatePStrPrev();
    }
    
    private int finish() {
        return this.bestLen;
    }
}
