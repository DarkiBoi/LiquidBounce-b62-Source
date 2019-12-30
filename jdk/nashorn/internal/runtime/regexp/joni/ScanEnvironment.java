// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;

public final class ScanEnvironment
{
    private static final int SCANENV_MEMNODES_SIZE = 8;
    int option;
    final int caseFoldFlag;
    public final Syntax syntax;
    int captureHistory;
    int btMemStart;
    int btMemEnd;
    int backrefedMem;
    public final Regex reg;
    public int numMem;
    public Node[] memNodes;
    
    public ScanEnvironment(final Regex regex, final Syntax syntax) {
        this.reg = regex;
        this.option = regex.options;
        this.caseFoldFlag = regex.caseFoldFlag;
        this.syntax = syntax;
    }
    
    public void clear() {
        this.captureHistory = BitStatus.bsClear();
        this.btMemStart = BitStatus.bsClear();
        this.btMemEnd = BitStatus.bsClear();
        this.backrefedMem = BitStatus.bsClear();
        this.numMem = 0;
        this.memNodes = null;
    }
    
    public int addMemEntry() {
        if (this.numMem++ == 0) {
            this.memNodes = new Node[8];
        }
        else if (this.numMem >= this.memNodes.length) {
            final Node[] tmp = new Node[this.memNodes.length << 1];
            System.arraycopy(this.memNodes, 0, tmp, 0, this.memNodes.length);
            this.memNodes = tmp;
        }
        return this.numMem;
    }
    
    public void setMemNode(final int num, final Node node) {
        if (this.numMem >= num) {
            this.memNodes[num] = node;
            return;
        }
        throw new InternalException("internal parser error (bug)");
    }
    
    public int convertBackslashValue(final int c) {
        if (this.syntax.opEscControlChars()) {
            switch (c) {
                case 110: {
                    return 10;
                }
                case 116: {
                    return 9;
                }
                case 114: {
                    return 13;
                }
                case 102: {
                    return 12;
                }
                case 97: {
                    return 7;
                }
                case 98: {
                    return 8;
                }
                case 101: {
                    return 27;
                }
                case 118: {
                    if (this.syntax.op2EscVVtab()) {
                        return 11;
                    }
                    break;
                }
            }
        }
        return c;
    }
    
    void ccEscWarn(final String s) {
        if (this.syntax.warnCCOpNotEscaped() && this.syntax.backSlashEscapeInCC()) {
            this.reg.warnings.warn("character class has '" + s + "' without escape");
        }
    }
}
