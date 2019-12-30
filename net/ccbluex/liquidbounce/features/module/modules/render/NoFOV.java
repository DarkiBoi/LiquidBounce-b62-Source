// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoFOV", description = "Disables FOV changes caused by speed effect, etc.", category = ModuleCategory.RENDER)
public class NoFOV extends Module
{
    public final FloatValue fovValue;
    
    public NoFOV() {
        this.fovValue = new FloatValue("FOV", 1.0f, 0.0f, 1.5f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("nofov", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("fov")) {
                    this.chatSyntax(".nofov <fov>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float fov = Float.parseFloat(args[2]);
                        NoFOV.this.fovValue.setValue(fov);
                        this.chat("§7NoFOV fov was set to §8" + fov + "§7.");
                        NoFOV$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".nofov fov <value>");
            }
        });
    }
}
