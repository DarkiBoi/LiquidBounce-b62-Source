// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.Collections;
import java.util.Iterator;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import java.util.RandomAccess;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class ObjectNode extends Expression implements LexicalContextNode, Splittable
{
    private static final long serialVersionUID = 1L;
    private final List<PropertyNode> elements;
    private final List<SplitRange> splitRanges;
    
    public ObjectNode(final long token, final int finish, final List<PropertyNode> elements) {
        super(token, finish);
        this.elements = elements;
        this.splitRanges = null;
        assert elements instanceof RandomAccess : "Splitting requires random access lists";
    }
    
    private ObjectNode(final ObjectNode objectNode, final List<PropertyNode> elements, final List<SplitRange> splitRanges) {
        super(objectNode);
        this.elements = elements;
        this.splitRanges = splitRanges;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        return Acceptor.accept(this, visitor);
    }
    
    @Override
    public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterObjectNode(this)) {
            return visitor.leaveObjectNode(this.setElements(lc, Node.accept(visitor, this.elements)));
        }
        return this;
    }
    
    @Override
    public Type getType() {
        return Type.OBJECT;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append('{');
        if (!this.elements.isEmpty()) {
            sb.append(' ');
            boolean first = true;
            for (final Node element : this.elements) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                element.toString(sb, printType);
            }
            sb.append(' ');
        }
        sb.append('}');
    }
    
    public List<PropertyNode> getElements() {
        return Collections.unmodifiableList((List<? extends PropertyNode>)this.elements);
    }
    
    private ObjectNode setElements(final LexicalContext lc, final List<PropertyNode> elements) {
        if (this.elements == elements) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ObjectNode(this, elements, this.splitRanges));
    }
    
    public ObjectNode setSplitRanges(final LexicalContext lc, final List<SplitRange> splitRanges) {
        if (this.splitRanges == splitRanges) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ObjectNode(this, this.elements, splitRanges));
    }
    
    @Override
    public List<SplitRange> getSplitRanges() {
        return (this.splitRanges == null) ? null : Collections.unmodifiableList((List<? extends SplitRange>)this.splitRanges);
    }
}
