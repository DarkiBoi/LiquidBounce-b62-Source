// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public final class CodeRangeBuffer implements Cloneable
{
    private static final int INIT_MULTI_BYTE_RANGE_SIZE = 5;
    private static final int ALL_MULTI_BYTE_RANGE = Integer.MAX_VALUE;
    int[] p;
    int used;
    
    public CodeRangeBuffer() {
        this.p = new int[5];
        this.writeCodePoint(0, 0);
    }
    
    public boolean isInCodeRange(final int code) {
        int low = 0;
        int high;
        final int n = high = this.p[0];
        while (low < high) {
            final int x = low + high >> 1;
            if (code > this.p[(x << 1) + 2]) {
                low = x + 1;
            }
            else {
                high = x;
            }
        }
        return low < n && code >= this.p[(low << 1) + 1];
    }
    
    private CodeRangeBuffer(final CodeRangeBuffer orig) {
        this.p = new int[orig.p.length];
        System.arraycopy(orig.p, 0, this.p, 0, this.p.length);
        this.used = orig.used;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("CodeRange");
        buf.append("\n  used: ").append(this.used);
        buf.append("\n  code point: ").append(this.p[0]);
        buf.append("\n  ranges: ");
        for (int i = 0; i < this.p[0]; ++i) {
            buf.append("[").append(rangeNumToString(this.p[i * 2 + 1])).append("..").append(rangeNumToString(this.p[i * 2 + 2])).append("]");
            if (i > 0 && i % 6 == 0) {
                buf.append("\n          ");
            }
        }
        return buf.toString();
    }
    
    private static String rangeNumToString(final int num) {
        return "0x" + Integer.toString(num, 16);
    }
    
    public void expand(final int low) {
        int length = this.p.length;
        do {
            length <<= 1;
        } while (length < low);
        final int[] tmp = new int[length];
        System.arraycopy(this.p, 0, tmp, 0, this.used);
        this.p = tmp;
    }
    
    public void ensureSize(final int size) {
        int length;
        for (length = this.p.length; length < size; length <<= 1) {}
        if (this.p.length != length) {
            final int[] tmp = new int[length];
            System.arraycopy(this.p, 0, tmp, 0, this.used);
            this.p = tmp;
        }
    }
    
    private void moveRight(final int from, final int to, final int n) {
        if (to + n > this.p.length) {
            this.expand(to + n);
        }
        System.arraycopy(this.p, from, this.p, to, n);
        if (to + n > this.used) {
            this.used = to + n;
        }
    }
    
    protected void moveLeft(final int from, final int to, final int n) {
        System.arraycopy(this.p, from, this.p, to, n);
    }
    
    private void moveLeftAndReduce(final int from, final int to) {
        System.arraycopy(this.p, from, this.p, to, this.used - from);
        this.used -= from - to;
    }
    
    public void writeCodePoint(final int pos, final int b) {
        final int u = pos + 1;
        if (this.p.length < u) {
            this.expand(u);
        }
        this.p[pos] = b;
        if (this.used < u) {
            this.used = u;
        }
    }
    
    public CodeRangeBuffer clone() {
        return new CodeRangeBuffer(this);
    }
    
    public static CodeRangeBuffer addCodeRangeToBuff(final CodeRangeBuffer pbufp, final int fromp, final int top) {
        int from = fromp;
        int to = top;
        CodeRangeBuffer pbuf = pbufp;
        if (from > to) {
            final int n = from;
            from = to;
            to = n;
        }
        if (pbuf == null) {
            pbuf = new CodeRangeBuffer();
        }
        final int[] p = pbuf.p;
        int n2 = p[0];
        int low = 0;
        int bound = n2;
        while (low < bound) {
            final int x = low + bound >>> 1;
            if (from > p[x * 2 + 2]) {
                low = x + 1;
            }
            else {
                bound = x;
            }
        }
        int high = low;
        bound = n2;
        while (high < bound) {
            final int x2 = high + bound >>> 1;
            if (to >= p[x2 * 2 + 1] - 1) {
                high = x2 + 1;
            }
            else {
                bound = x2;
            }
        }
        final int incN = low + 1 - high;
        if (n2 + incN > 10000) {
            throw new ValueException("too many multibyte code ranges are specified");
        }
        if (incN != 1) {
            if (from > p[low * 2 + 1]) {
                from = p[low * 2 + 1];
            }
            if (to < p[(high - 1) * 2 + 2]) {
                to = p[(high - 1) * 2 + 2];
            }
        }
        if (incN != 0 && high < n2) {
            final int fromPos = 1 + high * 2;
            final int toPos = 1 + (low + 1) * 2;
            final int size = (n2 - high) * 2;
            if (incN > 0) {
                pbuf.moveRight(fromPos, toPos, size);
            }
            else {
                pbuf.moveLeftAndReduce(fromPos, toPos);
            }
        }
        final int pos = 1 + low * 2;
        pbuf.writeCodePoint(pos, from);
        pbuf.writeCodePoint(pos + 1, to);
        n2 += incN;
        pbuf.writeCodePoint(0, n2);
        return pbuf;
    }
    
    public static CodeRangeBuffer addCodeRange(final CodeRangeBuffer pbuf, final ScanEnvironment env, final int from, final int to) {
        if (from <= to) {
            return addCodeRangeToBuff(pbuf, from, to);
        }
        if (env.syntax.allowEmptyRangeInCC()) {
            return pbuf;
        }
        throw new ValueException("empty range in char class");
    }
    
    protected static CodeRangeBuffer setAllMultiByteRange(final CodeRangeBuffer pbuf) {
        return addCodeRangeToBuff(pbuf, EncodingHelper.mbcodeStartPosition(), Integer.MAX_VALUE);
    }
    
    public static CodeRangeBuffer addAllMultiByteRange(final CodeRangeBuffer pbuf) {
        return setAllMultiByteRange(pbuf);
    }
    
    public static CodeRangeBuffer notCodeRangeBuff(final CodeRangeBuffer bbuf) {
        CodeRangeBuffer pbuf = null;
        if (bbuf == null) {
            return setAllMultiByteRange(pbuf);
        }
        final int[] p = bbuf.p;
        final int n = p[0];
        if (n <= 0) {
            return setAllMultiByteRange(pbuf);
        }
        int pre = EncodingHelper.mbcodeStartPosition();
        int to = 0;
        for (int i = 0; i < n; ++i) {
            final int from = p[i * 2 + 1];
            to = p[i * 2 + 2];
            if (pre <= from - 1) {
                pbuf = addCodeRangeToBuff(pbuf, pre, from - 1);
            }
            if (to == Integer.MAX_VALUE) {
                break;
            }
            pre = to + 1;
        }
        if (to < Integer.MAX_VALUE) {
            pbuf = addCodeRangeToBuff(pbuf, to + 1, Integer.MAX_VALUE);
        }
        return pbuf;
    }
    
    public static CodeRangeBuffer orCodeRangeBuff(final CodeRangeBuffer bbuf1p, final boolean not1p, final CodeRangeBuffer bbuf2p, final boolean not2p) {
        CodeRangeBuffer pbuf = null;
        CodeRangeBuffer bbuf1 = bbuf1p;
        CodeRangeBuffer bbuf2 = bbuf2p;
        boolean not1 = not1p;
        boolean not2 = not2p;
        if (bbuf1 == null && bbuf2 == null) {
            if (not1 || not2) {
                return setAllMultiByteRange(pbuf);
            }
            return null;
        }
        else {
            if (bbuf2 == null) {
                final boolean tnot = not1;
                not1 = not2;
                not2 = tnot;
                final CodeRangeBuffer tbuf = bbuf1;
                bbuf1 = bbuf2;
                bbuf2 = tbuf;
            }
            if (bbuf1 != null) {
                if (not1) {
                    final boolean tnot = not1;
                    not1 = not2;
                    not2 = tnot;
                    final CodeRangeBuffer tbuf = bbuf1;
                    bbuf1 = bbuf2;
                    bbuf2 = tbuf;
                }
                if (!not2 && !not1) {
                    pbuf = bbuf2.clone();
                }
                else if (!not1) {
                    pbuf = notCodeRangeBuff(bbuf2);
                }
                final int[] p1 = bbuf1.p;
                for (int n1 = p1[0], i = 0; i < n1; ++i) {
                    final int from = p1[i * 2 + 1];
                    final int to = p1[i * 2 + 2];
                    pbuf = addCodeRangeToBuff(pbuf, from, to);
                }
                return pbuf;
            }
            if (not1) {
                return setAllMultiByteRange(pbuf);
            }
            if (!not2) {
                return bbuf2.clone();
            }
            return notCodeRangeBuff(bbuf2);
        }
    }
    
    public static CodeRangeBuffer andCodeRange1(final CodeRangeBuffer pbufp, final int from1p, final int to1p, final int[] data, final int n) {
        CodeRangeBuffer pbuf = pbufp;
        int from1 = from1p;
        int to1 = to1p;
        for (int i = 0; i < n; ++i) {
            final int from2 = data[i * 2 + 1];
            final int to2 = data[i * 2 + 2];
            if (from2 < from1) {
                if (to2 < from1) {
                    continue;
                }
                from1 = to2 + 1;
            }
            else if (from2 <= to1) {
                if (to2 < to1) {
                    if (from1 <= from2 - 1) {
                        pbuf = addCodeRangeToBuff(pbuf, from1, from2 - 1);
                    }
                    from1 = to2 + 1;
                }
                else {
                    to1 = from2 - 1;
                }
            }
            else {
                from1 = from2;
            }
            if (from1 > to1) {
                break;
            }
        }
        if (from1 <= to1) {
            pbuf = addCodeRangeToBuff(pbuf, from1, to1);
        }
        return pbuf;
    }
    
    public static CodeRangeBuffer andCodeRangeBuff(final CodeRangeBuffer bbuf1p, final boolean not1p, final CodeRangeBuffer bbuf2p, final boolean not2p) {
        CodeRangeBuffer pbuf = null;
        CodeRangeBuffer bbuf1 = bbuf1p;
        CodeRangeBuffer bbuf2 = bbuf2p;
        boolean not1 = not1p;
        boolean not2 = not2p;
        if (bbuf1 == null) {
            if (not1 && bbuf2 != null) {
                return bbuf2.clone();
            }
            return null;
        }
        else {
            if (bbuf2 != null) {
                if (not1) {
                    final boolean tnot = not1;
                    not1 = not2;
                    not2 = tnot;
                    final CodeRangeBuffer tbuf = bbuf1;
                    bbuf1 = bbuf2;
                    bbuf2 = tbuf;
                }
                final int[] p1 = bbuf1.p;
                final int n1 = p1[0];
                final int[] p2 = bbuf2.p;
                final int n2 = p2[0];
                if (!not2 && !not1) {
                    for (int i = 0; i < n1; ++i) {
                        final int from1 = p1[i * 2 + 1];
                        final int to1 = p1[i * 2 + 2];
                        for (int j = 0; j < n2; ++j) {
                            final int from2 = p2[j * 2 + 1];
                            final int to2 = p2[j * 2 + 2];
                            if (from2 > to1) {
                                break;
                            }
                            if (to2 >= from1) {
                                final int from3 = (from1 > from2) ? from1 : from2;
                                final int to3 = (to1 < to2) ? to1 : to2;
                                pbuf = addCodeRangeToBuff(pbuf, from3, to3);
                            }
                        }
                    }
                }
                else if (!not1) {
                    for (int i = 0; i < n1; ++i) {
                        final int from1 = p1[i * 2 + 1];
                        final int to1 = p1[i * 2 + 2];
                        pbuf = andCodeRange1(pbuf, from1, to1, p2, n2);
                    }
                }
                return pbuf;
            }
            if (not2) {
                return bbuf1.clone();
            }
            return null;
        }
    }
}
