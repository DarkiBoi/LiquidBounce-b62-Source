// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

public abstract class SearchAlgorithm
{
    public static final SearchAlgorithm NONE;
    public static final SearchAlgorithm SLOW;
    public static final SearchAlgorithm BM;
    public static final SearchAlgorithm MAP;
    
    public abstract String getName();
    
    public abstract int search(final Regex p0, final char[] p1, final int p2, final int p3, final int p4);
    
    public abstract int searchBackward(final Regex p0, final char[] p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    static {
        NONE = new SearchAlgorithm() {
            @Override
            public final String getName() {
                return "NONE";
            }
            
            @Override
            public final int search(final Regex regex, final char[] text, final int textP, final int textEnd, final int textRange) {
                return textP;
            }
            
            @Override
            public final int searchBackward(final Regex regex, final char[] text, final int textP, final int adjustText, final int textEnd, final int textStart, final int s_, final int range_) {
                return textP;
            }
        };
        SLOW = new SearchAlgorithm() {
            @Override
            public final String getName() {
                return "EXACT";
            }
            
            @Override
            public final int search(final Regex regex, final char[] text, final int textP, final int textEnd, final int textRange) {
                final char[] target = regex.exact;
                final int targetP = regex.exactP;
                final int targetEnd = regex.exactEnd;
                int end = textEnd;
                end -= targetEnd - targetP - 1;
                if (end > textRange) {
                    end = textRange;
                }
                for (int s = textP; s < end; ++s) {
                    if (text[s] == target[targetP]) {
                        int p;
                        int t;
                        for (p = s + 1, t = targetP + 1; t < targetEnd && target[t] == text[p++]; ++t) {}
                        if (t == targetEnd) {
                            return s;
                        }
                    }
                }
                return -1;
            }
            
            @Override
            public final int searchBackward(final Regex regex, final char[] text, final int textP, final int adjustText, final int textEnd, final int textStart, final int s_, final int range_) {
                final char[] target = regex.exact;
                final int targetP = regex.exactP;
                final int targetEnd = regex.exactEnd;
                int s = textEnd;
                s -= targetEnd - targetP;
                if (s > textStart) {
                    s = textStart;
                }
                while (s >= textP) {
                    if (text[s] == target[targetP]) {
                        int p;
                        int t;
                        for (p = s + 1, t = targetP + 1; t < targetEnd && target[t] == text[p++]; ++t) {}
                        if (t == targetEnd) {
                            return s;
                        }
                    }
                    --s;
                }
                return -1;
            }
        };
        BM = new SearchAlgorithm() {
            private static final int BM_BACKWARD_SEARCH_LENGTH_THRESHOLD = 100;
            
            @Override
            public final String getName() {
                return "EXACT_BM";
            }
            
            @Override
            public final int search(final Regex regex, final char[] text, final int textP, final int textEnd, final int textRange) {
                final char[] target = regex.exact;
                final int targetP = regex.exactP;
                final int targetEnd = regex.exactEnd;
                int end = textRange + (targetEnd - targetP) - 1;
                if (end > textEnd) {
                    end = textEnd;
                }
                final int tail = targetEnd - 1;
                int s = textP + (targetEnd - targetP) - 1;
                if (regex.intMap == null) {
                    while (s < end) {
                        for (int p = s, t = tail; text[p] == target[t]; --p, --t) {
                            if (t == targetP) {
                                return p;
                            }
                        }
                        s += regex.map[text[s] & '\u00ff'];
                    }
                }
                else {
                    while (s < end) {
                        for (int p = s, t = tail; text[p] == target[t]; --p, --t) {
                            if (t == targetP) {
                                return p;
                            }
                        }
                        s += regex.intMap[text[s] & '\u00ff'];
                    }
                }
                return -1;
            }
            
            @Override
            public final int searchBackward(final Regex regex, final char[] text, final int textP, final int adjustText, final int textEnd, final int textStart, final int s_, final int range_) {
                final char[] target = regex.exact;
                final int targetP = regex.exactP;
                final int targetEnd = regex.exactEnd;
                if (regex.intMapBackward == null) {
                    if (s_ - range_ < 100) {
                        return SearchAlgorithm$3.SLOW.searchBackward(regex, text, textP, adjustText, textEnd, textStart, s_, range_);
                    }
                    this.setBmBackwardSkip(regex, target, targetP, targetEnd);
                }
                int s = textEnd - (targetEnd - targetP);
                if (textStart < s) {
                    s = textStart;
                }
                while (s >= textP) {
                    int p;
                    int t;
                    for (p = s, t = targetP; t < targetEnd && text[p] == target[t]; ++p, ++t) {}
                    if (t == targetEnd) {
                        return s;
                    }
                    s -= regex.intMapBackward[text[s] & '\u00ff'];
                }
                return -1;
            }
            
            private void setBmBackwardSkip(final Regex regex, final char[] chars, final int p, final int end) {
                int[] skip;
                if (regex.intMapBackward == null) {
                    skip = new int[256];
                    regex.intMapBackward = skip;
                }
                else {
                    skip = regex.intMapBackward;
                }
                final int len = end - p;
                for (int i = 0; i < 256; ++i) {
                    skip[i] = len;
                }
                for (int i = len - 1; i > 0; --i) {
                    skip[chars[i] & '\u00ff'] = i;
                }
            }
        };
        MAP = new SearchAlgorithm() {
            @Override
            public final String getName() {
                return "MAP";
            }
            
            @Override
            public final int search(final Regex regex, final char[] text, final int textP, final int textEnd, final int textRange) {
                final byte[] map = regex.map;
                for (int s = textP; s < textRange; ++s) {
                    if (text[s] > '\u00ff' || map[text[s]] != 0) {
                        return s;
                    }
                }
                return -1;
            }
            
            @Override
            public final int searchBackward(final Regex regex, final char[] text, final int textP, final int adjustText, final int textEnd, final int textStart, final int s_, final int range_) {
                final byte[] map = regex.map;
                int s = textStart;
                if (s >= textEnd) {
                    s = textEnd - 1;
                }
                while (s >= textP) {
                    if (text[s] > '\u00ff' || map[text[s]] != 0) {
                        return s;
                    }
                    --s;
                }
                return -1;
            }
        };
    }
    
    public static final class SLOW_IC extends SearchAlgorithm
    {
        public SLOW_IC(final Regex regex) {
        }
        
        @Override
        public final String getName() {
            return "EXACT_IC";
        }
        
        @Override
        public final int search(final Regex regex, final char[] text, final int textP, final int textEnd, final int textRange) {
            final char[] target = regex.exact;
            final int targetP = regex.exactP;
            final int targetEnd = regex.exactEnd;
            int end = textEnd;
            end -= targetEnd - targetP - 1;
            if (end > textRange) {
                end = textRange;
            }
            for (int s = textP; s < end; ++s) {
                if (lowerCaseMatch(target, targetP, targetEnd, text, s, textEnd)) {
                    return s;
                }
            }
            return -1;
        }
        
        @Override
        public final int searchBackward(final Regex regex, final char[] text, final int textP, final int adjustText, final int textEnd, final int textStart, final int s_, final int range_) {
            final char[] target = regex.exact;
            final int targetP = regex.exactP;
            final int targetEnd = regex.exactEnd;
            int s = textEnd;
            s -= targetEnd - targetP;
            if (s > textStart) {
                s = textStart;
            }
            while (s >= textP) {
                if (lowerCaseMatch(target, targetP, targetEnd, text, s, textEnd)) {
                    return s;
                }
                s = EncodingHelper.prevCharHead(adjustText, s);
            }
            return -1;
        }
        
        private static boolean lowerCaseMatch(final char[] t, final int tPp, final int tEnd, final char[] chars, final int pp, final int end) {
            int tP = tPp;
            int p = pp;
            while (tP < tEnd) {
                if (t[tP++] != EncodingHelper.toLowerCase(chars[p++])) {
                    return false;
                }
            }
            return true;
        }
    }
}
