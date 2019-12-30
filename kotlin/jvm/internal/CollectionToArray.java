// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.util.Iterator;
import java.util.Arrays;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015" }, d2 = { "EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib" })
@JvmName(name = "CollectionToArray")
public final class CollectionToArray
{
    private static final Object[] EMPTY;
    private static final int MAX_SIZE = 2147483645;
    
    @JvmName(name = "toArray")
    @NotNull
    public static final Object[] toArray(@NotNull final Collection<?> collection) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "collection"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: nop            
        //     7: aload_0         /* collection */
        //     8: invokeinterface java/util/Collection.size:()I
        //    13: istore_1        /* size$iv */
        //    14: iload_1         /* size$iv */
        //    15: ifne            24
        //    18: getstatic       kotlin/jvm/internal/CollectionToArray.EMPTY:[Ljava/lang/Object;
        //    21: goto            179
        //    24: aload_0         /* collection */
        //    25: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //    30: astore_2        /* iter$iv */
        //    31: aload_2         /* iter$iv */
        //    32: invokeinterface java/util/Iterator.hasNext:()Z
        //    37: ifne            46
        //    40: getstatic       kotlin/jvm/internal/CollectionToArray.EMPTY:[Ljava/lang/Object;
        //    43: goto            179
        //    46: iload_1         /* size$iv */
        //    47: istore_3        /* size */
        //    48: iload_3         /* size */
        //    49: anewarray       Ljava/lang/Object;
        //    52: astore_3        /* size */
        //    53: iconst_0       
        //    54: istore          i$iv
        //    56: aload_3         /* result$iv */
        //    57: iload           i$iv
        //    59: iinc            i$iv, 1
        //    62: aload_2         /* iter$iv */
        //    63: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    68: aastore        
        //    69: iload           i$iv
        //    71: aload_3         /* result$iv */
        //    72: arraylength    
        //    73: if_icmplt       144
        //    76: aload_2         /* iter$iv */
        //    77: invokeinterface java/util/Iterator.hasNext:()Z
        //    82: ifne            89
        //    85: aload_3         /* result$iv */
        //    86: goto            179
        //    89: iload           i$iv
        //    91: iconst_3       
        //    92: imul           
        //    93: iconst_1       
        //    94: iadd           
        //    95: iconst_1       
        //    96: iushr          
        //    97: istore          newSize$iv
        //    99: iload           newSize$iv
        //   101: iload           i$iv
        //   103: if_icmpgt       128
        //   106: iload           i$iv
        //   108: ldc             2147483645
        //   110: if_icmplt       124
        //   113: new             Ljava/lang/OutOfMemoryError;
        //   116: dup            
        //   117: invokespecial   java/lang/OutOfMemoryError.<init>:()V
        //   120: checkcast       Ljava/lang/Throwable;
        //   123: athrow         
        //   124: ldc             2147483645
        //   126: istore          newSize$iv
        //   128: aload_3         /* result$iv */
        //   129: iload           newSize$iv
        //   131: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //   134: dup            
        //   135: ldc             "Arrays.copyOf(result, newSize)"
        //   137: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   140: astore_3        /* result$iv */
        //   141: goto            176
        //   144: aload_2         /* iter$iv */
        //   145: invokeinterface java/util/Iterator.hasNext:()Z
        //   150: ifne            176
        //   153: aload_3         /* result$iv */
        //   154: iload           i$iv
        //   156: istore          6
        //   158: astore          result
        //   160: aload           result
        //   162: iload           size
        //   164: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //   167: dup            
        //   168: ldc             "Arrays.copyOf(result, size)"
        //   170: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   173: goto            179
        //   176: goto            56
        //   179: areturn        
        //    Signature:
        //  (Ljava/util/Collection<*>;)[Ljava/lang/Object;
        //    StackMapTable: 00 09 FC 00 18 01 FC 00 15 07 00 3F FD 00 09 07 00 44 01 20 FC 00 22 01 03 FA 00 0F 1F FF 00 02 00 02 07 00 33 01 00 01 07 00 44
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @JvmName(name = "toArray")
    @NotNull
    public static final Object[] toArray(@NotNull final Collection<?> collection, @Nullable final Object[] a) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "collection"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* a */
        //     7: ifnonnull       21
        //    10: new             Ljava/lang/NullPointerException;
        //    13: dup            
        //    14: invokespecial   java/lang/NullPointerException.<init>:()V
        //    17: checkcast       Ljava/lang/Throwable;
        //    20: athrow         
        //    21: nop            
        //    22: aload_0         /* collection */
        //    23: invokeinterface java/util/Collection.size:()I
        //    28: istore_2        /* size$iv */
        //    29: iload_2         /* size$iv */
        //    30: ifne            46
        //    33: aload_1         /* a */
        //    34: arraylength    
        //    35: ifle            42
        //    38: aload_1         /* a */
        //    39: iconst_0       
        //    40: aconst_null    
        //    41: aastore        
        //    42: aload_1         /* a */
        //    43: goto            262
        //    46: aload_0         /* collection */
        //    47: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //    52: astore          iter$iv
        //    54: aload           iter$iv
        //    56: invokeinterface java/util/Iterator.hasNext:()Z
        //    61: ifne            77
        //    64: aload_1         /* a */
        //    65: arraylength    
        //    66: ifle            73
        //    69: aload_1         /* a */
        //    70: iconst_0       
        //    71: aconst_null    
        //    72: aastore        
        //    73: aload_1         /* a */
        //    74: goto            262
        //    77: iload_2         /* size$iv */
        //    78: istore_3        /* size */
        //    79: iload_3         /* size */
        //    80: aload_1         /* a */
        //    81: arraylength    
        //    82: if_icmpgt       89
        //    85: aload_1         /* a */
        //    86: goto            117
        //    89: aload_1         /* a */
        //    90: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    93: invokevirtual   java/lang/Class.getComponentType:()Ljava/lang/Class;
        //    96: iload_3         /* size */
        //    97: invokestatic    java/lang/reflect/Array.newInstance:(Ljava/lang/Class;I)Ljava/lang/Object;
        //   100: dup            
        //   101: ifnonnull       114
        //   104: new             Lkotlin/TypeCastException;
        //   107: dup            
        //   108: ldc             "null cannot be cast to non-null type kotlin.Array<kotlin.Any?>"
        //   110: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   113: athrow         
        //   114: checkcast       [Ljava/lang/Object;
        //   117: astore          result$iv
        //   119: iconst_0       
        //   120: istore_3        /* i$iv */
        //   121: aload           result$iv
        //   123: iload_3         /* i$iv */
        //   124: iinc            i$iv, 1
        //   127: aload           iter$iv
        //   129: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   134: aastore        
        //   135: iload_3         /* i$iv */
        //   136: aload           result$iv
        //   138: arraylength    
        //   139: if_icmplt       211
        //   142: aload           iter$iv
        //   144: invokeinterface java/util/Iterator.hasNext:()Z
        //   149: ifne            157
        //   152: aload           result$iv
        //   154: goto            262
        //   157: iload_3         /* i$iv */
        //   158: iconst_3       
        //   159: imul           
        //   160: iconst_1       
        //   161: iadd           
        //   162: iconst_1       
        //   163: iushr          
        //   164: istore          newSize$iv
        //   166: iload           newSize$iv
        //   168: iload_3         /* i$iv */
        //   169: if_icmpgt       193
        //   172: iload_3         /* i$iv */
        //   173: ldc             2147483645
        //   175: if_icmplt       189
        //   178: new             Ljava/lang/OutOfMemoryError;
        //   181: dup            
        //   182: invokespecial   java/lang/OutOfMemoryError.<init>:()V
        //   185: checkcast       Ljava/lang/Throwable;
        //   188: athrow         
        //   189: ldc             2147483645
        //   191: istore          newSize$iv
        //   193: aload           result$iv
        //   195: iload           newSize$iv
        //   197: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //   200: dup            
        //   201: ldc             "Arrays.copyOf(result, newSize)"
        //   203: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   206: astore          result$iv
        //   208: goto            259
        //   211: aload           iter$iv
        //   213: invokeinterface java/util/Iterator.hasNext:()Z
        //   218: ifne            259
        //   221: aload           result$iv
        //   223: iload_3         /* i$iv */
        //   224: istore          7
        //   226: astore          result
        //   228: aload           result
        //   230: aload_1         /* a */
        //   231: if_acmpne       243
        //   234: aload_1         /* a */
        //   235: iload           size
        //   237: aconst_null    
        //   238: aastore        
        //   239: aload_1         /* a */
        //   240: goto            256
        //   243: aload           result
        //   245: iload           size
        //   247: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //   250: dup            
        //   251: ldc             "Arrays.copyOf(result, size)"
        //   253: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   256: goto            262
        //   259: goto            121
        //   262: areturn        
        //    Signature:
        //  (Ljava/util/Collection<*>;[Ljava/lang/Object;)[Ljava/lang/Object;
        //    StackMapTable: 00 11 15 FC 00 14 01 03 FD 00 1A 00 07 00 3F 03 FF 00 0B 00 05 07 00 33 07 00 44 01 01 07 00 3F 00 00 58 07 00 04 42 07 00 44 FD 00 03 00 07 00 44 23 FF 00 1F 00 07 07 00 33 07 00 44 01 01 07 00 3F 01 07 00 44 00 00 03 FF 00 11 00 07 07 00 33 07 00 44 01 01 07 00 3F 00 07 00 44 00 00 FD 00 1F 01 07 00 44 4C 07 00 44 F9 00 02 FF 00 02 00 03 07 00 33 07 00 44 01 00 01 07 00 44
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final Object[] toArrayImpl(final Collection<?> collection, final Function0<Object[]> empty, final Function1<? super Integer, Object[]> alloc, final Function2<? super Object[], ? super Integer, Object[]> trim) {
        final int size = collection.size();
        if (size == 0) {
            return empty.invoke();
        }
        final Iterator iter = collection.iterator();
        if (!iter.hasNext()) {
            return empty.invoke();
        }
        Object[] result = alloc.invoke(size);
        int i = 0;
        while (true) {
            result[i++] = iter.next();
            if (i >= result.length) {
                if (!iter.hasNext()) {
                    return result;
                }
                int newSize = i * 3 + 1 >>> 1;
                if (newSize <= i) {
                    if (i >= 2147483645) {
                        throw new OutOfMemoryError();
                    }
                    newSize = 2147483645;
                }
                final Object[] copy = Arrays.copyOf(result, newSize);
                Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, newSize)");
                result = copy;
            }
            else {
                if (!iter.hasNext()) {
                    return trim.invoke(result, i);
                }
                continue;
            }
        }
    }
    
    static {
        EMPTY = new Object[0];
    }
}
