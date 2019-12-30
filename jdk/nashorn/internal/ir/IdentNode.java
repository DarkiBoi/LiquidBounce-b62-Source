// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class IdentNode extends Expression implements PropertyKey, FunctionCall, Optimistic, JoinPredecessor
{
    private static final long serialVersionUID = 1L;
    private static final int PROPERTY_NAME = 1;
    private static final int INITIALIZED_HERE = 2;
    private static final int FUNCTION = 4;
    private static final int FUTURESTRICT_NAME = 8;
    private static final int IS_DECLARED_HERE = 16;
    private static final int IS_DEAD = 32;
    private final String name;
    private final Type type;
    private final int flags;
    private final int programPoint;
    private final LocalVariableConversion conversion;
    private Symbol symbol;
    
    public IdentNode(final long token, final int finish, final String name) {
        super(token, finish);
        this.name = name;
        this.type = null;
        this.flags = 0;
        this.programPoint = -1;
        this.conversion = null;
    }
    
    private IdentNode(final IdentNode identNode, final String name, final Type type, final int flags, final int programPoint, final LocalVariableConversion conversion) {
        super(identNode);
        this.name = name;
        this.type = type;
        this.flags = flags;
        this.programPoint = programPoint;
        this.conversion = conversion;
        this.symbol = identNode.symbol;
    }
    
    public IdentNode(final IdentNode identNode) {
        super(identNode);
        this.name = identNode.getName();
        this.type = identNode.type;
        this.flags = identNode.flags;
        this.conversion = identNode.conversion;
        this.programPoint = -1;
        this.symbol = identNode.symbol;
    }
    
    public static IdentNode createInternalIdentifier(final Symbol symbol) {
        return new IdentNode(Token.toDesc(TokenType.IDENT, 0, 0), 0, symbol.getName()).setSymbol(symbol);
    }
    
    @Override
    public Type getType() {
        if (this.type != null) {
            return this.type;
        }
        if (this.symbol != null && this.symbol.isScope()) {
            return Type.OBJECT;
        }
        return Type.UNDEFINED;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIdentNode(this)) {
            return visitor.leaveIdentNode(this);
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        if (printType) {
            this.optimisticTypeToString(sb, this.symbol == null || !this.symbol.hasSlot());
        }
        sb.append(this.name);
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getPropertyName() {
        return this.getName();
    }
    
    @Override
    public boolean isLocal() {
        return !this.getSymbol().isScope();
    }
    
    public Symbol getSymbol() {
        return this.symbol;
    }
    
    public IdentNode setSymbol(final Symbol symbol) {
        if (this.symbol == symbol) {
            return this;
        }
        final IdentNode newIdent = (IdentNode)this.clone();
        newIdent.symbol = symbol;
        return newIdent;
    }
    
    public boolean isPropertyName() {
        return (this.flags & 0x1) == 0x1;
    }
    
    public IdentNode setIsPropertyName() {
        if (this.isPropertyName()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x1, this.programPoint, this.conversion);
    }
    
    public boolean isFutureStrictName() {
        return (this.flags & 0x8) == 0x8;
    }
    
    public IdentNode setIsFutureStrictName() {
        if (this.isFutureStrictName()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x8, this.programPoint, this.conversion);
    }
    
    public boolean isInitializedHere() {
        return (this.flags & 0x2) == 0x2;
    }
    
    public IdentNode setIsInitializedHere() {
        if (this.isInitializedHere()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x2, this.programPoint, this.conversion);
    }
    
    public boolean isDead() {
        return (this.flags & 0x20) != 0x0;
    }
    
    public IdentNode markDead() {
        return new IdentNode(this, this.name, this.type, this.flags | 0x20, this.programPoint, this.conversion);
    }
    
    public boolean isDeclaredHere() {
        return (this.flags & 0x10) != 0x0;
    }
    
    public IdentNode setIsDeclaredHere() {
        if (this.isDeclaredHere()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x10, this.programPoint, this.conversion);
    }
    
    public boolean isCompileTimePropertyName() {
        return this.name.equals(CompilerConstants.__DIR__.symbolName()) || this.name.equals(CompilerConstants.__FILE__.symbolName()) || this.name.equals(CompilerConstants.__LINE__.symbolName());
    }
    
    @Override
    public boolean isFunction() {
        return (this.flags & 0x4) == 0x4;
    }
    
    @Override
    public IdentNode setType(final Type type) {
        if (this.type == type) {
            return this;
        }
        return new IdentNode(this, this.name, type, this.flags, this.programPoint, this.conversion);
    }
    
    public IdentNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x4, this.programPoint, this.conversion);
    }
    
    public IdentNode setIsNotFunction() {
        if (!this.isFunction()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags & 0xFFFFFFFB, this.programPoint, this.conversion);
    }
    
    @Override
    public int getProgramPoint() {
        return this.programPoint;
    }
    
    @Override
    public Optimistic setProgramPoint(final int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags, programPoint, this.conversion);
    }
    
    @Override
    public Type getMostOptimisticType() {
        return Type.INT;
    }
    
    @Override
    public Type getMostPessimisticType() {
        return Type.OBJECT;
    }
    
    @Override
    public boolean canBeOptimistic() {
        return true;
    }
    
    @Override
    public JoinPredecessor setLocalVariableConversion(final LexicalContext lc, final LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags, this.programPoint, conversion);
    }
    
    public boolean isInternal() {
        assert this.name != null;
        return this.name.charAt(0) == ':';
    }
    
    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
