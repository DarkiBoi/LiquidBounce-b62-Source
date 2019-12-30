// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

final class MinMaxLen
{
    int min;
    int max;
    private static final short[] distValues;
    static final int INFINITE_DISTANCE = Integer.MAX_VALUE;
    
    MinMaxLen() {
    }
    
    MinMaxLen(final int min, final int max) {
        this.min = min;
        this.max = max;
    }
    
    int distanceValue() {
        if (this.max == Integer.MAX_VALUE) {
            return 0;
        }
        final int d = this.max - this.min;
        return (d < MinMaxLen.distValues.length) ? MinMaxLen.distValues[d] : 1;
    }
    
    int compareDistanceValue(final MinMaxLen other, final int v1p, final int v2p) {
        int v1 = v1p;
        int v2 = v2p;
        if (v2 <= 0) {
            return -1;
        }
        if (v1 <= 0) {
            return 1;
        }
        v1 *= this.distanceValue();
        v2 *= other.distanceValue();
        if (v2 > v1) {
            return 1;
        }
        if (v2 < v1) {
            return -1;
        }
        if (other.min < this.min) {
            return 1;
        }
        if (other.min > this.min) {
            return -1;
        }
        return 0;
    }
    
    boolean equal(final MinMaxLen other) {
        return this.min == other.min && this.max == other.max;
    }
    
    void set(final int min, final int max) {
        this.min = min;
        this.max = max;
    }
    
    void clear() {
        final int n = 0;
        this.max = n;
        this.min = n;
    }
    
    void copy(final MinMaxLen other) {
        this.min = other.min;
        this.max = other.max;
    }
    
    void add(final MinMaxLen other) {
        this.min = distanceAdd(this.min, other.min);
        this.max = distanceAdd(this.max, other.max);
    }
    
    void addLength(final int len) {
        this.min = distanceAdd(this.min, len);
        this.max = distanceAdd(this.max, len);
    }
    
    void altMerge(final MinMaxLen other) {
        if (this.min > other.min) {
            this.min = other.min;
        }
        if (this.max < other.max) {
            this.max = other.max;
        }
    }
    
    static int distanceAdd(final int d1, final int d2) {
        if (d1 == Integer.MAX_VALUE || d2 == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (d1 <= Integer.MAX_VALUE - d2) {
            return d1 + d2;
        }
        return Integer.MAX_VALUE;
    }
    
    static int distanceMultiply(final int d, final int m) {
        if (m == 0) {
            return 0;
        }
        if (d < Integer.MAX_VALUE / m) {
            return d * m;
        }
        return Integer.MAX_VALUE;
    }
    
    static String distanceRangeToString(final int a, final int b) {
        String s = "";
        if (a == Integer.MAX_VALUE) {
            s += "inf";
        }
        else {
            s = s + "(" + a + ")";
        }
        s += "-";
        if (b == Integer.MAX_VALUE) {
            s += "inf";
        }
        else {
            s = s + "(" + b + ")";
        }
        return s;
    }
    
    static {
        distValues = new short[] { 1000, 500, 333, 250, 200, 167, 143, 125, 111, 100, 91, 83, 77, 71, 67, 63, 59, 56, 53, 50, 48, 45, 43, 42, 40, 38, 37, 36, 34, 33, 32, 31, 30, 29, 29, 28, 27, 26, 26, 25, 24, 24, 23, 23, 22, 22, 21, 21, 20, 20, 20, 19, 19, 19, 18, 18, 18, 17, 17, 17, 16, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 11, 11, 11, 11, 11, 11, 11, 11, 11, 10, 10, 10, 10, 10 };
    }
}
