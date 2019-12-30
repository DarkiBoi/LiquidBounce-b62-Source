// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Deque;
import java.util.ArrayDeque;

public final class ConsString implements CharSequence
{
    private CharSequence left;
    private CharSequence right;
    private final int length;
    private volatile int state;
    private static final int STATE_NEW = 0;
    private static final int STATE_THRESHOLD = 2;
    private static final int STATE_FLATTENED = -1;
    
    public ConsString(final CharSequence left, final CharSequence right) {
        this.state = 0;
        assert JSType.isString(left);
        assert JSType.isString(right);
        this.left = left;
        this.right = right;
        this.length = left.length() + right.length();
        if (this.length < 0) {
            throw new IllegalArgumentException("too big concatenated String");
        }
    }
    
    @Override
    public String toString() {
        return (String)this.flattened(true);
    }
    
    @Override
    public int length() {
        return this.length;
    }
    
    @Override
    public char charAt(final int index) {
        return this.flattened(true).charAt(index);
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.flattened(true).subSequence(start, end);
    }
    
    public synchronized CharSequence[] getComponents() {
        return new CharSequence[] { this.left, this.right };
    }
    
    private CharSequence flattened(final boolean flattenNested) {
        if (this.state != -1) {
            this.flatten(flattenNested);
        }
        return this.left;
    }
    
    private synchronized void flatten(final boolean flattenNested) {
        final char[] chars = new char[this.length];
        int pos = this.length;
        final Deque<CharSequence> stack = new ArrayDeque<CharSequence>();
        stack.addFirst(this.left);
        CharSequence cs = this.right;
        do {
            if (cs instanceof ConsString) {
                final ConsString cons = (ConsString)cs;
                if (cons.state == -1 || (flattenNested && ++cons.state >= 2)) {
                    cs = cons.flattened(false);
                }
                else {
                    stack.addFirst(cons.left);
                    cs = cons.right;
                }
            }
            else {
                final String str = (String)cs;
                pos -= str.length();
                str.getChars(0, str.length(), chars, pos);
                cs = (stack.isEmpty() ? null : stack.pollFirst());
            }
        } while (cs != null);
        this.left = new String(chars);
        this.right = "";
        this.state = -1;
    }
}
