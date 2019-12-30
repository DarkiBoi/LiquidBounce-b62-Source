// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.ui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[] { "configreload" });
    }
    
    @Override
    public void execute(final String[] args) {
        this.chat("Reloading...");
        this.chat("§c§lLoading modules...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.modulesConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lLoading values...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lLoading accounts...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lLoading friends...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.friendsConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lLoading xray...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.xrayConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lReloading scripts...");
        LiquidBounce.CLIENT.scriptManager.getScripts().forEach(Script::reloadScript);
        this.chat("§c§lReloaded.");
        this.chat("§c§lSorting modules...");
        LiquidBounce.CLIENT.moduleManager.sortModules();
        this.chat("§c§lSorted.");
        this.chat("§c§lLoading hud...");
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
        this.chat("§c§lLoaded.");
        this.chat("§c§lReloading ClickGUI...");
        LiquidBounce.CLIENT.clickGui = new ClickGui();
        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
        this.chat("§c§lReloaded.");
        this.chat("Reloaded.");
    }
}
