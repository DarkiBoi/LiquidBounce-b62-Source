// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class TargetCommand extends Command
{
    public TargetCommand() {
        super("target", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("players")) {
                EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                this.chat("§7Target player toggled " + (EntityUtils.targetPlayer ? "on" : "off") + ".");
                TargetCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                return;
            }
            if (args[1].equalsIgnoreCase("mobs")) {
                EntityUtils.targetMobs = !EntityUtils.targetMobs;
                this.chat("§7Target mobs toggled " + (EntityUtils.targetMobs ? "on" : "off") + ".");
                TargetCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                return;
            }
            if (args[1].equalsIgnoreCase("animals")) {
                EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                this.chat("§7Target animals toggled " + (EntityUtils.targetAnimals ? "on" : "off") + ".");
                TargetCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                return;
            }
            if (args[1].equalsIgnoreCase("invisible")) {
                EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                this.chat("§7Target Invisible toggled " + (EntityUtils.targetInvisible ? "on" : "off") + ".");
                TargetCommand.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                return;
            }
        }
        this.chatSyntax(".target <players, mobs, animals, invisible>");
    }
}
