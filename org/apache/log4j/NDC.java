// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.util.Enumeration;
import org.apache.log4j.helpers.LogLog;
import java.util.Vector;
import java.util.Stack;
import java.util.Hashtable;

public class NDC
{
    static Hashtable ht;
    static int pushCounter;
    static final int REAP_THRESHOLD = 5;
    
    private NDC() {
    }
    
    private static Stack getCurrentStack() {
        if (NDC.ht != null) {
            return NDC.ht.get(Thread.currentThread());
        }
        return null;
    }
    
    public static void clear() {
        final Stack stack = getCurrentStack();
        if (stack != null) {
            stack.setSize(0);
        }
    }
    
    public static Stack cloneStack() {
        final Stack stack = getCurrentStack();
        if (stack == null) {
            return null;
        }
        return (Stack)stack.clone();
    }
    
    public static void inherit(final Stack stack) {
        if (stack != null) {
            NDC.ht.put(Thread.currentThread(), stack);
        }
    }
    
    public static String get() {
        final Stack s = getCurrentStack();
        if (s != null && !s.isEmpty()) {
            return s.peek().fullMessage;
        }
        return null;
    }
    
    public static int getDepth() {
        final Stack stack = getCurrentStack();
        if (stack == null) {
            return 0;
        }
        return stack.size();
    }
    
    private static void lazyRemove() {
        if (NDC.ht == null) {
            return;
        }
        final Vector v;
        synchronized (NDC.ht) {
            if (++NDC.pushCounter <= 5) {
                return;
            }
            NDC.pushCounter = 0;
            int misses = 0;
            v = new Vector();
            final Enumeration enumeration = NDC.ht.keys();
            while (enumeration.hasMoreElements() && misses <= 4) {
                final Thread t = enumeration.nextElement();
                if (t.isAlive()) {
                    ++misses;
                }
                else {
                    misses = 0;
                    v.addElement(t);
                }
            }
        }
        for (int size = v.size(), i = 0; i < size; ++i) {
            final Thread t2 = v.elementAt(i);
            LogLog.debug("Lazy NDC removal for thread [" + t2.getName() + "] (" + NDC.ht.size() + ").");
            NDC.ht.remove(t2);
        }
    }
    
    public static String pop() {
        final Stack stack = getCurrentStack();
        if (stack != null && !stack.isEmpty()) {
            return stack.pop().message;
        }
        return "";
    }
    
    public static String peek() {
        final Stack stack = getCurrentStack();
        if (stack != null && !stack.isEmpty()) {
            return stack.peek().message;
        }
        return "";
    }
    
    public static void push(final String message) {
        Stack stack = getCurrentStack();
        if (stack == null) {
            final DiagnosticContext dc = new DiagnosticContext(message, null);
            stack = new Stack();
            final Thread key = Thread.currentThread();
            NDC.ht.put(key, stack);
            stack.push(dc);
        }
        else if (stack.isEmpty()) {
            final DiagnosticContext dc = new DiagnosticContext(message, null);
            stack.push(dc);
        }
        else {
            final DiagnosticContext parent = stack.peek();
            stack.push(new DiagnosticContext(message, parent));
        }
    }
    
    public static void remove() {
        if (NDC.ht != null) {
            NDC.ht.remove(Thread.currentThread());
            lazyRemove();
        }
    }
    
    public static void setMaxDepth(final int maxDepth) {
        final Stack stack = getCurrentStack();
        if (stack != null && maxDepth < stack.size()) {
            stack.setSize(maxDepth);
        }
    }
    
    static {
        NDC.ht = new Hashtable();
        NDC.pushCounter = 0;
    }
    
    private static class DiagnosticContext
    {
        String fullMessage;
        String message;
        
        DiagnosticContext(final String message, final DiagnosticContext parent) {
            this.message = message;
            if (parent != null) {
                this.fullMessage = parent.fullMessage + ' ' + message;
            }
            else {
                this.fullMessage = message;
            }
        }
    }
}
