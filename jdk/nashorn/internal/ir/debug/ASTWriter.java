// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.debug;

import java.util.Deque;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.TernaryNode;
import java.util.ArrayDeque;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Collection;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Terminal;
import java.util.LinkedList;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.IdentNode;
import java.lang.annotation.Annotation;
import jdk.nashorn.internal.ir.annotations.Reference;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;
import jdk.nashorn.internal.ir.Node;

public final class ASTWriter
{
    private final Node root;
    private static final int TABWIDTH = 4;
    
    public ASTWriter(final Node root) {
        this.root = root;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.printAST(sb, null, null, "root", this.root, 0);
        return sb.toString();
    }
    
    public Node[] toArray() {
        final List<Node> preorder = new ArrayList<Node>();
        this.printAST(new StringBuilder(), preorder, null, "root", this.root, 0);
        return preorder.toArray(new Node[preorder.size()]);
    }
    
    private void printAST(final StringBuilder sb, final List<Node> preorder, final Field field, final String name, final Node node, final int indent) {
        indent(sb, indent);
        if (node == null) {
            sb.append("[Object ");
            sb.append(name);
            sb.append(" null]\n");
            return;
        }
        if (preorder != null) {
            preorder.add(node);
        }
        final boolean isReference = field != null && field.isAnnotationPresent(Reference.class);
        final Class<?> clazz = node.getClass();
        String type = clazz.getName();
        type = type.substring(type.lastIndexOf(46) + 1, type.length());
        int truncate = type.indexOf("Node");
        if (truncate == -1) {
            truncate = type.indexOf("Statement");
        }
        if (truncate != -1) {
            type = type.substring(0, truncate);
        }
        type = type.toLowerCase();
        if (isReference) {
            type = "ref: " + type;
        }
        Symbol symbol;
        if (node instanceof IdentNode) {
            symbol = ((IdentNode)node).getSymbol();
        }
        else {
            symbol = null;
        }
        if (symbol != null) {
            type = type + ">" + symbol;
        }
        if (node instanceof Block && ((Block)node).needsScope()) {
            type += " <scope>";
        }
        final List<Field> children = new LinkedList<Field>();
        if (!isReference) {
            enqueueChildren(node, clazz, children);
        }
        String status = "";
        if (node instanceof Terminal && ((Terminal)node).isTerminal()) {
            status += " Terminal";
        }
        if (node instanceof Statement && ((Statement)node).hasGoto()) {
            status += " Goto ";
        }
        if (symbol != null) {
            status += symbol;
        }
        status = status.trim();
        if (!"".equals(status)) {
            status = " [" + status + "]";
        }
        if (symbol != null) {
            String tname = ((Expression)node).getType().toString();
            if (tname.indexOf(46) != -1) {
                tname = tname.substring(tname.lastIndexOf(46) + 1, tname.length());
            }
            status = status + " (" + tname + ")";
        }
        status = status + " @" + Debug.id(node);
        if (children.isEmpty()) {
            sb.append("[").append(type).append(' ').append(name).append(" = '").append(node).append("'").append(status).append("] ").append('\n');
        }
        else {
            sb.append("[").append(type).append(' ').append(name).append(' ').append(Token.toString(node.getToken())).append(status).append("]").append('\n');
            for (final Field child : children) {
                if (child.isAnnotationPresent(Ignore.class)) {
                    continue;
                }
                Object value;
                try {
                    value = child.get(node);
                }
                catch (IllegalArgumentException | IllegalAccessException ex2) {
                    final Exception ex;
                    final Exception e = ex;
                    Context.printStackTrace(e);
                    return;
                }
                if (value instanceof Node) {
                    this.printAST(sb, preorder, child, child.getName(), (Node)value, indent + 1);
                }
                else {
                    if (!(value instanceof Collection)) {
                        continue;
                    }
                    int pos = 0;
                    indent(sb, indent + 1);
                    sb.append('[').append(child.getName()).append("[0..").append(((Collection)value).size()).append("]]").append('\n');
                    for (final Node member : (Collection)value) {
                        this.printAST(sb, preorder, child, child.getName() + "[" + pos++ + "]", member, indent + 2);
                    }
                }
            }
        }
    }
    
    private static void enqueueChildren(final Node node, final Class<?> nodeClass, final List<Field> children) {
        final Deque<Class<?>> stack = new ArrayDeque<Class<?>>();
        Class<?> clazz = nodeClass;
        do {
            stack.push(clazz);
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        if (node instanceof TernaryNode) {
            stack.push(stack.removeLast());
        }
        final Iterator<Class<?>> iter = (node instanceof BinaryNode) ? stack.descendingIterator() : stack.iterator();
        while (iter.hasNext()) {
            final Class<?> c = iter.next();
            for (final Field f : c.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    final Object child = f.get(node);
                    if (child != null) {
                        if (child instanceof Node) {
                            children.add(f);
                        }
                        else if (child instanceof Collection && !((Collection)child).isEmpty()) {
                            children.add(f);
                        }
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException ex2) {
                    final Exception ex;
                    final Exception e = ex;
                    return;
                }
            }
        }
    }
    
    private static void indent(final StringBuilder sb, final int indent) {
        for (int i = 0; i < indent; ++i) {
            for (int j = 0; j < 4; ++j) {
                sb.append(' ');
            }
        }
    }
}
