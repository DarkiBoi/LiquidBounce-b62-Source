// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.ir;

import java.util.NoSuchElementException;
import jdk.nashorn.internal.runtime.Source;
import java.io.File;
import jdk.nashorn.internal.runtime.Debug;
import java.util.Iterator;

public class LexicalContext
{
    private LexicalContextNode[] stack;
    private int[] flags;
    private int sp;
    
    public LexicalContext() {
        this.stack = new LexicalContextNode[16];
        this.flags = new int[16];
    }
    
    public void setFlag(final LexicalContextNode node, final int flag) {
        if (flag != 0) {
            assert !(node instanceof Block);
            for (int i = this.sp - 1; i >= 0; --i) {
                if (this.stack[i] == node) {
                    final int[] flags = this.flags;
                    final int n = i;
                    flags[n] |= flag;
                    return;
                }
            }
        }
        assert false;
    }
    
    public void setBlockNeedsScope(final Block block) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] == block) {
                final int[] flags = this.flags;
                final int n = i;
                flags[n] |= 0x1;
                for (int j = i - 1; j >= 0; --j) {
                    if (this.stack[j] instanceof FunctionNode) {
                        final int[] flags2 = this.flags;
                        final int n2 = j;
                        flags2[n2] |= 0x80;
                        return;
                    }
                }
            }
        }
        assert false;
    }
    
    public int getFlags(final LexicalContextNode node) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] == node) {
                return this.flags[i];
            }
        }
        throw new AssertionError((Object)"flag node not on context stack");
    }
    
    public Block getFunctionBody(final FunctionNode functionNode) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] == functionNode) {
                return (Block)this.stack[i + 1];
            }
        }
        throw new AssertionError((Object)(functionNode.getName() + " not on context stack"));
    }
    
    public Iterator<LexicalContextNode> getAllNodes() {
        return new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
    }
    
    public FunctionNode getOutermostFunction() {
        return (FunctionNode)this.stack[0];
    }
    
    public <T extends LexicalContextNode> T push(final T node) {
        assert !this.contains(node);
        if (this.sp == this.stack.length) {
            final LexicalContextNode[] newStack = new LexicalContextNode[this.sp * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.sp);
            this.stack = newStack;
            final int[] newFlags = new int[this.sp * 2];
            System.arraycopy(this.flags, 0, newFlags, 0, this.sp);
            this.flags = newFlags;
        }
        this.stack[this.sp] = node;
        this.flags[this.sp] = 0;
        ++this.sp;
        return node;
    }
    
    public boolean isEmpty() {
        return this.sp == 0;
    }
    
    public int size() {
        return this.sp;
    }
    
    public <T extends Node> T pop(final T node) {
        --this.sp;
        final LexicalContextNode popped = this.stack[this.sp];
        this.stack[this.sp] = null;
        if (popped instanceof Flags) {
            return ((Flags)popped).setFlag(this, this.flags[this.sp]);
        }
        return (T)popped;
    }
    
    public <T extends jdk.nashorn.internal.ir.LexicalContextNode> T applyTopFlags(final T node) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifne            22
        //     6: aload_1         /* node */
        //     7: aload_0         /* this */
        //     8: invokevirtual   jdk/nashorn/internal/ir/LexicalContext.peek:()Ljdk/nashorn/internal/ir/LexicalContextNode;
        //    11: if_acmpeq       22
        //    14: new             Ljava/lang/AssertionError;
        //    17: dup            
        //    18: invokespecial   java/lang/AssertionError.<init>:()V
        //    21: athrow         
        //    22: aload_1         /* node */
        //    23: checkcast       Ljdk/nashorn/internal/ir/Flags;
        //    26: aload_0         /* this */
        //    27: aload_0         /* this */
        //    28: getfield        jdk/nashorn/internal/ir/LexicalContext.flags:[I
        //    31: aload_0         /* this */
        //    32: getfield        jdk/nashorn/internal/ir/LexicalContext.sp:I
        //    35: iconst_1       
        //    36: isub           
        //    37: iaload         
        //    38: invokeinterface jdk/nashorn/internal/ir/Flags.setFlag:(Ljdk/nashorn/internal/ir/LexicalContext;I)Ljdk/nashorn/internal/ir/LexicalContextNode;
        //    43: areturn        
        //    Signature:
        //  <T::Ljdk/nashorn/internal/ir/LexicalContextNode;:Ljdk/nashorn/internal/ir/Flags<TT;>;>(TT;)TT;
        //    StackMapTable: 00 01 16
        // 
        // The error that occurred was:
        // 
        // java.lang.StackOverflowError
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1283)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public LexicalContextNode peek() {
        return this.stack[this.sp - 1];
    }
    
    public boolean contains(final LexicalContextNode node) {
        for (int i = 0; i < this.sp; ++i) {
            if (this.stack[i] == node) {
                return true;
            }
        }
        return false;
    }
    
    public LexicalContextNode replace(final LexicalContextNode oldNode, final LexicalContextNode newNode) {
        int i = this.sp - 1;
        while (i >= 0) {
            if (this.stack[i] == oldNode) {
                assert i == this.sp - 1 : "violation of contract - we always expect to find the replacement node on top of the lexical context stack: " + newNode + " has " + this.stack[i + 1].getClass() + " above it";
                this.stack[i] = newNode;
                break;
            }
            else {
                --i;
            }
        }
        return newNode;
    }
    
    public Iterator<Block> getBlocks() {
        return new NodeIterator<Block>(Block.class);
    }
    
    public Iterator<FunctionNode> getFunctions() {
        return new NodeIterator<FunctionNode>(FunctionNode.class);
    }
    
    public Block getParentBlock() {
        final Iterator<Block> iter = new NodeIterator<Block>(Block.class, this.getCurrentFunction());
        iter.next();
        return iter.hasNext() ? iter.next() : null;
    }
    
    public LabelNode getCurrentBlockLabelNode() {
        assert this.stack[this.sp - 1] instanceof Block;
        if (this.sp < 2) {
            return null;
        }
        final LexicalContextNode parent = this.stack[this.sp - 2];
        return (parent instanceof LabelNode) ? ((LabelNode)parent) : null;
    }
    
    public Iterator<Block> getAncestorBlocks(final Block block) {
        final Iterator<Block> iter = this.getBlocks();
        while (iter.hasNext()) {
            final Block b = iter.next();
            if (block == b) {
                return iter;
            }
        }
        throw new AssertionError((Object)"Block is not on the current lexical context stack");
    }
    
    public Iterator<Block> getBlocks(final Block block) {
        final Iterator<Block> iter = this.getAncestorBlocks(block);
        return new Iterator<Block>() {
            boolean blockReturned = false;
            
            @Override
            public boolean hasNext() {
                return iter.hasNext() || !this.blockReturned;
            }
            
            @Override
            public Block next() {
                if (this.blockReturned) {
                    return iter.next();
                }
                this.blockReturned = true;
                return block;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public FunctionNode getFunction(final Block block) {
        final Iterator<LexicalContextNode> iter = new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
        while (iter.hasNext()) {
            final LexicalContextNode next = iter.next();
            if (next == block) {
                while (iter.hasNext()) {
                    final LexicalContextNode next2 = iter.next();
                    if (next2 instanceof FunctionNode) {
                        return (FunctionNode)next2;
                    }
                }
            }
        }
        assert false;
        return null;
    }
    
    public Block getCurrentBlock() {
        return this.getBlocks().next();
    }
    
    public FunctionNode getCurrentFunction() {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] instanceof FunctionNode) {
                return (FunctionNode)this.stack[i];
            }
        }
        return null;
    }
    
    public Block getDefiningBlock(final Symbol symbol) {
        final String name = symbol.getName();
        final Iterator<Block> it = this.getBlocks();
        while (it.hasNext()) {
            final Block next = it.next();
            if (next.getExistingSymbol(name) == symbol) {
                return next;
            }
        }
        throw new AssertionError((Object)("Couldn't find symbol " + name + " in the context"));
    }
    
    public FunctionNode getDefiningFunction(final Symbol symbol) {
        final String name = symbol.getName();
        final Iterator<LexicalContextNode> iter = new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
        while (iter.hasNext()) {
            final LexicalContextNode next = iter.next();
            if (next instanceof Block && ((Block)next).getExistingSymbol(name) == symbol) {
                while (iter.hasNext()) {
                    final LexicalContextNode next2 = iter.next();
                    if (next2 instanceof FunctionNode) {
                        return (FunctionNode)next2;
                    }
                }
                throw new AssertionError((Object)("Defining block for symbol " + name + " has no function in the context"));
            }
        }
        throw new AssertionError((Object)("Couldn't find symbol " + name + " in the context"));
    }
    
    public boolean isFunctionBody() {
        return this.getParentBlock() == null;
    }
    
    public boolean isSplitBody() {
        return this.sp >= 2 && this.stack[this.sp - 1] instanceof Block && this.stack[this.sp - 2] instanceof SplitNode;
    }
    
    public FunctionNode getParentFunction(final FunctionNode functionNode) {
        final Iterator<FunctionNode> iter = new NodeIterator<FunctionNode>(FunctionNode.class);
        while (iter.hasNext()) {
            final FunctionNode next = iter.next();
            if (next == functionNode) {
                return iter.hasNext() ? iter.next() : null;
            }
        }
        assert false;
        return null;
    }
    
    public int getScopeNestingLevelTo(final LexicalContextNode until) {
        assert until != null;
        int n = 0;
        final Iterator<LexicalContextNode> iter = this.getAllNodes();
        while (iter.hasNext()) {
            final LexicalContextNode node = iter.next();
            if (node == until) {
                break;
            }
            assert !(node instanceof FunctionNode);
            if (!(node instanceof WithNode) && (!(node instanceof Block) || !((Block)node).needsScope())) {
                continue;
            }
            ++n;
        }
        return n;
    }
    
    private BreakableNode getBreakable() {
        final NodeIterator<BreakableNode> iter = new NodeIterator<BreakableNode>(BreakableNode.class, this.getCurrentFunction());
        while (iter.hasNext()) {
            final BreakableNode next = iter.next();
            if (next.isBreakableWithoutLabel()) {
                return next;
            }
        }
        return null;
    }
    
    public boolean inLoop() {
        return this.getCurrentLoop() != null;
    }
    
    public LoopNode getCurrentLoop() {
        final Iterator<LoopNode> iter = new NodeIterator<LoopNode>(LoopNode.class, this.getCurrentFunction());
        return iter.hasNext() ? iter.next() : null;
    }
    
    public BreakableNode getBreakable(final String labelName) {
        if (labelName == null) {
            return this.getBreakable();
        }
        final LabelNode foundLabel = this.findLabel(labelName);
        if (foundLabel != null) {
            BreakableNode breakable = null;
            final NodeIterator<BreakableNode> iter = new NodeIterator<BreakableNode>(BreakableNode.class, foundLabel);
            while (iter.hasNext()) {
                breakable = iter.next();
            }
            return breakable;
        }
        return null;
    }
    
    private LoopNode getContinueTo() {
        return this.getCurrentLoop();
    }
    
    public LoopNode getContinueTo(final String labelName) {
        if (labelName == null) {
            return this.getContinueTo();
        }
        final LabelNode foundLabel = this.findLabel(labelName);
        if (foundLabel != null) {
            LoopNode loop = null;
            final NodeIterator<LoopNode> iter = new NodeIterator<LoopNode>(LoopNode.class, foundLabel);
            while (iter.hasNext()) {
                loop = iter.next();
            }
            return loop;
        }
        return null;
    }
    
    public Block getInlinedFinally(final String labelName) {
        final NodeIterator<TryNode> iter = new NodeIterator<TryNode>(TryNode.class);
        while (iter.hasNext()) {
            final Block inlinedFinally = iter.next().getInlinedFinally(labelName);
            if (inlinedFinally != null) {
                return inlinedFinally;
            }
        }
        return null;
    }
    
    public TryNode getTryNodeForInlinedFinally(final String labelName) {
        final NodeIterator<TryNode> iter = new NodeIterator<TryNode>(TryNode.class);
        while (iter.hasNext()) {
            final TryNode tryNode = iter.next();
            if (tryNode.getInlinedFinally(labelName) != null) {
                return tryNode;
            }
        }
        return null;
    }
    
    public LabelNode findLabel(final String name) {
        final Iterator<LabelNode> iter = new NodeIterator<LabelNode>(LabelNode.class, this.getCurrentFunction());
        while (iter.hasNext()) {
            final LabelNode next = iter.next();
            if (next.getLabelName().equals(name)) {
                return next;
            }
        }
        return null;
    }
    
    public boolean isExternalTarget(final SplitNode splitNode, final BreakableNode target) {
        int i = this.sp;
        while (i-- > 0) {
            final LexicalContextNode next = this.stack[i];
            if (next == splitNode) {
                return true;
            }
            if (next == target) {
                return false;
            }
            if (!(next instanceof TryNode)) {
                continue;
            }
            for (final Block inlinedFinally : ((TryNode)next).getInlinedFinallies()) {
                if (TryNode.getLabelledInlinedFinallyBlock(inlinedFinally) == target) {
                    return false;
                }
            }
        }
        throw new AssertionError((Object)(target + " was expected in lexical context " + this + " but wasn't"));
    }
    
    public boolean inUnprotectedSwitchContext() {
        for (int i = this.sp; i > 0; --i) {
            final LexicalContextNode next = this.stack[i];
            if (next instanceof Block) {
                return this.stack[i - 1] instanceof SwitchNode;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for (int i = 0; i < this.sp; ++i) {
            final Object node = this.stack[i];
            sb.append(node.getClass().getSimpleName());
            sb.append('@');
            sb.append(Debug.id(node));
            sb.append(':');
            if (node instanceof FunctionNode) {
                final FunctionNode fn = (FunctionNode)node;
                final Source source = fn.getSource();
                String src = source.toString();
                if (src.contains(File.pathSeparator)) {
                    src = src.substring(src.lastIndexOf(File.pathSeparator));
                }
                src += ' ';
                src += fn.getLineNumber();
                sb.append(src);
            }
            sb.append(' ');
        }
        sb.append(" ==> ]");
        return sb.toString();
    }
    
    private class NodeIterator<T extends LexicalContextNode> implements Iterator<T>
    {
        private int index;
        private T next;
        private final Class<T> clazz;
        private LexicalContextNode until;
        
        NodeIterator(final LexicalContext lexicalContext, final Class<T> clazz) {
            this(lexicalContext, clazz, null);
        }
        
        NodeIterator(final Class<T> clazz, final LexicalContextNode until) {
            this.index = LexicalContext.this.sp - 1;
            this.clazz = clazz;
            this.until = until;
            this.next = this.findNext();
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public T next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            final T lnext = this.next;
            this.next = this.findNext();
            return lnext;
        }
        
        private T findNext() {
            for (int i = this.index; i >= 0; --i) {
                final Object node = LexicalContext.this.stack[i];
                if (node == this.until) {
                    return null;
                }
                if (this.clazz.isAssignableFrom(node.getClass())) {
                    this.index = i - 1;
                    return (T)node;
                }
            }
            return null;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
