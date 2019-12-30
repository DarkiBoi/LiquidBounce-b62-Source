// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.transformers;

import net.ccbluex.liquidbounce.features.special.AntiForge;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.NodeUtils;
import org.objectweb.asm.tree.LabelNode;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.ClassUtils;
import net.minecraft.launchwrapper.IClassTransformer;

public class ForgeNetworkDispatcherTransformer implements IClassTransformer
{
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (name.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);
                final MethodNode methodNode;
                LabelNode labelNode;
                classNode.methods.forEach(o -> {
                    methodNode = o;
                    if (methodNode.name.equals("handleVanilla")) {
                        labelNode = new LabelNode();
                        methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes((AbstractInsnNode)new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkDispatcherTransformer", "returnMethod", "()Z", false), (AbstractInsnNode)new JumpInsnNode(153, labelNode), (AbstractInsnNode)new InsnNode(3), (AbstractInsnNode)new InsnNode(172), (AbstractInsnNode)labelNode));
                    }
                    return;
                });
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return basicClass;
    }
    
    public static boolean returnMethod() {
        return AntiForge.enabled && AntiForge.blockFML;
    }
}
