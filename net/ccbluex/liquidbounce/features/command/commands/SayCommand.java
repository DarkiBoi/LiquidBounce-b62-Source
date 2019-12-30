// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            SayCommand.mc.field_71439_g.func_71165_d(StringUtils.toCompleteString(args, 1));
            this.chat("Message was send to the chat.");
            return;
        }
        this.chatSyntax(".say <message...>");
    }
}
