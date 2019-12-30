// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoSwing", description = "Disabled swing effect when hitting an entity/mining a block.", category = ModuleCategory.RENDER)
public class NoSwing extends Module
{
    public final BoolValue serverSideValue;
    
    public NoSwing() {
        this.serverSideValue = new BoolValue("ServerSide", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("noswing", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("ServerSide")) {
                    NoSwing.this.serverSideValue.setValue(!NoSwing.this.serverSideValue.asBoolean());
                    this.chat("§7NoSwing ServerSide was toggled §8" + (NoSwing.this.serverSideValue.asBoolean() ? "on" : "off") + "§7.");
                    NoSwing$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                NoSwing$1.mc.field_71439_g.func_71038_i();
                this.chatSyntax(".noswing <ServerSide>");
            }
        });
    }
}
