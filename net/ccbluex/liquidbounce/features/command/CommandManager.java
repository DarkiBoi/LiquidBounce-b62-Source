// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.features.command.commands.ScriptManagerCommand;
import net.ccbluex.liquidbounce.features.command.commands.LoginCommand;
import net.ccbluex.liquidbounce.features.command.commands.ReloadCommand;
import net.ccbluex.liquidbounce.features.command.commands.EnchantCommand;
import net.ccbluex.liquidbounce.features.command.commands.RenameCommand;
import net.ccbluex.liquidbounce.features.command.commands.PingCommand;
import net.ccbluex.liquidbounce.features.command.commands.PanicCommand;
import net.ccbluex.liquidbounce.features.command.commands.HoloStandCommand;
import net.ccbluex.liquidbounce.features.command.commands.BindsCommand;
import net.ccbluex.liquidbounce.features.command.commands.TacoCommand;
import net.ccbluex.liquidbounce.features.command.commands.TargetCommand;
import net.ccbluex.liquidbounce.features.command.commands.UsernameCommand;
import net.ccbluex.liquidbounce.features.command.commands.GiveCommand;
import net.ccbluex.liquidbounce.features.command.commands.DamageCommand;
import net.ccbluex.liquidbounce.features.command.commands.ToggleCommand;
import net.ccbluex.liquidbounce.features.command.commands.ServerInfoCommand;
import net.ccbluex.liquidbounce.features.command.commands.LocalAutoSettingsCommand;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import net.ccbluex.liquidbounce.features.command.commands.FriendCommand;
import net.ccbluex.liquidbounce.features.command.commands.SayCommand;
import net.ccbluex.liquidbounce.features.command.commands.HelpCommand;
import net.ccbluex.liquidbounce.features.command.commands.HClipCommand;
import net.ccbluex.liquidbounce.features.command.commands.VClipCommand;
import net.ccbluex.liquidbounce.features.command.commands.BindCommand;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CommandManager
{
    private final List<Command> commands;
    public final char commandPrefix = '.';
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
    }
    
    public void registerCommands() {
        this.registerCommand(new BindCommand());
        this.registerCommand(new VClipCommand());
        this.registerCommand(new HClipCommand());
        this.registerCommand(new HelpCommand());
        this.registerCommand(new SayCommand());
        this.registerCommand(new FriendCommand());
        this.registerCommand(new AutoSettingsCommand());
        this.registerCommand(new LocalAutoSettingsCommand());
        this.registerCommand(new ServerInfoCommand());
        this.registerCommand(new ToggleCommand());
        this.registerCommand(new DamageCommand());
        this.registerCommand(new GiveCommand());
        this.registerCommand(new UsernameCommand());
        this.registerCommand(new TargetCommand());
        this.registerCommand(new TacoCommand());
        this.registerCommand(new BindsCommand());
        this.registerCommand(new HoloStandCommand());
        this.registerCommand(new PanicCommand());
        this.registerCommand(new PingCommand());
        this.registerCommand(new RenameCommand());
        this.registerCommand(new EnchantCommand());
        this.registerCommand(new ReloadCommand());
        this.registerCommand(new LoginCommand());
        this.registerCommand(new ScriptManagerCommand());
    }
    
    public void executeCommands(final String inputLine) {
        for (final Command command : this.commands) {
            final String[] args = inputLine.split(" ");
            if (args[0].equalsIgnoreCase('.' + command.getCommand())) {
                command.execute(args);
                return;
            }
            if (command.getAlias() == null) {
                continue;
            }
            for (final String alias : command.getAlias()) {
                if (args[0].equalsIgnoreCase('.' + alias)) {
                    command.execute(args);
                    return;
                }
            }
        }
        ChatUtils.displayChatMessage("§cCommand not found. Type .help to view all commands.");
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public void registerCommand(final Command command) {
        this.commands.add(command);
    }
    
    public void unregisterCommand(final Command command) {
        this.commands.remove(command);
    }
}
