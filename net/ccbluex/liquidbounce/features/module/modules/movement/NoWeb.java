// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoWeb", description = "Prevents you from getting slowed down in webs.", category = ModuleCategory.MOVEMENT)
public class NoWeb extends Module
{
    private final ListValue modeValue;
    
    public NoWeb() {
        this.modeValue = new ListValue("Mode", new String[] { "None", "AAC", "LAAC", "Rewi" }, "None");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("noweb", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".noweb <mode>");
                    return;
                }
                if (args.length > 2 && NoWeb.this.modeValue.contains(args[2])) {
                    NoWeb.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7NoWeb mode was set to §8" + NoWeb.this.modeValue.asString().toUpperCase() + "§7.");
                    NoWeb$1.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".noweb mode §c<§8" + Strings.join(NoWeb.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!NoWeb.mc.field_71439_g.field_70134_J) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "none": {
                NoWeb.mc.field_71439_g.field_70134_J = false;
                break;
            }
            case "aac": {
                NoWeb.mc.field_71439_g.field_70747_aH = 0.59f;
                if (!NoWeb.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    NoWeb.mc.field_71439_g.field_70181_x = 0.0;
                    break;
                }
                break;
            }
            case "laac": {
                NoWeb.mc.field_71439_g.field_70747_aH = ((NoWeb.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f) ? 1.0f : 1.21f);
                if (!NoWeb.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    NoWeb.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (NoWeb.mc.field_71439_g.field_70122_E) {
                    NoWeb.mc.field_71439_g.func_70664_aZ();
                    break;
                }
                break;
            }
            case "rewi": {
                NoWeb.mc.field_71439_g.field_70747_aH = 0.42f;
                if (NoWeb.mc.field_71439_g.field_70122_E) {
                    NoWeb.mc.field_71439_g.func_70664_aZ();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
