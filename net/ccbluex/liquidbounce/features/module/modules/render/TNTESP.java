// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.event.EventTarget;
import org.jetbrains.annotations.NotNull;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TNTESP", description = "Allows you to see ignited TNT blocks through walls.", category = ModuleCategory.RENDER)
@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007" }, d2 = { "Lnet/ccbluex/liquidbounce/features/module/modules/render/TNTESP;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/events/Render3DEvent;", "LiquidBounce" })
public final class TNTESP extends Module
{
    @EventTarget
    public final void onRender3D(@NotNull final Render3DEvent event) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "event"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: invokestatic    net/ccbluex/liquidbounce/features/module/modules/render/TNTESP.access$getMc$p$s-1984916852:()Lnet/minecraft/client/Minecraft;
        //     9: getfield        net/minecraft/client/Minecraft.field_71441_e:Lnet/minecraft/client/multiplayer/WorldClient;
        //    12: getfield        net/minecraft/client/multiplayer/WorldClient.field_72996_f:Ljava/util/List;
        //    15: dup            
        //    16: ldc             "mc.theWorld.loadedEntityList"
        //    18: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    21: checkcast       Ljava/lang/Iterable;
        //    24: astore_2        /* $receiver$iv */
        //    25: aload_2         /* $receiver$iv */
        //    26: astore_3       
        //    27: new             Ljava/util/ArrayList;
        //    30: dup            
        //    31: invokespecial   java/util/ArrayList.<init>:()V
        //    34: checkcast       Ljava/util/Collection;
        //    37: astore          destination$iv$iv
        //    39: aload_3         /* $receiver$iv$iv */
        //    40: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    45: astore          5
        //    47: aload           5
        //    49: invokeinterface java/util/Iterator.hasNext:()Z
        //    54: ifeq            97
        //    57: aload           5
        //    59: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    64: astore          element$iv$iv
        //    66: aload           element$iv$iv
        //    68: checkcast       Lnet/minecraft/entity/Entity;
        //    71: astore          it
        //    73: iconst_0       
        //    74: istore          $i$a$-filter-TNTESP$onRender3D$1
        //    76: aload           it
        //    78: instanceof      Lnet/minecraft/entity/item/EntityTNTPrimed;
        //    81: ifeq            47
        //    84: aload           destination$iv$iv
        //    86: aload           element$iv$iv
        //    88: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    93: pop            
        //    94: goto            47
        //    97: aload           destination$iv$iv
        //    99: checkcast       Ljava/util/List;
        //   102: checkcast       Ljava/lang/Iterable;
        //   105: astore_2       
        //   106: nop            
        //   107: aload_2         /* $receiver$iv */
        //   108: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   113: astore_3       
        //   114: aload_3        
        //   115: invokeinterface java/util/Iterator.hasNext:()Z
        //   120: ifeq            154
        //   123: aload_3        
        //   124: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   129: astore          element$iv
        //   131: aload           element$iv
        //   133: checkcast       Lnet/minecraft/entity/Entity;
        //   136: astore          it
        //   138: iconst_0       
        //   139: istore          $i$a$-forEach-TNTESP$onRender3D$2
        //   141: aload           it
        //   143: getstatic       java/awt/Color.RED:Ljava/awt/Color;
        //   146: iconst_0       
        //   147: invokestatic    net/ccbluex/liquidbounce/utils/render/RenderUtils.drawEntityBox:(Lnet/minecraft/entity/Entity;Ljava/awt/Color;Z)V
        //   150: nop            
        //   151: goto            114
        //   154: nop            
        //   155: return         
        //    StackMapTable: 00 04 FF 00 2F 00 06 07 00 02 07 00 4E 07 00 41 07 00 41 07 00 48 07 00 50 00 00 31 FF 00 10 00 06 07 00 02 07 00 4E 07 00 41 07 00 50 07 00 64 07 00 64 00 00 27
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
