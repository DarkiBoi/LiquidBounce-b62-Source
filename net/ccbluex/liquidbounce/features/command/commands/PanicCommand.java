// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.command.Command;

public class PanicCommand extends Command
{
    public PanicCommand() {
        super("panic", null);
    }
    
    @Override
    public void execute(final String[] args) {
        ModuleManager.getModules().stream().filter(module -> module.getState() && module.getCategory() != ModuleCategory.RENDER).forEach(module -> module.setState(false));
        this.chat("All modules was disabled.");
    }
}
