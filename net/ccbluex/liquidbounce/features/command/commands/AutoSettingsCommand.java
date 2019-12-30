// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import java.util.List;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.features.command.Command;

public class AutoSettingsCommand extends Command
{
    public AutoSettingsCommand() {
        super("autosettings", new String[] { "setting", "settings", "config" });
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
        //     3: if_icmple       28
        //     6: new             Ljava/lang/Thread;
        //     9: dup            
        //    10: aload_0         /* this */
        //    11: aload_1         /* args */
        //    12: invokedynamic   BootstrapMethod #0, run:(Lnet/ccbluex/liquidbounce/features/command/commands/AutoSettingsCommand;[Ljava/lang/String;)Ljava/lang/Runnable;
        //    17: ldc             "AutoSettings-Thread"
        //    19: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;Ljava/lang/String;)V
        //    22: astore_2        /* thread */
        //    23: aload_2         /* thread */
        //    24: invokevirtual   java/lang/Thread.start:()V
        //    27: return         
        //    28: aload_0         /* this */
        //    29: ldc             ".autosettings <server/url>"
        //    31: invokevirtual   net/ccbluex/liquidbounce/features/command/commands/AutoSettingsCommand.chatSyntax:(Ljava/lang/String;)V
        //    34: return         
        //    StackMapTable: 00 01 1C
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
