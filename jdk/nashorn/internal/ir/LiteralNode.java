// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.codegen.types.ArrayType;
import java.util.Collections;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.runtime.Undefined;
import java.util.List;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public abstract class LiteralNode<T> extends Expression implements PropertyKey
{
    private static final long serialVersionUID = 1L;
    protected final T value;
    public static final Object POSTSET_MARKER;
    
    protected LiteralNode(final long token, final int finish, final T value) {
        super(token, finish);
        this.value = value;
    }
    
    protected LiteralNode(final LiteralNode<T> literalNode) {
        this((LiteralNode<Object>)literalNode, literalNode.value);
    }
    
    protected LiteralNode(final LiteralNode<T> literalNode, final T newValue) {
        super(literalNode);
        this.value = newValue;
    }
    
    public LiteralNode<?> initialize(final LexicalContext lc) {
        return this;
    }
    
    public boolean isNull() {
        return this.value == null;
    }
    
    @Override
    public Type getType() {
        return Type.typeFor(this.value.getClass());
    }
    
    @Override
    public String getPropertyName() {
        return JSType.toString(this.getObject());
    }
    
    public boolean getBoolean() {
        return JSType.toBoolean(this.value);
    }
    
    public int getInt32() {
        return JSType.toInt32(this.value);
    }
    
    public long getUint32() {
        return JSType.toUint32(this.value);
    }
    
    public long getLong() {
        return JSType.toLong(this.value);
    }
    
    public double getNumber() {
        return JSType.toNumber(this.value);
    }
    
    public String getString() {
        return JSType.toString(this.value);
    }
    
    public Object getObject() {
        return this.value;
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    public boolean isNumeric() {
        return this.value instanceof Number;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterLiteralNode(this)) {
            return visitor.leaveLiteralNode(this);
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        if (this.value == null) {
            sb.append("null");
        }
        else {
            sb.append(this.value.toString());
        }
    }
    
    public final T getValue() {
        return this.value;
    }
    
    private static Expression[] valueToArray(final List<Expression> value) {
        return value.toArray(new Expression[value.size()]);
    }
    
    public static LiteralNode<Object> newInstance(final long token, final int finish) {
        return new NullLiteralNode(token, finish);
    }
    
    public static LiteralNode<Object> newInstance(final Node parent) {
        return new NullLiteralNode(parent.getToken(), parent.getFinish());
    }
    
    public static LiteralNode<Boolean> newInstance(final long token, final int finish, final boolean value) {
        return new BooleanLiteralNode(token, finish, value);
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final boolean value) {
        return new BooleanLiteralNode(parent.getToken(), parent.getFinish(), value);
    }
    
    public static LiteralNode<Number> newInstance(final long token, final int finish, final Number value) {
        assert !(value instanceof Long);
        return new NumberLiteralNode(token, finish, value);
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final Number value) {
        return new NumberLiteralNode(parent.getToken(), parent.getFinish(), value);
    }
    
    public static LiteralNode<Undefined> newInstance(final long token, final int finish, final Undefined value) {
        return new UndefinedLiteralNode(token, finish);
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final Undefined value) {
        return new UndefinedLiteralNode(parent.getToken(), parent.getFinish());
    }
    
    public static LiteralNode<String> newInstance(final long token, final int finish, final String value) {
        return new StringLiteralNode(token, finish, value);
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final String value) {
        return new StringLiteralNode(parent.getToken(), parent.getFinish(), value);
    }
    
    public static LiteralNode<Lexer.LexerToken> newInstance(final long token, final int finish, final Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(token, finish, value);
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(parent.getToken(), parent.getFinish(), value);
    }
    
    public static Object objectAsConstant(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Number || object instanceof String || object instanceof Boolean) {
            return object;
        }
        if (object instanceof LiteralNode) {
            return objectAsConstant(((LiteralNode)object).getValue());
        }
        return LiteralNode.POSTSET_MARKER;
    }
    
    public static boolean isConstant(final Object object) {
        return objectAsConstant(object) != LiteralNode.POSTSET_MARKER;
    }
    
    public static LiteralNode<Expression[]> newInstance(final long token, final int finish, final List<Expression> value) {
        return new ArrayLiteralNode(token, finish, valueToArray(value));
    }
    
    public static LiteralNode<?> newInstance(final Node parent, final List<Expression> value) {
        return new ArrayLiteralNode(parent.getToken(), parent.getFinish(), valueToArray(value));
    }
    
    public static LiteralNode<Expression[]> newInstance(final long token, final int finish, final Expression[] value) {
        return new ArrayLiteralNode(token, finish, value);
    }
    
    static {
        POSTSET_MARKER = new Object();
    }
    
    public static class PrimitiveLiteralNode<T> extends LiteralNode<T>
    {
        private static final long serialVersionUID = 1L;
        
        private PrimitiveLiteralNode(final long token, final int finish, final T value) {
            super(token, finish, value);
        }
        
        private PrimitiveLiteralNode(final PrimitiveLiteralNode<T> literalNode) {
            super(literalNode);
        }
        
        public boolean isTrue() {
            return JSType.toBoolean(this.value);
        }
        
        @Override
        public boolean isLocal() {
            return true;
        }
        
        @Override
        public boolean isAlwaysFalse() {
            return !this.isTrue();
        }
        
        @Override
        public boolean isAlwaysTrue() {
            return this.isTrue();
        }
    }
    
    @Immutable
    private static final class BooleanLiteralNode extends PrimitiveLiteralNode<Boolean>
    {
        private static final long serialVersionUID = 1L;
        
        private BooleanLiteralNode(final long token, final int finish, final boolean value) {
            super(Token.recast(token, value ? TokenType.TRUE : TokenType.FALSE), finish, (Object)value);
        }
        
        private BooleanLiteralNode(final BooleanLiteralNode literalNode) {
            super((PrimitiveLiteralNode)literalNode);
        }
        
        @Override
        public boolean isTrue() {
            return (boolean)this.value;
        }
        
        @Override
        public Type getType() {
            return Type.BOOLEAN;
        }
        
        @Override
        public Type getWidestOperationType() {
            return Type.BOOLEAN;
        }
    }
    
    @Immutable
    private static final class NumberLiteralNode extends PrimitiveLiteralNode<Number>
    {
        private static final long serialVersionUID = 1L;
        private final Type type;
        
        private NumberLiteralNode(final long token, final int finish, final Number value) {
            super(Token.recast(token, TokenType.DECIMAL), finish, (Object)value);
            this.type = numberGetType((Number)this.value);
        }
        
        private NumberLiteralNode(final NumberLiteralNode literalNode) {
            super((PrimitiveLiteralNode)literalNode);
            this.type = numberGetType((Number)this.value);
        }
        
        private static Type numberGetType(final Number number) {
            if (number instanceof Integer) {
                return Type.INT;
            }
            if (number instanceof Double) {
                return Type.NUMBER;
            }
            assert false;
            return null;
        }
        
        @Override
        public Type getType() {
            return this.type;
        }
        
        @Override
        public Type getWidestOperationType() {
            return this.getType();
        }
    }
    
    private static class UndefinedLiteralNode extends PrimitiveLiteralNode<Undefined>
    {
        private static final long serialVersionUID = 1L;
        
        private UndefinedLiteralNode(final long token, final int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, (Object)ScriptRuntime.UNDEFINED);
        }
        
        private UndefinedLiteralNode(final UndefinedLiteralNode literalNode) {
            super((PrimitiveLiteralNode)literalNode);
        }
    }
    
    @Immutable
    private static class StringLiteralNode extends PrimitiveLiteralNode<String>
    {
        private static final long serialVersionUID = 1L;
        
        private StringLiteralNode(final long token, final int finish, final String value) {
            super(Token.recast(token, TokenType.STRING), finish, (Object)value);
        }
        
        private StringLiteralNode(final StringLiteralNode literalNode) {
            super((PrimitiveLiteralNode)literalNode);
        }
        
        @Override
        public void toString(final StringBuilder sb, final boolean printType) {
            sb.append('\"');
            sb.append((String)this.value);
            sb.append('\"');
        }
    }
    
    @Immutable
    private static class LexerTokenLiteralNode extends LiteralNode<Lexer.LexerToken>
    {
        private static final long serialVersionUID = 1L;
        
        private LexerTokenLiteralNode(final long token, final int finish, final Lexer.LexerToken value) {
            super(Token.recast(token, TokenType.STRING), finish, value);
        }
        
        private LexerTokenLiteralNode(final LexerTokenLiteralNode literalNode) {
            super((LiteralNode<Object>)literalNode);
        }
        
        @Override
        public Type getType() {
            return Type.OBJECT;
        }
        
        @Override
        public void toString(final StringBuilder sb, final boolean printType) {
            sb.append(((Lexer.LexerToken)this.value).toString());
        }
    }
    
    private static final class NullLiteralNode extends PrimitiveLiteralNode<Object>
    {
        private static final long serialVersionUID = 1L;
        
        private NullLiteralNode(final long token, final int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, (Object)null);
        }
        
        @Override
        public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
            if (visitor.enterLiteralNode(this)) {
                return visitor.leaveLiteralNode(this);
            }
            return this;
        }
        
        @Override
        public Type getType() {
            return Type.OBJECT;
        }
        
        @Override
        public Type getWidestOperationType() {
            return Type.OBJECT;
        }
    }
    
    @Immutable
    public static final class ArrayLiteralNode extends LiteralNode<Expression[]> implements LexicalContextNode, Splittable
    {
        private static final long serialVersionUID = 1L;
        private final Type elementType;
        private final Object presets;
        private final int[] postsets;
        private final List<SplitRange> splitRanges;
        
        protected ArrayLiteralNode(final long token, final int finish, final Expression[] value) {
            super(Token.recast(token, TokenType.ARRAY), finish, value);
            this.elementType = Type.UNKNOWN;
            this.presets = null;
            this.postsets = null;
            this.splitRanges = null;
        }
        
        private ArrayLiteralNode(final ArrayLiteralNode node, final Expression[] value, final Type elementType, final int[] postsets, final Object presets, final List<SplitRange> splitRanges) {
            super(node, value);
            this.elementType = elementType;
            this.postsets = postsets;
            this.presets = presets;
            this.splitRanges = splitRanges;
        }
        
        public List<Expression> getElementExpressions() {
            return Collections.unmodifiableList((List<? extends Expression>)Arrays.asList((T[])(Object[])(Object)this.value));
        }
        
        @Override
        public ArrayLiteralNode initialize(final LexicalContext lc) {
            return Node.replaceInLexicalContext(lc, this, ArrayLiteralInitializer.initialize(this));
        }
        
        public ArrayType getArrayType() {
            return getArrayType(this.getElementType());
        }
        
        private static ArrayType getArrayType(final Type elementType) {
            if (elementType.isInteger()) {
                return Type.INT_ARRAY;
            }
            if (elementType.isNumeric()) {
                return Type.NUMBER_ARRAY;
            }
            return Type.OBJECT_ARRAY;
        }
        
        @Override
        public Type getType() {
            return Type.typeFor(NativeArray.class);
        }
        
        public Type getElementType() {
            assert !this.elementType.isUnknown() : this + " has elementType=unknown";
            return this.elementType;
        }
        
        public int[] getPostsets() {
            assert this.postsets != null : this + " elementType=" + this.elementType + " has no postsets";
            return this.postsets;
        }
        
        private boolean presetsMatchElementType() {
            if (this.elementType == Type.INT) {
                return this.presets instanceof int[];
            }
            if (this.elementType == Type.NUMBER) {
                return this.presets instanceof double[];
            }
            return this.presets instanceof Object[];
        }
        
        public Object getPresets() {
            assert this.presets != null && this.presetsMatchElementType() : this + " doesn't have presets, or invalid preset type: " + this.presets;
            return this.presets;
        }
        
        @Override
        public List<SplitRange> getSplitRanges() {
            return (this.splitRanges == null) ? null : Collections.unmodifiableList((List<? extends SplitRange>)this.splitRanges);
        }
        
        public ArrayLiteralNode setSplitRanges(final LexicalContext lc, final List<SplitRange> splitRanges) {
            if (this.splitRanges == splitRanges) {
                return this;
            }
            return Node.replaceInLexicalContext(lc, this, new ArrayLiteralNode(this, (Expression[])(Object)this.value, this.elementType, this.postsets, this.presets, splitRanges));
        }
        
        @Override
        public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
            return Acceptor.accept(this, visitor);
        }
        
        @Override
        public Node accept(final LexicalContext lc, final NodeVisitor<? extends LexicalContext> visitor) {
            if (visitor.enterLiteralNode(this)) {
                final List<Expression> oldValue = Arrays.asList((Expression[])(Object[])(Object)this.value);
                final List<Expression> newValue = Node.accept(visitor, oldValue);
                return visitor.leaveLiteralNode((oldValue != newValue) ? this.setValue(lc, newValue) : this);
            }
            return this;
        }
        
        private ArrayLiteralNode setValue(final LexicalContext lc, final Expression[] value) {
            if (this.value == value) {
                return this;
            }
            return Node.replaceInLexicalContext(lc, this, new ArrayLiteralNode(this, value, this.elementType, this.postsets, this.presets, this.splitRanges));
        }
        
        private ArrayLiteralNode setValue(final LexicalContext lc, final List<Expression> value) {
            return this.setValue(lc, value.toArray(new Expression[value.size()]));
        }
        
        @Override
        public void toString(final StringBuilder sb, final boolean printType) {
            sb.append('[');
            boolean first = true;
            for (final Node node : (Expression[])(Object)this.value) {
                if (!first) {
                    sb.append(',');
                    sb.append(' ');
                }
                if (node == null) {
                    sb.append("undefined");
                }
                else {
                    node.toString(sb, printType);
                }
                first = false;
            }
            sb.append(']');
        }
        
        private static final class ArrayLiteralInitializer
        {
            static final /* synthetic */ boolean $assertionsDisabled;
            
            static ArrayLiteralNode initialize(final ArrayLiteralNode node) {
                final Type elementType = computeElementType((Expression[])(Object)node.value);
                final int[] postsets = computePostsets((Expression[])(Object)node.value);
                final Object presets = computePresets((Expression[])(Object)node.value, elementType, postsets);
                return new ArrayLiteralNode(node, (Expression[])(Object)node.value, elementType, postsets, presets, node.splitRanges);
            }
            
            private static Type computeElementType(final Expression[] value) {
                Type widestElementType = Type.INT;
                for (final Expression elem : value) {
                    if (elem == null) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    final Type type = elem.getType().isUnknown() ? Type.OBJECT : elem.getType();
                    if (type.isBoolean()) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    widestElementType = widestElementType.widest(type);
                    if (widestElementType.isObject()) {
                        break;
                    }
                }
                return widestElementType;
            }
            
            private static int[] computePostsets(final Expression[] value) {
                final int[] computed = new int[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    final Expression element = value[i];
                    if (element == null || !LiteralNode.isConstant(element)) {
                        computed[nComputed++] = i;
                    }
                }
                return Arrays.copyOf(computed, nComputed);
            }
            
            private static boolean setArrayElement(final int[] array, final int i, final Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).intValue();
                    return true;
                }
                return false;
            }
            
            private static boolean setArrayElement(final long[] array, final int i, final Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).longValue();
                    return true;
                }
                return false;
            }
            
            private static boolean setArrayElement(final double[] array, final int i, final Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).doubleValue();
                    return true;
                }
                return false;
            }
            
            private static int[] presetIntArray(final Expression[] value, final int[] postsets) {
                final int[] array = new int[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!setArrayElement(array, i, LiteralNode.objectAsConstant(value[i])) && !ArrayLiteralInitializer.$assertionsDisabled && postsets[nComputed++] != i) {
                        throw new AssertionError();
                    }
                }
                assert postsets.length == nComputed;
                return array;
            }
            
            private static long[] presetLongArray(final Expression[] value, final int[] postsets) {
                final long[] array = new long[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!setArrayElement(array, i, LiteralNode.objectAsConstant(value[i])) && !ArrayLiteralInitializer.$assertionsDisabled && postsets[nComputed++] != i) {
                        throw new AssertionError();
                    }
                }
                assert postsets.length == nComputed;
                return array;
            }
            
            private static double[] presetDoubleArray(final Expression[] value, final int[] postsets) {
                final double[] array = new double[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!setArrayElement(array, i, LiteralNode.objectAsConstant(value[i])) && !ArrayLiteralInitializer.$assertionsDisabled && postsets[nComputed++] != i) {
                        throw new AssertionError();
                    }
                }
                assert postsets.length == nComputed;
                return array;
            }
            
            private static Object[] presetObjectArray(final Expression[] value, final int[] postsets) {
                final Object[] array = new Object[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    final Node node = value[i];
                    if (node == null) {
                        assert postsets[nComputed++] == i;
                    }
                    else {
                        final Object element = LiteralNode.objectAsConstant(node);
                        if (element != LiteralNode.POSTSET_MARKER) {
                            array[i] = element;
                        }
                        else {
                            assert postsets[nComputed++] == i;
                        }
                    }
                }
                assert postsets.length == nComputed;
                return array;
            }
            
            static Object computePresets(final Expression[] value, final Type elementType, final int[] postsets) {
                assert !elementType.isUnknown();
                if (elementType.isInteger()) {
                    return presetIntArray(value, postsets);
                }
                if (elementType.isNumeric()) {
                    return presetDoubleArray(value, postsets);
                }
                return presetObjectArray(value, postsets);
            }
        }
    }
}
