// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import java.util.List;
import java.io.IOException;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import net.ccbluex.liquidbounce.features.command.Command;

public class LocalAutoSettingsCommand extends Command
{
    public LocalAutoSettingsCommand() {
        super("localautosettings", new String[] { "localsetting", "localsettings", "localconfig" });
    }
    
    @Override
    public void execute(final String[] args) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: arraylength    
        //     2: iconst_1       
        //     3: if_icmple       443
        //     6: aload_1         /* args */
        //     7: iconst_1       
        //     8: aaload         
        //     9: ldc             "load"
        //    11: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //    14: ifeq            86
        //    17: aload_1         /* args */
        //    18: arraylength    
        //    19: iconst_2       
        //    20: if_icmple       79
        //    23: new             Ljava/io/File;
        //    26: dup            
        //    27: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //    30: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //    33: getfield        net/ccbluex/liquidbounce/file/FileManager.settingsDir:Ljava/io/File;
        //    36: aload_1         /* args */
        //    37: iconst_2       
        //    38: aaload         
        //    39: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    42: astore_2        /* scriptFile */
        //    43: aload_2         /* scriptFile */
        //    44: invokevirtual   java/io/File.exists:()Z
        //    47: ifeq            72
        //    50: new             Ljava/lang/Thread;
        //    53: dup            
        //    54: aload_0         /* this */
        //    55: aload_2         /* scriptFile */
        //    56: invokedynamic   BootstrapMethod #0, run:(Lnet/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand;Ljava/io/File;)Ljava/lang/Runnable;
        //    61: ldc             "AutoSettings-Thread"
        //    63: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;Ljava/lang/String;)V
        //    66: astore_3        /* thread */
        //    67: aload_3         /* thread */
        //    68: invokevirtual   java/lang/Thread.start:()V
        //    71: return         
        //    72: aload_0         /* this */
        //    73: ldc             "§cThe Script don't exist!"
        //    75: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //    78: return         
        //    79: aload_0         /* this */
        //    80: ldc             ".localautosettings load <name>"
        //    82: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chatSyntax:(Ljava/lang/String;)V
        //    85: return         
        //    86: aload_1         /* args */
        //    87: iconst_1       
        //    88: aaload         
        //    89: ldc             "save"
        //    91: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //    94: ifeq            361
        //    97: aload_1         /* args */
        //    98: arraylength    
        //    99: iconst_2       
        //   100: if_icmple       354
        //   103: new             Ljava/io/File;
        //   106: dup            
        //   107: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   110: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   113: getfield        net/ccbluex/liquidbounce/file/FileManager.settingsDir:Ljava/io/File;
        //   116: aload_1         /* args */
        //   117: iconst_2       
        //   118: aaload         
        //   119: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   122: astore_2        /* scriptFile */
        //   123: aload_2         /* scriptFile */
        //   124: invokevirtual   java/io/File.exists:()Z
        //   127: ifeq            135
        //   130: aload_2         /* scriptFile */
        //   131: invokevirtual   java/io/File.delete:()Z
        //   134: pop            
        //   135: aload_2         /* scriptFile */
        //   136: invokevirtual   java/io/File.createNewFile:()Z
        //   139: pop            
        //   140: aload_1         /* args */
        //   141: arraylength    
        //   142: iconst_3       
        //   143: if_icmple       157
        //   146: aload_1         /* args */
        //   147: iconst_3       
        //   148: invokestatic    net/ccbluex/liquidbounce/utils/misc/StringUtils.toCompleteString:([Ljava/lang/String;I)Ljava/lang/String;
        //   151: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   154: goto            159
        //   157: ldc             "values"
        //   159: astore_3        /* option */
        //   160: aload_3         /* option */
        //   161: ldc             "all"
        //   163: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   166: ifne            178
        //   169: aload_3         /* option */
        //   170: ldc             "values"
        //   172: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   175: ifeq            182
        //   178: iconst_1       
        //   179: goto            183
        //   182: iconst_0       
        //   183: istore          values
        //   185: aload_3         /* option */
        //   186: ldc             "all"
        //   188: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   191: ifne            203
        //   194: aload_3         /* option */
        //   195: ldc             "binds"
        //   197: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   200: ifeq            207
        //   203: iconst_1       
        //   204: goto            208
        //   207: iconst_0       
        //   208: istore          binds
        //   210: aload_3         /* option */
        //   211: ldc             "all"
        //   213: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   216: ifne            228
        //   219: aload_3         /* option */
        //   220: ldc             "states"
        //   222: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   225: ifeq            232
        //   228: iconst_1       
        //   229: goto            233
        //   232: iconst_0       
        //   233: istore          states
        //   235: iload           values
        //   237: ifne            255
        //   240: iload           binds
        //   242: ifne            255
        //   245: iload           states
        //   247: ifne            255
        //   250: aload_0         /* this */
        //   251: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chatSyntaxError:()V
        //   254: return         
        //   255: aload_0         /* this */
        //   256: ldc             "§9Creating settings..."
        //   258: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   261: iload           values
        //   263: iload           binds
        //   265: iload           states
        //   267: invokestatic    net/ccbluex/liquidbounce/utils/SettingsUtils.generateScript:(ZZZ)Ljava/lang/String;
        //   270: astore          settingsScript
        //   272: aload_0         /* this */
        //   273: ldc             "§9Saving settings..."
        //   275: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   278: new             Ljava/io/FileWriter;
        //   281: dup            
        //   282: aload_2         /* scriptFile */
        //   283: invokespecial   java/io/FileWriter.<init>:(Ljava/io/File;)V
        //   286: astore          fileWriter
        //   288: aload           fileWriter
        //   290: aload           settingsScript
        //   292: invokevirtual   java/io/FileWriter.append:(Ljava/lang/CharSequence;)Ljava/io/Writer;
        //   295: pop            
        //   296: aload           fileWriter
        //   298: invokevirtual   java/io/FileWriter.flush:()V
        //   301: aload           fileWriter
        //   303: invokevirtual   java/io/FileWriter.close:()V
        //   306: aload_0         /* this */
        //   307: ldc             "§6Settings saved successfully."
        //   309: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   312: goto            353
        //   315: astore_3        /* throwable */
        //   316: aload_0         /* this */
        //   317: new             Ljava/lang/StringBuilder;
        //   320: dup            
        //   321: invokespecial   java/lang/StringBuilder.<init>:()V
        //   324: ldc             "§cFailed to create local config: §3"
        //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   329: aload_3         /* throwable */
        //   330: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   333: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   336: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   339: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   342: invokestatic    net/ccbluex/liquidbounce/utils/ClientUtils.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   345: ldc             "Failed to create local config."
        //   347: aload_3         /* throwable */
        //   348: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   353: return         
        //   354: aload_0         /* this */
        //   355: ldc             ".localautosettings save <name> [all/values/binds/states]..."
        //   357: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chatSyntax:(Ljava/lang/String;)V
        //   360: return         
        //   361: aload_1         /* args */
        //   362: iconst_1       
        //   363: aaload         
        //   364: ldc             "list"
        //   366: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   369: ifeq            443
        //   372: aload_0         /* this */
        //   373: ldc             "§cSettings:"
        //   375: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   378: getstatic       net/ccbluex/liquidbounce/LiquidBounce.CLIENT:Lnet/ccbluex/liquidbounce/LiquidBounce;
        //   381: getfield        net/ccbluex/liquidbounce/LiquidBounce.fileManager:Lnet/ccbluex/liquidbounce/file/FileManager;
        //   384: getfield        net/ccbluex/liquidbounce/file/FileManager.settingsDir:Ljava/io/File;
        //   387: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //   390: astore_2       
        //   391: aload_2        
        //   392: arraylength    
        //   393: istore_3       
        //   394: iconst_0       
        //   395: istore          4
        //   397: iload           4
        //   399: iload_3        
        //   400: if_icmpge       442
        //   403: aload_2        
        //   404: iload           4
        //   406: aaload         
        //   407: astore          file
        //   409: aload_0         /* this */
        //   410: new             Ljava/lang/StringBuilder;
        //   413: dup            
        //   414: invokespecial   java/lang/StringBuilder.<init>:()V
        //   417: ldc             "> "
        //   419: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   422: aload           file
        //   424: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   427: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   430: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   433: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chat:(Ljava/lang/String;)V
        //   436: iinc            4, 1
        //   439: goto            397
        //   442: return         
        //   443: aload_0         /* this */
        //   444: ldc             ".localautosettings <load/save/list>"
        //   446: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/LocalAutoSettingsCommand.chatSyntax:(Ljava/lang/String;)V
        //   449: return         
        //    StackMapTable: 00 17 FC 00 48 07 00 27 FA 00 06 06 FC 00 30 07 00 27 15 41 07 00 10 FC 00 12 07 00 10 03 40 01 FC 00 13 01 03 40 01 FC 00 13 01 03 40 01 FC 00 15 01 FF 00 3B 00 03 07 00 02 07 00 A1 07 00 27 00 01 07 00 1F 25 FA 00 00 06 FE 00 23 07 00 CA 01 01 F8 00 2C 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  123    254    315    353    Ljava/lang/Throwable;
        //  255    312    315    353    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
