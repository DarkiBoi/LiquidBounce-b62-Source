// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.ObjectNode;
import java.util.Iterator;
import java.util.List;
import jdk.nashorn.internal.ir.Splittable;
import java.util.ArrayList;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.CompileUnitHolder;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

abstract class ReplaceCompileUnits extends SimpleNodeVisitor
{
    abstract CompileUnit getReplacement(final CompileUnit p0);
    
    CompileUnit getExistingReplacement(final CompileUnitHolder node) {
        final CompileUnit oldUnit = node.getCompileUnit();
        assert oldUnit != null;
        final CompileUnit newUnit = this.getReplacement(oldUnit);
        assert newUnit != null;
        return newUnit;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode node) {
        return node.setCompileUnit(this.lc, this.getExistingReplacement(node));
    }
    
    @Override
    public Node leaveLiteralNode(final LiteralNode<?> node) {
        if (!(node instanceof LiteralNode.ArrayLiteralNode)) {
            return node;
        }
        final LiteralNode.ArrayLiteralNode aln = (LiteralNode.ArrayLiteralNode)node;
        if (aln.getSplitRanges() == null) {
            return node;
        }
        final List<Splittable.SplitRange> newArrayUnits = new ArrayList<Splittable.SplitRange>();
        for (final Splittable.SplitRange au : aln.getSplitRanges()) {
            newArrayUnits.add(new Splittable.SplitRange(this.getExistingReplacement(au), au.getLow(), au.getHigh()));
        }
        return aln.setSplitRanges(this.lc, newArrayUnits);
    }
    
    @Override
    public Node leaveObjectNode(final ObjectNode objectNode) {
        final List<Splittable.SplitRange> ranges = objectNode.getSplitRanges();
        if (ranges != null) {
            final List<Splittable.SplitRange> newRanges = new ArrayList<Splittable.SplitRange>();
            for (final Splittable.SplitRange range : ranges) {
                newRanges.add(new Splittable.SplitRange(this.getExistingReplacement(range), range.getLow(), range.getHigh()));
            }
            return objectNode.setSplitRanges(this.lc, newRanges);
        }
        return super.leaveObjectNode(objectNode);
    }
}
