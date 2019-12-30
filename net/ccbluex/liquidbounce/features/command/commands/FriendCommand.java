// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Iterator;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "friends" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            final FriendsConfig friendsConfig = LiquidBounce.CLIENT.fileManager.friendsConfig;
            if (args[1].equalsIgnoreCase("add")) {
                if (args.length <= 2) {
                    this.chatSyntax(".friend add <name> [alias]");
                    return;
                }
                final String name = args[2];
                if (name.isEmpty()) {
                    this.chat("The name is empty.");
                    return;
                }
                Label_0150: {
                    if (args.length > 3) {
                        if (!friendsConfig.addFriend(name, args[3])) {
                            break Label_0150;
                        }
                    }
                    else if (!friendsConfig.addFriend(name)) {
                        break Label_0150;
                    }
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.friendsConfig);
                    this.chat("§a§l" + name + "§3 was added to your friend list.");
                    FriendCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chat("The name is already in the list.");
                return;
            }
            else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length > 2) {
                    final String name = args[2];
                    if (friendsConfig.removeFriend(name)) {
                        LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.friendsConfig);
                        this.chat("§a§l" + name + "§3 was removed from your friend list.");
                        FriendCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    else {
                        this.chat("This name is not in the list.");
                    }
                    return;
                }
                this.chatSyntax(".friend remove <name>");
                return;
            }
            else {
                if (args[1].equalsIgnoreCase("clear")) {
                    final int friends = LiquidBounce.CLIENT.fileManager.friendsConfig.getFriends().size();
                    LiquidBounce.CLIENT.fileManager.friendsConfig.clearFriends();
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.friendsConfig);
                    this.chat("Removed " + friends + " friend(s).");
                    return;
                }
                if (args[1].equalsIgnoreCase("list")) {
                    this.chat("Your Friends:");
                    for (final FriendsConfig.Friend friend : friendsConfig.getFriends()) {
                        this.chat("§7> §a§l" + friend.getPlayerName() + " §c(§7§l" + friend.getAlias() + "§c)");
                    }
                    this.chat("You have §c" + friendsConfig.getFriends().size() + "§3 friends.");
                    return;
                }
            }
        }
        this.chatSyntax(".friend <add, remove, list>");
    }
}
