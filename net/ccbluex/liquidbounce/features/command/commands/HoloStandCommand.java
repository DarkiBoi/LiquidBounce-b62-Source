// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class HoloStandCommand extends Command
{
    public HoloStandCommand() {
        super("holostand", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 4) {
            this.chatSyntax(".holostand <x> <y> <z> <message...>");
            return;
        }
        if (!HoloStandCommand.mc.field_71439_g.field_71075_bZ.field_75098_d) {
            this.chat("You need creative mode.");
            return;
        }
        try {
            final double x = Double.parseDouble(args[1]);
            final double y = Double.parseDouble(args[2]);
            final double z = Double.parseDouble(args[3]);
            final String message = StringUtils.toCompleteString(args, 4);
            final ItemStack itemStack = new ItemStack((Item)Items.field_179565_cj);
            final NBTTagCompound base = new NBTTagCompound();
            final NBTTagCompound entityTag = new NBTTagCompound();
            entityTag.func_74768_a("Invisible", 1);
            entityTag.func_74778_a("CustomName", message);
            entityTag.func_74768_a("CustomNameVisible", 1);
            entityTag.func_74768_a("NoGravity", 1);
            final NBTTagList position = new NBTTagList();
            position.func_74742_a((NBTBase)new NBTTagDouble(x));
            position.func_74742_a((NBTBase)new NBTTagDouble(y));
            position.func_74742_a((NBTBase)new NBTTagDouble(z));
            entityTag.func_74782_a("Pos", (NBTBase)position);
            base.func_74782_a("EntityTag", (NBTBase)entityTag);
            itemStack.func_77982_d(base);
            itemStack.func_151001_c("§c§lHolo§eStand");
            Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)new C10PacketCreativeInventoryAction(36, itemStack));
            this.chat("You got the HoloStand.");
        }
        catch (NumberFormatException exception) {
            this.chatSyntaxError();
        }
    }
}
