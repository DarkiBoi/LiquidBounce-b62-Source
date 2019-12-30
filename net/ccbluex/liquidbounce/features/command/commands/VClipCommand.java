// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;

public class VClipCommand extends Command
{
    public VClipCommand() {
        super("vclip", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            try {
                final double y = Double.parseDouble(args[1]);
                if (VClipCommand.mc.field_71439_g.field_70154_o == null) {
                    VClipCommand.mc.field_71439_g.func_70107_b(VClipCommand.mc.field_71439_g.field_70165_t, VClipCommand.mc.field_71439_g.field_70163_u + y, VClipCommand.mc.field_71439_g.field_70161_v);
                }
                else {
                    VClipCommand.mc.field_71439_g.field_70154_o.func_70107_b(VClipCommand.mc.field_71439_g.field_70154_o.field_70165_t, VClipCommand.mc.field_71439_g.field_70163_u + y, VClipCommand.mc.field_71439_g.field_70154_o.field_70161_v);
                }
                this.chat("You were teleported.");
            }
            catch (NumberFormatException exception) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax(".vclip <id>");
    }
}
