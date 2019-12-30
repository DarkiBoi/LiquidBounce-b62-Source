// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnyCharNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCVALTYPE;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCSTATE;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;

class Parser extends Lexer
{
    protected final Regex regex;
    protected Node root;
    protected int returnCode;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    protected Parser(final ScanEnvironment env, final char[] chars, final int p, final int end) {
        super(env, chars, p, end);
        this.regex = env.reg;
    }
    
    protected final Node parse() {
        this.root = this.parseRegexp();
        this.regex.numMem = this.env.numMem;
        return this.root;
    }
    
    private boolean codeExistCheck(final int code, final boolean ignoreEscaped) {
        this.mark();
        boolean inEsc = false;
        while (this.left()) {
            if (ignoreEscaped && inEsc) {
                inEsc = false;
            }
            else {
                this.fetch();
                if (this.c == code) {
                    this.restore();
                    return true;
                }
                if (this.c != this.syntax.metaCharTable.esc) {
                    continue;
                }
                inEsc = true;
            }
        }
        this.restore();
        return false;
    }
    
    private CClassNode parseCharClass() {
        this.fetchTokenInCC();
        boolean neg;
        if (this.token.type == TokenType.CHAR && this.token.getC() == 94 && !this.token.escaped) {
            neg = true;
            this.fetchTokenInCC();
        }
        else {
            neg = false;
        }
        if (this.token.type == TokenType.CC_CLOSE) {
            if (!this.codeExistCheck(93, true)) {
                throw new SyntaxException("empty char-class");
            }
            this.env.ccEscWarn("]");
            this.token.type = TokenType.CHAR;
        }
        CClassNode cc = new CClassNode();
        CClassNode prevCC = null;
        CClassNode workCC = null;
        final CClassNode.CCStateArg arg = new CClassNode.CCStateArg();
        boolean andStart = false;
        arg.state = CCSTATE.START;
        while (this.token.type != TokenType.CC_CLOSE) {
            boolean fetched = false;
            switch (this.token.type) {
                case CHAR: {
                    if (this.token.getC() > 255) {
                        arg.inType = CCVALTYPE.CODE_POINT;
                    }
                    else {
                        arg.inType = CCVALTYPE.SB;
                    }
                    arg.v = this.token.getC();
                    arg.vIsRaw = false;
                    this.parseCharClassValEntry2(cc, arg);
                    break;
                }
                case RAW_BYTE: {
                    arg.v = this.token.getC();
                    arg.inType = CCVALTYPE.SB;
                    arg.vIsRaw = true;
                    this.parseCharClassValEntry2(cc, arg);
                    break;
                }
                case CODE_POINT: {
                    arg.v = this.token.getCode();
                    arg.vIsRaw = true;
                    this.parseCharClassValEntry(cc, arg);
                    break;
                }
                case CHAR_TYPE: {
                    cc.addCType(this.token.getPropCType(), this.token.getPropNot(), this.env, this);
                    cc.nextStateClass(arg, this.env);
                    break;
                }
                case CC_RANGE: {
                    if (arg.state == CCSTATE.VALUE) {
                        this.fetchTokenInCC();
                        fetched = true;
                        if (this.token.type == TokenType.CC_CLOSE) {
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        if (this.token.type == TokenType.CC_AND) {
                            this.env.ccEscWarn("-");
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        arg.state = CCSTATE.RANGE;
                        break;
                    }
                    else {
                        if (arg.state == CCSTATE.START) {
                            arg.v = this.token.getC();
                            arg.vIsRaw = false;
                            this.fetchTokenInCC();
                            fetched = true;
                            if (this.token.type == TokenType.CC_RANGE || andStart) {
                                this.env.ccEscWarn("-");
                            }
                            this.parseCharClassValEntry(cc, arg);
                            break;
                        }
                        if (arg.state == CCSTATE.RANGE) {
                            this.env.ccEscWarn("-");
                            this.parseCharClassSbChar(cc, arg);
                            break;
                        }
                        this.fetchTokenInCC();
                        fetched = true;
                        if (this.token.type == TokenType.CC_CLOSE) {
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        if (this.token.type == TokenType.CC_AND) {
                            this.env.ccEscWarn("-");
                            this.parseCharClassRangeEndVal(cc, arg);
                            break;
                        }
                        if (this.syntax.allowDoubleRangeOpInCC()) {
                            this.env.ccEscWarn("-");
                            arg.inType = CCVALTYPE.SB;
                            arg.v = 45;
                            arg.vIsRaw = false;
                            this.parseCharClassValEntry2(cc, arg);
                            break;
                        }
                        throw new SyntaxException("unmatched range specifier in char-class");
                    }
                    break;
                }
                case CC_CC_OPEN: {
                    final CClassNode acc = this.parseCharClass();
                    cc.or(acc);
                    break;
                }
                case CC_AND: {
                    if (arg.state == CCSTATE.VALUE) {
                        arg.v = 0;
                        arg.vIsRaw = false;
                        cc.nextStateValue(arg, this.env);
                    }
                    andStart = true;
                    arg.state = CCSTATE.START;
                    if (prevCC != null) {
                        prevCC.and(cc);
                    }
                    else {
                        prevCC = cc;
                        if (workCC == null) {
                            workCC = new CClassNode();
                        }
                        cc = workCC;
                    }
                    cc.clear();
                    break;
                }
                case EOT: {
                    throw new SyntaxException("premature end of char-class");
                }
                default: {
                    throw new InternalException("internal parser error (bug)");
                }
            }
            if (!fetched) {
                this.fetchTokenInCC();
            }
        }
        if (arg.state == CCSTATE.VALUE) {
            arg.v = 0;
            arg.vIsRaw = false;
            cc.nextStateValue(arg, this.env);
        }
        if (prevCC != null) {
            prevCC.and(cc);
            cc = prevCC;
        }
        if (neg) {
            cc.setNot();
        }
        else {
            cc.clearNot();
        }
        if (cc.isNot() && this.syntax.notNewlineInNegativeCC() && !cc.isEmpty()) {
            final int NEW_LINE = 10;
            if (EncodingHelper.isNewLine(10)) {
                cc.bs.set(10);
            }
        }
        return cc;
    }
    
    private void parseCharClassSbChar(final CClassNode cc, final CClassNode.CCStateArg arg) {
        arg.inType = CCVALTYPE.SB;
        arg.v = this.token.getC();
        arg.vIsRaw = false;
        this.parseCharClassValEntry2(cc, arg);
    }
    
    private void parseCharClassRangeEndVal(final CClassNode cc, final CClassNode.CCStateArg arg) {
        arg.v = 45;
        arg.vIsRaw = false;
        this.parseCharClassValEntry(cc, arg);
    }
    
    private void parseCharClassValEntry(final CClassNode cc, final CClassNode.CCStateArg arg) {
        arg.inType = ((arg.v <= 255) ? CCVALTYPE.SB : CCVALTYPE.CODE_POINT);
        this.parseCharClassValEntry2(cc, arg);
    }
    
    private void parseCharClassValEntry2(final CClassNode cc, final CClassNode.CCStateArg arg) {
        cc.nextStateValue(arg, this.env);
    }
    
    private Node parseEnclose(final TokenType term) {
        Node node = null;
        if (!this.left()) {
            throw new SyntaxException("end pattern with unmatched parenthesis");
        }
        int option = this.env.option;
        if (this.peekIs(63) && this.syntax.op2QMarkGroupEffect()) {
            this.inc();
            if (!this.left()) {
                throw new SyntaxException("end pattern in group");
            }
            this.fetch();
            switch (this.c) {
                case 58: {
                    this.fetchToken();
                    node = this.parseSubExp(term);
                    this.returnCode = 1;
                    return node;
                }
                case 61: {
                    node = new AnchorNode(1024);
                    break;
                }
                case 33: {
                    node = new AnchorNode(2048);
                    break;
                }
                case 62: {
                    node = new EncloseNode(4);
                    break;
                }
                case 39: {
                    break;
                }
                case 60: {
                    this.fetch();
                    if (this.c == 61) {
                        node = new AnchorNode(4096);
                        break;
                    }
                    if (this.c == 33) {
                        node = new AnchorNode(8192);
                        break;
                    }
                    throw new SyntaxException("undefined group option");
                }
                case 64: {
                    if (!this.syntax.op2AtMarkCaptureHistory()) {
                        throw new SyntaxException("undefined group option");
                    }
                    final EncloseNode en = new EncloseNode();
                    final int num = this.env.addMemEntry();
                    if (num >= 32) {
                        throw new ValueException("group number is too big for capture history");
                    }
                    en.regNum = num;
                    node = en;
                    break;
                }
                case 45:
                case 105:
                case 109:
                case 115:
                case 120: {
                    boolean neg = false;
                    while (true) {
                        switch (this.c) {
                            case 41:
                            case 58: {
                                break;
                            }
                            case 45: {
                                neg = true;
                                break;
                            }
                            case 120: {
                                option = BitStatus.bsOnOff(option, 2, neg);
                                break;
                            }
                            case 105: {
                                option = BitStatus.bsOnOff(option, 1, neg);
                                break;
                            }
                            case 115: {
                                if (this.syntax.op2OptionPerl()) {
                                    option = BitStatus.bsOnOff(option, 4, neg);
                                    break;
                                }
                                throw new SyntaxException("undefined group option");
                            }
                            case 109: {
                                if (this.syntax.op2OptionPerl()) {
                                    option = BitStatus.bsOnOff(option, 8, !neg);
                                    break;
                                }
                                if (this.syntax.op2OptionRuby()) {
                                    option = BitStatus.bsOnOff(option, 4, neg);
                                    break;
                                }
                                throw new SyntaxException("undefined group option");
                            }
                            default: {
                                throw new SyntaxException("undefined group option");
                            }
                        }
                        if (this.c == 41) {
                            final EncloseNode en2 = (EncloseNode)(node = new EncloseNode(option, 0));
                            this.returnCode = 2;
                            return node;
                        }
                        if (this.c == 58) {
                            final int prev = this.env.option;
                            this.env.option = option;
                            this.fetchToken();
                            final Node target = this.parseSubExp(term);
                            this.env.option = prev;
                            final EncloseNode en3 = new EncloseNode(option, 0);
                            en3.setTarget(target);
                            node = en3;
                            this.returnCode = 0;
                            return node;
                        }
                        if (!this.left()) {
                            throw new SyntaxException("end pattern in group");
                        }
                        this.fetch();
                    }
                    break;
                }
                default: {
                    throw new SyntaxException("undefined group option");
                }
            }
        }
        else {
            if (Option.isDontCaptureGroup(this.env.option)) {
                this.fetchToken();
                node = this.parseSubExp(term);
                this.returnCode = 1;
                return node;
            }
            final EncloseNode en = new EncloseNode();
            final int num = this.env.addMemEntry();
            en.regNum = num;
            node = en;
        }
        this.fetchToken();
        final Node target2 = this.parseSubExp(term);
        if (node.getType() == 7) {
            final AnchorNode an = (AnchorNode)node;
            an.setTarget(target2);
        }
        else {
            final EncloseNode en2 = (EncloseNode)node;
            en2.setTarget(target2);
            if (en2.type == 1) {
                this.env.setMemNode(en2.regNum, node);
            }
        }
        this.returnCode = 0;
        return node;
    }
    
    private Node parseExp(final TokenType term) {
        if (this.token.type == term) {
            return StringNode.EMPTY;
        }
        Node node = null;
        boolean group = false;
        Label_0676: {
            switch (this.token.type) {
                case EOT:
                case ALT: {
                    return StringNode.EMPTY;
                }
                case SUBEXP_OPEN: {
                    node = this.parseEnclose(TokenType.SUBEXP_CLOSE);
                    if (this.returnCode == 1) {
                        group = true;
                        break;
                    }
                    if (this.returnCode == 2) {
                        final int prev = this.env.option;
                        final EncloseNode en = (EncloseNode)node;
                        this.env.option = en.option;
                        this.fetchToken();
                        final Node target = this.parseSubExp(term);
                        this.env.option = prev;
                        en.setTarget(target);
                        return node;
                    }
                    break;
                }
                case SUBEXP_CLOSE: {
                    if (!this.syntax.allowUnmatchedCloseSubexp()) {
                        throw new SyntaxException("unmatched close parenthesis");
                    }
                    if (this.token.escaped) {
                        return this.parseExpTkRawByte(group);
                    }
                    return this.parseExpTkByte(group);
                }
                case STRING: {
                    return this.parseExpTkByte(group);
                }
                case RAW_BYTE: {
                    return this.parseExpTkRawByte(group);
                }
                case CODE_POINT: {
                    final char[] buf = { (char)this.token.getCode() };
                    node = new StringNode(buf, 0, 1);
                    break;
                }
                case CHAR_TYPE: {
                    switch (this.token.getPropCType()) {
                        case 260:
                        case 265:
                        case 268: {
                            final CClassNode cc = new CClassNode();
                            cc.addCType(this.token.getPropCType(), false, this.env, this);
                            if (this.token.getPropNot()) {
                                cc.setNot();
                            }
                            node = cc;
                            break Label_0676;
                        }
                        case 4:
                        case 9:
                        case 11: {
                            final CClassNode ccn = new CClassNode();
                            ccn.addCType(this.token.getPropCType(), false, this.env, this);
                            if (this.token.getPropNot()) {
                                ccn.setNot();
                            }
                            node = ccn;
                            break Label_0676;
                        }
                        default: {
                            throw new InternalException("internal parser error (bug)");
                        }
                    }
                    break;
                }
                case CC_CC_OPEN: {
                    final CClassNode cc = (CClassNode)(node = this.parseCharClass());
                    if (Option.isIgnoreCase(this.env.option)) {
                        final ApplyCaseFoldArg arg = new ApplyCaseFoldArg(this.env, cc);
                        EncodingHelper.applyAllCaseFold(this.env.caseFoldFlag, ApplyCaseFold.INSTANCE, arg);
                        if (arg.altRoot != null) {
                            node = ConsAltNode.newAltNode(node, arg.altRoot);
                        }
                        break;
                    }
                    break;
                }
                case ANYCHAR: {
                    node = new AnyCharNode();
                    break;
                }
                case ANYCHAR_ANYTIME: {
                    node = new AnyCharNode();
                    final QuantifierNode qn = new QuantifierNode(0, -1, false);
                    qn.setTarget(node);
                    node = qn;
                    break;
                }
                case BACKREF: {
                    final int backRef = this.token.getBackrefRef();
                    node = new BackRefNode(backRef, this.env);
                    break;
                }
                case ANCHOR: {
                    node = new AnchorNode(this.token.getAnchor());
                    break;
                }
                case OP_REPEAT:
                case INTERVAL: {
                    if (!this.syntax.contextIndepRepeatOps()) {
                        return this.parseExpTkByte(group);
                    }
                    if (this.syntax.contextInvalidRepeatOps()) {
                        throw new SyntaxException("target of repeat operator is not specified");
                    }
                    node = StringNode.EMPTY;
                    break;
                }
                default: {
                    throw new InternalException("internal parser error (bug)");
                }
            }
        }
        this.fetchToken();
        return this.parseExpRepeat(node, group);
    }
    
    private Node parseExpTkByte(final boolean group) {
        final StringNode node = new StringNode(this.chars, this.token.backP, this.p);
        while (true) {
            this.fetchToken();
            if (this.token.type != TokenType.STRING) {
                break;
            }
            if (this.token.backP == node.end) {
                node.end = this.p;
            }
            else {
                node.cat(this.chars, this.token.backP, this.p);
            }
        }
        return this.parseExpRepeat(node, group);
    }
    
    private Node parseExpTkRawByte(final boolean group) {
        final StringNode node = new StringNode((char)this.token.getC());
        node.setRaw();
        this.fetchToken();
        node.clearRaw();
        return this.parseExpRepeat(node, group);
    }
    
    private Node parseExpRepeat(final Node targetp, final boolean group) {
        Node target = targetp;
        while (this.token.type == TokenType.OP_REPEAT || this.token.type == TokenType.INTERVAL) {
            if (target.isInvalidQuantifier()) {
                throw new SyntaxException("target of repeat operator is invalid");
            }
            final QuantifierNode qtfr = new QuantifierNode(this.token.getRepeatLower(), this.token.getRepeatUpper(), this.token.type == TokenType.INTERVAL);
            qtfr.greedy = this.token.getRepeatGreedy();
            final int ret = qtfr.setQuantifier(target, group, this.env, this.chars, this.getBegin(), this.getEnd());
            Node qn = qtfr;
            if (this.token.getRepeatPossessive()) {
                final EncloseNode en = new EncloseNode(4);
                en.setTarget(qn);
                qn = en;
            }
            if (ret == 0) {
                target = qn;
            }
            else if (ret == 2) {
                target = ConsAltNode.newListNode(target, null);
                final ConsAltNode tmp = ((ConsAltNode)target).setCdr(ConsAltNode.newListNode(qn, null));
                this.fetchToken();
                return this.parseExpRepeatForCar(target, tmp, group);
            }
            this.fetchToken();
        }
        return target;
    }
    
    private Node parseExpRepeatForCar(final Node top, final ConsAltNode target, final boolean group) {
        while (this.token.type == TokenType.OP_REPEAT || this.token.type == TokenType.INTERVAL) {
            if (target.car.isInvalidQuantifier()) {
                throw new SyntaxException("target of repeat operator is invalid");
            }
            final QuantifierNode qtfr = new QuantifierNode(this.token.getRepeatLower(), this.token.getRepeatUpper(), this.token.type == TokenType.INTERVAL);
            qtfr.greedy = this.token.getRepeatGreedy();
            final int ret = qtfr.setQuantifier(target.car, group, this.env, this.chars, this.getBegin(), this.getEnd());
            Node qn = qtfr;
            if (this.token.getRepeatPossessive()) {
                final EncloseNode en = new EncloseNode(4);
                en.setTarget(qn);
                qn = en;
            }
            if (ret == 0) {
                target.setCar(qn);
            }
            else if (ret == 2 && !Parser.$assertionsDisabled) {
                throw new AssertionError();
            }
            this.fetchToken();
        }
        return top;
    }
    
    private Node parseBranch(final TokenType term) {
        Node node = this.parseExp(term);
        if (this.token.type == TokenType.EOT || this.token.type == term || this.token.type == TokenType.ALT) {
            return node;
        }
        ConsAltNode t;
        final ConsAltNode top = t = ConsAltNode.newListNode(node, null);
        while (this.token.type != TokenType.EOT && this.token.type != term && this.token.type != TokenType.ALT) {
            node = this.parseExp(term);
            if (node.getType() == 8) {
                t.setCdr((ConsAltNode)node);
                while (((ConsAltNode)node).cdr != null) {
                    node = ((ConsAltNode)node).cdr;
                }
                t = (ConsAltNode)node;
            }
            else {
                t.setCdr(ConsAltNode.newListNode(node, null));
                t = t.cdr;
            }
        }
        return top;
    }
    
    private Node parseSubExp(final TokenType term) {
        Node node = this.parseBranch(term);
        if (this.token.type == term) {
            return node;
        }
        if (this.token.type == TokenType.ALT) {
            ConsAltNode t;
            final ConsAltNode top = t = ConsAltNode.newAltNode(node, null);
            while (this.token.type == TokenType.ALT) {
                this.fetchToken();
                node = this.parseBranch(term);
                t.setCdr(ConsAltNode.newAltNode(node, null));
                t = t.cdr;
            }
            if (this.token.type != term) {
                parseSubExpError(term);
            }
            return top;
        }
        parseSubExpError(term);
        return null;
    }
    
    private static void parseSubExpError(final TokenType term) {
        if (term == TokenType.SUBEXP_CLOSE) {
            throw new SyntaxException("end pattern with unmatched parenthesis");
        }
        throw new InternalException("internal parser error (bug)");
    }
    
    private Node parseRegexp() {
        this.fetchToken();
        return this.parseSubExp(TokenType.EOT);
    }
}
