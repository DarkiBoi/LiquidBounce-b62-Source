// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.ArrayList;
import java.util.Collections;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Deque;

public class OptimisticLexicalContext extends LexicalContext
{
    private final boolean isEnabled;
    private final Deque<List<Assumption>> optimisticAssumptions;
    
    public OptimisticLexicalContext(final boolean isEnabled) {
        this.optimisticAssumptions = new ArrayDeque<List<Assumption>>();
        this.isEnabled = isEnabled;
    }
    
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public void logOptimisticAssumption(final Symbol symbol, final Type type) {
        if (this.isEnabled) {
            final List<Assumption> peek = this.optimisticAssumptions.peek();
            peek.add(new Assumption(symbol, type));
        }
    }
    
    public List<Assumption> getOptimisticAssumptions() {
        return Collections.unmodifiableList((List<? extends Assumption>)this.optimisticAssumptions.peek());
    }
    
    public boolean hasOptimisticAssumptions() {
        return !this.optimisticAssumptions.isEmpty() && !this.getOptimisticAssumptions().isEmpty();
    }
    
    @Override
    public <T extends LexicalContextNode> T push(final T node) {
        if (this.isEnabled && node instanceof FunctionNode) {
            this.optimisticAssumptions.push(new ArrayList<Assumption>());
        }
        return super.push(node);
    }
    
    @Override
    public <T extends Node> T pop(final T node) {
        final T popped = super.pop(node);
        if (this.isEnabled && node instanceof FunctionNode) {
            this.optimisticAssumptions.pop();
        }
        return popped;
    }
    
    class Assumption
    {
        Symbol symbol;
        Type type;
        
        Assumption(final Symbol symbol, final Type type) {
            this.symbol = symbol;
            this.type = type;
        }
        
        @Override
        public String toString() {
            return this.symbol.getName() + "=" + this.type;
        }
    }
}
