// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.command.Command;

public class DamageCommand extends Command
{
    public DamageCommand() {
        super("damage", null);
    }
    
    @Override
    public void execute(final String[] args) {
        int damage = 1;
        if (args.length > 1) {
            try {
                damage = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex) {}
        }
        final double x = DamageCommand.mc.field_71439_g.field_70165_t;
        final double y = DamageCommand.mc.field_71439_g.field_70163_u;
        final double z = DamageCommand.mc.field_71439_g.field_70161_v;
        for (int i = 0; i < 65 * damage; ++i) {
            DamageCommand.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
            DamageCommand.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        }
        DamageCommand.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.chat("You got damage.");
    }
}
