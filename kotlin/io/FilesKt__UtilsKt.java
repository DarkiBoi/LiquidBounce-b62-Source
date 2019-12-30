// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import java.util.List;
import java.util.Iterator;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a(\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a(\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+" }, d2 = { "extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib" }, xs = "kotlin/io/FilesKt")
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt
{
    @NotNull
    public static final File createTempDir(@NotNull final String prefix, @Nullable final String suffix, @Nullable final File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        final File dir = File.createTempFile(prefix, suffix, directory);
        dir.delete();
        if (dir.mkdir()) {
            final File value = dir;
            Intrinsics.checkExpressionValueIsNotNull(value, "dir");
            return value;
        }
        throw new IOException("Unable to create temporary directory " + dir + '.');
    }
    
    @NotNull
    public static final File createTempFile(@NotNull final String prefix, @Nullable final String suffix, @Nullable final File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        final File tempFile = File.createTempFile(prefix, suffix, directory);
        Intrinsics.checkExpressionValueIsNotNull(tempFile, "File.createTempFile(prefix, suffix, directory)");
        return tempFile;
    }
    
    @NotNull
    public static final String getExtension(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final String name = $receiver.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt__StringsKt.substringAfterLast(name, '.', "");
    }
    
    @NotNull
    public static final String getInvariantSeparatorsPath(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        String s;
        if (File.separatorChar != '/') {
            final String path = $receiver.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "path");
            s = StringsKt__StringsJVMKt.replace$default(path, File.separatorChar, '/', false, 4, null);
        }
        else {
            Intrinsics.checkExpressionValueIsNotNull(s = $receiver.getPath(), "path");
        }
        return s;
    }
    
    @NotNull
    public static final String getNameWithoutExtension(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final String name = $receiver.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt__StringsKt.substringBeforeLast$default(name, ".", null, 2, null);
    }
    
    @NotNull
    public static final String toRelativeString(@NotNull final File $receiver, @NotNull final File base) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(base, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($receiver, base);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        throw new IllegalArgumentException("this and base files have different roots: " + $receiver + " and " + base + '.');
    }
    
    @NotNull
    public static final File relativeTo(@NotNull final File $receiver, @NotNull final File base) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(base, "base");
        return new File(toRelativeString($receiver, base));
    }
    
    @NotNull
    public static final File relativeToOrSelf(@NotNull final File $receiver, @NotNull final File base) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(base, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($receiver, base);
        File file;
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            final String p1 = relativeStringOrNull$FilesKt__UtilsKt;
            file = new File(p1);
        }
        else {
            file = $receiver;
        }
        return file;
    }
    
    @Nullable
    public static final File relativeToOrNull(@NotNull final File $receiver, @NotNull final File base) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(base, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($receiver, base);
        File file;
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            final String p1 = relativeStringOrNull$FilesKt__UtilsKt;
            file = new File(p1);
        }
        else {
            file = null;
        }
        return file;
    }
    
    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(@NotNull final File $receiver, final File base) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    kotlin/io/FilesKt.toComponents:(Ljava/io/File;)Lkotlin/io/FilePathComponents;
        //     4: invokestatic    kotlin/io/FilesKt__UtilsKt.normalize$FilesKt__UtilsKt:(Lkotlin/io/FilePathComponents;)Lkotlin/io/FilePathComponents;
        //     7: astore_2        /* thisComponents */
        //     8: aload_1         /* base */
        //     9: invokestatic    kotlin/io/FilesKt.toComponents:(Ljava/io/File;)Lkotlin/io/FilePathComponents;
        //    12: invokestatic    kotlin/io/FilesKt__UtilsKt.normalize$FilesKt__UtilsKt:(Lkotlin/io/FilePathComponents;)Lkotlin/io/FilePathComponents;
        //    15: astore_3        /* baseComponents */
        //    16: aload_2         /* thisComponents */
        //    17: invokevirtual   kotlin/io/FilePathComponents.getRoot:()Ljava/io/File;
        //    20: aload_3         /* baseComponents */
        //    21: invokevirtual   kotlin/io/FilePathComponents.getRoot:()Ljava/io/File;
        //    24: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    27: iconst_1       
        //    28: ixor           
        //    29: ifeq            34
        //    32: aconst_null    
        //    33: areturn        
        //    34: aload_3         /* baseComponents */
        //    35: invokevirtual   kotlin/io/FilePathComponents.getSize:()I
        //    38: istore          baseCount
        //    40: aload_2         /* thisComponents */
        //    41: invokevirtual   kotlin/io/FilePathComponents.getSize:()I
        //    44: istore          thisCount
        //    46: aload_0         /* $receiver */
        //    47: astore          7
        //    49: aload           7
        //    51: astore          $receiver
        //    53: iconst_0       
        //    54: istore          i
        //    56: iload           thisCount
        //    58: istore          10
        //    60: iload           baseCount
        //    62: istore          11
        //    64: iload           10
        //    66: iload           11
        //    68: invokestatic    java/lang/Math.min:(II)I
        //    71: istore          maxSameCount
        //    73: iload           i
        //    75: iload           maxSameCount
        //    77: if_icmpge       120
        //    80: aload_2         /* thisComponents */
        //    81: invokevirtual   kotlin/io/FilePathComponents.getSegments:()Ljava/util/List;
        //    84: iload           i
        //    86: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    91: checkcast       Ljava/io/File;
        //    94: aload_3         /* baseComponents */
        //    95: invokevirtual   kotlin/io/FilePathComponents.getSegments:()Ljava/util/List;
        //    98: iload           i
        //   100: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   105: checkcast       Ljava/io/File;
        //   108: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   111: ifeq            120
        //   114: iinc            i, 1
        //   117: goto            73
        //   120: iload           i
        //   122: istore          sameCount
        //   124: new             Ljava/lang/StringBuilder;
        //   127: dup            
        //   128: invokespecial   java/lang/StringBuilder.<init>:()V
        //   131: astore          res
        //   133: iload           baseCount
        //   135: iconst_1       
        //   136: isub           
        //   137: istore          8
        //   139: iload           sameCount
        //   141: istore          9
        //   143: iload           8
        //   145: iload           9
        //   147: if_icmplt       214
        //   150: aload_3         /* baseComponents */
        //   151: invokevirtual   kotlin/io/FilePathComponents.getSegments:()Ljava/util/List;
        //   154: iload           i
        //   156: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   161: checkcast       Ljava/io/File;
        //   164: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   167: ldc             ".."
        //   169: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   172: ifeq            177
        //   175: aconst_null    
        //   176: areturn        
        //   177: aload           res
        //   179: ldc             ".."
        //   181: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   184: pop            
        //   185: iload           i
        //   187: iload           sameCount
        //   189: if_icmpeq       201
        //   192: aload           res
        //   194: getstatic       java/io/File.separatorChar:C
        //   197: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   200: pop            
        //   201: iload           i
        //   203: iload           9
        //   205: if_icmpeq       214
        //   208: iinc            i, -1
        //   211: goto            150
        //   214: iload           sameCount
        //   216: iload           thisCount
        //   218: if_icmpge       281
        //   221: iload           sameCount
        //   223: iload           baseCount
        //   225: if_icmpge       237
        //   228: aload           res
        //   230: getstatic       java/io/File.separatorChar:C
        //   233: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   236: pop            
        //   237: aload_2         /* thisComponents */
        //   238: invokevirtual   kotlin/io/FilePathComponents.getSegments:()Ljava/util/List;
        //   241: checkcast       Ljava/lang/Iterable;
        //   244: iload           sameCount
        //   246: invokestatic    kotlin/collections/CollectionsKt.drop:(Ljava/lang/Iterable;I)Ljava/util/List;
        //   249: checkcast       Ljava/lang/Iterable;
        //   252: aload           res
        //   254: checkcast       Ljava/lang/Appendable;
        //   257: getstatic       java/io/File.separator:Ljava/lang/String;
        //   260: dup            
        //   261: ldc             "File.separator"
        //   263: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   266: checkcast       Ljava/lang/CharSequence;
        //   269: aconst_null    
        //   270: aconst_null    
        //   271: iconst_0       
        //   272: aconst_null    
        //   273: aconst_null    
        //   274: bipush          124
        //   276: aconst_null    
        //   277: invokestatic    kotlin/collections/CollectionsKt.joinTo$default:(Ljava/lang/Iterable;Ljava/lang/Appendable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Appendable;
        //   280: pop            
        //   281: aload           res
        //   283: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   286: areturn        
        //    StackMapTable: 00 09 FD 00 22 07 00 C3 07 00 C3 FF 00 26 00 0D 07 00 4D 07 00 4D 07 00 C3 07 00 C3 01 01 00 07 00 4D 07 00 4D 01 01 01 01 00 00 2E FF 00 1D 00 0D 07 00 4D 07 00 4D 07 00 C3 07 00 C3 01 01 01 07 00 5F 01 01 01 01 01 00 00 1A 17 0C 16 2B
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final File copyTo(@NotNull final File $receiver, @NotNull final File target, final boolean overwrite, final int bufferSize) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* target */
        //     7: ldc_w           "target"
        //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    13: aload_0         /* $receiver */
        //    14: invokevirtual   java/io/File.exists:()Z
        //    17: ifne            38
        //    20: new             Lkotlin/io/NoSuchFileException;
        //    23: dup            
        //    24: aload_0         /* $receiver */
        //    25: aconst_null    
        //    26: ldc_w           "The source file doesn't exist."
        //    29: iconst_2       
        //    30: aconst_null    
        //    31: invokespecial   kotlin/io/NoSuchFileException.<init>:(Ljava/io/File;Ljava/io/File;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //    34: checkcast       Ljava/lang/Throwable;
        //    37: athrow         
        //    38: aload_1         /* target */
        //    39: invokevirtual   java/io/File.exists:()Z
        //    42: ifeq            88
        //    45: iload_2         /* overwrite */
        //    46: ifne            53
        //    49: iconst_1       
        //    50: goto            65
        //    53: aload_1         /* target */
        //    54: invokevirtual   java/io/File.delete:()Z
        //    57: ifne            64
        //    60: iconst_1       
        //    61: goto            65
        //    64: iconst_0       
        //    65: istore          stillExists
        //    67: iload           stillExists
        //    69: ifeq            88
        //    72: new             Lkotlin/io/FileAlreadyExistsException;
        //    75: dup            
        //    76: aload_0         /* $receiver */
        //    77: aload_1         /* target */
        //    78: ldc_w           "The destination file already exists."
        //    81: invokespecial   kotlin/io/FileAlreadyExistsException.<init>:(Ljava/io/File;Ljava/io/File;Ljava/lang/String;)V
        //    84: checkcast       Ljava/lang/Throwable;
        //    87: athrow         
        //    88: aload_0         /* $receiver */
        //    89: invokevirtual   java/io/File.isDirectory:()Z
        //    92: ifeq            121
        //    95: aload_1         /* target */
        //    96: invokevirtual   java/io/File.mkdirs:()Z
        //    99: ifne            285
        //   102: new             Lkotlin/io/FileSystemException;
        //   105: dup            
        //   106: aload_0         /* $receiver */
        //   107: aload_1         /* target */
        //   108: ldc_w           "Failed to create target directory."
        //   111: invokespecial   kotlin/io/FileSystemException.<init>:(Ljava/io/File;Ljava/io/File;Ljava/lang/String;)V
        //   114: checkcast       Ljava/lang/Throwable;
        //   117: athrow         
        //   118: nop            
        //   119: nop            
        //   120: athrow         
        //   121: aload_1         /* target */
        //   122: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   125: dup            
        //   126: ifnull          136
        //   129: invokevirtual   java/io/File.mkdirs:()Z
        //   132: pop            
        //   133: goto            137
        //   136: pop            
        //   137: aload_0         /* $receiver */
        //   138: astore          4
        //   140: new             Ljava/io/FileInputStream;
        //   143: dup            
        //   144: aload           4
        //   146: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   149: checkcast       Ljava/io/Closeable;
        //   152: astore          4
        //   154: aconst_null    
        //   155: checkcast       Ljava/lang/Throwable;
        //   158: astore          5
        //   160: nop            
        //   161: aload           4
        //   163: checkcast       Ljava/io/FileInputStream;
        //   166: astore          input
        //   168: aload_1         /* target */
        //   169: astore          8
        //   171: new             Ljava/io/FileOutputStream;
        //   174: dup            
        //   175: aload           8
        //   177: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   180: checkcast       Ljava/io/Closeable;
        //   183: astore          8
        //   185: aconst_null    
        //   186: checkcast       Ljava/lang/Throwable;
        //   189: astore          9
        //   191: nop            
        //   192: aload           8
        //   194: checkcast       Ljava/io/FileOutputStream;
        //   197: astore          output
        //   199: aload           input
        //   201: checkcast       Ljava/io/InputStream;
        //   204: aload           output
        //   206: checkcast       Ljava/io/OutputStream;
        //   209: iload_3         /* bufferSize */
        //   210: invokestatic    kotlin/io/ByteStreamsKt.copyTo:(Ljava/io/InputStream;Ljava/io/OutputStream;I)J
        //   213: lstore          null
        //   215: aload           8
        //   217: aload           9
        //   219: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   222: lload           10
        //   224: goto            248
        //   227: astore          10
        //   229: aload           10
        //   231: astore          9
        //   233: aload           10
        //   235: athrow         
        //   236: astore          10
        //   238: aload           8
        //   240: aload           9
        //   242: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   245: aload           10
        //   247: athrow         
        //   248: nop            
        //   249: lstore          null
        //   251: aload           4
        //   253: aload           5
        //   255: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   258: lload           6
        //   260: goto            284
        //   263: astore          6
        //   265: aload           6
        //   267: astore          5
        //   269: aload           6
        //   271: athrow         
        //   272: astore          6
        //   274: aload           4
        //   276: aload           5
        //   278: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   281: aload           6
        //   283: athrow         
        //   284: pop2           
        //   285: aload_1         /* target */
        //   286: areturn        
        //    StackMapTable: 00 10 26 0E 0A 40 01 16 FF 00 1D 00 00 00 01 07 00 78 FF 00 02 00 04 07 00 4D 07 00 4D 01 01 00 00 4E 07 00 4D 00 FF 00 59 00 0A 07 00 4D 07 00 4D 01 01 07 01 28 07 00 78 07 01 23 00 07 01 28 07 00 78 00 01 07 00 78 48 07 00 78 FF 00 0B 00 0B 07 00 4D 07 00 4D 01 01 07 01 28 07 00 78 07 01 23 00 07 01 28 07 00 78 04 00 01 04 FF 00 0E 00 06 07 00 4D 07 00 4D 01 01 07 01 28 07 00 78 00 01 07 00 78 48 07 00 78 FF 00 0B 00 0A 07 00 4D 07 00 4D 01 01 07 01 28 07 00 78 04 07 01 28 07 00 78 04 00 01 04 FF 00 00 00 04 07 00 4D 07 00 4D 01 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  191    215    227    236    Ljava/lang/Throwable;
        //  191    215    236    248    Any
        //  227    236    236    248    Any
        //  236    238    236    248    Any
        //  160    251    263    272    Ljava/lang/Throwable;
        //  160    251    272    284    Any
        //  263    272    272    284    Any
        //  272    274    272    284    Any
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
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:548)
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
    
    @NotNull
    public static /* synthetic */ File copyTo$default(final File $receiver, final File target, boolean overwrite, int bufferSize, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            overwrite = false;
        }
        if ((n & 0x4) != 0x0) {
            bufferSize = 8192;
        }
        return copyTo($receiver, target, overwrite, bufferSize);
    }
    
    public static final boolean copyRecursively(@NotNull final File $receiver, @NotNull final File target, final boolean overwrite, @NotNull final Function2<? super File, ? super IOException, ? extends OnErrorAction> onError) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(target, "target");
        Intrinsics.checkParameterIsNotNull(onError, "onError");
        if (!$receiver.exists()) {
            return (OnErrorAction)onError.invoke($receiver, new NoSuchFileException($receiver, null, "The source file doesn't exist.", 2, null)) != OnErrorAction.TERMINATE;
        }
        try {
            for (final File src : FilesKt__FileTreeWalkKt.walkTopDown($receiver).onFail((Function2<? super File, ? super IOException, Unit>)new FilesKt__UtilsKt$copyRecursively.FilesKt__UtilsKt$copyRecursively$2((Function2)onError))) {
                if (!src.exists()) {
                    if ((OnErrorAction)onError.invoke(src, new NoSuchFileException(src, null, "The source file doesn't exist.", 2, null)) == OnErrorAction.TERMINATE) {
                        return false;
                    }
                    continue;
                }
                else {
                    final String relPath = toRelativeString(src, $receiver);
                    final File dstFile = new File(target, relPath);
                    if (dstFile.exists() && (!src.isDirectory() || !dstFile.isDirectory())) {
                        final boolean stillExists = !overwrite || (dstFile.isDirectory() ? (!deleteRecursively(dstFile)) : (!dstFile.delete()));
                        if (stillExists) {
                            if ((OnErrorAction)onError.invoke(dstFile, new FileAlreadyExistsException(src, dstFile, "The destination file already exists.")) == OnErrorAction.TERMINATE) {
                                return false;
                            }
                            continue;
                        }
                    }
                    if (src.isDirectory()) {
                        dstFile.mkdirs();
                    }
                    else {
                        if (copyTo$default(src, dstFile, overwrite, 0, 4, null).length() != src.length() && (OnErrorAction)onError.invoke(src, new IOException("Source file wasn't copied completely, length of destination file differs.")) == OnErrorAction.TERMINATE) {
                            return false;
                        }
                        continue;
                    }
                }
            }
            return true;
        }
        catch (TerminateException e) {
            return false;
        }
    }
    
    public static final boolean deleteRecursively(@NotNull final File $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokestatic    kotlin/io/FilesKt.walkBottomUp:(Ljava/io/File;)Lkotlin/io/FileTreeWalk;
        //    10: checkcast       Lkotlin/sequences/Sequence;
        //    13: astore_1       
        //    14: iconst_1       
        //    15: istore_2        /* initial$iv */
        //    16: iload_2         /* initial$iv */
        //    17: istore_3        /* accumulator$iv */
        //    18: aload_1         /* $receiver$iv */
        //    19: invokeinterface kotlin/sequences/Sequence.iterator:()Ljava/util/Iterator;
        //    24: astore          4
        //    26: aload           4
        //    28: invokeinterface java/util/Iterator.hasNext:()Z
        //    33: ifeq            85
        //    36: aload           4
        //    38: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    43: astore          element$iv
        //    45: iload_3         /* accumulator$iv */
        //    46: aload           element$iv
        //    48: checkcast       Ljava/io/File;
        //    51: astore          6
        //    53: istore          res
        //    55: aload           it
        //    57: invokevirtual   java/io/File.delete:()Z
        //    60: ifne            71
        //    63: aload           it
        //    65: invokevirtual   java/io/File.exists:()Z
        //    68: ifne            80
        //    71: iload           res
        //    73: ifeq            80
        //    76: iconst_1       
        //    77: goto            81
        //    80: iconst_0       
        //    81: istore_3        /* accumulator$iv */
        //    82: goto            26
        //    85: iload_3         /* accumulator$iv */
        //    86: ireturn        
        //    StackMapTable: 00 05 FF 00 1A 00 05 07 00 4D 07 01 8F 01 01 07 01 67 00 00 FE 00 2C 07 01 92 07 00 4D 01 08 40 01 F8 00 03
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final boolean startsWith(@NotNull final File $receiver, @NotNull final File other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents($receiver);
        final FilePathComponents otherComponents = FilesKt__FilePathComponentsKt.toComponents(other);
        return !(Intrinsics.areEqual(components.getRoot(), otherComponents.getRoot()) ^ true) && components.getSize() >= otherComponents.getSize() && components.getSegments().subList(0, otherComponents.getSize()).equals(otherComponents.getSegments());
    }
    
    public static final boolean startsWith(@NotNull final File $receiver, @NotNull final String other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return startsWith($receiver, new File(other));
    }
    
    public static final boolean endsWith(@NotNull final File $receiver, @NotNull final File other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents($receiver);
        final FilePathComponents otherComponents = FilesKt__FilePathComponentsKt.toComponents(other);
        if (otherComponents.isRooted()) {
            return Intrinsics.areEqual($receiver, other);
        }
        final int shift = components.getSize() - otherComponents.getSize();
        return shift >= 0 && components.getSegments().subList(shift, components.getSize()).equals(otherComponents.getSegments());
    }
    
    public static final boolean endsWith(@NotNull final File $receiver, @NotNull final String other) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return endsWith($receiver, new File(other));
    }
    
    @NotNull
    public static final File normalize(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final FilePathComponents $receiver2 = FilesKt__FilePathComponentsKt.toComponents($receiver);
        final File root = $receiver2.getRoot();
        final List<File> list = normalize$FilesKt__UtilsKt($receiver2.getSegments());
        final String separator = File.separator;
        Intrinsics.checkExpressionValueIsNotNull(separator, "File.separator");
        return resolve(root, CollectionsKt___CollectionsKt.joinToString$default(list, separator, null, null, 0, null, null, 62, null));
    }
    
    private static final FilePathComponents normalize$FilesKt__UtilsKt(@NotNull final FilePathComponents $receiver) {
        return new FilePathComponents($receiver.getRoot(), normalize$FilesKt__UtilsKt($receiver.getSegments()));
    }
    
    private static final List<File> normalize$FilesKt__UtilsKt(@NotNull final List<? extends File> $receiver) {
        final List list = new ArrayList($receiver.size());
        for (final File file : $receiver) {
            final String name = file.getName();
            if (name != null) {
                final String s = name;
                switch (s.hashCode()) {
                    case 1472: {
                        if (!s.equals("..")) {
                            break;
                        }
                        if (!list.isEmpty() && (Intrinsics.areEqual(CollectionsKt___CollectionsKt.last((List<? extends File>)list).getName(), "..") ^ true)) {
                            list.remove(list.size() - 1);
                            continue;
                        }
                        list.add(file);
                        continue;
                    }
                    case 46: {
                        if (s.equals(".")) {
                            continue;
                        }
                        break;
                    }
                }
            }
            list.add(file);
        }
        return (List<File>)list;
    }
    
    @NotNull
    public static final File resolve(@NotNull final File $receiver, @NotNull final File relative) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        if (FilesKt__FilePathComponentsKt.isRooted(relative)) {
            return relative;
        }
        final String string = $receiver.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "this.toString()");
        final String baseName = string;
        return (baseName.length() == 0 || StringsKt__StringsKt.endsWith$default(baseName, File.separatorChar, false, 2, null)) ? new File(baseName + relative) : new File(baseName + File.separatorChar + relative);
    }
    
    @NotNull
    public static final File resolve(@NotNull final File $receiver, @NotNull final String relative) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return resolve($receiver, new File(relative));
    }
    
    @NotNull
    public static final File resolveSibling(@NotNull final File $receiver, @NotNull final File relative) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents($receiver);
        final File parentSubPath = (components.getSize() == 0) ? new File("..") : components.subPath(0, components.getSize() - 1);
        return resolve(resolve(components.getRoot(), parentSubPath), relative);
    }
    
    @NotNull
    public static final File resolveSibling(@NotNull final File $receiver, @NotNull final String relative) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(relative, "relative");
        return resolveSibling($receiver, new File(relative));
    }
    
    public FilesKt__UtilsKt() {
    }
}
