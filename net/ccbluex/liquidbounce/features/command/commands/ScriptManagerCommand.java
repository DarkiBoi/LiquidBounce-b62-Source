// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.command.Command;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00070\u0006H\u0016¢\u0006\u0002\u0010\b¨\u0006\t" }, d2 = { "Lnet/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce" })
public final class ScriptManagerCommand extends Command
{
    @Override
    public void execute(@NotNull final String[] args) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "args"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* args */
        //     7: arraylength    
        //     8: iconst_1       
        //     9: if_icmple       896
        //    12: aload_1         /* args */
        //    13: iconst_1       
        //    14: aaload         
        //    15: ldc             "import"
        //    17: iconst_1       
        //    18: invokestatic    kotlin/text/StringsKt.equals:(Ljava/lang/String;Ljava/lang/String;Z)Z
        //    21: ifeq            514
        //    24: nop            
        //    25: new             Ljavax/swing/JFileChooser;
        //    28: dup            
        //    29: invokespecial   javax/swing/JFileChooser.<init>:()V
        //    32: astore_2        /* fileChooser */
        //    33: aload_2         /* fileChooser */
        //    34: iconst_0       
        //    35: invokevirtual   javax/swing/JFileChooser.setFileSelectionMode:(I)V
        //    38: aload_2         /* fileChooser */
        //    39: aconst_null    
        //    40: invokevirtual   javax/swing/JFileChooser.showOpenDialog:(Ljava/awt/Component;)I
        //    43: ifne            895
        //    46: aload_2         /* fileChooser */
        //    47: invokevirtual   javax/swing/JFileChooser.getSelectedFile:()Ljava/io/File;
        //    50: astore_3        /* file */
        //    51: aload_3         /* file */
        //    52: dup            
        //    53: ldc             "file"
        //    55: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    58: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //    61: astore          fileName
        //    63: aload           fileName
        //    65: dup            
        //    66: ldc             "fileName"
        //    68: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    71: ldc             ".js"
        //    73: iconst_0       
        //    74: iconst_2       
        //    75: aconst_null    
        //    76: invokestatic    kotlin/text/StringsKt.endsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //    79: ifeq            139
        //    82: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //    85: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //    88: aload_3         /* file */
        //    89: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.importScript:(Ljava/io/File;)V
        //    92: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //    95: getfield        net/ccbluex/liquidbounce/LiquidBounce.moduleManager:Lnet/ccbluex/liquidbounce/features/module/ModuleManager;
        //    98: invokevirtual   net/ccbluex/liquidbounce/features/module/ModuleManager.sortModules:()V
        //   101: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   104: new             Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   107: dup            
        //   108: invokespecial   net/ccbluex/liquidbounce/ui/clickgui/ClickGui.<init>:()V
        //   111: putfield        net/ccbluex/liquidbounce/LiquidBounce.clickGui:Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   114: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   117: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   120: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   123: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   126: getfield        net/ccbluex/liquidbounce/file/FileManager.clickGuiConfig:Lnet/ccbluex/liquidbounce/file/FileConfig;
        //   129: invokevirtual   net/ccbluex/liquidbounce/file/FileManager.loadConfig:(Lnet/ccbluex/liquidbounce/file/FileConfig;)V
        //   132: aload_0         /* this */
        //   133: ldc             "Successfully imported script."
        //   135: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   138: return         
        //   139: aload           fileName
        //   141: ldc             ".zip"
        //   143: iconst_0       
        //   144: iconst_2       
        //   145: aconst_null    
        //   146: invokestatic    kotlin/text/StringsKt.endsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //   149: ifeq            453
        //   152: new             Ljava/util/zip/ZipFile;
        //   155: dup            
        //   156: aload_3         /* file */
        //   157: invokespecial   java/util/zip/ZipFile.<init>:(Ljava/io/File;)V
        //   160: astore          zipFile
        //   162: aload           zipFile
        //   164: invokevirtual   java/util/zip/ZipFile.entries:()Ljava/util/Enumeration;
        //   167: astore          entries
        //   169: new             Ljava/util/ArrayList;
        //   172: dup            
        //   173: invokespecial   java/util/ArrayList.<init>:()V
        //   176: astore          scriptFiles
        //   178: aload           entries
        //   180: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //   185: ifeq            327
        //   188: aload           entries
        //   190: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //   195: checkcast       Ljava/util/zip/ZipEntry;
        //   198: astore          entry
        //   200: aload           entry
        //   202: dup            
        //   203: ldc             "entry"
        //   205: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   208: invokevirtual   java/util/zip/ZipEntry.getName:()Ljava/lang/String;
        //   211: astore          entryName
        //   213: new             Ljava/io/File;
        //   216: dup            
        //   217: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   220: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   223: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.getScriptsFolder:()Ljava/io/File;
        //   226: aload           entryName
        //   228: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   231: astore          entryFile
        //   233: aload           entry
        //   235: invokevirtual   java/util/zip/ZipEntry.isDirectory:()Z
        //   238: ifeq            250
        //   241: aload           entryFile
        //   243: invokevirtual   java/io/File.mkdir:()Z
        //   246: pop            
        //   247: goto            178
        //   250: aload           zipFile
        //   252: aload           entry
        //   254: invokevirtual   java/util/zip/ZipFile.getInputStream:(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
        //   257: astore          fileStream
        //   259: new             Ljava/io/FileOutputStream;
        //   262: dup            
        //   263: aload           entryFile
        //   265: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   268: astore          fileOutputStream
        //   270: aload           fileStream
        //   272: aload           fileOutputStream
        //   274: checkcast       Ljava/io/OutputStream;
        //   277: invokestatic    org/apache/commons/io/IOUtils.copy:(Ljava/io/InputStream;Ljava/io/OutputStream;)I
        //   280: pop            
        //   281: aload           fileOutputStream
        //   283: invokevirtual   java/io/FileOutputStream.close:()V
        //   286: aload           fileStream
        //   288: invokevirtual   java/io/InputStream.close:()V
        //   291: aload           entryName
        //   293: dup            
        //   294: ldc             "entryName"
        //   296: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   299: checkcast       Ljava/lang/CharSequence;
        //   302: ldc             "/"
        //   304: checkcast       Ljava/lang/CharSequence;
        //   307: iconst_0       
        //   308: iconst_2       
        //   309: aconst_null    
        //   310: invokestatic    kotlin/text/StringsKt.contains$default:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZILjava/lang/Object;)Z
        //   313: ifne            324
        //   316: aload           scriptFiles
        //   318: aload           entryFile
        //   320: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   323: pop            
        //   324: goto            178
        //   327: aload           scriptFiles
        //   329: checkcast       Ljava/lang/Iterable;
        //   332: astore          $receiver$iv
        //   334: aload           $receiver$iv
        //   336: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   341: astore          9
        //   343: aload           9
        //   345: invokeinterface java/util/Iterator.hasNext:()Z
        //   350: ifeq            387
        //   353: aload           9
        //   355: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   360: astore          element$iv
        //   362: aload           element$iv
        //   364: checkcast       Ljava/io/File;
        //   367: astore          scriptFile
        //   369: iconst_0       
        //   370: istore          $i$a$-forEach-ScriptManagerCommand$execute$1
        //   372: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   375: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   378: aload           scriptFile
        //   380: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.loadScript:(Ljava/io/File;)V
        //   383: nop            
        //   384: goto            343
        //   387: nop            
        //   388: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   391: getfield        net/ccbluex/liquidbounce/LiquidBounce.moduleManager:Lnet/ccbluex/liquidbounce/features/module/ModuleManager;
        //   394: invokevirtual   net/ccbluex/liquidbounce/features/module/ModuleManager.sortModules:()V
        //   397: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   400: new             Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   403: dup            
        //   404: invokespecial   net/ccbluex/liquidbounce/ui/clickgui/ClickGui.<init>:()V
        //   407: putfield        net/ccbluex/liquidbounce/LiquidBounce.clickGui:Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   410: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   413: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   416: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   419: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   422: getfield        net/ccbluex/liquidbounce/file/FileManager.clickGuiConfig:Lnet/ccbluex/liquidbounce/file/FileConfig;
        //   425: invokevirtual   net/ccbluex/liquidbounce/file/FileManager.loadConfig:(Lnet/ccbluex/liquidbounce/file/FileConfig;)V
        //   428: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   431: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   434: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   437: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   440: getfield        net/ccbluex/liquidbounce/file/FileManager.hudConfig:Lnet/ccbluex/liquidbounce/file/FileConfig;
        //   443: invokevirtual   net/ccbluex/liquidbounce/file/FileManager.loadConfig:(Lnet/ccbluex/liquidbounce/file/FileConfig;)V
        //   446: aload_0         /* this */
        //   447: ldc             "Successfully imported script."
        //   449: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   452: return         
        //   453: aload_0         /* this */
        //   454: ldc             "The file extension has to be .js or .zip"
        //   456: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   459: goto            895
        //   462: astore_2        /* t */
        //   463: invokestatic    net/ccbluex/liquidbounce/utils/ClientUtils.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   466: ldc             "Something went wrong while importing a script."
        //   468: aload_2         /* t */
        //   469: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   474: aload_0         /* this */
        //   475: new             Ljava/lang/StringBuilder;
        //   478: dup            
        //   479: invokespecial   java/lang/StringBuilder.<init>:()V
        //   482: aload_2         /* t */
        //   483: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   486: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   489: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   492: ldc_w           ": "
        //   495: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   498: aload_2         /* t */
        //   499: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   502: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   505: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   508: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   511: goto            895
        //   514: aload_1         /* args */
        //   515: iconst_1       
        //   516: aaload         
        //   517: ldc_w           "delete"
        //   520: iconst_1       
        //   521: invokestatic    kotlin/text/StringsKt.equals:(Ljava/lang/String;Ljava/lang/String;Z)Z
        //   524: ifeq            720
        //   527: nop            
        //   528: aload_1         /* args */
        //   529: arraylength    
        //   530: iconst_2       
        //   531: if_icmpgt       542
        //   534: aload_0         /* this */
        //   535: ldc_w           ".scriptmanager delete <index>"
        //   538: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chatSyntax:(Ljava/lang/String;)V
        //   541: return         
        //   542: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   545: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   548: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.getScripts:()Ljava/util/ArrayList;
        //   551: aload_1         /* args */
        //   552: iconst_2       
        //   553: aaload         
        //   554: astore_3       
        //   555: astore          14
        //   557: aload_3        
        //   558: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   561: istore          15
        //   563: aload           14
        //   565: iload           15
        //   567: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   570: dup            
        //   571: ldc_w           "LiquidBounce.CLIENT.scri\u2026.scripts[args[2].toInt()]"
        //   574: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   577: checkcast       Lnet/ccbluex/liquidbounce/script/Script;
        //   580: astore_2        /* script */
        //   581: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   584: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   587: aload_2         /* script */
        //   588: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.deleteScript:(Lnet/ccbluex/liquidbounce/script/Script;)V
        //   591: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   594: getfield        net/ccbluex/liquidbounce/LiquidBounce.moduleManager:Lnet/ccbluex/liquidbounce/features/module/ModuleManager;
        //   597: invokevirtual   net/ccbluex/liquidbounce/features/module/ModuleManager.sortModules:()V
        //   600: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   603: new             Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   606: dup            
        //   607: invokespecial   net/ccbluex/liquidbounce/ui/clickgui/ClickGui.<init>:()V
        //   610: putfield        net/ccbluex/liquidbounce/LiquidBounce.clickGui:Lnet/ccbluex/liquidbounce/ui/clickgui/ClickGui;
        //   613: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   616: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   619: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   622: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   625: getfield        net/ccbluex/liquidbounce/file/FileManager.clickGuiConfig:Lnet/ccbluex/liquidbounce/file/FileConfig;
        //   628: invokevirtual   net/ccbluex/liquidbounce/file/FileManager.loadConfig:(Lnet/ccbluex/liquidbounce/file/FileConfig;)V
        //   631: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   634: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   637: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   640: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   643: getfield        net/ccbluex/liquidbounce/file/FileManager.hudConfig:Lnet/ccbluex/liquidbounce/file/FileConfig;
        //   646: invokevirtual   net/ccbluex/liquidbounce/file/FileManager.loadConfig:(Lnet/ccbluex/liquidbounce/file/FileConfig;)V
        //   649: aload_0         /* this */
        //   650: ldc_w           "Successfully deleted script."
        //   653: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   656: goto            895
        //   659: astore_2        /* numberFormat */
        //   660: aload_0         /* this */
        //   661: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chatSyntaxError:()V
        //   664: goto            895
        //   667: astore_2        /* t */
        //   668: invokestatic    net/ccbluex/liquidbounce/utils/ClientUtils.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   671: ldc_w           "Something went wrong while deleting a script."
        //   674: aload_2         /* t */
        //   675: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   680: aload_0         /* this */
        //   681: new             Ljava/lang/StringBuilder;
        //   684: dup            
        //   685: invokespecial   java/lang/StringBuilder.<init>:()V
        //   688: aload_2         /* t */
        //   689: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   692: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   695: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   698: ldc_w           ": "
        //   701: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   704: aload_2         /* t */
        //   705: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   708: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   711: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   714: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   717: goto            895
        //   720: aload_1         /* args */
        //   721: iconst_1       
        //   722: aaload         
        //   723: ldc_w           "reload"
        //   726: iconst_1       
        //   727: invokestatic    kotlin/text/StringsKt.equals:(Ljava/lang/String;Ljava/lang/String;Z)Z
        //   730: ifeq            806
        //   733: nop            
        //   734: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   737: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   740: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.reloadScripts:()V
        //   743: aload_0         /* this */
        //   744: ldc_w           "Successfully reloaded all scripts."
        //   747: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   750: goto            895
        //   753: astore_2        /* t */
        //   754: invokestatic    net/ccbluex/liquidbounce/utils/ClientUtils.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   757: ldc_w           "Something went wrong while reloading all scripts."
        //   760: aload_2         /* t */
        //   761: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   766: aload_0         /* this */
        //   767: new             Ljava/lang/StringBuilder;
        //   770: dup            
        //   771: invokespecial   java/lang/StringBuilder.<init>:()V
        //   774: aload_2         /* t */
        //   775: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   778: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   781: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   784: ldc_w           ": "
        //   787: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   790: aload_2         /* t */
        //   791: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   794: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   797: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   800: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   803: goto            895
        //   806: aload_1         /* args */
        //   807: iconst_1       
        //   808: aaload         
        //   809: ldc_w           "folder"
        //   812: iconst_1       
        //   813: invokestatic    kotlin/text/StringsKt.equals:(Ljava/lang/String;Ljava/lang/String;Z)Z
        //   816: ifeq            895
        //   819: nop            
        //   820: invokestatic    java/awt/Desktop.getDesktop:()Ljava/awt/Desktop;
        //   823: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   826: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   829: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.getScriptsFolder:()Ljava/io/File;
        //   832: invokevirtual   java/awt/Desktop.open:(Ljava/io/File;)V
        //   835: aload_0         /* this */
        //   836: ldc_w           "Successfully opened scripts folder."
        //   839: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   842: goto            895
        //   845: astore_2        /* t */
        //   846: invokestatic    net/ccbluex/liquidbounce/utils/ClientUtils.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   849: ldc_w           "Something went wrong while trying to open your scripts folder."
        //   852: aload_2         /* t */
        //   853: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   858: aload_0         /* this */
        //   859: new             Ljava/lang/StringBuilder;
        //   862: dup            
        //   863: invokespecial   java/lang/StringBuilder.<init>:()V
        //   866: aload_2         /* t */
        //   867: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   870: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   873: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   876: ldc_w           ": "
        //   879: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   882: aload_2         /* t */
        //   883: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   886: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   889: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   892: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   895: return         
        //   896: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   899: getfield        net/ccbluex/liquidbounce/LiquidBounce.scriptManager:Lnet/ccbluex/liquidbounce/script/ScriptManager;
        //   902: astore_2        /* scriptManager */
        //   903: aload_2         /* scriptManager */
        //   904: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.getScripts:()Ljava/util/ArrayList;
        //   907: checkcast       Ljava/util/Collection;
        //   910: astore_3       
        //   911: aload_3        
        //   912: invokeinterface java/util/Collection.isEmpty:()Z
        //   917: ifne            924
        //   920: iconst_1       
        //   921: goto            925
        //   924: iconst_0       
        //   925: ifeq            1072
        //   928: aload_0         /* this */
        //   929: ldc_w           "§c§lScripts"
        //   932: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //   935: aload_2         /* scriptManager */
        //   936: invokevirtual   net/ccbluex/liquidbounce/script/ScriptManager.getScripts:()Ljava/util/ArrayList;
        //   939: checkcast       Ljava/lang/Iterable;
        //   942: astore_3        /* $receiver$iv */
        //   943: iconst_0       
        //   944: istore          index$iv
        //   946: aload_3         /* $receiver$iv */
        //   947: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   952: astore          5
        //   954: aload           5
        //   956: invokeinterface java/util/Iterator.hasNext:()Z
        //   961: ifeq            1071
        //   964: aload           5
        //   966: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   971: astore          item$iv
        //   973: iload           index$iv
        //   975: iinc            index$iv, 1
        //   978: istore          7
        //   980: iload           7
        //   982: ifge            988
        //   985: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   988: iload           7
        //   990: istore          8
        //   992: iload           8
        //   994: aload           item$iv
        //   996: checkcast       Lnet/ccbluex/liquidbounce/script/Script;
        //   999: astore          9
        //  1001: istore          index
        //  1003: iconst_0       
        //  1004: istore          $i$a$-forEachIndexed-ScriptManagerCommand$execute$2
        //  1006: aload_0         /* this */
        //  1007: new             Ljava/lang/StringBuilder;
        //  1010: dup            
        //  1011: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1014: iload           index
        //  1016: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1019: ldc_w           ": §a§l"
        //  1022: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1025: aload           script
        //  1027: invokevirtual   net/ccbluex/liquidbounce/script/Script.getScriptName:()Ljava/lang/String;
        //  1030: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1033: ldc_w           " §a§lv"
        //  1036: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1039: aload           script
        //  1041: invokevirtual   net/ccbluex/liquidbounce/script/Script.getScriptVersion:()D
        //  1044: invokevirtual   java/lang/StringBuilder.append:(D)Ljava/lang/StringBuilder;
        //  1047: ldc_w           " §3by §a§l"
        //  1050: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1053: aload           script
        //  1055: invokevirtual   net/ccbluex/liquidbounce/script/Script.getScriptAuthor:()Ljava/lang/String;
        //  1058: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1061: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1064: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chat:(Ljava/lang/String;)V
        //  1067: nop            
        //  1068: goto            954
        //  1071: nop            
        //  1072: aload_0         /* this */
        //  1073: ldc_w           ".scriptmanager <import, delete, reload, folder>"
        //  1076: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/ScriptManagerCommand.chatSyntax:(Ljava/lang/String;)V
        //  1079: return         
        //    StackMapTable: 00 19 FE 00 8B 07 00 2E 07 00 44 07 00 86 FE 00 26 07 00 8A 07 00 95 07 00 92 FE 00 47 07 00 9F 07 00 86 07 00 44 FD 00 49 07 00 C2 07 00 B4 FF 00 02 00 08 07 00 02 07 00 D3 07 00 2E 07 00 44 07 00 86 07 00 8A 07 00 95 07 00 92 00 00 FD 00 0F 07 00 D5 07 00 DB 2B FF 00 41 00 05 07 00 02 07 00 D3 07 00 2E 07 00 44 07 00 86 00 00 FF 00 08 00 02 07 00 02 07 00 D3 00 01 07 00 1B 33 1B F7 00 74 07 00 1D 47 07 00 1B 34 60 07 00 1B 34 66 07 00 1B 31 00 FD 00 1B 07 00 5C 07 01 4D 40 01 FF 00 1C 00 06 07 00 02 07 00 D3 07 00 5C 07 00 D5 01 07 00 DB 00 00 FD 00 21 07 00 FC 01 F9 00 52 FF 00 00 00 04 07 00 02 07 00 D3 07 00 5C 07 00 FC 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  24     459    462    514    Ljava/lang/Throwable;
        //  527    656    659    667    Ljava/lang/NumberFormatException;
        //  527    656    667    720    Ljava/lang/Throwable;
        //  733    750    753    806    Ljava/lang/Throwable;
        //  819    842    845    895    Ljava/lang/Throwable;
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
    
    public ScriptManagerCommand() {
        super("scriptmanager", new String[] { "scripts" });
    }
}
