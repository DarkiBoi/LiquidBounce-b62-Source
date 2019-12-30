// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

final class OptMapInfo
{
    final MinMaxLen mmd;
    final OptAnchorInfo anchor;
    int value;
    final byte[] map;
    private static final int z = 32768;
    static final short[] ByteValTable;
    
    OptMapInfo() {
        this.mmd = new MinMaxLen();
        this.anchor = new OptAnchorInfo();
        this.map = new byte[256];
    }
    
    void clear() {
        this.mmd.clear();
        this.anchor.clear();
        this.value = 0;
        for (int i = 0; i < this.map.length; ++i) {
            this.map[i] = 0;
        }
    }
    
    void copy(final OptMapInfo other) {
        this.mmd.copy(other.mmd);
        this.anchor.copy(other.anchor);
        this.value = other.value;
        System.arraycopy(other.map, 0, this.map, 0, other.map.length);
    }
    
    void addChar(final int c) {
        final int c_ = c & 0xFF;
        if (this.map[c_] == 0) {
            this.map[c_] = 1;
            this.value += positionValue(c_);
        }
    }
    
    void addCharAmb(final char[] chars, final int p, final int end, final int caseFoldFlag) {
        this.addChar(chars[p]);
        final char[] items = EncodingHelper.caseFoldCodesByString(caseFoldFlag & 0xBFFFFFFF, chars[p]);
        for (int i = 0; i < items.length; ++i) {
            this.addChar(items[i]);
        }
    }
    
    void select(final OptMapInfo alt) {
        if (alt.value == 0) {
            return;
        }
        if (this.value == 0) {
            this.copy(alt);
            return;
        }
        final int v1 = 32768 / this.value;
        final int v2 = 32768 / alt.value;
        if (this.mmd.compareDistanceValue(alt.mmd, v1, v2) > 0) {
            this.copy(alt);
        }
    }
    
    void altMerge(final OptMapInfo other) {
        if (this.value == 0) {
            return;
        }
        if (other.value == 0 || this.mmd.max < other.mmd.max) {
            this.clear();
            return;
        }
        this.mmd.altMerge(other.mmd);
        int val = 0;
        for (int i = 0; i < 256; ++i) {
            if (other.map[i] != 0) {
                this.map[i] = 1;
            }
            if (this.map[i] != 0) {
                val += positionValue(i);
            }
        }
        this.value = val;
        this.anchor.altMerge(other.anchor);
    }
    
    static int positionValue(final int i) {
        if (i < OptMapInfo.ByteValTable.length) {
            return OptMapInfo.ByteValTable[i];
        }
        return 4;
    }
    
    static {
        ByteValTable = new short[] { 5, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 12, 4, 7, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 1 };
    }
}
