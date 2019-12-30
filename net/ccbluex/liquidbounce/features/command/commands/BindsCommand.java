// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.command.Command;

public class BindsCommand extends Command
{
    public BindsCommand() {
        super("binds", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            final String lowerCase = args[1].toLowerCase();
            switch (lowerCase) {
                case "clear": {
                    ModuleManager.getModules().forEach(module -> module.setKeyBind(0));
                    this.chat("Removed all binds.");
                    return;
                }
            }
        }
        this.chat("§c§lBinds");
        ModuleManager.getModules().stream().filter(module -> module.getKeyBind() != 0).forEach(module -> ChatUtils.displayChatMessage("§6> §c" + module.getName() + ": §a§l" + Keyboard.getKeyName(module.getKeyBind())));
        this.chatSyntax(".binds [clear]");
    }
}
