// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.LexicalContext;
import java.util.Collections;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.FunctionNode;
import java.util.ArrayDeque;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import java.util.Deque;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

class CacheAst extends SimpleNodeVisitor
{
    private final Deque<RecompilableScriptFunctionData> dataStack;
    private final Compiler compiler;
    
    CacheAst(final Compiler compiler) {
        this.dataStack = new ArrayDeque<RecompilableScriptFunctionData>();
        this.compiler = compiler;
        assert !compiler.isOnDemandCompilation();
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        final int id = functionNode.getId();
        this.dataStack.push(this.dataStack.isEmpty() ? this.compiler.getScriptFunctionData(id) : this.dataStack.peek().getScriptFunctionData(id));
        return true;
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        final RecompilableScriptFunctionData data = this.dataStack.pop();
        if (functionNode.isSplit()) {
            data.setCachedAst(functionNode);
        }
        if (!this.dataStack.isEmpty() && (this.dataStack.peek().getFunctionFlags() & 0x10) != 0x0) {
            return functionNode.setBody(this.lc, functionNode.getBody().setStatements(null, Collections.emptyList()));
        }
        return functionNode;
    }
}
