// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.script.remapper.injection.transformers;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.NodeUtils;
import org.objectweb.asm.tree.MethodNode;
import net.minecraft.launchwrapper.IClassTransformer;

public class AbstractJavaLinkerTransformer implements IClassTransformer
{
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "jdk.internal.dynalink.beans.AbstractJavaLinker"
        //     3: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //     6: ifeq            49
        //     9: getstatic       net/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils.INSTANCE:Lnet/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils;
        //    12: aload_3         /* basicClass */
        //    13: invokevirtual   net/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils.toClassNode:([B)Lorg/objectweb/asm/tree/ClassNode;
        //    16: astore          classNode
        //    18: aload           classNode
        //    20: getfield        org/objectweb/asm/tree/ClassNode.methods:Ljava/util/List;
        //    23: invokedynamic   BootstrapMethod #0, accept:()Ljava/util/function/Consumer;
        //    28: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
        //    33: getstatic       net/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils.INSTANCE:Lnet/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils;
        //    36: aload           classNode
        //    38: invokevirtual   net/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils.toBytes:(Lorg/objectweb/asm/tree/ClassNode;)[B
        //    41: areturn        
        //    42: astore          throwable
        //    44: aload           throwable
        //    46: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //    49: aload_3         /* basicClass */
        //    50: areturn        
        //    StackMapTable: 00 02 6A 07 00 16 06
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  9      41     42     49     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
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
