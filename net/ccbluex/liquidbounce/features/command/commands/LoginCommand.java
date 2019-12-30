// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.features.command.Command;

public class LoginCommand extends Command
{
    public LoginCommand() {
        super("login", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            this.chatSyntax(".login <username/email> [password]");
            return;
        }
        String result;
        if (args.length > 2) {
            result = GuiAltManager.login(new MinecraftAccount(args[1], args[2]));
        }
        else {
            result = GuiAltManager.login(new MinecraftAccount(args[1]));
        }
        this.chat(result);
        if (result.startsWith("§cYour name is now")) {
            if (LoginCommand.mc.func_71387_A()) {
                return;
            }
            LoginCommand.mc.field_71441_e.func_72882_A();
            ServerUtils.connectToLastServer();
        }
    }
}
