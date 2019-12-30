// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;

abstract class Compiler implements ErrorMessages
{
    protected final Analyser analyser;
    protected final Regex regex;
    
    protected Compiler(final Analyser analyser) {
        this.analyser = analyser;
        this.regex = analyser.regex;
    }
    
    final void compile() {
        this.prepare();
        this.compileTree(this.analyser.root);
        this.finish();
    }
    
    protected abstract void prepare();
    
    protected abstract void finish();
    
    protected abstract void compileAltNode(final ConsAltNode p0);
    
    private void compileStringRawNode(final StringNode sn) {
        if (sn.length() <= 0) {
            return;
        }
        this.addCompileString(sn.chars, sn.p, sn.length(), false);
    }
    
    private void compileStringNode(final StringNode node) {
        final StringNode sn = node;
        if (sn.length() <= 0) {
            return;
        }
        final boolean ambig = sn.isAmbig();
        int p;
        final int prev = p = sn.p;
        final int end = sn.end;
        final char[] chars = sn.chars;
        ++p;
        int slen = 1;
        while (p < end) {
            ++slen;
            ++p;
        }
        this.addCompileString(chars, prev, slen, ambig);
    }
    
    protected abstract void addCompileString(final char[] p0, final int p1, final int p2, final boolean p3);
    
    protected abstract void compileCClassNode(final CClassNode p0);
    
    protected abstract void compileAnyCharNode();
    
    protected abstract void compileBackrefNode(final BackRefNode p0);
    
    protected abstract void compileNonCECQuantifierNode(final QuantifierNode p0);
    
    protected abstract void compileOptionNode(final EncloseNode p0);
    
    protected abstract void compileEncloseNode(final EncloseNode p0);
    
    protected abstract void compileAnchorNode(final AnchorNode p0);
    
    protected final void compileTree(final Node node) {
        switch (node.getType()) {
            case 8: {
                ConsAltNode lin = (ConsAltNode)node;
                do {
                    this.compileTree(lin.car);
                } while ((lin = lin.cdr) != null);
                break;
            }
            case 9: {
                this.compileAltNode((ConsAltNode)node);
                break;
            }
            case 0: {
                final StringNode sn = (StringNode)node;
                if (sn.isRaw()) {
                    this.compileStringRawNode(sn);
                    break;
                }
                this.compileStringNode(sn);
                break;
            }
            case 1: {
                this.compileCClassNode((CClassNode)node);
                break;
            }
            case 3: {
                this.compileAnyCharNode();
                break;
            }
            case 4: {
                this.compileBackrefNode((BackRefNode)node);
                break;
            }
            case 5: {
                this.compileNonCECQuantifierNode((QuantifierNode)node);
                break;
            }
            case 6: {
                final EncloseNode enode = (EncloseNode)node;
                if (enode.isOption()) {
                    this.compileOptionNode(enode);
                    break;
                }
                this.compileEncloseNode(enode);
                break;
            }
            case 7: {
                this.compileAnchorNode((AnchorNode)node);
                break;
            }
            default: {
                this.newInternalException("internal parser error (bug)");
                break;
            }
        }
    }
    
    protected final void compileTreeNTimes(final Node node, final int n) {
        for (int i = 0; i < n; ++i) {
            this.compileTree(node);
        }
    }
    
    protected void newSyntaxException(final String message) {
        throw new SyntaxException(message);
    }
    
    protected void newInternalException(final String message) {
        throw new InternalException(message);
    }
}
