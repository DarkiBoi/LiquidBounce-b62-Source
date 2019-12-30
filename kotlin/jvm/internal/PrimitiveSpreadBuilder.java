// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0082\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019" }, d2 = { "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "T", "", "size", "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "spreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib" })
public abstract class PrimitiveSpreadBuilder<T>
{
    private int position;
    private final T[] spreads;
    private final int size;
    
    protected abstract int getSize(@NotNull final T p0);
    
    protected final int getPosition() {
        return this.position;
    }
    
    protected final void setPosition(final int <set-?>) {
        this.position = <set-?>;
    }
    
    public final void addSpread(@NotNull final T spreadArgument) {
        Intrinsics.checkParameterIsNotNull(spreadArgument, "spreadArgument");
        final T[] spreads = this.spreads;
        final int position;
        this.position = (position = this.position) + 1;
        spreads[position] = spreadArgument;
    }
    
    protected final int size() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1        /* totalLength */
        //     2: iconst_0       
        //     3: istore_2       
        //     4: aload_0         /* this */
        //     5: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.size:I
        //     8: iconst_1       
        //     9: isub           
        //    10: istore_3       
        //    11: iload_2        
        //    12: iload_3        
        //    13: if_icmpgt       50
        //    16: iload_1         /* totalLength */
        //    17: aload_0         /* this */
        //    18: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.spreads:[Ljava/lang/Object;
        //    21: iload_2         /* i */
        //    22: aaload         
        //    23: dup            
        //    24: ifnull          35
        //    27: aload_0         /* this */
        //    28: swap           
        //    29: invokevirtual   kotlin/jvm/internal/PrimitiveSpreadBuilder.getSize:(Ljava/lang/Object;)I
        //    32: goto            37
        //    35: pop            
        //    36: iconst_1       
        //    37: iadd           
        //    38: istore_1        /* totalLength */
        //    39: iload_2         /* i */
        //    40: iload_3        
        //    41: if_icmpeq       50
        //    44: iinc            i, 1
        //    47: goto            16
        //    50: iload_1         /* totalLength */
        //    51: ireturn        
        //    StackMapTable: 00 04 FE 00 10 01 01 01 FF 00 12 00 04 07 00 02 01 01 01 00 02 01 07 00 05 FF 00 01 00 04 07 00 02 01 01 01 00 02 01 01 0C
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    protected final T toArray(@NotNull final T values, @NotNull final T result) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "values"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_2         /* result */
        //     7: ldc             "result"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: iconst_0       
        //    13: istore_3        /* dstIndex */
        //    14: iconst_0       
        //    15: istore          copyValuesFrom
        //    17: iconst_0       
        //    18: istore          5
        //    20: aload_0         /* this */
        //    21: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.size:I
        //    24: iconst_1       
        //    25: isub           
        //    26: istore          6
        //    28: iload           5
        //    30: iload           6
        //    32: if_icmpgt       119
        //    35: aload_0         /* this */
        //    36: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.spreads:[Ljava/lang/Object;
        //    39: iload           i
        //    41: aaload         
        //    42: astore          spreadArgument
        //    44: aload           spreadArgument
        //    46: ifnull          106
        //    49: iload           copyValuesFrom
        //    51: iload           i
        //    53: if_icmpge       77
        //    56: aload_1         /* values */
        //    57: iload           copyValuesFrom
        //    59: aload_2         /* result */
        //    60: iload_3         /* dstIndex */
        //    61: iload           i
        //    63: iload           copyValuesFrom
        //    65: isub           
        //    66: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //    69: iload_3         /* dstIndex */
        //    70: iload           i
        //    72: iload           copyValuesFrom
        //    74: isub           
        //    75: iadd           
        //    76: istore_3        /* dstIndex */
        //    77: aload_0         /* this */
        //    78: aload           spreadArgument
        //    80: invokevirtual   kotlin/jvm/internal/PrimitiveSpreadBuilder.getSize:(Ljava/lang/Object;)I
        //    83: istore          spreadSize
        //    85: aload           spreadArgument
        //    87: iconst_0       
        //    88: aload_2         /* result */
        //    89: iload_3         /* dstIndex */
        //    90: iload           spreadSize
        //    92: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //    95: iload_3         /* dstIndex */
        //    96: iload           spreadSize
        //    98: iadd           
        //    99: istore_3        /* dstIndex */
        //   100: iload           i
        //   102: iconst_1       
        //   103: iadd           
        //   104: istore          copyValuesFrom
        //   106: iload           i
        //   108: iload           6
        //   110: if_icmpeq       119
        //   113: iinc            i, 1
        //   116: goto            35
        //   119: iload           copyValuesFrom
        //   121: aload_0         /* this */
        //   122: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.size:I
        //   125: if_icmpge       143
        //   128: aload_1         /* values */
        //   129: iload           copyValuesFrom
        //   131: aload_2         /* result */
        //   132: iload_3         /* dstIndex */
        //   133: aload_0         /* this */
        //   134: getfield        kotlin/jvm/internal/PrimitiveSpreadBuilder.size:I
        //   137: iload           copyValuesFrom
        //   139: isub           
        //   140: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   143: aload_2         /* result */
        //   144: areturn        
        //    Signature:
        //  (TT;TT;)TT;
        //    StackMapTable: 00 05 FF 00 23 00 07 07 00 02 07 00 05 07 00 05 01 01 01 01 00 00 FC 00 29 07 00 05 1C FA 00 0C 17
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public PrimitiveSpreadBuilder(final int size) {
        this.size = size;
        this.spreads = (T[])new Object[this.size];
    }
}
