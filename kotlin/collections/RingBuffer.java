// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.TypeCastException;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import kotlin.Metadata;
import java.util.RandomAccess;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\f\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00028\u0000¢\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00028\u00002\u0006\u0010\u0017\u001a\u00020\u0006H\u0096\u0002¢\u0006\u0002\u0010\u0018J\u0006\u0010\u0019\u001a\u00020\u001aJ\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00028\u00000\u001cH\u0096\u0002J\u000e\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u0006J\u0015\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010 J'\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010\"J9\u0010#\u001a\u00020\u0013\"\u0004\b\u0001\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\t2\u0006\u0010\u0014\u001a\u0002H\u00012\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010&J\u0015\u0010'\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006(" }, d2 = { "Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "getCapacity", "()I", "<set-?>", "size", "getSize", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "fill", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "forward", "kotlin-stdlib" })
final class RingBuffer<T> extends AbstractList<T> implements RandomAccess
{
    private final Object[] buffer;
    private int startIndex;
    private int size;
    private final int capacity;
    
    @Override
    public int getSize() {
        return this.size;
    }
    
    @Override
    public T get(final int index) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: iload_1         /* index */
        //     4: aload_0         /* this */
        //     5: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //     8: invokevirtual   kotlin/collections/AbstractList$Companion.checkElementIndex$kotlin_stdlib:(II)V
        //    11: aload_0         /* this */
        //    12: getfield        kotlin/collections/RingBuffer.buffer:[Ljava/lang/Object;
        //    15: aload_0         /* this */
        //    16: aload_0         /* this */
        //    17: getfield        kotlin/collections/RingBuffer.startIndex:I
        //    20: istore_3       
        //    21: astore_2       
        //    22: astore          5
        //    24: iload_3         /* $receiver$iv */
        //    25: iload_1         /* index */
        //    26: iadd           
        //    27: aload_2         /* this_$iv */
        //    28: invokevirtual   kotlin/collections/RingBuffer.getCapacity:()I
        //    31: irem           
        //    32: istore          6
        //    34: aload           5
        //    36: iload           6
        //    38: aaload         
        //    39: areturn        
        //    Signature:
        //  (I)TT;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final boolean isFull() {
        return this.size() == this.capacity;
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)new RingBuffer$iterator.RingBuffer$iterator$1(this);
    }
    
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        T[] copy;
        if (array.length < this.size()) {
            Intrinsics.checkExpressionValueIsNotNull(copy = Arrays.copyOf(array, this.size()), "java.util.Arrays.copyOf(this, newSize)");
        }
        else {
            copy = array;
        }
        final Object[] result = copy;
        final int size = this.size();
        int widx = 0;
        for (int idx = this.startIndex; widx < size && idx < this.capacity; ++widx, ++idx) {
            result[widx] = this.buffer[idx];
        }
        for (int idx = 0; widx < size; ++widx, ++idx) {
            result[widx] = this.buffer[idx];
        }
        if (result.length > this.size()) {
            result[this.size()] = null;
        }
        final Object[] array2 = result;
        if (array2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (T[])array2;
    }
    
    @NotNull
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    public final void add(final T element) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   kotlin/collections/RingBuffer.isFull:()Z
        //     4: ifeq            20
        //     7: new             Ljava/lang/IllegalStateException;
        //    10: dup            
        //    11: ldc             "ring buffer is full"
        //    13: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    16: checkcast       Ljava/lang/Throwable;
        //    19: athrow         
        //    20: aload_0         /* this */
        //    21: getfield        kotlin/collections/RingBuffer.buffer:[Ljava/lang/Object;
        //    24: aload_0         /* this */
        //    25: aload_0         /* this */
        //    26: getfield        kotlin/collections/RingBuffer.startIndex:I
        //    29: istore_3       
        //    30: astore_2       
        //    31: aload_0         /* this */
        //    32: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //    35: istore          4
        //    37: astore          6
        //    39: iload_3         /* $receiver$iv */
        //    40: iload           n$iv
        //    42: iadd           
        //    43: aload_2         /* this_$iv */
        //    44: invokevirtual   kotlin/collections/RingBuffer.getCapacity:()I
        //    47: irem           
        //    48: istore          7
        //    50: aload           6
        //    52: iload           7
        //    54: aload_1         /* element */
        //    55: aastore        
        //    56: aload_0         /* this */
        //    57: dup            
        //    58: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //    61: dup            
        //    62: istore_2       
        //    63: iconst_1       
        //    64: iadd           
        //    65: putfield        kotlin/collections/RingBuffer.size:I
        //    68: return         
        //    Signature:
        //  (TT;)V
        //    StackMapTable: 00 01 14
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void removeFirst(final int n) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iflt            8
        //     4: iconst_1       
        //     5: goto            9
        //     8: iconst_0       
        //     9: istore_2       
        //    10: iload_2        
        //    11: ifne            51
        //    14: new             Ljava/lang/StringBuilder;
        //    17: dup            
        //    18: invokespecial   java/lang/StringBuilder.<init>:()V
        //    21: ldc             "n shouldn't be negative but it is "
        //    23: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    26: iload_1         /* n */
        //    27: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    30: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    33: astore          4
        //    35: new             Ljava/lang/IllegalArgumentException;
        //    38: dup            
        //    39: aload           4
        //    41: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    44: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    47: checkcast       Ljava/lang/Throwable;
        //    50: athrow         
        //    51: iload_1         /* n */
        //    52: aload_0         /* this */
        //    53: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //    56: if_icmpgt       63
        //    59: iconst_1       
        //    60: goto            64
        //    63: iconst_0       
        //    64: istore_2       
        //    65: iload_2        
        //    66: ifne            118
        //    69: new             Ljava/lang/StringBuilder;
        //    72: dup            
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: ldc             "n shouldn't be greater than the buffer size: n = "
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: iload_1         /* n */
        //    82: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    85: ldc             ", size = "
        //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    90: aload_0         /* this */
        //    91: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //    94: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    97: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   100: astore          4
        //   102: new             Ljava/lang/IllegalArgumentException;
        //   105: dup            
        //   106: aload           4
        //   108: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   111: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   114: checkcast       Ljava/lang/Throwable;
        //   117: athrow         
        //   118: iload_1         /* n */
        //   119: ifle            203
        //   122: aload_0         /* this */
        //   123: getfield        kotlin/collections/RingBuffer.startIndex:I
        //   126: istore_2        /* start */
        //   127: aload_0         /* this */
        //   128: iload_2         /* start */
        //   129: istore          5
        //   131: astore          this_$iv
        //   133: iload           $receiver$iv
        //   135: iload_1         /* n */
        //   136: iadd           
        //   137: aload           this_$iv
        //   139: invokevirtual   kotlin/collections/RingBuffer.getCapacity:()I
        //   142: irem           
        //   143: istore_3        /* end */
        //   144: iload_2         /* start */
        //   145: iload_3         /* end */
        //   146: if_icmple       177
        //   149: aload_0         /* this */
        //   150: aload_0         /* this */
        //   151: getfield        kotlin/collections/RingBuffer.buffer:[Ljava/lang/Object;
        //   154: aconst_null    
        //   155: iload_2         /* start */
        //   156: aload_0         /* this */
        //   157: getfield        kotlin/collections/RingBuffer.capacity:I
        //   160: invokespecial   kotlin/collections/RingBuffer.fill:([Ljava/lang/Object;Ljava/lang/Object;II)V
        //   163: aload_0         /* this */
        //   164: aload_0         /* this */
        //   165: getfield        kotlin/collections/RingBuffer.buffer:[Ljava/lang/Object;
        //   168: aconst_null    
        //   169: iconst_0       
        //   170: iload_3         /* end */
        //   171: invokespecial   kotlin/collections/RingBuffer.fill:([Ljava/lang/Object;Ljava/lang/Object;II)V
        //   174: goto            188
        //   177: aload_0         /* this */
        //   178: aload_0         /* this */
        //   179: getfield        kotlin/collections/RingBuffer.buffer:[Ljava/lang/Object;
        //   182: aconst_null    
        //   183: iload_2         /* start */
        //   184: iload_3         /* end */
        //   185: invokespecial   kotlin/collections/RingBuffer.fill:([Ljava/lang/Object;Ljava/lang/Object;II)V
        //   188: aload_0         /* this */
        //   189: iload_3         /* end */
        //   190: putfield        kotlin/collections/RingBuffer.startIndex:I
        //   193: aload_0         /* this */
        //   194: dup            
        //   195: invokevirtual   kotlin/collections/RingBuffer.size:()I
        //   198: iload_1         /* n */
        //   199: isub           
        //   200: putfield        kotlin/collections/RingBuffer.size:I
        //   203: return         
        //    StackMapTable: 00 09 08 40 01 FC 00 29 01 0B 40 01 35 FE 00 3A 01 07 00 02 01 0A F8 00 0E
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final int forward(final int $receiver, final int n) {
        return ($receiver + n) % this.getCapacity();
    }
    
    private final <T> void fill(@NotNull final T[] $receiver, final T element, final int fromIndex, final int toIndex) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore          5
        //     3: iload           toIndex
        //     5: istore          6
        //     7: iload           5
        //     9: iload           6
        //    11: if_icmpge       25
        //    14: aload_1         /* $receiver */
        //    15: iload           idx
        //    17: aload_2         /* element */
        //    18: aastore        
        //    19: iinc            idx, 1
        //    22: goto            7
        //    25: return         
        //    Signature:
        //  <T:Ljava/lang/Object;>([TT;TT;II)V
        //    StackMapTable: 00 02 FD 00 07 01 01 11
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final int getCapacity() {
        return this.capacity;
    }
    
    public RingBuffer(final int capacity) {
        this.capacity = capacity;
        if (this.capacity < 0) {
            throw new IllegalArgumentException(("ring buffer capacity should not be negative but it is " + this.capacity).toString());
        }
        this.buffer = new Object[this.capacity];
    }
}
