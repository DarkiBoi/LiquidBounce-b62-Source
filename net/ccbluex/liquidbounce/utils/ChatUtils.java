// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;

public final class ChatUtils
{
    public static void displayChatMessage(final String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a(IChatComponent.Serializer.func_150699_a("{text:\"" + message + "\"}"));
    }
}
