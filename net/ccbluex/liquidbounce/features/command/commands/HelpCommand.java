// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import joptsimple.internal.Strings;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help", null);
    }
    
    @Override
    public void execute(final String[] args) {
        int page = 1;
        final double maxPageDouble = LiquidBounce.CLIENT.commandManager.getCommands().size() / 8.0;
        final int maxPage = (maxPageDouble > (int)maxPageDouble) ? ((int)maxPageDouble + 1) : ((int)maxPageDouble);
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                this.chatSyntaxError();
            }
        }
        if (page <= 0) {
            this.chat("The number you have entered is too low, it must be over 0");
            return;
        }
        if (page > maxPage) {
            this.chat("The number you have entered is too big, it must be under " + maxPage + ".");
            return;
        }
        this.chat("§c§lHelp");
        ChatUtils.displayChatMessage("§7> Page: §8" + page + " / " + maxPage);
        for (int i = 8 * (page - 1); i < 8 * page && i < LiquidBounce.CLIENT.commandManager.getCommands().size(); ++i) {
            final Command command = LiquidBounce.CLIENT.commandManager.getCommands().get(i);
            ChatUtils.displayChatMessage("§6> §7." + command.getCommand() + ((command.getAlias() == null) ? "" : (" §7(§8" + Strings.join(command.getAlias(), "§7, §8") + "§7)")));
        }
        ChatUtils.displayChatMessage("§a------------\n§7> §c.help §8<§7§lpage§8>");
    }
}
