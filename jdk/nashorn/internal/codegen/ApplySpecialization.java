// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.options.Options;
import java.net.URL;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.objects.Global;
import java.util.ArrayList;
import java.util.Collection;
import jdk.nashorn.internal.ir.Node;
import java.util.Iterator;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.runtime.Context;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.ir.IdentNode;
import java.util.List;
import java.util.Deque;
import java.util.Set;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

@Logger(name = "apply2call")
public final class ApplySpecialization extends SimpleNodeVisitor implements Loggable
{
    private static final boolean USE_APPLY2CALL;
    private final DebugLogger log;
    private final Compiler compiler;
    private final Set<Integer> changed;
    private final Deque<List<IdentNode>> explodedArguments;
    private final Deque<MethodType> callSiteTypes;
    private static final String ARGUMENTS;
    private static final AppliesFoundException HAS_APPLIES;
    
    public ApplySpecialization(final Compiler compiler) {
        this.changed = new HashSet<Integer>();
        this.explodedArguments = new ArrayDeque<List<IdentNode>>();
        this.callSiteTypes = new ArrayDeque<MethodType>();
        this.compiler = compiler;
        this.log = this.initLogger(compiler.getContext());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    private boolean hasApplies(final FunctionNode functionNode) {
        try {
            functionNode.accept(new SimpleNodeVisitor() {
                @Override
                public boolean enterFunctionNode(final FunctionNode fn) {
                    return fn == functionNode;
                }
                
                @Override
                public boolean enterCallNode(final CallNode callNode) {
                    if (isApply(callNode)) {
                        throw ApplySpecialization.HAS_APPLIES;
                    }
                    return true;
                }
            });
        }
        catch (AppliesFoundException e) {
            return true;
        }
        this.log.fine("There are no applies in ", DebugLogger.quote(functionNode.getName()), " - nothing to do.");
        return false;
    }
    
    private static void checkValidTransform(final FunctionNode functionNode) {
        final Set<Expression> argumentsFound = new HashSet<Expression>();
        final Deque<Set<Expression>> stack = new ArrayDeque<Set<Expression>>();
        functionNode.accept(new SimpleNodeVisitor() {
            private boolean isCurrentArg(final Expression expr) {
                return !stack.isEmpty() && stack.peek().contains(expr);
            }
            
            private boolean isArguments(final Expression expr) {
                if (expr instanceof IdentNode && ApplySpecialization.ARGUMENTS.equals(((IdentNode)expr).getName())) {
                    argumentsFound.add(expr);
                    return true;
                }
                return false;
            }
            
            private boolean isParam(final String name) {
                for (final IdentNode param : functionNode.getParameters()) {
                    if (param.getName().equals(name)) {
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public Node leaveIdentNode(final IdentNode identNode) {
                if (this.isParam(identNode.getName())) {
                    throw new TransformFailedException(this.lc.getCurrentFunction(), "parameter: " + identNode.getName());
                }
                if (this.isArguments(identNode) && !this.isCurrentArg(identNode)) {
                    throw new TransformFailedException(this.lc.getCurrentFunction(), "is 'arguments': " + identNode.getName());
                }
                return identNode;
            }
            
            @Override
            public boolean enterCallNode(final CallNode callNode) {
                final Set<Expression> callArgs = new HashSet<Expression>();
                if (isApply(callNode)) {
                    final List<Expression> argList = callNode.getArgs();
                    if (argList.size() != 2 || !this.isArguments(argList.get(argList.size() - 1))) {
                        throw new TransformFailedException(this.lc.getCurrentFunction(), "argument pattern not matched: " + argList);
                    }
                    callArgs.addAll(callNode.getArgs());
                }
                stack.push(callArgs);
                return true;
            }
            
            @Override
            public Node leaveCallNode(final CallNode callNode) {
                stack.pop();
                return callNode;
            }
        });
    }
    
    @Override
    public boolean enterCallNode(final CallNode callNode) {
        return !this.explodedArguments.isEmpty();
    }
    
    @Override
    public Node leaveCallNode(final CallNode callNode) {
        final List<IdentNode> newParams = this.explodedArguments.peek();
        if (isApply(callNode)) {
            final List<Expression> newArgs = new ArrayList<Expression>();
            for (final Expression arg : callNode.getArgs()) {
                if (arg instanceof IdentNode && ApplySpecialization.ARGUMENTS.equals(((IdentNode)arg).getName())) {
                    newArgs.addAll(newParams);
                }
                else {
                    newArgs.add(arg);
                }
            }
            this.changed.add(this.lc.getCurrentFunction().getId());
            final CallNode newCallNode = callNode.setArgs(newArgs).setIsApplyToCall();
            if (this.log.isEnabled()) {
                this.log.fine("Transformed ", callNode, " from apply to call => ", newCallNode, " in ", DebugLogger.quote(this.lc.getCurrentFunction().getName()));
            }
            return newCallNode;
        }
        return callNode;
    }
    
    private void pushExplodedArgs(final FunctionNode functionNode) {
        int start = 0;
        final MethodType actualCallSiteType = this.compiler.getCallSiteType(functionNode);
        if (actualCallSiteType == null) {
            throw new TransformFailedException(this.lc.getCurrentFunction(), "No callsite type");
        }
        assert actualCallSiteType.parameterType(actualCallSiteType.parameterCount() - 1) != Object[].class : "error vararg callsite passed to apply2call " + functionNode.getName() + " " + actualCallSiteType;
        final TypeMap ptm = this.compiler.getTypeMap();
        if (ptm.needsCallee()) {
            ++start;
        }
        ++start;
        assert functionNode.getNumOfParams() == 0 : "apply2call on function with named paramaters!";
        final List<IdentNode> newParams = new ArrayList<IdentNode>();
        final long to = actualCallSiteType.parameterCount() - start;
        for (int i = 0; i < to; ++i) {
            newParams.add(new IdentNode(functionNode.getToken(), functionNode.getFinish(), CompilerConstants.EXPLODED_ARGUMENT_PREFIX.symbolName() + i));
        }
        this.callSiteTypes.push(actualCallSiteType);
        this.explodedArguments.push(newParams);
    }
    
    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode) {
        if (!ApplySpecialization.USE_APPLY2CALL || !this.compiler.isOnDemandCompilation() || !functionNode.needsArguments() || functionNode.hasEval() || functionNode.getNumOfParams() != 0) {
            return false;
        }
        if (!Global.isBuiltinFunctionPrototypeApply()) {
            this.log.fine("Apply transform disabled: apply/call overridden");
            assert !Global.isBuiltinFunctionPrototypeCall() : "call and apply should have the same SwitchPoint";
            return false;
        }
        else {
            if (!this.hasApplies(functionNode)) {
                return false;
            }
            if (this.log.isEnabled()) {
                this.log.info("Trying to specialize apply to call in '", functionNode.getName(), "' params=", functionNode.getParameters(), " id=", functionNode.getId(), " source=", massageURL(functionNode.getSource().getURL()));
            }
            try {
                checkValidTransform(functionNode);
                this.pushExplodedArgs(functionNode);
            }
            catch (TransformFailedException e) {
                this.log.info("Failure: ", e.getMessage());
                return false;
            }
            return true;
        }
    }
    
    @Override
    public Node leaveFunctionNode(final FunctionNode functionNode) {
        FunctionNode newFunctionNode = functionNode;
        final String functionName = newFunctionNode.getName();
        if (this.changed.contains(newFunctionNode.getId())) {
            newFunctionNode = newFunctionNode.clearFlag(this.lc, 8).setFlag(this.lc, 4096).setParameters(this.lc, this.explodedArguments.peek());
            if (this.log.isEnabled()) {
                this.log.info("Success: ", massageURL(newFunctionNode.getSource().getURL()), '.', functionName, "' id=", newFunctionNode.getId(), " params=", this.callSiteTypes.peek());
            }
        }
        this.callSiteTypes.pop();
        this.explodedArguments.pop();
        return newFunctionNode;
    }
    
    private static boolean isApply(final CallNode callNode) {
        final Expression f = callNode.getFunction();
        return f instanceof AccessNode && "apply".equals(((AccessNode)f).getProperty());
    }
    
    private static String massageURL(final URL url) {
        if (url == null) {
            return "<null>";
        }
        final String str = url.toString();
        final int slash = str.lastIndexOf(47);
        if (slash == -1) {
            return str;
        }
        return str.substring(slash + 1);
    }
    
    static {
        USE_APPLY2CALL = Options.getBooleanProperty("nashorn.apply2call", true);
        ARGUMENTS = CompilerConstants.ARGUMENTS_VAR.symbolName();
        HAS_APPLIES = new AppliesFoundException();
    }
    
    private static class TransformFailedException extends RuntimeException
    {
        TransformFailedException(final FunctionNode fn, final String message) {
            super(massageURL(fn.getSource().getURL()) + '.' + fn.getName() + " => " + message, null, false, false);
        }
    }
    
    private static class AppliesFoundException extends RuntimeException
    {
        AppliesFoundException() {
            super("applies_found", null, false, false);
        }
    }
}
