// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.LexicalContext;

public abstract class SimpleNodeVisitor extends NodeVisitor<LexicalContext>
{
    public SimpleNodeVisitor() {
        super(new LexicalContext());
    }
}
