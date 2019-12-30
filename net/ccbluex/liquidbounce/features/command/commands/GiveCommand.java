// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class GiveCommand extends Command
{
    public GiveCommand() {
        super("give", new String[] { "item", "i", "get" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (GiveCommand.mc.field_71442_b.func_78762_g()) {
            this.chat("Creative mode only.");
            return;
        }
        if (args.length <= 1) {
            this.chatSyntax(".give <item> [amount] [data] [datatag]");
            return;
        }
        final ItemStack itemStack = ItemUtils.createItem(StringUtils.toCompleteString(args, 1));
        if (itemStack == null) {
            this.chatSyntaxError();
            return;
        }
        int emptySlot = -1;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = GiveCommand.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null) {
                emptySlot = i;
            }
        }
        if (emptySlot != -1) {
            GiveCommand.mc.func_147114_u().func_147297_a((Packet)new C10PacketCreativeInventoryAction(emptySlot, itemStack));
            this.chat("§7Given [§8" + itemStack.func_82833_r() + "§7] * §8" + itemStack.field_77994_a + "§7 to §8" + GiveCommand.mc.func_110432_I().func_111285_a() + "§7.");
        }
        else {
            this.chat("Your inventory is full.");
        }
    }
}
