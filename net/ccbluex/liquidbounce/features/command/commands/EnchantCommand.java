// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.ccbluex.liquidbounce.features.command.Command;

public class EnchantCommand extends Command
{
    public EnchantCommand() {
        super("enchant", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 2) {
            this.chatSyntax(".enchant <type> [level]");
            return;
        }
        if (EnchantCommand.mc.field_71442_b.func_78762_g()) {
            this.chat("§c§lError: §3You need creative mode.");
            return;
        }
        final ItemStack item = EnchantCommand.mc.field_71439_g.func_70694_bm();
        if (item == null || item.func_77973_b() == null) {
            this.chat("§c§lError: §3You need to hold a item.");
            return;
        }
        try {
            int enchantID;
            try {
                enchantID = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e2) {
                final Enchantment enchantment = Enchantment.func_180305_b(args[1]);
                if (enchantment == null) {
                    this.chat("There is no such enchantment with the name " + args[1]);
                    return;
                }
                enchantID = enchantment.field_77352_x;
            }
            final Enchantment enchantment2 = Enchantment.func_180306_c(enchantID);
            if (enchantment2 == null) {
                this.chat("There is no such enchantment with ID " + enchantID);
                return;
            }
            int level;
            try {
                level = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e3) {
                this.chatSyntaxError();
                return;
            }
            item.func_77966_a(enchantment2, level);
            this.chat(enchantment2.func_77316_c(level) + " added to " + item.func_82833_r() + ".");
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
