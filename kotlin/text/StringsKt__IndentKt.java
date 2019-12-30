// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import java.util.List;
import kotlin.sequences.Sequence;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 5, xi = 1, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015" }, d2 = { "getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib" }, xs = "kotlin/text/StringsKt")
class StringsKt__IndentKt
{
    @NotNull
    public static final String trimMargin(@NotNull final String $receiver, @NotNull final String marginPrefix) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(marginPrefix, "marginPrefix");
        return replaceIndentByMargin($receiver, "", marginPrefix);
    }
    
    @NotNull
    public static final String replaceIndentByMargin(@NotNull final String $receiver, @NotNull final String newIndent, @NotNull final String marginPrefix) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* newIndent */
        //     7: ldc             "newIndent"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2         /* marginPrefix */
        //    13: ldc             "marginPrefix"
        //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    18: aload_2         /* marginPrefix */
        //    19: checkcast       Ljava/lang/CharSequence;
        //    22: astore_3       
        //    23: aload_3        
        //    24: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //    27: ifne            34
        //    30: iconst_1       
        //    31: goto            35
        //    34: iconst_0       
        //    35: istore_3       
        //    36: iload_3        
        //    37: ifne            60
        //    40: ldc             "marginPrefix must be non-blank string."
        //    42: astore          5
        //    44: new             Ljava/lang/IllegalArgumentException;
        //    47: dup            
        //    48: aload           5
        //    50: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    53: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    56: checkcast       Ljava/lang/Throwable;
        //    59: athrow         
        //    60: aload_0         /* $receiver */
        //    61: checkcast       Ljava/lang/CharSequence;
        //    64: invokestatic    kotlin/text/StringsKt.lines:(Ljava/lang/CharSequence;)Ljava/util/List;
        //    67: astore_3        /* lines */
        //    68: aload_3         /* lines */
        //    69: astore          4
        //    71: aload_0         /* $receiver */
        //    72: invokevirtual   java/lang/String.length:()I
        //    75: aload_1         /* newIndent */
        //    76: invokevirtual   java/lang/String.length:()I
        //    79: aload_3         /* lines */
        //    80: invokeinterface java/util/List.size:()I
        //    85: imul           
        //    86: iadd           
        //    87: istore          5
        //    89: aload_1         /* newIndent */
        //    90: invokestatic    kotlin/text/StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt:(Ljava/lang/String;)Lkotlin/jvm/functions/Function1;
        //    93: astore          indentAddFunction$iv
        //    95: aload           $receiver$iv
        //    97: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //   100: istore          lastIndex$iv
        //   102: aload           $receiver$iv
        //   104: checkcast       Ljava/lang/Iterable;
        //   107: astore          $receiver$iv$iv
        //   109: aload           $receiver$iv$iv
        //   111: astore          9
        //   113: new             Ljava/util/ArrayList;
        //   116: dup            
        //   117: invokespecial   java/util/ArrayList.<init>:()V
        //   120: checkcast       Ljava/util/Collection;
        //   123: astore          destination$iv$iv$iv
        //   125: aload           $receiver$iv$iv$iv
        //   127: astore          $receiver$iv$iv$iv$iv
        //   129: iconst_0       
        //   130: istore          index$iv$iv$iv$iv
        //   132: aload           $receiver$iv$iv$iv$iv
        //   134: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   139: astore          13
        //   141: aload           13
        //   143: invokeinterface java/util/Iterator.hasNext:()Z
        //   148: ifeq            421
        //   151: aload           13
        //   153: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   158: astore          item$iv$iv$iv$iv
        //   160: iload           index$iv$iv$iv$iv
        //   162: iinc            index$iv$iv$iv$iv, 1
        //   165: istore          15
        //   167: iload           15
        //   169: ifge            175
        //   172: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   175: iload           15
        //   177: istore          16
        //   179: iload           16
        //   181: aload           item$iv$iv$iv$iv
        //   183: astore          17
        //   185: istore          index$iv$iv$iv
        //   187: iload           index$iv$iv$iv
        //   189: aload           element$iv$iv$iv
        //   191: checkcast       Ljava/lang/String;
        //   194: astore          19
        //   196: istore          index$iv
        //   198: iload           index$iv
        //   200: ifeq            210
        //   203: iload           index$iv
        //   205: iload           lastIndex$iv
        //   207: if_icmpne       225
        //   210: aload           value$iv
        //   212: checkcast       Ljava/lang/CharSequence;
        //   215: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   218: ifeq            225
        //   221: aconst_null    
        //   222: goto            393
        //   225: aload           value$iv
        //   227: astore          line
        //   229: aload           line
        //   231: checkcast       Ljava/lang/CharSequence;
        //   234: astore          $receiver$iv
        //   236: iconst_0       
        //   237: istore          23
        //   239: aload           $receiver$iv
        //   241: invokeinterface java/lang/CharSequence.length:()I
        //   246: istore          24
        //   248: iload           23
        //   250: iload           24
        //   252: if_icmpge       293
        //   255: aload           $receiver$iv
        //   257: iload           index$iv
        //   259: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   264: istore          it
        //   266: iload           it
        //   268: invokestatic    kotlin/text/CharsKt.isWhitespace:(C)Z
        //   271: ifne            278
        //   274: iconst_1       
        //   275: goto            279
        //   278: iconst_0       
        //   279: ifeq            287
        //   282: iload           index$iv
        //   284: goto            294
        //   287: iinc            index$iv, 1
        //   290: goto            248
        //   293: iconst_m1      
        //   294: istore          firstNonWhitespaceIndex
        //   296: iload           firstNonWhitespaceIndex
        //   298: iconst_m1      
        //   299: if_icmpne       306
        //   302: aconst_null    
        //   303: goto            364
        //   306: aload           line
        //   308: aload_2         /* marginPrefix */
        //   309: iload           firstNonWhitespaceIndex
        //   311: iconst_0       
        //   312: iconst_4       
        //   313: aconst_null    
        //   314: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;IZILjava/lang/Object;)Z
        //   317: ifeq            363
        //   320: aload           line
        //   322: astore          22
        //   324: iload           firstNonWhitespaceIndex
        //   326: aload_2         /* marginPrefix */
        //   327: invokevirtual   java/lang/String.length:()I
        //   330: iadd           
        //   331: istore          23
        //   333: aload           22
        //   335: dup            
        //   336: ifnonnull       349
        //   339: new             Lkotlin/TypeCastException;
        //   342: dup            
        //   343: ldc             "null cannot be cast to non-null type java.lang.String"
        //   345: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   348: athrow         
        //   349: iload           23
        //   351: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   354: dup            
        //   355: ldc             "(this as java.lang.String).substring(startIndex)"
        //   357: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   360: goto            364
        //   363: aconst_null    
        //   364: nop            
        //   365: dup            
        //   366: ifnull          390
        //   369: astore          30
        //   371: aload           indentAddFunction$iv
        //   373: aload           30
        //   375: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   380: checkcast       Ljava/lang/String;
        //   383: dup            
        //   384: ifnull          390
        //   387: goto            393
        //   390: pop            
        //   391: aload           value$iv
        //   393: dup            
        //   394: ifnull          416
        //   397: astore          31
        //   399: aload           31
        //   401: astore          it$iv$iv$iv
        //   403: aload           destination$iv$iv$iv
        //   405: aload           it$iv$iv$iv
        //   407: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   412: pop            
        //   413: goto            417
        //   416: pop            
        //   417: nop            
        //   418: goto            141
        //   421: nop            
        //   422: aload           destination$iv$iv$iv
        //   424: checkcast       Ljava/util/List;
        //   427: checkcast       Ljava/lang/Iterable;
        //   430: new             Ljava/lang/StringBuilder;
        //   433: dup            
        //   434: iload           resultSizeEstimate$iv
        //   436: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   439: checkcast       Ljava/lang/Appendable;
        //   442: ldc             "\n"
        //   444: checkcast       Ljava/lang/CharSequence;
        //   447: aconst_null    
        //   448: aconst_null    
        //   449: iconst_0       
        //   450: aconst_null    
        //   451: aconst_null    
        //   452: bipush          124
        //   454: aconst_null    
        //   455: invokestatic    kotlin/collections/CollectionsKt.joinTo$default:(Ljava/lang/Iterable;Ljava/lang/Appendable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Appendable;
        //   458: checkcast       Ljava/lang/StringBuilder;
        //   461: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   464: dup            
        //   465: ldc             "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()"
        //   467: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   470: areturn        
        //    StackMapTable: 00 16 FC 00 22 07 00 49 40 01 FF 00 18 00 04 07 00 5D 07 00 5D 07 00 5D 01 00 00 FF 00 50 00 0E 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00 FD 00 21 07 00 04 01 FF 00 22 00 15 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 00 00 0E FF 00 16 00 19 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 07 00 5D 07 00 49 01 01 00 00 FC 00 1D 01 40 01 07 FA 00 05 40 01 FD 00 0B 00 01 FF 00 2A 00 1B 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 07 00 5D 07 00 5D 01 01 00 01 00 01 07 00 5D FF 00 0D 00 1B 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 07 00 5D 07 00 49 01 01 00 01 00 00 FF 00 00 00 1B 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 07 00 5D 07 00 04 01 01 00 01 00 01 07 00 5D 59 07 00 5D FF 00 02 00 15 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 00 01 07 00 5D 56 07 00 5D 00 FF 00 03 00 0E 07 00 5D 07 00 5D 07 00 5D 07 00 67 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String trimIndent(@NotNull final String $receiver) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        return replaceIndent($receiver, "");
    }
    
    @NotNull
    public static final String replaceIndent(@NotNull final String $receiver, @NotNull final String newIndent) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "receiver$0"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* newIndent */
        //     7: ldc             "newIndent"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $receiver */
        //    13: checkcast       Ljava/lang/CharSequence;
        //    16: invokestatic    kotlin/text/StringsKt.lines:(Ljava/lang/CharSequence;)Ljava/util/List;
        //    19: astore_2        /* lines */
        //    20: aload_2         /* lines */
        //    21: checkcast       Ljava/lang/Iterable;
        //    24: astore          4
        //    26: nop            
        //    27: aload           $receiver$iv
        //    29: astore          5
        //    31: new             Ljava/util/ArrayList;
        //    34: dup            
        //    35: invokespecial   java/util/ArrayList.<init>:()V
        //    38: checkcast       Ljava/util/Collection;
        //    41: astore          destination$iv$iv
        //    43: aload           $receiver$iv$iv
        //    45: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    50: astore          7
        //    52: aload           7
        //    54: invokeinterface java/util/Iterator.hasNext:()Z
        //    59: ifeq            114
        //    62: aload           7
        //    64: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    69: astore          element$iv$iv
        //    71: aload           element$iv$iv
        //    73: checkcast       Ljava/lang/String;
        //    76: astore          p1
        //    78: aload           p1
        //    80: checkcast       Ljava/lang/CharSequence;
        //    83: astore          10
        //    85: aload           10
        //    87: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //    90: ifne            97
        //    93: iconst_1       
        //    94: goto            98
        //    97: iconst_0       
        //    98: ifeq            52
        //   101: aload           destination$iv$iv
        //   103: aload           element$iv$iv
        //   105: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   110: pop            
        //   111: goto            52
        //   114: aload           destination$iv$iv
        //   116: checkcast       Ljava/util/List;
        //   119: checkcast       Ljava/lang/Iterable;
        //   122: astore          4
        //   124: nop            
        //   125: aload           $receiver$iv
        //   127: astore          5
        //   129: new             Ljava/util/ArrayList;
        //   132: dup            
        //   133: aload           $receiver$iv
        //   135: bipush          10
        //   137: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   140: invokespecial   java/util/ArrayList.<init>:(I)V
        //   143: checkcast       Ljava/util/Collection;
        //   146: astore          destination$iv$iv
        //   148: aload           $receiver$iv$iv
        //   150: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   155: astore          7
        //   157: aload           7
        //   159: invokeinterface java/util/Iterator.hasNext:()Z
        //   164: ifeq            210
        //   167: aload           7
        //   169: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   174: astore          item$iv$iv
        //   176: aload           destination$iv$iv
        //   178: aload           item$iv$iv
        //   180: checkcast       Ljava/lang/String;
        //   183: astore          9
        //   185: astore          33
        //   187: aload           p1
        //   189: invokestatic    kotlin/text/StringsKt__IndentKt.indentWidth$StringsKt__IndentKt:(Ljava/lang/String;)I
        //   192: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   195: astore          34
        //   197: aload           33
        //   199: aload           34
        //   201: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   206: pop            
        //   207: goto            157
        //   210: aload           destination$iv$iv
        //   212: checkcast       Ljava/util/List;
        //   215: checkcast       Ljava/lang/Iterable;
        //   218: invokestatic    kotlin/collections/CollectionsKt.min:(Ljava/lang/Iterable;)Ljava/lang/Comparable;
        //   221: checkcast       Ljava/lang/Integer;
        //   224: dup            
        //   225: ifnull          234
        //   228: invokevirtual   java/lang/Integer.intValue:()I
        //   231: goto            236
        //   234: pop            
        //   235: iconst_0       
        //   236: istore_3        /* minCommonIndent */
        //   237: aload_2         /* lines */
        //   238: astore          4
        //   240: aload_0         /* $receiver */
        //   241: invokevirtual   java/lang/String.length:()I
        //   244: aload_1         /* newIndent */
        //   245: invokevirtual   java/lang/String.length:()I
        //   248: aload_2         /* lines */
        //   249: invokeinterface java/util/List.size:()I
        //   254: imul           
        //   255: iadd           
        //   256: istore          5
        //   258: aload_1         /* newIndent */
        //   259: invokestatic    kotlin/text/StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt:(Ljava/lang/String;)Lkotlin/jvm/functions/Function1;
        //   262: astore          indentAddFunction$iv
        //   264: aload           $receiver$iv
        //   266: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //   269: istore          lastIndex$iv
        //   271: aload           $receiver$iv
        //   273: checkcast       Ljava/lang/Iterable;
        //   276: astore          $receiver$iv$iv
        //   278: aload           $receiver$iv$iv
        //   280: astore          9
        //   282: new             Ljava/util/ArrayList;
        //   285: dup            
        //   286: invokespecial   java/util/ArrayList.<init>:()V
        //   289: checkcast       Ljava/util/Collection;
        //   292: astore          destination$iv$iv$iv
        //   294: aload           $receiver$iv$iv$iv
        //   296: astore          $receiver$iv$iv$iv$iv
        //   298: iconst_0       
        //   299: istore          index$iv$iv$iv$iv
        //   301: aload           $receiver$iv$iv$iv$iv
        //   303: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   308: astore          13
        //   310: aload           13
        //   312: invokeinterface java/util/Iterator.hasNext:()Z
        //   317: ifeq            460
        //   320: aload           13
        //   322: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   327: astore          item$iv$iv$iv$iv
        //   329: iload           index$iv$iv$iv$iv
        //   331: iinc            index$iv$iv$iv$iv, 1
        //   334: istore          15
        //   336: iload           15
        //   338: ifge            344
        //   341: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   344: iload           15
        //   346: istore          16
        //   348: iload           16
        //   350: aload           item$iv$iv$iv$iv
        //   352: astore          17
        //   354: istore          index$iv$iv$iv
        //   356: iload           index$iv$iv$iv
        //   358: aload           element$iv$iv$iv
        //   360: checkcast       Ljava/lang/String;
        //   363: astore          19
        //   365: istore          index$iv
        //   367: iload           index$iv
        //   369: ifeq            379
        //   372: iload           index$iv
        //   374: iload           lastIndex$iv
        //   376: if_icmpne       394
        //   379: aload           value$iv
        //   381: checkcast       Ljava/lang/CharSequence;
        //   384: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   387: ifeq            394
        //   390: aconst_null    
        //   391: goto            432
        //   394: aload           value$iv
        //   396: astore          line
        //   398: aload           line
        //   400: iload_3         /* minCommonIndent */
        //   401: invokestatic    kotlin/text/StringsKt.drop:(Ljava/lang/String;I)Ljava/lang/String;
        //   404: dup            
        //   405: ifnull          429
        //   408: astore          23
        //   410: aload           indentAddFunction$iv
        //   412: aload           23
        //   414: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   419: checkcast       Ljava/lang/String;
        //   422: dup            
        //   423: ifnull          429
        //   426: goto            432
        //   429: pop            
        //   430: aload           value$iv
        //   432: dup            
        //   433: ifnull          455
        //   436: astore          24
        //   438: aload           24
        //   440: astore          it$iv$iv$iv
        //   442: aload           destination$iv$iv$iv
        //   444: aload           it$iv$iv$iv
        //   446: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   451: pop            
        //   452: goto            456
        //   455: pop            
        //   456: nop            
        //   457: goto            310
        //   460: nop            
        //   461: aload           destination$iv$iv$iv
        //   463: checkcast       Ljava/util/List;
        //   466: checkcast       Ljava/lang/Iterable;
        //   469: new             Ljava/lang/StringBuilder;
        //   472: dup            
        //   473: iload           resultSizeEstimate$iv
        //   475: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   478: checkcast       Ljava/lang/Appendable;
        //   481: ldc             "\n"
        //   483: checkcast       Ljava/lang/CharSequence;
        //   486: aconst_null    
        //   487: aconst_null    
        //   488: iconst_0       
        //   489: aconst_null    
        //   490: aconst_null    
        //   491: bipush          124
        //   493: aconst_null    
        //   494: invokestatic    kotlin/collections/CollectionsKt.joinTo$default:(Ljava/lang/Iterable;Ljava/lang/Appendable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Appendable;
        //   497: checkcast       Ljava/lang/StringBuilder;
        //   500: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   503: dup            
        //   504: ldc             "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()"
        //   506: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   509: areturn        
        //    StackMapTable: 00 11 FF 00 34 00 08 07 00 5D 07 00 5D 07 00 67 00 07 00 75 07 00 75 07 00 7C 07 00 84 00 00 FE 00 2C 07 00 04 07 00 5D 07 00 49 40 01 F8 00 0F 2A 34 57 07 00 F8 41 01 FF 00 49 00 0E 07 00 5D 07 00 5D 07 00 67 01 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00 FD 00 21 07 00 04 01 FF 00 22 00 15 07 00 5D 07 00 5D 07 00 67 01 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 00 00 0E FF 00 22 00 16 07 00 5D 07 00 5D 07 00 67 01 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 07 00 5D 00 01 07 00 5D FF 00 02 00 15 07 00 5D 07 00 5D 07 00 67 01 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 00 01 07 00 5D 56 07 00 5D 00 FF 00 03 00 0E 07 00 5D 07 00 5D 07 00 67 01 07 00 67 01 07 00 82 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String prependIndent(@NotNull final String $receiver, @NotNull final String indent) {
        Intrinsics.checkParameterIsNotNull($receiver, "receiver$0");
        Intrinsics.checkParameterIsNotNull(indent, "indent");
        return SequencesKt___SequencesKt.joinToString$default(SequencesKt___SequencesKt.map((Sequence<?>)StringsKt__StringsKt.lineSequence($receiver), (Function1<? super Object, ?>)new StringsKt__IndentKt$prependIndent.StringsKt__IndentKt$prependIndent$1(indent)), "\n", null, null, 0, null, null, 62, null);
    }
    
    private static final int indentWidth$StringsKt__IndentKt(@NotNull final String $receiver) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: checkcast       Ljava/lang/CharSequence;
        //     4: astore_1        /* $receiver$iv */
        //     5: iconst_0       
        //     6: istore_2       
        //     7: aload_1         /* $receiver$iv */
        //     8: invokeinterface java/lang/CharSequence.length:()I
        //    13: istore_3       
        //    14: iload_2        
        //    15: iload_3        
        //    16: if_icmpge       54
        //    19: aload_1         /* $receiver$iv */
        //    20: iload_2         /* index$iv */
        //    21: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    26: istore          it
        //    28: iload           it
        //    30: invokestatic    kotlin/text/CharsKt.isWhitespace:(C)Z
        //    33: ifne            40
        //    36: iconst_1       
        //    37: goto            41
        //    40: iconst_0       
        //    41: ifeq            48
        //    44: iload_2         /* index$iv */
        //    45: goto            55
        //    48: iinc            index$iv, 1
        //    51: goto            14
        //    54: iconst_m1      
        //    55: istore_1        /* $receiver$iv */
        //    56: iload_1        
        //    57: istore_2        /* it */
        //    58: iload_2         /* it */
        //    59: iconst_m1      
        //    60: if_icmpne       70
        //    63: aload_0         /* $receiver */
        //    64: invokevirtual   java/lang/String.length:()I
        //    67: goto            71
        //    70: iload_2         /* it */
        //    71: ireturn        
        //    StackMapTable: 00 08 FE 00 0E 07 00 49 01 01 FC 00 19 01 40 01 06 FA 00 05 40 01 FF 00 0E 00 04 07 00 5D 01 01 01 00 00 40 01
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(final String indent) {
        return (Function1<String, String>)((indent.length() == 0) ? StringsKt__IndentKt$getIndentFunction.StringsKt__IndentKt$getIndentFunction$1.INSTANCE : ((Function1<String, String>)new StringsKt__IndentKt$getIndentFunction.StringsKt__IndentKt$getIndentFunction$2(indent)));
    }
    
    private static final String reindent$StringsKt__IndentKt(@NotNull final List<String> $receiver, final int resultSizeEstimate, final Function1<? super String, String> indentAddFunction, final Function1<? super String, String> indentCutFunction) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    kotlin/collections/CollectionsKt.getLastIndex:(Ljava/util/List;)I
        //     4: istore          lastIndex
        //     6: aload_0         /* $receiver */
        //     7: checkcast       Ljava/lang/Iterable;
        //    10: astore          $receiver$iv
        //    12: aload           $receiver$iv
        //    14: astore          7
        //    16: new             Ljava/util/ArrayList;
        //    19: dup            
        //    20: invokespecial   java/util/ArrayList.<init>:()V
        //    23: checkcast       Ljava/util/Collection;
        //    26: astore          destination$iv$iv
        //    28: aload           $receiver$iv$iv
        //    30: astore          $receiver$iv$iv$iv
        //    32: iconst_0       
        //    33: istore          index$iv$iv$iv
        //    35: aload           $receiver$iv$iv$iv
        //    37: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    42: astore          11
        //    44: aload           11
        //    46: invokeinterface java/util/Iterator.hasNext:()Z
        //    51: ifeq            220
        //    54: aload           11
        //    56: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    61: astore          item$iv$iv$iv
        //    63: iload           index$iv$iv$iv
        //    65: iinc            index$iv$iv$iv, 1
        //    68: istore          13
        //    70: iload           13
        //    72: ifge            104
        //    75: iconst_1       
        //    76: iconst_3       
        //    77: iconst_0       
        //    78: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //    81: ifeq            90
        //    84: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //    87: goto            104
        //    90: new             Ljava/lang/ArithmeticException;
        //    93: dup            
        //    94: ldc_w           "Index overflow has happened."
        //    97: invokespecial   java/lang/ArithmeticException.<init>:(Ljava/lang/String;)V
        //   100: checkcast       Ljava/lang/Throwable;
        //   103: athrow         
        //   104: iload           13
        //   106: istore          14
        //   108: iload           14
        //   110: aload           item$iv$iv$iv
        //   112: astore          15
        //   114: istore          index$iv$iv
        //   116: iload           index$iv$iv
        //   118: aload           element$iv$iv
        //   120: checkcast       Ljava/lang/String;
        //   123: astore          17
        //   125: istore          index
        //   127: iload           index
        //   129: ifeq            139
        //   132: iload           index
        //   134: iload           lastIndex
        //   136: if_icmpne       154
        //   139: aload           value
        //   141: checkcast       Ljava/lang/CharSequence;
        //   144: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   147: ifeq            154
        //   150: aconst_null    
        //   151: goto            192
        //   154: aload_3         /* indentCutFunction */
        //   155: aload           value
        //   157: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   162: checkcast       Ljava/lang/String;
        //   165: dup            
        //   166: ifnull          189
        //   169: astore          19
        //   171: aload_2         /* indentAddFunction */
        //   172: aload           19
        //   174: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   179: checkcast       Ljava/lang/String;
        //   182: dup            
        //   183: ifnull          189
        //   186: goto            192
        //   189: pop            
        //   190: aload           value
        //   192: dup            
        //   193: ifnull          215
        //   196: astore          21
        //   198: aload           21
        //   200: astore          it$iv$iv
        //   202: aload           destination$iv$iv
        //   204: aload           it$iv$iv
        //   206: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   211: pop            
        //   212: goto            216
        //   215: pop            
        //   216: nop            
        //   217: goto            44
        //   220: nop            
        //   221: aload           destination$iv$iv
        //   223: checkcast       Ljava/util/List;
        //   226: checkcast       Ljava/lang/Iterable;
        //   229: new             Ljava/lang/StringBuilder;
        //   232: dup            
        //   233: iload_1         /* resultSizeEstimate */
        //   234: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   237: checkcast       Ljava/lang/Appendable;
        //   240: ldc             "\n"
        //   242: checkcast       Ljava/lang/CharSequence;
        //   245: aconst_null    
        //   246: aconst_null    
        //   247: iconst_0       
        //   248: aconst_null    
        //   249: aconst_null    
        //   250: bipush          124
        //   252: aconst_null    
        //   253: invokestatic    kotlin/collections/CollectionsKt.joinTo$default:(Ljava/lang/Iterable;Ljava/lang/Appendable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Appendable;
        //   256: checkcast       Ljava/lang/StringBuilder;
        //   259: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   262: dup            
        //   263: ldc             "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()"
        //   265: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   268: areturn        
        //    Signature:
        //  (Ljava/util/List<Ljava/lang/String;>;ILkotlin/jvm/functions/Function1<-Ljava/lang/String;Ljava/lang/String;>;Lkotlin/jvm/functions/Function1<-Ljava/lang/String;Ljava/lang/String;>;)Ljava/lang/String;
        //    StackMapTable: 00 0A FF 00 2C 00 0C 07 00 67 01 07 00 82 07 00 82 00 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00 FD 00 2D 07 00 04 01 0D FF 00 22 00 13 07 00 67 01 07 00 82 07 00 82 00 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 07 00 04 01 01 07 00 04 01 07 00 5D 01 00 00 0E 62 07 00 5D 42 07 00 5D 56 07 00 5D 00 FF 00 03 00 0C 07 00 67 01 07 00 82 07 00 82 00 01 07 00 75 07 00 75 07 00 7C 07 00 75 01 07 00 84 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public StringsKt__IndentKt() {
    }
}
