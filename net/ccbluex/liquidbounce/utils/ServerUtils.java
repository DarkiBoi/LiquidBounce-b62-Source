// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMultiplayer;
import net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ServerUtils
{
    public static ServerData serverData;
    
    public static void connectToLastServer() {
        if (ServerUtils.serverData == null) {
            return;
        }
        final Minecraft mc = Minecraft.func_71410_x();
        mc.func_147108_a((GuiScreen)new GuiConnecting((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), mc, ServerUtils.serverData));
    }
}
