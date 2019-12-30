// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "HitBox", description = "Makes hitboxes of targets bigger.", category = ModuleCategory.COMBAT)
public class HitBox extends Module
{
    public final FloatValue sizeValue;
    
    public HitBox() {
        this.sizeValue = new FloatValue("Size", 0.4f, 0.0f, 1.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("hitbox", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("size")) {
                    this.chatSyntax(".hitbox <size>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        HitBox.this.sizeValue.setValue(value);
                        this.chat("§7Hitbox size set to §8" + value + "§7.");
                        HitBox$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".hitbox size <value>");
            }
        });
    }
}
