// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.linker.NashornGuards;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandle;

public final class WithObject extends Scope
{
    private static final MethodHandle WITHEXPRESSIONGUARD;
    private static final MethodHandle WITHEXPRESSIONFILTER;
    private static final MethodHandle WITHSCOPEFILTER;
    private static final MethodHandle BIND_TO_EXPRESSION_OBJ;
    private static final MethodHandle BIND_TO_EXPRESSION_FN;
    private final ScriptObject expression;
    
    WithObject(final ScriptObject scope, final ScriptObject expression) {
        super(scope, null);
        this.expression = expression;
    }
    
    @Override
    public boolean delete(final Object key, final boolean strict) {
        final ScriptObject self = this.expression;
        final String propName = JSType.toString(key);
        final FindProperty find = self.findProperty(propName, true);
        return find != null && self.delete(propName, strict);
    }
    
    @Override
    public GuardedInvocation lookup(final CallSiteDescriptor desc, final LinkRequest request) {
        if (request.isCallSiteUnstable()) {
            return super.lookup(desc, request);
        }
        final NashornCallSiteDescriptor ndesc = (NashornCallSiteDescriptor)desc;
        FindProperty find = null;
        GuardedInvocation link = null;
        boolean isNamedOperation;
        String name;
        if (desc.getNameTokenCount() > 2) {
            isNamedOperation = true;
            name = desc.getNameToken(2);
        }
        else {
            isNamedOperation = false;
            name = null;
        }
        final ScriptObject self = this.expression;
        if (isNamedOperation) {
            find = self.findProperty(name, true);
        }
        if (find != null) {
            link = self.lookup(desc, request);
            if (link != null) {
                return fixExpressionCallSite(ndesc, link);
            }
        }
        final ScriptObject scope = this.getProto();
        if (isNamedOperation) {
            find = scope.findProperty(name, true);
        }
        if (find != null) {
            return this.fixScopeCallSite(scope.lookup(desc, request), name, find.getOwner());
        }
        if (self != null) {
            final String s;
            final String operator = s = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
            String fallBack = null;
            switch (s) {
                case "callMethod": {
                    throw new AssertionError();
                }
                case "getMethod": {
                    fallBack = "__noSuchMethod__";
                    break;
                }
                case "getProp":
                case "getElem": {
                    fallBack = "__noSuchProperty__";
                    break;
                }
                default: {
                    fallBack = null;
                    break;
                }
            }
            if (fallBack != null) {
                find = self.findProperty(fallBack, true);
                if (find != null) {
                    final String s2 = operator;
                    switch (s2) {
                        case "getMethod": {
                            link = self.noSuchMethod(desc, request);
                            break;
                        }
                        case "getProp":
                        case "getElem": {
                            link = self.noSuchProperty(desc, request);
                            break;
                        }
                    }
                }
            }
            if (link != null) {
                return fixExpressionCallSite(ndesc, link);
            }
        }
        link = scope.lookup(desc, request);
        if (link != null) {
            return this.fixScopeCallSite(link, name, null);
        }
        return null;
    }
    
    @Override
    protected FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        final FindProperty exprProperty = this.expression.findProperty(key, true, this.expression);
        if (exprProperty != null) {
            return exprProperty;
        }
        return super.findProperty(key, deep, start);
    }
    
    @Override
    protected Object invokeNoSuchProperty(final String name, final boolean isScope, final int programPoint) {
        final FindProperty find = this.expression.findProperty("__noSuchProperty__", true);
        if (find != null) {
            final Object func = find.getObjectValue();
            if (func instanceof ScriptFunction) {
                final ScriptFunction sfunc = (ScriptFunction)func;
                final Object self = (isScope && sfunc.isStrict()) ? ScriptRuntime.UNDEFINED : this.expression;
                return ScriptRuntime.apply(sfunc, self, name);
            }
        }
        return this.getProto().invokeNoSuchProperty(name, isScope, programPoint);
    }
    
    @Override
    public void setSplitState(final int state) {
        ((Scope)this.getNonWithParent()).setSplitState(state);
    }
    
    @Override
    public int getSplitState() {
        return ((Scope)this.getNonWithParent()).getSplitState();
    }
    
    @Override
    public void addBoundProperties(final ScriptObject source, final Property[] properties) {
        this.getNonWithParent().addBoundProperties(source, properties);
    }
    
    private ScriptObject getNonWithParent() {
        ScriptObject proto;
        for (proto = this.getProto(); proto != null && proto instanceof WithObject; proto = proto.getProto()) {}
        return proto;
    }
    
    private static GuardedInvocation fixReceiverType(final GuardedInvocation link, final MethodHandle filter) {
        final MethodType invType = link.getInvocation().type();
        final MethodType newInvType = invType.changeParameterType(0, filter.type().returnType());
        return link.asType(newInvType);
    }
    
    private static GuardedInvocation fixExpressionCallSite(final NashornCallSiteDescriptor desc, final GuardedInvocation link) {
        if (!"getMethod".equals(desc.getFirstOperator())) {
            return fixReceiverType(link, WithObject.WITHEXPRESSIONFILTER).filterArguments(0, WithObject.WITHEXPRESSIONFILTER);
        }
        final MethodHandle linkInvocation = link.getInvocation();
        final MethodType linkType = linkInvocation.type();
        final boolean linkReturnsFunction = ScriptFunction.class.isAssignableFrom(linkType.returnType());
        return link.replaceMethods(Lookup.MH.foldArguments(linkReturnsFunction ? WithObject.BIND_TO_EXPRESSION_FN : WithObject.BIND_TO_EXPRESSION_OBJ, filterReceiver(linkInvocation.asType(linkType.changeReturnType((Class<?>)(linkReturnsFunction ? ScriptFunction.class : Object.class)).changeParameterType(0, (Class<?>)Object.class)), WithObject.WITHEXPRESSIONFILTER)), filterGuardReceiver(link, WithObject.WITHEXPRESSIONFILTER));
    }
    
    private GuardedInvocation fixScopeCallSite(final GuardedInvocation link, final String name, final ScriptObject owner) {
        final GuardedInvocation newLink = fixReceiverType(link, WithObject.WITHSCOPEFILTER);
        final MethodHandle expressionGuard = this.expressionGuard(name, owner);
        final MethodHandle filterGuardReceiver = filterGuardReceiver(newLink, WithObject.WITHSCOPEFILTER);
        return link.replaceMethods(filterReceiver(newLink.getInvocation(), WithObject.WITHSCOPEFILTER), NashornGuards.combineGuards(expressionGuard, filterGuardReceiver));
    }
    
    private static MethodHandle filterGuardReceiver(final GuardedInvocation link, final MethodHandle receiverFilter) {
        final MethodHandle test = link.getGuard();
        if (test == null) {
            return null;
        }
        final Class<?> receiverType = test.type().parameterType(0);
        final MethodHandle filter = Lookup.MH.asType(receiverFilter, receiverFilter.type().changeParameterType(0, receiverType).changeReturnType(receiverType));
        return filterReceiver(test, filter);
    }
    
    private static MethodHandle filterReceiver(final MethodHandle mh, final MethodHandle receiverFilter) {
        return Lookup.MH.filterArguments(mh, 0, receiverFilter.asType(receiverFilter.type().changeReturnType(mh.type().parameterType(0))));
    }
    
    public static Object withFilterExpression(final Object receiver) {
        return ((WithObject)receiver).expression;
    }
    
    private static Object bindToExpression(final Object fn, final Object receiver) {
        if (fn instanceof ScriptFunction) {
            return bindToExpression((ScriptFunction)fn, receiver);
        }
        if (fn instanceof ScriptObjectMirror) {
            final ScriptObjectMirror mirror = (ScriptObjectMirror)fn;
            if (mirror.isFunction()) {
                return new AbstractJSObject() {
                    @Override
                    public Object call(final Object thiz, final Object... args) {
                        return mirror.call(WithObject.withFilterExpression(receiver), args);
                    }
                };
            }
        }
        return fn;
    }
    
    private static Object bindToExpression(final ScriptFunction fn, final Object receiver) {
        return fn.createBound(withFilterExpression(receiver), ScriptRuntime.EMPTY_ARRAY);
    }
    
    private MethodHandle expressionGuard(final String name, final ScriptObject owner) {
        final PropertyMap map = this.expression.getMap();
        final SwitchPoint[] sp = this.expression.getProtoSwitchPoints(name, owner);
        return Lookup.MH.insertArguments(WithObject.WITHEXPRESSIONGUARD, 1, map, sp);
    }
    
    private static boolean withExpressionGuard(final Object receiver, final PropertyMap map, final SwitchPoint[] sp) {
        return ((WithObject)receiver).expression.getMap() == map && !hasBeenInvalidated(sp);
    }
    
    private static boolean hasBeenInvalidated(final SwitchPoint[] switchPoints) {
        if (switchPoints != null) {
            for (final SwitchPoint switchPoint : switchPoints) {
                if (switchPoint.hasBeenInvalidated()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Object withFilterScope(final Object receiver) {
        return ((WithObject)receiver).getProto();
    }
    
    public ScriptObject getExpression() {
        return this.expression;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), WithObject.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        WITHEXPRESSIONGUARD = findOwnMH("withExpressionGuard", Boolean.TYPE, Object.class, PropertyMap.class, SwitchPoint[].class);
        WITHEXPRESSIONFILTER = findOwnMH("withFilterExpression", Object.class, Object.class);
        WITHSCOPEFILTER = findOwnMH("withFilterScope", Object.class, Object.class);
        BIND_TO_EXPRESSION_OBJ = findOwnMH("bindToExpression", Object.class, Object.class, Object.class);
        BIND_TO_EXPRESSION_FN = findOwnMH("bindToExpression", Object.class, ScriptFunction.class, Object.class);
    }
}
