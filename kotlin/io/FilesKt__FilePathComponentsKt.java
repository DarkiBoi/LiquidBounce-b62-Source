// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013" }, d2 = { "isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib" }, xs = "kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt
{
    private static final int getRootLength$FilesKt__FilePathComponentsKt(@NotNull final String $receiver) {
        int first = StringsKt__StringsKt.indexOf$default($receiver, File.separatorChar, 0, false, 4, null);
        if (first == 0) {
            if ($receiver.length() > 1 && $receiver.charAt(1) == File.separatorChar) {
                first = StringsKt__StringsKt.indexOf$default($receiver, File.separatorChar, 2, false, 4, null);
                if (first >= 0) {
                    first = StringsKt__StringsKt.indexOf$default($receiver, File.separatorChar, first + 1, false, 4, null);
                    if (first >= 0) {
                        return first + 1;
                    }
                    return $receiver.length();
                }
            }
            return 1;
        }
        if (first > 0 && $receiver.charAt(first - 1) == ':') {
            return ++first;
        }
        if (first == -1 && StringsKt__StringsKt.endsWith$default($receiver, ':', false, 2, null)) {
            return $receiver.length();
        }
        return 0;
    }
    
    @NotNull
    public static final String getRootName(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final String path = $receiver.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        final String s = path;
        final int beginIndex = 0;
        final String path2 = $receiver.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        final int rootLength$FilesKt__FilePathComponentsKt = getRootLength$FilesKt__FilePathComponentsKt(path2);
        final String s2 = s;
        if (s2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = s2.substring(beginIndex, rootLength$FilesKt__FilePathComponentsKt);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    @NotNull
    public static final File getRoot(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return new File(getRootName($receiver));
    }
    
    public static final boolean isRooted(@NotNull final File $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        final String path = $receiver.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        return getRootLength$FilesKt__FilePathComponentsKt(path) > 0;
    }
    
    @NotNull
    public static final FilePathComponents toComponents(@NotNull final File $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* $receiver */
        //     7: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    10: astore_1        /* path */
        //    11: aload_1         /* path */
        //    12: dup            
        //    13: ldc             "path"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: invokestatic    kotlin/io/FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt:(Ljava/lang/String;)I
        //    21: istore_2        /* rootLength */
        //    22: aload_1         /* path */
        //    23: astore          4
        //    25: iconst_0       
        //    26: istore          5
        //    28: aload           4
        //    30: iload           5
        //    32: iload_2         /* rootLength */
        //    33: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    36: dup            
        //    37: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    39: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    42: astore_3        /* rootName */
        //    43: aload_1         /* path */
        //    44: astore          5
        //    46: aload           5
        //    48: iload_2         /* rootLength */
        //    49: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    52: dup            
        //    53: ldc             "(this as java.lang.String).substring(startIndex)"
        //    55: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    58: astore          subPath
        //    60: aload           subPath
        //    62: checkcast       Ljava/lang/CharSequence;
        //    65: astore          6
        //    67: aload           6
        //    69: invokeinterface java/lang/CharSequence.length:()I
        //    74: ifne            81
        //    77: iconst_1       
        //    78: goto            82
        //    81: iconst_0       
        //    82: ifeq            91
        //    85: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //    88: goto            209
        //    91: aload           subPath
        //    93: checkcast       Ljava/lang/CharSequence;
        //    96: iconst_1       
        //    97: newarray        C
        //    99: dup            
        //   100: iconst_0       
        //   101: getstatic       java/io/File.separatorChar:C
        //   104: castore        
        //   105: iconst_0       
        //   106: iconst_0       
        //   107: bipush          6
        //   109: aconst_null    
        //   110: invokestatic    kotlin/text/StringsKt.split$default:(Ljava/lang/CharSequence;[CZIILjava/lang/Object;)Ljava/util/List;
        //   113: checkcast       Ljava/lang/Iterable;
        //   116: astore          $receiver$iv
        //   118: aload           $receiver$iv
        //   120: astore          7
        //   122: new             Ljava/util/ArrayList;
        //   125: dup            
        //   126: aload           $receiver$iv
        //   128: bipush          10
        //   130: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   133: invokespecial   java/util/ArrayList.<init>:(I)V
        //   136: checkcast       Ljava/util/Collection;
        //   139: astore          destination$iv$iv
        //   141: aload           $receiver$iv$iv
        //   143: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   148: astore          9
        //   150: aload           9
        //   152: invokeinterface java/util/Iterator.hasNext:()Z
        //   157: ifeq            204
        //   160: aload           9
        //   162: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   167: astore          item$iv$iv
        //   169: aload           destination$iv$iv
        //   171: aload           item$iv$iv
        //   173: checkcast       Ljava/lang/String;
        //   176: astore          11
        //   178: astore          15
        //   180: new             Ljava/io/File;
        //   183: dup            
        //   184: aload           p1
        //   186: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   189: astore          16
        //   191: aload           15
        //   193: aload           16
        //   195: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   200: pop            
        //   201: goto            150
        //   204: aload           destination$iv$iv
        //   206: checkcast       Ljava/util/List;
        //   209: astore          list
        //   211: new             Lkotlin/io/FilePathComponents;
        //   214: dup            
        //   215: new             Ljava/io/File;
        //   218: dup            
        //   219: aload_3         /* rootName */
        //   220: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   223: aload           list
        //   225: invokespecial   kotlin/io/FilePathComponents.<init>:(Ljava/io/File;Ljava/util/List;)V
        //   228: areturn        
        //    StackMapTable: 00 06 FF 00 51 00 07 07 00 2C 07 00 38 01 07 00 38 07 00 38 07 00 38 07 00 2A 00 00 40 01 08 FF 00 3A 00 0A 07 00 2C 07 00 38 01 07 00 38 07 00 38 07 00 38 07 00 80 07 00 80 07 00 8B 07 00 91 00 00 35 FF 00 04 00 07 07 00 2C 07 00 38 01 07 00 38 07 00 38 07 00 38 07 00 04 00 01 07 00 9F
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final File subPath(@NotNull final File $receiver, final int beginIndex, final int endIndex) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return toComponents($receiver).subPath(beginIndex, endIndex);
    }
    
    public FilesKt__FilePathComponentsKt() {
    }
}
