// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class VarNode extends Statement implements Assignment<IdentNode>
{
    private static final long serialVersionUID = 1L;
    private final IdentNode name;
    private final Expression init;
    private final int flags;
    public static final int IS_LET = 1;
    public static final int IS_CONST = 2;
    public static final int IS_LAST_FUNCTION_DECLARATION = 4;
    
    public VarNode(final int lineNumber, final long token, final int finish, final IdentNode name, final Expression init) {
        this(lineNumber, token, finish, name, init, 0);
    }
    
    private VarNode(final VarNode varNode, final IdentNode name, final Expression init, final int flags) {
        super(varNode);
        this.name = ((init == null) ? name : name.setIsInitializedHere());
        this.init = init;
        this.flags = flags;
    }
    
    public VarNode(final int lineNumber, final long token, final int finish, final IdentNode name, final Expression init, final int flags) {
        super(lineNumber, token, finish);
        this.name = ((init == null) ? name : name.setIsInitializedHere());
        this.init = init;
        this.flags = flags;
    }
    
    @Override
    public boolean isAssignment() {
        return this.hasInit();
    }
    
    @Override
    public IdentNode getAssignmentDest() {
        return this.isAssignment() ? this.name : null;
    }
    
    @Override
    public VarNode setAssignmentDest(final IdentNode n) {
        return this.setName(n);
    }
    
    @Override
    public Expression getAssignmentSource() {
        return this.isAssignment() ? this.getInit() : null;
    }
    
    public boolean isBlockScoped() {
        return this.getFlag(1) || this.getFlag(2);
    }
    
    public boolean isLet() {
        return this.getFlag(1);
    }
    
    public boolean isConst() {
        return this.getFlag(2);
    }
    
    public int getSymbolFlags() {
        if (this.isLet()) {
            return 18;
        }
        if (this.isConst()) {
            return 34;
        }
        return 2;
    }
    
    public boolean hasInit() {
        return this.init != null;
    }
    
    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterVarNode(this)) {
            final Expression newInit = (this.init == null) ? null : ((Expression)this.init.accept(visitor));
            final IdentNode newName = (IdentNode)this.name.accept(visitor);
            VarNode newThis;
            if (this.name != newName || this.init != newInit) {
                newThis = new VarNode(this, newName, newInit, this.flags);
            }
            else {
                newThis = this;
            }
            return visitor.leaveVarNode(newThis);
        }
        return this;
    }
    
    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        sb.append(this.tokenType().getName()).append(' ');
        this.name.toString(sb, printType);
        if (this.init != null) {
            sb.append(" = ");
            this.init.toString(sb, printType);
        }
    }
    
    public Expression getInit() {
        return this.init;
    }
    
    public VarNode setInit(final Expression init) {
        if (this.init == init) {
            return this;
        }
        return new VarNode(this, this.name, init, this.flags);
    }
    
    public IdentNode getName() {
        return this.name;
    }
    
    public VarNode setName(final IdentNode name) {
        if (this.name == name) {
            return this;
        }
        return new VarNode(this, name, this.init, this.flags);
    }
    
    private VarNode setFlags(final int flags) {
        if (this.flags == flags) {
            return this;
        }
        return new VarNode(this, this.name, this.init, flags);
    }
    
    public boolean getFlag(final int flag) {
        return (this.flags & flag) == flag;
    }
    
    public VarNode setFlag(final int flag) {
        return this.setFlags(this.flags | flag);
    }
    
    public boolean isFunctionDeclaration() {
        return this.init instanceof FunctionNode && ((FunctionNode)this.init).isDeclared();
    }
}
