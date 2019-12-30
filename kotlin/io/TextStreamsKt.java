// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.text.Charsets;
import java.nio.charset.Charset;
import java.net.URL;
import java.io.StringWriter;
import java.io.StringReader;
import kotlin.sequences.Sequence;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.io.BufferedWriter;
import java.io.Writer;
import kotlin.internal.InlineOnly;
import java.io.BufferedReader;
import org.jetbrains.annotations.NotNull;
import java.io.Reader;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 2, d1 = { "\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\u0087\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\u0086\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001f\u0082\u0002\b\n\u0006\b\u0011(\u001e0\u0001¨\u0006 " }, d2 = { "buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib" })
@JvmName(name = "TextStreamsKt")
public final class TextStreamsKt
{
    @InlineOnly
    private static final BufferedReader buffered(@NotNull final Reader $receiver, final int bufferSize) {
        return (BufferedReader)(($receiver instanceof BufferedReader) ? $receiver : new BufferedReader($receiver, bufferSize));
    }
    
    @InlineOnly
    private static final BufferedWriter buffered(@NotNull final Writer $receiver, final int bufferSize) {
        return (BufferedWriter)(($receiver instanceof BufferedWriter) ? $receiver : new BufferedWriter($receiver, bufferSize));
    }
    
    public static final void forEachLine(@NotNull final Reader $receiver, @NotNull final Function1<? super String, Unit> action) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* action */
        //     7: ldc             "action"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_2        /* $receiver$iv */
        //    14: aload_2         /* $receiver$iv */
        //    15: astore_3       
        //    16: sipush          8192
        //    19: istore          4
        //    21: aload_3        
        //    22: instanceof      Ljava/io/BufferedReader;
        //    25: ifeq            35
        //    28: aload_3        
        //    29: checkcast       Ljava/io/BufferedReader;
        //    32: goto            45
        //    35: new             Ljava/io/BufferedReader;
        //    38: dup            
        //    39: aload_3        
        //    40: iload           4
        //    42: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    45: checkcast       Ljava/io/Closeable;
        //    48: astore_3       
        //    49: aconst_null    
        //    50: checkcast       Ljava/lang/Throwable;
        //    53: astore          4
        //    55: nop            
        //    56: aload_3        
        //    57: checkcast       Ljava/io/BufferedReader;
        //    60: astore          it$iv
        //    62: aload           it$iv
        //    64: invokestatic    kotlin/io/TextStreamsKt.lineSequence:(Ljava/io/BufferedReader;)Lkotlin/sequences/Sequence;
        //    67: astore          it
        //    69: aload           it
        //    71: astore          7
        //    73: aload_1         /* action */
        //    74: astore          action$iv
        //    76: aload           $receiver$iv
        //    78: invokeinterface kotlin/sequences/Sequence.iterator:()Ljava/util/Iterator;
        //    83: astore          9
        //    85: aload           9
        //    87: invokeinterface java/util/Iterator.hasNext:()Z
        //    92: ifeq            117
        //    95: aload           9
        //    97: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   102: astore          element$iv
        //   104: aload           action$iv
        //   106: aload           element$iv
        //   108: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   113: pop            
        //   114: goto            85
        //   117: nop            
        //   118: nop            
        //   119: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   122: astore          null
        //   124: aload_3        
        //   125: aload           4
        //   127: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   130: goto            153
        //   133: astore          5
        //   135: aload           5
        //   137: astore          4
        //   139: aload           5
        //   141: athrow         
        //   142: astore          5
        //   144: aload_3        
        //   145: aload           4
        //   147: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   150: aload           5
        //   152: athrow         
        //   153: nop            
        //   154: return         
        //    Signature:
        //  (Ljava/io/Reader;Lkotlin/jvm/functions/Function1<-Ljava/lang/String;Lkotlin/Unit;>;)V
        //    StackMapTable: 00 07 FE 00 23 07 00 55 07 00 55 01 49 07 00 37 FF 00 27 00 0A 07 00 55 07 00 62 07 00 55 07 00 57 07 00 4A 07 00 37 07 00 5C 07 00 5C 07 00 62 07 00 64 00 00 1F FF 00 0F 00 05 07 00 55 07 00 62 07 00 55 07 00 57 07 00 4A 00 01 07 00 4A 48 07 00 4A FF 00 0A 00 0A 07 00 55 07 00 62 07 00 55 07 00 57 07 00 4A 07 00 72 07 00 5C 07 00 5C 07 00 62 07 00 64 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  55     124    133    142    Ljava/lang/Throwable;
        //  55     124    142    153    Any
        //  133    142    142    153    Any
        //  142    144    142    153    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final List<String> readLines(@NotNull final Reader $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final ArrayList result = new ArrayList();
        forEachLine($receiver, (Function1<? super String, Unit>)new TextStreamsKt$readLines.TextStreamsKt$readLines$1(result));
        return (List<String>)result;
    }
    
    public static final <T> T useLines(@NotNull final Reader $receiver, @NotNull final Function1<? super Sequence<String>, ? extends T> block) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* block */
        //     7: ldc             "block"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: astore_3       
        //    14: sipush          8192
        //    17: istore          4
        //    19: aload_3        
        //    20: instanceof      Ljava/io/BufferedReader;
        //    23: ifeq            33
        //    26: aload_3        
        //    27: checkcast       Ljava/io/BufferedReader;
        //    30: goto            43
        //    33: new             Ljava/io/BufferedReader;
        //    36: dup            
        //    37: aload_3        
        //    38: iload           4
        //    40: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    43: checkcast       Ljava/io/Closeable;
        //    46: astore_3       
        //    47: aconst_null    
        //    48: checkcast       Ljava/lang/Throwable;
        //    51: astore          4
        //    53: nop            
        //    54: aload_3        
        //    55: checkcast       Ljava/io/BufferedReader;
        //    58: astore          it
        //    60: aload_1         /* block */
        //    61: aload           it
        //    63: invokestatic    kotlin/io/TextStreamsKt.lineSequence:(Ljava/io/BufferedReader;)Lkotlin/sequences/Sequence;
        //    66: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    71: astore          null
        //    73: iconst_1       
        //    74: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //    77: iconst_1       
        //    78: iconst_1       
        //    79: iconst_0       
        //    80: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //    83: ifeq            95
        //    86: aload_3        
        //    87: aload           4
        //    89: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    92: goto            101
        //    95: aload_3        
        //    96: invokeinterface java/io/Closeable.close:()V
        //   101: iconst_1       
        //   102: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   105: aload           5
        //   107: goto            176
        //   110: astore          5
        //   112: aload           5
        //   114: astore          4
        //   116: aload           5
        //   118: athrow         
        //   119: astore          5
        //   121: iconst_1       
        //   122: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //   125: iconst_1       
        //   126: iconst_1       
        //   127: iconst_0       
        //   128: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   131: ifeq            143
        //   134: aload_3        
        //   135: aload           4
        //   137: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   140: goto            169
        //   143: aload           4
        //   145: ifnonnull       157
        //   148: aload_3        
        //   149: invokeinterface java/io/Closeable.close:()V
        //   154: goto            169
        //   157: nop            
        //   158: aload_3        
        //   159: invokeinterface java/io/Closeable.close:()V
        //   164: goto            169
        //   167: astore          6
        //   169: iconst_1       
        //   170: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   173: aload           5
        //   175: athrow         
        //   176: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/io/Reader;Lkotlin/jvm/functions/Function1<-Lkotlin/sequences/Sequence<Ljava/lang/String;>;+TT;>;)TT;
        //    StackMapTable: 00 0B FE 00 21 00 07 00 55 01 49 07 00 37 FF 00 33 00 06 07 00 55 07 00 62 00 07 00 57 07 00 4A 07 00 04 00 00 05 FF 00 08 00 05 07 00 55 07 00 62 00 07 00 57 07 00 4A 00 01 07 00 4A 48 07 00 4A FC 00 17 07 00 4A 0D 49 07 00 4A 01 FF 00 06 00 06 07 00 55 07 00 62 00 07 00 57 07 00 4A 07 00 04 00 01 07 00 04
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  53     73     110    119    Ljava/lang/Throwable;
        //  53     73     119    176    Any
        //  110    119    119    176    Any
        //  119    121    119    176    Any
        //  157    164    167    169    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:535)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:595)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
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
    
    @InlineOnly
    private static final StringReader reader(@NotNull final String $receiver) {
        return new StringReader($receiver);
    }
    
    @NotNull
    public static final Sequence<String> lineSequence(@NotNull final BufferedReader $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return SequencesKt__SequencesKt.constrainOnce((Sequence<? extends String>)new LinesSequence($receiver));
    }
    
    @NotNull
    public static final String readText(@NotNull final Reader $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final StringWriter buffer = new StringWriter();
        copyTo$default($receiver, buffer, 0, 2, null);
        final String string = buffer.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "buffer.toString()");
        return string;
    }
    
    public static final long copyTo(@NotNull final Reader $receiver, @NotNull final Writer out, final int bufferSize) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(out, "out");
        long charsCopied = 0L;
        final char[] buffer = new char[bufferSize];
        for (int chars = $receiver.read(buffer); chars >= 0; chars = $receiver.read(buffer)) {
            out.write(buffer, 0, chars);
            charsCopied += chars;
        }
        return charsCopied;
    }
    
    public static /* synthetic */ long copyTo$default(final Reader $receiver, final Writer out, int bufferSize, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            bufferSize = 8192;
        }
        return copyTo($receiver, out, bufferSize);
    }
    
    @InlineOnly
    private static final String readText(@NotNull final URL $receiver, final Charset charset) {
        return new String(readBytes($receiver), charset);
    }
    
    @NotNull
    public static final byte[] readBytes(@NotNull final URL $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokevirtual   java/net/URL.openStream:()Ljava/io/InputStream;
        //    10: checkcast       Ljava/io/Closeable;
        //    13: astore_1       
        //    14: aconst_null    
        //    15: checkcast       Ljava/lang/Throwable;
        //    18: astore_2       
        //    19: nop            
        //    20: aload_1        
        //    21: checkcast       Ljava/io/InputStream;
        //    24: astore_3       
        //    25: aload_3        
        //    26: dup            
        //    27: ldc             "it"
        //    29: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    32: invokestatic    kotlin/io/ByteStreamsKt.readBytes:(Ljava/io/InputStream;)[B
        //    35: astore_3       
        //    36: aload_1        
        //    37: aload_2        
        //    38: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    41: aload_3        
        //    42: goto            58
        //    45: astore_3       
        //    46: aload_3        
        //    47: astore_2       
        //    48: aload_3        
        //    49: athrow         
        //    50: astore_3       
        //    51: aload_1        
        //    52: aload_2        
        //    53: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    56: aload_3        
        //    57: athrow         
        //    58: areturn        
        //    StackMapTable: 00 03 FF 00 2D 00 03 07 00 F5 07 00 57 07 00 4A 00 01 07 00 4A 44 07 00 4A FF 00 07 00 04 07 00 F5 07 00 57 07 00 4A 07 01 03 00 01 07 01 03
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  19     36     45     50     Ljava/lang/Throwable;
        //  19     36     50     58     Any
        //  45     50     50     58     Any
        //  50     51     50     58     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:535)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:595)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
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
