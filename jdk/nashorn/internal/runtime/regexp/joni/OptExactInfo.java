// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

final class OptExactInfo
{
    static final int OPT_EXACT_MAXLEN = 24;
    final MinMaxLen mmd;
    final OptAnchorInfo anchor;
    boolean reachEnd;
    boolean ignoreCase;
    final char[] chars;
    int length;
    private static final int COMP_EM_BASE = 20;
    
    OptExactInfo() {
        this.mmd = new MinMaxLen();
        this.anchor = new OptAnchorInfo();
        this.chars = new char[24];
    }
    
    boolean isFull() {
        return this.length >= 24;
    }
    
    void clear() {
        this.mmd.clear();
        this.anchor.clear();
        this.reachEnd = false;
        this.ignoreCase = false;
        this.length = 0;
    }
    
    void copy(final OptExactInfo other) {
        this.mmd.copy(other.mmd);
        this.anchor.copy(other.anchor);
        this.reachEnd = other.reachEnd;
        this.ignoreCase = other.ignoreCase;
        this.length = other.length;
        System.arraycopy(other.chars, 0, this.chars, 0, 24);
    }
    
    void concat(final OptExactInfo other) {
        if (!this.ignoreCase && other.ignoreCase) {
            if (this.length >= other.length) {
                return;
            }
            this.ignoreCase = true;
        }
        int p;
        int end;
        int i;
        for (p = 0, end = p + other.length, i = this.length; p < end && i + 1 <= 24; this.chars[i++] = other.chars[p++]) {}
        this.length = i;
        this.reachEnd = (p == end && other.reachEnd);
        final OptAnchorInfo tmp = new OptAnchorInfo();
        tmp.concat(this.anchor, other.anchor, 1, 1);
        if (!other.reachEnd) {
            tmp.rightAnchor = 0;
        }
        this.anchor.copy(tmp);
    }
    
    void concatStr(final char[] lchars, final int pp, final int end, final boolean raw) {
        int p;
        int i;
        for (p = pp, i = this.length; p < end && i < 24 && i + 1 <= 24; this.chars[i++] = lchars[p++]) {}
        this.length = i;
    }
    
    void altMerge(final OptExactInfo other, final OptEnvironment env) {
        if (other.length == 0 || this.length == 0) {
            this.clear();
            return;
        }
        if (!this.mmd.equal(other.mmd)) {
            this.clear();
            return;
        }
        int i;
        for (i = 0; i < this.length && i < other.length && this.chars[i] == other.chars[i]; ++i) {}
        if (!other.reachEnd || i < other.length || i < this.length) {
            this.reachEnd = false;
        }
        this.length = i;
        this.ignoreCase |= other.ignoreCase;
        this.anchor.altMerge(other.anchor);
        if (!this.reachEnd) {
            this.anchor.rightAnchor = 0;
        }
    }
    
    void select(final OptExactInfo alt) {
        int v1 = this.length;
        int v2 = alt.length;
        if (v2 == 0) {
            return;
        }
        if (v1 == 0) {
            this.copy(alt);
            return;
        }
        if (v1 <= 2 && v2 <= 2) {
            v2 = OptMapInfo.positionValue(this.chars[0] & '\u00ff');
            v1 = OptMapInfo.positionValue(alt.chars[0] & '\u00ff');
            if (this.length > 1) {
                v1 += 5;
            }
            if (alt.length > 1) {
                v2 += 5;
            }
        }
        if (!this.ignoreCase) {
            v1 *= 2;
        }
        if (!alt.ignoreCase) {
            v2 *= 2;
        }
        if (this.mmd.compareDistanceValue(alt.mmd, v1, v2) > 0) {
            this.copy(alt);
        }
    }
    
    int compare(final OptMapInfo m) {
        if (m.value <= 0) {
            return -1;
        }
        final int ve = 20 * this.length * (this.ignoreCase ? 1 : 2);
        final int vm = 200 / m.value;
        return this.mmd.compareDistanceValue(m.mmd, ve, vm);
    }
}
