// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Iterator;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Collections;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.Block;
import java.util.ArrayDeque;
import java.util.HashMap;
import jdk.nashorn.internal.IntDeque;
import java.util.Collection;
import jdk.nashorn.internal.ir.Expression;
import java.util.Deque;
import java.util.Map;
import jdk.nashorn.internal.ir.LexicalContext;

final class CodeGeneratorLexicalContext extends LexicalContext
{
    private int dynamicScopeCount;
    private final Map<SharedScopeCall, SharedScopeCall> scopeCalls;
    private final Deque<CompileUnit> compileUnits;
    private final Deque<MethodEmitter> methodEmitters;
    private final Deque<Expression> discard;
    private final Deque<Map<String, Collection<Label>>> unwarrantedOptimismHandlers;
    private final Deque<StringBuilder> slotTypesDescriptors;
    private final IntDeque splitNodes;
    private int[] nextFreeSlots;
    private int nextFreeSlotsSize;
    
    CodeGeneratorLexicalContext() {
        this.scopeCalls = new HashMap<SharedScopeCall, SharedScopeCall>();
        this.compileUnits = new ArrayDeque<CompileUnit>();
        this.methodEmitters = new ArrayDeque<MethodEmitter>();
        this.discard = new ArrayDeque<Expression>();
        this.unwarrantedOptimismHandlers = new ArrayDeque<Map<String, Collection<Label>>>();
        this.slotTypesDescriptors = new ArrayDeque<StringBuilder>();
        this.splitNodes = new IntDeque();
        this.nextFreeSlots = new int[16];
    }
    
    private boolean isWithBoundary(final Object node) {
        return node instanceof Block && !this.isEmpty() && this.peek() instanceof WithNode;
    }
    
    @Override
    public <T extends LexicalContextNode> T push(final T node) {
        if (this.isWithBoundary(node)) {
            ++this.dynamicScopeCount;
        }
        else if (node instanceof FunctionNode) {
            if (((FunctionNode)node).inDynamicContext()) {
                ++this.dynamicScopeCount;
            }
            this.splitNodes.push(0);
        }
        return super.push(node);
    }
    
    void enterSplitNode() {
        this.splitNodes.getAndIncrement();
        this.pushFreeSlots(this.methodEmitters.peek().getUsedSlotsWithLiveTemporaries());
    }
    
    void exitSplitNode() {
        final int count = this.splitNodes.decrementAndGet();
        assert count >= 0;
    }
    
    @Override
    public <T extends Node> T pop(final T node) {
        final T popped = super.pop(node);
        if (this.isWithBoundary(node)) {
            --this.dynamicScopeCount;
            assert this.dynamicScopeCount >= 0;
        }
        else if (node instanceof FunctionNode) {
            if (((FunctionNode)node).inDynamicContext()) {
                --this.dynamicScopeCount;
                assert this.dynamicScopeCount >= 0;
            }
            assert this.splitNodes.peek() == 0;
            this.splitNodes.pop();
        }
        return popped;
    }
    
    boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }
    
    boolean inSplitNode() {
        return !this.splitNodes.isEmpty() && this.splitNodes.peek() > 0;
    }
    
    MethodEmitter pushMethodEmitter(final MethodEmitter newMethod) {
        this.methodEmitters.push(newMethod);
        return newMethod;
    }
    
    MethodEmitter popMethodEmitter(final MethodEmitter oldMethod) {
        assert this.methodEmitters.peek() == oldMethod;
        this.methodEmitters.pop();
        return this.methodEmitters.isEmpty() ? null : this.methodEmitters.peek();
    }
    
    void pushUnwarrantedOptimismHandlers() {
        this.unwarrantedOptimismHandlers.push(new HashMap<String, Collection<Label>>());
        this.slotTypesDescriptors.push(new StringBuilder());
    }
    
    Map<String, Collection<Label>> getUnwarrantedOptimismHandlers() {
        return this.unwarrantedOptimismHandlers.peek();
    }
    
    Map<String, Collection<Label>> popUnwarrantedOptimismHandlers() {
        this.slotTypesDescriptors.pop();
        return this.unwarrantedOptimismHandlers.pop();
    }
    
    CompileUnit pushCompileUnit(final CompileUnit newUnit) {
        this.compileUnits.push(newUnit);
        return newUnit;
    }
    
    CompileUnit popCompileUnit(final CompileUnit oldUnit) {
        assert this.compileUnits.peek() == oldUnit;
        final CompileUnit unit = this.compileUnits.pop();
        assert unit.hasCode() : "compile unit popped without code";
        unit.setUsed();
        return this.compileUnits.isEmpty() ? null : this.compileUnits.peek();
    }
    
    boolean hasCompileUnits() {
        return !this.compileUnits.isEmpty();
    }
    
    Collection<SharedScopeCall> getScopeCalls() {
        return Collections.unmodifiableCollection((Collection<? extends SharedScopeCall>)this.scopeCalls.values());
    }
    
    SharedScopeCall getScopeCall(final CompileUnit unit, final Symbol symbol, final Type valueType, final Type returnType, final Type[] paramTypes, final int flags) {
        final SharedScopeCall scopeCall = new SharedScopeCall(symbol, valueType, returnType, paramTypes, flags);
        if (this.scopeCalls.containsKey(scopeCall)) {
            return this.scopeCalls.get(scopeCall);
        }
        scopeCall.setClassAndName(unit, this.getCurrentFunction().uniqueName(":scopeCall"));
        this.scopeCalls.put(scopeCall, scopeCall);
        return scopeCall;
    }
    
    SharedScopeCall getScopeGet(final CompileUnit unit, final Symbol symbol, final Type valueType, final int flags) {
        return this.getScopeCall(unit, symbol, valueType, valueType, null, flags);
    }
    
    void onEnterBlock(final Block block) {
        this.pushFreeSlots(this.assignSlots(block, this.isFunctionBody() ? 0 : this.getUsedSlotCount()));
    }
    
    private void pushFreeSlots(final int freeSlots) {
        if (this.nextFreeSlotsSize == this.nextFreeSlots.length) {
            final int[] newNextFreeSlots = new int[this.nextFreeSlotsSize * 2];
            System.arraycopy(this.nextFreeSlots, 0, newNextFreeSlots, 0, this.nextFreeSlotsSize);
            this.nextFreeSlots = newNextFreeSlots;
        }
        this.nextFreeSlots[this.nextFreeSlotsSize++] = freeSlots;
    }
    
    int getUsedSlotCount() {
        return this.nextFreeSlots[this.nextFreeSlotsSize - 1];
    }
    
    void releaseSlots() {
        --this.nextFreeSlotsSize;
        final int undefinedFromSlot = (this.nextFreeSlotsSize == 0) ? 0 : this.nextFreeSlots[this.nextFreeSlotsSize - 1];
        if (!this.slotTypesDescriptors.isEmpty()) {
            this.slotTypesDescriptors.peek().setLength(undefinedFromSlot);
        }
        this.methodEmitters.peek().undefineLocalVariables(undefinedFromSlot, false);
    }
    
    private int assignSlots(final Block block, final int firstSlot) {
        int fromSlot = firstSlot;
        final MethodEmitter method = this.methodEmitters.peek();
        for (final Symbol symbol : block.getSymbols()) {
            if (symbol.hasSlot()) {
                symbol.setFirstSlot(fromSlot);
                final int toSlot = fromSlot + symbol.slotCount();
                method.defineBlockLocalVariable(fromSlot, toSlot);
                fromSlot = toSlot;
            }
        }
        return fromSlot;
    }
    
    static Type getTypeForSlotDescriptor(final char typeDesc) {
        switch (typeDesc) {
            case 'I':
            case 'i': {
                return Type.INT;
            }
            case 'J':
            case 'j': {
                return Type.LONG;
            }
            case 'D':
            case 'd': {
                return Type.NUMBER;
            }
            case 'A':
            case 'a': {
                return Type.OBJECT;
            }
            case 'U':
            case 'u': {
                return Type.UNKNOWN;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    void pushDiscard(final Expression expr) {
        this.discard.push(expr);
    }
    
    boolean popDiscardIfCurrent(final Expression expr) {
        if (this.isCurrentDiscard(expr)) {
            this.discard.pop();
            return true;
        }
        return false;
    }
    
    boolean isCurrentDiscard(final Expression expr) {
        return this.discard.peek() == expr;
    }
    
    int quickSlot(final Type type) {
        return this.methodEmitters.peek().defineTemporaryLocalVariable(type.getSlots());
    }
}
