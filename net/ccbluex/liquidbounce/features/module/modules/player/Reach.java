// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Reach", description = "Increases your reach.", category = ModuleCategory.PLAYER)
public class Reach extends Module
{
    public final FloatValue reachValue;
    
    public Reach() {
        this.reachValue = new FloatValue("Reach", 4.5f, 3.5f, 7.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("reach", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("reach")) {
                    this.chatSyntax(".reach <reach>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        Reach.this.reachValue.setValue(value);
                        this.chat("§7Reach was set to §8" + value + "§7.");
                        Reach$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".reach reach <value>");
            }
        });
    }
}
