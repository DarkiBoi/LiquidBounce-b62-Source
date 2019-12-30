// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;

public class PingCommand extends Command
{
    public PingCommand() {
        super("ping", null);
    }
    
    @Override
    public void execute(final String[] args) {
        this.chat("§3Your ping is §a" + PingCommand.mc.func_147114_u().func_175102_a(PingCommand.mc.field_71439_g.func_110124_au()).func_178853_c() + "§3ms.");
    }
}
