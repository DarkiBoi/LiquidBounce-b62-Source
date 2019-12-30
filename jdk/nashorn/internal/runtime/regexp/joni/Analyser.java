// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;

final class Analyser extends Parser
{
    private static final int GET_CHAR_LEN_VARLEN = -1;
    private static final int GET_CHAR_LEN_TOP_ALT_VARLEN = -2;
    private static final int THRESHOLD_CASE_FOLD_ALT_FOR_EXPANSION = 8;
    private static final int IN_ALT = 1;
    private static final int IN_NOT = 2;
    private static final int IN_REPEAT = 4;
    private static final int IN_VAR_REPEAT = 8;
    private static final int EXPAND_STRING_MAX_LENGTH = 100;
    private static final int MAX_NODE_OPT_INFO_REF_COUNT = 5;
    
    protected Analyser(final ScanEnvironment env, final char[] chars, final int p, final int end) {
        super(env, chars, p, end);
    }
    
    protected final void compile() {
        this.reset();
        this.regex.numMem = 0;
        this.regex.numRepeat = 0;
        this.regex.numNullCheck = 0;
        this.regex.repeatRangeLo = null;
        this.regex.repeatRangeHi = null;
        this.parse();
        this.root = this.setupTree(this.root, 0);
        this.regex.captureHistory = this.env.captureHistory;
        this.regex.btMemStart = this.env.btMemStart;
        this.regex.btMemEnd = this.env.btMemEnd;
        if (Option.isFindCondition(this.regex.options)) {
            this.regex.btMemEnd = BitStatus.bsAll();
        }
        else {
            this.regex.btMemEnd = this.env.btMemEnd;
            final Regex regex = this.regex;
            regex.btMemEnd |= this.regex.captureHistory;
        }
        this.regex.clearOptimizeInfo();
        this.setOptimizedInfoFromTree(this.root);
        this.env.memNodes = null;
        if (this.regex.numRepeat != 0 || this.regex.btMemEnd != 0) {
            this.regex.stackPopLevel = 2;
        }
        else if (this.regex.btMemStart != 0) {
            this.regex.stackPopLevel = 1;
        }
        else {
            this.regex.stackPopLevel = 0;
        }
    }
    
    private void swap(final Node a, final Node b) {
        a.swap(b);
        if (this.root == b) {
            this.root = a;
        }
        else if (this.root == a) {
            this.root = b;
        }
    }
    
    private int quantifiersMemoryInfo(final Node node) {
        int info = 0;
        Label_0182: {
            switch (node.getType()) {
                case 8:
                case 9: {
                    ConsAltNode can = (ConsAltNode)node;
                    do {
                        final int v = this.quantifiersMemoryInfo(can.car);
                        if (v > info) {
                            info = v;
                        }
                    } while ((can = can.cdr) != null);
                    break;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    if (qn.upper != 0) {
                        info = this.quantifiersMemoryInfo(qn.target);
                        break;
                    }
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 1: {
                            return 2;
                        }
                        case 2:
                        case 4: {
                            info = this.quantifiersMemoryInfo(en.target);
                            break Label_0182;
                        }
                        default: {
                            break Label_0182;
                        }
                    }
                    break;
                }
            }
        }
        return info;
    }
    
    private int getMinMatchLength(final Node node) {
        int min = 0;
        Label_0365: {
            switch (node.getType()) {
                case 4: {
                    final BackRefNode br = (BackRefNode)node;
                    if (br.isRecursion()) {
                        break;
                    }
                    if (br.backRef > this.env.numMem) {
                        throw new ValueException("invalid backref number");
                    }
                    min = this.getMinMatchLength(this.env.memNodes[br.backRef]);
                    break;
                }
                case 8: {
                    ConsAltNode can = (ConsAltNode)node;
                    do {
                        min += this.getMinMatchLength(can.car);
                    } while ((can = can.cdr) != null);
                    break;
                }
                case 9: {
                    ConsAltNode y = (ConsAltNode)node;
                    do {
                        final Node x = y.car;
                        final int tmin = this.getMinMatchLength(x);
                        if (y == node) {
                            min = tmin;
                        }
                        else {
                            if (min <= tmin) {
                                continue;
                            }
                            min = tmin;
                        }
                    } while ((y = y.cdr) != null);
                    break;
                }
                case 0: {
                    min = ((StringNode)node).length();
                    break;
                }
                case 2: {
                    min = 1;
                    break;
                }
                case 1:
                case 3: {
                    min = 1;
                    break;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    if (qn.lower > 0) {
                        min = this.getMinMatchLength(qn.target);
                        min = MinMaxLen.distanceMultiply(min, qn.lower);
                        break;
                    }
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 1: {
                            if (en.isMinFixed()) {
                                min = en.minLength;
                                break Label_0365;
                            }
                            min = this.getMinMatchLength(en.target);
                            en.minLength = min;
                            en.setMinFixed();
                            break Label_0365;
                        }
                        case 2:
                        case 4: {
                            min = this.getMinMatchLength(en.target);
                            break Label_0365;
                        }
                        default: {
                            break Label_0365;
                        }
                    }
                    break;
                }
            }
        }
        return min;
    }
    
    private int getMaxMatchLength(final Node node) {
        int max = 0;
        Label_0389: {
            switch (node.getType()) {
                case 8: {
                    ConsAltNode ln = (ConsAltNode)node;
                    do {
                        final int tmax = this.getMaxMatchLength(ln.car);
                        max = MinMaxLen.distanceAdd(max, tmax);
                    } while ((ln = ln.cdr) != null);
                    break;
                }
                case 9: {
                    ConsAltNode an = (ConsAltNode)node;
                    do {
                        final int tmax2 = this.getMaxMatchLength(an.car);
                        if (max < tmax2) {
                            max = tmax2;
                        }
                    } while ((an = an.cdr) != null);
                    break;
                }
                case 0: {
                    max = ((StringNode)node).length();
                    break;
                }
                case 2: {
                    max = 1;
                    break;
                }
                case 1:
                case 3: {
                    max = 1;
                    break;
                }
                case 4: {
                    final BackRefNode br = (BackRefNode)node;
                    if (br.isRecursion()) {
                        max = Integer.MAX_VALUE;
                        break;
                    }
                    if (br.backRef > this.env.numMem) {
                        throw new ValueException("invalid backref number");
                    }
                    final int tmax3 = this.getMaxMatchLength(this.env.memNodes[br.backRef]);
                    if (max < tmax3) {
                        max = tmax3;
                        break;
                    }
                    break;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    if (qn.upper == 0) {
                        break;
                    }
                    max = this.getMaxMatchLength(qn.target);
                    if (max == 0) {
                        break;
                    }
                    if (!QuantifierNode.isRepeatInfinite(qn.upper)) {
                        max = MinMaxLen.distanceMultiply(max, qn.upper);
                        break;
                    }
                    max = Integer.MAX_VALUE;
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 1: {
                            if (en.isMaxFixed()) {
                                max = en.maxLength;
                                break Label_0389;
                            }
                            max = this.getMaxMatchLength(en.target);
                            en.maxLength = max;
                            en.setMaxFixed();
                            break Label_0389;
                        }
                        case 2:
                        case 4: {
                            max = this.getMaxMatchLength(en.target);
                            break Label_0389;
                        }
                        default: {
                            break Label_0389;
                        }
                    }
                    break;
                }
            }
        }
        return max;
    }
    
    protected final int getCharLengthTree(final Node node) {
        return this.getCharLengthTree(node, 0);
    }
    
    private int getCharLengthTree(final Node node, final int levelp) {
        final int level = levelp + 1;
        int len = 0;
        this.returnCode = 0;
        Label_0442: {
            switch (node.getType()) {
                case 8: {
                    ConsAltNode ln = (ConsAltNode)node;
                    do {
                        final int tlen = this.getCharLengthTree(ln.car, level);
                        if (this.returnCode == 0) {
                            len = MinMaxLen.distanceAdd(len, tlen);
                        }
                        if (this.returnCode == 0) {
                            continue;
                        }
                        break;
                    } while ((ln = ln.cdr) != null);
                    break;
                }
                case 9: {
                    ConsAltNode an = (ConsAltNode)node;
                    boolean varLen = false;
                    final int tlen2 = this.getCharLengthTree(an.car, level);
                    while (this.returnCode == 0 && (an = an.cdr) != null) {
                        final int tlen3 = this.getCharLengthTree(an.car, level);
                        if (this.returnCode == 0 && tlen2 != tlen3) {
                            varLen = true;
                        }
                    }
                    if (this.returnCode != 0) {
                        break;
                    }
                    if (!varLen) {
                        len = tlen2;
                        break;
                    }
                    if (level == 1) {
                        this.returnCode = -2;
                        break;
                    }
                    this.returnCode = -1;
                    break;
                }
                case 0: {
                    final StringNode sn = (StringNode)node;
                    len = sn.length();
                    break;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    if (qn.lower != qn.upper) {
                        this.returnCode = -1;
                        break;
                    }
                    final int tlen2 = this.getCharLengthTree(qn.target, level);
                    if (this.returnCode == 0) {
                        len = MinMaxLen.distanceMultiply(tlen2, qn.lower);
                        break;
                    }
                    break;
                }
                case 1:
                case 2:
                case 3: {
                    len = 1;
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 1: {
                            if (en.isCLenFixed()) {
                                len = en.charLength;
                                break Label_0442;
                            }
                            len = this.getCharLengthTree(en.target, level);
                            if (this.returnCode == 0) {
                                en.charLength = len;
                                en.setCLenFixed();
                                break Label_0442;
                            }
                            break Label_0442;
                        }
                        case 2:
                        case 4: {
                            len = this.getCharLengthTree(en.target, level);
                            break Label_0442;
                        }
                        default: {
                            break Label_0442;
                        }
                    }
                    break;
                }
                case 7: {
                    break;
                }
                default: {
                    this.returnCode = -1;
                    break;
                }
            }
        }
        return len;
    }
    
    private static boolean isNotIncluded(final Node xn, final Node yn) {
        Node x = xn;
        Node y = yn;
    Label_0473:
        while (true) {
            final int yType = y.getType();
            switch (x.getType()) {
                case 2: {
                    switch (yType) {
                        case 1: {
                            final Node tmp = x;
                            x = y;
                            y = tmp;
                            continue;
                        }
                        case 0: {
                            final Node tmp = x;
                            x = y;
                            y = tmp;
                            continue;
                        }
                        default: {
                            break Label_0473;
                        }
                    }
                    break;
                }
                case 1: {
                    final CClassNode xc = (CClassNode)x;
                    switch (yType) {
                        case 1: {
                            final CClassNode yc = (CClassNode)y;
                            for (int i = 0; i < 256; ++i) {
                                boolean v = xc.bs.at(i);
                                if ((v && !xc.isNot()) || (!v && xc.isNot())) {
                                    v = yc.bs.at(i);
                                    if ((v && !yc.isNot()) || (!v && yc.isNot())) {
                                        return false;
                                    }
                                }
                            }
                            return (xc.mbuf == null && !xc.isNot()) || (yc.mbuf == null && !yc.isNot());
                        }
                        case 0: {
                            final Node tmp = x;
                            x = y;
                            y = tmp;
                            continue;
                        }
                        default: {
                            break Label_0473;
                        }
                    }
                    break;
                }
                case 0: {
                    final StringNode xs = (StringNode)x;
                    if (xs.length() == 0) {
                        break Label_0473;
                    }
                    switch (yType) {
                        case 1: {
                            final CClassNode cc = (CClassNode)y;
                            final int code = xs.chars[xs.p];
                            return !cc.isCodeInCC(code);
                        }
                        case 0: {
                            final StringNode ys = (StringNode)y;
                            int len = xs.length();
                            if (len > ys.length()) {
                                len = ys.length();
                            }
                            if (xs.isAmbig() || ys.isAmbig()) {
                                return false;
                            }
                            for (int j = 0, pt = ys.p, q = xs.p; j < len; ++j, ++pt, ++q) {
                                if (ys.chars[pt] != xs.chars[q]) {
                                    return true;
                                }
                            }
                            break Label_0473;
                        }
                        default: {
                            break Label_0473;
                        }
                    }
                    break;
                }
                default: {
                    break Label_0473;
                }
            }
        }
        return false;
    }
    
    private Node getHeadValueNode(final Node node, final boolean exact) {
        Node n = null;
        Label_0320: {
            switch (node.getType()) {
                case 1:
                case 2: {
                    if (!exact) {
                        n = node;
                        break;
                    }
                    break;
                }
                case 8: {
                    n = this.getHeadValueNode(((ConsAltNode)node).car, exact);
                    break;
                }
                case 0: {
                    final StringNode sn = (StringNode)node;
                    if (sn.end <= sn.p) {
                        break;
                    }
                    if (exact && !sn.isRaw() && Option.isIgnoreCase(this.regex.options)) {
                        break;
                    }
                    n = node;
                    break;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    if (qn.lower <= 0) {
                        break;
                    }
                    if (qn.headExact != null) {
                        n = qn.headExact;
                        break;
                    }
                    n = this.getHeadValueNode(qn.target, exact);
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 2: {
                            final int options = this.regex.options;
                            this.regex.options = en.option;
                            n = this.getHeadValueNode(en.target, exact);
                            this.regex.options = options;
                            break Label_0320;
                        }
                        case 1:
                        case 4: {
                            n = this.getHeadValueNode(en.target, exact);
                            break Label_0320;
                        }
                        default: {
                            break Label_0320;
                        }
                    }
                    break;
                }
                case 7: {
                    final AnchorNode an = (AnchorNode)node;
                    if (an.type == 1024) {
                        n = this.getHeadValueNode(an.target, exact);
                        break;
                    }
                    break;
                }
            }
        }
        return n;
    }
    
    private boolean checkTypeTree(final Node node, final int typeMask, final int encloseMask, final int anchorMask) {
        if ((node.getType2Bit() & typeMask) == 0x0) {
            return true;
        }
        boolean invalid = false;
        switch (node.getType()) {
            case 8:
            case 9: {
                ConsAltNode can = (ConsAltNode)node;
                do {
                    invalid = this.checkTypeTree(can.car, typeMask, encloseMask, anchorMask);
                    if (!invalid) {
                        continue;
                    }
                    break;
                } while ((can = can.cdr) != null);
                break;
            }
            case 5: {
                invalid = this.checkTypeTree(((QuantifierNode)node).target, typeMask, encloseMask, anchorMask);
                break;
            }
            case 6: {
                final EncloseNode en = (EncloseNode)node;
                if ((en.type & encloseMask) == 0x0) {
                    return true;
                }
                invalid = this.checkTypeTree(en.target, typeMask, encloseMask, anchorMask);
                break;
            }
            case 7: {
                final AnchorNode an = (AnchorNode)node;
                if ((an.type & anchorMask) == 0x0) {
                    return true;
                }
                if (an.target != null) {
                    invalid = this.checkTypeTree(an.target, typeMask, encloseMask, anchorMask);
                    break;
                }
                break;
            }
        }
        return invalid;
    }
    
    private Node divideLookBehindAlternatives(final Node nodep) {
        Node node = nodep;
        final AnchorNode an = (AnchorNode)node;
        final int anchorType = an.type;
        Node head = an.target;
        Node np = ((ConsAltNode)head).car;
        this.swap(node, head);
        final Node tmp = node;
        node = head;
        head = tmp;
        ((ConsAltNode)node).setCar(head);
        ((AnchorNode)head).setTarget(np);
        np = node;
        while ((np = ((ConsAltNode)np).cdr) != null) {
            final AnchorNode insert = new AnchorNode(anchorType);
            insert.setTarget(((ConsAltNode)np).car);
            ((ConsAltNode)np).setCar(insert);
        }
        if (anchorType == 8192) {
            np = node;
            do {
                ((ConsAltNode)np).toListNode();
            } while ((np = ((ConsAltNode)np).cdr) != null);
        }
        return node;
    }
    
    private Node setupLookBehind(final Node node) {
        final AnchorNode an = (AnchorNode)node;
        final int len = this.getCharLengthTree(an.target);
        switch (this.returnCode) {
            case 0: {
                an.charLength = len;
                break;
            }
            case -1: {
                throw new SyntaxException("invalid pattern in look-behind");
            }
            case -2: {
                if (this.syntax.differentLengthAltLookBehind()) {
                    return this.divideLookBehindAlternatives(node);
                }
                throw new SyntaxException("invalid pattern in look-behind");
            }
        }
        return node;
    }
    
    private void nextSetup(final Node nodep, final Node nextNode) {
        Node node = nodep;
        while (true) {
            final int type = node.getType();
            if (type == 5) {
                final QuantifierNode qn = (QuantifierNode)node;
                if (qn.greedy && QuantifierNode.isRepeatInfinite(qn.upper)) {
                    final StringNode n = (StringNode)this.getHeadValueNode(nextNode, true);
                    if (n != null && n.chars[n.p] != '\0') {
                        qn.nextHeadExact = n;
                    }
                    if (qn.lower <= 1 && qn.target.isSimple()) {
                        final Node x = this.getHeadValueNode(qn.target, false);
                        if (x != null) {
                            final Node y = this.getHeadValueNode(nextNode, false);
                            if (y != null && isNotIncluded(x, y)) {
                                final EncloseNode en = new EncloseNode(4);
                                en.setStopBtSimpleRepeat();
                                this.swap(node, en);
                                en.setTarget(node);
                            }
                        }
                    }
                }
                break;
            }
            if (type != 6) {
                break;
            }
            final EncloseNode en2 = (EncloseNode)node;
            if (!en2.isMemory()) {
                break;
            }
            node = en2.target;
        }
    }
    
    private void updateStringNodeCaseFoldMultiByte(final StringNode sn) {
        final char[] ch = sn.chars;
        final int end = sn.end;
        this.value = sn.p;
        int sp = 0;
        while (this.value < end) {
            final int ovalue = this.value;
            char buf = EncodingHelper.toLowerCase(ch[this.value++]);
            if (ch[ovalue] != buf) {
                char[] sbuf = new char[sn.length() << 1];
                System.arraycopy(ch, sn.p, sbuf, 0, ovalue - sn.p);
                this.value = ovalue;
                while (this.value < end) {
                    buf = EncodingHelper.toLowerCase(ch[this.value++]);
                    if (sp >= sbuf.length) {
                        final char[] tmp = new char[sbuf.length << 1];
                        System.arraycopy(sbuf, 0, tmp, 0, sbuf.length);
                        sbuf = tmp;
                    }
                    sbuf[sp++] = buf;
                }
                sn.set(sbuf, 0, sp);
                return;
            }
            ++sp;
        }
    }
    
    private void updateStringNodeCaseFold(final Node node) {
        final StringNode sn = (StringNode)node;
        this.updateStringNodeCaseFoldMultiByte(sn);
    }
    
    private Node expandCaseFoldMakeRemString(final char[] ch, final int pp, final int end) {
        final StringNode node = new StringNode(ch, pp, end);
        this.updateStringNodeCaseFold(node);
        node.setAmbig();
        node.setDontGetOptInfo();
        return node;
    }
    
    private static boolean expandCaseFoldStringAlt(final int itemNum, final char[] items, final char[] chars, final int p, final int slen, final int end, final ObjPtr<Node> node) {
        ConsAltNode altNode = (ConsAltNode)(node.p = ConsAltNode.newAltNode(null, null));
        StringNode snode = new StringNode(chars, p, p + slen);
        altNode.setCar(snode);
        for (int i = 0; i < itemNum; ++i) {
            snode = new StringNode();
            snode.catCode(items[i]);
            final ConsAltNode an = ConsAltNode.newAltNode(null, null);
            an.setCar(snode);
            altNode.setCdr(an);
            altNode = an;
        }
        return false;
    }
    
    private Node expandCaseFoldString(final Node node) {
        final StringNode sn = (StringNode)node;
        if (sn.isAmbig() || sn.length() <= 0) {
            return node;
        }
        final char[] chars1 = sn.chars;
        int pt = sn.p;
        final int end = sn.end;
        int altNum = 1;
        ConsAltNode topRoot = null;
        ConsAltNode r = null;
        final ObjPtr<Node> prevNode = new ObjPtr<Node>();
        StringNode stringNode = null;
        while (pt < end) {
            final char[] items = EncodingHelper.caseFoldCodesByString(this.regex.caseFoldFlag, chars1[pt]);
            if (items.length == 0) {
                if (stringNode == null) {
                    if (r == null && prevNode.p != null) {
                        r = (topRoot = ConsAltNode.listAdd(null, prevNode.p));
                    }
                    stringNode = (StringNode)(prevNode.p = new StringNode());
                    if (r != null) {
                        ConsAltNode.listAdd(r, stringNode);
                    }
                }
                stringNode.cat(chars1, pt, pt + 1);
            }
            else {
                altNum *= items.length + 1;
                if (altNum > 8) {
                    break;
                }
                if (r == null && prevNode.p != null) {
                    r = (topRoot = ConsAltNode.listAdd(null, prevNode.p));
                }
                expandCaseFoldStringAlt(items.length, items, chars1, pt, 1, end, prevNode);
                if (r != null) {
                    ConsAltNode.listAdd(r, prevNode.p);
                }
                stringNode = null;
            }
            ++pt;
        }
        if (pt < end) {
            final Node srem = this.expandCaseFoldMakeRemString(chars1, pt, end);
            if (prevNode.p != null && r == null) {
                r = (topRoot = ConsAltNode.listAdd(null, prevNode.p));
            }
            if (r == null) {
                prevNode.p = srem;
            }
            else {
                ConsAltNode.listAdd(r, srem);
            }
        }
        final Node xnode = (topRoot != null) ? topRoot : prevNode.p;
        this.swap(node, xnode);
        return xnode;
    }
    
    protected final Node setupTree(final Node nodep, final int statep) {
        Node node = nodep;
        int state = statep;
    Label_1059:
        while (true) {
            switch (node.getType()) {
                case 0: {
                    if (Option.isIgnoreCase(this.regex.options) && !((StringNode)node).isRaw()) {
                        node = this.expandCaseFoldString(node);
                        break Label_1059;
                    }
                    break Label_1059;
                }
                case 4: {
                    final BackRefNode br = (BackRefNode)node;
                    if (br.backRef > this.env.numMem) {
                        throw new ValueException("invalid backref number");
                    }
                    this.env.backrefedMem = BitStatus.bsOnAt(this.env.backrefedMem, br.backRef);
                    this.env.btMemStart = BitStatus.bsOnAt(this.env.btMemStart, br.backRef);
                    ((EncloseNode)this.env.memNodes[br.backRef]).setMemBackrefed();
                    break Label_1059;
                }
                case 5: {
                    final QuantifierNode qn = (QuantifierNode)node;
                    Node target = qn.target;
                    if ((state & 0x4) != 0x0) {
                        qn.setInRepeat();
                    }
                    if (QuantifierNode.isRepeatInfinite(qn.upper) || qn.lower >= 1) {
                        final int d = this.getMinMatchLength(target);
                        if (d == 0) {
                            qn.targetEmptyInfo = 1;
                            final int info = this.quantifiersMemoryInfo(target);
                            if (info > 0) {
                                qn.targetEmptyInfo = info;
                            }
                        }
                    }
                    state |= 0x4;
                    if (qn.lower != qn.upper) {
                        state |= 0x8;
                    }
                    target = this.setupTree(target, state);
                    if (target.getType() == 0 && !QuantifierNode.isRepeatInfinite(qn.lower) && qn.lower == qn.upper && qn.lower > 1 && qn.lower <= 100) {
                        final StringNode sn = (StringNode)target;
                        final int len = sn.length();
                        if (len * qn.lower <= 100) {
                            final StringNode str = qn.convertToString(sn.flag);
                            for (int n = qn.lower, i = 0; i < n; ++i) {
                                str.cat(sn.chars, sn.p, sn.end);
                            }
                            break Label_1059;
                        }
                    }
                    if (!qn.greedy || qn.targetEmptyInfo == 0) {
                        break Label_1059;
                    }
                    if (target.getType() == 5) {
                        final QuantifierNode tqn = (QuantifierNode)target;
                        if (tqn.headExact != null) {
                            qn.headExact = tqn.headExact;
                            tqn.headExact = null;
                        }
                        break Label_1059;
                    }
                    qn.headExact = this.getHeadValueNode(qn.target, true);
                    break Label_1059;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 2: {
                            final int options = this.regex.options;
                            this.regex.options = en.option;
                            this.setupTree(en.target, state);
                            this.regex.options = options;
                            break Label_1059;
                        }
                        case 1: {
                            if ((state & 0xB) != 0x0) {
                                this.env.btMemStart = BitStatus.bsOnAt(this.env.btMemStart, en.regNum);
                            }
                            this.setupTree(en.target, state);
                            break Label_1059;
                        }
                        case 4: {
                            this.setupTree(en.target, state);
                            if (en.target.getType() == 5) {
                                final QuantifierNode tqn2 = (QuantifierNode)en.target;
                                if (QuantifierNode.isRepeatInfinite(tqn2.upper) && tqn2.lower <= 1 && tqn2.greedy && tqn2.target.isSimple()) {
                                    en.setStopBtSimpleRepeat();
                                }
                                break Label_1059;
                            }
                            break Label_1059;
                        }
                        default: {
                            break Label_1059;
                        }
                    }
                    break;
                }
                case 7: {
                    final AnchorNode an = (AnchorNode)node;
                    switch (an.type) {
                        case 1024: {
                            this.setupTree(an.target, state);
                            break Label_1059;
                        }
                        case 2048: {
                            this.setupTree(an.target, state | 0x2);
                            break Label_1059;
                        }
                        case 4096: {
                            if (this.checkTypeTree(an.target, 2031, 1, 4135)) {
                                throw new SyntaxException("invalid pattern in look-behind");
                            }
                            node = this.setupLookBehind(node);
                            if (node.getType() != 7) {
                                continue;
                            }
                            this.setupTree(((AnchorNode)node).target, state);
                            break Label_1059;
                        }
                        case 8192: {
                            if (this.checkTypeTree(an.target, 2031, 1, 4135)) {
                                throw new SyntaxException("invalid pattern in look-behind");
                            }
                            node = this.setupLookBehind(node);
                            if (node.getType() != 7) {
                                continue;
                            }
                            this.setupTree(((AnchorNode)node).target, state | 0x2);
                            break Label_1059;
                        }
                        default: {
                            break Label_1059;
                        }
                    }
                    break;
                }
                default: {
                    break Label_1059;
                }
                case 8: {
                    ConsAltNode lin = (ConsAltNode)node;
                    Node prev = null;
                    do {
                        this.setupTree(lin.car, state);
                        if (prev != null) {
                            this.nextSetup(prev, lin.car);
                        }
                        prev = lin.car;
                    } while ((lin = lin.cdr) != null);
                    break Label_1059;
                }
                case 9: {
                    ConsAltNode aln = (ConsAltNode)node;
                    do {
                        this.setupTree(aln.car, state | 0x1);
                    } while ((aln = aln.cdr) != null);
                }
                case 1: {}
                case 2:
                case 3: {
                    break Label_1059;
                }
            }
        }
        return node;
    }
    
    private void optimizeNodeLeft(final Node node, final NodeOptInfo opt, final OptEnvironment oenv) {
        opt.clear();
        opt.setBoundNode(oenv.mmd);
        Label_1405: {
            switch (node.getType()) {
                case 8: {
                    final OptEnvironment nenv = new OptEnvironment();
                    final NodeOptInfo nopt = new NodeOptInfo();
                    nenv.copy(oenv);
                    ConsAltNode lin = (ConsAltNode)node;
                    do {
                        this.optimizeNodeLeft(lin.car, nopt, nenv);
                        nenv.mmd.add(nopt.length);
                        opt.concatLeftNode(nopt);
                    } while ((lin = lin.cdr) != null);
                    break;
                }
                case 9: {
                    final NodeOptInfo nopt2 = new NodeOptInfo();
                    ConsAltNode aln = (ConsAltNode)node;
                    do {
                        this.optimizeNodeLeft(aln.car, nopt2, oenv);
                        if (aln == node) {
                            opt.copy(nopt2);
                        }
                        else {
                            opt.altMerge(nopt2, oenv);
                        }
                    } while ((aln = aln.cdr) != null);
                    break;
                }
                case 0: {
                    final StringNode sn = (StringNode)node;
                    final int slen = sn.length();
                    if (!sn.isAmbig()) {
                        opt.exb.concatStr(sn.chars, sn.p, sn.end, sn.isRaw());
                        if (slen > 0) {
                            opt.map.addChar(sn.chars[sn.p]);
                        }
                        opt.length.set(slen, slen);
                    }
                    else {
                        int max;
                        if (sn.isDontGetOptInfo()) {
                            max = sn.length();
                        }
                        else {
                            opt.exb.concatStr(sn.chars, sn.p, sn.end, sn.isRaw());
                            opt.exb.ignoreCase = true;
                            if (slen > 0) {
                                opt.map.addCharAmb(sn.chars, sn.p, sn.end, oenv.caseFoldFlag);
                            }
                            max = slen;
                        }
                        opt.length.set(slen, max);
                    }
                    if (opt.exb.length == slen) {
                        opt.exb.reachEnd = true;
                        break;
                    }
                    break;
                }
                case 1: {
                    final CClassNode cc = (CClassNode)node;
                    if (cc.mbuf != null || cc.isNot()) {
                        opt.length.set(1, 1);
                        break;
                    }
                    for (int i = 0; i < 256; ++i) {
                        final boolean z = cc.bs.at(i);
                        if ((z && !cc.isNot()) || (!z && cc.isNot())) {
                            opt.map.addChar(i);
                        }
                    }
                    opt.length.set(1, 1);
                    break;
                }
                case 3: {
                    opt.length.set(1, 1);
                    break;
                }
                case 7: {
                    final AnchorNode an = (AnchorNode)node;
                    switch (an.type) {
                        case 1:
                        case 2:
                        case 4:
                        case 8:
                        case 16:
                        case 32: {
                            opt.anchor.add(an.type);
                            break Label_1405;
                        }
                        case 1024: {
                            final NodeOptInfo nopt = new NodeOptInfo();
                            this.optimizeNodeLeft(an.target, nopt, oenv);
                            if (nopt.exb.length > 0) {
                                opt.expr.copy(nopt.exb);
                            }
                            else if (nopt.exm.length > 0) {
                                opt.expr.copy(nopt.exm);
                            }
                            opt.expr.reachEnd = false;
                            if (nopt.map.value > 0) {
                                opt.map.copy(nopt.map);
                                break Label_1405;
                            }
                            break Label_1405;
                        }
                        case 2048:
                        case 4096:
                        case 8192: {
                            break Label_1405;
                        }
                        default: {
                            break Label_1405;
                        }
                    }
                    break;
                }
                case 4: {
                    final BackRefNode br = (BackRefNode)node;
                    if (br.isRecursion()) {
                        opt.length.set(0, Integer.MAX_VALUE);
                        break;
                    }
                    final Node[] nodes = oenv.scanEnv.memNodes;
                    final int min = this.getMinMatchLength(nodes[br.backRef]);
                    final int max2 = this.getMaxMatchLength(nodes[br.backRef]);
                    opt.length.set(min, max2);
                    break;
                }
                case 5: {
                    final NodeOptInfo nopt2 = new NodeOptInfo();
                    final QuantifierNode qn = (QuantifierNode)node;
                    this.optimizeNodeLeft(qn.target, nopt2, oenv);
                    if (qn.lower == 0 && QuantifierNode.isRepeatInfinite(qn.upper)) {
                        if (oenv.mmd.max == 0 && qn.target.getType() == 3 && qn.greedy) {
                            if (Option.isMultiline(oenv.options)) {
                                opt.anchor.add(32768);
                            }
                            else {
                                opt.anchor.add(16384);
                            }
                        }
                    }
                    else if (qn.lower > 0) {
                        opt.copy(nopt2);
                        if (nopt2.exb.length > 0 && nopt2.exb.reachEnd) {
                            int j;
                            for (j = 2; j <= qn.lower && !opt.exb.isFull(); ++j) {
                                opt.exb.concat(nopt2.exb);
                            }
                            if (j < qn.lower) {
                                opt.exb.reachEnd = false;
                            }
                        }
                        if (qn.lower != qn.upper) {
                            opt.exb.reachEnd = false;
                            opt.exm.reachEnd = false;
                        }
                        if (qn.lower > 1) {
                            opt.exm.reachEnd = false;
                        }
                    }
                    final int min = MinMaxLen.distanceMultiply(nopt2.length.min, qn.lower);
                    int max2;
                    if (QuantifierNode.isRepeatInfinite(qn.upper)) {
                        max2 = ((nopt2.length.max > 0) ? Integer.MAX_VALUE : 0);
                    }
                    else {
                        max2 = MinMaxLen.distanceMultiply(nopt2.length.max, qn.upper);
                    }
                    opt.length.set(min, max2);
                    break;
                }
                case 6: {
                    final EncloseNode en = (EncloseNode)node;
                    switch (en.type) {
                        case 2: {
                            final int save = oenv.options;
                            oenv.options = en.option;
                            this.optimizeNodeLeft(en.target, opt, oenv);
                            oenv.options = save;
                            break Label_1405;
                        }
                        case 1: {
                            if (++en.optCount > 5) {
                                int min = 0;
                                int max2 = Integer.MAX_VALUE;
                                if (en.isMinFixed()) {
                                    min = en.minLength;
                                }
                                if (en.isMaxFixed()) {
                                    max2 = en.maxLength;
                                }
                                opt.length.set(min, max2);
                                break Label_1405;
                            }
                            this.optimizeNodeLeft(en.target, opt, oenv);
                            if (opt.anchor.isSet(49152) && BitStatus.bsAt(oenv.scanEnv.backrefedMem, en.regNum)) {
                                opt.anchor.remove(49152);
                                break Label_1405;
                            }
                            break Label_1405;
                        }
                        case 4: {
                            this.optimizeNodeLeft(en.target, opt, oenv);
                            break Label_1405;
                        }
                        default: {
                            break Label_1405;
                        }
                    }
                    break;
                }
                default: {
                    throw new InternalException("internal parser error (bug)");
                }
            }
        }
    }
    
    protected final void setOptimizedInfoFromTree(final Node node) {
        final NodeOptInfo opt = new NodeOptInfo();
        final OptEnvironment oenv = new OptEnvironment();
        oenv.options = this.regex.options;
        oenv.caseFoldFlag = this.regex.caseFoldFlag;
        oenv.scanEnv = this.env;
        oenv.mmd.clear();
        this.optimizeNodeLeft(node, opt, oenv);
        this.regex.anchor = (opt.anchor.leftAnchor & 0xC005);
        final Regex regex = this.regex;
        regex.anchor |= (opt.anchor.rightAnchor & 0x18);
        if ((this.regex.anchor & 0x18) != 0x0) {
            this.regex.anchorDmin = opt.length.min;
            this.regex.anchorDmax = opt.length.max;
        }
        if (opt.exb.length > 0 || opt.exm.length > 0) {
            opt.exb.select(opt.exm);
            if (opt.map.value > 0 && opt.exb.compare(opt.map) > 0) {
                this.regex.setOptimizeMapInfo(opt.map);
                this.regex.setSubAnchor(opt.map.anchor);
            }
            else {
                this.regex.setExactInfo(opt.exb);
                this.regex.setSubAnchor(opt.exb.anchor);
            }
        }
        else if (opt.map.value > 0) {
            this.regex.setOptimizeMapInfo(opt.map);
            this.regex.setSubAnchor(opt.map.anchor);
        }
        else {
            final Regex regex2 = this.regex;
            regex2.subAnchor |= (opt.anchor.leftAnchor & 0x2);
            if (opt.length.max == 0) {
                final Regex regex3 = this.regex;
                regex3.subAnchor |= (opt.anchor.rightAnchor & 0x20);
            }
        }
    }
}
