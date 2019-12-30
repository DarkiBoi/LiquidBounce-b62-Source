// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "MidClick", description = "Allows you to add a player as a friend by right clicking him.", category = ModuleCategory.MISC)
public class MidClick extends Module
{
    private boolean wasDown;
    
    @EventTarget
    public void onRender(final Render2DEvent event) {
        if (MidClick.mc.field_71462_r != null) {
            return;
        }
        if (!this.wasDown && Mouse.isButtonDown(2)) {
            final Entity entity = MidClick.mc.field_71476_x.field_72308_g;
            if (entity instanceof EntityPlayer) {
                final String playerName = ChatColor.stripColor(entity.func_70005_c_());
                final FriendsConfig friendsConfig = LiquidBounce.CLIENT.fileManager.friendsConfig;
                if (!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName);
                    LiquidBounce.CLIENT.fileManager.saveConfig(friendsConfig);
                    ChatUtils.displayChatMessage("§a§l" + playerName + "§c was added to your friends.");
                }
                else {
                    friendsConfig.removeFriend(playerName);
                    LiquidBounce.CLIENT.fileManager.saveConfig(friendsConfig);
                    ChatUtils.displayChatMessage("§a§l" + playerName + "§c was removed from your friends.");
                }
            }
            else {
                ChatUtils.displayChatMessage("§c§lError: §aYou need to select a player.");
            }
        }
        this.wasDown = Mouse.isButtonDown(2);
    }
}
