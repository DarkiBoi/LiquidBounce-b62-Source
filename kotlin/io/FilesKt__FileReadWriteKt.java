// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.sequences.Sequence;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import java.io.Closeable;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.BufferedReader;
import kotlin.text.Charsets;
import kotlin.internal.InlineOnly;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\b\u00f8\u0001\u0000¢\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\b\n\u0006\b\u0011(+0\u0001¨\u00061" }, d2 = { "appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib" }, xs = "kotlin/io/FilesKt")
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt
{
    @InlineOnly
    private static final InputStreamReader reader(@NotNull final File $receiver, final Charset charset) {
        return new InputStreamReader(new FileInputStream($receiver), charset);
    }
    
    @InlineOnly
    private static final BufferedReader bufferedReader(@NotNull final File $receiver, final Charset charset, final int bufferSize) {
        final InputStreamReader in = new InputStreamReader(new FileInputStream($receiver), charset);
        return (in instanceof BufferedReader) ? in : new BufferedReader(in, bufferSize);
    }
    
    @InlineOnly
    private static final OutputStreamWriter writer(@NotNull final File $receiver, final Charset charset) {
        return new OutputStreamWriter(new FileOutputStream($receiver), charset);
    }
    
    @InlineOnly
    private static final BufferedWriter bufferedWriter(@NotNull final File $receiver, final Charset charset, final int bufferSize) {
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream($receiver), charset);
        return (out instanceof BufferedWriter) ? out : new BufferedWriter(out, bufferSize);
    }
    
    @InlineOnly
    private static final PrintWriter printWriter(@NotNull final File $receiver, final Charset charset) {
        final int sz = 8192;
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream($receiver), charset);
        return new PrintWriter((out instanceof BufferedWriter) ? out : new BufferedWriter(out, sz));
    }
    
    @NotNull
    public static final byte[] readBytes(@NotNull final File $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: astore_1       
        //     8: new             Ljava/io/FileInputStream;
        //    11: dup            
        //    12: aload_1        
        //    13: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    16: checkcast       Ljava/io/Closeable;
        //    19: astore_1       
        //    20: aconst_null    
        //    21: checkcast       Ljava/lang/Throwable;
        //    24: astore_2       
        //    25: nop            
        //    26: aload_1        
        //    27: checkcast       Ljava/io/FileInputStream;
        //    30: astore_3        /* input */
        //    31: iconst_0       
        //    32: istore          offset
        //    34: aload_0         /* $receiver */
        //    35: invokevirtual   java/io/File.length:()J
        //    38: lstore          5
        //    40: lload           5
        //    42: lstore          length
        //    44: lload           length
        //    46: ldc             2147483647
        //    48: i2l            
        //    49: lcmp           
        //    50: ifle            98
        //    53: new             Ljava/lang/OutOfMemoryError;
        //    56: dup            
        //    57: new             Ljava/lang/StringBuilder;
        //    60: dup            
        //    61: invokespecial   java/lang/StringBuilder.<init>:()V
        //    64: ldc             "File "
        //    66: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    69: aload_0         /* $receiver */
        //    70: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    73: ldc             " is too big ("
        //    75: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    78: lload           length
        //    80: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //    83: ldc             " bytes) to fit in memory."
        //    85: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    88: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    91: invokespecial   java/lang/OutOfMemoryError.<init>:(Ljava/lang/String;)V
        //    94: checkcast       Ljava/lang/Throwable;
        //    97: athrow         
        //    98: nop            
        //    99: lload           5
        //   101: l2i            
        //   102: istore          remaining
        //   104: iload           remaining
        //   106: newarray        B
        //   108: astore          result
        //   110: iload           remaining
        //   112: ifle            152
        //   115: aload_3         /* input */
        //   116: aload           result
        //   118: iload           offset
        //   120: iload           remaining
        //   122: invokevirtual   java/io/FileInputStream.read:([BII)I
        //   125: istore          read
        //   127: iload           read
        //   129: ifge            135
        //   132: goto            152
        //   135: iload           remaining
        //   137: iload           read
        //   139: isub           
        //   140: istore          remaining
        //   142: iload           offset
        //   144: iload           read
        //   146: iadd           
        //   147: istore          offset
        //   149: goto            110
        //   152: iload           remaining
        //   154: ifne            162
        //   157: aload           result
        //   159: goto            179
        //   162: aload           result
        //   164: astore          10
        //   166: aload           10
        //   168: iload           offset
        //   170: invokestatic    java/util/Arrays.copyOf:([BI)[B
        //   173: dup            
        //   174: ldc             "java.util.Arrays.copyOf(this, newSize)"
        //   176: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   179: nop            
        //   180: astore_3        /* input */
        //   181: aload_1        
        //   182: aload_2        
        //   183: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   186: aload_3        
        //   187: goto            203
        //   190: astore_3       
        //   191: aload_3        
        //   192: astore_2       
        //   193: aload_3        
        //   194: athrow         
        //   195: astore_3       
        //   196: aload_1        
        //   197: aload_2        
        //   198: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   201: aload_3        
        //   202: athrow         
        //   203: areturn        
        //    StackMapTable: 00 09 FF 00 62 00 07 07 00 6B 07 00 9C 07 00 92 07 00 49 01 04 04 00 00 FF 00 0B 00 09 07 00 6B 07 00 9C 07 00 92 07 00 49 01 07 00 C1 00 04 01 00 00 FC 00 18 01 FA 00 10 09 50 07 00 C1 FF 00 0A 00 03 07 00 6B 07 00 9C 07 00 92 00 01 07 00 92 44 07 00 92 FF 00 07 00 09 07 00 6B 07 00 9C 07 00 92 07 00 C1 01 07 00 C1 00 04 01 00 01 07 00 C1
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  25     181    190    195    Ljava/lang/Throwable;
        //  25     181    195    203    Any
        //  190    195    195    203    Any
        //  195    196    195    203    Any
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
    
    public static final void writeBytes(@NotNull final File $receiver, @NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "array");
        final FileOutputStream $receiver2 = new FileOutputStream($receiver);
        Throwable cause = null;
        try {
            final FileOutputStream it = $receiver2;
            it.write(array);
            final Unit instance = Unit.INSTANCE;
        }
        catch (Throwable t) {
            cause = t;
            throw t;
        }
        finally {
            CloseableKt.closeFinally($receiver2, cause);
        }
    }
    
    public static final void appendBytes(@NotNull final File $receiver, @NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "array");
        final FileOutputStream $receiver2 = new FileOutputStream($receiver, true);
        Throwable cause = null;
        try {
            final FileOutputStream it = $receiver2;
            it.write(array);
            final Unit instance = Unit.INSTANCE;
        }
        catch (Throwable t) {
            cause = t;
            throw t;
        }
        finally {
            CloseableKt.closeFinally($receiver2, cause);
        }
    }
    
    @NotNull
    public static final String readText(@NotNull final File $receiver, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return new String(readBytes($receiver), charset);
    }
    
    public static final void writeText(@NotNull final File $receiver, @NotNull final String text, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        writeBytes($receiver, bytes);
    }
    
    public static final void appendText(@NotNull final File $receiver, @NotNull final String text, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(text, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final byte[] bytes = text.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        appendBytes($receiver, bytes);
    }
    
    public static final void forEachBlock(@NotNull final File $receiver, @NotNull final Function2<? super byte[], ? super Integer, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        forEachBlock($receiver, 4096, action);
    }
    
    public static final void forEachBlock(@NotNull final File $receiver, final int blockSize, @NotNull final Function2<? super byte[], ? super Integer, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(action, "action");
        final byte[] arr = new byte[RangesKt___RangesKt.coerceAtLeast(blockSize, 512)];
        final FileInputStream $receiver2 = new FileInputStream($receiver);
        Throwable cause = null;
        try {
            final FileInputStream input = $receiver2;
            while (true) {
                final int size = input.read(arr);
                if (size <= 0) {
                    break;
                }
                action.invoke(arr, size);
            }
            final Unit instance = Unit.INSTANCE;
        }
        catch (Throwable t) {
            cause = t;
            throw t;
        }
        finally {
            CloseableKt.closeFinally($receiver2, cause);
        }
    }
    
    public static final void forEachLine(@NotNull final File $receiver, @NotNull final Charset charset, @NotNull final Function1<? super String, Unit> action) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        Intrinsics.checkParameterIsNotNull(action, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream($receiver), charset)), action);
    }
    
    @InlineOnly
    private static final FileInputStream inputStream(@NotNull final File $receiver) {
        return new FileInputStream($receiver);
    }
    
    @InlineOnly
    private static final FileOutputStream outputStream(@NotNull final File $receiver) {
        return new FileOutputStream($receiver);
    }
    
    @NotNull
    public static final List<String> readLines(@NotNull final File $receiver, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final ArrayList result = new ArrayList();
        forEachLine($receiver, charset, (Function1<? super String, Unit>)new FilesKt__FileReadWriteKt$readLines.FilesKt__FileReadWriteKt$readLines$1(result));
        return (List<String>)result;
    }
    
    public static final <T> T useLines(@NotNull final File $receiver, @NotNull final Charset charset, @NotNull final Function1<? super Sequence<String>, ? extends T> block) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* charset */
        //     7: ldc             "charset"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* block */
        //    13: ldc_w           "block"
        //    16: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    19: aload_0         /* $receiver */
        //    20: astore          4
        //    22: sipush          8192
        //    25: istore          5
        //    27: aload           4
        //    29: astore          7
        //    31: aload           7
        //    33: astore          8
        //    35: new             Ljava/io/FileInputStream;
        //    38: dup            
        //    39: aload           8
        //    41: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    44: checkcast       Ljava/io/InputStream;
        //    47: astore          8
        //    49: new             Ljava/io/InputStreamReader;
        //    52: dup            
        //    53: aload           8
        //    55: aload_1         /* charset */
        //    56: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
        //    59: checkcast       Ljava/io/Reader;
        //    62: astore          7
        //    64: aload           7
        //    66: instanceof      Ljava/io/BufferedReader;
        //    69: ifeq            80
        //    72: aload           7
        //    74: checkcast       Ljava/io/BufferedReader;
        //    77: goto            91
        //    80: new             Ljava/io/BufferedReader;
        //    83: dup            
        //    84: aload           7
        //    86: iload           5
        //    88: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    91: checkcast       Ljava/io/Closeable;
        //    94: astore          4
        //    96: aconst_null    
        //    97: checkcast       Ljava/lang/Throwable;
        //   100: astore          5
        //   102: nop            
        //   103: aload           4
        //   105: checkcast       Ljava/io/BufferedReader;
        //   108: astore          it
        //   110: aload_2         /* block */
        //   111: aload           it
        //   113: invokestatic    kotlin/io/TextStreamsKt.lineSequence:(Ljava/io/BufferedReader;)Lkotlin/sequences/Sequence;
        //   116: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   121: astore          null
        //   123: iconst_1       
        //   124: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //   127: iconst_1       
        //   128: iconst_1       
        //   129: iconst_0       
        //   130: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   133: ifeq            146
        //   136: aload           4
        //   138: aload           5
        //   140: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   143: goto            153
        //   146: aload           4
        //   148: invokeinterface java/io/Closeable.close:()V
        //   153: iconst_1       
        //   154: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   157: aload           6
        //   159: goto            231
        //   162: astore          6
        //   164: aload           6
        //   166: astore          5
        //   168: aload           6
        //   170: athrow         
        //   171: astore          6
        //   173: iconst_1       
        //   174: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //   177: iconst_1       
        //   178: iconst_1       
        //   179: iconst_0       
        //   180: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   183: ifeq            196
        //   186: aload           4
        //   188: aload           5
        //   190: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   193: goto            224
        //   196: aload           5
        //   198: ifnonnull       211
        //   201: aload           4
        //   203: invokeinterface java/io/Closeable.close:()V
        //   208: goto            224
        //   211: nop            
        //   212: aload           4
        //   214: invokeinterface java/io/Closeable.close:()V
        //   219: goto            224
        //   222: astore          7
        //   224: iconst_1       
        //   225: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   228: aload           6
        //   230: athrow         
        //   231: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1<-Lkotlin/sequences/Sequence<Ljava/lang/String;>;+TT;>;)TT;
        //    StackMapTable: 00 0B FF 00 50 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 6B 01 00 07 00 61 07 00 4F 00 00 4A 07 00 63 FF 00 36 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 9C 07 00 92 07 01 64 07 00 61 07 00 4F 00 00 06 FF 00 08 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 9C 07 00 92 00 07 00 61 07 00 4F 00 01 07 00 92 48 07 00 92 FF 00 18 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 9C 07 00 92 07 00 92 07 00 61 07 00 4F 00 00 0E 4A 07 00 92 FF 00 01 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 9C 07 00 92 07 00 92 07 01 64 07 00 4F 00 00 FF 00 06 00 09 07 00 6B 07 00 6D 07 01 47 00 07 00 9C 07 00 92 07 01 64 07 00 61 07 00 4F 00 01 07 01 64
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  102    123    162    171    Ljava/lang/Throwable;
        //  102    123    171    231    Any
        //  162    171    171    231    Any
        //  171    173    171    231    Any
        //  211    219    222    224    Ljava/lang/Throwable;
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
    
    public FilesKt__FileReadWriteKt() {
    }
}
