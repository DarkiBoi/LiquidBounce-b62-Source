// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0002\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\bH\u0002\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0002\u001a\u0019\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b*\u00020\bH\u0001¢\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\bH\u0001¢\u0006\u0002\b\u0010\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0011" }, d2 = { "COROUTINES_DEBUG_METADATA_VERSION", "", "checkDebugMetadataVersion", "", "expected", "actual", "getDebugMetadataAnnotation", "Lkotlin/coroutines/jvm/internal/DebugMetadata;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getLabel", "getSpilledVariableFieldMapping", "", "", "(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)[Ljava/lang/String;", "getStackTraceElementImpl", "Ljava/lang/StackTraceElement;", "getStackTraceElement", "kotlin-stdlib" })
public final class DebugMetadataKt
{
    private static final int COROUTINES_DEBUG_METADATA_VERSION = 1;
    
    @SinceKotlin(version = "1.3")
    @JvmName(name = "getStackTraceElement")
    @Nullable
    public static final StackTraceElement getStackTraceElement(@NotNull final BaseContinuationImpl $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final DebugMetadata debugMetadataAnnotation = getDebugMetadataAnnotation($receiver);
        if (debugMetadataAnnotation != null) {
            final DebugMetadata debugMetadata = debugMetadataAnnotation;
            checkDebugMetadataVersion(1, debugMetadata.v());
            final int label = getLabel($receiver);
            final int lineNumber = (label < 0) ? -1 : debugMetadata.l()[label];
            final String moduleName = ModuleNameRetriever.INSTANCE.getModuleName($receiver);
            final String moduleAndClass = (moduleName == null) ? debugMetadata.c() : (moduleName + '/' + debugMetadata.c());
            return new StackTraceElement(moduleAndClass, debugMetadata.m(), debugMetadata.f(), lineNumber);
        }
        return null;
    }
    
    private static final DebugMetadata getDebugMetadataAnnotation(@NotNull final BaseContinuationImpl $receiver) {
        return $receiver.getClass().getAnnotation(DebugMetadata.class);
    }
    
    private static final int getLabel(@NotNull final BaseContinuationImpl $receiver) {
        int n2;
        try {
            final Field declaredField;
            final Field field = declaredField = $receiver.getClass().getDeclaredField("label");
            Intrinsics.checkExpressionValueIsNotNull(declaredField, "field");
            declaredField.setAccessible(true);
            Object value;
            if (!((value = field.get($receiver)) instanceof Integer)) {
                value = null;
            }
            final Integer n = (Integer)value;
            n2 = ((n != null) ? n : 0) - 1;
        }
        catch (Exception e) {
            n2 = -1;
        }
        return n2;
    }
    
    private static final void checkDebugMetadataVersion(final int expected, final int actual) {
        if (actual > expected) {
            throw new IllegalStateException(("Debug metadata version mismatch. Expected: " + expected + ", got " + actual + ". Please update the Kotlin standard library.").toString());
        }
    }
    
    @SinceKotlin(version = "1.3")
    @JvmName(name = "getSpilledVariableFieldMapping")
    @Nullable
    public static final String[] getSpilledVariableFieldMapping(@NotNull final BaseContinuationImpl $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/coroutines/jvm/internal/DebugMetadataKt.getDebugMetadataAnnotation:(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)Lkotlin/coroutines/jvm/internal/DebugMetadata;
        //    10: dup            
        //    11: ifnull          17
        //    14: goto            20
        //    17: pop            
        //    18: aconst_null    
        //    19: areturn        
        //    20: astore_1        /* debugMetadata */
        //    21: iconst_1       
        //    22: aload_1         /* debugMetadata */
        //    23: invokeinterface kotlin/coroutines/jvm/internal/DebugMetadata.v:()I
        //    28: invokestatic    kotlin/coroutines/jvm/internal/DebugMetadataKt.checkDebugMetadataVersion:(II)V
        //    31: new             Ljava/util/ArrayList;
        //    34: dup            
        //    35: invokespecial   java/util/ArrayList.<init>:()V
        //    38: astore_2        /* res */
        //    39: aload_0         /* $receiver */
        //    40: invokestatic    kotlin/coroutines/jvm/internal/DebugMetadataKt.getLabel:(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)I
        //    43: istore_3        /* label */
        //    44: aload_1         /* debugMetadata */
        //    45: invokeinterface kotlin/coroutines/jvm/internal/DebugMetadata.i:()[I
        //    50: astore          6
        //    52: aload           6
        //    54: arraylength    
        //    55: istore          7
        //    57: iconst_0       
        //    58: istore          4
        //    60: iload           4
        //    62: iload           7
        //    64: if_icmpge       114
        //    67: aload           6
        //    69: iload           4
        //    71: iaload         
        //    72: istore          labelOfIndex
        //    74: iload           labelOfIndex
        //    76: iload_3         /* label */
        //    77: if_icmpne       108
        //    80: aload_2         /* res */
        //    81: aload_1         /* debugMetadata */
        //    82: invokeinterface kotlin/coroutines/jvm/internal/DebugMetadata.s:()[Ljava/lang/String;
        //    87: iload           i
        //    89: aaload         
        //    90: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    93: pop            
        //    94: aload_2         /* res */
        //    95: aload_1         /* debugMetadata */
        //    96: invokeinterface kotlin/coroutines/jvm/internal/DebugMetadata.n:()[Ljava/lang/String;
        //   101: iload           i
        //   103: aaload         
        //   104: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   107: pop            
        //   108: iinc            i, 1
        //   111: goto            60
        //   114: aload_2         /* res */
        //   115: checkcast       Ljava/util/Collection;
        //   118: astore          $receiver$iv
        //   120: aload           $receiver$iv
        //   122: astore          thisCollection$iv
        //   124: aload           thisCollection$iv
        //   126: iconst_0       
        //   127: anewarray       Ljava/lang/String;
        //   130: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   135: dup            
        //   136: ifnonnull       149
        //   139: new             Lkotlin/TypeCastException;
        //   142: dup            
        //   143: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //   145: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   148: athrow         
        //   149: checkcast       [Ljava/lang/String;
        //   152: areturn        
        //    StackMapTable: 00 06 51 07 00 36 42 07 00 36 FF 00 27 00 08 07 00 9A 07 00 36 07 00 B3 01 01 00 07 00 B9 01 00 00 FF 00 2F 00 08 07 00 9A 07 00 36 07 00 B3 01 01 01 07 00 B9 01 00 00 FF 00 05 00 08 07 00 9A 07 00 36 07 00 B3 01 01 00 07 00 B9 01 00 00 FF 00 22 00 08 07 00 9A 07 00 36 07 00 B3 01 07 00 C6 07 00 C6 07 00 B9 01 00 01 07 00 D1
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
