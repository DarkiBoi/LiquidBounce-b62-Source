// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import java.io.Serializable;

public abstract class Node implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int NO_LINE_NUMBER = -1;
    public static final long NO_TOKEN = 0L;
    public static final int NO_FINISH = 0;
    protected final int start;
    protected int finish;
    private final long token;
    
    public Node(final long token, final int finish) {
        this.token = token;
        this.start = Token.descPosition(token);
        this.finish = finish;
    }
    
    protected Node(final long token, final int start, final int finish) {
        this.start = start;
        this.finish = finish;
        this.token = token;
    }
    
    protected Node(final Node node) {
        this.token = node.token;
        this.start = node.start;
        this.finish = node.finish;
    }
    
    public boolean isLoop() {
        return false;
    }
    
    public boolean isAssignment() {
        return false;
    }
    
    public Node ensureUniqueLabels(final LexicalContext lc) {
        return this;
    }
    
    public abstract Node accept(final NodeVisitor<? extends LexicalContext> p0);
    
    @Override
    public final String toString() {
        return this.toString(true);
    }
    
    public final String toString(final boolean includeTypeInfo) {
        final StringBuilder sb = new StringBuilder();
        this.toString(sb, includeTypeInfo);
        return sb.toString();
    }
    
    public void toString(final StringBuilder sb) {
        this.toString(sb, true);
    }
    
    public abstract void toString(final StringBuilder p0, final boolean p1);
    
    public int getFinish() {
        return this.finish;
    }
    
    public void setFinish(final int finish) {
        this.finish = finish;
    }
    
    public int getStart() {
        return this.start;
    }
    
    @Override
    protected Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError((Object)e);
        }
    }
    
    @Override
    public final boolean equals(final Object other) {
        return this == other;
    }
    
    @Override
    public final int hashCode() {
        return Long.hashCode(this.token);
    }
    
    public int position() {
        return Token.descPosition(this.token);
    }
    
    public int length() {
        return Token.descLength(this.token);
    }
    
    public TokenType tokenType() {
        return Token.descType(this.token);
    }
    
    public boolean isTokenType(final TokenType type) {
        return this.tokenType() == type;
    }
    
    public long getToken() {
        return this.token;
    }
    
    static <T extends Node> List<T> accept(final NodeVisitor<? extends LexicalContext> visitor, final List<T> list) {
        final int size = list.size();
        if (size == 0) {
            return list;
        }
        List<T> newList = null;
        for (int i = 0; i < size; ++i) {
            final T node = list.get(i);
            final T newNode = (T)((node == null) ? null : node.accept(visitor));
            if (newNode != node) {
                if (newList == null) {
                    newList = new ArrayList<T>(size);
                    for (int j = 0; j < i; ++j) {
                        newList.add(list.get(j));
                    }
                }
                newList.add(newNode);
            }
            else if (newList != null) {
                newList.add(node);
            }
        }
        return (newList == null) ? list : newList;
    }
    
    static <T extends LexicalContextNode> T replaceInLexicalContext(final LexicalContext lc, final T oldNode, final T newNode) {
        if (lc != null) {
            lc.replace(oldNode, newNode);
        }
        return newNode;
    }
}
