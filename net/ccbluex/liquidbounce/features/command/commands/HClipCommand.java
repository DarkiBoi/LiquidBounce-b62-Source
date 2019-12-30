// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class HClipCommand extends Command
{
    public HClipCommand() {
        super("hclip", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            try {
                MovementUtils.forward(Double.parseDouble(args[1]));
                this.chat("You were teleported.");
            }
            catch (NumberFormatException exception) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax(".hclip <value>");
    }
}
