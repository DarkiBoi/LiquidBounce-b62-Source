// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.module.Module;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.ui.hud.element.elements.notifications.Notification;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.command.Command;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 2) {
            this.chatSyntax(new String[] { "<module> <key>", "<module> none" });
            return;
        }
        final Module module = ModuleManager.getModule(args[1]);
        if (module == null) {
            this.chat("Module §a§l" + args[1] + "§3 not found.");
            return;
        }
        final int key = Keyboard.getKeyIndex(args[2].toUpperCase());
        module.setKeyBind(key);
        this.chat("Bound module §a§l" + module.getName() + "§3 to key §a§l" + Keyboard.getKeyName(key) + "§3.");
        LiquidBounce.CLIENT.hud.addNotification(new Notification("Bound " + module.getName() + " to " + Keyboard.getKeyName(key)));
        BindCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
    }
}
