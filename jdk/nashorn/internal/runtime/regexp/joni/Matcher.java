// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

public abstract class Matcher extends IntHolder
{
    protected final Regex regex;
    protected final char[] chars;
    protected final int str;
    protected final int end;
    protected int msaStart;
    protected int msaOptions;
    protected final Region msaRegion;
    protected int msaBestLen;
    protected int msaBestS;
    protected int msaBegin;
    protected int msaEnd;
    int low;
    int high;
    
    public Matcher(final Regex regex, final char[] chars) {
        this(regex, chars, 0, chars.length);
    }
    
    public Matcher(final Regex regex, final char[] chars, final int p, final int end) {
        this.regex = regex;
        this.chars = chars;
        this.str = p;
        this.end = end;
        this.msaRegion = ((regex.numMem == 0) ? null : new Region(regex.numMem + 1));
    }
    
    protected abstract int matchAt(final int p0, final int p1, final int p2);
    
    public final Region getRegion() {
        return this.msaRegion;
    }
    
    public final int getBegin() {
        return this.msaBegin;
    }
    
    public final int getEnd() {
        return this.msaEnd;
    }
    
    protected final void msaInit(final int option, final int start) {
        this.msaOptions = option;
        this.msaStart = start;
        this.msaBestLen = -1;
    }
    
    public final int match(final int at, final int range, final int option) {
        this.msaInit(option, at);
        final int prev = EncodingHelper.prevCharHead(this.str, at);
        return this.matchAt(range, at, prev);
    }
    
    private boolean forwardSearchRange(final char[] ch, final int string, final int e, final int s, final int range, final IntHolder lowPrev) {
        int pprev = -1;
        int p = s;
        if (this.regex.dMin > 0) {
            p += this.regex.dMin;
        }
    Label_0209:
        while (true) {
            p = this.regex.searchAlgorithm.search(this.regex, ch, p, e, range);
            if (p == -1 || p >= range) {
                return false;
            }
            if (p - this.regex.dMin < s) {
                pprev = p;
                ++p;
            }
            else {
                if (this.regex.subAnchor == 0) {
                    break;
                }
                switch (this.regex.subAnchor) {
                    case 2: {
                        if (p == string) {
                            break Label_0209;
                        }
                        final int prev = EncodingHelper.prevCharHead((pprev != -1) ? pprev : string, p);
                        if (!EncodingHelper.isNewLine(ch, prev, e)) {
                            pprev = p;
                            ++p;
                            continue;
                        }
                        break Label_0209;
                    }
                    case 32: {
                        if (p == e) {
                            break Label_0209;
                        }
                        if (!EncodingHelper.isNewLine(ch, p, e)) {
                            pprev = p;
                            ++p;
                            continue;
                        }
                        break Label_0209;
                    }
                    default: {
                        break Label_0209;
                    }
                }
            }
        }
        if (this.regex.dMax == 0) {
            this.low = p;
            if (lowPrev != null) {
                if (this.low > s) {
                    lowPrev.value = EncodingHelper.prevCharHead(s, p);
                }
                else {
                    lowPrev.value = EncodingHelper.prevCharHead((pprev != -1) ? pprev : string, p);
                }
            }
        }
        else if (this.regex.dMax != Integer.MAX_VALUE) {
            this.low = p - this.regex.dMax;
            if (this.low > s) {
                this.low = EncodingHelper.rightAdjustCharHeadWithPrev(this.low, lowPrev);
                if (lowPrev != null && lowPrev.value == -1) {
                    lowPrev.value = EncodingHelper.prevCharHead((pprev != -1) ? pprev : s, this.low);
                }
            }
            else if (lowPrev != null) {
                lowPrev.value = EncodingHelper.prevCharHead((pprev != -1) ? pprev : string, this.low);
            }
        }
        this.high = p - this.regex.dMin;
        return true;
    }
    
    private boolean backwardSearchRange(final char[] ch, final int string, final int e, final int s, final int range, final int adjrange) {
        int r = range;
        r += this.regex.dMin;
        int p = s;
    Label_0166:
        while (true) {
            p = this.regex.searchAlgorithm.searchBackward(this.regex, ch, r, adjrange, e, p, s, r);
            if (p == -1) {
                return false;
            }
            if (this.regex.subAnchor == 0) {
                break;
            }
            switch (this.regex.subAnchor) {
                case 2: {
                    if (p == string) {
                        break Label_0166;
                    }
                    final int prev = EncodingHelper.prevCharHead(string, p);
                    if (!EncodingHelper.isNewLine(ch, prev, e)) {
                        p = prev;
                        continue;
                    }
                    break Label_0166;
                }
                case 32: {
                    if (p == e) {
                        break Label_0166;
                    }
                    if (EncodingHelper.isNewLine(ch, p, e)) {
                        break Label_0166;
                    }
                    p = EncodingHelper.prevCharHead(adjrange, p);
                    if (p == -1) {
                        return false;
                    }
                    continue;
                }
                default: {
                    break Label_0166;
                }
            }
        }
        if (this.regex.dMax != Integer.MAX_VALUE) {
            this.low = p - this.regex.dMax;
            this.high = p - this.regex.dMin;
        }
        return true;
    }
    
    private boolean matchCheck(final int upperRange, final int s, final int prev) {
        return this.matchAt(this.end, s, prev) != -1 && !Option.isFindLongest(this.regex.options);
    }
    
    public final int search(final int startp, final int rangep, final int option) {
        int start = startp;
        int range = rangep;
        final int origStart = start;
        final int origRange = range;
        if (start > this.end || start < this.str) {
            return -1;
        }
        if (this.regex.anchor != 0 && this.str < this.end) {
            if ((this.regex.anchor & 0x4) != 0x0) {
                if (range > start) {
                    range = start + 1;
                }
                else {
                    range = start;
                }
            }
            else if ((this.regex.anchor & 0x1) != 0x0) {
                if (range > start) {
                    if (start != this.str) {
                        return -1;
                    }
                    range = this.str + 1;
                }
                else {
                    if (range > this.str) {
                        return -1;
                    }
                    start = this.str;
                    range = this.str;
                }
            }
            else if ((this.regex.anchor & 0x8) != 0x0) {
                final int minSemiEnd;
                final int maxSemiEnd = minSemiEnd = this.end;
                if (this.endBuf(start, range, minSemiEnd, maxSemiEnd)) {
                    return -1;
                }
            }
            else if ((this.regex.anchor & 0x10) != 0x0) {
                final int preEnd = EncodingHelper.stepBack(this.str, this.end, 1);
                final int maxSemiEnd = this.end;
                if (EncodingHelper.isNewLine(this.chars, preEnd, this.end)) {
                    final int minSemiEnd = preEnd;
                    if (minSemiEnd > this.str && start <= minSemiEnd && this.endBuf(start, range, minSemiEnd, maxSemiEnd)) {
                        return -1;
                    }
                }
                else {
                    final int minSemiEnd = this.end;
                    if (this.endBuf(start, range, minSemiEnd, maxSemiEnd)) {
                        return -1;
                    }
                }
            }
            else if ((this.regex.anchor & 0x8000) != 0x0) {
                if (range > start) {
                    range = start + 1;
                }
                else {
                    range = start;
                }
            }
        }
        else if (this.str == this.end) {
            if (this.regex.thresholdLength != 0) {
                return -1;
            }
            final int s;
            start = (s = this.str);
            final int prev = -1;
            this.msaInit(option, start);
            if (this.matchCheck(this.end, s, prev)) {
                return this.match(s);
            }
            return this.mismatch();
        }
        this.msaInit(option, origStart);
        int s;
        if (range > (s = start)) {
            int prev;
            if (s > this.str) {
                prev = EncodingHelper.prevCharHead(this.str, s);
            }
            else {
                prev = 0;
            }
            if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
                int schRange = range;
                if (this.regex.dMax != 0) {
                    if (this.regex.dMax == Integer.MAX_VALUE) {
                        schRange = this.end;
                    }
                    else {
                        schRange += this.regex.dMax;
                        if (schRange > this.end) {
                            schRange = this.end;
                        }
                    }
                }
                if (this.end - start < this.regex.thresholdLength) {
                    return this.mismatch();
                }
                Label_0662: {
                    if (this.regex.dMax != Integer.MAX_VALUE) {
                        while (this.forwardSearchRange(this.chars, this.str, this.end, s, schRange, this)) {
                            if (s < this.low) {
                                s = this.low;
                                prev = this.value;
                            }
                            while (s <= this.high) {
                                if (this.matchCheck(origRange, s, prev)) {
                                    return this.match(s);
                                }
                                prev = s;
                                ++s;
                            }
                            if (s >= range) {
                                break Label_0662;
                            }
                        }
                        return this.mismatch();
                    }
                }
                if (!this.forwardSearchRange(this.chars, this.str, this.end, s, schRange, null)) {
                    return this.mismatch();
                }
                if ((this.regex.anchor & 0x4000) != 0x0) {
                    while (!this.matchCheck(origRange, s, prev)) {
                        prev = s;
                        if (++s >= range) {
                            return this.mismatch();
                        }
                    }
                    return this.match(s);
                }
            }
            while (!this.matchCheck(origRange, s, prev)) {
                prev = s;
                if (++s >= range) {
                    if (s == range && this.matchCheck(origRange, s, prev)) {
                        return this.match(s);
                    }
                    return this.mismatch();
                }
            }
            return this.match(s);
        }
        if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
            int adjrange;
            if (range < this.end) {
                adjrange = range;
            }
            else {
                adjrange = this.end;
            }
            if (this.regex.dMax != Integer.MAX_VALUE && this.end - range >= this.regex.thresholdLength) {
                do {
                    int schStart = s + this.regex.dMax;
                    if (schStart > this.end) {
                        schStart = this.end;
                    }
                    if (!this.backwardSearchRange(this.chars, this.str, this.end, schStart, range, adjrange)) {
                        return this.mismatch();
                    }
                    if (s > this.high) {
                        s = this.high;
                    }
                    while (s != -1 && s >= this.low) {
                        final int prev = EncodingHelper.prevCharHead(this.str, s);
                        if (this.matchCheck(origStart, s, prev)) {
                            return this.match(s);
                        }
                        s = prev;
                    }
                } while (s >= range);
                return this.mismatch();
            }
            if (this.end - range < this.regex.thresholdLength) {
                return this.mismatch();
            }
            int schStart = s;
            if (this.regex.dMax != 0) {
                if (this.regex.dMax == Integer.MAX_VALUE) {
                    schStart = this.end;
                }
                else {
                    schStart += this.regex.dMax;
                    if (schStart > this.end) {
                        schStart = this.end;
                    }
                }
            }
            if (!this.backwardSearchRange(this.chars, this.str, this.end, schStart, range, adjrange)) {
                return this.mismatch();
            }
        }
        do {
            final int prev = EncodingHelper.prevCharHead(this.str, s);
            if (this.matchCheck(origStart, s, prev)) {
                return this.match(s);
            }
            s = prev;
        } while (s >= range);
        return this.mismatch();
    }
    
    private boolean endBuf(final int startp, final int rangep, final int minSemiEnd, final int maxSemiEnd) {
        int start = startp;
        int range = rangep;
        if (maxSemiEnd - this.str < this.regex.anchorDmin) {
            return true;
        }
        if (range > start) {
            if (minSemiEnd - start > this.regex.anchorDmax) {
                start = minSemiEnd - this.regex.anchorDmax;
                if (start >= this.end) {
                    start = EncodingHelper.prevCharHead(this.str, this.end);
                }
            }
            if (maxSemiEnd - (range - 1) < this.regex.anchorDmin) {
                range = maxSemiEnd - this.regex.anchorDmin + 1;
            }
            if (start >= range) {
                return true;
            }
        }
        else {
            if (minSemiEnd - range > this.regex.anchorDmax) {
                range = minSemiEnd - this.regex.anchorDmax;
            }
            if (maxSemiEnd - start < this.regex.anchorDmin) {
                start = maxSemiEnd - this.regex.anchorDmin;
            }
            if (range > start) {
                return true;
            }
        }
        return false;
    }
    
    private int match(final int s) {
        return s - this.str;
    }
    
    private int mismatch() {
        if (this.msaBestLen >= 0) {
            final int s = this.msaBestS;
            return this.match(s);
        }
        return -1;
    }
}
