// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class RenameCommand extends Command
{
    public RenameCommand() {
        super("rename", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            this.chatSyntax(".rename <displayName>");
            return;
        }
        if (RenameCommand.mc.field_71442_b.func_78762_g()) {
            this.chat("§c§lError: §3You need creative mode.");
            return;
        }
        final ItemStack item = RenameCommand.mc.field_71439_g.func_70694_bm();
        if (item == null || item.func_77973_b() == null) {
            this.chat("§c§lError: §3You need to hold a item.");
            return;
        }
        item.func_151001_c(ChatColor.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
        this.chat("§3Item renamed to '" + item.func_82833_r() + "§3'");
    }
}
