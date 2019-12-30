// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.objects.ArrayBufferView;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.lang.invoke.MethodType;

final class TypeEvaluator
{
    private static final MethodType EMPTY_INVOCATION_TYPE;
    private final Compiler compiler;
    private final ScriptObject runtimeScope;
    
    TypeEvaluator(final Compiler compiler, final ScriptObject runtimeScope) {
        this.compiler = compiler;
        this.runtimeScope = runtimeScope;
    }
    
    boolean hasStringPropertyIterator(final Expression expr) {
        return this.evaluateSafely(expr) instanceof ScriptObject;
    }
    
    Type getOptimisticType(final Optimistic node) {
        assert this.compiler.useOptimisticTypes();
        final int programPoint = node.getProgramPoint();
        final Type validType = this.compiler.getInvalidatedProgramPointType(programPoint);
        if (validType != null) {
            return validType;
        }
        final Type mostOptimisticType = node.getMostOptimisticType();
        final Type evaluatedType = this.getEvaluatedType(node);
        if (evaluatedType != null) {
            if (evaluatedType.widerThan(mostOptimisticType)) {
                final Type newValidType = (evaluatedType.isObject() || evaluatedType.isBoolean()) ? Type.OBJECT : evaluatedType;
                this.compiler.addInvalidatedProgramPoint(node.getProgramPoint(), newValidType);
            }
            return evaluatedType;
        }
        return mostOptimisticType;
    }
    
    private static Type getPropertyType(final ScriptObject sobj, final String name) {
        final FindProperty find = sobj.findProperty(name, true);
        if (find == null) {
            return null;
        }
        final Property property = find.getProperty();
        final Class<?> propertyClass = property.getType();
        if (propertyClass == null) {
            return null;
        }
        if (propertyClass.isPrimitive()) {
            return Type.typeFor(propertyClass);
        }
        final ScriptObject owner = find.getOwner();
        if (property.hasGetterFunction(owner)) {
            return Type.OBJECT;
        }
        final Object value = property.needsDeclaration() ? ScriptRuntime.UNDEFINED : property.getObjectValue(owner, owner);
        if (value == ScriptRuntime.UNDEFINED) {
            return null;
        }
        return Type.typeFor(JSType.unboxedFieldType(value));
    }
    
    void declareLocalSymbol(final String symbolName) {
        assert this.compiler.useOptimisticTypes() && this.compiler.isOnDemandCompilation() && this.runtimeScope != null : "useOptimistic=" + this.compiler.useOptimisticTypes() + " isOnDemand=" + this.compiler.isOnDemandCompilation() + " scope=" + this.runtimeScope;
        if (this.runtimeScope.findProperty(symbolName, false) == null) {
            this.runtimeScope.addOwnProperty(symbolName, 7, ScriptRuntime.UNDEFINED);
        }
    }
    
    private Object evaluateSafely(final Expression expr) {
        if (expr instanceof IdentNode) {
            return (this.runtimeScope == null) ? null : evaluatePropertySafely(this.runtimeScope, ((IdentNode)expr).getName());
        }
        if (!(expr instanceof AccessNode)) {
            return null;
        }
        final AccessNode accessNode = (AccessNode)expr;
        final Object base = this.evaluateSafely(accessNode.getBase());
        if (!(base instanceof ScriptObject)) {
            return null;
        }
        return evaluatePropertySafely((ScriptObject)base, accessNode.getProperty());
    }
    
    private static Object evaluatePropertySafely(final ScriptObject sobj, final String name) {
        final FindProperty find = sobj.findProperty(name, true);
        if (find == null) {
            return null;
        }
        final Property property = find.getProperty();
        final ScriptObject owner = find.getOwner();
        if (property.hasGetterFunction(owner)) {
            return null;
        }
        return property.getObjectValue(owner, owner);
    }
    
    private Type getEvaluatedType(final Optimistic expr) {
        if (expr instanceof IdentNode) {
            if (this.runtimeScope == null) {
                return null;
            }
            return getPropertyType(this.runtimeScope, ((IdentNode)expr).getName());
        }
        else {
            if (!(expr instanceof AccessNode)) {
                if (expr instanceof IndexNode) {
                    final IndexNode indexNode = (IndexNode)expr;
                    final Object base = this.evaluateSafely(indexNode.getBase());
                    if (base instanceof NativeArray || base instanceof ArrayBufferView) {
                        return ((ScriptObject)base).getArray().getOptimisticType();
                    }
                }
                else if (expr instanceof CallNode) {
                    final CallNode callExpr = (CallNode)expr;
                    final Expression fnExpr = callExpr.getFunction();
                    if (fnExpr instanceof FunctionNode && this.compiler.getContext().getEnv()._lazy_compilation) {
                        final FunctionNode fn = (FunctionNode)fnExpr;
                        if (callExpr.getArgs().isEmpty()) {
                            final RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(fn.getId());
                            if (data != null) {
                                final Type returnType = Type.typeFor(data.getReturnType(TypeEvaluator.EMPTY_INVOCATION_TYPE, this.runtimeScope));
                                if (returnType == Type.BOOLEAN) {
                                    return Type.OBJECT;
                                }
                                assert returnType == Type.OBJECT;
                                return returnType;
                            }
                        }
                    }
                }
                return null;
            }
            final AccessNode accessNode = (AccessNode)expr;
            final Object base = this.evaluateSafely(accessNode.getBase());
            if (!(base instanceof ScriptObject)) {
                return null;
            }
            return getPropertyType((ScriptObject)base, accessNode.getProperty());
        }
    }
    
    static {
        EMPTY_INVOCATION_TYPE = MethodType.methodType(Object.class, ScriptFunction.class, Object.class);
    }
}
