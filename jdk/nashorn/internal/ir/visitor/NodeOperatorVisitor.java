// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.LexicalContext;

public abstract class NodeOperatorVisitor<T extends LexicalContext> extends NodeVisitor<T>
{
    public NodeOperatorVisitor(final T lc) {
        super(lc);
    }
    
    @Override
    public final boolean enterUnaryNode(final UnaryNode unaryNode) {
        switch (unaryNode.tokenType()) {
            case ADD: {
                return this.enterADD(unaryNode);
            }
            case BIT_NOT: {
                return this.enterBIT_NOT(unaryNode);
            }
            case DELETE: {
                return this.enterDELETE(unaryNode);
            }
            case NEW: {
                return this.enterNEW(unaryNode);
            }
            case NOT: {
                return this.enterNOT(unaryNode);
            }
            case SUB: {
                return this.enterSUB(unaryNode);
            }
            case TYPEOF: {
                return this.enterTYPEOF(unaryNode);
            }
            case VOID: {
                return this.enterVOID(unaryNode);
            }
            case DECPREFIX:
            case DECPOSTFIX:
            case INCPREFIX:
            case INCPOSTFIX: {
                return this.enterDECINC(unaryNode);
            }
            default: {
                return super.enterUnaryNode(unaryNode);
            }
        }
    }
    
    @Override
    public final Node leaveUnaryNode(final UnaryNode unaryNode) {
        switch (unaryNode.tokenType()) {
            case ADD: {
                return this.leaveADD(unaryNode);
            }
            case BIT_NOT: {
                return this.leaveBIT_NOT(unaryNode);
            }
            case DELETE: {
                return this.leaveDELETE(unaryNode);
            }
            case NEW: {
                return this.leaveNEW(unaryNode);
            }
            case NOT: {
                return this.leaveNOT(unaryNode);
            }
            case SUB: {
                return this.leaveSUB(unaryNode);
            }
            case TYPEOF: {
                return this.leaveTYPEOF(unaryNode);
            }
            case VOID: {
                return this.leaveVOID(unaryNode);
            }
            case DECPREFIX:
            case DECPOSTFIX:
            case INCPREFIX:
            case INCPOSTFIX: {
                return this.leaveDECINC(unaryNode);
            }
            default: {
                return super.leaveUnaryNode(unaryNode);
            }
        }
    }
    
    @Override
    public final boolean enterBinaryNode(final BinaryNode binaryNode) {
        switch (binaryNode.tokenType()) {
            case ADD: {
                return this.enterADD(binaryNode);
            }
            case AND: {
                return this.enterAND(binaryNode);
            }
            case ASSIGN: {
                return this.enterASSIGN(binaryNode);
            }
            case ASSIGN_ADD: {
                return this.enterASSIGN_ADD(binaryNode);
            }
            case ASSIGN_BIT_AND: {
                return this.enterASSIGN_BIT_AND(binaryNode);
            }
            case ASSIGN_BIT_OR: {
                return this.enterASSIGN_BIT_OR(binaryNode);
            }
            case ASSIGN_BIT_XOR: {
                return this.enterASSIGN_BIT_XOR(binaryNode);
            }
            case ASSIGN_DIV: {
                return this.enterASSIGN_DIV(binaryNode);
            }
            case ASSIGN_MOD: {
                return this.enterASSIGN_MOD(binaryNode);
            }
            case ASSIGN_MUL: {
                return this.enterASSIGN_MUL(binaryNode);
            }
            case ASSIGN_SAR: {
                return this.enterASSIGN_SAR(binaryNode);
            }
            case ASSIGN_SHL: {
                return this.enterASSIGN_SHL(binaryNode);
            }
            case ASSIGN_SHR: {
                return this.enterASSIGN_SHR(binaryNode);
            }
            case ASSIGN_SUB: {
                return this.enterASSIGN_SUB(binaryNode);
            }
            case BIND: {
                return this.enterBIND(binaryNode);
            }
            case BIT_AND: {
                return this.enterBIT_AND(binaryNode);
            }
            case BIT_OR: {
                return this.enterBIT_OR(binaryNode);
            }
            case BIT_XOR: {
                return this.enterBIT_XOR(binaryNode);
            }
            case COMMARIGHT: {
                return this.enterCOMMARIGHT(binaryNode);
            }
            case COMMALEFT: {
                return this.enterCOMMALEFT(binaryNode);
            }
            case DIV: {
                return this.enterDIV(binaryNode);
            }
            case EQ: {
                return this.enterEQ(binaryNode);
            }
            case EQ_STRICT: {
                return this.enterEQ_STRICT(binaryNode);
            }
            case GE: {
                return this.enterGE(binaryNode);
            }
            case GT: {
                return this.enterGT(binaryNode);
            }
            case IN: {
                return this.enterIN(binaryNode);
            }
            case INSTANCEOF: {
                return this.enterINSTANCEOF(binaryNode);
            }
            case LE: {
                return this.enterLE(binaryNode);
            }
            case LT: {
                return this.enterLT(binaryNode);
            }
            case MOD: {
                return this.enterMOD(binaryNode);
            }
            case MUL: {
                return this.enterMUL(binaryNode);
            }
            case NE: {
                return this.enterNE(binaryNode);
            }
            case NE_STRICT: {
                return this.enterNE_STRICT(binaryNode);
            }
            case OR: {
                return this.enterOR(binaryNode);
            }
            case SAR: {
                return this.enterSAR(binaryNode);
            }
            case SHL: {
                return this.enterSHL(binaryNode);
            }
            case SHR: {
                return this.enterSHR(binaryNode);
            }
            case SUB: {
                return this.enterSUB(binaryNode);
            }
            default: {
                return super.enterBinaryNode(binaryNode);
            }
        }
    }
    
    @Override
    public final Node leaveBinaryNode(final BinaryNode binaryNode) {
        switch (binaryNode.tokenType()) {
            case ADD: {
                return this.leaveADD(binaryNode);
            }
            case AND: {
                return this.leaveAND(binaryNode);
            }
            case ASSIGN: {
                return this.leaveASSIGN(binaryNode);
            }
            case ASSIGN_ADD: {
                return this.leaveASSIGN_ADD(binaryNode);
            }
            case ASSIGN_BIT_AND: {
                return this.leaveASSIGN_BIT_AND(binaryNode);
            }
            case ASSIGN_BIT_OR: {
                return this.leaveASSIGN_BIT_OR(binaryNode);
            }
            case ASSIGN_BIT_XOR: {
                return this.leaveASSIGN_BIT_XOR(binaryNode);
            }
            case ASSIGN_DIV: {
                return this.leaveASSIGN_DIV(binaryNode);
            }
            case ASSIGN_MOD: {
                return this.leaveASSIGN_MOD(binaryNode);
            }
            case ASSIGN_MUL: {
                return this.leaveASSIGN_MUL(binaryNode);
            }
            case ASSIGN_SAR: {
                return this.leaveASSIGN_SAR(binaryNode);
            }
            case ASSIGN_SHL: {
                return this.leaveASSIGN_SHL(binaryNode);
            }
            case ASSIGN_SHR: {
                return this.leaveASSIGN_SHR(binaryNode);
            }
            case ASSIGN_SUB: {
                return this.leaveASSIGN_SUB(binaryNode);
            }
            case BIND: {
                return this.leaveBIND(binaryNode);
            }
            case BIT_AND: {
                return this.leaveBIT_AND(binaryNode);
            }
            case BIT_OR: {
                return this.leaveBIT_OR(binaryNode);
            }
            case BIT_XOR: {
                return this.leaveBIT_XOR(binaryNode);
            }
            case COMMARIGHT: {
                return this.leaveCOMMARIGHT(binaryNode);
            }
            case COMMALEFT: {
                return this.leaveCOMMALEFT(binaryNode);
            }
            case DIV: {
                return this.leaveDIV(binaryNode);
            }
            case EQ: {
                return this.leaveEQ(binaryNode);
            }
            case EQ_STRICT: {
                return this.leaveEQ_STRICT(binaryNode);
            }
            case GE: {
                return this.leaveGE(binaryNode);
            }
            case GT: {
                return this.leaveGT(binaryNode);
            }
            case IN: {
                return this.leaveIN(binaryNode);
            }
            case INSTANCEOF: {
                return this.leaveINSTANCEOF(binaryNode);
            }
            case LE: {
                return this.leaveLE(binaryNode);
            }
            case LT: {
                return this.leaveLT(binaryNode);
            }
            case MOD: {
                return this.leaveMOD(binaryNode);
            }
            case MUL: {
                return this.leaveMUL(binaryNode);
            }
            case NE: {
                return this.leaveNE(binaryNode);
            }
            case NE_STRICT: {
                return this.leaveNE_STRICT(binaryNode);
            }
            case OR: {
                return this.leaveOR(binaryNode);
            }
            case SAR: {
                return this.leaveSAR(binaryNode);
            }
            case SHL: {
                return this.leaveSHL(binaryNode);
            }
            case SHR: {
                return this.leaveSHR(binaryNode);
            }
            case SUB: {
                return this.leaveSUB(binaryNode);
            }
            default: {
                return super.leaveBinaryNode(binaryNode);
            }
        }
    }
    
    public boolean enterADD(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveADD(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterBIT_NOT(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveBIT_NOT(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterDECINC(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveDECINC(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterDELETE(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveDELETE(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterNEW(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveNEW(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterNOT(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveNOT(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterSUB(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveSUB(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterTYPEOF(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveTYPEOF(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterVOID(final UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }
    
    public Node leaveVOID(final UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }
    
    public boolean enterADD(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveADD(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterAND(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveAND(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_ADD(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_ADD(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_BIT_AND(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_BIT_AND(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_BIT_OR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_BIT_OR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_BIT_XOR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_BIT_XOR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_DIV(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_DIV(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_MOD(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_MOD(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_MUL(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_MUL(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_SAR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_SAR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_SHL(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_SHL(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_SHR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_SHR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterASSIGN_SUB(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveASSIGN_SUB(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterBIND(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveBIND(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterBIT_AND(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveBIT_AND(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterBIT_OR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveBIT_OR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterBIT_XOR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveBIT_XOR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterCOMMALEFT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveCOMMALEFT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterCOMMARIGHT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveCOMMARIGHT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterDIV(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveDIV(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterEQ(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveEQ(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterEQ_STRICT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveEQ_STRICT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterGE(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveGE(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterGT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveGT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterIN(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveIN(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterINSTANCEOF(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveINSTANCEOF(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterLE(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveLE(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterLT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveLT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterMOD(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveMOD(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterMUL(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveMUL(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterNE(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveNE(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterNE_STRICT(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveNE_STRICT(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterOR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveOR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterSAR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveSAR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterSHL(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveSHL(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterSHR(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveSHR(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
    
    public boolean enterSUB(final BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }
    
    public Node leaveSUB(final BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
}
