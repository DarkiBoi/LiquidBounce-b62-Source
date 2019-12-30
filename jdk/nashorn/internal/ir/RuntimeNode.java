// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.parser.TokenType;
import java.util.Collections;
import java.util.Iterator;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.Arrays;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class RuntimeNode extends Expression
{
    private static final long serialVersionUID = 1L;
    private final Request request;
    private final List<Expression> args;
    
    public RuntimeNode(final long token, final int finish, final Request request, final List<Expression> args) {
        super(token, finish);
        this.request = request;
        this.args = args;
    }
    
    private RuntimeNode(final RuntimeNode runtimeNode, final Request request, final List<Expression> args) {
        super(runtimeNode);
        this.request = request;
        this.args = args;
    }
    
    public RuntimeNode(final long token, final int finish, final Request request, final Expression... args) {
        this(token, finish, request, Arrays.asList(args));
    }
    
    public RuntimeNode(final Expression parent, final Request request, final Expression... args) {
        this(parent, request, Arrays.asList(args));
    }
    
    public RuntimeNode(final Expression parent, final Request request, final List<Expression> args) {
        super(parent);
        this.request = request;
        this.args = args;
    }
    
    public RuntimeNode(final UnaryNode parent, final Request request) {
        this(parent, request, new Expression[] { parent.getExpression() });
    }
    
    public RuntimeNode(final BinaryNode parent) {
        this(parent, Request.requestFor(parent), new Expression[] { parent.lhs(), parent.rhs() });
    }
    
    public RuntimeNode setRequest(final Request request) {
        if (this.request == request) {
            return this;
        }
        return new RuntimeNode(this, request, this.args);
    }
    
    @Override
    public Type getType() {
        return this.request.getReturnType();
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterRuntimeNode(this)) {
            return visitor.leaveRuntimeNode(this.setArgs(Node.accept(visitor, this.args)));
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append("ScriptRuntime.");
        sb.append(this.request);
        sb.append('(');
        boolean first = true;
        for (final Node arg : this.args) {
            if (!first) {
                sb.append(", ");
            }
            else {
                first = false;
            }
            arg.toString(sb, printType);
        }
        sb.append(')');
    }
    
    public List<Expression> getArgs() {
        return Collections.unmodifiableList((List<? extends Expression>)this.args);
    }
    
    public RuntimeNode setArgs(final List<Expression> args) {
        if (this.args == args) {
            return this;
        }
        return new RuntimeNode(this, this.request, args);
    }
    
    public Request getRequest() {
        return this.request;
    }
    
    public boolean isPrimitive() {
        for (final Expression arg : this.args) {
            if (arg.getType().isObject()) {
                return false;
            }
        }
        return true;
    }
    
    public enum Request
    {
        ADD(TokenType.ADD, Type.OBJECT, 2, true), 
        DEBUGGER, 
        NEW, 
        TYPEOF, 
        REFERENCE_ERROR, 
        DELETE(TokenType.DELETE, Type.BOOLEAN, 1), 
        SLOW_DELETE(TokenType.DELETE, Type.BOOLEAN, 1, false), 
        FAIL_DELETE(TokenType.DELETE, Type.BOOLEAN, 1, false), 
        EQ_STRICT(TokenType.EQ_STRICT, Type.BOOLEAN, 2, true), 
        EQ(TokenType.EQ, Type.BOOLEAN, 2, true), 
        GE(TokenType.GE, Type.BOOLEAN, 2, true), 
        GT(TokenType.GT, Type.BOOLEAN, 2, true), 
        IN(TokenType.IN, Type.BOOLEAN, 2), 
        INSTANCEOF(TokenType.INSTANCEOF, Type.BOOLEAN, 2), 
        LE(TokenType.LE, Type.BOOLEAN, 2, true), 
        LT(TokenType.LT, Type.BOOLEAN, 2, true), 
        NE_STRICT(TokenType.NE_STRICT, Type.BOOLEAN, 2, true), 
        NE(TokenType.NE, Type.BOOLEAN, 2, true), 
        IS_UNDEFINED(TokenType.EQ_STRICT, Type.BOOLEAN, 2), 
        IS_NOT_UNDEFINED(TokenType.NE_STRICT, Type.BOOLEAN, 2);
        
        private final TokenType tokenType;
        private final Type returnType;
        private final int arity;
        private final boolean canSpecialize;
        
        private Request() {
            this(TokenType.VOID, Type.OBJECT, 0);
        }
        
        private Request(final TokenType tokenType, final Type returnType, final int arity) {
            this(tokenType, returnType, arity, false);
        }
        
        private Request(final TokenType tokenType, final Type returnType, final int arity, final boolean canSpecialize) {
            this.tokenType = tokenType;
            this.returnType = returnType;
            this.arity = arity;
            this.canSpecialize = canSpecialize;
        }
        
        public boolean canSpecialize() {
            return this.canSpecialize;
        }
        
        public int getArity() {
            return this.arity;
        }
        
        public Type getReturnType() {
            return this.returnType;
        }
        
        public TokenType getTokenType() {
            return this.tokenType;
        }
        
        public String nonStrictName() {
            switch (this) {
                case NE_STRICT: {
                    return Request.NE.name();
                }
                case EQ_STRICT: {
                    return Request.EQ.name();
                }
                default: {
                    return this.name();
                }
            }
        }
        
        public static Request requestFor(final Expression node) {
            switch (node.tokenType()) {
                case TYPEOF: {
                    return Request.TYPEOF;
                }
                case IN: {
                    return Request.IN;
                }
                case INSTANCEOF: {
                    return Request.INSTANCEOF;
                }
                case EQ_STRICT: {
                    return Request.EQ_STRICT;
                }
                case NE_STRICT: {
                    return Request.NE_STRICT;
                }
                case EQ: {
                    return Request.EQ;
                }
                case NE: {
                    return Request.NE;
                }
                case LT: {
                    return Request.LT;
                }
                case LE: {
                    return Request.LE;
                }
                case GT: {
                    return Request.GT;
                }
                case GE: {
                    return Request.GE;
                }
                default: {
                    assert false;
                    return null;
                }
            }
        }
        
        public static boolean isUndefinedCheck(final Request request) {
            return request == Request.IS_UNDEFINED || request == Request.IS_NOT_UNDEFINED;
        }
        
        public static boolean isEQ(final Request request) {
            return request == Request.EQ || request == Request.EQ_STRICT;
        }
        
        public static boolean isNE(final Request request) {
            return request == Request.NE || request == Request.NE_STRICT;
        }
        
        public static boolean isStrict(final Request request) {
            return request == Request.EQ_STRICT || request == Request.NE_STRICT;
        }
        
        public static Request reverse(final Request request) {
            switch (request) {
                case NE_STRICT:
                case EQ_STRICT:
                case EQ:
                case NE: {
                    return request;
                }
                case LE: {
                    return Request.GE;
                }
                case LT: {
                    return Request.GT;
                }
                case GE: {
                    return Request.LE;
                }
                case GT: {
                    return Request.LT;
                }
                default: {
                    return null;
                }
            }
        }
        
        public static Request invert(final Request request) {
            switch (request) {
                case EQ: {
                    return Request.NE;
                }
                case EQ_STRICT: {
                    return Request.NE_STRICT;
                }
                case NE: {
                    return Request.EQ;
                }
                case NE_STRICT: {
                    return Request.EQ_STRICT;
                }
                case LE: {
                    return Request.GT;
                }
                case LT: {
                    return Request.GE;
                }
                case GE: {
                    return Request.LT;
                }
                case GT: {
                    return Request.LE;
                }
                default: {
                    return null;
                }
            }
        }
        
        public static boolean isComparison(final Request request) {
            switch (request) {
                case NE_STRICT:
                case EQ_STRICT:
                case EQ:
                case NE:
                case LE:
                case LT:
                case GE:
                case GT:
                case IS_UNDEFINED:
                case IS_NOT_UNDEFINED: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }
}
